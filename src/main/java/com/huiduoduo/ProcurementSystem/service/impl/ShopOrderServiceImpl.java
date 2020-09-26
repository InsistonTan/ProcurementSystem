package com.huiduoduo.ProcurementSystem.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huiduoduo.ProcurementSystem.dao.GoodsOrderDao;
import com.huiduoduo.ProcurementSystem.dao.ShopOrderDao;
import com.huiduoduo.ProcurementSystem.domain.Account;
import com.huiduoduo.ProcurementSystem.domain.GoodsOrder;
import com.huiduoduo.ProcurementSystem.domain.ShopOrder;
import com.huiduoduo.ProcurementSystem.domain.pageBean.ShopOrderPage;
import com.huiduoduo.ProcurementSystem.service.ShopOrderService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import com.huiduoduo.ProcurementSystem.utils.TimeUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author TanJifeng
 * @Description ShopOrderServiceImpl
 * @date 2020/8/19 16:02
 */
@Service
public class ShopOrderServiceImpl implements ShopOrderService {
    @Autowired(required = false)
    private ShopOrderDao shopOrderDao;
    @Autowired(required = false)
    private GoodsOrderDao goodsOrderDao;
    @Autowired
    private HttpServletRequest request;

    //添加分店订单
    @Override
    public Map addShopOrder(ShopOrder shopOrder) {
        //结果 map
        Map result=new HashMap();

        //从session取出登陆者信息
        Account info=(Account)request.getSession().getAttribute("info");
        if(info==null||info.getShop_id()==null||info.getName()==null)
            return ResultUtil.getErrorRes("订单创建失败:登录信息缺失或权限不足");

        //取出该账号所属分店 id，账号姓名
        Integer shop_id=info.getShop_id();
        String name=info.getName();
        shopOrder.setShop_id(shop_id);
        shopOrder.setPrincipal(name);

        //生成订单编号
        String order_id = null;//订单编号
        int index=1;//当前订单序号
        TimeZone timeZone=TimeZone.getTimeZone("GMT+8");
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
        dateFormat.setTimeZone(timeZone);
        String date=dateFormat.format(new Date());
        //循环检查，生成一个可用的编号
        for(int i=0;i<1000;i++){
            //尝试性订单编号
            String temp_id=date+shop_id+index;
            //该编号没被使用
            if(shopOrderDao.selectOneByID(temp_id)==null){
                order_id=temp_id;
                break;
            }
            index++;
        }
        //添加订单编号进 shopOrder
        shopOrder.setOrder_id(order_id);
        //添加订单状态进 shopOrder
        shopOrder.setOrder_status("订单已创建，等待审批");
        //添加创建时间进 shopOrder
        SimpleDateFormat dateFormat1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat1.setTimeZone(timeZone);
        String create_time=dateFormat1.format(new Date());
        shopOrder.setStart_time(create_time);

        //在数据库创建该订单
        //订单创建失败
        if(!shopOrderDao.addOrder(shopOrder))
            return ResultUtil.getErrorRes("订单创建失败:添加进数据库失败");

        //循环处理该订单的具体订货信息
        List<GoodsOrder> goodsOrders=shopOrder.getGoods_order();
        if(goodsOrders==null||goodsOrders.size()==0)
            return ResultUtil.getErrorRes("订单创建失败:具体的供货信息为空");
        //
        for(GoodsOrder goodsOrder:goodsOrders){
            //设置所属分店订单编号
            goodsOrder.setOrder_id(order_id);
            //添加进数据库
            //如果有一个货品的订购信息添加失败，整个订单失败，并删除所属分店订单
            if(!goodsOrderDao.addGoodsOrder(goodsOrder)){
                //删除所属分店订单
                shopOrderDao.deleteOrder(shopOrder);
                return ResultUtil.getErrorRes("订单创建失败:具体的货品订购信息添加进数据库失败");
            }
        }

        //
        result.put("status","success");
        return result;
    }

