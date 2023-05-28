package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.otherinfo.plugin.bpplugin.OtherInfoPluginPoint;
import nc.vo.vehicle.otherinfo.AggOtherInfoVO;
import nc.itf.vehicle.IOtherInfoMaintain;

public class N_OINF_SAVE extends AbstractPfAction<AggOtherInfoVO> {

	protected CompareAroundProcesser<AggOtherInfoVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggOtherInfoVO> processor = new CompareAroundProcesser<AggOtherInfoVO>(
				OtherInfoPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggOtherInfoVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggOtherInfoVO[] processBP(Object userObj,
			AggOtherInfoVO[] clientFullVOs, AggOtherInfoVO[] originBills) {
		IOtherInfoMaintain operator = NCLocator.getInstance().lookup(
				IOtherInfoMaintain.class);
		AggOtherInfoVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
