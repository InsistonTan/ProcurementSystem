package com.huiduoduo.ProcurementSystem.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author TanJifeng
 * @Description BuyPlanBuyer 采购经理的单品分配方案的具体分配
 * @date 2020/8/13 13:16
 */
@Data
public class BuyPlanBuyer implements Serializable {
    private int id;//主键id
    private int buy_plan_id;//分配计划idprivate int goods_id;//货品id
    private String goods_name;//货品名称
    private String buyer_username;//采购员账号
    private String name;//采购员名字

}
