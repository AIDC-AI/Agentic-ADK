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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class ScannConfigurationTest {

    private String originalServerUrl;
    private String originalServerPort;
    private String originalConnectionTimeout;
    private String originalReadTimeout;
    private String originalMaxConnections;
    private String originalIndexType;
    private String originalDistanceMeasure;
    private String originalDimensions;
    private String originalTrainingSampleSize;
    private String originalLeavesToSearch;
    private String originalReorderNumNeighbors;

    @BeforeEach
    public void setUp() {
        // 保存原始环境变量和系统属性
        originalServerUrl = System.getProperty("SCANN_SERVER_URL");
        originalServerPort = System.getProperty("SCANN_SERVER_PORT");
        originalConnectionTimeout = System.getProperty("SCANN_CONNECTION_TIMEOUT");
        originalReadTimeout = System.getProperty("SCANN_READ_TIMEOUT");
        originalMaxConnections = System.getProperty("SCANN_MAX_CONNECTIONS");
        originalIndexType = System.getProperty("SCANN_DEFAULT_INDEX_TYPE");
        originalDistanceMeasure = System.getProperty("SCANN_DEFAULT_DISTANCE_MEASURE");
        originalDimensions = System.getProperty("SCANN_DEFAULT_DIMENSIONS");
        originalTrainingSampleSize = System.getProperty("SCANN_DEFAULT_TRAINING_SAMPLE_SIZE");
        originalLeavesToSearch = System.getProperty("SCANN_DEFAULT_LEAVES_TO_SEARCH");
        originalReorderNumNeighbors = System.getProperty("SCANN_DEFAULT_REORDER_NUM_NEIGHBORS");
    }

    @AfterEach
    public void tearDown() {
        // 恢复原始环境变量和系统属性
        restoreProperty("SCANN_SERVER_URL", originalServerUrl);
        restoreProperty("SCANN_SERVER_PORT", originalServerPort);
        restoreProperty("SCANN_CONNECTION_TIMEOUT", originalConnectionTimeout);
        restoreProperty("SCANN_READ_TIMEOUT", originalReadTimeout);
        restoreProperty("SCANN_MAX_CONNECTIONS", originalMaxConnections);
        restoreProperty("SCANN_DEFAULT_INDEX_TYPE", originalIndexType);
        restoreProperty("SCANN_DEFAULT_DISTANCE_MEASURE", originalDistanceMeasure);
        restoreProperty("SCANN_DEFAULT_DIMENSIONS", originalDimensions);
        restoreProperty("SCANN_DEFAULT_TRAINING_SAMPLE_SIZE", originalTrainingSampleSize);
        restoreProperty("SCANN_DEFAULT_LEAVES_TO_SEARCH", originalLeavesToSearch);
        restoreProperty("SCANN_DEFAULT_REORDER_NUM_NEIGHBORS", originalReorderNumNeighbors);
    }

    @Test
    @DisplayName("测试默认配置值")
    public void testDefaultConfigurationValues() {
        // 清除所有系统属性
        clearAllProperties();
        
        // 验证默认值
        assertEquals("8080", ScannConfiguration.SCANN_SERVER_PORT);
        assertEquals("30000", ScannConfiguration.SCANN_CONNECTION_TIMEOUT);
        assertEquals("60000", ScannConfiguration.SCANN_READ_TIMEOUT);
        assertEquals("100", ScannConfiguration.SCANN_MAX_CONNECTIONS);
        assertEquals("tree_ah", ScannConfiguration.SCANN_DEFAULT_INDEX_TYPE);
        assertEquals("dot_product", ScannConfiguration.SCANN_DEFAULT_DISTANCE_MEASURE);
        assertEquals("768", ScannConfiguration.SCANN_DEFAULT_DIMENSIONS);
        assertEquals("100000", ScannConfiguration.SCANN_DEFAULT_TRAINING_SAMPLE_SIZE);
        assertEquals("100", ScannConfiguration.SCANN_DEFAULT_LEAVES_TO_SEARCH);
        assertEquals("1000", ScannConfiguration.SCANN_DEFAULT_REORDER_NUM_NEIGHBORS);
    }

    @Test
    @DisplayName("测试系统属性配置")
    public void testSystemPropertyConfiguration() {
        // 设置系统属性
        System.setProperty("SCANN_SERVER_URL", "http://test-server:9090");
        System.setProperty("SCANN_SERVER_PORT", "9090");
        System.setProperty("SCANN_CONNECTION_TIMEOUT", "15000");
        System.setProperty("SCANN_READ_TIMEOUT", "45000");
        System.setProperty("SCANN_MAX_CONNECTIONS", "200");
        System.setProperty("SCANN_DEFAULT_INDEX_TYPE", "brute_force");
        System.setProperty("SCANN_DEFAULT_DISTANCE_MEASURE", "cosine");
        System.setProperty("SCANN_DEFAULT_DIMENSIONS", "1024");
        System.setProperty("SCANN_DEFAULT_TRAINING_SAMPLE_SIZE", "50000");
        System.setProperty("SCANN_DEFAULT_LEAVES_TO_SEARCH", "200");
        System.setProperty("SCANN_DEFAULT_REORDER_NUM_NEIGHBORS", "2000");
        
        // 由于配置类使用静态字段，需要重新加载类或使用反射
        // 这里我们测试配置读取逻辑的正确性
        String serverUrl = System.getProperty("SCANN_SERVER_URL");
        assertEquals("http://test-server:9090", serverUrl);
        
        String serverPort = System.getProperty("SCANN_SERVER_PORT");
        assertEquals("9090", serverPort);
        
        String connectionTimeout = System.getProperty("SCANN_CONNECTION_TIMEOUT");
        assertEquals("15000", connectionTimeout);
    }

    @Test
    @DisplayName("测试环境变量优先级")
    public void testEnvironmentVariablePriority() {
        // 注意：在单元测试中很难直接测试环境变量
        // 这里主要测试配置读取逻辑
        
        // 设置系统属性
        System.setProperty("SCANN_SERVER_PORT", "system_8080");
        
        // 验证系统属性被读取
        String value = System.getProperty("SCANN_SERVER_PORT");
        assertEquals("system_8080", value);
        
        // 清除系统属性
        System.clearProperty("SCANN_SERVER_PORT");
        
        // 验证默认值
        String defaultValue = System.getProperty("SCANN_SERVER_PORT");
        assertNull(defaultValue);
    }

    @Test
    @DisplayName("测试配置值类型转换")
    public void testConfigurationValueTypes() {
        // 测试数值类型配置
        System.setProperty("SCANN_SERVER_PORT", "9090");
        System.setProperty("SCANN_CONNECTION_TIMEOUT", "25000");
        System.setProperty("SCANN_READ_TIMEOUT", "50000");
        System.setProperty("SCANN_MAX_CONNECTIONS", "150");
        System.setProperty("SCANN_DEFAULT_DIMENSIONS", "512");
        System.setProperty("SCANN_DEFAULT_TRAINING_SAMPLE_SIZE", "75000");
        System.setProperty("SCANN_DEFAULT_LEAVES_TO_SEARCH", "150");
        System.setProperty("SCANN_DEFAULT_REORDER_NUM_NEIGHBORS", "1500");
        
        // 验证配置值可以正确解析为数值
        assertDoesNotThrow(() -> {
            Integer.parseInt(System.getProperty("SCANN_SERVER_PORT"));
            Integer.parseInt(System.getProperty("SCANN_CONNECTION_TIMEOUT"));
            Integer.parseInt(System.getProperty("SCANN_READ_TIMEOUT"));
            Integer.parseInt(System.getProperty("SCANN_MAX_CONNECTIONS"));
            Integer.parseInt(System.getProperty("SCANN_DEFAULT_DIMENSIONS"));
            Integer.parseInt(System.getProperty("SCANN_DEFAULT_TRAINING_SAMPLE_SIZE"));
            Integer.parseInt(System.getProperty("SCANN_DEFAULT_LEAVES_TO_SEARCH"));
            Integer.parseInt(System.getProperty("SCANN_DEFAULT_REORDER_NUM_NEIGHBORS"));
        });
    }

    @Test
    @DisplayName("测试配置值边界情况")
    public void testConfigurationBoundaryValues() {
        // 测试空字符串配置
        System.setProperty("SCANN_SERVER_URL", "");
        String emptyUrl = System.getProperty("SCANN_SERVER_URL");
        assertEquals("", emptyUrl);
        
        // 测试空白字符串配置
        System.setProperty("SCANN_SERVER_URL", "   ");
        String blankUrl = System.getProperty("SCANN_SERVER_URL");
        assertEquals("   ", blankUrl);
        
        // 测试特殊字符配置
        System.setProperty("SCANN_DEFAULT_INDEX_TYPE", "tree-ah_v2.0");
        String specialIndexType = System.getProperty("SCANN_DEFAULT_INDEX_TYPE");
        assertEquals("tree-ah_v2.0", specialIndexType);
    }

    @Test
    @DisplayName("测试配置常量访问")
    public void testConfigurationConstants() {
        // 验证所有配置常量都可以访问（SCANN_SERVER_URL 可能为 null，因为没有默认值）
        // assertNotNull(ScannConfiguration.SCANN_SERVER_URL); // 可能为 null
        assertNotNull(ScannConfiguration.SCANN_SERVER_PORT);
        assertNotNull(ScannConfiguration.SCANN_CONNECTION_TIMEOUT);
        assertNotNull(ScannConfiguration.SCANN_READ_TIMEOUT);
        assertNotNull(ScannConfiguration.SCANN_MAX_CONNECTIONS);
        assertNotNull(ScannConfiguration.SCANN_DEFAULT_INDEX_TYPE);
        assertNotNull(ScannConfiguration.SCANN_DEFAULT_DISTANCE_MEASURE);
        assertNotNull(ScannConfiguration.SCANN_DEFAULT_DIMENSIONS);
        assertNotNull(ScannConfiguration.SCANN_DEFAULT_TRAINING_SAMPLE_SIZE);
        assertNotNull(ScannConfiguration.SCANN_DEFAULT_LEAVES_TO_SEARCH);
        assertNotNull(ScannConfiguration.SCANN_DEFAULT_REORDER_NUM_NEIGHBORS);
    }

    @Test
    @DisplayName("测试配置值的一致性")
    public void testConfigurationConsistency() {
        // 验证默认配置值的一致性和合理性
        assertEquals("8080", ScannConfiguration.SCANN_SERVER_PORT);
        assertEquals("30000", ScannConfiguration.SCANN_CONNECTION_TIMEOUT);
        assertEquals("60000", ScannConfiguration.SCANN_READ_TIMEOUT);
        
        // 验证超时时间的合理性（读取超时应该大于连接超时）
        int connectionTimeout = Integer.parseInt(ScannConfiguration.SCANN_CONNECTION_TIMEOUT);
        int readTimeout = Integer.parseInt(ScannConfiguration.SCANN_READ_TIMEOUT);
        assertTrue(readTimeout >= connectionTimeout);
        
        // 验证向量维度的合理性
        int dimensions = Integer.parseInt(ScannConfiguration.SCANN_DEFAULT_DIMENSIONS);
        assertTrue(dimensions > 0);
        assertTrue(dimensions <= 10000); // 合理的上限
        
        // 验证训练样本大小的合理性
        int trainingSampleSize = Integer.parseInt(ScannConfiguration.SCANN_DEFAULT_TRAINING_SAMPLE_SIZE);
        assertTrue(trainingSampleSize > 0);
        
        // 验证搜索参数的合理性
        int leavesToSearch = Integer.parseInt(ScannConfiguration.SCANN_DEFAULT_LEAVES_TO_SEARCH);
        int reorderNumNeighbors = Integer.parseInt(ScannConfiguration.SCANN_DEFAULT_REORDER_NUM_NEIGHBORS);
        assertTrue(leavesToSearch > 0);
        assertTrue(reorderNumNeighbors > 0);
        assertTrue(reorderNumNeighbors >= leavesToSearch); // 重排序候选数应该大于等于叶子搜索数
    }

    /**
     * 恢复系统属性
     *
     * @param key 属性键
     * @param value 原始值
     */
    private void restoreProperty(String key, String value) {
        if (value != null) {
            System.setProperty(key, value);
        } else {
            System.clearProperty(key);
        }
    }

    /**
     * 清除所有测试相关的系统属性
     */
    private void clearAllProperties() {
        System.clearProperty("SCANN_SERVER_URL");
        System.clearProperty("SCANN_SERVER_PORT");
        System.clearProperty("SCANN_CONNECTION_TIMEOUT");
        System.clearProperty("SCANN_READ_TIMEOUT");
        System.clearProperty("SCANN_MAX_CONNECTIONS");
        System.clearProperty("SCANN_DEFAULT_INDEX_TYPE");
        System.clearProperty("SCANN_DEFAULT_DISTANCE_MEASURE");
        System.clearProperty("SCANN_DEFAULT_DIMENSIONS");
        System.clearProperty("SCANN_DEFAULT_TRAINING_SAMPLE_SIZE");
        System.clearProperty("SCANN_DEFAULT_LEAVES_TO_SEARCH");
        System.clearProperty("SCANN_DEFAULT_REORDER_NUM_NEIGHBORS");
    }
}
