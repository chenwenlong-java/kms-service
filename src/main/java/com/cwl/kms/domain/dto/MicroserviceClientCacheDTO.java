package com.cwl.kms.domain.dto;

import lombok.Data;

/**
 * @Author FastJson
 * @Create 2023/8/4 11:05
 */
@Data
public class MicroserviceClientCacheDTO {
    private String privateKey;
    private String business;
    private String businessDomain;
    private Integer tokenTTL;
    private Long keyTTL;
    private Integer version;
}
