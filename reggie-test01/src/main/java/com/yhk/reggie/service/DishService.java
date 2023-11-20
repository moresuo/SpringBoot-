package com.yhk.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yhk.reggie.bean.Dish;
import com.yhk.reggie.dto.DishDto;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/5 14:58
 * @注释
 */
public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);
    public DishDto getByIdWithFlavor(Long id);
    public void updateWithFlavor(DishDto dishDto);
}
