package nc.itf.vehicle;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.drivermileage.AggDriverMileageHVO;
import nc.vo.pub.BusinessException;

public interface IDriverMileageMaintain {

	public void delete(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException;

	public AggDriverMileageHVO[] insert(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException;

	public AggDriverMileageHVO[] update(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException;

	public AggDriverMileageHVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggDriverMileageHVO[] save(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException;

	public AggDriverMileageHVO[] unsave(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException;

	public AggDriverMileageHVO[] approve(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException;

	public AggDriverMileageHVO[] unapprove(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException;
}
