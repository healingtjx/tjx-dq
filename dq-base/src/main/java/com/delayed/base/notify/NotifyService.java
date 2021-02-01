package com.delayed.base.notify;

import com.delayed.base.model.DqRedisConfig;
import com.delayed.base.repository.DqRedisConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author: tjx
 * @描述: 通知service
 * @创建时间: 创建于15:05 2020/9/3
 **/

@Slf4j
@Service
public class NotifyService {

    @Autowired
    private DqRedisConfigRepository dqRedisConfigRepository;


    public void notifyMessage(String msg) {
        DqRedisConfig config = dqRedisConfigRepository.findAll().iterator().next();
        if (config == null) {
            log.error("配置错误导致无法通知");
            return;
        }
        String phone = config.getWarningPhone();
        //消息通知模块，留给开发者自己实现
        log.info("消息通知(" + phone + "\t" + msg + ")");
    }
}
