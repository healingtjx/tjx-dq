package com.delayed.server.service;

import com.delayed.base.model.ComResponseBean;
import com.delayed.base.utils.ResponseUtils;
import com.delayed.server.model.DqTopicConfig;
import com.delayed.server.repository.DqTopicConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @作者: tjx
 * @描述:
 * @创建时间: 创建于17:01 2020/7/16
 **/
@Service
public class DqTopicConfigService {

    @Autowired
    private DqTopicConfigRepository dqTopicConfigRepository;

    public ComResponseBean<Iterable<DqTopicConfig>> list(){
        return ResponseUtils.succeed(dqTopicConfigRepository.findAll());
    }

    public ComResponseBean add(DqTopicConfig dqTopicConfig){
        DqTopicConfig save = dqTopicConfigRepository.save(dqTopicConfig);
        return ResponseUtils.succeed(save);
    }
}
