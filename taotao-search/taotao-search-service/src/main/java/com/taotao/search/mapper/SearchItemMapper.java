package com.taotao.search.mapper;

import com.taotao.common.pojo.SearchItem;

import java.util.List;

public interface SearchItemMapper {
    List<SearchItem> getSearchItemList();
    SearchItem getItemById(Long itemId);
}
