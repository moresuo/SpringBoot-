package com.yhk.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yhk.reggie.bean.Employee;
import com.yhk.reggie.common.R;
import com.yhk.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/5/30 16:41
 * @注释
 */
@Slf4j
@RestController//默认将类中方法都标记为返回默认响应体，而不是返回视图，方法的返回值会作为http响应内容直接返回给客户端，而不是通过视图解析器渲染
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //@RequestBody接收前端发来的json对象
        //request用于将员工保存到session

        //将页面提交的password进行md5加密
        String password=employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        //根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp=employeeService.getOne(queryWrapper);
        //如果没有查询到则返回登录失败结果
        if(emp==null){
            return R.error("登录失败");
        }
        //密码比对，如果不一致就返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }
        //查看员工状态，如果为禁用状态，则返回禁用结果
        if(emp.getStatus()==0){
            return R.error("账号已禁用");
        }
        //登录成功，将员工id存入session并返回登录结果
        request.getSession().setAttribute("emp", emp.getId());
        return R.success(emp);

    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理session中保存的当前登录员工id
        request.getSession().removeAttribute("emp");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());
        //设置初始密码，进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("12345".getBytes()));
        //设置创建时间
        //employee.setCreateTime(LocalDateTime.now());
        //更新时间
        //employee.setUpdateTime(LocalDateTime.now());
        //获取当前用户id
        Long empId= (Long) request.getSession().getAttribute("emp");
        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("添加成功");
    }

    /**
     * 员工信息分页信息查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page={},pageSize={},name={}", page, pageSize, name);
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like((name != null&&"".equals(name)), Employee::getUsername, name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee,HttpServletRequest request){
        log.info(employee.toString());
        //修改之前更新修改时间
        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser((Long) request.getSession().getAttribute("emp"));
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }


    /**
     * 根据Id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息");
        Employee employee = employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }
}
