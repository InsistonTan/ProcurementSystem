package com.huiduoduo.ProcurementSystem.service.impl;


import com.huiduoduo.ProcurementSystem.dao.SupplierDao;
import com.huiduoduo.ProcurementSystem.domain.Supplier;
import com.huiduoduo.ProcurementSystem.service.SupplierService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author WJQ
 * @since 2020-8-16 14:09
 */
@Service
public class SupplierServiceImpl implements SupplierService {
    @Resource
    private SupplierDao supplierDao;


    @Override
    public List<Supplier> getAllSupplier() {

        return supplierDao.selectAll();
    }

    @Override
    public Map addSupplier(Supplier supplier) {
        if(supplier.getId() == null || supplier.getId() <= 0)
            return ResultUtil.getErrorRes("添加失败:请输入正确的id");

        if(supplierDao.selectByName(supplier.getSupplier_name()) != null)
            return ResultUtil.getErrorRes("添加失败:名字不能重复");

        if(supplierDao.add(supplier) == true)
            return ResultUtil.getSuccessRes();
        else
            return ResultUtil.getErrorRes("添加失败");
    }

    @Override
    public Map updateSupplier(Supplier supplier) {
        if(supplier.getId() == null || supplier.getId() <= 0)
            return ResultUtil.getErrorRes("修改失败:请输入正确的id");

        if(supplierDao.update(supplier) == true)
            return ResultUtil.getSuccessRes();
        else
            return ResultUtil.getErrorRes("修改失败");
    }

    @Override
    public Map deleteSupplier(Supplier supplier) {
        if(supplier.getId() == null || supplier.getId() <= 0)
            return ResultUtil.getErrorRes("删除失败:请输入正确的id");

        if(supplierDao.delete(supplier.getId()) == true)
            return ResultUtil.getSuccessRes();
        else
            return ResultUtil.getErrorRes("删除失败");
    }

}
