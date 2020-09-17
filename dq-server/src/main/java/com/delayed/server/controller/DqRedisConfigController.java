package com.delayed.server.controller;

import com.delayed.base.bean.ComResponseBean;
import com.delayed.base.model.DqRedisConfig;
import com.delayed.base.notify.NotifyService;
import com.delayed.server.pojo.vo.DqRedisConfigVo;
import com.delayed.server.service.DqRedisConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @作者: tjx
 * @描述: redis 配置信息操作
 * @创建时间: 创建于16:22 2020/7/16
 **/
@RestController
@RequestMapping("/config/redis/")
public class DqRedisConfigController {

    @Autowired
    private DqRedisConfigService dqRedisConfigService;

    @Autowired
    private NotifyService notifyService;

    /**
     * redis列表
     * @return
     */
    @GetMapping("/list")
    public ComResponseBean list(){
        notifyService.notifyMessage("ces ");
        return dqRedisConfigService.list();
    }

    @PostMapping("/update")
    public ComResponseBean update(@RequestBody DqRedisConfigVo configVo){
        return dqRedisConfigService.save(configVo);
    }

    @PostMapping("/updatePhone")
    public ComResponseBean updatePhone (long id,String warningPhone){
        return dqRedisConfigService.updatePhone(id, warningPhone);
    }



}
