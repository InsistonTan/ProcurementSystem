package com.huiduoduo.ProcurementSystem.dao;


import com.huiduoduo.ProcurementSystem.domain.Shop;
import org.apache.ibatis.annotations.*;


import java.util.List;

/**
 * @author WJQ
 * @since 2020/8/16 17:44
 */
@Mapper
public interface ShopDao {

    //查询所有门店
    @Select("SELECT * FROM shop WHERE shop_id=#{search} OR shop_name LIKE CONCAT('%',#{search},'%')")
    List<Shop> selectAll(String search);

    @Select("SELECT * FROM shop WHERE shop_name=#{shop_name}")
    Shop selectByName(String shop_name);

    //增加门店
    @Insert("INSERT INTO shop(shop_id,shop_name,shop_position,shop_phone) " +
            "VALUES(#{shop_id},#{shop_name},#{shop_position},#{shop_phone})")
    boolean add(Shop shop);

    //(根据id)更新门店
    @Update("UPDATE shop SET shop_name=#{shop_name},shop_position=#{shop_position}," +
            "shop_phone=#{shop_phone} WHERE shop_id=#{shop_id}")
    boolean update(Shop shop);

    //(根据id)删除门店
    @Delete("DELETE FROM shop WHERE shop_id=#{shop_id}")
    boolean delete(Integer shop_id);







}
