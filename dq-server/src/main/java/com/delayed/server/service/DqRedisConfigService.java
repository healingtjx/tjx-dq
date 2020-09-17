package com.delayed.server.service;

import com.delayed.base.bean.ComResponseBean;
import com.delayed.base.utils.RedisUtils;
import com.delayed.base.utils.ResponseUtils;
import com.delayed.base.model.DqRedisConfig;
import com.delayed.base.repository.DqRedisConfigRepository;
import com.delayed.server.pojo.vo.DqRedisConfigVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Optional;

/**
 * @作者: tjx
 * @描述:
 * @创建时间: 创建于16:32 2020/7/16
 **/
@Slf4j
@Service
public class DqRedisConfigService {
    @Autowired
    private DqRedisConfigRepository dqRedisConfigRepository;

    public ComResponseBean<Iterable<DqRedisConfig>> list(){
        return ResponseUtils.succeed(dqRedisConfigRepository.findAll());
    }

    public ComResponseBean save(DqRedisConfigVo dqRedisConfigVo){
        Long id = dqRedisConfigVo.getId();
        DqRedisConfig config = dqRedisConfigRepository.findById(id).get();
        if(config == null)
            return ResponseUtils.error(500,"id 不合法");
        BeanUtils.copyProperties(dqRedisConfigVo,config);
        dqRedisConfigRepository.save(config);
        //同步修改 配置文件
        try {
            PropertiesConfiguration conf = new PropertiesConfiguration("application.properties");
            conf.setProperty("delayed.timer",config.getTimer());
            conf.setProperty("delayed.timer.speed",config.getTimerSpeed());
            conf.setProperty("delayed.consume",config.getConsume());
            conf.setProperty("delayed.consume.speed",config.getConsumeSpeed());
            conf.save();
        } catch (ConfigurationException e) {
            log.error("修改配置文件出错:"+e);
            //回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return ResponseUtils.succeed(null);
    }

    public ComResponseBean updatePhone(long id,String warningPhone) {

        DqRedisConfig config = dqRedisConfigRepository.findById(id).get();
        if(config == null)
            return ResponseUtils.error(500,"id 不合法");
        config.setWarningPhone(warningPhone);
        dqRedisConfigRepository.save(config);
        //同步修改 配置文件
        try {
            PropertiesConfiguration conf = new PropertiesConfiguration("application.properties");
            conf.setProperty("warning.phone",warningPhone);
            conf.save();
        } catch (ConfigurationException e) {
            log.error("修改配置文件出错:"+e);
            //回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        }
        return ResponseUtils.succeed(null);
    }

}

