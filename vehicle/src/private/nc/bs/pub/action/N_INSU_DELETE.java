package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.insurancefiles.plugin.bpplugin.InsuranceFilesPluginPoint;
import nc.vo.vehicle.insurance.AggInsuranceHVO;
import nc.itf.vehicle.IInsuranceFilesMaintain;

public class N_INSU_DELETE extends AbstractPfAction<AggInsuranceHVO> {

	@Override
	protected CompareAroundProcesser<AggInsuranceHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInsuranceHVO> processor = new CompareAroundProcesser<AggInsuranceHVO>(
				InsuranceFilesPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggInsuranceHVO[] processBP(Object userObj,
			AggInsuranceHVO[] clientFullVOs, AggInsuranceHVO[] originBills) {
		IInsuranceFilesMaintain operator = NCLocator.getInstance().lookup(
				IInsuranceFilesMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
