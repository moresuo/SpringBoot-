package com.yhk.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhk.reggie.bean.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/5/30 16:37
 * @注释
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
