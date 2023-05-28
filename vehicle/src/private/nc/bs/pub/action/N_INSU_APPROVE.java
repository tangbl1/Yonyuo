package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.insurancefiles.plugin.bpplugin.InsuranceFilesPluginPoint;
import nc.vo.vehicle.insurance.AggInsuranceHVO;
import nc.itf.vehicle.IInsuranceFilesMaintain;

public class N_INSU_APPROVE extends AbstractPfAction<AggInsuranceHVO> {

	public N_INSU_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggInsuranceHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInsuranceHVO> processor = new CompareAroundProcesser<AggInsuranceHVO>(
				InsuranceFilesPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggInsuranceHVO[] processBP(Object userObj,
			AggInsuranceHVO[] clientFullVOs, AggInsuranceHVO[] originBills) {
		AggInsuranceHVO[] bills = null;
		IInsuranceFilesMaintain operator = NCLocator.getInstance().lookup(
				IInsuranceFilesMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
