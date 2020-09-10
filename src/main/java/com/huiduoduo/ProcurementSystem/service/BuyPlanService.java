package com.huiduoduo.ProcurementSystem.service;

import com.huiduoduo.ProcurementSystem.domain.BuyPlan;
import com.huiduoduo.ProcurementSystem.domain.ShopOrderPlan;
import com.huiduoduo.ProcurementSystem.domain.pageBean.Page;

import java.util.Map;

/**
 * @author TanJifeng
 * @Description
 * @date 2020/8/29 19:35
 */
public interface BuyPlanService {
    //添加
    Map add(BuyPlan buyPlan);
    //删除
    Map delete(BuyPlan buyPlan);
    //查询
    Map getBuyPlans(Page page);
    //修改
    Map update(BuyPlan buyPlan);
}
