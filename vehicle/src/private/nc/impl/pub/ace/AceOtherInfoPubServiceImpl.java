package nc.impl.pub.ace;

import nc.bs.vehicle.otherinfo.ace.bp.AceOtherInfoInsertBP;
import nc.bs.vehicle.otherinfo.ace.bp.AceOtherInfoUpdateBP;
import nc.bs.vehicle.otherinfo.ace.bp.AceOtherInfoDeleteBP;
import nc.bs.vehicle.otherinfo.ace.bp.AceOtherInfoSendApproveBP;
import nc.bs.vehicle.otherinfo.ace.bp.AceOtherInfoUnSendApproveBP;
import nc.bs.vehicle.otherinfo.ace.bp.AceOtherInfoApproveBP;
import nc.bs.vehicle.otherinfo.ace.bp.AceOtherInfoUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.otherinfo.AggOtherInfoVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceOtherInfoPubServiceImpl {
	// 新增
	public AggOtherInfoVO[] pubinsertBills(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggOtherInfoVO> transferTool = new BillTransferTool<AggOtherInfoVO>(
					clientFullVOs);
			// 调用BP
			AceOtherInfoInsertBP action = new AceOtherInfoInsertBP();
			AggOtherInfoVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceOtherInfoDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggOtherInfoVO[] pubupdateBills(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggOtherInfoVO> transferTool = new BillTransferTool<AggOtherInfoVO>(
					clientFullVOs);
			AceOtherInfoUpdateBP bp = new AceOtherInfoUpdateBP();
			AggOtherInfoVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggOtherInfoVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggOtherInfoVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggOtherInfoVO> query = new BillLazyQuery<AggOtherInfoVO>(
					AggOtherInfoVO.class);
			bills = query.query(queryScheme, null);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return bills;
	}

	/**
	 * 由子类实现，查询之前对queryScheme进行加工，加入自己的逻辑
	 * 
	 * @param queryScheme
	 */
	protected void preQuery(IQueryScheme queryScheme) {
		// 查询之前对queryScheme进行加工，加入自己的逻辑
	}

	// 提交
	public AggOtherInfoVO[] pubsendapprovebills(
			AggOtherInfoVO[] clientFullVOs, AggOtherInfoVO[] originBills)
			throws BusinessException {
		AceOtherInfoSendApproveBP bp = new AceOtherInfoSendApproveBP();
		AggOtherInfoVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggOtherInfoVO[] pubunsendapprovebills(
			AggOtherInfoVO[] clientFullVOs, AggOtherInfoVO[] originBills)
			throws BusinessException {
		AceOtherInfoUnSendApproveBP bp = new AceOtherInfoUnSendApproveBP();
		AggOtherInfoVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggOtherInfoVO[] pubapprovebills(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceOtherInfoApproveBP bp = new AceOtherInfoApproveBP();
		AggOtherInfoVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggOtherInfoVO[] pubunapprovebills(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceOtherInfoUnApproveBP bp = new AceOtherInfoUnApproveBP();
		AggOtherInfoVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}