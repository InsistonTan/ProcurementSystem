package com.huiduoduo.ProcurementSystem.service.impl;
import com.huiduoduo.ProcurementSystem.dao.ShopOrderPlanDao;
import com.huiduoduo.ProcurementSystem.dao.ShopOrderPlanGoodsDao;
import com.huiduoduo.ProcurementSystem.domain.Account;
import com.huiduoduo.ProcurementSystem.domain.ShopOrderPlan;
import com.huiduoduo.ProcurementSystem.domain.ShopOrderPlanGoods;
import com.huiduoduo.ProcurementSystem.service.ShopOrderPlanService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author TanJifeng
 * @Description ShopOrderPlanServiceImpl
 * @date 2020/8/29 19:39
 */
@Service
public class ShopOrderPlanServiceImpl implements ShopOrderPlanService {
    @Autowired(required = false)
    private ShopOrderPlanDao shopOrderPlanDao;
    @Autowired
    private HttpServletRequest request;
    @Autowired(required = false)
    private ShopOrderPlanGoodsDao shopOrderPlanGoodsDao;

    //获取登陆用户信息
    private Account getLoginInfo(){
        return (Account) request.getSession().getAttribute("info");
    }

    @Override
    public Map add(ShopOrderPlan orderPlan) {
        //检查权限
        Account login_info=getLoginInfo();
        //不是分店用户
        if(!"shop".equals(login_info.getRole()))
            return ResultUtil.getErrorRes("操作失败：没有权限");

        //数据检查
        if(orderPlan.getPlan_name()==null)
            orderPlan.setPlan_name("未命名方案");
        if(orderPlan.getOrder_goods()==null||orderPlan.getOrder_goods().size()==0)
            return ResultUtil.getErrorRes("操作失败：具体的订货信息为空");


        //设置shop_id
        orderPlan.setShop_id(login_info.getShop_id());
        //添加
        shopOrderPlanDao.add(orderPlan);
        //获取方案id
        int plan_id=shopOrderPlanDao.getMaxId(login_info.getShop_id());

        //循环添加具体的订货信息
        List<ShopOrderPlanGoods> shopOrderPlanGoods=orderPlan.getOrder_goods();
        for(ShopOrderPlanGoods planGoods:shopOrderPlanGoods){
            //检查关键数据
            if(planGoods.getGoods_id()==null)
                return ResultUtil.getErrorRes("操作失败：货品编号不能为空");
            //
            planGoods.setShop_plan_id(plan_id);
            //添加
            shopOrderPlanGoodsDao.add(planGoods);
        }

        return ResultUtil.getSuccessRes();
    }

    @Override
    public Map delete(ShopOrderPlan orderPlan) {
        //检查权限
        Account login_info=getLoginInfo();
        //不是分店用户
        if(!"shop".equals(login_info.getRole()))
            return ResultUtil.getErrorRes("操作失败：没有权限");

        //数据检查
        ShopOrderPlan shopOrderPlan=shopOrderPlanDao.selectByID(orderPlan.getId());
        if(orderPlan.getId()==null||shopOrderPlan==null)
            return ResultUtil.getErrorRes("操作失败：方案编号"+orderPlan.getId()+"不存在");
        //检查目标方案是否可以删除
        if(shopOrderPlan.getShop_id()!=login_info.getShop_id())
            return ResultUtil.getErrorRes("操作失败：目标方案所属分店与该账号所属分店不一致");

        //删除
        if(shopOrderPlanDao.delete(orderPlan.getId()))
            return ResultUtil.getSuccessRes();
        else
            return ResultUtil.getErrorRes("操作失败：更新数据库失败");
    }

    @Override
    public Map getShopOrderPlans() {
        //检查权限
        Account login_info=getLoginInfo();
        //不是分店用户
        if(!"shop".equals(login_info.getRole()))
            return ResultUtil.getErrorRes("操作失败：没有权限");

        //查询
        List<ShopOrderPlan> shopOrderPlans=shopOrderPlanDao.selectByShopID(login_info.getShop_id());
        //循环获取具体订货信息
        if(shopOrderPlans!=null)
        for(int i=0;i<shopOrderPlans.size();i++){
            ShopOrderPlan orderPlan=shopOrderPlans.get(i);
            List<ShopOrderPlanGoods> shopOrderPlanGoods=shopOrderPlanGoodsDao.selectByPlanId(orderPlan.getId());
            shopOrderPlans.get(i).setOrder_goods(shopOrderPlanGoods);
        }

        return ResultUtil.getSuccessRes(shopOrderPlans);
    }

    @Override
    public Map update(ShopOrderPlan orderPlan) {
        //检查权限
        Account login_info=getLoginInfo();
        //不是分店用户
        if(!"shop".equals(login_info.getRole()))
            return ResultUtil.getErrorRes("操作失败：没有权限");

        //数据检查
        ShopOrderPlan shopOrderPlan=shopOrderPlanDao.selectByID(orderPlan.getId());
        if(orderPlan.getId()==null||shopOrderPlan==null)
            return ResultUtil.getErrorRes("操作失败：方案编号"+orderPlan.getId()+"不存在");
        //检查目标方案是否可以修改
        if(shopOrderPlan.getShop_id()!=login_info.getShop_id())
            return ResultUtil.getErrorRes("操作失败：目标方案所属分店与该账号所属分店不一致");

        //更新
        if(orderPlan.getPlan_name()==null)
            orderPlan.setPlan_name("未命名方案");
        shopOrderPlanDao.update(orderPlan);

        //更新具体的订货信息
        List<ShopOrderPlanGoods> planGoodss=orderPlan.getOrder_goods();
        if(planGoodss!=null&&planGoodss.size()>0){
            //删除原来的
            shopOrderPlanGoodsDao.deleteByPlanId(orderPlan.getId());
            //添加最新的
            for(ShopOrderPlanGoods planGoods:planGoodss){
                //检查关键数据
                if(planGoods.getGoods_id()==null)
                    return ResultUtil.getErrorRes("操作失败：货品编号不能为空");
                //
                planGoods.setShop_plan_id(orderPlan.getId());
                //添加
                shopOrderPlanGoodsDao.add(planGoods);
            }
        }

        return ResultUtil.getSuccessRes();
    }
}
