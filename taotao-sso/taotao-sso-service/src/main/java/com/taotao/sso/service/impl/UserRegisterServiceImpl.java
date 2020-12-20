package com.taotao.sso.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserRegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

@Service
public class UserRegisterServiceImpl implements UserRegisterService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public TaotaoResult checkDate(String param, Integer type) {
        //1.注入mapper
        //2.根据参数动态的生成查询条件
        TbUserExample tbUserExample =new TbUserExample();
        Criteria criteria = tbUserExample.createCriteria();
        if (type==1){//username
            if (StringUtils.isEmpty(param)){
                return TaotaoResult.ok(false);
            }
            criteria.andUsernameEqualTo(param);
        }else if (type==2){//phone
            criteria.andPhoneEqualTo(param);
        }else if (type==3){//email
            criteria.andEmailEqualTo(param);
        }else {
            return TaotaoResult.build(400,"参数不合法");
        }
        //3.调用mapper的查询方法获取数据
        List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
        //4.如果查到了数据 不可用 false
        if (list != null || list.size()>0){
            return TaotaoResult.ok(false);
        }
        //5.没有查到数据 可用 true
        return TaotaoResult.ok(true);
    }

    @Override
    public TaotaoResult register(TbUser tbUser) {
        //校验用户名和密码不能为空
        if (StringUtils.isEmpty(tbUser.getUsername())){
            return TaotaoResult.build(400,"注册失败，请校验数据");
        }
        if (StringUtils.isEmpty(tbUser.getPassword())){
            return TaotaoResult.build(400,"注册失败，请校验数据");
        }
        //不为空后校验用户名是否可用
        TaotaoResult result = checkDate(tbUser.getUsername(), 1);
        if (!(boolean)result.getData()){
            //数据不可用
            return TaotaoResult.build(400,"注册失败，请校验数据");
        }
        //校验电话号码是否可用
        TaotaoResult result1 = checkDate(tbUser.getPhone(), 2);
        if (!(boolean)result1.getData()){
            //数据不可用
            return TaotaoResult.build(400,"注册失败，请校验数据");
        }
        //校验Email是否可用
        TaotaoResult result2 = checkDate(tbUser.getEmail(), 3);
        if (!(boolean)result2.getData()){
            //数据不可用
            return TaotaoResult.build(400,"注册失败，请校验数据");
        }
        //如果校验成功，补全其他属性
        tbUser.setCreated(new Date());
        tbUser.setUpdated(tbUser.getCreated());
        //对密码进行MD5加密
        String md5Password = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
        tbUser.setPassword(md5Password);
        //插入数据
        tbUserMapper.insertSelective(tbUser);
        return TaotaoResult.ok();
    }
}
