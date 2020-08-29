package com.huiduoduo.ProcurementSystem.controller;
import com.huiduoduo.ProcurementSystem.domain.Account;
import com.huiduoduo.ProcurementSystem.service.AccountService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        if(account==null||account.getUsername()==null||account.getPassword()==null||account.getRole()==null)
            return ResultUtil.getErrorRes("登陆信息中存在空信息");
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
            request.getSession().setAttribute("info",info);
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
        if(!"admin".equalsIgnoreCase(role))
            return ResultUtil.getErrorRes("没有权限进行此操作");
        //
        try{
            List<Account> data=accountService.selectAll();
            result.put("status","success");
            result.put("data",data);
            return result;
        }catch (Exception e){
            return ResultUtil.getErrorRes("添加进数据库失败");
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
        result=accountService.addAccount(account);
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
        result=accountService.deleteAccount(account);
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
        result=accountService.updateAccount(account);
        return result;
    }
}
