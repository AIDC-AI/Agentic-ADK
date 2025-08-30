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

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FeishuMessageToolTest {

    @Mock
    private FeishuClient mockFeishuClient;

    private FeishuMessageTool messageTool;

    @BeforeEach
    void setUp() {
        // 创建真实的配置对象而不是mock，因为构造函数中有验证逻辑
        FeishuConfiguration realConfiguration = new FeishuConfiguration();
        realConfiguration.setAppId("test_app_id");
        realConfiguration.setAppSecret("test_app_secret");

        messageTool = new FeishuMessageTool();
        messageTool.setFeishuClient(mockFeishuClient);
        messageTool.setConfiguration(realConfiguration);
    }

    @Test
    void testDefaultConstructor() {
        // 测试默认构造函数
        FeishuMessageTool tool = new FeishuMessageTool();
        
        assertThat(tool.getName()).isEqualTo("feishu_send_message");
        assertThat(tool.getDescription()).contains("发送消息到飞书");
        assertThat(tool.getStructuredSchema()).isNotNull();
    }

    @Test
    void testConstructorWithConfiguration() {
        // 测试带配置的构造函数
        FeishuConfiguration config = new FeishuConfiguration("test_app_id", "test_app_secret");
        FeishuMessageTool tool = new FeishuMessageTool(config);
        
        assertThat(tool.getConfiguration()).isEqualTo(config);
        assertThat(tool.getName()).isEqualTo("feishu_send_message");
    }

    @Test
    void testExecuteWithValidTextMessage() throws Exception {
        // 测试发送有效的文本消息
        Map<String, Object> input = new HashMap<>();
        input.put("receive_id", "test_user_id");
        input.put("receive_id_type", "open_id");
        input.put("msg_type", "text");
        input.put("content", "Hello, World!");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"message_id\":\"msg_123\"}}");
        
        ToolExecuteResult result = messageTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("消息发送成功");
        assertThat(result.getOutput()).contains("msg_123");
        
        // 验证方法调用
        verify(mockFeishuClient).getTenantAccessToken();
        verify(mockFeishuClient).doPost(anyString(), anyString(), eq("mock_token"));
    }

    @Test
    void testExecuteWithMissingReceiveId() {
        // 测试缺少接收者ID
        Map<String, Object> input = new HashMap<>();
        input.put("msg_type", "text");
        input.put("content", "Hello, World!");
        
        String inputJson = JSON.toJSONString(input);
        
        ToolExecuteResult result = messageTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("receive_id 参数不能为空");
    }

    @Test
    void testExecuteWithEmptyReceiveId() {
        // 测试空的接收者ID
        Map<String, Object> input = new HashMap<>();
        input.put("receive_id", "");
        input.put("msg_type", "text");
        input.put("content", "Hello, World!");
        
        String inputJson = JSON.toJSONString(input);
        
        ToolExecuteResult result = messageTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("receive_id 参数不能为空");
    }

    @Test
    void testExecuteWithMissingContent() {
        // 测试缺少内容
        Map<String, Object> input = new HashMap<>();
        input.put("receive_id", "test_user_id");
        input.put("msg_type", "text");
        
        String inputJson = JSON.toJSONString(input);
        
        ToolExecuteResult result = messageTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("content 参数不能为空");
    }

    @Test
    void testExecuteWithDefaultParameters() throws Exception {
        // 测试使用默认参数
        Map<String, Object> input = new HashMap<>();
        input.put("receive_id", "test_user_id");
        input.put("content", "Hello, World!");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"message_id\":\"msg_123\"}}");
        
        ToolExecuteResult result = messageTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        
        // 验证使用了默认的receive_id_type和msg_type
        verify(mockFeishuClient).doPost(contains("receive_id_type=open_id"), anyString(), anyString());
    }

    @Test
    void testExecuteWithPostMessage() throws Exception {
        // 测试发送富文本消息
        Map<String, Object> input = new HashMap<>();
        input.put("receive_id", "test_user_id");
        input.put("receive_id_type", "open_id");
        input.put("msg_type", "post");
        
        Map<String, Object> postContent = new HashMap<>();
        Map<String, Object> zhCn = new HashMap<>();
        zhCn.put("title", "测试标题");
        zhCn.put("content", new Object[][]{{Map.of("tag", "text", "text", "测试内容")}});
        postContent.put("zh_cn", zhCn);
        input.put("content", postContent);
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"message_id\":\"msg_456\"}}");
        
        ToolExecuteResult result = messageTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("msg_456");
    }

    @Test
    void testExecuteWithInteractiveMessage() throws Exception {
        // 测试发送交互式卡片消息
        Map<String, Object> input = new HashMap<>();
        input.put("receive_id", "test_user_id");
        input.put("msg_type", "interactive");
        
        Map<String, Object> cardContent = new HashMap<>();
        cardContent.put("elements", new Object[]{
            Map.of("tag", "div", "text", Map.of("content", "卡片内容", "tag", "lark_md"))
        });
        input.put("content", cardContent);
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"message_id\":\"msg_789\"}}");
        
        ToolExecuteResult result = messageTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("msg_789");
    }

    @Test
    void testExecuteWithFeishuException() throws Exception {
        // 测试飞书API异常
        Map<String, Object> input = new HashMap<>();
        input.put("receive_id", "test_user_id");
        input.put("content", "Hello, World!");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端抛出异常
        when(mockFeishuClient.getTenantAccessToken()).thenThrow(new FeishuException(1001, "invalid param"));
        
        ToolExecuteResult result = messageTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("发送消息失败");
        assertThat(result.getOutput()).contains("invalid param");
    }

    @Test
    void testExecuteWithApiError() throws Exception {
        // 测试API返回错误
        Map<String, Object> input = new HashMap<>();
        input.put("receive_id", "test_user_id");
        input.put("content", "Hello, World!");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":1002,\"msg\":\"Permission denied\"}");
        
        ToolExecuteResult result = messageTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("发送消息失败");
        assertThat(result.getOutput()).contains("Permission denied");
    }

    @Test
    void testExecuteWithInvalidJson() {
        // 测试无效的JSON输入
        String invalidJson = "invalid json";
        
        ToolExecuteResult result = messageTool.execute(invalidJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("发生未知错误");
    }

    @Test
    void testExecuteWithStringContent() throws Exception {
        // 测试字符串内容
        Map<String, Object> input = new HashMap<>();
        input.put("receive_id", "test_user_id");
        input.put("msg_type", "text");
        input.put("content", "Simple text message");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"message_id\":\"msg_string\"}}");
        
        ToolExecuteResult result = messageTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("msg_string");
    }

    @Test
    void testSetConfiguration() {
        // 测试设置配置
        FeishuConfiguration newConfig = new FeishuConfiguration("new_app_id", "new_app_secret");
        
        messageTool.setConfiguration(newConfig);
        
        assertThat(messageTool.getConfiguration()).isEqualTo(newConfig);
        // 注意：由于setConfiguration会创建新的FeishuClient，这里无法直接验证
        // 在实际使用中，新的客户端会使用新的配置
    }

    @Test
    void testProcessMessageContentWithTextMessage() {
        // 由于processMessageContent是私有方法，我们通过execute方法间接测试
        Map<String, Object> input = new HashMap<>();
        input.put("receive_id", "test_user_id");
        input.put("msg_type", "text");
        input.put("content", "Test text");
        
        String inputJson = JSON.toJSONString(input);
        
        // 这里主要测试方法不会抛出异常
        assertThatCode(() -> messageTool.execute(inputJson)).doesNotThrowAnyException();
    }

    @Test
    void testProcessMessageContentWithMapContent() throws Exception {
        // 测试Map类型的内容
        Map<String, Object> input = new HashMap<>();
        input.put("receive_id", "test_user_id");
        input.put("msg_type", "text");
        
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("text", "Map content");
        input.put("content", contentMap);
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"message_id\":\"msg_map\"}}");
        
        ToolExecuteResult result = messageTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
    }

    @Test
    void testToolMetadata() {
        // 测试工具元数据
        assertThat(messageTool.getName()).isEqualTo("feishu_send_message");
        assertThat(messageTool.getDescription()).isNotEmpty();
        assertThat(messageTool.getStructuredSchema()).isNotNull();
        assertThat(messageTool.getStructuredSchema().getParameters()).isNotEmpty();
    }
}
