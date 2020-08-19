package com.huiduoduo.ProcurementSystem.controller;


import com.huiduoduo.ProcurementSystem.domain.Supplier;
import com.huiduoduo.ProcurementSystem.service.SupplierService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    @RequestMapping("/getAll")
    public Map getAll(){

        //开始查询
        try{
            List data = supplierService.getAllSupplier();
            return ResultUtil.getSuccessRes(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("查询失败");
        }

    }

    @RequestMapping(value = "/set",method = RequestMethod.POST)
    public Map set(Supplier supplier){

//        //权限控制，如果使用AOP控制权限，此段可删除
//        String role=(String) request.getSession().getAttribute("role");
//        if(!"admin".equalsIgnoreCase(role))
//            return ResultUtil.getErrorRes("没有权限进行此操作");

        //开始添加
        try{
            Map result = supplierService.addSupplier(supplier);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("添加失败：出现异常");
        }

    }
/**http://localhost:8080/supplier/update?id=2&supplier_name=testget&shop_phone=777775&note=note*/
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Map update(Supplier supplier){

//        //权限控制，如果使用AOP控制权限，此段可删除
//        String role=(String) request.getSession().getAttribute("role");
//        if(!"admin".equalsIgnoreCase(role))
//            return ResultUtil.getErrorRes("没有权限进行此操作");
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
    public Map delete(Supplier supplier){

//        //权限控制，如果使用AOP控制权限，此段可删除
//        String role=(String) request.getSession().getAttribute("role");
//        if(!"admin".equalsIgnoreCase(role))
//            return ResultUtil.getErrorRes("没有权限进行此操作");

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
