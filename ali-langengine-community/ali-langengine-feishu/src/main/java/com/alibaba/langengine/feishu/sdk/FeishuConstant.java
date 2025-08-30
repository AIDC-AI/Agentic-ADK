package com.alibaba.langengine.feishu.sdk;


public final class FeishuConstant {

    private FeishuConstant() {
        // 工具类，禁止实例化
    }

    // ==================== API 端点 ====================
    
    /**
     * 获取tenant_access_token
     */
    public static final String API_TENANT_ACCESS_TOKEN = "/open-apis/auth/v3/tenant_access_token";
    
    /**
     * 获取app_access_token
     */
    public static final String API_APP_ACCESS_TOKEN = "/open-apis/auth/v3/app_access_token";
    
    /**
     * 发送消息
     */
    public static final String API_SEND_MESSAGE = "/open-apis/im/v1/messages";
    
    /**
     * 批量发送消息
     */
    public static final String API_BATCH_SEND_MESSAGE = "/open-apis/im/v1/batch_messages";
    
    /**
     * 获取用户信息
     */
    public static final String API_GET_USER = "/open-apis/contact/v3/users/{user_id}";
    
    /**
     * 搜索用户
     */
    public static final String API_SEARCH_USER = "/open-apis/contact/v3/users/batch_get_id";
    
    /**
     * 获取部门信息
     */
    public static final String API_GET_DEPARTMENT = "/open-apis/contact/v3/departments/{department_id}";
    
    /**
     * 获取部门列表
     */
    public static final String API_LIST_DEPARTMENTS = "/open-apis/contact/v3/departments";
    
    /**
     * 创建文档
     */
    public static final String API_CREATE_DOCUMENT = "/open-apis/docx/v1/documents";
    
    /**
     * 获取文档内容
     */
    public static final String API_GET_DOCUMENT = "/open-apis/docx/v1/documents/{document_id}/content";
    
    /**
     * 更新文档
     */
    public static final String API_UPDATE_DOCUMENT = "/open-apis/docx/v1/documents/{document_id}/content";
    
    /**
     * 创建会议
     */
    public static final String API_CREATE_MEETING = "/open-apis/vc/v1/reserves";
    
    /**
     * 获取会议信息
     */
    public static final String API_GET_MEETING = "/open-apis/vc/v1/reserves/{reserve_id}";
    
    /**
     * 获取会议列表
     */
    public static final String API_LIST_MEETINGS = "/open-apis/vc/v1/reserves";

    // ==================== 消息类型 ====================
    
    /**
     * 文本消息
     */
    public static final String MSG_TYPE_TEXT = "text";
    
    /**
     * 富文本消息
     */
    public static final String MSG_TYPE_POST = "post";
    
    /**
     * 图片消息
     */
    public static final String MSG_TYPE_IMAGE = "image";
    
    /**
     * 文件消息
     */
    public static final String MSG_TYPE_FILE = "file";
    
    /**
     * 音频消息
     */
    public static final String MSG_TYPE_AUDIO = "audio";
    
    /**
     * 视频消息
     */
    public static final String MSG_TYPE_VIDEO = "video";
    
    /**
     * 交互式卡片消息
     */
    public static final String MSG_TYPE_INTERACTIVE = "interactive";
    
    /**
     * 分享群名片
     */
    public static final String MSG_TYPE_SHARE_CHAT = "share_chat";
    
    /**
     * 分享用户名片
     */
    public static final String MSG_TYPE_SHARE_USER = "share_user";

    // ==================== 接收者类型 ====================
    
    /**
     * 用户ID
     */
    public static final String RECEIVE_ID_TYPE_USER_ID = "user_id";
    
    /**
     * 邮箱
     */
    public static final String RECEIVE_ID_TYPE_EMAIL = "email";
    
    /**
     * 手机号
     */
    public static final String RECEIVE_ID_TYPE_MOBILE = "mobile";
    
    /**
     * 开放平台ID
     */
    public static final String RECEIVE_ID_TYPE_OPEN_ID = "open_id";
    
    /**
     * 群聊ID
     */
    public static final String RECEIVE_ID_TYPE_CHAT_ID = "chat_id";

    // ==================== HTTP 头部 ====================
    
    /**
     * 授权头部
     */
    public static final String HEADER_AUTHORIZATION = "Authorization";
    
    /**
     * 内容类型头部
     */
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    
    /**
     * JSON内容类型
     */
    public static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
    
    /**
     * Bearer令牌前缀
     */
    public static final String BEARER_PREFIX = "Bearer ";

    // ==================== 响应状态码 ====================
    
    /**
     * 成功
     */
    public static final int CODE_SUCCESS = 0;
    
    /**
     * 参数错误
     */
    public static final int CODE_INVALID_PARAM = 1001;
    
    /**
     * 权限不足
     */
    public static final int CODE_PERMISSION_DENIED = 1002;
    
    /**
     * 令牌无效
     */
    public static final int CODE_INVALID_TOKEN = 99991663;
    
    /**
     * 令牌过期
     */
    public static final int CODE_TOKEN_EXPIRED = 99991664;

    // ==================== 其他常量 ====================
    
    /**
     * 默认页面大小
     */
    public static final int DEFAULT_PAGE_SIZE = 20;
    
    /**
     * 最大页面大小
     */
    public static final int MAX_PAGE_SIZE = 100;
    
    /**
     * 令牌有效期（秒）
     */
    public static final int TOKEN_EXPIRE_TIME = 7200;
    
    /**
     * 令牌刷新提前时间（秒）
     */
    public static final int TOKEN_REFRESH_ADVANCE_TIME = 300;
    
    /**
     * UTF-8编码
     */
    public static final String CHARSET_UTF8 = "UTF-8";
    
    /**
     * 默认超时时间（毫秒）
     */
    public static final int DEFAULT_TIMEOUT = 30000;
}
