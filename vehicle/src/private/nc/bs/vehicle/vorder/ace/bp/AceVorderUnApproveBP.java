package nc.bs.vehicle.vorder.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.vehicle.vorder.AggVorderHVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceVorderUnApproveBP {

	public AggVorderHVO[] unApprove(AggVorderHVO[] clientBills,
			AggVorderHVO[] originBills) {
		for (AggVorderHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggVorderHVO> update = new BillUpdate<AggVorderHVO>();
		AggVorderHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
