package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.vehicle.plugin.bpplugin.VehiclePluginPoint;
import nc.vo.vehicle.AggVehicleMessageVO;
import nc.itf.vehicle.IVehicleMaintain;

public class N_VEHI_DELETE extends AbstractPfAction<AggVehicleMessageVO> {

	@Override
	protected CompareAroundProcesser<AggVehicleMessageVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggVehicleMessageVO> processor = new CompareAroundProcesser<AggVehicleMessageVO>(
				VehiclePluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggVehicleMessageVO[] processBP(Object userObj,
			AggVehicleMessageVO[] clientFullVOs, AggVehicleMessageVO[] originBills) {
		IVehicleMaintain operator = NCLocator.getInstance().lookup(
				IVehicleMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
