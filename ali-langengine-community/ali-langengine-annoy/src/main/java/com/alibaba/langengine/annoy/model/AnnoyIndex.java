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

import java.io.File;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnoyIndex {

    /**
     * 索引唯一标识
     */
    private String indexId;

    /**
     * 索引名称
     */
    private String indexName;

    /**
     * 索引文件路径
     */
    private String indexPath;

    /**
     * 索引参数配置
     */
    private AnnoyParam param;

    /**
     * 索引状态
     */
    @Builder.Default
    private IndexStatus status = IndexStatus.CREATED;

    /**
     * 索引中的向量数量
     */
    @Builder.Default
    private AtomicInteger vectorCount = new AtomicInteger(0);

    /**
     * 索引文件大小（字节）
     */
    @Builder.Default
    private AtomicLong indexSize = new AtomicLong(0);

    /**
     * 索引创建时间
     */
    @Builder.Default
    private LocalDateTime createTime = LocalDateTime.now();

    /**
     * 索引最后更新时间
     */
    @Builder.Default
    private LocalDateTime updateTime = LocalDateTime.now();

    /**
     * 索引最后构建时间
     */
    private LocalDateTime lastBuildTime;

    /**
     * 是否已构建
     */
    @Builder.Default
    private boolean built = false;

    /**
     * 是否已加载到内存
     */
    @Builder.Default
    private boolean loaded = false;

    /**
     * 索引状态枚举
     */
    public enum IndexStatus {
        CREATED,     // 已创建
        BUILDING,    // 构建中
        BUILT,       // 已构建
        LOADING,     // 加载中
        LOADED,      // 已加载
        ERROR,       // 错误状态
        DESTROYED    // 已销毁
    }

    /**
     * 获取索引文件
     */
    public File getIndexFile() {
        return new File(indexPath);
    }

    /**
     * 检查索引文件是否存在
     */
    public boolean indexFileExists() {
        return getIndexFile().exists();
    }

    /**
     * 更新索引文件大小
     */
    public void updateIndexSize() {
        File file = getIndexFile();
        if (file.exists()) {
            indexSize.set(file.length());
        }
    }

    /**
     * 增加向量数量
     */
    public int incrementVectorCount() {
        updateTime = LocalDateTime.now();
        return vectorCount.incrementAndGet();
    }

    /**
     * 增加向量数量（批量）
     */
    public int addVectorCount(int count) {
        updateTime = LocalDateTime.now();
        return vectorCount.addAndGet(count);
    }

    /**
     * 设置构建完成状态
     */
    public void setBuildCompleted() {
        this.built = true;
        this.status = IndexStatus.BUILT;
        this.lastBuildTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        updateIndexSize();
    }

    /**
     * 设置加载完成状态
     */
    public void setLoadCompleted() {
        this.loaded = true;
        this.status = IndexStatus.LOADED;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 设置错误状态
     */
    public void setErrorStatus() {
        this.status = IndexStatus.ERROR;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 重置索引状态
     */
    public void reset() {
        this.built = false;
        this.loaded = false;
        this.status = IndexStatus.CREATED;
        this.vectorCount.set(0);
        this.indexSize.set(0);
        this.lastBuildTime = null;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 检查索引是否可用
     */
    public boolean isAvailable() {
        return built && loaded && status == IndexStatus.LOADED && indexFileExists();
    }

    /**
     * 获取索引信息摘要
     */
    public String getSummary() {
        return String.format("AnnoyIndex{id='%s', name='%s', status=%s, vectors=%d, size=%d bytes, built=%s, loaded=%s}",
                indexId, indexName, status, vectorCount.get(), indexSize.get(), built, loaded);
    }

    /**
     * 创建新的索引实例
     */
    public static AnnoyIndex create(String indexId, String indexName, String indexPath, AnnoyParam param) {
        return AnnoyIndex.builder()
                .indexId(indexId)
                .indexName(indexName)
                .indexPath(indexPath)
                .param(param)
                .build();
    }
}
