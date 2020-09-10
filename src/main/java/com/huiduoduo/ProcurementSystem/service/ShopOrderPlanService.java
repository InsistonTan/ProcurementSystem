package com.huiduoduo.ProcurementSystem.service;

import com.huiduoduo.ProcurementSystem.domain.ShopOrderPlan;
import com.huiduoduo.ProcurementSystem.domain.pageBean.Page;

import java.util.Map;

/**
 * @author TanJifeng
 * @Description
 * @date 2020/8/29 19:32
 */
public interface ShopOrderPlanService {
    //添加
    Map add(ShopOrderPlan orderPlan);
    //删除
    Map delete(ShopOrderPlan orderPlan);
    //查询
    Map getShopOrderPlans(Page page);
    //修改
    Map update(ShopOrderPlan orderPlan);
}
