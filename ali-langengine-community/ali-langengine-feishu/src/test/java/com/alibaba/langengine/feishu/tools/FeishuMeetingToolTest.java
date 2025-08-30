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
class FeishuMeetingToolTest {

    @Mock
    private FeishuClient mockFeishuClient;

    private FeishuMeetingTool meetingTool;

    @BeforeEach
    void setUp() {
        // 创建真实的配置对象而不是mock，因为构造函数中有验证逻辑
        FeishuConfiguration realConfiguration = new FeishuConfiguration();
        realConfiguration.setAppId("test_app_id");
        realConfiguration.setAppSecret("test_app_secret");

        meetingTool = new FeishuMeetingTool();
        meetingTool.setFeishuClient(mockFeishuClient);
        meetingTool.setConfiguration(realConfiguration);
    }

    @Test
    void testDefaultConstructor() {
        // 测试默认构造函数
        FeishuMeetingTool tool = new FeishuMeetingTool();
        
        assertThat(tool.getName()).isEqualTo("feishu_meeting_operation");
        assertThat(tool.getDescription()).contains("飞书会议管理工具");
        assertThat(tool.getStructuredSchema()).isNotNull();
    }

    @Test
    void testConstructorWithConfiguration() {
        // 测试带配置的构造函数
        FeishuConfiguration config = new FeishuConfiguration("test_app_id", "test_app_secret");
        FeishuMeetingTool tool = new FeishuMeetingTool(config);
        
        assertThat(tool.getConfiguration()).isEqualTo(config);
        assertThat(tool.getName()).isEqualTo("feishu_meeting_operation");
    }

    @Test
    void testExecuteWithMissingOperation() {
        // 测试缺少操作类型
        Map<String, Object> input = new HashMap<>();
        input.put("topic", "Test Meeting");
        
        String inputJson = JSON.toJSONString(input);
        
        ToolExecuteResult result = meetingTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("operation 参数不能为空");
    }

    @Test
    void testExecuteWithUnsupportedOperation() {
        // 测试不支持的操作类型
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "delete");

        String inputJson = JSON.toJSONString(input);

