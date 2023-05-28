package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.drivermileage.plugin.bpplugin.DriverMileagePluginPoint;
import nc.vo.vehicle.drivermileage.AggDriverMileageHVO;
import nc.itf.vehicle.IDriverMileageMaintain;

public class N_MILE_DELETE extends AbstractPfAction<AggDriverMileageHVO> {

	@Override
	protected CompareAroundProcesser<AggDriverMileageHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggDriverMileageHVO> processor = new CompareAroundProcesser<AggDriverMileageHVO>(
				DriverMileagePluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggDriverMileageHVO[] processBP(Object userObj,
			AggDriverMileageHVO[] clientFullVOs, AggDriverMileageHVO[] originBills) {
		IDriverMileageMaintain operator = NCLocator.getInstance().lookup(
				IDriverMileageMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
