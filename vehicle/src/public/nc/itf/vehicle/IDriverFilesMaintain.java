package nc.itf.vehicle;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.driverfiles.AggDriverFiles;
import nc.vo.pub.BusinessException;

public interface IDriverFilesMaintain {

	public void delete(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException;

	public AggDriverFiles[] insert(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException;

	public AggDriverFiles[] update(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException;

	public AggDriverFiles[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggDriverFiles[] save(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException;

	public AggDriverFiles[] unsave(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException;

	public AggDriverFiles[] approve(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException;

	public AggDriverFiles[] unapprove(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException;
}
