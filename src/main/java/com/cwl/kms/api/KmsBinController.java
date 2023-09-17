package com.cwl.kms.api;

import com.cwl.kms.domain.dto.MicroserviceClientCacheDTO;
import com.cwl.kms.domain.vo.base.ResponseDataWrapper;
import com.cwl.kms.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: KmsBinController
 * Package: com.cwl.kms.api
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 21:51
 * @Version 1.0
 */
@Tag(name = "KMS-BIN", description = "伴生服务: KMS-BIN交互接口")
@RestController
@RequestMapping("/kms/api/v1/bin")
public class KmsBinController {

    @Autowired
    private AuthenticationService authenticationService;

    @Operation(summary = "KMS-BIN获取初始私钥")
    @GetMapping("/getPrivateKey")
    public ResponseDataWrapper<MicroserviceClientCacheDTO> getByAppId(@RequestParam(value = "appId") String appId) {
        return ResponseDataWrapper.success(authenticationService.getByAppId(appId));
    }
}
