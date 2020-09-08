package com.delayed.base.bean;

import lombok.Data;


/**
 * @作者: tjx
 * @描述: 返回结果类
 * @创建时间: 创建于16:24 2020/7/16
 **/
@Data
public class ComResponseBean<T> {
    private int code;
    private String msg;
    private T data;
}
