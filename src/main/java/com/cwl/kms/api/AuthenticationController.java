package com.cwl.kms.api;

import com.cwl.kms.domain.vo.AuthenticationVO;
import com.cwl.kms.domain.vo.base.ResponseDataWrapper;
import com.cwl.kms.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: AuthenticationController
 * Package: com.cwl.kms.api
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 21:40
 * @Version 1.0
 */
@Tag(name = "认证", description = "认证接口")
@RestController
@RequestMapping("/kms/api/v1/authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Operation(summary = "新增或修改认证信息, 目前仅提供给navigator-console调用")
    @PostMapping("/saveOrUpdate")
    public ResponseDataWrapper<Long> saveOrUpdate(@RequestBody AuthenticationVO authenticationVO) {
        return ResponseDataWrapper.success(authenticationService.saveOrUpdate(authenticationVO));
    }

    @Operation(summary = "查询认证信息, 目前仅提供给navigator-console调用")
    @GetMapping("/selectByAppId")
    public ResponseDataWrapper<AuthenticationVO> selectByAppId(@RequestParam(value = "appId") String appId,
                                                               @RequestParam(value = "deployEnv") String deployEnv) {
        return ResponseDataWrapper.success(authenticationService.selectByAppId(appId, deployEnv));
    }
}
