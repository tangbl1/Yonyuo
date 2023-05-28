package nc.bs.vehicle.inspectionfiles.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.vehicle.inspectionfiles.AggInspectionFileHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼�����ջص�BP
 */
public class AceInspectionFilesUnSendApproveBP {

	public AggInspectionFileHVO[] unSend(AggInspectionFileHVO[] clientBills,
			AggInspectionFileHVO[] originBills) {
		// ��VO�־û������ݿ���
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggInspectionFileHVO> update = new BillUpdate<AggInspectionFileHVO>();
		AggInspectionFileHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggInspectionFileHVO[] clientBills) {
		for (AggInspectionFileHVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
