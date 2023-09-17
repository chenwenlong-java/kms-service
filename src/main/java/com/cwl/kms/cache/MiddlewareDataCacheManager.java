package com.cwl.kms.cache;

import com.cwl.kms.service.MiddlewareService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ClassName: MiddlewareDataCacheManager
 * Package: com.cwl.kms.cache
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 22:52
 * @Version 1.0
 */
@Slf4j
@Component
public class MiddlewareDataCacheManager implements CacheManager<String, String> {

    private Map<String, String> cacheMap = new ConcurrentHashMap<>();

    @Autowired
    private MiddlewareService middlewareService;

    private final ReentrantLock refreshLock = new ReentrantLock();

    @Override
    public String get(String key) {
        String value = cacheMap.get(key);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        refreshLock.lock();
        try {
            refresh(key);
            value = cacheMap.get(key);
            if (StringUtils.isBlank(value)) {
                return StringUtils.EMPTY;
            }
        } finally {
            refreshLock.unlock();
        }
        return value;
    }

    @Override
    public void put(String key, String value) {
        cacheMap.put(key, value);
    }

    @Override
    public void putAll(Map<String, String> cacheMap) {
        if (Objects.isNull(cacheMap) || cacheMap.isEmpty()) {
            log.info(">>> putAll cacheMap is empty in MiddlewareDataCacheManager.putAll");
            return;
        }
        this.cacheMap.putAll(cacheMap);
    }

    @Override
    public void refresh(String key) {
        middlewareService.refresh(key);
    }

    @Override
    public void initializeCache() {
        this.cacheMap.put("username","password");
        this.cacheMap.put("root","admin");
    }
}
