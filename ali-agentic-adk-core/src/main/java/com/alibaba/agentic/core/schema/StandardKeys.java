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
package com.alibaba.agentic.core.schema;

/**
 * 标准输出键名常量，避免魔法字符串。
 */
public final class StandardKeys {
    private StandardKeys() {}

    public static final String TEXT = "text";
    public static final String SESSION_ID = "sessionId";
    public static final String TOKENS = "tokens";
    public static final String METADATA = "metadata";
}

