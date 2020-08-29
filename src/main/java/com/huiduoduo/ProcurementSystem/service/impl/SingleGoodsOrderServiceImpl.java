package com.huiduoduo.ProcurementSystem.service.impl;

import com.huiduoduo.ProcurementSystem.dao.AccountDao;
import com.huiduoduo.ProcurementSystem.dao.GoodsOrderDao;
import com.huiduoduo.ProcurementSystem.dao.ShopOrderDao;
import com.huiduoduo.ProcurementSystem.dao.SingleGoodsOrderDao;
import com.huiduoduo.ProcurementSystem.domain.*;
import com.huiduoduo.ProcurementSystem.service.SingleGoodsOrderService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import com.huiduoduo.ProcurementSystem.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author TanJifeng
 * @Description SingleGoodsOrderServiceImpl SingleGoodsOrderService实现类
 * @date 2020/8/28 20:53
 */
@Service
public class SingleGoodsOrderServiceImpl implements SingleGoodsOrderService {
    @Autowired(required = false)
    private SingleGoodsOrderDao singleGoodsOrderDao;
    @Autowired(required = false)
    private GoodsOrderDao goodsOrderDao;
    @Autowired(required = false)
    private ShopOrderDao shopOrderDao;
    @Autowired(required = false)
    private AccountDao accountDao;
    @Autowired
    private HttpServletRequest request;

    //将分店订单合并成单品采购单
    @Override
    public Map addSingleOrder(List<ShopOrder> shopOrders) {
        //权限检查（只有采购经理有权限）
        Account login_info=(Account)request.getSession().getAttribute("info");
        String role=login_info.getRole();
        //非采购经理权限
        if(!"manager".equals(role))
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //整理出所有订购的单品、单品订购总数
        Map singleGoodsMap=new HashMap();//存放单品，key为货品id，value为订购数
        //循环处理分店订单
        for(ShopOrder shopOrder:shopOrders){
            //获得该分店订单的具体订货信息
            List<GoodsOrder> goodsOrders=goodsOrderDao.selectByShopOrderID(shopOrder.getOrder_id());
            //循环处理该分店订单的具体的订购的货品
            for(GoodsOrder goodsOrder:goodsOrders){
                int goods_id=goodsOrder.getGoods_id();
                //如果该货品订购信息已经添加进singleGoodsMap
                if(singleGoodsMap.containsKey(goods_id)){
                    //订购数量增加
                    float orderNum=(float)singleGoodsMap.get(goods_id)+goodsOrder.getOrder_num();
                    //更新
                    singleGoodsMap.replace(goods_id,orderNum);
                }
                //如果该货品还未添加进singleGoodsMap
                else {
                    singleGoodsMap.put(goods_id,goodsOrder.getOrder_num());
                }
            }
        }

        //循环处理singleGoodsMap里的单品信息，生成多个单品采购单
        //存放生成的单品采购单信息，key为货品id，value为对应单品采购单编号
        Map singleOrdersMap=new HashMap();
        //单品，货品编号集
        Set<Integer> goods_ids=singleGoodsMap.keySet();
        //采购单创建时间
        String start_time= TimeUtil.getTime("yyyy-MM-dd HH:mm:ss");
        //循环生成单品采购单
        for(int goods_id:goods_ids){
            //生成可用采购单号
            String singleOrderId=null;//可用采购单号
            int temp=01;//尝试位
            for(int i=0;i<1000;i++){
                String temp_id=TimeUtil.getTime("yyyyMMdd")+goods_id+temp;
                //编号可用
                if(singleGoodsOrderDao.selectOrderById(temp_id)==null){
                    singleOrderId=temp_id;
                    break;
                }
                temp++;
            }
            if(singleOrderId==null)
                return ResultUtil.getErrorRes("操作失败：单品采购单编号生成失败");
            //目前货品的单品采购单
            SingleGoodsOrder order=new SingleGoodsOrder();
            order.setGoods_id(goods_id);
            order.setSingle_order_id(singleOrderId);
            order.setOrder_goods_num((Float)singleGoodsMap.get(goods_id));
            order.setManager(login_info.getName());
            order.setStart_time(start_time);
            order.setBuy_status("采购单已创建");
            //添加进数据库成功
            if(singleGoodsOrderDao.addSingleOrder(order)){
                //添加进 singleOrdersMap
                singleOrdersMap.put(goods_id,singleOrderId);
            }
            else return ResultUtil.getErrorRes("操作失败：单品采购单添加进数据库失败");
        }

        //循环将单品采购单编号更新到所有分店的所有货品订购信息
        //循环处理分店订单
        for(ShopOrder shopOrder:shopOrders){
            //获得该分店订单的具体订货信息
            List<GoodsOrder> goodsOrders=goodsOrderDao.selectByShopOrderID(shopOrder.getOrder_id());
            //循环处理该分店订单的具体的订购的货品
            for(GoodsOrder goodsOrder:goodsOrders){
                int goods_id=goodsOrder.getGoods_id();
                //获得货品对应的单品采购单编号
                String single_order_id= (String) singleOrdersMap.get(goods_id);
                //将单品采购单编号更新到goodsOrder
                goodsOrder.setSingle_order_id(single_order_id);
                //更新到数据库
                goodsOrderDao.addSingleOrderID(goodsOrder);
            }
        }

        //循环将分店订单的状态更新
        for(ShopOrder shopOrder:shopOrders){
            ShopOrder order=shopOrderDao.selectOneByID(shopOrder.getOrder_id());
            order.setApproved(1);//1代表已生成单品采购单
            order.setOrder_status("订单已生成单品采购单");
            //更新到数据库
            shopOrderDao.updateOrderByManager(order);
        }

        return ResultUtil.getSuccessRes();
    }

