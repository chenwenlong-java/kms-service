package com.cwl.kms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: KmsConfig
 * Package: com.cwl.kms.config
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 23:14
 * @Version 1.0
 */
@Configuration
public class KmsConfig {

    @Bean
    @ConfigurationProperties("kms.allow")
    public KmsAllowProperties kmsAllowProperties() {
        return new KmsAllowProperties();
    }
}
