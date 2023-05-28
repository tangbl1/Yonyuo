package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.inspectionfiles.plugin.bpplugin.InspectionFilesPluginPoint;
import nc.vo.vehicle.inspectionfiles.AggInspectionFileHVO;
import nc.itf.vehicle.IInspectionFilesMaintain;

public class N_INSP_APPROVE extends AbstractPfAction<AggInspectionFileHVO> {

	public N_INSP_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggInspectionFileHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInspectionFileHVO> processor = new CompareAroundProcesser<AggInspectionFileHVO>(
				InspectionFilesPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggInspectionFileHVO[] processBP(Object userObj,
			AggInspectionFileHVO[] clientFullVOs, AggInspectionFileHVO[] originBills) {
		AggInspectionFileHVO[] bills = null;
		IInspectionFilesMaintain operator = NCLocator.getInstance().lookup(
				IInspectionFilesMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
