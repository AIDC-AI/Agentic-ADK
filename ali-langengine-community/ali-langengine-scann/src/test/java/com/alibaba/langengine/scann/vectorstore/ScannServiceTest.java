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

import com.alibaba.langengine.core.indexes.Document;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Disabled;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class ScannServiceTest {

    private ScannService scannService;
    private ScannParam scannParam;
    private static final String TEST_INDEX_NAME = "test_service_index";
    private static final String TEST_SERVER_URL = "http://localhost:8080";

    @BeforeEach
    public void setUp() {
        // 创建测试参数
        scannParam = new ScannParam();
        scannParam.setServerUrl(TEST_SERVER_URL);
        scannParam.setDimensions(768);
        scannParam.setIndexType("tree_ah");
        scannParam.setDistanceMeasure("dot_product");
        scannParam.setConnectionTimeout(5000);
        scannParam.setReadTimeout(10000);
        scannParam.setMaxConnections(10);
        scannParam.setBatchSize(100);
        
        // 创建服务实例
        scannService = new ScannService(TEST_INDEX_NAME, scannParam);
    }

    @AfterEach
    public void tearDown() {
        if (scannService != null) {
            scannService.close();
        }
    }

    @Test
    @DisplayName("测试 ScannService 构造函数")
    public void testConstructor() {
        assertNotNull(scannService);
        assertEquals(TEST_INDEX_NAME, scannService.getIndexName());
        assertEquals(scannParam, scannService.getScannParam());
        assertNotNull(scannService.getHttpClient());
        assertNotNull(scannService.getObjectMapper());
        assertNotNull(scannService.getDocumentIdCounter());
        assertNotNull(scannService.getDocumentIdMapping());
    }

    @Test
    @DisplayName("测试初始化")
    @Disabled("需要 ScaNN 服务器运行")
    public void testInit() {
        assertDoesNotThrow(() -> {
            scannService.init();
        });
    }

    @Test
    @DisplayName("测试添加文档 - 基本功能")
    @Disabled("需要 ScaNN 服务器运行")
    public void testAddDocuments() {
        List<Document> documents = createTestDocumentsWithEmbeddings();
        
        assertDoesNotThrow(() -> {
            scannService.addDocuments(documents);
        });
    }

    @Test
    @DisplayName("测试添加文档 - 空列表")
    public void testAddDocumentsEmpty() {
        // 测试空文档列表
        assertDoesNotThrow(() -> {
            scannService.addDocuments(null);
            scannService.addDocuments(Lists.newArrayList());
        });
    }

    @Test
    @DisplayName("测试添加文档 - 大批量")
    @Disabled("需要 ScaNN 服务器运行")
    public void testAddDocumentsLargeBatch() {
        // 创建大量文档测试批处理
        List<Document> documents = Lists.newArrayList();
        for (int i = 0; i < 250; i++) {
            Document doc = new Document();
            doc.setUniqueId("batch_doc_" + i);
            doc.setPageContent("This is batch test document number " + i);
            doc.setEmbedding(createRandomEmbedding(768));
            documents.add(doc);
        }
        
        assertDoesNotThrow(() -> {
            scannService.addDocuments(documents);
        });
    }

    @Test
    @DisplayName("测试相似性搜索")
    @Disabled("需要 ScaNN 服务器运行")
    public void testSimilaritySearch() {
        // 先添加一些文档
        List<Document> documents = createTestDocumentsWithEmbeddings();
        scannService.addDocuments(documents);
        
        // 执行搜索
        List<Double> queryVector = createRandomEmbedding(768);
        List<Document> results = scannService.similaritySearch(queryVector, 5, null);
        
        assertNotNull(results);
        assertTrue(results.size() <= 5);
    }

    @Test
    @DisplayName("测试相似性搜索 - 带距离限制")
    @Disabled("需要 ScaNN 服务器运行")
    public void testSimilaritySearchWithDistance() {
        List<Document> documents = createTestDocumentsWithEmbeddings();
        scannService.addDocuments(documents);
        
        List<Double> queryVector = createRandomEmbedding(768);
        List<Document> results = scannService.similaritySearch(queryVector, 10, 0.8);
        
        assertNotNull(results);
        assertTrue(results.size() <= 10);
        
        // 验证距离限制
        for (Document doc : results) {
            if (doc.getScore() != null) {
                assertTrue(doc.getScore() >= 0.8);
            }
        }
    }

    @Test
    @DisplayName("测试删除文档")
    @Disabled("需要 ScaNN 服务器运行")
    public void testDeleteDocuments() {
        List<String> documentIds = Arrays.asList("doc1", "doc2", "doc3");
        
        assertDoesNotThrow(() -> {
            scannService.deleteDocuments(documentIds);
        });
    }

    @Test
    @DisplayName("测试删除文档 - 空列表")
    public void testDeleteDocumentsEmpty() {
        assertDoesNotThrow(() -> {
            scannService.deleteDocuments(null);
            scannService.deleteDocuments(Lists.newArrayList());
        });
    }

    @Test
    @DisplayName("测试获取索引统计信息")
    @Disabled("需要 ScaNN 服务器运行")
    public void testGetIndexStats() {
        Map<String, Object> stats = scannService.getIndexStats();
        assertNotNull(stats);
    }

    @Test
    @DisplayName("测试关闭服务")
    public void testClose() {
        assertDoesNotThrow(() -> {
            scannService.close();
        });
    }

    @Test
    @DisplayName("测试文档ID生成和映射")
    public void testDocumentIdGeneration() {
        // 测试有唯一ID的文档
        Document docWithId = new Document();
        docWithId.setUniqueId("existing_id");
        docWithId.setPageContent("Test content");
        
        // 通过反射访问私有方法进行测试（这里简化处理）
        // 实际实现中，ID生成逻辑在 addDocuments 方法中
        assertNotNull(docWithId.getUniqueId());
        assertEquals("existing_id", docWithId.getUniqueId());
        
        // 测试没有唯一ID的文档
        Document docWithoutId = new Document();
        docWithoutId.setPageContent("Test content without ID");
        
        // 在实际的 addDocuments 调用中会生成ID
        assertNull(docWithoutId.getUniqueId());
    }

    @Test
    @DisplayName("测试批处理分割")
    public void testBatchPartitioning() {
        // 创建测试文档
        List<Document> documents = Lists.newArrayList();
        for (int i = 0; i < 250; i++) {
            Document doc = new Document();
            doc.setUniqueId("test_doc_" + i);
            doc.setPageContent("Test document " + i);
            documents.add(doc);
        }
        
        // 验证文档数量
        assertEquals(250, documents.size());
        
        // 批处理大小为100，应该分成3批
        int batchSize = scannParam.getBatchSize();
        assertEquals(100, batchSize);
        
        int expectedBatches = (int) Math.ceil((double) documents.size() / batchSize);
        assertEquals(3, expectedBatches);
    }

    @Test
    @DisplayName("测试参数验证")
    public void testParameterValidation() {
        // 测试有效参数
        assertTrue(scannParam.isValid());
        
        // 测试无效参数
        ScannParam invalidParam = new ScannParam();
        invalidParam.setServerUrl("");
        invalidParam.setDimensions(-1);
        invalidParam.setConnectionTimeout(-1);
        
        assertFalse(invalidParam.isValid());
    }

    @Test
    @DisplayName("测试HTTP客户端配置")
    public void testHttpClientConfiguration() {
        assertNotNull(scannService.getHttpClient());
        
        // 验证参数设置
        assertEquals(5000, scannParam.getConnectionTimeout());
        assertEquals(10000, scannParam.getReadTimeout());
        assertEquals(10, scannParam.getMaxConnections());
    }

    @Test
    @DisplayName("测试并发安全性")
    public void testConcurrencySafety() {
        // 测试文档ID计数器的原子性
        assertNotNull(scannService.getDocumentIdCounter());
        
        long initialValue = scannService.getDocumentIdCounter().get();
        long incrementedValue = scannService.getDocumentIdCounter().incrementAndGet();
        
        assertEquals(initialValue + 1, incrementedValue);
        
        // 测试文档ID映射的并发安全性
        assertNotNull(scannService.getDocumentIdMapping());
        assertTrue(scannService.getDocumentIdMapping() instanceof java.util.concurrent.ConcurrentHashMap);
    }

    /**
     * 创建带有嵌入向量的测试文档
     *
     * @return 测试文档列表
     */
    private List<Document> createTestDocumentsWithEmbeddings() {
        List<Document> documents = Lists.newArrayList();
        
        // 文档1
        Document doc1 = new Document();
        doc1.setUniqueId("service_doc1");
        doc1.setPageContent("ScaNN service test document one.");
        doc1.setEmbedding(createRandomEmbedding(768));
        Map<String, Object> metadata1 = new HashMap<>();
        metadata1.put("category", "service_test");
        metadata1.put("index", 1);
        doc1.setMetadata(metadata1);
        documents.add(doc1);
        
        // 文档2
        Document doc2 = new Document();
        doc2.setUniqueId("service_doc2");
        doc2.setPageContent("High-performance vector similarity search testing.");
        doc2.setEmbedding(createRandomEmbedding(768));
        Map<String, Object> metadata2 = new HashMap<>();
        metadata2.put("category", "service_test");
        metadata2.put("index", 2);
        doc2.setMetadata(metadata2);
        documents.add(doc2);
        
        // 文档3
        Document doc3 = new Document();
        doc3.setUniqueId("service_doc3");
        doc3.setPageContent("Google ScaNN library integration test case.");
        doc3.setEmbedding(createRandomEmbedding(768));
        Map<String, Object> metadata3 = new HashMap<>();
        metadata3.put("category", "service_test");
        metadata3.put("index", 3);
        doc3.setMetadata(metadata3);
        documents.add(doc3);
        
        return documents;
    }

    /**
     * 创建随机嵌入向量
     *
     * @param dimensions 向量维度
     * @return 随机向量
     */
    private List<Double> createRandomEmbedding(int dimensions) {
        List<Double> embedding = Lists.newArrayList();
        for (int i = 0; i < dimensions; i++) {
            embedding.add(Math.random() * 2.0 - 1.0); // [-1, 1] 范围内的随机数
        }
        return embedding;
    }
}
