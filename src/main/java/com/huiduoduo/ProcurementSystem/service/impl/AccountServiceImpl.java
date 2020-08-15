package com.huiduoduo.ProcurementSystem.service.impl;
import com.huiduoduo.ProcurementSystem.domain.Account;
import com.huiduoduo.ProcurementSystem.dao.AccountDao;
import com.huiduoduo.ProcurementSystem.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TanJifeng
 * @Description AccountService的实现类
 * @date 2020/8/15 13:41
 */
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired(required = false)
    private AccountDao accountMapper;

    @Override
    public Map login(Account account) {
        //存放登陆结果
        Map result=new HashMap();
        //检查的登陆用户是否存在
        Account temp=accountMapper.selectOneByUsername(account.getUsername());
        if(temp==null){
            result.put("status","failed");
            result.put("msg","用户不存在");
            return result;
        }
        //用户存在
        //角色匹配
        if(account.getRole().equals(temp.getRole())){
            //密码正确
            if(temp.getPassword().equals(account.getPassword())){
                temp.setPassword(null);
                result.put("status","success");
                result.put("user",temp);
                return result;
            }
            //密码错误
            else {
                result.put("status","failed");
                result.put("msg","密码错误");
                return result;
            }
        }
        //角色不匹配
        else {
            result.put("status","failed");
            result.put("msg","登陆的账号类型不匹配");
            return result;
        }
    }

    @Override
    public List<Account> selectAll() {
        return accountMapper.selectAll();
    }

    @Override
    public Map addAccount(Account account) {
        Map result=new HashMap();
        //检查该账号是否已经存在
        Account temp=accountMapper.selectOneByUsername(account.getUsername());
        if(temp!=null){
            result.put("status","failed");
            result.put("msg","添加账号失败:该账号已存在");
            return result;
        }
        //添加账号
        if(accountMapper.addAccount(account)){
            result.put("status","success");
            return result;
        }
        else {
            result.put("status","failed");
            result.put("msg","添加账号失败");
            return result;
        }
    }

    @Override
    public Map updateAccount(Account account) {
        Map result=new HashMap();
        //
        if(account.getUsername()!=null&&account.getPassword()!=null){
            //更新成功
            if(accountMapper.updateAccount(account)){
                result.put("status","success");
                return result;
            }
            //更新失败
            else {
                result.put("status","failed");
                result.put("msg","更新用户信息失败");
                return result;
            }
        }
        else {
            result.put("status","failed");
            result.put("msg","更新失败：账号和密码都不能为空");
            return result;
        }
    }

    @Override
    public Map deleteAccount(Account account) {
        Map result=new HashMap();
        if(accountMapper.deleteAccount(account.getUsername())){
            result.put("status","success");
            return result;
        }
        else {
            result.put("status","failed");
            result.put("msg","删除账号失败");
            return result;
        }
    }
}
