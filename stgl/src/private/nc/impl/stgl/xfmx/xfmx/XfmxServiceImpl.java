package nc.impl.stgl.xfmx.xfmx;

import java.util.Map;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.UUID;
import java.lang.String;
import java.util.stream.Stream;
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

import nc.vo.yz.xfmx.Xfmx;
import nc.vo.yz.xfmx.AggXfmx;
import nc.itf.stgl.xfmx.xfmx.IXfmxService;


import nc.bs.framework.common.InvocationInfoProxy;
import nccloud.framework.core.exception.ExceptionUtils;

public class  XfmxServiceImpl extends ServiceSupport implements IXfmxService {


	@Override
	public AggXfmx[] listAggXfmxByPk(String...pks) throws BusinessException{
		return listAggXfmxByPk(false,pks);
	}

	@Override
	public AggXfmx[] listAggXfmxByPk(boolean blazyLoad,String... pks) throws BusinessException{
		return dao.listByPksWithOrder(AggXfmx.class,pks,blazyLoad);
	}

	@Override
	public AggXfmx findAggXfmxByPk(String pk) throws BusinessException{
		return dao.findByPk(AggXfmx.class, pk, false);
	}

	@Override
	public  AggXfmx[] listAggXfmxByCondition(String condition) throws BusinessException{
		return listAggXfmxByCondition(condition,new String[]{"pk_xfmx"});
	}
	@Override
	public  AggXfmx[] listAggXfmxByCondition(String condition,String[] orderPath) throws BusinessException{
		return dao.listByCondition(AggXfmx.class, condition, false,false,orderPath);
	}
	@Override
	public Xfmx[] listXfmxByPk(String... pks) throws BusinessException{
		return dao.listByPk(Xfmx.class, pks, true);
	}

	@Override
	public  Xfmx findXfmxByPk(String pk) throws BusinessException{
		return dao.findByPk(Xfmx.class, pk, true);
	}

	@Override
	public  Xfmx[] listXfmxByCondition(String condition) throws BusinessException{
		return listXfmxByCondition(condition,new String[]{"pk_xfmx"});
	}
	@Override
	public  Xfmx[] listXfmxByCondition(String condition,String[] orderPath) throws BusinessException{
		return dao.listByCondition(Xfmx.class, condition, false,false,orderPath);
	}

	@Override
	public String[] listXfmxPkByCond(String condition) throws BusinessException{
		return listXfmxPkByCond(condition,new String[]{"pk_xfmx"});
	}
	@Override
	public String[] listXfmxPkByCond(String condition,String[] orderPath) throws BusinessException{
		if(StringUtils.isEmpty(condition)) {
			condition = " 1 = 1 ";
		}
		StringBuilder sql = new StringBuilder();
		sql.append(" select pk_xfmx from ").append(Xfmx.getDefaultTableName());
		sql.append(" where ").append(condition);
		if (ArrayUtils.isNotEmpty(orderPath)) {
			sql.append(" order by ").append(StringUtils.join(orderPath, ", "));
		}
		return (String[]) dao.getBaseDAO().executeQuery(sql.toString(), (rs) -> {
			List<String> pks = new ArrayList<>();
			while (rs.next()) {
				pks.add(rs.getString(1));
			}
			return pks.toArray(new String[0]);
		});
	}
	@Override
	public void initDefaultData(Xfmx vo){
		if(vo.getAttributeValue("pk_group") == null){
			vo.setAttributeValue("pk_group",InvocationInfoProxy.getInstance().getGroupId());
		}
		if(vo.getAttributeValue("billmaker") == null){
			vo.setAttributeValue("billmaker",InvocationInfoProxy.getInstance().getUserId());
		}
		if(vo.getAttributeValue("documentdate") == null){
			vo.setAttributeValue("documentdate",new UFDateTime(InvocationInfoProxy.getInstance().getBizDateTime()));
		}
	}
	@Override
	public AggXfmx preAddAggXfmx(AggXfmx vo,Map<String,Object> userJson) throws BusinessException{

		getMainVO(vo).setStatus(VOStatus.NEW);
		initDefaultData((Xfmx)getMainVO(vo));

		//下面这段要判断是否是树表界面插件
		Map<String,String> data = userJson!=null && userJson.get("data") != null?(Map<String,String>)userJson.get("data"):null;
		if(data!=null && data.size()>0){
			String parentKey = data.get("parentKey");
			String parentPk = data.get("parentPk");
			getMainVO(vo).setAttributeValue(parentKey,parentPk);
		}

		//编码规则生成vo的编码
		BillCodeContext billCodeContext = getBillCodeContext("hanwangxfmx");
		if(billCodeContext == null){
			throw new BusinessException("当前编码规则不存在，请到【编码规则定义-全局】节点检查是否存在"+"hanwangxfmx");
		}
		if(billCodeContext.isPrecode()){
			String pk_group = InvocationInfoProxy.getInstance().getGroupId();
			String code = getBillcodeManage().getPreBillCode_RequiresNew("hanwangxfmx", pk_group, pk_group);
			getMainVO(vo).setAttributeValue("code",code);
		}

		return vo;
	}
	@Override
	public AggXfmx preAddAggXfmx(Map<String,Object> userJson) throws BusinessException{
		AggXfmx result = null;

		Xfmx mainvo = new Xfmx();
		//设置默认值
		initDefaultData(mainvo);
		AggXfmx aggvo = new AggXfmx();
		aggvo.setParent(mainvo);
		result = aggvo;
		return preAddAggXfmx(result,userJson);
	}

