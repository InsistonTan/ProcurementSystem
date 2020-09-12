package com.huiduoduo.ProcurementSystem.dao;


import com.huiduoduo.ProcurementSystem.domain.Goods;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author WJQ
 * @since 2020/8/16 17:44
 */
@Mapper
public interface GoodsDao {
    //查询所有货品
    @Select("SELECT goods.*,type_name FROM goods LEFT JOIN goods_type ON goods_type_id=type_id " +
            "WHERE goods_id=#{search} OR goods_name LIKE CONCAT('%',#{search},'%')")
    List<Goods> selectAll(String search);

    //按货物类型分类查询
    @Select("SELECT goods.*,type_name FROM goods LEFT JOIN goods_type ON goods_type_id=type_id " +
            "WHERE (goods_id=#{search} OR goods_name LIKE CONCAT('%',#{search},'%')) " +
            "AND goods_type_id LIKE CONCAT(#{goods_type_id},'%')")
    List<Goods> selectAllByType(String search,Integer goods_type_id);


    @Select("SELECT * FROM goods WHERE goods_name=#{goods_name}")
    Goods selectByName(String goods_name);

    //增加货品
    @Insert("INSERT INTO goods(goods_id,goods_sort,goods_type_id,goods_name,order_unit,rec_unit,sale_unit) " +
            "VALUES(#{goods_id},#{goods_sort},#{goods_type_id},#{goods_name},#{order_unit},#{rec_unit},#{sale_unit})")
    boolean add(Goods goods);

    //(根据id)更新货品
    @Update("UPDATE goods SET goods_sort=#{goods_sort},goods_type_id=#{goods_type_id},goods_name=#{goods_name}," +
            "order_unit=#{order_unit},rec_unit=#{rec_unit},sale_unit=#{sale_unit} WHERE goods_id=#{goods_id}")
    boolean update(Goods goods);

    //(根据id)删除货品
    @Delete("DELETE FROM goods WHERE goods_id=#{goods_id}")
    boolean delete(Integer goods_id);


}
