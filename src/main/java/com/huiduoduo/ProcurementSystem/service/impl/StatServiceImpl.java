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
    @Autowired(required = false)
    private GoodsTypeDao goodsTypeDao;


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

    //获取正在采购的采购员数量
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

    //获取等待送货的分店数
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

    //获取当天各分店的订单提交数
    @Override
    public Map getTodayOrderNum() {
        //存放结果
        Map result=new HashMap();

        //获取所有分店信息
        List<Shop> shopList=shopDao.selectAll("");
        //获取所有货品类型信息
        List<GoodsType> typeList=goodsTypeDao.selectAll();
        //将混合类型添加到列表
        GoodsType temp=new GoodsType();
        temp.setType_name("混合");
        typeList.add(temp);
        //获取当前日期
        String date=TimeUtil.getTime("yyyyMMdd");

        //循环查询各类订单数
        Map shopData=new HashMap();//存放各分店的数据
        for(Shop shop:shopList){
            Map typeData=new HashMap();//存放当前分店的各个类型订单数
            for(GoodsType goodsType:typeList){
                int num=shopOrderDao.getOnedayTypeOrderNum(date,shop.getShop_id(),goodsType.getType_name()+"类订单");
                typeData.put(goodsType.getType_name()+"类订单数",num);
            }
            shopData.put(shop.getShop_name(),typeData);
        }
        result.put("data",shopData);

        //返回结果
        result.put("status","success");
        return result;
    }

    //获取当天单品订购量预汇总
    @Override
    public Map getTodayGoodsNum() {
        //存放结果
        Map result=new HashMap();

        //获取所有分店信息
        List<Shop> shopList=shopDao.selectAll("");

        //当前日期
        String date=TimeUtil.getTime("yyyyMMdd");

        //存放单品的信息，key为品名，value为 Map
        Map<String,Map> goodsData=new HashMap();

        //循环进行统计
        for(Shop shop:shopList){
            //获取分店当天的订单列表
            List<ShopOrder> orderList=shopOrderDao.selectOneDayOrder(shop.getShop_id(),date);
            //订单列表不为空
            if(orderList!=null&&orderList.size()>0)
                for(ShopOrder order:orderList){
                    //获取订单的订货详情
                    List<GoodsOrder> goodsOrderList=goodsOrderDao.selectByShopOrderID(order.getOrder_id());
                    //详情列表不为空
                    if(goodsOrderList!=null&&goodsOrderList.size()>0)
                       for(GoodsOrder goodsOrder:goodsOrderList){
                           //map中已经有该货品
                           if(goodsData.containsKey(goodsOrder.getGoods_name())){
                               //取出该货品信息，对订购量累加
                               Map tempGoodsInfo=goodsData.get(goodsOrder.getGoods_name());
                               float order_num=(float)tempGoodsInfo.get("order_num")+goodsOrder.getOrder_num();
                               //刷新订购总量的值
                               tempGoodsInfo.replace("order_num",order_num);

                               //取出各分店的订购数据
                               Map tempShopData=(Map)tempGoodsInfo.get("shop_data");
                               //判断是否已经有当前分店的记录
                               if(tempShopData.containsKey(shop.getShop_name())){
                                   //更新订购总量
                                   float temp_num= (float) tempShopData.get(shop.getShop_name())+goodsOrder.getOrder_num();
                                   tempShopData.replace(shop.getShop_name(),temp_num);
                               }
                               //还没有当前分店的记录
                               else {
                                   tempShopData.put(shop.getShop_name(),goodsOrder.getOrder_num());
                               }
                               //刷新
                               tempGoodsInfo.replace("shop_data",tempShopData);
                               goodsData.replace(goodsOrder.getGoods_name(),tempGoodsInfo);

                               //
                           }
                           //map中还没有该货品,需将该货品信息添加进 Map goodsData
                           else{
                               //货品详情
                               Map goodsInfo=new HashMap();
                               goodsInfo.put("goods_id",goodsOrder.getGoods_id());//货品id
                               goodsInfo.put("goods_name",goodsOrder.getGoods_name());//货品名称
                               goodsInfo.put("goods_sort",goodsOrder.getGoods_sort());//排序标志位
                               goodsInfo.put("goods_type_id",goodsOrder.getGoods_type_id());//货品类型 id
                               goodsInfo.put("type_name",goodsOrder.getType_name());//货品类型名
                               goodsInfo.put("order_unit",goodsOrder.getOrder_unit());//订购单位
                               goodsInfo.put("order_num",goodsOrder.getOrder_num());//订购量
                               //各分店的订购量
                               Map temp=new HashMap();
                               temp.put(shop.getShop_name(),goodsOrder.getOrder_num());
                               //把其余的分店都加进去，订购量设为 0
                               for(Shop shop1:shopList){
                                   if(!shop1.getShop_name().equals(shop.getShop_name())){
                                       temp.put(shop1.getShop_name(),0.0f);
                                   }
                               }
                               goodsInfo.put("shop_data",temp);
                               //将该货品的详情添加进 goodsData
                               goodsData.put(goodsOrder.getGoods_name(),goodsInfo);
                           }
                       }
                }
        }

        result.put("data",goodsData);
        //返回结果
        result.put("status","success");
        return result;
    }
}
