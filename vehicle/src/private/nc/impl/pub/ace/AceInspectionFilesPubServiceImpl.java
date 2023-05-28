package nc.impl.pub.ace;

import nc.bs.vehicle.inspectionfiles.ace.bp.AceInspectionFilesInsertBP;
import nc.bs.vehicle.inspectionfiles.ace.bp.AceInspectionFilesUpdateBP;
import nc.bs.vehicle.inspectionfiles.ace.bp.AceInspectionFilesDeleteBP;
import nc.bs.vehicle.inspectionfiles.ace.bp.AceInspectionFilesSendApproveBP;
import nc.bs.vehicle.inspectionfiles.ace.bp.AceInspectionFilesUnSendApproveBP;
import nc.bs.vehicle.inspectionfiles.ace.bp.AceInspectionFilesApproveBP;
import nc.bs.vehicle.inspectionfiles.ace.bp.AceInspectionFilesUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.inspectionfiles.AggInspectionFileHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceInspectionFilesPubServiceImpl {
	// 新增
	public AggInspectionFileHVO[] pubinsertBills(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggInspectionFileHVO> transferTool = new BillTransferTool<AggInspectionFileHVO>(
					clientFullVOs);
			// 调用BP
			AceInspectionFilesInsertBP action = new AceInspectionFilesInsertBP();
			AggInspectionFileHVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceInspectionFilesDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggInspectionFileHVO[] pubupdateBills(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggInspectionFileHVO> transferTool = new BillTransferTool<AggInspectionFileHVO>(
					clientFullVOs);
			AceInspectionFilesUpdateBP bp = new AceInspectionFilesUpdateBP();
			AggInspectionFileHVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggInspectionFileHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggInspectionFileHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggInspectionFileHVO> query = new BillLazyQuery<AggInspectionFileHVO>(
					AggInspectionFileHVO.class);
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
	public AggInspectionFileHVO[] pubsendapprovebills(
			AggInspectionFileHVO[] clientFullVOs, AggInspectionFileHVO[] originBills)
			throws BusinessException {
		AceInspectionFilesSendApproveBP bp = new AceInspectionFilesSendApproveBP();
		AggInspectionFileHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggInspectionFileHVO[] pubunsendapprovebills(
			AggInspectionFileHVO[] clientFullVOs, AggInspectionFileHVO[] originBills)
			throws BusinessException {
		AceInspectionFilesUnSendApproveBP bp = new AceInspectionFilesUnSendApproveBP();
		AggInspectionFileHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggInspectionFileHVO[] pubapprovebills(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceInspectionFilesApproveBP bp = new AceInspectionFilesApproveBP();
		AggInspectionFileHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggInspectionFileHVO[] pubunapprovebills(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceInspectionFilesUnApproveBP bp = new AceInspectionFilesUnApproveBP();
		AggInspectionFileHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}