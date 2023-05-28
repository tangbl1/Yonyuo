package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.inspectionfiles.plugin.bpplugin.InspectionFilesPluginPoint;
import nc.vo.vehicle.inspectionfiles.AggInspectionFileHVO;
import nc.itf.vehicle.IInspectionFilesMaintain;

public class N_INSP_UNAPPROVE extends AbstractPfAction<AggInspectionFileHVO> {

	@Override
	protected CompareAroundProcesser<AggInspectionFileHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInspectionFileHVO> processor = new CompareAroundProcesser<AggInspectionFileHVO>(
				InspectionFilesPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggInspectionFileHVO[] processBP(Object userObj,
			AggInspectionFileHVO[] clientFullVOs, AggInspectionFileHVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggInspectionFileHVO[] bills = null;
		try {
			IInspectionFilesMaintain operator = NCLocator.getInstance()
					.lookup(IInspectionFilesMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
