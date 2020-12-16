package com.taotao.content.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;


    @Override
    public List<EasyUITreeNode> getContentCategoryList(Long parentid) {
        //1.注入mapper
        //2.创建example
        TbContentCategoryExample example=new TbContentCategoryExample();
        //3.设置条件
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentid);
        //4.执行查询
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
        //5.转成EasyUITreeNode 列表
        List<EasyUITreeNode> nodes=new ArrayList<>();
        for (TbContentCategory tbContentCategory : list) {
            EasyUITreeNode node=new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setState(tbContentCategory.getIsParent()?"closed":"open");
            node.setText(tbContentCategory.getName());
            nodes.add(node);
        }
        return nodes;
    }

    @Override
    public TaotaoResult createContentCategory(Long parentId, String name) {
        //1.构建对象，补全其他属性
        TbContentCategory tbContentCategory=new TbContentCategory();
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setIsParent(false);
        tbContentCategory.setName(name);
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setStatus(1);
        tbContentCategory.setUpdated(tbContentCategory.getCreated());
        tbContentCategoryMapper.insertSelective(tbContentCategory);

        //判断如果要添加的节点的父节点本身是叶子节点，那么就要跟新其为父节点
        TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if (parent.getIsParent()==false){
            parent.setIsParent(true);
            tbContentCategoryMapper.updateByPrimaryKeySelective(parent);
        }


        return TaotaoResult.ok(tbContentCategory);

    }
}
