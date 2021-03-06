package com.huiduoduo.ProcurementSystem.domain.pageBean;
import lombok.Data;
import java.io.Serializable;

/**
 * @author TanJifeng
 * @Description Page，用于接收前端的分页参数
 * @date 2020/9/10 10:29
 */
@Data
public class Page implements Serializable {
    protected int page;//当前页码
    protected int limit;//页的大小
    protected String sort;//升序或降序，"+id"为升序
}
