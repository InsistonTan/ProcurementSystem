package com.huiduoduo.ProcurementSystem.controller;


import com.huiduoduo.ProcurementSystem.domain.Shop;
import com.huiduoduo.ProcurementSystem.service.ShopService;
import com.huiduoduo.ProcurementSystem.service.impl.ShopServiceImpl;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    @RequestMapping("/getAllShop")
    public Map getAllShop(){

        //开始查询
        try{
            List data = shopService.getAllShop();
            return ResultUtil.getSuccessRes(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("查询失败");
        }
    }
/**http://localhost:8080/shop/setShop?shop_id=7&shop_name=testget&shop_position=ttt&shop_phone=77777*/
    @RequestMapping(value = "/setShop",method = RequestMethod.POST)
    public Map setShop(Shop shop){

//        //权限控制，如果使用AOP控制权限，此段可删除
//        String role=(String) request.getSession().getAttribute("role");
//        if(!"admin".equalsIgnoreCase(role))
//            return ResultUtil.getErrorRes("没有权限进行此操作");

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
    public Map update(Shop shop){

//        //权限控制，如果使用AOP控制权限，此段可删除
//        String role=(String) request.getSession().getAttribute("role");
//        if(!"admin".equalsIgnoreCase(role))
//            return ResultUtil.getErrorRes("没有权限进行此操作");

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
    public Map del(Shop shop){

//        //权限控制，如果使用AOP控制权限，此段可删除
//        String role=(String) request.getSession().getAttribute("role");
//        if(!"admin".equalsIgnoreCase(role))
//            return ResultUtil.getErrorRes("没有权限进行此操作");

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
