package com.huiduoduo.ProcurementSystem.service;

import com.huiduoduo.ProcurementSystem.domain.Shop;
import com.huiduoduo.ProcurementSystem.domain.pageBean.SearchPage;

import java.util.List;
import java.util.Map;

/**
 * @author WJQ
 * @since 2020-8-16 14:05
 */
public interface ShopService {
    //查看所有商店
    Map getAllShop(SearchPage searchPage);

    //增加商店
    Map addShop(Shop shop);

    //修改商店
    Map updateShop(Shop shop);

    //删除商店
    Map deleteShop(Shop shop);


}
