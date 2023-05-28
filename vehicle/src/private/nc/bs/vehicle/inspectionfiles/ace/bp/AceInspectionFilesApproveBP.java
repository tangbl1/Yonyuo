package nc.bs.vehicle.inspectionfiles.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.vehicle.inspectionfiles.AggInspectionFileHVO;

/**
 * ��׼������˵�BP
 */
public class AceInspectionFilesApproveBP {

	/**
	 * ��˶���
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggInspectionFileHVO[] approve(AggInspectionFileHVO[] clientBills,
			AggInspectionFileHVO[] originBills) {
		for (AggInspectionFileHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggInspectionFileHVO> update = new BillUpdate<AggInspectionFileHVO>();
		AggInspectionFileHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
