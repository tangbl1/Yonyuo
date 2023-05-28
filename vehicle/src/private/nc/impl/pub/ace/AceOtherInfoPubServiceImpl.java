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
	// ����
	public AggOtherInfoVO[] pubinsertBills(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggOtherInfoVO> transferTool = new BillTransferTool<AggOtherInfoVO>(
					clientFullVOs);
			// ����BP
			AceOtherInfoInsertBP action = new AceOtherInfoInsertBP();
			AggOtherInfoVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceOtherInfoDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggOtherInfoVO[] pubupdateBills(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggOtherInfoVO> transferTool = new BillTransferTool<AggOtherInfoVO>(
					clientFullVOs);
			AceOtherInfoUpdateBP bp = new AceOtherInfoUpdateBP();
			AggOtherInfoVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
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
	 * ������ʵ�֣���ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
	 * 
	 * @param queryScheme
	 */
	protected void preQuery(IQueryScheme queryScheme) {
		// ��ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
	}

	// �ύ
	public AggOtherInfoVO[] pubsendapprovebills(
			AggOtherInfoVO[] clientFullVOs, AggOtherInfoVO[] originBills)
			throws BusinessException {
		AceOtherInfoSendApproveBP bp = new AceOtherInfoSendApproveBP();
		AggOtherInfoVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggOtherInfoVO[] pubunsendapprovebills(
			AggOtherInfoVO[] clientFullVOs, AggOtherInfoVO[] originBills)
			throws BusinessException {
		AceOtherInfoUnSendApproveBP bp = new AceOtherInfoUnSendApproveBP();
		AggOtherInfoVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggOtherInfoVO[] pubapprovebills(AggOtherInfoVO[] clientFullVOs,
			AggOtherInfoVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceOtherInfoApproveBP bp = new AceOtherInfoApproveBP();
		AggOtherInfoVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

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