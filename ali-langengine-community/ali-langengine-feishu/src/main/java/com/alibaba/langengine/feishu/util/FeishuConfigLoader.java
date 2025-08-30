package com.alibaba.langengine.feishu.util;

import com.alibaba.langengine.feishu.FeishuConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


@Slf4j
public class FeishuConfigLoader {

    private static final String DEFAULT_CONFIG_FILE = "feishu-config.properties";
    
    // 环境变量名称
    private static final String ENV_APP_ID = "FEISHU_APP_ID";
    private static final String ENV_APP_SECRET = "FEISHU_APP_SECRET";
    private static final String ENV_BASE_URL = "FEISHU_BASE_URL";
    private static final String ENV_DEBUG = "FEISHU_DEBUG";
    
    // 系统属性名称
    private static final String PROP_APP_ID = "feishu.app.id";
    private static final String PROP_APP_SECRET = "feishu.app.secret";
    private static final String PROP_BASE_URL = "feishu.base.url";
    private static final String PROP_CONNECT_TIMEOUT = "feishu.connect.timeout";
    private static final String PROP_READ_TIMEOUT = "feishu.read.timeout";
    private static final String PROP_DEBUG = "feishu.debug";
    private static final String PROP_MAX_RETRIES = "feishu.max.retries";
    private static final String PROP_RETRY_INTERVAL = "feishu.retry.interval";

    /**
     * 从默认配置文件加载配置
     * 
     * @return 飞书配置
     */
    public static FeishuConfiguration loadFromDefaultFile() {
        return loadFromFile(DEFAULT_CONFIG_FILE);
    }

    /**
     * 从指定配置文件加载配置
     * 
     * @param configFile 配置文件路径
     * @return 飞书配置
     */
    public static FeishuConfiguration loadFromFile(String configFile) {
        Properties properties = new Properties();
        
        try (InputStream inputStream = FeishuConfigLoader.class.getClassLoader().getResourceAsStream(configFile)) {
            if (inputStream == null) {
                log.warn("Configuration file not found: {}", configFile);
                return loadFromEnvironment();
            }
            
            properties.load(inputStream);
            log.info("Loaded configuration from file: {}", configFile);
            
        } catch (IOException e) {
            log.warn("Failed to load configuration from file: {}", configFile, e);
            return loadFromEnvironment();
        }
        
        return createConfigurationFromProperties(properties);
    }

    /**
     * 从环境变量加载配置
     * 
     * @return 飞书配置
     */
    public static FeishuConfiguration loadFromEnvironment() {
        FeishuConfiguration config = new FeishuConfiguration();
        
        // 从环境变量加载
        String appId = System.getenv(ENV_APP_ID);
        String appSecret = System.getenv(ENV_APP_SECRET);
        String baseUrl = System.getenv(ENV_BASE_URL);
        String debug = System.getenv(ENV_DEBUG);
        
        if (appId != null && !appId.trim().isEmpty()) {
            config.setAppId(appId.trim());
        }
        
        if (appSecret != null && !appSecret.trim().isEmpty()) {
            config.setAppSecret(appSecret.trim());
        }
        
        if (baseUrl != null && !baseUrl.trim().isEmpty()) {
            config.setBaseUrl(baseUrl.trim());
        }
        
        if (debug != null && !debug.trim().isEmpty()) {
            config.setDebug(Boolean.parseBoolean(debug.trim()));
        }
        
        log.info("Loaded configuration from environment variables");
        return config;
    }

    /**
     * 从系统属性加载配置
     * 
     * @return 飞书配置
     */
    public static FeishuConfiguration loadFromSystemProperties() {
        Properties systemProps = System.getProperties();
        return createConfigurationFromProperties(systemProps);
    }

    /**
     * 综合加载配置（优先级：系统属性 > 环境变量 > 配置文件）
     * 
     * @return 飞书配置
     */
    public static FeishuConfiguration loadConfiguration() {
        // 1. 先从配置文件加载基础配置
        FeishuConfiguration config = loadFromDefaultFile();
        
        // 2. 环境变量覆盖配置文件
        FeishuConfiguration envConfig = loadFromEnvironment();
        mergeConfiguration(config, envConfig);
        
        // 3. 系统属性覆盖环境变量
        FeishuConfiguration sysConfig = loadFromSystemProperties();
        mergeConfiguration(config, sysConfig);
        
        log.info("Final configuration loaded: {}", config);
        return config;
    }

