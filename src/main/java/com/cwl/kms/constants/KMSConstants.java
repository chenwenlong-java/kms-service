package com.cwl.kms.constants;

/**
 * ClassName: KMSConstants
 * Package: com.cwl.kms.util
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 22:24
 * @Version 1.0
 */
public class KMSConstants {
    // ====================DEFAULT_APP===========================
    public static final String KMS_APP_ID = "kms.application";
    public static final String AUTH_APP_ID = "authentication.application";



    // ====================ALGORITHM==============================
    public final static String ALGORITHM_RSA = "RSA";
    // 加密方式
    // ECC stream encrypt ==> 速度慢 破解难
    // ECB block encrypt  ==> 速度块 破解快

    // 填充模式
    // NoPadding 不填充 自己选择的密钥对, 明文本 需要是 8的倍数
    // PKCS1Padding 如果不满足8的倍数 那么就填充空格
    public final static String ENCRYPTION_STYLE = "RSA/ECB/PKCS1Padding";
    public final static String ALGORITHM_SHA_256 = "SHA-256";
    public final static String ALGORITHM_PROVIDER = "BC";

    /**
     *  512 普通家用电脑的算力能破解
     *  1024 专业电脑能破解
     *  2048 服务器级别 工业级
     *  4096 金融级别 目前工业级无法破解 军工级
     *  8192 目前没人在用
     */
    public final static int KEY_LENGTH = 2048; // 512 1024 2048 4096 8192
    public final static int KEY_TTL_UNIT_CONVERSION = 86400000;
    public final static int DEFAULT_TOKEN_TTL = 10;
    public final static int DEFAULT_KEY_TTL = 10;
    public final static int INIT_VERSION = 1;
    public final static int MICROSERVICE_KEY_TYPE = 0;
    public final static int MIDDLEWARE_KEY_TYPE = 1;
    public final static long NEVER_EXPIRE_TTL = -1;
    public final static int NEVER_EXPIRE_VERSION = -1;
    public static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    // ====================ALGORITHM==============================

    // ===================COMMUNICATION FLAG======================
    public final static String CLIENT_FLAG = "CLIENT";
    public final static String SERVER_FLAG = "SERVER";
    public final static String ALL_FLAG = "ALL";
}
