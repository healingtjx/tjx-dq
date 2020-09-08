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
     * 已使用内存
     */
    @Column(name="ram_used")
    private String ramUsed;

    /**
     * 消息总数
     */
    @Column(name="amount_all")
    private String amountAll;

    /**
     * 已经消费消息数量
     */
    @Column(name="amount_existing")
    private String amountExisting;

    /**
     * 已经被删除消息数量
     */
    @Column(name="amount_del")
    private String amountDel;


}
