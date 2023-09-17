package com.cwl.kms.cache;

import com.cwl.kms.constants.KMSConstants;
import com.cwl.kms.domain.dto.AsymmetricKeysDTO;
import com.cwl.kms.domain.dto.AuthenticationDTO;
import com.cwl.kms.domain.dto.MicroserviceCacheDTO;
import com.cwl.kms.domain.dto.MicroserviceClientCacheDTO;
import com.cwl.kms.service.AuthenticationService;
import com.cwl.kms.service.KeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ClassName: MicroserviceCacheManager
 * Package: com.cwl.kms.cache
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 22:06
 * @Version 1.0
 */
@Slf4j
@Component
public class MicroserviceCacheManager implements CacheManager<String, Map<Integer, MicroserviceCacheDTO>> {
    // HashMap
    // refresh concurrent
    private Map<String, Map<Integer, MicroserviceCacheDTO>> cacheMap = new ConcurrentHashMap<>();

    private final ReentrantLock refreshLock = new ReentrantLock();

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private KeyService keyService;

    private final int MAX_VERSIONS = 10;

    public MicroserviceClientCacheDTO getFromClient(String appId) {
        MicroserviceClientCacheDTO resultDTO = new MicroserviceClientCacheDTO();

        get(appId, KMSConstants.CLIENT_FLAG).forEach((version, keyPair) -> {
            resultDTO.setKeyTTL(keyPair.getKeyTTL());
            resultDTO.setVersion(version);
            resultDTO.setPrivateKey(keyPair.getPrivateKey());
            resultDTO.setTokenTTL(keyPair.getTokenTTL());
            resultDTO.setBusiness(keyPair.getBusiness());
            resultDTO.setBusinessDomain(keyPair.getBusinessDomain());
        });

        return resultDTO;
    }

    public Map<Integer, MicroserviceCacheDTO> getFromServer(String appId) {
        return get(appId, KMSConstants.SERVER_FLAG);
    }

    private Map<Integer, MicroserviceCacheDTO> get(String appId, String flag) {
        Map<Integer, MicroserviceCacheDTO> resultMap = cacheMap.get(appId);
        if (Objects.isNull(resultMap)) {
            refreshLock.lock();
            try {
                refresh(appId);
                resultMap = cacheMap.get(appId);
                if (Objects.isNull(resultMap)) {
                    log.warn("appId : {} is not open authentication", appId);
                    return Collections.EMPTY_MAP;
                }
            } finally {
                refreshLock.unlock();
            }
        }
        return switchResult(resultMap, flag);
    }

    private Map<Integer, MicroserviceCacheDTO> switchResult(Map<Integer, MicroserviceCacheDTO> resultMap, String flag) {
        switch (flag) {
            // If it is the client side,only the latest version of the key is returned.
            case KMSConstants.CLIENT_FLAG:
                return resultMap.entrySet().stream()
                        .max(Map.Entry.comparingByKey())
                        .map(entry -> {
                            Map<Integer, MicroserviceCacheDTO> result = new HashMap<>();
                            result.put(entry.getKey(), entry.getValue());
                            return result;
                        })
                        .orElse(new HashMap<>());
            // If it is server,return the latest two versions.
            case KMSConstants.SERVER_FLAG:
                return resultMap.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                        .limit(2)
                        .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
            default:
                return resultMap;
        }
    }

    @Override
    public Map<Integer, MicroserviceCacheDTO> get(String key) {
        return get(key, KMSConstants.ALL_FLAG);
    }

    @Override
    public void put(String appId, Map<Integer, MicroserviceCacheDTO> newData) {
        cacheMap.put(appId,newData);

        // only 10 version
        if (cacheMap.get(appId).size() > MAX_VERSIONS) {
            Integer oldestVersion = cacheMap.get(appId).keySet().stream()
                    .min(Integer::compareTo)
                    .orElse(null);
            if (Objects.isNull(oldestVersion)) {
                cacheMap.get(appId).remove(oldestVersion);
            }
        }
    }

    @Override
    public void putAll(Map<String, Map<Integer, MicroserviceCacheDTO>> cacheMap) {
        if (cacheMap.isEmpty()) {
            log.warn("microservice cache map excute putAll, but cacheMap is empty");
            return;
        }
        cacheMap.forEach((key, value) -> put(key, value));
    }

    @Override
    public void refresh(String key) {
        Map<Integer, MicroserviceCacheDTO> resultMap = new HashMap<>();
        MicroserviceCacheDTO cacheDTO = authenticationService.getFromDataBase(key);
        resultMap.put(cacheDTO.getVersion(), cacheDTO);
        cacheMap.put(key, resultMap);
    }

    @Override
    public void initializeCache() {
        Map<Long, AuthenticationDTO> authenticationMap = authenticationService.selectForInit().stream().
                collect(Collectors.toMap(AuthenticationDTO::getId, a -> a));
        Map<Long, AsymmetricKeysDTO> keysDTOMap = keyService.selectMicroserviceByServiceIds(new ArrayList<>(authenticationMap.keySet())).stream()
                .collect(Collectors.toMap(AsymmetricKeysDTO::getServiceId, Function.identity()));

        if (CollectionUtils.isEmpty(authenticationMap) || CollectionUtils.isEmpty(keysDTOMap)) {
            log.info(">>> kms first start authenticationMap is empty");
            return;
        }
        Map<String, Map<Integer, MicroserviceCacheDTO>> tempMap = new ConcurrentHashMap<>();
        authenticationMap.forEach((id, authentication) -> {
            AsymmetricKeysDTO asymmetricKey = keysDTOMap.get(id);

            MicroserviceCacheDTO cacheData = new MicroserviceCacheDTO();
            Map<Integer, MicroserviceCacheDTO> keyMap = new HashMap<>();
            cacheData.setPublicKey(asymmetricKey.getPublicKey());
            cacheData.setPrivateKey(asymmetricKey.getPrivateKey());
            cacheData.setKeyTTL(asymmetricKey.getTTL());
            cacheData.setTokenTTL(authentication.getTokenTTL());
            cacheData.setBusiness("safety");
            cacheData.setBusinessDomain("istio");

            keyMap.put(asymmetricKey.getVersion(), cacheData);
            tempMap.put(authentication.getAppId(), keyMap);
            log.info(">>> start initialize microservice cache appId : {}", authentication.getAppId());
        });
        cacheMap.putAll(tempMap);
    }
}
