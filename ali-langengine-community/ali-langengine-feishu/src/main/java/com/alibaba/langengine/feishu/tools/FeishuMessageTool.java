package com.alibaba.langengine.feishu.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.langengine.core.tool.StructuredTool;
import com.alibaba.langengine.core.tool.ToolExecuteResult;
import com.alibaba.langengine.feishu.FeishuConfiguration;
import com.alibaba.langengine.feishu.sdk.FeishuClient;
import com.alibaba.langengine.feishu.sdk.FeishuConstant;
import com.alibaba.langengine.feishu.sdk.FeishuException;
import com.alibaba.langengine.feishu.tools.schema.FeishuMessageSchema;
import com.alibaba.langengine.feishu.util.FeishuConfigLoader;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


@Data
@EqualsAndHashCode(callSuper = true)
public class FeishuMessageTool extends StructuredTool<FeishuMessageSchema> {

    private static final Logger logger = LoggerFactory.getLogger(FeishuMessageTool.class);

    private FeishuClient feishuClient;
    private FeishuConfiguration configuration;

    /**
     * 默认构造函数
     */
    public FeishuMessageTool() {
        setName("feishu_send_message");
        setDescription("发送消息到飞书。支持发送文本消息、富文本消息、卡片消息等多种类型的消息到指定的用户或群聊。");
        setStructuredSchema(new FeishuMessageSchema());

        // 不在构造函数中初始化客户端，等待配置设置后再初始化
    }

    /**
     * 带配置的构造函数
     * 
     * @param configuration 飞书配置
     */
    public FeishuMessageTool(FeishuConfiguration configuration) {
        this();
        this.configuration = configuration;
        this.feishuClient = new FeishuClient(configuration);
    }

    /**
     * 执行消息发送
     * 
     * @param toolInput 工具输入参数
     * @return 执行结果
     */
    @Override
    public ToolExecuteResult execute(String toolInput) {
        try {
            logger.debug("FeishuMessageTool input: {}", toolInput);

            // 解析输入参数
            Map<String, Object> inputMap = JSON.parseObject(toolInput, Map.class);

            // 验证必需参数
            String receiveId = (String) inputMap.get("receive_id");
            String receiveIdType = (String) inputMap.get("receive_id_type");
            String msgType = (String) inputMap.get("msg_type");
            Object content = inputMap.get("content");

            if (receiveId == null || receiveId.trim().isEmpty()) {
                return new ToolExecuteResult("错误：receive_id 参数不能为空", true);
            }

            if (receiveIdType == null || receiveIdType.trim().isEmpty()) {
                receiveIdType = FeishuConstant.RECEIVE_ID_TYPE_OPEN_ID; // 默认使用open_id
            }

            if (msgType == null || msgType.trim().isEmpty()) {
                msgType = FeishuConstant.MSG_TYPE_TEXT; // 默认使用文本消息
            }

            if (content == null) {
                return new ToolExecuteResult("错误：content 参数不能为空", true);
            }

            // 发送消息
            String result = sendMessage(receiveId, receiveIdType, msgType, content);

            return new ToolExecuteResult("消息发送成功：" + result, false);

        } catch (FeishuException e) {
            logger.error("Failed to send Feishu message", e);
            return new ToolExecuteResult("发送消息失败：" + e.getMessage(), true);
        } catch (Exception e) {
            logger.error("Unexpected error in FeishuMessageTool", e);
            return new ToolExecuteResult("发送消息时发生未知错误：" + e.getMessage(), true);
        }
    }

    /**
     * 发送消息
     * 
     * @param receiveId 接收者ID
     * @param receiveIdType 接收者ID类型
     * @param msgType 消息类型
     * @param content 消息内容
     * @return 发送结果
     * @throws FeishuException 发送失败时抛出异常
     */
    private String sendMessage(String receiveId, String receiveIdType, String msgType, Object content) throws FeishuException {
        // 获取访问令牌
        String accessToken = feishuClient.getTenantAccessToken();
        
        // 构建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("receive_id", receiveId);
        requestBody.put("msg_type", msgType);
        
        // 根据消息类型处理内容
        JSONObject contentJson = processMessageContent(msgType, content);
        requestBody.put("content", contentJson.toJSONString());
        
        // 构建API路径
        String apiPath = FeishuConstant.API_SEND_MESSAGE + "?receive_id_type=" + receiveIdType;
        
        // 发送请求
        String response = feishuClient.doPost(apiPath, requestBody.toJSONString(), accessToken);
        
        // 解析响应
        JSONObject responseJson = JSON.parseObject(response);
        int code = responseJson.getIntValue("code");
        
        if (code != FeishuConstant.CODE_SUCCESS) {
            String msg = responseJson.getString("msg");
            throw new FeishuException(code, msg);
        }
        
        // 提取消息ID
        JSONObject data = responseJson.getJSONObject("data");
        String messageId = data != null ? data.getString("message_id") : "unknown";
        
        logger.info("Message sent successfully, message_id: {}", messageId);
        return messageId;
    }

