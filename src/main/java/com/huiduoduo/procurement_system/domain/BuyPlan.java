package com.huiduoduo.procurement_system.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author TanJifeng
 * @Description BuyPlan
 * @date 2020/8/13 13:14
 */
public class BuyPlan implements Serializable {
    private int id;//主键 id
    private String manager_username;//采购经理账号
    private String name;//经理名称
    private String plan_name;//方案名称
    private List<BuyPlanBuyer> buy_plan_buyer;//具体的分配列表

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BuyPlanBuyer> getBuy_plan_buyer() {
        return buy_plan_buyer;
    }

    public void setBuy_plan_buyer(List<BuyPlanBuyer> buy_plan_buyer) {
        this.buy_plan_buyer = buy_plan_buyer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManager_username() {
        return manager_username;
    }

    public void setManager_username(String manager_username) {
        this.manager_username = manager_username;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    @Override
    public String toString() {
        return "BuyPlan{" +
                "id=" + id +
                ", manager_username='" + manager_username + '\'' +
                ", name='" + name + '\'' +
                ", plan_name='" + plan_name + '\'' +
                ", buy_plan_buyer=" + buy_plan_buyer +
                '}';
    }
}
