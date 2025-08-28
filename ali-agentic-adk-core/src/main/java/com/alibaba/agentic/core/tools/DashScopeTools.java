/**
 * Copyright (C) 2024 AIDC-AI
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.agentic.core.tools;

import com.alibaba.agentic.core.engine.constants.PropertyConstant;
import com.alibaba.agentic.core.executor.SystemContext;
import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.fastjson.JSONObject;
import com.google.genai.types.FunctionDeclaration;
import com.google.genai.types.Schema;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import com.alibaba.agentic.core.schema.StandardKeys;

@Component
@Data
@Accessors(chain = true)
public class DashScopeTools implements FunctionTool {

    private String apiKey;

    private String appId;


    @Override
    public Flowable<Map<String, Object>> run(Map<String, Object> args, SystemContext systemContext) {
        ApplicationParam applicationParam = new JSONObject(args).toJavaObject(ApplicationParam.class);
        if (StringUtils.isBlank(applicationParam.getApiKey())) {
            applicationParam.setApiKey(apiKey);
        }
        if (StringUtils.isBlank(applicationParam.getAppId())) {
            applicationParam.setAppId(appId);
        }

        return Flowable.create(emitter -> {
            try {
                Application application = new Application();
                ApplicationResult result = application.call(applicationParam);
                Map<String, Object> output = new HashMap<>();
                output.put(StandardKeys.TEXT, result.getOutput().getText());
                output.put(StandardKeys.SESSION_ID, result.getOutput().getSessionId());
                emitter.onNext(output);
                emitter.onComplete();
            } catch (ApiException | NoApiKeyException | InputRequiredException e) {
                emitter.onError(e);
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public String name() {
        return "dash_scope_tool";
    }

    @Override
    public String description() {
        return "这是一个阿里百炼的工具簇，集合了常用的信息查询能力，如天气、汇率、油价、IP等，统一提供标准化接口，便于集成和扩展。";
    }

    @Override
    public FunctionDeclaration declaration() {
        return FunctionDeclaration.builder()
                .name(name())
                .description(description())
                .parameters(Schema.builder()
                        .type("OBJECT")
                        .properties(Map.of(
                                "appId", Schema.builder()
                                        .type("STRING")
                                        .description("百炼智能应用ID")
                                        .build(),
                                "prompt", Schema.builder()
                                        .type("STRING")
                                        .description("用户提示词")
                                        .build(),
                                "sessionId", Schema.builder()
                                        .type("STRING")
                                        .description("会话ID（多轮时传入）")
                                        .build()
                        ))
                        .required(java.util.List.of("appId", "prompt"))
                        .build())
                .response(
                        Schema.builder()
                                .type("OBJECT")
                                .properties(Map.of(
                                        StandardKeys.TEXT, Schema.builder()
                                                .type("STRING").description("回复内容").build(),
                                        StandardKeys.SESSION_ID, Schema.builder()
                                                .type("STRING").description("本轮返回的会话ID").build()
                                ))
                                .required(java.util.List.of(StandardKeys.TEXT, StandardKeys.SESSION_ID))
                                .build()
                )
                .build();
    }

    private String getApiKey() {
        if (apiKey == null) {
            apiKey = PropertyConstant.dashscopeApiKey;
        }
        return apiKey;
    }
}
