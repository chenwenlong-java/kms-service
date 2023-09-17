package com.cwl.kms.job;

import com.cwl.kms.cache.MicroserviceCacheManager;
import com.cwl.kms.cache.MiddlewareDataCacheManager;
import com.cwl.kms.cache.MiddlewareKeyCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * ClassName: RefreshCacheJob
 * Package: com.cwl.kms.job
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/17 20:45
 * @Version 1.0
 */
@Component
@Slf4j
public class RefreshCacheJob {
    @Autowired
    private MicroserviceCacheManager microserviceCacheManager;

    @Autowired
    private MiddlewareKeyCacheManager middlewareKeyCacheManager;

    @Autowired
    private MiddlewareDataCacheManager middlewareDataCacheManager;

    /**
     * Since it is a stand-alone cache,
     * only one machine may sync up the cache when adding authentication,
     * and the contents of the database need to be sync up regularly
     */
    @Scheduled(cron = "${kms.job.cacheExpression}")
    public void refreshCacheTask() {
        // total volume refresh
        log.info(">>> Scheduled tasks total volume refresh cache start ...");
        microserviceCacheManager.initializeCache();
        middlewareDataCacheManager.initializeCache();
        middlewareKeyCacheManager.initializeCache();
        log.info(">>> Scheduled tasks total volume refresh cache start !!!");
    }

}
