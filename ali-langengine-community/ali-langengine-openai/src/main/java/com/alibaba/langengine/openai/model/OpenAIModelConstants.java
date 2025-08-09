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
package com.alibaba.langengine.openai.model;

/**
 * 相关常量
 *
 * @author xiaoxuan.lp
 */
public class OpenAIModelConstants {

    /**
     * The latest GPT-4 Turbo model with vision capabilities.
     * Vision requests can now use JSON mode and function calling.
     * Currently points to gpt-4-turbo-2024-04-09.
     */
    public static final String GPT_4_TURBO = "gpt-4-turbo";

    /**
     * GPT-4 Turbo with Vision model.
     * Vision requests can now use JSON mode and function calling.
     * gpt-4-turbo currently points to this version.
     */
    public static final String GPT_4_TURBO_2024_04_09 = "gpt-4-turbo-2024-04-09";

    /**
     * GPT-4 Turbo preview model.
     * Currently points to gpt-4-0125-preview.
     */
    public static final String GPT_4_TURBO_PREVIEW = "gpt-4-turbo-preview";

    /**
     * GPT-4 Turbo with visionNew
     * Ability to understand images, in addition to all other GPT-4 Turbo capabilities.
     * Returns a maximum of 4,096 output tokens. This is a preview model version and
     * not suited yet for production traffic.
     */
    public static final String GPT_4_TURBO_WITH_VISION = "gpt-4-vision-preview";

    /**
     * Currently points to gpt-4-0613.
     */
    public static final String GPT_4 = "gpt-4";

    /**
     * Currently points to gpt-4-32k-0613.
     */
    public static final String GPT_4_32K = "gpt-4-32k";

    /**
     * Snapshot of gpt-4 from June 13th 2023 with improved function calling support.
     */
    public static final String GPT_4_0613 = "gpt-4-0613";

    /**
     * Snapshot of gpt-4-32k from June 13th 2023 with improved function calling support.
     */
    public static final String GPT_4_32K_0613 = "gpt-4-32k-0613";

    /**
     * 	Snapshot of gpt-4 from March 14th 2023 with function calling support.
     * 	This model version will be deprecated on June 13th 2024.
     */
    public static final String GPT_4_0314 = "gpt-4-0314";

    /**
     * Snapshot of gpt-4-32k from March 14th 2023 with function calling support.
     * This model version will be deprecated on June 13th 2024.
     */
    public static final String gpt_4_32k_0314 = "gpt-4-32k-0314";

    /**
     * Updated GPT 3.5 TurboNew
     * The latest GPT-3.5 Turbo model with improved instruction following,
     * JSON mode, reproducible outputs, parallel function calling, and more.
     * Returns a maximum of 4,096 output tokens.
     */
    public static final String GPT_35_TURBO_1106 = "gpt-3.5-turbo-1106";

    /**
     * 	Currently points to gpt-3.5-turbo-0613.
     * 	Will point to gpt-3.5-turbo-1106 starting Dec 11, 2023.
     */
    public static final String GPT_35_TURBO = "gpt-3.5-turbo";

    /**
     * 	Currently points to gpt-3.5-turbo-0613.
     * 	Will point to gpt-3.5-turbo-1106 starting Dec 11, 2023.
     */
    public static final String GPT_35_TURBO_16K = "gpt-3.5-turbo-16k";

    /**
     * Similar capabilities as text-davinci-003 but compatible
     * with legacy Completions endpoint and not Chat Completions.
     */
    public static final String GPT_35_TURBO_INSTRUCT = "gpt-3.5-turbo-instruct";

    /**
     * Snapshot of gpt-3.5-turbo from June 13th 2023.
     * Will be deprecated on June 13, 2024.
     */
    public static final String GPT_35_TURBO_0613 = "gpt-3.5-turbo-0613";

    /**
     * Snapshot of gpt-3.5-16k-turbo from June 13th 2023.
     * Will be deprecated on June 13, 2024.
     */
    public static final String GPT_35_TURBO_16K_0613 = "gpt-3.5-turbo-16k-0613";

    /**
     * Snapshot of gpt-3.5-turbo from March 1st 2023.
     * Will be deprecated on June 13th 2024.
     */
    public static final String GPT_35_TURBO_0301 = "gpt-3.5-turbo-0301";

    /**
     * Can do language tasks with better quality and consistency than
     * the curie, babbage, or ada models. Will be deprecated on Jan 4th 2024.
     */
    public static final String TEXT_DAVINCI_003 = "text-davinci-003";

    /**
     * Similar capabilities to text-davinci-003 but trained with supervised
     * fine-tuning instead of reinforcement learning.
     * Will be deprecated on Jan 4th 2024.
     */
    public static final String TEXT_DAVINCI_002 = "text-davinci-002";

    /**
     * 	Optimized for code-completion tasks.
     * 	Will be deprecated on Jan 4th 2024.
     */
    public static final String CODE_DAVINCI_002 = "code-davinci-002";
}
