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
 * �ذ�ϵͳ1��ÿ�춨ʱ�� ������Ϣ ���� NC���������� ���۶���
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
		    //��ȡ������Ϣ
		    Map<String,String> sqlServerConfigMap = loadSqlServerConfigFromXML();
			if (sqlServerConfigMap.size() <= 0)
			{
				Logger.error(new String("��ȡ�����ļ�ʧ�ܣ�"));
			}
			//��ȡ�����ļ��еĿ�ʼ�������ڣ����û�����û����ó��Ȳ���ȷ����ʹ��Ĭ�ϵĵ�������
			String webService = sqlServerConfigMap.get("WebService");
			String fromSQL = sqlServerConfigMap.get("FROMSQL");
			String startDateInConfig = sqlServerConfigMap.get("startDate");
			String endDateInConfig = sqlServerConfigMap.get("endDate");
			Pattern p = Pattern.compile("(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");
			int datePatternCheckFlag = 0;
			int startDatePatternCheckFlag = 0;
			//��֤ʱ���ʽ
			if(!p.matcher(startDateInConfig).matches()){
				datePatternCheckFlag = 1;
				startDatePatternCheckFlag = 1;
				if(startDateInConfig.isEmpty()){
					Logger.error("δ���ÿ�ʼʱ�䣡ʹ��Ĭ�ϵ������ڣ�");
				} else {
					Logger.error("��ʼʱ�䲻��ȷ��ʹ��Ĭ�ϵ�������");
				}
			}
			if(!p.matcher(endDateInConfig).matches()){
				datePatternCheckFlag = 1;
				if(endDateInConfig.isEmpty()){
					Logger.error("δ���ý���ʱ�䣡ʹ��Ĭ�ϵ������ڣ�");
				} else {
					startDatePatternCheckFlag = 1;
					Logger.error("����ʱ�䲻��ȷ��ʹ��Ĭ�ϵ������ڣ�");
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
		   
           //��ѯ���
//           String query=
//        		   " select ���, ��ˮ��,����,��������,������λ, "
//        		 + " �ջ���λ,����,���,convert(varchar(20),ë��) ë��, "
//        		 + " convert(varchar(20),Ƥ��) Ƥ��, convert(varchar(20),����) ����, "
//        		 + " convert(varchar(20),����) ����, convert(varchar(20),ʵ��) ʵ��, convert(varchar(20),����) ����, "
//        		 + " convert(varchar(20),���) ���, convert(varchar(20),�۷�ϵ��) �۷�ϵ��, convert(varchar(20),����) ����, "
//        		 + " convert(varchar(20),������) ������, ë��˾��Ա,Ƥ��˾��Ա,ë�ذ���,Ƥ�ذ���,"
//        		 + " ë��ʱ�� ë��ʱ��,Ƥ��ʱ�� Ƥ��ʱ��,һ�ι���ʱ�� һ�ι���ʱ��,���ι���ʱ�� ���ι���ʱ��,������,����ʱ�� ����ʱ��,��ע,��ӡ����,�ϴ���,����1,����2 "
//        		 + " ,����13 �Ƿ����,����14 ����ʱ�� from ������Ϣ "
//        		 + " where ����ʱ�� >= '"+beginBeforeDay+"' "
//        		 		+ "and ����ʱ�� <= '"+endBeforeDay+"' "
//        		 						+ " and "
//        		 						+ "  ���� in  ('������','������(����)') ";
           String query= fromSQL
        		 + " where ����ʱ�� >= '"+beginBeforeDay+"' "
        		 		+ "and ����ʱ�� <= '"+endBeforeDay+"' "
        		 						+ " and "
        		 						+ "  ���� in  ('������','������(����)') ";           
           
           JSONObject jsonobject = new JSONObject();
		   jsonobject.put("doType", "query");
		   jsonobject.put("sqlStr", query);
//		   String endpoint = "http://39.152.48.75:18090/hw_weight/services/WeightHandle?wsdl";
		   Service service = new Service();
		   Call call = (Call) service.createCall();
		   call.setTargetEndpointAddress(new URL(webService));
		   call.setOperationName("opeHandle");
		   String result = (String)call.invoke(new Object[] {jsonobject.toString()});
		   ArrayList<Wb01VO> res_insert = new ArrayList<Wb01VO>();//Ҫ���������
		   ArrayList<Wb01VO> res_update = new ArrayList<Wb01VO>();//Ҫ���µ�����
		   Wb01VO vo = null;
		   JSONObject jsonStr = JSONObject.fromObject(result);
		   String status = jsonStr.getString("status");
		   JSONObject jb = null;
		   if ( "true".equals(status) ) {
			   JSONArray ja = jsonStr.getJSONArray("result");
			   for ( int i=0; i<ja.size(); i++ ) {
				   jb = ja.getJSONObject(i);
				   int xh=(int) jb.get("���");
				   String lsh=jb.get("��ˮ��").toString();
				   //���±�־�ж��Ƿ����
				   if(jb.get("�Ƿ����")!=null&&"��".equals(jb.get("�Ƿ����").toString())&&history.containsKey(lsh) ){
					 //����ʱ-�ȸ�����ź���ˮ���ҵ���Ӧ������
					   String sql="select * from weighbridge01 where xh='"+xh+"' "
		        		  		+ " and xh='"+lsh+"' " ;
		        		vo= (Wb01VO) dao.executeQuery(sql, new BeanProcessor(Wb01VO.class));
		        		vo.setIsupdate("Y");
				   }else{//���Ǹ���--�����
					   if ( history.containsKey(lsh) ) 
		        		   continue;
	        		   vo =  new Wb01VO();
	        		   vo.setIsupdate("N");
				   }
				   vo.setAttributeValue("xh",xh);
	        	   vo.setAttributeValue("lsh",lsh);
	        	   vo.setAttributeValue("ch",jb.get("����"));
	        	   vo.setAttributeValue("wbtype",jb.get("��������"));
	        	   vo.setAttributeValue("fh_org",jb.get("������λ"));
	        	   vo.setAttributeValue("sh_org",jb.get("�ջ���λ"));
	        	   vo.setAttributeValue("hm",jb.get("����"));
	        	   vo.setAttributeValue("gg",jb.get("���"));
	        	   vo.setMz(new UFDouble(jb.get("ë��") == null? "0" : jb.get("ë��").toString()));
	        	   vo.setPz(new UFDouble(jb.get("Ƥ��") == null? "0" : jb.get("Ƥ��").toString()));
	        	   vo.setJz(new UFDouble(jb.get("����") == null? "0" : jb.get("����").toString()));
	        	   vo.setKz(new UFDouble(jb.get("����") == null? "0" : jb.get("����").toString()));
	        	   vo.setSz(new UFDouble(jb.get("ʵ��") == null? "0" : jb.get("ʵ��").toString()));
	        	   vo.setDj(new UFDouble(jb.get("����") == null? "0" : jb.get("����").toString()));
	        	   vo.setJe(new UFDouble(jb.get("���") == null? "0" : jb.get("���").toString()));
	        	   vo.setZfxs(new UFDouble(jb.get("�۷�ϵ��") == null? "0" : jb.get("�۷�ϵ��").toString()));
	        	   vo.setFl(new UFDouble(jb.get("����") == null? "0" : jb.get("����").toString()));
	        	   vo.setGbf(new UFDouble(jb.get("������") == null? "0" : jb.get("������").toString()));
	        	   vo.setAttributeValue("mz_psn",jb.get("ë��˾��Ա"));
	        	   vo.setAttributeValue("pz_psn",jb.get("Ƥ��˾��Ա"));
	        	   vo.setAttributeValue("mz_no",jb.get("ë�ذ���"));
	        	   vo.setAttributeValue("pz_no",jb.get("Ƥ�ذ���"));
	        	   if ( jb.get("ë��ʱ��") != null )
	        		   vo.setAttributeValue("mz_datetime",new UFDateTime(jb.get("ë��ʱ��").toString().substring(0, 19)));
	        	   if ( jb.get("Ƥ��ʱ��") != null )
	        		   vo.setAttributeValue("pz_datetime",new UFDateTime(jb.get("Ƥ��ʱ��").toString().substring(0, 19)));
	        	   if ( jb.get("һ�ι���ʱ��") != null )
	        		   vo.setAttributeValue("first_datetime",new UFDateTime(jb.get("һ�ι���ʱ��").toString().substring(0, 19)));
	        	   if ( jb.get("���ι���ʱ��") != null )
	        		   vo.setAttributeValue("second_datetime",new UFDateTime(jb.get("���ι���ʱ��").toString().substring(0, 19)));
	        	   vo.setAttributeValue("updater",jb.get("������"));
	        	   if ( jb.get("����ʱ��") != null )
	        		   vo.setAttributeValue("updatetime",new UFDateTime(jb.get("����ʱ��").toString().substring(0, 19)));
	        	   if ( jb.get("����ʱ��") != null )
	        		   vo.setAttributeValue("delivery_time",new UFDateTime(jb.get("����ʱ��").toString().substring(0, 19)));
	        	   vo.setAttributeValue("demo",jb.get("��ע"));
	        	   vo.setAttributeValue("prints",jb.get("��ӡ����"));
	        	   if ( jb.get("�ϴ���") != null )
	        	   {
	        		  vo.setAttributeValue("isupload", "true".equals(jb.get("�ϴ���").toString())?"Y":"N");
	        	   }
	        	   vo.setInfo_status("N");
	        	   vo.setDef01("N");
	        	   if(jb.get("�Ƿ����")!=null&&"��".equals(jb.get("�Ƿ����").toString())&&history.containsKey(lsh) ){
	        		   res_update.add(vo);  
					}else{//���Ǹ���--�����
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
		//�������Map
		Map<String,String> sqlServerConfigMap = new HashMap<String,String>();
		//��ȡ�����ļ�
		String nchome = RuntimeEnv.getInstance().getProperty("nc.server.location");
		String path = (new StringBuilder(String.valueOf(nchome))).append("\\WeighBridgeToNCTaskConfig.xml").toString();
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
					Logger.error(new String("�����ļ���WebServiceֵ����"));
				}
			}
			if (webServiceFlag == 0) {
				Logger.error(new String("�����ļ���WebService��ǩ����"));
			}
			
			
			int fROMSQLFlag = 0;
			for (Iterator ite = XMLUtil.getElementsByTagName(e, "FROMSQL"); ite.hasNext();){
				Element sys = (Element)ite.next();
				FROMSQL = sys.getTextContent();
				sqlServerConfigMap.put("FROMSQL", FROMSQL);
				fROMSQLFlag = 1;
				if (FROMSQL.isEmpty()) {
					Logger.error(new String("�����ļ���FROMSQLֵ����"));
				}
			}
			if (fROMSQLFlag == 0) {
				Logger.error(new String("�����ļ���FROMSQL��ǩ����"));
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
			throw new BusinessException("��ȡ�����ļ�����Ϣ�����쳣��" + e.getMessage() + "  ���������ļ����ݣ�");
		}
		return sqlServerConfigMap;
	}	

}


