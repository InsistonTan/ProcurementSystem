package com.huiduoduo.ProcurementSystem.controller;

import com.huiduoduo.ProcurementSystem.domain.Account;
import com.huiduoduo.ProcurementSystem.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
        Map result=new HashMap();
        //检查登录信息
        if(account==null||account.getUsername()==null||account.getPassword()==null||account.getRole()==null){
            result.put("status","failed");
            result.put("msg","登陆信息中存在空信息");
            return result;
        }
        //登陆
        result=accountService.login(account);
        //获取登陆结果状态
        String status=(String) result.get("status");
        //登陆成功
        if("success".equalsIgnoreCase(status)){
            Account info=(Account) result.get("user");
            request.getSession().setMaxInactiveInterval(6*60*60);
            request.getSession().setAttribute("username",info.getUsername());
            request.getSession().setAttribute("name",info.getName());
            request.getSession().setAttribute("role",info.getRole());
        }
        return result;
    }

    //（管理员）获取所有用户信息
    @RequestMapping("/getAllUser")
    @ResponseBody
    public Map getAllUser(){
        Map result=new HashMap();
        //检查权限
        String role=(String) request.getSession().getAttribute("role");
        if(!"admin".equalsIgnoreCase(role)){
            result.put("status","failed");
            result.put("msg","没有权限进行此操作");
            return result;
        }
        //
        List<Account> data=accountService.selectAll();
        result.put("status","success");
        result.put("data",data);
        return result;
        //System.out.println(res);
    }
}
