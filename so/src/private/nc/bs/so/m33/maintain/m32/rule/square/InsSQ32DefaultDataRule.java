//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package nc.bs.so.m33.maintain.m32.rule.square;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.scmpub.reference.uap.bd.payterm.PaytermService;
import nc.itf.scmpub.reference.uap.org.CostRegionPubService;
import nc.itf.scmpub.reference.uap.org.StockOrgPubService;
import nc.itf.so.m33.ref.so.m30.SOSaleOrderServicesUtil;
import nc.itf.so.m33.ref.to.settlerule.TOSettleRuleServicesUtil;
import nc.pubitf.to.settlerule.ic.MatchSettleRuleResult;
import nc.pubitf.to.settlerule.so.MatchSettleRuleVOForSo;
import nc.vo.bd.income.IncomeVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.scmpub.res.billtype.ICBillType;
import nc.vo.scmpub.res.billtype.SOBillType;
import nc.vo.so.m30.entity.SaleOrderViewVO;
import nc.vo.so.m33.m32.entity.SquareInvBVO;
import nc.vo.so.m33.m32.entity.SquareInvHVO;
import nc.vo.so.m33.m32.entity.SquareInvVO;
import nc.vo.so.m33.m32.entity.SquareInvVOUtils;
import nc.vo.so.m33.m32.entity.SquareInvViewVO;
import nc.vo.so.m33.pub.biz.toia.ProcessToIA;
import nc.vo.so.m33.pub.biz.toia.ProcessToIAPara;
import nc.vo.so.pub.util.AggVOUtil;
import nc.vo.so.pub.votools.SoVoTools;
import nc.vo.trade.checkrule.VOChecker;

public class InsSQ32DefaultDataRule implements IRule<SquareInvVO> {
    public InsSQ32DefaultDataRule() {
    }

    public void process(SquareInvVO[] vos) {
        String[] vsrctype = (String[])AggVOUtil.getDistinctItemFieldArray(vos, "vsrctype", String.class);
        SquareInvViewVO[] svvos = SquareInvVOUtils.getInstance().changeToSaleSquareViewVO(vos);
        // 从订单补充数据
        // 罕王不需要从销售订单补充数据，因为罕王的发票不来源自销售订单，来源于新开发的单据
        if(!"KHJS".equals(svvos[0].getItem().getVsrctype())){
            if (SOBillType.INITOUTREG.getCode().equals(vsrctype[0])) {
                this.setDataFromInitoutreg(svvos);
            } else {
                this.setDataFromSaleOrder(svvos);
            }
        }


        this.setBCostFlag(svvos, vsrctype);
        this.setARFlag(svvos);
        this.setEffectdate(svvos);
    }

    private void setBCostFlag(SquareInvViewVO[] svvos, String[] vsrctype) {
        List<SquareInvViewVO[]> list = this.filterSingleSpanData(svvos);
        SquareInvViewVO[] singlevos = (SquareInvViewVO[])list.get(0);
        SquareInvViewVO[] spanlevos = (SquareInvViewVO[])list.get(1);
        if (SOBillType.INITOUTREG.getCode().equals(vsrctype[0])) {
            String ccostorgid = this.queryCostByInit(svvos);
            SquareInvViewVO[] var7 = svvos;
            int var8 = svvos.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                SquareInvViewVO view = var7[var9];
                view.getItem().setCcostorgid(ccostorgid);
            }
        } else {
            this.setCostOrgForSingle(singlevos);
        }

