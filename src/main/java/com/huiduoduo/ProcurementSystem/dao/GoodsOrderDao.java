package com.huiduoduo.ProcurementSystem.dao;

import com.huiduoduo.ProcurementSystem.domain.Goods;
import com.huiduoduo.ProcurementSystem.domain.GoodsOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author TanJifeng
 * @Description GoodsOrderDao 具体的货品订购记录表
 * @date 2020/8/18 20:08
 */
@Mapper
public interface GoodsOrderDao {
    //新增
    @Insert("insert into goods_order(order_id,goods_id,order_num,goods_note,buy_unit) " +
            "values(#{order_id},#{goods_id},#{order_num},#{goods_note},#{buy_unit})")
    boolean addGoodsOrder(GoodsOrder goodsOrder);

    //按照分店订单id查询
    @Select("select goods_order.*,goods.goods_name,goods.order_unit,goods.rec_unit,goods.goods_sort,goods.goods_type_id " +
            "from goods_order,goods " +
            "where order_id=#{order_id} and goods.goods_id=goods_order.goods_id")
    List<GoodsOrder> selectByShopOrderID(@Param("order_id") String shopOrderID);

    //按照单品订单id查询
    @Select("select goods_order.*,goods.goods_name,goods.order_unit,goods.rec_unit,goods.goods_sort,goods.goods_type_id,shop.shop_name,shop.shop_id " +
            "from goods_order,goods,shop_order,shop " +
            "where single_order_id=#{order_id} and goods.goods_id=goods_order.goods_id " +
            "  and shop_order.order_id=goods_order.order_id and shop.`shop_id`=shop_order.shop_id ")
    List<GoodsOrder> selectBySingleOrderID(@Param("order_id") String singleOrderID);

    //修改订购货品、数量
    @Update("update goods_order set goods_id=#{goods_id},order_num=#{order_num} " +
            "where `id`=#{id}")
    boolean updateGoodsOrder(GoodsOrder goodsOrder);

    //添加所属单品订单号
    @Update("update goods_order set single_order_id=#{single_order_id} " +
            "where `id`=#{id}")
    boolean addSingleOrderID(GoodsOrder goodsOrder);

    //添加具体的采购信息
    @Update("update goods_order set " +
            "goods_price=#{goods_price},buy_num=#{buy_num},buy_unit=#{buy_unit} " +
            "where `id`=#{id}")
    boolean addBuyRes(GoodsOrder goodsOrder);

    //删除某个分店订单的所有订货信息
    @Delete("delete from goods_order where order_id=#{order_id}")
    boolean deleteByShopOrderID(@Param("order_id") String order_id);

    //删除某个单品采购单的所有订货信息
    @Delete("delete from goods_order where single_order_id=#{single_order_id}")
    boolean deleteBySingleOrderID(@Param("single_order_id") String order_id);

    //删除
    @Delete("delete from goods_order where `id`=#{id}")
    boolean delete(GoodsOrder goodsOrder);

    //通过货品id，获取该货品的采购单位
    @Select("SELECT rec_unit FROM goods WHERE `goods_id`=#{id}")
    Goods selectRec_unitById(@Param("id") int id);
}
