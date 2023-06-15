package nc.impl.pub.ace;

import nc.bs.vehicle.drivermileage.ace.bp.AceDriverMileageInsertBP;
import nc.bs.vehicle.drivermileage.ace.bp.AceDriverMileageUpdateBP;
import nc.bs.vehicle.drivermileage.ace.bp.AceDriverMileageDeleteBP;
import nc.bs.vehicle.drivermileage.ace.bp.AceDriverMileageSendApproveBP;
import nc.bs.vehicle.drivermileage.ace.bp.AceDriverMileageUnSendApproveBP;
import nc.bs.vehicle.drivermileage.ace.bp.AceDriverMileageApproveBP;
import nc.bs.vehicle.drivermileage.ace.bp.AceDriverMileageUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.drivermileage.AggDriverMileageHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceDriverMileagePubServiceImpl {
	// 新增
	public AggDriverMileageHVO[] pubinsertBills(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggDriverMileageHVO> transferTool = new BillTransferTool<AggDriverMileageHVO>(
					clientFullVOs);
			// 调用BP
			AceDriverMileageInsertBP action = new AceDriverMileageInsertBP();
			AggDriverMileageHVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceDriverMileageDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggDriverMileageHVO[] pubupdateBills(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggDriverMileageHVO> transferTool = new BillTransferTool<AggDriverMileageHVO>(
					clientFullVOs);
			AceDriverMileageUpdateBP bp = new AceDriverMileageUpdateBP();
			AggDriverMileageHVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggDriverMileageHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggDriverMileageHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggDriverMileageHVO> query = new BillLazyQuery<AggDriverMileageHVO>(
					AggDriverMileageHVO.class);
			bills = query.query(queryScheme, " where dr=0");
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
	public AggDriverMileageHVO[] pubsendapprovebills(
			AggDriverMileageHVO[] clientFullVOs, AggDriverMileageHVO[] originBills)
			throws BusinessException {
		AceDriverMileageSendApproveBP bp = new AceDriverMileageSendApproveBP();
		AggDriverMileageHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggDriverMileageHVO[] pubunsendapprovebills(
			AggDriverMileageHVO[] clientFullVOs, AggDriverMileageHVO[] originBills)
			throws BusinessException {
		AceDriverMileageUnSendApproveBP bp = new AceDriverMileageUnSendApproveBP();
		AggDriverMileageHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggDriverMileageHVO[] pubapprovebills(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceDriverMileageApproveBP bp = new AceDriverMileageApproveBP();
		AggDriverMileageHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggDriverMileageHVO[] pubunapprovebills(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceDriverMileageUnApproveBP bp = new AceDriverMileageUnApproveBP();
		AggDriverMileageHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}