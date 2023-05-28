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
	 * 车辆档案页面servlet
	 * 用于处理车辆档案页面请求
	 * @author
	 * @date 2019-11-21
	 * @param 
	 * 	req 前台页面请求
	 * 	resp 返回信息
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
		String method = req.getParameter("param");	//获取前台传递method标识，用于区分处理方法
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
				ExceptionUtils.wrappBusinessException("失败的result赋值失败!"+e.getMessage());
			}
		}
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().write(result.toString());
		resp.flushBuffer();
	}
		
		
	
	/**
	 * 修改车辆档案
	 * 参数：JSONObject json
	 * @return JSONObject 
	 * @throws JSONException 
	 */
	private JSONObject updateVehicle(JSONObject json) throws JSONException {
		JSONObject result = new JSONObject();
		//单据号
		String billno = json.getString("billno");
		//根据单据号获取vo集合
		String sql_list = " select * from cl_vehicle where dr = 0 and billno='"+billno+"'";
		
		List<VehicleMessageVO> voList = null;
		try {
			voList = (ArrayList<VehicleMessageVO>) dao.executeQuery(sql_list, new BeanListProcessor(VehicleMessageVO.class));
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("车辆档案VO获取出错!"+e.getMessage());
		}
		//取出集合中的第一个对象（只有一个）
		VehicleMessageVO vehiclevo = voList.get(0);
		//司机
		String driver = json.getString("driver");
		//司机主键
		String pk_driver = json.getString("pk_driver");
		//车牌号
		String vehicleno = json.getString("vehicleno");
		//司机电话
		String dphone = json.getString("dphone");
		//车辆类型
		String vtype = json.getString("vtype");
		//车辆状态
		String vstate = json.getString("vstate");
		//载客数量
		String passengernum = json.getString("passengernum");
		//车辆性质
		String vcharacter = json.getString("vcharacter");
		//单位
		String unit = json.getString("unit");
		//部门
		String dept = json.getString("dept");
		//图片
		String image = json.getString("image");
		//司机
		vehiclevo.setDriver(driver);
		//司机主键
		vehiclevo.setPk_driver(pk_driver);
		//车牌号
		vehiclevo.setVehicleno(vehicleno);
		//司机电话
		vehiclevo.setDphone(dphone);
		//车辆类型
		vehiclevo.setVtype(vtype);
		//载客数量
		vehiclevo.setPassengernum(Integer.parseInt(passengernum));
		//车辆性质
		vehiclevo.setVcharacter(vcharacter);
		//单位
		vehiclevo.setUnit(unit);
		//部门
		vehiclevo.setDept(dept);
		//状态
		vehiclevo.setVstate(vstate);
		//图片
		vehiclevo.setVphoto(image);
		VOUpdate vo=new VOUpdate();
		ISuperVO[] vos={vehiclevo};
		vo.update(vos);
		result.put("result", "true");
		return result;
	}
	
	/**
	 * 删除车辆档案
	 * @param json json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject DelVehicle(JSONObject json) throws JSONException {
		JSONObject result = new JSONObject();
		//单据号
		String billno = json.getString("billno");
		//删除车辆基本信息（逻辑删除）
		String del_sql = "update cl_vehicle set dr = '1' where billno = '"+billno+"'";
		try {
			//执行sql
			dao.executeUpdate(del_sql);
			result.put("result", "true");
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("车辆基本信息删除失败！"+e.getMessage());
		}
		return result;
	}
	
	/**
	 * 查询车辆档案详情
	 * @param resp json
	 * @return 
	 * @throws Exception 
	 */
	private JSONObject queryDetail(HttpServletResponse resp,JSONObject json) throws Exception {
		JSONObject result = new JSONObject();
		//单据号
		String billno = json.getString("billno");
		//查询车辆性情信息
		String detail_sql = "select cl_vehicle.*,org_hrorg.pk_hrorg,org_hrorg.name orgname,org_dept.pk_dept,org_dept.name deptname from cl_vehicle "
				+ " left join org_hrorg on org_hrorg.pk_hrorg=cl_vehicle.unit and org_hrorg.dr=0"
				+ " left join org_dept on org_dept.pk_dept=cl_vehicle.dept and org_dept.dr=0"
				+ " where cl_vehicle.dr = '0' and billno = '"+billno+"'";
		//执行sql并赋值
		List<SuperVO> list = (List<SuperVO>) DBCacheFacade.runQuery(detail_sql, new BeanListProcessor(VehicleMessageVO.class));
		JSONArray jsonArr = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		//单据号
		jsonObj.put("billno", list.get(0).getAttributeValue("billno"));
		//司机
		jsonObj.put("driver", list.get(0).getAttributeValue("driver"));
		//车牌号
		jsonObj.put("vehicleno", list.get(0).getAttributeValue("vehicleno"));
		//司机电话
		jsonObj.put("dphone", list.get(0).getAttributeValue("dphone"));
		//车辆类型
		jsonObj.put("vtype", list.get(0).getAttributeValue("vtype"));
		//载客数量
		jsonObj.put("number", list.get(0).getAttributeValue("passengernum"));
		//车辆性质
		jsonObj.put("vcharacter", list.get(0).getAttributeValue("vcharacter"));
		//单位
		jsonObj.put("unit", list.get(0).getAttributeValue("unit"));
		//部门
		jsonObj.put("dept", list.get(0).getAttributeValue("dept"));
		//状态
		jsonObj.put("vstate", list.get(0).getAttributeValue("vstate"));
		
		//司机存为json对象
		JSONObject jsonobj_driver = new JSONObject();
		jsonobj_driver.put("pk_driver", list.get(0).getAttributeValue("pk_driver"));
		jsonobj_driver.put("dname", list.get(0).getAttributeValue("driver"));
		jsonobj_driver.put("dphone", list.get(0).getAttributeValue("dphone"));
		
		//组织存为json对象
		JSONObject jsonobj_orgs = new JSONObject();
		jsonobj_orgs.put("pk_hrorg", list.get(0).getAttributeValue("pk_hrorg"));
		jsonobj_orgs.put("name", list.get(0).getAttributeValue("orgname"));
		
		//部门存为json对象
		JSONObject jsonobj_depts = new JSONObject();
		jsonobj_depts.put("pk_dept", list.get(0).getAttributeValue("pk_dept"));
		jsonobj_depts.put("name", list.get(0).getAttributeValue("deptname"));
		
		
		
		jsonObj.put("selectedDrivers", jsonobj_driver);//选择司机
		jsonObj.put("drivers", (new JSONArray()).put(jsonobj_driver) );//司机
		jsonObj.put("orgs",  (new JSONArray()).put(jsonobj_orgs) );//选择组织
		jsonObj.put("selectedorgs", jsonobj_orgs);//组织
		jsonObj.put("selecteddepts", jsonobj_depts );//部门
		jsonObj.put("depts", (new JSONArray()).put(jsonobj_depts) );//选择部门

		byte[] byte_img = (byte[])list.get(0).getAttributeValue("vphoto");
		String imgDatas = new String(byte_img,"UTF-8");
		jsonObj.put("vphoto", imgDatas);
		result.put("values", jsonObj);
		result.put("result", "true");
		return result;
	}

	/**
	 * 新增车辆档案
	 * @param json json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject addVehicle(JSONObject json) throws JSONException {
		//用于返回的result
		JSONObject result = new JSONObject();
		//携带当前登录用户信息的userjson
		JSONObject userjsonObject = json.getJSONObject("userjson"); // 登录人
		String cuserid = userjsonObject.getString("cuserid"); //当前登录人主键
		String pk_org = userjsonObject.getString("pk_org"); //当前登录人所属组织
		String pk_group = userjsonObject.getString("pk_group"); //当前登录人所属集团
		//司机
		String driver = json.getString("driver");
		//司机主键
		String pk_driver = json.getString("pk_driver");
		//车牌号
		String vehicleno = json.getString("vehicleno");
		//司机电话
		String dphone = json.getString("dphone");
		//车辆类型
		String vtype = json.getString("vtype");
		//车辆状态
		String vstate = json.getString("vstate");
		//载客数量
		String passengernum = json.getString("passengernum");
		//车辆性质
		String vcharacter = json.getString("vcharacter");
		//单位
		String unit = json.getString("unit");
		//部门
		String dept = json.getString("dept");
		//图片
		String image = json.getString("image");
		VehicleMessageVO vehiclevo = new VehicleMessageVO();
		//赋值--司机
		vehiclevo.setDriver(driver);
		//司机主键
		vehiclevo.setPk_driver(pk_driver);
		//车牌号
		vehiclevo.setVehicleno(vehicleno);
		//司机电话
		vehiclevo.setDphone(dphone);
		//车辆类型
		vehiclevo.setVtype(vtype);
		//载客数量
		vehiclevo.setPassengernum(Integer.parseInt(passengernum));
		//车辆性质
		vehiclevo.setVcharacter(vcharacter);
		//单位
		vehiclevo.setUnit(unit);
		//部门
		vehiclevo.setDept(dept);
		//状态
		vehiclevo.setVstate(vstate);
		//图片
		vehiclevo.setVphoto(image);
		//集团
		vehiclevo.setAttributeValue("pk_group", pk_group);
		//组织
		vehiclevo.setAttributeValue("pk_org", pk_org);
		//创建人
		vehiclevo.setAttributeValue("cuserId", cuserid);
		//获取AggVehicleMessageVO数组
		AggVehicleMessageVO aggmessagevo = new AggVehicleMessageVO();
		aggmessagevo.setParentVO(vehiclevo);
		AggVehicleMessageVO[] aggVOs = new AggVehicleMessageVO[]{aggmessagevo};
		try {
			InvocationInfoProxy.getInstance().setGroupId(pk_group);
			InvocationInfoProxy.getInstance().setUserId(cuserid);
			//车辆基本信息标准接口
			IVehicleMaintain iVehicleMaintain = (IVehicleMaintain) NCLocator
					.getInstance().lookup(IVehicleMaintain.class);
			//调用标准接口的新增
			AggVehicleMessageVO[] aggVO = iVehicleMaintain.insert(aggVOs, null);
			result.put("result", "true");
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("车辆基本信息新增失败！"+e.getMessage());
		}
		return result;
	}

	/**
	 * 查询司机
	 * @param jsonObject json
	 * @return JSONObject
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject queryDriver(JSONObject jsonObject) throws DAOException, JSONException {
		JSONObject result = new JSONObject();
		JSONArray driverArray = new JSONArray();
		JSONArray orgArray = new JSONArray();
		//查询dr=0的司机sql
		String sql="select pk_driver,dphone,dname from cl_driver"
				+ " where cl_driver.dr=0 and dstate <> '2' and pk_driver not in "
				+ "(select pk_driver from cl_vehicle where dr = 0)";
		//查询dr = 0的人力资源组织的sql(启用状态等于2即为已启用的),根据编码排序
		String sql_org="select name, pk_hrorg  from org_hrorg where dr=0 and enablestate =2 order by code";
		List<Map<String,String>> list = (List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
		List<Map<String,String>> list_org = (List<Map<String, String>>) dao.executeQuery(sql_org, new  MapListProcessor());
		//遍历司机结果集
		for (Map<String,String> map : list) {
			JSONObject driverJson = new JSONObject();
			//司机主键
			driverJson.put("pk_driver", map.get("pk_driver"));
			//司机姓名
			driverJson.put("dname", map.get("dname"));
			//司机电话
			driverJson.put("dphone", map.get("dphone"));
			driverArray.put(driverJson);
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
		result.put("drivers", driverArray);
		result.put("orgs", orgArray);
		result.put("result", "true");	
		return result;
	}

	/**
	 * 查询部门
	 * @param json json
	 * @return JSONObject
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject queryDept(JSONObject json) throws DAOException, JSONException {
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		//传入的单单位主键
		String pk_hrorg = json.getString("pk_hrorg");
		//查询部门的名称和主键(启用状态等于2即为已启用的),根据编码排序
		String sql_dept="select name,pk_dept from org_dept "
				+"where dr=0 and enablestate =2 and pk_org= '"+pk_hrorg +"' order by code";
		//存放部门结果集
		List<Map<String,String>> list_dept=(List<Map<String, String>>) dao.executeQuery(sql_dept, new  MapListProcessor());
		//遍历部门的结果集
		for (Map<String,String> map : list_dept) {
			JSONObject jsonObj = new JSONObject();
			//部门名称
			jsonObj.put("name", map.get("name"));
			//部门主键
			jsonObj.put("pk_dept", map.get("pk_dept"));
			jsonArray.put(jsonObj); 
		}
		
		result.put("depts", jsonArray);
		result.put("result", "true");
		return result;
	}

	/**
	 * 车辆档案列表查询方法
	 * @param json json
	 * @return JSONObject
	 * @throws UnsupportedEncodingException 
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject queryList(JSONObject json) throws UnsupportedEncodingException, DAOException, JSONException {
		int maxcount = json.getInt("maxcount"); // 最大数量
		int mincount = json.getInt("mincount"); // 最小数量
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		//查询所有dr = 0的车辆基本信息（分页查询）
		String sql_list = "SELECT *from (SELECT ROWNUM AS rowno1, t.*from (select * from cl_vehicle where dr = '0'"
				+ " order by ts desc) t where ROWNUM < ="+maxcount+") c WHERE c.rowno1 > "+mincount;
		//执行sql并赋值
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql_list, new  MapListProcessor());
		//遍历结果集
		for (Map<String,String> map : list) {
			//存放1条数据的mapJSON
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("pk_driver", map.get("pk_driver"));
			//取出结果集中的一条数据并放入mapJSON----司机
			jsonObj.put("driver", map.get("driver")==null?"":map.get("driver"));
			//取出结果集中的一条数据并放入mapJSON----车牌号
			jsonObj.put("vehicleno", map.get("vehicleno"));
			//取出结果集中的一条数据并放入mapJSON----司机电话
			jsonObj.put("dphone", map.get("dphone")==null?"":map.get("dphone"));
			//取出结果集中的一条数据并放入mapJSON----车辆类型
			jsonObj.put("vtype", map.get("vtype"));
			//取出结果集中的一条数据并放入mapJSON----载客数量
			jsonObj.put("passengernum",map.get("passengernum"));
			//取出结果集中的一条数据并放入mapJSON----车辆性质
			jsonObj.put("vcharacter", map.get("vcharacter"));
			//取出结果集中的一条数据并放入mapJSON----单位
			jsonObj.put("unit", map.get("unit"));
			//取出结果集中的一条数据并放入mapJSON----部门
			jsonObj.put("dept", map.get("dept"));
			//取出结果集中的一条数据并放入mapJSON----单据号
			jsonObj.put("billno", map.get("billno"));
			//取出结果集中的一条数据并放入mapJSON----车辆状态
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
