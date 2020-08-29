package com.huiduoduo.ProcurementSystem.dao;

import com.huiduoduo.ProcurementSystem.domain.ShopOrderPlanGoods;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author TanJifeng
 * @Description
 * @date 2020/8/29 14:43
 */
@Mapper
public interface ShopOrderPlanGoodsDao {
    //添加
    @Insert("insert into shop_order_plan_goods(shop_plan_id,goods_id,order_num) " +
            "values(#{shop_plan_id},#{goods_id},#{order_num})")
    boolean add(ShopOrderPlanGoods planGoods);

    //修改
    @Update("update shop_order_plan_goods set goods_id=#{goods_id},order_num=#{order_num} " +
            "where `id`=#{id}")
    boolean update(ShopOrderPlanGoods planGoods);

    //以id删除
    @Delete("delete from shop_order_plan_goods where `id`=#{id}")
    boolean deleteById(@Param("id")int id);

    //以方案id删除
    @Delete("delete from shop_order_plan_goods where `shop_plan_id`=#{id}")
    boolean deleteByPlanId(@Param("id")int plan_id);

    //以id查询
    @Select("select shop_order_plan_goods.*,goods.goods_name " +
            "from shop_order_plan_goods,goods " +
            "where `id`=#{id} and goods.`goods_id`=shop_order_plan_goods.`goods_id`")
    ShopOrderPlanGoods selectOneById(@Param("id")int id);

    //以方案id查询
    @Select("select shop_order_plan_goods.*,goods.goods_name " +
            "from shop_order_plan_goods,goods " +
            "where `shop_plan_id`=#{id} and goods.`goods_id`=shop_order_plan_goods.`goods_id`")
    List<ShopOrderPlanGoods> selectByPlanId(@Param("id")int id);
}
