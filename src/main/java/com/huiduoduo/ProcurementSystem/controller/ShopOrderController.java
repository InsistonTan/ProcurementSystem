package com.huiduoduo.ProcurementSystem.controller;

import com.huiduoduo.ProcurementSystem.domain.ShopOrder;
import com.huiduoduo.ProcurementSystem.service.ShopOrderService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author TanJifeng
 * @Description  ShopOrderController
 * @date 2020/8/22 21:58
 */
@RequestMapping("/shop_order")
@Controller
public class ShopOrderController {
    @Autowired
    private ShopOrderService shopOrderService;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/history")
    @ResponseBody
    public Map getHistory(){
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //
        return shopOrderService.getHistory();
    }

    @RequestMapping("/ongoing")
    @ResponseBody
    public Map getOngoing(){
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //
        return shopOrderService.getOngoing();
    }

    @RequestMapping("/create")
    @ResponseBody
    public Map create(@RequestBody ShopOrder shopOrder){
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //
        return shopOrderService.addShopOrder(shopOrder);
    }

    @RequestMapping("/update")
    @ResponseBody
    public Map getHistory(@RequestBody ShopOrder shopOrder){
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //
        return shopOrderService.updateShopOrder(shopOrder);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map delete(@RequestBody ShopOrder shopOrder){
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //
        return shopOrderService.deleteShopOrder(shopOrder);
    }

    @RequestMapping("/confirm")
    @ResponseBody
    public Map confirm(@RequestBody ShopOrder shopOrder){
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //
        return shopOrderService.confirmShopOrder(shopOrder);
    }

    @RequestMapping("/approve")
    @ResponseBody
    public Map approve(@RequestBody List<ShopOrder> shop_order){
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //
        return shopOrderService.approved(shop_order);
    }

}
