package com.taotao.order.interceptor;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.service.UserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Value("TT_TOKEN_KEY")
    private String TT_TOKEN_KEY;

    @Autowired
    private UserLoginService userLoginService;

    //进入目标方法之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.从cookie中获取用户token
        String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        if (StringUtils.isEmpty(token)){
            response.sendRedirect("http://localhost:8089/page/login?redirect="+request.getRequestURL().toString());
            return false;
        }
        TaotaoResult userByToken = userLoginService.getUserByToken(token);
        if (userByToken.getStatus()!=200){
            //用户过期
            response.sendRedirect("http://localhost:8089/page/login?redirect="+request.getRequestURL().toString());
            return false;
        }
        request.setAttribute("USER_INFO",userByToken.getData());
        return true;
    }

    //进入目标方法之后，返回modelandview之前执行
    //共用变量的一些设置
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    //返回modelandview之后，渲染页面之前执行
    //异常处理，清理工作
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
