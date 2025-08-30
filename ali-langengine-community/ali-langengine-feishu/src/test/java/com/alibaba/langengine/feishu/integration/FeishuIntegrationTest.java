package com.alibaba.langengine.feishu.integration;

import com.alibaba.fastjson.JSON;
import com.alibaba.langengine.core.tool.ToolExecuteResult;
import com.alibaba.langengine.feishu.FeishuConfiguration;
import com.alibaba.langengine.feishu.tools.FeishuContactTool;
import com.alibaba.langengine.feishu.tools.FeishuDocumentTool;
import com.alibaba.langengine.feishu.tools.FeishuMeetingTool;
import com.alibaba.langengine.feishu.tools.FeishuMessageTool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class FeishuIntegrationTest {

    private FeishuConfiguration configuration;
    private FeishuMessageTool messageTool;
    private FeishuDocumentTool documentTool;
    private FeishuMeetingTool meetingTool;
    private FeishuContactTool contactTool;

    @BeforeEach
    void setUp() {
        // 使用测试配置
        configuration = new FeishuConfiguration("test_app_id", "test_app_secret");
        
        // 初始化所有工具
        messageTool = new FeishuMessageTool(configuration);
        documentTool = new FeishuDocumentTool(configuration);
        meetingTool = new FeishuMeetingTool(configuration);
        contactTool = new FeishuContactTool(configuration);
    }

    @Test
    void testAllToolsInitialization() {
        // 测试所有工具的初始化
        assertThat(messageTool).isNotNull();
        assertThat(messageTool.getName()).isEqualTo("feishu_send_message");
        assertThat(messageTool.getConfiguration()).isEqualTo(configuration);
        
        assertThat(documentTool).isNotNull();
        assertThat(documentTool.getName()).isEqualTo("feishu_document_operation");
        assertThat(documentTool.getConfiguration()).isEqualTo(configuration);
        
        assertThat(meetingTool).isNotNull();
        assertThat(meetingTool.getName()).isEqualTo("feishu_meeting_operation");
        assertThat(meetingTool.getConfiguration()).isEqualTo(configuration);
        
        assertThat(contactTool).isNotNull();
        assertThat(contactTool.getName()).isEqualTo("feishu_contact_query");
        assertThat(contactTool.getConfiguration()).isEqualTo(configuration);
    }

    @Test
    void testToolsWithInvalidConfiguration() {
        // 测试使用无效配置的工具
        FeishuConfiguration invalidConfig = new FeishuConfiguration();
        invalidConfig.setAppId("");
        invalidConfig.setAppSecret("");

        // 现在构造函数会验证配置，应该抛出异常
        assertThatThrownBy(() -> new FeishuMessageTool(invalidConfig))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid FeishuConfiguration");
    }

    @Test
    void testMessageToolParameterValidation() {
        // 测试消息工具的参数验证
        Map<String, Object> input = new HashMap<>();
        input.put("receive_id", "test_user");
        input.put("content", "Hello from integration test");
        
        String inputJson = JSON.toJSONString(input);
        
        // 由于没有真实的服务器，这里主要测试参数验证逻辑
        ToolExecuteResult result = messageTool.execute(inputJson);
        
        // 应该失败，但不是因为参数验证问题
        assertThat(result.isInterrupted()).isTrue();
        // 错误应该是网络或认证相关，而不是参数验证
        assertThat(result.getOutput()).doesNotContain("参数不能为空");
    }

    @Test
    void testDocumentToolParameterValidation() {
        // 测试文档工具的参数验证
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "create");
        input.put("title", "Integration Test Document");
        
        String inputJson = JSON.toJSONString(input);
        
        ToolExecuteResult result = documentTool.execute(inputJson);
        
        // 应该失败，但不是因为参数验证问题
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).doesNotContain("参数不能为空");
    }

    @Test
    void testMeetingToolParameterValidation() {
        // 测试会议工具的参数验证
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "create");
        input.put("topic", "Integration Test Meeting");
        input.put("start_time", "2024-12-01 10:00:00");
        
        String inputJson = JSON.toJSONString(input);
        
        ToolExecuteResult result = meetingTool.execute(inputJson);
        
        // 应该失败，但不是因为参数验证问题
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).doesNotContain("参数不能为空");
    }

    @Test
    void testContactToolParameterValidation() {
        // 测试通讯录工具的参数验证
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "search_user");
        input.put("emails", Arrays.asList("test@example.com"));
        
        String inputJson = JSON.toJSONString(input);
        
        ToolExecuteResult result = contactTool.execute(inputJson);
        
        // 应该失败，但不是因为参数验证问题
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).doesNotContain("参数不能为空");
    }

    @Test
    void testToolsWithSameConfiguration() {
        // 测试所有工具使用相同配置
        FeishuConfiguration sharedConfig = new FeishuConfiguration("shared_app_id", "shared_app_secret");
        
        FeishuMessageTool sharedMessageTool = new FeishuMessageTool(sharedConfig);
        FeishuDocumentTool sharedDocumentTool = new FeishuDocumentTool(sharedConfig);
        FeishuMeetingTool sharedMeetingTool = new FeishuMeetingTool(sharedConfig);
        FeishuContactTool sharedContactTool = new FeishuContactTool(sharedConfig);
        
        assertThat(sharedMessageTool.getConfiguration()).isEqualTo(sharedConfig);
        assertThat(sharedDocumentTool.getConfiguration()).isEqualTo(sharedConfig);
        assertThat(sharedMeetingTool.getConfiguration()).isEqualTo(sharedConfig);
        assertThat(sharedContactTool.getConfiguration()).isEqualTo(sharedConfig);
    }

    @Test
    void testToolsWithDifferentConfigurations() {
        // 测试工具使用不同配置
        FeishuConfiguration config1 = new FeishuConfiguration("app1", "secret1");
        FeishuConfiguration config2 = new FeishuConfiguration("app2", "secret2");
        
        FeishuMessageTool tool1 = new FeishuMessageTool(config1);
        FeishuMessageTool tool2 = new FeishuMessageTool(config2);
        
        assertThat(tool1.getConfiguration()).isEqualTo(config1);
        assertThat(tool2.getConfiguration()).isEqualTo(config2);
        assertThat(tool1.getConfiguration()).isNotEqualTo(tool2.getConfiguration());
    }

    @Test
    void testConfigurationUpdate() {
        // 测试配置更新
        FeishuConfiguration newConfig = new FeishuConfiguration("new_app_id", "new_app_secret");
        
        messageTool.setConfiguration(newConfig);
        documentTool.setConfiguration(newConfig);
        meetingTool.setConfiguration(newConfig);
        contactTool.setConfiguration(newConfig);
        
        assertThat(messageTool.getConfiguration()).isEqualTo(newConfig);
        assertThat(documentTool.getConfiguration()).isEqualTo(newConfig);
        assertThat(meetingTool.getConfiguration()).isEqualTo(newConfig);
        assertThat(contactTool.getConfiguration()).isEqualTo(newConfig);
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "FEISHU_INTEGRATION_TEST", matches = "true")
    void testRealApiIntegration() {
        // 这个测试只在设置了环境变量时运行，用于真实API测试
        // 需要设置真实的FEISHU_APP_ID和FEISHU_APP_SECRET环境变量
        
        FeishuConfiguration realConfig = new FeishuConfiguration();
        if (!realConfig.isValid()) {
            // 跳过测试，因为没有有效的配置
            return;
        }
        
        FeishuMessageTool realMessageTool = new FeishuMessageTool(realConfig);
        
        // 这里可以添加真实的API调用测试
        // 注意：需要谨慎使用，避免发送垃圾消息
        assertThat(realMessageTool).isNotNull();
    }

    @Test
    void testToolSchemaValidation() {
        // 测试所有工具的Schema验证
        assertThat(messageTool.getStructuredSchema().getParameters()).isNotEmpty();
        assertThat(documentTool.getStructuredSchema().getParameters()).isNotEmpty();
        assertThat(meetingTool.getStructuredSchema().getParameters()).isNotEmpty();
        assertThat(contactTool.getStructuredSchema().getParameters()).isNotEmpty();
        
        // 验证每个工具都有必需的参数
        boolean messageToolHasRequiredParams = messageTool.getStructuredSchema().getParameters().stream()
                .anyMatch(param -> Boolean.TRUE.equals(param.getRequired()));
        boolean documentToolHasRequiredParams = documentTool.getStructuredSchema().getParameters().stream()
                .anyMatch(param -> Boolean.TRUE.equals(param.getRequired()));
        boolean meetingToolHasRequiredParams = meetingTool.getStructuredSchema().getParameters().stream()
                .anyMatch(param -> Boolean.TRUE.equals(param.getRequired()));
        boolean contactToolHasRequiredParams = contactTool.getStructuredSchema().getParameters().stream()
                .anyMatch(param -> Boolean.TRUE.equals(param.getRequired()));
        
        assertThat(messageToolHasRequiredParams).isTrue();
        assertThat(documentToolHasRequiredParams).isTrue();
        assertThat(meetingToolHasRequiredParams).isTrue();
        assertThat(contactToolHasRequiredParams).isTrue();
    }
}
