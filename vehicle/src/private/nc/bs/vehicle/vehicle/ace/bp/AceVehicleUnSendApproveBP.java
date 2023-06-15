package nc.bs.vehicle.vehicle.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.vehicle.AggVehicleMessageVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceVehicleUnSendApproveBP {

	public AggVehicleMessageVO[] unSend(AggVehicleMessageVO[] clientBills,
			AggVehicleMessageVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggVehicleMessageVO> update = new BillUpdate<AggVehicleMessageVO>();
		AggVehicleMessageVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggVehicleMessageVO[] clientBills) {
		for (AggVehicleMessageVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
