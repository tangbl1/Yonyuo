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
 * 标准单据审核的BP
 */
public class AceVorderApproveBP {

	/**
	 * 审核动作
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
			//判断审批状态，如果审批通过，修改单据状态为“部门审批完成”
			//单据状态判断：车管审批通过之后，部门经理审批不改变单据状态
			if("2".equals(clientBill.getParentVO().getAttributeValue("billstate"))){
				if(state == 1){
					clientBill.getParentVO().setAttributeValue("billstate", 3);
					String date=clientBill. getChildrenVO()[0].getAttributeValue("departtime").toString().substring(0, 10);//用车时间
					String pk_applier=clientBill.getChildrenVO()[0].getAttributeValue("applier").toString();//司机
					String applier = "";
					String sql = " select user_name from sm_user where cuserid='"+pk_applier+"'";//车管编码（丁喆）
					try {
						applier=(String) new BaseDAO().executeQuery(sql, new ColumnProcessor());
					} catch (DAOException e) {
						ExceptionUtils.wrappBusinessException("查询车管电话号异常");
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
	 * 发送友空间消息
	 * @return
	 */
	private boolean sendYonyouMessage(String txet, String pk_driver){
		boolean messageResult = false;
		// 获取access_token
		String accessToken = YonyouMessageUtil.getAccessToken();
		//查询车管的电话
		String field = "";
		String sql = "select bd_psndoc.mobile from bd_psndoc,org_orgs,cl_driver where org_orgs.pk_org = cl_driver.pk_org "
				+ "and bd_psndoc.code = decode(org_orgs.code,'1001','200503001','1002','201103001','') "
				+ "and cl_driver.pk_driver='" + pk_driver + "'";//车管编码（丁喆/张九鹏）
		try {
			field=(String) new BaseDAO().executeQuery(sql, new ColumnProcessor());
		} catch (DAOException e) {
			ExceptionUtils.wrappBusinessException("查询车管电话号异常");
		}
		
		String fieldtype = "1";
		//field="17609814307";
		// 获取MemberId（1：手机 2：邮箱）
		String memberId = YonyouMessageUtil.getMemberId(accessToken, field, fieldtype);
		List<String> tos = new ArrayList<String>();
		tos.add(memberId);
		String message =txet+ "有新的用车申请单部门审批已完成，请注意查看处理。";
		messageResult = YonyouMessageUtil.sendMessage(accessToken, YonyouMessageUtil.messagePojo(tos ,"车辆申请单审批提醒", message));
		return messageResult;
	}

}
