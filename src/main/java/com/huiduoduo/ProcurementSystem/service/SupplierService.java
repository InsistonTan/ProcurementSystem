package com.huiduoduo.ProcurementSystem.service;


import com.huiduoduo.ProcurementSystem.domain.Supplier;
import com.huiduoduo.ProcurementSystem.domain.pageBean.SearchPage;

import java.util.List;
import java.util.Map;

/**
 * @author WJQ
 * @since 2020-8-16 14:05
 */
public interface SupplierService {
    //查看所有供应商
    Map getAllSupplier(SearchPage searchPage);

    //增加供应商
    Map addSupplier(Supplier supplier);

    //修改供应商
    Map updateSupplier(Supplier supplier);

    //删除供应商
    Map deleteSupplier(Supplier supplier);
}
