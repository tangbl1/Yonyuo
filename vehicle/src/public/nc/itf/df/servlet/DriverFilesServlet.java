package nc.itf.df.servlet;

import disposition.GetDatasourceName;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.comn.NetStreamContext;
import nc.bs.framework.core.service.IFwLogin;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.itf.vehicle.IDriverFilesMaintain;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.vehicle.driverfiles.AggDriverFiles;
import nc.vo.vehicle.driverfiles.DriverFiles;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@SuppressWarnings("unused")
@WebServlet("/hwdf")
public class DriverFilesServlet extends HttpServlet {

	BaseDAO dao = new BaseDAO();
	private static final long serialVersionUID = 1L;
	
	/**
	 * ����������Ϣ˾��ҳ��servlet
	 * ���ڴ���˾��ҳ������
	 * @author 
	 * @date 2019-11-20
	 * @param 
	 * 	req ǰ̨ҳ������
	 * 	resp ������Ϣ
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject result = new JSONObject();	 //����ǰ̨���
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
				result = addDriver(jsonObject);
			} else if ("query".equals(method)) {
				result = queryDrivers(jsonObject);
			} else if ("detail".equals(method)){
				result = queryDetail(jsonObject);
			} else if ("dele".equals(method)){
				result = deleDriver(jsonObject);
			} else if ("save".equals(method)){
				result = saveDriver(jsonObject);
			} else if ("query_driver".equals(method)){
				result = queryDriver(jsonObject);
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
	 * ˾��������ѯ
	 * @param JSONObject jsonObject
	 * @return JSONObject
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject queryDriver(JSONObject jsonObject) throws DAOException, JSONException {
		JSONArray jsonArr = new JSONArray(); //���ڴ洢������Ϣ����
		JSONObject result = new JSONObject();
		JSONArray orgArray = new JSONArray();
		//��ѯ��λ��˾�����û����������ֵ�sql
		/*String sql= " select sm_user.cuserid ,sm_user. user_name  "
				+ " from sm_user"
				+ " left join bd_psnjob on bd_psnjob.pk_psndoc=sm_user.pk_psndoc"
				+ " left join om_post   on om_post.pk_post=bd_psnjob.pk_post  "
				+ " where om_post.postname='˾��'";*/
		String sql = "select user_name, cuserid from sm_user where dr = 0 "
				+ "and pk_psndoc in (select pk_psndoc from bd_psnjob where dr = 0 "
				+ "and pk_post in (select pk_post from om_post where postname = '˾��') and dr = 0)";
		//��ѯdr = 0��������Դ��֯��sql(����״̬����2��Ϊ�����õ�),���ݱ�������
		String sql_org="select name, pk_hrorg  from org_hrorg where dr=0 and enablestate =2 order by code";
		//��Ų�ѯ����˾����Ϣ�ļ���
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
		List<Map<String,String>> list_org = (List<Map<String, String>>) dao.executeQuery(sql_org, new  MapListProcessor());
		//ѭ����ȡ������Ϣ���洢��json����
		for (Map<String, String> map : list) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("cuserid", map.get("cuserid"));//�û�����
			jsonObj.put("user_name", map.get("user_name"));//����	
			jsonArr.put(jsonObj);
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
		result.put("values", jsonArr);
		result.put("orgs", orgArray);
		result.put("result", "true");
		return result;	
	}
	
	/**
	 * ˾��������� 
	 * @param JSONObject jsonObject
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject addDriver(JSONObject jsonObject) throws JSONException {
		JSONArray jsonArr = new JSONArray(); //���ڴ洢������Ϣ����
		JSONObject result = new JSONObject();
		JSONObject userjsonObject = jsonObject.getJSONObject("userjson"); // ��¼��
		//�û�����
		String cuserid = userjsonObject.getString("cuserid");;
		//�û���֯
		String pk_org = jsonObject.getString("pk_org");;
		//�û�����
		String pk_group = userjsonObject.getString("pk_group");
		
		String dname = jsonObject.getString("driver"); // ��Ա����
		String userid = jsonObject.getString("cuserid"); // ��Ա������Ϊ�Զ�����1
		String dphone = jsonObject.getString("tel"); // �绰
		String dage = jsonObject.getString("driving_age"); // ����
		String iexpiredate = jsonObject.getString("iexpiredate");// ��ʻ֤��Ч��
		String dvtype = jsonObject.getString("dtype");// ׼�ݳ���
		String dstate = jsonObject.getString("statue"); // ״̬
		String starlevel = jsonObject.getString("star"); // �Ǽ�
		String image = jsonObject.getString("image"); // ͼƬ
		String fix_road_num = jsonObject.getString("fix_road_num"); // �̶���·��
		String driver_addr = jsonObject.getString("driver_addr"); // ˾����ַ
		UFDate date = null;
		if (StringUtils.isNotBlank(iexpiredate)) {
			iexpiredate = iexpiredate.replace("-", "/");
			//�������ڸ�ʽ
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy/MM/dd");
			try {
				date = new UFDate(timeFormat.parse(iexpiredate));// ��ʻ֤��Ч��
			} catch (java.text.ParseException e) {
				ExceptionUtils.wrappBusinessException("���ڸ�ʽת��ʧ�ܣ�"+e.getMessage());
			}
		}
		//�½�aggVO
		AggDriverFiles aggvo = new AggDriverFiles();
		//����˾����������ֵ
		DriverFiles dfvo = new DriverFiles();
		dfvo.setAttributeValue("dname", dname);// ����
		dfvo.setAttributeValue("vdef0", userid);// ��Ա����
		dfvo.setAttributeValue("dphone", dphone);// �绰
		dfvo.setAttributeValue("dage", dage);// ����
		dfvo.setAttributeValue("dexpiredate", date);// ��ʻ֤��Ч��
		dfvo.setAttributeValue("dvtype", dvtype);// ׼�ݳ���
		dfvo.setAttributeValue("dstate", dstate);// ״̬
		dfvo.setAttributeValue("starlevel", starlevel);// �Ǽ�
		dfvo.setAttributeValue("dphoto", image);// ͼƬ
		dfvo.setAttributeValue("pk_group", pk_group);//����
		dfvo.setAttributeValue("pk_org", pk_org);//��֯
		dfvo.setAttributeValue("cuserId", cuserid);
		dfvo.setAttributeValue("fix_road_num", fix_road_num);// �̶���·��
		dfvo.setAttributeValue("driver_addr", driver_addr);// ˾����ַ
		aggvo.setParent(dfvo);
		// ����GroupId
		InvocationInfoProxy.getInstance().setGroupId(
				dfvo.getAttributeValue("pk_group").toString());
		//���ô�����
		InvocationInfoProxy.getInstance().setUserId(
				dfvo.getAttributeValue("cuserId").toString());
		//�½�AggVo���鲢��ֵ
		AggDriverFiles[] aggvos = { aggvo };
		//˾���ı�׼�ӿ�
		IDriverFilesMaintain iDriverFilesMaintain = (IDriverFilesMaintain) NCLocator
				.getInstance().lookup(IDriverFilesMaintain.class);
		
		try {
			AggDriverFiles[] aggdfVO = iDriverFilesMaintain.insert(aggvos, null);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException("�����������ʧ�ܣ�"+e.getMessage());
		}
		result.put("result", "true");
		return result;	
	}

	/**
	 * ˾��������ԃ
	 * @param JSONObject jsonObject
	 * @return JSONObject
	 * @throws DAOException 
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
	 */
	private JSONObject queryDrivers(JSONObject json) throws DAOException, UnsupportedEncodingException, JSONException {
		boolean ISDRIVER = false;	//�Ƿ���˾����ʶ��
		JSONObject userjsonObject = json.getJSONObject("userjson"); // ��¼��
		String postname = userjsonObject.getString("postname"); //��ǰ��¼�˸�λ
		String cuserid = userjsonObject.getString("cuserid"); //��ǰ��¼������
		JSONArray jsonArr = new JSONArray(); //���ڴ洢������Ϣ����
		JSONObject result = new JSONObject();
		Integer maxcount = json.getInt("maxcount");//�������
		Integer mincount = json.getInt("mincount");//��С����
		if("˾��".equals(postname)){
			ISDRIVER = true;
		}
		
		List<Map<String, Object>> mapArrJSON = new ArrayList<Map<String, Object>>();
		//��ѯ����dr=0��˾����������ҳ��ѯ��
		String sql ="SELECT *from (SELECT ROWNUM AS rowno1, t.*from "
				+ "(select * from cl_driver where dr = 0 "
				+((ISDRIVER) ? "and vdef1='"+cuserid+"'" : "")
				+ "order by ts desc)"
				+ " t where ROWNUM < ="+maxcount+") c WHERE c.rowno1 > "+mincount;
		//ִ��sql����ֵ��list����
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
		//�Լ��Ͻ��н��б���
		for (Map<String, String> map : list) {
			JSONObject jsonObj = new JSONObject();			
			jsonObj.put("pk_driver", map.get("pk_driver"));//˾�����I
			jsonObj.put("dname", map.get("dname"));//����
			jsonObj.put("dphone", map.get("dphone"));//�绰
			jsonObj.put("dage", map.get("dage"));//����
			jsonObj.put("dexpiredate",map.get("dexpiredate").toString().substring(0, 10));// ��ʻ֤��Ч�ڣ������գ�
			jsonObj.put("dvtype", map.get("dvtype"));// ׼�ݳ���
			String dstate = map.get("dstate");
			jsonObj.put("dstate", dstate);// ״̬
			jsonObj.put("starlevel", map.get("starlevel"));// �Ǽ�
			String dstateName = "";
			if("1".equals(dstate)){
				dstateName = "ֵ��";
			}else if("2".equals(dstate)){
				dstateName = "��Ϣ";
			}else if("3".equals(dstate)){
				dstateName = "�ڸ�";
			}
			jsonObj.put("dstateName", dstateName);// �Ǽ�
			Object img = map.get("dphoto");
			byte[] byte_img = (byte[]) img;
			String imgDatas = new String(byte_img,"UTF-8");
			jsonObj.put("dphoto", imgDatas);// ͼƬ
			jsonObj.put("fix_road_num", map.get("fix_road_num")==null?"":map.get("fix_road_num"));//�̶���·��
			jsonObj.put("driver_addr", map.get("driver_addr")==null?"":map.get("driver_addr"));//˾����ַ
			jsonArr.put(jsonObj);
		}
		result.put("values", jsonArr);
		result.put("result", "true");
		return result;
	}

	/**
	 * ˾�����������ѯ
	 * @param JSONObject jsonObject
	 * @return JSONObject
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
	 */
	private JSONObject queryDetail(JSONObject json) throws UnsupportedEncodingException, JSONException {
		JSONArray jsonArr = new JSONArray(); //���ڴ洢������Ϣ����
		JSONObject result = new JSONObject();
		String pk_driver = json.getString("pk_driver"); // ����
		String sql = "select h.*,o.name orgname from cl_driver h left join org_hrorg o on h.pk_org=o.pk_hrorg and o.dr=0 where h.dr = '0' and h.pk_driver = '"
				+ pk_driver + "'";
		Map<String,String> map = new HashMap<String,String>();
		try {
			map = (Map<String,String>) dao.executeQuery(sql, new MapProcessor());
		} catch (DAOException e) {
			ExceptionUtils.wrappBusinessException("˾�������ѯʧ�ܣ�"+e.getMessage());
		}
		JSONObject jsonObj = new JSONObject();	
		jsonObj.put("dphone", map.get("dphone"));//�绰
		jsonObj.put("dage", map.get("dage"));//����
		jsonObj.put("dexpiredate", map.get("dexpiredate").toString().substring(0, 10));// ��ʻ֤��Ч��
		jsonObj.put("dvtype", map.get("dvtype"));// ׼�ݳ���
		jsonObj.put("dstate", map.get("dstate"));// ״̬
		//˾����Ϊjson����
		JSONObject jsonobj_driver = new JSONObject();
		jsonobj_driver.put("cuserid", map.get("vdef1"));
		jsonobj_driver.put("user_name", map.get("dname"));	
		jsonObj.put("pk_driver", jsonobj_driver.toString());//�����ŵĶ���
		
		jsonObj.put("starlevel", map.get("starlevel"));// �Ǽ�
		Object img = map.get("dphoto");
		byte[] byte_img = (byte[]) img;
		String imgDatas = new String(byte_img,"UTF-8");
		jsonObj.put("dphoto", imgDatas);// ͼƬ
		jsonObj.put("fix_road_num", map.get("fix_road_num")==null?"":map.get("fix_road_num"));//�̶���·��
		jsonObj.put("driver_addr", map.get("driver_addr")==null?"":map.get("driver_addr"));//˾����ַ
		jsonObj.put("pk_org", map.get("pk_org")==null?"":map.get("pk_org"));//��֯
		jsonObj.put("orgname", map.get("orgname")==null?"":map.get("orgname"));//��֯��
		result.put("values", jsonObj);
		result.put("result", "true");
		return result;
	}

	/**
	 * ˾�������h��
	 * @param JSONObject jsonObject
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject deleDriver(JSONObject json) throws JSONException {
		JSONObject result = new JSONObject();
		//ȡ�������˾������
		String pk_driver = json.getString("pk_driver");//���I
		//���������ݵ�dr����1��ʵ��ɾ����
		String sql = "update cl_driver set dr = 1 where pk_driver ='"
				+ pk_driver + "'";
		try {
			//ִ��ɾ������
			dao.executeUpdate(sql);
			//��ɾ���ɹ�����Ϣ����rtnMap
			result.put("result", "true");
		} catch (DAOException e) {
			ExceptionUtils.wrappBusinessException("ɾ��˾��������Ϣʧ�ܣ�"+e.getMessage());
		}
		return result;
	}

	/**
	 * ˾�������޸ĺ�ı���
	 * @param JSONObject jsonObject
	 * @return JSONObject
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject saveDriver(JSONObject json) throws DAOException, JSONException {
		JSONObject result = new JSONObject();
		String pk_driver = json.getString("pk_driver");//���I
		String dname = json.getString("driver"); // ����
		String cuserid = json.getString("cuserid"); // ��Ա������Ϊ�Զ�����1
		String dphone = json.getString("tel"); // �绰
		String dage = json.getString("driving_age"); // ����
		String iexpiredate = json.getString("iexpiredate");// ��ʻ֤��Ч��
		UFDate date = null;
		if (StringUtils.isNotBlank(iexpiredate)) {
			iexpiredate = iexpiredate.replace("-", "/");
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy/MM/dd");
			try {
				date = new UFDate(timeFormat.parse(iexpiredate));// ��ʻ֤��Ч��
			} catch (java.text.ParseException e1) {
				ExceptionUtils.wrappBusinessException("���ڸ�ʽת��ʧ�ܣ�");
			}
		}
		String dvtype = json.getString("dtype");// ׼�ݳ���
		String dstate = json.getString("statue"); // ״̬
		String starlevel = json.getString("star"); // �Ǽ�
		String image =(String) json.getString("image"); // ͼƬ
		String fix_road_num = json.getString("fix_road_num"); // �̶���·��
		String driver_addr = json.getString("driver_addr"); // ˾����ַ
		String pk_org = json.getString("pk_org"); // ��֯
		//�ɵ�vo
		String sql_list = " select * from cl_driver where dr = 0 and pk_driver='"+pk_driver+"'";
		List<DriverFiles> voList = (ArrayList<DriverFiles>) dao.executeQuery(sql_list, new BeanListProcessor(DriverFiles.class));

		
		DriverFiles newvo=voList.get(0);
		//�µ�vo
		newvo.setAttributeValue("dname", dname);// ����
		newvo.setAttributeValue("dphone", dphone);// �绰
		newvo.setAttributeValue("dage", dage);// ����
		newvo.setAttributeValue("dexpiredate", date);// ��ʻ֤��Ч��
		newvo.setAttributeValue("dvtype", dvtype);// ׼�ݳ���
		newvo.setAttributeValue("dstate", dstate);// ״̬
		newvo.setAttributeValue("starlevel", starlevel);// �Ǽ�
		newvo.setAttributeValue("dphoto", image);// ͼƬ
		newvo.setAttributeValue("vdef0", cuserid);
		newvo.setAttributeValue("fix_road_num", fix_road_num);// �̶���·��
		newvo.setAttributeValue("driver_addr", driver_addr);// ˾����ַ
		newvo.setAttributeValue("pk_org", pk_org);// ��֯
		newvo.setTs(new UFDateTime());
		VOUpdate vo=new VOUpdate();
		ISuperVO[] vos={newvo};
		vo.update(vos);
		result.put("result", "true");
		return result;
	}
	
	@SuppressWarnings({ "unchecked" })
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
}
