package com.cwl.kms.listener;

import com.cwl.kms.cache.MicroserviceCacheManager;
import com.cwl.kms.cache.MiddlewareDataCacheManager;
import com.cwl.kms.cache.MiddlewareKeyCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * ClassName: CacheListener
 * Package: com.cwl.kms.listener
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 23:07
 * @Version 1.0
 */
@Component
@Slf4j
public class CacheListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private MicroserviceCacheManager microserviceCacheManager;

    @Autowired
    private MiddlewareDataCacheManager middlewareDataCacheManager;

    @Autowired
    private MiddlewareKeyCacheManager middlewareKeyCacheManager;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // refresh
        log.info(">>> start to init cache ...");
        microserviceCacheManager.initializeCache();
        middlewareDataCacheManager.initializeCache();
        middlewareKeyCacheManager.initializeCache();
        log.info(">>> init cache success...");
    }
}
