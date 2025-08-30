package com.alibaba.langengine.feishu.tools.schema;

import com.alibaba.langengine.core.tool.StructuredParameter;
import com.alibaba.langengine.core.tool.StructuredSchema;

import java.util.HashMap;
import java.util.Map;


public class FeishuDocumentSchema extends StructuredSchema {

    public FeishuDocumentSchema() {
        // 操作类型参数
        StructuredParameter operationParam = new StructuredParameter();
        operationParam.setName("operation");
        operationParam.setDescription("文档操作类型，可选值：create（创建文档）、read/get（读取文档）、update（更新文档）");
        operationParam.setRequired(true);
        
        Map<String, Object> operationSchema = new HashMap<>();
        operationSchema.put("type", "string");
        operationSchema.put("enum", new String[]{"create", "read", "get", "update"});
        operationParam.setSchema(operationSchema);
        
        getParameters().add(operationParam);

        // 文档ID参数
        StructuredParameter documentIdParam = new StructuredParameter();
        documentIdParam.setName("document_id");
        documentIdParam.setDescription("文档ID，读取和更新操作时必需");
        documentIdParam.setRequired(false);
        
        Map<String, Object> documentIdSchema = new HashMap<>();
        documentIdSchema.put("type", "string");
        documentIdParam.setSchema(documentIdSchema);
        
        getParameters().add(documentIdParam);

        // 文档标题参数
        StructuredParameter titleParam = new StructuredParameter();
        titleParam.setName("title");
        titleParam.setDescription("文档标题，创建文档时必需");
        titleParam.setRequired(false);
        
        Map<String, Object> titleSchema = new HashMap<>();
        titleSchema.put("type", "string");
        titleParam.setSchema(titleSchema);
        
        getParameters().add(titleParam);

        // 文档内容参数
        StructuredParameter contentParam = new StructuredParameter();
        contentParam.setName("content");
        contentParam.setDescription("文档内容，创建或更新文档时使用");
        contentParam.setRequired(false);
        
        Map<String, Object> contentSchema = new HashMap<>();
        contentSchema.put("type", "string");
        contentParam.setSchema(contentSchema);
        
        getParameters().add(contentParam);

        // 文件夹ID参数
        StructuredParameter folderIdParam = new StructuredParameter();
        folderIdParam.setName("folder_id");
        folderIdParam.setDescription("文件夹ID，创建文档时可选，指定文档创建的位置");
        folderIdParam.setRequired(false);
        
        Map<String, Object> folderIdSchema = new HashMap<>();
        folderIdSchema.put("type", "string");
        folderIdParam.setSchema(folderIdSchema);
        
        getParameters().add(folderIdParam);
    }
}
