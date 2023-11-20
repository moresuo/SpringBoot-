package com.yhk.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yhk.reggie.bean.Category;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/5 13:18
 * @注释
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
