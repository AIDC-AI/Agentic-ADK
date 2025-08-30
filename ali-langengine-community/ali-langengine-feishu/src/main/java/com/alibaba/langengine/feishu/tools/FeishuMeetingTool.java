package com.alibaba.langengine.feishu.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.langengine.core.tool.StructuredTool;
import com.alibaba.langengine.core.tool.ToolExecuteResult;
import com.alibaba.langengine.feishu.FeishuConfiguration;
import com.alibaba.langengine.feishu.sdk.FeishuClient;
import com.alibaba.langengine.feishu.sdk.FeishuConstant;
import com.alibaba.langengine.feishu.sdk.FeishuException;
import com.alibaba.langengine.feishu.tools.schema.FeishuMeetingSchema;
import com.alibaba.langengine.feishu.util.FeishuConfigLoader;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


@Data
@EqualsAndHashCode(callSuper = true)
public class FeishuMeetingTool extends StructuredTool<FeishuMeetingSchema> {

    private static final Logger logger = LoggerFactory.getLogger(FeishuMeetingTool.class);

    private FeishuClient feishuClient;
    private FeishuConfiguration configuration;

    /**
     * 默认构造函数
     */
    public FeishuMeetingTool() {
        setName("feishu_meeting_operation");
        setDescription("飞书会议管理工具。支持创建会议、查询会议信息、获取会议列表等操作。");
        setStructuredSchema(new FeishuMeetingSchema());

        // 不在构造函数中初始化客户端，等待配置设置后再初始化
    }

    /**
     * 带配置的构造函数
     * 
     * @param configuration 飞书配置
     */
    public FeishuMeetingTool(FeishuConfiguration configuration) {
        this();
        this.configuration = configuration;
        this.feishuClient = new FeishuClient(configuration);
    }

