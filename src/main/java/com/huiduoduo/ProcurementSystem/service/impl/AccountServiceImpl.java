package com.huiduoduo.ProcurementSystem.service.impl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huiduoduo.ProcurementSystem.domain.Account;
import com.huiduoduo.ProcurementSystem.dao.AccountDao;
import com.huiduoduo.ProcurementSystem.domain.vo.AccountVO;
import com.huiduoduo.ProcurementSystem.service.AccountService;
import com.huiduoduo.ProcurementSystem.utils.EncryptionUtil;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
    private AccountDao accountDao;
    @Autowired
    private HttpServletRequest request;

    @Override
    public Map login(Account account) {
        //存放登陆结果
        Map result=new HashMap();
        //检查的登陆用户是否存在
        Account temp=accountDao.selectOneByUsername(account.getUsername());
        if(temp==null)
            return ResultUtil.getErrorRes("用户不存在");
        //用户存在
        //验证密码正确
        if(EncryptionUtil.verify(account.getPassword(),temp.getPassword())){
            temp.setPassword(null);
            result.put("status","success");
            result.put("user",temp);
            return result;
        }
        //密码错误
        else return ResultUtil.getErrorRes("密码错误");
    }

    @Override
    public List<Account> selectAll() {
        return accountDao.selectAll("asc");
    }

    @Override
    public Map addAccount(Account account) {
        Map result=new HashMap();
        //检查关键信息是否非空
        if(account.getUsername()==null||account.getName()==null||account.getRole()==null)
            return ResultUtil.getErrorRes("账号关键信息都不能为空");

        //如果密码为空，则设置初始密码123456
        if(account.getPassword()==null||account.getPassword().equals("")){
            account.setPassword("123456");
        }

        //检查该账号是否已经存在
        Account temp=accountDao.selectOneByUsername(account.getUsername());
        if(temp!=null)
            return ResultUtil.getErrorRes("添加账号失败:该账号已存在");

        //密码加密
        String pass=account.getPassword();
        account.setPassword(EncryptionUtil.encryptText(pass));
        //添加账号
        if(accountDao.addAccount(account)){
            result.put("status","success");
            return result;
        }
        else return ResultUtil.getErrorRes("添加账号失败:添加进数据库失败");
    }

    @Override
    public Map updateAccount(Account account) {
        Map result=new HashMap();
        //进行权限判定，因为不同权限支持修改的项不一样
        Object obj_role=request.getSession().getAttribute("role");
        String loginUsername=(String) request.getSession().getAttribute("username");
        if(obj_role==null)
            return ResultUtil.getErrorRes("更新失败:权限错误");

        //角色权限role
        String role=(String) obj_role;

        //检查登陆者是否可以修改该账号信息
        //如果不是管理员则不能修改除了自己以外的账号
        if((!role.equalsIgnoreCase("admin"))&&(!loginUsername.equals(account.getUsername())))
            return ResultUtil.getErrorRes("更新失败:没有权限修改'"+account.getUsername()+"'用户信息");

        //数据检查，关键数据不能为空
        if(account.getUsername()==null||account.getUsername().equals(""))
            return ResultUtil.getErrorRes("更新失败:账号为空，无法进行更新");

        //检查该账号是否存在
        Account temp_account=accountDao.selectOneByUsername(account.getUsername());
        if(temp_account==null)
            return ResultUtil.getErrorRes("更新失败:该用户不存在，无法进行更新");

        //进行信息更新
        Account updatedAccount=getUpdatedAccount(role,temp_account,account);
        if(accountDao.updateAccount(updatedAccount)){
            result.put("status","success");
            return result;
        }
        else
            return ResultUtil.getErrorRes("更新失败:数据库修改失败");
    }

    @Override
    public Map selectBuyersByManager() {
        //查询
        List<AccountVO> data=accountDao.selectAllUsernameAndNameByRole("buyer","asc");
        return ResultUtil.getSuccessRes(data);
    }

    //分页查询
    @Override
    public PageInfo<Account> selectAllByPage(Account input) {
        //检查参数
        if(input.getPage()==0)
            input.setPage(1);
        if(input.getLimit()==0)
            input.setLimit(20);
        //检查排序
        if("-name".equals(input.getSort()))
            input.setSort("desc");//倒序
        else
            input.setSort("asc");//正序

        //开始分页
        PageHelper.startPage(input.getPage(),input.getLimit());
        List<Account> data;
        //检查参数
        if(input.getName()!=null&&!"".equals(input.getName()))
            data=accountDao.selectAllByName(input.getName(),input.getSort());
        else if(input.getRole()!=null&&!"".equals(input.getRole()))
            data=accountDao.selectAllByRole(input.getRole(),input.getSort());
        else
            data=accountDao.selectAll(input.getSort());
        //
        for(Account account:data)
            account.setPassword(null);
        //
        PageInfo<Account> pageInfo=new PageInfo<>(data);
        return pageInfo;
    }

    private Account getUpdatedAccount(String role, Account oldAccount, Account newAccount){
        String newPass=newAccount.getPassword();
        //如果新密码为空，则保留之前的密码
        if(newPass==null||"".equals(newPass)){
            newAccount.setPassword(oldAccount.getPassword());
        }
        else {
            //密码加密
            newAccount.setPassword(EncryptionUtil.encryptText(newPass));
        }

        //如果新的真实姓名为空，则也保留之前的
        String newName=newAccount.getName();
        if(newName==null||"".equals(newName))
            newAccount.setName(oldAccount.getName());

        //如果新的role为空或者不符合要求，则保留以前的
        String newRole=newAccount.getRole();
        if(newRole==null||(!newRole.equals("admin")&&!newRole.equals("manager")&&!newRole.equals("shop")&&!newRole.equals("buyer")))
            newAccount.setRole(oldAccount.getRole());

        //管理员
        if(role.equalsIgnoreCase("admin")){
            //帐号的新的角色不是分店，则应该把 shop_ip设为 null
            if(!newAccount.getRole().equals("shop"))
                newAccount.setShop_id(null);

            return newAccount;
        }
        //非管理员用户，不支持修改 role（职务）和 shop_id(所属分店 id)
        else {
            newAccount.setRole(oldAccount.getRole());
            newAccount.setShop_id(oldAccount.getShop_id());
            return newAccount;
        }
    }

    @Override
    public Map deleteAccount(Account account) {
        Map result=new HashMap();
        //检查username是否为空
        if(account.getUsername()==null)
            return ResultUtil.getErrorRes("删除失败：账号信息缺失");
        //
        Account temp=accountDao.selectOneByUsername(account.getUsername());
        if(temp==null)
            return ResultUtil.getErrorRes("删除失败：账号不存在");
        //
        if(accountDao.deleteAccount(account.getUsername())){
            result.put("status","success");
            return result;
        }
        else
            return ResultUtil.getErrorRes("删除账号失败");
    }
}
