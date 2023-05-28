package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.driverfiles.plugin.bpplugin.DriverFilesPluginPoint;
import nc.vo.vehicle.driverfiles.AggDriverFiles;
import nc.itf.vehicle.IDriverFilesMaintain;

public class N_DRIV_DELETE extends AbstractPfAction<AggDriverFiles> {

	@Override
	protected CompareAroundProcesser<AggDriverFiles> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggDriverFiles> processor = new CompareAroundProcesser<AggDriverFiles>(
				DriverFilesPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggDriverFiles[] processBP(Object userObj,
			AggDriverFiles[] clientFullVOs, AggDriverFiles[] originBills) {
		IDriverFilesMaintain operator = NCLocator.getInstance().lookup(
				IDriverFilesMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
