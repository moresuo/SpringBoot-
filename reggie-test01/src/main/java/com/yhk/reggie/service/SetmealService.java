package com.yhk.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yhk.reggie.bean.Setmeal;
import com.yhk.reggie.dto.SetmealDto;

import java.util.List;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/5 14:59
 * @注释
 */
public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);
    public void deleteWithDish(List<Long> ids);
    public SetmealDto getByIdWithDish(Long id);
    public void updateWithDish(SetmealDto setmealDto);
}
