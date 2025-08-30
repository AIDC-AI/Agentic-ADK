package com.alibaba.langengine.feishu.sdk;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class FeishuExceptionTest {

    @Test
    void testConstructorWithMessage() {
        // 测试只有消息的构造函数
        String message = "Test error message";
        FeishuException exception = new FeishuException(message);
        
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCode()).isEqualTo(-1);
        assertThat(exception.getRequestId()).isNull();
    }

    @Test
    void testConstructorWithCodeAndMessage() {
        // 测试带错误码和消息的构造函数
        int code = 1001;
        String message = "Invalid parameter";
        FeishuException exception = new FeishuException(code, message);
        
        assertThat(exception.getCode()).isEqualTo(code);
        assertThat(exception.getMessage()).isEqualTo("Feishu API Error [1001]: Invalid parameter");
        assertThat(exception.getRequestId()).isNull();
    }

    @Test
    void testConstructorWithCodeMessageAndRequestId() {
        // 测试带错误码、消息和请求ID的构造函数
        int code = 1002;
        String message = "Permission denied";
        String requestId = "req-12345";
        FeishuException exception = new FeishuException(code, message, requestId);
        
        assertThat(exception.getCode()).isEqualTo(code);
        assertThat(exception.getMessage()).isEqualTo("Feishu API Error [1002]: Permission denied (RequestId: req-12345)");
        assertThat(exception.getRequestId()).isEqualTo(requestId);
    }

    @Test
    void testConstructorWithMessageAndCause() {
        // 测试带消息和原因异常的构造函数
        String message = "Network error";
        Exception cause = new RuntimeException("Connection timeout");
        FeishuException exception = new FeishuException(message, cause);
        
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
        assertThat(exception.getCode()).isEqualTo(-1);
        assertThat(exception.getRequestId()).isNull();
    }

    @Test
    void testConstructorWithCodeMessageAndCause() {
        // 测试带错误码、消息和原因异常的构造函数
        int code = 500;
        String message = "Internal server error";
        Exception cause = new RuntimeException("Database connection failed");
        FeishuException exception = new FeishuException(code, message, cause);
        
        assertThat(exception.getCode()).isEqualTo(code);
        assertThat(exception.getMessage()).isEqualTo("Feishu API Error [500]: Internal server error");
        assertThat(exception.getCause()).isEqualTo(cause);
        assertThat(exception.getRequestId()).isNull();
    }

    @Test
    void testConstructorWithAllParameters() {
        // 测试带所有参数的构造函数
        int code = 404;
        String message = "Resource not found";
        String requestId = "req-67890";
        Exception cause = new RuntimeException("HTTP 404");
        FeishuException exception = new FeishuException(code, message, requestId, cause);
        
        assertThat(exception.getCode()).isEqualTo(code);
        assertThat(exception.getMessage()).isEqualTo("Feishu API Error [404]: Resource not found (RequestId: req-67890)");
        assertThat(exception.getRequestId()).isEqualTo(requestId);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    void testIsTokenErrorWithInvalidToken() {
        // 测试无效令牌错误
        FeishuException exception = new FeishuException(FeishuConstant.CODE_INVALID_TOKEN, "Invalid token");
        
        assertThat(exception.isTokenError()).isTrue();
        assertThat(exception.isPermissionError()).isFalse();
        assertThat(exception.isParameterError()).isFalse();
    }

    @Test
    void testIsTokenErrorWithExpiredToken() {
        // 测试过期令牌错误
        FeishuException exception = new FeishuException(FeishuConstant.CODE_TOKEN_EXPIRED, "Token expired");
        
        assertThat(exception.isTokenError()).isTrue();
        assertThat(exception.isPermissionError()).isFalse();
        assertThat(exception.isParameterError()).isFalse();
    }

    @Test
    void testIsPermissionError() {
        // 测试权限错误
        FeishuException exception = new FeishuException(FeishuConstant.CODE_PERMISSION_DENIED, "Permission denied");
        
        assertThat(exception.isPermissionError()).isTrue();
        assertThat(exception.isTokenError()).isFalse();
        assertThat(exception.isParameterError()).isFalse();
    }

    @Test
    void testIsParameterError() {
        // 测试参数错误
        FeishuException exception = new FeishuException(FeishuConstant.CODE_INVALID_PARAM, "Invalid parameter");
        
        assertThat(exception.isParameterError()).isTrue();
        assertThat(exception.isTokenError()).isFalse();
        assertThat(exception.isPermissionError()).isFalse();
    }

    @Test
    void testIsRetryableWithTokenError() {
        // 测试令牌错误是否可重试
        FeishuException exception = new FeishuException(FeishuConstant.CODE_INVALID_TOKEN, "Invalid token");
        
        assertThat(exception.isRetryable()).isTrue();
    }

    @Test
    void testIsRetryableWithNetworkError() {
        // 测试网络错误是否可重试
        FeishuException exception = new FeishuException("Network error");
        
        assertThat(exception.isRetryable()).isTrue();
    }

    @Test
    void testIsRetryableWithPermissionError() {
        // 测试权限错误是否可重试
        FeishuException exception = new FeishuException(FeishuConstant.CODE_PERMISSION_DENIED, "Permission denied");
        
        assertThat(exception.isRetryable()).isFalse();
    }

    @Test
    void testIsRetryableWithParameterError() {
        // 测试参数错误是否可重试
        FeishuException exception = new FeishuException(FeishuConstant.CODE_INVALID_PARAM, "Invalid parameter");
        
        assertThat(exception.isRetryable()).isFalse();
    }

    @Test
    void testToString() {
        // 测试toString方法
        int code = 1001;
        String message = "Test error";
        String requestId = "req-test";
        FeishuException exception = new FeishuException(code, message, requestId);
        
        String result = exception.toString();
        
        assertThat(result).contains("FeishuException{");
        assertThat(result).contains("code=1001");
        assertThat(result).contains("message='Test error'");
        assertThat(result).contains("requestId='req-test'");
    }

    @Test
    void testToStringWithNullValues() {
        // 测试toString方法处理null值
        FeishuException exception = new FeishuException("Test message");
        
        String result = exception.toString();
        
        assertThat(result).contains("code=-1");
        assertThat(result).contains("message='Test message'");
        assertThat(result).contains("requestId='null'");
    }

    @Test
    void testExceptionInheritance() {
        // 测试异常继承关系
        FeishuException exception = new FeishuException("Test");
        
        assertThat(exception).isInstanceOf(RuntimeException.class);
        assertThat(exception).isInstanceOf(Exception.class);
        assertThat(exception).isInstanceOf(Throwable.class);
    }

    @Test
    void testExceptionCanBeThrown() {
        // 测试异常可以被抛出和捕获
        assertThatThrownBy(() -> {
            throw new FeishuException(1001, "Test error");
        })
        .isInstanceOf(FeishuException.class)
        .hasMessageContaining("Feishu API Error [1001]: Test error");
    }

    @Test
    void testExceptionWithNullMessage() {
        // 测试null消息
        FeishuException exception = new FeishuException(1001, null);
        
        assertThat(exception.getCode()).isEqualTo(1001);
        assertThat(exception.getMessage()).isEqualTo("Feishu API Error [1001]: null");
    }

    @Test
    void testExceptionWithEmptyMessage() {
        // 测试空消息
        FeishuException exception = new FeishuException(1001, "");
        
        assertThat(exception.getCode()).isEqualTo(1001);
        assertThat(exception.getMessage()).isEqualTo("Feishu API Error [1001]: ");
    }

    @Test
    void testMultipleErrorTypes() {
        // 测试多种错误类型的组合判断
        FeishuException tokenException = new FeishuException(FeishuConstant.CODE_INVALID_TOKEN, "Invalid token");
        FeishuException permissionException = new FeishuException(FeishuConstant.CODE_PERMISSION_DENIED, "Permission denied");
        FeishuException parameterException = new FeishuException(FeishuConstant.CODE_INVALID_PARAM, "Invalid parameter");
        FeishuException unknownException = new FeishuException(9999, "Unknown error");
        
        // 令牌错误
        assertThat(tokenException.isTokenError()).isTrue();
        assertThat(tokenException.isPermissionError()).isFalse();
        assertThat(tokenException.isParameterError()).isFalse();
        assertThat(tokenException.isRetryable()).isTrue();
        
        // 权限错误
        assertThat(permissionException.isTokenError()).isFalse();
        assertThat(permissionException.isPermissionError()).isTrue();
        assertThat(permissionException.isParameterError()).isFalse();
        assertThat(permissionException.isRetryable()).isFalse();
        
        // 参数错误
        assertThat(parameterException.isTokenError()).isFalse();
        assertThat(parameterException.isPermissionError()).isFalse();
        assertThat(parameterException.isParameterError()).isTrue();
        assertThat(parameterException.isRetryable()).isFalse();
        
        // 未知错误
        assertThat(unknownException.isTokenError()).isFalse();
        assertThat(unknownException.isPermissionError()).isFalse();
        assertThat(unknownException.isParameterError()).isFalse();
        assertThat(unknownException.isRetryable()).isFalse();
    }
}
