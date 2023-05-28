package nc.impl.pub.ace;

import nc.bs.vehicle.vehicle.ace.bp.AceVehicleInsertBP;
import nc.bs.vehicle.vehicle.ace.bp.AceVehicleUpdateBP;
import nc.bs.vehicle.vehicle.ace.bp.AceVehicleDeleteBP;
import nc.bs.vehicle.vehicle.ace.bp.AceVehicleSendApproveBP;
import nc.bs.vehicle.vehicle.ace.bp.AceVehicleUnSendApproveBP;
import nc.bs.vehicle.vehicle.ace.bp.AceVehicleApproveBP;
import nc.bs.vehicle.vehicle.ace.bp.AceVehicleUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.vehicle.AggVehicleMessageVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceVehiclePubServiceImpl {
	// ����
	public AggVehicleMessageVO[] pubinsertBills(AggVehicleMessageVO[] clientFullVOs,
			AggVehicleMessageVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggVehicleMessageVO> transferTool = new BillTransferTool<AggVehicleMessageVO>(
					clientFullVOs);
			// ����BP
			AceVehicleInsertBP action = new AceVehicleInsertBP();
			AggVehicleMessageVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggVehicleMessageVO[] clientFullVOs,
			AggVehicleMessageVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceVehicleDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggVehicleMessageVO[] pubupdateBills(AggVehicleMessageVO[] clientFullVOs,
			AggVehicleMessageVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggVehicleMessageVO> transferTool = new BillTransferTool<AggVehicleMessageVO>(
					clientFullVOs);
			AceVehicleUpdateBP bp = new AceVehicleUpdateBP();
			AggVehicleMessageVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggVehicleMessageVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggVehicleMessageVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggVehicleMessageVO> query = new BillLazyQuery<AggVehicleMessageVO>(
					AggVehicleMessageVO.class);
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
	public AggVehicleMessageVO[] pubsendapprovebills(
			AggVehicleMessageVO[] clientFullVOs, AggVehicleMessageVO[] originBills)
			throws BusinessException {
		AceVehicleSendApproveBP bp = new AceVehicleSendApproveBP();
		AggVehicleMessageVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggVehicleMessageVO[] pubunsendapprovebills(
			AggVehicleMessageVO[] clientFullVOs, AggVehicleMessageVO[] originBills)
			throws BusinessException {
		AceVehicleUnSendApproveBP bp = new AceVehicleUnSendApproveBP();
		AggVehicleMessageVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggVehicleMessageVO[] pubapprovebills(AggVehicleMessageVO[] clientFullVOs,
			AggVehicleMessageVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceVehicleApproveBP bp = new AceVehicleApproveBP();
		AggVehicleMessageVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

	public AggVehicleMessageVO[] pubunapprovebills(AggVehicleMessageVO[] clientFullVOs,
			AggVehicleMessageVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceVehicleUnApproveBP bp = new AceVehicleUnApproveBP();
		AggVehicleMessageVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}