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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Annoy配置测试")
class AnnoyConfigurationTest {

    @Test
    @DisplayName("测试默认配置值")
    void testDefaultConfigurationValues() {
        // 测试索引路径配置
        assertNotNull(AnnoyConfiguration.ANNOY_INDEX_PATH);
        assertEquals("./annoy_indexes", AnnoyConfiguration.ANNOY_INDEX_PATH);

        // 测试索引前缀配置
        assertNotNull(AnnoyConfiguration.ANNOY_INDEX_PREFIX);
        assertEquals("annoy_index", AnnoyConfiguration.ANNOY_INDEX_PREFIX);

        // 测试向量维度配置
        assertNotNull(AnnoyConfiguration.ANNOY_VECTOR_DIMENSION);
        assertEquals(Integer.valueOf(1536), AnnoyConfiguration.ANNOY_VECTOR_DIMENSION);

        // 测试距离度量配置
        assertNotNull(AnnoyConfiguration.ANNOY_DISTANCE_METRIC);
        assertEquals("angular", AnnoyConfiguration.ANNOY_DISTANCE_METRIC);

        // 测试树数量配置
        assertNotNull(AnnoyConfiguration.ANNOY_N_TREES);
        assertEquals(Integer.valueOf(10), AnnoyConfiguration.ANNOY_N_TREES);

        // 测试搜索参数配置
        assertNotNull(AnnoyConfiguration.ANNOY_SEARCH_K);
        assertEquals(Integer.valueOf(-1), AnnoyConfiguration.ANNOY_SEARCH_K);
    }

    @Test
    @DisplayName("测试布尔类型配置")
    void testBooleanConfigurations() {
        // 测试内存映射配置
        assertNotNull(AnnoyConfiguration.ANNOY_MMAP_ENABLED);
        assertTrue(AnnoyConfiguration.ANNOY_MMAP_ENABLED);

        // 测试预加载配置
        assertNotNull(AnnoyConfiguration.ANNOY_PRELOAD_ENABLED);
        assertTrue(AnnoyConfiguration.ANNOY_PRELOAD_ENABLED);

        // 测试自动保存配置
        assertNotNull(AnnoyConfiguration.ANNOY_AUTO_SAVE);
        assertTrue(AnnoyConfiguration.ANNOY_AUTO_SAVE);

        // 测试并发构建配置
        assertNotNull(AnnoyConfiguration.ANNOY_CONCURRENT_BUILD);
        assertFalse(AnnoyConfiguration.ANNOY_CONCURRENT_BUILD);
    }

    @Test
    @DisplayName("测试数值类型配置")
    void testNumericConfigurations() {
        // 测试构建超时配置
        assertNotNull(AnnoyConfiguration.ANNOY_BUILD_TIMEOUT);
        assertEquals(Long.valueOf(300), AnnoyConfiguration.ANNOY_BUILD_TIMEOUT);

        // 测试批次大小配置
        assertNotNull(AnnoyConfiguration.ANNOY_BATCH_SIZE);
        assertEquals(Integer.valueOf(1000), AnnoyConfiguration.ANNOY_BATCH_SIZE);

        // 测试自动保存间隔配置
        assertNotNull(AnnoyConfiguration.ANNOY_AUTO_SAVE_INTERVAL);
        assertEquals(Long.valueOf(60), AnnoyConfiguration.ANNOY_AUTO_SAVE_INTERVAL);

        // 测试最大索引大小配置
        assertNotNull(AnnoyConfiguration.ANNOY_MAX_INDEX_SIZE);
        assertEquals(Long.valueOf(1024), AnnoyConfiguration.ANNOY_MAX_INDEX_SIZE);

        // 测试构建线程数配置
        assertNotNull(AnnoyConfiguration.ANNOY_BUILD_THREADS);
        assertEquals(Integer.valueOf(4), AnnoyConfiguration.ANNOY_BUILD_THREADS);
    }

    @Test
    @DisplayName("测试配置值的合理性")
    void testConfigurationValueReasonableness() {
        // 测试向量维度为正数
        assertTrue(AnnoyConfiguration.ANNOY_VECTOR_DIMENSION > 0, 
                  "Vector dimension should be positive");

        // 测试树数量为正数
        assertTrue(AnnoyConfiguration.ANNOY_N_TREES > 0, 
                  "Number of trees should be positive");

        // 测试批次大小为正数
        assertTrue(AnnoyConfiguration.ANNOY_BATCH_SIZE > 0, 
                  "Batch size should be positive");

        // 测试构建超时为正数
        assertTrue(AnnoyConfiguration.ANNOY_BUILD_TIMEOUT > 0, 
                  "Build timeout should be positive");

        // 测试自动保存间隔为正数
        assertTrue(AnnoyConfiguration.ANNOY_AUTO_SAVE_INTERVAL > 0, 
                  "Auto save interval should be positive");

        // 测试最大索引大小为正数
        assertTrue(AnnoyConfiguration.ANNOY_MAX_INDEX_SIZE > 0, 
                  "Max index size should be positive");

        // 测试构建线程数为正数
        assertTrue(AnnoyConfiguration.ANNOY_BUILD_THREADS > 0, 
                  "Build threads should be positive");
    }

    @Test
    @DisplayName("测试距离度量类型的有效性")
    void testDistanceMetricValidity() {
        String metric = AnnoyConfiguration.ANNOY_DISTANCE_METRIC;
        assertNotNull(metric);
        assertFalse(metric.trim().isEmpty());
        
        // 测试是否为支持的距离度量类型
        assertTrue(
            "angular".equalsIgnoreCase(metric) ||
            "euclidean".equalsIgnoreCase(metric) ||
            "manhattan".equalsIgnoreCase(metric) ||
            "hamming".equalsIgnoreCase(metric) ||
            "dot".equalsIgnoreCase(metric),
            "Distance metric should be one of: angular, euclidean, manhattan, hamming, dot"
        );
    }

    @Test
    @DisplayName("测试索引路径和前缀的有效性")
    void testIndexPathAndPrefixValidity() {
        // 测试索引路径不为空
        assertNotNull(AnnoyConfiguration.ANNOY_INDEX_PATH);
        assertFalse(AnnoyConfiguration.ANNOY_INDEX_PATH.trim().isEmpty());

        // 测试索引前缀不为空
        assertNotNull(AnnoyConfiguration.ANNOY_INDEX_PREFIX);
        assertFalse(AnnoyConfiguration.ANNOY_INDEX_PREFIX.trim().isEmpty());

        // 测试索引前缀不包含特殊字符
        String prefix = AnnoyConfiguration.ANNOY_INDEX_PREFIX;
        assertTrue(prefix.matches("[a-zA-Z0-9_-]+"), 
                  "Index prefix should only contain alphanumeric characters, underscore, and hyphen");
    }
}
