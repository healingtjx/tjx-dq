package com.delayed.base.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: tjx
 * @描述:
 * @创建时间: 创建于16:54 2020/7/16
 **/
@Data
@Entity
@Table(name = "dq_topic_config")
public class DqTopicConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 创建人
     */
    private String creator;

    private String topic;


    /**
     * 延迟执行时间
     */
    @Column(name = "delay_time")
    private int delayTime;

    /**
     * 回调地址
     */
    @Column(name = "call_back")
    private String callBack;

    /**
     * 请求方法类型/get/post
     */
    @Column(name = "request_mode")
    private String requestMode;

    /**
     * 重试标记
     */
    @Column(name = "retry_mark")
    private String retryMark;

    /**
     * 超时时间
     */
    @Column(name = "over_time")
    private int overTime;

    /**
     * 超时通知
     */
    @Column(name = "exception_back")
    private String exceptionBack;


}
