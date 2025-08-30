package com.alibaba.langengine.feishu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class FeishuConfigurationTest {

    private FeishuConfiguration configuration;

    @BeforeEach
    void setUp() {
        configuration = new FeishuConfiguration();
    }

    @Test
    void testDefaultConstructor() {
        // 测试默认构造函数
        assertThat(configuration).isNotNull();
        assertThat(configuration.getBaseUrl()).isEqualTo("https://open.feishu.cn");
        assertThat(configuration.getConnectTimeout()).isEqualTo(30000);
        assertThat(configuration.getReadTimeout()).isEqualTo(30000);
        assertThat(configuration.isDebug()).isFalse();
        assertThat(configuration.getMaxRetries()).isEqualTo(3);
        assertThat(configuration.getRetryInterval()).isEqualTo(1000);
    }

    @Test
    void testConstructorWithAppIdAndSecret() {
        // 测试带应用ID和密钥的构造函数
        FeishuConfiguration config = new FeishuConfiguration("test_app_id", "test_app_secret");
        
        assertThat(config.getAppId()).isEqualTo("test_app_id");
        assertThat(config.getAppSecret()).isEqualTo("test_app_secret");
        assertThat(config.getBaseUrl()).isEqualTo("https://open.feishu.cn");
    }

    @Test
    void testConstructorWithFullParameters() {
        // 测试带完整参数的构造函数
        FeishuConfiguration config = new FeishuConfiguration("test_app_id", "test_app_secret", "https://custom.feishu.cn");
        
        assertThat(config.getAppId()).isEqualTo("test_app_id");
        assertThat(config.getAppSecret()).isEqualTo("test_app_secret");
        assertThat(config.getBaseUrl()).isEqualTo("https://custom.feishu.cn");
    }

    @Test
    void testIsValidWithValidConfiguration() {
        // 测试有效配置
        configuration.setAppId("valid_app_id");
        configuration.setAppSecret("valid_app_secret");
        configuration.setBaseUrl("https://open.feishu.cn");
        
        assertThat(configuration.isValid()).isTrue();
    }

    @Test
    void testIsValidWithNullAppId() {
        // 测试空应用ID
        configuration.setAppId(null);
        configuration.setAppSecret("valid_app_secret");
        configuration.setBaseUrl("https://open.feishu.cn");
        
        assertThat(configuration.isValid()).isFalse();
    }

    @Test
    void testIsValidWithEmptyAppId() {
        // 测试空字符串应用ID
        configuration.setAppId("");
        configuration.setAppSecret("valid_app_secret");
        configuration.setBaseUrl("https://open.feishu.cn");
        
        assertThat(configuration.isValid()).isFalse();
    }

    @Test
    void testIsValidWithWhitespaceAppId() {
        // 测试只有空格的应用ID
        configuration.setAppId("   ");
        configuration.setAppSecret("valid_app_secret");
        configuration.setBaseUrl("https://open.feishu.cn");
        
        assertThat(configuration.isValid()).isFalse();
    }

    @Test
    void testIsValidWithNullAppSecret() {
        // 测试空应用密钥
        configuration.setAppId("valid_app_id");
        configuration.setAppSecret(null);
        configuration.setBaseUrl("https://open.feishu.cn");
        
        assertThat(configuration.isValid()).isFalse();
    }

    @Test
    void testIsValidWithEmptyAppSecret() {
        // 测试空字符串应用密钥
        configuration.setAppId("valid_app_id");
        configuration.setAppSecret("");
        configuration.setBaseUrl("https://open.feishu.cn");
        
        assertThat(configuration.isValid()).isFalse();
    }

    @Test
    void testIsValidWithNullBaseUrl() {
        // 测试空基础URL
        configuration.setAppId("valid_app_id");
        configuration.setAppSecret("valid_app_secret");
        configuration.setBaseUrl(null);
        
        assertThat(configuration.isValid()).isFalse();
    }

    @Test
    void testIsValidWithEmptyBaseUrl() {
        // 测试空字符串基础URL
        configuration.setAppId("valid_app_id");
        configuration.setAppSecret("valid_app_secret");
        configuration.setBaseUrl("");
        
        assertThat(configuration.isValid()).isFalse();
    }

    @Test
    void testGetApiUrlWithNullPath() {
        // 测试空路径
        configuration.setBaseUrl("https://open.feishu.cn");
        
        String result = configuration.getApiUrl(null);
        assertThat(result).isEqualTo("https://open.feishu.cn");
    }

    @Test
    void testGetApiUrlWithEmptyPath() {
        // 测试空字符串路径
        configuration.setBaseUrl("https://open.feishu.cn");
        
        String result = configuration.getApiUrl("");
        assertThat(result).isEqualTo("https://open.feishu.cn");
    }

    @Test
    void testGetApiUrlWithValidPath() {
        // 测试有效路径
        configuration.setBaseUrl("https://open.feishu.cn");
        
        String result = configuration.getApiUrl("/open-apis/auth/v3/tenant_access_token");
        assertThat(result).isEqualTo("https://open.feishu.cn/open-apis/auth/v3/tenant_access_token");
    }

    @Test
    void testGetApiUrlWithPathStartingWithSlash() {
        // 测试以斜杠开头的路径
        configuration.setBaseUrl("https://open.feishu.cn");
        
        String result = configuration.getApiUrl("/api/test");
        assertThat(result).isEqualTo("https://open.feishu.cn/api/test");
    }

    @Test
    void testGetApiUrlWithPathNotStartingWithSlash() {
        // 测试不以斜杠开头的路径
        configuration.setBaseUrl("https://open.feishu.cn");
        
        String result = configuration.getApiUrl("api/test");
        assertThat(result).isEqualTo("https://open.feishu.cn/api/test");
    }

    @Test
    void testGetApiUrlWithBaseUrlEndingWithSlash() {
        // 测试以斜杠结尾的基础URL
        configuration.setBaseUrl("https://open.feishu.cn/");
        
        String result = configuration.getApiUrl("/api/test");
        assertThat(result).isEqualTo("https://open.feishu.cn/api/test");
    }

    @Test
    void testGetApiUrlWithBaseUrlEndingWithSlashAndPathNotStartingWithSlash() {
        // 测试以斜杠结尾的基础URL和不以斜杠开头的路径
        configuration.setBaseUrl("https://open.feishu.cn/");
        
        String result = configuration.getApiUrl("api/test");
        assertThat(result).isEqualTo("https://open.feishu.cn/api/test");
    }

    @Test
    void testSettersAndGetters() {
        // 测试所有setter和getter方法
        configuration.setAppId("test_app_id");
        configuration.setAppSecret("test_app_secret");
        configuration.setBaseUrl("https://custom.feishu.cn");
        configuration.setConnectTimeout(15000);
        configuration.setReadTimeout(25000);
        configuration.setDebug(true);
        configuration.setMaxRetries(5);
        configuration.setRetryInterval(2000);
        
        assertThat(configuration.getAppId()).isEqualTo("test_app_id");
        assertThat(configuration.getAppSecret()).isEqualTo("test_app_secret");
        assertThat(configuration.getBaseUrl()).isEqualTo("https://custom.feishu.cn");
        assertThat(configuration.getConnectTimeout()).isEqualTo(15000);
        assertThat(configuration.getReadTimeout()).isEqualTo(25000);
        assertThat(configuration.isDebug()).isTrue();
        assertThat(configuration.getMaxRetries()).isEqualTo(5);
        assertThat(configuration.getRetryInterval()).isEqualTo(2000);
    }

    @Test
    void testToString() {
        // 测试toString方法
        configuration.setAppId("test_app_id_12345678");
        configuration.setAppSecret("test_app_secret");
        configuration.setBaseUrl("https://open.feishu.cn");
        
        String result = configuration.toString();
        
        assertThat(result).contains("appId='test_app...'"); // 应该被截断
        assertThat(result).contains("appSecret='***'"); // 应该被隐藏
        assertThat(result).contains("baseUrl='https://open.feishu.cn'");
        assertThat(result).contains("connectTimeout=30000");
        assertThat(result).contains("readTimeout=30000");
        assertThat(result).contains("debug=false");
        assertThat(result).contains("maxRetries=3");
        assertThat(result).contains("retryInterval=1000");
    }

    @Test
    void testToStringWithNullValues() {
        // 测试toString方法处理null值
        configuration.setAppId(null);
        configuration.setAppSecret(null);
        
        String result = configuration.toString();
        
        assertThat(result).contains("appId='null'");
        assertThat(result).contains("appSecret='null'");
    }

    @Test
    void testToStringWithShortAppId() {
        // 测试toString方法处理短应用ID
        configuration.setAppId("short");
        
        String result = configuration.toString();
        
        assertThat(result).contains("appId='short...'");
    }
}
