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
	// ����
	public AggDriverFiles[] pubinsertBills(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggDriverFiles> transferTool = new BillTransferTool<AggDriverFiles>(
					clientFullVOs);
			// ����BP
			AceDriverFilesInsertBP action = new AceDriverFilesInsertBP();
			AggDriverFiles[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceDriverFilesDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggDriverFiles[] pubupdateBills(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggDriverFiles> transferTool = new BillTransferTool<AggDriverFiles>(
					clientFullVOs);
			AceDriverFilesUpdateBP bp = new AceDriverFilesUpdateBP();
			AggDriverFiles[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
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
	 * ������ʵ�֣���ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
	 * 
	 * @param queryScheme
	 */
	protected void preQuery(IQueryScheme queryScheme) {
		// ��ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
		String sql = queryScheme.getWhereSQLOnly();
	}

	// �ύ
	public AggDriverFiles[] pubsendapprovebills(
			AggDriverFiles[] clientFullVOs, AggDriverFiles[] originBills)
			throws BusinessException {
		AceDriverFilesSendApproveBP bp = new AceDriverFilesSendApproveBP();
		AggDriverFiles[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggDriverFiles[] pubunsendapprovebills(
			AggDriverFiles[] clientFullVOs, AggDriverFiles[] originBills)
			throws BusinessException {
		AceDriverFilesUnSendApproveBP bp = new AceDriverFilesUnSendApproveBP();
		AggDriverFiles[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggDriverFiles[] pubapprovebills(AggDriverFiles[] clientFullVOs,
			AggDriverFiles[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceDriverFilesApproveBP bp = new AceDriverFilesApproveBP();
		AggDriverFiles[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

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