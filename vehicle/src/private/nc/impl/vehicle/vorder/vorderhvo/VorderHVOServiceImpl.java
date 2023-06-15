package nc.impl.vehicle.vorder.vorderhvo;

import java.util.Map;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.UUID;
import java.lang.String;
import java.util.stream.Stream;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.itf.vehicle.util.YonyouMessageUtil;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.uap.utils.InSQLCreator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.beanutils.PropertyUtils;


import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.codeplatform.framework.service.ServiceSupport;

import nc.pub.billcode.vo.BillCodeContext;

import nc.vo.vehicle.vorder.VorderHVO;
import nc.vo.vehicle.vorder.VorderBVO;
import nc.vo.vehicle.vorder.AggVorderHVO;
import nc.itf.vehicle.vorder.vorderhvo.IVorderHVOService;


import nc.vo.pub.pf.BillStatusEnum;
import nc.bs.framework.common.InvocationInfoProxy;
import nccloud.framework.core.exception.ExceptionUtils;

public class  VorderHVOServiceImpl extends ServiceSupport implements IVorderHVOService {


	@Override
	public AggVorderHVO[] listAggVorderHVOByPk(String...pks) throws BusinessException{
		return listAggVorderHVOByPk(false,pks);
	}

	@Override
	public AggVorderHVO[] listAggVorderHVOByPk(boolean blazyLoad,String... pks) throws BusinessException{
		return dao.listByPksWithOrder(AggVorderHVO.class,pks,blazyLoad);
	}

	@Override
	public AggVorderHVO findAggVorderHVOByPk(String pk) throws BusinessException{
		return dao.findByPk(AggVorderHVO.class, pk, false);
	}

	@Override
	public  AggVorderHVO[] listAggVorderHVOByCondition(String condition) throws BusinessException{
		return listAggVorderHVOByCondition(condition,new String[]{"pk_vorder"});
	}
	@Override
	public  AggVorderHVO[] listAggVorderHVOByCondition(String condition,String[] orderPath) throws BusinessException{
		return dao.listByCondition(AggVorderHVO.class, condition, false,false,orderPath);
	}
	@Override
	public VorderHVO[] listVorderHVOByPk(String... pks) throws BusinessException{
		return dao.listByPk(VorderHVO.class, pks, true);
	}

	@Override
	public  VorderHVO findVorderHVOByPk(String pk) throws BusinessException{
		return dao.findByPk(VorderHVO.class, pk, true);
	}

	@Override
	public  VorderHVO[] listVorderHVOByCondition(String condition) throws BusinessException{
		return listVorderHVOByCondition(condition,new String[]{"pk_vorder"});
	}
	@Override
	public  VorderHVO[] listVorderHVOByCondition(String condition,String[] orderPath) throws BusinessException{
		return dao.listByCondition(VorderHVO.class, condition, false,false,orderPath);
	}

