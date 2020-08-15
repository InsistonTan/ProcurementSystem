package com.huiduoduo.ProcurementSystem.dao;

import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @author LPC
 * @create 2020-08-15 15:20
 */
public interface DaoTest {
    
    @Select("select * from account")
    Map daoTest();
}
