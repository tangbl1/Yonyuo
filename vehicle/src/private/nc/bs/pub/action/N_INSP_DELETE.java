package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.vehicle.inspectionfiles.plugin.bpplugin.InspectionFilesPluginPoint;
import nc.vo.vehicle.inspectionfiles.AggInspectionFileHVO;
import nc.itf.vehicle.IInspectionFilesMaintain;

public class N_INSP_DELETE extends AbstractPfAction<AggInspectionFileHVO> {

	@Override
	protected CompareAroundProcesser<AggInspectionFileHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInspectionFileHVO> processor = new CompareAroundProcesser<AggInspectionFileHVO>(
				InspectionFilesPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggInspectionFileHVO[] processBP(Object userObj,
			AggInspectionFileHVO[] clientFullVOs, AggInspectionFileHVO[] originBills) {
		IInspectionFilesMaintain operator = NCLocator.getInstance().lookup(
				IInspectionFilesMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
