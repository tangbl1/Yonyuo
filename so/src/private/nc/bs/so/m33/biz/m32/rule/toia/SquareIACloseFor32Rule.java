//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package nc.bs.so.m33.biz.m32.rule.toia;

import java.util.ArrayList;
import java.util.List;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.so.m33.ref.so.m30.SOSaleOrderServicesUtil;
import nc.pubitf.so.m30.balend.BalEndPara;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.res.billtype.SOBillType;
import nc.vo.so.m30.balend.enumeration.BalEndTrigger;
import nc.vo.so.m33.m32.entity.SquareInvBVO;
import nc.vo.so.m33.m32.entity.SquareInvVO;

public class SquareIACloseFor32Rule implements IRule<SquareInvVO> {
    public SquareIACloseFor32Rule() {
    }

    public void process(SquareInvVO[] vos) {

        //罕王东洋组织新开流程，不走标准流程校验
        if (!"KHJS".equals(vos[0].getChildrenVO()[0].getVsrctype())){
            String srctype = vos[0].getChildrenVO()[0].getVsrctype();
            List<String> orderbids = new ArrayList();
            SquareInvBVO[] var4 = vos[0].getChildrenVO();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                SquareInvBVO bvo = var4[var6];
                UFDouble num = bvo.getNnum();
                if (num.compareTo(UFDouble.ZERO_DBL) != 0 || bvo.getBdiscountflag() != null && bvo.getBdiscountflag().booleanValue()) {
                    orderbids.add(bvo.getCfirstbid());
                }
            }

            if (!orderbids.isEmpty()) {
                BalEndTrigger trigger = BalEndTrigger.VOICE_COST;
                BalEndPara para = new BalEndPara((String[])orderbids.toArray(new String[0]), trigger);

                try {
                    if (SOBillType.INITOUTREG.getCode().equals(srctype)) {
                        return;
                    }

                    SOSaleOrderServicesUtil.processAutoBalEnd(para);
                } catch (BusinessException var9) {
                    ExceptionUtils.wrappException(var9);
                }

            }
        }

    }
}