	@Override
	public String[] listVorderHVOPkByCond(String condition) throws BusinessException{
		return listVorderHVOPkByCond(condition,new String[]{"pk_vorder"});
	}
	@Override
	public String[] listVorderHVOPkByCond(String condition,String[] orderPath) throws BusinessException{
		if(StringUtils.isEmpty(condition)) {
			condition = " 1 = 1 ";
		}
		StringBuilder sql = new StringBuilder();
		sql.append(" select pk_vorder from ").append(VorderHVO.getDefaultTableName());
		sql.append(" where ").append(condition);
		if (ArrayUtils.isNotEmpty(orderPath)) {
			sql.append(" order by ").append(StringUtils.join(orderPath, ", "));
		}
		return
				(String[]) dao.getBaseDAO().executeQuery(sql.toString(), (rs) -> {
			List<String> pks = new ArrayList<>();
			while (rs.next()) {
				pks.add(rs.getString(1));
			}
			return pks.toArray(new String[0]);
		});
	}
	@Override
	public void initDefaultData(VorderHVO vo){
		if(vo.getAttributeValue("pk_group") == null){
			vo.setAttributeValue("pk_group",InvocationInfoProxy.getInstance().getGroupId());
		}
		if(vo.getAttributeValue("pk_org") == null){
			vo.setAttributeValue("pk_org",InvocationInfoProxy.getInstance().getGroupId());
		}
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		if(vo.getAttributeValue("billmaker") == null){
			vo.setAttributeValue("billmaker",InvocationInfoProxy.getInstance().getUserId());
		}
		if(vo.getAttributeValue("maketime") == null){
			vo.setAttributeValue("maketime",new UFDateTime(InvocationInfoProxy.getInstance().getBizDateTime()));
		}
		if(vo.getAttributeValue("billtype") == null){
			vo.setAttributeValue("billtype","VORD");
		}
		if(vo.getAttributeValue("approvestatus") == null){
			vo.setAttributeValue("approvestatus",BillStatusEnum.FREE.toIntValue());
		}
		if(vo.getAttributeValue("vbilldate") == null){
			vo.setAttributeValue("vbilldate",new UFDate());
		}
	}
	@Override
	public AggVorderHVO preAddAggVorderHVO(AggVorderHVO vo,Map<String,Object> userJson) throws BusinessException{

		getMainVO(vo).setStatus(VOStatus.NEW);
		initDefaultData((VorderHVO)getMainVO(vo));

		//下面这段要判断是否是树表界面插件
		Map<String,String> data = userJson!=null && userJson.get("data") != null?(Map<String,String>)userJson.get("data"):null;
		if(data!=null && data.size()>0){
			String parentKey = data.get("parentKey");
			String parentPk = data.get("parentPk");
			getMainVO(vo).setAttributeValue(parentKey,parentPk);
		}

		//编码规则生成vo的编码
		BillCodeContext billCodeContext = getBillCodeContext("vehiclevorder");
		if(billCodeContext == null){
			throw new BusinessException("当前编码规则不存在，请到【编码规则定义-全局】节点检查是否存在"+"vehiclevorder");
		}
		if(billCodeContext.isPrecode()){
			String pk_group = InvocationInfoProxy.getInstance().getGroupId();
			String billno = getBillcodeManage().getPreBillCode_RequiresNew("vehiclevorder", pk_group, pk_group);
			getMainVO(vo).setAttributeValue("billno",billno);
		}

		return vo;
	}
	@Override
	public AggVorderHVO preAddAggVorderHVO(Map<String,Object> userJson) throws BusinessException{
		AggVorderHVO result = null;

		VorderHVO mainvo = new VorderHVO();
		//设置默认值
		initDefaultData(mainvo);
		AggVorderHVO aggvo = new AggVorderHVO();
		aggvo.setParent(mainvo);
		result = aggvo;
		return preAddAggVorderHVO(result,userJson);
	}

	@Override
	public AggVorderHVO preEditAggVorderHVO(String pk) throws BusinessException{
		return dao.findByPk(AggVorderHVO.class, pk, false);
	}

