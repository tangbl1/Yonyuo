package nc.bs.vehicle.inspectionfiles.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.vehicle.inspectionfiles.AggInspectionFileHVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceInspectionFilesUnApproveBP {

	public AggInspectionFileHVO[] unApprove(AggInspectionFileHVO[] clientBills,
			AggInspectionFileHVO[] originBills) {
		for (AggInspectionFileHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggInspectionFileHVO> update = new BillUpdate<AggInspectionFileHVO>();
		AggInspectionFileHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
