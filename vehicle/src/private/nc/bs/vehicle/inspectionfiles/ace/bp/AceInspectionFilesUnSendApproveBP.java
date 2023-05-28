package nc.bs.vehicle.inspectionfiles.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.vehicle.inspectionfiles.AggInspectionFileHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceInspectionFilesUnSendApproveBP {

	public AggInspectionFileHVO[] unSend(AggInspectionFileHVO[] clientBills,
			AggInspectionFileHVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggInspectionFileHVO> update = new BillUpdate<AggInspectionFileHVO>();
		AggInspectionFileHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggInspectionFileHVO[] clientBills) {
		for (AggInspectionFileHVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
