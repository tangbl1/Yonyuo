package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.vehicle.plugin.bpplugin.VehiclePluginPoint;
import nc.vo.vehicle.AggVehicleMessageVO;
import nc.itf.vehicle.IVehicleMaintain;

public class N_VEHI_APPROVE extends AbstractPfAction<AggVehicleMessageVO> {

	public N_VEHI_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggVehicleMessageVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggVehicleMessageVO> processor = new CompareAroundProcesser<AggVehicleMessageVO>(
				VehiclePluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggVehicleMessageVO[] processBP(Object userObj,
			AggVehicleMessageVO[] clientFullVOs, AggVehicleMessageVO[] originBills) {
		AggVehicleMessageVO[] bills = null;
		IVehicleMaintain operator = NCLocator.getInstance().lookup(
				IVehicleMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
