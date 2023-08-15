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
		JSONObject result = new JSONObject();	//返回前台结果
		String errMsg = "";		//错误信息
		String datasource = "";//数据源
		try {
			datasource = new GetDatasourceName().doreadxml();
		} catch (JDOMException e) {
			throw new RuntimeException("配置文件获取数据源名称失败！");
		}
		// 必须放在首行
		InvocationInfoProxy.getInstance().setUserDataSource(datasource);	//指定本次操作数据的数据源
		req.setCharacterEncoding("utf-8");		//设置请求的编码为UTF-8
		String method = req.getParameter("method");	//获取前台传递method标识，用于区分处理方法
		String json = req.getParameter("json");	//获取前台传递过来的参数JSON对象
		
		if(StringUtils.isBlank(method))
			errMsg = "method为空，参数传递异常!";
		if(StringUtils.isBlank(json))
			errMsg = "json为空，参数传递异常!";
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(json); //String->JSONObject
		} catch (JSONException e1) {
			ExceptionUtils.wrappException(e1);
		}	
		
		// 进行NC虚拟登录
		String username = "hwapp";		//用户名
		String password = "asdqwe123";	//密码
		IFwLogin loginService = (IFwLogin) NCLocator.getInstance().lookup(IFwLogin.class);
		byte[] token = loginService.login(username, password, null);
		NetStreamContext.setToken(token);
		
		//通过用户编码查询对应cuserid,pk_group
		String sqluserid = "select cuserid,pk_group from sm_user where user_code='" + username + "'";
		Map<String,String> map = null;
		try {
			map = (Map<String,String>) dao.executeQuery(sqluserid, new MapProcessor());
			if(map==null || map.size() <= 0)
				errMsg = "查询hwapp用户失败，请联系管理员！";
		} catch (Exception e) {
			errMsg = "cuserid,pk_group查询失败！";
			ExceptionUtils.wrappBusinessException(errMsg);
		}
		
		String userid = map.get("cuserid");
		String pk_group = map.get("pk_group");
		InvocationInfoProxy.getInstance().setUserCode(username);
		InvocationInfoProxy.getInstance().setGroupId(pk_group);// 人员基本信息表
		InvocationInfoProxy.getInstance().setUserId(userid);
		if (method.equals("queryPost")) {
			try {
				result = getUserPost(jsonObject);
			} catch (Exception e) {
				ExceptionUtils.wrappBusinessException("岗位查询异常！"+e.getMessage());
			}
		}
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("GBK");
		resp.getWriter().write(result.toString());
		resp.flushBuffer();
		
	}
	
	/**
	 * 返回登录用户的岗位
	 * 参数：JSONObject jsonObject
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject getUserPost(JSONObject json) throws Exception {
		//返回的JSONObject
		JSONObject jsonResult = new JSONObject();
		//存放车牌号的JSONArray
		JSONArray jsonArray = new JSONArray(); //用于存储单据信息集合
		//获取传入的code
		String code = json.getString("user_code");		
		tokenParam tp = new tokenParam();
		//获取token方法
		getToken getToken1 = new getToken();
		token token1 = getToken1.getAccessToken(tp.getAppid(),tp.getScret());
		//得到token
		String accessToken = token1.getAccessToken();
		//友空间虚拟登录
		String url = "https://openapi.yonyoucloud.com/certified/userInfo/"+code+"?access_token="+accessToken;
		String json0="";
		try {
			json0 = getUser(url);
		} catch (BusinessException e1) {
			ExceptionUtils.wrappBusinessException(e1.getMessage());
		}
	         
	         
//		String json0 = HttpUtil.sendGet(url);
		String str =new String(json0.getBytes("ISO-8859-1"),"utf-8");
		//友空间人员信息
		com.alibaba.fastjson.JSONObject jo = com.alibaba.fastjson.JSONObject.parseObject(str);
		//人员编码
		String staffNo = "";
		if(jo.containsKey("data")){
			//赋值
			staffNo = jo.getJSONObject("data").getString("staff_no");
		}else{
			ExceptionUtils.wrappBusinessException("code过期！！");
		}
		
		//测试司机的staffNo="200809003";
		//测试用户的staffNo="201307003";
		//测试车管的staffNo="200503001";
		//通过用户编码得到身份（pk_psndoc），根据身份找到人员的岗位pk
		String sql = "select post.*,role.role_name from ( "
				+ "select p.postname,d.name, u.cuserid,u.pk_org,u.pk_group,d.pk_dept "
				+ "from om_post p, org_dept d,"
				+ " sm_user u where p.dr = 0 and  pk_post in (select pk_post  from hi_psnjob  where dr = 0 and  "
				+ "pk_psndoc =  (select pk_psndoc from bd_psndoc where code = "
				+ "'"+staffNo+"' and dr = 0) and pk_org = (select pk_org from bd_psndoc where dr = 0 and code = '"+staffNo+"') and lastflag = 'Y') and p.pk_dept "
				+ "= d.pk_dept  and pk_psndoc = (select pk_psndoc from bd_psndoc"
				+ " where code = '"+staffNo+"'and dr = 0)) post left join "
				+ "(select distinct u.cuserid,r.role_name from bd_psndoc p,sm_user u,sm_user_role ur,sm_role r "
				+ "where p.pk_psndoc = u.pk_psndoc and u.cuserid = ur.cuserid and ur.pk_role = r.pk_role "
				+ "and p.dr=0 and u.dr=0  and r.dr=0 and r.role_name = '车辆管理员' and p.code = '"+staffNo
				+ "') role on post.cuserid = role.cuserid";
		
		
		//存放岗位岗位和部门的集合
		List<Map<String,Object>> list = new ArrayList<>();
		try {
			//执行sql得到岗位名称和部门名称的集合
			list = (List<Map<String, Object>>) dao.executeQuery(sql,
					new MapListProcessor());
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("岗位查询出错！！"+e.getMessage()+staffNo);
		}
		//循环获取单据信息并存储成json对象
		for (Map<String, Object> map : list) {
				//存放一条数据的json
				JSONObject jsonObject = new JSONObject();
				//岗位名称
				jsonObject.put("postname", map.get("postname"));
				//部门名称
				jsonObject.put("name", map.get("name"));
				//部门主键
				jsonObject.put("pk_dept", map.get("pk_dept"));
				//用户pk
				jsonObject.put("cuserid", map.get("cuserid"));
				//用户所属组织
				jsonObject.put("pk_org", map.get("pk_org"));
				//用户所属集团
				jsonObject.put("pk_group", map.get("pk_group"));
				//用户所属集团
				jsonObject.put("role_name", map.get("role_name")==null ? "" : map.get("role_name"));
				//放入jsonArray
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
		/** 网络的url地址 */     
	     URL url = null;           
	        /** http连接 */ 
	     HttpURLConnection httpConn = null;         
	         /**//** 输入流 */
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