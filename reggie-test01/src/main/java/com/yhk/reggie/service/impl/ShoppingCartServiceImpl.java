package com.yhk.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhk.reggie.bean.ShoppingCart;
import com.yhk.reggie.mapper.ShoppingCartMapper;
import com.yhk.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/15 21:20
 * @注释
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
