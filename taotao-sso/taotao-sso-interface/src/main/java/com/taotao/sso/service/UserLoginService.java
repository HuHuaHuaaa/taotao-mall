package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;

public interface UserLoginService {
    public TaotaoResult login(String username,String password);

    public TaotaoResult getUserByToken(String token);

    public TaotaoResult logout(String token);
}
