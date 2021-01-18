package com.taotao.sso.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.sso.service.UserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class UserLoginController {

    @Autowired
    private UserLoginService userLoginService;

    @Value("${TT_TOKEN_KEY}")
    private String TT_TOKEN_KEY;

    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,String username, String password){
        TaotaoResult result = userLoginService.login(username, password);
        if (result.getStatus()==200){
            CookieUtils.setCookie(httpServletRequest,httpServletResponse,TT_TOKEN_KEY,result.getData().toString());
        }
        return result;
    }

    @RequestMapping(value="/user/token/{token}",method=RequestMethod.GET,produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getUserByToken(@PathVariable String token,String callback){
        //判断是否是Jsonp请求
        if(StringUtils.isNotBlank(callback)){
            //如果是jsonp 需要拼接 类似于fun({id:1});
            TaotaoResult result = userLoginService.getUserByToken(token);
            String jsonpstr = callback+"("+ JsonUtils.objectToJson(result)+")";
            return jsonpstr;
        }
        //如果不是jsonp
        //1.调用服务
        TaotaoResult result = userLoginService.getUserByToken(token);
        return JsonUtils.objectToJson(result);
    }

    @RequestMapping(value = "/user/logout/{token}",method = RequestMethod.GET)
    @ResponseBody
    public String logout(@PathVariable String token,HttpServletResponse response, HttpServletRequest request) throws IOException {
        TaotaoResult logout = userLoginService.logout(token);
        if (logout.getStatus()==200){
            // 代表退出成功
            CookieUtils.deleteCookie(request, response, TT_TOKEN_KEY);
            response.sendRedirect("http://localhost:8084");
            return null;
        }
        return null;
    }
}
