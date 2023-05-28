package nc.bs.vehicle.inspectionfiles.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.vehicle.inspectionfiles.AggInspectionFileHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceInspectionFilesSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggInspectionFileHVO[] sendApprove(AggInspectionFileHVO[] clientBills,
			AggInspectionFileHVO[] originBills) {
		for (AggInspectionFileHVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggInspectionFileHVO[] returnVos = new BillUpdate<AggInspectionFileHVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
