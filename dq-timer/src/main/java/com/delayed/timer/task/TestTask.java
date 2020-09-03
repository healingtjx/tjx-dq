package com.delayed.timer.task;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @作者: tjx
 * @描述:
 * @创建时间: 创建于14:09 2020/7/29
 **/
public class TestTask  implements   Runnable{

    public static void main(String[] args) {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
        service.scheduleAtFixedRate(new TestTask(), 3, 1,TimeUnit.SECONDS);


    }

    @Override
    public void run() {
        System.out.println("123123");
    }
}
