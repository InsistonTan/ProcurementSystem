package com.huiduoduo.ProcurementSystem.dao;

import com.huiduoduo.ProcurementSystem.domain.BuyPlanBuyer;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author TanJifeng
 * @Description
 * @date 2020/8/29 19:09
 */
@Mapper
public interface BuyPlanBuyerDao {
    //添加
    @Insert("insert into buy_plan_buyer(buy_plan_id,goods_id,buyer_username) " +
            "values(#{buy_plan_id},#{goods_id},#{buyer_username})")
    boolean add(BuyPlanBuyer buyPlanBuyer);

    //修改
    @Update("update buy_plan_buyer set goods_id=#{goods_id},buyer_username=#{buyer_username} " +
            "where `id`=#{id}")
    boolean update(BuyPlanBuyer buyPlanBuyer);

    //以id删除
    @Delete("delete from buy_plan_buyer where `id`=#{id}")
    boolean deleteById(@Param("id") int id);

    //以方案id删除
    @Delete("delete from buy_plan_buyer where `buy_plan_id`=#{buy_plan_id}")
    boolean deleteByPlanId(@Param("buy_plan_id") int buy_plan_id);

    //以id查询
    @Select("select buy_plan_buyer.*,goods.goods_name,account.name " +
            "from buy_plan_buyer,goods,account " +
            "where `id`=#{id} and goods.goods_id=buy_plan_buyer.goods_id " +
            "and account.username=buy_plan_buyer.buyer_username")
    BuyPlanBuyer selectOneById(@Param("id") int id);

    //以方案id查询
    @Select("select buy_plan_buyer.*,goods.goods_name,account.name " +
            "from buy_plan_buyer,goods,account " +
            "where `buy_plan_id`=#{plan_id} and goods.goods_id=buy_plan_buyer.goods_id " +
            "and account.username=buy_plan_buyer.buyer_username")
    List<BuyPlanBuyer> selectByPlanId(@Param("plan_id") int plan_id);
}
