package com.huiduoduo.ProcurementSystem.dao;

import com.huiduoduo.ProcurementSystem.domain.Account;
import com.huiduoduo.ProcurementSystem.domain.vo.AccountVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author TanJifeng
 * @Description AccountMapper
 * @date 2020/8/14 11:24
 */
@Mapper
public interface AccountDao {

    //选择所有用户的所有信息(管理员查看),按照中文排序
    @Select("select account.*,shop.shop_name from " +
            "(account left join shop on account.shop_id=shop.shop_id) " +
            "order by convert(name USING gbk) ${sort}")
    List<Account> selectAll(@Param("sort") String sort);//sort为排序

    //名字模糊选择用户的所有信息(管理员查看),按照中文排序
    @Select("select account.*,shop.shop_name from " +
            "(account left join shop on account.shop_id=shop.shop_id) " +
            "where name like '%${name}%' " +
            "order by convert(name USING gbk) ${sort}")
    List<Account> selectAllByName(@Param("name") String name,@Param("sort") String sort);

    //按角色选择用户的所有信息(管理员查看),按照中文排序
    @Select("select account.*,shop.shop_name from " +
            "(account left join shop on account.shop_id=shop.shop_id) " +
            "where role=#{role} " +
            "order by convert(name USING gbk) ${sort}")
    List<Account> selectAllByRole(@Param("role") String role,@Param("sort") String sort);

    //按角色选择用户的姓名和用户名信息(采购经理查看),按照中文排序
    @Select("select username,name from account " +
            "where role=#{role} " +
            "order by convert(name USING gbk) ${sort}")
    List<AccountVO> selectAllUsernameAndNameByRole(@Param("role") String role, @Param("sort") String sort);


    //通过账号选择该用户所有信息
    @Select("select account.*,shop.shop_name from " +
            "(account left join shop on account.shop_id=shop.shop_id) " +
            "where username=#{username}")
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

    //获取指定分店员工数
    @Select("select count(username) from account where shop_id=#{shop_id}")
    int selectShoperNum(@Param("shop_id")int shop_id);
}
