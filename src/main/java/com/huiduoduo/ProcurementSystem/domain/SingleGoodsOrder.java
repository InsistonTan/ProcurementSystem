package com.huiduoduo.ProcurementSystem.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author TanJifeng
 * @Description SingleGoodsOrder 单品采购单信息
 * @date 2020/8/13 14:19
 */
@Data
public class SingleGoodsOrder implements Serializable {
    private String single_order_id;//单品采购单编号
    private Integer goods_id;//货品编号
    private String goods_name;//货品名称
    private float order_goods_num;//订购总数量
    private String order_unit;//订购单位
    private String manager_note;//采购经理备注
    private String buyer_note;//采购员备注
    private String buy_status;//订单状态
    private String start_time;//开始时间
    private String end_time;//结束时间
    private String manager;//处理该订单的采购经理姓名
    private String buyer_username;//负责该订单的采购员账号
    private String name;//负责该订单的采购员姓名
    private Integer supplier_id;//供应商编号
    private String supplier_name;//供应商名称
    private float total_money;//实际采购总金额
    private float buy_goods_num;//实际采购总数量
    private float buy_goods_price;//实际采购单价
    private String buy_goods_unit;//实际采购单位
    private List<GoodsOrder> goods_order;//具体的各分店货品订购信息
}
