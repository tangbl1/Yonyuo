package nc.itf.ispt.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import disposition.GetDatasourceName;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.comn.NetStreamContext;
import nc.bs.framework.core.service.IFwLogin;
import nc.itf.vehicle.IInspectionFilesMaintain;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pub.lang.UFDate;
import nc.vo.vehicle.inspectionfiles.AggInspectionFileHVO;
import nc.vo.vehicle.inspectionfiles.InspectionFileBVO;
import nc.vo.vehicle.inspectionfiles.InspectionFileHVO;


@SuppressWarnings("restriction")
@WebServlet("/hwip")
public class InspectionServlet extends HttpServlet {

	
	BaseDAO dao = new BaseDAO();
	private static final long serialVersionUID = 1L;
	
	/**
	 * ����������Ϣ�쳵��ҳ��servlet
	 * ���ڴ���쳵��ҳ������
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
		String method = req.getParameter("method");	//��ȡǰ̨����method��ʶ���������ִ�����
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
			if ("add".equals(method)) {
				result = addInspection(jsonObject);
			} else if ("query".equals(method)){
				result = queryInspection(jsonObject);
			} else if("delete".equals(method)){
				result = deleInspection(jsonObject);
			} else if("save".equals(method)){
				result = saveInspection(jsonObject);
			} else if("query_vehicle".equals(method)){
				result = querVehicle(jsonObject);
			}
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		
		if(StringUtils.isNotBlank(errMsg)){
			try {
				result.put("success", false);
				result.put("errMsg", errMsg);
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
	 * ��ѯ�����ĳ��ƺ�pk�ͳ��ƺ�
	 * ������JSONObject json
	 * @return JSONObject
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject querVehicle(JSONObject jsonObject) throws DAOException, JSONException {
		//���ڷ���ǰ̨��jsonResult
		JSONObject result = new JSONObject();
		//������г��ƺ���Ϣ��JSONArray
		JSONArray jsonArray = new JSONArray();
		//��ѯ����dr=0�ĳ��ƺ�pk�ͳ��ƺ�
		String sql="select pk_vehicle,vehicleno from cl_vehicle"
				+ " where cl_vehicle.dr=0";
		//��ų��ƺŽ�����ļ���
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
		//�����ѯ�Ľ������Ϊ�գ�����
		if (list != null) {
			//���������
			for (Map<String,String> map : list) {
				//���һ�����ƺ���Ϣ��JSONObject
				JSONObject jsonObj = new JSONObject();
				//���ƺ�����
				jsonObj.put("pk_vehicle", map.get("pk_vehicle"));
				//���ƺ�
				jsonObj.put("vehicleno", map.get("vehicleno"));
				//���������ݷŵ�JSONArray��
				jsonArray.put(jsonObj);
			}
			//��ʾ��Ϣ����jsonResult
			result.put("result", "true");
			//���ƺ���Ϣ����jsonResult
			result.put("data", jsonArray);
		}
		//����
		return result;
	}
	/**
	 * �޸ı���쳵��
	 * ������JSONObject json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject saveInspection(JSONObject json) throws JSONException {
		//���ڷ��ص�jsonobject
		JSONObject result = new JSONObject();
		//�쳵������
		String pk_inspection = json.getString("pk_inspection"); 
		//�쳵���ӱ�����
		String pk_inspection_b = json.getString("pk_inspection_b");
		//���ƺ�
		String vehicleno = json.getString("vehicleno"); 
		//�쳵��Ч��
		String iexpiredate = json.getString("iexpiredate");
		//�쳵����
		String dmv = json.getString("dmv");
		UFDate date=null;
		//����쳵��Ч�ڲ�Ϊ�գ��������ڸ�ʽ����ȡ��ֵ
		if(StringUtils.isNotBlank(iexpiredate)){
			//�������ڸ�ʽ
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM");
			try {
				//ת��
				 date = new UFDate( timeFormat.parse( iexpiredate ) ) ;
			} catch (java.text.ParseException e1) {
				ExceptionUtils.wrappBusinessException("�쳵������ת��ʧ��"+e1.getMessage());
			}
		}
		//���±�ͷ����
		String hvosql="update cl_inspection set vehicleno='"+vehicleno
				       +"' where pk_inspection='"+pk_inspection+"'";
		//���±�������
		String bvosql="update cl_inspection_b set iexpiredate='"+iexpiredate+"' , dmv='"+dmv
				       + "' where pk_inspection_b='"+pk_inspection_b+"'";
		try {
			//ִ���޸ĵ�sql
			dao.executeUpdate(hvosql);
			dao.executeUpdate(bvosql);
			result.put("result", "true");
		} catch (DAOException e1) {
			ExceptionUtils.wrappBusinessException("�޸ļ쳵����Ϣʧ�ܣ�"+e1.getMessage());
		}
		return result;
	}
	
	/**
	 * ɾ���쳵��
	 * ������JSONObject json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject deleInspection(JSONObject json) throws JSONException {
		//���ڷ��ص�jsonobject
		JSONObject result = new JSONObject();
		//��鵥��������
		String pk_inspection = json.getString("pk_inspection");
		//�쳵���ӱ�����
		String pk_inspection_b = json.getString("pk_inspection_b");
		//ɾ����ͷ����
		String sql_h = "update cl_inspection set dr = 1 where pk_inspection ='"
				+ pk_inspection + "'";
		//ɾ����ͷ����
		String sql_b = "update cl_inspection_b set dr = 1 where pk_inspection_b ='"
				+ pk_inspection_b + "'";
		try {
			//ִ��sql���ɾ������
			dao.executeUpdate(sql_h);
			dao.executeUpdate(sql_b);
			result.put("result", "true");
		} catch (DAOException e) {
			ExceptionUtils.wrappBusinessException("ɾ��˾��������Ϣʧ�ܣ�"+e.getMessage());
		}
		return result;
	}
	
	/**
	 * ��ѯ�쳵��
	 * ������JSONObject json
	 * @return 
	 * @throws Exception
	 */
	private JSONObject queryInspection(JSONObject json) throws Exception {
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		int maxnum = json.getInt("maxcount"); // �������
		int minnum = json.getInt("mincount"); // ��С����
		//��ѯ�쳵���������ݺź��ӱ�ȫ�����ݣ���ҳ��ѯ��
		String sql="SELECT * from (SELECT ROWNUM AS rowno1, t.* from ( select cl_inspection.billno,"
				+ "cl_inspection_b.*,cl_vehicle.pk_vehicle,cl_vehicle.vehicleno vlno,org_hrorg.name as unit,"
				+ "cl_vehicle.vtype,cl_vehicle.vphoto,cl_vehicle.vcharacter"
				+ " from cl_inspection inner join cl_inspection_b"
				+" on cl_inspection.pk_inspection=cl_inspection_b.pk_inspection and cl_inspection_b.dr=0"
				+" left join cl_vehicle on cl_vehicle.pk_vehicle=cl_inspection.vehicleno left join org_hrorg  on org_hrorg.pk_hrorg = cl_vehicle.unit"
				+" where cl_inspection.dr=0 order by cl_inspection.ts desc) t where ROWNUM <= "+maxnum+") c WHERE c.rowno1 >"+minnum;
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
		//���������
		for (Map<String,String> map : list) {
			//���һ�����ݵ�mapJSON
			JSONObject jsonObj = new JSONObject();
			//�쳵������pk
			jsonObj.put("pk_inspection", map.get("pk_inspection"));
			//�쳵���ӱ�pk
			jsonObj.put("pk_inspection_b", map.get("pk_inspection_b"));
			//���ݺ�
			jsonObj.put("billno", map.get("billno"));
			//����
			jsonObj.put("dept", map.get("dept")!=null?map.get("dept"):"");
			//��λ
			jsonObj.put("unit", map.get("unit")!=null?map.get("unit"):"");
			//���ƺŴ�Ϊjson����
			JSONObject jsonobj_vehicle = new JSONObject();
			//���ƺ�pk
			jsonobj_vehicle.put("pk_vehicle", map.get("pk_vehicle")!=null?map.get("pk_vehicle"):"");
			//���ƺ�
			jsonobj_vehicle.put("vehicleno", map.get("vlno")!=null?map.get("vlno"):"");
			//���ƺŶ���
			jsonObj.put("pk_vehicle", jsonobj_vehicle);
			//���ƺ�
			jsonObj.put("vlno", map.get("vlno")!=null?map.get("vlno"):"");
			//��������
			jsonObj.put("vtype", map.get("vtype")!=null?map.get("vtype"):"");
			//��������
			jsonObj.put("vcharacter",map.get("vcharacter")!=null?map.get("vcharacter"):"");
			//�������ڶ���
			String iexpiredate=map.get("iexpiredate");
			//��ȡ����
			if(StringUtils.isNotBlank(iexpiredate)){
				jsonObj.put("iexpiredate", iexpiredate.toString().substring(0, 7));
			}
			//�������
			jsonObj.put("dmv", map.get("dmv"));
			//ͼƬ����
			Object img = map.get("vphoto");
			byte[] byte_img = null;
			String imgDatas = "";
			if(img != null){
				byte_img = (byte[]) img;
				imgDatas = new String(byte_img,"UTF-8");
			}
			jsonObj.put("vphoto", imgDatas);// ͼƬ
			//���������ݷ���mapArrJSON
			jsonArray.put(jsonObj);	
		}
		result.put("values", jsonArray);
		result.put("result", "true");
		return result;
	}
	
