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
    @Select("select shop_order.*,shop.shop_name from shop_order,shop " +
            "where order_id=#{id} and shop.shop_id=shop_order.shop_id")
    ShopOrder selectOneByID(@Param("id") String id);

    //选择某个门店的历史订单
    @Select("select shop_order.*,shop.shop_name " +
            "from shop_order,shop " +
            "where end_time IS NOT NULL and shop_order.`shop_id`=#{id} " +
            "and order_id like '%${key}%' " + //模糊搜索 order_id
            "${condition} " + //condition为筛选的时间条件
            "and shop.shop_id=shop_order.shop_id " +
            "order by order_id ${sort}")
    List<ShopOrder> selectHistoryByShopID(@Param("id") int shop_id,@Param("condition")String condition,@Param("key") String key,@Param("sort") String sort);

    //选择所有门店的历史订单
    @Select("select shop_order.*,shop.shop_name " +
            "from shop_order,shop " +
            "where end_time IS NOT NULL " +
            "and (order_id like '%${key}%' " +//模糊搜索 order_id
            "or (shop.shop_name like '%${key}%' and shop_order.shop_id=shop.shop_id)) " + //模糊搜索 shop_name
            "${condition} " + //condition为筛选的时间条件
            "and shop.shop_id=shop_order.shop_id " +
            "order by order_id ${sort}") //sort为排序
    List<ShopOrder> selectAllHistory(@Param("condition")String condition,@Param("key") String key,@Param("sort") String sort);

    //选择某个门店的正在进行的订单
    @Select("select shop_order.*,shop.shop_name " +
            "from shop_order,shop " +
            "where end_time IS NULL and shop_order.`shop_id`=#{id} " +
            "and order_id like '%${key}%' " + //模糊搜索 order_id
            "and shop.shop_id=shop_order.shop_id " +
            "order by order_id ${sort}")
    List<ShopOrder> selectOnGoingByShopID(@Param("id") int shop_id,@Param("key") String key,@Param("sort") String sort);

    //选择所有门店的正在进行的订单
    @Select("select shop_order.*,shop.shop_name " +
            "from shop_order,shop " +
            "where end_time IS NULL " +
            "and (order_id like '%${key}%' " +//模糊搜索 order_id
            "or (shop.shop_name like '%${key}%' and shop_order.shop_id=shop.shop_id)) " + //模糊搜索 shop_name
            "and shop.shop_id=shop_order.shop_id " +
            "order by order_id ${sort}")
    List<ShopOrder> selectAllOnGoing(@Param("key") String key,@Param("sort") String sort);

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

    //选择所有批准但未生成单品采购单的分店订单号（approved=0则代表同意但未生成单品采购单，1代表生成了单品采购单，-1为拒绝）
    @Select("select order_id from shop_order where approved=0")
    List<ShopOrder> selectApproved();

    //选择某分店，某个日期的分店订单
    @Select("select order_id from shop_order " +
            "where shop_id=#{shop_id} and order_id like '${date}%'")
    List<ShopOrder> selectOneDayOrder(@Param("shop_id") int shop_id,@Param("date") String date);

    //统计待审批的分店订单数
    @Select("select COUNT(order_id) from shop_order " +
            "where approved IS NULL")
    int getUnDoSum();

    //统计没完成的某个分店的订单数
    @Select("select COUNT(order_id) from shop_order " +
            "where shop_id=#{shop_id} and end_time IS NULL")
    int getOnGoingSum(@Param("shop_id")int shop_id);

    //统计等待送货的分店数
    @Select("select COUNT(DISTINCT shop_id) from shop_order " +
            "where approved=1")
    int getWaitingShopSum();
}
