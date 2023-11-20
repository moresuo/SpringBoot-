package com.yhk.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yhk.reggie.bean.Orders;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/16 0:16
 * @注释
 */
public interface OrderService extends IService<Orders> {
    public void submit(Orders orders);
}