    //采购经理审批分店订单
    @Override
    public Map approved(List<ShopOrder> shopOrders) {
        Map result=new HashMap();

        //检查登陆者权限
        //从session取出登陆者信息
        Account login_info=(Account)request.getSession().getAttribute("info");
        if(login_info==null||!login_info.getRole().equalsIgnoreCase("manager"))
            return ResultUtil.getErrorRes("更新失败：登录信息缺失或权限不足");

        //循环处理每一个审批订单
        for(ShopOrder shopOrder:shopOrders){
            //检查该订单是否存在
            if(shopOrder.getOrder_id()==null||shopOrderDao.selectOneByID(shopOrder.getOrder_id())==null)
                return ResultUtil.getErrorRes("更新失败：订单"+shopOrder.getOrder_id()+"不存在");

            //检查审批结果
            if(shopOrder.getApproved()!=null&&shopOrder.getApproved().intValue()!=0&&shopOrder.getApproved().intValue()!=-1)
                return ResultUtil.getErrorRes("更新失败：审批数据错误");
            //拒绝
            if(shopOrder.getApproved()!=null&&shopOrder.getApproved().intValue()==-1){
                String end_time=TimeUtil.getTime("yyyy-MM-dd HH:mm:ss");
                shopOrder.setEnd_time(end_time);
                shopOrder.setOrder_status("订单已结束，审批未通过");
            }
            //同意
            else if(shopOrder.getApproved()!=null&&shopOrder.getApproved().intValue()==0){
                shopOrder.setOrder_status("订单已审批，等待采购");
            }

            //设置审批的经理名
            shopOrder.setManager(login_info.getName());
            //更新
            shopOrderDao.updateOrderByManager(shopOrder);
        }

        result.put("status","success");
        return result;
    }

