package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.otherinfo.plugin.bpplugin.OtherInfoPluginPoint;
import nc.vo.vehicle.otherinfo.AggOtherInfoVO;
import nc.itf.vehicle.IOtherInfoMaintain;

public class N_OINF_SAVEBASE extends AbstractPfAction<AggOtherInfoVO> {

	@Override
	protected CompareAroundProcesser<AggOtherInfoVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggOtherInfoVO> processor = null;
		AggOtherInfoVO[] clientFullVOs = (AggOtherInfoVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggOtherInfoVO>(
					OtherInfoPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggOtherInfoVO>(
					OtherInfoPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggOtherInfoVO> rule = null;

		return processor;
	}

	@Override
	protected AggOtherInfoVO[] processBP(Object userObj,
			AggOtherInfoVO[] clientFullVOs, AggOtherInfoVO[] originBills) {

		AggOtherInfoVO[] bills = null;
		try {
			IOtherInfoMaintain operator = NCLocator.getInstance()
					.lookup(IOtherInfoMaintain.class);
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
