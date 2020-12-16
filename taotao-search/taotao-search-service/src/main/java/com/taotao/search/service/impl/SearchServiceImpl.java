package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private SolrServer solrServer;

    @Autowired
    private SearchDao searchDao;

    @Override
    public TaotaoResult importAllSearchItems() throws Exception{
        //注入mapper
        //调用mapper的方法，查询所有的商品的数据
        List<SearchItem> searchItemList = searchItemMapper.getSearchItemList();
        //通过solrj将数据写入到索引库中
        for (SearchItem searchItem : searchItemList) {
            SolrInputDocument solrInputDocument=new SolrInputDocument();
            solrInputDocument.addField("id",searchItem.getId().toString());
            solrInputDocument.addField("item_title",searchItem.getTitle());
            solrInputDocument.addField("item_sell_point",searchItem.getSell_point());
            solrInputDocument.addField("item_price",searchItem.getPrice());
            solrInputDocument.addField("item_image",searchItem.getImage());
            solrInputDocument.addField("item_category_name",searchItem.getCategory_name());
            solrInputDocument.addField("item_desc",searchItem.getItem_desc());

            solrServer.add(solrInputDocument);
        }
        solrServer.commit();
        return TaotaoResult.ok();
    }

    @Override
    public SearchResult search(String queryString, Integer page, Integer rows) throws Exception {
        //1.创建solrquery对象
        SolrQuery query = new SolrQuery();
        //2.设置主查询条件
        if(StringUtils.isNotBlank(queryString)){
            query.setQuery(queryString);
        }else{
            query.setQuery("*:*");
        }
        //2.1设置过滤条件 设置分页
        if(page==null)page=1;
        if(rows==null)rows=60;
        query.setStart((page-1)*rows);//page-1 * rows
        query.setRows(rows);
        //2.2.设置默认的搜索域
        query.set("df", "item_keywords");
        //2.3设置高亮
        query.setHighlight(true);
        query.setHighlightSimplePre("<em style=\"color:red\">");
        query.setHighlightSimplePost("</em>");
        query.addHighlightField("item_title");//设置高亮显示的域

        //3.调用dao的方法 返回的是SearchResult 只包含了总记录数和商品的列表
        SearchResult search = searchDao.search(query);
        //4.设置SearchResult 的总页数
        long pageCount = 0l;
        pageCount = search.getRecordCount()/rows;
        if(search.getRecordCount()%rows>0){
            pageCount++;
        }
        search.setPageCount(pageCount);
        //5.返回
        return search;
    }

    @Override
    public TaotaoResult updateItemById(Long itemId) throws Exception {
        return searchDao.updateItemById(itemId);
    }


}
