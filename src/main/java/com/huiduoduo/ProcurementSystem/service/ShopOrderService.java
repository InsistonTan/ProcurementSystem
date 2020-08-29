package com.huiduoduo.ProcurementSystem.service;

import com.huiduoduo.ProcurementSystem.domain.ShopOrder;

import java.util.List;
import java.util.Map;

/**
 * @author TanJifeng
 * @Description ShopOrderService interface
 * @date 2020/8/19 15:55
 */
public interface ShopOrderService {
    //新增分店订单
    Map addShopOrder(ShopOrder shopOrder);
    //修改分店订单
    Map updateShopOrder(ShopOrder shopOrder);
    //采购经理审核订单
    Map approved(List<ShopOrder> shopOrders);
    //删除
    Map deleteShopOrder(ShopOrder shopOrder);
    //查看历史订单
    Map getHistory();
    //查看正在进行的订单
    Map getOngoing();
    //确认完成订单
    Map confirmShopOrder(ShopOrder shopOrder);
}
