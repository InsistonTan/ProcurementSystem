package com.huiduoduo.ProcurementSystem.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author TanJifeng
 * @Description ShopOrder 分店订单信息
 * @date 2020/8/13 13:53
 */
public class ShopOrder implements Serializable {
    private String order_id;//订单编号
    private int shop_id;//所属分店id
    private String shop_name;//所属分店名称
    private String principal;//制单人姓名
    private String order_status;//订单状态
    private String start_time;//开始时间
    private String end_time;//结束时间
    private int approved;//采购经理审批，-1为不同意，0为同意但还没生成单品采购单，1为以生成单品采购单
    private String shop_note;//分店备注
    private String manager_note;//采购经理备注
    private String manager;//处理该订单的采购经理姓名
    private List<GoodsOrder> goods_order;//具体的订购货品信息

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
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

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public String getShop_note() {
        return shop_note;
    }

    public void setShop_note(String shop_note) {
        this.shop_note = shop_note;
    }

    public String getManager_note() {
        return manager_note;
    }

    public void setManager_note(String manager_note) {
        this.manager_note = manager_note;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public List<GoodsOrder> getGoods_order() {
        return goods_order;
    }

    public void setGoods_order(List<GoodsOrder> goods_order) {
        this.goods_order = goods_order;
    }

    @Override
    public String toString() {
        return "ShopOrder{" +
                "order_id='" + order_id + '\'' +
                ", shop_id=" + shop_id +
                ", shop_name='" + shop_name + '\'' +
                ", principal='" + principal + '\'' +
                ", order_status='" + order_status + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", approved=" + approved +
                ", shop_note='" + shop_note + '\'' +
                ", manager_note='" + manager_note + '\'' +
                ", manager='" + manager + '\'' +
                ", goods_order=" + goods_order +
                '}';
    }
}
