package nc.bs.so.m30.weighbridge.plugin;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.jcom.xml.XMLUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.m30.weighbridge.Wb01VO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
/**
 * 地磅系统1号每天定时将 称重信息 传入 NC，用于生成 销售订单
 * @author admin
 *
 */
public class weighbridgeNO1toNCtableWorkPlugin  implements IBackgroundWorkPlugin {

	public PreAlertObject executeTask(BgWorkingContext arg0)
			throws BusinessException {
		runTask();
		return null;
	}
	
	public void runTask() {		
	   try{
		    //读取配置信息
		    Map<String,String> sqlServerConfigMap = loadSqlServerConfigFromXML();
			if (sqlServerConfigMap.size() <= 0)
			{
				Logger.error(new String("获取配置文件失败！"));
			}
			//读取配置文件中的开始结束日期，如果没有设置或设置长度不正确，则使用默认的导入周期
			String webService = sqlServerConfigMap.get("WebService");
			String fromSQL = sqlServerConfigMap.get("FROMSQL");
			String startDateInConfig = sqlServerConfigMap.get("startDate");
			String endDateInConfig = sqlServerConfigMap.get("endDate");
			Pattern p = Pattern.compile("(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");
			int datePatternCheckFlag = 0;
			int startDatePatternCheckFlag = 0;
			//验证时间格式
			if(!p.matcher(startDateInConfig).matches()){
				datePatternCheckFlag = 1;
				startDatePatternCheckFlag = 1;
				if(startDateInConfig.isEmpty()){
					Logger.error("未设置开始时间！使用默认导入周期！");
				} else {
					Logger.error("开始时间不正确！使用默认导入周期");
				}
			}
			if(!p.matcher(endDateInConfig).matches()){
				datePatternCheckFlag = 1;
				if(endDateInConfig.isEmpty()){
					Logger.error("未设置结束时间！使用默认导入周期！");
				} else {
					startDatePatternCheckFlag = 1;
					Logger.error("结束时间不正确！使用默认导入周期！");
				}
			}			
			
           UFDateTime daytime = new UFDateTime(new java.util.Date());	           
           BaseDAO dao = new BaseDAO();
           String beginBeforeDay = daytime.getDateTimeBefore(2).toString().substring(0, 10) + " 00:00:00";
           String endBeforeDay = daytime.getDateTimeBefore(1).toString().substring(0, 10) + " 23:59:59";	
           if(!startDateInConfig.isEmpty()){
        	   beginBeforeDay = startDateInConfig + " 00:00:00";
           }
           if(!endDateInConfig.isEmpty()){
        	   endBeforeDay = endDateInConfig + " 23:59:59";
           }
           //todo :
//            beginBeforeDay ="2021-02-01 00:00:00";
//            endBeforeDay="2021-02-08 23:59:59";
		   String nc_sql = " select lsh, ch from weighbridge01 "
		   		+ "where  updatetime >= '"+beginBeforeDay+"' and updatetime <= '"+endBeforeDay+"'";		   
		   ArrayList<HashMap> al = (ArrayList<HashMap>)dao.executeQuery(nc_sql, new MapListProcessor());	
		   HashMap history = new HashMap();
		   for ( int i=0; i<al.size(); i++ ) {
			   Map hm = al.get(i);
			   history.put(hm.get("lsh"), hm.get("ch"));
		   }
		   
           //查询语句
//           String query=
//        		   " select 序号, 流水号,车号,过磅类型,发货单位, "
//        		 + " 收货单位,货名,规格,convert(varchar(20),毛重) 毛重, "
//        		 + " convert(varchar(20),皮重) 皮重, convert(varchar(20),净重) 净重, "
//        		 + " convert(varchar(20),扣重) 扣重, convert(varchar(20),实重) 实重, convert(varchar(20),单价) 单价, "
//        		 + " convert(varchar(20),金额) 金额, convert(varchar(20),折方系数) 折方系数, convert(varchar(20),方量) 方量, "
//        		 + " convert(varchar(20),过磅费) 过磅费, 毛重司磅员,皮重司磅员,毛重磅号,皮重磅号,"
//        		 + " 毛重时间 毛重时间,皮重时间 皮重时间,一次过磅时间 一次过磅时间,二次过磅时间 二次过磅时间,更新人,更新时间 更新时间,备注,打印次数,上传否,备用1,备用2 "
//        		 + " ,备用13 是否更新,备用14 发货时间 from 称重信息 "
//        		 + " where 更新时间 >= '"+beginBeforeDay+"' "
//        		 		+ "and 更新时间 <= '"+endBeforeDay+"' "
//        		 						+ " and "
//        		 						+ "  货名 in  ('铁精粉','铁精粉(罕王)') ";
           String query= fromSQL
        		 + " where 更新时间 >= '"+beginBeforeDay+"' "
        		 		+ "and 更新时间 <= '"+endBeforeDay+"' "
        		 						+ " and "
        		 						+ "  货名 in  ('铁精粉','铁精粉(罕王)') ";           
           
           JSONObject jsonobject = new JSONObject();
		   jsonobject.put("doType", "query");
		   jsonobject.put("sqlStr", query);
//		   String endpoint = "http://39.152.48.75:18090/hw_weight/services/WeightHandle?wsdl";
		   Service service = new Service();
		   Call call = (Call) service.createCall();
		   call.setTargetEndpointAddress(new URL(webService));
		   call.setOperationName("opeHandle");
		   String result = (String)call.invoke(new Object[] {jsonobject.toString()});
		   ArrayList<Wb01VO> res_insert = new ArrayList<Wb01VO>();//要插入的数据
		   ArrayList<Wb01VO> res_update = new ArrayList<Wb01VO>();//要更新的数据
		   Wb01VO vo = null;
		   JSONObject jsonStr = JSONObject.fromObject(result);
		   String status = jsonStr.getString("status");
		   JSONObject jb = null;
		   if ( "true".equals(status) ) {
			   JSONArray ja = jsonStr.getJSONArray("result");
			   for ( int i=0; i<ja.size(); i++ ) {
				   jb = ja.getJSONObject(i);
				   int xh=(int) jb.get("序号");
				   String lsh=jb.get("流水号").toString();
				   //更新标志判断是否更新
				   if(jb.get("是否更新")!=null&&"是".equals(jb.get("是否更新").toString())&&history.containsKey(lsh) ){
					 //更新时-先根据序号和流水号找到对应的数据
					   String sql="select * from weighbridge01 where xh='"+xh+"' "
		        		  		+ " and xh='"+lsh+"' " ;
		        		vo= (Wb01VO) dao.executeQuery(sql, new BeanProcessor(Wb01VO.class));
		        		vo.setIsupdate("Y");
				   }else{//不是更新--插入的
					   if ( history.containsKey(lsh) ) 
		        		   continue;
	        		   vo =  new Wb01VO();
	        		   vo.setIsupdate("N");
				   }
				   vo.setAttributeValue("xh",xh);
	        	   vo.setAttributeValue("lsh",lsh);
	        	   vo.setAttributeValue("ch",jb.get("车号"));
	        	   vo.setAttributeValue("wbtype",jb.get("过磅类型"));
	        	   vo.setAttributeValue("fh_org",jb.get("发货单位"));
	        	   vo.setAttributeValue("sh_org",jb.get("收货单位"));
	        	   vo.setAttributeValue("hm",jb.get("货名"));
	        	   vo.setAttributeValue("gg",jb.get("规格"));
	        	   vo.setMz(new UFDouble(jb.get("毛重") == null? "0" : jb.get("毛重").toString()));
	        	   vo.setPz(new UFDouble(jb.get("皮重") == null? "0" : jb.get("皮重").toString()));
	        	   vo.setJz(new UFDouble(jb.get("净重") == null? "0" : jb.get("净重").toString()));
	        	   vo.setKz(new UFDouble(jb.get("扣重") == null? "0" : jb.get("扣重").toString()));
	        	   vo.setSz(new UFDouble(jb.get("实重") == null? "0" : jb.get("实重").toString()));
	        	   vo.setDj(new UFDouble(jb.get("单价") == null? "0" : jb.get("单价").toString()));
	        	   vo.setJe(new UFDouble(jb.get("金额") == null? "0" : jb.get("金额").toString()));
	        	   vo.setZfxs(new UFDouble(jb.get("折方系数") == null? "0" : jb.get("折方系数").toString()));
	        	   vo.setFl(new UFDouble(jb.get("方量") == null? "0" : jb.get("方量").toString()));
	        	   vo.setGbf(new UFDouble(jb.get("过磅费") == null? "0" : jb.get("过磅费").toString()));
	        	   vo.setAttributeValue("mz_psn",jb.get("毛重司磅员"));
	        	   vo.setAttributeValue("pz_psn",jb.get("皮重司磅员"));
	        	   vo.setAttributeValue("mz_no",jb.get("毛重磅号"));
	        	   vo.setAttributeValue("pz_no",jb.get("皮重磅号"));
	        	   if ( jb.get("毛重时间") != null )
	        		   vo.setAttributeValue("mz_datetime",new UFDateTime(jb.get("毛重时间").toString().substring(0, 19)));
	        	   if ( jb.get("皮重时间") != null )
	        		   vo.setAttributeValue("pz_datetime",new UFDateTime(jb.get("皮重时间").toString().substring(0, 19)));
	        	   if ( jb.get("一次过磅时间") != null )
	        		   vo.setAttributeValue("first_datetime",new UFDateTime(jb.get("一次过磅时间").toString().substring(0, 19)));
	        	   if ( jb.get("二次过磅时间") != null )
	        		   vo.setAttributeValue("second_datetime",new UFDateTime(jb.get("二次过磅时间").toString().substring(0, 19)));
	        	   vo.setAttributeValue("updater",jb.get("更新人"));
	        	   if ( jb.get("更新时间") != null )
	        		   vo.setAttributeValue("updatetime",new UFDateTime(jb.get("更新时间").toString().substring(0, 19)));
	        	   if ( jb.get("发货时间") != null )
	        		   vo.setAttributeValue("delivery_time",new UFDateTime(jb.get("发货时间").toString().substring(0, 19)));
	        	   vo.setAttributeValue("demo",jb.get("备注"));
	        	   vo.setAttributeValue("prints",jb.get("打印次数"));
	        	   if ( jb.get("上传否") != null )
	        	   {
	        		  vo.setAttributeValue("isupload", "true".equals(jb.get("上传否").toString())?"Y":"N");
	        	   }
	        	   vo.setInfo_status("N");
	        	   vo.setDef01("N");
	        	   if(jb.get("是否更新")!=null&&"是".equals(jb.get("是否更新").toString())&&history.containsKey(lsh) ){
	        		   res_update.add(vo);  
					}else{//不是更新--插入的
						res_insert.add(vo);  
					}
	        	   
			   }
		   } else {
			   
		   }
           if ( res_insert.size() != 0 )
        	   dao.insertVOList(res_insert);
           if ( res_update.size() != 0 )
        	   dao.updateVOList(res_update);
	   } catch(Exception e){
		   e.printStackTrace();
	   } 
	}
	
