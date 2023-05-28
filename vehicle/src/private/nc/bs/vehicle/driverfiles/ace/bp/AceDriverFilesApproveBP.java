package nc.bs.vehicle.driverfiles.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.vehicle.driverfiles.AggDriverFiles;

/**
 * 标准单据审核的BP
 */
public class AceDriverFilesApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggDriverFiles[] approve(AggDriverFiles[] clientBills,
			AggDriverFiles[] originBills) {
		for (AggDriverFiles clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggDriverFiles> update = new BillUpdate<AggDriverFiles>();
		AggDriverFiles[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
