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

import com.alibaba.langengine.annoy.exception.AnnoyException;
import com.alibaba.langengine.annoy.model.AnnoyIndex;
import com.alibaba.langengine.annoy.model.AnnoyParam;
import com.alibaba.langengine.annoy.model.AnnoySearchResult;
import com.alibaba.langengine.core.indexes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Annoy服务测试")
class AnnoyServiceTest {

    private AnnoyService annoyService;
    private AnnoyParam testParam;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        annoyService = new AnnoyService();
        annoyService.initialize();
        
        testParam = AnnoyParam.builder()
                .vectorDimension(3)
                .distanceMetric("euclidean")
                .nTrees(5)
                .batchSize(10)
                .build();
    }

    @Test
    @DisplayName("测试服务初始化")
    void testServiceInitialization() {
        AnnoyService service = new AnnoyService();
        assertDoesNotThrow(service::initialize);
    }

    @Test
    @DisplayName("测试创建索引")
    void testCreateIndex() {
        String indexId = "test_index";
        AnnoyIndex index = annoyService.createIndex(indexId, testParam);
        
        assertNotNull(index);
        assertEquals(indexId, index.getIndexId());
        assertEquals(indexId, index.getIndexName());
        assertEquals(testParam, index.getParam());
        assertEquals(AnnoyIndex.IndexStatus.CREATED, index.getStatus());
        assertEquals(0, index.getVectorCount().get());
        assertFalse(index.isBuilt());
        assertFalse(index.isLoaded());
    }

    @Test
    @DisplayName("测试创建默认索引")
    void testCreateDefaultIndex() {
        AnnoyIndex index = annoyService.getOrCreateDefaultIndex(testParam);
        
        assertNotNull(index);
        assertEquals("default", index.getIndexId());
        assertEquals(testParam, index.getParam());
    }

    @Test
    @DisplayName("测试添加文档")
    void testAddDocuments() {
        String indexId = "test_index";
        AnnoyIndex index = annoyService.createIndex(indexId, testParam);
        
        // 创建测试文档
        Document doc1 = createTestDocument("doc1", Arrays.asList(1.0, 2.0, 3.0));
        Document doc2 = createTestDocument("doc2", Arrays.asList(4.0, 5.0, 6.0));
        List<Document> documents = Arrays.asList(doc1, doc2);
        
        // 添加文档
        annoyService.addDocuments(documents, indexId);
        
        // 验证结果
        assertEquals(2, index.getVectorCount().get());
        assertFalse(index.isBuilt()); // 添加文档后索引需要重新构建
        
        // 验证文档映射
        Integer vectorId1 = annoyService.getVectorIdByDocumentId("doc1", indexId);
        Integer vectorId2 = annoyService.getVectorIdByDocumentId("doc2", indexId);
        assertNotNull(vectorId1);
        assertNotNull(vectorId2);
        assertNotEquals(vectorId1, vectorId2);

        Document retrievedDoc1 = annoyService.getDocumentByVectorId(vectorId1, indexId);
        Document retrievedDoc2 = annoyService.getDocumentByVectorId(vectorId2, indexId);
        assertEquals("doc1", retrievedDoc1.getUniqueId());
        assertEquals("doc2", retrievedDoc2.getUniqueId());
    }

    @Test
    @DisplayName("测试添加空文档列表")
    void testAddEmptyDocuments() {
        String indexId = "test_index";
        AnnoyIndex index = annoyService.createIndex(indexId, testParam);
        
        // 添加空列表应该不抛出异常
        assertDoesNotThrow(() -> annoyService.addDocuments(null, indexId));
        assertDoesNotThrow(() -> annoyService.addDocuments(Arrays.asList(), indexId));
        
        assertEquals(0, index.getVectorCount().get());
    }

    @Test
    @DisplayName("测试添加文档到不存在的索引")
    void testAddDocumentsToNonExistentIndex() {
        Document doc = createTestDocument("doc1", Arrays.asList(1.0, 2.0, 3.0));
        
        AnnoyException.VectorAddException exception = assertThrows(
                AnnoyException.VectorAddException.class,
                () -> annoyService.addDocuments(Arrays.asList(doc), "non_existent_index")
        );
        
        assertTrue(exception.getMessage().contains("Index not found"));
    }

    @Test
    @DisplayName("测试添加无效向量维度的文档")
    void testAddDocumentWithInvalidDimension() {
        String indexId = "test_index";
        annoyService.createIndex(indexId, testParam);
        
        // 创建维度不匹配的文档（期望3维，提供2维）
        Document doc = createTestDocument("doc1", Arrays.asList(1.0, 2.0));
        
        AnnoyException.VectorAddException exception = assertThrows(
                AnnoyException.VectorAddException.class,
                () -> annoyService.addDocuments(Arrays.asList(doc), indexId)
        );
        
        assertTrue(exception.getMessage().contains("Failed to add documents to index") ||
                   (exception.getCause() != null && exception.getCause().getMessage().contains("Vector dimension mismatch")));
    }

    @Test
    @DisplayName("测试构建索引")
    void testBuildIndex() {
        String indexId = "test_index";
        AnnoyIndex index = annoyService.createIndex(indexId, testParam);
        
        // 添加一些文档
        Document doc1 = createTestDocument("doc1", Arrays.asList(1.0, 2.0, 3.0));
        Document doc2 = createTestDocument("doc2", Arrays.asList(4.0, 5.0, 6.0));
        annoyService.addDocuments(Arrays.asList(doc1, doc2), indexId);
        
        // 构建索引
        annoyService.buildIndex(indexId);
        
        // 验证索引状态
        assertTrue(index.isBuilt());
        assertEquals(AnnoyIndex.IndexStatus.BUILT, index.getStatus());
        assertNotNull(index.getLastBuildTime());
        assertTrue(index.getIndexFile().exists());
    }

    @Test
    @DisplayName("测试构建空索引")
    void testBuildEmptyIndex() {
        String indexId = "test_index";
        annoyService.createIndex(indexId, testParam);
        
        AnnoyException.IndexBuildException exception = assertThrows(
                AnnoyException.IndexBuildException.class,
                () -> annoyService.buildIndex(indexId)
        );
        
        assertTrue(exception.getMessage().contains("No vectors to build index"));
    }

    @Test
    @DisplayName("测试构建不存在的索引")
    void testBuildNonExistentIndex() {
        AnnoyException.IndexBuildException exception = assertThrows(
                AnnoyException.IndexBuildException.class,
                () -> annoyService.buildIndex("non_existent_index")
        );
        
        assertTrue(exception.getMessage().contains("Index not found"));
    }

    @Test
    @DisplayName("测试加载索引")
    void testLoadIndex() {
        String indexId = "test_index";
        AnnoyIndex index = annoyService.createIndex(indexId, testParam);
        
        // 添加文档并构建索引
        Document doc = createTestDocument("doc1", Arrays.asList(1.0, 2.0, 3.0));
        annoyService.addDocuments(Arrays.asList(doc), indexId);
        annoyService.buildIndex(indexId);
        
        // 加载索引
        annoyService.loadIndex(indexId);
        
        // 验证索引状态
        assertTrue(index.isLoaded());
        assertEquals(AnnoyIndex.IndexStatus.LOADED, index.getStatus());
    }

    @Test
    @DisplayName("测试加载未构建的索引")
    void testLoadUnbuiltIndex() {
        String indexId = "test_index";
        annoyService.createIndex(indexId, testParam);
        
        AnnoyException.IndexLoadException exception = assertThrows(
                AnnoyException.IndexLoadException.class,
                () -> annoyService.loadIndex(indexId)
        );
        
        assertTrue(exception.getMessage().contains("Index not built"));
    }

    @Test
    @DisplayName("测试相似性搜索")
    void testSimilaritySearch() {
        String indexId = "test_index";
        AnnoyIndex index = annoyService.createIndex(indexId, testParam);
        
        // 添加文档
        Document doc1 = createTestDocument("doc1", Arrays.asList(1.0, 2.0, 3.0));
        Document doc2 = createTestDocument("doc2", Arrays.asList(4.0, 5.0, 6.0));
        Document doc3 = createTestDocument("doc3", Arrays.asList(1.1, 2.1, 3.1));
        annoyService.addDocuments(Arrays.asList(doc1, doc2, doc3), indexId);
        
        // 构建并加载索引
        annoyService.buildIndex(indexId);
        annoyService.loadIndex(indexId);
        
        // 执行相似性搜索
        List<Float> queryVector = Arrays.asList(1.0f, 2.0f, 3.0f);
        List<AnnoySearchResult> results = annoyService.similaritySearch(queryVector, 2, indexId);
        
        // 验证结果
        assertNotNull(results);
        assertTrue(results.size() <= 2);
        
        // 验证结果按距离排序
        for (int i = 1; i < results.size(); i++) {
            assertTrue(results.get(i-1).getDistance() <= results.get(i).getDistance());
        }
        
        // 验证相似度计算
        for (AnnoySearchResult result : results) {
            assertNotNull(result.getVectorId());
            assertNotNull(result.getDistance());
            assertNotNull(result.getSimilarity());
            assertTrue(result.getDistance() >= 0);
            assertTrue(result.getSimilarity() >= 0);
        }
    }

    @Test
    @DisplayName("测试搜索不可用的索引")
    void testSearchUnavailableIndex() {
        String indexId = "test_index";
        annoyService.createIndex(indexId, testParam);
        
        List<Float> queryVector = Arrays.asList(1.0f, 2.0f, 3.0f);
        
        AnnoyException.SearchException exception = assertThrows(
                AnnoyException.SearchException.class,
                () -> annoyService.similaritySearch(queryVector, 5, indexId)
        );
        
        assertTrue(exception.getMessage().contains("Index not available"));
    }

    @Test
    @DisplayName("测试搜索维度不匹配的向量")
    void testSearchWithInvalidDimension() {
        String indexId = "test_index";
        AnnoyIndex index = annoyService.createIndex(indexId, testParam);
        
        // 添加文档并构建索引
        Document doc = createTestDocument("doc1", Arrays.asList(1.0, 2.0, 3.0));
        annoyService.addDocuments(Arrays.asList(doc), indexId);
        annoyService.buildIndex(indexId);
        annoyService.loadIndex(indexId);
        
        // 使用错误维度的查询向量
        List<Float> queryVector = Arrays.asList(1.0f, 2.0f); // 2维而不是3维
        
        AnnoyException.SearchException exception = assertThrows(
                AnnoyException.SearchException.class,
                () -> annoyService.similaritySearch(queryVector, 5, indexId)
        );
        
        assertTrue(exception.getMessage().contains("Query vector dimension mismatch"));
    }

    @Test
    @DisplayName("测试删除索引")
    void testDeleteIndex() {
        String indexId = "test_index";
        AnnoyIndex index = annoyService.createIndex(indexId, testParam);
        
        // 添加文档并构建索引
        Document doc = createTestDocument("doc1", Arrays.asList(1.0, 2.0, 3.0));
        annoyService.addDocuments(Arrays.asList(doc), indexId);
        annoyService.buildIndex(indexId);
        
        File indexFile = index.getIndexFile();
        assertTrue(indexFile.exists());
        
        // 删除索引
        annoyService.deleteIndex(indexId);
        
        // 验证索引已删除
        assertNull(annoyService.getIndex(indexId));
        assertFalse(indexFile.exists());
    }

    @Test
    @DisplayName("测试获取所有索引")
    void testGetAllIndexes() {
        // 创建多个索引
        annoyService.createIndex("index1", testParam);
        annoyService.createIndex("index2", testParam);
        annoyService.createIndex("index3", testParam);
        
        // 获取所有索引
        java.util.Collection<AnnoyIndex> allIndexes = annoyService.getAllIndexes();
        
        assertEquals(3, allIndexes.size());
    }

    /**
     * 创建测试文档
     */
    private Document createTestDocument(String id, List<Double> embedding) {
        Document document = new Document();
        document.setUniqueId(id);
        document.setPageContent("Test content for " + id);
        document.setEmbedding(embedding);
        return document;
    }
}
