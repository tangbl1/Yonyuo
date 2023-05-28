package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.insurancefiles.plugin.bpplugin.InsuranceFilesPluginPoint;
import nc.vo.vehicle.insurance.AggInsuranceHVO;
import nc.itf.vehicle.IInsuranceFilesMaintain;

public class N_INSU_SAVEBASE extends AbstractPfAction<AggInsuranceHVO> {

	@Override
	protected CompareAroundProcesser<AggInsuranceHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInsuranceHVO> processor = null;
		AggInsuranceHVO[] clientFullVOs = (AggInsuranceHVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggInsuranceHVO>(
					InsuranceFilesPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggInsuranceHVO>(
					InsuranceFilesPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggInsuranceHVO> rule = null;

		return processor;
	}

	@Override
	protected AggInsuranceHVO[] processBP(Object userObj,
			AggInsuranceHVO[] clientFullVOs, AggInsuranceHVO[] originBills) {

		AggInsuranceHVO[] bills = null;
		try {
			IInsuranceFilesMaintain operator = NCLocator.getInstance()
					.lookup(IInsuranceFilesMaintain.class);
			if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
					.getPrimaryKey())) {
				bills = operator.update(clientFullVOs, originBills);
			} else {
				bills = operator.insert(clientFullVOs, originBills);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}
}
