 package nc.itf.vehicle.servlet;

 import disposition.GetDatasourceName;
 import nc.bs.dao.BaseDAO;
 import nc.bs.dao.DAOException;
 import nc.bs.framework.common.InvocationInfoProxy;
 import nc.bs.framework.common.NCLocator;
 import nc.bs.framework.comn.NetStreamContext;
 import nc.bs.framework.core.service.IFwLogin;
 import nc.impl.pubapp.pattern.data.vo.VOUpdate;
 import nc.itf.vehicle.IVehicleMaintain;
 import nc.jdbc.framework.processor.BeanListProcessor;
 import nc.jdbc.framework.processor.MapListProcessor;
 import nc.jdbc.framework.processor.MapProcessor;
 import nc.ui.dbcache.DBCacheFacade;
 import nc.vo.pub.ISuperVO;
 import nc.vo.pub.SuperVO;
 import nc.vo.pubapp.pattern.exception.ExceptionUtils;
 import nc.vo.vehicle.AggVehicleMessageVO;
 import nc.vo.vehicle.VehicleMessageVO;
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
 import java.io.UnsupportedEncodingException;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Map;


@SuppressWarnings("unused")
@WebServlet("/vehicle")
public class VehicleServlet extends HttpServlet {
	
	BaseDAO dao = new BaseDAO();
	private static final long serialVersionUID = 1L;
	
	/**
	 * ��������ҳ��servlet
	 * ���ڴ���������ҳ������
	 * @author
	 * @date 2019-11-21
	 * @param 
	 * 	req ǰ̨ҳ������
	 * 	resp ������Ϣ
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
		String method = req.getParameter("param");	//��ȡǰ̨����method��ʶ���������ִ�����
		String json = req.getParameter("json");	//��ȡǰ̨���ݹ����Ĳ���JSON����
		
		if(StringUtils.isBlank(method))
			errMsg = "methodΪ�գ����������쳣!";
		if(StringUtils.isBlank(json))
			errMsg = "jsonΪ�գ����������쳣!";
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(json); //String->JSONObject
		} catch (Exception e1) {
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
			//��ѯ����
			if ("listQuery".equals(method)){
				result = queryList(jsonObject);
			} else if("query_dept".equals(method)){
				result = queryDept(jsonObject);
			} else if("query_driver".equals(method)){
				result = queryDriver(jsonObject);
			} else if("add".equals(method)){
				result = addVehicle(jsonObject);
			} else if("detail".equals(method)){
				result = queryDetail(resp,jsonObject);
			} else if("del".equals(method)){
				result = DelVehicle(jsonObject);
			} else if("edit".equals(method)){
				result = updateVehicle(jsonObject);
			}
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		
		if(StringUtils.isNotBlank(errMsg)){
			
			try {
				result.put("errMsg", errMsg);
				result.put("success", false);
			} catch (JSONException e) {
				ExceptionUtils.wrappBusinessException("ʧ�ܵ�result��ֵʧ��!"+e.getMessage());
			}
		}
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().write(result.toString());
		resp.flushBuffer();
	}
		
		
	
	/**
	 * �޸ĳ�������
	 * ������JSONObject json
	 * @return JSONObject 
	 * @throws JSONException 
	 */
	private JSONObject updateVehicle(JSONObject json) throws JSONException {
		JSONObject result = new JSONObject();
		//���ݺ�
		String billno = json.getString("billno");
		//���ݵ��ݺŻ�ȡvo����
		String sql_list = " select * from cl_vehicle where dr = 0 and billno='"+billno+"'";
		
		List<VehicleMessageVO> voList = null;
		try {
			voList = (ArrayList<VehicleMessageVO>) dao.executeQuery(sql_list, new BeanListProcessor(VehicleMessageVO.class));
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("��������VO��ȡ����!"+e.getMessage());
		}
		//ȡ�������еĵ�һ������ֻ��һ����
		VehicleMessageVO vehiclevo = voList.get(0);
		//˾��
		String driver = json.getString("driver");
		//˾������
		String pk_driver = json.getString("pk_driver");
		//���ƺ�
		String vehicleno = json.getString("vehicleno");
		//˾���绰
		String dphone = json.getString("dphone");
		//��������
		String vtype = json.getString("vtype");
		//����״̬
		String vstate = json.getString("vstate");
		//�ؿ�����
		String passengernum = json.getString("passengernum");
		//��������
		String vcharacter = json.getString("vcharacter");
		//��λ
		String unit = json.getString("unit");
		//����
		String dept = json.getString("dept");
		//ͼƬ
		String image = json.getString("image");
		//˾��
		vehiclevo.setDriver(driver);
		//˾������
		vehiclevo.setPk_driver(pk_driver);
		//���ƺ�
		vehiclevo.setVehicleno(vehicleno);
		//˾���绰
		vehiclevo.setDphone(dphone);
		//��������
		vehiclevo.setVtype(vtype);
		//�ؿ�����
		vehiclevo.setPassengernum(Integer.parseInt(passengernum));
		//��������
		vehiclevo.setVcharacter(vcharacter);
		//��λ
		vehiclevo.setUnit(unit);
		//����
		vehiclevo.setDept(dept);
		//״̬
		vehiclevo.setVstate(vstate);
		//ͼƬ
		vehiclevo.setVphoto(image);
		VOUpdate vo=new VOUpdate();
		ISuperVO[] vos={vehiclevo};
		vo.update(vos);
		result.put("result", "true");
		return result;
	}
	
