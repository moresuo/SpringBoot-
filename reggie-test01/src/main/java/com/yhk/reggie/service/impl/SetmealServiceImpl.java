package com.yhk.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhk.reggie.bean.Category;
import com.yhk.reggie.bean.Setmeal;
import com.yhk.reggie.bean.SetmealDish;
import com.yhk.reggie.common.CustomException;
import com.yhk.reggie.dto.SetmealDto;
import com.yhk.reggie.mapper.SetmealDishMapper;
import com.yhk.reggie.mapper.SetmealMapper;
import com.yhk.reggie.service.CategoryService;
import com.yhk.reggie.service.SetmealDishService;
import com.yhk.reggie.service.SetmealService;
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
 * @Date 2023/6/5 15:00
 * @注释
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 保存setmeal表中的套餐信息以及套餐分类名称，修改套餐菜品关联表
     */
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐表中的基本信息
        this.save(setmealDto);
        //设置关联表
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes=setmealDishes.stream().map((item)->{
            //为每个菜品设置相应的套餐id
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);//将关联信息保存到表中
    }

    /**
     * 根据id集合删除套餐，关联信息
     * @param ids
     */
    @Transactional
    public void deleteWithDish(List<Long> ids) {
        //如果所选的套餐为起售状态就不能删除，抛出业务异常
        //构造条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);//状态为起售状态
        int count = this.count(queryWrapper);
        if(count>0){
            //如果count大于0，说明存在起售状态的套餐
            //抛出业务异常
            throw new CustomException("套餐正在售卖中，请先停售在删除");
        }
        //没有起售状态的套餐就进行删除操作
        this.removeByIds(ids);
        //还要对setmeal_dish表进行删除
        //根据setmealId进行删除
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);

    }

    /**
     * 回显数据
     * @param id
     * @return
     */
    @Override
    public SetmealDto getByIdWithDish(Long id) {
        //返回对象
        SetmealDto setmealDto=new SetmealDto();
        //根据id获取setmeal
        Setmeal setmeal = setmealService.getById(id);
        //将setmeal拷贝给setmealDto
        BeanUtils.copyProperties(setmeal, setmealDto);//还差菜品集合和套餐分类
        //构造条件对象
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        //设置条件
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        //执行查询
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);
        //根据categoryId获取套餐分类名
        Long categoryId = setmeal.getCategoryId();
        Category category = categoryService.getById(categoryId);
        setmealDto.setCategoryName(category.getName());
        return setmealDto;

    }

    /**
     * 修改数据
     * 修改对应的setmeal表，setmeal_dish表
     * @param setmealDto
     */
    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        this.updateById(setmealDto);//修改对应的setmeal表
        //修改setmeal_dish表

        //清空对应setmealDishes
        //构造条件构造器
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        //获取相应菜品
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //加入修改之后的数据
        setmealDishes=setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //批量添加
        setmealDishService.saveBatch(setmealDishes);
    }
}
