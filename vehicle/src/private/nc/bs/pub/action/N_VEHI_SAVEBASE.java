package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.vehicle.plugin.bpplugin.VehiclePluginPoint;
import nc.vo.vehicle.AggVehicleMessageVO;
import nc.itf.vehicle.IVehicleMaintain;

public class N_VEHI_SAVEBASE extends AbstractPfAction<AggVehicleMessageVO> {

	@Override
	protected CompareAroundProcesser<AggVehicleMessageVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggVehicleMessageVO> processor = null;
		AggVehicleMessageVO[] clientFullVOs = (AggVehicleMessageVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggVehicleMessageVO>(
					VehiclePluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggVehicleMessageVO>(
					VehiclePluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggVehicleMessageVO> rule = null;

		return processor;
	}

	@Override
	protected AggVehicleMessageVO[] processBP(Object userObj,
			AggVehicleMessageVO[] clientFullVOs, AggVehicleMessageVO[] originBills) {

		AggVehicleMessageVO[] bills = null;
		try {
			IVehicleMaintain operator = NCLocator.getInstance()
					.lookup(IVehicleMaintain.class);
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
