package nc.itf.vehicle;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.vorder.AggVorderHVO;
import nc.vo.pub.BusinessException;

public interface IVorderMaintain {

	public void delete(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException;

	public AggVorderHVO[] insert(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException;

	public AggVorderHVO[] update(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException;

	public AggVorderHVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggVorderHVO[] save(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException;

	public AggVorderHVO[] unsave(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException;

	public AggVorderHVO[] approve(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException;

	public AggVorderHVO[] unapprove(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException;
}