    /**
     * 从Properties创建配置
     * 
     * @param properties 属性对象
     * @return 飞书配置
     */
    private static FeishuConfiguration createConfigurationFromProperties(Properties properties) {
        FeishuConfiguration config = new FeishuConfiguration();
        
        String appId = properties.getProperty(PROP_APP_ID);
        if (appId != null && !appId.trim().isEmpty()) {
            config.setAppId(appId.trim());
        }
        
        String appSecret = properties.getProperty(PROP_APP_SECRET);
        if (appSecret != null && !appSecret.trim().isEmpty()) {
            config.setAppSecret(appSecret.trim());
        }
        
        String baseUrl = properties.getProperty(PROP_BASE_URL);
        if (baseUrl != null && !baseUrl.trim().isEmpty()) {
            config.setBaseUrl(baseUrl.trim());
        }
        
        String connectTimeout = properties.getProperty(PROP_CONNECT_TIMEOUT);
        if (connectTimeout != null && !connectTimeout.trim().isEmpty()) {
            try {
                config.setConnectTimeout(Integer.parseInt(connectTimeout.trim()));
            } catch (NumberFormatException e) {
                log.warn("Invalid connect timeout value: {}", connectTimeout);
            }
        }
        
        String readTimeout = properties.getProperty(PROP_READ_TIMEOUT);
        if (readTimeout != null && !readTimeout.trim().isEmpty()) {
            try {
                config.setReadTimeout(Integer.parseInt(readTimeout.trim()));
            } catch (NumberFormatException e) {
                log.warn("Invalid read timeout value: {}", readTimeout);
            }
        }
        
        String debug = properties.getProperty(PROP_DEBUG);
        if (debug != null && !debug.trim().isEmpty()) {
            config.setDebug(Boolean.parseBoolean(debug.trim()));
        }
        
        String maxRetries = properties.getProperty(PROP_MAX_RETRIES);
        if (maxRetries != null && !maxRetries.trim().isEmpty()) {
            try {
                config.setMaxRetries(Integer.parseInt(maxRetries.trim()));
            } catch (NumberFormatException e) {
                log.warn("Invalid max retries value: {}", maxRetries);
            }
        }
        
        String retryInterval = properties.getProperty(PROP_RETRY_INTERVAL);
        if (retryInterval != null && !retryInterval.trim().isEmpty()) {
            try {
                config.setRetryInterval(Integer.parseInt(retryInterval.trim()));
            } catch (NumberFormatException e) {
                log.warn("Invalid retry interval value: {}", retryInterval);
            }
        }
        
        return config;
    }

    /**
     * 合并配置（source覆盖target中的非空值）
     * 
     * @param target 目标配置
     * @param source 源配置
     */
    private static void mergeConfiguration(FeishuConfiguration target, FeishuConfiguration source) {
        if (source.getAppId() != null && !source.getAppId().trim().isEmpty()) {
            target.setAppId(source.getAppId());
        }
        
        if (source.getAppSecret() != null && !source.getAppSecret().trim().isEmpty()) {
            target.setAppSecret(source.getAppSecret());
        }
        
        if (source.getBaseUrl() != null && !source.getBaseUrl().trim().isEmpty()) {
            target.setBaseUrl(source.getBaseUrl());
        }
        
        if (source.getConnectTimeout() > 0) {
            target.setConnectTimeout(source.getConnectTimeout());
        }
        
        if (source.getReadTimeout() > 0) {
            target.setReadTimeout(source.getReadTimeout());
        }
        
        target.setDebug(source.isDebug());
        
        if (source.getMaxRetries() > 0) {
            target.setMaxRetries(source.getMaxRetries());
        }
        
        if (source.getRetryInterval() > 0) {
            target.setRetryInterval(source.getRetryInterval());
        }
    }

    /**
     * 验证配置是否有效
     * 
     * @param config 配置对象
     * @return 是否有效
     */
    public static boolean validateConfiguration(FeishuConfiguration config) {
        if (config == null) {
            log.error("Configuration is null");
            return false;
        }
        
        if (!config.isValid()) {
            log.error("Invalid configuration: {}", config);
            return false;
        }
        
        log.info("Configuration validation passed");
        return true;
    }

    /**
     * 创建带验证的配置
     * 
     * @return 验证通过的配置，如果验证失败则抛出异常
     * @throws IllegalStateException 配置无效时抛出异常
     */
    public static FeishuConfiguration loadAndValidateConfiguration() {
        FeishuConfiguration config = loadConfiguration();
        
        if (!validateConfiguration(config)) {
            throw new IllegalStateException("Failed to load valid Feishu configuration. " +
                    "Please check your environment variables, system properties, or configuration file.");
        }
        
        return config;
    }

    /**
     * 打印配置信息（隐藏敏感信息）
     * 
     * @param config 配置对象
     */
    public static void printConfiguration(FeishuConfiguration config) {
        if (config == null) {
            System.out.println("Configuration: null");
            return;
        }
        
        System.out.println("=== Feishu Configuration ===");
        System.out.println("App ID: " + maskSensitiveInfo(config.getAppId()));
        System.out.println("App Secret: " + maskSensitiveInfo(config.getAppSecret()));
        System.out.println("Base URL: " + config.getBaseUrl());
        System.out.println("Connect Timeout: " + config.getConnectTimeout() + "ms");
        System.out.println("Read Timeout: " + config.getReadTimeout() + "ms");
        System.out.println("Debug Mode: " + config.isDebug());
        System.out.println("Max Retries: " + config.getMaxRetries());
        System.out.println("Retry Interval: " + config.getRetryInterval() + "ms");
        System.out.println("Valid: " + config.isValid());
        System.out.println("============================");
    }

    /**
     * 隐藏敏感信息
     * 
     * @param value 原始值
     * @return 隐藏后的值
     */
    private static String maskSensitiveInfo(String value) {
        if (value == null || value.length() <= 8) {
            return "***";
        }
        
        return value.substring(0, 4) + "***" + value.substring(value.length() - 4);
    }
}
