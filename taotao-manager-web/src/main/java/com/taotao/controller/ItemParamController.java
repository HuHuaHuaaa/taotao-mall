package com.taotao.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParam;
import com.taotao.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/item/param")
public class ItemParamController {
    @Autowired
    private ItemParamService itemParamService;

    ///item/param/query/itemcatid/
    @RequestMapping("/query/itemcatid/{itemCatid}")
    @ResponseBody
    public TaotaoResult getItemParamById(@PathVariable Long itemCatid){
        TaotaoResult itemParamByCid = itemParamService.getItemParamByCid(itemCatid);
        return itemParamByCid;
    }


    @RequestMapping("/save/{cid}")
    @ResponseBody
    public TaotaoResult insertItemParem(@PathVariable Long cid,String paramData){
        TbItemParam itemParam=new TbItemParam();
        itemParam.setItemCatId(cid);
        itemParam.setParamData(paramData);
        TaotaoResult result=itemParamService.insertItemParam(itemParam);
        System.out.println(paramData);
        return result;

    }
}
