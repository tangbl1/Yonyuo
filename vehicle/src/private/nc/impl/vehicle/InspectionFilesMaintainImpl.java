package nc.impl.vehicle;

import nc.impl.pub.ace.AceInspectionFilesPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.inspectionfiles.AggInspectionFileHVO;
import nc.itf.vehicle.IInspectionFilesMaintain;
import nc.vo.pub.BusinessException;

public class InspectionFilesMaintainImpl extends AceInspectionFilesPubServiceImpl
		implements IInspectionFilesMaintain {

	@Override
	public void delete(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggInspectionFileHVO[] insert(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggInspectionFileHVO[] update(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggInspectionFileHVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggInspectionFileHVO[] save(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggInspectionFileHVO[] unsave(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggInspectionFileHVO[] approve(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggInspectionFileHVO[] unapprove(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
