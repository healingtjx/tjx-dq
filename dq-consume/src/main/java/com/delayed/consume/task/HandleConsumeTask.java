package com.delayed.consume.task;

import com.alibaba.fastjson.JSON;
import com.delayed.base.bean.Job;
import com.delayed.base.enumeration.CommonKeyEnum;
import com.delayed.base.notify.NotifyService;
import com.delayed.base.repository.DqTopicConfigRepository;
import com.delayed.base.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * @author: tjx
 * @描述: 处理 接口回调
 * @创建时间: 创建于14:36 2020/9/9
 **/
@Slf4j
public class HandleConsumeTask extends TimerTask {
    private NotifyService notifyService;
    private DqTopicConfigRepository dqTopicConfigRepository;
    /**
     * 线程池(可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程)
     */
    private ExecutorService cachedThreadPool;

    public HandleConsumeTask(DqTopicConfigRepository dqTopicConfigRepository, NotifyService notifyService) {
        this.dqTopicConfigRepository = dqTopicConfigRepository;
        this.notifyService = notifyService;
        cachedThreadPool = new ThreadPoolExecutor(20, 50,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),new HandleThreadFactory("ConsumeTask"));
    }

    @Override
    public void run() {

        //1. spop  ready queue 随机的一个元素消费
        String jobId = RedisUtils.spop(CommonKeyEnum.readyQueue.name());
        if (jobId == null) {
            return;
        }

        //2. 拉取job元数据
        String jobStr = RedisUtils.get(jobId);
        if (jobStr == null) {
            return;
        }
        Job job = null;
        try {
            job = JSON.parseObject(jobStr, Job.class);
        } catch (Exception e) {
            log.error("HandleConsumeTask 读取" + jobId + "\t错误");
        }
        if (job == null) {
            return;
        }
        //消费
        log.info("开始消费" + "\t" + jobStr);
        long s = System.currentTimeMillis();
        cachedThreadPool.execute(new HandeCalByHttpTask(job, dqTopicConfigRepository, notifyService));
        long e = System.currentTimeMillis();
        log.info("丢入线程池时间: "+(e-s)+"ms");
    }
}
