package com.yfkk.cardbag.mvp.base;

/**
 * Created by litao on 2018/4/8.
 */
public interface BaseView {

    default void onError(String tag, Throwable e){} // 请求出现异常（和onCompleted不会同时调用）
    default void onCompleted(){} // 正常请求完成
}
