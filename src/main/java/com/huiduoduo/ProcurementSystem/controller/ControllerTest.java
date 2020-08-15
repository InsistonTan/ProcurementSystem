package com.huiduoduo.ProcurementSystem.controller;

import com.huiduoduo.ProcurementSystem.dao.DaoTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author LPC
 * @create 2020-08-15 14:42
 */

@RestController
public class ControllerTest {

    @Autowired
    DaoTest daoTest;

    @RequestMapping("/test")
    public String test(){
        Map map = daoTest.daoTest();
        return map.toString();
    }
}
