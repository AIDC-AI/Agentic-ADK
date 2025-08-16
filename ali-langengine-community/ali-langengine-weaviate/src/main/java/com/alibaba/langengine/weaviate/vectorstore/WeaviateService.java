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

import com.alibaba.langengine.core.indexes.Document;
import io.weaviate.client.Config;
import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.batch.api.ObjectsBatcher;
import io.weaviate.client.v1.data.model.WeaviateObject;
import io.weaviate.client.v1.graphql.model.GraphQLResponse;
import io.weaviate.client.v1.graphql.query.argument.NearVectorArgument;
import io.weaviate.client.v1.graphql.query.fields.Field;
import io.weaviate.client.v1.misc.model.VectorIndexConfig;
import io.weaviate.client.v1.schema.model.DataType;
import io.weaviate.client.v1.schema.model.Property;
import io.weaviate.client.v1.schema.model.WeaviateClass;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author: xmhu2001
 * @create: 2025-08-16 10:00
 **/
@Slf4j
@Data
public class WeaviateService {

    private String className;

    private String scheme;

    private String host;

    private WeaviateParam weaviateParam;

    private final WeaviateClient client;

    public WeaviateService(String scheme,
                           String host,
                           String className,
                           WeaviateParam weaviateParam) {
        this.scheme = scheme;
        this.host = host;
        this.className = className;
        this.weaviateParam = weaviateParam != null ? weaviateParam : new WeaviateParam();

        Config config = new Config(this.scheme, this.host);
        this.client = new WeaviateClient(config);
    }

    public void init() {
        Result<WeaviateClass> get = client.schema().classGetter().withClassName(className).run();

        if (get != null && get.getResult() != null) {
            return;
        }

        WeaviateParam.InitParam initParam = weaviateParam.getInitParam();

        String contentField = weaviateParam.getFieldNamePageContent();
        String idField = weaviateParam.getFieldNameUniqueId();
        String metaField = weaviateParam.getFieldMeta();

        Property pContent = Property.builder()
                .name(contentField)
                .dataType(Collections.singletonList(DataType.TEXT))
                .build();

        Property pId = Property.builder()
                .name(idField)
                .dataType(Collections.singletonList(DataType.TEXT))
                .build();

        Property pMeta = Property.builder()
                .name(metaField)
                .dataType(Collections.singletonList(DataType.OBJECT))
                .build();

        VectorIndexConfig vic = VectorIndexConfig.builder()
                .distance(initParam.getVectorDistance())
                .vectorCacheMaxObjects(initParam.getVectorCacheMaxObjects())
                .build();

        WeaviateClass clazz = WeaviateClass.builder()
                .className(className)
                .vectorIndexType(initParam.getVectorIndexType())
                .vectorIndexConfig(vic)
                .vectorizer("none")
                .properties(Arrays.asList(pContent, pId, pMeta))
                .build();

        try {
            client.schema().classCreator().withClass(clazz).run();
            log.info("Successfully initialized weaviate class: {}", clazz.getClassName());
        } catch (Exception e) {
            log.error("Weaviate Service init failed", e);
        }
    }

    public void addDocuments(List<Document> documents) {
        if (documents == null || documents.isEmpty()) {
            return;
        }

        String contentField = weaviateParam.getFieldNamePageContent();
        String idField = weaviateParam.getFieldNameUniqueId();
        String metaField = weaviateParam.getFieldMeta();

        ObjectsBatcher batcher = client.batch().objectsBatcher();

        for (Document d : documents) {
            Map<String, Object> props = new HashMap<>();

            if (StringUtils.isNotBlank(d.getPageContent())) {
                props.put(contentField, d.getPageContent());
            }

            if (StringUtils.isNotBlank(d.getUniqueId())) {
                props.put(idField, d.getUniqueId());
            }

            if (d.getMetadata() != null && !d.getMetadata().isEmpty()) {
                props.put(metaField, d.getMetadata());
            }

            List<Double> emb = d.getEmbedding();
            Float[] vec = (emb == null || emb.isEmpty()) ? null : emb.stream()
                    .map(Double::floatValue)
                    .toArray(Float[]::new);

            WeaviateObject obj = WeaviateObject.builder()
                    .className(className)
                    .properties(props)
                    .vector(vec)
                    .build();

            batcher = batcher.withObject(obj);
        }
        batcher.run();
    }

    public List<Document> similaritySearch(List<Float> query, int k, Double maxDistanceValue, Integer type) {
        if (query == null || query.isEmpty()) {
            return Collections.emptyList();
        }

        Float[] vec = query.toArray(new Float[0]);

        NearVectorArgument.NearVectorArgumentBuilder nearBuilder =
                NearVectorArgument.builder().vector(vec);
        if (maxDistanceValue != null) {
            nearBuilder.distance(maxDistanceValue.floatValue());
        }
        NearVectorArgument near = nearBuilder.build();

        Field contentField = Field.builder()
                .name(weaviateParam.getFieldNamePageContent())
                .build();

        Field idField = Field.builder()
                .name(weaviateParam.getFieldNameUniqueId())
                .build();

        Field metaField = Field.builder()
                .name(weaviateParam.getFieldMeta())
                .build();

        Field additional = Field.builder()
                .name("_additional")
                .fields(
                        Field.builder().name("id").build(),
                        Field.builder().name("distance").build(),
                        Field.builder().name("certainty").build()
                )
                .build();

        Result<GraphQLResponse> result = client.graphQL().get()
                .withClassName(className)
                .withFields(contentField, idField, metaField, additional)
                .withNearVector(near)
                .withLimit(k)
                .run();

        if (result == null || result.getResult() == null) {
            return Collections.emptyList();
        }

        GraphQLResponse<?> resp = result.getResult();
        if (resp == null || resp.getData() == null) {
            return Collections.emptyList();
        }

        Map<String, Object> data = (Map<String, Object>) resp.getData();
        Object classObjects = ((Map<?, ?>) data.get("Get")).get(className);
        if (!(classObjects instanceof List)) {
            return Collections.emptyList();
        }

        List<?> items = (List<?>) classObjects;
        List<Document> out = new ArrayList<>();
        for (Object o : items) {
            Map<?, ?> m = (Map<?, ?>) o;
            Map<?, ?> add = (Map<?, ?>) m.get("_additional");

            Document doc = new Document();
            Object uniqueIdVal = m.get(idField.getName());
            if (uniqueIdVal != null) {
                doc.setUniqueId(String.valueOf(uniqueIdVal));
            }

            Object pageContentVal = m.get(contentField.getName());
            if (pageContentVal != null) {
                doc.setPageContent(String.valueOf(pageContentVal));
            }

            Object metadataVal = m.get(metaField.getName());
            if (metadataVal instanceof Map) {
                doc.setMetadata((Map<String, Object>) metadataVal);
            }

            Object distVal = add != null ? add.get("distance") : null;
            if (distVal instanceof Number) {
                doc.setScore(((Number) distVal).doubleValue());
            }

            out.add(doc);
        }
        return out;
    }

    public void dropClass() {
        client.schema().classDeleter().withClassName(className).run();
    }
}
