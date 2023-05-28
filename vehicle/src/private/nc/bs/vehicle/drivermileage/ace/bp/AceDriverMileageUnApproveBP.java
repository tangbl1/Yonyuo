package nc.bs.vehicle.drivermileage.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.vehicle.drivermileage.AggDriverMileageHVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceDriverMileageUnApproveBP {

	public AggDriverMileageHVO[] unApprove(AggDriverMileageHVO[] clientBills,
			AggDriverMileageHVO[] originBills) {
		for (AggDriverMileageHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggDriverMileageHVO> update = new BillUpdate<AggDriverMileageHVO>();
		AggDriverMileageHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
