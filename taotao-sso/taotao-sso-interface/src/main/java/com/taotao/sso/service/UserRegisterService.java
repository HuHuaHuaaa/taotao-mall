package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;


public interface UserRegisterService {
    public TaotaoResult checkDate(String param,Integer type);
    public TaotaoResult register(TbUser tbUser);
}
