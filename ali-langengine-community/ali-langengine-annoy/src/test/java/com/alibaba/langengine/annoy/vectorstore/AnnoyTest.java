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

import com.alibaba.langengine.annoy.model.AnnoyIndex;
import com.alibaba.langengine.annoy.model.AnnoyParam;
import com.alibaba.langengine.core.embeddings.Embeddings;
import com.alibaba.langengine.core.indexes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@DisplayName("Annoy向量存储测试")
@ExtendWith(MockitoExtension.class)
class AnnoyTest {

    @Mock
    private Embeddings mockEmbedding;

    private Annoy annoyVectorStore;
    private AnnoyParam testParam;

    @BeforeEach
    void setUp() {
        testParam = AnnoyParam.builder()
                .vectorDimension(3)
                .distanceMetric("euclidean")
                .nTrees(5)
                .build();
        
        annoyVectorStore = new Annoy(mockEmbedding, "test_index", testParam);
        annoyVectorStore.setAutoBuild(false); // 禁用自动构建以便测试
        annoyVectorStore.setAutoLoad(false);  // 禁用自动加载以便测试
    }

    @Test
    @DisplayName("测试构造函数")
    void testConstructors() {
        // 测试基本构造函数
        Annoy annoy1 = new Annoy(mockEmbedding);
        assertNotNull(annoy1);
        assertNotNull(annoy1.getIndexId());
        assertEquals(mockEmbedding, annoy1.getEmbedding());

        // 测试带索引ID的构造函数
        Annoy annoy2 = new Annoy(mockEmbedding, "custom_index");
        assertNotNull(annoy2);
        assertEquals("custom_index", annoy2.getIndexId());
        assertEquals(mockEmbedding, annoy2.getEmbedding());

        // 测试完整构造函数
        Annoy annoy3 = new Annoy(mockEmbedding, "custom_index", testParam);
        assertNotNull(annoy3);
        assertEquals("custom_index", annoy3.getIndexId());
        assertEquals(mockEmbedding, annoy3.getEmbedding());
        assertEquals(testParam, annoy3.getParam());
    }

    @Test
    @DisplayName("测试初始化")
    void testInitialization() {
        assertDoesNotThrow(() -> annoyVectorStore.init());
        
        AnnoyIndex indexInfo = annoyVectorStore.getIndexInfo();
        assertNotNull(indexInfo);
        assertEquals("test_index", indexInfo.getIndexId());
    }

    @Test
    @DisplayName("测试添加文档")
    void testAddDocuments() {
        // 模拟embedding结果
        Document doc1 = createTestDocument("doc1", "Content 1");
        Document doc2 = createTestDocument("doc2", "Content 2");
        List<Document> inputDocs = Arrays.asList(doc1, doc2);
        
        // 设置embedding后的文档
        Document embeddedDoc1 = createTestDocumentWithEmbedding("doc1", "Content 1", Arrays.asList(1.0, 2.0, 3.0));
        Document embeddedDoc2 = createTestDocumentWithEmbedding("doc2", "Content 2", Arrays.asList(4.0, 5.0, 6.0));
        List<Document> embeddedDocs = Arrays.asList(embeddedDoc1, embeddedDoc2);
        
        when(mockEmbedding.embedDocument(inputDocs)).thenReturn(embeddedDocs);
        
        // 添加文档
        assertDoesNotThrow(() -> annoyVectorStore.addDocuments(inputDocs));
        
        // 验证embedding被调用
        verify(mockEmbedding, times(1)).embedDocument(inputDocs);
        
        // 验证文档数量
        assertEquals(2, annoyVectorStore.getDocumentCount());
    }

    @Test
    @DisplayName("测试添加空文档列表")
    void testAddEmptyDocuments() {
        assertDoesNotThrow(() -> annoyVectorStore.addDocuments(null));
        assertDoesNotThrow(() -> annoyVectorStore.addDocuments(Arrays.asList()));
        
        assertEquals(0, annoyVectorStore.getDocumentCount());
        
        // 验证embedding没有被调用
        verify(mockEmbedding, never()).embedDocument(any());
    }

    @Test
    @DisplayName("测试相似性搜索")
    void testSimilaritySearch() {
        // 首先添加一些文档
        setupDocumentsForSearch();
        
        // 模拟查询embedding
        String query = "test query";
        List<String> queryEmbedding = Arrays.asList("[1.0, 2.0, 3.0]");
        when(mockEmbedding.embedQuery(eq(query), eq(2))).thenReturn(queryEmbedding);
        
        // 执行搜索
        List<Document> results = annoyVectorStore.similaritySearch(query, 2, null, null);
        
        // 验证结果
        assertNotNull(results);
        
        // 验证embedding查询被调用
        verify(mockEmbedding, times(1)).embedQuery(eq(query), eq(2));
    }

    @Test
    @DisplayName("测试空查询的相似性搜索")
    void testSimilaritySearchWithEmptyQuery() {
        List<Document> results = annoyVectorStore.similaritySearch("", 5, null, null);
        
        assertNotNull(results);
        assertTrue(results.isEmpty());
        
        // 验证embedding没有被调用
        verify(mockEmbedding, never()).embedQuery(any(), anyInt());
    }

    @Test
    @DisplayName("测试null查询的相似性搜索")
    void testSimilaritySearchWithNullQuery() {
        List<Document> results = annoyVectorStore.similaritySearch(null, 5, null, null);
        
        assertNotNull(results);
        assertTrue(results.isEmpty());
        
        // 验证embedding没有被调用
        verify(mockEmbedding, never()).embedQuery(any(), anyInt());
    }

