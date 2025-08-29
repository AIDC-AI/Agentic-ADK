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
package com.alibaba.langengine.annoy.vectorstore;

import com.alibaba.fastjson2.JSON;
import com.alibaba.langengine.annoy.model.AnnoyIndex;
import com.alibaba.langengine.annoy.model.AnnoyParam;
import com.alibaba.langengine.annoy.model.AnnoySearchResult;
import com.alibaba.langengine.annoy.service.AnnoyService;
import com.alibaba.langengine.annoy.exception.AnnoyException;
import com.alibaba.langengine.core.embeddings.Embeddings;
import com.alibaba.langengine.core.indexes.Document;
import com.alibaba.langengine.core.vectorstore.VectorStore;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.alibaba.langengine.annoy.AnnoyConfiguration.*;


@Slf4j
@Data
@EqualsAndHashCode(callSuper=false)
public class Annoy extends VectorStore {

    /**
     * 向量库的embedding
     */
    private Embeddings embedding;

    /**
     * 索引标识符
     */
    private String indexId;

    /**
     * Annoy参数配置
     */
    private AnnoyParam param;

    /**
     * Annoy服务实例
     */
    private AnnoyService annoyService;

    /**
     * 是否自动构建索引
     */
    private boolean autoBuild = true;

    /**
     * 是否自动加载索引
     */
    private boolean autoLoad = true;

    /**
     * 构造函数
     */
    public Annoy(Embeddings embedding) {
        this(embedding, null, AnnoyParam.defaultParam());
    }

    /**
     * 构造函数
     */
    public Annoy(Embeddings embedding, String indexId) {
        this(embedding, indexId, AnnoyParam.defaultParam());
    }

    /**
     * 构造函数
     */
    public Annoy(Embeddings embedding, String indexId, AnnoyParam param) {
        this.embedding = embedding;
        this.indexId = StringUtils.isEmpty(indexId) ? UUID.randomUUID().toString() : indexId;
        this.param = param;
        this.annoyService = new AnnoyService();
        
        // 初始化服务
        this.annoyService.initialize();
        
        log.info("Initialized Annoy vector store with indexId: {}", this.indexId);
    }

    /**
     * 初始化索引
     * 如果索引不存在则创建，如果存在则加载
     */
    public void init() {
        try {
            AnnoyIndex index = annoyService.getIndex(indexId);
            if (index == null) {
                // 创建新索引
                index = annoyService.createIndex(indexId, param);
                log.info("Created new Annoy index: {}", indexId);
            } else if (index.indexFileExists() && autoLoad) {
                // 加载已存在的索引
                if (!index.isLoaded()) {
                    annoyService.loadIndex(indexId);
                    log.info("Loaded existing Annoy index: {}", indexId);
                }
            }
        } catch (Exception e) {
            log.error("Failed to initialize Annoy index: {}", indexId, e);
            throw new AnnoyException("Failed to initialize Annoy index: " + indexId, e);
        }
    }

    @Override
    public void addDocuments(List<Document> documents) {
        if (CollectionUtils.isEmpty(documents)) {
            return;
        }

        try {
            // 确保索引已初始化
            init();

            // 为没有uniqueId的文档生成UUID
            for (Document document : documents) {
                if (StringUtils.isEmpty(document.getUniqueId())) {
                    document.setUniqueId(UUID.randomUUID().toString());
                }
            }

            // 使用embedding模型生成向量
            documents = embedding.embedDocument(documents);

            // 添加到Annoy索引
            annoyService.addDocuments(documents, indexId);

            // 如果启用自动构建，则构建索引
            if (autoBuild) {
                buildIndex();
            }

            log.info("Added {} documents to Annoy index: {}", documents.size(), indexId);

        } catch (Exception e) {
            log.error("Failed to add documents to Annoy index: {}", indexId, e);
            throw new AnnoyException("Failed to add documents to Annoy index: " + indexId, e);
        }
    }

