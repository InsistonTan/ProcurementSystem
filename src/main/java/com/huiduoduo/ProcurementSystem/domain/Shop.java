package com.huiduoduo.ProcurementSystem.domain;

import java.io.Serializable;

/**
 * @author TanJifeng
 * @Description Shop 分店信息
 * @date 2020/8/13 13:28
 */
public class Shop implements Serializable {
    private int shop_id;//门店编号
    private String shop_name;//名称
    private String shop_position;//地址
    private String shop_phone;//联系电话

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

    public String getShop_position() {
        return shop_position;
    }

    public void setShop_position(String shop_position) {
        this.shop_position = shop_position;
    }

    public String getShop_phone() {
        return shop_phone;
    }

    public void setShop_phone(String shop_phone) {
        this.shop_phone = shop_phone;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "shop_id=" + shop_id +
                ", shop_name='" + shop_name + '\'' +
                ", shop_position='" + shop_position + '\'' +
                ", shop_phone='" + shop_phone + '\'' +
                '}';
    }
}
