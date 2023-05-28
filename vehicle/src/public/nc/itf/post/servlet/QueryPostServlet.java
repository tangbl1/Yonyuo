package nc.itf.post.servlet;

import disposition.GetDatasourceName;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.comn.NetStreamContext;
import nc.bs.framework.core.service.IFwLogin;
import nc.itf.stgl.pojo.token;
import nc.itf.stgl.pojo.tokenParam;
import nc.itf.stgl.util.getToken;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import nc.vo.pub.BusinessException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



@SuppressWarnings("restriction")
@WebServlet("/hwpost")
public class QueryPostServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	BaseDAO dao = new BaseDAO();
	
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
		if (method.equals("queryPost")) {
			try {
				result = getUserPost(jsonObject);
			} catch (Exception e) {
				ExceptionUtils.wrappBusinessException("��λ��ѯ�쳣��"+e.getMessage());
			}
		}
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("GBK");
		resp.getWriter().write(result.toString());
		resp.flushBuffer();
		
	}
	
	/**
	 * ���ص�¼�û��ĸ�λ
	 * ������JSONObject jsonObject
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject getUserPost(JSONObject json) throws Exception {
		//���ص�JSONObject
		JSONObject jsonResult = new JSONObject();
		//��ų��ƺŵ�JSONArray
		JSONArray jsonArray = new JSONArray(); //���ڴ洢������Ϣ����
		//��ȡ�����code
		String code = json.getString("user_code");		
		tokenParam tp = new tokenParam();
		//��ȡtoken����
		getToken getToken1 = new getToken();
		token token1 = getToken1.getAccessToken(tp.getAppid(),tp.getScret());
		//�õ�token
		String accessToken = token1.getAccessToken();
		//�ѿռ������¼
		String url = "https://openapi.yonyoucloud.com/certified/userInfo/"+code+"?access_token="+accessToken;
		String json0="";
		try {
			json0 = getUser(url);
		} catch (BusinessException e1) {
			ExceptionUtils.wrappBusinessException(e1.getMessage());
		}
	         
	         
//		String json0 = HttpUtil.sendGet(url);
		String str =new String(json0.getBytes("ISO-8859-1"),"utf-8");
		//�ѿռ���Ա��Ϣ
		com.alibaba.fastjson.JSONObject jo = com.alibaba.fastjson.JSONObject.parseObject(str);
		//��Ա����
		String staffNo = "";
		if(jo.containsKey("data")){
			//��ֵ
			staffNo = jo.getJSONObject("data").getString("staff_no");
		}else{
			ExceptionUtils.wrappBusinessException("code���ڣ���");
		}
		
		//����˾����staffNo="200809003";
		//�����û���staffNo="201307003";
		//���Գ��ܵ�staffNo="200503001";
		//ͨ���û�����õ���ݣ�pk_psndoc������������ҵ���Ա�ĸ�λpk
		String sql = "select post.*,role.role_name from ( "
				+ "select p.postname,d.name, u.cuserid,u.pk_org,u.pk_group,d.pk_dept "
				+ "from om_post p, org_dept d,"
				+ " sm_user u where p.dr = 0 and  pk_post in (select pk_post  from bd_psnjob  where dr = 0 and  "
				+ "pk_psndoc =  (select pk_psndoc from bd_psndoc where code = "
				+ "'"+staffNo+"' and dr = 0) and pk_org = (select pk_org from bd_psndoc where dr = 0 and code = '"+staffNo+"')) and p.pk_dept "
				+ "= d.pk_dept  and pk_psndoc = (select pk_psndoc from bd_psndoc"
				+ " where code = '"+staffNo+"'and dr = 0)) post left join "
				+ "(select distinct u.cuserid,r.role_name from bd_psndoc p,sm_user u,sm_user_role ur,sm_role r "
				+ "where p.pk_psndoc = u.pk_psndoc and u.cuserid = ur.cuserid and ur.pk_role = r.pk_role "
				+ "and p.dr=0 and u.dr=0  and r.dr=0 and r.role_name = '��������Ա' and p.code = '"+staffNo
				+ "') role on post.cuserid = role.cuserid";
		
		
		//��Ÿ�λ��λ�Ͳ��ŵļ���
		List<Map<String,Object>> list = new ArrayList<>();
		try {
			//ִ��sql�õ���λ���ƺͲ������Ƶļ���
			list = (List<Map<String, Object>>) dao.executeQuery(sql,
					new MapListProcessor());
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("��λ��ѯ������"+e.getMessage()+staffNo);
		}
		//ѭ����ȡ������Ϣ���洢��json����
		for (Map<String, Object> map : list) {
				//���һ�����ݵ�json
				JSONObject jsonObject = new JSONObject();
				//��λ����
				jsonObject.put("postname", map.get("postname"));
				//��������
				jsonObject.put("name", map.get("name"));
				//��������
				jsonObject.put("pk_dept", map.get("pk_dept"));
				//�û�pk
				jsonObject.put("cuserid", map.get("cuserid"));
				//�û�������֯
				jsonObject.put("pk_org", map.get("pk_org"));
				//�û���������
				jsonObject.put("pk_group", map.get("pk_group"));
				//�û���������
				jsonObject.put("role_name", map.get("role_name")==null ? "" : map.get("role_name"));
				//����jsonArray
				jsonArray.put(jsonObject);
		}
		JSONObject result = new JSONObject();
		result.put("values", jsonArray);
		result.put("result", "true");
		return result;	
	}
	
	@SuppressWarnings({ "unchecked" })
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	public String getUser(String userurl) throws BusinessException {
		/** �����url��ַ */     
	     URL url = null;           
	        /** http���� */ 
	     HttpURLConnection httpConn = null;         
	         /**//** ������ */
	     BufferedReader in = null;
	     StringBuffer sb = new StringBuffer();
	     try{  
	      url = new URL(userurl);  
	      in = new BufferedReader( new InputStreamReader(url.openStream(),"UTF-8") );
	      String str = null; 
	      while((str = in.readLine()) != null) { 
	       sb.append( str );  
	             }  
	         } catch (Exception ex) {
	        	 ex.printStackTrace();
	        	 ExceptionUtils.wrappBusinessException(ex.getMessage());
	         } finally{ 
        try{          
         if(in!=null) {
          in.close();  
               }  
           }catch(IOException ex) {   
           }  
       }  
	     return sb.toString();
	    //return json0;
	}
}