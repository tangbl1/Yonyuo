package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.insurancefiles.plugin.bpplugin.InsuranceFilesPluginPoint;
import nc.vo.vehicle.insurance.AggInsuranceHVO;
import nc.itf.vehicle.IInsuranceFilesMaintain;

public class N_INSU_UNSAVEBILL extends AbstractPfAction<AggInsuranceHVO> {

	@Override
	protected CompareAroundProcesser<AggInsuranceHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInsuranceHVO> processor = new CompareAroundProcesser<AggInsuranceHVO>(
				InsuranceFilesPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggInsuranceHVO[] processBP(Object userObj,
			AggInsuranceHVO[] clientFullVOs, AggInsuranceHVO[] originBills) {
		IInsuranceFilesMaintain operator = NCLocator.getInstance().lookup(
				IInsuranceFilesMaintain.class);
		AggInsuranceHVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
