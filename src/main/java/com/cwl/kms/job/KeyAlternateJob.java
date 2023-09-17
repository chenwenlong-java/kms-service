package com.cwl.kms.job;

import com.cwl.kms.cache.MicroserviceCacheManager;
import com.cwl.kms.constants.KMSConstants;
import com.cwl.kms.domain.dto.KeyPairDTO;
import com.cwl.kms.domain.dto.MicroserviceCacheDTO;
import com.cwl.kms.domain.po.AsymmetricKeysPO;
import com.cwl.kms.domain.po.AuthenticationPO;
import com.cwl.kms.service.AuthenticationService;
import com.cwl.kms.service.KeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ClassName: KeyAlternateJob
 * Package: com.cwl.kms.job
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/17 20:29
 * @Version 1.0
 */
@Component
@Slf4j
public class KeyAlternateJob {

    @Autowired
    private KeyService keyService;

    @Autowired
    private MicroserviceCacheManager microserviceCacheManager;

    @Autowired
    private AuthenticationService authenticationService;

    @Scheduled(cron = "${kms.job.keyExpression}")
    public void cacheTask() {
        // query the laest version of the key from the database
        List<AsymmetricKeysPO> latestVersionList = keyService.selectLatestVersion().stream()
                .filter(po -> po.getTTL() != null && po.getTTL() < System.currentTimeMillis())
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(latestVersionList)) {
            log.info(">>> first start key is empty");
            return;
        }

        latestVersionList.forEach(latest -> {
            // Regenerate the key and reset the TTL
            KeyPairDTO keyPairDTO = keyService.generateKey();
            // latest.getTTL() == 10 + currentMills
            // clockCycle : 密钥轮换周期
            long clockCycle = latest.getTTL() - latest.getLastClockCycle();
            long currentTimeMillis = System.currentTimeMillis();
            latest.setTTL(currentTimeMillis + clockCycle);
            latest.setVersion(latest.getVersion() + 1);
            latest.setPrivateKey(keyPairDTO.getPrivateKey());
            latest.setLastClockCycle(currentTimeMillis);
            latest.setPublicKey(keyPairDTO.getPublicKey());
            latest.setKeyType(KMSConstants.MICROSERVICE_KEY_TYPE);
        });

        try {
            // 单机定时任务
            keyService.batchInsert(latestVersionList);
        } catch (DuplicateKeyException exception) {
            // If there is an abnormality, it may be executed concurrently by multiple nodes.
            // You can directly re-query the database and update the cache.
            log.info("Concurrent insert, reset data");
            latestVersionList = keyService.selectLatestVersion();
        }

        List<Long> serviceIds = latestVersionList.stream()
                .map(AsymmetricKeysPO::getServiceId)
                .collect(Collectors.toList());
        Map<Long, String> appIdMap = authenticationService.selectByIds(serviceIds).stream()
                .collect(Collectors.toMap(AuthenticationPO::getId, AuthenticationPO::getAppId));

        Map<String, Map<Integer, MicroserviceCacheDTO>> tempMap = new ConcurrentHashMap<>();
        latestVersionList.forEach(latest -> {
            String appId = appIdMap.get(latest.getServiceId());
            Map<Integer, MicroserviceCacheDTO> newData = new HashMap<>();
            MicroserviceCacheDTO cacheDTO = new MicroserviceCacheDTO();

            cacheDTO.setPublicKey(latest.getPublicKey());
            cacheDTO.setPrivateKey(latest.getPrivateKey());
            cacheDTO.setKeyTTL(latest.getTTL());

            newData.put(latest.getVersion(), cacheDTO);
            tempMap.put(appId, newData);
        });
        microserviceCacheManager.putAll(tempMap);

    }
}
