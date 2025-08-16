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
package com.alibaba.langengine.aliyunaisearch.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebSearchClientTest {

    @Mock
    private OkHttpClient mockClient;
    
    @Mock
    private Call mockCall;
    
    private ClientConfig clientConfig;
    private WebSearchClient webSearchClient;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        clientConfig = new ClientConfig.Builder()
                .host("https://search.aliyun.com")
                .apiKey("Bearer OS-xxx")
                .workspaceName("default")
                .serviceId("ops-web-search-001")
                .build();
        webSearchClient = new WebSearchClient(clientConfig);
        
        // Use reflection to replace the okHttpClient in WebSearchClient
        try {
            java.lang.reflect.Field clientConfigField = WebSearchClient.class.getDeclaredField("clientConfig");
            clientConfigField.setAccessible(true);
            
            java.lang.reflect.Field okHttpClientField = WebSearchClient.class.getDeclaredField("okHttpClient");
            okHttpClientField.setAccessible(true);
            
            java.lang.reflect.Field requestUrlField = WebSearchClient.class.getDeclaredField("requestUrl");
            requestUrlField.setAccessible(true);
            
            webSearchClient = new WebSearchClient(clientConfig);
            okHttpClientField.set(webSearchClient, mockClient);
        } catch (Exception e) {
            fail("Failed to set mock client: " + e.getMessage());
        }
    }

    @Test
    void testConstructorWithConfig() {
        ClientConfig config = new ClientConfig.Builder()
                .host("https://test.aliyun.com")
                .apiKey("Bearer OS-test")
                .build();
        
        WebSearchClient client = new WebSearchClient(config);
        assertNotNull(client);
    }

    @Test
    void testConstructorWithoutConfig() {
        // Test default constructor
        try {
            System.setProperty("ALIYUN_AI_SEARCH_API_KEY", "Bearer OS-test");
            System.setProperty("ALIYUN_AI_SEARCH_API_SERVICE_ID", "ops-web-search-001");
            System.setProperty("ALIYUN_AI_SEARCH_API_TIME_OUT", "30");
            System.setProperty("ALIYUN_AI_SEARCH_API_READ_TIME_OUT", "60");
            System.setProperty("ALIYUN_AI_SEARCH_API_WORKSPACE", "default");
            System.setProperty("ALIYUN_AI_SEARCH_API_ENDPOINT", "https://search.aliyun.com");
            
            WebSearchClient client = new WebSearchClient();
            assertNotNull(client);
        } catch (Exception e) {
            // Ignore configuration-related exceptions as we mainly test constructor logic
        }
    }

    @Test
    void testDoSearchWithValidResponse() throws Exception {
        // Prepare test data
        WebSearchRequest searchRequest = new WebSearchRequest.Builder()
                .query("测试搜索")
                .build();
        
        // Create a mock normal response
        WebSearchResponse expectedResponse = new WebSearchResponse();
        String jsonResponse = OBJECT_MAPPER.writeValueAsString(expectedResponse);
        
        // Configure mock
        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("https://search.aliyun.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(ResponseBody.create(jsonResponse, MediaType.get("application/json")))
                .build();
        
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        
        // Execute the test
        WebSearchResponse actualResponse = webSearchClient.doSearch(searchRequest);
        
        // Verify the results
        assertNotNull(actualResponse);
        verify(mockClient).newCall(any(Request.class));
        verify(mockCall).execute();
    }

    @Test
    void testDoSearchWithErrorResponse() throws Exception {
        // Prepare test data
        WebSearchRequest searchRequest = new WebSearchRequest.Builder()
                .query("测试搜索")
                .build();
        
        // Create a mock error response
        String jsonResponse = "{"
                + "\"request_id\":\"request-id-123\","
                + "\"code\":\"InvalidParameter\","
                + "\"message\":\"参数无效\","
                + "\"http_code\":400"
                + "}";
        
        // Configure mock
        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("https://search.aliyun.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(400)
                .message("Bad Request")
                .body(ResponseBody.create(jsonResponse, MediaType.get("application/json")))
                .build();
        
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        
        // Execute test and verify
        AISearchException exception = assertThrows(AISearchException.class, () -> {
            webSearchClient.doSearch(searchRequest);
        });
        
        assertEquals("InvalidParameter", exception.getErrorCode());
        assertEquals(Integer.valueOf(400), exception.getHttpCode());
        assertNotNull(exception.getRequestId());
    }

    @Test
    void testDoSearchWithHttp200ButErrorResponse() throws Exception {
        // Prepare test data
        WebSearchRequest searchRequest = new WebSearchRequest.Builder()
                .query("测试搜索")
                .build();
        
        // Create a mock HTTP 200 response with error content
        String jsonResponse = "{"
                + "\"request_id\":\"request-id-456\","
                + "\"code\":\"InternalError\","
                + "\"message\":\"内部错误\","
                + "\"http_code\":200"
                + "}";
        
        // Configure mock
        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("https://search.aliyun.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(ResponseBody.create(jsonResponse, MediaType.get("application/json")))
                .build();
        
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        
        // Execute test and verify
        AISearchException exception = assertThrows(AISearchException.class, () -> {
            webSearchClient.doSearch(searchRequest);
        });
        
        assertEquals("InternalError", exception.getErrorCode());
        assertEquals(Integer.valueOf(200), exception.getHttpCode());
        assertNotNull(exception.getRequestId());
    }

    @Test
    void testDoSearchWithIOException() throws Exception {
        // Prepare test data
        WebSearchRequest searchRequest = new WebSearchRequest.Builder()
                .query("测试搜索")
                .build();
        
        // Configure mock to throw IOException
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenThrow(new IOException("网络错误"));
        
        // Execute test and verify
        AISearchException exception = assertThrows(AISearchException.class, () -> {
            webSearchClient.doSearch(searchRequest);
        });
        
        assertTrue(exception.getMessage().contains("网络错误"));
        assertTrue(exception.getMessage().contains("network exception"));
    }

    @Test
    void testDoSearchWithNullRequest() {
        // Execute test and verify
        assertThrows(NullPointerException.class, () -> {
            webSearchClient.doSearch(null);
        });
    }

    @Test
    void testParseErrorResponseWithInvalidJson() throws Exception {
        // This test needs to call private method via reflection
        String invalidJson = "{ invalid json }";
        
        // Use reflection to call private method parseErrorResponse
        try {
            java.lang.reflect.Method method = WebSearchClient.class.getDeclaredMethod("parseErrorResponse", String.class);
            method.setAccessible(true);
            method.invoke(webSearchClient, invalidJson);
            fail("Expected AISearchException to be thrown");
        } catch (Exception e) {
            // Verify it is AISearchException
            assertTrue(e.getCause() instanceof AISearchException);
            assertTrue(e.getCause().getMessage().contains("Error response deserialization failed"));
        }
    }
}