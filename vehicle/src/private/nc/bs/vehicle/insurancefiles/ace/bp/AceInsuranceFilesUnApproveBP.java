package nc.bs.vehicle.insurancefiles.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.vehicle.insurance.AggInsuranceHVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceInsuranceFilesUnApproveBP {

	public AggInsuranceHVO[] unApprove(AggInsuranceHVO[] clientBills,
			AggInsuranceHVO[] originBills) {
		for (AggInsuranceHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggInsuranceHVO> update = new BillUpdate<AggInsuranceHVO>();
		AggInsuranceHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
