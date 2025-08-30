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
class FeishuDocumentToolTest {

    @Mock
    private FeishuClient mockFeishuClient;

    private FeishuDocumentTool documentTool;

    @BeforeEach
    void setUp() {
        // 创建真实的配置对象而不是mock，因为构造函数中有验证逻辑
        FeishuConfiguration realConfiguration = new FeishuConfiguration();
        realConfiguration.setAppId("test_app_id");
        realConfiguration.setAppSecret("test_app_secret");

        documentTool = new FeishuDocumentTool();
        documentTool.setFeishuClient(mockFeishuClient);
        documentTool.setConfiguration(realConfiguration);
    }

    @Test
    void testDefaultConstructor() {
        // 测试默认构造函数
        FeishuDocumentTool tool = new FeishuDocumentTool();
        
        assertThat(tool.getName()).isEqualTo("feishu_document_operation");
        assertThat(tool.getDescription()).contains("飞书文档操作工具");
        assertThat(tool.getStructuredSchema()).isNotNull();
    }

    @Test
    void testConstructorWithConfiguration() {
        // 测试带配置的构造函数
        FeishuConfiguration config = new FeishuConfiguration("test_app_id", "test_app_secret");
        FeishuDocumentTool tool = new FeishuDocumentTool(config);
        
        assertThat(tool.getConfiguration()).isEqualTo(config);
        assertThat(tool.getName()).isEqualTo("feishu_document_operation");
    }

    @Test
    void testExecuteWithMissingOperation() {
        // 测试缺少操作类型
        Map<String, Object> input = new HashMap<>();
        input.put("title", "Test Document");
        
        String inputJson = JSON.toJSONString(input);
        
        ToolExecuteResult result = documentTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("operation 参数不能为空");
    }

    @Test
    void testExecuteWithUnsupportedOperation() {
        // 测试不支持的操作类型
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "delete");

        String inputJson = JSON.toJSONString(input);

