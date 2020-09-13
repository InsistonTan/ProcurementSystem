package com.huiduoduo.ProcurementSystem.controller;


import com.huiduoduo.ProcurementSystem.domain.Account;
import com.huiduoduo.ProcurementSystem.domain.Shop;
import com.huiduoduo.ProcurementSystem.domain.pageBean.SearchPage;
import com.huiduoduo.ProcurementSystem.service.ShopService;
import com.huiduoduo.ProcurementSystem.service.impl.ShopServiceImpl;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WJQ
 * @since 2020-08-16 11:27
 */
@RestController
@RequestMapping("/shop")
public class ShopController {
    @Resource
    private ShopService shopService;
    @Resource
    private HttpServletRequest request;

    private boolean checkLogin(){
        Object user=request.getSession().getAttribute("username");
        return user != null;
    }

    //获取登陆用户信息
    private Account getLoginInfo(){
        return (Account) request.getSession().getAttribute("info");
    }

    @RequestMapping("/getAllShop")
    public Map getAllShop(@RequestBody SearchPage searchPage){

        //开始查询
        try{
            Map data = shopService.getAllShop(searchPage);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("查询失败");
        }
    }

    @RequestMapping(value = "/setShop",method = RequestMethod.POST)
    public Map setShop(@RequestBody Shop shop){

        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");

        if(!"admin".equals(getLoginInfo().getRole()) )
            return ResultUtil.getErrorRes("操作失败：只有管理员才能进行此操作");

        //开始添加
        try{
            Map result = shopService.addShop(shop);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("添加失败：出现异常");
        }
    }


    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Map update(@RequestBody Shop shop){

        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");

        if(!"admin".equals(getLoginInfo().getRole()) )
            return ResultUtil.getErrorRes("操作失败：只有管理员才能进行此操作");

        //开始修改
        try{
            Map result = shopService.updateShop(shop);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("修改失败：出现异常");
        }
    }

    @RequestMapping(value = "/del",method = RequestMethod.POST)
    public Map del(@RequestBody Shop shop){

        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");

        if(!"admin".equals(getLoginInfo().getRole()) )
            return ResultUtil.getErrorRes("操作失败：只有管理员才能进行此操作");

        //开始删除
        try{
            Map result = shopService.deleteShop(shop);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("删除失败：出现异常");
        }
    }

}
