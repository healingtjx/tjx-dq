package com.delayed.server.controller;

import com.delayed.base.bean.ComResponseBean;
import com.delayed.base.model.DqTopicConfig;
import com.delayed.server.service.DqTopicConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: tjx
 * @描述: top 配置信息操作
 * @创建时间: 创建于16:22 2020/7/16
 **/
@Slf4j
@RestController
@RequestMapping("/config/topic/")
public class DqTopicConfigController {


    @Autowired
    private DqTopicConfigService dqTopicConfigService;

    /**
     * redis列表
     *
     * @return
     */
    @GetMapping("/list")
    public ComResponseBean list() {
        return dqTopicConfigService.list();
    }

    /**
     * 添加
     *
     * @param dqTopicConfig
     * @return
     */
    @PostMapping("/add")
    public ComResponseBean add(@RequestBody DqTopicConfig dqTopicConfig) {
        return dqTopicConfigService.add(dqTopicConfig);
    }

    @PostMapping("/del")
    public ComResponseBean del(int id) {
        return dqTopicConfigService.del(id);
    }
}
