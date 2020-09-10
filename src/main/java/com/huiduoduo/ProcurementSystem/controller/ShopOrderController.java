package com.huiduoduo.ProcurementSystem.controller;

import com.huiduoduo.ProcurementSystem.domain.ShopOrder;
import com.huiduoduo.ProcurementSystem.domain.pageBean.ShopOrderPage;
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

    @RequestMapping("/getAll")
    @ResponseBody
    public Map getHistory(@RequestBody ShopOrderPage page){
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //
        try {
            if(page.isHistory())
                return shopOrderService.getHistory(page);
            else
                return shopOrderService.getOngoing(page);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }
    }

    @RequestMapping("/create")
    @ResponseBody
    public Map create(@RequestBody ShopOrder shopOrder){
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //
        try{
            return shopOrderService.addShopOrder(shopOrder);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

    }

    @RequestMapping("/update")
    @ResponseBody
    public Map getHistory(@RequestBody ShopOrder shopOrder){
        System.out.println("Update:"+shopOrder);
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //
        try {
            return shopOrderService.updateShopOrder(shopOrder);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map delete(@RequestBody ShopOrder shopOrder){
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //
        try{
            return shopOrderService.deleteShopOrder(shopOrder);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

    }

    @RequestMapping("/confirm")
    @ResponseBody
    public Map confirm(@RequestBody ShopOrder shopOrder){
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //
        try{
            return shopOrderService.confirmShopOrder(shopOrder);
        } catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }
    }

    @RequestMapping("/approve")
    @ResponseBody
    public Map approve(@RequestBody List<ShopOrder> shop_order){
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //
        try {
            return shopOrderService.approved(shop_order);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

    }

}
