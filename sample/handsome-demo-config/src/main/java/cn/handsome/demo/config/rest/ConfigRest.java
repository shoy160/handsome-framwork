package cn.handsome.demo.config.rest;

import cn.handsome.core.http.HttpHelper;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import org.springframework.web.bind.annotation.*;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * todo
 *
 * @author shay
 * @date 2023/10/18
 **/
@RestController
@RequestMapping("app/config")
public class ConfigRest {
    private final static ConcurrentHashMap<String, Object> CONFIG_CACHE = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<String, List<String>> CONSUMER_DATA = new ConcurrentHashMap<>();

    @GetMapping("{key}")
    public Object index(@PathVariable String key, String consumer) {
        subscribe(key, consumer);
        return CONFIG_CACHE.get(key);
    }

    @PutMapping("{key}")
    public Object save(@PathVariable String key, @RequestBody Object config) {
        notify(key, config);
        return CONFIG_CACHE.put(key, config);
    }

    @DeleteMapping("{key}")
    public Object remove(@PathVariable String key) {
        notify(key, null);
        return CONFIG_CACHE.remove(key);
    }

    private void subscribe(String key, String consumer) {
        if (StrUtil.isBlank(key) || StrUtil.isBlank(consumer)) {
            return;
        }
        if (ReUtil.isMatch("^https?://", consumer)) {
            List<String> consumers = CONSUMER_DATA.getOrDefault(key, new ArrayList<>(0));
            if (!consumers.contains(consumer)) {
                consumers.add(consumer);
                CONSUMER_DATA.put(key, consumers);
            }
        }
    }

    private void notify(String key, Object config) {
        List<String> consumers = CONSUMER_DATA.get(key);
        if (CollUtil.isNotEmpty(consumers)) {
            for (String consumer : consumers) {
                ThreadUtil.execAsync(() -> {
                    String url = URLUtil.completeUrl(consumer, "/_sync/config?key=" + key);
                    HttpHelper.put(url, config);
                });
            }
        }
    }
}
