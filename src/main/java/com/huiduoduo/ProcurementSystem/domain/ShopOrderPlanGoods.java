package com.huiduoduo.ProcurementSystem.domain;

import java.io.Serializable;

/**
 * @author TanJifeng
 * @Description ShopOrderPlanGoods 分店订购方案具体的货品订购信息
 * @date 2020/8/13 13:33
 */
public class ShopOrderPlanGoods implements Serializable {
    private int id;//id
    private int shop_plan_id;//方案编号
    private int goods_id;//订购的货品编号
    private String goods_name;//订购的货品名称
    private float order_num;//订购数量

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShop_plan_id() {
        return shop_plan_id;
    }

    public void setShop_plan_id(int shop_plan_id) {
        this.shop_plan_id = shop_plan_id;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public float getOrder_num() {
        return order_num;
    }

    public void setOrder_num(float order_num) {
        this.order_num = order_num;
    }

    @Override
    public String
    toString() {
        return "ShopOrderPlanGoods{" +
                "id=" + id +
                ", shop_plan_id=" + shop_plan_id +
                ", goods_id=" + goods_id +
                ", goods_name='" + goods_name + '\'' +
                ", order_num=" + order_num +
                '}';
    }
}
