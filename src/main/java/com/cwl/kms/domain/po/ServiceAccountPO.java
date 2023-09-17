package com.cwl.kms.domain.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author FastJson
 * @Create 2023/8/1 16:31
 */
@Data
public class ServiceAccountPO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    /**
     * 账户标识
     */
    private String serviceAccount;

    /**
     * 账户类型 0 - Caster 1 - MariaDB
     */
    private String appId;

    /**
     * 是否禁用账号: 0 -否 ; 1 -是
     */
    private Integer enabled;
}
