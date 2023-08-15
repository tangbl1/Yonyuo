//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package nc.pubimpl.so.m33.so.m32.helper;

import java.util.List;
import java.util.Map;
import nc.bs.so.m33.biz.m32.bp.cancelsquare.CancelSquareInvDetailBP;
import nc.bs.so.m33.biz.m32.bp.check.SquareInvoiceCheckBP;
import nc.bs.so.m33.biz.pub.cancelsquare.AbstractCancelSquareDetail;
import nc.bs.so.m33.maintain.m32.DeleteSquare32BP;
import nc.bs.so.m33.maintain.m32.query.QuerySquare32VOBP;
import nc.impl.pubapp.env.BSContext;
import nc.impl.pubapp.pattern.data.bill.tool.BillConcurrentTool;
import nc.impl.pubapp.pattern.data.vo.tool.VOConcurrentTool;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.so.m32.entity.SaleInvoiceVO;
import nc.vo.so.m33.enumeration.SquareType;
import nc.vo.so.m33.m32.entity.SquareInvBVO;
import nc.vo.so.m33.m32.entity.SquareInvDetailVO;
import nc.vo.so.m33.m32.entity.SquareInvVO;
import nc.vo.so.m33.m4c.entity.SquareOutVO;
import nc.vo.so.pub.util.AggVOUtil;
import nc.vo.so.pub.votools.SoVoTools;

public abstract class Square33For32BaseHelper {
    public Square33For32BaseHelper() {
    }

    public void doCancelSquare(SaleInvoiceVO[] voInvoice) throws BusinessException {
        BSContext.getInstance().setSession(SaleInvoiceVO.class.getName(), voInvoice);

        try {
            BillConcurrentTool tool = new BillConcurrentTool();
            tool.lockBill(voInvoice);
            SquareInvVO[] sqvos = (new QuerySquare32VOBP()).querySquareInvVOBy32ID(SoVoTools.getVOPKValues(voInvoice));
            if (sqvos == null || sqvos.length == 0) {
                return;
            }
            //罕王东洋 来源不是标准流程，不走销售出库校验
            if(!"KHJS".equals(sqvos[0].getChildrenVO()[0].getVsrctype())){
                (new SquareInvoiceCheckBP()).checkETREGForCancelSquare(sqvos);
            }

            tool.lockBill(sqvos);
            this.cancelSquareDetail(sqvos);
            (new DeleteSquare32BP()).delete(sqvos);
        } catch (Exception var7) {
            ExceptionUtils.marsh(var7);
        } finally {
            BSContext.getInstance().removeSession(SaleInvoiceVO.class.getName());
        }

    }

    private void cancelSquareDetail(SquareInvVO[] sqvos) {
        SquareInvDetailVO[] sqdvos = (new QuerySquare32VOBP()).querySquareInvDetailVOBySQHID((String[])AggVOUtil.getDistinctItemFieldArray(sqvos, "csalesquareid", String.class));
        if (sqdvos != null) {
            (new VOConcurrentTool()).lock(sqdvos);
            SquareInvVO[] var3 = sqvos;
            int var4 = sqvos.length;

            int var5;
            for(var5 = 0; var5 < var4; ++var5) {
                SquareInvVO svo = var3[var5];
                SquareInvBVO[] var7 = svo.getChildrenVO();
                int var8 = var7.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    SquareInvBVO bvo = var7[var9];
                    bvo.setNthisnum(MathTool.oppose(bvo.getNnum()));
                    bvo.setNorigtaxmny(MathTool.oppose(bvo.getNorigtaxmny()));
                }
            }

            SquareInvDetailVO[] var11 = sqdvos;
            var4 = sqdvos.length;

            for(var5 = 0; var5 < var4; ++var5) {
                SquareInvDetailVO dvo = var11[var5];
                dvo.setNsquarenum(MathTool.oppose(dvo.getNsquarenum()));
                dvo.setNorigtaxmny(MathTool.oppose(dvo.getNorigtaxmny()));
            }

            BSContext.getInstance().setSession(SquareInvVO.class.getName(), sqvos);
            AbstractCancelSquareDetail<SquareInvDetailVO> caction = new CancelSquareInvDetailBP();
            Map<SquareType, List<SquareInvDetailVO>> m_sqDetailVo = caction.splitVOBySquareType(sqdvos, "fsquaretype");
            this.cancelSquareDetail(m_sqDetailVo);
            BSContext.getInstance().removeSession(SquareOutVO.class.getName());
        }

    }

    protected void cancelSquareDetail(Map<SquareType, List<SquareInvDetailVO>> m_sqDetailVo) {
    }
}
