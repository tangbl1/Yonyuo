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
	// ����
	public AggDriverMileageHVO[] pubinsertBills(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException {
		try {
			// ���ݿ������ݺ�ǰ̨���ݹ����Ĳ���VO�ϲ���Ľ��
			BillTransferTool<AggDriverMileageHVO> transferTool = new BillTransferTool<AggDriverMileageHVO>(
					clientFullVOs);
			// ����BP
			AceDriverMileageInsertBP action = new AceDriverMileageInsertBP();
			AggDriverMileageHVO[] retvos = action.insert(clientFullVOs);
			// ���췵������
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// ɾ��
	public void pubdeleteBills(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException {
		try {
			// ����BP
			new AceDriverMileageDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// �޸�
	public AggDriverMileageHVO[] pubupdateBills(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException {
		try {
			// ���� + ���ts
			BillTransferTool<AggDriverMileageHVO> transferTool = new BillTransferTool<AggDriverMileageHVO>(
					clientFullVOs);
			AceDriverMileageUpdateBP bp = new AceDriverMileageUpdateBP();
			AggDriverMileageHVO[] retvos = bp.update(clientFullVOs, originBills);
			// ���췵������
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
	 * ������ʵ�֣���ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
	 * 
	 * @param queryScheme
	 */
	protected void preQuery(IQueryScheme queryScheme) {
		// ��ѯ֮ǰ��queryScheme���мӹ��������Լ����߼�
	}

	// �ύ
	public AggDriverMileageHVO[] pubsendapprovebills(
			AggDriverMileageHVO[] clientFullVOs, AggDriverMileageHVO[] originBills)
			throws BusinessException {
		AceDriverMileageSendApproveBP bp = new AceDriverMileageSendApproveBP();
		AggDriverMileageHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// �ջ�
	public AggDriverMileageHVO[] pubunsendapprovebills(
			AggDriverMileageHVO[] clientFullVOs, AggDriverMileageHVO[] originBills)
			throws BusinessException {
		AceDriverMileageUnSendApproveBP bp = new AceDriverMileageUnSendApproveBP();
		AggDriverMileageHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// ����
	public AggDriverMileageHVO[] pubapprovebills(AggDriverMileageHVO[] clientFullVOs,
			AggDriverMileageHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceDriverMileageApproveBP bp = new AceDriverMileageApproveBP();
		AggDriverMileageHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// ����

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