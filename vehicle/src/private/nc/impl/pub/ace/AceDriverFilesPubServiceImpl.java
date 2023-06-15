package nc.impl.pub.ace;

import nc.bs.vehicle.driverfiles.ace.bp.AceDriverFilesInsertBP;
import nc.bs.vehicle.driverfiles.ace.bp.AceDriverFilesUpdateBP;
import nc.bs.vehicle.driverfiles.ace.bp.AceDriverFilesDeleteBP;
import nc.bs.vehicle.driverfiles.ace.bp.AceDriverFilesSendApproveBP;
import nc.bs.vehicle.driverfiles.ace.bp.AceDriverFilesUnSendApproveBP;
import nc.bs.vehicle.driverfiles.ace.bp.AceDriverFilesApproveBP;
import nc.bs.vehicle.driverfiles.ace.bp.AceDriverFilesUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.driverfiles.AggDriverFiles;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceDriverFilesPubServiceImpl {
	// 新增
	public AggDriverFiles[] pubinsertBills(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggDriverFiles> transferTool = new BillTransferTool<AggDriverFiles>(
					clientFullVOs);
			// 调用BP
			AceDriverFilesInsertBP action = new AceDriverFilesInsertBP();
			AggDriverFiles[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceDriverFilesDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggDriverFiles[] pubupdateBills(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggDriverFiles> transferTool = new BillTransferTool<AggDriverFiles>(
					clientFullVOs);
			AceDriverFilesUpdateBP bp = new AceDriverFilesUpdateBP();
			AggDriverFiles[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggDriverFiles[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggDriverFiles[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggDriverFiles> query = new BillLazyQuery<AggDriverFiles>(
					AggDriverFiles.class);
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
		String sql = queryScheme.getWhereSQLOnly();
	}

	// 提交
	public AggDriverFiles[] pubsendapprovebills(
			AggDriverFiles[] clientFullVOs, AggDriverFiles[] originBills)
			throws BusinessException {
		AceDriverFilesSendApproveBP bp = new AceDriverFilesSendApproveBP();
		AggDriverFiles[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggDriverFiles[] pubunsendapprovebills(
			AggDriverFiles[] clientFullVOs, AggDriverFiles[] originBills)
			throws BusinessException {
		AceDriverFilesUnSendApproveBP bp = new AceDriverFilesUnSendApproveBP();
		AggDriverFiles[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggDriverFiles[] pubapprovebills(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceDriverFilesApproveBP bp = new AceDriverFilesApproveBP();
		AggDriverFiles[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggDriverFiles[] pubunapprovebills(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceDriverFilesUnApproveBP bp = new AceDriverFilesUnApproveBP();
		AggDriverFiles[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}