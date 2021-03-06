package com.delayed.base.enumeration;

/**
 * @author: tjx
 * @描述: 消息状态
 * @创建时间: 创建于15:14 2020/9/7
 **/
public enum JobStatusEnum {
    /**
     * ready 可执行状态，等待消费。
     */
    ready,
    /**
     * delay 不可执行状态，等待时钟周期。
     */
    delay,
    /**
     * reserved 已被消费者读取，但还未得到消费者的响应（delete、finish）。
     */
    reserved,
    /**
     * deleted 已被消费完成或者已被删除。
     */
    deleted
}
