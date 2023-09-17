package com.cwl.kms.util;

import com.cwl.kms.constants.KMSConstants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * ClassName: UUIDGenerator
 * Package: com.cwl.kms.util
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 23:16
 * @Version 1.0
 */
public class UUIDGenerator {

    public static final String CHEN_ACCOUNT = generateUUID();
    public static final String YUAN_ACCOUNT = generateUUID();

    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString().replace("-", "");

        // Use SHA-256 to create a hash and then truncate to desired length
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(KMSConstants.ALGORITHM_SHA_256);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("init middleware serviceAccount falied , besause no such algorithm exception");
        }
        byte[] hash = md.digest(uuidString.getBytes());
        String hashHex = bytesToHex(hash);

        return hashHex.substring(0, 64); // Truncate to fixed length of 64
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int value = bytes[i] & 0xFF;
            hexChars[i * 2] = KMSConstants.HEX_ARRAY[value >>> 4];
            hexChars[i * 2 + 1] = KMSConstants.HEX_ARRAY[value & 0x0F];
        }
        return new String(hexChars);
    }
}
