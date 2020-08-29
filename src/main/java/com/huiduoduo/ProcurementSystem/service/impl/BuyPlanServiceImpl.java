package com.huiduoduo.ProcurementSystem.service.impl;

import com.huiduoduo.ProcurementSystem.dao.AccountDao;
import com.huiduoduo.ProcurementSystem.dao.BuyPlanBuyerDao;
import com.huiduoduo.ProcurementSystem.dao.BuyPlanDao;
import com.huiduoduo.ProcurementSystem.domain.Account;
import com.huiduoduo.ProcurementSystem.domain.BuyPlan;
import com.huiduoduo.ProcurementSystem.domain.BuyPlanBuyer;
import com.huiduoduo.ProcurementSystem.service.BuyPlanService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author TanJifeng
 * @Description
 * @date 2020/8/29 20:37
 */
@Service
public class BuyPlanServiceImpl implements BuyPlanService {
    @Autowired(required = false)
    private BuyPlanDao buyPlanDao;
    @Autowired(required = false)
    private BuyPlanBuyerDao buyPlanBuyerDao;
    @Autowired(required = false)
    private AccountDao accountDao;
    @Autowired
    private HttpServletRequest request;

    //获取登陆用户信息
    private Account getLoginInfo(){
        return (Account) request.getSession().getAttribute("info");
    }

    @Override
    public Map add(BuyPlan buyPlan) {
        //检查权限
        Account login_info=getLoginInfo();
        if(!"manager".equals(login_info.getRole()))
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //数据检查
        if(buyPlan.getBuy_plan_buyer()==null||buyPlan.getBuy_plan_buyer().size()==0)
            return ResultUtil.getErrorRes("操作失败：具体的分配方案为空");
        if(buyPlan.getPlan_name()==null)
            buyPlan.setPlan_name("未命名方案");

        //添加采购经理信息
        buyPlan.setManager_username(login_info.getUsername());
        //添加失败
        if(!buyPlanDao.add(buyPlan))
            return ResultUtil.getErrorRes("操作失败：方案添加进数据库失败");
        //获取方案id
        int plan_id=buyPlanDao.getMaxId(login_info.getUsername());

        //循环处理具体的分配信息
        List<BuyPlanBuyer> buyPlanBuyers=buyPlan.getBuy_plan_buyer();
        for(BuyPlanBuyer planBuyer:buyPlanBuyers){
            //检查关键信息
            Account target=accountDao.selectOneByUsername(planBuyer.getBuyer_username());
            if(target==null||!"buyer".equals(target.getRole()))
                return ResultUtil.getErrorRes("操作失败：账号"+planBuyer.getBuyer_username()+"不存在或者不是采购员");
            if(planBuyer.getGoods_id()==null)
                return ResultUtil.getErrorRes("操作失败：货品编号不能为空");

            //设置方案id
            planBuyer.setBuy_plan_id(plan_id);
            //添加
            buyPlanBuyerDao.add(planBuyer);
        }

        return ResultUtil.getSuccessRes();
    }

    @Override
    public Map delete(BuyPlan buyPlan) {
        //检查权限
        Account login_info=getLoginInfo();
        if(!"manager".equals(login_info.getRole()))
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //数据检查
        BuyPlan temp=buyPlanDao.selectOneById(buyPlan.getId());
        if(buyPlan.getId()==null||temp==null)
            return ResultUtil.getErrorRes("操作失败：方案编号"+buyPlan.getId()+"不存在");

        //检查是否可删除
        if(!temp.getManager_username().equals(login_info.getUsername()))
            return ResultUtil.getErrorRes("操作失败：目标方案不属于目前登陆用户");

        //删除
        if(buyPlanDao.delete(buyPlan.getId()))
            return ResultUtil.getSuccessRes();
        else
            return ResultUtil.getErrorRes("操作失败：更新数据库失败");
    }

    @Override
    public Map getBuyPlans() {
        //检查权限
        Account login_info=getLoginInfo();
        if(!"manager".equals(login_info.getRole()))
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //获取方案信息
        List<BuyPlan> buyPlans=buyPlanDao.selectByMangerName(login_info.getUsername());
        //循环获取具体的分配信息、
        if(buyPlans!=null)
            for (int i=0;i<buyPlans.size();i++){
                BuyPlan temp=buyPlans.get(i);
                List<BuyPlanBuyer> buyPlanBuyers=buyPlanBuyerDao.selectByPlanId(temp.getId());
                buyPlans.get(i).setBuy_plan_buyer(buyPlanBuyers);
            }

        return ResultUtil.getSuccessRes(buyPlans);
    }

    @Override
    public Map update(BuyPlan buyPlan) {
        //检查权限
        Account login_info=getLoginInfo();
        if(!"manager".equals(login_info.getRole()))
            return ResultUtil.getErrorRes("操作失败：没有权限进行此操作");

        //数据检查
        BuyPlan temp=buyPlanDao.selectOneById(buyPlan.getId());
        if(buyPlan.getId()==null||temp==null)
            return ResultUtil.getErrorRes("操作失败：方案编号"+buyPlan.getId()+"不存在");

        //检查是否可修改
        if(!temp.getManager_username().equals(login_info.getUsername()))
            return ResultUtil.getErrorRes("操作失败：目标方案不属于目前登陆用户");

        //更新
        if(buyPlan.getPlan_name()==null)
            buyPlan.setPlan_name("未命名方案");
        buyPlanDao.update(buyPlan);

        //循环处理具体分配信息
        List<BuyPlanBuyer> buyPlanBuyers=buyPlan.getBuy_plan_buyer();
        if(buyPlanBuyers!=null&&buyPlanBuyers.size()>0){
            //删除原来的
            buyPlanBuyerDao.deleteByPlanId(buyPlan.getId());
            //
            for(BuyPlanBuyer buyPlanBuyer:buyPlanBuyers){
                //检查关键信息
                if(buyPlanBuyer.getGoods_id()==null||buyPlanBuyer.getBuyer_username()==null)
                    return ResultUtil.getErrorRes("操作失败：货品编号和分配的采购员都不能为空");
                Account target=accountDao.selectOneByUsername(buyPlanBuyer.getBuyer_username());
                if(!"buyer".equals(target.getRole()))
                    return ResultUtil.getErrorRes("操作失败：账号"+buyPlanBuyer.getBuyer_username()+"不是采购员");
                //添加新的
                buyPlanBuyer.setBuy_plan_id(buyPlan.getId());
                buyPlanBuyerDao.add(buyPlanBuyer);
            }
        }

        return ResultUtil.getSuccessRes();
    }
}
