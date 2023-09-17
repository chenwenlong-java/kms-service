package com.cwl.kms.domain.dto;

import lombok.Data;

/**
 * @Author FastJson
 * @Create 2023/8/2 17:03
 */
@Data
public class MicroserviceCacheDTO extends KeyPairDTO {
    private String business;
    private String businessDomain;
    private long keyTTL;
    private int tokenTTL;
    private int version;
}
