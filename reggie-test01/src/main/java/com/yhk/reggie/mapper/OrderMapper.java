package com.yhk.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhk.reggie.bean.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/16 0:15
 * @注释
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
