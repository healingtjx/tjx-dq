package com.delayed.consume.task;

import com.delayed.base.bean.Job;
import com.delayed.base.utils.StringUtil;

/**
 * @作者: tjx
 * @描述: 处理 利用 http请求消费
 * @创建时间: 创建于15:30 2020/9/9
 **/
public class HandeCalByHttpTask implements Runnable{

    //当前消费对象
    private Job job;

    public HandeCalByHttpTask(Job job){
        this.job = job;
    }
    @Override
    public void run() {
        if(job == null)
            return;
        //判断是否有url
        String url = job.getUrl();
        if(StringUtil.isNull(url))
            return;



    }
}
