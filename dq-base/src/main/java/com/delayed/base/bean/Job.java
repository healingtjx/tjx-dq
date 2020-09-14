package com.delayed.base.bean;

import lombok.Data;

/**
 * @作者: tjx
 * @描述: 存入reids 的 job
 * @创建时间: 创建于14:01 2020/9/7
 **/
@Data
public class Job {


    /**
     * topic
     */
    private String topic;

    /**
     * id 必须唯一
     */
    private String id;

    /**
     * 延迟时间秒数字
     */
    private long delay;

    /**
     * 最大执行时间
     */
    private int ttr;


    /**
     * 参数
     */
    private String body;

    /**
     * 状态 详见 com.delayed.base.enumeration.JobStatusEnum
     */
    private String status;
}
