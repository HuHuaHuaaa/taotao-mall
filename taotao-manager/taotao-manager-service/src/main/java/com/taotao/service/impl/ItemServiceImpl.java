package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Resource(name="topicDestination")
    private Destination destination;

    @Override
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        //1.设置分页信息
        if (page==null)page=1;
        if (rows==null)rows=30;
        PageHelper.startPage(page,rows);
        //2.注入mapper
        //3.创建example
        TbItemExample itemExample=new TbItemExample();
        //4.根据mapper调用查询所有数据的方法
        List<TbItem> list = itemMapper.selectByExample(itemExample);
        //5.获取分页信息
        PageInfo<TbItem> info = new PageInfo<>(list);
        //6.封装到EasyUIDataGridResult
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal((int)info.getTotal());
        result.setRows(info.getList());
        //7.返回
        return result;
    }

    @Override
    public TaotaoResult saveItem(TbItem item, String desc,String itemParam) throws Exception {

        long itemId = IDUtils.genItemId();
        item.setId(itemId);
        item.setStatus((byte)1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        itemMapper.insertSelective(item);

        //商品描述数据
        TaotaoResult result=insertItemDesc(itemId,desc);
        if (result.getStatus()!=200){
            throw new Exception();
        }
        result=insertItemParamItem(itemId,itemParam);
        if (result.getStatus()!=200){
            throw new Exception();
        }

        //添加发送消息的业务逻辑
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(itemId+"");
            }
        });

        return TaotaoResult.ok();
    }

    @Override
    public TbItem getItemById(Long itemId) {
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        return tbItem;
    }

    @Override
    public TbItemDesc getItemDescById(Long itemId) {
        return itemDescMapper.selectByPrimaryKey(itemId);
    }


    private TaotaoResult insertItemDesc(Long itemId,String desc){
        TbItemDesc desc2 = new TbItemDesc();
        desc2.setItemDesc(desc);
        desc2.setItemId(itemId);
        desc2.setCreated(new Date());
        desc2.setUpdated(new Date());
        itemDescMapper.insertSelective(desc2);
        return TaotaoResult.ok();
    }

    private TaotaoResult insertItemParamItem(Long itemId,String itemParam){
        TbItemParamItem tbItemParamItem=new TbItemParamItem();
        tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setParamData(itemParam);
        tbItemParamItem.setCreated(new Date());
        tbItemParamItem.setUpdated(new Date());
        tbItemParamItemMapper.insert(tbItemParamItem);
        return TaotaoResult.ok();
    }
}
