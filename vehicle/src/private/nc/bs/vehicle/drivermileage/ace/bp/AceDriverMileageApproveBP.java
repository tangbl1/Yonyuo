package nc.bs.vehicle.drivermileage.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.vehicle.drivermileage.AggDriverMileageHVO;

/**
 * 标准单据审核的BP
 */
public class AceDriverMileageApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggDriverMileageHVO[] approve(AggDriverMileageHVO[] clientBills,
			AggDriverMileageHVO[] originBills) {
		for (AggDriverMileageHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggDriverMileageHVO> update = new BillUpdate<AggDriverMileageHVO>();
		AggDriverMileageHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