    /**
     * 处理消息内容
     * 
     * @param msgType 消息类型
     * @param content 原始内容
     * @return 处理后的内容JSON
     */
    private JSONObject processMessageContent(String msgType, Object content) {
        JSONObject contentJson = new JSONObject();
        
        switch (msgType) {
            case FeishuConstant.MSG_TYPE_TEXT:
                // 文本消息
                if (content instanceof String) {
                    contentJson.put("text", content);
                } else if (content instanceof Map) {
                    Map<String, Object> contentMap = (Map<String, Object>) content;
                    contentJson.put("text", contentMap.get("text"));
                } else {
                    contentJson.put("text", content.toString());
                }
                break;
                
            case FeishuConstant.MSG_TYPE_POST:
                // 富文本消息
                if (content instanceof String) {
                    // 如果是字符串，尝试解析为JSON
                    try {
                        contentJson = JSON.parseObject((String) content);
                    } catch (Exception e) {
                        // 解析失败，作为普通文本处理
                        JSONObject post = new JSONObject();
                        JSONObject zhCn = new JSONObject();
                        zhCn.put("title", "");
                        zhCn.put("content", new Object[][]{{new JSONObject().fluentPut("tag", "text").fluentPut("text", content)}});
                        post.put("zh_cn", zhCn);
                        contentJson = post;
                    }
                } else if (content instanceof Map) {
                    contentJson.putAll((Map<String, Object>) content);
                } else {
                    contentJson = JSON.parseObject(JSON.toJSONString(content));
                }
                break;
                
            case FeishuConstant.MSG_TYPE_INTERACTIVE:
                // 交互式卡片消息
                if (content instanceof String) {
                    try {
                        contentJson = JSON.parseObject((String) content);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Interactive message content must be valid JSON");
                    }
                } else if (content instanceof Map) {
                    contentJson.putAll((Map<String, Object>) content);
                } else {
                    contentJson = JSON.parseObject(JSON.toJSONString(content));
                }
                break;
                
            default:
                // 其他类型消息，直接使用原始内容
                if (content instanceof String) {
                    try {
                        contentJson = JSON.parseObject((String) content);
                    } catch (Exception e) {
                        contentJson.put("content", content);
                    }
                } else if (content instanceof Map) {
                    contentJson.putAll((Map<String, Object>) content);
                } else {
                    contentJson = JSON.parseObject(JSON.toJSONString(content));
                }
                break;
        }
        
        return contentJson;
    }

    /**
     * 初始化客户端
     */
    private void initializeClient() {
        if (configuration == null) {
            try {
                configuration = FeishuConfigLoader.loadConfiguration();
            } catch (Exception e) {
                // 如果无法加载配置，则跳过客户端初始化
                logger.warn("Failed to load configuration, client will not be initialized: {}", e.getMessage());
                return;
            }
        }

        if (feishuClient == null && configuration != null) {
            try {
                feishuClient = new FeishuClient(configuration);
            } catch (Exception e) {
                logger.warn("Failed to initialize FeishuClient: {}", e.getMessage());
            }
        }
    }

    /**
     * 设置配置
     *
     * @param configuration 飞书配置
     */
    public void setConfiguration(FeishuConfiguration configuration) {
        this.configuration = configuration;
        // 只有在没有设置客户端时才创建新的客户端（避免覆盖测试中的mock客户端）
        if (configuration != null && feishuClient == null) {
            try {
                this.feishuClient = new FeishuClient(configuration);
            } catch (Exception e) {
                logger.warn("Failed to initialize FeishuClient: {}", e.getMessage());
            }
        }
    }

    /**
     * 设置飞书客户端（用于测试）
     *
     * @param feishuClient 飞书客户端
     */
    public void setFeishuClient(FeishuClient feishuClient) {
        this.feishuClient = feishuClient;
    }
}
