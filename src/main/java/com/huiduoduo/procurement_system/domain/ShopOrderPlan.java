package com.huiduoduo.procurement_system.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author TanJifeng
 * @Description ShopOrderPlan 分店订购方案信息
 * @date 2020/8/13 13:35
 */
public class ShopOrderPlan implements Serializable {
    private int id;//方案id
    private int shop_id;//所属分店id
    private String shop_name;//所属分店名称
    private String plan_name;//方案名
    private List<ShopOrderPlanGoods> order_goods;//具体的货品订购数量

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public List<ShopOrderPlanGoods> getOrder_goods() {
        return order_goods;
    }

    public void setOrder_goods(List<ShopOrderPlanGoods> order_goods) {
        this.order_goods = order_goods;
    }

    @Override
    public String toString() {
        return "ShopOrderPlan{" +
                "id=" + id +
                ", shop_id=" + shop_id +
                ", shop_name='" + shop_name + '\'' +
                ", plan_name='" + plan_name + '\'' +
                ", order_goods=" + order_goods +
                '}';
    }
}
