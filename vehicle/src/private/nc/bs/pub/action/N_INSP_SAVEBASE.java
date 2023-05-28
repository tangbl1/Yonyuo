package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.inspectionfiles.plugin.bpplugin.InspectionFilesPluginPoint;
import nc.vo.vehicle.inspectionfiles.AggInspectionFileHVO;
import nc.itf.vehicle.IInspectionFilesMaintain;

public class N_INSP_SAVEBASE extends AbstractPfAction<AggInspectionFileHVO> {

	@Override
	protected CompareAroundProcesser<AggInspectionFileHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInspectionFileHVO> processor = null;
		AggInspectionFileHVO[] clientFullVOs = (AggInspectionFileHVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggInspectionFileHVO>(
					InspectionFilesPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggInspectionFileHVO>(
					InspectionFilesPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggInspectionFileHVO> rule = null;

		return processor;
	}

	@Override
	protected AggInspectionFileHVO[] processBP(Object userObj,
			AggInspectionFileHVO[] clientFullVOs, AggInspectionFileHVO[] originBills) {

		AggInspectionFileHVO[] bills = null;
		try {
			IInspectionFilesMaintain operator = NCLocator.getInstance()
					.lookup(IInspectionFilesMaintain.class);
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
