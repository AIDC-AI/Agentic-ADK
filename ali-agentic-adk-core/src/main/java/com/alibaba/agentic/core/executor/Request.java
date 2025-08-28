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

import io.reactivex.rxjava3.processors.FlowableProcessor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 执行请求。
 * <p>
 * 封装调用模式（同步/双工）、事件处理器（BIDI 模式下）以及业务入参。
 * </p>
 *
 * @author baliang.smy
 * @date 2025/7/4 17:47
 */
@Data
@Accessors(chain = true)
public class Request {

    /**
     * 调用模式，默认为同步。
     */
    private InvokeMode invokeMode = InvokeMode.SYNC;

    /**
     * 双工模式下的事件处理器。框架会从该处理器订阅事件作为连续请求输入。
     */
    private FlowableProcessor<Map<String, Object>> processor;

    /**
     * 业务入参，键值结构。
     */
    private Map<String, Object> param;

    public Request setParam(Map<String, Object> param) {
        // 防御性复制，隔离调用者后续修改
        this.param = (param == null) ? null : new java.util.HashMap<>(param);
        return this;
    }
}
