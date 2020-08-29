package com.huiduoduo.ProcurementSystem.dao;

import com.huiduoduo.ProcurementSystem.domain.Supplier;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author WJQ
 * @since 2020/8/16 17:44
 */
@Mapper
public interface SupplierDao {
    //查询所有供应商
    @Select("SELECT * FROM supplier")
    List<Supplier> selectAll();

    @Select("SELECT * FROM supplier WHERE supplier_name=#{supplier_name}")
    Supplier selectByName(String supplier_name);

    //增加供应商
    @Insert("INSERT INTO supplier(id,supplier_name,supplier_phone,note) " +
            "VALUES(#{id},#{supplier_name},#{supplier_phone},#{note})")
    boolean add(Supplier supplier);

    //(根据id)更新供应商
    @Update("UPDATE supplier SET supplier_name=#{supplier_name}," +
            "supplier_phone=#{supplier_phone} note=#{note} WHERE supplier_id=#{supplier_id}")
    boolean update(Supplier supplier);

    //(根据id)删除供应商
    @Delete("DELETE FROM supplier WHERE id=#{id}")
    boolean delete(Integer supplier_id);


}