    //更新分店订单
    @Override
    public Map updateShopOrder(ShopOrder shopOrder) {
        Map result=new HashMap();
        //检查登陆者权限
        //从session取出登陆者信息
        Account login_info=(Account)request.getSession().getAttribute("info");
        if(login_info==null)
            return ResultUtil.getErrorRes("更新失败：登录信息缺失");

        //检查目标订单是否存在
        ShopOrder tempShopOrder=shopOrderDao.selectOneByID(shopOrder.getOrder_id());
        if(tempShopOrder==null)
            return ResultUtil.getErrorRes("更新失败：该订单不存在");

        //判断权限
        String role=login_info.getRole();
        //分店权限
        if("shop".equalsIgnoreCase(role)){
            //判断登陆者修改的订单是否属于该分店
            if(tempShopOrder.getShop_id()!=login_info.getShop_id())
                return ResultUtil.getErrorRes("更新失败：该订单所属分店与账号所属分店不一致，没有权限修改");
            //更新分店订单 不成功
            if(!shopOrderDao.updateOrderByShop(shopOrder))
                return ResultUtil.getErrorRes("更新失败：更新时数据库发生错误");

            //检查该订单是否还可以修改具体的订货信息（当采购经理处理过该订单后，便不支持修改具体订货信息）
            //approved=1为已经生成单品采购单，0为经理同意但还没有生成单品，-1为经理拒绝，null则还未处理
            if(tempShopOrder.getApproved()==null){
                //检查具体的订货信息是否要修改
                List<GoodsOrder> goodsOrders=shopOrder.getGoods_order();
                //不为空，需要更新具体的订货信息
                if(goodsOrders!=null&&goodsOrders.size()>0){
                    //删除原有的具体订货信息
                    if(goodsOrderDao.deleteByShopOrderID(shopOrder.getOrder_id())){
                        //循环添加
                        for(GoodsOrder goodsOrder:goodsOrders){
                            goodsOrder.setOrder_id(shopOrder.getOrder_id());
                            //添加
                            goodsOrderDao.addGoodsOrder(goodsOrder);
                        }
                    }
                    else return ResultUtil.getErrorRes("更新失败：更新原有的订货信息失败");
                }
            }
            //返回结果
            result.put("status","success");
            return result;
        }
        //采购经理权限
        else if("manager".equalsIgnoreCase(role)){
            //如果该订单已经生成单品订单，则无法修改审批结果，也无法修改具体定货信息，只能修改 manager_note
            if(tempShopOrder.getApproved()!=null&&tempShopOrder.getApproved().intValue()==1){
                tempShopOrder.setManager_note(shopOrder.getManager_note());
                //失败
                if(!shopOrderDao.updateOrderByManager(tempShopOrder))
                    return ResultUtil.getErrorRes("更新失败：更新数据库失败");
                //成功
                result.put("status","success");
                return result;
            }

            //检查经理审批结果
            Integer approved=shopOrder.getApproved();
            if(approved!=null&&approved.intValue()!=0&&approved.intValue()!=-1)//只有为 0或-1才是合理的
                return ResultUtil.getErrorRes("更新失败：审批数据错误");
            //审批结果发生改变
            if(tempShopOrder.getApproved()!=shopOrder.getApproved()){
                shopOrder.setManager(login_info.getName());
                if(approved!=null){
                    //当 approved为-1，代表该分店订单被经理拒绝，订单结素
                    if(approved.intValue()==-1){
                        String end_time= TimeUtil.getTime("yyyy-MM-dd HH:mm:ss");
                        shopOrder.setEnd_time(end_time);
                        shopOrder.setOrder_status("订单已结束，审批未通过");
                    }
                    //订单approved改为 0
                    else if(approved.intValue()==0){
                        shopOrder.setOrder_status("订单已审批，等待采购");
                        shopOrder.setEnd_time(null);
                    }
                }
                //订单approved改为 null
                else {
                    shopOrder.setOrder_status("订单已创建，等待审批");
                    shopOrder.setEnd_time(null);
                }
            }
            //审批结果未发生改变
            else {
                shopOrder.setOrder_status(tempShopOrder.getOrder_status());
            }

            //在数据库更新订单
            if(!shopOrderDao.updateOrderByManager(shopOrder))
                return ResultUtil.getErrorRes("更新失败：更新数据库失败");

            //检查具体的订货信息是否要修改
            List<GoodsOrder> goodsOrders=shopOrder.getGoods_order();
            //不为空，需要更新具体的订货信息
            if(goodsOrders!=null&&goodsOrders.size()>0){
                //删除原有的具体订货信息
                if(goodsOrderDao.deleteByShopOrderID(shopOrder.getOrder_id())){
                    //循环添加
                    for(GoodsOrder goodsOrder:goodsOrders){
                        goodsOrder.setOrder_id(shopOrder.getOrder_id());
                        //添加
                        goodsOrderDao.addGoodsOrder(goodsOrder);
                    }
                }
                else return ResultUtil.getErrorRes("更新失败：更新原有的订货信息失败");
            }
            //返回结果
            result.put("status","success");
            return result;
        }
        //其他权限
        else return ResultUtil.getErrorRes("更新失败：没有权限进行此操作");
    }

    //删除分店订单
    @Override
    public Map deleteShopOrder(ShopOrder shopOrder) {
        //检查权限
        Account login_info=(Account) request.getSession().getAttribute("info");
        String role=login_info.getRole();
        if(!role.equals("manager")&&!role.equals("shop"))
            return ResultUtil.getErrorRes("删除失败：权限不足");

        //判断目标订单是否存在
        ShopOrder temp_order=shopOrderDao.selectOneByID(shopOrder.getOrder_id());
        if(temp_order==null)
            return ResultUtil.getErrorRes("删除失败：该订单不存在");

        //判断该订单是否可以被删除（如果该订单已生成单品采购单，则不能被删除）
        if(temp_order.getApproved()!=null&&temp_order.getApproved().intValue()==1)
            return ResultUtil.getErrorRes("删除失败：该订单已生成单品采购单，不能删除");

        //判断权限
        //分店
        if(role.equals("shop")){
            //检查目标订单是否属于该分店
            if(temp_order.getShop_id()!=login_info.getShop_id())
                return ResultUtil.getErrorRes("删除失败：该订单所属分店与登陆账号所属分店不一致");
        }

        //删除
        if(shopOrderDao.deleteOrder(shopOrder))
            return ResultUtil.getSuccessRes();
        else
            return ResultUtil.getErrorRes("删除失败：更新数据库失败");
    }

