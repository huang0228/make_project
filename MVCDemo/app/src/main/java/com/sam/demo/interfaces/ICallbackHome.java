/*
 * @author 牛世杰
 */

package com.sam.demo.interfaces;

/**
 * Created by sam.huang.
 * 为了计算首页接口请求次数专用
 */

public interface ICallbackHome<T> {
    void exec(T t);
    void error(T e);
}
