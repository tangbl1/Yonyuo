package nc.impl.uapbd.pricemanage.pricemanage;

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


import nc.vo.uapbd.PriceManage;
import nc.vo.uapbd.pricemanage.Addprice;
import nc.vo.uapbd.pricemanage.Baseprice;
import nc.vo.uapbd.AggPriceManage;
import nc.itf.uapbd.pricemanage.pricemanage.IPriceManageService;


import nc.bs.framework.common.InvocationInfoProxy;
import nccloud.framework.core.exception.ExceptionUtils;

public class  PriceManageServiceImpl extends ServiceSupport implements IPriceManageService {


	@Override
	public AggPriceManage[] listAggPriceManageByPk(String...pks) throws BusinessException{
		return listAggPriceManageByPk(false,pks);
	}

	@Override
	public AggPriceManage[] listAggPriceManageByPk(boolean blazyLoad,String... pks) throws BusinessException{
		return dao.listByPksWithOrder(AggPriceManage.class,pks,blazyLoad);
	}

	@Override
	public AggPriceManage findAggPriceManageByPk(String pk) throws BusinessException{
		return dao.findByPk(AggPriceManage.class, pk, false);
	}

	@Override
	public  AggPriceManage[] listAggPriceManageByCondition(String condition) throws BusinessException{
		return listAggPriceManageByCondition(condition,new String[]{"pk_pricemanage"});
	}
	@Override
	public  AggPriceManage[] listAggPriceManageByCondition(String condition,String[] orderPath) throws BusinessException{
		return dao.listByCondition(AggPriceManage.class, condition, false,false,orderPath);
	}
	@Override
	public PriceManage[] listPriceManageByPk(String... pks) throws BusinessException{
		return dao.listByPk(PriceManage.class, pks, true);
	}

	@Override
	public  PriceManage findPriceManageByPk(String pk) throws BusinessException{
		return dao.findByPk(PriceManage.class, pk, true);
	}

	@Override
	public  PriceManage[] listPriceManageByCondition(String condition) throws BusinessException{
		return listPriceManageByCondition(condition,new String[]{"pk_pricemanage"});
	}
	@Override
	public  PriceManage[] listPriceManageByCondition(String condition,String[] orderPath) throws BusinessException{
		return dao.listByCondition(PriceManage.class, condition, false,false,orderPath);
	}

