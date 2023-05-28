package nc.itf.vehicle;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.otherinfo.AggOtherInfoVO;
import nc.vo.pub.BusinessException;

public interface IOtherInfoMaintain {

	public void delete(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException;

	public AggOtherInfoVO[] insert(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException;

	public AggOtherInfoVO[] update(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException;

	public AggOtherInfoVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggOtherInfoVO[] save(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException;

	public AggOtherInfoVO[] unsave(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException;

	public AggOtherInfoVO[] approve(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException;

	public AggOtherInfoVO[] unapprove(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException;
}
