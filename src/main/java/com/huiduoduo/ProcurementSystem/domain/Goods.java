package com.huiduoduo.ProcurementSystem.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author TanJifeng
 * @Description Goods 货品信息
 * @date 2020/8/13 13:19
 */
@Data
public class Goods implements Serializable {
    private Integer goods_id;//货品编号
    private Integer goods_sort;//排序标志
    private Integer goods_type_id;//货品类型编号
    private String type_name;//类型名称
    private String goods_name;//货品名称
    private String order_unit;//订购单位
    private String rec_unit;//收货单位
    private String sale_unit;//销售单位
    private Boolean can_order;//可不可订购
    private String desc;//备注
}
