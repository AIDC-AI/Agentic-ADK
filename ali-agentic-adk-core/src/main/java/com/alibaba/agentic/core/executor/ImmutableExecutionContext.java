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
package com.alibaba.agentic.core.executor;

import com.alibaba.smart.framework.engine.context.ExecutionContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 将 Smart Engine 的 ExecutionContext 包装为不可变快照，
 * 防止跨回调链共享的可变状态引入副作用。
 */
public class ImmutableExecutionContext {

    private final Map<String, Object> request;
    private final Map<String, Object> response;

    public ImmutableExecutionContext(ExecutionContext ctx) {
        this.request = ctx.getRequest() == null ? Map.of() : Collections.unmodifiableMap(new HashMap<>(ctx.getRequest()));
        this.response = ctx.getResponse() == null ? Map.of() : Collections.unmodifiableMap(new HashMap<>(ctx.getResponse()));
    }

    public Map<String, Object> getRequest() {
        return request;
    }

    public Map<String, Object> getResponse() {
        return response;
    }
}

