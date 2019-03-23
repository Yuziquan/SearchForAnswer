package com.wuchangi.searchforanswer.utils;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by WuchangI on 2019/3/9.
 */

public class RetrofitUtils {
    private static RetrofitUtils sRetrofitUtils;

    private RetrofitUtils() {
    }

    public static RetrofitUtils getInstance() {
        return sRetrofitUtils == null ? sRetrofitUtils = new RetrofitUtils() : sRetrofitUtils;
    }

    /**
     * 获取一个带有日志拦截器的OkHttpClient实例，用于打印请求参数
     * @return
     */
    public static OkHttpClient getOkHttpClientWithLoggingInterceptor(){
        // 新建日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> Log.i("请求参数:", message));

        // 设置日志显示级别
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        return okHttpClient;
    }


}

