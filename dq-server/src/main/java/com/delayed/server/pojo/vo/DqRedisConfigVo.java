package com.delayed.server.pojo.vo;

import lombok.Data;

import javax.persistence.Column;

/**
 * redis 修改 VO
 */
@Data
public class DqRedisConfigVo {

    private Long id;

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
