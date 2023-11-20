package com.yhk.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/1 18:44
 * @注释 全局异常处理器
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})//该类下的所有方法都可以被所有控制器共享，在发生异常时拦截下来并处理异常
@ResponseBody//类中的所有方法返回json格式，并作为http响应内容返回给客户端
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理添加员工时，username重复的异常
     * 全局异常处理器
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)//处理SQLIntegrityConstraintViolationException这个特定异常
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");//split()根据空格分隔成数组
            String msg=split[2]+"已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    /**
     * 全局业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)//用于处理自定义异常
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());//返回错误信息
    }
}
