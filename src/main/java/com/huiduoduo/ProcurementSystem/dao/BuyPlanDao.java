package com.huiduoduo.ProcurementSystem.dao;

import com.huiduoduo.ProcurementSystem.domain.BuyPlan;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author TanJifeng
 * @Description
 * @date 2020/8/29 19:21
 */
@Mapper
public interface BuyPlanDao {
    //添加
    @Insert("insert into buy_plan(manager_username,plan_name) " +
            "values(#{manager_username},#{plan_name})")
    boolean add(BuyPlan buyPlan);

    //删除
    @Delete("delete from buy_plan where `id`=#{id}")
    boolean delete(@Param("id") int id);

    //查询该分店目前最大id
    @Select("select MAX(id) from buy_plan where manager_username=#{username}")
    int getMaxId(@Param("username") String managerName);

    //以id查询
    @Select("select buy_plan.*,account.name from buy_plan,account " +
            "where `id`=#{id} and account.username=manager_username")
    BuyPlan selectOneById(@Param("id") int id);

    //以采购经理账号查询
    @Select("select buy_plan.*,account.name from buy_plan,account " +
            "where manager_username=#{username} and account.username=manager_username")
    List<BuyPlan> selectByMangerName(@Param("username") String managerName);

    //修改
    @Update("update buy_plan set plan_name=#{plan_name} " +
            "where `id`=#{id}")
    boolean update(BuyPlan buyPlan);
}
