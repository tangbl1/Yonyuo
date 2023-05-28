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
	// ����
	public AggInsuranceHVO[] pubinsertBills(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggInsuranceHVO> transferTool = new BillTransferTool<AggInsuranceHVO>(
					clientFullVOs);
			// ����BP
			AceInsuranceFilesInsertBP action = new AceInsuranceFilesInsertBP();
			AggInsuranceHVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceInsuranceFilesDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggInsuranceHVO[] pubupdateBills(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggInsuranceHVO> transferTool = new BillTransferTool<AggInsuranceHVO>(
					clientFullVOs);
			AceInsuranceFilesUpdateBP bp = new AceInsuranceFilesUpdateBP();
			AggInsuranceHVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
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
	 * ������ʵ�֣���ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
	 * 
	 * @param queryScheme
	 */
	protected void preQuery(IQueryScheme queryScheme) {
		// ��ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
	}

	// �ύ
	public AggInsuranceHVO[] pubsendapprovebills(
			AggInsuranceHVO[] clientFullVOs, AggInsuranceHVO[] originBills)
			throws BusinessException {
		AceInsuranceFilesSendApproveBP bp = new AceInsuranceFilesSendApproveBP();
		AggInsuranceHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggInsuranceHVO[] pubunsendapprovebills(
			AggInsuranceHVO[] clientFullVOs, AggInsuranceHVO[] originBills)
			throws BusinessException {
		AceInsuranceFilesUnSendApproveBP bp = new AceInsuranceFilesUnSendApproveBP();
		AggInsuranceHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggInsuranceHVO[] pubapprovebills(AggInsuranceHVO[] clientFullVOs,
			AggInsuranceHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceInsuranceFilesApproveBP bp = new AceInsuranceFilesApproveBP();
		AggInsuranceHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

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