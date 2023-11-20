package com.yhk.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yhk.reggie.bean.User;
import com.yhk.reggie.common.R;
import com.yhk.reggie.service.UserService;
import com.yhk.reggie.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/14 16:58
 * @注释
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 邮箱验证码登录功能
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession httpSession) throws MessagingException {
        //获取邮箱
        String phone=user.getPhone();
        if(phone!=null){
            //如果邮箱不为空，就随机生成验证码
            String code= MailUtils.achieveCode();
            //发送邮箱
            MailUtils.sendTestMail(phone, code);
            //验证码保存在session中，方便后面对比
            httpSession.setAttribute(phone,code);
            return R.success("验证码发送成功");
        }
        return R.error("验证码发送失败");
    }


    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession httpSession){//因为User表中没有code字段，所以可以用map集合来接受键值对
        //获取邮箱
        String phone=map.get("phone").toString();
        //获取验证码
        String code=map.get("code").toString();
        //从session中获取验证码
        String codeInSession = httpSession.getAttribute(phone).toString();
        //比较用户输入的验证码和session中验证码是你否一致
        if(code!=null&&code.equals(codeInSession)){
            //如果一致，就判断数据库中是否有对应的user
            LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            //如果不存在user，就创建user存进数据库
            if(user==null){
                user=new User();
                user.setPhone(phone);
                user.setName("a"+code);
                userService.save(user);
            }
            //存入userid表示登录状态
            httpSession.setAttribute("user", user.getId());
            //返回
            return R.success(user);
        }
        return R.error("登录失败");

    }
}
