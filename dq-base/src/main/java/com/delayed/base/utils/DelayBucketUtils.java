package com.delayed.base.utils;

/**
 * @作者: tjx
 * @描述: 用于 DelayBucket 相关的操作
 * @创建时间: 创建于14:37 2020/9/7
 **/
public class DelayBucketUtils {

    private static volatile int[] indexs = new int[2];

    private static volatile int index = 0;

    final static String BUCKET_KEY = "delayBucket";

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
            return BUCKET_KEY + indexs[index];
        }finally {
            index++;
            if(index == indexs.length)
                index = 0;
        }
    }

    public static String[] getAllBucket(){
        String[] keys = new String[indexs.length];
        for (int i = 0; i < indexs.length; i++) {
            keys[i] = BUCKET_KEY + indexs[i];
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