    //对分页查询进行参数预处理
    public ShopOrderPage doPage(ShopOrderPage page){
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
            condition+=" and start_time>'"+TimeUtil.getTime(startDate,"yyyy-MM-dd HH:mm:ss")+"'";
        }
        if(endDate!=null){
            condition+=" and end_time<'"+TimeUtil.getTime(endDate,"yyyy-MM-dd HH:mm:ss")+"'";
        }
        page.setTimeCondition(condition);

        return page;
    }
    //获取历史分店订单
    @Override
    public Map getHistory(ShopOrderPage page) {
        //登陆信息
        Account login_info=(Account) request.getSession().getAttribute("info");
        String role=login_info.getRole();

        //对输入参数进行预处理
        page=doPage(page);

        //开始分页处理
        PageHelper.startPage(page.getPage(),page.getLimit());
        //获取历史订单
        List<ShopOrder> shopOrders;
        //采购经理
        if(role.equals("manager")){
            shopOrders=shopOrderDao.selectAllHistory(page.getTimeCondition(),page.getSearch(),page.getSort());
        }
        //分店
        else if(role.equals("shop")){
            shopOrders=shopOrderDao.selectHistoryByShopID(login_info.getShop_id(),page.getTimeCondition(),page.getSearch(),page.getSort());
        }
        //
        else return ResultUtil.getErrorRes("获取失败：权限不足");

        //获取订单的具体订货信息
        if(shopOrders!=null&&shopOrders.size()>0){
            //循环获取
            for(int i=0;i<shopOrders.size();i++){
                ShopOrder order=shopOrders.get(i);
                List<GoodsOrder> goodsOrders=goodsOrderDao.selectByShopOrderID(order.getOrder_id());
                shopOrders.get(i).setGoods_order(goodsOrders);
            }
        }

        //生成PageInfo
        PageInfo<ShopOrder> pageInfo=new PageInfo<>(shopOrders);

        //返回结果
        Map result=new HashMap();
        result.put("status","success");
        result.put("data",shopOrders);
        result.put("total",pageInfo.getTotal());
        return result;
    }

    //获取正在进行的订单
    @Override
    public Map getOngoing(ShopOrderPage page) {
        //登陆信息
        Account login_info=(Account) request.getSession().getAttribute("info");
        String role=login_info.getRole();

        //对输入参数进行预处理
        page=doPage(page);

        //开始分页处理
        PageHelper.startPage(page.getPage(),page.getLimit());
        //获取历史订单
        List<ShopOrder> shopOrders;
        //采购经理
        if(role.equals("manager")){
            shopOrders=shopOrderDao.selectAllOnGoing(page.getSearch(),page.getSort());
        }
        //分店
        else if(role.equals("shop")){
            shopOrders=shopOrderDao.selectOnGoingByShopID(login_info.getShop_id(),page.getSearch(),page.getSort());
        }
        //
        else return ResultUtil.getErrorRes("获取失败：权限不足");

        //获取订单的具体订货信息
        if(shopOrders!=null&&shopOrders.size()>0){
            //循环获取
            for(int i=0;i<shopOrders.size();i++){
                ShopOrder order=shopOrders.get(i);
                List<GoodsOrder> goodsOrders=goodsOrderDao.selectByShopOrderID(order.getOrder_id());
                shopOrders.get(i).setGoods_order(goodsOrders);
            }
        }

        //生成PageInfo
        PageInfo<ShopOrder> pageInfo=new PageInfo<>(shopOrders);

        //返回结果
        Map result=new HashMap();
        result.put("status","success");
        result.put("data",shopOrders);
        result.put("total",pageInfo.getTotal());
        return result;
    }

    //分店确认完成订单
    @Override
    public Map confirmShopOrder(ShopOrder shopOrder) {
        //检查权限
        Account login_info=(Account) request.getSession().getAttribute("info");
        String role=login_info.getRole();
        if(!role.equals("shop"))
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //判断目标订单是否存在
        ShopOrder temp_order=shopOrderDao.selectOneByID(shopOrder.getOrder_id());
        if(temp_order==null)
            return ResultUtil.getErrorRes("操作失败：该订单不存在");

        //判断该订单是否属于登陆的分店
        if(temp_order.getShop_id()!=login_info.getShop_id())
            return ResultUtil.getErrorRes("操作失败：该订单不属于登陆的分店");

        //设置订单状态和结束时间
        shopOrder.setOrder_status("订单已送达，订单完成");
        shopOrder.setEnd_time(TimeUtil.getTime("yyyy-MM-dd HH:mm:ss"));

        //确认完成
        if(shopOrderDao.confirmOrder(shopOrder))
            return ResultUtil.getSuccessRes();
        else
            return ResultUtil.getErrorRes("操作失败：数据库更新失败");
    }

    @Override
    public HSSFWorkbook printOne(ShopOrder shopOrder) {
        //检查权限
        Account login_info=(Account) request.getSession().getAttribute("info");
        String role=login_info.getRole();
        if(!role.equals("shop")&&!"manager".equals(role))
            return null;

        //判断目标订单是否存在
        ShopOrder temp_order=shopOrderDao.selectOneByID(shopOrder.getOrder_id());
        if(temp_order==null)
            return null;

        //判断该订单是否属于登陆的分店
        if(role.equals("shop")&&temp_order.getShop_id()!=login_info.getShop_id())
            return null;

        //获取订单的具体订货信息
        temp_order.setGoods_order(goodsOrderDao.selectByShopOrderID(temp_order.getOrder_id()));

        //表格的标题
        String title=temp_order.getOrder_id()+" "+temp_order.getShop_name()+"订购清单  订货日期："+temp_order.getStart_time()
                +"   制单人："+temp_order.getPrincipal();

        //创建excel文件
        //声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格，设置表格名称
        HSSFSheet sheet = workbook.createSheet("分店订购表");
        //设置表格列宽度为10个字节
        sheet.setDefaultColumnWidth(10);
        //创建标题，合并单元格区域
        CellRangeAddress cra=new CellRangeAddress(0, 0, 0, 9);
        //在sheet里增加合并单元格
        sheet.addMergedRegion(cra);
        //设置标题
        HSSFRow titleRow=sheet.createRow(0);
        HSSFCell titleCell=titleRow.createCell(0);
        titleCell.setCellValue(new HSSFRichTextString(title));
        //添加数据
        Map result=print(workbook,"分店订购表",1,temp_order.getGoods_order());
        HSSFWorkbook hssfWorkbook=(HSSFWorkbook)result.get("workbook");
        //
        return hssfWorkbook;
    }

    @Override
    public HSSFWorkbook printHistory(ShopOrderPage page) {
        //检查权限
        Account login_info=(Account) request.getSession().getAttribute("info");
        String role=login_info.getRole();
        if(!role.equals("shop")&&!"manager".equals(role))
            return null;

        //
        List<ShopOrder> shopOrderList =null;
        //获取所有历史订单
        if(page.isHistory()==true){
            if("shop".equals(login_info.getRole()))
                shopOrderList=shopOrderDao.selectHistoryByShopID(login_info.getShop_id(),"","","desc");
            else if("manager".equals(login_info.getRole()))
                shopOrderList=shopOrderDao.selectAllHistory("","","desc");
        }
        //获取所有正在进行的订单
        else {
            if("shop".equals(login_info.getRole()))
                shopOrderList=shopOrderDao.selectOnGoingByShopID(login_info.getShop_id(),"","desc");
            else if("manager".equals(login_info.getRole()))
                shopOrderList=shopOrderDao.selectAllOnGoing("","desc");
        }
        //循环获取订单的订货信息
        if(shopOrderList.size()>0)
        for(ShopOrder order:shopOrderList)
            order.setGoods_order(goodsOrderDao.selectByShopOrderID(order.getOrder_id()));

        //创建excel文件
        //声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格，设置表格名称
        HSSFSheet sheet = workbook.createSheet("分店订购表");
        //设置表格列宽度为10个字节
        sheet.setDefaultColumnWidth(10);
        int startRow=0;//开始行号
        //循环处理
        for(int i=0;i<shopOrderList.size();i++){
            ShopOrder temp_order=shopOrderList.get(i);
            //表格的标题
            String title=temp_order.getOrder_id()+" "+temp_order.getShop_name()
                    +"订购清单  订货日期："+temp_order.getStart_time()
                    +"   制单人："+temp_order.getPrincipal();
            //创建标题，合并单元格区域
            CellRangeAddress cra=new CellRangeAddress(startRow, startRow, 0, 9);
            //在sheet里增加合并单元格
            workbook.getSheet("分店订购表").addMergedRegion(cra);
            //设置标题
            HSSFRow titleRow=sheet.createRow(startRow++);
            HSSFCell titleCell=titleRow.createCell(0);
            titleCell.setCellValue(new HSSFRichTextString(title));

            //添加货品信息
            Map res=print(workbook,"分店订购表",startRow,temp_order.getGoods_order());
            startRow=(int)res.get("maxRow")+2;
            workbook=(HSSFWorkbook)res.get("workbook");
        }


        return workbook;
    }

    @Override
    public Map print(HSSFWorkbook workbook,String sheetName, int startRow, List<GoodsOrder> goodsOrderList) {
        HSSFSheet sheet=workbook.getSheet(sheetName);
        //设置表头
        String[] heads={"货号","品名","订购单位","门店数量","采购数量","采购单位","备注"};
        HSSFRow headRow=sheet.createRow(startRow++);
        //循环添加表头
        for(int i=0;i<heads.length;i++){
            HSSFCell headCell=headRow.createCell(i);
            headCell.setCellValue(new HSSFRichTextString(heads[i]));
        }
        //循环添加货品信息
        for(int i=0;i<goodsOrderList.size();i++){

            GoodsOrder order=goodsOrderList.get(i);
            //创建行
            HSSFRow row=sheet.createRow(startRow++);

            //货号
            HSSFCell cell=row.createCell(0);
            cell.setCellValue(new HSSFRichTextString(String.valueOf(order.getGoods_id())));
            //品名
            cell=row.createCell(1);
            cell.setCellValue(new HSSFRichTextString(order.getGoods_name()));
            //单位
            cell=row.createCell(2);
            cell.setCellValue(new HSSFRichTextString(order.getOrder_unit()));
            //门店数量
            cell=row.createCell(3);
            cell.setCellValue(new HSSFRichTextString(String.valueOf(order.getOrder_num())));
            //采购数量
            cell=row.createCell(4);
            cell.setCellValue(new HSSFRichTextString(String.valueOf(order.getBuy_num())));
            //采购单位
            cell=row.createCell(5);
            cell.setCellValue(new HSSFRichTextString(order.getBuy_unit()));
            //备注
            cell=row.createCell(6);
            cell.setCellValue(new HSSFRichTextString(order.getGoods_note()));
        }

        //
        Map res=new HashMap();
        res.put("maxRow",startRow);
        res.put("workbook",workbook);
        return res;
    }
}
