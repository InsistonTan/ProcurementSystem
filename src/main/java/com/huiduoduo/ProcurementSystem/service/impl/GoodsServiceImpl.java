package com.huiduoduo.ProcurementSystem.service.impl;


import com.huiduoduo.ProcurementSystem.dao.GoodsDao;
import com.huiduoduo.ProcurementSystem.dao.GoodsTypeDao;
import com.huiduoduo.ProcurementSystem.domain.Goods;
import com.huiduoduo.ProcurementSystem.service.GoodsService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author WJQ
 * @since 2020-8-16 14:09
 */
@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    private GoodsDao goodsDao;
    
    @Override
    public List<Goods> getAllGoods() {
        return goodsDao.selectAll();
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
