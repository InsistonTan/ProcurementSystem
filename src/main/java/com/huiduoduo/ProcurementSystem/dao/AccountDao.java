package com.huiduoduo.ProcurementSystem.dao;

import com.huiduoduo.ProcurementSystem.domain.Account;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author TanJifeng
 * @Description AccountMapper
 * @date 2020/8/14 11:24
 */
@Mapper
public interface AccountDao {

    //选择所有用户的所有信息(管理员查看)
    @Select("select * from account")
    List<Account> selectAll();

    //通过账号选择该用户所有信息
    @Select("select * from account where username=#{username}")
    Account selectOneByUsername(@Param("username")String username);

    //增加用户
    @Insert("insert into account(username,password,role,shop_id,gender,name,phone,addr,id_card)" +
            " values(#{username},#{password},#{role},#{shop_id},#{gender},#{name},#{phone},#{addr},#{id_card})")
    boolean addAccount(Account account);

    //修改用户信息(username不支持修改)
    @Update("update account set password=#{password},role=#{role},shop_id=#{shop_id}" +
            ",gender=#{gender},name=#{name},phone=#{phone},addr=#{addr},id_card=#{id_card} " +
            "where username=#{username}")
    boolean updateAccount(Account account);

    //删除用户
    @Delete("delete from account where username=#{username}")
    boolean deleteAccount(@Param("username")String username);
}
