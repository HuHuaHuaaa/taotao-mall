package pagehelper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class TestPageHelper {
    @Test
    public void testhelper(){
        //2.初始化spring容器
        ApplicationContext context=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
        //3.获取mapper的代理对象
        TbItemMapper itemMapper = context.getBean(TbItemMapper.class);
        //1.设置分页信息
        PageHelper.startPage(1,3);//3行 紧跟着的第一个查询才会分页
        //4.调用mapper的方法查询数据
        TbItemExample example=new TbItemExample();//设置查询条件使用
        List<TbItem> list1 = itemMapper.selectByExample(example);//相当于select * from tb_item;
        List<TbItem> list2 = itemMapper.selectByExample(example);

        //取分页信息
        PageInfo<TbItem> info=new PageInfo<>(list1);
        System.out.println("第一个分页的list的集合长度"+list1.size());
        System.out.println("第二个分页的list的集合长度"+list2.size());


        //5.遍历结果，打印
        System.out.println("查询的总记录数"+info.getTotal());

        for (TbItem tbItem : list1) {
            System.out.println(tbItem.getId()+">>>>mingch>>"+tbItem.getTitle());
        }
    }
    
}
