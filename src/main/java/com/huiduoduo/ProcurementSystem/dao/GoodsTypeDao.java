package com.huiduoduo.ProcurementSystem.dao;

import com.huiduoduo.ProcurementSystem.domain.GoodsType;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author WJQ
 * @since 2020/8/16 17:44
 */
@Mapper
public interface GoodsTypeDao {
    //查询所有货物类型
    @Select("SELECT * FROM goods_type")
    List<GoodsType> selectAll();

    @Select("SELECT * FROM goods_type WHERE type_name=#{type_name}")
    GoodsType selectByName(String goodsType_name);

    //增加货物类型
    @Insert("INSERT INTO goods_type(type_id,type_name) VALUES(#{type_id},#{type_name})")
    boolean add(GoodsType goodsType);

    //(根据id)更新货物类型
    @Update("UPDATE goods_type SET type_name=#{type_name} WHERE type_id=#{type_id}")
    boolean update(GoodsType goodsType);

    //(根据id)删除货物类型
    @Delete("DELETE FROM goods_type WHERE type_id=#{type_id}")
    boolean delete(Integer type_id);



}
