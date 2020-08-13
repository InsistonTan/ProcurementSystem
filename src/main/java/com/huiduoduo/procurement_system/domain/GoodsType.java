package com.huiduoduo.procurement_system.domain;

import java.io.Serializable;

/**
 * @author TanJifeng
 * @Description GoodsType 货品类型
 * @date 2020/8/13 13:26
 */
public class GoodsType implements Serializable {
    private int id;//类型编号
    private String type_name;//类型名称

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    @Override
    public String toString() {
        return "GoodsType{" +
                "id=" + id +
                ", type_name='" + type_name + '\'' +
                '}';
    }
}
