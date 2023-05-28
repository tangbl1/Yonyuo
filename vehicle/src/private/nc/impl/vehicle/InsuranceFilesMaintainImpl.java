package nc.impl.vehicle;

import nc.impl.pub.ace.AceInsuranceFilesPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.insurance.AggInsuranceHVO;
import nc.itf.vehicle.IInsuranceFilesMaintain;
import nc.vo.pub.BusinessException;

public class InsuranceFilesMaintainImpl extends AceInsuranceFilesPubServiceImpl
		implements IInsuranceFilesMaintain {

	@Override
	public void delete(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggInsuranceHVO[] insert(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggInsuranceHVO[] update(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggInsuranceHVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggInsuranceHVO[] save(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggInsuranceHVO[] unsave(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggInsuranceHVO[] approve(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggInsuranceHVO[] unapprove(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
