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
package com.alibaba.langengine.weaviate.vectorstore;

import com.alibaba.fastjson.JSON;
import com.alibaba.langengine.core.embeddings.Embeddings;
import com.alibaba.langengine.core.indexes.Document;
import com.alibaba.langengine.core.vectorstore.VectorStore;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.List;

import static com.alibaba.langengine.weaviate.WeaviateConfiguration.WEAVIATE_SERVER_URL;

/**
 * @author: xmhu2001
 * @create: 2025-08-16 10:00
 **/
@Slf4j
@Data
public class Weaviate extends VectorStore {

    private Embeddings embedding;

    private final String className;

    private final WeaviateService weaviateService;

    public Weaviate(String className) {
        this(className, null);
    }

    public Weaviate(String className, WeaviateParam weaviateParam) {
        this.className = className;

        String serverUrl = WEAVIATE_SERVER_URL;
        if (serverUrl != null && !serverUrl.contains("://")) {
            serverUrl = "http://" + serverUrl;
        }
        URI uri = URI.create(serverUrl);

        String scheme = (uri.getScheme() != null) ? uri.getScheme() : "http";
        String host = (uri.getAuthority() != null && !uri.getAuthority().isEmpty())
                ? uri.getAuthority()
                : (uri.getHost() != null ? uri.getHost() + (uri.getPort() > -1 ? (":" + uri.getPort()) : "") : "");

        this.weaviateService = new WeaviateService(scheme, host, className, weaviateParam);
    }

    public void init() {
        try {
            weaviateService.init();
        } catch (Exception e) {
            log.error("init weaviate failed", e);
        }
    }

    @Override
    public void addDocuments(List<Document> documents) {
        if (CollectionUtils.isEmpty(documents)) {
            return;
        }
        documents = embedding.embedDocument(documents);
        weaviateService.addDocuments(documents);
    }

    @Override
    public List<Document> similaritySearch(String query, int k, Double maxDistanceValue, Integer type) {
        List<String> embeddingStrings = embedding.embedQuery(query, k);
        if (CollectionUtils.isEmpty(embeddingStrings) || !embeddingStrings.get(0).startsWith("[")) {
            return Lists.newArrayList();
        }
        List<Float> vec = JSON.parseArray(embeddingStrings.get(0), Float.class);
        return weaviateService.similaritySearch(vec, k, maxDistanceValue, type);
    }
}
