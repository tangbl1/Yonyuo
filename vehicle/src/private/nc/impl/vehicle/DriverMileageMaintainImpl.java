package nc.impl.vehicle;

import nc.impl.pub.ace.AceDriverMileagePubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.drivermileage.AggDriverMileageHVO;
import nc.itf.vehicle.IDriverMileageMaintain;
import nc.vo.pub.BusinessException;

public class DriverMileageMaintainImpl extends AceDriverMileagePubServiceImpl
		implements IDriverMileageMaintain {

	@Override
	public void delete(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggDriverMileageHVO[] insert(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggDriverMileageHVO[] update(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggDriverMileageHVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggDriverMileageHVO[] save(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggDriverMileageHVO[] unsave(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggDriverMileageHVO[] approve(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggDriverMileageHVO[] unapprove(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
