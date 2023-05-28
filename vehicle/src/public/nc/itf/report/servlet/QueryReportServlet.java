package nc.itf.report.servlet;

import disposition.GetDatasourceName;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.comn.NetStreamContext;
import nc.bs.framework.core.service.IFwLogin;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@SuppressWarnings("restriction")
@WebServlet("/hwreport")
public class QueryReportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	BaseDAO dao = new BaseDAO();
	/**
	 * ����ҳ��servlet
	 * ���ڴ�����ҳ������
	 * @author �ܾ�
	 * @date 2019-11-20
	 * @param 
	 * 	req ǰ̨ҳ������
	 * 	resp ������Ϣ
	 */
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
		//��ѯ����
		try {
			if (method.equals("queryReport")) {
					result = getReport(jsonObject);
			} 
			else if (method.equals("queryDriver")) {
				result = queryDriver(jsonObject);
			} 
			else if (method.equals("queryDriStatus")) {
				result = queryDriStatus1(jsonObject);
			} 
			if(StringUtils.isNotBlank(errMsg)){
				result.put("success", false);
				result.put("errMsg", errMsg);
			}
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		resp.setContentType("text/html");
		resp.setCharacterEncoding("GBK");
		resp.getWriter().write(result.toString());
		resp.flushBuffer();
	}
	/**
	 * ��ѯ˾���ͳ��ƺ�
	 * ������JSONObject jsonObject
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject queryDriver(JSONObject json) throws Exception {
		JSONObject result = new JSONObject();
		//������г��ƺ���Ϣ��JSONArray
		JSONArray jsonVehicleArray = new JSONArray();
		//��ѯ���ƺźͳ��ƺ�pk��sql������������dr��Ϊ0��
		String sql = "select pk_vehicle,vehicleno from cl_vehicle"
				+ " where cl_vehicle.dr=0";
		//��ų��ƺŶ�list
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql, new MapListProcessor());
		//����
		for (Map<String, String> map : list) {
			//���һ�����ƺ���Ϣ��json
			JSONObject jsonObj = new JSONObject();
			//�����ƺ�pk����jsonObj
			jsonObj.put("pk_vehicle", map.get("pk_vehicle"));
			//�����ƺŷ���jsonObj
			jsonObj.put("vehicleno", map.get("vehicleno"));
			//���������ƺ���Ϣ���������г��ƺ���Ϣ��jsonArray��
			jsonVehicleArray.put(jsonObj);
		}
		//���������ݷ���һ��JSONObject
		result.put("jsonvehiclearray", jsonVehicleArray);
		//�������˾����Ϣ��JSONArray
		JSONArray jsonDriverArray = new JSONArray();
		//��ѯ˾������������pk��sql������������dr��Ϊ0��
		String sql_driver = "select pk_driver,dname from cl_driver"
				+ " where cl_driver.dr=0 ";
		//��ų��ƺŶ�list
		List<Map<String,String>> driverList= (List<Map<String, String>>) dao.executeQuery(sql_driver, new MapListProcessor());
		//����
		for (Map<String, String> map : driverList) {
			//���һ�����ƺ���Ϣ��json
			JSONObject jsonObj = new JSONObject();
			//˾������
			jsonObj.put("pk_driver", map.get("pk_driver"));
			//˾������
			jsonObj.put("dname", map.get("dname"));
			//���������ƺ���Ϣ���������г��ƺ���Ϣ��jsonArray��
			jsonDriverArray.put(jsonObj);
		}
		//���������ݷ���һ��JSONObject
		result.put("jsondriverarray", jsonDriverArray);
		return result;
		
	}
	/**
	 * ��ѯ�ó����뱨��
	 * ������JSONObject jsonObject
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject getReport(JSONObject json) throws Exception {
		JSONArray jsonArr = new JSONArray(); //���ڴ洢������Ϣ����
		String dept = json.getString("dept"); // ����
		String applier = json.getString("applier"); // ������
		String pk_driver = json.getString("pk_driver"); // ˾������
		String pk_vehicle = json.getString("pk_vehicle"); // ��������
		String beginTime = json.getString("beginTime"); // ��ʼʱ��
		String endTime = json.getString("endTime"); // ����ʱ��
		String sql_list="";
		//��ѯ����ֻ�г��ƺŻ���˾��ʱ,���ݴӱ�ͷ��ѯ����������ʾ�ǵ�һ�����˵�������ֻ�г��ƺ�ʱ�Գ�Ϊ�������ܱ����м�����
		if((StringUtils.isBlank(applier)&&StringUtils.isBlank(dept))&&(StringUtils.isNotBlank(pk_vehicle)||StringUtils.isNotBlank(pk_driver))){
			sql_list=" select substr(departtime,0,10) departtime,cl_driver.dname,cl_vehicle.vehicleno,sm_user.user_name username,"
					+ " travelmileage,org_dept.name dept from cl_vorder"
					+ " inner join cl_vorder_b on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0 and cl_vorder_b.isfisrtapplier='Y'"
					+ " inner join sm_user on sm_user.cuserid =cl_vorder_b.applier and sm_user.dr=0"
					+ " inner join org_dept on org_dept.pk_dept=cl_vorder_b.dept and org_dept.dr=0"
					+ " left join cl_driver on cl_driver.pk_driver=cl_vorder.pk_driver and cl_driver.dr=0"
					+ " left join cl_vehicle on cl_vehicle.pk_vehicle=cl_vorder.pk_vehicle and cl_vehicle.dr=0"
					+ " where cl_vorder.dr = 0 and billstate=1";
					if(StringUtils.isNotBlank(pk_vehicle)){
						sql_list=sql_list+ " and cl_vorder.pk_vehicle = '"+pk_vehicle+"'";
					}
					if(StringUtils.isNotBlank(pk_driver)){
						sql_list=sql_list+ " and cl_vorder.pk_driver = '"+pk_driver+"'";
					}
		}else{//����ӱ����ѯ�������м�������ʾ������	
			sql_list = " select substr(departtime,0,10) departtime,cl_driver.dname,cl_vehicle.vehicleno,sm_user.user_name username,"
					+ " travelmileage,org_dept.name dept from cl_vorder_b"
					+ " inner join cl_vorder on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder.dr=0"
					+ " inner join sm_user on sm_user.cuserid =cl_vorder_b.applier and sm_user.dr=0"
					+ " inner join org_dept on org_dept.pk_dept=cl_vorder_b.dept and org_dept.dr=0"
					+ " left join cl_driver on cl_driver.pk_driver=cl_vorder.pk_driver and cl_driver.dr=0"
					+ " left join cl_vehicle on cl_vehicle.pk_vehicle=cl_vorder.pk_vehicle and cl_vehicle.dr=0"
					+ " where cl_vorder_b.dr = 0 and billstate=1";
			if(StringUtils.isNotBlank(dept)){
				sql_list=sql_list+ " and org_dept.name like '%"+dept+"%'";
			}
			if(StringUtils.isNotBlank(applier)){
				sql_list=sql_list+ " and sm_user.user_name like '%"+applier+"%'"	;
			}
			if(StringUtils.isNotBlank(pk_vehicle)){
				sql_list=sql_list+ " and cl_vorder.pk_vehicle = '"+pk_vehicle+"'";
			}
			if(StringUtils.isNotBlank(pk_driver)){
				sql_list=sql_list+ " and cl_vorder.pk_driver = '"+pk_driver+"'";
			}
		}
		sql_list=sql_list+"and substr(departtime, 0, 10) >='"+beginTime+"'"
				+"and substr(departtime, 0, 10) <='"+endTime+"'"
				+ "order by cl_vorder.ts desc ";
		List<Map<String,Object>> list= (List<Map<String, Object>>) dao.executeQuery(sql_list, new  MapListProcessor());		

		UFDouble sumMileag=UFDouble.ZERO_DBL;
		//ѭ����ȡ������Ϣ���洢��json����
		for (Map<String, Object> map : list) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("departtime", map.get("departtime"));//����
			jsonObj.put("dname", map.get("dname")!=null?map.get("dname"):"");//˾������
			jsonObj.put("vehicleno", map.get("vehicleno")!=null?map.get("vehicleno"):"");//���ƺ�
			jsonObj.put("username", map.get("username"));//�û�
			UFDouble travelmileage=UFDouble.ZERO_DBL;//������
			if(map.get("travelmileage")!=null){
			travelmileage=new UFDouble(map.get("travelmileage").toString());
			}
			jsonObj.put("travelmileage", travelmileage);//������
			
			jsonObj.put("dept", map.get("dept"));// ����
			sumMileag=sumMileag.add(travelmileage);
			jsonArr.put(jsonObj);
		}
		JSONObject result = new JSONObject();
		result.put("values", jsonArr);
		result.put("vordcount", list.size());
		result.put("summileage", sumMileag);
		result.put("result", "true");
		return result;
	}
	/**
	 * ˾����;����
	 * ������JSONObject jsonObject
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject queryDriStatus(JSONObject json) throws Exception {
		JSONArray jsonArr = new JSONArray(); //���ڴ洢������Ϣ����
		String today = json.getString("today"); // ��ȡ����
		String sql_list="";
		//��ѯ����ֻ�г��ƺŻ���˾��ʱ,���ݴӱ�ͷ��ѯ����������ʾ�ǵ�һ�����˵�������ֻ�г��ƺ�ʱ�Գ�Ϊ�������ܱ����м�����
		sql_list=" select substr(cl_vorder.begintime,11,18) begintime,substr(cl_vorder.endtime,11,18) endtime,cl_driver.dname,"
				+ " cl_vorder.vehicleno,cl_vorder_b.destarea,cl_vorder_b.origin"
				+ " from cl_vorder"
				+ " inner join cl_vorder_b on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0 and cl_vorder_b.isfisrtapplier='Y'"
				+ " left join cl_driver on cl_driver.pk_driver=cl_vorder.pk_driver and cl_driver.dr=0"
				+ " left join cl_vehicle on cl_vehicle.pk_vehicle=cl_vorder.pk_vehicle and cl_vehicle.dr=0"
				+ " where cl_vorder.dr = 0 and cl_vorder.billstate in(5,7) and substr(cl_vorder_b.departtime,0,10)='"+today+"'";
		
		
		List<Map<String,Object>> list= (List<Map<String, Object>>) dao.executeQuery(sql_list, new  MapListProcessor());		

		UFDouble sumMileag=UFDouble.ZERO_DBL;
		//ѭ����ȡ������Ϣ���洢��json����
		for (Map<String, Object> map : list) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("begintime", map.get("begintime"));//����ʱ��
			jsonObj.put("endtime", map.get("endtime"));//����ʱ��
			jsonObj.put("dname", map.get("dname")!=null?map.get("dname"):"");//˾������
			jsonObj.put("vehicleno", map.get("vehicleno")!=null?map.get("vehicleno"):"");//���ƺ�
			jsonObj.put("start_direction", map.get("origin")+"-"+map.get("destarea"));//��������
			String return_direction="";//���ط���
			if(map.get("endtime")!=null){
				return_direction=map.get("destarea")+"-"+map.get("origin");
			}else{
				jsonObj.put("endtime", "");//����ʱ��
			}
			jsonObj.put("return_direction", return_direction);
			jsonArr.put(jsonObj);
		}
		JSONObject result = new JSONObject();
		result.put("values", jsonArr);
		result.put("vordcount", list.size());
		result.put("summileage", sumMileag);
		result.put("result", "true");
		return result;
	}
	/**
	 * ˾����;����
	 * ������JSONObject jsonObject
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject queryDriStatus1(JSONObject json) throws Exception {
		JSONArray jsonArr = new JSONArray(); //���ڴ洢������Ϣ����
		String day = json.getString("billdate"); // ��ȡ����
		//TODO:����ʾ���صĶ���������ɵĶ���
		String sql="select sm_user.user_name,cl_vorder.driver,cl_vorder.approvestatus,cl_vorder.vehicleno,"
				+ " cl_vorder.pk_vorder,cl_vorder.pk_driver,cl_vorder.billstate,substr(departtime,0,10) departdate,sumselectpnum,"
				+ " substr(cl_vorder.begintime,11,18) begintime,substr(cl_vorder.endtime,11,18) endtime,cl_vorder_b.* "
				+" from cl_vorder "
				+" inner join cl_vorder_b"
				+" on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0"
				+" and isfisrtapplier='Y' left join sm_user on sm_user.cuserid = cl_vorder_b.applier" 
				+" left join ("
				+"	SELECT sum(selectpnum) sumselectpnum,cl_vorder_b.pk_vorder,cl_vorder_b.dr from cl_vorder_b "
				+"	group BY cl_vorder_b.pk_vorder,cl_vorder_b.dr"
				+" ) sum on sum.pk_vorder=cl_vorder.pk_vorder and sum.dr=0"
				+" where cl_vorder.dr=0 and substr(departtime,0,10)='"+day+"' "
				+ " and cl_vorder.billstate not in('1','9') order by cl_vorder.ts desc ";
	
	List<Map<String,Object>> list=(List<Map<String, Object>>) dao.executeQuery(sql, new  MapListProcessor());
	//ѭ����ȡ������Ϣ���洢��json����
	for (Map<String, Object> map : list) {
			String pk_vorder = (String) map.get("pk_vorder");
			String sql_applier = "select user_name from sm_user where cuserid in (select applier from cl_vorder_b where  pk_vorder = '"+pk_vorder+"' and dr = 0)";
			List<Map<String,Object>> applierList=(List<Map<String, Object>>) dao.executeQuery(sql_applier, new  MapListProcessor());
			String name = "";
			for (int i = 0; i < applierList.size(); i++) {
				Map<String, Object> applierMap = applierList.get(i);
				if(i==applierList.size()-1){
					name += applierMap.get("user_name");
				}else{
					name += applierMap.get("user_name")+"��";
				}
			}
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("appliername", name);
			Map<String,String> state=new HashMap<String,String>();//״̬ӳ�䣬���ڻ�ȡ״̬�����Ӧ��״̬����
			
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
			
			jsonObj.put("billstate", state.get(map.get("billstate")));//����״̬
			jsonObj.put("vbilldate", map.get("departdate"));//ʱ��(�ó�����)
			jsonObj.put("origin", map.get("origin"));//ʼ����
			jsonObj.put("destarea", map.get("destarea"));//Ŀ�ĵ�
			jsonObj.put("finaldest", map.get("finaldest"));//����Ŀ�ĵ�
			jsonObj.put("driver", map.get("driver"));
			jsonObj.put("vehicleno", map.get("vehicleno")==null?"":map.get("vehicleno"));
			jsonObj.put("begintime", map.get("begintime")==null?"":map.get("begintime"));//����ʱ��
			jsonObj.put("endtime", map.get("endtime")==null?"":map.get("endtime"));//����ʱ��
			String start_direction="";//��������
			if(map.get("begintime")!=null){
				start_direction=map.get("origin")+"-"+map.get("destarea");
			}
			jsonObj.put("start_direction", start_direction);//��������
			String return_direction="";//���ط���
			if(map.get("endtime")!=null){
				return_direction=map.get("destarea")+"-"+map.get("origin");
			}
			jsonObj.put("return_direction", return_direction);//���ط���
			jsonObj.put("user_name", map.get("user_name")!=null?map.get("user_name"):"");
			String useTime = (String) map.get("departtime");
			String departdate = useTime.substring(11,16);
			jsonObj.put("departdate", departdate);//��������
			jsonObj.put("origin", map.get("origin"));//ʼ����
			jsonObj.put("destarea", map.get("destarea"));//ʼ����
			jsonArr.put(jsonObj);
			
			
		}
	
		//��ѯ���е�˾��,�������������Ϣ��
		String sql_driver="select cl_driver.dname driver,cl_vehicle.vehicleno "
				+ " from cl_driver "
				+ " left join cl_vehicle on cl_vehicle.pk_driver=cl_driver.pk_driver and cl_vehicle.dr=0"
				+ " where cl_driver.dr=0 and dstate not in (2,4)"
				+"  and cl_driver.pk_driver not in (select pk_driver from cl_vorder "
				+ " inner join cl_vorder_b on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0  "
				+ " where cl_vorder.billstate not in('1','9') and substr(cl_vorder_b.departtime,0,10) <='"+day+ "' "
				+ " and substr(cl_vorder_b.returntime,0,10)>='"+day
				+ "')";
		List<Map<String,Object>> driverList=(List<Map<String, Object>>) dao.executeQuery(sql_driver, new  MapListProcessor());//����δ��ռ�õ�˾���б�
		JSONArray jsonDriver = new JSONArray(); //δ��ռ�õ�˾��
		//ѭ����ȡ������Ϣ���洢��json����
		for (Map<String, Object> map : driverList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("driver", map.get("driver"));
			jsonObj.put("vehicleno", map.get("vehicleno")==null?"":map.get("vehicleno"));
			jsonObj.put("user_name", "");//�û���
			jsonObj.put("appliername", "");
			jsonObj.put("billstate", "����");//����״̬
			jsonObj.put("origin", "");//ʼ����
			jsonObj.put("destarea","");//Ŀ�ĵ�
			jsonObj.put("begintime", "");//����ʱ��
			jsonObj.put("endtime", "");//����ʱ��
			jsonObj.put("departdate", "");//��������
			jsonDriver.put(jsonObj);
			
		}
		JSONObject result = new JSONObject();
		result.put("values", jsonArr);
		result.put("jsonDriver", jsonDriver);
		result.put("result", "true");
		return result;	
	}
}