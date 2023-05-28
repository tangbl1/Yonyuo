package nc.impl.vehicle;

import nc.impl.pub.ace.AceOtherInfoPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.otherinfo.AggOtherInfoVO;
import nc.itf.vehicle.IOtherInfoMaintain;
import nc.vo.pub.BusinessException;

public class OtherInfoMaintainImpl extends AceOtherInfoPubServiceImpl
		implements IOtherInfoMaintain {

	@Override
	public void delete(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggOtherInfoVO[] insert(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggOtherInfoVO[] update(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggOtherInfoVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggOtherInfoVO[] save(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggOtherInfoVO[] unsave(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggOtherInfoVO[] approve(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggOtherInfoVO[] unapprove(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
