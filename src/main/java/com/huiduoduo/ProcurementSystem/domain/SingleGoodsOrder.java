package com.huiduoduo.ProcurementSystem.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author TanJifeng
 * @Description SingleGoodsOrder 单品采购单信息
 * @date 2020/8/13 14:19
 */
public class SingleGoodsOrder implements Serializable {
    private String single_order_id;//单品采购单编号
    private int goods_id;//货品编号
    private String goods_name;//货品名称
    private float order_goods_num;//订购总数量
    private String manager_note;//采购经理备注
    private String buyer_note;//采购员备注
    private String buy_status;//订单状态
    private String start_time;//开始时间
    private String end_time;//结束时间
    private String manager;//处理该订单的采购经理姓名
    private String buyer_username;//负责该订单的采购员账号
    private String name;//采购员姓名
    private int supplier_id;//供应商编号
    private String supplier_name;//供应商名称
    private float total_money;//实际采购总金额
    private float buy_goods_num;//实际采购总数量
    private float buy_goods_price;//实际采购单价
    private String buy_goods_unit;//实际采购单位
    private List<GoodsOrder> goods_order;//具体的各分店货品订购信息

    public List<GoodsOrder> getGoods_order() {
        return goods_order;
    }

    public void setGoods_order(List<GoodsOrder> goods_order) {
        this.goods_order = goods_order;
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

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public float getOrder_goods_num() {
        return order_goods_num;
    }

    public void setOrder_goods_num(float order_goods_num) {
        this.order_goods_num = order_goods_num;
    }

    public String getManager_note() {
        return manager_note;
    }

    public void setManager_note(String manager_note) {
        this.manager_note = manager_note;
    }

    public String getBuyer_note() {
        return buyer_note;
    }

    public void setBuyer_note(String buyer_note) {
        this.buyer_note = buyer_note;
    }

    public String getBuy_status() {
        return buy_status;
    }

    public void setBuy_status(String buy_status) {
        this.buy_status = buy_status;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getBuyer_username() {
        return buyer_username;
    }

    public void setBuyer_username(String buyer_username) {
        this.buyer_username = buyer_username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public float getTotal_money() {
        return total_money;
    }

    public void setTotal_money(float total_money) {
        this.total_money = total_money;
    }

    public float getBuy_goods_num() {
        return buy_goods_num;
    }

    public void setBuy_goods_num(float buy_goods_num) {
        this.buy_goods_num = buy_goods_num;
    }

    public float getBuy_goods_price() {
        return buy_goods_price;
    }

    public void setBuy_goods_price(float buy_goods_price) {
        this.buy_goods_price = buy_goods_price;
    }

    public String getBuy_goods_unit() {
        return buy_goods_unit;
    }

    public void setBuy_goods_unit(String buy_goods_unit) {
        this.buy_goods_unit = buy_goods_unit;
    }

    @Override
    public String toString() {
        return "SingleGoodsOrder{" +
                "single_order_id='" + single_order_id + '\'' +
                ", goods_id=" + goods_id +
                ", goods_name='" + goods_name + '\'' +
                ", order_goods_num=" + order_goods_num +
                ", manager_note='" + manager_note + '\'' +
                ", buyer_note='" + buyer_note + '\'' +
                ", buy_status='" + buy_status + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", manager='" + manager + '\'' +
                ", buyer_username='" + buyer_username + '\'' +
                ", name='" + name + '\'' +
                ", supplier_id=" + supplier_id +
                ", supplier_name='" + supplier_name + '\'' +
                ", total_money=" + total_money +
                ", buy_goods_num=" + buy_goods_num +
                ", buy_goods_price=" + buy_goods_price +
                ", buy_goods_unit='" + buy_goods_unit + '\'' +
                ", goods_order=" + goods_order +
                '}';
    }
}