	@Override
	public AggXfmx preEditAggXfmx(String pk) throws BusinessException{
		return dao.findByPk(AggXfmx.class, pk, false);
	}

	@Override
	public AggXfmx copyAggXfmx(String pk) throws BusinessException{

		AggXfmx vo = dao.findByPk(AggXfmx.class, pk, false);

		getMainVO(vo).setPrimaryKey(null);
		getMainVO(vo).setStatus(VOStatus.NEW);

		getMainVO(vo).setAttributeValue("srcbilltype",null);
		getMainVO(vo).setAttributeValue("srcbillid",null);

		getMainVO(vo).setAttributeValue("code",null);
		getMainVO(vo).setAttributeValue("billno",null);
		getMainVO(vo).setAttributeValue("name",null);
		//编码规则生成vo的编码
		BillCodeContext billCodeContext = getBillCodeContext("hanwangxfmx");
		if(billCodeContext == null){
			throw new BusinessException("当前编码规则不存在，请到【编码规则定义-全局】节点检查是否存在"+"hanwangxfmx");
		}
		if(billCodeContext.isPrecode()){
			String pk_group = InvocationInfoProxy.getInstance().getGroupId();
			String code = getBillcodeManage().getPreBillCode_RequiresNew("hanwangxfmx", pk_group, pk_group);
			getMainVO(vo).setAttributeValue("code",code);
		}
		getMainVO(vo).setAttributeValue("documentdate", new UFDateTime(InvocationInfoProxy.getInstance().getBizDateTime()));

		return vo;
	}
	@Override
	public AggXfmx[] saveAggXfmx(AggXfmx vo) throws BusinessException{
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
	private void setBillCode(AggXfmx... vos) throws BusinessException {
		if(ArrayUtils.isNotEmpty(vos)) {
			for(AggXfmx vo : vos) {
				String pk = getVOPrimaryKey(vo);
				if(StringUtils.isEmpty(pk)){
					BillCodeContext billCodeContext = getBillCodeContext("hanwangxfmx");
					String pk_group = InvocationInfoProxy.getInstance().getGroupId();
					if(billCodeContext!=null && !billCodeContext.isPrecode()){
						if(getMainVO(vo).getAttributeValue("code") == null){
							String code = getBillcodeManage().getBillCode_RequiresNew("hanwangxfmx", pk_group, pk_group, getMainVO(vo));
							getMainVO(vo).setAttributeValue("code",code);
						}
					} else {
						String code = (String) getMainVO(vo).getAttributeValue("code");
						getBillcodeManage().commitPreBillCode("hanwangxfmx", pk_group, pk_group, code);
					}
				}
			}
		}
	}
	/**
	 * 保存前设置审计信息
	 * @param vos
	 */
	private void setAuditInfo(AggXfmx... vos) throws BusinessException {
		if(ArrayUtils.isNotEmpty(vos)) {
			UFDateTime now = new UFDateTime();
			for(AggXfmx vo : vos) {
				String pk = getVOPrimaryKey(vo);
				if(StringUtils.isEmpty(pk)){
					//设置创建人创建时间
					getMainVO(vo).setAttributeValue("creator",InvocationInfoProxy.getInstance().getUserId());
					getMainVO(vo).setAttributeValue("creationtime",now);
					getMainVO(vo).setAttributeValue("documentdate",now);
					getMainVO(vo).setAttributeValue("billmaker", InvocationInfoProxy.getInstance().getUserId()); // 制单人
					getMainVO(vo).setAttributeValue("modifier",null);
					getMainVO(vo).setAttributeValue("modifiedtime",null);
				}else{
					//设置修改人修改时间
					getMainVO(vo).setAttributeValue("modifier",InvocationInfoProxy.getInstance().getUserId());
					getMainVO(vo).setAttributeValue("modifiedtime",now);
					getMainVO(vo).setAttributeValue("modifiedtime",now);
				}
			}
		}
	}
	/**
	 * 保存前处理一些默认值
	 * @param vos
	 */
	private void setDefaultVal(AggXfmx... vos) throws BusinessException {
		setBillCode(vos);
		setAuditInfo(vos);
		//其他默认值处理
	}

	// 给单表（行编辑表）单独适配
	@Override
	public AggXfmx[] saveAggXfmx(AggXfmx[] vos) throws BusinessException {
		if (ArrayUtils.isEmpty(vos)) {
			return new AggXfmx[0];
		}
		setDefaultVal(vos); // 设置默认值
		List<String> pks = Arrays.stream(vos).filter(v -> getMainVO(v).getStatus() == VOStatus.DELETED)
				.map(v -> getMainVO(v).getPrimaryKey()).collect(Collectors.toList()); // 删除单据主键
		if (pks == null || pks.size() == 0) {
			return dao.save(vos, true);
		}
		AggXfmx[] deleteVOs = dao.listByPk(AggXfmx.class, pks.toArray(new String[0]));
		for (int i = 0; i < deleteVOs.length; i++) {
			SuperVO mainVO = getMainVO(deleteVOs[i]);
			// 删除单据时回退单据号
			String code = (String)mainVO.getAttributeValue("code");
			if (StringUtils.isNotEmpty(code)) {
				String pk_group = InvocationInfoProxy.getInstance().getGroupId();
				getBillcodeManage().returnBillCodeOnDelete("hanwangxfmx", pk_group, pk_group, code, deleteVOs[i]);
			}
		}
		return dao.save(vos,true);
	}

	@Override
	public AggXfmx[] deleteAggXfmxs(Map<String,String> tsMap) throws BusinessException{
		AggXfmx[] vos = dao.listByPk(AggXfmx.class,tsMap.keySet().toArray(new String[0]));
		validate(vos,tsMap);
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		Arrays.stream(vos).forEach(vo->{
			String code = (String)getMainVO(vo).getAttributeValue("code");
			try {
				getBillcodeManage().returnBillCodeOnDelete("hanwangxfmx",pk_group,pk_group,code,vo);
			} catch (BusinessException e) {
				ExceptionUtils.wrapException(e.getMessage(),e);
			}
		});
		dao.delete(vos,true);
		return vos;
	}
	
	//校验  包括ts校验  已提交校验
	private void validate(AggXfmx[] vos,Map<String,String> tsMap) throws BusinessException{

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
	public BillCodeContext getBillCodeContext(String coderuleid) throws BusinessException{
		return super.getBillCodeContext(coderuleid);
	}

	@Override
	public BillCodeContext getBillCodeContext(String coderuleid, String pkgroup, String pkorg) throws BusinessException{
		return super.getBillCodeContext(coderuleid,pkgroup,pkorg);
	}
}
