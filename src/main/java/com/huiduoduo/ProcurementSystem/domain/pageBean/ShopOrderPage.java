package com.huiduoduo.ProcurementSystem.domain.pageBean;

import lombok.Data;

import java.util.Date;

/**
 * @author TanJifeng
 * @Description 用于接收前端的分页查询ShopOrder的参数
 * @date 2020/9/10 18:50
 */
@Data
public class ShopOrderPage extends Page{
    private boolean history;//是否查询历史
    private Date timeBegin;//订单开始时间
    private Date timeEnd;//结束时间
    private String search;//模糊搜索关键字（搜索订单 id或分店名称// ）
    private String timeCondition;//根据 timeBegin和 timeEnd处理生成的sql时间条件
}
