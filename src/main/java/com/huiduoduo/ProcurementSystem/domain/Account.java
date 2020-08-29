package com.huiduoduo.ProcurementSystem.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author TanJifeng
 * @Description Account 账号信息
 * @date 2020/8/13 13:10
 */
@Data
public class Account implements Serializable {
    private String username;//账号
    private String password;//密码
    private String role;//职务
    private Integer shop_id;//所属分店id
    private String gender;//性别
    private String name;//真实姓名
    private String phone;//电话
    private String addr;//住址
    private String id_card;//身份证

}
