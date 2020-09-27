package com.huiduoduo.ProcurementSystem.service;

import com.huiduoduo.ProcurementSystem.domain.ShopOrder;
import com.huiduoduo.ProcurementSystem.domain.SingleGoodsOrder;
import com.huiduoduo.ProcurementSystem.domain.pageBean.SingleOrderPage;

import java.util.List;
import java.util.Map;

/**
 * @author TanJifeng
 * @Description SingleGoodsOrderService 单品采购单service
 * @date 2020/8/28 20:44
 */
public interface SingleGoodsOrderService {
    //添加
    Map addSingleOrder();

    //（采购经理）修改单品采购单-分配采购单给采购员
    Map distribute(List<SingleGoodsOrder> orders);

    //（采购员）修改订单状态
    Map updateStatus(SingleGoodsOrder order);

    //（采购员）更新实际的采购结果
    Map updateBuyRes(SingleGoodsOrder order);

    //删除
    Map delete(SingleGoodsOrder order);

    //查询历史单品采购单
    Map selectHistory(SingleOrderPage page);

    //（采购经理）查询正在进行的单品采购单
    Map selectOngoing(SingleOrderPage page);
}
