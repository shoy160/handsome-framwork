package cn.handsome.nacos.utils;

import cn.handsome.core.Constants;
import cn.handsome.core.http.HttpHelper;
import cn.handsome.core.http.HttpResponse;
import cn.handsome.nacos.config.NacosProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shoy
 * @date 2021/6/25
 */
@Getter
@Setter
@RequiredArgsConstructor
public class NacosHelper {
    private final NacosProperties config;

    public String getConfig(String dataId, String group) {
        String url = String.format("http://%s/nacos/v1/cs/configs", config.getServerAddr());
        Map<String, String> query = new HashMap<>(3);
        query.put("tenant", config.getNamespace());
        query.put("dataId", dataId);
        query.put("group", group);
        HttpResponse response = HttpHelper.get(url, query);
        if (response.isErrorCode()) {
            return Constants.STR_EMPTY;
        }
        return response.readBody();
    }

    public String getConfig(String dataId) {
        return getConfig(dataId, "DEFAULT_GROUP");
    }
}
