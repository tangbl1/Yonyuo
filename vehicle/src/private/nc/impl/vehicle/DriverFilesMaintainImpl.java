package nc.impl.vehicle;

import nc.impl.pub.ace.AceDriverFilesPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.driverfiles.AggDriverFiles;
import nc.itf.vehicle.IDriverFilesMaintain;
import nc.vo.pub.BusinessException;

public class DriverFilesMaintainImpl extends AceDriverFilesPubServiceImpl
		implements IDriverFilesMaintain {

	@Override
	public void delete(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggDriverFiles[] insert(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggDriverFiles[] update(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggDriverFiles[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggDriverFiles[] save(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggDriverFiles[] unsave(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggDriverFiles[] approve(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggDriverFiles[] unapprove(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
