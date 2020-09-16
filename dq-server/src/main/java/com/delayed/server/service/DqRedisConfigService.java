package com.delayed.server.service;

import com.delayed.base.bean.ComResponseBean;
import com.delayed.base.utils.RedisUtils;
import com.delayed.base.utils.ResponseUtils;
import com.delayed.base.model.DqRedisConfig;
import com.delayed.base.repository.DqRedisConfigRepository;
import com.delayed.server.pojo.vo.DqRedisConfigVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @作者: tjx
 * @描述:
 * @创建时间: 创建于16:32 2020/7/16
 **/
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
        return ResponseUtils.succeed(null);
    }

}
