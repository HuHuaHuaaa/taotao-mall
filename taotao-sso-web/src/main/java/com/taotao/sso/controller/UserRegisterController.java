package com.taotao.sso.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserRegisterController {

    @Autowired
    private UserRegisterService userRegisterService;

    @RequestMapping(value = "/user/check/{param}/{type}",method = RequestMethod.GET)
    @ResponseBody
    public TaotaoResult checkDate(@PathVariable String param, @PathVariable Integer type){
        return userRegisterService.checkDate(param,type);
    }

    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult register(TbUser tbUser){
        TaotaoResult register = userRegisterService.register(tbUser);
        return register;
    }
}
