package com.yhk.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yhk.reggie.bean.ShoppingCart;
import com.yhk.reggie.common.BaseContext;
import com.yhk.reggie.common.R;
import com.yhk.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/15 21:23
 * @注释
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加菜品或套餐
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //获取当前用户id，BaseContext中保存当前用户id
        Long currrentId = BaseContext.getCurrrentId();
        //设置当前用户id
        shoppingCart.setUserId(currrentId);
        //获取当前菜品id,判断当前添加的是菜品还是套餐
        Long dishId = shoppingCart.getDishId();
        //构造条件构造器
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currrentId);
        if(dishId!=null){
            //dishId不为空，说明当前添加的是菜品
            //根据userId和dishId查找购物车信息
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        }else{
            //dishId为空，说明当前添加的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        //查询当前菜品或套餐是否在数据库中
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);
        if(cart!=null){
            //如果当前菜品或套餐在数据库中，就在原先的number字段加一
            Integer number = cart.getNumber();
            cart.setNumber(number + 1);
            shoppingCartService.updateById(cart);
        }else{
            //如果不在数据库中,就存入数据库中
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cart=shoppingCart;
        }
        //返回shoppingCart的话可能没有id
        return R.success(cart);
    }

    /**
     * 获取购物车列表
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        //根据当前用户id查询对应购物车中的菜品或套餐
        Long currrentId = BaseContext.getCurrrentId();
        //条件构造器
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currrentId);
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 清空用户购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        //根据userId去批量删除
        Long currrentId = BaseContext.getCurrrentId();
        //构造条件构造器
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        //设置条件
        queryWrapper.eq(ShoppingCart::getUserId, currrentId);
        //执行条件,清空用户所有·购物车
        shoppingCartService.remove(queryWrapper);
        return R.success("成功清空购物车");

    }


}
