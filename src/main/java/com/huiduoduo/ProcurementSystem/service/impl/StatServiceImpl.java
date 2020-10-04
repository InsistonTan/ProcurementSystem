package com.huiduoduo.ProcurementSystem.service.impl;
import com.huiduoduo.ProcurementSystem.dao.*;
import com.huiduoduo.ProcurementSystem.domain.*;
import com.huiduoduo.ProcurementSystem.service.StatService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import com.huiduoduo.ProcurementSystem.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author TanJifeng
 * @Description
 * @date 2020/10/1 14:11
 */
@Service
public class StatServiceImpl implements StatService {
    @Autowired
    private HttpServletRequest request;
    @Autowired(required = false)
    private AccountDao accountDao;
    @Autowired(required = false)
    private ShopDao shopDao;
    @Autowired(required = false)
    private ShopOrderDao shopOrderDao;
    @Autowired(required = false)
    private SingleGoodsOrderDao singleGoodsOrderDao;
    @Autowired(required = false)
    private GoodsOrderDao goodsOrderDao;


    //获取最近7天的每天的金额（分店权限：分店订单的金额，采购经理：单品的金额）
    @Override
    public Map getRecentMoney() {
        //权限检查（只有采购经理有权限）
        Account login_info=(Account)request.getSession().getAttribute("info");
        String role=login_info.getRole();

        //存放返回的结果
        Map result=new HashMap();
        //存放每个分店的数据
        Map shops_data=new HashMap();
        //采购经理
        if("manager".equals(role)){
            //首先查询各分店，最近7天的采购金额
            //获取所有分店的分店 id
            List<Shop> shopList=shopDao.selectAll("");
            //分店为空
            if(shopList==null||shopList.size()<1)
                result.put("shop_total",shops_data);
            else {
                //循环对每个分店进行统计
                for(Shop shop:shopList){
                    float[] data=getOneShopMoney(shop.getShop_id());
                    shops_data.put(shop.getShop_id(),data);
                }
                result.put("shop_total",shops_data);
            }

            //查询最近7天单品采购的金额
            float[] totalMoney=getTotalMoney();
            result.put("total",totalMoney);
        }
        //分店
        else if("shop".equals(role)){
            float[] data=getOneShopMoney(login_info.getShop_id());
            shops_data.put(login_info.getShop_id(),data);
            result.put("shop_total",shops_data);
        }
        else if("buyer".equals(role)){
            String today=TimeUtil.getTime("yyyyMMdd");
            List<SingleGoodsOrder> orders=singleGoodsOrderDao.selectOneDay_Buyer(login_info.getUsername(),today);
            //
            float[] sum={0};
            if(orders!=null&&orders.size()>0){
                for (SingleGoodsOrder order:orders)
                    sum[0]+=order.getTotal_money();
            }
            result.put("total",sum);
        }
        else return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");


        //
        result.put("status","success");
        return result;
    }
    //获取某个分店的最近7天的金额
    private float[] getOneShopMoney(int shop_id){
        //分店最近7天的金额
        float[] result=new float[7];

        //循环获取最近 7天的
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
        Calendar cal=new GregorianCalendar();
        cal.setTime(new Date());
        //日期递减
        for(int i=0;i<7;i++){
            if(i>0)
                cal.add(Calendar.DATE,-1);//日期减一
            String date=dateFormat.format(cal.getTime());
            //获取该日期的订单
            List<ShopOrder> shopOrders=shopOrderDao.selectOneDayOrder(shop_id,date);
            //订单为空，则金额为 0
            if(shopOrders==null||shopOrders.size()<1)
                result[i]=0;
            else {
                float sum=0;
                //循环计算出总金额
                for(ShopOrder shopOrder:shopOrders){
                    List<GoodsOrder> goodsOrders=goodsOrderDao.selectByShopOrderID(shopOrder.getOrder_id());
                    for(GoodsOrder goodsOrder:goodsOrders){
                        sum+=goodsOrder.getBuy_num()*goodsOrder.getGoods_price();//数量*单价
                    }
                }
                //
                result[i]=sum;
            }

        }
        return result;
    }
    //获取最近7天的总的采购金额
    private float[] getTotalMoney(){
        //最近7天的金额
        float[] result=new float[7];

        //循环获取最近 7天的
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
        Calendar cal=new GregorianCalendar();
        cal.setTime(new Date());
        //日期递减
        for(int i=0;i<7;i++){
            if(i>0)
                cal.add(Calendar.DATE,-1);//日期减一
            String date=dateFormat.format(cal.getTime());
            //获取某个日期date的单品采购单
            List<SingleGoodsOrder> singleGoodsOrders=singleGoodsOrderDao.selectOneDayHistory(date);
            //
            if(singleGoodsOrders==null||singleGoodsOrders.size()<1)
                result[i]=0;
            else {
                float sum=0;
                for(SingleGoodsOrder order:singleGoodsOrders)
                    sum+=order.getTotal_money();
                result[i]=sum;
            }
        }
        return result;
    }

