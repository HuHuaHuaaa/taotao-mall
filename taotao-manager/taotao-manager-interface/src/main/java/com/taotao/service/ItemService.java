package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

public interface ItemService {
/**
 *  根据当前的页码和每页 的行数进行分页查询
 */
    public EasyUIDataGridResult getItemList(Integer page,Integer rows);

    public TaotaoResult saveItem(TbItem item,String desc,String itemParam) throws Exception;

    public TbItem getItemById(Long itemId);

    public TbItemDesc getItemDescById(Long itemId);


}
