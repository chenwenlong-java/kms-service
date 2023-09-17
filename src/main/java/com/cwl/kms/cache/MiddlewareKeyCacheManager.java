package com.cwl.kms.cache;

import com.cwl.kms.domain.dto.AsymmetricKeysDTO;
import com.cwl.kms.domain.dto.MiddlewareCacheDTO;
import com.cwl.kms.domain.po.ServiceAccountPO;
import com.cwl.kms.service.KeyService;
import com.cwl.kms.service.MiddlewareService;
import com.cwl.kms.service.ServiceAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ClassName: MiddlewareKeyCacheManager
 * Package: com.cwl.kms.cache
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 22:54
 * @Version 1.0
 */
@Slf4j
@Component
public class MiddlewareKeyCacheManager implements CacheManager<String, MiddlewareCacheDTO> {

    @Autowired
    private ServiceAccountService accountService;

    @Autowired
    private MiddlewareService middlewareService;

    @Autowired
    private KeyService keyService;

    private final ReentrantLock refreshLock = new ReentrantLock();

    private Map<String, MiddlewareCacheDTO> cacheMap = new ConcurrentHashMap<>();

    @Override
    public void put(String key, MiddlewareCacheDTO value) {
        cacheMap.put(key, value);
    }

    @Override
    public MiddlewareCacheDTO get(String serviceAccount) {
        MiddlewareCacheDTO cacheDTO = cacheMap.get(serviceAccount);
        if (Objects.nonNull(cacheDTO)) {
            return cacheDTO;
        }
        refreshLock.lock();
        try {
            refresh(serviceAccount);
            cacheDTO = cacheMap.get(serviceAccount);
            if (Objects.isNull(cacheDTO)) {
                log.warn("middleware get cache is empty , service account : {} ", serviceAccount);
                return new MiddlewareCacheDTO();
            }
        } finally {
            refreshLock.unlock();
        }
        return cacheDTO;
    }

    @Override
    public void putAll(Map<String, MiddlewareCacheDTO> cacheMap) {
        if (Objects.isNull(cacheMap) || CollectionUtils.isEmpty(cacheMap)) {
            log.warn("put all cache map is empty in MiddlewareKeyCacheManager");
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

        Map<Long, ServiceAccountPO> serviceIdMap = accountService.selectAllAccount().stream()
                .collect(Collectors.toMap(ServiceAccountPO::getId, Function.identity()));
        // appId : redis / mysql / monogodb
        // TODO 一个服务账号 UUID ==> 一个中间件 redis
        // 一个中间件 对应 一个密钥对
        // 一个服务账号的ID ==> 查密钥对的ID
        if (CollectionUtils.isEmpty(serviceIdMap)) {
            log.info(">>> middleware cache init is empty");
            return;
        }

        // Put serviceAccount as key, publicKey and privateKey as value in cache
        List<AsymmetricKeysDTO> asymmetricKeysDTOS = keyService.selectMiddlewareByServiceId(new ArrayList<>(serviceIdMap.keySet()));
        Map<String, MiddlewareCacheDTO> tempMap = new HashMap<>();
        asymmetricKeysDTOS.forEach(dto -> {
            String serviceAccount = serviceIdMap.get(dto.getServiceId()).getServiceAccount();
            MiddlewareCacheDTO cacheData = new MiddlewareCacheDTO();
            cacheData.setPrivateKey(dto.getPrivateKey());
            cacheData.setPublicKey(dto.getPublicKey());
            tempMap.put(serviceAccount,cacheData);
        });

        cacheMap.putAll(tempMap);
        log.info(">>> initialize middleware key cache successful");
    }
}
