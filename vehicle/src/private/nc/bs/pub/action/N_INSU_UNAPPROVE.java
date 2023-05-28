package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.insurancefiles.plugin.bpplugin.InsuranceFilesPluginPoint;
import nc.vo.vehicle.insurance.AggInsuranceHVO;
import nc.itf.vehicle.IInsuranceFilesMaintain;

public class N_INSU_UNAPPROVE extends AbstractPfAction<AggInsuranceHVO> {

	@Override
	protected CompareAroundProcesser<AggInsuranceHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInsuranceHVO> processor = new CompareAroundProcesser<AggInsuranceHVO>(
				InsuranceFilesPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggInsuranceHVO[] processBP(Object userObj,
			AggInsuranceHVO[] clientFullVOs, AggInsuranceHVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggInsuranceHVO[] bills = null;
		try {
			IInsuranceFilesMaintain operator = NCLocator.getInstance()
					.lookup(IInsuranceFilesMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
