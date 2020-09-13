package com.huiduoduo.ProcurementSystem.utils;

import java.util.regex.Pattern;

/**
 * @author WJQ
 * @since 2020/9/12 11:24
 */
public class SortStringUtil {

    public static String replace(String sort) throws Exception {
        //如果输入空字符则直接返回
        if (sort.length() == 0)
            return sort;

        //判断表达式是否符合排序表达式
        if (!Pattern.matches("((\\+|\\-)[\\w]+)+", sort)) {
            throw new Exception("错误的排序表达式");
        }

        //对表达式进行处理
        sort = sort.replaceAll("\\+([\\w]+)", " $1" + " ASC,");
        sort = sort.replaceAll("\\-([\\w]+)", " $1" + " DESC,");
        sort = sort.substring(0, sort.length() - 1);
        return sort;
    }
}
//    //测试
//    public static void main(String[] args) throws Exception {
//        String a1 = "+goods_id";
//        String a2 = "+id-name+age";
//        String a3 = "-id+name-age-a-s+d+f-s";
//        a1=replace(a1);
//        a2=replace(a2);
//        a3=replace(a3);
//        System.out.println(a1);// goods_id ASC
//        System.out.println(a2);//id ASC, name DESC, age ASC
//        System.out.println(a3);//id DESC, name ASC, age DESC, a DESC, s DESC, d ASC, f ASC, s DESC
//
//    }


