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
	// ����
	public AggInspectionFileHVO[] pubinsertBills(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggInspectionFileHVO> transferTool = new BillTransferTool<AggInspectionFileHVO>(
					clientFullVOs);
			// ����BP
			AceInspectionFilesInsertBP action = new AceInspectionFilesInsertBP();
			AggInspectionFileHVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceInspectionFilesDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggInspectionFileHVO[] pubupdateBills(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggInspectionFileHVO> transferTool = new BillTransferTool<AggInspectionFileHVO>(
					clientFullVOs);
			AceInspectionFilesUpdateBP bp = new AceInspectionFilesUpdateBP();
			AggInspectionFileHVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
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
	 * ������ʵ�֣���ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
	 * 
	 * @param queryScheme
	 */
	protected void preQuery(IQueryScheme queryScheme) {
		// ��ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
	}

	// �ύ
	public AggInspectionFileHVO[] pubsendapprovebills(
			AggInspectionFileHVO[] clientFullVOs, AggInspectionFileHVO[] originBills)
			throws BusinessException {
		AceInspectionFilesSendApproveBP bp = new AceInspectionFilesSendApproveBP();
		AggInspectionFileHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggInspectionFileHVO[] pubunsendapprovebills(
			AggInspectionFileHVO[] clientFullVOs, AggInspectionFileHVO[] originBills)
			throws BusinessException {
		AceInspectionFilesUnSendApproveBP bp = new AceInspectionFilesUnSendApproveBP();
		AggInspectionFileHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggInspectionFileHVO[] pubapprovebills(AggInspectionFileHVO[] clientFullVOs,
			AggInspectionFileHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceInspectionFilesApproveBP bp = new AceInspectionFilesApproveBP();
		AggInspectionFileHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

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