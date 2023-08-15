//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package nc.pubimpl.so.m30.so.m33;

import com.yonyou.cloud.ncc.NCCSagas;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.so.m30.plugin.ServicePlugInPoint;
import nc.bs.so.m30.rule.credit.RenovateARByBidsBeginRule;
import nc.bs.so.m30.rule.credit.RenovateARByBidsEndRule;
import nc.impl.pubapp.env.BSContext;
import nc.impl.pubapp.pattern.data.view.ViewQuery;
import nc.impl.pubapp.pattern.data.view.ViewUpdate;
import nc.impl.pubapp.pattern.pub.LockOperator;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.itf.so.m30.compensate.ISaleOrderToRmSagasCompensate;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubimpl.so.m30.so.m33.rule.RewriteInvoiceStateRule;
import nc.pubitf.so.m30.so.m33.IRewrite30For33;
import nc.pubitf.so.m30.so.m33.Rewrite33Para;
import nc.pubitf.so.m30.so.m33.Rewrite33RmPara;
import nc.vo.credit.engrossmaintain.pub.action.M30EngrossAction;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.bill.CombineBill;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.log.TimeLog;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.scmpub.res.billtype.SOBillType;
import nc.vo.scmpub.util.SagasUtil;
import nc.vo.so.m30.entity.SaleOrderBVO;
import nc.vo.so.m30.entity.SaleOrderVO;
import nc.vo.so.m30.entity.SaleOrderViewVO;

public class Rewrite30For33Impl implements IRewrite30For33 {
    public Rewrite30For33Impl() {
    }

    public void rewrite30RMForConrim(Rewrite33RmPara[] paras) throws BusinessException {
        List<String> orderBidPks = new ArrayList();
        Rewrite33RmPara[] var3 = paras;
        int var4 = paras.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Rewrite33RmPara para = var3[var5];
            orderBidPks.add(para.getCsaleorderbid());
        }

