package com.huiduoduo.ProcurementSystem.controller;


import com.huiduoduo.ProcurementSystem.domain.Goods;
import com.huiduoduo.ProcurementSystem.domain.GoodsType;
import com.huiduoduo.ProcurementSystem.service.GoodsService;
import com.huiduoduo.ProcurementSystem.service.GoodsTypeService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author WJQ
 * @since 2020-08-16 11:27
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Resource
    private GoodsService goodsService;
    @Resource
    private GoodsTypeService goodsTypeService;

    @RequestMapping("/getAllGoods")
    public Map getAllGoods(){
        //开始查询
        try{
            List data = goodsService.getAllGoods();
            return ResultUtil.getSuccessRes(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("查询失败");
        }
    }

    @RequestMapping(value = "/setGoods",method = RequestMethod.POST)
    public Map setGoods(Goods goods){

//        //权限控制，如果使用AOP控制权限，此段可删除
//        String role=(String) request.getSession().getAttribute("role");
//        if(!"admin".equalsIgnoreCase(role))
//            return ResultUtil.getErrorRes("没有权限进行此操作");


        //开始添加
        try{
            Map result = goodsService.addGoods(goods);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("添加失败：出现异常");
        }

    }

    @RequestMapping(value = "/updateGoods",method = RequestMethod.POST)
    public Map updateGoods(Goods goods){

//        //权限控制，如果使用AOP控制权限，此段可删除
//        String role=(String) request.getSession().getAttribute("role");
//        if(!"admin".equalsIgnoreCase(role))
//            return ResultUtil.getErrorRes("没有权限进行此操作");

        //开始修改
        try{
            Map result = goodsService.updateGoods(goods);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("修改失败：出现异常");
        }
    }

    @RequestMapping(value = "/deleteGoods",method = RequestMethod.POST)
    public Map deleteGoods(Goods goods){

//        //权限控制，如果使用AOP控制权限，此段可删除
//        String role=(String) request.getSession().getAttribute("role");
//        if(!"admin".equalsIgnoreCase(role))
//            return ResultUtil.getErrorRes("没有权限进行此操作");

        //开始删除
        try{
            Map result = goodsService.deleteGoods(goods);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("删除失败：出现异常");
        }
    }

    @RequestMapping("/getAllGoodsType")
    public Map getAllGoodsType(){

        //开始查询
        try{
            List data = goodsTypeService.getAllGoodsType();
            return ResultUtil.getSuccessRes(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("查询失败");
        }
    }
/**http://localhost:8080/goods/setGoodsType?type_id=2&type_name=testget*/
    @RequestMapping(value = "/setGoodsType",method = RequestMethod.POST)
    public Map setGoodsType (GoodsType goodsType){

//        //权限控制，如果使用AOP控制权限，此段可删除
//        String role=(String) request.getSession().getAttribute("role");
//        if(!"admin".equalsIgnoreCase(role))
//            return ResultUtil.getErrorRes("没有权限进行此操作");

        //开始添加
        try{
            Map result = goodsTypeService.addGoodsType(goodsType);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("添加失败：出现异常");
        }
    }

    @RequestMapping(value = "/updateGoodsType",method = RequestMethod.POST)
    public Map updateGoodsType(GoodsType goodsType){

//        //权限控制，如果使用AOP控制权限，此段可删除
//        String role=(String) request.getSession().getAttribute("role");
//        if(!"admin".equalsIgnoreCase(role))
//            return ResultUtil.getErrorRes("没有权限进行此操作");

        //开始修改
        try{
            Map result = goodsTypeService.updateGoodsType(goodsType);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("修改失败：出现异常");
        }
    }

    @RequestMapping(value = "/deleteGoodsType",method = RequestMethod.POST)
    public Map deleteGoodsType(GoodsType goodsType){

//        //权限控制，如果使用AOP控制权限，此段可删除
//        String role=(String) request.getSession().getAttribute("role");
//        if(!"admin".equalsIgnoreCase(role))
//            return ResultUtil.getErrorRes("没有权限进行此操作");

        //开始删除
        try{
            Map result = goodsTypeService.deleteGoodsType(goodsType);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("删除失败：出现异常");
        }
    }







}
