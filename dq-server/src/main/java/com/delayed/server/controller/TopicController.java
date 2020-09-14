package com.delayed.server.controller;

import com.delayed.base.bean.ComResponseBean;
import com.delayed.base.utils.ResponseUtils;
import com.delayed.server.pojo.vo.TopicVo;
import com.delayed.server.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @作者: tjx
 * @描述: 处理 topic 添加，删除 等操作
 * @创建时间: 创建于11:16 2020/9/7
 **/
@Slf4j
@RestController
@RequestMapping("/v1/topic/")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @PostMapping("/submit")
    public ComResponseBean submit(@RequestBody TopicVo topicVo){
        return topicService.submit(topicVo);
    }


    @RequestMapping("/callBack")
    public ComResponseBean callBack(String id,String test){
        System.out.println(id +"\t" +test);
        return ResponseUtils.succeed(id);
    }
}
