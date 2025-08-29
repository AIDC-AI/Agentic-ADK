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
package com.alibaba.langengine.annoy.service;

import com.alibaba.langengine.annoy.model.AnnoyIndex;
import com.alibaba.langengine.annoy.model.AnnoyParam;
import com.alibaba.langengine.annoy.model.AnnoySearchResult;
import com.alibaba.langengine.annoy.exception.AnnoyException;
import com.alibaba.langengine.core.indexes.Document;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class AnnoyService {

    /**
     * 索引管理器
     */
    private final Map<String, AnnoyIndex> indexMap = new ConcurrentHashMap<>();

    /**
     * 向量ID计数器
     */
    private final AtomicInteger vectorIdCounter = new AtomicInteger(0);

    /**
     * 每个索引的文档ID到向量ID的映射
     */
    private final Map<String, Map<String, Integer>> indexDocumentIdToVectorId = new ConcurrentHashMap<>();

    /**
     * 每个索引的向量ID到文档的映射
     */
    private final Map<String, Map<Integer, Document>> indexVectorIdToDocument = new ConcurrentHashMap<>();

    /**
     * 每个索引的向量数据存储
     */
    private final Map<String, Map<Integer, List<Float>>> indexVectorData = new ConcurrentHashMap<>();

    /**
     * 默认索引ID
     */
    private static final String DEFAULT_INDEX_ID = "default";

    /**
     * 初始化服务
     */
    public void initialize() {
        log.info("Initializing AnnoyService...");
        // 创建索引存储目录
        createIndexDirectory();
        log.info("AnnoyService initialized successfully");
    }

    /**
     * 创建索引
     */
    public AnnoyIndex createIndex(String indexId, AnnoyParam param) {
        if (StringUtils.isEmpty(indexId)) {
            indexId = DEFAULT_INDEX_ID;
        }

        // 验证参数
        param.validate();

        // 生成索引文件路径
        String indexPath = generateIndexPath(indexId);

        // 创建索引对象
        AnnoyIndex index = AnnoyIndex.create(indexId, indexId, indexPath, param);

        // 存储索引
        indexMap.put(indexId, index);

        // 初始化索引相关的数据映射
        indexDocumentIdToVectorId.put(indexId, new ConcurrentHashMap<>());
        indexVectorIdToDocument.put(indexId, new ConcurrentHashMap<>());
        indexVectorData.put(indexId, new ConcurrentHashMap<>());

        log.info("Created Annoy index: {}", index.getSummary());
        return index;
    }

    /**
     * 获取或创建默认索引
     */
    public AnnoyIndex getOrCreateDefaultIndex(AnnoyParam param) {
        AnnoyIndex existingIndex = indexMap.get(DEFAULT_INDEX_ID);
        if (existingIndex != null) {
            return existingIndex;
        }

        // 直接创建索引，避免递归调用
        param.validate();
        String indexPath = generateIndexPath(DEFAULT_INDEX_ID);
        AnnoyIndex index = AnnoyIndex.create(DEFAULT_INDEX_ID, DEFAULT_INDEX_ID, indexPath, param);

        // 存储索引
        indexMap.put(DEFAULT_INDEX_ID, index);

        // 初始化索引相关的数据映射
        indexDocumentIdToVectorId.put(DEFAULT_INDEX_ID, new ConcurrentHashMap<>());
        indexVectorIdToDocument.put(DEFAULT_INDEX_ID, new ConcurrentHashMap<>());
        indexVectorData.put(DEFAULT_INDEX_ID, new ConcurrentHashMap<>());

        log.info("Created default Annoy index: {}", index.getSummary());
        return index;
    }

    /**
     * 添加文档向量
     */
    public void addDocuments(List<Document> documents, String indexId) {
        if (CollectionUtils.isEmpty(documents)) {
            return;
        }

        AnnoyIndex index = indexMap.get(indexId);
        if (index == null) {
            throw new AnnoyException.VectorAddException("Index not found: " + indexId);
        }

        try {
            for (Document document : documents) {
                addDocument(document, index);
            }
            
            // 标记索引需要重新构建
            index.setBuilt(false);
            index.setLoaded(false);
            
            log.info("Added {} documents to index {}", documents.size(), indexId);
        } catch (Exception e) {
            throw new AnnoyException.VectorAddException("Failed to add documents to index " + indexId, e);
        }
    }

    /**
     * 添加单个文档
     */
    private void addDocument(Document document, AnnoyIndex index) {
        if (document.getEmbedding() == null || document.getEmbedding().isEmpty()) {
            throw new AnnoyException.VectorAddException("Document embedding is null or empty");
        }

        // 检查向量维度
        List<Double> embedding = document.getEmbedding();
        if (embedding.size() != index.getParam().getVectorDimension()) {
            throw new AnnoyException.VectorAddException(
                String.format("Vector dimension mismatch: expected %d, got %d", 
                    index.getParam().getVectorDimension(), embedding.size()));
        }

        // 转换向量格式
        List<Float> vector = new ArrayList<>();
        for (Double d : embedding) {
            vector.add(d.floatValue());
        }

        // 生成向量ID
        int vectorId = vectorIdCounter.getAndIncrement();

        // 获取索引相关的数据映射
        Map<String, Integer> docToVectorMap = indexDocumentIdToVectorId.get(index.getIndexId());
        Map<Integer, Document> vectorToDocMap = indexVectorIdToDocument.get(index.getIndexId());
        Map<Integer, List<Float>> vectorDataMap = indexVectorData.get(index.getIndexId());

        // 存储映射关系
        if (StringUtils.isNotEmpty(document.getUniqueId())) {
            docToVectorMap.put(document.getUniqueId(), vectorId);
        }
        vectorToDocMap.put(vectorId, document);
        vectorDataMap.put(vectorId, vector);

        // 更新索引统计
        index.incrementVectorCount();
    }

    /**
     * 构建索引
     */
    public void buildIndex(String indexId) {
        AnnoyIndex index = indexMap.get(indexId);
        if (index == null) {
            throw new AnnoyException.IndexBuildException("Index not found: " + indexId);
        }

        if (index.getVectorCount().get() == 0) {
            throw new AnnoyException.IndexBuildException("No vectors to build index for: " + indexId);
        }

        try {
            index.setStatus(AnnoyIndex.IndexStatus.BUILDING);
            
            // 使用原生库构建索引
            log.info("Building index {} with {} vectors...", indexId, index.getVectorCount().get());
            buildIndexWithNativeLibrary(index);
            
            index.setBuildCompleted();
            log.info("Index {} built successfully", indexId);
            
        } catch (Exception e) {
            index.setErrorStatus();
            throw new AnnoyException.IndexBuildException("Failed to build index " + indexId, e);
        }
    }

    /**
     * 加载索引
     */
    public void loadIndex(String indexId) {
        AnnoyIndex index = indexMap.get(indexId);
        if (index == null) {
            throw new AnnoyException.IndexLoadException("Index not found: " + indexId);
        }

        if (!index.isBuilt()) {
            throw new AnnoyException.IndexLoadException("Index not built: " + indexId);
        }

        try {
            index.setStatus(AnnoyIndex.IndexStatus.LOADING);
            
            // 使用原生库加载索引
            log.info("Loading index {}...", indexId);
            loadIndexWithNativeLibrary(index);
            
            index.setLoadCompleted();
            log.info("Index {} loaded successfully", indexId);
            
        } catch (Exception e) {
            index.setErrorStatus();
            throw new AnnoyException.IndexLoadException("Failed to load index " + indexId, e);
        }
    }

    /**
     * 相似性搜索
     */
    public List<AnnoySearchResult> similaritySearch(List<Float> queryVector, int k, String indexId) {
        AnnoyIndex index = indexMap.get(indexId);
        if (index == null) {
            throw new AnnoyException.SearchException("Index not found: " + indexId);
        }

        if (!index.isAvailable()) {
            throw new AnnoyException.SearchException("Index not available: " + indexId);
        }

        if (queryVector.size() != index.getParam().getVectorDimension()) {
            throw new AnnoyException.SearchException(
                String.format("Query vector dimension mismatch: expected %d, got %d", 
                    index.getParam().getVectorDimension(), queryVector.size()));
        }

        try {
            // 使用原生库进行相似性搜索
            List<AnnoySearchResult> results = searchWithNativeLibrary(queryVector, k, index);
            
            log.debug("Found {} similar vectors for query in index {}", results.size(), indexId);
            return results;
            
        } catch (Exception e) {
            throw new AnnoyException.SearchException("Failed to search in index " + indexId, e);
        }
    }

    /**
     * 获取文档通过向量ID
     */
    public Document getDocumentByVectorId(Integer vectorId, String indexId) {
        Map<Integer, Document> vectorToDocMap = indexVectorIdToDocument.get(indexId);
        return vectorToDocMap != null ? vectorToDocMap.get(vectorId) : null;
    }

    /**
     * 获取向量ID通过文档ID
     */
    public Integer getVectorIdByDocumentId(String documentId, String indexId) {
        Map<String, Integer> docToVectorMap = indexDocumentIdToVectorId.get(indexId);
        return docToVectorMap != null ? docToVectorMap.get(documentId) : null;
    }

    /**
     * 获取文档通过向量ID（兼容性方法，搜索所有索引）
     */
    public Document getDocumentByVectorId(Integer vectorId) {
        for (Map<Integer, Document> vectorToDocMap : indexVectorIdToDocument.values()) {
            Document doc = vectorToDocMap.get(vectorId);
            if (doc != null) {
                return doc;
            }
        }
        return null;
    }

    /**
     * 获取向量ID通过文档ID（兼容性方法，搜索所有索引）
     */
    public Integer getVectorIdByDocumentId(String documentId) {
        for (Map<String, Integer> docToVectorMap : indexDocumentIdToVectorId.values()) {
            Integer vectorId = docToVectorMap.get(documentId);
            if (vectorId != null) {
                return vectorId;
            }
        }
        return null;
    }

    /**
     * 获取索引信息
     */
    public AnnoyIndex getIndex(String indexId) {
        return indexMap.get(indexId);
    }

    /**
     * 获取所有索引
     */
    public Collection<AnnoyIndex> getAllIndexes() {
        return indexMap.values();
    }

    /**
     * 删除索引
     */
    public void deleteIndex(String indexId) {
        AnnoyIndex index = indexMap.remove(indexId);
        if (index != null) {
            // 清理相关数据
            cleanupIndexData(index);
            
            // 删除索引文件
            File indexFile = index.getIndexFile();
            if (indexFile.exists()) {
                indexFile.delete();
            }
            
            log.info("Deleted index: {}", indexId);
        }
    }

    /**
     * 生成索引文件路径
     */
    private String generateIndexPath(String indexId) {
        return Paths.get(com.alibaba.langengine.annoy.AnnoyConfiguration.ANNOY_INDEX_PATH, 
                        com.alibaba.langengine.annoy.AnnoyConfiguration.ANNOY_INDEX_PREFIX + "_" + indexId + ".ann")
                    .toString();
    }

    /**
     * 创建索引存储目录
     */
    private void createIndexDirectory() {
        try {
            Files.createDirectories(Paths.get(com.alibaba.langengine.annoy.AnnoyConfiguration.ANNOY_INDEX_PATH));
        } catch (Exception e) {
            throw new AnnoyException.FileOperationException("Failed to create index directory", e);
        }
    }

    /**
     * 实际的索引构建实现
     */
    private void buildIndexWithNativeLibrary(AnnoyIndex index) {
        try {
            // 检查原生库是否可用
            if (!com.alibaba.langengine.annoy.native_.AnnoyNativeLibrary.isLibraryAvailable()) {
                log.warn("Annoy native library not available, falling back to simulation mode");
                try {
                    simulateIndexBuild(index);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new AnnoyException.IndexBuildException("Index build was interrupted", e);
                }
                return;
            }

            com.alibaba.langengine.annoy.native_.AnnoyNativeLibrary library =
                com.alibaba.langengine.annoy.native_.AnnoyNativeLibrary.INSTANCE;

            // 创建原生索引
            com.sun.jna.Pointer nativeIndex = library.annoy_create_index(
                index.getParam().getVectorDimension(),
                index.getParam().getDistanceMetric()
            );

            if (nativeIndex == null) {
                throw new AnnoyException.IndexBuildException("Failed to create native index");
            }

            try {
                // 获取该索引的向量数据
                Map<Integer, List<Float>> vectorDataMap = indexVectorData.get(index.getIndexId());

                // 添加所有向量到原生索引
                for (Map.Entry<Integer, List<Float>> entry : vectorDataMap.entrySet()) {
                    Integer vectorId = entry.getKey();
                    List<Float> vector = entry.getValue();

                    float[] vectorArray = new float[vector.size()];
                    for (int i = 0; i < vector.size(); i++) {
                        vectorArray[i] = vector.get(i);
                    }

                    if (!library.annoy_add_item(nativeIndex, vectorId, vectorArray)) {
                        throw new AnnoyException.IndexBuildException(
                            "Failed to add vector " + vectorId + " to native index");
                    }
                }

                // 构建索引
                if (!library.annoy_build(nativeIndex, index.getParam().getNTrees())) {
                    throw new AnnoyException.IndexBuildException("Failed to build native index");
                }

                // 保存索引到文件
                File indexFile = index.getIndexFile();
                indexFile.getParentFile().mkdirs();

                if (!library.annoy_save(nativeIndex, indexFile.getAbsolutePath())) {
                    throw new AnnoyException.IndexBuildException("Failed to save index to file");
                }

                log.info("Successfully built native Annoy index with {} vectors",
                        index.getVectorCount().get());

            } finally {
                // 清理原生资源
                library.annoy_destroy_index(nativeIndex);
            }

        } catch (Exception e) {
            if (e instanceof AnnoyException) {
                throw e;
            }
            throw new AnnoyException.IndexBuildException("Failed to build index with native library", e);
        }
    }

    /**
     * 实际的索引加载实现
     */
    private void loadIndexWithNativeLibrary(AnnoyIndex index) {
        try {
            // 检查原生库是否可用
            if (!com.alibaba.langengine.annoy.native_.AnnoyNativeLibrary.isLibraryAvailable()) {
                log.warn("Annoy native library not available, falling back to simulation mode");
                try {
                    simulateIndexLoad(index);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new AnnoyException.IndexLoadException("Index load was interrupted", e);
                }
                return;
            }

            com.alibaba.langengine.annoy.native_.AnnoyNativeLibrary library =
                com.alibaba.langengine.annoy.native_.AnnoyNativeLibrary.INSTANCE;

            // 创建原生索引
            com.sun.jna.Pointer nativeIndex = library.annoy_create_index(
                index.getParam().getVectorDimension(),
                index.getParam().getDistanceMetric()
            );

            if (nativeIndex == null) {
                throw new AnnoyException.IndexLoadException("Failed to create native index for loading");
            }

            try {
                // 从文件加载索引
                File indexFile = index.getIndexFile();
                if (!indexFile.exists()) {
                    throw new AnnoyException.IndexLoadException("Index file does not exist: " + indexFile.getAbsolutePath());
                }

                if (!library.annoy_load(nativeIndex, indexFile.getAbsolutePath())) {
                    throw new AnnoyException.IndexLoadException("Failed to load index from file");
                }

                // 验证加载的索引
                int loadedItems = library.annoy_get_n_items(nativeIndex);
                int expectedDimension = library.annoy_get_dimension(nativeIndex);

                if (expectedDimension != index.getParam().getVectorDimension()) {
                    throw new AnnoyException.IndexLoadException(
                        String.format("Dimension mismatch: expected %d, got %d",
                            index.getParam().getVectorDimension(), expectedDimension));
                }

                log.info("Successfully loaded native Annoy index with {} items, dimension {}",
                        loadedItems, expectedDimension);

                // 存储原生索引指针到索引对象中（注意：这需要在AnnoyIndex中添加相应字段）
                // index.setNativeIndexPointer(nativeIndex); // 这需要修改AnnoyIndex类

            } catch (Exception e) {
                // 如果加载失败，清理资源
                library.annoy_destroy_index(nativeIndex);
                throw e;
            }

        } catch (Exception e) {
            if (e instanceof AnnoyException) {
                throw e;
            }
            throw new AnnoyException.IndexLoadException("Failed to load index with native library", e);
        }
    }

    /**
     * 实际的相似性搜索实现
     */
    private List<AnnoySearchResult> searchWithNativeLibrary(List<Float> queryVector, int k, AnnoyIndex index) {
        try {
            // 检查原生库是否可用
            if (!com.alibaba.langengine.annoy.native_.AnnoyNativeLibrary.isLibraryAvailable()) {
                log.warn("Annoy native library not available, falling back to simulation mode");
                return simulateSearch(queryVector, k, index);
            }

            com.alibaba.langengine.annoy.native_.AnnoyNativeLibrary library =
                com.alibaba.langengine.annoy.native_.AnnoyNativeLibrary.INSTANCE;

            // 创建并加载索引（在实际实现中，应该重用已加载的索引）
            com.sun.jna.Pointer nativeIndex = library.annoy_create_index(
                index.getParam().getVectorDimension(),
                index.getParam().getDistanceMetric()
            );

            if (nativeIndex == null) {
                throw new AnnoyException.SearchException("Failed to create native index for search");
            }

            try {
                // 加载索引
                File indexFile = index.getIndexFile();
                if (!library.annoy_load(nativeIndex, indexFile.getAbsolutePath())) {
                    throw new AnnoyException.SearchException("Failed to load index for search");
                }

                // 准备查询向量
                float[] queryArray = new float[queryVector.size()];
                for (int i = 0; i < queryVector.size(); i++) {
                    queryArray[i] = queryVector.get(i);
                }

                // 准备结果数组
                int[] resultIds = new int[k];
                float[] distances = new float[k];

                int searchK = index.getParam().getSearchK();
                if (searchK <= 0) {
                    searchK = -1; // 使用默认值
                }

                // 执行搜索
                int actualResults = library.annoy_get_nns_by_vector(
                    nativeIndex, queryArray, k, searchK, resultIds, distances);

                // 构建结果列表
                List<AnnoySearchResult> results = new ArrayList<>();
                for (int i = 0; i < actualResults; i++) {
                    AnnoySearchResult result = AnnoySearchResult.create(resultIds[i], distances[i]);
                    result.calculateSimilarity(index.getParam().getDistanceMetric());
                    results.add(result);
                }

                log.debug("Native search returned {} results for query", actualResults);
                return results;

            } finally {
                // 清理原生资源
                library.annoy_destroy_index(nativeIndex);
            }

        } catch (Exception e) {
            if (e instanceof AnnoyException) {
                throw e;
            }
            throw new AnnoyException.SearchException("Failed to search with native library", e);
        }
    }

    /**
     * 模拟索引构建（备用方案）
     */
    private void simulateIndexBuild(AnnoyIndex index) throws InterruptedException {
        log.info("Using simulation mode for index building");
        // 模拟构建时间
        Thread.sleep(100);

        // 创建索引文件
        try {
            File indexFile = index.getIndexFile();
            indexFile.getParentFile().mkdirs();
            indexFile.createNewFile();
        } catch (Exception e) {
            throw new AnnoyException.IndexBuildException("Failed to create index file", e);
        }
    }

    /**
     * 模拟索引加载（备用方案）
     */
    private void simulateIndexLoad(AnnoyIndex index) throws InterruptedException {
        log.info("Using simulation mode for index loading");
        // 模拟加载时间
        Thread.sleep(50);
    }

    /**
     * 模拟相似性搜索（备用方案）
     */
    private List<AnnoySearchResult> simulateSearch(List<Float> queryVector, int k, AnnoyIndex index) {
        log.debug("Using simulation mode for similarity search");
        List<AnnoySearchResult> results = new ArrayList<>();

        // 获取该索引的向量数据
        Map<Integer, List<Float>> vectorDataMap = indexVectorData.get(index.getIndexId());
        if (vectorDataMap == null || vectorDataMap.isEmpty()) {
            return results;
        }

        // 简单的线性搜索实现（实际中会使用Annoy的近似搜索）
        List<Map.Entry<Integer, Float>> distances = new ArrayList<>();

        for (Map.Entry<Integer, List<Float>> entry : vectorDataMap.entrySet()) {
            float distance = calculateDistance(queryVector, entry.getValue(), index.getParam().getDistanceMetric());
            distances.add(new AbstractMap.SimpleEntry<>(entry.getKey(), distance));
        }

        // 排序并取前k个
        distances.sort(Map.Entry.comparingByValue());

        for (int i = 0; i < Math.min(k, distances.size()); i++) {
            Map.Entry<Integer, Float> entry = distances.get(i);
            AnnoySearchResult result = AnnoySearchResult.create(entry.getKey(), entry.getValue());
            result.calculateSimilarity(index.getParam().getDistanceMetric());
            results.add(result);
        }

        return results;
    }

    /**
     * 计算距离
     */
    private float calculateDistance(List<Float> v1, List<Float> v2, String metric) {
        switch (metric.toLowerCase()) {
            case "euclidean":
                return calculateEuclideanDistance(v1, v2);
            case "angular":
            case "cosine":
                return calculateAngularDistance(v1, v2);
            case "manhattan":
                return calculateManhattanDistance(v1, v2);
            case "dot":
                return calculateDotProduct(v1, v2);
            default:
                return calculateEuclideanDistance(v1, v2);
        }
    }

    private float calculateEuclideanDistance(List<Float> v1, List<Float> v2) {
        float sum = 0;
        for (int i = 0; i < v1.size(); i++) {
            float diff = v1.get(i) - v2.get(i);
            sum += diff * diff;
        }
        return (float) Math.sqrt(sum);
    }

    private float calculateAngularDistance(List<Float> v1, List<Float> v2) {
        float dot = 0, norm1 = 0, norm2 = 0;
        for (int i = 0; i < v1.size(); i++) {
            dot += v1.get(i) * v2.get(i);
            norm1 += v1.get(i) * v1.get(i);
            norm2 += v2.get(i) * v2.get(i);
        }
        float cosine = dot / (float) (Math.sqrt(norm1) * Math.sqrt(norm2));
        return (float) Math.acos(Math.max(-1, Math.min(1, cosine)));
    }

    private float calculateManhattanDistance(List<Float> v1, List<Float> v2) {
        float sum = 0;
        for (int i = 0; i < v1.size(); i++) {
            sum += Math.abs(v1.get(i) - v2.get(i));
        }
        return sum;
    }

    private float calculateDotProduct(List<Float> v1, List<Float> v2) {
        float sum = 0;
        for (int i = 0; i < v1.size(); i++) {
            sum += v1.get(i) * v2.get(i);
        }
        return -sum; // Negative because we want smaller distances for higher similarity
    }

    /**
     * 清理索引数据
     */
    private void cleanupIndexData(AnnoyIndex index) {
        String indexId = index.getIndexId();

        // 清理该索引相关的所有数据映射
        Map<String, Integer> docToVectorMap = indexDocumentIdToVectorId.remove(indexId);
        Map<Integer, Document> vectorToDocMap = indexVectorIdToDocument.remove(indexId);
        Map<Integer, List<Float>> vectorDataMap = indexVectorData.remove(indexId);

        // 记录清理的数据量
        int docMappings = docToVectorMap != null ? docToVectorMap.size() : 0;
        int vectorMappings = vectorToDocMap != null ? vectorToDocMap.size() : 0;
        int vectorDataCount = vectorDataMap != null ? vectorDataMap.size() : 0;

        log.info("Cleaned up index {} data: {} document mappings, {} vector mappings, {} vector data entries",
                indexId, docMappings, vectorMappings, vectorDataCount);
    }
}
