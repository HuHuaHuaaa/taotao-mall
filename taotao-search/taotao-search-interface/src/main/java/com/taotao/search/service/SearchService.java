package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;

import java.io.IOException;

public interface SearchService {
    public TaotaoResult importAllSearchItems() throws Exception;
    public SearchResult search(String queryString,Integer page,Integer rows) throws Exception;
    public TaotaoResult updateItemById(Long itemId) throws Exception;
}
