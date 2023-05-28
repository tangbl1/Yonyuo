package nc.bs.vehicle.otherinfo.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.vehicle.otherinfo.AggOtherInfoVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceOtherInfoUnApproveBP {

	public AggOtherInfoVO[] unApprove(AggOtherInfoVO[] clientBills,
			AggOtherInfoVO[] originBills) {
		for (AggOtherInfoVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggOtherInfoVO> update = new BillUpdate<AggOtherInfoVO>();
		AggOtherInfoVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
