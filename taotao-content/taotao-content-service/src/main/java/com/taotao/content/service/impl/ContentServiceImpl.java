package com.taotao.content.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.jedis.JedisClient;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private JedisClient client;

    @Autowired
    private TbContentMapper tbContentMapper;

    @Value("${CONTENT_KEY}")
    private String CONTENT_KEY;

    @Override
    public TaotaoResult saveContent(TbContent content) {

        content.setCreated(new Date());
        content.setUpdated(new Date());
        tbContentMapper.insertSelective(content);

        //当添加内容时，需要清空所属分类下的所有缓存
        try {
            client.hdel(CONTENT_KEY,content.getCategoryId()+"");
            System.out.println("插入时清空缓存！！！！！");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return TaotaoResult.ok();
    }

    @Override
    public List<TbContent> getContentListByCatId(Long categoryId) {

        //先从缓存中读取
        try {
            String hget = client.hget(CONTENT_KEY, categoryId + "");
            if (StringUtils.isNotBlank(hget)){
                System.out.println("这里有缓存！！！");
                return JsonUtils.jsonToList(hget,TbContent.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //1.注入mapper
        //2.创建example
        TbContentExample tbContentExample=new TbContentExample();
        //3.设置查询的条件
        tbContentExample.createCriteria().andCategoryIdEqualTo(categoryId);
        //4.执行查询
        List<TbContent> tbContentList = tbContentMapper.selectByExample(tbContentExample);


        //把内容写如缓存
        try {
            System.out.println("没有缓存!!!");
            client.hset(CONTENT_KEY,categoryId+"", JsonUtils.objectToJson(tbContentList));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //返回
        return tbContentList;
    }
}
