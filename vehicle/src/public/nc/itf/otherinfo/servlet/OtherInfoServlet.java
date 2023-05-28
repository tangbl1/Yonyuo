package nc.itf.otherinfo.servlet;

import java.io.IOException;
import java.util.HashMap;
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
import nc.bs.trade.business.HYPubBO;
import nc.itf.vehicle.IOtherInfoMaintain;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.vehicle.insurance.InsuranceBVO;
import nc.vo.vehicle.otherinfo.AggOtherInfoVO;
import nc.vo.vehicle.otherinfo.OtherInfoVO;




@SuppressWarnings("restriction")
@WebServlet("/otherinfo")
public class OtherInfoServlet extends HttpServlet {
	
	BaseDAO dao = new BaseDAO();
	HYPubBO hYPubBO = new HYPubBO();
	private HashMap<String, InsuranceBVO> containVos = new HashMap<String, InsuranceBVO>();
	private static final long serialVersionUID = 1L;
	
	/**
	 * ����������Ϣ������Ϣ�ҳ��servlet
	 * ���ڴ���������Ϣ�ҳ������
	 * @author 
	 * @date 2021-04-25
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
			if ("save".equals(method)) {
				result = saveOtherInfo(jsonObject);//�޸ĺ󱣴�
			} else if ("add".equals(method)) {
				result = addOtherInfo(jsonObject);//����������Ϣ�
			} else if("delete".equals(method)){
				result = deleOtherInfo(jsonObject);//ɾ��������Ϣ�
			} else if("query".equals(method)){
				result = queryOtherInfo(jsonObject);//��ѯ������Ϣ��б�
			} else if("updateStatus".equals(method)){
				result = updateStatus(jsonObject);//���µ���״̬
				
			}
		} catch (Exception e) {
			errMsg = e.getMessage();
			if(StringUtils.isBlank(errMsg)) {
				errMsg = "δ֪����";
			}
//			ExceptionUtils.wrappException(e);
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
	 * ������Ϣ��޸ı��淽��
	 * ������JSONObject json
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject saveOtherInfo(JSONObject json) throws Exception {
		//���ں���Ա��Ƿ��б�ɾ�����ӱ�
		containVos = new HashMap<String, InsuranceBVO>();
		JSONObject userjsonObject = json.getJSONObject("userjson"); // ��¼��
		String pk_org = userjsonObject.getString("pk_org"); //��ǰ��¼��������֯
		String pk_group = userjsonObject.getString("pk_group"); //��ǰ��¼����������
		//���ڷ���ǰ̨��JSONObject
		JSONObject result = new JSONObject();
		//�õ�������Ϣ�����pk
		String pk_otherinfo = json.getString("pk_otherinfo");
				
		//�޸������sql������������Ϣ�����pk���޸Ķ�Ӧ���ݵĳ��ƺ�
		String sql = "update cl_otherinfo set dayfuel = " + new UFDouble(json.getString("dayfuel")) 
				+ ", amount = " + new UFDouble(json.getString("amount")) 
				+ ", otherkm = " + new UFDouble(json.getString("otherkm"))
				+ ", emptytimesam = '" + new UFBoolean(json.getBoolean("emptytimesam"))
				+ "', emptytimespm = '" + new UFBoolean(json.getBoolean("emptytimespm"))
				+ "', bonuspenalty = " + new UFDouble(json.getString("bonuspenalty"))
				+ ", wkdovertime = " + new UFDouble(json.getString("wkdovertime"))
				+ ", def1 = '" + json.getString("def1")
				+ "', def2 = '" + json.getString("def2")
				+ "' where pk_otherinfo = '"+pk_otherinfo+"'";
		dao.executeUpdate(sql);
		//�����Ƿ��޸ĳɹ�
		result.put("success", true);
		result.put("result", "true");
		//��������ظ�ǰ̨
		return result;
	}
	

	/**
	 * ɾ������
	 * ������JSONObject json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject deleOtherInfo(JSONObject json) throws JSONException {
		//���ڷ���ǰ̨
		JSONObject result = new JSONObject();
		// ��������
		String pk_otherinfo = json.getString("pk_otherinfo");
//		//���ڵõ����ӱ�VO�����sql
//		String sql = "update cl_otherinfo set dr = 1 where pk_otherinfo = '"+ pk_otherinfo + "'";
		
		try {
			//ɾ�����ӱ�VO����
			dao.deleteByPK(OtherInfoVO.class, pk_otherinfo);
			result.put("success", true);
			result.put("result", "true");
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("ɾ��������Ϣ���Ϣʧ�ܣ�"+e.getMessage());
		}
		//����
		return result;
	}
	/**
	 * ��ѯ������Ϣ�
	 * ������JSONObject json
	 * @return 
	 * @return 
	 * @throws Exception
	 */
	private JSONObject queryOtherInfo(JSONObject json) throws Exception {
		JSONObject result = new JSONObject();
		//��Ų�ѯ������������
		JSONArray jsonArray = new JSONArray();
		JSONObject userjsonObject = json.getJSONObject("userjson"); // ��¼��
		String roleName = userjsonObject.getString("role_name"); //��ǰ��¼�˽�ɫ
//		postName = new String(postName.getBytes("iso-8859-1"),"UTF-8");
		String cuserId = userjsonObject.getString("cuserid"); //��ǰ��¼������
		
		int maxnum = json.getInt("maxcount"); // �������
		int minnum = json.getInt("mincount"); // ��С����
		//��ѯ����pk�����ڡ��ռ������������������������������ʻ�����ʻ���ս�������sql
		
		String sql="SELECT * FROM (SELECT ROWNUM AS rowno, t.* "
				+ "FROM (select pk_otherinfo, infodate, dayfuel, amount, otherkm, emptytimesam, emptytimespm, bonuspenalty, wkdovertime, approvestatus,def1,def2,user_name "
				+ "from cl_otherinfo,sm_user where  cl_otherinfo.creator = sm_user.cuserid and cl_otherinfo.dr=0 ";
		if(roleName.equals("��������Ա")) {
			sql += "and cl_otherinfo.approvestatus in ( 2, 3 )";
		}else {
			sql += "and cl_otherinfo.creator = '" + cuserId + "' ";
		}
		sql += "order by cl_otherinfo.infodate desc) t "
				+ "where ROWNUM <= " + maxnum + ") table_alias WHERE table_alias.rowno > " + minnum	+ " ";
		//�½��������ڴ�Ų�ѯ���
		List<Map<String,Object>> list= (List<Map<String, Object>>) dao.executeQuery(sql, new  MapListProcessor());
		//����������ѯ�������ݷ���json
		for (Map<String,Object> map : list) {
			//�������һ�����ݵ�json
			JSONObject jsonObj = new JSONObject();
			
			//��ֵ--������Ϣ�����pk
			jsonObj.put("pk_otherinfo", map.get("pk_otherinfo") == null ? "" : map.get("pk_otherinfo"));
			//����
			jsonObj.put("infodate", map.get("infodate") == null ? "" : map.get("infodate"));
			//�ռ�����
			jsonObj.put("dayfuel", map.get("dayfuel") == null ? "" : map.get("dayfuel"));
			//���
			jsonObj.put("amount", map.get("amount") == null ? "" : map.get("amount"));
			//����������������
			jsonObj.put("otherkm", map.get("otherkm") == null ? "" : map.get("otherkm"));
			//���ʻ
			jsonObj.put("emptytimesam", map.get("emptytimesam") == null ? "" : map.get("emptytimesam"));
			//���ʻ
			jsonObj.put("emptytimespm", map.get("emptytimespm") == null ? "" : map.get("emptytimespm"));
			//�ս������
			jsonObj.put("bonuspenalty", map.get("bonuspenalty") == null ? "" : map.get("bonuspenalty"));
			//��ĩ�Ӱ����
			jsonObj.put("wkdovertime", map.get("wkdovertime") == null ? "" : map.get("wkdovertime"));
			//�೵ÿ����ʼ���
			jsonObj.put("def1", map.get("def1") == null ? "" : map.get("def1"));
			//�೵ÿ���ֹ���
			jsonObj.put("def2", map.get("def2") == null ? "" : map.get("def2"));
			//�Ƶ���
			jsonObj.put("user_name", map.get("user_name") == null ? "" : map.get("user_name"));
			//״̬
			Integer approvestatus = Integer.valueOf(map.get("approvestatus").toString());
			switch(approvestatus) {
			case -1://����
				jsonObj.put("approvestatus", "����");
				break;
			case 3://�ύ
				jsonObj.put("approvestatus", "���ύ");
				break;
			case 1://������
				jsonObj.put("approvestatus", "������");
				break;
			default:
				jsonObj.put("approvestatus", "����");
				break;
			}
			jsonArray.put(jsonObj);
		}
		result.put("values",jsonArray);
		result.put("success", true);
		result.put("result","true");
		return result;
	}
		
