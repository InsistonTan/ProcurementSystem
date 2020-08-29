package com.huiduoduo.ProcurementSystem.service.impl;


import com.huiduoduo.ProcurementSystem.dao.ShopDao;
import com.huiduoduo.ProcurementSystem.domain.Shop;
import com.huiduoduo.ProcurementSystem.service.ShopService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WJQ
 * @since 2020-8-16 14:09
 */
@Service
public class ShopServiceImpl implements ShopService {
    @Resource
    private ShopDao shopDao;


    @Override
    public List getAllShop() {
        return shopDao.selectAll();
    }

    @Override
    public Map addShop(Shop shop) {

        if(shop.getShop_id() == null || shop.getShop_id() <= 0)
            return ResultUtil.getErrorRes("添加失败:请输入正确的id");

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
