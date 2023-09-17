package com.cwl.kms.service.impl;

import com.cwl.kms.config.KmsAllowProperties;
import com.cwl.kms.constants.KMSConstants;
import com.cwl.kms.domain.po.ServiceAccountPO;
import com.cwl.kms.service.ServiceAccountService;
import com.cwl.kms.util.UUIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * ClassName: ServiceAccountServiceImpl
 * Package: com.cwl.kms.service.impl
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 21:55
 * @Version 1.0
 */
@Slf4j
@Service
public class ServiceAccountServiceImpl implements ServiceAccountService {

    @Autowired
    private KmsAllowProperties kmsAllowProperties;

    private static final int BLOCK_SIZE = 32; // 256 bits

    @Override
    public List<ServiceAccountPO> selectAllAccount() {
        List<ServiceAccountPO> resultList = new ArrayList<>();
        ServiceAccountPO accountPO1 = new ServiceAccountPO();
        accountPO1.setServiceAccount(UUIDGenerator.CHEN_ACCOUNT);
        accountPO1.setId(1L);
        accountPO1.setEnabled(1);
        accountPO1.setAppId(KMSConstants.KMS_APP_ID);
        ServiceAccountPO accountPO2 = new ServiceAccountPO();
        accountPO2.setAppId(KMSConstants.AUTH_APP_ID);
        accountPO2.setServiceAccount(UUIDGenerator.YUAN_ACCOUNT);
        accountPO1.setId(2L);
        accountPO1.setEnabled(1);
        return resultList;
    }

    @Override
    public String selectByAppId(String appId) {
        if (kmsAllowProperties.getAppIds().contains(appId)) {
            throw new RuntimeException("no permission , appId : " + appId);
        }
        return StringUtils.EMPTY;
    }

    @Override
    public boolean notExist(String serviceAccount) {
        return selectAllAccount().contains(serviceAccount);
    }

    @Override
    public ServiceAccountPO selectByServiceAccount(String serviceAccount) {
        return null;
    }

    @Override
    public String encryptAES(String serviceAccount, String key) {
        byte[] keyDecode = Base64.getDecoder().decode(key);
        AESEngine engine = new AESEngine();
        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CFBBlockCipher(engine, BLOCK_SIZE));
        CipherParameters params = new ParametersWithIV(new KeyParameter(keyDecode), new byte[BLOCK_SIZE]);

        cipher.init(true, params);

        byte[] inputBytes = serviceAccount.getBytes(StandardCharsets.UTF_8);
        byte[] outputBytes = new byte[cipher.getOutputSize(inputBytes.length)];

        int outputLen = cipher.processBytes(inputBytes, 0, inputBytes.length, outputBytes, 0);
        try {
            cipher.doFinal(outputBytes, outputLen);
        } catch (InvalidCipherTextException e) {
            log.error("serviceAccount encrypt error : {}", e.getMessage());
        }

        return Base64.getEncoder().encodeToString(outputBytes);
    }

    @Override
    public String decryptAES(String serviceAccount, String key) {
        byte[] keyDecode = Base64.getDecoder().decode(key);
        AESEngine engine = new AESEngine();
        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CFBBlockCipher(engine, BLOCK_SIZE));
        CipherParameters params = new ParametersWithIV(new KeyParameter(keyDecode), new byte[BLOCK_SIZE]);

        cipher.init(false, params);

        byte[] inputBytes = Base64.getDecoder().decode(serviceAccount);
        byte[] outputBytes = new byte[cipher.getOutputSize(inputBytes.length)];

        int outputLen = cipher.processBytes(inputBytes, 0, inputBytes.length, outputBytes, 0);
        try {
            cipher.doFinal(outputBytes, outputLen);
        } catch (Exception exception) {
            log.error("service account decrypt error: {}", exception.getMessage());
        }
        // BC
        return StringUtils.trim(new String(outputBytes, StandardCharsets.UTF_8));
    }
}
