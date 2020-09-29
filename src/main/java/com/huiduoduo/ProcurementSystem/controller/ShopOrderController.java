package com.huiduoduo.ProcurementSystem.controller;

import com.huiduoduo.ProcurementSystem.domain.ShopOrder;
import com.huiduoduo.ProcurementSystem.domain.pageBean.ShopOrderPage;
import com.huiduoduo.ProcurementSystem.service.ShopOrderService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author TanJifeng
 * @Description  ShopOrderController
 * @date 2020/8/22 21:58
 */
@RequestMapping("/shop_order")
@Controller
public class ShopOrderController {
    @Autowired
    private ShopOrderService shopOrderService;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/getAll")
    @ResponseBody
    public Map getHistory(@RequestBody ShopOrderPage page){
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //
        try {
            if(page.isHistory())
                return shopOrderService.getHistory(page);
            else
                return shopOrderService.getOngoing(page);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }
    }

    @RequestMapping("/create")
    @ResponseBody
    public Map create(@RequestBody ShopOrder shopOrder){
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //
        try{
            return shopOrderService.addShopOrder(shopOrder);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

    }

    @RequestMapping("/update")
    @ResponseBody
    public Map getHistory(@RequestBody ShopOrder shopOrder){
        System.out.println("Update:"+shopOrder);
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //
        try {
            return shopOrderService.updateShopOrder(shopOrder);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map delete(@RequestBody ShopOrder shopOrder){
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //
        try{
            return shopOrderService.deleteShopOrder(shopOrder);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

    }

    @RequestMapping("/confirm")
    @ResponseBody
    public Map confirm(@RequestBody ShopOrder shopOrder){
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //
        try{
            return shopOrderService.confirmShopOrder(shopOrder);
        } catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }
    }

    @RequestMapping("/approve")
    @ResponseBody
    public Map approve(@RequestBody List<ShopOrder> shop_order){
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");
        //
        try {
            return shopOrderService.approved(shop_order);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }

    }

    @RequestMapping("/printOne")
    @ResponseBody
    public Map printOne(HttpServletResponse response)throws IOException {
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");

        HSSFWorkbook workbook;
        //
        String order_id=request.getParameter("order_id");
        if(order_id==null||"".equals(order_id))
            return ResultUtil.getErrorRes("操作失败：订单号为空");

        ShopOrder shopOrder=new ShopOrder();
        shopOrder.setOrder_id(order_id);
        //
        try {
            workbook=shopOrderService.printOne(shopOrder);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }
        if(workbook==null)
            return ResultUtil.getErrorRes("操作失败：没有权限或者数据为空");

        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        //response.setContentType("application/octet-stream");
        response.setContentType("application/vnd.ms-excel");

        //这后面可以设置导出Excel的名称
        String agent = request.getHeader("USER-AGENT").toLowerCase();
        response.setCharacterEncoding("utf-8");
        if (agent.contains("firefox")) {
            response.setHeader("content-disposition", "attachment;filename=" + new String("分店订购表".getBytes(), "ISO8859-1") +shopOrder.getOrder_id()+ ".xls" );
        } else {
            response.setHeader("Content-disposition", "attachment;filename="+java.net.URLEncoder.encode("分店订购表", "UTF-8")+shopOrder.getOrder_id()+".xls");
        }
        //response.setHeader("Content-disposition", "attachment;filename="+shopOrder.getOrder_id()+"分店订购表.xls");

        //刷新缓冲
        response.flushBuffer();

        //workbook将Excel写入到response的输出流中，供页面下载
        workbook.write(response.getOutputStream());
        return null;
    }

    @RequestMapping("/printAll")
    @ResponseBody
    public Map printHistory(HttpServletResponse response)throws IOException {
        Object obj=request.getSession().getAttribute("username");
        if(obj==null)
            return ResultUtil.getErrorRes("操作失败：你还未登陆");

        HSSFWorkbook workbook;
        //
        String history=request.getParameter("history");
        ShopOrderPage shopOrderPage=new ShopOrderPage();
        if(history!=null&&"true".equals(history))
            shopOrderPage.setHistory(true);
        //
        try {
            workbook=shopOrderService.printHistory(shopOrderPage);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.getErrorRes("数据库操作失败");
        }
        if(workbook==null)
            return ResultUtil.getErrorRes("操作失败：没有权限或者数据为空");
        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        //response.setContentType("application/octet-stream");
        response.setContentType("application/vnd.ms-excel");

        //这后面可以设置导出Excel的名称
        String agent = request.getHeader("USER-AGENT").toLowerCase();
        response.setCharacterEncoding("utf-8");
        if (agent.contains("firefox")) {
            response.setHeader("content-disposition", "attachment;filename=" + new String("分店历史订购表".getBytes(), "ISO8859-1") + ".xls" );
        } else {
            response.setHeader("Content-disposition", "attachment;filename="+java.net.URLEncoder.encode("分店历史订购表", "UTF-8")+".xls");
        }


        //刷新缓冲
        response.flushBuffer();

        //workbook将Excel写入到response的输出流中，供页面下载
        workbook.write(response.getOutputStream());
        return null;
    }
}
