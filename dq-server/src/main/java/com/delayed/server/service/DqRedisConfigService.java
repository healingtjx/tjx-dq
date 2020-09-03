package com.delayed.server.service;

import com.delayed.base.model.ComResponseBean;
import com.delayed.base.utils.ResponseUtils;
import com.delayed.server.model.DqRedisConfig;
import com.delayed.server.repository.DqRedisConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @作者: tjx
 * @描述:
 * @创建时间: 创建于16:32 2020/7/16
 **/
@Service
public class DqRedisConfigService {
    @Autowired
    private DqRedisConfigRepository dqRedisConfigRepository;

    public ComResponseBean<Iterable<DqRedisConfig>> list(){
        return ResponseUtils.succeed(dqRedisConfigRepository.findAll());
    }

}
