package com.cwl.kms.service.impl;

import com.cwl.kms.domain.vo.MiddlewareVO;
import com.cwl.kms.service.MiddlewareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * ClassName: MiddlewareServiceImpl
 * Package: com.cwl.kms.service.impl
 * Description:
 *
 * @Author chenwenlong
 * @Create 2023/9/15 21:54
 * @Version 1.0
 */
@Slf4j
@Service
public class MiddlewareServiceImpl implements MiddlewareService {

    @Override
    public int init(String serviceAccount, List<MiddlewareVO> middlewareVOList) {
        return 0;
    }

    @Override
    public String getValue(String key) {
        return null;
    }

    @Override
    public void refresh(String key) {
        // NOOP
    }

    @Override
    public List<Long> selectByAppIds(Set<String> appIds) {
        return null;
    }

    @Override
    public String getPrivateKey(String serviceAccount) {
        return null;
    }
}
