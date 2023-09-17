package com.cwl.kms.service;

import com.cwl.kms.domain.po.ServiceAccountPO;

import java.util.List;

/**
 * ClassName: ServiceAccountService
 * Package: com.cwl.kms.service
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 21:55
 * @Version 1.0
 */
public interface ServiceAccountService {

    List<ServiceAccountPO> selectAllAccount();

    String selectByAppId(String appId);

    boolean notExist(String serviceAccount);

    ServiceAccountPO selectByServiceAccount(String serviceAccount);

    String encryptAES(String serviceAccount, String key);

    String decryptAES(String serviceAccount, String key);
}
