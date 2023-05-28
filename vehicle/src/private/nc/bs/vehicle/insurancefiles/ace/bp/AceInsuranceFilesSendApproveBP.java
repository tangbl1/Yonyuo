package nc.bs.vehicle.insurancefiles.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.vehicle.insurance.AggInsuranceHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceInsuranceFilesSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggInsuranceHVO[] sendApprove(AggInsuranceHVO[] clientBills,
			AggInsuranceHVO[] originBills) {
		for (AggInsuranceHVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggInsuranceHVO[] returnVos = new BillUpdate<AggInsuranceHVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
