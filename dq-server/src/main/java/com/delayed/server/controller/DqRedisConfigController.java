package com.delayed.server.controller;

import com.delayed.base.model.ComResponseBean;
import com.delayed.server.service.DqRedisConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @作者: tjx
 * @描述: redis 配置信息操作
 * @创建时间: 创建于16:22 2020/7/16
 **/
@RestController
@RequestMapping("/v1/redis/")
public class DqRedisConfigController {

    @Autowired
    private DqRedisConfigService dqRedisConfigService;

    /**
     * redis列表
     * @return
     */
    @GetMapping("/list")
    public ComResponseBean list(){
        return dqRedisConfigService.list();
    }

}
