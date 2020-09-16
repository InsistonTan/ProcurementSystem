package com.huiduoduo.ProcurementSystem.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author TanJifeng
 * @Description AccountVO 只保留前端所需的信息
 * @date 2020/9/16 18:06
 */
@Data
public class AccountVO implements Serializable {
    private String username;//账号
    private String name;//真实姓名
}
