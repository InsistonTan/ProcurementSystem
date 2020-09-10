package com.huiduoduo.ProcurementSystem.controller;
import com.github.pagehelper.PageInfo;
import com.huiduoduo.ProcurementSystem.domain.Account;
import com.huiduoduo.ProcurementSystem.service.AccountService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TanJifeng
 * @Description
 * @date 2020/8/14 11:27
 */
@Controller
@RequestMapping("/user")
public class AccountController {
    @Autowired
    private HttpServletRequest request;
    @Autowired(required = false)
    private AccountService accountService;

    //登陆
    @RequestMapping("/login")
    @ResponseBody
    public Map login(@RequestBody Account account){
        Map result;
        //检查登录信息
        System.out.println(account);
        if(account==null||account.getUsername()==null||account.getPassword()==null)
            return ResultUtil.getErrorRes("登陆失败：登陆信息中存在空信息");
        //登陆
        try {
            result=accountService.login(account);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("登录失败：数据库操作失败");
        }

        //获取登陆结果状态
        String status=(String) result.get("status");
        //登陆成功
        if("success".equalsIgnoreCase(status)){
            Account info=(Account) result.get("user");
            request.getSession().setMaxInactiveInterval(6*60*60);
            request.getSession().setAttribute("username",info.getUsername());
            request.getSession().setAttribute("name",info.getName());
            request.getSession().setAttribute("role",info.getRole());
            request.getSession().setAttribute("info",info);
        }
        return result;
    }

    //退出登录
    @RequestMapping("/logout")
    @ResponseBody
    public Map logout(){
        //检查登陆状态
        Object user_obj=request.getSession().getAttribute("username");
        if(user_obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //注销登陆信息
        request.getSession().removeAttribute("username");
        request.getSession().removeAttribute("name");
        request.getSession().removeAttribute("role");
        request.getSession().removeAttribute("info");
        //
        return ResultUtil.getSuccessRes();
    }

    //（管理员）获取所有用户信息
    /*@RequestMapping("/getAllUser")
    @ResponseBody
    public Map getAllUser(){
        Map result=new HashMap();
        //检查权限
        String role=(String) request.getSession().getAttribute("role");
        if(!"admin".equalsIgnoreCase(role))
            return ResultUtil.getErrorRes("没有权限进行此操作");
        //
        try{
            List<Account> data=accountService.selectAll();
            result.put("status","success");
            result.put("data",data);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("查询数据库失败");
        }

        //System.out.println(res);
    }*/

    //分页（管理员）获取所有用户信息
    @RequestMapping("/getAllUser")
    @ResponseBody
    public Map getAllUser(@RequestBody Account input){
        //
        Map result=new HashMap();
        //检查权限
        String login_role=(String) request.getSession().getAttribute("role");
        if(!"admin".equalsIgnoreCase(login_role))
            return ResultUtil.getErrorRes("没有权限进行此操作");
        //
        try{
            PageInfo<Account> pageInfo=accountService.selectAllByPage(input);
            result.put("status","success");
            result.put("data",pageInfo.getList());
            result.put("total",pageInfo.getTotal());
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("查询数据库失败");
        }

        //System.out.println(res);
    }

    //（管理员）增加用户
    @RequestMapping("/setUser")
    @ResponseBody
    public Map setUser(@RequestBody Account account){
        Map result;
        //检查登陆用户权限
        String role=(String) request.getSession().getAttribute("role");
        //不是管理员
        if(!"admin".equalsIgnoreCase(role))
            return ResultUtil.getErrorRes("没有权限进行此操作");

        //是管理员
        try {
            result=accountService.addAccount(account);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

        return result;
    }

    //（管理员）删除用户
    @RequestMapping("/deleteUser")
    @ResponseBody
    public Map deleteUser(@RequestBody Account account){
        Map result;
        //检查登陆用户权限
        String role=(String) request.getSession().getAttribute("role");
        //不是管理员
        if(!"admin".equalsIgnoreCase(role))
            return ResultUtil.getErrorRes("没有权限进行此操作");

        //是管理员
        try {
            result=accountService.deleteAccount(account);
        } catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }
        //返回结果
        return result;
    }

    //修改用户信息
    @RequestMapping("/updateUser")
    @ResponseBody
    public Map updateUser(@RequestBody Account account){
        Map result;
        //检查登陆
        Object obj_username=request.getSession().getAttribute("username");
        if(obj_username==null)
            return ResultUtil.getErrorRes("你还没有登陆");
        //
        try {
            result=accountService.updateAccount(account);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

        return result;
    }
}
