package com.delayed.consume.listener;

import com.delayed.base.model.DqRedisConfig;
import com.delayed.base.repository.DqRedisConfigRepository;
import com.delayed.base.repository.DqTopicConfigRepository;
import com.delayed.base.utils.DelayBucketUtils;
import com.delayed.base.utils.RedisUtils;
import com.delayed.consume.task.HandleConsumeTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Timer;


/**
 * @作者: tjx
 * @描述:
 * @创建时间: 创建于15:05 2020/9/3
 **/
@Slf4j
@Component
public class ApplicationStartedListener implements ApplicationListener<ApplicationStartedEvent> {

    @Autowired
    private DqRedisConfigRepository dqRedisConfigRepository;

    @Autowired
    private DqTopicConfigRepository dqTopicConfigRepository;

    @Value("${redis.consume.speed}")
    private int speed;

    @Value("${redis.delayed.consume}")
    private int consume;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

        //1.实例化redis
        Iterable<DqRedisConfig> configs = dqRedisConfigRepository.findAll();
        long size = configs.spliterator().getExactSizeIfKnown();
        if(size  == 0){
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
        if(!RedisUtils.checkStatus()){
            //抛出异常
            throw new RuntimeException("redis实例化失败，请认真检测配置的redis(url格式:host:port:auth)");

        }
        //打印成功信息
        log.info("redis实例化成功:当前名称:"+delayedName+"\t当前url:"+url);


        //3.拉取consume 配置个数
        for (int i = 0; i < consume; i++) {
            //2. 启动 consume
            new Timer().schedule(new HandleConsumeTask(dqTopicConfigRepository),1000,speed*100);
        }




    }
}
