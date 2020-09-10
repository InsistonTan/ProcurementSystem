package com.huiduoduo.ProcurementSystem.controller;

import com.huiduoduo.ProcurementSystem.domain.ShopOrderPlan;
import com.huiduoduo.ProcurementSystem.domain.pageBean.Page;
import com.huiduoduo.ProcurementSystem.service.ShopOrderPlanService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author TanJifeng
 * @Description ShopOrderPlanController
 * @date 2020/8/29 21:38
 */
@Controller
@RequestMapping("/orderPlan")
public class ShopOrderPlanController {
    @Autowired
    private ShopOrderPlanService shopOrderPlanService;
    @Autowired
    private HttpServletRequest request;

    private boolean checkLogin(){
        Object user=request.getSession().getAttribute("username");
        if(user==null)
            return false;
        else
            return true;
    }

    //查询
    @RequestMapping("/get")
    @ResponseBody
    public Map get(@RequestBody Page page){
        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try{
            return shopOrderPlanService.getShopOrderPlans(page);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

    }

    //添加
    @RequestMapping("/set")
    @ResponseBody
    public Map set(@RequestBody ShopOrderPlan shopOrderPlan){
        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try {
            return shopOrderPlanService.add(shopOrderPlan);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

    }

    //修改
    @RequestMapping("/update")
    @ResponseBody
    public Map update(@RequestBody ShopOrderPlan shopOrderPlan){
        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try {
            return shopOrderPlanService.update(shopOrderPlan);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

    }

    //删除
    @RequestMapping("/delete")
    @ResponseBody
    public Map delete(@RequestBody ShopOrderPlan shopOrderPlan){
        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try {
            return shopOrderPlanService.delete(shopOrderPlan);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

    }

}
