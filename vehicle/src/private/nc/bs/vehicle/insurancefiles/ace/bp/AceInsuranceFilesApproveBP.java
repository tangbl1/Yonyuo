package nc.bs.vehicle.insurancefiles.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.vehicle.insurance.AggInsuranceHVO;

/**
 * 标准单据审核的BP
 */
public class AceInsuranceFilesApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggInsuranceHVO[] approve(AggInsuranceHVO[] clientBills,
			AggInsuranceHVO[] originBills) {
		for (AggInsuranceHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggInsuranceHVO> update = new BillUpdate<AggInsuranceHVO>();
		AggInsuranceHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
