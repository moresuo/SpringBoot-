package com.yhk.reggie.common;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/5 15:25
 * @注释 自定义业务异常
 */
public class CustomException extends RuntimeException {
    public CustomException(String message){
        super(message);
    }
}
