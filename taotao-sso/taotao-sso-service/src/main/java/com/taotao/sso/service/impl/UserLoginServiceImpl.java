package com.taotao.sso.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.jedis.JedisClient;
import com.taotao.sso.service.UserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${USER_INFO}")
    private String USER_INFO;

    @Value("${EXPIRE_TIME}")
    private Integer EXPIRE_TIME;


    @Override
    public TaotaoResult login(String username, String password) {
        //1.注入mapper
        //2.校验用户名密码是否为空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return TaotaoResult.build(400,"用户名或者密码错误");
        }
        //3.先校验用户名，再校验密码
        TbUserExample tbUserExample=new TbUserExample();
        Criteria criteria = tbUserExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
        if (list==null || list.size()==0){
            return TaotaoResult.build(400,"用户名或者密码错误");
        }
        TbUser tbUser = list.get(0);
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(tbUser.getPassword())){
            return TaotaoResult.build(400,"用户名或者密码错误");
        }
        //4.如果校验成功
        //5.生成token
        String token= UUID.randomUUID().toString();
        //存放数据到redis中
        //设置密码为空
        tbUser.setPassword(null);
        jedisClient.set(USER_INFO+":"+token, JsonUtils.objectToJson(tbUser));
        //设置过期时间
        jedisClient.expire(USER_INFO+":"+token,EXPIRE_TIME);
        //6.把token设置在cookie中  表现层中设置
        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserByToken(String token) {
        //根据token查询用户信息
        String strjson = jedisClient.get(USER_INFO + ":" + token);
        //判断是否查询到
        if (StringUtils.isNotBlank(strjson)){
            //如果查询到
            TbUser tbUser = JsonUtils.jsonToPojo(strjson, TbUser.class);
            jedisClient.expire(USER_INFO+":"+token,EXPIRE_TIME);
            return TaotaoResult.ok(tbUser);
        }
        return TaotaoResult.build(400,"用户已过期");
    }

    @Override
    public TaotaoResult logout(String token) {
        String strjson = jedisClient.get(USER_INFO + ":" + token);
        if (StringUtils.isNotBlank(strjson)){
            jedisClient.del(USER_INFO + ":" + token);
            return TaotaoResult.ok();
        }
        return TaotaoResult.build(400,"用户未登录");
    }
}