	/**
	 * ��ѯ�쳵��
	 * ������JSONObject json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject addInspection(JSONObject json) throws JSONException {
		//���ڷ��ص�result
		JSONObject result = new JSONObject();
		//Я����ǰ��¼�û���Ϣ��userjson
		JSONObject userjsonObject = json.getJSONObject("userjson"); // ��¼��
		String cuserid = userjsonObject.getString("cuserid"); //��ǰ��¼������
		String pk_org = userjsonObject.getString("pk_org"); //��ǰ��¼��������֯
		String pk_group = userjsonObject.getString("pk_group"); //��ǰ��¼����������
		String vehicleno = json.getString("vehicleno"); // ���ƺ�
		String iexpiredate = json.getString("iexpiredate");// �쳵��Ч��
		String dmv =json.getString("dmv"); // �쳵����
		UFDate date=null;
		if(StringUtils.isNotBlank(iexpiredate)&&!"��ѡ��".equals(iexpiredate)){
		//�������ڸ�ʽ
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM");
			try {
				//ת��
				 date = new UFDate( timeFormat.parse( iexpiredate ) ) ;
			} catch (java.text.ParseException e1) {
				ExceptionUtils.wrappBusinessException("�쳵��Ч�ڸ�ʽת��ʧ��"+e1.getMessage());
			}
		}
		//�½�AggVO
		AggInspectionFileHVO aggvo = new AggInspectionFileHVO();
		//�½��쳵������VO
		InspectionFileHVO hvo = new InspectionFileHVO();
		//��ֵ�����ƺ�
		hvo.setAttributeValue("vehicleno", vehicleno);
		//��ֵ������
		hvo.setAttributeValue("pk_group", pk_group);
		//��ֵ����֯
		hvo.setAttributeValue("pk_org", pk_org);
		//��ֵ��cuserId
		hvo.setAttributeValue("cuserId", cuserid);
		//aggvo����ֵ
		aggvo.setParent(hvo);
		//�½���ⵥ�ӱ�VO
		InspectionFileBVO bvo=new InspectionFileBVO();
		//��ֵ���쳵����Ч��
		bvo.setAttributeValue("iexpiredate", date);
		//��ֵ���쳵����
		bvo.setAttributeValue("dmv", dmv);
		//�����vo���뵽�ӱ�vo������
		InspectionFileBVO[] bvos={bvo};
		//aggvo��ֵ
		aggvo.setChildrenVO(bvos);
		// ����GroupId
		InvocationInfoProxy.getInstance().setGroupId(
				hvo.getAttributeValue("pk_group").toString());
		//������
		InvocationInfoProxy.getInstance().setUserId(
				hvo.getAttributeValue("cuserId").toString());
		//�½�AggVO���鲢��ֵ
		AggInspectionFileHVO[] aggvos = { aggvo };
		//�쳵����׼�ӿ�
		IInspectionFilesMaintain iInspectionMaintain = (IInspectionFilesMaintain) NCLocator
				.getInstance().lookup(IInspectionFilesMaintain.class);
		try {
			//���ýӿڵ�������������
			AggInspectionFileHVO[] aggdfVO = iInspectionMaintain.insert(aggvos,
					null);
			result.put("result", "true");
		} catch (nc.vo.pub.BusinessException e) {
			ExceptionUtils.wrappBusinessException("���˾��������Ϣʧ�ܣ�"+e.getMessage());
		}
		return result;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

}
