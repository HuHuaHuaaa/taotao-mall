package com.taotao.order.controller;

import com.taotao.cart.service.CartService;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private CartService cartService;

    @Value("TT_TOKEN_KEY")
    private String TT_TOKEN_KEY;

    @RequestMapping("/order/order-cart")
    public String showOrder(HttpServletRequest request){
        //1.从cookie中获取用户token
//        String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
//        if (StringUtils.isNotBlank(token)){
//            //2.调用sso服务获取用户信息
//            TaotaoResult userByToken = userLoginService.getUserByToken(token);
//            //3.必须是用户登录了才显示
//            if (userByToken.getStatus()==200){
//                //4.展示用户的送货地址 根据用户的ID查询该用户的配送地址 静态数据
//                //5.展示支付方式 从数据库中获取支付方式 静态数据
//                //6.调用购物车服务从redis中获取购物车中的商品列表
//                TbUser user =(TbUser) userByToken.getData();
//                List<TbItem> cartList = cartService.getCartList(user.getId());
//                //7.将列表展示到页面 （传递到页面中用model）
//                request.setAttribute("cartList",cartList);
//            }
//
//        }
        TbUser user =(TbUser) request.getAttribute("USER_INFO");
        List<TbItem> cartList = cartService.getCartList(user.getId());
        //7.将列表展示到页面 （传递到页面中用model）
        request.setAttribute("cartList",cartList);
        return "order-cart";
    }
}
