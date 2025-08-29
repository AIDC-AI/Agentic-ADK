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

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import static com.alibaba.langengine.annoy.AnnoyConfiguration.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnoyParam {

    /**
     * 向量维度
     */
    @Builder.Default
    private Integer vectorDimension = ANNOY_VECTOR_DIMENSION;

    /**
     * 距离度量类型
     * 支持: angular, euclidean, manhattan, hamming, dot
     */
    @Builder.Default
    private String distanceMetric = ANNOY_DISTANCE_METRIC;

    /**
     * 构建索引时的树数量
     * 更多的树意味着更好的精度但更大的内存使用
     */
    @Builder.Default
    private Integer nTrees = ANNOY_N_TREES;

    /**
     * 搜索时检查的节点数
     * -1表示使用默认值（n_trees * n）
     * 更多的节点意味着更好的精度但更慢的搜索
     */
    @Builder.Default
    private Integer searchK = ANNOY_SEARCH_K;

    /**
     * 是否启用内存映射模式
     */
    @Builder.Default
    private Boolean mmapEnabled = ANNOY_MMAP_ENABLED;

    /**
     * 索引构建超时时间（秒）
     */
    @Builder.Default
    private Long buildTimeout = ANNOY_BUILD_TIMEOUT;

    /**
     * 是否启用索引预加载
     */
    @Builder.Default
    private Boolean preloadEnabled = ANNOY_PRELOAD_ENABLED;

    /**
     * 批量添加向量的批次大小
     */
    @Builder.Default
    private Integer batchSize = ANNOY_BATCH_SIZE;

    /**
     * 是否启用索引自动保存
     */
    @Builder.Default
    private Boolean autoSave = ANNOY_AUTO_SAVE;

    /**
     * 索引自动保存间隔（秒）
     */
    @Builder.Default
    private Long autoSaveInterval = ANNOY_AUTO_SAVE_INTERVAL;

    /**
     * 最大索引文件大小（MB）
     */
    @Builder.Default
    private Long maxIndexSize = ANNOY_MAX_INDEX_SIZE;

    /**
     * 是否启用并发构建
     */
    @Builder.Default
    private Boolean concurrentBuild = ANNOY_CONCURRENT_BUILD;

    /**
     * 并发构建线程数
     */
    @Builder.Default
    private Integer buildThreads = ANNOY_BUILD_THREADS;

    /**
     * 验证参数有效性
     */
    public void validate() {
        com.alibaba.langengine.annoy.util.AnnoyUtils.validatePositiveInteger(vectorDimension, "Vector dimension");
        com.alibaba.langengine.annoy.util.AnnoyUtils.validateDistanceMetric(distanceMetric);
        com.alibaba.langengine.annoy.util.AnnoyUtils.validatePositiveInteger(nTrees, "Number of trees");
        com.alibaba.langengine.annoy.util.AnnoyUtils.validatePositiveInteger(batchSize, "Batch size");
        com.alibaba.langengine.annoy.util.AnnoyUtils.validatePositiveLong(buildTimeout, "Build timeout");
        com.alibaba.langengine.annoy.util.AnnoyUtils.validatePositiveInteger(buildThreads, "Build threads");
    }

    /**
     * 创建默认参数
     */
    public static AnnoyParam defaultParam() {
        return AnnoyParam.builder().build();
    }

    /**
     * 创建自定义参数
     */
    public static AnnoyParam customParam(Integer vectorDimension, String distanceMetric) {
        return AnnoyParam.builder()
                .vectorDimension(vectorDimension)
                .distanceMetric(distanceMetric)
                .build();
    }
}
