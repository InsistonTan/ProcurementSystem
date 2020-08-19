package com.huiduoduo.ProcurementSystem.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author TanJifeng
 * @Description Shop 分店信息
 * @date 2020/8/13 13:28
 */
@Data
public class Shop implements Serializable {
    private Integer shop_id;//门店编号
    private String shop_name;//名称
    private String shop_position;//地址
    private String shop_phone;//联系电话
}
