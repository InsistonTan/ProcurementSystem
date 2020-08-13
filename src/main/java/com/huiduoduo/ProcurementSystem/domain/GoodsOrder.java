package com.huiduoduo.ProcurementSystem.domain;

import java.io.Serializable;

/**
 * @author TanJifeng
 * @Description GoodsOrder 具体的订单订购的货品
 * @date 2020/8/13 13:45
 */
public class GoodsOrder implements Serializable {
    private int id;
    private String order_id;//所属分店订单编号
    private String single_order_id;//所属单品采购单编号
    private int goods_id;//订购的货品编号
    private float order_num;//订购数量
    private float goods_price;//实际采购的单价
    private float buy_num;//实际采购数量
    private String buy_unit;//实际采购的单位
    private String goods_note;//备注

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getSingle_order_id() {
        return single_order_id;
    }

    public void setSingle_order_id(String single_order_id) {
        this.single_order_id = single_order_id;
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

    public float getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(float goods_price) {
        this.goods_price = goods_price;
    }

    public float getBuy_num() {
        return buy_num;
    }

    public void setBuy_num(float buy_num) {
        this.buy_num = buy_num;
    }

    public String getBuy_unit() {
        return buy_unit;
    }

    public void setBuy_unit(String buy_unit) {
        this.buy_unit = buy_unit;
    }

    public String getGoods_note() {
        return goods_note;
    }

    public void setGoods_note(String goods_note) {
        this.goods_note = goods_note;
    }

    @Override
    public String toString() {
        return "GoodsOrder{" +
                "id=" + id +
                ", order_id='" + order_id + '\'' +
                ", single_order_id='" + single_order_id + '\'' +
                ", goods_id=" + goods_id +
                ", order_num=" + order_num +
                ", goods_price=" + goods_price +
                ", buy_num=" + buy_num +
                ", buy_unit='" + buy_unit + '\'' +
                ", goods_note='" + goods_note + '\'' +
                '}';
    }
}
