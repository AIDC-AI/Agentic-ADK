package com.alibaba.langengine.feishu.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.langengine.core.tool.StructuredTool;
import com.alibaba.langengine.core.tool.ToolExecuteResult;
import com.alibaba.langengine.feishu.FeishuConfiguration;
import com.alibaba.langengine.feishu.sdk.FeishuClient;
import com.alibaba.langengine.feishu.sdk.FeishuConstant;
import com.alibaba.langengine.feishu.sdk.FeishuException;
import com.alibaba.langengine.feishu.tools.schema.FeishuDocumentSchema;
import com.alibaba.langengine.feishu.util.FeishuConfigLoader;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


@Data
@EqualsAndHashCode(callSuper = true)
public class FeishuDocumentTool extends StructuredTool<FeishuDocumentSchema> {

    private static final Logger logger = LoggerFactory.getLogger(FeishuDocumentTool.class);

    private FeishuClient feishuClient;
    private FeishuConfiguration configuration;

    /**
     * 默认构造函数
     */
    public FeishuDocumentTool() {
        setName("feishu_document_operation");
        setDescription("飞书文档操作工具。支持创建新文档、读取文档内容、更新文档内容等操作。");
        setStructuredSchema(new FeishuDocumentSchema());

        // 不在构造函数中初始化客户端，等待配置设置后再初始化
    }

    /**
     * 带配置的构造函数
     * 
     * @param configuration 飞书配置
     */
    public FeishuDocumentTool(FeishuConfiguration configuration) {
        this();
        this.configuration = configuration;
        this.feishuClient = new FeishuClient(configuration);
    }

    /**
     * 执行文档操作
     * 
     * @param toolInput 工具输入参数
     * @return 执行结果
     */
    @Override
    public ToolExecuteResult execute(String toolInput) {
        try {
            logger.debug("FeishuDocumentTool input: {}", toolInput);

            // 解析输入参数
            Map<String, Object> inputMap = JSON.parseObject(toolInput, Map.class);

            // 获取操作类型
            String operation = (String) inputMap.get("operation");
            if (operation == null || operation.trim().isEmpty()) {
                return new ToolExecuteResult("错误：operation 参数不能为空", true);
            }

            // 根据操作类型执行相应操作
            switch (operation.toLowerCase()) {
                case "create":
                    return createDocument(inputMap);
                case "read":
                case "get":
                    return readDocument(inputMap);
                case "update":
                    return updateDocument(inputMap);
                default:
                    return new ToolExecuteResult("错误：不支持的操作类型：" + operation, true);
            }

        } catch (FeishuException e) {
            logger.error("Failed to execute Feishu document operation", e);
            return new ToolExecuteResult("文档操作失败：" + e.getMessage(), true);
        } catch (Exception e) {
            logger.error("Unexpected error in FeishuDocumentTool", e);
            return new ToolExecuteResult("文档操作时发生未知错误：" + e.getMessage(), true);
        }
    }

    /**
     * 创建文档
     * 
     * @param inputMap 输入参数
     * @return 执行结果
     * @throws FeishuException 操作失败时抛出异常
     */
    private ToolExecuteResult createDocument(Map<String, Object> inputMap) throws FeishuException {
        String title = (String) inputMap.get("title");
        String content = (String) inputMap.get("content");
        String folderId = (String) inputMap.get("folder_id");
        
        if (title == null || title.trim().isEmpty()) {
            return new ToolExecuteResult("错误：创建文档时 title 参数不能为空", true);
        }
        
        // 获取访问令牌
        String accessToken = feishuClient.getTenantAccessToken();
        
        // 构建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", title);
        
        if (folderId != null && !folderId.trim().isEmpty()) {
            requestBody.put("folder_token", folderId);
        }
        
        // 发送创建文档请求
        String response = feishuClient.doPost(FeishuConstant.API_CREATE_DOCUMENT, requestBody.toJSONString(), accessToken);
        
        // 解析响应
        JSONObject responseJson = JSON.parseObject(response);
        int code = responseJson.getIntValue("code");
        
        if (code != FeishuConstant.CODE_SUCCESS) {
            String msg = responseJson.getString("msg");
            throw new FeishuException(code, msg);
        }
        
        // 提取文档信息
        JSONObject data = responseJson.getJSONObject("data");
        JSONObject document = data.getJSONObject("document");
        String documentId = document.getString("document_id");
        String documentUrl = document.getString("url");
        
        // 如果有内容，更新文档内容
        if (content != null && !content.trim().isEmpty()) {
            updateDocumentContent(documentId, content, accessToken);
        }
        
        JSONObject result = new JSONObject();
        result.put("document_id", documentId);
        result.put("title", title);
        result.put("url", documentUrl);
        result.put("status", "created");
        
        logger.info("Document created successfully, document_id: {}", documentId);
        return new ToolExecuteResult("文档创建成功：" + result.toJSONString(), false);
    }

