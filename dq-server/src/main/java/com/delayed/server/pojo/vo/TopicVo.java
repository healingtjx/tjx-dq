package com.delayed.server.pojo.vo;

import lombok.Data;

/**
 * @作者: tjx
 * @描述:
 * @创建时间: 创建于11:26 2020/9/7
 **/
@Data
public class TopicVo {

    /**
     * 指令 add 添加， del 删除
     */
    private String cmd;

    /**
     * topic
     */
    private String topic;

    /**
     * 必须唯一
     */
    private String id;

    /**
     * 回调时候body
     */
    private String body;

}
