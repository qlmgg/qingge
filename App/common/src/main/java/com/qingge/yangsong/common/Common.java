package com.qingge.yangsong.common;


public class Common {
    /**
     * 一些不可变的永恒的参数
     * 通常用于一些配置
     */
    public interface Constance {
        // 手机号的正则,11位手机号
        String REGEX_MOBILE = "[1][3,4,5,7,8][0-9]{9}$";

        // 基础的网络请求地址
//        String API_URL = "http://192.168.43.212:8080/api/";
        String API_URL = "http://www.1009527.xyz:8888/api/";
    }


}
