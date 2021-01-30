package com.huiduoduo.ProcurementSystem.service.impl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huiduoduo.ProcurementSystem.dao.ShopOrderPlanDao;
import com.huiduoduo.ProcurementSystem.dao.ShopOrderPlanGoodsDao;
import com.huiduoduo.ProcurementSystem.domain.Account;
import com.huiduoduo.ProcurementSystem.domain.ShopOrderPlan;
import com.huiduoduo.ProcurementSystem.domain.ShopOrderPlanGoods;
import com.huiduoduo.ProcurementSystem.domain.pageBean.Page;
import com.huiduoduo.ProcurementSystem.service.ShopOrderPlanService;
import com.huiduoduo.ProcurementSystem.utils.ResultUtil;
import com.huiduoduo.ProcurementSystem.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
        String role=login_info.getRole();
        //不是分店用户,也不是采购经理
        if(!"shop".equals(role)&&!"manager".equals(role))
            return ResultUtil.getErrorRes("操作失败：没有权限");

        //数据检查
        if(orderPlan.getPlan_name()==null)
            orderPlan.setPlan_name("未命名方案");
        if(orderPlan.getOrder_goods()==null||orderPlan.getOrder_goods().size()==0)
            return ResultUtil.getErrorRes("操作失败：具体的订货信息为空");


        //设置shop_id(只有分店用户有，采购经理没有)
        if("shop".equals(role))
            orderPlan.setShop_id(login_info.getShop_id());

        //生成一个可用的方案 id
        int plan_id=1;
        for(;plan_id<1000000;plan_id++){
            ShopOrderPlan temp=shopOrderPlanDao.checkID(plan_id);
            if(temp==null)
                break;
        }
        //再次检查该 id，防止上面循环到底
        ShopOrderPlan temp=shopOrderPlanDao.checkID(plan_id);
        if(temp!=null)
            return ResultUtil.getErrorRes("操作失败：生成方案ID失败");

        orderPlan.setId(plan_id);
        //添加
        shopOrderPlanDao.add(orderPlan);

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
        String role=login_info.getRole();
        //不是分店用户,也不是采购经理
        if(!"shop".equals(role)&&!"manager".equals(role))
            return ResultUtil.getErrorRes("操作失败：没有权限");

        //数据检查
        ShopOrderPlan shopOrderPlan=shopOrderPlanDao.selectByID(orderPlan.getId());
        if(orderPlan.getId()==null||shopOrderPlan==null)
            return ResultUtil.getErrorRes("操作失败：方案编号"+orderPlan.getId()+"不存在");

        //分权限，检查目标方案是否可以删除
        if("shop".equals(role)){
            if(shopOrderPlan.getShop_id()!=null){
                if(shopOrderPlan.getShop_id()!=login_info.getShop_id())
                    return ResultUtil.getErrorRes("操作失败：该方案所属分店与该账号所属分店不一致");
            }
            else {
                return ResultUtil.getErrorRes("操作失败：无法删除该方案，需要删除方案请联系采购经理");
            }
        }
        else {
            if(shopOrderPlan.getShop_id()!=null)
                return ResultUtil.getErrorRes("操作失败：采购经理没有权限删除分店方案");
        }

        //删除
        if(shopOrderPlanDao.delete(orderPlan.getId()))
            return ResultUtil.getSuccessRes();
        else
            return ResultUtil.getErrorRes("操作失败：更新数据库失败");
    }

    @Override
    public Map getShopOrderPlans(Page page) {
        //检查权限
        Account login_info=getLoginInfo();
        String role=login_info.getRole();
        //不是分店用户,也不是采购经理
        if(!"shop".equals(role)&&!"manager".equals(role))
            return ResultUtil.getErrorRes("操作失败：没有权限");

        //检查参数
        if(page.getPage()==0)
            page.setPage(1);
        if(page.getLimit()==0)
            page.setLimit(20);

        //开始分页
        PageHelper.startPage(page.getPage(),page.getLimit());

        //查询
        List<ShopOrderPlan> shopOrderPlans = null;
        if("shop".equals(role)){
            //查询该分店的方案
            shopOrderPlans=shopOrderPlanDao.selectByShopID(login_info.getShop_id());
        }
        else{
            //采购经理查询所有方案
            shopOrderPlans=shopOrderPlanDao.selectManagerPlans();
        }

        //循环获取具体订货信息
        if(shopOrderPlans!=null)
        for(int i=0;i<shopOrderPlans.size();i++){
            ShopOrderPlan orderPlan=shopOrderPlans.get(i);
            List<ShopOrderPlanGoods> shopOrderPlanGoods=shopOrderPlanGoodsDao.selectByPlanId(orderPlan.getId());
            shopOrderPlans.get(i).setOrder_goods(shopOrderPlanGoods);
        }

        //生成PageInfo
        PageInfo<ShopOrderPlan> pageInfo=new PageInfo<>(shopOrderPlans);

        //返回结果
        Map result=new HashMap();
        result.put("status","success");
        result.put("data",shopOrderPlans);
        result.put("total",pageInfo.getTotal());
        return result;
    }

    @Override
    public Map update(ShopOrderPlan orderPlan) {
        //检查权限
        Account login_info=getLoginInfo();
        String role=login_info.getRole();
        //不是分店用户,也不是采购经理
        if(!"shop".equals(role)&&!"manager".equals(role))
            return ResultUtil.getErrorRes("操作失败：没有权限");

        //数据检查
        ShopOrderPlan shopOrderPlan=shopOrderPlanDao.selectByID(orderPlan.getId());
        if(orderPlan.getId()==null||shopOrderPlan==null)
            return ResultUtil.getErrorRes("操作失败：方案编号"+orderPlan.getId()+"不存在");

        //分权限，检查目标方案是否可以修改
        if("shop".equals(role)){
            if(shopOrderPlan.getShop_id()!=null){
                if(shopOrderPlan.getShop_id()!=login_info.getShop_id())
                    return ResultUtil.getErrorRes("操作失败：该方案所属分店与该账号所属分店不一致");
            }
            else {
                return ResultUtil.getErrorRes("操作失败：无法修改该方案，需要修改方案请联系采购经理");
            }

        }
        else {
            //采购经理修改分店方案（shop_id!=null代表分店方案）
            if(shopOrderPlan.getShop_id()!=null){
                //保留原来的shop_id
                orderPlan.setShop_id(shopOrderPlan.getShop_id());
                //添加经理修改的备注
                orderPlan.setNotice("[系统提醒]:采购经理于"+ TimeUtil.getTime("yyyy-MM-dd HH:mm")+"修改了此方案。");
            }
        }

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
