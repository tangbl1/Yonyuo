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
	 * 报表页面servlet
	 * 用于处理报表页面请求
	 * @author 周静
	 * @date 2019-11-20
	 * @param 
	 * 	req 前台页面请求
	 * 	resp 返回信息
	 */
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
		//查询订单
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
	 * 查询司机和车牌号
	 * 参数：JSONObject jsonObject
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject queryDriver(JSONObject json) throws Exception {
		JSONObject result = new JSONObject();
		//存放所有车牌号信息的JSONArray
		JSONArray jsonVehicleArray = new JSONArray();
		//查询车牌号和车牌号pk的sql，条件是所有dr不为0的
		String sql = "select pk_vehicle,vehicleno from cl_vehicle"
				+ " where cl_vehicle.dr=0";
		//存放车牌号额list
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql, new MapListProcessor());
		//遍历
		for (Map<String, String> map : list) {
			//存放一条车牌号信息的json
			JSONObject jsonObj = new JSONObject();
			//将车牌号pk放入jsonObj
			jsonObj.put("pk_vehicle", map.get("pk_vehicle"));
			//将车牌号放入jsonObj
			jsonObj.put("vehicleno", map.get("vehicleno"));
			//将这条车牌号信息放入存放所有车牌号信息的jsonArray中
			jsonVehicleArray.put(jsonObj);
		}
		//将所有数据放入一个JSONObject
		result.put("jsonvehiclearray", jsonVehicleArray);
		//存放所有司机信息的JSONArray
		JSONArray jsonDriverArray = new JSONArray();
		//查询司机姓名和主键pk的sql，条件是所有dr不为0的
		String sql_driver = "select pk_driver,dname from cl_driver"
				+ " where cl_driver.dr=0 ";
		//存放车牌号额list
		List<Map<String,String>> driverList= (List<Map<String, String>>) dao.executeQuery(sql_driver, new MapListProcessor());
		//遍历
		for (Map<String, String> map : driverList) {
			//存放一条车牌号信息的json
			JSONObject jsonObj = new JSONObject();
			//司机主键
			jsonObj.put("pk_driver", map.get("pk_driver"));
			//司机姓名
			jsonObj.put("dname", map.get("dname"));
			//将这条车牌号信息放入存放所有车牌号信息的jsonArray中
			jsonDriverArray.put(jsonObj);
		}
		//将所有数据放入一个JSONObject
		result.put("jsondriverarray", jsonDriverArray);
		return result;
		
	}
	/**
	 * 查询用车申请报表
	 * 参数：JSONObject jsonObject
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject getReport(JSONObject json) throws Exception {
		JSONArray jsonArr = new JSONArray(); //用于存储单据信息集合
		String dept = json.getString("dept"); // 部门
		String applier = json.getString("applier"); // 申请人
		String pk_driver = json.getString("pk_driver"); // 司机主键
		String pk_vehicle = json.getString("pk_vehicle"); // 车辆主键
		String beginTime = json.getString("beginTime"); // 开始时间
		String endTime = json.getString("endTime"); // 结束时间
		String sql_list="";
		//查询条件只有车牌号或者司机时,数据从表头查询。表体是显示是第一申请人的那条（只有车牌号时以车为主，不管表体有几条）
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
		}else{//否则从表体查询（表体有几条都显示出来）	
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
		//循环获取单据信息并存储成json对象
		for (Map<String, Object> map : list) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("departtime", map.get("departtime"));//日期
			jsonObj.put("dname", map.get("dname")!=null?map.get("dname"):"");//司机姓名
			jsonObj.put("vehicleno", map.get("vehicleno")!=null?map.get("vehicleno"):"");//车牌号
			jsonObj.put("username", map.get("username"));//用户
			UFDouble travelmileage=UFDouble.ZERO_DBL;//公里数
			if(map.get("travelmileage")!=null){
			travelmileage=new UFDouble(map.get("travelmileage").toString());
			}
			jsonObj.put("travelmileage", travelmileage);//公里数
			
			jsonObj.put("dept", map.get("dept"));// 部门
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
	 * 司机在途报表
	 * 参数：JSONObject jsonObject
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject queryDriStatus(JSONObject json) throws Exception {
		JSONArray jsonArr = new JSONArray(); //用于存储单据信息集合
		String today = json.getString("today"); // 获取日期
		String sql_list="";
		//查询条件只有车牌号或者司机时,数据从表头查询。表体是显示是第一申请人的那条（只有车牌号时以车为主，不管表体有几条）
		sql_list=" select substr(cl_vorder.begintime,11,18) begintime,substr(cl_vorder.endtime,11,18) endtime,cl_driver.dname,"
				+ " cl_vorder.vehicleno,cl_vorder_b.destarea,cl_vorder_b.origin"
				+ " from cl_vorder"
				+ " inner join cl_vorder_b on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0 and cl_vorder_b.isfisrtapplier='Y'"
				+ " left join cl_driver on cl_driver.pk_driver=cl_vorder.pk_driver and cl_driver.dr=0"
				+ " left join cl_vehicle on cl_vehicle.pk_vehicle=cl_vorder.pk_vehicle and cl_vehicle.dr=0"
				+ " where cl_vorder.dr = 0 and cl_vorder.billstate in(5,7) and substr(cl_vorder_b.departtime,0,10)='"+today+"'";
		
		
		List<Map<String,Object>> list= (List<Map<String, Object>>) dao.executeQuery(sql_list, new  MapListProcessor());		

		UFDouble sumMileag=UFDouble.ZERO_DBL;
		//循环获取单据信息并存储成json对象
		for (Map<String, Object> map : list) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("begintime", map.get("begintime"));//出发时间
			jsonObj.put("endtime", map.get("endtime"));//返回时间
			jsonObj.put("dname", map.get("dname")!=null?map.get("dname"):"");//司机姓名
			jsonObj.put("vehicleno", map.get("vehicleno")!=null?map.get("vehicleno"):"");//车牌号
			jsonObj.put("start_direction", map.get("origin")+"-"+map.get("destarea"));//出发方向
			String return_direction="";//返回方向
			if(map.get("endtime")!=null){
				return_direction=map.get("destarea")+"-"+map.get("origin");
			}else{
				jsonObj.put("endtime", "");//返回时间
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
	 * 司机在途报表
	 * 参数：JSONObject jsonObject
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject queryDriStatus1(JSONObject json) throws Exception {
		JSONArray jsonArr = new JSONArray(); //用于存储单据信息集合
		String day = json.getString("billdate"); // 获取日期
		//TODO:不显示驳回的订单和已完成的订单
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
	//循环获取单据信息并存储成json对象
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
					name += applierMap.get("user_name")+"、";
				}
			}
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("appliername", name);
			Map<String,String> state=new HashMap<String,String>();//状态映射，用于获取状态编码对应的状态名称
			
				//单据状态做映射，已完成、申请中、部门审批完成、准备出发、出发、到达、返程、休息、值班。
				state.put("1", "已完成");
				state.put("2", "申请中");
				state.put("3", "部门审批完成");
				state.put("4", "准备出发");
				state.put("5", "出发");
				state.put("6", "到达");
				state.put("7", "返程");
				state.put("8", "休息");
				state.put("9", "驳回");
			
			jsonObj.put("billstate", state.get(map.get("billstate")));//单据状态
			jsonObj.put("vbilldate", map.get("departdate"));//时间(用车日期)
			jsonObj.put("origin", map.get("origin"));//始发地
			jsonObj.put("destarea", map.get("destarea"));//目的地
			jsonObj.put("finaldest", map.get("finaldest"));//最终目的地
			jsonObj.put("driver", map.get("driver"));
			jsonObj.put("vehicleno", map.get("vehicleno")==null?"":map.get("vehicleno"));
			jsonObj.put("begintime", map.get("begintime")==null?"":map.get("begintime"));//出发时间
			jsonObj.put("endtime", map.get("endtime")==null?"":map.get("endtime"));//返回时间
			String start_direction="";//出发方向
			if(map.get("begintime")!=null){
				start_direction=map.get("origin")+"-"+map.get("destarea");
			}
			jsonObj.put("start_direction", start_direction);//出发方向
			String return_direction="";//返回方向
			if(map.get("endtime")!=null){
				return_direction=map.get("destarea")+"-"+map.get("origin");
			}
			jsonObj.put("return_direction", return_direction);//返回方向
			jsonObj.put("user_name", map.get("user_name")!=null?map.get("user_name"):"");
			String useTime = (String) map.get("departtime");
			String departdate = useTime.substring(11,16);
			jsonObj.put("departdate", departdate);//车管审批
			jsonObj.put("origin", map.get("origin"));//始发地
			jsonObj.put("destarea", map.get("destarea"));//始发地
			jsonArr.put(jsonObj);
			
			
		}
	
		//查询所有的司机,不包括外出和休息的
		String sql_driver="select cl_driver.dname driver,cl_vehicle.vehicleno "
				+ " from cl_driver "
				+ " left join cl_vehicle on cl_vehicle.pk_driver=cl_driver.pk_driver and cl_vehicle.dr=0"
				+ " where cl_driver.dr=0 and dstate not in (2,4)"
				+"  and cl_driver.pk_driver not in (select pk_driver from cl_vorder "
				+ " inner join cl_vorder_b on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0  "
				+ " where cl_vorder.billstate not in('1','9') and substr(cl_vorder_b.departtime,0,10) <='"+day+ "' "
				+ " and substr(cl_vorder_b.returntime,0,10)>='"+day
				+ "')";
		List<Map<String,Object>> driverList=(List<Map<String, Object>>) dao.executeQuery(sql_driver, new  MapListProcessor());//今天未被占用的司机列表
		JSONArray jsonDriver = new JSONArray(); //未被占用的司机
		//循环获取单据信息并存储成json对象
		for (Map<String, Object> map : driverList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("driver", map.get("driver"));
			jsonObj.put("vehicleno", map.get("vehicleno")==null?"":map.get("vehicleno"));
			jsonObj.put("user_name", "");//用户名
			jsonObj.put("appliername", "");
			jsonObj.put("billstate", "空闲");//单据状态
			jsonObj.put("origin", "");//始发地
			jsonObj.put("destarea","");//目的地
			jsonObj.put("begintime", "");//出发时间
			jsonObj.put("endtime", "");//返回时间
			jsonObj.put("departdate", "");//车管审批
			jsonDriver.put(jsonObj);
			
		}
		JSONObject result = new JSONObject();
		result.put("values", jsonArr);
		result.put("jsonDriver", jsonDriver);
		result.put("result", "true");
		return result;	
	}
}