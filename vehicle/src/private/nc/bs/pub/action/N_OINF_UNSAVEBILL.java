package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.otherinfo.plugin.bpplugin.OtherInfoPluginPoint;
import nc.vo.vehicle.otherinfo.AggOtherInfoVO;
import nc.itf.vehicle.IOtherInfoMaintain;

public class N_OINF_UNSAVEBILL extends AbstractPfAction<AggOtherInfoVO> {

	@Override
	protected CompareAroundProcesser<AggOtherInfoVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggOtherInfoVO> processor = new CompareAroundProcesser<AggOtherInfoVO>(
				OtherInfoPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggOtherInfoVO[] processBP(Object userObj,
			AggOtherInfoVO[] clientFullVOs, AggOtherInfoVO[] originBills) {
		IOtherInfoMaintain operator = NCLocator.getInstance().lookup(
				IOtherInfoMaintain.class);
		AggOtherInfoVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
