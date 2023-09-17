package com.cwl.kms.api;

import com.cwl.kms.domain.dto.MicroserviceClientCacheDTO;
import com.cwl.kms.domain.dto.MicroserviceServerCacheDTO;
import com.cwl.kms.domain.vo.base.ResponseDataWrapper;
import com.cwl.kms.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName: MicroserviceController
 * Package: com.cwl.kms.api
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 21:52
 * @Version 1.0
 */
@Tag(name = "微服务", description = "提供给微服务的认证接口")
@RestController
@RequestMapping("/kms/api/v1/microservice")
public class MicroserviceController {

    @Autowired
    private AuthenticationService authenticationService;

    @Operation(summary = "微服务Client通过SDK获取私钥")
    @GetMapping("/getPrivateKey")
    public ResponseDataWrapper<MicroserviceClientCacheDTO> getByAppId(@RequestParam(value = "appId") String appId) {
        return ResponseDataWrapper.success(authenticationService.getByAppId(appId));
    }

    @GetMapping("/getPublicKey")
    public ResponseDataWrapper<List<MicroserviceServerCacheDTO>> getPublicKey(@RequestParam(value = "appId") String appId) {
        return ResponseDataWrapper.success(authenticationService.getPublicKey(appId));
    }
}