    //采购经理分配采购员
    @Override
    public Map distribute(List<SingleGoodsOrder> orders) {
        //权限检查（只有采购经理有权限）
        Account login_info=(Account)request.getSession().getAttribute("info");
        String role=login_info.getRole();
        //非采购经理权限
        if(!"manager".equals(role))
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //循环更新
        for(SingleGoodsOrder order:orders){
            if(order.getSingle_order_id()==null)
                return ResultUtil.getErrorRes("操作失败：存在采购单编号为空");
            if(singleGoodsOrderDao.selectOrderById(order.getSingle_order_id())==null)
                return ResultUtil.getErrorRes("操作失败：采购单"+order.getSingle_order_id()+"不存在");
            if(order.getBuyer_username()==null||accountDao.selectOneByUsername(order.getBuyer_username())==null)
                return ResultUtil.getErrorRes("操作失败：账号"+order.getBuyer_username()+"不存在");
            //更新
            singleGoodsOrderDao.updateDistribution(order);
        }

        return ResultUtil.getSuccessRes();
    }

    //更新采购状态
    @Override
    public Map updateStatus(SingleGoodsOrder order) {
        //权限检查（只有采购员有权限）
        Account login_info=(Account)request.getSession().getAttribute("info");
        String role=login_info.getRole();
        //非采购员权限
        if(!"buyer".equals(role))
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //
        if(order.getSingle_order_id()==null)
            return ResultUtil.getErrorRes("操作失败：采购单编号为空");
        if(singleGoodsOrderDao.selectOrderById(order.getSingle_order_id())==null)
            return ResultUtil.getErrorRes("操作失败：采购单"+order.getSingle_order_id()+"不存在");

        //更新
        singleGoodsOrderDao.updateStatus(order);


        return null;
    }

