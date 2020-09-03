package com.delayed.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @作者: tjx
 * @描述:  配置
 * @创建时间: 创建于14:53 2020/7/15
 **/
@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping
    public String index(){
        return "index";
    }

}
