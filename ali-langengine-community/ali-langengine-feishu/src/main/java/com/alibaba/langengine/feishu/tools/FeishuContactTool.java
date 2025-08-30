package com.alibaba.langengine.feishu.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.langengine.core.tool.StructuredTool;
import com.alibaba.langengine.core.tool.ToolExecuteResult;
import com.alibaba.langengine.feishu.FeishuConfiguration;
import com.alibaba.langengine.feishu.sdk.FeishuClient;
import com.alibaba.langengine.feishu.sdk.FeishuConstant;
import com.alibaba.langengine.feishu.sdk.FeishuException;
import com.alibaba.langengine.feishu.tools.schema.FeishuContactSchema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;


@Data
@EqualsAndHashCode(callSuper = true)
public class FeishuContactTool extends StructuredTool<FeishuContactSchema> {

    private static final Logger logger = LoggerFactory.getLogger(FeishuContactTool.class);

    private FeishuClient feishuClient;
    private FeishuConfiguration configuration;

    /**
     * 默认构造函数
     */
    public FeishuContactTool() {
        setName("feishu_contact_query");
        setDescription("飞书通讯录查询工具。支持查询用户信息、搜索用户、查询部门信息、获取部门列表等操作。");
        setStructuredSchema(new FeishuContactSchema());

        // 不在构造函数中初始化客户端，等待配置设置后再初始化
    }

    /**
     * 带配置的构造函数
     * 
     * @param configuration 飞书配置
     */
    public FeishuContactTool(FeishuConfiguration configuration) {
        this();
        this.configuration = configuration;
        this.feishuClient = new FeishuClient(configuration);
    }

    /**
     * 执行通讯录查询操作
     * 
     * @param toolInput 工具输入参数
     * @return 执行结果
     */
    @Override
    public ToolExecuteResult execute(String toolInput) {
        try {
            logger.debug("FeishuContactTool input: {}", toolInput);
            
            // 解析输入参数
            Map<String, Object> inputMap = JSON.parseObject(toolInput, Map.class);
            
            // 获取操作类型
            String operation = (String) inputMap.get("operation");
            if (operation == null || operation.trim().isEmpty()) {
                return new ToolExecuteResult("错误：operation 参数不能为空", true);
            }

            // 根据操作类型执行相应操作
            switch (operation.toLowerCase()) {
                case "get_user":
                    return getUser(inputMap);
                case "search_user":
                    return searchUser(inputMap);
                case "get_department":
                    return getDepartment(inputMap);
                case "list_departments":
                    return listDepartments(inputMap);
                default:
                    return new ToolExecuteResult("错误：不支持的操作类型：" + operation, true);
            }
            
        } catch (FeishuException e) {
            logger.error("Failed to execute Feishu contact operation", e);
            return new ToolExecuteResult("通讯录查询失败：" + e.getMessage(), true);
        } catch (Exception e) {
            logger.error("Unexpected error in FeishuContactTool", e);
            return new ToolExecuteResult("通讯录查询时发生未知错误：" + e.getMessage(), true);
        }
    }

