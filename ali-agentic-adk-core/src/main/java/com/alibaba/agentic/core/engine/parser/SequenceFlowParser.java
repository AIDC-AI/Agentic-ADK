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
package com.alibaba.agentic.core.engine.parser;

import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = SequenceFlow.class, priority = 1)
public class SequenceFlowParser extends AbstractBpmnParser<SequenceFlow>   {

    @Override
    public Class<SequenceFlow> getModelType() {
        return SequenceFlow.class;
    }

    @Override
    public SequenceFlow parseModel(XMLStreamReader reader, ParseContext context) {
        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setId(XmlParseUtil.getString(reader, "id"));
        sequenceFlow.setName(XmlParseUtil.getString(reader, "name"));
        sequenceFlow.setSourceRef(XmlParseUtil.getString(reader, "sourceRef"));
        sequenceFlow.setTargetRef(XmlParseUtil.getString(reader, "targetRef"));

        Map<String, String> properties = super.parseExtendedProperties(reader,  context);
        sequenceFlow.setProperties(properties);

        return sequenceFlow;
    }

}