	@Override
	public AggVorderHVO copyAggVorderHVO(String pk) throws BusinessException{

		AggVorderHVO vo = dao.findByPk(AggVorderHVO.class, pk, false);

		getMainVO(vo).setPrimaryKey(null);
		getMainVO(vo).setStatus(VOStatus.NEW);

		getMainVO(vo).setAttributeValue("srcbilltype",null);
		getMainVO(vo).setAttributeValue("srcbillid",null);

		getMainVO(vo).setAttributeValue("billno",null);
		getMainVO(vo).setAttributeValue("billno",null);
		getMainVO(vo).setAttributeValue("name",null);
		//编码规则生成vo的编码
		BillCodeContext billCodeContext = getBillCodeContext("vehiclevorder");
		if(billCodeContext == null){
			throw new BusinessException("当前编码规则不存在，请到【编码规则定义-全局】节点检查是否存在"+"vehiclevorder");
		}
		if(billCodeContext.isPrecode()){
			String pk_group = InvocationInfoProxy.getInstance().getGroupId();
			String billno = getBillcodeManage().getPreBillCode_RequiresNew("vehiclevorder", pk_group, pk_group);
			getMainVO(vo).setAttributeValue("billno",billno);
		}
		getMainVO(vo).setAttributeValue("approvestatus", BillStatusEnum.FREE.toIntValue());
		getMainVO(vo).setAttributeValue("billmaker", InvocationInfoProxy.getInstance().getUserId());
		getMainVO(vo).setAttributeValue("approver", null);
		getMainVO(vo).setAttributeValue("approvenote", null);
		getMainVO(vo).setAttributeValue("approvedate", null);
		//设置审计信息为空
		getMainVO(vo).setAttributeValue("creator",null);
		getMainVO(vo).setAttributeValue("creationtime",null);
		getMainVO(vo).setAttributeValue("modifier",null);
		getMainVO(vo).setAttributeValue("modifiedtime",null);
		getMainVO(vo).setAttributeValue("maketime", new UFDateTime(InvocationInfoProxy.getInstance().getBizDateTime()));

		VorderBVO[] vorderBVOs = (VorderBVO[])vo.getChildren(VorderBVO.class);
		if(vorderBVOs!=null && vorderBVOs.length>0){
			Arrays.stream(vorderBVOs).forEach(subvo->{
				subvo.setPrimaryKey(null);
				subvo.setStatus(VOStatus.NEW);
				subvo.setAttributeValue("srcbilltype",null);
				subvo.setAttributeValue("srcbillid",null);
				subvo.setAttributeValue("rowno", null);
				subvo.setAttributeValue("", null);
			});
		}
		return vo;
	}
	@Override
	public AggVorderHVO[] saveAggVorderHVO(AggVorderHVO vo) throws BusinessException{
		String pk = getVOPrimaryKey(vo);
		setDefaultVal(vo);
		if(StringUtils.isEmpty(pk)){
			return dao.insert(vo); //插入
		}else{
			return dao.update(vo); //更新
		}
	}
	/**
	 * 保存前处理编码规则
	 * @param vos
	 */
	private void setBillCode(AggVorderHVO... vos) throws BusinessException {
		if(ArrayUtils.isNotEmpty(vos)) {
			for(AggVorderHVO vo : vos) {
				String pk = getVOPrimaryKey(vo);
				if(StringUtils.isEmpty(pk)){
					BillCodeContext billCodeContext = getBillCodeContext("vehiclevorder");
					String pk_group = InvocationInfoProxy.getInstance().getGroupId();
					if(billCodeContext!=null && !billCodeContext.isPrecode()){
						if(getMainVO(vo).getAttributeValue("billno") == null){
								String billno = getBillcodeManage().getBillCode_RequiresNew("vehiclevorder", pk_group, pk_group, getMainVO(vo));
							getMainVO(vo).setAttributeValue("billno",billno);
						}
					} else {
						String billno = (String) getMainVO(vo).getAttributeValue("billno");
						getBillcodeManage().commitPreBillCode("VORD", pk_group, pk_group, billno);
					}
				}
			}
		}
	}
	/**
	 * 保存前设置审计信息
	 * @param vos
	 */
	private void setAuditInfo(AggVorderHVO... vos) throws BusinessException {
		if(ArrayUtils.isNotEmpty(vos)) {
			UFDateTime now = new UFDateTime();
			for(AggVorderHVO vo : vos) {
				String pk = getVOPrimaryKey(vo);
				if(StringUtils.isEmpty(pk)){
					//设置创建人创建时间
					getMainVO(vo).setAttributeValue("creator",InvocationInfoProxy.getInstance().getUserId());
					getMainVO(vo).setAttributeValue("creationtime",now);
					getMainVO(vo).setAttributeValue("maketime",now);
					getMainVO(vo).setAttributeValue("billmaker", InvocationInfoProxy.getInstance().getUserId()); // 制单人
					getMainVO(vo).setAttributeValue("modifier",null);
					getMainVO(vo).setAttributeValue("modifiedtime",null);
				}else{
					//设置修改人修改时间
					getMainVO(vo).setAttributeValue("modifier",InvocationInfoProxy.getInstance().getUserId());
					getMainVO(vo).setAttributeValue("modifiedtime",now);
					getMainVO(vo).setAttributeValue("lastmaketime",now);
				}
			}
		}
	}
	/**
	 * 保存前处理一些默认值
	 * @param vos
	 */
	private void setDefaultVal(AggVorderHVO... vos) throws BusinessException {
		setBillCode(vos);
		setAuditInfo(vos);
		//其他默认值处理
	}

