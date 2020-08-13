package com.huiduoduo.procurement_system.domain;

import java.io.Serializable;

/**
 * @author TanJifeng
 * @Description Goods 货品信息
 * @date 2020/8/13 13:19
 */
public class Goods implements Serializable {
    private int goods_id;//货品编号
    private int goods_sort;//排序标志
    private int goods_type_id;//货品类型编号
    private String type_name;//类型名称
    private String goods_name;//货品名称
    private String order_unit;//订购单位
    private String rec_unit;//收货单位
    private String sale_unit;//销售单位

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public int getGoods_sort() {
        return goods_sort;
    }

    public void setGoods_sort(int goods_sort) {
        this.goods_sort = goods_sort;
    }

    public int getGoods_type_id() {
        return goods_type_id;
    }

    public void setGoods_type_id(int goods_type_id) {
        this.goods_type_id = goods_type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getOrder_unit() {
        return order_unit;
    }

    public void setOrder_unit(String order_unit) {
        this.order_unit = order_unit;
    }

    public String getRec_unit() {
        return rec_unit;
    }

    public void setRec_unit(String rec_unit) {
        this.rec_unit = rec_unit;
    }

    public String getSale_unit() {
        return sale_unit;
    }

    public void setSale_unit(String sale_unit) {
        this.sale_unit = sale_unit;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "goods_id=" + goods_id +
                ", goods_sort=" + goods_sort +
                ", goods_type_id=" + goods_type_id +
                ", type_name='" + type_name + '\'' +
                ", goods_name='" + goods_name + '\'' +
                ", order_unit='" + order_unit + '\'' +
                ", rec_unit='" + rec_unit + '\'' +
                ", sale_unit='" + sale_unit + '\'' +
                '}';
    }
}
