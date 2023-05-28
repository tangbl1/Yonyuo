package nc.impl.vehicle;

import nc.impl.pub.ace.AceVehiclePubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.AggVehicleMessageVO;
import nc.itf.vehicle.IVehicleMaintain;
import nc.vo.pub.BusinessException;

public class VehicleMaintainImpl extends AceVehiclePubServiceImpl
		implements IVehicleMaintain {

	@Override
	public void delete(AggVehicleMessageVO[] clientFullVOs,
			AggVehicleMessageVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggVehicleMessageVO[] insert(AggVehicleMessageVO[] clientFullVOs,
			AggVehicleMessageVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggVehicleMessageVO[] update(AggVehicleMessageVO[] clientFullVOs,
			AggVehicleMessageVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggVehicleMessageVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggVehicleMessageVO[] save(AggVehicleMessageVO[] clientFullVOs,
			AggVehicleMessageVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggVehicleMessageVO[] unsave(AggVehicleMessageVO[] clientFullVOs,
			AggVehicleMessageVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggVehicleMessageVO[] approve(AggVehicleMessageVO[] clientFullVOs,
			AggVehicleMessageVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggVehicleMessageVO[] unapprove(AggVehicleMessageVO[] clientFullVOs,
			AggVehicleMessageVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
