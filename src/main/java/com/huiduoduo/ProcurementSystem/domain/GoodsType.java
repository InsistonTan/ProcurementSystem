package com.huiduoduo.ProcurementSystem.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author TanJifeng
 * @Description GoodsType 货品类型
 * @date 2020/8/13 13:26
 */
@Data
public class GoodsType implements Serializable {
    private Integer id;//类型编号
    private Integer type_id;//类型编号
    private String type_name;//类型名称
}
