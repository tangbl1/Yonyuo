package nc.bs.vehicle.driverfiles.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.vehicle.driverfiles.AggDriverFiles;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceDriverFilesUnApproveBP {

	public AggDriverFiles[] unApprove(AggDriverFiles[] clientBills,
			AggDriverFiles[] originBills) {
		for (AggDriverFiles clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggDriverFiles> update = new BillUpdate<AggDriverFiles>();
		AggDriverFiles[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
