package com.yhk.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.yhk.reggie.common.BaseContext;
import com.yhk.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/5/31 23:34
 * @注释 检查用户是否已经完成登录
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")//表示这个类处理拦截url是所有资源路径
@Slf4j
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;

        //获取本次请求的URI
        String requestURI=request.getRequestURI();
        log.info("拦截到请求：{}",requestURI);
        //不需要处理的请求路径
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                //用户登录，注册不用拦截
                "/user/login",
                "/user/sendMsg"

        };

        //判断本次请求是否需要处理
        if(check(urls,requestURI)){
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);//放行
            return;
        }

        //判断员工登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("emp")!=null){
            log.info("用户已登录，用户id为："+request.getSession().getAttribute("emp"));
            Long empId = (Long) request.getSession().getAttribute("emp");
            BaseContext.setCurrentId(empId);//如果已经登录，就将登录的id信息保存在ThreadLocal中，因为当前程序都是在一个线程中
            filterChain.doFilter(request, response);
            return;
        }
        //判断员工登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user")!=null){
            log.info("用户已登录，用户id为："+request.getSession().getAttribute("user"));
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);//如果已经登录，就将登录的id信息保存在ThreadLocal中，因为当前程序都是在一个线程中
            filterChain.doFilter(request, response);
            return;
        }
        log.info("用户未登录");
        //如果未登录就返回未登录结果，通过输出流的方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;


    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