    //获取待处理订单数
    @Override
    public Map getToDoNum() {
        //权限检查（只有采购经理有权限）
        Account login_info=(Account)request.getSession().getAttribute("info");
        String role=login_info.getRole();

        //存放返回的结果
        Map result=new HashMap();

        if("manager".equals(role)){
            int undoShopOrderSum=shopOrderDao.getUnDoSum();//未审批的订单数
            int ongoingSum=singleGoodsOrderDao.getAllOnGoingSum();//未完成的采购单数
            int dontHaveBuyerSum=singleGoodsOrderDao.getAllDontHaveBuyer();//未分配采购员的采购单数
            int[] data=new int[3];
            data[0]=undoShopOrderSum;
            data[1]=ongoingSum;
            data[2]=dontHaveBuyerSum;
            //
            result.put("todo_num",data);
        }
        else if("shop".equals(role)){
            int ongoningSum=shopOrderDao.getOnGoingSum(login_info.getShop_id());
            int[] data=new int[1];
            data[0]=ongoningSum;
            //
            result.put("todo_num",data);
        }
        else if("buyer".equals(role)){
            int ongoingSum=singleGoodsOrderDao.getOneBuyerOnGoingSum(login_info.getUsername());
            int[] data=new int[1];
            data[0]=ongoingSum;
            //
            result.put("todo_num",data);
        }
        else return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //
        result.put("status","success");
        return result;
    }

    //获取分店员工数
    @Override
    public Map getShoperNum() {
        //权限检查（只有采购经理有权限）
        Account login_info=(Account)request.getSession().getAttribute("info");
        String role=login_info.getRole();
        if(!"shop".equals(role))
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //存放返回的结果
        Map result=new HashMap();
        int num=accountDao.selectShoperNum(login_info.getShop_id());
        result.put("status","success");
        result.put("shop_man_num",num);
        //
        return result;
    }

    //获取今日采购总数
    @Override
    public Map getBuyTotal() {
        //权限检查（只有采购经理有权限）
        Account login_info=(Account)request.getSession().getAttribute("info");
        String role=login_info.getRole();

        //存放返回的结果
        Map result=new HashMap();

        //
        String today= TimeUtil.getTime("yyyyMMdd");
        float sum=0;
        //
        if("shop".equals(role)){
            //
            List<ShopOrder> shopOrders=shopOrderDao.selectOneDayOrder(login_info.getShop_id(),today);
            //
            if(shopOrders!=null&&shopOrders.size()>0)
            for(ShopOrder shopOrder:shopOrders){
                List<GoodsOrder> goodsOrders=goodsOrderDao.selectByShopOrderID(shopOrder.getOrder_id());
                if(goodsOrders!=null&&goodsOrders.size()>0)
                    for(GoodsOrder goodsOrder:goodsOrders)
                        sum+=goodsOrder.getBuy_num();
            }
            //
            result.put("buy_total",sum);
        }
        else if("buyer".equals(role)){
            //
            List<SingleGoodsOrder> singleGoodsOrders=singleGoodsOrderDao.selectOneDay_Buyer(login_info.getUsername(),today);
            //
            if(singleGoodsOrders!=null&&singleGoodsOrders.size()>0){
                for(SingleGoodsOrder order:singleGoodsOrders){
                    sum+=order.getBuy_goods_num();
                }
            }
            //
            result.put("buy_total",sum);
        }
        else return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //
        result.put("status","success");
        return result;
    }

    @Override
    public Map getBuying_man_num() {
        //权限检查（只有采购经理有权限）
        Account login_info=(Account)request.getSession().getAttribute("info");
        String role=login_info.getRole();
        //
        if(!"manager".equals(role))
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //存放返回的结果
        Map result=new HashMap();

        //
        result.put("status","success");
        result.put("buying_man_num",singleGoodsOrderDao.getBuyingBuyerSum());
        //
        return result;
    }

    @Override
    public Map getWaiting_shop_num() {
        //权限检查（只有采购经理有权限）
        Account login_info=(Account)request.getSession().getAttribute("info");
        String role=login_info.getRole();
        //
        if(!"manager".equals(role))
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //存放返回的结果
        Map result=new HashMap();

        //
        result.put("status","success");
        result.put("waiting_shop_num",shopOrderDao.getWaitingShopSum());
        //
        return result;
    }

    /*public static void main(String[] args){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
        Calendar cal=new GregorianCalendar();
        cal.setTime(new Date());
        for(int i=1;i<=7;i++){
            if(i>1)
                cal.add(Calendar.DATE,-1);
            String date=dateFormat.format(cal.getTime());
            System.out.println(date);
        }
    }*/
}
