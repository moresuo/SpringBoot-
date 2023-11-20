package com.yhk.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhk.reggie.bean.OrderDetail;
import com.yhk.reggie.mapper.OrderDetailMapper;
import com.yhk.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/16 0:20
 * @注释
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
