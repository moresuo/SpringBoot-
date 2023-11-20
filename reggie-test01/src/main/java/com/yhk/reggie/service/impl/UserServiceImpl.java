package com.yhk.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhk.reggie.bean.User;
import com.yhk.reggie.mapper.UserMapper;
import com.yhk.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/14 16:57
 * @注释
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
