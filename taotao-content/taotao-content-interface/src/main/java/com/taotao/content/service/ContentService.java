package com.taotao.content.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

public interface ContentService {
    public TaotaoResult saveContent(TbContent content);
    public List<TbContent> getContentListByCatId(Long categoryId);
}
