package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.driverfiles.plugin.bpplugin.DriverFilesPluginPoint;
import nc.vo.vehicle.driverfiles.AggDriverFiles;
import nc.itf.vehicle.IDriverFilesMaintain;

public class N_DRIV_SAVEBASE extends AbstractPfAction<AggDriverFiles> {

	@Override
	protected CompareAroundProcesser<AggDriverFiles> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggDriverFiles> processor = null;
		AggDriverFiles[] clientFullVOs = (AggDriverFiles[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggDriverFiles>(
					DriverFilesPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggDriverFiles>(
					DriverFilesPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggDriverFiles> rule = null;

		return processor;
	}

	@Override
	protected AggDriverFiles[] processBP(Object userObj,
			AggDriverFiles[] clientFullVOs, AggDriverFiles[] originBills) {

		AggDriverFiles[] bills = null;
		try {
			IDriverFilesMaintain operator = NCLocator.getInstance()
					.lookup(IDriverFilesMaintain.class);
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
