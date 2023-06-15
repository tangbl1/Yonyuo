package nc.impl.pub.ace;

import nc.bs.vehicle.vorder.ace.bp.AceVorderInsertBP;
import nc.bs.vehicle.vorder.ace.bp.AceVorderUpdateBP;
import nc.bs.vehicle.vorder.ace.bp.AceVorderDeleteBP;
import nc.bs.vehicle.vorder.ace.bp.AceVorderSendApproveBP;
import nc.bs.vehicle.vorder.ace.bp.AceVorderUnSendApproveBP;
import nc.bs.vehicle.vorder.ace.bp.AceVorderApproveBP;
import nc.bs.vehicle.vorder.ace.bp.AceVorderUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.vorder.AggVorderHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceVorderPubServiceImpl {
	// 新增
	public AggVorderHVO[] pubinsertBills(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggVorderHVO> transferTool = new BillTransferTool<AggVorderHVO>(
					clientFullVOs);
			// 调用BP
			AceVorderInsertBP action = new AceVorderInsertBP();
			AggVorderHVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceVorderDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggVorderHVO[] pubupdateBills(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggVorderHVO> transferTool = new BillTransferTool<AggVorderHVO>(
					clientFullVOs);
			AceVorderUpdateBP bp = new AceVorderUpdateBP();
			AggVorderHVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggVorderHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggVorderHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggVorderHVO> query = new BillLazyQuery<AggVorderHVO>(
					AggVorderHVO.class);
			bills = query.query(queryScheme,null );
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
	public AggVorderHVO[] pubsendapprovebills(
			AggVorderHVO[] clientFullVOs, AggVorderHVO[] originBills)
			throws BusinessException {
		AceVorderSendApproveBP bp = new AceVorderSendApproveBP();
		AggVorderHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggVorderHVO[] pubunsendapprovebills(
			AggVorderHVO[] clientFullVOs, AggVorderHVO[] originBills)
			throws BusinessException {
		AceVorderUnSendApproveBP bp = new AceVorderUnSendApproveBP();
		AggVorderHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggVorderHVO[] pubapprovebills(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceVorderApproveBP bp = new AceVorderApproveBP();
		AggVorderHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggVorderHVO[] pubunapprovebills(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
			clientFullVOs[i].getParentVO().setAttributeValue("billstate", 2);
		}
		AceVorderUnApproveBP bp = new AceVorderUnApproveBP();
		AggVorderHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}