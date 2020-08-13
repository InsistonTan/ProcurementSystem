package com.huiduoduo.procurement_system.domain;

import java.io.Serializable;

/**
 * @author TanJifeng
 * @Description Supplier 供应商信息
 * @date 2020/8/13 13:30
 */
public class Supplier implements Serializable {
    private int id;//供应商编号
    private String supplier_name;//名称
    private String supplier_phone;//电话
    private String note;//备注

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getSupplier_phone() {
        return supplier_phone;
    }

    public void setSupplier_phone(String supplier_phone) {
        this.supplier_phone = supplier_phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "id=" + id +
                ", supplier_name='" + supplier_name + '\'' +
                ", supplier_phone='" + supplier_phone + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
