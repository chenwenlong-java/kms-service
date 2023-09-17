package com.cwl.kms.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * ClassName: AuthenticationVO
 * Package: com.cwl.kms.domain.vo.base
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 21:43
 * @Version 1.0
 */
@Data
@Schema(description = "认证请求")
public class AuthenticationVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "应用ID")
    private String appId;

    @Schema(description = "环境",
            allowableValues = {"dev", "local", "uat", "pre", "prod"})
    private String deployEnv;

    @Schema(description = "提交人信息")
    private String submitter;

    @Schema(description = "是否开启 0 关闭 1 开启",
            allowableValues = {"0", "1"})
    private Integer enabled;

    @Schema(description = "token过期时间；单位：分钟 【取值范围：10-60】",
            allowableValues = {"10", "20", "30", "40", "50", "60"})
    private Integer tokenTTL;

    @Schema(description = "密钥过期时间；单位：天 【取值范围：1-30】")
    private Integer keyTTL;

    @Schema(description = "审批状态: 0 -未审批; 1 -审批通过; 2 -审批驳回")
    private Integer approvalState;
}
