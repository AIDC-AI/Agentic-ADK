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
package com.alibaba.langengine.scann.vectorstore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;


public class ScannParamTest {

    private ScannParam scannParam;

    @BeforeEach
    public void setUp() {
        scannParam = new ScannParam();
    }

    @Test
    @DisplayName("测试默认构造函数")
    public void testDefaultConstructor() {
        ScannParam param = new ScannParam();
        
        // 验证默认值
        assertEquals("http://localhost:8080", param.getServerUrl());
        assertEquals(30000, param.getConnectionTimeout());
        assertEquals(60000, param.getReadTimeout());
        assertEquals(100, param.getMaxConnections());
        assertEquals(768, param.getDimensions());
        assertEquals("tree_ah", param.getIndexType());
        assertEquals("dot_product", param.getDistanceMeasure());
        assertEquals(100000, param.getTrainingSampleSize());
        assertEquals(100, param.getLeavesToSearch());
        assertEquals(1000, param.getReorderNumNeighbors());
        assertTrue(param.isEnableReordering());
        assertEquals("scalar", param.getQuantizationType());
        assertEquals(2, param.getQuantizationDimensions());
        assertTrue(param.isEnableParallelSearch());
        assertEquals(4, param.getSearchThreads());
        assertEquals(8, param.getBuildThreads());
        assertEquals(1024L * 1024L * 1024L, param.getMemoryMappedFileSize());
        assertTrue(param.isEnablePrefetch());
        assertEquals(1000, param.getBatchSize());
        assertEquals("default_index", param.getIndexName());
        assertEquals("default_dataset", param.getDatasetName());
        assertTrue(param.isAutoSaveIndex());
        assertEquals(300, param.getSaveInterval());
    }

    @Test
    @DisplayName("测试带参数的构造函数")
    public void testParameterizedConstructors() {
        // 测试两参数构造函数
        ScannParam param1 = new ScannParam("http://test:9090", 512);
        assertEquals("http://test:9090", param1.getServerUrl());
        assertEquals(512, param1.getDimensions());
        
        // 测试四参数构造函数
        ScannParam param2 = new ScannParam("http://test:9090", 1024, "brute_force", "cosine");
        assertEquals("http://test:9090", param2.getServerUrl());
        assertEquals(1024, param2.getDimensions());
        assertEquals("brute_force", param2.getIndexType());
        assertEquals("cosine", param2.getDistanceMeasure());
    }

    @Test
    @DisplayName("测试参数设置和获取")
    public void testSettersAndGetters() {
        // 测试服务器配置
        scannParam.setServerUrl("http://custom:8888");
        assertEquals("http://custom:8888", scannParam.getServerUrl());
        
        scannParam.setConnectionTimeout(15000);
        assertEquals(15000, scannParam.getConnectionTimeout());
        
        scannParam.setReadTimeout(30000);
        assertEquals(30000, scannParam.getReadTimeout());
        
        scannParam.setMaxConnections(200);
        assertEquals(200, scannParam.getMaxConnections());
        
        // 测试向量配置
        scannParam.setDimensions(1024);
        assertEquals(1024, scannParam.getDimensions());
        
        scannParam.setIndexType("tree_x_hybrid");
        assertEquals("tree_x_hybrid", scannParam.getIndexType());
        
        scannParam.setDistanceMeasure("squared_l2");
        assertEquals("squared_l2", scannParam.getDistanceMeasure());
        
        // 测试搜索配置
        scannParam.setTrainingSampleSize(50000);
        assertEquals(50000, scannParam.getTrainingSampleSize());
        
        scannParam.setLeavesToSearch(200);
        assertEquals(200, scannParam.getLeavesToSearch());
        
        scannParam.setReorderNumNeighbors(2000);
        assertEquals(2000, scannParam.getReorderNumNeighbors());
        
        scannParam.setEnableReordering(false);
        assertFalse(scannParam.isEnableReordering());
        
        // 测试量化配置
        scannParam.setQuantizationType("product");
        assertEquals("product", scannParam.getQuantizationType());
        
        scannParam.setQuantizationDimensions(4);
        assertEquals(4, scannParam.getQuantizationDimensions());
        
        // 测试并行配置
        scannParam.setEnableParallelSearch(false);
        assertFalse(scannParam.isEnableParallelSearch());
        
        scannParam.setSearchThreads(8);
        assertEquals(8, scannParam.getSearchThreads());
        
        scannParam.setBuildThreads(16);
        assertEquals(16, scannParam.getBuildThreads());
        
        // 测试内存配置
        scannParam.setMemoryMappedFileSize(2048L * 1024L * 1024L);
        assertEquals(2048L * 1024L * 1024L, scannParam.getMemoryMappedFileSize());
        
        scannParam.setEnablePrefetch(false);
        assertFalse(scannParam.isEnablePrefetch());
        
        // 测试批处理配置
        scannParam.setBatchSize(500);
        assertEquals(500, scannParam.getBatchSize());
        
        // 测试索引配置
        scannParam.setIndexName("custom_index");
        assertEquals("custom_index", scannParam.getIndexName());
        
        scannParam.setDatasetName("custom_dataset");
        assertEquals("custom_dataset", scannParam.getDatasetName());
        
        scannParam.setAutoSaveIndex(false);
        assertFalse(scannParam.isAutoSaveIndex());
        
        scannParam.setSaveInterval(600);
        assertEquals(600, scannParam.getSaveInterval());
    }

