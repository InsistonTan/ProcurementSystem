package com.huiduoduo.ProcurementSystem.service;
import com.huiduoduo.ProcurementSystem.domain.Account;
import java.util.List;
import java.util.Map;

/**
 * @author TanJifeng
 * @Description AccountService
 * @date 2020/8/15 13:30
 */
public interface AccountService {

    //登陆
    Map login(Account account);

    //选择所有用户的所有信息(管理员查看)
    List<Account> selectAll();

    //增加用户
    Map addAccount(Account account);

    //修改用户信息(username不支持修改)
    Map updateAccount(Account account);

    //删除用户
    Map deleteAccount(Account account);
}