    //更新实际采购货品结果
    @Override
    public Map updateBuyRes(SingleGoodsOrder order) {
        //权限检查（只有采购员有权限）
        Account login_info=(Account)request.getSession().getAttribute("info");
        String role=login_info.getRole();
        //非采购员权限
        if(!"buyer".equals(role))
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //数据判断
        if(order.getSingle_order_id()==null)
            return ResultUtil.getErrorRes("操作失败：采购单编号为空");
        if(singleGoodsOrderDao.selectOrderById(order.getSingle_order_id())==null)
            return ResultUtil.getErrorRes("操作失败：采购单"+order.getSingle_order_id()+"不存在");


        //更新具体货品的采购结果
        List<GoodsOrder> goodsOrders=order.getGoods_order();
        if(goodsOrders==null||goodsOrders.size()==0)
            return ResultUtil.getErrorRes("操作失败：具体的各分店实际采购数量信息为空");
        //检查各分店数的和是否等于总数
        int shop_all_num=0;
        for (GoodsOrder goodsOrder:goodsOrders){
            shop_all_num+=goodsOrder.getBuy_num();
        }
        if(shop_all_num!=order.getBuy_goods_num())
            return ResultUtil.getErrorRes("操作失败：各分店实际采购数之和与采购总数不相等");
        //循环更新
        for (GoodsOrder goodsOrder:goodsOrders){
            goodsOrder.setBuy_unit(order.getBuy_goods_unit());
            goodsOrder.setGoods_price(order.getBuy_goods_price());
            goodsOrderDao.addBuyRes(goodsOrder);
        }

        //更新采购单
        order.setBuy_status("采购完成");
        order.setEnd_time(TimeUtil.getTime("yyyy-MM-dd HH:mm:ss"));
        singleGoodsOrderDao.updateBuyRes(order);
        //返回结果
        return ResultUtil.getSuccessRes();
    }

    //删除采购单
    @Override
    public Map delete(SingleGoodsOrder order) {
        //权限检查（只有采购经理有权限）
        Account login_info=(Account)request.getSession().getAttribute("info");
        String role=login_info.getRole();
        //非采购经理权限
        if(!"manager".equals(role))
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //数据判断
        if(order.getSingle_order_id()==null)
            return ResultUtil.getErrorRes("操作失败：目标采购单编号为空");
        if(singleGoodsOrderDao.selectOrderById(order.getSingle_order_id())==null)
            return ResultUtil.getErrorRes("操作失败：采购单"+order.getSingle_order_id()+"不存在");

        if(singleGoodsOrderDao.delete(order))
            return ResultUtil.getSuccessRes();
        else
            return ResultUtil.getErrorRes("操作失败：数据库操作失败");

    }

    //选择历史采购单
    @Override
    public Map selectHistory() {
        //权限检查（只有采购经理有权限）
        Account login_info=(Account)request.getSession().getAttribute("info");
        String role=login_info.getRole();
        //采购经理权限
        if("manager".equals(role)){
            return ResultUtil.getSuccessRes(getGoodsOrders(singleGoodsOrderDao.selectHistoryManager()));
        }
        else if("buyer".equals(role)){
            return ResultUtil.getSuccessRes(getGoodsOrders(singleGoodsOrderDao.selectHistoryBuyer(login_info.getUsername())));
        }
        else {
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");
        }

    }

    //选择正在进行的采购单
    @Override
    public Map selectOngoing() {
        //权限检查（只有采购经理有权限）
        Account login_info=(Account)request.getSession().getAttribute("info");
        String role=login_info.getRole();
        //采购经理权限
        if("manager".equals(role)){
            return ResultUtil.getSuccessRes(getGoodsOrders(singleGoodsOrderDao.selectOngoingManager()));
        }
        else if("buyer".equals(role)){
            return ResultUtil.getSuccessRes(getGoodsOrders(singleGoodsOrderDao.selectOngoingBuyer(login_info.getUsername())));
        }
        else {
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");
        }
    }

    //获取单品采购单的具体的货品信息
    private List<SingleGoodsOrder> getGoodsOrders(List<SingleGoodsOrder> orders){
        for(int i=0;i<orders.size();i++){
            SingleGoodsOrder order=orders.get(i);
            //获取具体的货品订购信息
            List<GoodsOrder> goodsOrders=goodsOrderDao.selectBySingleOrderID(order.getSingle_order_id());
            //添加进单品采购单
            orders.get(i).setGoods_order(goodsOrders);
        }
        return orders;
    }
}
