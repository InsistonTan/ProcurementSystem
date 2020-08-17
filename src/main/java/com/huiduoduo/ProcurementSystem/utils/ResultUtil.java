package com.huiduoduo.ProcurementSystem.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TanJifeng
 * @Description 封装结果工具类
 * @date 2020/8/17 21:04
 */
public class ResultUtil {
    //获取操作失败时的result
    public static Map getErrorRes(String msg){
        Map result=new HashMap();
        result.put("status","failed");
        result.put("msg",msg);
        return result;
    }
}
