package com.huiduoduo.ProcurementSystem.utils;

/**
 * @author TanJifeng
 * @Description 同步工具类，保证操作原子性
 * @date 2020/9/30 12:08
 */
public class SynUtil {
    //此变量代表是否有账号正在生成单品采购单（以避免多个采购经理同时在生成单品采购单，造成错误）
    //原则上应该在同一时刻，只有一个经理在生成单品
    public static boolean addingSingleOrder=false;
    public static String managerName=null;
}
