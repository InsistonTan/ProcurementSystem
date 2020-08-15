package com.huiduoduo.ProcurementSystem.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author TanJifeng
 * @Description ShopOrderPlan 分店订购方案信息
 * @date 2020/8/13 13:35
 */
@Data
public class ShopOrderPlan implements Serializable {
    private int id;//方案id
    private int shop_id;//所属分店id
    private String shop_name;//所属分店名称
    private String plan_name;//方案名
    private List<ShopOrderPlanGoods> order_goods;//具体的货品订购数量
}
