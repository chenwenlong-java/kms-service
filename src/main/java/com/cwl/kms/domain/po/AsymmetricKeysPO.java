package com.cwl.kms.domain.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author FastJson
 * @Create 2023/8/1 16:34
 */
@Data
public class AsymmetricKeysPO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 密钥过期时间，-1 表示永不过期
     */
    private Long TTL;

    /**
     * 版本号 负数表示已经删除
     */
    private Integer version;

    /**
     * 密钥类型 0 - 微服务 1 - 中间件
     */
    private Integer keyType;

    /**
     * 关联服务的ID；如果是中间件，则为中间件的ID；如果是微服务，则为微服务的ID
     */
    private Long serviceId;

    /**
     * 上一个时钟周期，用来计算下一个时钟周期
     */
    private Long lastClockCycle;
}
