package com.huiduoduo.ProcurementSystem.controller;


import com.huiduoduo.ProcurementSystem.domain.Account;
import com.huiduoduo.ProcurementSystem.domain.Goods;
import com.huiduoduo.ProcurementSystem.domain.GoodsType;
import com.huiduoduo.ProcurementSystem.domain.pageBean.GoodsPage;
import com.huiduoduo.ProcurementSystem.service.GoodsService;
import com.huiduoduo.ProcurementSystem.service.GoodsTypeService;
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
 * @since 2020-08-16 11:27
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Resource
    private GoodsService goodsService;
    @Resource
    private GoodsTypeService goodsTypeService;
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

    @RequestMapping("/getAllGoods")
    public Map getAllGoods(@RequestBody GoodsPage goodsPage){

        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");

        //开始查询
        try{
            Map data = goodsService.getAllGoods(goodsPage);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("查询失败");
        }
    }

    @RequestMapping(value = "/setGoods",method = RequestMethod.POST)
    public Map setGoods(@RequestBody Goods goods){

        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");

        if(!"admin".equals(getLoginInfo().getRole()) && !"manager".equals(getLoginInfo().getRole()))
            return ResultUtil.getErrorRes("操作失败：只有管理员和经理才能进行此操作");


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
    public Map updateGoods(@RequestBody Goods goods){

        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");

        if(!"admin".equals(getLoginInfo().getRole()) && !"manager".equals(getLoginInfo().getRole()))
            return ResultUtil.getErrorRes("操作失败：只有管理员和经理才能进行此操作");

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
    public Map deleteGoods(@RequestBody Goods goods){

        if(!checkLogin())
        return ResultUtil.getErrorRes("操作失败：你还没有登陆");

        if(!"admin".equals(getLoginInfo().getRole()) && !"manager".equals(getLoginInfo().getRole()))
            return ResultUtil.getErrorRes("操作失败：只有管理员和经理才能进行此操作");

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

        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");

        //开始查询
        try{
            List data = goodsTypeService.getAllGoodsType();
            return ResultUtil.getSuccessRes(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.getErrorRes("查询失败");
        }
    }

    @RequestMapping(value = "/setGoodsType",method = RequestMethod.POST)
    public Map setGoodsType (@RequestBody GoodsType goodsType){

        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");

        if(!"admin".equals(getLoginInfo().getRole()) && !"manager".equals(getLoginInfo().getRole()))
            return ResultUtil.getErrorRes("操作失败：只有管理员和经理才能进行此操作");

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
    public Map updateGoodsType(@RequestBody GoodsType goodsType){

        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");

        if(!"admin".equals(getLoginInfo().getRole()) && !"manager".equals(getLoginInfo().getRole()))
            return ResultUtil.getErrorRes("操作失败：只有管理员和经理才能进行此操作");

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
    public Map deleteGoodsType(@RequestBody GoodsType goodsType){

        if(!checkLogin())
            return ResultUtil.getErrorRes("操作失败：你还没有登陆");

        if(!"admin".equals(getLoginInfo().getRole()) && !"manager".equals(getLoginInfo().getRole()))
            return ResultUtil.getErrorRes("操作失败：只有管理员和经理才能进行此操作");

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
