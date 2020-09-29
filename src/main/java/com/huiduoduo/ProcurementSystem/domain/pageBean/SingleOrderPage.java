package com.huiduoduo.ProcurementSystem.domain.pageBean;

import lombok.Data;

import java.util.Date;

/**
 * @author TanJifeng
 * @Description
 * @date 2020/9/27 10:18
 */
@Data
public class SingleOrderPage extends Page{
    private boolean history;//是否查询历史
    private Date timeBegin;//订单开始时间
    private Date timeEnd;//结束时间
    private String search;//模糊搜索关键字（搜索订单 id或分店名称// ）
    private String condition;//根据 timeBegin和 timeEnd处理生成的sql时间条件以及供应商过滤条件
    private String supplier;//供应商名称过滤
}
