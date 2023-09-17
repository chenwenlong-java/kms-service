package com.cwl.kms.domain.dto;

import lombok.Data;

/**
 * @Author FastJson
 * @Create 2023/8/2 17:22
 */
@Data
public class AsymmetricKeysDTO extends KeyPairDTO {

    private Long TTL;

    private Integer keyType;

    private Integer version;

    private Long serviceId;

}
