package com.delayed.timer.task;


import com.alibaba.fastjson.JSON;
import com.delayed.base.bean.Job;
import com.delayed.base.enumeration.CommonKeyEnum;
import com.delayed.base.enumeration.JobStatusEnum;
import com.delayed.base.utils.DelayBucketUtils;
import com.delayed.base.utils.RedisUtils;
import com.delayed.base.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Set;
import java.util.TimerTask;
import java.util.UUID;

/**
 * @author: tjx
 * @描述: 处理 timmer 逻辑 的task
 * 轮询各个bucket
 * @创建时间: 创建于15:12 2020/9/8
 **/
@Slf4j
public class HandleTimerTask extends TimerTask {

    private String[] bucketKeys;

    public HandleTimerTask(String[] bucketKeys) {
        this.bucketKeys = bucketKeys;
    }

    /**
     * 处理 Bucket
     */
    private void handleBucket(String bucketKey) {
        //拉取当前bucket 数据
        Set<String> zrange = RedisUtils.zrange(bucketKey, 0, -1);
        if (zrange != null && zrange.size() > 0) {
            for (String key : zrange) {
                //拉取 job 元信息
                String jobStr = RedisUtils.get(key);
                //判断是否已经执行完毕被删除
                if (StringUtil.isNull(jobStr)) {
                    //移除当前异常元素
                    RedisUtils.zrem(bucketKey, key);
                    //删除当前job元数据
                    RedisUtils.del(key);
                    //执行下一个元素
                    continue;
                }
                //转化 str 变成 对象
                Job job = null;
                try {
                    job = JSON.parseObject(jobStr, Job.class);
                } catch (Exception e) {
                    //移除当前异常元素
                    RedisUtils.zrem(bucketKey, key);
                    //删除当前job元数据
                    RedisUtils.del(key);
                    //执行下一个元素
                    continue;
                }
                if (job == null) {
                    continue;
                }

                long delay = job.getDelay();
                //获取当前时间
                long now = System.currentTimeMillis();
                //获取job 状态
                String status = job.getStatus();
                //如果是 deleted 状态
                if (status == null || JobStatusEnum.deleted.name().equals(status)) {
                    //移除当前JobId
                    RedisUtils.zrem(bucketKey, key);
                    //直接进行下一个
                    continue;
                }

                if (now >= delay) {
                    log.info(jobStr + "\t" + bucketKey);
                    //如果 不是 reday 状态
                    if (!JobStatusEnum.ready.name().equals(status)) {
                        //说明已经超过ttr时间
                        if (JobStatusEnum.delay.name().equals(status)) {
                            //1.通知 开发人员
                            log.info("消息主体:(" + jobStr + ")" + "在" + new Date().toString() + "超时");
                            //2.改成重试模式
                            job.setStatus(JobStatusEnum.delay.name());
                        }
                        //3.获取当前重试次数
                        long n = RedisUtils.incr(job.getId() + CommonKeyEnum.incr.name());
                        log.info("消息主体:(" + jobStr + ")" + "第" + n + "次重试");
                        //计算重试时间  公式(2n+1分钟)
                        long delayTime = 2 * n + 1;
                        long oldDelay = job.getDelay();
                        // (ps: * 1000 是为了 从秒转化 到毫秒 * 60 转化到分钟)
                        long newDelay = oldDelay + (delayTime * 1000 * 60);
                        job.setDelay(newDelay);
                        //存入 ready queue
                        RedisUtils.sadd(CommonKeyEnum.readyQueue.name(), job.getId());
                        //移除当前JobId
                        RedisUtils.zrem(bucketKey, key);
                        RedisUtils.setKeyValue(job.getId(), JSON.toJSONString(job));
                    } else {
                        //如果是ready 状态
                        //存入 ready queue
                        RedisUtils.sadd(CommonKeyEnum.readyQueue.name(), job.getId());
                        //移除当前JobId
                        RedisUtils.zrem(bucketKey, key);
                        //设置当前状态
                        job.setStatus(JobStatusEnum.reserved.name());
                        //重新计算时间  加上ttr的时间
                        long oldDelay = job.getDelay();
                        // (ps: * 1000 是为了 从秒转化 到毫秒 * 60 转化到分钟)
                        long newDelay = oldDelay + (job.getTtr() * 1000);
                        job.setDelay(newDelay);
                        RedisUtils.setKeyValue(job.getId(), JSON.toJSONString(job));
                        //移除当前JobId
                        RedisUtils.zrem(bucketKey, key);
                        //存入新的 bucket
                        String nextBucket = DelayBucketUtils.getNextBucket();
                        RedisUtils.zaddOne(nextBucket, key);
                    }
                } else {
                    //移除当前JobId
                    RedisUtils.zrem(bucketKey, key);
                    //存入新的 bucket
                    String nextBucket = DelayBucketUtils.getNextBucket();
                    RedisUtils.zaddOne(nextBucket, key);
                }
            }
        }

    }

    @Override
    public void run() {
        String requestId = UUID.randomUUID().toString();
        //扫描 bucket
        for (int i = 0; i < bucketKeys.length; i++) {
            String bucketKey = bucketKeys[i];
            String lockKey = "lock" + bucketKey;
            try {
                //锁定当前 bucket 的 锁
                if (RedisUtils.tryGetDistributedLock(lockKey, requestId, 10000)) {
                    //处理Bucket
                    handleBucket(bucketKey);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("解析:" + bucketKey + "时出错 (" + e.getMessage() + ")");
            } finally {
                //解锁
                RedisUtils.releaseDistributedLock(lockKey, requestId);
            }
        }

    }
}
