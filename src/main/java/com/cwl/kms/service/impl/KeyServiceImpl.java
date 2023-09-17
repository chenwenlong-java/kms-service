package com.cwl.kms.service.impl;

import com.cwl.kms.constants.KMSConstants;
import com.cwl.kms.domain.dto.AsymmetricKeysDTO;
import com.cwl.kms.domain.dto.KeyPairDTO;
import com.cwl.kms.domain.po.AsymmetricKeysPO;
import com.cwl.kms.service.KeyService;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ClassName: KeyServiceImpl
 * Package: com.cwl.kms.service.impl
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 22:27
 * @Version 1.0
 */
@Slf4j
@Service
public class KeyServiceImpl implements KeyService {
    @Override
    public KeyPairDTO generateKey() {
        // add Bouncy Castle provider
        Security.addProvider(new BouncyCastleProvider());

        // generate RSA key pair
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(KMSConstants.ALGORITHM_RSA, KMSConstants.ALGORITHM_PROVIDER);
        } catch (GeneralSecurityException e) {
            log.error("generator key error : {}", e.getMessage());
        }
        keyPairGenerator.initialize(KMSConstants.KEY_LENGTH);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // byte[] binary file
        String privateKey = Base64.toBase64String(keyPair.getPrivate().getEncoded());
        String publicKey = Base64.toBase64String(keyPair.getPublic().getEncoded());

        KeyPairDTO resultDTO = new KeyPairDTO();
        resultDTO.setPrivateKey(privateKey);
        resultDTO.setPublicKey(publicKey);

        return resultDTO;
    }

    @Override
    public String publicKeyEncryption(String plaintext, String publicKeyString) {
        Cipher encryptCipher;
        PublicKey publicKey;
        byte[] encryptedBytes;
        try {
            encryptCipher = Cipher.getInstance(KMSConstants.ENCRYPTION_STYLE, KMSConstants.ALGORITHM_PROVIDER);
            publicKey = loadPublicKey(publicKeyString);
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encryptedBytes = encryptCipher.doFinal(plaintext.getBytes());
        } catch (Exception exception) {
            throw new RuntimeException("publicKey encryption error " + exception.getMessage());
        }
        return Base64.toBase64String(encryptedBytes);
    }

    @Override
    public PublicKey loadPublicKey(String publicKeyBase64String) {
        byte[] publicKeyBytes = Base64.decode(publicKeyBase64String);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KMSConstants.ALGORITHM_RSA);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("load publicKey error, no such algorithm");
        }
    }

    @Override
    public PrivateKey loadPrivateKey(String privateKeyBase64String) {
        byte[] privateKeyBytes = Base64.decode(privateKeyBase64String);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KMSConstants.ALGORITHM_RSA);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("load privateKey error, no such algorithm");
        }
    }

    @Override
    public List<AsymmetricKeysDTO> selectMiddlewareByServiceId(List<Long> serviceIds) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<AsymmetricKeysDTO> selectMicroserviceByServiceIds(List<Long> serviceIds) {
        long now = System.currentTimeMillis();
        KeyPairDTO keyPairDTO = generateKey();

        List<AsymmetricKeysDTO> resultList = new ArrayList<>();
        AsymmetricKeysDTO keysDTO1 = new AsymmetricKeysDTO();
        keysDTO1.setKeyType(0);
        keysDTO1.setServiceId(1L);
        keysDTO1.setTTL(now + KMSConstants.DEFAULT_KEY_TTL * KMSConstants.KEY_TTL_UNIT_CONVERSION);
        keysDTO1.setVersion(1);
        keysDTO1.setPublicKey(keyPairDTO.getPublicKey());
        keysDTO1.setPrivateKey(keyPairDTO.getPrivateKey());
        AsymmetricKeysDTO keysDTO2 = new AsymmetricKeysDTO();
        keysDTO2.setPrivateKey(keyPairDTO.getPrivateKey());
        keysDTO2.setPublicKey(keyPairDTO.getPublicKey());
        keysDTO2.setKeyType(0);
        keysDTO2.setServiceId(2L);
        keysDTO2.setTTL(now + KMSConstants.DEFAULT_KEY_TTL * KMSConstants.KEY_TTL_UNIT_CONVERSION);
        keysDTO2.setVersion(1);

        resultList.add(keysDTO1);
        resultList.add(keysDTO2);
        return resultList;
    }

    @Override
    public List<AsymmetricKeysPO> selectLatestVersion() {
        // version
        return Collections.EMPTY_LIST;
    }

    @Override
    public int batchInsert(List<AsymmetricKeysPO> latestVersionList) {
        return 0;
    }

    @Override
    public int insert(AsymmetricKeysPO asymmetricKeysPO) {
        return 0;
    }

    @Override
    public int updateByServiceId(Long id, Long ttl) {
        return 0;
    }

    @Override
    public AsymmetricKeysDTO selectMicroserviceByServiceId(Long id) {
        return null;
    }

    @Override
    public AsymmetricKeysPO selectTTLBySingleServiceId(Long id) {
        return null;
    }

    @Override
    public AsymmetricKeysPO selectByServiceId(Long id) {
        return null;
    }
}
