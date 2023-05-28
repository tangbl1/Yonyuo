package nc.impl.pub.ace;

import nc.bs.vehicle.insurancefiles.ace.bp.AceInsuranceFilesInsertBP;
import nc.bs.vehicle.insurancefiles.ace.bp.AceInsuranceFilesUpdateBP;
import nc.bs.vehicle.insurancefiles.ace.bp.AceInsuranceFilesDeleteBP;
import nc.bs.vehicle.insurancefiles.ace.bp.AceInsuranceFilesSendApproveBP;
import nc.bs.vehicle.insurancefiles.ace.bp.AceInsuranceFilesUnSendApproveBP;
import nc.bs.vehicle.insurancefiles.ace.bp.AceInsuranceFilesApproveBP;
import nc.bs.vehicle.insurancefiles.ace.bp.AceInsuranceFilesUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.insurance.AggInsuranceHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceInsuranceFilesPubServiceImpl {
	// 新增
	public AggInsuranceHVO[] pubinsertBills(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggInsuranceHVO> transferTool = new BillTransferTool<AggInsuranceHVO>(
					clientFullVOs);
			// 调用BP
			AceInsuranceFilesInsertBP action = new AceInsuranceFilesInsertBP();
			AggInsuranceHVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceInsuranceFilesDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggInsuranceHVO[] pubupdateBills(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggInsuranceHVO> transferTool = new BillTransferTool<AggInsuranceHVO>(
					clientFullVOs);
			AceInsuranceFilesUpdateBP bp = new AceInsuranceFilesUpdateBP();
			AggInsuranceHVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggInsuranceHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggInsuranceHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggInsuranceHVO> query = new BillLazyQuery<AggInsuranceHVO>(
					AggInsuranceHVO.class);
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
	public AggInsuranceHVO[] pubsendapprovebills(
			AggInsuranceHVO[] clientFullVOs, AggInsuranceHVO[] originBills)
			throws BusinessException {
		AceInsuranceFilesSendApproveBP bp = new AceInsuranceFilesSendApproveBP();
		AggInsuranceHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggInsuranceHVO[] pubunsendapprovebills(
			AggInsuranceHVO[] clientFullVOs, AggInsuranceHVO[] originBills)
			throws BusinessException {
		AceInsuranceFilesUnSendApproveBP bp = new AceInsuranceFilesUnSendApproveBP();
		AggInsuranceHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggInsuranceHVO[] pubapprovebills(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceInsuranceFilesApproveBP bp = new AceInsuranceFilesApproveBP();
		AggInsuranceHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggInsuranceHVO[] pubunapprovebills(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceInsuranceFilesUnApproveBP bp = new AceInsuranceFilesUnApproveBP();
		AggInsuranceHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}