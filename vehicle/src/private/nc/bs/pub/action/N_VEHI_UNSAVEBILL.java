package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.vehicle.plugin.bpplugin.VehiclePluginPoint;
import nc.vo.vehicle.AggVehicleMessageVO;
import nc.itf.vehicle.IVehicleMaintain;

public class N_VEHI_UNSAVEBILL extends AbstractPfAction<AggVehicleMessageVO> {

	@Override
	protected CompareAroundProcesser<AggVehicleMessageVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggVehicleMessageVO> processor = new CompareAroundProcesser<AggVehicleMessageVO>(
				VehiclePluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggVehicleMessageVO[] processBP(Object userObj,
			AggVehicleMessageVO[] clientFullVOs, AggVehicleMessageVO[] originBills) {
		IVehicleMaintain operator = NCLocator.getInstance().lookup(
				IVehicleMaintain.class);
		AggVehicleMessageVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