	/**
	 * ɾ����������
	 * @param json json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject DelVehicle(JSONObject json) throws JSONException {
		JSONObject result = new JSONObject();
		//���ݺ�
		String billno = json.getString("billno");
		//ɾ������������Ϣ���߼�ɾ����
		String del_sql = "update cl_vehicle set dr = '1' where billno = '"+billno+"'";
		try {
			//ִ��sql
			dao.executeUpdate(del_sql);
			result.put("result", "true");
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("����������Ϣɾ��ʧ�ܣ�"+e.getMessage());
		}
		return result;
	}
	
	/**
	 * ��ѯ������������
	 * @param resp json
	 * @return 
	 * @throws Exception 
	 */
	private JSONObject queryDetail(HttpServletResponse resp,JSONObject json) throws Exception {
		JSONObject result = new JSONObject();
		//���ݺ�
		String billno = json.getString("billno");
		//��ѯ����������Ϣ
		String detail_sql = "select cl_vehicle.*,org_hrorg.pk_hrorg,org_hrorg.name orgname,org_dept.pk_dept,org_dept.name deptname from cl_vehicle "
				+ " left join org_hrorg on org_hrorg.pk_hrorg=cl_vehicle.unit and org_hrorg.dr=0"
				+ " left join org_dept on org_dept.pk_dept=cl_vehicle.dept and org_dept.dr=0"
				+ " where cl_vehicle.dr = '0' and billno = '"+billno+"'";
		//ִ��sql����ֵ
		List<SuperVO> list = (List<SuperVO>) DBCacheFacade.runQuery(detail_sql, new BeanListProcessor(VehicleMessageVO.class));
		JSONArray jsonArr = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		//���ݺ�
		jsonObj.put("billno", list.get(0).getAttributeValue("billno"));
		//˾��
		jsonObj.put("driver", list.get(0).getAttributeValue("driver"));
		//���ƺ�
		jsonObj.put("vehicleno", list.get(0).getAttributeValue("vehicleno"));
		//˾���绰
		jsonObj.put("dphone", list.get(0).getAttributeValue("dphone"));
		//��������
		jsonObj.put("vtype", list.get(0).getAttributeValue("vtype"));
		//�ؿ�����
		jsonObj.put("number", list.get(0).getAttributeValue("passengernum"));
		//��������
		jsonObj.put("vcharacter", list.get(0).getAttributeValue("vcharacter"));
		//��λ
		jsonObj.put("unit", list.get(0).getAttributeValue("unit"));
		//����
		jsonObj.put("dept", list.get(0).getAttributeValue("dept"));
		//״̬
		jsonObj.put("vstate", list.get(0).getAttributeValue("vstate"));
		
		//˾����Ϊjson����
		JSONObject jsonobj_driver = new JSONObject();
		jsonobj_driver.put("pk_driver", list.get(0).getAttributeValue("pk_driver"));
		jsonobj_driver.put("dname", list.get(0).getAttributeValue("driver"));
		jsonobj_driver.put("dphone", list.get(0).getAttributeValue("dphone"));
		
		//��֯��Ϊjson����
		JSONObject jsonobj_orgs = new JSONObject();
		jsonobj_orgs.put("pk_hrorg", list.get(0).getAttributeValue("pk_hrorg"));
		jsonobj_orgs.put("name", list.get(0).getAttributeValue("orgname"));
		
		//���Ŵ�Ϊjson����
		JSONObject jsonobj_depts = new JSONObject();
		jsonobj_depts.put("pk_dept", list.get(0).getAttributeValue("pk_dept"));
		jsonobj_depts.put("name", list.get(0).getAttributeValue("deptname"));
		
		
		
		jsonObj.put("selectedDrivers", jsonobj_driver);//ѡ��˾��
		jsonObj.put("drivers", (new JSONArray()).put(jsonobj_driver) );//˾��
		jsonObj.put("orgs",  (new JSONArray()).put(jsonobj_orgs) );//ѡ����֯
		jsonObj.put("selectedorgs", jsonobj_orgs);//��֯
		jsonObj.put("selecteddepts", jsonobj_depts );//����
		jsonObj.put("depts", (new JSONArray()).put(jsonobj_depts) );//ѡ����

		byte[] byte_img = (byte[])list.get(0).getAttributeValue("vphoto");
		String imgDatas = new String(byte_img,"UTF-8");
		jsonObj.put("vphoto", imgDatas);
		result.put("values", jsonObj);
		result.put("result", "true");
		return result;
	}

