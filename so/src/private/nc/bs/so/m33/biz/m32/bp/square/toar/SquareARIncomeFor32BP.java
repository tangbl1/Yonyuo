//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package nc.bs.so.m33.biz.m32.bp.square.toar;

import java.util.ArrayList;
import java.util.List;
import nc.bs.framework.common.NCLocator;
import nc.bs.so.m33.biz.m32.rule.toar.GetNewARorgVidFor32Rule;
import nc.bs.so.m33.biz.m32.rule.toar.SquareARCloseFor32Rule;
import nc.bs.so.m33.biz.m32.rule.toar.ToARCheckFor32Rule;
import nc.bs.so.m33.maintain.m32.InsertSquare32DetailBP;
import nc.bs.so.m33.maintain.m32.rule.detail.RewriteARIncomeFor32Rule;
import nc.bs.so.m33.plugin.BPPlugInPoint;
import nc.impl.pubapp.env.BSContext;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.arap.receivable.AggReceivableBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.scmpub.res.billtype.SOBillType;
import nc.vo.scmpub.util.SagasUtil;
import nc.vo.scmpub.util.TransferSortUtil;
import nc.vo.so.m33.m32.entity.SquareInvDetailVO;
import nc.vo.so.m33.m32.entity.SquareInvVO;
import nc.vo.so.m33.m32.entity.SquareInvVOUtils;
import nc.vo.so.m33.m32.entity.SquareInvViewVO;
import nc.vo.so.m33.pub.context.m32.SaleInvoiceContextForArap;
import nc.vo.so.m33.pub.exchange.ExchangeBillUtils;
import nccloud.pubitf.arap.sagascheck.IArapRecBillSagasCheck;

public class SquareARIncomeFor32BP {
    public SquareARIncomeFor32BP() {
    }

    public void square(SquareInvVO[] sqvos) throws BusinessException {
        sqvos = this.filteVO(sqvos);
        if (sqvos != null && sqvos.length != 0) {
            AroundProcesser<SquareInvVO> processer = new AroundProcesser(BPPlugInPoint.SquareARIncome);
            this.addBeforeRule(processer);

            //罕王东洋新开流程，不走标准流程校验
            if(!"KHJS".equals(sqvos[0].getChildrenVO()[0].getVsrctype())){
                this.addAfterRule(processer);
            }

            SquareInvDetailVO[] bills = SquareInvVOUtils.getInstance().changeSQVOtoSQDVOForAR(sqvos);
            processer.before(sqvos);
            this.saveDetail(sqvos, bills);
            this.toAR(sqvos);
            BSContext.getInstance().setSession("SquareARIncomeProcesser", processer);
        }
    }

    private SquareInvVO[] filteVO(SquareInvVO[] sqvos) {
        List<SquareInvViewVO> toArap = new ArrayList();
        SquareInvViewVO[] viewvos = SquareInvVOUtils.getInstance().changeToSaleSquareViewVO(sqvos);
        SquareInvViewVO[] var4 = viewvos;
        int var5 = viewvos.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            SquareInvViewVO vvo = var4[var6];
            UFDouble taxmny = vvo.getItem().getNorigtaxmny();
            UFDouble mny = vvo.getItem().getNmny();
            UFDouble tax = vvo.getItem().getNtax();
            if (taxmny != null && taxmny.compareTo(UFDouble.ZERO_DBL) != 0 || mny != null && mny.compareTo(UFDouble.ZERO_DBL) != 0 && tax != null && tax.compareTo(UFDouble.ZERO_DBL) != 0 && MathTool.oppose(mny).equals(tax)) {
                toArap.add(vvo);
            }
        }

        return SquareInvVOUtils.getInstance().combineBill((SquareInvViewVO[])toArap.toArray(new SquareInvViewVO[0]));
    }

    private void addAfterRule(AroundProcesser<SquareInvVO> processer) {
        IRule<SquareInvVO> rule = new SquareARCloseFor32Rule();
        processer.addAfterRule(rule);
    }

    private void addBeforeRule(AroundProcesser<SquareInvVO> processer) {
        IRule<SquareInvVO> rule = new GetNewARorgVidFor32Rule();
        processer.addBeforeRule(rule);
         rule = new ToARCheckFor32Rule();
        processer.addBeforeRule(rule);
    }

    private void saveDetail(SquareInvVO[] sqvos, SquareInvDetailVO[] bills) {
        AroundProcesser<SquareInvDetailVO> processer = new AroundProcesser(BPPlugInPoint.SquareARIncomeDetail);
        (new InsertSquare32DetailBP()).insert(sqvos, bills);
        IRule<SquareInvDetailVO> rule = new RewriteARIncomeFor32Rule();
        processer.addAfterRule(rule);
        processer.after(bills);
    }

    private void toAR(SquareInvVO[] sqvos) throws BusinessException {
        String srcBillType = SOBillType.Invoice.getCode();
        String destBillType = "F0";
        String squareBillType = SOBillType.SquareInvoice.getCode();
        AggReceivableBillVO[] arapvos = (AggReceivableBillVO[])(new ExchangeBillUtils(SquareInvVO.class)).exchangeBill(sqvos, squareBillType, srcBillType, destBillType);
        arapvos = (AggReceivableBillVO[])(new TransferSortUtil("invoiceno", "top_itemid")).sort(arapvos);
        if (!SagasUtil.isNccnativeMerged()) {
            IArapRecBillSagasCheck validateService = (IArapRecBillSagasCheck)NCLocator.getInstance().lookup(IArapRecBillSagasCheck.class);
            validateService.billInsertSagasCheck(arapvos);
        }

        SaleInvoiceContextForArap context = (SaleInvoiceContextForArap)BSContext.getInstance().getSession(SaleInvoiceContextForArap.class.getName());
        context.setRecbills(arapvos);
    }
}
