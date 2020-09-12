package com.huiduoduo.ProcurementSystem.service;

import com.huiduoduo.ProcurementSystem.domain.Goods;
import com.huiduoduo.ProcurementSystem.domain.pageBean.GoodsPage;

import java.util.List;
import java.util.Map;

/**
 * @author WJQ
 * @since 2020-8-16 14:05
 */
public interface GoodsService {
    //查看所有货品
    Map getAllGoods(GoodsPage goodsPage);

    //增加货品
    Map addGoods(Goods goods);

    //修改货品
    Map updateGoods(Goods goods);

    //删除货品
    Map deleteGoods(Goods goods);



}
