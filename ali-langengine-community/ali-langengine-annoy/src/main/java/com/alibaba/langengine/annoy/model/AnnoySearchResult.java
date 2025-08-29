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

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnoySearchResult {

    /**
     * 向量ID
     */
    private Integer vectorId;

    /**
     * 距离值
     */
    private Float distance;

    /**
     * 相似度分数（1 - distance，仅对归一化距离有效）
     */
    private Float similarity;

    /**
     * 向量数据（可选）
     */
    private List<Float> vector;

    /**
     * 元数据（可选）
     */
    private Object metadata;

    /**
     * 创建搜索结果
     */
    public static AnnoySearchResult create(Integer vectorId, Float distance) {
        return AnnoySearchResult.builder()
                .vectorId(vectorId)
                .distance(distance)
                .similarity(1.0f - distance)
                .build();
    }

    /**
     * 创建带向量数据的搜索结果
     */
    public static AnnoySearchResult create(Integer vectorId, Float distance, List<Float> vector) {
        return AnnoySearchResult.builder()
                .vectorId(vectorId)
                .distance(distance)
                .similarity(1.0f - distance)
                .vector(vector)
                .build();
    }

    /**
     * 创建完整的搜索结果
     */
    public static AnnoySearchResult create(Integer vectorId, Float distance, List<Float> vector, Object metadata) {
        return AnnoySearchResult.builder()
                .vectorId(vectorId)
                .distance(distance)
                .similarity(1.0f - distance)
                .vector(vector)
                .metadata(metadata)
                .build();
    }

    /**
     * 计算相似度分数
     */
    public void calculateSimilarity(String distanceMetric) {
        if (distance == null) {
            this.similarity = 0.0f;
            return;
        }

        switch (distanceMetric.toLowerCase()) {
            case "angular":
            case "cosine":
                // Angular distance is already normalized between 0 and 2
                this.similarity = Math.max(0.0f, 1.0f - distance / 2.0f);
                break;
            case "euclidean":
                // For euclidean, we use 1/(1+distance) to get similarity
                this.similarity = 1.0f / (1.0f + distance);
                break;
            case "manhattan":
                // For manhattan, we use 1/(1+distance) to get similarity
                this.similarity = 1.0f / (1.0f + distance);
                break;
            case "dot":
                // For dot product, if vectors are normalized, distance is typically -dot_product
                // Convert to similarity in [0, 1] range: (1 + dot_product) / 2
                // Since distance = -dot_product, similarity = (1 - distance) / 2
                this.similarity = Math.max(0.0f, Math.min(1.0f, (1.0f - distance) / 2.0f));
                break;
            case "hamming":
                // Hamming distance is integer, normalize by vector dimension
                this.similarity = Math.max(0.0f, 1.0f - distance);
                break;
            default:
                // Default: assume distance is normalized
                this.similarity = Math.max(0.0f, 1.0f - distance);
        }
    }

    /**
     * 检查结果是否有效
     */
    public boolean isValid() {
        return vectorId != null && vectorId >= 0 && distance != null && distance >= 0;
    }

    /**
     * 获取结果摘要
     */
    public String getSummary() {
        return String.format("AnnoySearchResult{id=%d, distance=%.4f, similarity=%.4f}", 
                vectorId, distance, similarity);
    }

    @Override
    public String toString() {
        return getSummary();
    }
}
