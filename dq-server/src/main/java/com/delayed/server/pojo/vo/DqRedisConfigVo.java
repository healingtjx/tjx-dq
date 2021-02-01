package com.delayed.server.pojo.vo;

import lombok.Data;

/**
 * @author: tjx
 * @描述: redis 修改 VO
 * @创建时间: 创建于15:05 2020/9/3
 **/
@Data
public class DqRedisConfigVo {

    private Long id;

    /**
     * consume 个数
     */
    private int consume;


    /**
     * consume 扫描速度(单位毫秒) 默认 2毫秒
     */
    private int consumeSpeed;


    /**
     * timer 个数
     */
    private int timer;

    /**
     * timer 扫描速度(单位毫秒) 默认 2毫秒
     */
    private int timerSpeed;


}
