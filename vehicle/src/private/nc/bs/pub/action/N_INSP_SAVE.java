package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.inspectionfiles.plugin.bpplugin.InspectionFilesPluginPoint;
import nc.vo.vehicle.inspectionfiles.AggInspectionFileHVO;
import nc.itf.vehicle.IInspectionFilesMaintain;

public class N_INSP_SAVE extends AbstractPfAction<AggInspectionFileHVO> {

	protected CompareAroundProcesser<AggInspectionFileHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInspectionFileHVO> processor = new CompareAroundProcesser<AggInspectionFileHVO>(
				InspectionFilesPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggInspectionFileHVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggInspectionFileHVO[] processBP(Object userObj,
			AggInspectionFileHVO[] clientFullVOs, AggInspectionFileHVO[] originBills) {
		IInspectionFilesMaintain operator = NCLocator.getInstance().lookup(
				IInspectionFilesMaintain.class);
		AggInspectionFileHVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
