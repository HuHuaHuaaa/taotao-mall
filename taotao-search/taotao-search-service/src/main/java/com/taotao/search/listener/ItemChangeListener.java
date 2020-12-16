package com.taotao.search.listener;

import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ItemChangeListener implements MessageListener {

    @Autowired
    private SearchService searchService;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = null;
            Long itemId = null;
            //取商品id
            if (message instanceof TextMessage) {
                textMessage = (TextMessage) message;
                itemId = Long.parseLong(textMessage.getText());
            }
            //向索引库添加文档
            searchService.updateItemById(itemId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
