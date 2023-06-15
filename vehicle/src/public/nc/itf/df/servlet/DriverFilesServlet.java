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
	 * 车辆基本信息司机页面servlet
	 * 用于处理司机页面请求
	 * @author 
	 * @date 2019-11-20
	 * @param 
	 * 	req 前台页面请求
	 * 	resp 返回信息
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject result = new JSONObject();	 //返回前台结果
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
		} catch (Exception e1) {
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
		
		
		try {
			//查询订单
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
				ExceptionUtils.wrappBusinessException("失败的result赋值失败!"+e.getMessage());
			}	
		}
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().write(result.toString());
		resp.flushBuffer();
	}
	
	/**
	 * 司机档案查询
	 * @param JSONObject jsonObject
	 * @return JSONObject
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject queryDriver(JSONObject jsonObject) throws DAOException, JSONException {
		JSONArray jsonArr = new JSONArray(); //用于存储单据信息集合
		JSONObject result = new JSONObject();
		JSONArray orgArray = new JSONArray();
		//查询岗位是司机的用户主键和名字的sql
		/*String sql= " select sm_user.cuserid ,sm_user. user_name  "
				+ " from sm_user"
				+ " left join bd_psnjob on bd_psnjob.pk_psndoc=sm_user.pk_psndoc"
				+ " left join om_post   on om_post.pk_post=bd_psnjob.pk_post  "
				+ " where om_post.postname='司机'";*/
		String sql = "select user_name, cuserid from sm_user where dr = 0 "
				+ "and pk_psndoc in (select pk_psndoc from bd_psnjob where dr = 0 "
				+ "and pk_post in (select pk_post from om_post where postname = '司机') and dr = 0)";
		//查询dr = 0的人力资源组织的sql(启用状态等于2即为已启用的),根据编码排序
		String sql_org="select name, pk_hrorg  from org_hrorg where dr=0 and enablestate =2 order by code";
		//存放查询到的司机信息的集合
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
		List<Map<String,String>> list_org = (List<Map<String, String>>) dao.executeQuery(sql_org, new  MapListProcessor());
		//循环获取单据信息并存储成json对象
		for (Map<String, String> map : list) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("cuserid", map.get("cuserid"));//用户主键
			jsonObj.put("user_name", map.get("user_name"));//姓名	
			jsonArr.put(jsonObj);
		}
		//遍历人力资源组织结果集
		for (Map<String,String> map : list_org) {
			JSONObject orgJson = new JSONObject();
			//组织名称
			orgJson.put("name", map.get("name"));
			//人力资源组织主键
			orgJson.put("pk_hrorg", map.get("pk_hrorg"));
			orgArray.put(orgJson); 
		}
		result.put("values", jsonArr);
		result.put("orgs", orgArray);
		result.put("result", "true");
		return result;	
	}
	
	/**
	 * 司机档案添加 
	 * @param JSONObject jsonObject
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject addDriver(JSONObject jsonObject) throws JSONException {
		JSONArray jsonArr = new JSONArray(); //用于存储单据信息集合
		JSONObject result = new JSONObject();
		JSONObject userjsonObject = jsonObject.getJSONObject("userjson"); // 登录人
		//用户主键
		String cuserid = userjsonObject.getString("cuserid");;
		//用户组织
		String pk_org = jsonObject.getString("pk_org");;
		//用户集团
		String pk_group = userjsonObject.getString("pk_group");
		
		String dname = jsonObject.getString("driver"); // 人员姓名
		String userid = jsonObject.getString("cuserid"); // 人员主键存为自定义项1
		String dphone = jsonObject.getString("tel"); // 电话
		String dage = jsonObject.getString("driving_age"); // 驾龄
		String iexpiredate = jsonObject.getString("iexpiredate");// 驾驶证有效期
		String dvtype = jsonObject.getString("dtype");// 准驾车型
		String dstate = jsonObject.getString("statue"); // 状态
		String starlevel = jsonObject.getString("star"); // 星级
		String image = jsonObject.getString("image"); // 图片
		String fix_road_num = jsonObject.getString("fix_road_num"); // 固定公路数
		String driver_addr = jsonObject.getString("driver_addr"); // 司机地址
		UFDate date = null;
		if (StringUtils.isNotBlank(iexpiredate)) {
			iexpiredate = iexpiredate.replace("-", "/");
			//设置日期格式
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy/MM/dd");
			try {
				date = new UFDate(timeFormat.parse(iexpiredate));// 驾驶证有效期
			} catch (java.text.ParseException e) {
				ExceptionUtils.wrappBusinessException("日期格式转换失败！"+e.getMessage());
			}
		}
		//新建aggVO
		AggDriverFiles aggvo = new AggDriverFiles();
		//新增司机主表，并赋值
		DriverFiles dfvo = new DriverFiles();
		dfvo.setAttributeValue("dname", dname);// 姓名
		dfvo.setAttributeValue("vdef0", userid);// 人员主键
		dfvo.setAttributeValue("dphone", dphone);// 电话
		dfvo.setAttributeValue("dage", dage);// 驾龄
		dfvo.setAttributeValue("dexpiredate", date);// 驾驶证有效期
		dfvo.setAttributeValue("dvtype", dvtype);// 准驾车型
		dfvo.setAttributeValue("dstate", dstate);// 状态
		dfvo.setAttributeValue("starlevel", starlevel);// 星级
		dfvo.setAttributeValue("dphoto", image);// 图片
		dfvo.setAttributeValue("pk_group", pk_group);//集团
		dfvo.setAttributeValue("pk_org", pk_org);//组织
		dfvo.setAttributeValue("cuserId", cuserid);
		dfvo.setAttributeValue("fix_road_num", fix_road_num);// 固定公路数
		dfvo.setAttributeValue("driver_addr", driver_addr);// 司机地址
		aggvo.setParent(dfvo);
		// 设置GroupId
		InvocationInfoProxy.getInstance().setGroupId(
				dfvo.getAttributeValue("pk_group").toString());
		//设置创建人
		InvocationInfoProxy.getInstance().setUserId(
				dfvo.getAttributeValue("cuserId").toString());
		//新建AggVo数组并赋值
		AggDriverFiles[] aggvos = { aggvo };
		//司机的标准接口
		IDriverFilesMaintain iDriverFilesMaintain = (IDriverFilesMaintain) NCLocator
				.getInstance().lookup(IDriverFilesMaintain.class);
		
		try {
			AggDriverFiles[] aggdfVO = iDriverFilesMaintain.insert(aggvos, null);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException("车辆档案添加失败！"+e.getMessage());
		}
		result.put("result", "true");
		return result;	
	}

	/**
	 * 司机档案查詢
	 * @param JSONObject jsonObject
	 * @return JSONObject
	 * @throws DAOException 
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
	 */
	private JSONObject queryDrivers(JSONObject json) throws DAOException, UnsupportedEncodingException, JSONException {
		boolean ISDRIVER = false;	//是否是司机标识符
		JSONObject userjsonObject = json.getJSONObject("userjson"); // 登录人
		String postname = userjsonObject.getString("postname"); //当前登录人岗位
		String cuserid = userjsonObject.getString("cuserid"); //当前登录人主键
		JSONArray jsonArr = new JSONArray(); //用于存储单据信息集合
		JSONObject result = new JSONObject();
		Integer maxcount = json.getInt("maxcount");//最大数量
		Integer mincount = json.getInt("mincount");//最小数量
		if("司机".equals(postname)){
			ISDRIVER = true;
		}
		
		List<Map<String, Object>> mapArrJSON = new ArrayList<Map<String, Object>>();
		//查询所有dr=0的司机档案（分页查询）
		String sql ="SELECT *from (SELECT ROWNUM AS rowno1, t.*from "
				+ "(select * from cl_driver where dr = 0 "
				+((ISDRIVER) ? "and vdef1='"+cuserid+"'" : "")
				+ "order by ts desc)"
				+ " t where ROWNUM < ="+maxcount+") c WHERE c.rowno1 > "+mincount;
		//执行sql并赋值给list集合
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
		//对集合进行进行遍历
		for (Map<String, String> map : list) {
			JSONObject jsonObj = new JSONObject();			
			jsonObj.put("pk_driver", map.get("pk_driver"));//司机主鍵
			jsonObj.put("dname", map.get("dname"));//姓名
			jsonObj.put("dphone", map.get("dphone"));//电话
			jsonObj.put("dage", map.get("dage"));//驾龄
			jsonObj.put("dexpiredate",map.get("dexpiredate").toString().substring(0, 10));// 驾驶证有效期（年月日）
			jsonObj.put("dvtype", map.get("dvtype"));// 准驾车型
			String dstate = map.get("dstate");
			jsonObj.put("dstate", dstate);// 状态
			jsonObj.put("starlevel", map.get("starlevel"));// 星级
			String dstateName = "";
			if("1".equals(dstate)){
				dstateName = "值班";
			}else if("2".equals(dstate)){
				dstateName = "休息";
			}else if("3".equals(dstate)){
				dstateName = "在岗";
			}
			jsonObj.put("dstateName", dstateName);// 星级
			Object img = map.get("dphoto");
			byte[] byte_img = (byte[]) img;
			String imgDatas = new String(byte_img,"UTF-8");
			jsonObj.put("dphoto", imgDatas);// 图片
			jsonObj.put("fix_road_num", map.get("fix_road_num")==null?"":map.get("fix_road_num"));//固定公路数
			jsonObj.put("driver_addr", map.get("driver_addr")==null?"":map.get("driver_addr"));//司机地址
			jsonArr.put(jsonObj);
		}
		result.put("values", jsonArr);
		result.put("result", "true");
		return result;
	}

	/**
	 * 司机档案详情查询
	 * @param JSONObject jsonObject
	 * @return JSONObject
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
	 */
	private JSONObject queryDetail(JSONObject json) throws UnsupportedEncodingException, JSONException {
		JSONArray jsonArr = new JSONArray(); //用于存储单据信息集合
		JSONObject result = new JSONObject();
		String pk_driver = json.getString("pk_driver"); // 主键
		String sql = "select h.*,o.name orgname from cl_driver h left join org_hrorg o on h.pk_org=o.pk_hrorg and o.dr=0 where h.dr = '0' and h.pk_driver = '"
				+ pk_driver + "'";
		Map<String,String> map = new HashMap<String,String>();
		try {
			map = (Map<String,String>) dao.executeQuery(sql, new MapProcessor());
		} catch (DAOException e) {
			ExceptionUtils.wrappBusinessException("司机详情查询失败！"+e.getMessage());
		}
		JSONObject jsonObj = new JSONObject();	
		jsonObj.put("dphone", map.get("dphone"));//电话
		jsonObj.put("dage", map.get("dage"));//驾龄
		jsonObj.put("dexpiredate", map.get("dexpiredate").toString().substring(0, 10));// 驾驶证有效期
		jsonObj.put("dvtype", map.get("dvtype"));// 准驾车型
		jsonObj.put("dstate", map.get("dstate"));// 状态
		//司机存为json对象
		JSONObject jsonobj_driver = new JSONObject();
		jsonobj_driver.put("cuserid", map.get("vdef1"));
		jsonobj_driver.put("user_name", map.get("dname"));	
		jsonObj.put("pk_driver", jsonobj_driver.toString());//车辆号的对象
		
		jsonObj.put("starlevel", map.get("starlevel"));// 星级
		Object img = map.get("dphoto");
		byte[] byte_img = (byte[]) img;
		String imgDatas = new String(byte_img,"UTF-8");
		jsonObj.put("dphoto", imgDatas);// 图片
		jsonObj.put("fix_road_num", map.get("fix_road_num")==null?"":map.get("fix_road_num"));//固定公路数
		jsonObj.put("driver_addr", map.get("driver_addr")==null?"":map.get("driver_addr"));//司机地址
		jsonObj.put("pk_org", map.get("pk_org")==null?"":map.get("pk_org"));//组织
		jsonObj.put("orgname", map.get("orgname")==null?"":map.get("orgname"));//组织名
		result.put("values", jsonObj);
		result.put("result", "true");
		return result;
	}

	/**
	 * 司机档案刪除
	 * @param JSONObject jsonObject
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject deleDriver(JSONObject json) throws JSONException {
		JSONObject result = new JSONObject();
		//取出传入的司机主键
		String pk_driver = json.getString("pk_driver");//主鍵
		//将该条数据的dr改正1（实现删除）
		String sql = "update cl_driver set dr = 1 where pk_driver ='"
				+ pk_driver + "'";
		try {
			//执行删除方法
			dao.executeUpdate(sql);
			//将删除成功的信息放入rtnMap
			result.put("result", "true");
		} catch (DAOException e) {
			ExceptionUtils.wrappBusinessException("删除司机档案信息失败！"+e.getMessage());
		}
		return result;
	}

	/**
	 * 司机档案修改后的保存
	 * @param JSONObject jsonObject
	 * @return JSONObject
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject saveDriver(JSONObject json) throws DAOException, JSONException {
		JSONObject result = new JSONObject();
		String pk_driver = json.getString("pk_driver");//主鍵
		String dname = json.getString("driver"); // 姓名
		String cuserid = json.getString("cuserid"); // 人员主键存为自定义项1
		String dphone = json.getString("tel"); // 电话
		String dage = json.getString("driving_age"); // 驾龄
		String iexpiredate = json.getString("iexpiredate");// 驾驶证有效期
		UFDate date = null;
		if (StringUtils.isNotBlank(iexpiredate)) {
			iexpiredate = iexpiredate.replace("-", "/");
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy/MM/dd");
			try {
				date = new UFDate(timeFormat.parse(iexpiredate));// 驾驶证有效期
			} catch (java.text.ParseException e1) {
				ExceptionUtils.wrappBusinessException("日期格式转换失败！");
			}
		}
		String dvtype = json.getString("dtype");// 准驾车型
		String dstate = json.getString("statue"); // 状态
		String starlevel = json.getString("star"); // 星级
		String image =(String) json.getString("image"); // 图片
		String fix_road_num = json.getString("fix_road_num"); // 固定公路数
		String driver_addr = json.getString("driver_addr"); // 司机地址
		String pk_org = json.getString("pk_org"); // 组织
		//旧的vo
		String sql_list = " select * from cl_driver where dr = 0 and pk_driver='"+pk_driver+"'";
		List<DriverFiles> voList = (ArrayList<DriverFiles>) dao.executeQuery(sql_list, new BeanListProcessor(DriverFiles.class));

		
		DriverFiles newvo=voList.get(0);
		//新的vo
		newvo.setAttributeValue("dname", dname);// 姓名
		newvo.setAttributeValue("dphone", dphone);// 电话
		newvo.setAttributeValue("dage", dage);// 驾龄
		newvo.setAttributeValue("dexpiredate", date);// 驾驶证有效期
		newvo.setAttributeValue("dvtype", dvtype);// 准驾车型
		newvo.setAttributeValue("dstate", dstate);// 状态
		newvo.setAttributeValue("starlevel", starlevel);// 星级
		newvo.setAttributeValue("dphoto", image);// 图片
		newvo.setAttributeValue("vdef0", cuserid);
		newvo.setAttributeValue("fix_road_num", fix_road_num);// 固定公路数
		newvo.setAttributeValue("driver_addr", driver_addr);// 司机地址
		newvo.setAttributeValue("pk_org", pk_org);// 组织
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