    @Test
    @DisplayName("测试参数验证 - 有效参数")
    public void testValidParameters() {
        // 默认参数应该是有效的
        assertTrue(scannParam.isValid());
        
        // 设置有效的自定义参数
        scannParam.setServerUrl("http://valid:8080");
        scannParam.setDimensions(512);
        scannParam.setConnectionTimeout(10000);
        scannParam.setReadTimeout(20000);
        scannParam.setMaxConnections(50);
        scannParam.setTrainingSampleSize(10000);
        scannParam.setLeavesToSearch(50);
        scannParam.setReorderNumNeighbors(500);
        
        assertTrue(scannParam.isValid());
    }

    @Test
    @DisplayName("测试参数验证 - 无效参数")
    public void testInvalidParameters() {
        // 测试空服务器URL
        scannParam.setServerUrl("");
        assertFalse(scannParam.isValid());
        
        scannParam.setServerUrl(null);
        assertFalse(scannParam.isValid());
        
        scannParam.setServerUrl("   ");
        assertFalse(scannParam.isValid());
        
        // 恢复有效的服务器URL
        scannParam.setServerUrl("http://localhost:8080");
        assertTrue(scannParam.isValid());
        
        // 测试无效的维度
        scannParam.setDimensions(0);
        assertFalse(scannParam.isValid());
        
        scannParam.setDimensions(-1);
        assertFalse(scannParam.isValid());
        
        // 恢复有效的维度
        scannParam.setDimensions(768);
        assertTrue(scannParam.isValid());
        
        // 测试无效的连接超时
        scannParam.setConnectionTimeout(0);
        assertFalse(scannParam.isValid());
        
        scannParam.setConnectionTimeout(-1);
        assertFalse(scannParam.isValid());
        
        // 恢复有效的连接超时
        scannParam.setConnectionTimeout(30000);
        assertTrue(scannParam.isValid());
        
        // 测试无效的读取超时
        scannParam.setReadTimeout(0);
        assertFalse(scannParam.isValid());
        
        scannParam.setReadTimeout(-1);
        assertFalse(scannParam.isValid());
        
        // 恢复有效的读取超时
        scannParam.setReadTimeout(60000);
        assertTrue(scannParam.isValid());
        
        // 测试无效的最大连接数
        scannParam.setMaxConnections(0);
        assertFalse(scannParam.isValid());
        
        scannParam.setMaxConnections(-1);
        assertFalse(scannParam.isValid());
        
        // 恢复有效的最大连接数
        scannParam.setMaxConnections(100);
        assertTrue(scannParam.isValid());
        
        // 测试无效的训练样本大小
        scannParam.setTrainingSampleSize(0);
        assertFalse(scannParam.isValid());
        
        scannParam.setTrainingSampleSize(-1);
        assertFalse(scannParam.isValid());
        
        // 恢复有效的训练样本大小
        scannParam.setTrainingSampleSize(100000);
        assertTrue(scannParam.isValid());
        
        // 测试无效的叶子节点搜索数
        scannParam.setLeavesToSearch(0);
        assertFalse(scannParam.isValid());
        
        scannParam.setLeavesToSearch(-1);
        assertFalse(scannParam.isValid());
        
        // 恢复有效的叶子节点搜索数
        scannParam.setLeavesToSearch(100);
        assertTrue(scannParam.isValid());
        
        // 测试无效的重排序候选数
        scannParam.setReorderNumNeighbors(0);
        assertFalse(scannParam.isValid());
        
        scannParam.setReorderNumNeighbors(-1);
        assertFalse(scannParam.isValid());
        
        // 恢复有效的重排序候选数
        scannParam.setReorderNumNeighbors(1000);
        assertTrue(scannParam.isValid());
    }

