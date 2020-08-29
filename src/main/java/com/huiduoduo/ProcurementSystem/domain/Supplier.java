package com.huiduoduo.ProcurementSystem.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author TanJifeng
 * @Description Supplier 供应商信息
 * @date 2020/8/13 13:30
 */
@Data
public class Supplier implements Serializable {
    private Integer id;//供应商编号
    private String supplier_name;//名称
    private String supplier_phone;//电话
    private String note;//备注
}
