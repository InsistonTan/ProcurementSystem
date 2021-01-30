package com.huiduoduo.ProcurementSystem.dao;

import com.huiduoduo.ProcurementSystem.domain.ShopOrder;
import com.huiduoduo.ProcurementSystem.domain.ShopOrderPlan;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author TanJifeng
 * @Description ShopOrderPlanDao
 * @date 2020/8/29 14:58
 */
@Mapper
public interface ShopOrderPlanDao {
    //添加
    @Insert("insert into shop_order_plan(id,shop_id,plan_name,description) " +
            "values(#{id},#{shop_id},#{plan_name},#{description})")
    boolean add(ShopOrderPlan plan);

    //删除
    @Delete("delete from shop_order_plan where `id`=#{id}")
    boolean delete(@Param("id") int id);

    @Select("SELECT * FROM shop_order_plan where `id`=#{id}")
    ShopOrderPlan checkID(@Param("id") int id);

    //以分店编号查询(分店的方案+经理的方案)
    @Select("select shop_order_plan.*,shop.shop_name " +
            "from shop_order_plan " +
            "left join shop on shop_order_plan.`shop_id`=shop.`shop_id` " +
            "where shop_order_plan.`shop_id`=#{shop_id} or shop_order_plan.`shop_id` IS NULL")
    List<ShopOrderPlan> selectByShopID(@Param("shop_id") int shop_id);

    //采购经理查询所有方案
    @Select("SELECT shop_order_plan.*,shop.shop_name " +
            "FROM shop_order_plan " +
            "left join shop on shop_order_plan.`shop_id`=shop.`shop_id` ")
    List<ShopOrderPlan> selectManagerPlans();

    //以方案id查询
    @Select("select shop_order_plan.*,shop.shop_name " +
            "from shop_order_plan " +
            "left join shop on shop_order_plan.`shop_id`=shop.`shop_id` " +
            "where `id`=#{id} ")
    ShopOrderPlan selectByID(@Param("id") int id);

    //修改
    @Update("update shop_order_plan " +
            "set plan_name=#{plan_name},description=#{description},notice=#{notice} " +
            "where `id`=#{id}")
    boolean update(ShopOrderPlan plan);
}
