package nc.bs.vehicle.vorder.ace.bp;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.itf.vehicle.util.YonyouMessageUtil;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.vehicle.vorder.AggVorderHVO;

import java.util.ArrayList;
import java.util.List;

/**
 * ��׼������˵�BP
 */
public class AceVorderApproveBP {

	/**
	 * ��˶���
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggVorderHVO[] approve(AggVorderHVO[] clientBills,
			AggVorderHVO[] originBills) {
		Integer state = null;
		for (AggVorderHVO clientBill : clientBills) {
			state = (Integer) clientBill.getParentVO().getAttributeValue("approvestatus");
			//�ж�����״̬���������ͨ�����޸ĵ���״̬Ϊ������������ɡ�
			//����״̬�жϣ���������ͨ��֮�󣬲��ž����������ı䵥��״̬
			if("2".equals(clientBill.getParentVO().getAttributeValue("billstate"))){
				if(state == 1){
					clientBill.getParentVO().setAttributeValue("billstate", 3);
					String date=clientBill. getChildrenVO()[0].getAttributeValue("departtime").toString().substring(0, 10);//�ó�ʱ��
					String pk_applier=clientBill.getChildrenVO()[0].getAttributeValue("applier").toString();//˾��
					String applier = "";
					String sql = " select user_name from sm_user where cuserid='"+pk_applier+"'";//���ܱ��루������
					try {
						applier=(String) new BaseDAO().executeQuery(sql, new ColumnProcessor());
					} catch (DAOException e) {
						ExceptionUtils.wrappBusinessException("��ѯ���ܵ绰���쳣");
					}
					String txet=date+applier;
					;
					sendYonyouMessage(txet, (String) clientBill.getParentVO().getAttributeValue("pk_driver"));
				}else{
					clientBill.getParentVO().setAttributeValue("billstate", 9);
				}
			}
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggVorderHVO> update = new BillUpdate<AggVorderHVO>();
		AggVorderHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
	/**
	 * �����ѿռ���Ϣ
	 * @return
	 */
	private boolean sendYonyouMessage(String txet, String pk_driver){
		boolean messageResult = false;
		// ��ȡaccess_token
		String accessToken = YonyouMessageUtil.getAccessToken();
		//��ѯ���ܵĵ绰
		String field = "";
		String sql = "select bd_psndoc.mobile from bd_psndoc,org_orgs,cl_driver where org_orgs.pk_org = cl_driver.pk_org "
				+ "and bd_psndoc.code = decode(org_orgs.code,'1001','200503001','1002','201103001','') "
				+ "and cl_driver.pk_driver='" + pk_driver + "'";//���ܱ��루����/�ž�����
		try {
			field=(String) new BaseDAO().executeQuery(sql, new ColumnProcessor());
		} catch (DAOException e) {
			ExceptionUtils.wrappBusinessException("��ѯ���ܵ绰���쳣");
		}
		
		String fieldtype = "1";
		//field="17609814307";
		// ��ȡMemberId��1���ֻ� 2�����䣩
		String memberId = YonyouMessageUtil.getMemberId(accessToken, field, fieldtype);
		List<String> tos = new ArrayList<String>();
		tos.add(memberId);
		String message =txet+ "���µ��ó����뵥������������ɣ���ע��鿴����";
		messageResult = YonyouMessageUtil.sendMessage(accessToken, YonyouMessageUtil.messagePojo(tos ,"�������뵥��������", message));
		return messageResult;
	}

}
