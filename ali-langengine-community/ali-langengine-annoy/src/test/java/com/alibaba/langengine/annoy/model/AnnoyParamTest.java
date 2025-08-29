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
package com.alibaba.langengine.annoy.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Annoy参数测试")
class AnnoyParamTest {

    @Test
    @DisplayName("测试默认参数创建")
    void testDefaultParamCreation() {
        AnnoyParam param = AnnoyParam.defaultParam();
        
        assertNotNull(param);
        assertEquals(Integer.valueOf(1536), param.getVectorDimension());
        assertEquals("angular", param.getDistanceMetric());
        assertEquals(Integer.valueOf(10), param.getNTrees());
        assertEquals(Integer.valueOf(-1), param.getSearchK());
        assertTrue(param.getMmapEnabled());
        assertTrue(param.getPreloadEnabled());
        assertTrue(param.getAutoSave());
        assertFalse(param.getConcurrentBuild());
    }

    @Test
    @DisplayName("测试自定义参数创建")
    void testCustomParamCreation() {
        AnnoyParam param = AnnoyParam.customParam(768, "euclidean");
        
        assertNotNull(param);
        assertEquals(Integer.valueOf(768), param.getVectorDimension());
        assertEquals("euclidean", param.getDistanceMetric());
        // 其他参数应该使用默认值
        assertEquals(Integer.valueOf(10), param.getNTrees());
        assertEquals(Integer.valueOf(-1), param.getSearchK());
    }

    @Test
    @DisplayName("测试Builder模式创建参数")
    void testBuilderPatternCreation() {
        AnnoyParam param = AnnoyParam.builder()
                .vectorDimension(512)
                .distanceMetric("manhattan")
                .nTrees(20)
                .searchK(100)
                .mmapEnabled(false)
                .batchSize(500)
                .buildTimeout(600L)
                .build();
        
        assertNotNull(param);
        assertEquals(Integer.valueOf(512), param.getVectorDimension());
        assertEquals("manhattan", param.getDistanceMetric());
        assertEquals(Integer.valueOf(20), param.getNTrees());
        assertEquals(Integer.valueOf(100), param.getSearchK());
        assertFalse(param.getMmapEnabled());
        assertEquals(Integer.valueOf(500), param.getBatchSize());
        assertEquals(Long.valueOf(600), param.getBuildTimeout());
    }

    @Test
    @DisplayName("测试参数验证 - 有效参数")
    void testValidParameterValidation() {
        AnnoyParam param = AnnoyParam.builder()
                .vectorDimension(128)
                .distanceMetric("angular")
                .nTrees(5)
                .batchSize(100)
                .buildTimeout(120L)
                .buildThreads(2)
                .build();
        
        // 应该不抛出异常
        assertDoesNotThrow(param::validate);
    }

    @Test
    @DisplayName("测试参数验证 - 无效向量维度")
    void testInvalidVectorDimensionValidation() {
        // 测试null向量维度
        AnnoyParam param1 = AnnoyParam.builder()
                .vectorDimension(null)
                .build();
        
        com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException exception1 = assertThrows(
                com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException.class,
                param1::validate
        );
        assertTrue(exception1.getMessage().contains("Vector dimension must be a positive integer"));

        // 测试负向量维度
        AnnoyParam param2 = AnnoyParam.builder()
                .vectorDimension(-1)
                .build();

        com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException exception2 = assertThrows(
                com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException.class,
                param2::validate
        );
        assertTrue(exception2.getMessage().contains("Vector dimension must be a positive integer"));

        // 测试零向量维度
        AnnoyParam param3 = AnnoyParam.builder()
                .vectorDimension(0)
                .build();

        com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException exception3 = assertThrows(
                com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException.class,
                param3::validate
        );
        assertTrue(exception3.getMessage().contains("Vector dimension must be a positive integer"));
    }

    @Test
    @DisplayName("测试参数验证 - 无效距离度量")
    void testInvalidDistanceMetricValidation() {
        // 测试null距离度量
        AnnoyParam param1 = AnnoyParam.builder()
                .distanceMetric(null)
                .build();
        
        com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException exception1 = assertThrows(
                com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException.class,
                param1::validate
        );
        assertTrue(exception1.getMessage().contains("Distance metric cannot be null or empty"));

        // 测试空距离度量
        AnnoyParam param2 = AnnoyParam.builder()
                .distanceMetric("")
                .build();

        com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException exception2 = assertThrows(
                com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException.class,
                param2::validate
        );
        assertTrue(exception2.getMessage().contains("Distance metric cannot be null or empty"));

        // 测试无效距离度量
        AnnoyParam param3 = AnnoyParam.builder()
                .distanceMetric("invalid_metric")
                .build();

        com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException exception3 = assertThrows(
                com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException.class,
                param3::validate
        );
        assertTrue(exception3.getMessage().contains("Invalid distance metric"));
    }

