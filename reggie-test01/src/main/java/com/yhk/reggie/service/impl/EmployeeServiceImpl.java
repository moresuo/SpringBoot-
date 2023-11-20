package com.yhk.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhk.reggie.bean.Employee;
import com.yhk.reggie.mapper.EmployeeMapper;
import com.yhk.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/5/30 16:39
 * @注释
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
