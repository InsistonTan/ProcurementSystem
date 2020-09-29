package com.huiduoduo.ProcurementSystem.dao;

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
    @Insert("insert into shop_order_plan(shop_id,plan_name,description) " +
            "values(#{shop_id},#{plan_name},#{description})")
    boolean add(ShopOrderPlan plan);

    //删除
    @Delete("delete from shop_order_plan where `id`=#{id}")
    boolean delete(@Param("id") int id);

    //查询该分店目前最大id
    @Select("select MAX(id) from shop_order_plan where `shop_id`=#{shop_id}")
    int getMaxId(@Param("shop_id") int shop_id);

    //以分店编号查询
    @Select("select shop_order_plan.*,shop.shop_name " +
            "from shop_order_plan,shop " +
            "where shop_order_plan.`shop_id`=#{shop_id} and shop.`shop_id`=#{shop_id}")
    List<ShopOrderPlan> selectByShopID(@Param("shop_id") int shop_id);

    //以方案id查询
    @Select("select shop_order_plan.*,shop.shop_name " +
            "from shop_order_plan,shop " +
            "where `id`=#{id} and shop.`shop_id`=shop_order_plan.`shop_id`")
    ShopOrderPlan selectByID(@Param("id") int id);

    //修改
    @Update("update shop_order_plan " +
            "set plan_name=#{plan_name},description=#{description} " +
            "where `id`=#{id}")
    boolean update(ShopOrderPlan plan);
}
