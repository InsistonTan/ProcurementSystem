package com.huiduoduo.ProcurementSystem.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author TanJifeng
 * @Description 获取当前北京时间 工具类
 * @date 2020/8/21 22:28
 */
public class TimeUtil {
    public static TimeZone timeZone=TimeZone.getTimeZone("GMT+8");

    //获取当前北京时间
    public static String getTime(String pattern){
        SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(new Date());
    }
}