    @Test
    @DisplayName("测试参数验证 - 有效距离度量")
    void testValidDistanceMetrics() {
        String[] validMetrics = {"angular", "euclidean", "manhattan", "hamming", "dot"};
        
        for (String metric : validMetrics) {
            AnnoyParam param = AnnoyParam.builder()
                    .distanceMetric(metric)
                    .build();
            
            assertDoesNotThrow(param::validate, 
                    "Distance metric '" + metric + "' should be valid");
        }

        // 测试大小写不敏感
        String[] caseVariations = {"ANGULAR", "Euclidean", "MaNhAtTaN", "HAMMING", "DoT"};
        
        for (String metric : caseVariations) {
            AnnoyParam param = AnnoyParam.builder()
                    .distanceMetric(metric)
                    .build();
            
            assertDoesNotThrow(param::validate, 
                    "Distance metric '" + metric + "' should be valid (case insensitive)");
        }
    }

    @Test
    @DisplayName("测试参数验证 - 无效树数量")
    void testInvalidNTreesValidation() {
        // 测试null树数量
        AnnoyParam param1 = AnnoyParam.builder()
                .nTrees(null)
                .build();
        
        com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException exception1 = assertThrows(
                com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException.class,
                param1::validate
        );
        assertTrue(exception1.getMessage().contains("Number of trees must be a positive integer"));

        // 测试负树数量
        AnnoyParam param2 = AnnoyParam.builder()
                .nTrees(-1)
                .build();

        com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException exception2 = assertThrows(
                com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException.class,
                param2::validate
        );
        assertTrue(exception2.getMessage().contains("Number of trees must be a positive integer"));

        // 测试零树数量
        AnnoyParam param3 = AnnoyParam.builder()
                .nTrees(0)
                .build();

        com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException exception3 = assertThrows(
                com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException.class,
                param3::validate
        );
        assertTrue(exception3.getMessage().contains("Number of trees must be a positive integer"));
    }

    @Test
    @DisplayName("测试参数验证 - 无效批次大小")
    void testInvalidBatchSizeValidation() {
        AnnoyParam param = AnnoyParam.builder()
                .batchSize(0)
                .build();
        
        com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException exception = assertThrows(
                com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException.class,
                param::validate
        );
        assertTrue(exception.getMessage().contains("Batch size must be a positive integer"));
    }

    @Test
    @DisplayName("测试参数验证 - 无效构建超时")
    void testInvalidBuildTimeoutValidation() {
        AnnoyParam param = AnnoyParam.builder()
                .buildTimeout(0L)
                .build();
        
        com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException exception = assertThrows(
                com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException.class,
                param::validate
        );
        assertTrue(exception.getMessage().contains("Build timeout must be a positive long"));
    }

    @Test
    @DisplayName("测试参数验证 - 无效构建线程数")
    void testInvalidBuildThreadsValidation() {
        AnnoyParam param = AnnoyParam.builder()
                .buildThreads(0)
                .build();
        
        com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException exception = assertThrows(
                com.alibaba.langengine.annoy.exception.AnnoyException.ParameterValidationException.class,
                param::validate
        );
        assertTrue(exception.getMessage().contains("Build threads must be a positive integer"));
    }

    @Test
    @DisplayName("测试参数的getter和setter")
    void testGettersAndSetters() {
        AnnoyParam param = new AnnoyParam();
        
        // 测试向量维度
        param.setVectorDimension(256);
        assertEquals(Integer.valueOf(256), param.getVectorDimension());
        
        // 测试距离度量
        param.setDistanceMetric("euclidean");
        assertEquals("euclidean", param.getDistanceMetric());
        
        // 测试树数量
        param.setNTrees(15);
        assertEquals(Integer.valueOf(15), param.getNTrees());
        
        // 测试搜索参数
        param.setSearchK(50);
        assertEquals(Integer.valueOf(50), param.getSearchK());
        
        // 测试布尔参数
        param.setMmapEnabled(false);
        assertFalse(param.getMmapEnabled());
        
        param.setPreloadEnabled(false);
        assertFalse(param.getPreloadEnabled());
        
        param.setAutoSave(false);
        assertFalse(param.getAutoSave());
        
        param.setConcurrentBuild(true);
        assertTrue(param.getConcurrentBuild());
    }
}