    /**
     * 执行会议操作
     * 
     * @param toolInput 工具输入参数
     * @return 执行结果
     */
    @Override
    public ToolExecuteResult execute(String toolInput) {
        try {
            logger.debug("FeishuMeetingTool input: {}", toolInput);
            
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
                    return createMeeting(inputMap);
                case "get":
                case "query":
                    return getMeeting(inputMap);
                case "list":
                    return listMeetings(inputMap);
                default:
                    return new ToolExecuteResult("错误：不支持的操作类型：" + operation, true);
            }
            
        } catch (FeishuException e) {
            logger.error("Failed to execute Feishu meeting operation", e);
            return new ToolExecuteResult("会议操作失败：" + e.getMessage(), true);
        } catch (Exception e) {
            logger.error("Unexpected error in FeishuMeetingTool", e);
            return new ToolExecuteResult("会议操作时发生未知错误：" + e.getMessage(), true);
        }
    }

    /**
     * 创建会议
     * 
     * @param inputMap 输入参数
     * @return 执行结果
     * @throws FeishuException 操作失败时抛出异常
     */
    private ToolExecuteResult createMeeting(Map<String, Object> inputMap) throws FeishuException {
        String topic = (String) inputMap.get("topic");
        String startTime = (String) inputMap.get("start_time");
        Integer duration = (Integer) inputMap.get("duration");
        
        if (topic == null || topic.trim().isEmpty()) {
            return new ToolExecuteResult("错误：创建会议时 topic 参数不能为空", true);
        }

        if (startTime == null || startTime.trim().isEmpty()) {
            return new ToolExecuteResult("错误：创建会议时 start_time 参数不能为空", true);
        }
        
        if (duration == null || duration <= 0) {
            duration = 60; // 默认60分钟
        }
        
        // 获取访问令牌
        String accessToken = feishuClient.getTenantAccessToken();
        
        // 构建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("topic", topic);
        requestBody.put("start_time", parseDateTime(startTime));
        requestBody.put("end_time", parseDateTime(startTime) + duration * 60); // 转换为秒
        
        // 处理可选参数
        String description = (String) inputMap.get("description");
        if (description != null && !description.trim().isEmpty()) {
            requestBody.put("description", description);
        }
        
        @SuppressWarnings("unchecked")
        List<String> invitees = (List<String>) inputMap.get("invitees");
        if (invitees != null && !invitees.isEmpty()) {
            requestBody.put("invitees", invitees);
        }
        
        Boolean needPassword = (Boolean) inputMap.get("need_password");
        if (needPassword != null) {
            requestBody.put("need_password", needPassword);
        }
        
        Boolean autoRecord = (Boolean) inputMap.get("auto_record");
        if (autoRecord != null) {
            requestBody.put("auto_record", autoRecord);
        }
        
        // 发送创建会议请求
        String response = feishuClient.doPost(FeishuConstant.API_CREATE_MEETING, requestBody.toJSONString(), accessToken);
        
        // 解析响应
        JSONObject responseJson = JSON.parseObject(response);
        int code = responseJson.getIntValue("code");
        
        if (code != FeishuConstant.CODE_SUCCESS) {
            String msg = responseJson.getString("msg");
            throw new FeishuException(code, msg);
        }
        
        // 提取会议信息
        JSONObject data = responseJson.getJSONObject("data");
        JSONObject reserve = data.getJSONObject("reserve");
        
        JSONObject result = new JSONObject();
        result.put("reserve_id", reserve.getString("id"));
        result.put("topic", topic);
        result.put("start_time", startTime);
        result.put("duration", duration);
        result.put("meeting_url", reserve.getString("meeting_url"));
        result.put("join_url", reserve.getString("join_url"));
        result.put("status", "created");
        
        logger.info("Meeting created successfully, reserve_id: {}", reserve.getString("id"));
        return new ToolExecuteResult("会议创建成功：" + result.toJSONString(), false);
    }

    /**
     * 获取会议信息
     * 
     * @param inputMap 输入参数
     * @return 执行结果
     * @throws FeishuException 操作失败时抛出异常
     */
    private ToolExecuteResult getMeeting(Map<String, Object> inputMap) throws FeishuException {
        String reserveId = (String) inputMap.get("reserve_id");
        
        if (reserveId == null || reserveId.trim().isEmpty()) {
            return new ToolExecuteResult("错误：查询会议时 reserve_id 参数不能为空", true);
        }
        
        // 获取访问令牌
        String accessToken = feishuClient.getTenantAccessToken();
        
        // 构建API路径
        String apiPath = FeishuConstant.API_GET_MEETING.replace("{reserve_id}", reserveId);
        
        // 发送请求
        String response = feishuClient.doGet(apiPath, accessToken);
        
        // 解析响应
        JSONObject responseJson = JSON.parseObject(response);
        int code = responseJson.getIntValue("code");
        
        if (code != FeishuConstant.CODE_SUCCESS) {
            String msg = responseJson.getString("msg");
            throw new FeishuException(code, msg);
        }
        
        // 提取会议信息
        JSONObject data = responseJson.getJSONObject("data");
        
        JSONObject result = new JSONObject();
        result.put("reserve_id", reserveId);
        result.put("meeting_info", data);
        result.put("status", "retrieved");
        
        logger.info("Meeting retrieved successfully, reserve_id: {}", reserveId);
        return new ToolExecuteResult("会议信息获取成功：" + result.toJSONString(), false);
    }

    /**
     * 获取会议列表
     * 
     * @param inputMap 输入参数
     * @return 执行结果
     * @throws FeishuException 操作失败时抛出异常
     */
    private ToolExecuteResult listMeetings(Map<String, Object> inputMap) throws FeishuException {
        // 获取访问令牌
        String accessToken = feishuClient.getTenantAccessToken();
        
        // 构建查询参数
        StringBuilder queryParams = new StringBuilder();
        
        Integer pageSize = (Integer) inputMap.get("page_size");
        if (pageSize != null && pageSize > 0) {
            queryParams.append("page_size=").append(Math.min(pageSize, FeishuConstant.MAX_PAGE_SIZE));
        } else {
            queryParams.append("page_size=").append(FeishuConstant.DEFAULT_PAGE_SIZE);
        }
        
        String pageToken = (String) inputMap.get("page_token");
        if (pageToken != null && !pageToken.trim().isEmpty()) {
            queryParams.append("&page_token=").append(pageToken);
        }
        
        // 构建API路径
        String apiPath = FeishuConstant.API_LIST_MEETINGS + "?" + queryParams.toString();
        
        // 发送请求
        String response = feishuClient.doGet(apiPath, accessToken);
        
        // 解析响应
        JSONObject responseJson = JSON.parseObject(response);
        int code = responseJson.getIntValue("code");
        
        if (code != FeishuConstant.CODE_SUCCESS) {
            String msg = responseJson.getString("msg");
            throw new FeishuException(code, msg);
        }
        
        // 提取会议列表
        JSONObject data = responseJson.getJSONObject("data");
        
        JSONObject result = new JSONObject();
        result.put("meetings", data.getJSONArray("reserves"));
        result.put("has_more", data.getBoolean("has_more"));
        result.put("page_token", data.getString("page_token"));
        result.put("status", "listed");
        
        logger.info("Meeting list retrieved successfully, count: {}",
                data.getJSONArray("reserves") != null ? data.getJSONArray("reserves").size() : 0);
        return new ToolExecuteResult("会议列表获取成功：" + result.toJSONString(), false);
    }

    /**
     * 解析日期时间字符串为时间戳
     * 
     * @param dateTimeStr 日期时间字符串
     * @return 时间戳（秒）
     */
    private long parseDateTime(String dateTimeStr) {
        try {
            // 支持多种日期时间格式
            DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
            };
            
            for (DateTimeFormatter formatter : formatters) {
                try {
                    LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
                    return dateTime.atZone(java.time.ZoneId.systemDefault()).toEpochSecond();
                } catch (Exception ignored) {
                    // 尝试下一个格式
                }
            }
            
            // 如果都解析失败，尝试解析为时间戳
            return Long.parseLong(dateTimeStr);
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date time format: " + dateTimeStr, e);
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
