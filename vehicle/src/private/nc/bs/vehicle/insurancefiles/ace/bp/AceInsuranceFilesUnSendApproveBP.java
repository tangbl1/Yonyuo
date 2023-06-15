package nc.bs.vehicle.insurancefiles.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.vehicle.insurance.AggInsuranceHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceInsuranceFilesUnSendApproveBP {

	public AggInsuranceHVO[] unSend(AggInsuranceHVO[] clientBills,
			AggInsuranceHVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggInsuranceHVO> update = new BillUpdate<AggInsuranceHVO>();
		AggInsuranceHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggInsuranceHVO[] clientBills) {
		for (AggInsuranceHVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
