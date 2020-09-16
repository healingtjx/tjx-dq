package com.delayed.base.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @作者: tjx
 * @描述:
 * @创建时间: 创建于19:25 2020/7/15
 **/
@Data
@Entity
@Table(name = "dq_redis_config")
public class DqRedisConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 唯一key
     */
    private String key;


    /**
     * bucket 数量
     */
    private int bucket;

    /**
     * 配置信息
     */
    private String url;

    /**
     * 名称
     */
    private String name;
    /**
     * 状态
     */
    private int status;

    /**
     * redis版本
     */
    private String vserion;

    /**
     *  consume 个数
     */
    private int consume;


    /**
     * consume 扫描速度(单位毫秒) 默认 2毫秒
     */
    @Column(name="consume_speed")
    private int consumeSpeed;


    /**
     * timer 个数
     */
    private int timer;

    /**
     * timer 扫描速度(单位毫秒) 默认 2毫秒
     */
    @Column(name="timer_speed")
    private int timerSpeed;



}
