package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.drivermileage.plugin.bpplugin.DriverMileagePluginPoint;
import nc.vo.vehicle.drivermileage.AggDriverMileageHVO;
import nc.itf.vehicle.IDriverMileageMaintain;

public class N_MILE_APPROVE extends AbstractPfAction<AggDriverMileageHVO> {

	public N_MILE_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggDriverMileageHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggDriverMileageHVO> processor = new CompareAroundProcesser<AggDriverMileageHVO>(
				DriverMileagePluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggDriverMileageHVO[] processBP(Object userObj,
			AggDriverMileageHVO[] clientFullVOs, AggDriverMileageHVO[] originBills) {
		AggDriverMileageHVO[] bills = null;
		IDriverMileageMaintain operator = NCLocator.getInstance().lookup(
				IDriverMileageMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
