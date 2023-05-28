package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.otherinfo.plugin.bpplugin.OtherInfoPluginPoint;
import nc.vo.vehicle.otherinfo.AggOtherInfoVO;
import nc.itf.vehicle.IOtherInfoMaintain;

public class N_OINF_APPROVE extends AbstractPfAction<AggOtherInfoVO> {

	public N_OINF_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggOtherInfoVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggOtherInfoVO> processor = new CompareAroundProcesser<AggOtherInfoVO>(
				OtherInfoPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggOtherInfoVO[] processBP(Object userObj,
			AggOtherInfoVO[] clientFullVOs, AggOtherInfoVO[] originBills) {
		AggOtherInfoVO[] bills = null;
		IOtherInfoMaintain operator = NCLocator.getInstance().lookup(
				IOtherInfoMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
