package com.huiduoduo.ProcurementSystem.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author TanJifeng
 * @Description GoodsOrder 具体的订单订购的货品
 * @date 2020/8/13 13:45
 */
@Data
public class GoodsOrder implements Serializable {
    private Integer id;
    private String order_id;//所属分店订单编号
    private Integer shop_id;//所属分店号
    private String shop_name;//所属分店名称
    private String single_order_id;//所属单品采购单编号
    private Integer goods_id;//订购的货品编号
    private String goods_name;//订购的货品名称
    private float order_num;//订购数量
    private String order_unit;//订购单位
    private String rec_unit;//该货品预设置的采购单位
    private float goods_price;//实际采购的单价
    private float buy_num;//实际采购数量
    private String buy_unit;//采购员实际采购到的货品的单位
    private String goods_note;//备注
    private Integer goods_sort;//货品排序标志位
    private Integer goods_type_id;//货品类型id
    private String type_name;//货品类型名
    private Float total_money;//采购单价x采购数量=采购金额
}