	@Override
	public String[] listPriceManagePkByCond(String condition) throws BusinessException{
		return listPriceManagePkByCond(condition,new String[]{"pk_pricemanage"});
	}
	@Override
	public String[] listPriceManagePkByCond(String condition,String[] orderPath) throws BusinessException{
		if(StringUtils.isEmpty(condition)) {
			condition = " 1 = 1 ";
		}
		StringBuilder sql = new StringBuilder();
		sql.append(" select pk_pricemanage from ").append(PriceManage.getDefaultTableName());
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
	public void initDefaultData(PriceManage vo){
		if(vo.getAttributeValue("pk_group") == null){
			vo.setAttributeValue("pk_group",InvocationInfoProxy.getInstance().getGroupId());
		}
		if(vo.getAttributeValue("maketime") == null){
			vo.setAttributeValue("maketime",new UFDateTime(InvocationInfoProxy.getInstance().getBizDateTime()));
		}
	}
	@Override
	public AggPriceManage preAddAggPriceManage(AggPriceManage vo,Map<String,Object> userJson) throws BusinessException{

		getMainVO(vo).setStatus(VOStatus.NEW);
		initDefaultData((PriceManage)getMainVO(vo));

		//下面这段要判断是否是树表界面插件
		Map<String,String> data = userJson!=null && userJson.get("data") != null?(Map<String,String>)userJson.get("data"):null;
		if(data!=null && data.size()>0){
			String parentKey = data.get("parentKey");
			String parentPk = data.get("parentPk");
			getMainVO(vo).setAttributeValue(parentKey,parentPk);
		}


		return vo;
	}
	@Override
	public AggPriceManage preAddAggPriceManage(Map<String,Object> userJson) throws BusinessException{
		AggPriceManage result = null;

		PriceManage mainvo = new PriceManage();
		//设置默认值
		initDefaultData(mainvo);
		AggPriceManage aggvo = new AggPriceManage();
		aggvo.setParent(mainvo);
		result = aggvo;
		return preAddAggPriceManage(result,userJson);
	}

	@Override
	public AggPriceManage preEditAggPriceManage(String pk) throws BusinessException{
		return dao.findByPk(AggPriceManage.class, pk, false);
	}

	@Override
	public AggPriceManage copyAggPriceManage(String pk) throws BusinessException{

		AggPriceManage vo = dao.findByPk(AggPriceManage.class, pk, false);

		getMainVO(vo).setPrimaryKey(null);
		getMainVO(vo).setStatus(VOStatus.NEW);

		getMainVO(vo).setAttributeValue("srcbilltype",null);
		getMainVO(vo).setAttributeValue("srcbillid",null);

		getMainVO(vo).setAttributeValue("code",null);
		getMainVO(vo).setAttributeValue("",null);
		getMainVO(vo).setAttributeValue("",null);
		getMainVO(vo).setAttributeValue("maketime", new UFDateTime(InvocationInfoProxy.getInstance().getBizDateTime()));

		Baseprice[] baseprices = (Baseprice[])vo.getChildren(Baseprice.class);
		if(baseprices!=null && baseprices.length>0){
			Arrays.stream(baseprices).forEach(subvo->{
				subvo.setPrimaryKey(null);
				subvo.setStatus(VOStatus.NEW);
				subvo.setAttributeValue("srcbilltype",null);
				subvo.setAttributeValue("srcbillid",null);
			});
		}
		Addprice[] addprices = (Addprice[])vo.getChildren(Addprice.class);
		if(addprices!=null && addprices.length>0){
			Arrays.stream(addprices).forEach(subvo->{
				subvo.setPrimaryKey(null);
				subvo.setStatus(VOStatus.NEW);
				subvo.setAttributeValue("srcbilltype",null);
				subvo.setAttributeValue("srcbillid",null);
			});
		}
		return vo;
	}
	@Override
	public AggPriceManage[] saveAggPriceManage(AggPriceManage vo) throws BusinessException{
		String pk = getVOPrimaryKey(vo);
		setDefaultVal(vo);
		if(StringUtils.isEmpty(pk)){
			return dao.insert(vo); //插入
		}else{
			return dao.update(vo); //更新
		}
	}
	/**
	 * 保存前设置审计信息
	 * @param vos
	 */
	private void setAuditInfo(AggPriceManage... vos) throws BusinessException {
		if(ArrayUtils.isNotEmpty(vos)) {
			UFDateTime now = new UFDateTime();
			for(AggPriceManage vo : vos) {
				String pk = getVOPrimaryKey(vo);
				if(StringUtils.isEmpty(pk)){
					//设置创建人创建时间
					getMainVO(vo).setAttributeValue("creator",InvocationInfoProxy.getInstance().getUserId());
					getMainVO(vo).setAttributeValue("creationtime",now);
					getMainVO(vo).setAttributeValue("maketime",now);
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
	private void setDefaultVal(AggPriceManage... vos) throws BusinessException {
		setAuditInfo(vos);
		//其他默认值处理
	}

	// 给单表（行编辑表）单独适配
	@Override
	public AggPriceManage[] saveAggPriceManage(AggPriceManage[] vos) throws BusinessException {
		if (ArrayUtils.isEmpty(vos)) {
			return new AggPriceManage[0];
		}
		setDefaultVal(vos); // 设置默认值
		return dao.save(vos,true);
	}

	@Override
	public AggPriceManage[] deleteAggPriceManages(Map<String,String> tsMap) throws BusinessException{
		AggPriceManage[] vos = dao.listByPk(AggPriceManage.class,tsMap.keySet().toArray(new String[0]));
		validate(vos,tsMap);
		dao.delete(vos,true);
		return vos;
	}
	
	//校验  包括ts校验  已提交校验
	private void validate(AggPriceManage[] vos,Map<String,String> tsMap) throws BusinessException{

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
	public String[] queryChildPksByParentId(Class childClazz, String parentId) throws BusinessException{
		String cond = " pk_pricemanage = '" + parentId + "' ";
		SuperVO[] vos  = (SuperVO[]) dao.listByCondition(childClazz, cond, false);
		if (vos == null || vos.length == 0) {
			return new String[0];
		}
		return Stream.of(vos).map(vo -> vo.getPrimaryKey()).toArray(String[]::new);
	}


	public SuperVO[] queryChildVOByPks(Class childClazz, String[] pks) throws BusinessException{
		return (SuperVO[]) dao.listByPk(childClazz, pks, false);
	}

}
