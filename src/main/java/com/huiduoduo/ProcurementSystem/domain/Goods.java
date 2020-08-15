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
    private int goods_id;//货品编号
    private int goods_sort;//排序标志
    private int goods_type_id;//货品类型编号
    private String type_name;//类型名称
    private String goods_name;//货品名称
    private String order_unit;//订购单位
    private String rec_unit;//收货单位
    private String sale_unit;//销售单位
}
