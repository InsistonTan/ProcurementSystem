package com.huiduoduo.procurement_system.domain;

import java.io.Serializable;

/**
 * @author TanJifeng
 * @Description BuyPlanBuyer
 * @date 2020/8/13 13:16
 */
public class BuyPlanBuyer implements Serializable {
    private int id;//主键id
    private int buy_plan_id;//分配计划id
    private int goods_id;//货品id
    private String goods_name;//货品名称
    private String buyer_username;//采购员账号
    private String name;//采购员名字

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBuy_plan_id() {
        return buy_plan_id;
    }

    public void setBuy_plan_id(int buy_plan_id) {
        this.buy_plan_id = buy_plan_id;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getBuyer_username() {
        return buyer_username;
    }

    public void setBuyer_username(String buyer_username) {
        this.buyer_username = buyer_username;
    }

    @Override
    public String toString() {
        return "BuyPlanBuyer{" +
                "id=" + id +
                ", buy_plan_id=" + buy_plan_id +
                ", goods_id=" + goods_id +
                ", goods_name='" + goods_name + '\'' +
                ", buyer_username='" + buyer_username + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
