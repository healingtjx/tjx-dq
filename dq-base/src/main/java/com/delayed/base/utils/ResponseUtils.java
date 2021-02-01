package com.delayed.base.utils;

import com.delayed.base.bean.ComResponseBean;

/**
 * @author: tjx
 * @描述:
 * @创建时间: 创建于16:37 2020/7/16
 **/
public class ResponseUtils {

    public static ComResponseBean succeed(Object o) {
        ComResponseBean bean = new ComResponseBean();
        bean.setCode(200);
        bean.setMsg("success");
        bean.setData(o);
        return bean;
    }

    public static ComResponseBean error(int code, String msg) {
        ComResponseBean bean = new ComResponseBean();
        bean.setCode(code);
        bean.setMsg(msg);
        return bean;
    }
}
