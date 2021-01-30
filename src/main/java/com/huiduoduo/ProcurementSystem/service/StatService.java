package com.huiduoduo.ProcurementSystem.service;

import java.util.Map;

/**
 * @author TanJifeng
 * @Description 用于处理一些前端的要求
 * @date 2020/10/1 14:06
 */

public interface StatService {
    //获取最近7天的每天的金额（分店权限：分店订单的金额，采购经理：单品的金额）
    Map getRecentMoney();

    //获取待处理订单数
    Map getToDoNum();

    //获取分店员工数
    Map getShoperNum();

    //获取今日采购总数
    Map getBuyTotal();

    //获取正在采购的采购员数量
    Map getBuying_man_num();

    //获取等待送货的分店数
    Map getWaiting_shop_num();

    //获取当天各分店的订单提交数
    Map getTodayOrderNum();
}
