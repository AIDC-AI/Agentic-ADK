package com.alibaba.langengine.weaviate.vectorstore;

import com.alibaba.langengine.core.indexes.Document;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WeaviateServiceTest {

    private WeaviateService service;

    @BeforeEach
    void setUp() {
        service = mock(WeaviateService.class);
    }

    @Test
    @Order(1)
    void testAddDocuments() {
        Document d1 = new Document();
        d1.setUniqueId("d1");
        d1.setPageContent("alpha");
        d1.setEmbedding(Arrays.asList(1.0, 0.0, 0.0));

        doNothing().when(service).addDocuments(anyList());

        service.addDocuments(Collections.singletonList(d1));

        verify(service, times(1)).addDocuments(anyList());
    }

    @Test
    @Order(2)
    void testSimilaritySearch() {
        Document d1 = new Document();
        d1.setUniqueId("d1");
        d1.setScore(0.1);

        Document d2 = new Document();
        d2.setUniqueId("d2");
        d2.setScore(0.5);

        when(service.similaritySearch(anyList(), eq(2), isNull(), isNull()))
                .thenReturn(Arrays.asList(d1, d2));

        List<Document> res = service.similaritySearch(Arrays.asList(1.0f, 0.0f), 2, null, null);

        assertEquals(2, res.size());
        assertEquals("d1", res.get(0).getUniqueId());
        assertTrue(res.get(0).getScore() <= res.get(1).getScore());

        verify(service, times(1)).similaritySearch(anyList(), eq(2), isNull(), isNull());
    }

    @Test
    @Order(3)
    void testSimilaritySearchWithMaxDistance() {
        Document d1 = new Document();
        d1.setUniqueId("d1");
        d1.setScore(0.1);

        when(service.similaritySearch(anyList(), eq(5), eq(0.5), isNull()))
                .thenReturn(Collections.singletonList(d1));

        List<Document> res = service.similaritySearch(Arrays.asList(0.0f, 1.0f), 5, 0.5, null);

        assertEquals(1, res.size());
        assertEquals("d1", res.get(0).getUniqueId());
        assertTrue(res.get(0).getScore() <= 0.5);

        verify(service, times(1)).similaritySearch(anyList(), eq(5), eq(0.5), isNull());
    }

    @Test
    @Order(4)
    void testDropClass() {
        doNothing().when(service).dropClass();

        service.dropClass();

        verify(service, times(1)).dropClass();
    }
}
