package com.delayed.server.controller;

import com.delayed.base.model.ComResponseBean;
import com.delayed.server.model.DqTopicConfig;
import com.delayed.server.service.DqRedisConfigService;
import com.delayed.server.service.DqTopicConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @作者: tjx
 * @描述: top 配置信息操作
 * @创建时间: 创建于16:22 2020/7/16
 **/
@Slf4j
@RestController
@RequestMapping("/v1/topic/")
public class DqTopicConfigController {


    @Autowired
    private DqTopicConfigService dqTopicConfigService;

    /**
     * redis列表
     * @return
     */
    @GetMapping("/list")
    public ComResponseBean list(){
        return dqTopicConfigService.list();
    }

    @PostMapping("/add")
    public ComResponseBean add(@RequestBody DqTopicConfig dqTopicConfig){
        return dqTopicConfigService.add(dqTopicConfig);
    }
}
