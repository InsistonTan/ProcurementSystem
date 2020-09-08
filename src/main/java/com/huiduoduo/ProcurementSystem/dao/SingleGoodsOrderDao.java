package com.huiduoduo.ProcurementSystem.dao;

import com.huiduoduo.ProcurementSystem.domain.SingleGoodsOrder;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author TanJifeng
 * @Description SingleGoodsOrderDao 单品采购单 dao
 * @date 2020/8/26 22:32
 */
@Mapper
public interface SingleGoodsOrderDao {
    //通过采购单id选择采购单
    @Select("select * from single_goods_order where single_order_id=#{id}")
    SingleGoodsOrder selectOrderById(@Param("id") String id);

    //添加单品采购单
    @Insert("insert into single_goods_order(single_order_id,goods_id,order_goods_num," +
            "manager_note,buy_status,start_time,manager,supplier_id) " +
            "values(#{single_order_id},#{goods_id},#{order_goods_num},#{manager_note},#{buy_status},#{start_time},#{manager},#{supplier_id})")
    boolean addSingleOrder(SingleGoodsOrder singleGoodsOrder);

    //（采购经理）修改单品采购单-分配采购单给采购员
    @Update("update single_goods_order set " +
            "manager_note=#{manager_note},buyer_username=#{buyer_username} " +
            "where single_order_id=#{single_order_id}")
    boolean updateDistribution(SingleGoodsOrder singleGoodsOrder);

    //（采购员）修改订单状态
    @Update("update single_goods_order set buy_status=#{buy_status} " +
            "where single_order_id=#{single_order_id}")
    boolean updateStatus(SingleGoodsOrder singleGoodsOrder);

    //（采购员）修改实际的采购结果
    @Update("update single_goods_order set supplier_id=#{supplier_id},total_money=#{total_money}," +
            "buy_goods_num=#{buy_goods_num},buy_goods_price=#{buy_goods_price}," +
            "buy_goods_unit=#{buy_goods_unit},buy_status=#{buy_status},end_time=#{end_time} " +
            "where single_order_id=#{single_order_id}")
    boolean updateBuyRes(SingleGoodsOrder singleGoodsOrder);

    //删除
    @Delete("delete from single_goods_order where single_order_id=#{single_order_id}")
    boolean delete(SingleGoodsOrder singleGoodsOrder);

    //（采购经理）查询历史单品采购单
    @Select("select single_goods_order.*,goods.goods_name,supplier.supplier_name,account.name " +
            "from (single_goods_order " +
            "left join supplier on single_goods_order.supplier_id=supplier.id " +
            "left join account on single_goods_order.buyer_username=account.username)" +
            ",goods " +
            "where start_time IS NOT NULL and end_time IS NOT NULL " +
            "and goods.goods_id=single_goods_order.goods_id " +
            "order by end_time desc")
    List<SingleGoodsOrder> selectHistoryManager();

    //（采购员）查询历史单品采购单
    @Select("select single_goods_order.*,goods.goods_name,supplier.supplier_name,account.name " +
            "from (single_goods_order " +
            "left join supplier on single_goods_order.supplier_id=supplier.id " +
            "left join account on single_goods_order.buyer_username=account.username)" +
            ",goods " +
            "where buyer_username=#{buyer_username} " +
            "start_time IS NOT NULL and end_time IS NOT NULL " +
            "and goods.goods_id=single_goods_order.goods_id " +
            "order by end_time desc")
    List<SingleGoodsOrder> selectHistoryBuyer(@Param("buyer_username") String buyer_username);

    //（采购经理）查询正在进行的单品采购单
    @Select("select single_goods_order.*,goods.goods_name,supplier.supplier_name,account.name " +
            "from (single_goods_order " +
            "left join supplier on single_goods_order.supplier_id=supplier.id " +
            "left join account on single_goods_order.buyer_username=account.username)" +
            ",goods " +
            "where start_time IS NOT NULL and end_time IS NULL " +
            "and goods.goods_id=single_goods_order.goods_id " +
            "order by start_time desc")
    List<SingleGoodsOrder> selectOngoingManager();

    //（采购员）查询正在进行的单品采购单
    @Select("select single_goods_order.*,goods.goods_name,supplier.supplier_name,account.name " +
            "from (single_goods_order " +
            "left join supplier on single_goods_order.supplier_id=supplier.id " +
            "left join account on single_goods_order.buyer_username=account.username)" +
            ",goods " +
            "where buyer_username=#{buyer_username} " +
            "start_time IS NOT NULL and end_time IS NULL " +
            "and goods.goods_id=single_goods_order.goods_id " +
            "order by start_time desc")
    List<SingleGoodsOrder> selectOngoingBuyer(@Param("buyer_username") String buyer_username);
}