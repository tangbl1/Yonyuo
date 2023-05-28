package nc.itf.vord.servlet;

import disposition.GetDatasourceName;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.comn.NetStreamContext;
import nc.bs.framework.core.service.IFwLogin;
import nc.bs.trade.business.HYPubBO;
import nc.itf.stgl.util.YonyouMessageUtil;
import nc.itf.vehicle.IVorderMaintain;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
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
@WebServlet("/hworder")
public class VorderManagerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	BaseDAO dao = new BaseDAO();
	private static Map<String,String> state=new HashMap<String,String>();//״̬ӳ�䣬���ڻ�ȡ״̬�����Ӧ��״̬����
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
	 * �������뵥ҳ��servlet
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
		String username = "hwapp";
		String password = "asdqwe123";
		IFwLogin loginService = (IFwLogin) NCLocator.getInstance().lookup(IFwLogin.class);
		byte[] token = loginService.login(username, password, null);
		NetStreamContext.setToken(token);
		//ͨ���û������ѯ��Ӧcuserid,pk_group
		String sqluserid = "select cuserid,pk_group from sm_user where user_code='"
				+ username + "'";
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
			if ("add".equals(method)) { // ���
				result = addVorder(jsonObject);
			} else if ("query".equals(method)) { // ��ѯ
				result = queryVorder(jsonObject);
			} else if ("querydetail".equals(method)) { // ��ϸ
				result = queryDetail(jsonObject);
			} else if ("updateStaue".equals(method)) { // ����״̬
				result = updateStaue(jsonObject);
			}else if ("update_save".equals(method)) { // �޸Ķ���
				result = updateVorder(jsonObject);//�޸�ʱ����
			}else if ("query_vehicle".equals(method)) {
				result = querVehicle(jsonObject);//��ѯ������Ϣ
			}else if (method.equals("iscarpool")) {
				result = filterDriver(jsonObject);//�Ƿ����ó�
			}else if (method.equals("query_code")) {
				result = queryCode(jsonObject);//��ѯ��֯����
			}
		} catch (DAOException e) {
			ExceptionUtils.wrappException(e);
		} catch (JSONException e) {
			ExceptionUtils.wrappException(e);
		}catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		} catch (ParseException e) {
			ExceptionUtils.wrappException(e);
		} 

		resp.setContentType("text/html");
		resp.setCharacterEncoding("GBK");
		resp.getWriter().write(result.toString());
		resp.flushBuffer();

	}

	/**
	 * �ɳ������
	 * 
	 * @param json
	 * @return
	 * @throws BusinessException 
	 */
	@SuppressWarnings("unused")
	private JSONObject addVorder(JSONObject json) throws JSONException, BusinessException{
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
		//�����û��绰������Ա
		String sql=" select cuserid  from bd_psndoc"
				+" inner join sm_user on bd_psndoc.pk_psndoc =sm_user.pk_psndoc and sm_user.dr=0"
				+"  where bd_psndoc.dr=0 and mobile ='"+phone+"'";
		Object o=dao.executeQuery(sql, new ColumnProcessor());
		if(o!=null){//��Ա���¸�ֵ��ʵ���ó���
			cuserid=o.toString();
		}
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
		hvo.setAttributeValue("billstate", "4");//��������ʱ����״̬Ϊ׼������
		hvo.setAttributeValue("pk_group", pk_group);
		hvo.setAttributeValue("pk_org", pk_org);
		hvo.setAttributeValue("cuserId", cuserid);
		UFDate vbilldate = new UFDate();
		hvo.setAttributeValue("vbilldate",vbilldate);
		
		aggvo.setParent(hvo);
		VorderBVO bvo=new VorderBVO();
		//��������ʱĬ���ǵ�һ������
		hvo.setAttributeValue("approvestatus", 3);
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
		InvocationInfoProxy.getInstance().setGroupId(
				hvo.getAttributeValue("pk_group").toString());
		InvocationInfoProxy.getInstance().setUserId(
				hvo.getAttributeValue("cuserId").toString());

		AggVorderHVO[] aggvos = { aggvo };
		IVorderMaintain iVorderMaintain = (IVorderMaintain) NCLocator
				.getInstance().lookup(IVorderMaintain.class);
		
		AggVorderHVO[] aggdfVO = iVorderMaintain.insert(aggvos, null);
		result.put("result", "true");
		
		return result;
	}
	
	/**
	 * ���뵥�б�-���ܶ�
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private JSONObject queryVorder(JSONObject json) throws DAOException, JSONException{
		JSONArray jsonArr = new JSONArray(); //���ڴ洢������Ϣ����
		String departtime = json.getString("billdate"); // �ó�����
		String orgCode = json.getString("orgcode"); // ��֯����
		String sql="select cl_driver.dname user_name,cl_vorder.driver,cl_vorder.approvestatus,cl_vorder.vehicleno,cl_vorder.pk_vorder,cl_vorder.pk_driver,cl_vorder.billstate,substr(departtime,0,10) departdate,sumselectpnum,cl_vorder_b.* "
					+" from cl_vorder "
					+" inner join cl_vorder_b"
					+" on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0"
					+" and isfisrtapplier='Y' left join cl_driver on cl_driver.pk_driver = cl_vorder.pk_driver and cl_driver.dr=0 "
					+ "left join org_orgs on cl_driver.pk_org = org_orgs.pk_org " 
					+" left join ("
					+"	SELECT sum(selectpnum) sumselectpnum,cl_vorder_b.pk_vorder,cl_vorder_b.dr from cl_vorder_b "
					+"	group BY cl_vorder_b.pk_vorder,cl_vorder_b.dr"
					+" ) sum on sum.pk_vorder=cl_vorder.pk_vorder and sum.dr=0"
					+" where cl_vorder.dr=0 and substr(departtime,0,10)='"+departtime+"' and org_orgs.code='" +orgCode
							+ "' order by cl_vorder.ts desc ";
		
		List<Map<String,Object>> list=(List<Map<String, Object>>) dao.executeQuery(sql, new  MapListProcessor());
		//ѭ����ȡ������Ϣ���洢��json����
		for (Map<String, Object> map : list) {
				String pk_dept = (String) map.get("dept");
				String pk_vorder = (String) map.get("pk_vorder");
				String sql_dept = "select name from bd_psndoc where pk_psndoc "
						+ "= (select principal from org_dept where  "
						+ "pk_dept = '"+pk_dept+"' and dr = 0) and dr = 0";
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
				Map<String,Object> deptmap = (Map<String,Object>) dao.executeQuery(sql_dept, new MapProcessor());
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("name", deptmap==null?"":deptmap.get("name"));//��������
				jsonObj.put("appliername", name);
				jsonObj.put("pk_vorder", map.get("pk_vorder"));
				jsonObj.put("pk_vorder_b", map.get("pk_vorder_b"));
				jsonObj.put("billstate", state.get(map.get("billstate")));//����״̬
				jsonObj.put("vbilldate", map.get("departdate"));//ʱ��(�ó�����)
				jsonObj.put("origin", map.get("origin"));//ʼ����
				jsonObj.put("destarea", map.get("destarea"));//ʼ����
				jsonObj.put("selectedPNum", map.get("selectpnum"));//����
				jsonObj.put("sumselectpnum", map.get("sumselectpnum"));//����������
				jsonObj.put("finaldest", map.get("finaldest"));//����Ŀ�ĵ�
				jsonObj.put("pk_driver", map.get("pk_driver"));
				jsonObj.put("driver", map.get("driver"));
				jsonObj.put("vehicleno", map.get("vehicleno")==null?"":map.get("vehicleno"));
				jsonObj.put("pk_vehicle", map.get("pk_vehicle")==null?"":map.get("pk_vehicle"));
				
				jsonObj.put("user_name", map.get("user_name")!=null?map.get("user_name"):"");
				String useTime = (String) map.get("departtime");
				String departdate = useTime.substring(11,16);
				String departApproval = "";//��������״̬
				String vehicleApproval = "";//��������״̬
				if(Integer.parseInt(map.get("billstate").toString())!=2&&Integer.parseInt(map.get("approvestatus").toString())==1&&Integer.parseInt(map.get("billstate").toString())!=9){
					 departApproval = "������";
				}else if(Integer.parseInt(map.get("billstate").toString())==9){
					departApproval = "����";
				}else{
					departApproval = "δ����";
				}
				if(Integer.parseInt(map.get("billstate").toString())!=2&&Integer.parseInt(map.get("billstate").toString())!=3&&Integer.parseInt(map.get("billstate").toString())!=9){
					vehicleApproval = "������";
				}else if(Integer.parseInt(map.get("billstate").toString())==9){
					vehicleApproval = "����";
				}else{
					vehicleApproval = "δ����";
				}
				jsonObj.put("departApproval", departApproval);//��������
				jsonObj.put("vehicleApproval", vehicleApproval);//��������
				jsonObj.put("departdate", departdate);//��������
				if("1001".equals(orgCode)) {
					jsonObj.put("approver", "����");//��������
				}else if("1002".equals(orgCode)) {
					jsonObj.put("approver", "�ž���");//��������
				}else {
					jsonObj.put("approver", "");//��������
				}
				jsonArr.put(jsonObj);
				
				
			}
			JSONObject result = new JSONObject();
			result.put("values", jsonArr);
			result.put("result", "true");
			return result;	
	}
	

	/**
	 * ���뵥����-���ܶ�
	 * 
	 * @param json
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	private JSONObject queryDetail(JSONObject json) throws DAOException, JSONException, ParseException{
		JSONObject result = new JSONObject();
		String pk_vorder = json.getString("pk_vorder"); // ��ͷ����
		//��ͷ����
		String sql="select cl_vorder.billstate,cl_vorder.iscarpool,cl_vorder.driver,"
				+ "cl_vorder.pk_driver,cl_vorder.pk_vehicle,cl_vorder.vehicleno,cl_vorder.dphone,cl_vorder.startmileage,"
				+ "cl_vorder.backmileage,cl_vorder.travelmileage,cl_vorder_b.*"
				+" from cl_vorder "
				+" inner join cl_vorder_b"
				+" on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0"
				+" where cl_vorder.dr=0  and cl_vorder.pk_vorder='"+pk_vorder+"'";
		//��������
		String sql_detail="select cl_vorder_b.*,sm_user.user_name username"
				+" from cl_vorder_b"
				+" left join sm_user on sm_user.cuserid  =cl_vorder_b.applier and sm_user.dr=0"
				+" where cl_vorder_b.dr=0 and pk_vorder='"+pk_vorder+"'"
				+ "order by cl_vorder_b.ts desc ";
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> list_detail=new ArrayList<>();
		try {
			map = (Map<String,Object>) dao.executeQuery(sql, new  MapProcessor());
			list_detail = (List<Map<String,Object>>) dao.executeQuery(sql_detail, new  MapListProcessor());
		} catch (DAOException e) {
			ExceptionUtils.wrappBusinessException("��ѯ�ó�����ʧ�ܣ�");
		}
		JSONObject jsonVO = new JSONObject();
		jsonVO.put("origin", map.get("origin"));//������
		jsonVO.put("destarea", map.get("destarea"));//Ŀ������
		jsonVO.put("dest1", map.get("dest1")==null?"":(String)map.get("dest1"));//Ŀ�ĵ�1
		//���Ŀ�ĵ���ֵ����ʾ�����ҿ�ʼ�ӺŲ���ʾ
		if(map.get("dest2")!=null&&""!=map.get("dest2")){
			jsonVO.put("dest2Div", "display: block;");//Ŀ�ĵ�2
		}else{
			jsonVO.put("dest2Div", "display: none;");//Ŀ�ĵ�2����ʾ
		}
		if(map.get("dest3")!=null&&""!=map.get("dest3")){
			jsonVO.put("dest3Div", "display: block;");//Ŀ�ĵ�3
		}else{
			jsonVO.put("dest3Div", "display: none;");//Ŀ�ĵ�3����ʾ
		}
		jsonVO.put("dest2", map.get("dest2"));//Ŀ�ĵ�2
		jsonVO.put("dest3", map.get("dest3"));//Ŀ�ĵ�3
		//˾����Ϣ��Ϊjson����
		JSONObject jsonobj_driver = new JSONObject();
		jsonobj_driver.put("vehicleno", map.get("vehicleno")==null?"":map.get("vehicleno"));
		jsonobj_driver.put("pk_vehicle", map.get("pk_vehicle")==null?"":map.get("pk_vehicle"));
		jsonobj_driver.put("driver", map.get("driver"));
		jsonobj_driver.put("dphone", map.get("dphone"));
		jsonobj_driver.put("pk_driver", map.get("pk_driver"));
		jsonVO.put("selectedDrivers", jsonobj_driver);//�����ŵĶ���
		JSONObject jsonobj=querVehicle(json);
		JSONArray drivers = jsonobj.getJSONArray("values");//��ѯ˾������
		JSONArray jsonVehicleArray = jsonobj.getJSONArray("jsonVehicleArray");//��ѯ��������
		//jsonVO.put("jsonVehicleArray",  jsonVehicleArray);//����	
		JSONObject jsonobj_vehicle = new JSONObject();
		jsonobj_vehicle.put("vehicleno", map.get("vehicleno")==null?"":map.get("vehicleno"));
		jsonobj_vehicle.put("pk_vehicle", map.get("pk_vehicle")==null?"":map.get("pk_vehicle"));
		jsonVO.put("selectedVehicle", jsonobj_vehicle);//�����ŵĶ���
		
		jsonVO.put("jsonVehicleArray",  jsonVehicleArray);//����	
		//drivers.put(jsonobj_vehicle);
		String departtime = (String) map.get("departtime");
		if (departtime != null && !departtime.equals("")) {
			departtime = departtime.substring(0, 16);
		}
		jsonVO.put("drivers",  drivers);//˾��	
		jsonVO.put("departtime", departtime);//����ʱ��
		String returntime = (String) map.get("returntime");
		if (returntime != null && !returntime.equals("")) {
			returntime = returntime.substring(0, 16);
		}		
		jsonVO.put("returntime", returntime);//����ʱ��
		jsonVO.put("iscarpool", map.get("iscarpool"));//�ܷ�ƴ��
		jsonVO.put("billstate", state.get(map.get("billstate")));//״̬
		jsonVO.put("vehicleno", map.get("vehicleno")==null?"":map.get("vehicleno"));//���ƺ�
		jsonVO.put("pk_vehicle", map.get("pk_vehicle")==null?"":map.get("pk_vehicle"));//��������
		jsonVO.put("remark", map.get("remark"));//����
		jsonVO.put("turndownreason", map.get("turndownreason"));//����ԭ��
		jsonVO.put("startMileage", map.get("startmileage")==null?"":map.get("startmileage"));//�������
		jsonVO.put("backMileage", map.get("backmileage")==null?"":map.get("backmileage"));//�������
		jsonVO.put("travelMileage", map.get("travelmileage")==null?"":map.get("travelmileage"));//��ʻ���
		jsonVO.put("read", true);// ֻ��
		
		//ѭ�����壬�õ�����list
		JSONArray jsonDetails = new JSONArray(); //����JSON���ݺϼ�
		for (Map<String, Object> map_detail : list_detail) {
			JSONObject jsonDetail = new JSONObject();
			int PNum =Integer.parseInt(map_detail.get("selectpnum").toString()); // ����
			int  selectpnum[]={PNum};
			jsonDetail.put("selectedPNum", selectpnum);//����
			jsonDetail.put("applier", map_detail.get("applier"));//��Ա����
			jsonDetail.put("username", map_detail.get("username"));//����
			jsonDetail.put("userphone", map_detail.get("phone"));//��ϵ�绰
			jsonDetail.put("finaldest", map_detail.get("finaldest"));//Ŀ�ĵ�
			jsonDetail.put("starlevel", map_detail.get("starlevel")==null?"":map.get("starlevel"));//����
			jsonDetail.put("review", map_detail.get("review")==null?"":map.get("review"));//����
			jsonDetail.put("read", true);// ֻ��
			jsonDetails.put(jsonDetail);
		}
		jsonVO.put("user", jsonDetails);
		
		result.put("values", jsonVO);
		result.put("result", "true");
		return result;

	}
	/**
	 * ���µ���״̬
	 * @param jsonObject
	 * @return
	 */
	private JSONObject updateStaue(JSONObject json) throws DAOException, JSONException{
		HYPubBO hYPubBO = new HYPubBO();
		JSONObject result = new JSONObject();
		String pk_vorder = json.getString("pk_vorder"); // ��������
		String billstate = json.getString("billstate"); // ����״̬
		String origin = json.getString("origin"); // ������
		String fdestination = json.getString("destarea"); // ����Ŀ�ĵ�
		String pk_driver = json.getString("pk_driver"); // ˾��
		String vstate = json.getString("vstate"); // ����״̬
		String pk_vehicle = json.getString("pk_vehicle"); // ��������
		//ƴ������ͨ����ʾ��Ϣ
		String message = "";
		//���±�ͷ����״̬
		String sql="update cl_vorder set billstate='"+billstate
				       +"' where pk_vorder='"+pk_vorder+"'";
		//ͬ�����³�����״̬
		String sql_vehicle="update cl_vehicle set vstate='"+vstate
					 +"' where pk_vehicle='"+pk_vehicle+"'";
		//ִ��sql���ĵ���״̬
		try {
			dao.executeUpdate(sql);
			dao.executeUpdate(sql_vehicle);
		} catch (DAOException e1) {
			ExceptionUtils.wrappBusinessException("���ĵ���״̬ʧ�ܣ�"+e1.getMessage());
		}
	
		//��ѯ�ó�ʱ���sql
		String timesql = "select departtime from cl_vorder_b where pk_vorder = '"+pk_vorder+"'";
		//�ó�ʱ��
		String departtime = (String) new BaseDAO().executeQuery(timesql, new ColumnProcessor());
		message = "��\n�ó�ʱ�䣺"+departtime+"��\n�г̣�"+origin+"--"+fdestination+"��";
		//�ҵ������ء�Ŀ�ĵأ�����ʱ�䣬˾����ͬ������pk���ӱ�pk
		String sameSql = "select u.user_name,h.pk_vorder,b.pk_vorder_b,b.phone��b.ts from cl_vorder h, cl_vorder_b b "
				+ ",sm_user u where h.pk_vorder = b.pk_vorder and b.origin = '"+origin+"' "
						+ "and b.finaldest = '"+fdestination+"' and b.departtime "
								+ "= '"+departtime+"' and pk_driver = '"+pk_driver+"' "
										+ "and h.dr = 0 and b.dr = 0 and billstate!=9 and u.cuserid = b.applier";
		//��ѯ�����������������ӱ��pk
		List<Map<String, Object>> pklist=(List<Map<String, Object>>)dao.executeQuery(sameSql, new  MapListProcessor());
		
		VorderBVO bvo = null;
		VorderBVO[] bvos = new VorderBVO[pklist.size()];
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//�����ӱ�pk����
		//�õ���һ�����˵�ts
		if (pklist != null&&pklist.size()>0) {
			
			//�Ƚϣ��������
			Date maxdate = null;
			try {
				maxdate = sf.parse((String)pklist.get(0).get("ts"));
			} catch (ParseException e3) {
				ExceptionUtils.wrappBusinessException("����ת��ʧ�ܣ�"+e3.getMessage());
			}
			//�õ���һ�����˵�ts����������С���Ǹ�ts��
			for (int j = 1; j < pklist.size(); j++) {	
				//ȡ��ts���αȽ�
				String olddate = (String) pklist.get(j).get("ts");
				Date nextts = null;
				try {
					nextts = sf.parse(olddate);
				} catch (ParseException e) {
					ExceptionUtils.wrappBusinessException("����ת��ʧ�ܣ�"+e.getMessage());
				}
				if(nextts.compareTo(maxdate)<0){
					maxdate = nextts;
				}
			}
			//ѭ���õ�����ƴ���ˣ�ֻ���ڷ���Ϣ֮ǰ�õ���
			String text = "�ó��ˣ�"; 
			for (int i = 0; i < pklist.size(); i++) {
				Map<String, Object> map = pklist.get(i);
				//ƴ��������
				String user_name = (String) map.get("user_name");
				if(i==pklist.size()-1){
					text += user_name+"��";
				}else{
					text += user_name+"��";
				}
			}
			message = message + "\n" + text;
			for (int i = 0; i < pklist.size(); i++) {
				String isfirstApplier = "N";
				//ȡ��ÿ���ӱ�pk
				Map<String, Object> map = pklist.get(i);
				String hvopk = (String) map.get("pk_vorder");
				String bvopk = (String) map.get("pk_vorder_b");
				//ȡ��ÿ��ƴ���˵ĵ绰
				String phone = (String) map.get("phone");
				//�õ�ts�ж��Ƿ��ǵ�һ������
				String ts = (String) map.get("ts");
				Date date = null;
				try {
					date =  sf.parse(ts);
				} catch (ParseException e2) {
					ExceptionUtils.wrappBusinessException("����ת��ʧ�ܣ�"+e2.getMessage());
				}
				//�����ӱ�pk�õ��ӱ����
				try {
					bvo = (VorderBVO) hYPubBO.queryByPrimaryKey(VorderBVO.class, bvopk);
				} catch (UifException e) {
					ExceptionUtils.wrappBusinessException("�õ��ӱ����ʧ�ܣ�"+e.getMessage());
				}
				//�����ǰ�˵�ts==��һ�����˵�ts������Ϊ��һ������
				if(date.compareTo(maxdate)==0){
					isfirstApplier = "Y";
				}
				
				//���õ���bvo����Ĺ�����pk_vorder�ĳɺʹ����pk_vorderһ��
				String updatehpk = "update cl_vorder_b set isfisrtapplier = '"+isfirstApplier+"', pk_vorder = '"+pk_vorder+"' where pk_vorder_b ='"+bvopk+"' ";
				try {
					dao.executeUpdate(updatehpk);
				} catch (DAOException e1) {
					ExceptionUtils.wrappBusinessException("�����ӱ�ʧ�ܣ�"+e1.getMessage());
				}
				//�ӱ�ϲ�
				bvos[i] = bvo;
				if(!hvopk.equals(pk_vorder)){
					String updateDrsql = "update cl_vorder set dr = 1 where pk_vorder = '"+hvopk+"'";
					try {
						dao.executeUpdate(updateDrsql);
					} catch (DAOException e) {
						ExceptionUtils.wrappBusinessException("ɾ�������ӱ���ʧ�ܣ�"+e.getMessage());
					}
				}
				//�ѿռ��ƴ���˷�����Ϣ��4ͨ��
				if("4".equals(billstate)){
					//����ͨ�����뵥������ͨ������ʾ��Ϣ��ƴ���ˣ����һ������0�����û���ͨ����Ϣ;1����˾����ͨ������Ϣ;3�����û�����ͨ������Ϣ��
					sendYonyouMessage(message,phone,0);
				}else{
					//���ܲ������뵥�����ò�ͨ������ʾ��Ϣ�������͸�ƴ����
					message = "���ύ���ó����뵥�ѱ����أ����޸ĵ��ݺ������ύ";
					sendYonyouMessage(message,phone,3);
				}
			}
		}
		
		
		//��4����������ͨ�����������ͨ������Ҫ��˾������֪ͨ
		if("4".equals(billstate)){	
			//��ѯ˾���绰,���ڸ�˾������֪ͨ
			String driversql = "select dphone from cl_vorder where pk_vorder = '"+pk_vorder+"'";
			//�õ�˾���ĵ绰
			String dphone = "";
			try {
				dphone = (String) new BaseDAO().executeQuery(driversql, new ColumnProcessor());
				//�ѿռ��˾��������Ϣ
				sendYonyouMessage(message,dphone,1);
			} catch (DAOException e) {
				ExceptionUtils.wrappBusinessException("��ѯ˾���绰ʧ�ܣ�"+e.getMessage());
			}
		}else{
			//���²���ԭ��
			String turndownreason = "";
			if(StringUtils.isNotBlank(json.getString("turndownreason"))){
				turndownreason=	json.getString("turndownreason"); //��ǰ��¼������
				String sql_turndownreason="update cl_vorder_b set turndownreason='"+turndownreason+"'"
						+ " where pk_vorder='"+pk_vorder+"'";
				dao.executeUpdate(sql_turndownreason);
			}
			//������ݱ����أ��������󷽷��޸ĵ��ݵ�����״̬
			VorderHVO oldhvo = null;;
			try {
				oldhvo = (VorderHVO) hYPubBO.queryByPrimaryKey(VorderHVO.class, pk_vorder);
			} catch (UifException e) {
				ExceptionUtils.wrappBusinessException("��ѯ������Ϣʧ�ܣ�"+e.getMessage());
			}
			//Ҫ�޸�����״̬Ϊ���󣨻ص��ύ״̬��������VO
			VorderHVO newhvo = (VorderHVO) oldhvo.clone();
			//��������״̬Ϊ�ύ
			newhvo.setAttributeValue("approvestatus", 3);
			//ԭ�е�Aggvo���飨������״̬��
			AggVorderHVO[] oldAggvos = getAggVOs(oldhvo, bvos);	
			//�µ�Aggvo���飨�ύ״̬״̬��
			AggVorderHVO[] newAggvos = getAggVOs(newhvo, bvos);
			//�������󷽷�
			//new AceVorderUnApproveBP().unApprove(newAggvos,oldAggvos);
		}
		return result;
	}
	/**
	 * ���µ�����Ϣ
	 * @param jsonObject
	 * @return
	 * @throws ParseException 
	 * @throws Exception 
	 */
	private JSONObject updateVorder(JSONObject json) throws DAOException, JSONException, ParseException{
	
		JSONObject result = new JSONObject();
		String pk_vorder = json.getString("pk_vorder"); // ����
		String driver =json.getString("driver"); // ˾��
		String pk_driver =json.getString("pk_driver"); // ˾������
		String dphone =json.getString("dphone"); // ˾���绰
		String billstate =json.getString("billstate"); // ����״̬
		String pk_vehicle ="";
		if(json.get("pk_vehicle")!=null){
			pk_vehicle=json.getString("pk_vehicle"); // ���ƺ�
		}
		String vehicleno ="";
		if(json.get("vehicleno")!=null){
			vehicleno=json.getString("vehicleno"); // ���ƺ�
		}
		//����ǰУ��˾��  kkk
		String departtime =json.getString("departtime"); // ����ʱ��
		String returntime =json.getString("returntime"); // ����ʱ��
		JSONObject check_driver = new JSONObject();
		check_driver.put("pk_driver", pk_driver);
		check_driver.put("departtime", departtime);
		check_driver.put("returntime", returntime);
		check_driver.put("pk_vorder", pk_vorder);
		JSONObject check=updateCheck(check_driver);
		JSONObject ret=check.getJSONObject("values");
		if(!ret.getBoolean("isvord")&&!ret.getBoolean("iscarpool")){
			result.put("result", "false");
			result.put("message", ret.get("message").toString());
			return result;
		}
		//���±�ͷ����
		String hvosql="update cl_vorder set driver='"+driver
					   +"' , pk_driver='"+pk_driver
					   +"' , dphone='"+dphone
					   +"' , pk_vehicle='"+pk_vehicle
					   +"' , vehicleno='"+vehicleno
				       +"' where pk_vorder='"+pk_vorder+"'";
		dao.executeUpdate(hvosql);
		if("�����".equals(billstate)){//��� ����״̬Ϊ����ɣ��������
			String startMileage =json.getString("startMileage"); 
			String backMileage =json.getString("backMileage"); 
			String travelMileage =json.getString("travelMileage"); 
			//���±�ͷ����
			String sql="update cl_vorder set startMileage='"+startMileage
						   +"' , backMileage='"+backMileage
						   +"' , travelMileage='"+travelMileage
					       +"' where pk_vorder='"+pk_vorder+"'";
			dao.executeUpdate(sql);
		}
		result.put("result", "true");
		return result;
	}
	/**
	 * ��ѯ������Ϣ
	 * @param jsonObject
	 * @return
	 */
	private JSONObject querVehicle(JSONObject jsonObject) throws DAOException, JSONException{
		JSONArray jsonArr = new JSONArray(); //���ڴ洢������Ϣ����
		//���˵���Ϣ�������˾��
		String sql_alldrivers="select cl_driver.pk_driver,cl_driver.dname,cl_driver.dphone,cl_vehicle.pk_vehicle,cl_vehicle.vehicleno "
				+ " from cl_driver "
				+ " left join cl_vehicle on cl_vehicle.pk_driver=cl_driver.pk_driver and cl_vehicle.dr=0"
				+ " where cl_driver.dr=0 and dstate not in (2,4)";
		
		List<Map<String,Object>> list=(List<Map<String, Object>>) dao.executeQuery(sql_alldrivers, new  MapListProcessor());
		for (Map<String, Object> map : list) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("pk_driver", map.get("pk_driver"));//˾������
			jsonObj.put("pk_vehicle", map.get("pk_vehicle")==null?"":map.get("pk_vehicle"));//��������
			jsonObj.put("vehicleno", map.get("vehicleno")==null?"":map.get("vehicleno"));//���ƺ�
			jsonObj.put("driver", map.get("dname"));//˾������
			jsonObj.put("dphone", map.get("dphone"));//˾���绰
			jsonArr.put(jsonObj);
		}
		//������г��ƺ���Ϣ��JSONArray
		JSONArray jsonVehicleArray = new JSONArray();
		//��ѯ���ƺźͳ��ƺ�pk��sql������������dr��Ϊ0��
		String sql_vehicle = "select pk_vehicle,vehicleno from cl_vehicle"
				+ " where cl_vehicle.dr=0";
		//��ų��ƺŶ�list
		List<Map<String,String>> list_vehicle= (List<Map<String, String>>) dao.executeQuery(sql_vehicle, new MapListProcessor());
		//����
		for (Map<String, String> map : list_vehicle) {
			//���һ�����ƺ���Ϣ��json
			JSONObject jsonObj = new JSONObject();
			//�����ƺ�pk����jsonObj
			jsonObj.put("pk_vehicle", map.get("pk_vehicle"));
			//�����ƺŷ���jsonObj
			jsonObj.put("vehicleno", map.get("vehicleno"));
			//���������ƺ���Ϣ���������г��ƺ���Ϣ��jsonArray��
			jsonVehicleArray.put(jsonObj);
		}
		JSONObject result = new JSONObject();
		result.put("values", jsonArr);//˾��
		result.put("jsonVehicleArray", jsonVehicleArray);//����
		result.put("result", "true");
		return result;	
	}
	
	/**
	 * ����˾��
	 * @param jsonObject
	 * @return
	 * @throws JSONException 
	 * @throws DAOException 
	 * @throws ParseException 
	 */
	private JSONObject filterDriver(JSONObject json) throws JSONException, DAOException, ParseException {
		JSONObject result = new JSONObject();
		JSONArray jsonArr_vord = new JSONArray(); //���ڴ洢�����ó���˾������
		String selectDeparttime = json.getString("departtime"); //����ʱ��
		String selectReturntime = json.getString("returntime"); //����ʱ��
		//��ѯ���е�˾��,�������������Ϣ��
		String sql_alldrivers="select cl_driver.pk_driver,cl_driver.dname,cl_driver.dphone,cl_vehicle.pk_vehicle,cl_vehicle.vehicleno "
				+ " from cl_driver "
				+ " left join cl_vehicle on cl_vehicle.pk_driver=cl_driver.pk_driver and cl_vehicle.dr=0"
				+ " where cl_driver.dr=0 and dstate not in (2,4)";
		List<Map<String,String>> list_alldrivers= (List<Map<String, String>>) dao.executeQuery(sql_alldrivers, new  MapListProcessor());
		for(Map<String,String> driverMap:list_alldrivers){//����˾��������
			Boolean isvord=false;//�Ƿ��������ó�
			String pk_driver=driverMap.get("pk_driver");//˾������
			String dname=driverMap.get("dname");//˾������
			String dphone=driverMap.get("dphone");//˾���绰
			String pk_vehicle=driverMap.get("pk_vehicle")==null?"":driverMap.get("pk_vehicle");//��������
			String vehicleno=driverMap.get("vehicleno")==null?"":driverMap.get("vehicleno");//���ƺ�
			//��ѯ���ϸ�˾���Ķ�����������ϢΪ��һ�����˵ģ�
			String sql="select * "
					+" from cl_vorder "
					+" inner join cl_vorder_b"
					+" on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0"
					+" where pk_driver='"+pk_driver+"' "
					+ " and cl_vorder.dr=0 "
					+" and billstate !=9 and billstate !=1"
					//��ѯǰ������Ķ���
					+" and trunc(to_date(substr(departtime,0,10),'yyyy-mm-dd')) BETWEEN  trunc(sysdate)-4 and trunc(sysdate)+4"
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
					//����ʱ��
					Date departtime = sf.parse((String)map.get("departtime"));
					//����ʱ��
					Date returntime = sf.parse((String)map.get("returntime"));
					 if(selectDepartdate.after(new Date(returntime.getTime() + time))){//�û�����ʱ��>����ʱ��+30�����������ó�
						isvord=true;//�����ó�
					}else if(selectReturndate.before(new Date(departtime.getTime() - time))){//�û�����ʱ��<�ó�ʱ��-30�����������ó�
						isvord=true;//�����ó�
					}else{//������ʾ��ʱ��α�ռ��
						isvord=false;//���������ó�
						break;
					}	
				}
			}else{
				isvord=true;//���������ó�
			}
			 if(isvord){//���������ó�
				JSONObject jsonVO = new JSONObject();
				jsonVO.put("pk_driver", pk_driver);
				jsonVO.put("driver", dname);
				jsonVO.put("dphone", dphone);
				jsonVO.put("pk_vehicle", pk_vehicle);
				jsonVO.put("vehicleno", vehicleno);
				jsonArr_vord.put(jsonVO);
			}
		}
		
		result.put("values", jsonArr_vord);
		result.put("result", "true");
		return result;
		
	}
	
	/**
	 * ����У��
	 * @param jsonObject
	 * @return
	 * @throws JSONException 
	 * @throws DAOException 
	 * @throws ParseException 
	 */
	private JSONObject updateCheck(JSONObject jsonObject) throws JSONException, DAOException, ParseException {
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
	 * �����ѿռ���Ϣ
	 * @return
	 */
	private boolean sendYonyouMessage(String message,String field,Integer num){
		boolean messageResult = false;
		// ��ȡaccess_token
		String accessToken = YonyouMessageUtil.getAccessToken();
		// TODO ��������Ҫ����Ϊ��̬��Ա
		/*field = "15541523099";*/
		String fieldtype = "1";
		// ��ȡMemberId��1���ֻ� 2�����䣩
		String memberId = YonyouMessageUtil.getMemberId(accessToken, field, fieldtype);
		List<String> tos = new ArrayList<String>();
		tos.add(memberId);
		String text = "";
		if(num == 0){
			text = "���ύ���ó����뵥������ͨ������ע���ó�ʱ��";
		}else if(num == 1){
			text = "���µ��ó����뵥������ͨ�����뼰ʱ�鿴����";
		}
		//ƴ����������ʾ��Ϣ
		message = text + message;
		//������Ϣ
		messageResult = YonyouMessageUtil.sendMessage(accessToken, YonyouMessageUtil.messagePojo(tos ,"�������뵥��������", message));
		return messageResult;
	}
	
	
	/**
	 * ���aggvo�����ķ���
	 * @return 
	 * @return
	 */
	private AggVorderHVO[] getAggVOs(VorderHVO hvo,VorderBVO[] bvos){
		//�½�aggVorderHVO����
		AggVorderHVO aggVorderHVO = new AggVorderHVO();
		//��ֵ--����
		aggVorderHVO.setParentVO(hvo);
		//�ӱ�
		aggVorderHVO.setChildrenVO(bvos);
		//�½�AggVorderHVO��������ֵ
		AggVorderHVO[] aggVorderHVOs = new AggVorderHVO[]{aggVorderHVO};
		//����AggVorderHVO��������
		return aggVorderHVOs;
	}
	
	/**
	 * ��ѯ��֯����
	 * @param jsonObject
	 * @return
	 */
	private JSONObject queryCode(JSONObject jsonObject) throws DAOException, JSONException{
		//����������ѯ��֯����
		String sql="select code from org_orgs where pk_org = '" + jsonObject.getString("pk_org")+ "'";
		
		String code=(String) dao.executeQuery(sql, new  ColumnProcessor());
	
		JSONObject result = new JSONObject();
		result.put("values", code);//˾��
		result.put("result", "true");
		return result;	
	}
}