    @Test
    @DisplayName("测试无效embedding格式的相似性搜索")
    void testSimilaritySearchWithInvalidEmbedding() {
        setupDocumentsForSearch();
        
        // 模拟无效的embedding格式
        String query = "test query";
        List<String> invalidEmbedding = Arrays.asList("invalid_format");
        when(mockEmbedding.embedQuery(eq(query), eq(2))).thenReturn(invalidEmbedding);
        
        List<Document> results = annoyVectorStore.similaritySearch(query, 2, null, null);
        
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("测试构建索引")
    void testBuildIndex() {
        setupDocumentsForSearch();
        
        assertDoesNotThrow(() -> annoyVectorStore.buildIndex());
        
        AnnoyIndex indexInfo = annoyVectorStore.getIndexInfo();
        assertTrue(indexInfo.isBuilt());
    }

    @Test
    @DisplayName("测试加载索引")
    void testLoadIndex() {
        setupDocumentsForSearch();
        annoyVectorStore.buildIndex();
        
        assertDoesNotThrow(() -> annoyVectorStore.loadIndex());
        
        AnnoyIndex indexInfo = annoyVectorStore.getIndexInfo();
        assertTrue(indexInfo.isLoaded());
    }

    @Test
    @DisplayName("测试清空索引")
    void testClearIndex() {
        setupDocumentsForSearch();
        
        assertEquals(2, annoyVectorStore.getDocumentCount());
        
        assertDoesNotThrow(() -> annoyVectorStore.clearIndex());
        
        assertEquals(0, annoyVectorStore.getDocumentCount());
    }

    @Test
    @DisplayName("测试删除索引")
    void testDeleteIndex() {
        setupDocumentsForSearch();
        
        assertTrue(annoyVectorStore.indexExists());
        
        assertDoesNotThrow(() -> annoyVectorStore.deleteIndex());

        // 删除后索引信息应该为null
        assertNull(annoyVectorStore.getIndexInfo());
    }

    @Test
    @DisplayName("测试索引状态检查")
    void testIndexStatusChecks() {
        // 初始状态 - 索引还未创建，状态应该为null
        assertNull(annoyVectorStore.getIndexStatus());
        
        // 添加文档后
        setupDocumentsForSearch();
        assertEquals(2, annoyVectorStore.getDocumentCount());
        
        // 构建后
        annoyVectorStore.buildIndex();
        AnnoyIndex.IndexStatus status = annoyVectorStore.getIndexStatus();
        assertTrue(status == AnnoyIndex.IndexStatus.BUILT || status == AnnoyIndex.IndexStatus.LOADED);
    }

    @Test
    @DisplayName("测试自动构建和加载设置")
    void testAutoBuildAndLoadSettings() {
        // 测试设置自动构建
        annoyVectorStore.setAutoBuild(true);
        assertTrue(annoyVectorStore.isAutoBuild());
        
        annoyVectorStore.setAutoBuild(false);
        assertFalse(annoyVectorStore.isAutoBuild());
        
        // 测试设置自动加载
        annoyVectorStore.setAutoLoad(true);
        assertTrue(annoyVectorStore.isAutoLoad());
        
        annoyVectorStore.setAutoLoad(false);
        assertFalse(annoyVectorStore.isAutoLoad());
    }

    @Test
    @DisplayName("测试距离过滤的相似性搜索")
    void testSimilaritySearchWithDistanceFilter() {
        setupDocumentsForSearch();
        
        String query = "test query";
        List<String> queryEmbedding = Arrays.asList("[1.0, 2.0, 3.0]");
        when(mockEmbedding.embedQuery(eq(query), eq(5))).thenReturn(queryEmbedding);
        
        // 使用距离过滤
        Double maxDistance = 0.5;
        List<Document> results = annoyVectorStore.similaritySearch(query, 5, maxDistance, null);
        
        assertNotNull(results);
        // 由于是模拟数据，具体结果取决于模拟的距离计算
    }

    /**
     * 设置用于搜索测试的文档
     */
    private void setupDocumentsForSearch() {
        Document doc1 = createTestDocument("doc1", "Content 1");
        Document doc2 = createTestDocument("doc2", "Content 2");
        List<Document> inputDocs = Arrays.asList(doc1, doc2);

        Document embeddedDoc1 = createTestDocumentWithEmbedding("doc1", "Content 1", Arrays.asList(1.0, 2.0, 3.0));
        Document embeddedDoc2 = createTestDocumentWithEmbedding("doc2", "Content 2", Arrays.asList(4.0, 5.0, 6.0));
        List<Document> embeddedDocs = Arrays.asList(embeddedDoc1, embeddedDoc2);

        when(mockEmbedding.embedDocument(inputDocs)).thenReturn(embeddedDocs);

        annoyVectorStore.addDocuments(inputDocs);

        // 手动构建和加载索引，因为自动构建被禁用了
        annoyVectorStore.buildIndex();
        annoyVectorStore.loadIndex();
    }

    /**
     * 创建测试文档
     */
    private Document createTestDocument(String id, String content) {
        Document document = new Document();
        document.setUniqueId(id);
        document.setPageContent(content);
        return document;
    }

    /**
     * 创建带embedding的测试文档
     */
    private Document createTestDocumentWithEmbedding(String id, String content, List<Double> embedding) {
        Document document = new Document();
        document.setUniqueId(id);
        document.setPageContent(content);
        document.setEmbedding(embedding);
        return document;
    }
}
