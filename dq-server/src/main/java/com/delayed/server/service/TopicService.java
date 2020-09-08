package com.delayed.server.service;

import com.alibaba.fastjson.JSON;
import com.delayed.base.bean.ComResponseBean;
import com.delayed.base.bean.Job;
import com.delayed.base.utils.DelayBucketUtils;
import com.delayed.base.utils.RedisUtils;
import com.delayed.base.utils.ResponseUtils;
import com.delayed.base.utils.StringUtil;
import com.delayed.base.model.DqTopicConfig;
import com.delayed.server.pojo.enumeration.JobStatusEnum;
import com.delayed.server.pojo.vo.TopicVo;
import com.delayed.base.repository.DqTopicConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @作者: tjx
 * @描述:
 * @创建时间: 创建于11:40 2020/9/7
 **/
@Slf4j
@Service
public class TopicService {

    @Autowired
    private DqTopicConfigRepository dqTopicConfigRepository;

    public ComResponseBean submit(TopicVo topic){
        //1.校验 参数
        if(StringUtil.isHaveNull(topic.getBody(),topic.getCmd(),topic.getId(),topic.getTopic())){
            return ResponseUtils.error(500,"请检查传入的参数");
        }

        //2.校验 topic 真实性
        List<DqTopicConfig> topicConfigs = dqTopicConfigRepository.findByName(topic.getTopic());
        if(topicConfigs.size() == 0)
            return ResponseUtils.error(500,"topic 不合法");
        DqTopicConfig dqTopicConfig = topicConfigs.get(0);
        //3.存入 job pool
        Job job = new Job();
        job.setId(topic.getId());
        job.setBody(topic.getBody());
        job.setTopic(topic.getTopic());
        job.setDelay(dqTopicConfig.getDelayTime());
        job.setTtr(dqTopicConfig.getOverTime());
        job.setStatus(JobStatusEnum.ready.name());
        if(!(RedisUtils.get(topic.getId()) == null))
            return ResponseUtils.error(500,"id已经存在");
        if(!RedisUtils.setKV(topic.getId(), JSON.toJSONString(job)))
            return ResponseUtils.error(500,"redis添加失败");

        //4. 存入   Delay Bucket
        String bucketKey = DelayBucketUtils.getNextBucket();
        if(!RedisUtils.zaddOne(bucketKey,topic.getId()))
            return ResponseUtils.error(500,"Delay Bucket插入失败");

        return ResponseUtils.succeed(null);

    }
}
