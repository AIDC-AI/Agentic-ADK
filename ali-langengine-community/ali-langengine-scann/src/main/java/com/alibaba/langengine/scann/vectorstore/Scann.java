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

import com.alibaba.fastjson.JSON;
import com.alibaba.langengine.core.embeddings.Embeddings;
import com.alibaba.langengine.core.indexes.Document;
import com.alibaba.langengine.core.vectorstore.VectorStore;
import com.alibaba.langengine.scann.exception.*;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.alibaba.langengine.scann.ScannConfiguration.SCANN_SERVER_URL;


@Slf4j
@Data
public class Scann extends VectorStore {

    /**
     * embedding模型
     */
    private Embeddings embedding;

    /**
     * 索引名称
     */
    private final String indexName;

    /**
     * ScaNN 参数配置
     */
    private final ScannParam scannParam;

    /**
     * ScaNN 服务实例
     */
    private ScannService scannService;

    /**
     * 构造函数
     *
     * @param indexName 索引名称
     */
    public Scann(String indexName) {
        this(indexName, new ScannParam());
    }

    /**
     * 构造函数
     *
     * @param indexName 索引名称
     * @param scannParam ScaNN 参数配置
     */
    public Scann(String indexName, ScannParam scannParam) {
        if (StringUtils.isEmpty(indexName)) {
            throw new IllegalArgumentException("Index name cannot be empty");
        }
        
        this.indexName = indexName;
        this.scannParam = scannParam != null ? scannParam : new ScannParam();

        // 只有在没有传入 scannParam 时才从环境变量获取服务器地址
        if (scannParam == null && StringUtils.isNotEmpty(SCANN_SERVER_URL)) {
            this.scannParam.setServerUrl(SCANN_SERVER_URL);
        }
        
        // 验证参数
        if (!this.scannParam.isValid()) {
            throw new IllegalArgumentException("Invalid ScaNN parameters");
        }
        
        // 初始化服务
        this.scannService = new ScannService(indexName, this.scannParam);
        
        log.info("Initialized ScaNN vector store with index: {}, server: {}", 
                indexName, this.scannParam.getServerUrl());
    }

    /**
     * 构造函数
     *
     * @param indexName 索引名称
     * @param serverUrl 服务器地址
     * @param dimensions 向量维度
     */
    public Scann(String indexName, String serverUrl, int dimensions) {
        this(indexName, new ScannParam(serverUrl, dimensions));
    }

    /**
     * 构造函数
     *
     * @param indexName 索引名称
     * @param serverUrl 服务器地址
     * @param dimensions 向量维度
     * @param indexType 索引类型
     * @param distanceMeasure 距离度量类型
     */
    public Scann(String indexName, String serverUrl, int dimensions, String indexType, String distanceMeasure) {
        this(indexName, new ScannParam(serverUrl, dimensions, indexType, distanceMeasure));
    }

    /**
     * 初始化 ScaNN 索引
     * 
     * 该方法会：
     * 1. 检查索引是否存在
     * 2. 如果不存在则创建新索引
     * 3. 配置索引参数（维度、距离度量等）
     */
    public void init() {
        try {
            log.info("Initializing ScaNN vector store for index: {}", indexName);
            scannService.init();
            log.info("ScaNN vector store initialized successfully for index: {}", indexName);
        } catch (Exception e) {
            log.error("Failed to initialize ScaNN vector store for index: {}", indexName, e);
            throw new RuntimeException("Failed to initialize ScaNN vector store", e);
        }
    }

    /**
     * 添加文档到向量存储
     * 
     * 该方法会：
     * 1. 使用 embedding 模型为文档生成向量
     * 2. 为没有唯一ID的文档生成UUID
     * 3. 将文档和向量存储到 ScaNN 索引中
     *
     * @param documents 要添加的文档列表
     */
    @Override
    public void addDocuments(List<Document> documents) {
        if (CollectionUtils.isEmpty(documents)) {
            log.debug("No documents to add");
            return;
        }
        
        try {
            log.info("Adding {} documents to ScaNN index: {}", documents.size(), indexName);
            
            // 为没有唯一ID的文档生成UUID
            for (Document document : documents) {
                if (StringUtils.isEmpty(document.getUniqueId())) {
                    document.setUniqueId(UUID.randomUUID().toString());
                }
            }
            
            // 使用 embedding 模型生成向量
            if (embedding != null) {
                documents = embedding.embedDocument(documents);
            } else {
                log.warn("No embedding model configured, documents should already have embeddings");
            }
            
            // 验证文档是否有向量
            for (Document document : documents) {
                if (CollectionUtils.isEmpty(document.getEmbedding())) {
                    throw new IllegalArgumentException("Document must have embedding vector: " + document.getUniqueId());
                }
            }
            
            // 添加到 ScaNN 索引
            scannService.addDocuments(documents);
            
            log.info("Successfully added {} documents to ScaNN index: {}", documents.size(), indexName);
        } catch (Exception e) {
            log.error("Failed to add documents to ScaNN index: {}", indexName, e);
            throw new RuntimeException("Failed to add documents to ScaNN index", e);
        }
    }