	/**
	 * ����������Ϣ�
	 * ������JSONObject json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject addOtherInfo(JSONObject json) throws JSONException {
		//���ڷ��ص�JSONObject
		JSONObject result = new JSONObject();
		String infodate = json.getString("infodate"); // ����
		JSONObject userjsonObject = json.getJSONObject("userjson"); // ��¼��
		String cuserid = userjsonObject.getString("cuserid"); //��ǰ��¼������
		String pk_org = userjsonObject.getString("pk_org"); //��ǰ��¼��������֯
		String pk_group = userjsonObject.getString("pk_group"); //��ǰ��¼����������
		//�½�����VO	
		OtherInfoVO hvo = new OtherInfoVO();
		hvo.setStatus(VOStatus.NEW);
		//���ü���--ȡ�û���������
		hvo.setAttributeValue("pk_group", pk_group);
		//������֯--ȡ�û�������֯
		hvo.setAttributeValue("pk_org", pk_org);
		//���ô�����
		hvo.setAttributeValue("creator", cuserid);
		//���õ�������
		hvo.setAttributeValue("billdate", new UFDate());
		//���õ���״̬
		hvo.setAttributeValue("approvestatus", BillStatusEnum.FREE.value());
		
		//��������
		hvo.setAttributeValue("infodate", new UFDate(infodate));
		//�����ռ�����
		hvo.setAttributeValue("dayfuel", new UFDouble(json.getString("dayfuel")));
		//���ý��
		hvo.setAttributeValue("amount", new UFDouble(json.getString("amount")));
		//��������������������
		hvo.setAttributeValue("otherkm", new UFDouble(json.getString("otherkm")));
		//�������ʻ
		hvo.setAttributeValue("emptytimesam", new UFBoolean(json.getBoolean("emptytimesam")));
		//�������ʻ
		hvo.setAttributeValue("emptytimespm", new UFBoolean(json.getBoolean("emptytimespm")));
		//�����ս������
		hvo.setAttributeValue("bonuspenalty", new UFDouble(json.getString("bonuspenalty")));
		//������ĩ�Ӱ����
		hvo.setAttributeValue("wkdovertime", new UFDouble(json.getString("wkdovertime")));
		//�೵ÿ����ʼ���
		hvo.setAttributeValue("def1", json.getString("def1"));
		//�೵ÿ���ֹ���
		hvo.setAttributeValue("def2", json.getString("def2"));
		AggOtherInfoVO otherInfoAggVO = new AggOtherInfoVO();
		otherInfoAggVO.setParent(hvo);
		
		try {
			InvocationInfoProxy.getInstance().setGroupId(pk_group);
			InvocationInfoProxy.getInstance().setUserId(cuserid);
		} catch (Exception e1) {
			ExceptionUtils.wrappBusinessException("ͨ�ô���error"+e1.getMessage());
		}
		//˾��+����ΨһУ��
		String checksql = "select 1 from cl_otherinfo where creator = '" + cuserid
				+ "' and substr(infodate,0,10) = '" + infodate + "'";
		Integer checkresult = 0;
		try {
			checkresult = (Integer) dao.executeQuery(checksql, new ColumnProcessor());
		} catch (DAOException e1) {
			ExceptionUtils.wrappBusinessException("���������Ϣ���Ϣʧ�ܣ�"+e1.getMessage());
		}
		if(checkresult != null && checkresult == 1) {
			ExceptionUtils.wrappBusinessException("��ǰ������������,���ѯ���޸�");
		}
		//������Ϣ���׼�ӿ�
		IOtherInfoMaintain service = (IOtherInfoMaintain) NCLocator.getInstance().lookup(IOtherInfoMaintain.class);
		try {
			//������������
			AggOtherInfoVO[] aggVos = service.insert(new AggOtherInfoVO[] {otherInfoAggVO}, null);
			//TODO: �ύ,У��
			result.put("success", true);
			result.put("result", "true");
		} catch (nc.vo.pub.BusinessException e) {
			ExceptionUtils.wrappBusinessException("���������Ϣ���Ϣʧ�ܣ�"+e.getMessage());
		}
		return result;
	}

	/**
	 * ���µ���״̬
	 * ������JSONObject json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject updateStatus(JSONObject json) throws JSONException {
		//���ڷ���ǰ̨
		JSONObject result = new JSONObject();
		// ��������
		String pk_otherinfo = json.getString("pk_otherinfo");
		// ״̬
		Integer status = json.getInt("status");
		String sql = "update cl_otherinfo set approvestatus = " + status 
				+ " where pk_otherinfo = '"+ pk_otherinfo + "'";
		
		try {
			//ɾ�����ӱ�VO����
			dao.executeUpdate(sql);
			result.put("success", true);
			result.put("result", "true");
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("���µ���״̬ʧ�ܣ�"+e.getMessage());
		}
		//����
		return result;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

}