    /**
     * 获取用户信息
     * 
     * @param inputMap 输入参数
     * @return 执行结果
     * @throws FeishuException 操作失败时抛出异常
     */
    private ToolExecuteResult getUser(Map<String, Object> inputMap) throws FeishuException {
        String userId = (String) inputMap.get("user_id");
        String userIdType = (String) inputMap.get("user_id_type");
        
        if (userId == null || userId.trim().isEmpty()) {
            return new ToolExecuteResult("错误：获取用户信息时 user_id 参数不能为空", true);
        }
        
        if (userIdType == null || userIdType.trim().isEmpty()) {
            userIdType = FeishuConstant.RECEIVE_ID_TYPE_OPEN_ID; // 默认使用open_id
        }
        
        // 获取访问令牌
        String accessToken = feishuClient.getTenantAccessToken();
        
        // 构建API路径
        String apiPath = FeishuConstant.API_GET_USER.replace("{user_id}", userId) + "?user_id_type=" + userIdType;
        
        // 发送请求
        String response = feishuClient.doGet(apiPath, accessToken);
        
        // 解析响应
        JSONObject responseJson = JSON.parseObject(response);
        int code = responseJson.getIntValue("code");
        
        if (code != FeishuConstant.CODE_SUCCESS) {
            String msg = responseJson.getString("msg");
            throw new FeishuException(code, msg);
        }
        
        // 提取用户信息
        JSONObject data = responseJson.getJSONObject("data");
        JSONObject user = data.getJSONObject("user");
        
        JSONObject result = new JSONObject();
        result.put("user_id", userId);
        result.put("user_info", user);
        result.put("status", "retrieved");
        
        logger.info("User info retrieved successfully, user_id: {}", userId);
        return new ToolExecuteResult("用户信息获取成功：" + result.toJSONString(), false);
    }

    /**
     * 搜索用户
     * 
     * @param inputMap 输入参数
     * @return 执行结果
     * @throws FeishuException 操作失败时抛出异常
     */
    private ToolExecuteResult searchUser(Map<String, Object> inputMap) throws FeishuException {
        @SuppressWarnings("unchecked")
        List<String> emails = (List<String>) inputMap.get("emails");
        @SuppressWarnings("unchecked")
        List<String> mobiles = (List<String>) inputMap.get("mobiles");
        
        if ((emails == null || emails.isEmpty()) && (mobiles == null || mobiles.isEmpty())) {
            return new ToolExecuteResult("错误：搜索用户时 emails 或 mobiles 参数至少需要提供一个", true);
        }
        
        // 获取访问令牌
        String accessToken = feishuClient.getTenantAccessToken();
        
        // 构建请求体
        JSONObject requestBody = new JSONObject();
        if (emails != null && !emails.isEmpty()) {
            requestBody.put("emails", emails);
        }
        if (mobiles != null && !mobiles.isEmpty()) {
            requestBody.put("mobiles", mobiles);
        }
        
        // 发送请求
        String response = feishuClient.doPost(FeishuConstant.API_SEARCH_USER, requestBody.toJSONString(), accessToken);
        
        // 解析响应
        JSONObject responseJson = JSON.parseObject(response);
        int code = responseJson.getIntValue("code");
        
        if (code != FeishuConstant.CODE_SUCCESS) {
            String msg = responseJson.getString("msg");
            throw new FeishuException(code, msg);
        }
        
        // 提取搜索结果
        JSONObject data = responseJson.getJSONObject("data");
        
        JSONObject result = new JSONObject();
        result.put("user_list", data.getJSONArray("user_list"));
        result.put("status", "searched");
        
        logger.info("User search completed successfully, found {} users",
                data.getJSONArray("user_list") != null ? data.getJSONArray("user_list").size() : 0);
        return new ToolExecuteResult("用户搜索成功：" + result.toJSONString(), false);
    }

