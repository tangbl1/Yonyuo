package nc.itf.vehicle;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.AggVehicleMessageVO;
import nc.vo.pub.BusinessException;

public interface IVehicleMaintain {

	public void delete(AggVehicleMessageVO[] clientFullVOs,
			AggVehicleMessageVO[] originBills) throws BusinessException;

	public AggVehicleMessageVO[] insert(AggVehicleMessageVO[] clientFullVOs,
			AggVehicleMessageVO[] originBills) throws BusinessException;

	public AggVehicleMessageVO[] update(AggVehicleMessageVO[] clientFullVOs,
			AggVehicleMessageVO[] originBills) throws BusinessException;

	public AggVehicleMessageVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggVehicleMessageVO[] save(AggVehicleMessageVO[] clientFullVOs,
			AggVehicleMessageVO[] originBills) throws BusinessException;

	public AggVehicleMessageVO[] unsave(AggVehicleMessageVO[] clientFullVOs,
			AggVehicleMessageVO[] originBills) throws BusinessException;

	public AggVehicleMessageVO[] approve(AggVehicleMessageVO[] clientFullVOs,
			AggVehicleMessageVO[] originBills) throws BusinessException;

	public AggVehicleMessageVO[] unapprove(AggVehicleMessageVO[] clientFullVOs,
			AggVehicleMessageVO[] originBills) throws BusinessException;
}
