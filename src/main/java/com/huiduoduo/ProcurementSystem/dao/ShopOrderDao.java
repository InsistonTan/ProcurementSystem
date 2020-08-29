package com.huiduoduo.ProcurementSystem.dao;

import com.huiduoduo.ProcurementSystem.domain.ShopOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author TanJifeng
 * @Description ShopOrderDao
 * @date 2020/8/18 19:54
 */
@Mapper
public interface ShopOrderDao {
    //新建分店订单
    @Insert("INSERT INTO shop_order(order_id,shop_id,principal,order_status,start_time,shop_note) " +
            "VALUES(#{order_id},#{shop_id},#{principal},#{order_status},#{start_time},#{shop_note})")
    boolean addOrder(ShopOrder shopOrder);

    //以订单 id选择一个订单
    @Select("select * from shop_order where order_id=#{id}")
    ShopOrder selectOneByID(@Param("id") String id);

    //选择某个门店的历史订单
    @Select("select shop_order.*,shop.shop_name " +
            "from shop_order,shop " +
            "where shop_order.`shop_id`=#{id} and start_time IS NOT NULL and end_time IS NOT NULL " +
            "and shop.shop_id=shop_order.shop_id " +
            "order by end_time desc")
    List<ShopOrder> selectHistoryByShopID(@Param("id") int shop_id);

    //选择所有门店的历史订单
    @Select("select shop_order.*,shop.shop_name " +
            "from shop_order,shop " +
            "where start_time IS NOT NULL and end_time IS NOT NULL and shop.shop_id=shop_order.shop_id " +
            "order by end_time desc")
    List<ShopOrder> selectAllHistory();

    //选择某个门店的正在进行的订单
    @Select("select shop_order.*,shop.shop_name " +
            "from shop_order,shop " +
            "where shop_order.`shop_id`=#{id} and start_time IS NOT NULL and end_time IS NULL " +
            "and shop.shop_id=shop_order.shop_id " +
            "order by start_time desc")
    List<ShopOrder> selectOnGoingByShopID(@Param("id") int shop_id);

    //选择所有门店的正在进行的订单
    @Select("select shop_order.*,shop.shop_name " +
            "from shop_order,shop " +
            "where and start_time IS NOT NULL and end_time IS NULL and shop.shop_id=shop_order.shop_id " +
            "order by start_time desc")
    List<ShopOrder> selectAllOnGoing();

    //（分店）修改订单信息
    @Update("update shop_order set shop_note=#{shop_note} " +
            "where order_id=#{order_id}")
    boolean updateOrderByShop(ShopOrder shopOrder);

    //（采购经理）修改订单信息
    @Update("update shop_order set " +
            "approved=#{approved},manager_note=#{manager_note},manager=#{manager},end_time=#{end_time},order_status=#{order_status} " +
            "where order_id=#{order_id}")
    boolean updateOrderByManager(ShopOrder shopOrder);

    //（分店）确认完成订单
    @Update("update shop_order set order_status=#{order_status},end_time=#{end_time} " +
            "where order_id=#{order_id}")
    boolean confirmOrder(ShopOrder shopOrder);

    //删除订单
    @Delete("delete from shop_order where order_id=#{order_id}")
    boolean deleteOrder(ShopOrder shopOrder);
}
