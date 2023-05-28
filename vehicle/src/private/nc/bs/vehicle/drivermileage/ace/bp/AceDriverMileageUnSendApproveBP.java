package nc.bs.vehicle.drivermileage.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.vehicle.drivermileage.AggDriverMileageHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼�����ջص�BP
 */
public class AceDriverMileageUnSendApproveBP {

	public AggDriverMileageHVO[] unSend(AggDriverMileageHVO[] clientBills,
			AggDriverMileageHVO[] originBills) {
		// ��VO�־û������ݿ���
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggDriverMileageHVO> update = new BillUpdate<AggDriverMileageHVO>();
		AggDriverMileageHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggDriverMileageHVO[] clientBills) {
		for (AggDriverMileageHVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
