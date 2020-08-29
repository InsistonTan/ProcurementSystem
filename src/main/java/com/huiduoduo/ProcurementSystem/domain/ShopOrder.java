package com.huiduoduo.ProcurementSystem.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author TanJifeng
 * @Description ShopOrder 分店订单信息
 * @date 2020/8/13 13:53
 */
@Data
public class ShopOrder implements Serializable {
    private String order_id;//订单编号
    private Integer shop_id;//所属分店id
    private String shop_name;//所属分店名称
    private String principal;//制单人姓名
    private String order_status;//订单状态
    private String start_time;//开始时间
    private String end_time;//结束时间
    private Integer approved;//采购经理审批，-1为不同意，0为同意但还没生成单品采购单，1为以生成单品采购单
    private String shop_note;//分店备注
    private String manager_note;//采购经理备注
    private String manager;//处理该订单的采购经理姓名
    private List<GoodsOrder> goods_order;//具体的订购货品信息
}
