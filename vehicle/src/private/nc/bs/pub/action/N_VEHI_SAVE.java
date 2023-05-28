package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.vehicle.plugin.bpplugin.VehiclePluginPoint;
import nc.vo.vehicle.AggVehicleMessageVO;
import nc.itf.vehicle.IVehicleMaintain;

public class N_VEHI_SAVE extends AbstractPfAction<AggVehicleMessageVO> {

	protected CompareAroundProcesser<AggVehicleMessageVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggVehicleMessageVO> processor = new CompareAroundProcesser<AggVehicleMessageVO>(
				VehiclePluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggVehicleMessageVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggVehicleMessageVO[] processBP(Object userObj,
			AggVehicleMessageVO[] clientFullVOs, AggVehicleMessageVO[] originBills) {
		IVehicleMaintain operator = NCLocator.getInstance().lookup(
				IVehicleMaintain.class);
		AggVehicleMessageVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