	// 给单表（行编辑表）单独适配
	@Override
	public AggVorderHVO[] saveAggVorderHVO(AggVorderHVO[] vos) throws BusinessException {
		if (ArrayUtils.isEmpty(vos)) {
			return new AggVorderHVO[0];
		}
		setDefaultVal(vos); // 设置默认值
		List<String> pks = Arrays.stream(vos).filter(v -> getMainVO(v).getStatus() == VOStatus.DELETED)
				.map(v -> getMainVO(v).getPrimaryKey()).collect(Collectors.toList()); // 删除单据主键
		if (pks == null || pks.size() == 0) {
			return dao.save(vos, true);
		}
		AggVorderHVO[] deleteVOs = dao.listByPk(AggVorderHVO.class, pks.toArray(new String[0]));
		for (int i = 0; i < deleteVOs.length; i++) {
			SuperVO mainVO = getMainVO(deleteVOs[i]);
			// 删除单据时校验单据状态
			Integer approveStatus = (Integer) mainVO.getAttributeValue("approvestatus");
			if (approveStatus != null && !approveStatus.equals(-1)) {
				throw new BusinessException("第" + (i + 1) + "张单据处理失败：单据状态不正确，不能删除！");
			}
			// 删除单据时回退单据号
			String billno = (String)mainVO.getAttributeValue("billno");
			if (StringUtils.isNotEmpty(billno)) {
				String pk_group = InvocationInfoProxy.getInstance().getGroupId();
				getBillcodeManage().returnBillCodeOnDelete("vehiclevorder", pk_group, pk_group, billno, deleteVOs[i]);
			}
		}
		return dao.save(vos,true);
	}

	@Override
	public AggVorderHVO[] deleteAggVorderHVOs(Map<String,String> tsMap) throws BusinessException{
		AggVorderHVO[] vos = dao.listByPk(AggVorderHVO.class,tsMap.keySet().toArray(new String[0]));
		validate(vos,tsMap);
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		Arrays.stream(vos).forEach(vo->{
			String billno = (String)getMainVO(vo).getAttributeValue("billno");
			try {
				getBillcodeManage().returnBillCodeOnDelete("vehiclevorder",pk_group,pk_group,billno,vo);
			} catch (BusinessException e) {
				ExceptionUtils.wrapException(e.getMessage(),e);
			}
		});
		dao.delete(vos,true);
		return vos;
	}
	
	//校验  包括ts校验  已提交校验
	private void validate(AggVorderHVO[] vos,Map<String,String> tsMap) throws BusinessException{

		Boolean isPass = true;
		String error = "";    //错误信息
		if(ArrayUtils.isEmpty(vos)){
			isPass = false;
		}
		
		for(int i = 0 ; i < vos.length ; i++){
			SuperVO mainvo = vos[i].getParentVO();
			UFDateTime ts = (UFDateTime)mainvo.getAttributeValue("ts");
			if(!StringUtils.equals(tsMap.get(mainvo.getPrimaryKey()),ts.toString())){
				isPass = false;
				break;
			}
			Integer approvestatus = (Integer) mainvo.getAttributeValue("approvestatus");
			if(approvestatus == null || approvestatus != BillStatusEnum.FREE.toIntValue()){
				error += "第"+(i+1)+"张单据处理失败：审批状态不正确，不能删除！\n";
			}
		}
		if(!isPass) {
			throw new BusinessException("您操作的数据已被他人修改或删除，请刷新后重试！");
		}
		if(!"".equals(error)){
			throw new BusinessException(error);
		}
	}
	
	@Override
	public <T> T[] loadTreeData(Class<T> clazz,Map<String,Object> userJson) throws BusinessException{
		String condition = "dr = 0 ";
		return dao.listByCondition(clazz, condition, false);
	}