    /**
     * 读取文档
     * 
     * @param inputMap 输入参数
     * @return 执行结果
     * @throws FeishuException 操作失败时抛出异常
     */
    private ToolExecuteResult readDocument(Map<String, Object> inputMap) throws FeishuException {
        String documentId = (String) inputMap.get("document_id");
        
        if (documentId == null || documentId.trim().isEmpty()) {
            return new ToolExecuteResult("错误：读取文档时 document_id 参数不能为空", true);
        }
        
        // 获取访问令牌
        String accessToken = feishuClient.getTenantAccessToken();
        
        // 构建API路径
        String apiPath = FeishuConstant.API_GET_DOCUMENT.replace("{document_id}", documentId);
        
        // 发送请求
        String response = feishuClient.doGet(apiPath, accessToken);
        
        // 解析响应
        JSONObject responseJson = JSON.parseObject(response);
        int code = responseJson.getIntValue("code");
        
        if (code != FeishuConstant.CODE_SUCCESS) {
            String msg = responseJson.getString("msg");
            throw new FeishuException(code, msg);
        }
        
        // 提取文档内容
        JSONObject data = responseJson.getJSONObject("data");
        
        JSONObject result = new JSONObject();
        result.put("document_id", documentId);
        result.put("content", data);
        result.put("status", "read");
        
        logger.info("Document read successfully, document_id: {}", documentId);
        return new ToolExecuteResult("文档读取成功：" + result.toJSONString(), false);
    }

    /**
     * 更新文档
     * 
     * @param inputMap 输入参数
     * @return 执行结果
     * @throws FeishuException 操作失败时抛出异常
     */
    private ToolExecuteResult updateDocument(Map<String, Object> inputMap) throws FeishuException {
        String documentId = (String) inputMap.get("document_id");
        String content = (String) inputMap.get("content");
        
        if (documentId == null || documentId.trim().isEmpty()) {
            return new ToolExecuteResult("错误：更新文档时 document_id 参数不能为空", true);
        }

        if (content == null || content.trim().isEmpty()) {
            return new ToolExecuteResult("错误：更新文档时 content 参数不能为空", true);
        }
        
        // 获取访问令牌
        String accessToken = feishuClient.getTenantAccessToken();
        
        // 更新文档内容
        updateDocumentContent(documentId, content, accessToken);
        
        JSONObject result = new JSONObject();
        result.put("document_id", documentId);
        result.put("status", "updated");
        
        logger.info("Document updated successfully, document_id: {}", documentId);
        return new ToolExecuteResult("文档更新成功：" + result.toJSONString(), false);
    }

    /**
     * 更新文档内容
     * 
     * @param documentId 文档ID
     * @param content 文档内容
     * @param accessToken 访问令牌
     * @throws FeishuException 更新失败时抛出异常
     */
    private void updateDocumentContent(String documentId, String content, String accessToken) throws FeishuException {
        // 构建请求体
        JSONObject requestBody = new JSONObject();
        
        // 构建文档内容结构
        JSONObject contentObj = new JSONObject();
        contentObj.put("type", "text");
        contentObj.put("text", content);
        
        requestBody.put("requests", new Object[]{
            new JSONObject().fluentPut("insertText", new JSONObject()
                .fluentPut("location", new JSONObject().fluentPut("index", 0))
                .fluentPut("elements", new Object[]{contentObj}))
        });
        
        // 构建API路径
        String apiPath = FeishuConstant.API_UPDATE_DOCUMENT.replace("{document_id}", documentId);
        
        // 发送请求
        String response = feishuClient.doPut(apiPath, requestBody.toJSONString(), accessToken);
        
        // 解析响应
        JSONObject responseJson = JSON.parseObject(response);
        int code = responseJson.getIntValue("code");
        
        if (code != FeishuConstant.CODE_SUCCESS) {
            String msg = responseJson.getString("msg");
            throw new FeishuException(code, msg);
        }
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
