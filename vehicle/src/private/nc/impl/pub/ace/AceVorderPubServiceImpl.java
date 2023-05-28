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
	// ����
	public AggVorderHVO[] pubinsertBills(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggVorderHVO> transferTool = new BillTransferTool<AggVorderHVO>(
					clientFullVOs);
			// ����BP
			AceVorderInsertBP action = new AceVorderInsertBP();
			AggVorderHVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceVorderDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggVorderHVO[] pubupdateBills(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggVorderHVO> transferTool = new BillTransferTool<AggVorderHVO>(
					clientFullVOs);
			AceVorderUpdateBP bp = new AceVorderUpdateBP();
			AggVorderHVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
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
	 * ������ʵ�֣���ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
	 * 
	 * @param queryScheme
	 */
	protected void preQuery(IQueryScheme queryScheme) {
		// ��ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
	}

	// �ύ
	public AggVorderHVO[] pubsendapprovebills(
			AggVorderHVO[] clientFullVOs, AggVorderHVO[] originBills)
			throws BusinessException {
		AceVorderSendApproveBP bp = new AceVorderSendApproveBP();
		AggVorderHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggVorderHVO[] pubunsendapprovebills(
			AggVorderHVO[] clientFullVOs, AggVorderHVO[] originBills)
			throws BusinessException {
		AceVorderUnSendApproveBP bp = new AceVorderUnSendApproveBP();
		AggVorderHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggVorderHVO[] pubapprovebills(AggVorderHVO[] clientFullVOs,
			AggVorderHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceVorderApproveBP bp = new AceVorderApproveBP();
		AggVorderHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

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