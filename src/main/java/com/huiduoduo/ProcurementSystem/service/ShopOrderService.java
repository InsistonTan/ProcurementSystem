package com.huiduoduo.ProcurementSystem.service;

import com.github.pagehelper.PageInfo;
import com.huiduoduo.ProcurementSystem.domain.GoodsOrder;
import com.huiduoduo.ProcurementSystem.domain.ShopOrder;
import com.huiduoduo.ProcurementSystem.domain.pageBean.ShopOrderPage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;
import java.util.Map;

/**
 * @author TanJifeng
 * @Description ShopOrderService interface
 * @date 2020/8/19 15:55
 */
public interface ShopOrderService {
    //新增分店订单
    Map addShopOrder(ShopOrder shopOrder);
    //修改分店订单
    Map updateShopOrder(ShopOrder shopOrder);
    //采购经理审核订单
    Map approved(List<ShopOrder> shopOrders);
    //删除
    Map deleteShopOrder(ShopOrder shopOrder);
    //查看历史订单
    Map getHistory(ShopOrderPage page);
    //查看正在进行的订单
    Map getOngoing(ShopOrderPage page);
    //确认完成订单
    Map confirmShopOrder(ShopOrder shopOrder);
    //打印单个分店订单
    HSSFWorkbook printOne(ShopOrder shopOrder);
    //打印全部历史订单
    HSSFWorkbook printHistory(ShopOrderPage page);
    /*
    * 打印
    * @workbook:目标workbook
    * @sheetName:目标表格名
    * @startRow:表格起始行的索引
    * @goodsOrderList:分店订单的订购货品数据
    *
    * return: key:"maxRow" value:int //目前表格最大行
    *         key:"workbook" value:HSSFWorkbook//内容添加后的workbook
    * */
    Map print(HSSFWorkbook workbook,String sheetName, int startRow, List<GoodsOrder> goodsOrderList);
}