    /**
     * 执行相似性搜索
     * 
     * 该方法会：
     * 1. 使用 embedding 模型为查询文本生成向量
     * 2. 在 ScaNN 索引中搜索最相似的向量
     * 3. 返回对应的文档列表，按相似度排序
     *
     * @param query 查询文本
     * @param k 返回结果数量
     * @param maxDistanceValue 最大距离值（可选）
     * @param type 搜索类型（可选）
     * @return 最相似的文档列表
     */
    @Override
    public List<Document> similaritySearch(String query, int k, Double maxDistanceValue, Integer type) {
        if (StringUtils.isEmpty(query)) {
            log.debug("Empty query provided");
            return Lists.newArrayList();
        }
        
        if (k <= 0) {
            log.debug("Invalid k value: {}", k);
            return Lists.newArrayList();
        }
        
        try {
            log.debug("Performing similarity search in ScaNN index: {} with query: '{}', k: {}", 
                    indexName, query, k);
            
            // 使用 embedding 模型生成查询向量
            if (embedding == null) {
                log.error("No embedding model configured for similarity search");
                throw new ScannSearchException("No embedding model configured for similarity search");
            }
            
            List<String> embeddingStrings = embedding.embedQuery(query, k);
            if (CollectionUtils.isEmpty(embeddingStrings) || !embeddingStrings.get(0).startsWith("[")) {
                log.warn("Failed to generate embedding for query: {}", query);
                return Lists.newArrayList();
            }
            
            // 解析向量
            List<Double> queryVector = JSON.parseArray(embeddingStrings.get(0), Double.class);
            if (CollectionUtils.isEmpty(queryVector)) {
                log.warn("Empty query vector generated for query: {}", query);
                return Lists.newArrayList();
            }
            
            // 执行搜索
            List<Document> results = scannService.similaritySearch(queryVector, k, maxDistanceValue);
            
            log.debug("Found {} similar documents for query: '{}'", results.size(), query);
            return results;
            
        } catch (ScannSearchException e) {
            throw e; // Re-throw ScaNN specific exceptions
        } catch (Exception e) {
            log.error("Unexpected error during similarity search in ScaNN index: {}", indexName, e);
            throw new ScannSearchException("Failed to perform similarity search in ScaNN index: " + indexName, e);
        }
    }

    /**
     * 删除文档
     *
     * @param documentIds 要删除的文档ID列表
     */
    public void deleteDocuments(List<String> documentIds) {
        if (CollectionUtils.isEmpty(documentIds)) {
            return;
        }
        
        try {
            log.info("Deleting {} documents from ScaNN index: {}", documentIds.size(), indexName);
            scannService.deleteDocuments(documentIds);
            log.info("Successfully deleted {} documents from ScaNN index: {}", documentIds.size(), indexName);
        } catch (Exception e) {
            log.error("Failed to delete documents from ScaNN index: {}", indexName, e);
            throw new RuntimeException("Failed to delete documents from ScaNN index", e);
        }
    }

    /**
     * 获取索引统计信息
     *
     * @return 索引统计信息
     */
    public Map<String, Object> getIndexStats() {
        try {
            return scannService.getIndexStats();
        } catch (Exception e) {
            log.error("Failed to get index stats for ScaNN index: {}", indexName, e);
            throw new RuntimeException("Failed to get index stats", e);
        }
    }

    /**
     * 关闭向量存储，释放资源
     */
    public void close() {
        try {
            if (scannService != null) {
                scannService.close();
            }
            log.info("ScaNN vector store closed for index: {}", indexName);
        } catch (Exception e) {
            log.error("Failed to close ScaNN vector store for index: {}", indexName, e);
        }
    }
}
