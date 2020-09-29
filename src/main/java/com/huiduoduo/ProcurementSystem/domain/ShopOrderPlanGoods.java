package com.huiduoduo.ProcurementSystem.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author TanJifeng
 * @Description ShopOrderPlanGoods 分店订购方案具体的货品订购信息
 * @date 2020/8/13 13:33
 */
@Data
public class ShopOrderPlanGoods implements Serializable {
    private Integer id;//id
    private Integer shop_plan_id;//方案编号
    private Integer goods_id;//订购的货品编号
    private String goods_name;//订购的货品名称
    private float order_num;//订购数量
    private String order_unit;//订购单位
}
