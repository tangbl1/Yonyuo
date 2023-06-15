package nc.bs.vehicle.vehicle.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.vehicle.AggVehicleMessageVO;

/**
 * 标准单据审核的BP
 */
public class AceVehicleApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggVehicleMessageVO[] approve(AggVehicleMessageVO[] clientBills,
			AggVehicleMessageVO[] originBills) {
		for (AggVehicleMessageVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggVehicleMessageVO> update = new BillUpdate<AggVehicleMessageVO>();
		AggVehicleMessageVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