	/**
	 * ������������
	 * @param json json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject addVehicle(JSONObject json) throws JSONException {
		//���ڷ��ص�result
		JSONObject result = new JSONObject();
		//Я����ǰ��¼�û���Ϣ��userjson
		JSONObject userjsonObject = json.getJSONObject("userjson"); // ��¼��
		String cuserid = userjsonObject.getString("cuserid"); //��ǰ��¼������
		String pk_org = userjsonObject.getString("pk_org"); //��ǰ��¼��������֯
		String pk_group = userjsonObject.getString("pk_group"); //��ǰ��¼����������
		//˾��
		String driver = json.getString("driver");
		//˾������
		String pk_driver = json.getString("pk_driver");
		//���ƺ�
		String vehicleno = json.getString("vehicleno");
		//˾���绰
		String dphone = json.getString("dphone");
		//��������
		String vtype = json.getString("vtype");
		//����״̬
		String vstate = json.getString("vstate");
		//�ؿ�����
		String passengernum = json.getString("passengernum");
		//��������
		String vcharacter = json.getString("vcharacter");
		//��λ
		String unit = json.getString("unit");
		//����
		String dept = json.getString("dept");
		//ͼƬ
		String image = json.getString("image");
		VehicleMessageVO vehiclevo = new VehicleMessageVO();
		//��ֵ--˾��
		vehiclevo.setDriver(driver);
		//˾������
		vehiclevo.setPk_driver(pk_driver);
		//���ƺ�
		vehiclevo.setVehicleno(vehicleno);
		//˾���绰
		vehiclevo.setDphone(dphone);
		//��������
		vehiclevo.setVtype(vtype);
		//�ؿ�����
		vehiclevo.setPassengernum(Integer.parseInt(passengernum));
		//��������
		vehiclevo.setVcharacter(vcharacter);
		//��λ
		vehiclevo.setUnit(unit);
		//����
		vehiclevo.setDept(dept);
		//״̬
		vehiclevo.setVstate(vstate);
		//ͼƬ
		vehiclevo.setVphoto(image);
		//����
		vehiclevo.setAttributeValue("pk_group", pk_group);
		//��֯
		vehiclevo.setAttributeValue("pk_org", pk_org);
		//������
		vehiclevo.setAttributeValue("cuserId", cuserid);
		//��ȡAggVehicleMessageVO����
		AggVehicleMessageVO aggmessagevo = new AggVehicleMessageVO();
		aggmessagevo.setParentVO(vehiclevo);
		AggVehicleMessageVO[] aggVOs = new AggVehicleMessageVO[]{aggmessagevo};
		try {
			InvocationInfoProxy.getInstance().setGroupId(pk_group);
			InvocationInfoProxy.getInstance().setUserId(cuserid);
			//����������Ϣ��׼�ӿ�
			IVehicleMaintain iVehicleMaintain = (IVehicleMaintain) NCLocator
					.getInstance().lookup(IVehicleMaintain.class);
			//���ñ�׼�ӿڵ�����
			AggVehicleMessageVO[] aggVO = iVehicleMaintain.insert(aggVOs, null);
			result.put("result", "true");
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("����������Ϣ����ʧ�ܣ�"+e.getMessage());
		}
		return result;
	}

	/**
	 * ��ѯ˾��
	 * @param jsonObject json
	 * @return JSONObject
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject queryDriver(JSONObject jsonObject) throws DAOException, JSONException {
		JSONObject result = new JSONObject();
		JSONArray driverArray = new JSONArray();
		JSONArray orgArray = new JSONArray();
		//��ѯdr=0��˾��sql
		String sql="select pk_driver,dphone,dname from cl_driver"
				+ " where cl_driver.dr=0 and dstate <> '2' and pk_driver not in "
				+ "(select pk_driver from cl_vehicle where dr = 0)";
		//��ѯdr = 0��������Դ��֯��sql(����״̬����2��Ϊ�����õ�),���ݱ�������
		String sql_org="select name, pk_hrorg  from org_hrorg where dr=0 and enablestate =2 order by code";
		List<Map<String,String>> list = (List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
		List<Map<String,String>> list_org = (List<Map<String, String>>) dao.executeQuery(sql_org, new  MapListProcessor());
		//����˾�������
		for (Map<String,String> map : list) {
			JSONObject driverJson = new JSONObject();
			//˾������
			driverJson.put("pk_driver", map.get("pk_driver"));
			//˾������
			driverJson.put("dname", map.get("dname"));
			//˾���绰
			driverJson.put("dphone", map.get("dphone"));
			driverArray.put(driverJson);
		}
		//����������Դ��֯�����
		for (Map<String,String> map : list_org) {
			JSONObject orgJson = new JSONObject();
			//��֯����
			orgJson.put("name", map.get("name"));
			//������Դ��֯����
			orgJson.put("pk_hrorg", map.get("pk_hrorg"));
			orgArray.put(orgJson); 
		}
		result.put("drivers", driverArray);
		result.put("orgs", orgArray);
		result.put("result", "true");	
		return result;
	}

	/**
	 * ��ѯ����
	 * @param json json
	 * @return JSONObject
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject queryDept(JSONObject json) throws DAOException, JSONException {
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		//����ĵ���λ����
		String pk_hrorg = json.getString("pk_hrorg");
		//��ѯ���ŵ����ƺ�����(����״̬����2��Ϊ�����õ�),���ݱ�������
		String sql_dept="select name,pk_dept from org_dept "
				+"where dr=0 and enablestate =2 and pk_org= '"+pk_hrorg +"' order by code";
		//��Ų��Ž����
		List<Map<String,String>> list_dept=(List<Map<String, String>>) dao.executeQuery(sql_dept, new  MapListProcessor());
		//�������ŵĽ����
		for (Map<String,String> map : list_dept) {
			JSONObject jsonObj = new JSONObject();
			//��������
			jsonObj.put("name", map.get("name"));
			//��������
			jsonObj.put("pk_dept", map.get("pk_dept"));
			jsonArray.put(jsonObj); 
		}
		
		result.put("depts", jsonArray);
		result.put("result", "true");
		return result;
	}

	/**
	 * ���������б��ѯ����
	 * @param json json
	 * @return JSONObject
	 * @throws UnsupportedEncodingException 
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject queryList(JSONObject json) throws UnsupportedEncodingException, DAOException, JSONException {
		int maxcount = json.getInt("maxcount"); // �������
		int mincount = json.getInt("mincount"); // ��С����
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		//��ѯ����dr = 0�ĳ���������Ϣ����ҳ��ѯ��
		String sql_list = "SELECT *from (SELECT ROWNUM AS rowno1, t.*from (select * from cl_vehicle where dr = '0'"
				+ " order by ts desc) t where ROWNUM < ="+maxcount+") c WHERE c.rowno1 > "+mincount;
		//ִ��sql����ֵ
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql_list, new  MapListProcessor());
		//���������
		for (Map<String,String> map : list) {
			//���1�����ݵ�mapJSON
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("pk_driver", map.get("pk_driver"));
			//ȡ��������е�һ�����ݲ�����mapJSON----˾��
			jsonObj.put("driver", map.get("driver")==null?"":map.get("driver"));
			//ȡ��������е�һ�����ݲ�����mapJSON----���ƺ�
			jsonObj.put("vehicleno", map.get("vehicleno"));
			//ȡ��������е�һ�����ݲ�����mapJSON----˾���绰
			jsonObj.put("dphone", map.get("dphone")==null?"":map.get("dphone"));
			//ȡ��������е�һ�����ݲ�����mapJSON----��������
			jsonObj.put("vtype", map.get("vtype"));
			//ȡ��������е�һ�����ݲ�����mapJSON----�ؿ�����
			jsonObj.put("passengernum",map.get("passengernum"));
			//ȡ��������е�һ�����ݲ�����mapJSON----��������
			jsonObj.put("vcharacter", map.get("vcharacter"));
			//ȡ��������е�һ�����ݲ�����mapJSON----��λ
			jsonObj.put("unit", map.get("unit"));
			//ȡ��������е�һ�����ݲ�����mapJSON----����
			jsonObj.put("dept", map.get("dept"));
			//ȡ��������е�һ�����ݲ�����mapJSON----���ݺ�
			jsonObj.put("billno", map.get("billno"));
			//ȡ��������е�һ�����ݲ�����mapJSON----����״̬
			jsonObj.put("vstate", map.get("vstate"));
			Object img = map.get("vphoto");
			byte[] byte_img = (byte[])img;
			String imgDatas = new String(byte_img,"UTF-8");
			jsonObj.put("vphoto", imgDatas);
			jsonArray.put(jsonObj);
		}
		result.put("result", "true");
		result.put("values", jsonArray);
		return result;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
}
