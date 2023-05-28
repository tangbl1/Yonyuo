package nc.bs.vehicle.vehicle.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.vehicle.AggVehicleMessageVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceVehicleUnApproveBP {

	public AggVehicleMessageVO[] unApprove(AggVehicleMessageVO[] clientBills,
			AggVehicleMessageVO[] originBills) {
		for (AggVehicleMessageVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggVehicleMessageVO> update = new BillUpdate<AggVehicleMessageVO>();
		AggVehicleMessageVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
