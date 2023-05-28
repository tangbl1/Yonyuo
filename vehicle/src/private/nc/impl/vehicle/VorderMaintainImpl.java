package nc.impl.vehicle;

import nc.impl.pub.ace.AceVorderPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.vorder.AggVorderHVO;
import nc.itf.vehicle.IVorderMaintain;
import nc.vo.pub.BusinessException;

public class VorderMaintainImpl extends AceVorderPubServiceImpl
		implements IVorderMaintain {

	@Override
	public void delete(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggVorderHVO[] insert(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggVorderHVO[] update(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggVorderHVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggVorderHVO[] save(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggVorderHVO[] unsave(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggVorderHVO[] approve(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggVorderHVO[] unapprove(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
