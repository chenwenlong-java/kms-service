package com.cwl.kms.service.impl;

import com.cwl.kms.cache.MicroserviceCacheManager;
import com.cwl.kms.constants.KMSConstants;
import com.cwl.kms.domain.dto.AuthenticationDTO;
import com.cwl.kms.domain.dto.MicroserviceCacheDTO;
import com.cwl.kms.domain.dto.MicroserviceClientCacheDTO;
import com.cwl.kms.domain.dto.MicroserviceServerCacheDTO;
import com.cwl.kms.domain.po.AuthenticationPO;
import com.cwl.kms.domain.vo.AuthenticationVO;
import com.cwl.kms.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ClassName: AuthenticationServiceImpl
 * Package: com.cwl.kms.service.impl
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 21:48
 * @Version 1.0
 */
@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private MicroserviceCacheManager microserviceCacheManager;

    @Override
    public Long saveOrUpdate(AuthenticationVO authenticationRequest) {
        return null;
    }

    @Override
    public AuthenticationVO selectByAppId(String appId, String deployEnv) {
        AuthenticationVO resultVO = new AuthenticationVO();
        resultVO.setAppId(KMSConstants.KMS_APP_ID);
        resultVO.setId(1L);
        resultVO.setEnabled(1);
        resultVO.setSubmitter("chenwenlong");
        resultVO.setApprovalState(1);
        resultVO.setKeyTTL(10);
        resultVO.setTokenTTL(30);
        resultVO.setDeployEnv("dev");
        return resultVO;
    }

    @Override
    public MicroserviceClientCacheDTO getByAppId(String appId) {
        // get from cache
        MicroserviceClientCacheDTO clientCache = microserviceCacheManager.getFromClient(appId);
        if (Objects.isNull(clientCache)) {
            throw new RuntimeException("key does not exist");
        }
        return clientCache;
    }

    @Override
    public List<MicroserviceServerCacheDTO> getPublicKey(String appId) {
        return microserviceCacheManager.getFromServer(appId).entrySet().stream()
                .map(entry -> {
                    MicroserviceCacheDTO cacheDTO = entry.getValue();
                    MicroserviceServerCacheDTO serverCacheDTO = new MicroserviceServerCacheDTO();
                    serverCacheDTO.setPublicKey(cacheDTO.getPublicKey());
                    serverCacheDTO.setKeyTTL(cacheDTO.getKeyTTL());
                    serverCacheDTO.setVersion(entry.getKey());
                    return serverCacheDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<AuthenticationPO> selectByIds(List<Long> serviceIds) {
        List<AuthenticationPO> resultList = new ArrayList<>();
        AuthenticationPO poOne = new AuthenticationPO();
        poOne.setId(1L);
        poOne.setEnabled(true);
        poOne.setSubmitter("chenwenlong");
        poOne.setAppId(KMSConstants.KMS_APP_ID);
        poOne.setDeployEnv("dev");
        poOne.setTokenTTL(30);
        poOne.setCtime(LocalDateTime.now());
        poOne.setMtime(LocalDateTime.now());
        poOne.setApprovalState(1);
        AuthenticationPO poTwo = new AuthenticationPO();
        poTwo.setId(2L);
        poTwo.setEnabled(true);
        poTwo.setSubmitter("yuanyuan");
        poTwo.setAppId(KMSConstants.AUTH_APP_ID);
        poTwo.setDeployEnv("dev");
        poTwo.setTokenTTL(20);
        poTwo.setCtime(LocalDateTime.now());
        poTwo.setMtime(LocalDateTime.now());
        poTwo.setApprovalState(1);

        resultList.add(poOne);
        resultList.add(poTwo);
        return resultList;
    }

    @Override
    public List<AuthenticationDTO> selectForInit() {
        List<AuthenticationDTO> resultList = new ArrayList<>();
        AuthenticationDTO dtoOne = new AuthenticationDTO();
        dtoOne.setId(1L);
        dtoOne.setAppId(KMSConstants.KMS_APP_ID);
        dtoOne.setTokenTTL(30);
        AuthenticationDTO dtoTwo = new AuthenticationDTO();
        dtoTwo.setAppId(KMSConstants.AUTH_APP_ID);
        dtoTwo.setId(2L);
        dtoTwo.setTokenTTL(20);

        resultList.add(dtoOne);
        resultList.add(dtoTwo);
        return resultList;
    }

    @Override
    public MicroserviceCacheDTO getFromDataBase(String appId) {
        return new MicroserviceCacheDTO();
    }
}
