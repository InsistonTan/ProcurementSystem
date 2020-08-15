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
    private int id;
    private String order_id;//所属分店订单编号
    private String single_order_id;//所属单品采购单编号
    private int goods_id;//订购的货品编号
    private float order_num;//订购数量
    private float goods_price;//实际采购的单价
    private float buy_num;//实际采购数量
    private String buy_unit;//实际采购的单位
    private String goods_note;//备注
}
