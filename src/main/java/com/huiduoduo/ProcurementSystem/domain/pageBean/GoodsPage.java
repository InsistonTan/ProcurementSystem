package com.huiduoduo.ProcurementSystem.domain.pageBean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WJQ
 * @since 2020/9/11 17:44
 */
@Data
public class GoodsPage implements Serializable {
    private Integer page;//当前页码
    private Integer limit;//页的大小
    private String sort;//升序或降序，"+id"为升序
    private String search;//模糊搜索关键字（搜索 id或名称// ）
    private Integer goods_type_id;//按货品类型id查询
}