        if (orderBidPks.size() > 0) {
            if (!SagasUtil.isNccnativeMerged()) {
                String[] ids = (String[])orderBidPks.toArray(new String[orderBidPks.size()]);
                ViewQuery<SaleOrderViewVO> bo = new ViewQuery(SaleOrderViewVO.class);
                bo.setSharedHead(true);
                SaleOrderViewVO[] views = (SaleOrderViewVO[])bo.query(ids);
                SaleOrderVO[] vos = this.viewToSaleOrderVO(views);
                SagasUtil.frozenAndAddSagaForHead(vos, SOBillType.Order.getCode());
                Map<String, Serializable> paraMap = new HashMap();
                paraMap.put("operater", "rewrite");
                paraMap.put("rewritePara", paras);
                NCCSagas.compensate(ISaleOrderToRmSagasCompensate.class, paraMap);
            }

            this.rewrite30RMFor33(paras);
        }

    }

    private SaleOrderVO[] viewToSaleOrderVO(SaleOrderViewVO[] views) {
        int len = views.length;
        SaleOrderVO[] bills = new SaleOrderVO[len];

        for(int i = 0; i < len; ++i) {
            bills[i] = views[i].changeToSaleOrderVO();
        }

        CombineBill<SaleOrderVO> cb = new CombineBill();
        cb.appendKey("csaleorderid");
        return (SaleOrderVO[])cb.combine(bills);
    }

    public void rewrite30RMFor33(Rewrite33RmPara[] paras) throws BusinessException {
        Map<String, Rewrite33RmPara> index = this.prepareParamsRm(paras);
        SaleOrderViewVO[] views = this.queryRm(index);
        this.setRmMny(views, index);
        String[] names = new String[]{"deferralmny", "revconfirmmny"};
        ViewUpdate<SaleOrderViewVO> bo = new ViewUpdate();
        bo.update(views, SaleOrderBVO.class, names);
        TimeLog.info("更新数据库");
    }

    public void rewrite30ARFor33(Rewrite33Para[] paras) throws BusinessException {
        try {
            Map<String, Rewrite33Para> index = this.prepareParams(paras);
            //begin 罕王销售二开，发票来源不是销售订单，没有回写
            String[] ids = this.lockBills(index);
            //查询来源是否为客户结算单
            String sql=" select pk_settlement_b from so_settlement_b where pk_settlement_b='"+ids[0]+"'";
            BaseDAO dao=new BaseDAO();
            Object n=dao.executeQuery(sql, new ColumnProcessor());
            if(n!=null){
                return;
            }
            //end

            BSContext.getInstance().setSession(Rewrite33Para.class.getName(), index);
            SaleOrderViewVO[] views = this.query(index);
            AroundProcesser<SaleOrderViewVO> processer = new AroundProcesser(ServicePlugInPoint.rewrite30ARFor33);
            this.addRule(processer, M30EngrossAction.M30ArReWrite);
            processer.before(views);
            this.setARNumMny(views, index);
            String[] names = new String[]{"ntotalarnum", "ntotalarmny"};
            this.rewrite(views, names);
            processer.after(views);
            BSContext.getInstance().removeSession(Rewrite33Para.class.getName());
        } catch (RuntimeException var6) {
            ExceptionUtils.marsh(var6);
        }

    }

    public void rewrite30ETFor33(Rewrite33Para[] paras) throws BusinessException {
        Map<String, Rewrite33Para> index = this.prepareParams(paras);
        BSContext.getInstance().setSession(Rewrite33Para.class.getName(), index);
        SaleOrderViewVO[] views = this.query(index);
        AroundProcesser<SaleOrderViewVO> processer = new AroundProcesser(ServicePlugInPoint.rewrite30ETFor33);
        this.addRule(processer, M30EngrossAction.M30EstArReWrite);
        processer.before(views);
        this.setETNumMny(views, index);
        String[] names = new String[]{"ntotalestarmny", "ntotalestarnum"};
        this.rewrite(views, names);
        processer.after(views);
        BSContext.getInstance().removeSession(Rewrite33Para.class.getName());
    }

    public void rewrite30IAFor33(Rewrite33Para[] paras) throws BusinessException {
        Map<String, Rewrite33Para> index = this.prepareParams(paras);
        // begin 罕王销售二开，发票来源不是销售订单，没有回写
        String[] ids = this.lockBills(index);
        //查询来源是否为客户结算单
        String sql=" select pk_settlement_b from so_settlement_b where pk_settlement_b='"+ids[0]+"'";
        BaseDAO dao=new BaseDAO();
        Object n=dao.executeQuery(sql, new ColumnProcessor());
        if(n!=null){
            return;
        }
        // end

        BSContext.getInstance().setSession(Rewrite33Para.class.getName(), index);
        SaleOrderViewVO[] views = this.query(index);
        this.setCostNum(views, index);
        String[] names = new String[]{"ntotalcostnum"};
        this.rewrite(views, names);
        BSContext.getInstance().removeSession(Rewrite33Para.class.getName());
    }

    public void rewrite30OutRushFor33(Rewrite33Para[] paras) throws BusinessException {
        Map<String, Rewrite33Para> index = this.prepareParams(paras);
        BSContext.getInstance().setSession(Rewrite33Para.class.getName(), index);
        SaleOrderViewVO[] views = this.query(index);
        AroundProcesser<SaleOrderViewVO> processer = new AroundProcesser(ServicePlugInPoint.rewrite30OutRushFor33);
        this.addRuleForOutRush(processer, M30EngrossAction.M30RushReWrite);
        processer.before(views);
        this.setOutRushNum(views, index);
        String[] names = new String[]{"ntotalrushnum"};
        this.rewrite(views, names);
        processer.after(views);
        BSContext.getInstance().removeSession(Rewrite33Para.class.getName());
    }

    private void addRule(AroundProcesser<SaleOrderViewVO> processer, M30EngrossAction engrossAction) {
        processer.addBeforeRule(new RenovateARByBidsBeginRule(engrossAction));
        processer.addAfterRule(new RenovateARByBidsEndRule(engrossAction));
    }

    private void addRuleForOutRush(AroundProcesser<SaleOrderViewVO> processer, M30EngrossAction engrossAction) {
        processer.addBeforeRule(new RenovateARByBidsBeginRule(engrossAction));
        processer.addAfterRule(new RenovateARByBidsEndRule(engrossAction));
        processer.addAfterRule(new RewriteInvoiceStateRule());
    }

    private String[] lockBills(Map<String, Rewrite33Para> index) {
        int size = index.size();
        String[] bids = new String[size];
        bids = (String[])index.keySet().toArray(bids);
        LockOperator locker = new LockOperator();
        String message = NCLangRes4VoTransl.getNCLangRes().getStrByID("4006011_0", "04006011-0187");
        locker.lock(bids, message);
        return bids;
    }

    private String[] lockBillRms(Map<String, Rewrite33RmPara> index) {
        int size = index.size();
        String[] bids = new String[size];
        bids = (String[])index.keySet().toArray(bids);
        LockOperator locker = new LockOperator();
        String message = "销售结算回写销售订单收入确认的金额，锁销售订单表体失败";
        locker.lock(bids, message);
        return bids;
    }

    private Map<String, Rewrite33Para> prepareParams(Rewrite33Para[] paras) {
        Map<String, Rewrite33Para> index = new HashMap();
        Rewrite33Para[] var3 = paras;
        int var4 = paras.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Rewrite33Para para = var3[var5];
            index.put(para.getCsaleorderbid(), para);
        }

        return index;
    }

    private Map<String, Rewrite33RmPara> prepareParamsRm(Rewrite33RmPara[] paras) {
        Map<String, Rewrite33RmPara> index = new HashMap();
        Rewrite33RmPara[] var3 = paras;
        int var4 = paras.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Rewrite33RmPara para = var3[var5];
            if (null == index.get(para.getCsaleorderbid())) {
                index.put(para.getCsaleorderbid(), para);
            } else {
                Rewrite33RmPara old = (Rewrite33RmPara)index.get(para.getCsaleorderbid());
                if (old.getRmtype().equals(para.getRmtype())) {
                    old.setNarmny(MathTool.add(old.getNarmny(), para.getNarmny()));
                }
            }
        }

        return index;
    }

    private SaleOrderViewVO[] query(Map<String, Rewrite33Para> index) {
        String[] ids = this.lockBills(index);
        ViewQuery<SaleOrderViewVO> bo = new ViewQuery(SaleOrderViewVO.class);
        bo.setSharedHead(true);
        SaleOrderViewVO[] views = (SaleOrderViewVO[])bo.query(ids);
        if (views.length != index.size()) {
            String message = NCLangRes4VoTransl.getNCLangRes().getStrByID("4006011_0", "04006011-0171");
            ExceptionUtils.wrappBusinessException(message);
        }

        return views;
    }

    private SaleOrderViewVO[] queryRm(Map<String, Rewrite33RmPara> index) {
        String[] ids = this.lockBillRms(index);
        ViewQuery<SaleOrderViewVO> bo = new ViewQuery(SaleOrderViewVO.class);
        bo.setSharedHead(true);
        SaleOrderViewVO[] views = (SaleOrderViewVO[])bo.query(ids);
        if (views.length != index.size()) {
            String message = NCLangRes4VoTransl.getNCLangRes().getStrByID("4006011_0", "04006011-0171");
            ExceptionUtils.wrappBusinessException(message);
        }

        return views;
    }

    private void rewrite(SaleOrderViewVO[] views, String[] names) {
        AroundProcesser<SaleOrderViewVO> processer = new AroundProcesser(ServicePlugInPoint.rewrite30NumFor33);
        TimeLog.logStart();
        processer.before(views);
        TimeLog.info("写数据库前执行业务规则");
        TimeLog.logStart();
        ViewUpdate<SaleOrderViewVO> bo = new ViewUpdate();
        SaleOrderViewVO[] retviews = (SaleOrderViewVO[])bo.update(views, SaleOrderBVO.class, names);
        TimeLog.info("更新数据库");
        TimeLog.logStart();
        processer.after(retviews);
        TimeLog.info("写数据库后执行业务规则");
    }

    private void setARNumMny(SaleOrderViewVO[] vos, Map<String, Rewrite33Para> index) {
        UFDouble ntotalarnum = null;
        UFDouble ntotalarmny = null;
        SaleOrderBVO body = null;
        SaleOrderViewVO[] var6 = vos;
        int var7 = vos.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            SaleOrderViewVO vo = var6[var8];
            body = vo.getBody();
            Rewrite33Para para = (Rewrite33Para)index.get(body.getCsaleorderbid());
            ntotalarnum = body.getNtotalarnum();
            ntotalarmny = body.getNtotalarmny();
            body.setNtotalarnum(MathTool.add(ntotalarnum, para.getNarnum()));
            body.setNtotalarmny(MathTool.add(ntotalarmny, para.getNarmny()));
        }

    }

    private void setRmMny(SaleOrderViewVO[] vos, Map<String, Rewrite33RmPara> index) {
        UFDouble deferralmny = null;
        UFDouble revconfirmmny = null;
        SaleOrderBVO body = null;
        SaleOrderViewVO[] var6 = vos;
        int var7 = vos.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            SaleOrderViewVO vo = var6[var8];
            body = vo.getBody();
            Rewrite33RmPara para = (Rewrite33RmPara)index.get(body.getCsaleorderbid());
            if ("RMDC".equals(para.getRmtype())) {
                deferralmny = body.getDeferralmny();
                body.setDeferralmny(MathTool.add(deferralmny, para.getNarmny()));
            } else if ("RMCF".equals(para.getRmtype())) {
                revconfirmmny = body.getRevconfirmmny();
                body.setRevconfirmmny(MathTool.add(revconfirmmny, para.getNarmny()));
            }
        }

    }

    private void setCostNum(SaleOrderViewVO[] vos, Map<String, Rewrite33Para> index) {
        SaleOrderBVO body = null;
        SaleOrderViewVO[] var5 = vos;
        int var6 = vos.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            SaleOrderViewVO vo = var5[var7];
            body = vo.getBody();
            Rewrite33Para para = (Rewrite33Para)index.get(body.getCsaleorderbid());
            UFDouble ntotaletcostnum = body.getNtotalcostnum();
            body.setNtotalcostnum(MathTool.add(ntotaletcostnum, para.getNarnum()));
        }

    }

    private void setETNumMny(SaleOrderViewVO[] vos, Map<String, Rewrite33Para> index) {
        UFDouble ntotaletarnum = null;
        UFDouble ntotaletarmny = null;
        SaleOrderBVO body = null;
        SaleOrderViewVO[] var6 = vos;
        int var7 = vos.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            SaleOrderViewVO vo = var6[var8];
            body = vo.getBody();
            Rewrite33Para para = (Rewrite33Para)index.get(body.getCsaleorderbid());
            ntotaletarnum = body.getNtotalestarnum();
            ntotaletarmny = body.getNtotalestarmny();
            body.setNtotalestarnum(MathTool.add(ntotaletarnum, para.getNarnum()));
            body.setNtotalestarmny(MathTool.add(ntotaletarmny, para.getNarmny()));
        }

    }

    private void setOutRushNum(SaleOrderViewVO[] vos, Map<String, Rewrite33Para> index) {
        SaleOrderBVO body = null;
        SaleOrderViewVO[] var5 = vos;
        int var6 = vos.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            SaleOrderViewVO vo = var5[var7];
            body = vo.getBody();
            Rewrite33Para para = (Rewrite33Para)index.get(body.getCsaleorderbid());
            UFDouble ntotaloutrushnum = body.getNtotalrushnum();
            body.setNtotalrushnum(MathTool.add(ntotaloutrushnum, para.getNarnum()));
        }

    }
}