        this.setBCostForPubFlag(svvos);
        this.setBCostForSingleFlag(singlevos);
        this.setBCostForSpanFlag(spanlevos);
    }

    private String queryCostByInit(SquareInvViewVO[] svvos) {
        String[] ordhids = SoVoTools.getVOsOnlyValues(SquareInvVOUtils.getInstance().getSquareInvBVO(svvos), "cfirstid");
        DataAccessUtils util = new DataAccessUtils();
        SqlBuilder sql = new SqlBuilder();
        sql.append(" select ccostorgid  from  so_initoutreg where initoutregid = '" + ordhids[0] + "' and dr = 0");
        IRowSet rs = util.query(sql.toString());

        String ccostorgid;
        for(ccostorgid = null; rs.next(); ccostorgid = rs.getString(0)) {
        }

        return ccostorgid;
    }

    private void setBCostForSingleFlag(SquareInvViewVO[] singlevos) {
        if (null != singlevos && singlevos.length != 0) {
            List<SquareInvViewVO> l_single_cost = new ArrayList();
            SquareInvViewVO[] svvos = singlevos;
            int var4 = singlevos.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                SquareInvViewVO view = svvos[var5];
                if (view.getItem().getBcost().booleanValue()) {
                    l_single_cost.add(view);
                }
            }

            if (l_single_cost.size() != 0) {
                svvos = new SquareInvViewVO[l_single_cost.size()];
                l_single_cost.toArray(svvos);
                ProcessToIAPara[] paras = this.getProcessToIAPara(svvos);
                ProcessToIA iaprc = new ProcessToIA();
                Map<String, UFBoolean> ret = iaprc.getSingleToIAResult(paras);
                int index = 0;
                SquareInvViewVO[] var8 = svvos;
                int var9 = svvos.length;

                for(int var10 = 0; var10 < var9; ++var10) {
                    SquareInvViewVO view = var8[var10];
                    UFBoolean flag = (UFBoolean)ret.get(String.valueOf(index));
                    view.getItem().setBcost(flag);
                    ++index;
                }

            }
        }
    }

    private List<SquareInvViewVO[]> filterSingleSpanData(SquareInvViewVO[] svvos) {
        Map<String, String> m_st_fin = this.queryFinanceOrgIDByStockOrgID(svvos);
        List<SquareInvViewVO> l_single = new ArrayList();
        List<SquareInvViewVO> l_span = new ArrayList();
        SquareInvViewVO[] var5 = svvos;
        int size = svvos.length;

        for(int var7 = 0; var7 < size; ++var7) {
            SquareInvViewVO view = var5[var7];
            String pkorg = view.getHead().getPk_org();
            String sendstock = view.getItem().getCsendstockorgid();
            boolean bfeediscount = view.getItem().getBlaborflag().booleanValue() || view.getItem().getBdiscountflag().booleanValue();
            if (!PubAppTool.isNull(sendstock) && !bfeediscount) {
                if (pkorg.equals(m_st_fin.get(sendstock))) {
                    l_single.add(view);
                } else {
                    l_span.add(view);
                }
            } else {
                view.getItem().setBcost(UFBoolean.FALSE);
            }
        }

        List<SquareInvViewVO[]> list = new ArrayList();
        size = l_single.size();
        if (size > 0) {
            list.add(l_single.toArray(new SquareInvViewVO[size]));
        } else {
            list.add((SquareInvViewVO[]) null);
        }

        size = l_span.size();
        if (size > 0) {
            list.add(l_span.toArray(new SquareInvViewVO[size]));
        } else {
            list.add((SquareInvViewVO[]) null);
        }

        return list;
    }

    private Map<MatchSettleRuleVOForSo, MatchSettleRuleResult> getMatchSettleRuleResult(SquareInvViewVO[] svvos) {
        List<MatchSettleRuleVOForSo> l_matchSettle = new ArrayList();
        SquareInvViewVO[] var3 = svvos;
        int var4 = svvos.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            SquareInvViewVO view = var3[var5];
            MatchSettleRuleVOForSo mso = this.getMatchSettleRuleVOForSo(view.getHead(), view.getItem());
            l_matchSettle.add(mso);
        }

        Map<MatchSettleRuleVOForSo, MatchSettleRuleResult> m_index_cr = null;

        try {
            m_index_cr = TOSettleRuleServicesUtil.matchSettleRule(l_matchSettle);
        } catch (BusinessException var8) {
            ExceptionUtils.wrappBusinessException(var8.getMessage());
        }

        if (m_index_cr == null || m_index_cr.size() == 0) {
            ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4006010_0", "04006010-0036"));
        }

        return m_index_cr;
    }

    private MatchSettleRuleVOForSo getMatchSettleRuleVOForSo(SquareInvHVO hvo, SquareInvBVO bvo) {
        MatchSettleRuleVOForSo mstoVo = new MatchSettleRuleVOForSo();
        mstoVo.setPk_group(hvo.getPk_group());
        mstoVo.setCtranstype(bvo.getVfirsttrantype());
        mstoVo.setCoutstockorgid(bvo.getCsendstockorgid());
        mstoVo.setCinventoryid(bvo.getCmaterialid());
        mstoVo.setCinfinanceorgid(bvo.getPk_org());
        return mstoVo;
    }

    private ProcessToIAPara[] getProcessToIAPara(SquareInvViewVO[] svvos) {
        ProcessToIAPara[] paras = new ProcessToIAPara[svvos.length];
        int index = 0;
        SquareInvViewVO[] var4 = svvos;
        int var5 = svvos.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            SquareInvViewVO view = var4[var6];
            SquareInvBVO bvo = view.getItem();
            String saleOutTransType = null;
            if (ICBillType.SaleOut.getCode().equals(bvo.getVsrctype())) {
                saleOutTransType = bvo.getVsrctrantype();
            }

            String materialvid = bvo.getCmaterialvid();
            String stordocid = bvo.getCsendstordocid();
            String pk_org = view.getHead().getPk_org();
            paras[index] = new ProcessToIAPara();
            paras[index].setId(String.valueOf(index));
            paras[index].setFinorgoid(pk_org);
            paras[index].setMaterialvid(materialvid);
            paras[index].setSaleOutTransType(saleOutTransType);
            paras[index].setStordocid(stordocid);
            paras[index].setBdiscountflag(ValueUtils.getBoolean(bvo.getBdiscountflag()));
            paras[index].setBlaborflag(ValueUtils.getBoolean(bvo.getBlaborflag()));
            ++index;
        }

        return paras;
    }

    private Map<String, String> queryFinanceOrgIDByStockOrgID(SquareInvViewVO[] svvos) {
        SquareInvBVO[] bvos = SquareInvVOUtils.getInstance().getSquareInvBVO(svvos);
        Map<String, String> m_st_fin = null;
        m_st_fin = StockOrgPubService.queryFinanceOrgIDByStockOrgID(SoVoTools.getVOsOnlyValues(bvos, "csendstockorgid"));
        return m_st_fin;
    }

    private void setARFlag(SquareInvViewVO[] svvos) {
        SquareInvViewVO[] var2 = svvos;
        int var3 = svvos.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            SquareInvViewVO view = var2[var4];
            boolean blar = view.getItem().getBlargessflag().booleanValue();
            view.getItem().setBincome(UFBoolean.valueOf(!blar));
        }

    }

    private void setBCostForPubFlag(SquareInvViewVO[] svvos) {
        if (!VOChecker.isEmpty(svvos)) {
            ProcessToIAPara[] paras = this.getProcessToIAPara(svvos);
            ProcessToIA iaprc = new ProcessToIA();
            Map<String, UFBoolean> ret = iaprc.getPubToIAResult(paras);
            int index = 0;
            SquareInvViewVO[] var6 = svvos;
            int var7 = svvos.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                SquareInvViewVO view = var6[var8];
                UFBoolean flag = (UFBoolean)ret.get(String.valueOf(index));
                view.getItem().setBcost(flag);
                ++index;
            }
        }

    }

    private void setBCostForSpanFlag(SquareInvViewVO[] spanvos) {
        if (null != spanvos && spanvos.length != 0) {
            List<SquareInvViewVO> l_span_cost = new ArrayList();
            SquareInvViewVO[] spanlevos = spanvos;
            int var4 = spanvos.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                SquareInvViewVO view = spanlevos[var5];
                if (view.getItem().getBcost().booleanValue()) {
                    l_span_cost.add(view);
                }
            }

            if (l_span_cost.size() != 0) {
                spanlevos = new SquareInvViewVO[l_span_cost.size()];
                l_span_cost.toArray(spanlevos);
                Map<MatchSettleRuleVOForSo, MatchSettleRuleResult> m_index_cr = this.setCostOrgForSpan(spanlevos);
                if (VOChecker.isEmpty(m_index_cr)) {
                    ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4006010_0", "04006010-0036"));
                }

                if (!VOChecker.isEmpty(spanlevos)) {
                    SquareInvViewVO[] var12 = spanlevos;
                    int var13 = spanlevos.length;

                    for(int var7 = 0; var7 < var13; ++var7) {
                        SquareInvViewVO view = var12[var7];
                        MatchSettleRuleVOForSo para = this.getMatchSettleRuleVOForSo(view.getHead(), view.getItem());
                        MatchSettleRuleResult mrlt = (MatchSettleRuleResult)m_index_cr.get(para);
                        if (mrlt == null) {
                            ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4006010_0", "04006010-0036"));
                        } else {
                            view.getItem().setBcost(mrlt.getSendtoia());
                        }
                    }
                }

            }
        }
    }

    private void setCostOrgByFinance(SquareInvViewVO[] svvos) {
        SquareInvBVO[] bvos = SquareInvVOUtils.getInstance().getSquareInvBVO(svvos);
        String[] financeorgids = SoVoTools.getVOsOnlyValues(bvos, "pk_org");
        Map<String, String> m_fico = null;
        m_fico = CostRegionPubService.getDefaultCostRegionMapByFinanceOrgIDS(financeorgids);
        if (m_fico != null) {
            SquareInvViewVO[] var5 = svvos;
            int var6 = svvos.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                SquareInvViewVO view = var5[var7];
                view.getItem().setCcostorgid((String)m_fico.get(view.getHead().getPk_org()));
            }
        }

    }

    private void setCostOrgBySpan(Map<MatchSettleRuleVOForSo, MatchSettleRuleResult> m_index_cr, SquareInvViewVO[] svvos) {
        List<SquareInvViewVO> l_ByFinanceList = this.setCostOrgByTOSettleRule(m_index_cr, svvos);
        int size = l_ByFinanceList.size();
        if (size > 0) {
            this.setCostOrgByFinance((SquareInvViewVO[])l_ByFinanceList.toArray(new SquareInvViewVO[size]));
        }

    }

    private void setCostOrgByStockOrgsAndStordocs(String[] stockorgids, String[] stordocids, SquareInvViewVO[] svvos) {
        Map<String, String> m_CostRegion = CostRegionPubService.queryCostRegionIDByStockOrgsAndStordocs(stockorgids, stordocids);
        if (m_CostRegion != null && m_CostRegion.size() != 0) {
            SquareInvBVO bvo = null;
            SquareInvViewVO[] var6 = svvos;
            int var7 = svvos.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                SquareInvViewVO view = var6[var8];
                bvo = view.getItem();
                String key = bvo.getCsendstockorgid() + bvo.getCsendstordocid();
                bvo.setCcostorgid((String)m_CostRegion.get(key));
            }

        }
    }

    private List<SquareInvViewVO> setCostOrgByTOSettleRule(Map<MatchSettleRuleVOForSo, MatchSettleRuleResult> m_index_cr, SquareInvViewVO[] svvos) {
        String costorgid = null;
        List<SquareInvViewVO> l_ByFinanceList = new ArrayList();
        SquareInvViewVO[] var5 = svvos;
        int var6 = svvos.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            SquareInvViewVO view = var5[var7];
            SquareInvBVO bvo = view.getItem();
            MatchSettleRuleVOForSo para = this.getMatchSettleRuleVOForSo(view.getHead(), bvo);
            MatchSettleRuleResult mrlt = (MatchSettleRuleResult)m_index_cr.get(para);
            if (mrlt == null) {
                ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4006010_0", "04006010-0036"));
            } else {
                costorgid = mrlt.getCostRegion();
                if (costorgid == null) {
                    l_ByFinanceList.add(view);
                } else {
                    bvo.setCcostorgid(costorgid);
                }
            }
        }

        return l_ByFinanceList;
    }

    private void setCostOrgForSingle(SquareInvViewVO[] singlevos) {
        if (null != singlevos && singlevos.length != 0) {
            List<String> s_stockOrg = new ArrayList();
            List<String> s_storddoc = new ArrayList();
            SquareInvViewVO[] var4 = singlevos;
            int var5 = singlevos.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                SquareInvViewVO view = var4[var6];
                String stockOrg = view.getItem().getCsendstockorgid();
                s_stockOrg.add(stockOrg);
                String storddoc = view.getItem().getCsendstordocid();
                s_storddoc.add(storddoc);
            }

            String[] stockorgids = (String[])s_stockOrg.toArray(new String[s_stockOrg.size()]);
            String[] stordocids = (String[])s_storddoc.toArray(new String[s_storddoc.size()]);
            this.setCostOrgByStockOrgsAndStordocs(stockorgids, stordocids, singlevos);
        }
    }

    private Map<MatchSettleRuleVOForSo, MatchSettleRuleResult> setCostOrgForSpan(SquareInvViewVO[] svvos) {
        Map<MatchSettleRuleVOForSo, MatchSettleRuleResult> m_index_cr = new HashMap();
        if (!VOChecker.isEmpty(svvos)) {
            m_index_cr = this.getMatchSettleRuleResult(svvos);
            this.setCostOrgBySpan((Map)m_index_cr, svvos);
        }

        return (Map)m_index_cr;
    }

    private void setDataFromSaleOrder(SquareInvViewVO[] svvos) {
        String[] ordbids = SoVoTools.getVOsOnlyValues(SquareInvVOUtils.getInstance().getSquareInvBVO(svvos), "cfirstbid");
        if (VOChecker.isEmpty(ordbids)) {
            ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4006010_0", "04006010-0037"));
        }

        Map<String, SaleOrderViewVO> map = new HashMap();

        int var6;
        SaleOrderViewVO m30view;
        try {
            SaleOrderViewVO[] views = SOSaleOrderServicesUtil.querySaleOrderViewVOs(ordbids, new String[]{"csaleorderbid", "cprodlineid", "cchanneltypeid", "vctcode"});
            if (VOChecker.isEmpty(views)) {
                ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4006010_0", "04006010-0038"));
            }

            SaleOrderViewVO[] var5 = views;
            var6 = views.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                m30view = var5[var7];
                map.put(m30view.getBody().getCsaleorderbid(), m30view);
            }
        } catch (BusinessException var10) {
            ExceptionUtils.wrappException(var10);
        }

        SquareInvViewVO[] var11 = svvos;
        int var12 = svvos.length;

        for(var6 = 0; var6 < var12; ++var6) {
            SquareInvViewVO view = var11[var6];
            m30view = (SaleOrderViewVO)map.get(view.getItem().getCfirstbid());
            if (!VOChecker.isEmpty(m30view)) {
                SquareInvBVO bvo = view.getItem();
                bvo.setCchanneltypeid(m30view.getHead().getCchanneltypeid());
                bvo.setCprolineid(m30view.getBody().getCprodlineid());
                bvo.setVctcode(m30view.getBody().getVctcode());
            }
        }

    }

    private void setEffectdate(SquareInvViewVO[] svvos) {
        String[] paytermids = SoVoTools.getVOsOnlyValues(SquareInvVOUtils.getInstance().getSquareInvHVO(svvos), "cpaytermid");
        Map<String, IncomeVO> map = null;
        if (!VOChecker.isEmpty(paytermids)) {
            map = PaytermService.queryIncomeByPk(paytermids);
        }

        if (VOChecker.isEmpty(map)) {
            this.setEffectdateWithNoPayterm(svvos);
        } else {
            this.setEffectdateWithPayterm(map, svvos);
        }

    }

    private void setEffectdateWithNoPayterm(SquareInvViewVO[] svvos) {
        SquareInvViewVO[] var2 = svvos;
        int var3 = svvos.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            SquareInvViewVO view = var2[var4];
            view.getItem().setDeffectdate(view.getHead().getDbilldate());
        }

    }

    private void setEffectdateWithPayterm(Map<String, IncomeVO> map, SquareInvViewVO[] svvos) {
        SquareInvViewVO[] var3 = svvos;
        int var4 = svvos.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            SquareInvViewVO view = var3[var5];
            IncomeVO pay = (IncomeVO)map.get(view.getHead().getCpaytermid());
            UFDate deffdate = pay.getEffectdate();
            view.getItem().setDeffectdate(deffdate);
        }

    }

    private void setDataFromInitoutreg(SquareInvViewVO[] svvos) {
        String[] ordbids = SoVoTools.getVOsOnlyValues(SquareInvVOUtils.getInstance().getSquareInvBVO(svvos), "cfirstbid");
        if (VOChecker.isEmpty(ordbids)) {
            ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4006010_0", "04006010-0037"));
        }

    }
}