	@Override
	public String[] queryChildPksByParentId(Class childClazz, String parentId) throws BusinessException{
		String cond = " pk_vorder = '" + parentId + "' ";
		SuperVO[] vos  = (SuperVO[]) dao.listByCondition(childClazz, cond, false);
		if (vos == null || vos.length == 0) {
			return new String[0];
		}
		return Stream.of(vos).map(vo -> vo.getPrimaryKey()).toArray(String[]::new);
	}


	public SuperVO[] queryChildVOByPks(Class childClazz, String[] pks) throws BusinessException{
		return (SuperVO[]) dao.listByPk(childClazz, pks, false);
	}

	/**
	 * 提交前校验:
	 * 检查单据状态
	 * @throws BusinessException
	 * */
	private void validateCommitAggVorderHVO(AggVorderHVO... vos) throws BusinessException {
		if(ArrayUtils.isEmpty(vos)) {
			return ;
		}
		List<AggVorderHVO> list = Arrays.stream(vos)
			.filter(item ->item.getParentVO()!=null)
			.filter(item->{
				Integer status =  (Integer) item.getParentVO().getAttributeValue("approvestatus");
				return status==null||status!=BillStatusEnum.FREE.toIntValue()&&status!=BillStatusEnum.NOPASS.toIntValue();
			}).map(item->item)
			.collect(Collectors.toList());
		if(list == null||list.size() == 0) {
			return;
		}
		String errors = "";
		for(AggVorderHVO vo  : list) {
			errors+="单据号：["+vo.getParentVO().getAttributeValue("billno")+"]提交失败，失败原因：单据状态不正确，请检查。\n";
		}
		throw new BusinessException(errors);
	}
	/**
	 * 收回前校验:
	 * 检查单据状态
	 * @throws BusinessException
	 * */
	private void validateUnCommitAggVorderHVO(AggVorderHVO... vos) throws BusinessException {
		if(ArrayUtils.isEmpty(vos)) {
			return ;
		}
		List<AggVorderHVO> list = Arrays.stream(vos)
			.filter(item ->item.getParentVO()!=null)
			.filter(item->{
				Integer status =  (Integer)item.getParentVO().getAttributeValue("approvestatus");
				return status==null||status==BillStatusEnum.FREE.toIntValue();
			}).map(item->item)
			.collect(Collectors.toList());
		if(list == null||list.size() == 0) {
			return;
		}
		String errors = "";
		for(AggVorderHVO vo  : list) {
			errors+="单据号：["+vo.getParentVO().getAttributeValue("billno")+"]收回失败，失败原因：单据状态不正确，请检查。\n";
		}
		throw new BusinessException(errors);
	}
	@Override
	public Object commitAggVorderHVO(String actionName,Map<String,String> tsMap,Object assign) throws BusinessException{
		AggVorderHVO[] vos = dao.listByPk(AggVorderHVO.class,getAllPks(tsMap),false);
		validateTs(tsMap,vos);
		//提交前校验及业务逻辑
		validateCommitAggVorderHVO(vos);
		Map<String,Object> res = this.execFlows(actionName,"VORD",assign,vos);
		//提交后业务逻辑
		return res;
	}

	@Override
	public Object batchCommitAggVorderHVO(String actionName,Map<String,String> tsMap) throws BusinessException{
		AggVorderHVO[] vos = dao.listByPk(AggVorderHVO.class,getAllPks(tsMap),false);
		validateTs(tsMap,vos);
		//批量提交前校验及业务逻辑
		validateCommitAggVorderHVO(vos);
		Map<String,Object> res = this.execFlows(actionName,"VORD",vos);
		//批量提交后业务逻辑
		return res;
	}

	@Override
	public Object uncommitAggVorderHVO(String actionName,Map<String,String> tsMap) throws BusinessException{
		AggVorderHVO[] vos = dao.listByPk(AggVorderHVO.class,getAllPks(tsMap),false);
		validateTs(tsMap,vos);
		//收回前校验及业务逻辑
		validateUnCommitAggVorderHVO(vos);
		Map<String,Object> res = this.execFlows(actionName,"VORD",vos);
		//收回后业务逻辑
		return res;
	}

