package nc.bs.vehicle.drivermileage.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.vehicle.drivermileage.AggDriverMileageHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceDriverMileageSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggDriverMileageHVO[] sendApprove(AggDriverMileageHVO[] clientBills,
			AggDriverMileageHVO[] originBills) {
		for (AggDriverMileageHVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggDriverMileageHVO[] returnVos = new BillUpdate<AggDriverMileageHVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
