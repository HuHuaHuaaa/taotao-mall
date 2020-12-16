package com.taotao.portal.controller;

import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.Ad1Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PageController {

    @Autowired
    private ContentService contentService;

    @Value("${AD1_CATEGORY_ID}")
    private Long categoryId;

    @Value("${AD1_HEIGHT_B}")
    private String heightB;

    @Value("${AD1_HEIGHT}")
    private String height;

    @Value("${AD1_WIDTH}")
    private String width;

    @Value("${AD1_WIDTH_B}")
    private String widthB;

    @RequestMapping("/index")
    public String showIndex(Model model){
        List<TbContent> contentListByCatId = contentService.getContentListByCatId(categoryId);
        List<Ad1Node> nodes=new ArrayList<>();
        for (TbContent tbContent : contentListByCatId) {
            Ad1Node node=new Ad1Node();
            node.setAlt(tbContent.getSubTitle());
            node.setHeight(height);
            node.setHeightB(heightB);
            node.setHref(tbContent.getUrl());
            node.setSrc(tbContent.getPic());
            node.setSrcB(tbContent.getPic2());
            node.setWidth(width);
            node.setWidthB(widthB);
            nodes.add(node);
        }
        model.addAttribute("ad1", JsonUtils.objectToJson(nodes));
        return "index";
    }



    
}
