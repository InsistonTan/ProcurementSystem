package com.huiduoduo.ProcurementSystem.domain;

import java.io.Serializable;

/**
 * @author TanJifeng
 * @Description Account 账号信息
 * @date 2020/8/13 13:10
 */
public class Account implements Serializable {
    private String username;//账号
    private String password;//密码
    private String role;//职务
    private int shop_id;//所属分店id
    private String gender;//性别
    private String name;//真实姓名
    private String phone;//电话
    private String addr;//住址
    private String id_card;//身份证

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", shop_id=" + shop_id +
                ", gender='" + gender + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", addr='" + addr + '\'' +
                ", id_card='" + id_card + '\'' +
                '}';
    }
}
