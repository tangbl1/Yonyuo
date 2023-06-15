package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.driverfiles.plugin.bpplugin.DriverFilesPluginPoint;
import nc.vo.vehicle.driverfiles.AggDriverFiles;
import nc.itf.vehicle.IDriverFilesMaintain;

public class N_DRIV_SAVE extends AbstractPfAction<AggDriverFiles> {

	protected CompareAroundProcesser<AggDriverFiles> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggDriverFiles> processor = new CompareAroundProcesser<AggDriverFiles>(
				DriverFilesPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggDriverFiles> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggDriverFiles[] processBP(Object userObj,
			AggDriverFiles[] clientFullVOs, AggDriverFiles[] originBills) {
		IDriverFilesMaintain operator = NCLocator.getInstance().lookup(
				IDriverFilesMaintain.class);
		AggDriverFiles[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
