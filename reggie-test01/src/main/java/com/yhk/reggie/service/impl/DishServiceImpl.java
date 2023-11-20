package com.yhk.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhk.reggie.bean.Dish;
import com.yhk.reggie.bean.DishFlavor;
import com.yhk.reggie.dto.DishDto;
import com.yhk.reggie.mapper.DishMapper;
import com.yhk.reggie.service.DishFlavorService;
import com.yhk.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/5 14:59
 * @注释
 */
@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 将dishDto中的数据分别关联到dish表和dish_flavor表
     * @param dishDto
     */
    @Transactional//声明式事务注解，当方法执行成功则保存数据，如果抛出异常，事务将被回滚
    public void saveWithFlavor(DishDto dishDto) {
        //将菜品数据保存到dish中
        this.save(dishDto);
        //将口味数据保存到dish_flavor中，注意：保存的同时，还要关联对应菜品的id,这个id从dishDto中取出
        //获取dish_id
        Long dishId=dishDto.getId();
        List<DishFlavor> flavors=dishDto.getFlavors();
        //通过stream流遍历每个flavor,为每个flavor添加dish_id字段
        flavors=flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        //将菜品口味数据保存到dish_flavor表
        dishFlavorService.saveBatch(flavors);//saveBatch接收一个对象的集合作为参数，批量将数据存进数据库中
    }

    /**
     * 根据id获取对应口味信息
     * @param id
     * @return
     */
    public DishDto getByIdWithFlavor(Long id) {
        //返回对象
        DishDto dishDto=new DishDto();
        //执行条件
        Dish dish = this.getById(id);
        //将dish对象拷贝给dishDto
        BeanUtils.copyProperties(dish, dishDto);
        //根据id查询对应口味
        //构造条件构造器
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
       //获取查询结果
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);
        return dishDto;

    }

    /**
     * 修改菜品及口味
     * @param dishDto
     */
    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //修改dish表中的内容
        this.updateById(dishDto);
        //清理当前菜品对应dish_flavor表中的口味
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        //设置条件
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        //执行条件
        dishFlavorService.remove(queryWrapper);
        //获取口味字段
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors=flavors.stream().map((item)->{
            //给不同菜品id设置对应的口味
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        //批量添加
        dishFlavorService.saveBatch(flavors);
    }
}