    @Override
    public List<Document> similaritySearch(String query, int k, Double maxDistanceValue, Integer type) {
        if (StringUtils.isEmpty(query)) {
            return Lists.newArrayList();
        }

        try {
            // 确保索引已初始化和加载
            ensureIndexReady();

            // 使用embedding模型生成查询向量
            // 注意：这里的k参数对于embedding服务来说通常不是直接相关的，但保持接口一致性
            List<String> embeddingStrings = embedding.embedQuery(query, k);
            if (CollectionUtils.isEmpty(embeddingStrings) || !embeddingStrings.get(0).startsWith("[")) {
                return Lists.newArrayList();
            }

            // 解析向量
            List<Float> queryVector = JSON.parseArray(embeddingStrings.get(0), Float.class);

            // 执行相似性搜索
            List<AnnoySearchResult> searchResults = annoyService.similaritySearch(queryVector, k, indexId);

            // 转换为Document列表
            List<Document> results = new ArrayList<>();
            for (AnnoySearchResult result : searchResults) {
                // 应用距离过滤
                if (maxDistanceValue != null && result.getDistance() > maxDistanceValue) {
                    continue;
                }

                Document document = annoyService.getDocumentByVectorId(result.getVectorId(), indexId);
                if (document != null) {
                    // 确保元数据不为null
                    if (document.getMetadata() == null) {
                        document.setMetadata(new HashMap<>());
                    }
                    // 设置相似度分数
                    document.getMetadata().put("similarity", result.getSimilarity());
                    document.getMetadata().put("distance", result.getDistance());
                    results.add(document);
                }
            }

            log.debug("Found {} similar documents for query in index: {}", results.size(), indexId);
            return results;

        } catch (Exception e) {
            log.error("Failed to perform similarity search in Annoy index: {}", indexId, e);
            throw new AnnoyException("Failed to perform similarity search in Annoy index: " + indexId, e);
        }
    }

    /**
     * 构建索引
     */
    public void buildIndex() {
        try {
            annoyService.buildIndex(indexId);
            
            // 如果启用自动加载，则加载索引
            if (autoLoad) {
                loadIndex();
            }
            
            log.info("Built Annoy index: {}", indexId);
        } catch (Exception e) {
            log.error("Failed to build Annoy index: {}", indexId, e);
            throw new AnnoyException("Failed to build Annoy index: " + indexId, e);
        }
    }

    /**
     * 加载索引
     */
    public void loadIndex() {
        try {
            annoyService.loadIndex(indexId);
            log.info("Loaded Annoy index: {}", indexId);
        } catch (Exception e) {
            log.error("Failed to load Annoy index: {}", indexId, e);
            throw new AnnoyException("Failed to load Annoy index: " + indexId, e);
        }
    }

    /**
     * 确保索引准备就绪
     */
    private void ensureIndexReady() {
        AnnoyIndex index = annoyService.getIndex(indexId);
        if (index == null) {
            throw new AnnoyException("Index not found: " + indexId);
        }

        if (!index.isBuilt()) {
            if (autoBuild) {
                buildIndex();
            } else {
                throw new AnnoyException("Index not built: " + indexId);
            }
        }

        if (!index.isLoaded()) {
            if (autoLoad) {
                loadIndex();
            } else {
                throw new AnnoyException("Index not loaded: " + indexId);
            }
        }
    }

    /**
     * 获取索引信息
     */
    public AnnoyIndex getIndexInfo() {
        return annoyService.getIndex(indexId);
    }

    /**
     * 获取文档数量
     */
    public int getDocumentCount() {
        AnnoyIndex index = annoyService.getIndex(indexId);
        return index != null ? index.getVectorCount().get() : 0;
    }

    /**
     * 清空索引
     */
    public void clearIndex() {
        try {
            annoyService.deleteIndex(indexId);
            // 重新创建索引
            annoyService.createIndex(indexId, param);
            log.info("Cleared Annoy index: {}", indexId);
        } catch (Exception e) {
            log.error("Failed to clear Annoy index: {}", indexId, e);
            throw new AnnoyException("Failed to clear Annoy index: " + indexId, e);
        }
    }

    /**
     * 删除索引
     */
    public void deleteIndex() {
        try {
            annoyService.deleteIndex(indexId);
            log.info("Deleted Annoy index: {}", indexId);
        } catch (Exception e) {
            log.error("Failed to delete Annoy index: {}", indexId, e);
            throw new AnnoyException("Failed to delete Annoy index: " + indexId, e);
        }
    }

    /**
     * 检查索引是否存在
     */
    public boolean indexExists() {
        AnnoyIndex index = annoyService.getIndex(indexId);
        return index != null && index.indexFileExists();
    }

    /**
     * 获取索引状态
     */
    public AnnoyIndex.IndexStatus getIndexStatus() {
        AnnoyIndex index = annoyService.getIndex(indexId);
        return index != null ? index.getStatus() : null;
    }

    /**
     * 设置自动构建
     */
    public void setAutoBuild(boolean autoBuild) {
        this.autoBuild = autoBuild;
    }

    /**
     * 设置自动加载
     */
    public void setAutoLoad(boolean autoLoad) {
        this.autoLoad = autoLoad;
    }
}
