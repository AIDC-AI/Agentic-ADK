/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.langengine.annoy;

import com.alibaba.langengine.core.util.WorkPropertiesUtils;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AnnoyConfiguration {

    /**
     * Annoy索引文件存储路径
     */
    public static String ANNOY_INDEX_PATH = WorkPropertiesUtils.get("annoy_index_path", "./annoy_indexes");

    /**
     * Annoy索引文件前缀
     */
    public static String ANNOY_INDEX_PREFIX = WorkPropertiesUtils.get("annoy_index_prefix", "annoy_index");

    /**
     * 默认向量维度
     */
    public static Integer ANNOY_VECTOR_DIMENSION = getIntegerProperty("annoy_vector_dimension", 1536);

    /**
     * 默认距离度量类型
     * 支持: angular, euclidean, manhattan, hamming, dot
     */
    public static String ANNOY_DISTANCE_METRIC = WorkPropertiesUtils.get("annoy_distance_metric", "angular");

    /**
     * 构建索引时的树数量，更多的树意味着更好的精度但更大的内存使用
     */
    public static Integer ANNOY_N_TREES = getIntegerProperty("annoy_n_trees", 10);

    /**
     * 搜索时检查的节点数，更多的节点意味着更好的精度但更慢的搜索
     */
    public static Integer ANNOY_SEARCH_K = getIntegerProperty("annoy_search_k", -1);

    /**
     * 是否启用内存映射模式
     */
    public static Boolean ANNOY_MMAP_ENABLED = Boolean.valueOf(WorkPropertiesUtils.get("annoy_mmap_enabled", "true"));

    /**
     * 索引构建超时时间（秒）
     */
    public static Long ANNOY_BUILD_TIMEOUT = getLongProperty("annoy_build_timeout", 300L);

    /**
     * 是否启用索引预加载
     */
    public static Boolean ANNOY_PRELOAD_ENABLED = Boolean.valueOf(WorkPropertiesUtils.get("annoy_preload_enabled", "true"));

    /**
     * 批量添加向量的批次大小
     */
    public static Integer ANNOY_BATCH_SIZE = getIntegerProperty("annoy_batch_size", 1000);

    /**
     * 是否启用索引自动保存
     */
    public static Boolean ANNOY_AUTO_SAVE = Boolean.valueOf(WorkPropertiesUtils.get("annoy_auto_save", "true"));

    /**
     * 索引自动保存间隔（秒）
     */
    public static Long ANNOY_AUTO_SAVE_INTERVAL = getLongProperty("annoy_auto_save_interval", 60L);

    /**
     * 最大索引文件大小（MB）
     */
    public static Long ANNOY_MAX_INDEX_SIZE = getLongProperty("annoy_max_index_size", 1024L);

    /**
     * 是否启用并发构建
     */
    public static Boolean ANNOY_CONCURRENT_BUILD = Boolean.valueOf(WorkPropertiesUtils.get("annoy_concurrent_build", "false"));

    /**
     * 并发构建线程数
     */
    public static Integer ANNOY_BUILD_THREADS = getIntegerProperty("annoy_build_threads", 4);

    /**
     * Annoy原生库路径
     */
    public static String ANNOY_NATIVE_LIBRARY_PATH = WorkPropertiesUtils.get("annoy_native_library_path", "");

    /**
     * 安全地获取整数配置属性
     */
    private static Integer getIntegerProperty(String key, Integer defaultValue) {
        String value = WorkPropertiesUtils.get(key, String.valueOf(defaultValue));
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            log.warn("Invalid integer value for property {}: {}, using default {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * 安全地获取长整数配置属性
     */
    private static Long getLongProperty(String key, Long defaultValue) {
        String value = WorkPropertiesUtils.get(key, String.valueOf(defaultValue));
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            log.warn("Invalid long value for property {}: {}, using default {}", key, value, defaultValue);
            return defaultValue;
        }
    }
}
