package com.alibaba.langengine.feishu;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
public class FeishuConfiguration {

    /**
     * 飞书应用ID
     */
    private String appId;

    /**
     * 飞书应用密钥
     */
    private String appSecret;

    /**
     * 飞书API基础URL
     */
    private String baseUrl = "https://open.feishu.cn";

    /**
     * 连接超时时间（毫秒）
     */
    private int connectTimeout = 30000;

    /**
     * 读取超时时间（毫秒）
     */
    private int readTimeout = 30000;

    /**
     * 是否启用调试模式
     */
    private boolean debug = false;

    /**
     * 最大重试次数
     */
    private int maxRetries = 3;

    /**
     * 重试间隔时间（毫秒）
     */
    private int retryInterval = 1000;

    /**
     * 默认构造函数
     */
    public FeishuConfiguration() {
        // 从环境变量或系统属性中读取配置
        this.appId = getConfigValue("FEISHU_APP_ID", "feishu.app.id");
        this.appSecret = getConfigValue("FEISHU_APP_SECRET", "feishu.app.secret");
        
        String baseUrlConfig = getConfigValue("FEISHU_BASE_URL", "feishu.base.url");
        if (baseUrlConfig != null && !baseUrlConfig.isEmpty()) {
            this.baseUrl = baseUrlConfig;
        }
        
        String debugConfig = getConfigValue("FEISHU_DEBUG", "feishu.debug");
        if (debugConfig != null && !debugConfig.isEmpty()) {
            this.debug = Boolean.parseBoolean(debugConfig);
        }
    }

    /**
     * 带参数的构造函数
     * 
     * @param appId 应用ID
     * @param appSecret 应用密钥
     */
    public FeishuConfiguration(String appId, String appSecret) {
        this();
        this.appId = appId;
        this.appSecret = appSecret;
    }

    /**
     * 带完整参数的构造函数
     * 
     * @param appId 应用ID
     * @param appSecret 应用密钥
     * @param baseUrl 基础URL
     */
    public FeishuConfiguration(String appId, String appSecret, String baseUrl) {
        this(appId, appSecret);
        this.baseUrl = baseUrl;
    }

    /**
     * 验证配置是否有效
     * 
     * @return 配置是否有效
     */
    public boolean isValid() {
        return appId != null && !appId.trim().isEmpty() 
            && appSecret != null && !appSecret.trim().isEmpty()
            && baseUrl != null && !baseUrl.trim().isEmpty();
    }

    /**
     * 从环境变量或系统属性中获取配置值
     * 
     * @param envKey 环境变量键
     * @param propKey 系统属性键
     * @return 配置值
     */
    private String getConfigValue(String envKey, String propKey) {
        String value = System.getenv(envKey);
        if (value == null || value.isEmpty()) {
            value = System.getProperty(propKey);
        }
        return value;
    }

    /**
     * 获取完整的API URL
     * 
     * @param path API路径
     * @return 完整的API URL
     */
    public String getApiUrl(String path) {
        if (path == null || path.isEmpty()) {
            return baseUrl;
        }
        
        String normalizedBaseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        String normalizedPath = path.startsWith("/") ? path : "/" + path;
        
        return normalizedBaseUrl + normalizedPath;
    }

    // Getter和Setter方法
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }

    @Override
    public String toString() {
        return "FeishuConfiguration{" +
                "appId='" + (appId != null ? appId.substring(0, Math.min(appId.length(), 8)) + "..." : "null") + '\'' +
                ", appSecret='" + (appSecret != null ? "***" : "null") + '\'' +
                ", baseUrl='" + baseUrl + '\'' +
                ", connectTimeout=" + connectTimeout +
                ", readTimeout=" + readTimeout +
                ", debug=" + debug +
                ", maxRetries=" + maxRetries +
                ", retryInterval=" + retryInterval +
                '}';
    }
}
