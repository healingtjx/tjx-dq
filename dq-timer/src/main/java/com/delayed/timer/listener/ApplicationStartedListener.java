package com.delayed.timer.listener;

import com.delayed.base.model.DqRedisConfig;
import com.delayed.base.repository.DqRedisConfigRepository;
import com.delayed.base.utils.DelayBucketUtils;
import com.delayed.base.utils.RedisUtils;
import com.delayed.timer.task.HandleTimerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Timer;



/**
 * @author: tjx
 * @描述:
 * @创建时间: 创建于15:05 2020/9/3
 **/
@Slf4j
@Component
public class ApplicationStartedListener implements ApplicationListener<ApplicationStartedEvent> {

    @Autowired
    private DqRedisConfigRepository dqRedisConfigRepository;


    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

        //1.实例化redis
        Iterable<DqRedisConfig> configs = dqRedisConfigRepository.findAll();
        long size = configs.spliterator().getExactSizeIfKnown();
        if (size == 0) {
            new RuntimeException("没有可用的redis实例，请在dq-server里面配置");
            return;
        }
        DqRedisConfig dqRedisConfig = configs.iterator().next();
        String url = dqRedisConfig.getUrl();
        String delayedName = dqRedisConfig.getName();
        int bucket = dqRedisConfig.getBucket();
        //实例化redis
        RedisUtils.initialize(url);
        //实例化DelayBucket
        DelayBucketUtils.initialize(bucket);
        //判断是否连接成功
        if (!RedisUtils.checkStatus()) {
            //抛出异常
            throw new RuntimeException("redis实例化失败，请认真检测配置的redis(url格式:host:port:auth)");

        }
        //打印成功信息
        log.info("redis实例化成功:当前名称:" + delayedName + "\t当前url:" + url);
        //2.拉取 全部的 bucket
        String[] allBucket = DelayBucketUtils.getAllBucket();
        if (allBucket.length == 0) {
            new RuntimeException("没有可用bucket，请在dq-server里面配置");
            return;
        }
        int timer = dqRedisConfig.getTimer();
        int timerSpeed = dqRedisConfig.getTimerSpeed();

        //3.拉取timer 配置个数
        for (int i = 0; i < timer; i++) {
            //3.启动timer
            new Timer().schedule(new HandleTimerTask(allBucket), 1000, timerSpeed * 100);
        }
    }
}
