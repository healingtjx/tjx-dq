package com.delayed.consume.task;

import com.alibaba.fastjson.JSON;
import com.delayed.base.bean.Job;
import com.delayed.base.enumeration.CommonKeyEnum;
import com.delayed.base.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

/**
 * @作者: tjx
 * @描述: 处理 接口回调
 * @创建时间: 创建于14:36 2020/9/9
 **/
@Slf4j
public class HandleConsumeTask extends TimerTask {

    @Override
    public void run() {

        //1. spop  ready queue 随机的一个元素消费
        String jobId = RedisUtils.spop(CommonKeyEnum.readyQueue.name());
        if(jobId==null)
            return;

        //2. 拉取job元数据
        String jobStr = RedisUtils.get(jobId);
        if(jobStr == null)
            return;
        Job job = null;
        try {
            job = JSON.parseObject(jobStr, Job.class);
        }catch (Exception e){
            log.error("HandleConsumeTask 读取"+jobId +"\t错误");
        }
        if(job == null)
            return;
        //消费
        log.info("消费成功"+"\t"+jobStr);

        //删除所有相关元数据
        RedisUtils.del(jobId);
        RedisUtils.del(jobId+CommonKeyEnum.incr);

    }
}
