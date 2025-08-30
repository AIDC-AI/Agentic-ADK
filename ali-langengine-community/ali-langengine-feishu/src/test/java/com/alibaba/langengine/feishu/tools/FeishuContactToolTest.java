package com.alibaba.langengine.feishu.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.langengine.core.tool.ToolExecuteResult;
import com.alibaba.langengine.feishu.FeishuConfiguration;
import com.alibaba.langengine.feishu.sdk.FeishuClient;
import com.alibaba.langengine.feishu.sdk.FeishuException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FeishuContactToolTest {

    @Mock
    private FeishuClient mockFeishuClient;

    private FeishuContactTool contactTool;

    @BeforeEach
    void setUp() {
        // 创建真实的配置对象而不是mock，因为构造函数中有验证逻辑
        FeishuConfiguration realConfiguration = new FeishuConfiguration();
        realConfiguration.setAppId("test_app_id");
        realConfiguration.setAppSecret("test_app_secret");

        contactTool = new FeishuContactTool();
        contactTool.setFeishuClient(mockFeishuClient);
        contactTool.setConfiguration(realConfiguration);
    }

    @Test
    void testDefaultConstructor() {
        // 测试默认构造函数
        FeishuContactTool tool = new FeishuContactTool();
        
        assertThat(tool.getName()).isEqualTo("feishu_contact_query");
        assertThat(tool.getDescription()).contains("飞书通讯录查询工具");
        assertThat(tool.getStructuredSchema()).isNotNull();
    }

    @Test
    void testConstructorWithConfiguration() {
        // 测试带配置的构造函数
        FeishuConfiguration config = new FeishuConfiguration("test_app_id", "test_app_secret");
        FeishuContactTool tool = new FeishuContactTool(config);
        
        assertThat(tool.getConfiguration()).isEqualTo(config);
        assertThat(tool.getName()).isEqualTo("feishu_contact_query");
    }

    @Test
    void testExecuteWithMissingOperation() {
        // 测试缺少操作类型
        Map<String, Object> input = new HashMap<>();
        input.put("user_id", "test_user");
        
        String inputJson = JSON.toJSONString(input);
        
        ToolExecuteResult result = contactTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("operation 参数不能为空");
    }

    @Test
    void testExecuteWithUnsupportedOperation() {
        // 测试不支持的操作类型
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "delete_user");

        String inputJson = JSON.toJSONString(input);

        ToolExecuteResult result = contactTool.execute(inputJson);

        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("不支持的操作类型：delete_user");
    }

    @Test
    void testGetUserSuccess() throws Exception {
        // 测试成功获取用户信息
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "get_user");
        input.put("user_id", "user_123");
        input.put("user_id_type", "open_id");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doGet(anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"user\":{\"name\":\"Test User\",\"email\":\"test@example.com\"}}}");
        
        ToolExecuteResult result = contactTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("用户信息获取成功");
        assertThat(result.getOutput()).contains("user_123");
        
        // 验证方法调用
        verify(mockFeishuClient).getTenantAccessToken();
        verify(mockFeishuClient).doGet(contains("user_123"), eq("mock_token"));
        verify(mockFeishuClient).doGet(contains("user_id_type=open_id"), eq("mock_token"));
    }

    @Test
    void testGetUserWithMissingUserId() {
        // 测试获取用户时缺少用户ID
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "get_user");
        input.put("user_id_type", "open_id");
        
        String inputJson = JSON.toJSONString(input);
        
        ToolExecuteResult result = contactTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("user_id 参数不能为空");
    }

    @Test
    void testGetUserWithDefaultUserIdType() throws Exception {
        // 测试使用默认用户ID类型
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "get_user");
        input.put("user_id", "user_456");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doGet(anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"user\":{\"name\":\"Default User\"}}}");
        
        ToolExecuteResult result = contactTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();

        // 验证使用了默认的user_id_type
        verify(mockFeishuClient).doGet(contains("user_id_type=open_id"), eq("mock_token"));
    }

    @Test
    void testSearchUserWithEmails() throws Exception {
        // 测试通过邮箱搜索用户
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "search_user");
        input.put("emails", Arrays.asList("user1@example.com", "user2@example.com"));
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"user_list\":[{\"user_id\":\"user1\"},{\"user_id\":\"user2\"}]}}");
        
        ToolExecuteResult result = contactTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("用户搜索成功");

        // 验证方法调用
        verify(mockFeishuClient).getTenantAccessToken();
        verify(mockFeishuClient).doPost(anyString(), contains("user1@example.com"), eq("mock_token"));
    }

    @Test
    void testSearchUserWithMobiles() throws Exception {
        // 测试通过手机号搜索用户
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "search_user");
        input.put("mobiles", Arrays.asList("13800138000", "13900139000"));
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"user_list\":[{\"user_id\":\"mobile_user1\"}]}}");
        
        ToolExecuteResult result = contactTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("用户搜索成功");

        // 验证方法调用
        verify(mockFeishuClient).doPost(anyString(), contains("13800138000"), eq("mock_token"));
    }

    @Test
    void testSearchUserWithBothEmailsAndMobiles() throws Exception {
        // 测试同时使用邮箱和手机号搜索用户
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "search_user");
        input.put("emails", Arrays.asList("test@example.com"));
        input.put("mobiles", Arrays.asList("13800138000"));
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"user_list\":[{\"user_id\":\"combined_user\"}]}}");
        
        ToolExecuteResult result = contactTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();

        // 验证请求体包含两种类型的信息
        verify(mockFeishuClient).doPost(anyString(), contains("test@example.com"), eq("mock_token"));
        verify(mockFeishuClient).doPost(anyString(), contains("13800138000"), eq("mock_token"));
    }

    @Test
    void testSearchUserWithMissingParameters() {
        // 测试搜索用户时缺少必要参数
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "search_user");
        
        String inputJson = JSON.toJSONString(input);
        
        ToolExecuteResult result = contactTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("emails 或 mobiles 参数至少需要提供一个");
    }

    @Test
    void testGetDepartmentSuccess() throws Exception {
        // 测试成功获取部门信息
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "get_department");
        input.put("department_id", "dept_123");
        input.put("department_id_type", "department_id");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doGet(anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"department\":{\"name\":\"Test Department\",\"parent_department_id\":\"parent_123\"}}}");
        
        ToolExecuteResult result = contactTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("部门信息获取成功");
        assertThat(result.getOutput()).contains("dept_123");
        
        // 验证方法调用
        verify(mockFeishuClient).getTenantAccessToken();
        verify(mockFeishuClient).doGet(contains("dept_123"), eq("mock_token"));
        verify(mockFeishuClient).doGet(contains("department_id_type=department_id"), eq("mock_token"));
    }

    @Test
    void testGetDepartmentWithMissingDepartmentId() {
        // 测试获取部门时缺少部门ID
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "get_department");
        
        String inputJson = JSON.toJSONString(input);
        
        ToolExecuteResult result = contactTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("department_id 参数不能为空");
    }

    @Test
    void testGetDepartmentWithDefaultDepartmentIdType() throws Exception {
        // 测试使用默认部门ID类型
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "get_department");
        input.put("department_id", "dept_456");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doGet(anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"department\":{\"name\":\"Default Department\"}}}");
        
        ToolExecuteResult result = contactTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();

        // 验证使用了默认的department_id_type
        verify(mockFeishuClient).doGet(contains("department_id_type=department_id"), eq("mock_token"));
    }

    @Test
    void testListDepartmentsSuccess() throws Exception {
        // 测试成功获取部门列表
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "list_departments");
        input.put("parent_department_id", "parent_123");
        input.put("fetch_child", true);
        input.put("page_size", 50);
        input.put("page_token", "token_456");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doGet(anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"items\":[{\"department_id\":\"dept1\"},{\"department_id\":\"dept2\"}],\"has_more\":false,\"page_token\":\"\"}}");
        
        ToolExecuteResult result = contactTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("部门列表获取成功");
        assertThat(result.getOutput()).contains("has_more");
        
        // 验证方法调用
        verify(mockFeishuClient).getTenantAccessToken();
        verify(mockFeishuClient).doGet(contains("parent_department_id=parent_123"), eq("mock_token"));
        verify(mockFeishuClient).doGet(contains("fetch_child=true"), eq("mock_token"));
        verify(mockFeishuClient).doGet(contains("page_size=50"), eq("mock_token"));
        verify(mockFeishuClient).doGet(contains("page_token=token_456"), eq("mock_token"));
    }

    @Test
    void testListDepartmentsWithMinimalParameters() throws Exception {
        // 测试使用最少参数获取部门列表
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "list_departments");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doGet(anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"items\":[],\"has_more\":false,\"page_token\":\"\"}}");
        
        ToolExecuteResult result = contactTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        
        // 验证API路径不包含查询参数（或只包含默认参数）
        verify(mockFeishuClient).doGet(anyString(), eq("mock_token"));
    }

    @Test
    void testExecuteWithFeishuException() throws Exception {
        // 测试飞书API异常
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "get_user");
        input.put("user_id", "user_123");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端抛出异常
        when(mockFeishuClient.getTenantAccessToken()).thenThrow(new FeishuException(1001, "Invalid token"));
        
        ToolExecuteResult result = contactTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("通讯录查询失败");
        assertThat(result.getOutput()).contains("Invalid token");
    }

    @Test
    void testExecuteWithApiError() throws Exception {
        // 测试API返回错误
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "get_user");
        input.put("user_id", "user_123");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doGet(anyString(), anyString()))
                .thenReturn("{\"code\":1002,\"msg\":\"Permission denied\"}");
        
        ToolExecuteResult result = contactTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("通讯录查询失败");
        assertThat(result.getOutput()).contains("Permission denied");
    }

    @Test
    void testExecuteWithInvalidJson() {
        // 测试无效的JSON输入
        String invalidJson = "invalid json";
        
        ToolExecuteResult result = contactTool.execute(invalidJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("发生未知错误");
    }

    @Test
    void testSetConfiguration() {
        // 测试设置配置
        FeishuConfiguration newConfig = new FeishuConfiguration("new_app_id", "new_app_secret");
        
        contactTool.setConfiguration(newConfig);
        
        assertThat(contactTool.getConfiguration()).isEqualTo(newConfig);
    }

    @Test
    void testToolMetadata() {
        // 测试工具元数据
        assertThat(contactTool.getName()).isEqualTo("feishu_contact_query");
        assertThat(contactTool.getDescription()).isNotEmpty();
        assertThat(contactTool.getStructuredSchema()).isNotNull();
        assertThat(contactTool.getStructuredSchema().getParameters()).isNotEmpty();
    }
}
