package nc.bs.vehicle.otherinfo.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.vehicle.otherinfo.AggOtherInfoVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据送审的BP
 */
public class AceOtherInfoSendApproveBP {
	/**
	 * 送审动作
	 * 
	 * @param vos
	 *            单据VO数组
	 * @param script
	 *            单据动作脚本对象
	 * @return 送审后的单据VO数组
	 */

	public AggOtherInfoVO[] sendApprove(AggOtherInfoVO[] clientBills,
			AggOtherInfoVO[] originBills) {
		for (AggOtherInfoVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// 数据持久化
		AggOtherInfoVO[] returnVos = new BillUpdate<AggOtherInfoVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
