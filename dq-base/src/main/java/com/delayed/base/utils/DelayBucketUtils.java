package com.delayed.base.utils;

import com.delayed.base.enumeration.CommonKeyEnum;

/**
 * @作者: tjx
 * @描述: 用于 DelayBucket 相关的操作
 * @创建时间: 创建于14:37 2020/9/7
 **/
public class DelayBucketUtils {

    private static volatile int[] indexs = new int[2];

    private static volatile int index = 0;


    /**
     * 实例化
     * @param bucket bucket 个数
     */
    public static void initialize(int bucket){
        indexs = new int[bucket];
        for (int i = 0; i <bucket ; i++) {
            indexs[i] = i;
        }
    }

    public static String getNextBucket(){
        try {
            return CommonKeyEnum.delayBucket.name() + indexs[index];
        }finally {
            index++;
            if(index == indexs.length)
                index = 0;
        }
    }

    public static String[] getAllBucket(){
        String[] keys = new String[indexs.length];
        for (int i = 0; i < indexs.length; i++) {
            keys[i] = CommonKeyEnum.delayBucket.name() + indexs[i];
        }
        return keys;
    }

    public static void main(String[] args) {
        DelayBucketUtils.initialize(4);
        String[] allBucket = getAllBucket();
        for (int i = 0; i < allBucket.length; i++) {
            System.out.println(allBucket[i]);
        }
    }

}
