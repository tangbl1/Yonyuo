package nc.bs.vehicle.driverfiles.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.vehicle.driverfiles.AggDriverFiles;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceDriverFilesUnSendApproveBP {

	public AggDriverFiles[] unSend(AggDriverFiles[] clientBills,
			AggDriverFiles[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggDriverFiles> update = new BillUpdate<AggDriverFiles>();
		AggDriverFiles[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggDriverFiles[] clientBills) {
		for (AggDriverFiles clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
