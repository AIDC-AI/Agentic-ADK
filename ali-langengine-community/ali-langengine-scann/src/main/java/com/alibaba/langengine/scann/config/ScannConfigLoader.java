/**
 * Copyright (C) 2024 AIDC-AI
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.langengine.scann.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ScaNN 配置加载器
 * 负责从配置文件、环境变量和系统属性中加载配置
 */
@Slf4j
public class ScannConfigLoader {

    private static final String DEFAULT_CONFIG_FILE = "scann.properties";
    private static final String CUSTOM_CONFIG_FILE_PROPERTY = "scann.config.file";
    
    private static Properties properties;
    private static volatile boolean initialized = false;

    /**
     * 初始化配置
     */
    public static synchronized void initialize() {
        if (initialized) {
            return;
        }
        
        properties = new Properties();
        
        // 1. 加载默认配置文件
        loadConfigFile(DEFAULT_CONFIG_FILE);
        
        // 2. 加载自定义配置文件（如果指定）
        String customConfigFile = System.getProperty(CUSTOM_CONFIG_FILE_PROPERTY);
        if (StringUtils.isNotEmpty(customConfigFile)) {
            loadConfigFile(customConfigFile);
        }
        
        // 3. 加载系统属性（优先级最高）
        loadSystemProperties();
        
        initialized = true;
        log.info("ScaNN configuration initialized successfully");
    }

    /**
     * 加载配置文件
     *
     * @param configFile 配置文件名
     */
    private static void loadConfigFile(String configFile) {
        try (InputStream inputStream = ScannConfigLoader.class.getClassLoader().getResourceAsStream(configFile)) {
            if (inputStream != null) {
                Properties fileProperties = new Properties();
                fileProperties.load(inputStream);
                properties.putAll(fileProperties);
                log.info("Loaded ScaNN configuration from: {}", configFile);
            } else {
                log.warn("ScaNN configuration file not found: {}", configFile);
            }
        } catch (IOException e) {
            log.error("Failed to load ScaNN configuration file: {}", configFile, e);
        }
    }

    /**
     * 加载系统属性
     */
    private static void loadSystemProperties() {
        // 加载所有以 scann. 开头的系统属性
        System.getProperties().entrySet().stream()
                .filter(entry -> entry.getKey().toString().startsWith("scann."))
                .forEach(entry -> properties.setProperty(entry.getKey().toString(), entry.getValue().toString()));
        
        // 加载环境变量（转换为属性格式）
        System.getenv().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("SCANN_"))
                .forEach(entry -> {
                    String propertyKey = entry.getKey().toLowerCase().replace('_', '.');
                    properties.setProperty(propertyKey, entry.getValue());
                });
    }

    /**
     * 获取字符串配置值
     *
     * @param key 配置键
     * @return 配置值
     */
    public static String getString(String key) {
        return getString(key, null);
    }

    /**
     * 获取字符串配置值
     *
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public static String getString(String key, String defaultValue) {
        ensureInitialized();
        return properties.getProperty(key, defaultValue);
    }

    /**
     * 获取整数配置值
     *
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public static int getInt(String key, int defaultValue) {
        String value = getString(key);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.warn("Invalid integer value for key {}: {}, using default: {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * 获取长整数配置值
     *
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public static long getLong(String key, long defaultValue) {
        String value = getString(key);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.warn("Invalid long value for key {}: {}, using default: {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * 获取布尔配置值
     *
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = getString(key);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * 获取双精度浮点数配置值
     *
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public static double getDouble(String key, double defaultValue) {
        String value = getString(key);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            log.warn("Invalid double value for key {}: {}, using default: {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * 检查配置是否存在
     *
     * @param key 配置键
     * @return 是否存在
     */
    public static boolean hasProperty(String key) {
        ensureInitialized();
        return properties.containsKey(key);
    }

    /**
     * 获取所有配置属性
     *
     * @return 配置属性
     */
    public static Properties getAllProperties() {
        ensureInitialized();
        return new Properties(properties);
    }

    /**
     * 重新加载配置
     */
    public static synchronized void reload() {
        initialized = false;
        properties = null;
        initialize();
    }

    /**
     * 确保配置已初始化
     */
    private static void ensureInitialized() {
        if (!initialized) {
            initialize();
        }
    }

    /**
     * 打印所有配置（用于调试）
     */
    public static void printAllConfigurations() {
        ensureInitialized();
        log.info("=== ScaNN Configuration ===");
        properties.entrySet().stream()
                .sorted((e1, e2) -> e1.getKey().toString().compareTo(e2.getKey().toString()))
                .forEach(entry -> log.info("{} = {}", entry.getKey(), entry.getValue()));
        log.info("=== End of Configuration ===");
    }
}
