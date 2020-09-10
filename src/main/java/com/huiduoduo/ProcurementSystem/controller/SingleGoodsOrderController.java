package com.huiduoduo.ProcurementSystem.controller;
import com.huiduoduo.ProcurementSystem.domain.ShopOrder;
import com.huiduoduo.ProcurementSystem.domain.SingleGoodsOrder;
import com.huiduoduo.ProcurementSystem.service.SingleGoodsOrderService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author TanJifeng
 * @Description SingleGoodsOrderController
 * @date 2020/8/29 8:33
 */
@Controller
@RequestMapping("/single_order")
public class SingleGoodsOrderController {
    @Autowired
    private SingleGoodsOrderService singleGoodsOrderService;
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

    //生成单品采购单
    @RequestMapping("/create")
    @ResponseBody
    public Map creatSingleOrder(@RequestBody List<ShopOrder> shopOrders){
        //检查登陆
        if(checkLogin()==false)
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try{
            return singleGoodsOrderService.addSingleOrder(shopOrders);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

    }

    //为单品采购单分配采购员
    @RequestMapping("/distribute")
    @ResponseBody
    public Map distribute(@RequestBody List<SingleGoodsOrder> orders){
        //检查登陆
        if(checkLogin()==false)
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try {
            return singleGoodsOrderService.distribute(orders);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

    }

    //为单品采购单分配采购员
    @RequestMapping("/delete")
    @ResponseBody
    public Map distribute(@RequestBody SingleGoodsOrder order){
        //检查登陆
        if(checkLogin()==false)
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try {
            return singleGoodsOrderService.delete(order);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

    }

    //查询历史
    @RequestMapping("/history")
    @ResponseBody
    public Map history(){
        //检查登陆
        if(checkLogin()==false)
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try {
            return singleGoodsOrderService.selectHistory();
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

    }

    //正在进行的
    @RequestMapping("/ongoing")
    @ResponseBody
    public Map ongoing(){
        //检查登陆
        if(checkLogin()==false)
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try {
            return singleGoodsOrderService.selectOngoing();
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

    }

    //采购员确认实际收货信息
    @RequestMapping("/buyer_confirm")
    @ResponseBody
    public Map confirm(@RequestBody SingleGoodsOrder order){
        //检查登陆
        if(checkLogin()==false)
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try {
            return singleGoodsOrderService.updateBuyRes(order);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

    }

    //更新采购状态（进度）
    @RequestMapping("/buy_status")
    @ResponseBody
    public Map buy_status(@RequestBody SingleGoodsOrder order){
        //检查登陆
        if(checkLogin()==false)
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try {
            return singleGoodsOrderService.updateStatus(order);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

    }
}
