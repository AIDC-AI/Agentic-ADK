package com.alibaba.langengine.feishu.tools.schema;

import com.alibaba.langengine.core.tool.StructuredParameter;
import com.alibaba.langengine.core.tool.StructuredSchema;

import java.util.HashMap;
import java.util.Map;


public class FeishuContactSchema extends StructuredSchema {

    public FeishuContactSchema() {
        // 操作类型参数
        StructuredParameter operationParam = new StructuredParameter();
        operationParam.setName("operation");
        operationParam.setDescription("通讯录操作类型，可选值：get_user（获取用户信息）、search_user（搜索用户）、get_department（获取部门信息）、list_departments（获取部门列表）");
        operationParam.setRequired(true);
        
        Map<String, Object> operationSchema = new HashMap<>();
        operationSchema.put("type", "string");
        operationSchema.put("enum", new String[]{"get_user", "search_user", "get_department", "list_departments"});
        operationParam.setSchema(operationSchema);
        
        getParameters().add(operationParam);

        // 用户ID参数
        StructuredParameter userIdParam = new StructuredParameter();
        userIdParam.setName("user_id");
        userIdParam.setDescription("用户ID，获取用户信息时必需");
        userIdParam.setRequired(false);
        
        Map<String, Object> userIdSchema = new HashMap<>();
        userIdSchema.put("type", "string");
        userIdParam.setSchema(userIdSchema);
        
        getParameters().add(userIdParam);

        // 用户ID类型参数
        StructuredParameter userIdTypeParam = new StructuredParameter();
        userIdTypeParam.setName("user_id_type");
        userIdTypeParam.setDescription("用户ID类型，可选值：open_id（默认）、user_id、email、mobile");
        userIdTypeParam.setRequired(false);
        
        Map<String, Object> userIdTypeSchema = new HashMap<>();
        userIdTypeSchema.put("type", "string");
        userIdTypeSchema.put("enum", new String[]{"open_id", "user_id", "email", "mobile"});
        userIdTypeSchema.put("default", "open_id");
        userIdTypeParam.setSchema(userIdTypeSchema);
        
        getParameters().add(userIdTypeParam);

        // 邮箱列表参数
        StructuredParameter emailsParam = new StructuredParameter();
        emailsParam.setName("emails");
        emailsParam.setDescription("邮箱地址列表，搜索用户时使用");
        emailsParam.setRequired(false);
        
        Map<String, Object> emailsSchema = new HashMap<>();
        emailsSchema.put("type", "array");
        Map<String, Object> emailItemSchema = new HashMap<>();
        emailItemSchema.put("type", "string");
        emailItemSchema.put("format", "email");
        emailsSchema.put("items", emailItemSchema);
        emailsParam.setSchema(emailsSchema);
        
        getParameters().add(emailsParam);

        // 手机号列表参数
        StructuredParameter mobilesParam = new StructuredParameter();
        mobilesParam.setName("mobiles");
        mobilesParam.setDescription("手机号列表，搜索用户时使用");
        mobilesParam.setRequired(false);
        
        Map<String, Object> mobilesSchema = new HashMap<>();
        mobilesSchema.put("type", "array");
        Map<String, Object> mobileItemSchema = new HashMap<>();
        mobileItemSchema.put("type", "string");
        mobilesSchema.put("items", mobileItemSchema);
        mobilesParam.setSchema(mobilesSchema);
        
        getParameters().add(mobilesParam);

        // 部门ID参数
        StructuredParameter departmentIdParam = new StructuredParameter();
        departmentIdParam.setName("department_id");
        departmentIdParam.setDescription("部门ID，获取部门信息时必需");
        departmentIdParam.setRequired(false);
        
        Map<String, Object> departmentIdSchema = new HashMap<>();
        departmentIdSchema.put("type", "string");
        departmentIdParam.setSchema(departmentIdSchema);
        
        getParameters().add(departmentIdParam);

        // 部门ID类型参数
        StructuredParameter departmentIdTypeParam = new StructuredParameter();
        departmentIdTypeParam.setName("department_id_type");
        departmentIdTypeParam.setDescription("部门ID类型，可选值：department_id（默认）、open_department_id");
        departmentIdTypeParam.setRequired(false);
        
        Map<String, Object> departmentIdTypeSchema = new HashMap<>();
        departmentIdTypeSchema.put("type", "string");
        departmentIdTypeSchema.put("enum", new String[]{"department_id", "open_department_id"});
        departmentIdTypeSchema.put("default", "department_id");
        departmentIdTypeParam.setSchema(departmentIdTypeSchema);
        
        getParameters().add(departmentIdTypeParam);

        // 父部门ID参数
        StructuredParameter parentDepartmentIdParam = new StructuredParameter();
        parentDepartmentIdParam.setName("parent_department_id");
        parentDepartmentIdParam.setDescription("父部门ID，获取部门列表时可选，用于获取指定部门下的子部门");
        parentDepartmentIdParam.setRequired(false);
        
        Map<String, Object> parentDepartmentIdSchema = new HashMap<>();
        parentDepartmentIdSchema.put("type", "string");
        parentDepartmentIdParam.setSchema(parentDepartmentIdSchema);
        
        getParameters().add(parentDepartmentIdParam);

        // 是否递归获取子部门参数
        StructuredParameter fetchChildParam = new StructuredParameter();
        fetchChildParam.setName("fetch_child");
        fetchChildParam.setDescription("是否递归获取子部门，获取部门列表时可选，默认false");
        fetchChildParam.setRequired(false);
        
        Map<String, Object> fetchChildSchema = new HashMap<>();
        fetchChildSchema.put("type", "boolean");
        fetchChildSchema.put("default", false);
        fetchChildParam.setSchema(fetchChildSchema);
        
        getParameters().add(fetchChildParam);

        // 页面大小参数
        StructuredParameter pageSizeParam = new StructuredParameter();
        pageSizeParam.setName("page_size");
        pageSizeParam.setDescription("每页返回的数量，获取部门列表时可选，默认20，最大100");
        pageSizeParam.setRequired(false);
        
        Map<String, Object> pageSizeSchema = new HashMap<>();
        pageSizeSchema.put("type", "integer");
        pageSizeSchema.put("minimum", 1);
        pageSizeSchema.put("maximum", 100);
        pageSizeSchema.put("default", 20);
        pageSizeParam.setSchema(pageSizeSchema);
        
        getParameters().add(pageSizeParam);

        // 页面令牌参数
        StructuredParameter pageTokenParam = new StructuredParameter();
        pageTokenParam.setName("page_token");
        pageTokenParam.setDescription("分页令牌，获取部门列表时可选，用于获取下一页数据");
        pageTokenParam.setRequired(false);
        
        Map<String, Object> pageTokenSchema = new HashMap<>();
        pageTokenSchema.put("type", "string");
        pageTokenParam.setSchema(pageTokenSchema);
        
        getParameters().add(pageTokenParam);
    }
}
