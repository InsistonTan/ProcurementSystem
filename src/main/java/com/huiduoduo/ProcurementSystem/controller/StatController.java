package com.huiduoduo.ProcurementSystem.controller;

import com.huiduoduo.ProcurementSystem.service.StatService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author TanJifeng
 * @Description
 * @date 2020/10/1 16:29
 */
@Controller
@RequestMapping("/stat")
public class StatController {
    @Autowired
    private StatService statService;
    @Autowired
    private HttpServletRequest request;

    //检查登陆
    private boolean checkLogin(){
        Object user=request.getSession().getAttribute("username");
        if(user==null)
            return false;
        else
            return true;
    }

    //获取最近 7天采购金额
    @RequestMapping("/total")
    @ResponseBody
    public Map total(){
        //检查登陆
        if(checkLogin()==false)
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try{
            return statService.getRecentMoney();
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("服务器异常");
        }
    }

    //获取未处理订单数
    @RequestMapping("/todo_num")
    @ResponseBody
    public Map todo_num(){
        //检查登陆
        if(checkLogin()==false)
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try{
            return statService.getToDoNum();
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("服务器异常");
        }
    }

    //获取分店店员数量
    @RequestMapping("/shop_man_num")
    @ResponseBody
    public Map get_shoper_num(){
        //检查登陆
        if(checkLogin()==false)
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try{
            return statService.getShoperNum();
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("服务器异常");
        }
    }

    //获取今日采购总数
    @RequestMapping("/buy_total")
    @ResponseBody
    public Map get_buy_total(){
        //检查登陆
        if(checkLogin()==false)
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try{
            return statService.getBuyTotal();
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("服务器异常");
        }
    }

    //获取正在采购的采购员数量
    @RequestMapping("/buying_man_num")
    @ResponseBody
    public Map get_buying_man_num(){
        //检查登陆
        if(checkLogin()==false)
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try{
            return statService.getBuying_man_num();
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("服务器异常");
        }
    }

    //获取等待送货的分店数量
    @RequestMapping("/waiting_shop_num")
    @ResponseBody
    public Map get_waiting_shop_num(){
        //检查登陆
        if(checkLogin()==false)
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try{
            return statService.getWaiting_shop_num();
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("服务器异常");
        }
    }

    //获取当天各分店的订单提交数
    @RequestMapping("/today_order_num")
    @ResponseBody
    public Map get_today_order_num(){
        //检查登陆
        if(checkLogin()==false)
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try{
            return statService.getTodayOrderNum();
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("服务器异常");
        }
    }

    //获取当天各类型货品订购量
    @RequestMapping("/today_goods_num")
    @ResponseBody
    public Map get_today_goods_num(){
        //检查登陆
        if(checkLogin()==false)
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try{
            return statService.getTodayGoodsNum();
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("服务器异常");
        }
    }
}
