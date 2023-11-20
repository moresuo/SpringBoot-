package com.yhk.reggie.common;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/5 1:26
 * @注释 基于ThreadLocal封装工具类，用于保存和获取当前登录的id
 * ThreadLocal为每个线程提供单独的一个存储空间，具有线程隔离效果，用于存放当前线程的一个变量，在此线程中任何时刻都可以访问
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrrentId(){
        return threadLocal.get();
    }

}
