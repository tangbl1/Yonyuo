package nc.bs.vehicle.otherinfo.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.vehicle.otherinfo.AggOtherInfoVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceOtherInfoUnSendApproveBP {

	public AggOtherInfoVO[] unSend(AggOtherInfoVO[] clientBills,
			AggOtherInfoVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggOtherInfoVO> update = new BillUpdate<AggOtherInfoVO>();
		AggOtherInfoVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggOtherInfoVO[] clientBills) {
		for (AggOtherInfoVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
