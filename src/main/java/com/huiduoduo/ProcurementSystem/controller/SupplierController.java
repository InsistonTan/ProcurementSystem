package com.huiduoduo.ProcurementSystem.controller;


import com.huiduoduo.ProcurementSystem.domain.Account;
import com.huiduoduo.ProcurementSystem.domain.Supplier;
import com.huiduoduo.ProcurementSystem.domain.pageBean.SearchPage;
import com.huiduoduo.ProcurementSystem.service.SupplierService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author WJQ
 * @since  2020-08-16 11:27
 */
@RestController
@RequestMapping("/supplier")
public class SupplierController {
    @Resource
    private SupplierService supplierService;
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

    @RequestMapping("/getAll")
    public Map getAll(@RequestBody SearchPage searchPage){
        //检查登陆
        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");

        //开始查询
        try{
            Map data = supplierService.getAllSupplier(searchPage);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("查询失败");
        }

    }

    @RequestMapping(value = "/set",method = RequestMethod.POST)
    public Map set(@RequestBody Supplier supplier){

        //检查登陆
        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");

        //权限判断
        String role = getLoginInfo().getRole();

        if(!"admin".equals(role) && !"manager".equals(role) && !"buyer".equals(role))
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //开始添加
        try{
            Map result = supplierService.addSupplier(supplier);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("添加失败：出现异常");
        }

    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Map update(@RequestBody Supplier supplier){

        //检查登陆
        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");

        //权限判断
        String role = getLoginInfo().getRole();

        if(!"admin".equals(role) && !"manager".equals(role) && !"buyer".equals(role))
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //开始修改
        try{
            Map result = supplierService.updateSupplier(supplier);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("修改失败：出现异常");
        }
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Map delete(@RequestBody Supplier supplier){

        //检查登陆
        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");

        //权限判断
        String role = getLoginInfo().getRole();

        if(!"admin".equals(role) && !"manager".equals(role) && !"buyer".equals(role))
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //开始删除
        try{
            Map result = supplierService.deleteSupplier(supplier);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("删除失败：出现异常");
        }
    }


}
