/*gDoB4S+UYfafwH1tyLzQ2HjqAv2GTQY7CY/SuMF4ZVN933YLjZIMOCUGk1GvxvVM*/
/**
 *
 */
package nc.bs.ic.m4c.cancelsign;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nc.bs.ic.general.cancelsign.CancelSignBPTemplate;
import nc.bs.ic.general.cancelsign.ICancelSignBP;
import nc.bs.ic.general.cancelsign.ICancelSignRuleProvider;
import nc.bs.ic.m4c.base.BPPlugInPoint;
import nc.bs.ic.m4c.cancelsign.rule.*;
import nc.bs.ic.m4d.cancelsign.rule.CancelSignVmiCheck;
import nc.bs.ic.pub.util.SagasUtils;
import nc.bs.scmpub.rule.VOSagaFrozenValidateRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.itf.ic.m4c.compensate.ISaleOutSagasCompensate;
import nc.vo.ic.general.define.SagasConst;
import nc.vo.ic.m4c.entity.SaleOutVO;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.scmpub.res.billtype.ICBillType;
import nccloud.bs.ic.mobile.component.operation.rule.MobAfterUnSignMessageRule;
import nccloud.bs.ic.tms.kj.KJTMSConfirmAction;
import nccloud.vo.scmpub.tms.enumeration.BusiScene;

/**
 * <p>
 * <b>销售出库单取消签字：</b>
 * <p>
 *
 * @version 6.0
 * @since v60
 * @author wuweiping
 * @time 2010-1-26下午04:57:42
 */
public class CancelSignBP implements ICancelSignBP<SaleOutVO>,
        ICancelSignRuleProvider<SaleOutVO> {


    @Override
    public SaleOutVO[] cancelSign(SaleOutVO[] bills) {
        CancelSignBPTemplate<SaleOutVO> insertBP = new CancelSignBPTemplate<SaleOutVO>(
                BPPlugInPoint.CancelSignBP, this);
        SagasUtils.frozenAndAddSaga(bills, ICBillType.SaleOut.getCode(),SagasConst.COMPENSABLE, null);
        Map<String, Serializable> paramMap = new HashMap<String, Serializable>();
        paramMap.put(SagasConst.KEY_ACTIONNAME, SagasConst.CANCELSIGN_4C);
        paramMap.put(SagasConst.KEY_HID, VOEntityUtil.getPksFromAggVO(bills));
        SagasUtils.compensate(paramMap,ISaleOutSagasCompensate.class);
        SaleOutVO[] resultVOs = insertBP.cancelSign(bills);
        // ---- start---- 科箭TMS 处理
        KJTMSConfirmAction action = new KJTMSConfirmAction(BusiScene.SODelivery);
        action.cancelConfirm(resultVOs);
        // ---- end-----
        return resultVOs;
    }

    @Override
    public void addAfterRule(SaleOutVO[] vos, AroundProcesser<SaleOutVO> processor) {
        CancelSquareRule reWrite33 = new CancelSquareRule();
        processor.addAfterRule(reWrite33);
        AfterUnsignRuleForFinanceProcess unsignRule = new AfterUnsignRuleForFinanceProcess();
        processor.addAfterRule(unsignRule);
        processor.addAfterRule(new AfterCancelSignRuleForLiabilityProcess());
        processor.addAfterRule(new CancelSaleOutProceedsRule());
        processor.addAfterRule(new CancelArsubToVoucherRule());
        processor.addAfterRule(new MobAfterUnSignMessageRule());
        //红色出库单取消签字规则
        processor.addAfterFinalRule(new AfterCancelRedRuleTOArsubProcess());
    }

    @Override
    public void addBeforeRule(SaleOutVO[] vos,
                              AroundProcesser<SaleOutVO> processor) {
        CheckCancelSign  checkrule = new CheckCancelSign();
        //sagas状态校验
        processor.addBeforeRule(new VOSagaFrozenValidateRule<SaleOutVO>(true));
        processor.addBeforeRule(checkrule);
        //取消签字：校验：是否已参与消耗汇总
        processor.addBeforeRule(new CancelSignVmiCheck<SaleOutVO>());
        //add 东洋销售出库取消签字校验
        processor.addBeforeRule( new DyCheckCancelSign());
    }
}
/*gDoB4S+UYfafwH1tyLzQ2HjqAv2GTQY7CY/SuMF4ZVN933YLjZIMOCUGk1GvxvVM*/