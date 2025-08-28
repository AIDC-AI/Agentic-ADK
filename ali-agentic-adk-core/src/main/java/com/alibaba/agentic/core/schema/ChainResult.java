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

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 标准链式输出，替代 Map + 魔法 key 的弱类型返回。
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ChainResult {

    /**
     * 主文本输出。
     */
    private String text;

    /**
     * 可选：会话标识，便于多轮场景续写。
     */
    private String sessionId;

    /**
     * 令牌用量等统计信息。
     */
    private TokenUsage tokens;

    /**
     * 其他扩展信息（结构化元数据）。
     */
    private Map<String, Object> metadata;
}

