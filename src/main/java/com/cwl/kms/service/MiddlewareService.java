package com.cwl.kms.service;

import com.cwl.kms.domain.vo.MiddlewareVO;

import java.util.List;
import java.util.Set;

/**
 * ClassName: MiddlewareService
 * Package: com.cwl.kms.service
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 21:53
 * @Version 1.0
 */
public interface MiddlewareService {

    int init(String serviceAccount, List<MiddlewareVO> middlewareVOList);

    String getValue(String key);

    void refresh(String key);

    List<Long> selectByAppIds(Set<String> appIds);

    String getPrivateKey(String serviceAccount);
}
