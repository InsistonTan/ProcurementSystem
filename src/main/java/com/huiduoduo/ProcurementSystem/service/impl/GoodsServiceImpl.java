package com.huiduoduo.ProcurementSystem.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huiduoduo.ProcurementSystem.dao.GoodsDao;
import com.huiduoduo.ProcurementSystem.dao.GoodsTypeDao;
import com.huiduoduo.ProcurementSystem.domain.Goods;
import com.huiduoduo.ProcurementSystem.domain.ShopOrder;
import com.huiduoduo.ProcurementSystem.domain.pageBean.GoodsPage;
import com.huiduoduo.ProcurementSystem.service.GoodsService;
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
public class GoodsServiceImpl implements GoodsService {
    @Resource
    private GoodsDao goodsDao;
    
    @Override
    public Map getAllGoods(GoodsPage goodsPage) {
        //检查参数
        if(goodsPage.getPage() == null || goodsPage.getPage() == 0)
            goodsPage.setPage(1);
        if(goodsPage.getLimit() == null || goodsPage.getLimit() == 0)
            goodsPage.setLimit(20);

        //判断搜索内容
        if(goodsPage.getSearch() == null)
            goodsPage.setSearch("");

        String sort = goodsPage.getSort();
        //判断是否是默认排序
        if (sort == null || sort.length() == 0)
            sort="+goods_id";

        //处理排序语句
        try {
            sort = SortStringUtil.replace(sort);
        }   catch (Exception e) {
            return ResultUtil.getErrorRes("错误的排序表达式");
        }

        //将空内容排序到最后
        if(sort.indexOf("goods_sort") >= 0)
            //若找到goods_sort字段在goods_sort字段前插入字符串使空的goods_sort排最后
        sort = new StringBuffer(sort).insert(sort.indexOf("goods_sort"),"IF(ISNULL(goods_sort),1,0),").toString();



        //开始分页
        PageHelper.startPage(goodsPage.getPage(),goodsPage.getLimit(),sort);

        List goods = null;

        //判断是否要按货物类型分类查询
        if(goodsPage.getGoods_type_id()!=null && goodsPage.getGoods_type_id()>=0) {
            goods = goodsDao.selectAllByType(goodsPage.getSearch(),goodsPage.getGoods_type_id());
        }else{
            goods = goodsDao.selectAll(goodsPage.getSearch());
        }

        //生成PageInfo
        PageInfo pageInfo=new PageInfo<>(goods);
        //返回结果
        Map result=new HashMap();
        result.put("status","success");
        result.put("data",goods);
        result.put("total",pageInfo.getTotal());

        return result;
    }

    @Override
    public Map addGoods(Goods goods) {
        if(goods.getGoods_id() == null || goods.getGoods_id() <= 0)
            return ResultUtil.getErrorRes("添加失败:请输入正确的id");

        if(goodsDao.selectByName(goods.getGoods_name()) != null)
            return ResultUtil.getErrorRes("添加失败:名字不能重复");

        if(goodsDao.add(goods) == true)
            return ResultUtil.getSuccessRes();
        else
            return ResultUtil.getErrorRes("添加失败");
    }

    @Override
    public Map updateGoods(Goods goods) {
        if(goods.getGoods_id() == null || goods.getGoods_id() <= 0)
            return ResultUtil.getErrorRes("修改失败:请输入正确的id");

        if(goodsDao.update(goods) == true)
            return ResultUtil.getSuccessRes();
        else
            return ResultUtil.getErrorRes("修改失败");
    }

    @Override
    public Map deleteGoods(Goods goods) {
        if(goods.getGoods_id() == null || goods.getGoods_id() <= 0)
            return ResultUtil.getErrorRes("删除失败:请输入正确的id");

        if(goodsDao.delete(goods.getGoods_id()) == true)
            return ResultUtil.getSuccessRes();
        else
            return ResultUtil.getErrorRes("删除失败");
    }




}
