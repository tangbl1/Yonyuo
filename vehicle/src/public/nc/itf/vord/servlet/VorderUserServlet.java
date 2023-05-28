package nc.itf.vord.servlet;

import disposition.GetDatasourceName;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.comn.NetStreamContext;
import nc.bs.framework.core.service.IFwLogin;
import nc.bs.trade.business.HYPubBO;
import nc.bs.uap.lock.PKLock;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.md.model.impl.MDEnum;
import nc.ui.pub.beans.constenum.IConstEnum;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.vehicle.vorder.AggVorderHVO;
import nc.vo.vehicle.vorder.VorderBVO;
import nc.vo.vehicle.vorder.VorderHVO;
import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@SuppressWarnings("restriction")
@WebServlet("/voruserservlet")
public class VorderUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	BaseDAO dao = new BaseDAO();
	HYPubBO hypubBo = new HYPubBO();
	private static Map<String,String> state=new HashMap<String,String>();	//״̬ӳ�䣬���ڻ�ȡ״̬�����Ӧ��״̬����
	static {
		//����״̬��ӳ�䣬����ɡ������С�����������ɡ�׼��������������������̡���Ϣ��ֵ�ࡣ
		state.put("1", "�����");
		state.put("2", "������");
		state.put("3", "�����������");
		state.put("4", "׼������");
		state.put("5", "����");
		state.put("6", "����");
		state.put("7", "����");
		state.put("8", "��Ϣ");
		state.put("9", "����");
	}

	/**
	 * �û����뵥ҳ��servlet
	 * ���ڴ����û�ҳ������
	 * @author �ܾ�
	 * @date 2019-11-20
	 * @param 
	 * 	req ǰ̨ҳ������
	 * 	resp ������Ϣ
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		JSONObject result = new JSONObject();	//����ǰ̨���
		String errMsg = "";		//������Ϣ
		String datasource = "";//����Դ
		try {
			datasource = new GetDatasourceName().doreadxml();
		} catch (JDOMException e) {
			throw new RuntimeException("�����ļ���ȡ����Դ����ʧ�ܣ�");
		}
		// �����������
		InvocationInfoProxy.getInstance().setUserDataSource(datasource);	//ָ�����β������ݵ�����Դ
		req.setCharacterEncoding("utf-8");		//��������ı���ΪUTF-8
		String method = req.getParameter("method");	//��ȡǰ̨����method��ʶ���������ִ�����
		String json = req.getParameter("json");	//��ȡǰ̨���ݹ����Ĳ���JSON����
		
		if(StringUtils.isBlank(method))
			errMsg = "methodΪ�գ����������쳣!";
		if(StringUtils.isBlank(json))
			errMsg = "jsonΪ�գ����������쳣!";
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(json); //String->JSONObject
		} catch (JSONException e1) {
			ExceptionUtils.wrappException(e1);
		}	
		
		// ����NC�����¼
		String username = "hwapp";		//�û���
		String password = "asdqwe123";	//����
		IFwLogin loginService = (IFwLogin) NCLocator.getInstance().lookup(IFwLogin.class);
		byte[] token = loginService.login(username, password, null);
		NetStreamContext.setToken(token);
		
		//ͨ���û������ѯ��Ӧcuserid,pk_group
		String sqluserid = "select cuserid,pk_group from sm_user where user_code='" + username + "'";
		Map<String,String> map = null;
		try {
			map = (Map<String,String>) dao.executeQuery(sqluserid, new MapProcessor());
			if(map==null || map.size() <= 0)
				errMsg = "��ѯhwapp�û�ʧ�ܣ�����ϵ����Ա��";
		} catch (Exception e) {
			errMsg = "cuserid,pk_group��ѯʧ�ܣ�";
			ExceptionUtils.wrappBusinessException(errMsg);
		}
		
		String userid = map.get("cuserid");
		String pk_group = map.get("pk_group");
		InvocationInfoProxy.getInstance().setUserCode(username);
		InvocationInfoProxy.getInstance().setGroupId(pk_group);// ��Ա������Ϣ��
		InvocationInfoProxy.getInstance().setUserId(userid);
		try {
			if (method.equals("add")) {
				result = addVorder(jsonObject);//��Ӷ���
			} else if (method.equals("query")) {
				result =queryVorder(jsonObject);//��ѯ����
			} else if (method.equals("delete")) {
				result = deleVorder(jsonObject);//ɾ������
			} else if (method.equals("update")) {
				result = updateVorder(jsonObject);//�޸�ʱ����
			}else if (method.equals("query_vehicle")) {
				result = querVehicle(jsonObject);//��ѯ������Ϣ
			}else if (method.equals("remark")) {
				result = remarkVorder(jsonObject);//���۶���
			}else if (method.equals("iscarpool")) {
				result = isCarPool(jsonObject);//�Ƿ�ƴ��
			}else if (method.equals("carpool")) {
				result = carPool(jsonObject);//ƴ��
			}else if (method.equals("query_remark")) {
				result = queryRemark(jsonObject);//�ж϶����Ƿ��Ѿ�������
			}else if (method.equals("filterdriver")) {
				result = filterDriver(jsonObject);//����˾��
			}
		}  catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		resp.setContentType("text/html");
		resp.setCharacterEncoding("GBK");
		resp.getWriter().write(result.toString());
		resp.flushBuffer();

	}
	/**
	 * �жϸö����Ƿ��Ѿ�����
	 * @param jsonObject
	 * @return
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject queryRemark(JSONObject jsonObject) throws DAOException, JSONException {
		JSONObject result = new JSONObject();
		Integer num = 0;
		String pk_vorder_b = jsonObject.getString("pk_vorder_b"); // ��������
		String sql = "select review,starlevel from cl_vorder_b where pk_vorder_b = '"+pk_vorder_b+"'";//��ѯ���ۺ��Ǽ�
		Map<String,Integer> map = (Map<String, Integer>) dao.executeQuery(sql, new MapProcessor());
		if(map.get("starlevel")!=null){//����Ѿ������ۣ�num����Ϊ1
			num = 1;
		}
		
		result.put("num",num );//����
		result.put("starlevel",map.get("starlevel"));//�Ǽ�
		result.put("review",map.get("review")!=null?map.get("review"):"" );//����
		result.put("result", "true");
		return result;
	}
	
	
	
	/**
	 * �ж��Ƿ�ƴ��
	 * @param jsonObject
	 * @return
	 * @throws JSONException 
	 * @throws DAOException 
	 * @throws ParseException 
	 */
	private JSONObject isCarPool(JSONObject jsonObject) throws JSONException, DAOException, ParseException {
		JSONObject result = new JSONObject();
		String pk_driver = jsonObject.getString("pk_driver"); // ��������
		Date now = new Date(); //ϵͳ��ǰʱ��
		boolean isupdate=false;//�Ƿ�Ϊ����״̬
		String pk_vorder ="";
		if(StringUtils.isNotBlank(jsonObject.getString("pk_vorder"))){//������Ϊ��
			isupdate=true;//Ϊ�༭̬���޸ģ���ѯʱ��Ҫ���˵���ǰһ��
			pk_vorder= jsonObject.getString("pk_vorder"); //��ǰ��������
		}
		//��ѯ�ó����Ķ�����������ϢΪ��һ�����˵ģ�
		String sql="select cl_vorder.*,cl_vorder_b.*,sm_user.user_name username "
				+" from cl_vorder "
				+" inner join cl_vorder_b"
				+" on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0"
				+" left join sm_user on sm_user.cuserid  =cl_vorder_b.applier and sm_user.dr=0"
				+" where pk_driver='"+pk_driver+"' "
				+" and billstate !=9 and billstate !=1"
			//	+" and isfisrtapplier='Y'"
				+ " and cl_vorder.dr=0 "
				+" and trunc(to_date(substr(departtime,0,10),'yyyy-mm-dd')) BETWEEN  trunc(sysdate) and trunc(sysdate)+1"
				+ ((isupdate) ? " and cl_vorder_b.pk_vorder!='"+pk_vorder+"'" : "")
				+ " order by cl_vorder_b.departtime ";
		List<Map<String,String>> list=(List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
		String selectDeparttime = jsonObject.getString("departtime"); //����ʱ��
		String selectReturntime = jsonObject.getString("returntime"); //����ʱ��
		JSONObject jsonVO = new JSONObject();
		String message="";//����ǰ̨����ʾ��Ϣ
		Boolean isvord=true;//�Ƿ��������ó�
		Boolean iscarpool=false;//�Ƿ���ƴ��
		//����ʱ��ͷ���ʱ�䶼��Ϊ��
		if(StringUtils.isNotBlank(selectDeparttime)&&StringUtils.isNotBlank(selectReturntime)){
			//�Ƚ��û�ѡ���ʱ����ó�ʱ��,��30�����ڿ���ƴ��������ʱ����ʾ��ʱ��α�ռ��
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long time = 10*60*1000;//10����
			//�û�ѡ��ĳ���ʱ��
			Date selectDepartdate = sf.parse(selectDeparttime+":00");
			//�û�ѡ��ķ���ʱ��
			Date selectReturndate = sf.parse(selectReturntime+":00");
			if(list.size()>0){
				for(int i = 0; i < list.size(); i++){
					Map<String, String> map = list.get(i);
					//����ʱ��
					Date departtime = sf.parse((String)map.get("departtime"));
					//����ʱ��
					Date returntime = sf.parse((String)map.get("returntime"));
					//��ѯ��ʱ����������кͲ���������ɵĶ���������С��4�Ŀ���ѡ��,�Ƿ��ƴ����ֵΪ���񡯵� Ҳ����ƴ��
					String sql_vehicle="select sum(selectpnum) selectpnum from cl_vorder"
							+ " inner join cl_vorder_b on cl_vorder_b.pk_vorder= cl_vorder.pk_vorder and cl_vorder_b.dr=0"
							+ " where cl_vorder.dr=0 and departtime='"+map.get("departtime")+"' and returntime='"+map.get("returntime")+"'"
							+ " and cl_vorder.billstate in ('2','3','4') and iscarpool='Y' and pk_driver='"+pk_driver+"'"
							+ " group by pk_vehicle, iscarpool,cl_vorder_b.origin, cl_vorder_b.finaldest, cl_vorder_b.departtime";
					Object selectpnum =  dao.executeQuery(sql_vehicle, new ColumnProcessor());
					//Boolean billiscarpool=true;//��ʱ��ε����ܲ���ƴ��
					int selectpnums=0;
					if(selectpnum!=null){		
						selectpnums=Integer.parseInt(selectpnum.toString());//�ó�����}
					}
				    //�û��ĳ���ʱ��>����ʱ��-30 ���� �û��ķ���ʱ��<����ʱ��+30����ƴ����
					if(selectDepartdate.after(new Date(departtime.getTime()- time))&&selectReturndate.before(new Date(returntime.getTime()+ time))
							&&selectpnums<4&&0<selectpnums&&departtime.after(new Date(now.getTime()+ time))){//�����ʱ�䷶Χ֮�ڿ���ƴ��
						isvord=false;
						iscarpool=true;//����ƴ��
						//����ƴ���Ļ�����ƴ����Ϣ
						jsonVO.put("pk_vorder", map.get("pk_vorder"));
						jsonVO.put("pk_vorder_b", map.get("pk_vorder_b"));
						jsonVO.put("read", true);//ֻ��
						int allPNum=4;//����һ��4����λ
						jsonVO.put("remainPNum", allPNum-selectpnums);//ʣ������
						String selectedOrigins[]={map.get("origin").toString()};
						
						jsonVO.put("username", map.get("username"));//����//��Ա
						jsonVO.put("selectedOrigins", selectedOrigins);//������
						String selectedDestAreas[]={map.get("destarea").toString()};
						jsonVO.put("selectedDestAreas", selectedDestAreas);//Ŀ������
						jsonVO.put("finaldest", map.get("finaldest"));//����Ŀ�ĵ�
						jsonVO.put("pk_driver", pk_driver);//˾������
						jsonVO.put("driver", map.get("driver"));//˾��
						jsonVO.put("dPhone", map.get("dphone"));//˾���绰
						jsonVO.put("vehicleno", map.get("vehicleno")==null?"":map.get("vehicleno"));//���ƺ�
						jsonVO.put("pk_vehicle", map.get("pk_vehicle")==null?"":map.get("pk_vehicle"));//��������
						jsonVO.put("turndownreason", map.get("turndownreason")==null?"":map.get("turndownreason"));//����ԭ��
						String billdeparttime = (String) map.get("departtime");
						if (billdeparttime != null && !billdeparttime.equals("")) {
							billdeparttime = billdeparttime.substring(0, 16);
						}		
						jsonVO.put("departtime", billdeparttime);//����ʱ��
						
						String billreturntime = (String) map.get("returntime");
						if (billreturntime != null && !billreturntime.equals("")) {
							billreturntime = billreturntime.substring(0, 16);
						}		
						jsonVO.put("returntime", billreturntime);//����ʱ��
						jsonVO.put("iscarpool", "Y".equals(map.get("iscarpool"))?true:false);//�Ƿ�ƴ��
						jsonVO.put("dest2Div", "display: none;");//Ŀ�ĵ�2����ʾ
						jsonVO.put("dest3Div", "display: none;");//Ŀ�ĵ�3����ʾ
						break;
					}else if(selectDepartdate.after(new Date(returntime.getTime() + time))){//�û�����ʱ��>����ʱ��+30�����������ó�
						isvord=true;//�����ó�
						iscarpool=false;//����ƴ��
					}else if(selectReturndate.before(new Date(departtime.getTime() - time))){//�û�����ʱ��<�ó�ʱ��-30�����������ó�
						isvord=true;//�����ó�
						iscarpool=false;//����ƴ��
					}else{//������ʾ��ʱ��α�ռ��
						iscarpool=false;//����ƴ��
						isvord=false;//���������ó�
						String departday = (String) map.get("departtime").substring(0, 16);
						String returnday = (String) map.get("returntime").substring(0, 16);
						message=message+"��˾����"+ "\n"+departday+"~"+returnday+ "\n"+"��ռ�ã�������ѡ���ó�ʱ��";
						break;
					}
				}
				jsonVO.put("message", message);//��ʾ��Ϣ
				jsonVO.put("isvord", isvord);
				jsonVO.put("iscarpool", iscarpool);
			}else{//��˾��û�ж���
				jsonVO.put("isvord", true);
				jsonVO.put("iscarpool", false);
			}
		}else{
			//��ѯʱ�����������ʾ����ѡ���ʱ��
			//�����˾����δ��ɵ����뵥������������ʾ��Ϣ
			String departday = "";
			if(list.size()>0){
				message="��˾����"+ "\n";
				for(int i = 0; i < list.size(); i++){
					Map<String, String> map = list.get(i);
					//ʱ���ȡ������ʱ��
					if(departday.equals((String)map.get("departtime").substring(0, 16))) continue;
					departday = (String) map.get("departtime").substring(0, 16);
					String returnday = (String) map.get("returntime").substring(0, 16);
					if(i==list.size()-1){
						message=message+departday+"~"+returnday+ "\n";
					}else{
						message=message+departday+"~"+returnday+","+ "\n";
					}
				}
				message=message+"��ռ�ã������ѡ���ó�ʱ��";
			}
			jsonVO.put("message", message);//��ʾ��Ϣ
			jsonVO.put("iscarpool", false);//�Ƿ�ƴ��Ϊ��
			jsonVO.put("isvord", true);//�����ó�
		}
		result.put("values", jsonVO);
		result.put("result", "true");
		return result;	
		
		
		
		
		
	}
	/**
	 * ����˾��
	 * @param json
	 * @return
	 * @throws JSONException 
	 * @throws DAOException 
	 * @throws ParseException 
	 */
	private JSONObject filterDriver(JSONObject json) throws JSONException, DAOException, ParseException {
		JSONObject result = new JSONObject();
		JSONArray jsonArr = new JSONArray(); //���ڴ洢˾������
		JSONArray jsonArr_pool = new JSONArray(); //���ڴ洢ƴ����˾������
		JSONArray jsonArr_vord = new JSONArray(); //���ڴ洢�����ó���˾������
		String cuserid = json.getString("cuserid"); //��ǰ��¼������
		String origin = json.getString("origin"); // ������
		String destarea = json.getString("destarea");// Ŀ������
		String selectDeparttime = json.getString("departtime"); //����ʱ��
		String selectReturntime = json.getString("returntime"); //����ʱ��
		boolean isupdate=false;//�Ƿ�Ϊ����״̬
		String pk_vorder ="";
		if(StringUtils.isNotBlank(json.getString("pk_vorder"))){//������Ϊ��
			isupdate=true;//Ϊ�༭̬���޸ģ���ѯʱ��Ҫ���˵���ǰһ��
			pk_vorder= json.getString("pk_vorder"); //��ǰ��������
		}
		Date now = new Date(); //ϵͳ��ǰʱ
		//��ѯ���е�˾��,�������������Ϣ��
		String sql_alldrivers="select cl_driver.pk_driver,cl_driver.dname,cl_driver.dphone,cl_vehicle.pk_vehicle,cl_vehicle.vehicleno "
				+ " from cl_driver "
				+ " left join cl_vehicle on cl_vehicle.pk_driver=cl_driver.pk_driver and cl_vehicle.dr=0"
				+ " where cl_driver.dr=0 and dstate not in (2,4) order by length(dname),dname";
		List<Map<String,String>> list_alldrivers= (List<Map<String, String>>) dao.executeQuery(sql_alldrivers, new  MapListProcessor());
		for(Map<String,String> driverMap:list_alldrivers){//����˾��������
			Boolean isvord=true;//�Ƿ��������ó�
			Boolean iscarpool=false;//�Ƿ���ƴ��
			Boolean isContinueCuserid=false;//�Ƿ�Ϊ��ǰ�û�
			String pk_driver=driverMap.get("pk_driver");//˾������
			String dname=driverMap.get("dname");//˾������
			String dphone=driverMap.get("dphone");//˾���绰
			String pk_vehicle=driverMap.get("pk_vehicle")==null?"":driverMap.get("pk_vehicle");//��������
			String vehicleno=driverMap.get("vehicleno")==null?"":driverMap.get("vehicleno");//���ƺ�
			//��ѯ���ϸ�˾���Ķ�����������ϢΪ��һ�����˵ģ�״̬��Ϊ���غ������
			String sql="select * "
					+" from cl_vorder "
					+" inner join cl_vorder_b"
					+" on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0"
					+" where pk_driver='"+pk_driver+"' "
					//+" and isfisrtapplier='Y'"
					+ " and cl_vorder.dr=0 "
					+" and billstate !=9 and billstate !=1"
					//��ѯǰ������Ķ���
					+" and trunc(to_date(substr(departtime,0,10),'yyyy-mm-dd')) BETWEEN  trunc(sysdate)-4 and trunc(sysdate)+4"
					+ ((isupdate) ? " and cl_vorder_b.pk_vorder!='"+pk_vorder+"'" : "")
					+ " order by cl_vorder_b.departtime ";
			List<Map<String,String>> list_vords=(List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
			//����ʱ��ͷ���ʱ�䶼��Ϊ��
			//�Ƚ��û�ѡ���ʱ����ó�ʱ��,��30�����ڿ���ƴ��������ʱ����ʾ��ʱ��α�ռ��
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long time = 10*60*1000;//10����
			//�û�ѡ��ĳ���ʱ��
			Date selectDepartdate = sf.parse(selectDeparttime+":00");
			//�û�ѡ��ķ���ʱ��
			Date selectReturndate = sf.parse(selectReturntime+":00");
			if(list_vords.size()>0){
				for(int i = 0; i < list_vords.size(); i++){
					Map<String, String> map = list_vords.get(i);
					//������
					String cuserid_vord =(String) map.get("applier");
					
					//����ʱ��
					Date departtime = sf.parse((String)map.get("departtime"));
					//����ʱ��
					Date returntime = sf.parse((String)map.get("returntime"));
					//��ѯ��ʱ����������кͲ���������ɵĶ���������С��4�Ŀ���ѡ��,�Ƿ��ƴ����ֵΪ���񡯵� Ҳ����ƴ��,���ܺ��Լ�ƴ��
					String sql_vehicle="select sum(selectpnum) selectpnum from cl_vorder"
							+ " inner join cl_vorder_b on cl_vorder_b.pk_vorder= cl_vorder.pk_vorder and cl_vorder_b.dr=0"
							+ " where cl_vorder.dr=0 and departtime='"+map.get("departtime")+"' and returntime='"+map.get("returntime")+"'"
							+ " and cl_vorder.billstate in ('2','3','4') and iscarpool='Y' "
							//+ "and applier!='"+cuserid+"'"
							+ " group by pk_driver, iscarpool,cl_vorder_b.origin, cl_vorder_b.finaldest, cl_vorder_b.departtime";
					
					
					Object selectpnum =  dao.executeQuery(sql_vehicle, new ColumnProcessor());
					//Boolean billiscarpool=true;//��ʱ��ε����ܲ���ƴ��
					int selectpnums=0;
					if(selectpnum!=null){		
						selectpnums=Integer.parseInt(selectpnum.toString());//�ó�����}
					}
				    //�û��ĳ���ʱ��>����ʱ��-30 ���� �û��ķ���ʱ��<����ʱ��+30����ƴ����,�����غ�Ŀ�ĵ���ͬ//����ƴ����
					//����ǰ���Сʱ����ƴ��    �û��ĳ���ʱ��>��ǰϵͳʱ��+30
					if(selectDepartdate.after(new Date(departtime.getTime()- time))&&selectReturndate.before(new Date(returntime.getTime()+ time))
						&&origin.equals(map.get("origin"))&&destarea.equals(map.get("destarea"))&&selectpnums<4&&0<selectpnums&&departtime.after(new Date(now.getTime()+ time))){
						if(cuserid_vord.equals(cuserid)){
							isContinueCuserid=true;//�Ƿ�Ϊ��ǰ��¼��
						}
						isvord=false;
						iscarpool=true;//����ƴ��
						break;
					}else if(selectDepartdate.after(new Date(returntime.getTime() + time))){//�û�����ʱ��>����ʱ��+30�����������ó�
						isvord=true;//�����ó�
						iscarpool=false;//����ƴ��
					}else if(selectReturndate.before(new Date(departtime.getTime() - time))){//�û�����ʱ��<�ó�ʱ��-30�����������ó�
						isvord=true;//�����ó�
						iscarpool=false;//����ƴ��
					}else{//������ʾ��ʱ��α�ռ��
						iscarpool=false;//����ƴ��
						isvord=false;//���������ó�
						break;
					}	
				}
			}else{//���������ó�
				iscarpool=false;//����ƴ��
				isvord=true;//���������ó�
			}
			if(!isvord&&iscarpool&&!isContinueCuserid){//����ƴ��
				//����ƴ���Ļ�������˾���ֶκ�����Ͽ�ƴ�ı�־
				JSONObject jsonVO = new JSONObject();
				jsonVO.put("pk_driver", pk_driver);
				jsonVO.put("driver", dname+"(��ƴ)");
				jsonVO.put("dphone", dphone);
				jsonVO.put("pk_vehicle", pk_vehicle);
				jsonVO.put("vehicleno", vehicleno);
				jsonArr_pool.put(jsonVO);
			}else if(isvord&&!iscarpool){//���������ó�
				JSONObject jsonVO = new JSONObject();
				jsonVO.put("pk_driver", pk_driver);
				jsonVO.put("driver", dname);
				jsonVO.put("dphone", dphone);
				jsonVO.put("pk_vehicle", pk_vehicle);
				jsonVO.put("vehicleno", vehicleno);
				jsonArr_vord.put(jsonVO);
			}
		}
		
		jsonArr.put(jsonArr_pool);
		jsonArr.put(jsonArr_vord);
		result.put("values", jsonArr);
		result.put("jsonArr_pool", jsonArr_pool);
		result.put("jsonArr_vord", jsonArr_vord);
		result.put("result", "true");
		return result;	
	}
	/**
	 * ƴ��
	 * @param json
	 * @return
	 * @throws BusinessException 
	 * @throws JSONException 
	 * @throws Exception 
	 */
	private JSONObject carPool(JSONObject json) throws Exception {

		String datasource = "";//����Դ
		try {
			datasource = new GetDatasourceName().doreadxml();
		} catch (JDOMException e) {
			throw new RuntimeException("�����ļ���ȡ����Դ����ʧ�ܣ�");
		}
		
		JSONObject result = new JSONObject();
		JSONObject userjsonObject = json.getJSONObject("userjson"); // ��¼��
		String cuserid=userjsonObject.getString("cuserid");
		String pk_org=userjsonObject.getString("pk_org");
		String pk_group=userjsonObject.getString("pk_group");
		String pk_vorder = json.getString("pk_vorder"); // ����
		String pk_vorder_b = json.getString("pk_vorder_b"); // �ӱ�����
		String dest1 =json.getString("dest1"); // Ŀ�ĵ�1
		String dest2 =json.getString("dest2"); // Ŀ�ĵ�2
		String dest3 =json.getString("dest3"); // Ŀ�ĵ�3
		String reason =json.getString("reason"); // ����
		int selectedPNum =Integer.parseInt(json.getString("selectedPNum")); // ����
		String phone =json.getString("phone"); // �û��绰
		String pk_driver =json.getString("pk_driver"); // ˾������
		//����ǰУ��˾��  kkk
		String departtime =json.getString("departtime"); // ����ʱ��
		String returntime =json.getString("returntime"); // ����ʱ��
		JSONObject check_driver = new JSONObject();
		check_driver.put("pk_driver", pk_driver);
		check_driver.put("departtime", departtime);
		check_driver.put("returntime", returntime);
		check_driver.put("pk_vorder", "");
		JSONObject check=isCarPool(check_driver);
		JSONObject ret=check.getJSONObject("values");
		if(!ret.getBoolean("isvord")&&!ret.getBoolean("iscarpool")){
			result.put("result", "false");
			result.put("message", ret.get("message").toString());
			return result;
		}
		
		//�ɵ�vo
		VorderHVO hvo = (VorderHVO) hypubBo.queryByPrimaryKey(VorderHVO.class, pk_vorder);
		VorderBVO bvo = (VorderBVO) hypubBo.queryByPrimaryKey(VorderBVO.class, pk_vorder_b);
		//����pk�����ݺţ�����״̬Ϊ��
		hvo.setAttributeValue("pk_vorder", null);
		hvo.setAttributeValue("ts", null);
		hvo.setAttributeValue("billno", null);
		UFDate vbilldate = new UFDate();
		hvo.setAttributeValue("vbilldate",vbilldate);
		hvo.setAttributeValue("approvestatus", -1);
		hvo.setAttributeValue("billstate", "2");//��������ʱ����״̬Ϊ������
		hvo.setAttributeValue("pk_group", pk_group);
		hvo.setAttributeValue("pk_org", pk_org);
		bvo.setAttributeValue("pk_vorder_b", null);
		bvo.setAttributeValue("dest1", dest1);//Ŀ�ĵ�1
		bvo.setAttributeValue("dest2", dest2);//Ŀ�ĵ�2
		bvo.setAttributeValue("dest3", dest3);//Ŀ�ĵ�3
		bvo.setAttributeValue("remark", reason);//����
		bvo.setAttributeValue("selectpnum",selectedPNum);//��������
		bvo.setAttributeValue("phone", phone);//�绰
		bvo.setAttributeValue("ts", null);
		bvo.setAttributeValue("applier", cuserid);//������
		// ����GroupId
		InvocationInfoProxy.getInstance().setUserId(cuserid);
		
		AggVorderHVO aggvo = new AggVorderHVO();
		VorderBVO[] bvos={bvo};
		aggvo.setChildrenVO(bvos);
		aggvo.setParent(hvo);
		AggVorderHVO[] aggvos = { aggvo };
		//������ӣ��ύ
		IPFBusiAction ipfBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class);
		aggvos = (AggVorderHVO[]) ipfBusiAction.processAction("SAVEBASE","VORD" , null,aggvo , null, null);//����
		ipfBusiAction.processAction("SAVE","VORD" , null,aggvos[0], null, null);//�ύ
		pk_vorder=(String) aggvos[0].getParentVO().getAttributeValue("pk_vorder");//�õ������ɵĵ�����������
		pk_vorder_b=(String) aggvos[0].getChildrenVO()[0].getAttributeValue("pk_vorder_b");//�õ������ɵĵ����ӱ�����
		PKLock.getInstance().releaseLock(pk_vorder, cuserid, datasource);//����������������
		PKLock.getInstance().releaseLock(pk_vorder_b, cuserid, datasource);//�������������ӱ�
		result.put("result", "true");
		return result;	
	}


	/**
	 * ���۶���
	 * @param json
	 * @return 
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject remarkVorder(JSONObject json) throws DAOException, JSONException {
		JSONObject result = new JSONObject();
		String pk_vorder = json.getString("pk_vorder"); // ��������
		String pk_vorder_b = json.getString("pk_vorder_b"); // �����ӱ�����
		String starlevel = json.getString("starlevel"); // �Ǽ�
		String review = json.getString("review"); // ����
		//���±�ͷ�����ۺ��Ǽ�
		String sql="update cl_vorder_b set starlevel='"+starlevel
				+"',review='"+review+"'"
				+" where pk_vorder_b='"+pk_vorder_b+"'";
		dao.executeUpdate(sql);
		//wuchy1-2019-10-16 �õ�˾�����Ǽ�
		UFDate date=new UFDate();//��ȡ��ǰ����
		String starsql = "select count(starlevel) as \"count\", sum(starlevel) as \"sum\" from cl_vorder_b "
				+ " inner join cl_vorder on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder.dr=0"
				+ " where cl_vorder_b.dr = 0 and pk_driver ="
				+ " (select pk_driver from cl_vorder where pk_vorder = '"+pk_vorder+"')"
				+ " and substr(cl_vorder_b.departtime,0,7)='"+date.toString().substring(0, 7)+"'";//��ѯһ����Ȼ�µ�
		Map<String,Integer> map = (Map<String, Integer>) dao.executeQuery(starsql, new MapProcessor());
		UFDouble star = null;
		if(map.size()!=0){
			double count = map.get("count");
			double sum = map.get("sum");
			star = new UFDouble(sum/count);//�Ǽ�Ϊ�õ����ǵ��������������۴���
		}
		
		//����˾�����Ǽ�
		String driversql = "update cl_driver set starlevel = "+star+" where dr = 0 "
				+ "and pk_driver = (select pk_driver from cl_vorder where pk_vorder = '"+pk_vorder+"')";
		dao.executeUpdate(driversql);
		result.put("result", "true");
		return result;
	}





	/**
	 * ��ѯ������Ϣ
	 * @param jsonObject
	 * @return 
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	@SuppressWarnings({ "unchecked" })
	private JSONObject querVehicle(JSONObject jsonObject) throws DAOException, JSONException {
		JSONArray jsonArr = new JSONArray(); //���ڴ洢������Ϣ����
		String cuserid = jsonObject.getString("cuserid"); //��ǰ��¼������
		//���˵���Ϣ�������˾��
		String sql="select  a.pk_vehicle,a.pk_driver, a.vehicleno,a.driver,a.dphone"
				+" from cl_vehicle  a where"
				+"  (select dstate from cl_driver where  pk_driver=a.pk_driver and dr=0) not in (2,4)"
				+"  and a.dr=0";
		List<Map<String,Object>> list=(List<Map<String, Object>>) dao.executeQuery(sql, new  MapListProcessor());
		//ѭ����ȡ������Ϣ,���ж��ι��˲��洢��json����
		for (Map<String, Object> map : list) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("pk_driver", map.get("pk_driver"));	//	˾������
			jsonObj.put("pk_vehicle", map.get("pk_vehicle"));//��������
			jsonObj.put("vehicleno", map.get("vehicleno"));//���ƺ�
			jsonObj.put("driver", map.get("driver"));//˾������
			jsonObj.put("dphone", map.get("dphone"));//˾���绰
			jsonArr.put(jsonObj);
		}
		JSONObject result = new JSONObject();
		result.put("values", jsonArr);
		result.put("result", "true");
		return result;	
	}





	/**
	 * ���¶���
	 * @param json
	 * @return
	 * @throws DAOException 
	 * @throws JSONException 
	 * @throws UifException 
	 */

	private JSONObject updateVorder(JSONObject json) throws DAOException, JSONException, UifException {
		JSONObject result = new JSONObject();
		String pk_vorder = json.getString("pk_vorder"); // ����
		String pk_vorder_b = json.getString("pk_vorder_b");// �ӱ�����
		//�ɵ�vo
		VorderHVO	hvo = (VorderHVO) hypubBo.queryByPrimaryKey(VorderHVO.class, pk_vorder);
		VorderBVO	bvo = (VorderBVO) hypubBo.queryByPrimaryKey(VorderBVO.class, pk_vorder_b);
		String origin = json.getString("origin"); // ������
//		IConstEnum[] list = MDEnum.valueOfConstEnum(Enumerate1.class);
//		for (IConstEnum iConstEnum:list){
//			if(iConstEnum.getName().equals(origin)){
//				origin = (String) iConstEnum.getValue();
//				break;
//			}
//		}
		String destarea = json.getString("destarea");// Ŀ������
//		 list = MDEnum.valueOfConstEnum(Enumerate2.class);
//		for (IConstEnum iConstEnum:list){
//			if(iConstEnum.getName().equals(destarea)){
//				destarea = (String) iConstEnum.getValue();
//				break;
//			}
//		}

		String driver =json.getString("driver"); // ˾��
		String pk_driver =json.getString("pk_driver"); // ˾������
		String driverphone =json.getString("driverphone"); // ˾���绰
		String vehicleno =json.getString("vehicleno"); // ���ƺ�
		String pk_vehicle =json.getString("pk_vehicle"); // ���ƺ�
		String dest1 =json.getString("dest1"); // Ŀ�ĵ�1
		String dest2 =json.getString("dest2"); // Ŀ�ĵ�2
		String dest3 =json.getString("dest3"); // Ŀ�ĵ�3
		String finaldest =json.getString("finaldest"); // ����Ŀ�ĵ�
		String reason =json.getString("reason"); // ����
		int selectedPNum =Integer.parseInt(json.getString("selectedPNum")); // ����
		UFBoolean iscarpool =new UFBoolean(json.getString("mySwitch")); // �Ƿ�ƴ��
		String phone =json.getString("phone"); // �û��绰
		String departtime =json.getString("departtime"); // �ó�ʱ��
		String returntime =json.getString("returntime"); // ����ʱ��
		if(departtime!=null&&!departtime.equals("")){
		departtime=departtime.replaceAll("-", "/");
		departtime=departtime+":00";
		}
		if(returntime!=null&&!returntime.equals("")){
			returntime=returntime.replaceAll("-", "/");
			returntime=returntime+":00";
			}
		AggVorderHVO aggvo = new AggVorderHVO();
		hvo.setAttributeValue("iscarpool", iscarpool);
		int allPNum=4;//����һ��4����λ
		hvo.setAttributeValue("remainpnum", allPNum-selectedPNum);//ʣ����λ
		hvo.setAttributeValue("billstate", "2");//��������ʱ����״̬Ϊ������
		hvo.setAttributeValue("driver", driver);
		hvo.setAttributeValue("pk_driver", pk_driver);
		hvo.setAttributeValue("dphone", driverphone);
		hvo.setAttributeValue("vehicleno", vehicleno);
		hvo.setAttributeValue("pk_vehicle", pk_vehicle);
		aggvo.setParent(hvo);
		bvo.setAttributeValue("pk_vorder_b", pk_vorder_b);
		bvo.setAttributeValue("pk_vorder", pk_vorder);
		bvo.setAttributeValue("isfisrtapplier", 'Y');
		bvo.setAttributeValue("phone", phone);
		bvo.setAttributeValue("origin", origin);
		bvo.setAttributeValue("destarea", destarea);
		bvo.setAttributeValue("dest1", dest1);
		bvo.setAttributeValue("dest2", dest2);
		bvo.setAttributeValue("dest3", dest3);
		bvo.setAttributeValue("finaldest", finaldest);
		bvo.setAttributeValue("departtime", departtime);
		bvo.setAttributeValue("returntime", returntime);
	    bvo.setAttributeValue("selectpnum", selectedPNum);
		bvo.setAttributeValue("remark", reason);
		VorderBVO[] bvos={bvo};
		aggvo.setChildrenVO(bvos);
		VOUpdate vo=new VOUpdate();
		
		ISuperVO[] vos={bvo};
		vo.update(vos);//���±���
		ISuperVO[] hvos={hvo};
		vo.update(hvos);//���±�ͷ
		result.put("result", "true");
		return result;
	}








	private JSONObject deleVorder(JSONObject json) throws DAOException, JSONException {
		JSONObject result = new JSONObject();
		String pk_vorder = json.getString("pk_vorder");// ��������
		String pk_vorder_b = json.getString("pk_vorder_b");// �ӱ�����
		//ɾ����ͷ���ݣ�����dr=0
		String sql_h = "update cl_vorder set dr = 1 where pk_vorder ='"
				+ pk_vorder + "'";
		//ɾ���������ݣ�����dr=0
		String sql_b = "update cl_vorder_b set dr = 1 where pk_vorder_b ='"
				+ pk_vorder_b + "'";
		dao.executeUpdate(sql_h);
		dao.executeUpdate(sql_b);
		//ɾ�����������ݣ�����dr=0
		String sql_workflownote = "delete  pub_workflownote where pk_billtype='VORD' and  billid ='"
				+ pk_vorder + "'";
		dao.executeUpdate(sql_workflownote);
		result.put("result", "true");
		return result;
	}








	/**
	 * �û��˲����ó��б�
	 * @param json
	 * @return
	 * @throws Exception 
	 */
	private JSONObject queryVorder(JSONObject json) throws Exception{
		JSONArray jsonArr = new JSONArray(); //���ڴ洢������Ϣ����
		String cuserid = json.getString("cuserid"); //��ǰ��¼������
		int maxcount = json.getInt("maxcount"); // �������
		int mincount = json.getInt("mincount"); // ��С����
		String sql="SELECT * FROM (SELECT ROWNUM AS rowno1, t.* from (select cl_vorder.billstate,cl_vorder.driver,cl_vorder.pk_driver,cl_vorder.dphone,"
				+ "cl_vorder.billno,cl_vorder.pk_vehicle,cl_vorder.vehicleno,cl_vorder.begintime,cl_vorder.endtime,cl_vorder.iscarpool,"
				+ "cl_vorder_b.*,sm_user.user_name,d.vphoto dphoto"
				+" from cl_vorder "
				+" inner join cl_vorder_b"
				+" on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0"
				+" left join cl_vehicle d on d.pk_vehicle=cl_vorder.pk_vehicle and d.dr = 0"//��������
				+" left join sm_user on sm_user.cuserid  =cl_vorder_b.applier and sm_user.dr=0"//������Ա
				+" where applier='"+cuserid+"' and cl_vorder.dr=0 order by cl_vorder.ts desc "
						+ ") t where ROWNUM <= "+maxcount+") table_alias WHERE table_alias.rowno1 >"+mincount;
		List<Map<String,Object>> list=(List<Map<String, Object>>) dao.executeQuery(sql, new  MapListProcessor());
		//���������
		for (Map<String, Object> map : list) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("pk_vorder", map.get("pk_vorder"));
			jsonObj.put("pk_vorder_b", map.get("pk_vorder_b"));
			jsonObj.put("billno", map.get("billno"));
			jsonObj.put("read", true);//ֻ��
			jsonObj.put("applyer", map.get("user_name"));//����������
			String selectedOrigins[]={map.get("origin").toString()};
			jsonObj.put("selectedOrigins", selectedOrigins);//������
			String selectedDestAreas[]={map.get("destarea").toString()};
			jsonObj.put("selectedDestAreas", selectedDestAreas);//Ŀ������
			jsonObj.put("departtime",gettime((String)map.get("departtime")));//�ó�ʱ��
			jsonObj.put("returntime",gettime((String)map.get("returntime")));//����ʱ��
			jsonObj.put("beginTime",map.get("begintime")==null?"":gettime((String)map.get("begintime")));//��ʼʱ��
			jsonObj.put("endTime",map.get("begintime")==null?"":gettime((String)map.get("endtime")));//����ʱ��
			jsonObj.put("status",state.get(map.get("billstate")));//״̬
			jsonObj.put("dest1", map.get("dest1")==null?"":(String)map.get("dest1"));//Ŀ�ĵ�1
			//���Ŀ�ĵ���ֵ����ʾ�����ҿ�ʼ�ӺŲ���ʾ
			if(map.get("dest2")!=null&&""!=map.get("dest2")){
				jsonObj.put("dest2Div", "display: block;");//Ŀ�ĵ�2
			}else{
				jsonObj.put("dest2Div", "display: none;");//Ŀ�ĵ�2����ʾ
			}
			if(map.get("dest3")!=null&&""!=map.get("dest3")){
				jsonObj.put("dest3Div", "display: block;");//Ŀ�ĵ�3
			}else{
				jsonObj.put("dest3Div", "display: none;");//Ŀ�ĵ�3����ʾ
			}
			jsonObj.put("dest2", map.get("dest2")==null?"":map.get("dest2"));//Ŀ�ĵ�2
			jsonObj.put("dest3", map.get("dest3")==null?"":map.get("dest3"));//Ŀ�ĵ�3
			int PNum =Integer.parseInt(map.get("selectpnum").toString()); // ����
			int  selectpnum[]={PNum};
			jsonObj.put("selectedPNum", selectpnum);//����
			jsonObj.put("adddiv1", "display: none;align:right;");//�ӺŲ���ʾ
			jsonObj.put("adddiv2", "display: none;align:right;");//�ӺŲ���ʾ
			jsonObj.put("finaldest", map.get("finaldest"));//����Ŀ�ĵ�
			jsonObj.put("reason", map.get("remark"));//����
			jsonObj.put("iscarpool", "Y".equals(map.get("iscarpool"))?true:false);//�Ƿ�ƴ��
			jsonObj.put("phone", map.get("phone"));//�绰
			if(map.get("dphoto")!=null){
				// ��Ƭ add by dingft start
				byte[] byte_img = (byte[]) map.get("dphoto");
				String imgDatas = new String(byte_img,"UTF-8");
				jsonObj.put("dphoto", imgDatas);// ͼƬ
				// ��Ƭ add by dingft end
			}else{
				jsonObj.put("dphoto", "");// ͼƬ
				
			}
			//������Ϣ��Ϊjson����
			JSONObject jsonobj_vehicle = new JSONObject();
			jsonobj_vehicle.put("vehicleno", map.get("vehicleno")==null?"":map.get("vehicleno"));
			jsonobj_vehicle.put("pk_vehicle", map.get("pk_vehicle")==null?"":map.get("pk_vehicle"));
			jsonobj_vehicle.put("driver", map.get("driver"));
			jsonobj_vehicle.put("dphone", map.get("dphone"));
			jsonobj_vehicle.put("pk_driver", map.get("pk_driver"));
			jsonObj.put("selectedDrivers", jsonobj_vehicle.toString());//�����ŵĶ���
			
			jsonObj.put("turndownreason", map.get("turndownreason"));//����ԭ��
			jsonArr.put(jsonObj);
			
		}
		JSONObject result = new JSONObject();
		result.put("values", jsonArr);
		result.put("result", "true");
		return result;
		
	}








	private JSONObject addVorder(JSONObject json) throws BusinessException, JSONException {

		String datasource = "";//����Դ
		try {
			datasource = new GetDatasourceName().doreadxml();
		} catch (JDOMException|IOException e ) {
			throw new RuntimeException("�����ļ���ȡ����Դ����ʧ�ܣ�");
		}

		JSONObject result = new JSONObject();
		//�õ�ǰ̨���������ֶ�
		JSONObject userjsonObject = json.getJSONObject("userjson"); // ��¼��
		String	cuserid=userjsonObject.getString("cuserid");//��ǰ��¼������
		String pk_org=userjsonObject.getString("pk_org");//��֯
		String 	pk_group=userjsonObject.getString("pk_group");//����
		String 	pk_dept=userjsonObject.getString("pk_dept");//����
		String origin = json.getString("origin"); // ������
		String destarea = json.getString("destarea");// Ŀ������
		String dest1 =json.getString("dest1"); // Ŀ�ĵ�1
		String dest2 =json.getString("dest2"); // Ŀ�ĵ�2
		String dest3 =json.getString("dest3"); // Ŀ�ĵ�3
		String finaldest =json.getString("finaldest"); // ����Ŀ�ĵ�
		String reason =json.getString("reason"); // ����
		int selectedPNum =Integer.parseInt(json.getString("selectedPNum")); // ����
		UFBoolean iscarpool =new UFBoolean(json.getString("mySwitch")); // �Ƿ�ƴ��
		String phone =json.getString("phone"); // �û��绰
		String driver =json.getString("driver"); // ˾��
		String pk_driver =json.getString("pk_driver"); // ˾������
		String driverphone =json.getString("driverphone"); // ˾���绰
		String vehicleno =json.getString("vehicleno"); // ���ƺ�
		String pk_vehicle =json.getString("pk_vehicle"); // ���ƺ�
		String departtime =json.getString("departtime"); // �ó�ʱ��
		String returntime =json.getString("returntime"); // ����ʱ��
		String turndownreason =json.getString("turndownreason"); // ����ԭ��
		if(departtime!=null&&!departtime.equals("")){
		departtime=departtime.replaceAll("-", "/");
		departtime=departtime+":00";
		}
		if(returntime!=null&&!returntime.equals("")){
			returntime=returntime.replaceAll("-", "/");
			returntime=returntime+":00";
			}
		AggVorderHVO aggvo = new AggVorderHVO();
		VorderHVO hvo = new VorderHVO();
		hvo.setAttributeValue("iscarpool", iscarpool);
		hvo.setAttributeValue("driver", driver);
		hvo.setAttributeValue("pk_driver", pk_driver);
		hvo.setAttributeValue("dphone", driverphone);
		hvo.setAttributeValue("vehicleno", vehicleno);
		hvo.setAttributeValue("pk_vehicle", pk_vehicle);
		int allPNum=4;//����һ��4����λ
		hvo.setAttributeValue("remainpnum", allPNum-selectedPNum);//ʣ����λ
		hvo.setAttributeValue("billstate", "2");//��������ʱ����״̬Ϊ������
		hvo.setAttributeValue("pk_group", pk_group);
		hvo.setAttributeValue("pk_org", pk_org);
		hvo.setAttributeValue("cuserId", cuserid);
		UFDate vbilldate = new UFDate();
		hvo.setAttributeValue("vbilldate",vbilldate);
		hvo.setAttributeValue("billtype","VORD");
		
		aggvo.setParent(hvo);
		VorderBVO bvo=new VorderBVO();
		//��������ʱĬ���ǵ�һ������
		hvo.setAttributeValue("approvestatus", -1);
		bvo.setAttributeValue("isfisrtapplier", 'Y');
		bvo.setAttributeValue("applier", cuserid);
		bvo.setAttributeValue("dept", pk_dept);
		bvo.setAttributeValue("phone", phone);
		bvo.setAttributeValue("origin", origin);
		bvo.setAttributeValue("destarea", destarea);
		
		bvo.setAttributeValue("dest1", dest1);
		bvo.setAttributeValue("dest2", dest2);
		bvo.setAttributeValue("dest3", dest3);
		bvo.setAttributeValue("finaldest", finaldest);
		bvo.setAttributeValue("departtime", departtime);
		bvo.setAttributeValue("returntime", returntime);
	    bvo.setAttributeValue("selectpnum", selectedPNum);
		bvo.setAttributeValue("remark", reason);
		bvo.setAttributeValue("turndownreason", turndownreason);
		VorderBVO[] bvos={bvo};
		aggvo.setChildrenVO(bvos);

		// ����GroupId
	//	InvocationInfoProxy.getInstance().setGroupId(hvo.getAttributeValue("pk_group").toString());
		InvocationInfoProxy.getInstance().setUserId(hvo.getAttributeValue("cuserId").toString());

		AggVorderHVO[] aggvos = { aggvo };
        //�������ݲ��ύ
		IPFBusiAction ipfBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class);
		aggvos = (AggVorderHVO[]) ipfBusiAction.processAction("SAVEBASE","VORD" , null,aggvo , null, null);//����һ�����뵥
		ipfBusiAction.processAction("SAVE","VORD" , null,aggvos[0], null, null);//�ύ
		String pk_vorder=(String) aggvos[0].getParentVO().getAttributeValue("pk_vorder");//�õ������ɵĵ�����������
		String pk_vorder_b=(String) aggvos[0].getChildrenVO()[0].getAttributeValue("pk_vorder_b");//�õ������ɵĵ����ӱ�����
		PKLock.getInstance().releaseLock(pk_vorder, cuserid, datasource);//��������������Ӧ����
		PKLock.getInstance().releaseLock(pk_vorder_b, cuserid, datasource);//��������������Ӧ�ӱ�
		
		result.put("result", "true");
		return result;
	}

	/**
	 * ��ȡʱ�䣨��ȡ��ʱ�֣�����Ҫ�룩
	 * @param time
	 * @return
	 */
	private String gettime(String time) {
		if (time != null && !time.equals("")) {
			time = time.substring(0, 16);
		}else{
			time ="";
		}
		return time;
	}
}

