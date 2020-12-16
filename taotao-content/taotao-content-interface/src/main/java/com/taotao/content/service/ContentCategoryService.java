package com.taotao.content.service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;

import java.util.List;

public interface ContentCategoryService {
    public List<EasyUITreeNode> getContentCategoryList(Long parentid);
    public TaotaoResult createContentCategory(Long parentId,String name);
}