	private Map<String,String> loadSqlServerConfigFromXML() throws BusinessException	{
		//清空配置Map
		Map<String,String> sqlServerConfigMap = new HashMap<String,String>();
		//读取配置文件
		String nchome = RuntimeEnv.getInstance().getProperty("nc.server.location");
//		String path = (new StringBuilder(String.valueOf(nchome))).append("\\WeighBridgeToNCTaskConfig.xml").toString();
		String path = new StringBuilder(this.getClass().getResource("").getPath()).append("WeighBridgeToNCTaskConfig.xml").toString();
		File file = new File(path);
		String WebService = "";
		String FROMSQL = "";
		String startDate = "";
		String endDate = "";
		try
		{
			Document doc = XMLUtil.getDocumentBuilder().parse(file);
			Element e = doc.getDocumentElement();
			
			int webServiceFlag = 0;
			for (Iterator ite = XMLUtil.getElementsByTagName(e, "WebService"); ite.hasNext();){
				Element sys = (Element)ite.next();
				WebService = sys.getTextContent();
				sqlServerConfigMap.put("WebService", WebService);
				webServiceFlag = 1;
				if (WebService.isEmpty()) {
					Logger.error(new String("配置文件中WebService值错误！"));
				}
			}
			if (webServiceFlag == 0) {
				Logger.error(new String("配置文件中WebService标签错误！"));
			}
			
			
			int fROMSQLFlag = 0;
			for (Iterator ite = XMLUtil.getElementsByTagName(e, "FROMSQL"); ite.hasNext();){
				Element sys = (Element)ite.next();
				FROMSQL = sys.getTextContent();
				sqlServerConfigMap.put("FROMSQL", FROMSQL);
				fROMSQLFlag = 1;
				if (FROMSQL.isEmpty()) {
					Logger.error(new String("配置文件中FROMSQL值错误！"));
				}
			}
			if (fROMSQLFlag == 0) {
				Logger.error(new String("配置文件中FROMSQL标签错误！"));
			}
			
			for (Iterator ite = XMLUtil.getElementsByTagName(e, "StartDate"); ite.hasNext();){
				Element sys = (Element)ite.next();
				startDate = sys.getTextContent();
				sqlServerConfigMap.put("startDate", startDate);
			}
			
			for (Iterator ite = XMLUtil.getElementsByTagName(e, "EndDate"); ite.hasNext();){
				Element sys = (Element)ite.next();
				endDate = sys.getTextContent();
				sqlServerConfigMap.put("endDate", endDate);
			}	
			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new BusinessException("读取配置文件内信息出现异常：" + e.getMessage() + "  请检查配置文件内容！");
		}
		return sqlServerConfigMap;
	}	

}