        ToolExecuteResult result = documentTool.execute(inputJson);

        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("不支持的操作类型：delete");
    }

    @Test
    void testCreateDocumentSuccess() throws Exception {
        // 测试成功创建文档
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "create");
        input.put("title", "Test Document");
        input.put("content", "This is test content");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"document\":{\"document_id\":\"doc_123\",\"url\":\"https://example.com/doc\"}}}");
        when(mockFeishuClient.doPut(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\"}");
        
        ToolExecuteResult result = documentTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("文档创建成功");
        assertThat(result.getOutput()).contains("doc_123");
        
        // 验证方法调用
        verify(mockFeishuClient).getTenantAccessToken();
        verify(mockFeishuClient).doPost(anyString(), anyString(), eq("mock_token"));
        verify(mockFeishuClient).doPut(anyString(), anyString(), eq("mock_token")); // 更新内容
    }

    @Test
    void testCreateDocumentWithMissingTitle() {
        // 测试创建文档时缺少标题
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "create");
        input.put("content", "This is test content");
        
        String inputJson = JSON.toJSONString(input);
        
        ToolExecuteResult result = documentTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("title 参数不能为空");
    }

    @Test
    void testCreateDocumentWithFolderId() throws Exception {
        // 测试在指定文件夹中创建文档
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "create");
        input.put("title", "Test Document");
        input.put("folder_id", "folder_123");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"document\":{\"document_id\":\"doc_456\",\"url\":\"https://example.com/doc\"}}}");
        
        ToolExecuteResult result = documentTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("doc_456");
    }

    @Test
    void testReadDocumentSuccess() throws Exception {
        // 测试成功读取文档
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "read");
        input.put("document_id", "doc_123");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doGet(anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"content\":\"Document content\"}}");
        
        ToolExecuteResult result = documentTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("文档读取成功");
        assertThat(result.getOutput()).contains("doc_123");
        
        // 验证方法调用
        verify(mockFeishuClient).getTenantAccessToken();
        verify(mockFeishuClient).doGet(contains("doc_123"), eq("mock_token"));
    }

    @Test
    void testReadDocumentWithMissingDocumentId() {
        // 测试读取文档时缺少文档ID
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "read");
        
        String inputJson = JSON.toJSONString(input);
        
        ToolExecuteResult result = documentTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("document_id 参数不能为空");
    }

    @Test
    void testReadDocumentWithGetOperation() throws Exception {
        // 测试使用get操作读取文档
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "get");
        input.put("document_id", "doc_789");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doGet(anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"content\":\"Document content\"}}");
        
        ToolExecuteResult result = documentTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("文档读取成功");
    }

    @Test
    void testUpdateDocumentSuccess() throws Exception {
        // 测试成功更新文档
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "update");
        input.put("document_id", "doc_123");
        input.put("content", "Updated content");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPut(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\"}");
        
        ToolExecuteResult result = documentTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("文档更新成功");
        assertThat(result.getOutput()).contains("doc_123");
        
        // 验证方法调用
        verify(mockFeishuClient).getTenantAccessToken();
        verify(mockFeishuClient).doPut(contains("doc_123"), anyString(), eq("mock_token"));
    }

    @Test
    void testUpdateDocumentWithMissingDocumentId() {
        // 测试更新文档时缺少文档ID
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "update");
        input.put("content", "Updated content");
        
        String inputJson = JSON.toJSONString(input);
        
        ToolExecuteResult result = documentTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("document_id 参数不能为空");
    }

    @Test
    void testUpdateDocumentWithMissingContent() {
        // 测试更新文档时缺少内容
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "update");
        input.put("document_id", "doc_123");
        
        String inputJson = JSON.toJSONString(input);
        
        ToolExecuteResult result = documentTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("content 参数不能为空");
    }

    @Test
    void testExecuteWithFeishuException() throws Exception {
        // 测试飞书API异常
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "read");
        input.put("document_id", "doc_123");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端抛出异常
        when(mockFeishuClient.getTenantAccessToken()).thenThrow(new FeishuException(1001, "Invalid token"));
        
        ToolExecuteResult result = documentTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("文档操作失败");
        assertThat(result.getOutput()).contains("Invalid token");
    }

    @Test
    void testExecuteWithApiError() throws Exception {
        // 测试API返回错误
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "create");
        input.put("title", "Test Document");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":1002,\"msg\":\"Permission denied\"}");
        
        ToolExecuteResult result = documentTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("文档操作失败");
        assertThat(result.getOutput()).contains("Permission denied");
    }

    @Test
    void testExecuteWithInvalidJson() {
        // 测试无效的JSON输入
        String invalidJson = "invalid json";
        
        ToolExecuteResult result = documentTool.execute(invalidJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("发生未知错误");
    }

    @Test
    void testSetConfiguration() {
        // 测试设置配置
        FeishuConfiguration newConfig = new FeishuConfiguration("new_app_id", "new_app_secret");
        
        documentTool.setConfiguration(newConfig);
        
        assertThat(documentTool.getConfiguration()).isEqualTo(newConfig);
    }

    @Test
    void testToolMetadata() {
        // 测试工具元数据
        assertThat(documentTool.getName()).isEqualTo("feishu_document_operation");
        assertThat(documentTool.getDescription()).isNotEmpty();
        assertThat(documentTool.getStructuredSchema()).isNotNull();
        assertThat(documentTool.getStructuredSchema().getParameters()).isNotEmpty();
    }

    @Test
    void testCreateDocumentWithoutContent() throws Exception {
        // 测试创建文档但不添加内容
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "create");
        input.put("title", "Empty Document");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"document\":{\"document_id\":\"doc_empty\",\"url\":\"https://example.com/doc\"}}}");
        
        ToolExecuteResult result = documentTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isFalse();
        assertThat(result.getOutput()).contains("doc_empty");
        
        // 验证没有调用更新内容的方法
        verify(mockFeishuClient, never()).doPut(anyString(), anyString(), anyString());
    }

    @Test
    void testUpdateDocumentContentError() throws Exception {
        // 测试更新文档内容时出错
        Map<String, Object> input = new HashMap<>();
        input.put("operation", "create");
        input.put("title", "Test Document");
        input.put("content", "Test content");
        
        String inputJson = JSON.toJSONString(input);
        
        // Mock客户端行为
        when(mockFeishuClient.getTenantAccessToken()).thenReturn("mock_token");
        when(mockFeishuClient.doPost(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":0,\"msg\":\"success\",\"data\":{\"document\":{\"document_id\":\"doc_123\",\"url\":\"https://example.com/doc\"}}}");
        when(mockFeishuClient.doPut(anyString(), anyString(), anyString()))
                .thenReturn("{\"code\":1003,\"msg\":\"Update failed\"}");
        
        ToolExecuteResult result = documentTool.execute(inputJson);
        
        assertThat(result.isInterrupted()).isTrue();
        assertThat(result.getOutput()).contains("文档操作失败");
        assertThat(result.getOutput()).contains("Update failed");
    }
}
