package nc.bs.vehicle.driverfiles.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.vehicle.driverfiles.AggDriverFiles;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceDriverFilesSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggDriverFiles[] sendApprove(AggDriverFiles[] clientBills,
			AggDriverFiles[] originBills) {
		for (AggDriverFiles clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggDriverFiles[] returnVos = new BillUpdate<AggDriverFiles>().update(
				clientBills, originBills);
		return returnVos;
	}
}
