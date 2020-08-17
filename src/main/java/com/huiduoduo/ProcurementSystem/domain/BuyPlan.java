package com.huiduoduo.ProcurementSystem.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author TanJifeng
 * @Description BuyPlan 采购经理的单品订单分配方案
 * @date 2020/8/13 13:14
 */
@Data
public class BuyPlan implements Serializable {
    private Integer id;//主键 id
    private String manager_username;//采购经理账号
    private String name;//经理名称
    private String plan_name;//方案名称
    private List<BuyPlanBuyer> buy_plan_buyer;//具体的分配列表

}