    /**
     * 获取部门信息
     * 
     * @param inputMap 输入参数
     * @return 执行结果
     * @throws FeishuException 操作失败时抛出异常
     */
    private ToolExecuteResult getDepartment(Map<String, Object> inputMap) throws FeishuException {
        String departmentId = (String) inputMap.get("department_id");
        String departmentIdType = (String) inputMap.get("department_id_type");
        
        if (departmentId == null || departmentId.trim().isEmpty()) {
            return new ToolExecuteResult("错误：获取部门信息时 department_id 参数不能为空", true);
        }
        
        if (departmentIdType == null || departmentIdType.trim().isEmpty()) {
            departmentIdType = "department_id"; // 默认使用department_id
        }
        
        // 获取访问令牌
        String accessToken = feishuClient.getTenantAccessToken();
        
        // 构建API路径
        String apiPath = FeishuConstant.API_GET_DEPARTMENT.replace("{department_id}", departmentId) 
                + "?department_id_type=" + departmentIdType;
        
        // 发送请求
        String response = feishuClient.doGet(apiPath, accessToken);
        
        // 解析响应
        JSONObject responseJson = JSON.parseObject(response);
        int code = responseJson.getIntValue("code");
        
        if (code != FeishuConstant.CODE_SUCCESS) {
            String msg = responseJson.getString("msg");
            throw new FeishuException(code, msg);
        }
        
        // 提取部门信息
        JSONObject data = responseJson.getJSONObject("data");
        JSONObject department = data.getJSONObject("department");
        
        JSONObject result = new JSONObject();
        result.put("department_id", departmentId);
        result.put("department_info", department);
        result.put("status", "retrieved");
        
        logger.info("Department info retrieved successfully, department_id: {}", departmentId);
        return new ToolExecuteResult("部门信息获取成功：" + result.toJSONString(), false);
    }

    /**
     * 获取部门列表
     * 
     * @param inputMap 输入参数
     * @return 执行结果
     * @throws FeishuException 操作失败时抛出异常
     */
    private ToolExecuteResult listDepartments(Map<String, Object> inputMap) throws FeishuException {
        // 获取访问令牌
        String accessToken = feishuClient.getTenantAccessToken();
        
        // 构建查询参数
        StringBuilder queryParams = new StringBuilder();
        
        String parentDepartmentId = (String) inputMap.get("parent_department_id");
        if (parentDepartmentId != null && !parentDepartmentId.trim().isEmpty()) {
            queryParams.append("parent_department_id=").append(parentDepartmentId);
        }
        
        Boolean fetchChild = (Boolean) inputMap.get("fetch_child");
        if (fetchChild != null) {
            if (queryParams.length() > 0) queryParams.append("&");
            queryParams.append("fetch_child=").append(fetchChild);
        }
        
        Integer pageSize = (Integer) inputMap.get("page_size");
        if (pageSize != null && pageSize > 0) {
            if (queryParams.length() > 0) queryParams.append("&");
            queryParams.append("page_size=").append(Math.min(pageSize, FeishuConstant.MAX_PAGE_SIZE));
        }
        
        String pageToken = (String) inputMap.get("page_token");
        if (pageToken != null && !pageToken.trim().isEmpty()) {
            if (queryParams.length() > 0) queryParams.append("&");
            queryParams.append("page_token=").append(pageToken);
        }
        
        // 构建API路径
        String apiPath = FeishuConstant.API_LIST_DEPARTMENTS;
        if (queryParams.length() > 0) {
            apiPath += "?" + queryParams.toString();
        }
        
        // 发送请求
        String response = feishuClient.doGet(apiPath, accessToken);
        
        // 解析响应
        JSONObject responseJson = JSON.parseObject(response);
        int code = responseJson.getIntValue("code");
        
        if (code != FeishuConstant.CODE_SUCCESS) {
            String msg = responseJson.getString("msg");
            throw new FeishuException(code, msg);
        }
        
        // 提取部门列表
        JSONObject data = responseJson.getJSONObject("data");
        
        JSONObject result = new JSONObject();
        result.put("departments", data.getJSONArray("items"));
        result.put("has_more", data.getBoolean("has_more"));
        result.put("page_token", data.getString("page_token"));
        result.put("status", "listed");
        
        logger.info("Department list retrieved successfully, count: {}",
                data.getJSONArray("items") != null ? data.getJSONArray("items").size() : 0);
        return new ToolExecuteResult("部门列表获取成功：" + result.toJSONString(), false);
    }

    /**
     * 初始化客户端
     */
    private void initializeClient() {
        if (configuration == null) {
            configuration = new FeishuConfiguration();
        }
        
        if (feishuClient == null) {
            feishuClient = new FeishuClient(configuration);
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
