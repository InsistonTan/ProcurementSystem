package com.huiduoduo.ProcurementSystem.controller;

import com.huiduoduo.ProcurementSystem.domain.BuyPlan;
import com.huiduoduo.ProcurementSystem.domain.pageBean.Page;
import com.huiduoduo.ProcurementSystem.service.BuyPlanService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author TanJifeng
 * @Description
 * @date 2020/8/29 21:48
 */
@Controller
@RequestMapping("/buy_plan")
public class BuyPlanController {
    @Autowired
    private BuyPlanService buyPlanService;
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
    @RequestMapping("/getAll")
    @ResponseBody
    public Map getAll(@RequestBody Page page){
        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try {
            return buyPlanService.getBuyPlans(page);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }
    }

    //添加
    @RequestMapping("/set")
    @ResponseBody
    public Map set(@RequestBody BuyPlan buyPlan){
        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try {
            return buyPlanService.add(buyPlan);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }
    }

    //修改
    @RequestMapping("/update")
    @ResponseBody
    public Map update(@RequestBody BuyPlan buyPlan){
        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try{
            return buyPlanService.update(buyPlan);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }
    }

    //删除
    @RequestMapping("/delete")
    @ResponseBody
    public Map delete(@RequestBody BuyPlan buyPlan){
        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");
        //
        try {
            return buyPlanService.delete(buyPlan);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }
    }
}
