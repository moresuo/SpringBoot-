package com.yhk.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhk.reggie.bean.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/5 14:56
 * @注释
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