        ToolExecuteResult result = meetingTool.execute(inputJson);

        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("不支持的操作类型：delete");
    }

    @Test
    void testCreateMeetingSuccess() throws Exception {
        // 测试成功创建会议
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "create");
        input.put("topic", "Test Meeting");
        input.put("start_time", "2024-12-01 10:00:00");
        input.put("duration", 60);
        input.put("description", "This is a test meeting");
        input.put("invitees", Arrays.asList("user1@example.com", "user2@example.com"));
        input.put("need_password", true);
        input.put("auto_record", false);
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"reserve\":{\"id\":\"meeting_123\",\"meeting_url\":\"https://meeting.feishu.cn/123\",\"join_url\":\"https://meeting.feishu.cn/join/123\"}}}");
        
        ToolExecuteResult result = meetingTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("会议创建成功");
        assertThat(result.getOutput()).contains("meeting_123");
        assertThat(result.getOutput()).contains("meeting_url");
        
        // 验证方法调用
        verify(mockFeishuClient).getTenantAccessToken();
        verify(mockFeishuClient).doPost(anyString(), anyString(), eq("mock_token"));
    }

    @Test
    void testCreateMeetingWithMissingTopic() {
        // 测试创建会议时缺少主题
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "create");
        input.put("start_time", "2024-12-01 10:00:00");
        
        String inputJson = JSON.toJSONString(input);
        
        ToolExecuteResult result = meetingTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("topic 参数不能为空");
    }

    @Test
    void testCreateMeetingWithMissingStartTime() {
        // 测试创建会议时缺少开始时间
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "create");
        input.put("topic", "Test Meeting");

        String inputJson = JSON.toJSONString(input);

        ToolExecuteResult result = meetingTool.execute(inputJson);

        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("start_time 参数不能为空");
    }

    @Test
    void testCreateMeetingWithDefaultDuration() throws Exception {
        // 测试创建会议时使用默认时长
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "create");
        input.put("topic", "Test Meeting");
        input.put("start_time", "2024-12-01 10:00:00");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"reserve\":{\"id\":\"meeting_456\",\"meeting_url\":\"https://meeting.feishu.cn/456\",\"join_url\":\"https://meeting.feishu.cn/join/456\"}}}");
        
        ToolExecuteResult result = meetingTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("duration\":60"); // 默认60分钟
    }

    @Test
    void testGetMeetingSuccess() throws Exception {
        // 测试成功获取会议信息
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "get");
        input.put("reserve_id", "meeting_123");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doGet(anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"topic\":\"Test Meeting\",\"start_time\":1701406800}}");
        
        ToolExecuteResult result = meetingTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("会议信息获取成功");
        assertThat(result.getOutput()).contains("meeting_123");
        
        // 验证方法调用
        verify(mockFeishuClient).getTenantAccessToken();
        verify(mockFeishuClient).doGet(contains("meeting_123"), eq("mock_token"));
    }

    @Test
    void testGetMeetingWithMissingReserveId() {
        // 测试获取会议时缺少预约ID
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "get");
        
        String inputJson = JSON.toJSONString(input);
        
        ToolExecuteResult result = meetingTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("reserve_id 参数不能为空");
    }

    @Test
    void testQueryMeetingSuccess() throws Exception {
        // 测试使用query操作获取会议信息
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "query");
        input.put("reserve_id", "meeting_789");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doGet(anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"topic\":\"Query Meeting\",\"start_time\":1701406800}}");
        
        ToolExecuteResult result = meetingTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("会议信息获取成功");
    }

    @Test
    void testListMeetingsSuccess() throws Exception {
        // 测试成功获取会议列表
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "list");
        input.put("page_size", 10);
        input.put("page_token", "token_123");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doGet(anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"reserves\":[{\"id\":\"meeting_1\"},{\"id\":\"meeting_2\"}],\"has_more\":false,\"page_token\":\"\"}}");
        
        ToolExecuteResult result = meetingTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("会议列表获取成功");
        assertThat(result.getOutput()).contains("has_more");
        
        // 验证方法调用
        verify(mockFeishuClient).getTenantAccessToken();
        verify(mockFeishuClient).doGet(contains("page_size=10"), eq("mock_token"));
        verify(mockFeishuClient).doGet(contains("page_token=token_123"), eq("mock_token"));
    }

    @Test
    void testListMeetingsWithDefaultParameters() throws Exception {
        // 测试使用默认参数获取会议列表
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "list");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doGet(anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"reserves\":[],\"has_more\":false,\"page_token\":\"\"}}");
        
        ToolExecuteResult result = meetingTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();

        // 验证使用了默认的page_size
        verify(mockFeishuClient).doGet(contains("page_size=20"), eq("mock_token"));
    }

    @Test
    void testExecuteWithFeishuException() throws Exception {
        // 测试飞书API异常
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "get");
        input.put("reserve_id", "meeting_123");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端抛出异常
        when(mockFeishuClient.getTenantAccessToken()).thenThrow(new FeishuException(1001, "Invalid token"));
        
        ToolExecuteResult result = meetingTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("会议操作失败");
        assertThat(result.getOutput()).contains("Invalid token");
    }

    @Test
    void testExecuteWithApiError() throws Exception {
        // 测试API返回错误
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "create");
        input.put("topic", "Test Meeting");
        input.put("start_time", "2024-12-01 10:00:00");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":1002,\"msg\":\"Permission denied\"}");
        
        ToolExecuteResult result = meetingTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("会议操作失败");
        assertThat(result.getOutput()).contains("Permission denied");
    }

    @Test
    void testExecuteWithInvalidJson() {
        // 测试无效的JSON输入
        String invalidJson = "invalid json";
        
        ToolExecuteResult result = meetingTool.execute(invalidJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("发生未知错误");
    }

    @Test
    void testParseDateTimeWithDifferentFormats() throws Exception {
        // 测试不同的日期时间格式
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "create");
        input.put("topic", "Test Meeting");
        input.put("start_time", "2024/12/01 10:00:00"); // 不同格式
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"reserve\":{\"id\":\"meeting_format\",\"meeting_url\":\"https://meeting.feishu.cn/format\",\"join_url\":\"https://meeting.feishu.cn/join/format\"}}}");
        
        ToolExecuteResult result = meetingTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
    }

    @Test
    void testParseDateTimeWithTimestamp() throws Exception {
        // 测试时间戳格式
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "create");
        input.put("topic", "Test Meeting");
        input.put("start_time", "1701406800"); // 时间戳
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"reserve\":{\"id\":\"meeting_timestamp\",\"meeting_url\":\"https://meeting.feishu.cn/timestamp\",\"join_url\":\"https://meeting.feishu.cn/join/timestamp\"}}}");
        
        ToolExecuteResult result = meetingTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
    }

    @Test
    void testSetConfiguration() {
        // 测试设置配置
        FeishuConfiguration newConfig = new FeishuConfiguration("new_app_id", "new_app_secret");
        
        meetingTool.setConfiguration(newConfig);
        
        assertThat(meetingTool.getConfiguration()).isEqualTo(newConfig);
    }

    @Test
    void testToolMetadata() {
        // 测试工具元数据
        assertThat(meetingTool.getName()).isEqualTo("feishu_meeting_operation");
        assertThat(meetingTool.getDescription()).isNotEmpty();
        assertThat(meetingTool.getStructuredSchema()).isNotNull();
        assertThat(meetingTool.getStructuredSchema().getParameters()).isNotEmpty();
    }

    @Test
    void testCreateMeetingWithInvalidDuration() throws Exception {
        // 测试创建会议时使用无效时长（会被修正为默认值）
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "create");
        input.put("topic", "Test Meeting");
        input.put("start_time", "2024-12-01 10:00:00");
        input.put("duration", -10); // 无效时长
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"reserve\":{\"id\":\"meeting_invalid_duration\",\"meeting_url\":\"https://meeting.feishu.cn/invalid\",\"join_url\":\"https://meeting.feishu.cn/join/invalid\"}}}");
        
        ToolExecuteResult result = meetingTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("duration\":60"); // 应该使用默认值60
    }
}
