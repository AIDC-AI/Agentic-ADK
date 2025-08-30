package com.alibaba.langengine.feishu.sdk;


public class FeishuException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    /**
     * 错误消息
     */
    private final String message;

    /**
     * 请求ID，用于问题追踪
     */
    private final String requestId;

    /**
     * 构造函数
     * 
     * @param message 错误消息
     */
    public FeishuException(String message) {
        super(message);
        this.code = -1;
        this.message = message;
        this.requestId = null;
    }

    /**
     * 构造函数
     * 
     * @param code 错误码
     * @param message 错误消息
     */
    public FeishuException(int code, String message) {
        super(String.format("Feishu API Error [%d]: %s", code, message));
        this.code = code;
        this.message = message;
        this.requestId = null;
    }

    /**
     * 构造函数
     * 
     * @param code 错误码
     * @param message 错误消息
     * @param requestId 请求ID
     */
    public FeishuException(int code, String message, String requestId) {
        super(String.format("Feishu API Error [%d]: %s (RequestId: %s)", code, message, requestId));
        this.code = code;
        this.message = message;
        this.requestId = requestId;
    }

    /**
     * 构造函数
     * 
     * @param message 错误消息
     * @param cause 原因异常
     */
    public FeishuException(String message, Throwable cause) {
        super(message, cause);
        this.code = -1;
        this.message = message;
        this.requestId = null;
    }

    /**
     * 构造函数
     * 
     * @param code 错误码
     * @param message 错误消息
     * @param cause 原因异常
     */
    public FeishuException(int code, String message, Throwable cause) {
        super(String.format("Feishu API Error [%d]: %s", code, message), cause);
        this.code = code;
        this.message = message;
        this.requestId = null;
    }

    /**
     * 构造函数
     * 
     * @param code 错误码
     * @param message 错误消息
     * @param requestId 请求ID
     * @param cause 原因异常
     */
    public FeishuException(int code, String message, String requestId, Throwable cause) {
        super(String.format("Feishu API Error [%d]: %s (RequestId: %s)", code, message, requestId), cause);
        this.code = code;
        this.message = message;
        this.requestId = requestId;
    }

    /**
     * 获取错误码
     * 
     * @return 错误码
     */
    public int getCode() {
        return code;
    }



    /**
     * 获取请求ID
     * 
     * @return 请求ID
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * 判断是否为令牌相关错误
     * 
     * @return 是否为令牌相关错误
     */
    public boolean isTokenError() {
        return code == FeishuConstant.CODE_INVALID_TOKEN || code == FeishuConstant.CODE_TOKEN_EXPIRED;
    }

    /**
     * 判断是否为权限错误
     * 
     * @return 是否为权限错误
     */
    public boolean isPermissionError() {
        return code == FeishuConstant.CODE_PERMISSION_DENIED;
    }

    /**
     * 判断是否为参数错误
     * 
     * @return 是否为参数错误
     */
    public boolean isParameterError() {
        return code == FeishuConstant.CODE_INVALID_PARAM;
    }

    /**
     * 判断是否可以重试
     * 
     * @return 是否可以重试
     */
    public boolean isRetryable() {
        // 令牌错误可以重试（会自动刷新令牌）
        // 网络错误等也可以重试
        return isTokenError() || code == -1;
    }

    @Override
    public String toString() {
        return "FeishuException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}