    @Test
    @DisplayName("测试URL构建")
    public void testGetFullUrl() {
        scannParam.setServerUrl("http://localhost:8080");
        
        // 测试带斜杠的端点
        assertEquals("http://localhost:8080/api/v1/test", scannParam.getFullUrl("/api/v1/test"));
        
        // 测试不带斜杠的端点
        assertEquals("http://localhost:8080/api/v1/test", scannParam.getFullUrl("api/v1/test"));
        
        // 测试服务器URL带尾部斜杠
        scannParam.setServerUrl("http://localhost:8080/");
        assertEquals("http://localhost:8080/api/v1/test", scannParam.getFullUrl("/api/v1/test"));
        assertEquals("http://localhost:8080/api/v1/test", scannParam.getFullUrl("api/v1/test"));
        
        // 测试复杂路径
        scannParam.setServerUrl("http://example.com:9090/scann");
        assertEquals("http://example.com:9090/scann/api/v1/indexes/test", 
                scannParam.getFullUrl("/api/v1/indexes/test"));
        
        // 测试根路径
        assertEquals("http://example.com:9090/scann/", scannParam.getFullUrl("/"));
        assertEquals("http://example.com:9090/scann/", scannParam.getFullUrl(""));
    }

    @Test
    @DisplayName("测试边界值")
    public void testBoundaryValues() {
        // 测试最小有效值
        scannParam.setDimensions(1);
        scannParam.setConnectionTimeout(1);
        scannParam.setReadTimeout(1);
        scannParam.setMaxConnections(1);
        scannParam.setTrainingSampleSize(1);
        scannParam.setLeavesToSearch(1);
        scannParam.setReorderNumNeighbors(1);
        
        assertTrue(scannParam.isValid());
        
        // 测试大值
        scannParam.setDimensions(10000);
        scannParam.setConnectionTimeout(300000);
        scannParam.setReadTimeout(600000);
        scannParam.setMaxConnections(1000);
        scannParam.setTrainingSampleSize(10000000);
        scannParam.setLeavesToSearch(10000);
        scannParam.setReorderNumNeighbors(100000);
        
        assertTrue(scannParam.isValid());
    }

    @Test
    @DisplayName("测试特殊字符和编码")
    public void testSpecialCharactersAndEncoding() {
        // 测试包含特殊字符的URL
        scannParam.setServerUrl("http://test-server.example.com:8080");
        assertTrue(scannParam.isValid());
        
        scannParam.setServerUrl("http://192.168.1.100:8080");
        assertTrue(scannParam.isValid());
        
        // 测试包含特殊字符的索引名称
        scannParam.setIndexName("test_index-v1.0");
        assertEquals("test_index-v1.0", scannParam.getIndexName());
        
        scannParam.setDatasetName("dataset_2024-01");
        assertEquals("dataset_2024-01", scannParam.getDatasetName());
    }

    @Test
    @DisplayName("测试配置组合")
    public void testConfigurationCombinations() {
        // 测试高性能配置
        scannParam.setIndexType("tree_ah");
        scannParam.setDistanceMeasure("dot_product");
        scannParam.setEnableReordering(true);
        scannParam.setEnableParallelSearch(true);
        scannParam.setSearchThreads(8);
        scannParam.setBuildThreads(16);
        scannParam.setEnablePrefetch(true);
        
        assertTrue(scannParam.isValid());
        
        // 测试内存优化配置
        scannParam.setQuantizationType("product");
        scannParam.setQuantizationDimensions(8);
        scannParam.setMemoryMappedFileSize(512L * 1024L * 1024L);
        scannParam.setBatchSize(100);
        
        assertTrue(scannParam.isValid());
        
        // 测试精度优化配置
        scannParam.setIndexType("brute_force");
        scannParam.setDistanceMeasure("cosine");
        scannParam.setEnableReordering(false);
        scannParam.setQuantizationType("none");
        
        assertTrue(scannParam.isValid());
    }
}
