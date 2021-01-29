package com.huiduoduo.ProcurementSystem.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huiduoduo.ProcurementSystem.dao.AccountDao;
import com.huiduoduo.ProcurementSystem.dao.GoodsOrderDao;
import com.huiduoduo.ProcurementSystem.dao.ShopOrderDao;
import com.huiduoduo.ProcurementSystem.dao.SingleGoodsOrderDao;
import com.huiduoduo.ProcurementSystem.domain.*;
import com.huiduoduo.ProcurementSystem.domain.pageBean.SingleOrderPage;
import com.huiduoduo.ProcurementSystem.service.SingleGoodsOrderService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import com.huiduoduo.ProcurementSystem.utils.SynUtil;
import com.huiduoduo.ProcurementSystem.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    public Map addSingleOrder() {
        //权限检查（只有采购经理有权限）
        Account login_info=(Account)request.getSession().getAttribute("info");
        String role=login_info.getRole();
        //非采购经理权限
        if(!"manager".equals(role))
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //使用类锁，保证操作原子性，同一时刻只能有一个经理在进行生成单品的操作
        synchronized (this.getClass()){
            //目前没有采购经理在进行此操作
            if(SynUtil.addingSingleOrder==false){
                SynUtil.addingSingleOrder=true;
                SynUtil.managerName=login_info.getName();
            }
            //有采购经理在进行此操作
            else {
                return ResultUtil.getErrorRes("操作失败：目前采购经理"+SynUtil.managerName+"正在生成单品采购单");
            }
        }
        //用try把以下代码包住，无论发不发生错误，都要把 SynUtil.addingSingleOrder还原
        try{
            //选择所有批准但未生成单品大分店订单
            List<ShopOrder> shopOrders=shopOrderDao.selectApproved();
            if(shopOrders==null||shopOrders.size()==0)
                return ResultUtil.getErrorRes("操作失败：批准但还未生成单品采购单的分店订单为空");

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

                //获取该货品的采购单位
                Goods goods=goodsOrderDao.selectRec_unitById(goods_id);
                //目前货品的单品采购单
                SingleGoodsOrder order=new SingleGoodsOrder();
                order.setGoods_id(goods_id);
                order.setSingle_order_id(singleOrderId);
                order.setOrder_goods_num((Float)singleGoodsMap.get(goods_id));
                order.setManager(login_info.getName());
                order.setStart_time(start_time);
                order.setBuy_status("等待发配");
                order.setBuy_goods_unit(goods.getRec_unit());//设置默认采购单位
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
                order.setOrder_status("采购中");
                //更新到数据库
                shopOrderDao.updateOrderByManager(order);
            }

        }catch (Exception e){
            return ResultUtil.getErrorRes("操作失败：数据库操作失败");
        }finally {
            SynUtil.addingSingleOrder=false;
            SynUtil.managerName=null;
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
            order.setBuy_status("已发配，等待采购");
            singleGoodsOrderDao.updateDistribution(order);
        }

        return ResultUtil.getSuccessRes();
    }

    //采购员确认完成采购单
    @Override
    public Map finish(SingleGoodsOrder order) {
        //权限检查（只有采购员有权限）
        Account login_info=(Account)request.getSession().getAttribute("info");
        String role=login_info.getRole();
        //非采购员权限
        if(!"buyer".equals(role))
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //
        if(order.getSingle_order_id()==null)
            return ResultUtil.getErrorRes("操作失败：采购单编号为空");
        //
        SingleGoodsOrder temp_order=singleGoodsOrderDao.selectOrderById(order.getSingle_order_id());
        //
        if(temp_order==null)
            return ResultUtil.getErrorRes("操作失败：采购单"+order.getSingle_order_id()+"不存在");
        //检查该采购员是否可以更新该采购单
        if(!temp_order.getBuyer_username().equals(login_info.getUsername()))
            return ResultUtil.getErrorRes("操作失败：目标采购单所属采购员帐号与登陆帐号不一致");
        if(temp_order.getEnd_time()!=null)
            return ResultUtil.getErrorRes("操作失败：目标采购单已确认完成，无需再次操作");

        //检查具体货品的采购结果
        List<GoodsOrder> goodsOrders=goodsOrderDao.selectBySingleOrderID(order.getSingle_order_id());
        //检查各分店数的和是否等于总数
        int shop_all_num=0;
        for (GoodsOrder goodsOrder:goodsOrders){
            shop_all_num+=goodsOrder.getBuy_num();
        }
        if(shop_all_num!=temp_order.getBuy_goods_num())
            return ResultUtil.getErrorRes("操作失败：各分店实际采购数之和与采购总数不相等，请检查");
        //检查供应商
        if(temp_order.getSupplier_id()==null)
            return ResultUtil.getErrorRes("操作失败：该采购单的供应商信息未填写，请检查");
        if(temp_order.getBuy_goods_num()<0)
            return ResultUtil.getErrorRes("操作失败：该采购单的采购总数应该大于等于0，请检查");
        if(temp_order.getBuy_goods_price()<0)
            return ResultUtil.getErrorRes("操作失败：该采购单的单价应该大于等于0，请检查");
        //检查采购总金额（如果采购数大于0）
        if(temp_order.getBuy_goods_num()>0&&temp_order.getTotal_money()<=0)
            return ResultUtil.getErrorRes("操作失败：该采购单的采购总金额应该大于0，请检查");

        //更新
        order.setBuy_status("采购完成");
        order.setEnd_time(TimeUtil.getTime("yyyy-MM-dd HH:mm:ss"));
        singleGoodsOrderDao.finish(order);

        return ResultUtil.getSuccessRes();
    }

    //更新实际采购货品结果
    @Override
    public Map updateBuyRes(SingleGoodsOrder order) {
        //权限检查（只有采购员有权限更新实际采购信息，采购经理有更新manager_note的权限）
        Account login_info=(Account)request.getSession().getAttribute("info");
        String role=login_info.getRole();
        //权限
        if(!"buyer".equals(role)&&!"manager".equals(role))
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //数据判断
        if(order.getSingle_order_id()==null)
            return ResultUtil.getErrorRes("操作失败：采购单编号为空");
        //
        SingleGoodsOrder temp_order=singleGoodsOrderDao.selectOrderById(order.getSingle_order_id());
        //
        if(temp_order==null)
            return ResultUtil.getErrorRes("操作失败：采购单"+order.getSingle_order_id()+"不存在");
        if(temp_order.getEnd_time()!=null)
            return ResultUtil.getErrorRes("操作失败：目标采购单已结束，无法再进行修改");

        //采购员更新实际采购信息
        if("buyer".equals(role)){
            //检查该采购员是否可以更新该采购单
            if(!temp_order.getBuyer_username().equals(login_info.getUsername()))
                return ResultUtil.getErrorRes("操作失败：目标采购单所属采购员帐号与登陆帐号不一致");

            //更新单品采购单信息
            //检查采购单位是否发生改变
            if(order.getBuy_goods_unit()==null||order.getBuy_goods_unit().equals(""))
                order.setBuy_goods_unit(temp_order.getBuy_goods_unit());
            singleGoodsOrderDao.updateBuyRes(order);

            //更新具体货品的采购结果
            List<GoodsOrder> goodsOrders=order.getGoods_order();
            //循环更新具体各分店的实际采购信息
            if(goodsOrders!=null&&goodsOrders.size()>0)
            for (GoodsOrder goodsOrder:goodsOrders){
                goodsOrder.setBuy_unit(order.getBuy_goods_unit());
                goodsOrder.setGoods_price(order.getBuy_goods_price());
                //设置 单价x数量=该goodsOrder的金额
                float buy_num=goodsOrder.getBuy_num();
                float goods_price=goodsOrder.getGoods_price();
                if(buy_num>0&&goods_price>0){
                    float total_money=buy_num*goods_price;
                    goodsOrder.setTotal_money(total_money);
                }
                goodsOrderDao.addBuyRes(goodsOrder);
            }
        }
        //采购经理修改manager_note
        else {
            singleGoodsOrderDao.updateNote(order);
        }

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

    //对分页查询进行参数预处理
    public SingleOrderPage doPage(SingleOrderPage page){
        //处理输入参数
        if(page.getPage()==0)
            page.setPage(1);
        if(page.getLimit()==0)
            page.setLimit(20);
        if(page.getSearch()==null||"".equals(page.getSearch()))
            page.setSearch("");
        if("-id".equals(page.getSort()))
            page.setSort("desc");
        else
            page.setSort("asc");
        //处理时间条件
        String condition ="";//时间条件
        Date startDate=page.getTimeBegin();
        Date endDate=page.getTimeEnd();
        if(startDate!=null){
            condition+=" and start_time>='"+TimeUtil.getTime(startDate,"yyyy-MM-dd HH:mm:ss")+"'";
        }
        if(endDate!=null){
            condition+=" and end_time<='"+TimeUtil.getTime(endDate,"yyyy-MM-dd HH:mm:ss")+"'";
        }
        //处理供应商条件
        if(page.getSupplier()!=null&&!"".equals(page.getSupplier()))
            condition+=" and supplier.supplier_name='"+page.getSupplier()+"' and single_goods_order.supplier_id=supplier.id ";

        page.setCondition(condition);

        return page;
    }
    //选择历史采购单
    @Override
    public Map selectHistory(SingleOrderPage page) {
        //权限检查（只有采购经理有权限）
        Account login_info=(Account)request.getSession().getAttribute("info");
        String role=login_info.getRole();

        //对输入参数进行预处理
        page=doPage(page);

        //开始分页处理
        PageHelper.startPage(page.getPage(),page.getLimit());

        //
        List<SingleGoodsOrder> orders;

        //采购经理权限
        if("manager".equals(role)){
            orders=singleGoodsOrderDao.selectHistoryManager(page.getCondition(),page.getSearch(),page.getSort());
        }
        else if("buyer".equals(role)){
            orders=singleGoodsOrderDao.selectHistoryBuyer(login_info.getUsername(),page.getCondition(),page.getSearch(),page.getSort());
        }
        else {
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");
        }
        //循环获取具体订货信息
        if(orders!=null&&orders.size()>0) {
            for(int i=0;i<orders.size();i++){
                List<GoodsOrder> goodsOrders=goodsOrderDao.selectBySingleOrderID(orders.get(i).getSingle_order_id());
                orders.get(i).setGoods_order(goodsOrders);
            }
        }
        //
        //生成PageInfo
        PageInfo<SingleGoodsOrder> pageInfo=new PageInfo<>(orders);

        //返回结果
        Map result=new HashMap();
        result.put("status","success");
        result.put("data",orders);
        result.put("total",pageInfo.getTotal());
        return result;
    }
    //选择正在进行的采购单
    @Override
    public Map selectOngoing(SingleOrderPage page) {
        //权限检查（只有采购经理有权限）
        Account login_info=(Account)request.getSession().getAttribute("info");
        String role=login_info.getRole();

        //对输入参数进行预处理
        page=doPage(page);

        //开始分页处理
        PageHelper.startPage(page.getPage(),page.getLimit());

        //
        List<SingleGoodsOrder> orders;

        //采购经理权限
        if("manager".equals(role)){
            orders=singleGoodsOrderDao.selectOngoingManager(page.getSearch(),page.getSort());
        }
        else if("buyer".equals(role)){
            orders=singleGoodsOrderDao.selectOngoingBuyer(login_info.getUsername(),page.getSearch(),page.getSort());
        }
        else {
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");
        }
        //循环获取具体订货信息
        if(orders!=null&&orders.size()>0) {
            for(int i=0;i<orders.size();i++){
                List<GoodsOrder> goodsOrders=goodsOrderDao.selectBySingleOrderID(orders.get(i).getSingle_order_id());
                orders.get(i).setGoods_order(goodsOrders);
            }
        }
        //
        //生成PageInfo
        PageInfo<SingleGoodsOrder> pageInfo=new PageInfo<>(orders);

        //返回结果
        Map result=new HashMap();
        result.put("status","success");
        result.put("data",orders);
        result.put("total",pageInfo.getTotal());
        return result;
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
