package nccloud.web.so.saleorder.util;

import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.so.m30.entity.SoCalcPriceVO;
import nccloud.framework.service.ServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoCalcPriceUtil {
	private static Map<String,String> grade=new HashMap<String,String>();	//状态映射，用于获取品位对应编码
	static {
		//单据状态做映射，已完成、申请中、部门审批完成、准备出发、出发、到达、返程、休息、值班。
		grade.put("TFe", "1");
		grade.put("SiO2", "2");
		grade.put("S", "3");
		grade.put("P", "4");
		grade.put("TiO2", "5");
		grade.put("H2O", "6");
	}
	IUAPQueryBS queryBS = ServiceLocator.find(IUAPQueryBS.class);
	
	/**
	 * 查询变价
	 */
	public UFDouble calcPrice(SoCalcPriceVO[] calcPriceVOs)
			throws BusinessException {
		
		UFDouble sumPrice = UFDouble.ZERO_DBL;
		//每个指标对应的变价求和
		for (SoCalcPriceVO calcPriceVO : calcPriceVOs) {
			Map bj=queryZBBJ(calcPriceVO.getValue(),calcPriceVO.getZbmc(),calcPriceVO.getCustomer_name());//查询每个指标的变价
			if("3".equals(bj.get("type"))){//固定价格时直接返回
				return new UFDouble(bj.get("bj").toString()) ;
			}
			sumPrice = sumPrice.add(new UFDouble(bj.get("bj").toString()));
		}
		return sumPrice;
	}
	/**
	 * 查询指标变价，对应客户价格管理中的数据
	 * @param content 指标对应含量
	 * @param name  指标名称
	 * @param supplierName 供应商名称
	 * @return
	 * @throws BusinessException
	 */
	public Map<UFDouble,Integer> queryZBBJ(UFDouble content, String name, String supplierName) 
			throws BusinessException {
		Map primap=new HashMap();
		UFDouble price = UFDouble.ZERO_DBL;//浮动价格
		//查询品位对应的区间和价格
		String query = "select pricetype,case"
				+ " when region01_begin<"+content+" and (region01_end>="+content+" or region01_end is null) then region01_price"
				+ " when region02_begin<"+content+" and (region02_end>="+content+" or region02_end is null) then region02_price "
				+ " when region03_begin<"+content+" and (region03_end>="+content+" or region03_end is null) then region03_price"
				+ " else null end dj"
				+ " ,case"
				+ " when region01_begin<"+content+" and (region01_end>="+content+" or region01_end is null) then region01_begin"
				+ " when region02_begin<"+content+" and (region02_end>="+content+" or region02_end is null) then region02_begin"
				+ " when region03_begin<"+content+" and (region03_end>="+content+" or region03_end is null) then region03_begin"
				+ " else null end min"
				+ " from uapbd_addprice"
				+ " left join uapbd_pricemanage on uapbd_pricemanage.pk_pricemanage=uapbd_addprice.pk_pricemanage and uapbd_pricemanage.dr=0 "
				+   " left join bd_customer on bd_customer.pk_customer  = uapbd_pricemanage.pk_customer  and bd_customer.dr=0" 
				+	" where uapbd_addprice.dr = 0  "  
				+ " and bd_customer.name='"+supplierName+"'"
				+ " and target='"+name+"'";
		List rs = (ArrayList<HashMap>) queryBS.executeQuery(query,new MapListProcessor());
		UFDouble dj =UFDouble.ZERO_DBL;
		UFDouble min =UFDouble.ZERO_DBL;
		if (rs != null) {	
			for (int i = 0; i < rs.size(); i++) {
				Map map=(HashMap)rs.get(i);
				if(map.get("dj")!=null){
					dj= new UFDouble(map.get("dj").toString());// 单价
					//判断是扣价还是加价,或者固定价格
					if(map.get("pricetype").toString().equals("3")){//固定价格
						primap.put("bj", dj);
						primap.put("pricetype", 3);
						return primap;
					}
					//区间最小值
					min= new UFDouble(map.get("min").toString());
					if(map.get("pricetype").toString().equals("2")){//2为扣价，将单价写为负数
						//dj=dj.multiply(-1);	
						price=dj.multiply(-1).multiply((content.sub(min)).div(0.1));//价格=区间单价*（-1）*（导入含量-区间最小值）/0.1
					}else{
						price=dj.multiply((content.sub(min)).div(0.1)).add(dj.div(0.1));//价格=区间单价*（导入含量-区间最小值）/0.1 +区间单价/0.1
					}
				}
				
			}
		}
		primap.put("bj", price);
		primap.put("pricetype", 1);
		return primap;
	}
	
	public UFDouble queryBasePrice(SoCalcPriceVO calcPriceVO)
			 {
		UFDouble basePrice = UFDouble.ZERO_DBL;
		IUAPQueryBS queryBS = ServiceLocator.find(IUAPQueryBS.class);
		//在客户价格管理中的基价维护==由客户和日期限制（发货日期）
		String now =calcPriceVO.getDate();//发货日期
		String queryBasePrice = 
				" select uapbd_Baseprice.baseprice from uapbd_Baseprice "  
			+	" left join uapbd_pricemanage on uapbd_pricemanage.pk_pricemanage=uapbd_Baseprice.pk_pricemanage and uapbd_pricemanage.dr=0 " 
			+   " left join bd_customer on bd_customer.pk_customer  = uapbd_pricemanage.pk_customer  and bd_customer.dr=0" 
			+	" where uapbd_Baseprice.dr = 0  "  
			+	" and uapbd_Baseprice.begindate <= '"+now+"' and( uapbd_Baseprice.enddate >= '"+now+"' "
			+ " or uapbd_Baseprice.enddate is null)"		
			+	" and bd_customer.name = '" + calcPriceVO.getCustomer_name() + "'      ";
		
		
		Object obj;
		try {
			obj = queryBS.executeQuery(queryBasePrice,
					new ColumnProcessor());
			if ( obj != null && !"".equals(obj)) {
				basePrice = new UFDouble(obj.toString());
			} else {
				basePrice =  null;
			}
			if (basePrice == null) {
				throw new BusinessException("请在客户价格管理中维护基础价格");
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return basePrice;
	}

	
}
