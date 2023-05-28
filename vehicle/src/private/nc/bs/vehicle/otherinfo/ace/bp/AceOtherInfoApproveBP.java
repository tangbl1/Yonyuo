package nc.bs.vehicle.otherinfo.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.vehicle.otherinfo.AggOtherInfoVO;

/**
 * 标准单据审核的BP
 */
public class AceOtherInfoApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggOtherInfoVO[] approve(AggOtherInfoVO[] clientBills,
			AggOtherInfoVO[] originBills) {
		for (AggOtherInfoVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggOtherInfoVO> update = new BillUpdate<AggOtherInfoVO>();
		AggOtherInfoVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
