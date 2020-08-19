package com.huiduoduo.ProcurementSystem.service;

import com.huiduoduo.ProcurementSystem.domain.GoodsType;

import java.util.List;
import java.util.Map;

/**
 * @author WJQ
 * @since 2020-8-16 14:05
 */
public interface GoodsTypeService {
    //查看所有商店
    List getAllGoodsType();

    //增加商店
    Map addGoodsType(GoodsType goodsType);

    //修改商店
    Map updateGoodsType(GoodsType goodsType);

    //删除商店
    Map deleteGoodsType(GoodsType goodsType);


}
