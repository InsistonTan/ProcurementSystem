package com.huiduoduo.ProcurementSystem.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huiduoduo.ProcurementSystem.dao.ShopDao;
import com.huiduoduo.ProcurementSystem.domain.Shop;
import com.huiduoduo.ProcurementSystem.domain.pageBean.SearchPage;
import com.huiduoduo.ProcurementSystem.service.ShopService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import com.huiduoduo.ProcurementSystem.utils.SortStringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WJQ
 * @date 2020-8-16 14:09
 */
@Service
public class ShopServiceImpl implements ShopService {
    @Resource
    private ShopDao shopDao;


    @Override
    public Map getAllShop(SearchPage searchPage) {
        //检查参数

        if(searchPage.getPage() == null || searchPage.getPage() == 0)
            searchPage.setPage(1);
        if(searchPage.getLimit() == null || searchPage.getLimit() == 0)
            searchPage.setLimit(20);

        //判断搜索内容
        if(searchPage.getSearch() == null)
            searchPage.setSearch("");

        String sort = searchPage.getSort();
        //判断是否是默认排序
        if (sort == null || sort.length() == 0)
            sort="+shop_id";

        //处理排序语句
        try {
            sort = SortStringUtil.replace(sort);
        }   catch (Exception e) {
            return ResultUtil.getErrorRes("错误的排序表达式");
        }

        //开始分页
        PageHelper.startPage(searchPage.getPage(),searchPage.getLimit(),sort);

        List data = shopDao.selectAll(searchPage.getSearch());


        //生成PageInfo
        PageInfo pageInfo=new PageInfo<>(data);
        //返回结果
        Map result=new HashMap();
        result.put("status","success");
        result.put("data",data);
        result.put("total",pageInfo.getTotal());

        return result;
    }

    @Override
    public Map addShop(Shop shop) {


        if(shop.getShop_name() == null || shop.getShop_name().length() == 0)
            return  ResultUtil.getErrorRes("添加失败:请输入正确的名字");

        if(shopDao.selectByName(shop.getShop_name()) != null)
            return ResultUtil.getErrorRes("添加失败:名字不能重复");

        if(shopDao.add(shop) == true)
            return ResultUtil.getSuccessRes();
        else
            return ResultUtil.getErrorRes("添加失败");

    }

    @Override
    public Map updateShop(Shop shop) {

        if(shop.getShop_id() == null || shop.getShop_id() <= 0)
            return ResultUtil.getErrorRes("修改失败:请输入正确的id");

        if(shopDao.update(shop) == true)
            return ResultUtil.getSuccessRes();
        else
            return ResultUtil.getErrorRes("修改失败");

    }




    @Override
    public Map deleteShop(Shop shop) {
        if(shop.getShop_id() == null || shop.getShop_id() <= 0)
            return ResultUtil.getErrorRes("删除失败:请输入正确的id");

        if(shopDao.delete(shop.getShop_id()) == true)
            return ResultUtil.getSuccessRes();
        else
            return ResultUtil.getErrorRes("删除失败");
    }
}
