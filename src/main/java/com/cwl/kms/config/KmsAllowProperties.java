package com.cwl.kms.config;

import lombok.Data;

import java.util.List;

/**
 * ClassName: KmsAllowProperties
 * Package: com.cwl.kms.config
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 23:13
 * @Version 1.0
 */
@Data
public class KmsAllowProperties {
    /**
     * allow call kms appId list
     */
    private List<String> appIds;
}
