package cn.handsome.core;

import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 常用变量
 *
 * @author shay
 * @date 2020/7/15
 */
public interface Constants {
    String APP_NAME = "";
    String EMPTY_STR = "";
    String REGION_SPLIT = ":";
    String APPLICATION_VERSION = "1.1.0";
    /**
     * 基础包
     */
    String BASE_PACKAGES = "cn.handsome";

    String MODE_DEV = "dev";

    String MODE_SECRET = "secret";

    String MODE_TEST = "test";

    String MODE_READY = "ready";

    String MODE_PROD = "prod";

    List<String> MODE_LIST = Arrays.asList(MODE_DEV, MODE_TEST, MODE_READY, MODE_PROD);

    /**
     * 模式校验
     *
     * @param mode mode
     * @return valid
     */
    static boolean validMode(String mode) {
        return StrUtil.isNotEmpty(mode) && MODE_LIST.contains(mode);
    }

    String CLAIM_USER_ID = "user-id";
    String CLAIM_TENANT_ID = "tenant-id";
    String CLAIM_USERNAME = "user-name";
    String CLAIM_ROLE = "role";
    String CLAIM_PREFIX = "claim-";

    String SESSION_TOKEN = "context-token";

    String HEADER_TENANT_ID = "tenant_id";

    String HEADER_CONTENT_TYPE = "Content-Type";

    String CONTENT_TYPE_JSON = "application/json";
    String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    String CONTENT_TYPE_FILE = "multipart/form-data";
    String CONTENT_TYPE_XML = "text/xml";
    String CONTENT_TYPE_HTML = "text/html";

    int CODE_SUCCESS = 200;

    String GROUP_APP = "app";
    String GROUP_MANAGE = "manage";
    String GROUP_MERCHANT = "merchant";

    String CACHE_SYNC_CHANNEL = "cache_sync";
}
