package com.delayed.base.utils;

import com.delayed.base.model.ComResponseBean;

/**
 * @作者: tjx
 * @描述:
 * @创建时间: 创建于16:37 2020/7/16
 **/
public class ResponseUtils {

    public static ComResponseBean succeed(Object o){
        ComResponseBean bean = new ComResponseBean();
        bean.setCode(200);
        bean.setMsg("success");
        bean.setData(o);
        return bean;
    }
}
