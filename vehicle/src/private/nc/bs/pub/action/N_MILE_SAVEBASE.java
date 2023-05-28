package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.drivermileage.plugin.bpplugin.DriverMileagePluginPoint;
import nc.vo.vehicle.drivermileage.AggDriverMileageHVO;
import nc.itf.vehicle.IDriverMileageMaintain;

public class N_MILE_SAVEBASE extends AbstractPfAction<AggDriverMileageHVO> {

	@Override
	protected CompareAroundProcesser<AggDriverMileageHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggDriverMileageHVO> processor = null;
		AggDriverMileageHVO[] clientFullVOs = (AggDriverMileageHVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggDriverMileageHVO>(
					DriverMileagePluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggDriverMileageHVO>(
					DriverMileagePluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggDriverMileageHVO> rule = null;

		return processor;
	}

	@Override
	protected AggDriverMileageHVO[] processBP(Object userObj,
			AggDriverMileageHVO[] clientFullVOs, AggDriverMileageHVO[] originBills) {

		AggDriverMileageHVO[] bills = null;
		try {
			IDriverMileageMaintain operator = NCLocator.getInstance()
					.lookup(IDriverMileageMaintain.class);
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
