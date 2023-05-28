package nc.itf.vehicle;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.inspectionfiles.AggInspectionFileHVO;
import nc.vo.pub.BusinessException;

public interface IInspectionFilesMaintain {

	public void delete(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException;

	public AggInspectionFileHVO[] insert(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException;

	public AggInspectionFileHVO[] update(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException;

	public AggInspectionFileHVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggInspectionFileHVO[] save(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException;

	public AggInspectionFileHVO[] unsave(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException;

	public AggInspectionFileHVO[] approve(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException;

	public AggInspectionFileHVO[] unapprove(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException;
}
