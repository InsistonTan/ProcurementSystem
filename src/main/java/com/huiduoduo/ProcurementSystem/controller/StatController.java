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
            return ResultUtil.getErrorRes("数据库操作失败");
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
            return ResultUtil.getErrorRes("数据库操作失败");
        }
    }
}
