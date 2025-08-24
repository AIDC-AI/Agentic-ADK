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
package com.alibaba.agentic.core.engine.behavior;

import com.alibaba.agentic.core.engine.constants.ExecutionConstant;
import com.alibaba.agentic.core.engine.constants.PropertyConstant;
import com.alibaba.agentic.core.exceptions.BaseException;
import com.alibaba.agentic.core.exceptions.ErrorEnum;
import com.alibaba.agentic.core.executor.SystemContext;
import com.alibaba.smart.framework.engine.behavior.TransitionBehavior;
import com.alibaba.smart.framework.engine.behavior.base.AbstractTransitionBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.Transition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 序列流行为实现。
 * <p>
 * 实现序列流的条件匹配逻辑，决定流程是否应该沿着特定的序列流继续执行。
 * 支持条件分支判断，对于默认分支（else分支）会在其他条件都不匹配时作为兜底选择。
 * </p>
 *
 * @author 框架团队
 */
@Slf4j
@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = TransitionBehavior.class, priority = 1)
public class SequenceFlowBehavior extends AbstractTransitionBehavior<SequenceFlow> {

    /**
     * 判断序列流是否匹配当前执行上下文。
     *
     * @param executionContext 执行上下文
     * @param transition       转换（序列流）
     * @return true - 匹配，false - 不匹配
     */
    @Override
    public boolean match(ExecutionContext executionContext, Transition transition) {
        log.info("smart engine execute fancy sequence flow match: {}", executionContext.getRequest());
        Map<String, Object> request = executionContext.getRequest();
        if (!request.containsKey(ExecutionConstant.SYSTEM_CONTEXT)) {
            throw new BaseException(String.format("smart engine execute flow sequence flow's systemcontext should not be empty, executionContext: %s, transition: %s.", executionContext, transition), ErrorEnum.SYSTEM_ERROR);
        }
        // default/else分支，默认先不匹配。如果所有条件分支均不成立，才在chooseOnlyOne逻辑中匹配
        if (MapUtils.isNotEmpty(transition.getProperties()) && PropertyConstant.SYMBOL_VALUE_CONDITION_DEFAULT_FLOW.equals(transition.getProperties().get(PropertyConstant.SYMBOL_KEY))) {
            return false;
        }
        // exclusiveGatewayId
        String sourceRefId = transition.getSourceRef();
        // next nodeId if branch is true
        String targetRefId = transition.getTargetRef();
        if (StringUtils.isEmpty(sourceRefId) || StringUtils.isEmpty(targetRefId)) {
            throw new BaseException(String.format("smart engine execute flow sequence flow's sourceRef or targetRef should not be empty, executionContext: %s, transition: %s.", executionContext, transition), ErrorEnum.SYSTEM_ERROR);
        }
        SystemContext systemContext = (SystemContext) request.get(ExecutionConstant.SYSTEM_CONTEXT);
        return ConditionRegistry.eval(systemContext, sourceRefId, targetRefId);
    }
}