	@Override
	public Object batchUncommitAggVorderHVO(String actionName,Map<String,String> tsMap) throws BusinessException{
		AggVorderHVO[] vos = dao.listByPk(AggVorderHVO.class,getAllPks(tsMap),false);
		validateTs(tsMap,vos);
		//批量收回前校验及业务逻辑
		validateUnCommitAggVorderHVO(vos);
		Map<String,Object> res = this.execFlows(actionName,"VORD",vos);
		//批量收回后业务逻辑
		return res;
	}

	@Override
	public AggVorderHVO[] callbackSAVEBASE(AggVorderHVO...vos) throws BusinessException{
		if(ArrayUtils.isEmpty(vos)) {
			return null;
		}
		return this.saveAggVorderHVO(vos);

	}


	@Override
	public AggVorderHVO[] callbackSAVE(AggVorderHVO...vos) throws BusinessException{
		if(ArrayUtils.isEmpty(vos)) {
			return null;
		}
		//同步单据状态和审批状态(只有提交时候需要手动设置审批状态。其余审批完后审批状态都已更新)
		Arrays.stream(vos).forEach(v->{
				v.getParent().setAttributeValue("approvestatus",BillStatusEnum.COMMIT.toIntValue());
		});
		return this.saveAggVorderHVO(vos);

	}


	@Override
	public AggVorderHVO[] callbackUNSAVE(AggVorderHVO...vos) throws BusinessException{
		if(ArrayUtils.isEmpty(vos)) {
			return null;
		}
		return this.saveAggVorderHVO(vos);

	}


	@Override
	public AggVorderHVO[] callbackAPPROVE(AggVorderHVO...vos) throws BusinessException{
		if(!ArrayUtils.isEmpty(vos)) {
			Integer state = null;
			for (AggVorderHVO aggvo : vos){
				state = (Integer) aggvo.getParentVO().getAttributeValue("approvestatus");
				//判断审批状态，如果审批通过，修改单据状态为“部门审批完成”
				//单据状态判断：车管审批通过之后，部门经理审批不改变单据状态
				if("2".equals(aggvo.getParentVO().getAttributeValue("billstate"))){
					if(state == 1){
						aggvo.getParentVO().setAttributeValue("billstate",3);
						String date = aggvo.getChildrenVO()[0].getAttributeValue("departtime").toString().substring(0,10);//用车时间
						String pk_applier = aggvo.getChildrenVO()[0].getAttributeValue("applier").toString();//司机
						String applier = "";
						String sql = " select user_name from sm_user where cuserid='"+pk_applier+"'";//车管编码（丁喆）
						applier = (String) new BaseDAO().executeQuery(sql, new ColumnProcessor());
						if(applier == null){
							ExceptionUtils.wrapBusinessException("查询车管电话号异常");
						}
						String txet=date+applier;
						sendYonyouMessage(txet);

					}else{
						aggvo.getParentVO().setAttributeValue("billstate",9);
					}
				}

				aggvo.getParentVO().setStatus(VOStatus.UPDATED);

			}

		}
		return this.saveAggVorderHVO(vos);

	}
	/**
	 * 发送友空间消息
	 * @return
	 */
	private boolean sendYonyouMessage(String txet){
		boolean messageResult = false;
		// 获取access_token
		String accessToken = YonyouMessageUtil.getAccessToken();
		//查询车管的电话
		String field = "";
		String sql = " select mobile from bd_psndoc where code='200503001'";//车管编码（丁喆）
		try {
			field=(String) new BaseDAO().executeQuery(sql, new ColumnProcessor());
		} catch (DAOException e) {
			ExceptionUtils.wrapBusinessException("查询车管电话号异常");
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

	@Override
	public AggVorderHVO[] callbackUNAPPROVE(AggVorderHVO...vos) throws BusinessException{
		if(ArrayUtils.isEmpty(vos)) {
			return null;
		}
		return this.saveAggVorderHVO(vos);

	}


	@Override
	public BillCodeContext getBillCodeContext(String coderuleid) throws BusinessException{
		return super.getBillCodeContext(coderuleid);
	}

	@Override
	public BillCodeContext getBillCodeContext(String coderuleid, String pkgroup, String pkorg) throws BusinessException{
		return super.getBillCodeContext(coderuleid,pkgroup,pkorg);
	}
}
