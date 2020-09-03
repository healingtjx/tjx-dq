package com.delayed.server.listener;

import com.delayed.base.utils.RedisUtils;
import com.delayed.server.ServerMain;
import com.delayed.server.model.DqRedisConfig;
import com.delayed.server.repository.DqRedisConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

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

    @Value("${redis.delayed.name}")
    private String delayedName ;

    @Value("${redis.delayed.url}")
    private String url;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        try {
            //修改数据库
            List<DqRedisConfig> configs = dqRedisConfigRepository.findByUrl(url);
            //如果找到配置相同的 不做任何操作
            if(configs.size()>0)
                return;
            //没有找到匹配的
            //1.删除所有
            dqRedisConfigRepository.deleteAll();
            //2.添加当前配置
            DqRedisConfig newDq = new DqRedisConfig();
            newDq.setId(1l);
            newDq.setUrl(url);
            newDq.setName(delayedName);
            newDq.setStatus(0);

            //其他连接状态这个版本暂时不处理
            newDq.setVserion("未知");
            newDq.setRamUsed("未知");
            newDq.setAmountAll("未知");
            newDq.setAmountDel("未知");
            newDq.setAmountExisting("未知");
            dqRedisConfigRepository.save(newDq);
        }finally {
            //实例化redis
            RedisUtils.initialize(url);
            //判断是否连接成功
            log.info(RedisUtils.checkStatus()+"");
            if(RedisUtils.checkStatus()){
                //打印成功信息
                log.info("redis实例化成功:当前名称:"+delayedName+"\t当前url:"+url);
            }else {
                //抛出异常
                throw new RuntimeException("redis实例化失败，请认真检测配置的redis(url格式:host:port:auth)");

            }
        }



    }
}
