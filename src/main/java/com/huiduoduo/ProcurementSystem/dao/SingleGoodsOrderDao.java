package com.huiduoduo.ProcurementSystem.dao;

import com.huiduoduo.ProcurementSystem.domain.SingleGoodsOrder;
import org.apache.ibatis.annotations.*;

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
            "manager_note,buy_status,start_time,manager,supplier_id,buy_goods_unit) " +
            "values(#{single_order_id},#{goods_id},#{order_goods_num},#{manager_note},#{buy_status},#{start_time},#{manager},#{supplier_id},#{buy_goods_unit})")
    boolean addSingleOrder(SingleGoodsOrder singleGoodsOrder);

    //（采购经理）修改经理备注
    @Update("update single_goods_order set " +
            "manager_note=#{manager_note} " +
            "where single_order_id=#{single_order_id}")
    boolean updateNote(SingleGoodsOrder singleGoodsOrder);

    //（采购经理）修改单品采购单-分配采购单给采购员
    @Update("update single_goods_order set " +
            "manager_note=#{manager_note},buyer_username=#{buyer_username},buy_status=#{buy_status} " +
            "where single_order_id=#{single_order_id}")
    boolean updateDistribution(SingleGoodsOrder singleGoodsOrder);

    //（采购员）修改订单状态
    @Update("update single_goods_order set buy_status=#{buy_status} " +
            "where single_order_id=#{single_order_id}")
    boolean updateStatus(SingleGoodsOrder singleGoodsOrder);

    //（采购员）修改实际的采购结果
    @Update("update single_goods_order set supplier_id=#{supplier_id},total_money=#{total_money}," +
            "buy_goods_num=#{buy_goods_num},buy_goods_price=#{buy_goods_price}," +
            "buy_goods_unit=#{buy_goods_unit},buyer_note=#{buyer_note} " +
            "where single_order_id=#{single_order_id}")
    boolean updateBuyRes(SingleGoodsOrder singleGoodsOrder);

    //采购员确认完成采购
    @Update("update single_goods_order set buy_status=#{buy_status},end_time=#{end_time} " +
            "where single_order_id=#{single_order_id}")
    boolean finish(SingleGoodsOrder order);

    //删除
    @Delete("delete from single_goods_order where single_order_id=#{single_order_id}")
    boolean delete(SingleGoodsOrder singleGoodsOrder);

    //（采购经理）查询历史单品采购单
    @Select("select single_goods_order.*,goods.goods_name,goods.order_unit,supplier.supplier_name,account.name " +
            "from (single_goods_order " +
            "left join supplier on single_goods_order.supplier_id=supplier.id " +
            "left join account on single_goods_order.buyer_username=account.username)" +
            ",goods " +
            "where start_time IS NOT NULL and end_time IS NOT NULL " +
            //模糊搜索
            "and (single_order_id like '%${key}%' " +
            "      or (goods.goods_name like '%${key}%' and single_goods_order.goods_id=goods.goods_id) " +
            "      or single_goods_order.manager like '%${key}%' " +
            "      or buyer_username like '%${key}%'" +
            "     ) " +
            //condition包括时间条件，供应商条件
            "${condition} " +
            "and goods.goods_id=single_goods_order.goods_id " +
            //
            "order by single_order_id ${sort}")
    List<SingleGoodsOrder> selectHistoryManager(@Param("condition")String condition,@Param("key") String key,@Param("sort")String sort);

    //（采购员）查询历史单品采购单
    @Select("select single_goods_order.*,goods.goods_name,goods.order_unit,supplier.supplier_name,account.name " +
            "from (single_goods_order " +
            "left join supplier on single_goods_order.supplier_id=supplier.id " +
            "left join account on single_goods_order.buyer_username=account.username)" +
            ",goods " +
            "where buyer_username=#{buyer_username} " +
            " and start_time IS NOT NULL and end_time IS NOT NULL " +
            //模糊搜索
            "and (single_order_id like '%${key}%' " +
            "      or (goods.goods_name like '%${key}%' and single_goods_order.goods_id=goods.goods_id) " +
            "      or single_goods_order.manager like '%${key}%' " +
            "      or buyer_username like '%${key}%'" +
            "     ) " +
            //condition包括时间条件，供应商条件
            "${condition} " +
            "and goods.goods_id=single_goods_order.goods_id " +
            //
            "order by single_order_id ${sort}")
    List<SingleGoodsOrder> selectHistoryBuyer(@Param("buyer_username") String buyer_username,@Param("condition")String condition,@Param("key") String key,@Param("sort")String sort);

    //（采购经理）查询正在进行的单品采购单
    @Select("select single_goods_order.*,goods.goods_name,goods.order_unit,supplier.supplier_name,account.name " +
            "from (single_goods_order " +
            "left join supplier on single_goods_order.supplier_id=supplier.id " +
            "left join account on single_goods_order.buyer_username=account.username)" +
            ",goods " +
            "where start_time IS NOT NULL and end_time IS NULL " +
            //模糊搜索
            "and (single_order_id like '%${key}%' " +
            "      or (goods.goods_name like '%${key}%' and single_goods_order.goods_id=goods.goods_id) " +
            "      or single_goods_order.manager like '%${key}%' " +
            "      or buyer_username like '%${key}%'" +
            "     ) " +
            "and goods.goods_id=single_goods_order.goods_id " +
            "order by single_order_id ${sort}")
    List<SingleGoodsOrder> selectOngoingManager(@Param("key") String key,@Param("sort")String sort);

    //（采购员）查询正在进行的单品采购单
    @Select("select single_goods_order.*,goods.goods_name,goods.order_unit,supplier.supplier_name,account.name " +
            "from (single_goods_order " +
            "left join supplier on single_goods_order.supplier_id=supplier.id " +
            "left join account on single_goods_order.buyer_username=account.username)" +
            ",goods " +
            "where buyer_username=#{buyer_username} " +
            "and start_time IS NOT NULL and end_time IS NULL " +
            //模糊搜索
            "and (single_order_id like '%${key}%' " +
            "      or (goods.goods_name like '%${key}%' and single_goods_order.goods_id=goods.goods_id) " +
            "      or single_goods_order.manager like '%${key}%' " +
            "      or buyer_username like '%${key}%'" +
            "     ) " +
            "and goods.goods_id=single_goods_order.goods_id " +
            "order by single_order_id ${sort}")
    List<SingleGoodsOrder> selectOngoingBuyer(@Param("buyer_username") String buyer_username,@Param("key") String key,@Param("sort")String sort);

    //查询某个日期的历史单品订单的采购金额
    @Select("select single_order_id,total_money from single_goods_order " +
            "where single_order_id like '${date}%'")
    List<SingleGoodsOrder> selectOneDayHistory(@Param("date") String date);

    //统计所有未完成的采购单数
    @Select("select COUNT(single_order_id) from single_goods_order " +
            "where end_time IS NULL")
    int getAllOnGoingSum();

    //统计所有未分配采购员的采购单数
    @Select("select COUNT(single_order_id) from single_goods_order " +
            "where end_time IS NULL and (buyer_username IS NULL or buyer_username='') ")
    int getAllDontHaveBuyer();

    //统计某个采购员未完成的采购单数
    @Select("select COUNT(single_order_id) from single_goods_order " +
            "where buyer_username=#{username} and end_time IS NULL ")
    int getOneBuyerOnGoingSum(@Param("username") String username);
}
