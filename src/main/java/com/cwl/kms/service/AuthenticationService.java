package com.cwl.kms.service;

import com.cwl.kms.domain.dto.AuthenticationDTO;
import com.cwl.kms.domain.dto.MicroserviceCacheDTO;
import com.cwl.kms.domain.dto.MicroserviceClientCacheDTO;
import com.cwl.kms.domain.dto.MicroserviceServerCacheDTO;
import com.cwl.kms.domain.po.AuthenticationPO;
import com.cwl.kms.domain.vo.AuthenticationVO;

import java.util.List;

/**
 * ClassName: AuthenticationService
 * Package: com.cwl.kms.service
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 21:44
 * @Version 1.0
 */
public interface AuthenticationService {

    /**
     * open or update authentication
     *
     * @param authenticationRequest 认证参数
     * @return appId
     */
    Long saveOrUpdate(AuthenticationVO authenticationRequest);

    /**
     * select by appId
     *
     * @param appId application flag
     * @return
     */
    AuthenticationVO selectByAppId(String appId, String deployEnv);

    /**
     * get privateKey from microservice client
     *
     * @param appId application flag
     * @return
     */
    MicroserviceClientCacheDTO getByAppId(String appId);

    /**
     * get publicKey from microservice server
     *
     * @param appId application flag
     * @return
     */
    List<MicroserviceServerCacheDTO> getPublicKey(String appId);

    List<AuthenticationPO> selectByIds(List<Long> serviceIds);

    List<AuthenticationDTO> selectForInit();

    MicroserviceCacheDTO getFromDataBase(String appId);
}
