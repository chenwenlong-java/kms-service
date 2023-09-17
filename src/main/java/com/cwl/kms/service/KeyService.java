package com.cwl.kms.service;

import com.cwl.kms.domain.dto.AsymmetricKeysDTO;
import com.cwl.kms.domain.dto.KeyPairDTO;
import com.cwl.kms.domain.po.AsymmetricKeysPO;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

/**
 * ClassName: KeyService
 * Package: com.cwl.kms.service
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 22:26
 * @Version 1.0
 */
public interface KeyService {

    /**
     * generate key
     *
     * @return
     */
    KeyPairDTO generateKey();

    /**
     * use publicKey encryption plaintext, Returns the result after using Base64 encoding
     * @param plaintext
     * @return
     */
    String publicKeyEncryption(String plaintext,String publicKey);

    /**
     * load publicKey from base64 string
     *
     * @param publicKeyBase64String
     * @return
     */
    PublicKey loadPublicKey(String publicKeyBase64String);

    /**
     * load privateKey from base64 string
     *
     * @param privateKeyBase64String
     * @return
     */
    PrivateKey loadPrivateKey(String privateKeyBase64String);

    List<AsymmetricKeysDTO> selectMiddlewareByServiceId(List<Long> serviceIds);

    List<AsymmetricKeysDTO> selectMicroserviceByServiceIds(List<Long> serviceIds);

    List<AsymmetricKeysPO> selectLatestVersion();

    int batchInsert(List<AsymmetricKeysPO> latestVersionList);

    int insert(AsymmetricKeysPO asymmetricKeysPO);

    int updateByServiceId(Long id, Long ttl);

    AsymmetricKeysDTO selectMicroserviceByServiceId(Long id);

    AsymmetricKeysPO selectTTLBySingleServiceId(Long id);

    AsymmetricKeysPO selectByServiceId(Long id);
}
