package nc.itf.vehicle;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.insurance.AggInsuranceHVO;
import nc.vo.pub.BusinessException;

public interface IInsuranceFilesMaintain {

	public void delete(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException;

	public AggInsuranceHVO[] insert(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException;

	public AggInsuranceHVO[] update(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException;

	public AggInsuranceHVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggInsuranceHVO[] save(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException;

	public AggInsuranceHVO[] unsave(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException;

	public AggInsuranceHVO[] approve(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException;

	public AggInsuranceHVO[] unapprove(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException;
}
