package com.huiduoduo.ProcurementSystem.service.impl;


import com.huiduoduo.ProcurementSystem.dao.GoodsTypeDao;
import com.huiduoduo.ProcurementSystem.domain.GoodsType;
import com.huiduoduo.ProcurementSystem.service.GoodsTypeService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author WJQ
 * @since 2020-8-16 14:09
 */
@Service
public class GoodsTypeServiceImpl implements GoodsTypeService {
    @Resource
    private GoodsTypeDao goodsTypeDao;


    @Override
    public List getAllGoodsType() {
        return goodsTypeDao.selectAll();
    }

    @Override
    public Map addGoodsType(GoodsType goodsType) {

        if(goodsType.getType_id() == null || goodsType.getType_id() <= 0)
            return ResultUtil.getErrorRes("添加失败:请输入正确的id");

        if(goodsTypeDao.selectByName(goodsType.getType_name()) != null)
            return ResultUtil.getErrorRes("添加失败:名字不能重复");

        if(goodsTypeDao.add(goodsType) == true)
            return ResultUtil.getSuccessRes();
        else
            return ResultUtil.getErrorRes("添加失败");

    }

    @Override
    public Map updateGoodsType(GoodsType goodsType) {

        if(goodsType.getType_id() == null || goodsType.getType_id() <= 0)
            return ResultUtil.getErrorRes("修改失败:请输入正确的id");

        if(goodsTypeDao.update(goodsType) == true)
            return ResultUtil.getSuccessRes();
        else
            return ResultUtil.getErrorRes("修改失败");

    }




    @Override
    public Map deleteGoodsType(GoodsType goodsType) {
        if(goodsType.getType_id() == null || goodsType.getType_id() <= 0)
            return ResultUtil.getErrorRes("删除失败:请输入正确的id");

        if(goodsTypeDao.delete(goodsType.getType_id()) == true)
            return ResultUtil.getSuccessRes();
        else
            return ResultUtil.getErrorRes("删除失败");
    }
}
