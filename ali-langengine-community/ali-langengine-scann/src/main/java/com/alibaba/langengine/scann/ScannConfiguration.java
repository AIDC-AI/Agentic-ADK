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
package com.alibaba.langengine.scann;

import com.alibaba.langengine.scann.config.ScannConfigLoader;
import org.apache.commons.lang3.StringUtils;

/**
 * ScaNN 配置类
 * 提供统一的配置访问接口，支持配置文件、环境变量和系统属性
 */
public class ScannConfiguration {

    // 初始化配置加载器
    static {
        ScannConfigLoader.initialize();
    }

    /**
     * ScaNN 服务器地址
     */
    public static final String SCANN_SERVER_URL = getConfiguration("SCANN_SERVER_URL", "scann.server.url");

    /**
     * ScaNN 服务器端口
     */
    public static final String SCANN_SERVER_PORT = getConfiguration("SCANN_SERVER_PORT", "scann.server.port", "8080");

    /**
     * ScaNN 连接超时时间（毫秒）
     */
    public static final String SCANN_CONNECTION_TIMEOUT = getConfiguration("SCANN_CONNECTION_TIMEOUT", "scann.server.connection.timeout", "30000");

    /**
     * ScaNN 读取超时时间（毫秒）
     */
    public static final String SCANN_READ_TIMEOUT = getConfiguration("SCANN_READ_TIMEOUT", "scann.server.read.timeout", "60000");

    /**
     * ScaNN 最大连接数
     */
    public static final String SCANN_MAX_CONNECTIONS = getConfiguration("SCANN_MAX_CONNECTIONS", "scann.server.max.connections", "100");

    /**
     * ScaNN 默认索引类型
     */
    public static final String SCANN_DEFAULT_INDEX_TYPE = getConfiguration("SCANN_DEFAULT_INDEX_TYPE", "scann.index.default.type", "tree_ah");

    /**
     * ScaNN 默认距离度量类型
     */
    public static final String SCANN_DEFAULT_DISTANCE_MEASURE = getConfiguration("SCANN_DEFAULT_DISTANCE_MEASURE", "scann.index.default.distance.measure", "dot_product");

    /**
     * ScaNN 默认向量维度
     */
    public static final String SCANN_DEFAULT_DIMENSIONS = getConfiguration("SCANN_DEFAULT_DIMENSIONS", "scann.index.default.dimensions", "768");

    /**
     * ScaNN 默认训练样本数
     */
    public static final String SCANN_DEFAULT_TRAINING_SAMPLE_SIZE = getConfiguration("SCANN_DEFAULT_TRAINING_SAMPLE_SIZE", "scann.index.training.sample.size", "100000");

    /**
     * ScaNN 默认叶子节点大小
     */
    public static final String SCANN_DEFAULT_LEAVES_TO_SEARCH = getConfiguration("SCANN_DEFAULT_LEAVES_TO_SEARCH", "scann.index.leaves.to.search", "100");

    /**
     * ScaNN 默认重排序候选数
     */
    public static final String SCANN_DEFAULT_REORDER_NUM_NEIGHBORS = getConfiguration("SCANN_DEFAULT_REORDER_NUM_NEIGHBORS", "scann.index.reorder.num.neighbors", "1000");

    /**
     * 获取配置值（支持环境变量和配置文件）
     *
     * @param envKey 环境变量键
     * @param propKey 配置文件键
     * @return 配置值
     */
    private static String getConfiguration(String envKey, String propKey) {
        return getConfiguration(envKey, propKey, null);
    }

    /**
     * 获取配置值，如果不存在则返回默认值
     *
     * @param envKey 环境变量键
     * @param propKey 配置文件键
     * @param defaultValue 默认值
     * @return 配置值
     */
    private static String getConfiguration(String envKey, String propKey, String defaultValue) {
        // 1. 优先使用系统属性
        String value = System.getProperty(envKey);
        if (StringUtils.isNotEmpty(value)) {
            return value;
        }

        // 2. 使用环境变量
        value = System.getenv(envKey);
        if (StringUtils.isNotEmpty(value)) {
            return value;
        }

        // 3. 使用配置文件
        value = ScannConfigLoader.getString(propKey);
        if (StringUtils.isNotEmpty(value)) {
            return value;
        }

        // 4. 返回默认值
        return defaultValue;
    }

    /**
     * 获取配置加载器实例
     *
     * @return 配置加载器
     */
    public static ScannConfigLoader getConfigLoader() {
        return new ScannConfigLoader();
    }

    /**
     * 重新加载配置
     */
    public static void reloadConfiguration() {
        ScannConfigLoader.reload();
    }

    /**
     * 打印所有配置（用于调试）
     */
    public static void printAllConfigurations() {
        ScannConfigLoader.printAllConfigurations();
    }


    /**
     * 获取字符串配置值
     *
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public static String getString(String key, String defaultValue) {
        return ScannConfigLoader.getString(key, defaultValue);
    }

    /**
     * 获取整数配置值
     *
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public static int getInt(String key, int defaultValue) {
        return ScannConfigLoader.getInt(key, defaultValue);
    }

    /**
     * 获取长整数配置值
     *
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public static long getLong(String key, long defaultValue) {
        return ScannConfigLoader.getLong(key, defaultValue);
    }

    /**
     * 获取布尔配置值
     *
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        return ScannConfigLoader.getBoolean(key, defaultValue);
    }

    /**
     * 获取双精度浮点数配置值
     *
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public static double getDouble(String key, double defaultValue) {
        return ScannConfigLoader.getDouble(key, defaultValue);
    }
}
