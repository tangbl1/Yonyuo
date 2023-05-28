package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.driverfiles.plugin.bpplugin.DriverFilesPluginPoint;
import nc.vo.vehicle.driverfiles.AggDriverFiles;
import nc.itf.vehicle.IDriverFilesMaintain;

public class N_DRIV_APPROVE extends AbstractPfAction<AggDriverFiles> {

	public N_DRIV_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggDriverFiles> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggDriverFiles> processor = new CompareAroundProcesser<AggDriverFiles>(
				DriverFilesPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggDriverFiles[] processBP(Object userObj,
			AggDriverFiles[] clientFullVOs, AggDriverFiles[] originBills) {
		AggDriverFiles[] bills = null;
		IDriverFilesMaintain operator = NCLocator.getInstance().lookup(
				IDriverFilesMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
