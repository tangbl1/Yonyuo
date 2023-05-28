package nc.itf.vord.servlet;

import disposition.GetDatasourceName;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.comn.NetStreamContext;
import nc.bs.framework.core.service.IFwLogin;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("restriction")
@WebServlet("/vordriservlet")
public class VorderDriverServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	BaseDAO dao = new BaseDAO();
	private static boolean TEST_FLAG = false;	//测试标识：true->测试服务器	false->正式服务器
	private static Map<String,String> state=new HashMap<String,String>();	//状态映射，用于获取状态编码对应的状态名称
	static {
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
	}
	/**
	 * 司机申请单页面servlet
	 * 用于处理司机页面请求
	 * @author 周静
	 * @date 2019-11-20
	 * @param 
	 * 	req 前台页面请求
	 * 	resp 返回信息
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
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
		try {
			//查询订单
			if (method.equals("query")) {
				result = queryVorder(jsonObject);//查询订单列表
			} else if (method.equals("updateStaue")) {
				result = updateStaue(jsonObject);//更新状态
			} else if(method.equals("querydetail")){
				result = queryDetail(jsonObject);//查询订单详情
			}else if (method.equals("query_remark")) {
				result = queryRemark(jsonObject);//判断订单是否已经被评价
			}
			if(StringUtils.isNotBlank(errMsg)){
				result.put("success", false);
				result.put("errMsg", errMsg);
			}
		} catch (DAOException e) {
			ExceptionUtils.wrappException(e);
		} catch (JSONException e) {
			ExceptionUtils.wrappException(e);
		}
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("GBK");
		resp.getWriter().write(result.toString());
		resp.flushBuffer();
	}
	
	/**
	 * 司机端查新用车详情
	 * @param jsonObject
	 * @return
	 * @throws JSONException 
	 */
	private JSONObject queryDetail(JSONObject jsonObject) throws JSONException {
		JSONObject result = new JSONObject();
		String pk_vorder = jsonObject.getString("pk_vorder"); // 表头主键
		//查询表头信息
		String sql="select cl_vorder.billstate,cl_vorder.iscarpool,cl_vorder.pk_vehicle,"
				+ "cl_vorder.startMileage,cl_vorder.backMileage,cl_vorder.travelMileage,cl_vorder_b.*"
				+" from cl_vorder "
				+" inner join cl_vorder_b"
				+" on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0"
				+" and isfisrtapplier='Y'"
				+" where cl_vorder.dr=0  and cl_vorder.pk_vorder='"+pk_vorder+"'";
		//查询表体信息
		String sql_detail="select cl_vorder_b.*,sm_user.user_name username"
				+" from cl_vorder_b"
				+" left join sm_user on sm_user.cuserid  =cl_vorder_b.applier and sm_user.dr=0"
				+" where cl_vorder_b.dr=0 and pk_vorder='"+pk_vorder+"'"
				+ "order by cl_vorder_b.ts desc ";

		Map<String,String> map = new HashMap<String,String>();
		List<Map<String,String>> list_detail=new ArrayList<>();
		try {
			map = (Map<String,String>) dao.executeQuery(sql, new  MapProcessor());
			list_detail = (List<Map<String,String>>) dao.executeQuery(sql_detail, new  MapListProcessor());
		} catch (DAOException e) {
			ExceptionUtils.wrappBusinessException("查询用车申请失败！");
		}
		JSONObject jsonVO = new JSONObject();
		jsonVO.put("origin", map.get("origin"));//出发地
		jsonVO.put("destarea", map.get("destarea"));//目标区域
		jsonVO.put("dest1", map.get("dest1")==null?"":(String)map.get("dest1"));//目的地1
		//如果目的地有值就显示，并且开始加号不显示
		if(StringUtils.isNotBlank(map.get("dest2"))){
			jsonVO.put("dest2Div", "display: block;");//目的地2
		}else{
			jsonVO.put("dest2Div", "display: none;");//目的地2不显示
		}
		if(StringUtils.isNotBlank(map.get("dest3"))){
			jsonVO.put("dest3Div", "display: block;");//目的地3
		}else{
			jsonVO.put("dest3Div", "display: none;");//目的地3不显示
		}
		jsonVO.put("dest2", map.get("dest2"));//目的地2
		jsonVO.put("dest3", map.get("dest3"));//目的地3
		jsonVO.put("departtime",gettime(map.get("departtime")));//用车时间
		jsonVO.put("returntime",gettime(map.get("returntime")));//返回时间
		jsonVO.put("iscarpool", "Y".equals(map.get("iscarpool"))?"是":"否");//能否拼车
		jsonVO.put("billstate", state.get(map.get("billstate")));//状态
		jsonVO.put("finaldest", map.get("finaldest"));//最终目的地
		jsonVO.put("startMileage", map.get("startmileage"));//出发里程
		jsonVO.put("backMileage", map.get("backmileage"));//返回里程
		jsonVO.put("travelMileage", map.get("travelmileage"));//行驶里程
		jsonVO.put("pk_vehicle", map.get("pk_vehicle"));//车辆主键
		
		Boolean isreview = true;//判断订单是否被评价(默认为是)
		//循环表体，得到表体list
		JSONArray jsonDetails = new JSONArray(); //表体JSON数据合计
		for (Map<String, String> map_detail : list_detail) {
			JSONObject jsonDetail = new JSONObject();
			jsonDetail.put("selectedPNum", map_detail.get("selectpnum"));//人数
			jsonDetail.put("username", map_detail.get("username"));//姓名
			jsonDetail.put("userphone", map_detail.get("phone"));//联系电话
			jsonDetail.put("dest1", map_detail.get("dest1"));//目的地1
			jsonDetail.put("finaldest", map_detail.get("finaldest"));//目的地
			jsonDetail.put("remark", map_detail.get("remark"));//事由
			if(map.get("starlevel")==null){//如果没有被评价，review设置为false
				isreview = false;
			}
			jsonDetails.put(jsonDetail);
		}
		jsonVO.put("user", jsonDetails);
		jsonVO.put("isreview", isreview);
		result.put("values", jsonVO);
		result.put("result", "true");
		return result;	
	}
	/**
	 * 判断该订单是否已经评价
	 * @param jsonObject
	 * @return
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject queryRemark(JSONObject jsonObject) throws DAOException, JSONException {
		JSONObject result = new JSONObject();
		Boolean isreview = true;//判断订单是否被评价(默认为是)
		String pk_vorder = jsonObject.getString("pk_vorder"); // 单据主键
		String sql = "select count(starlevel) sumlevel,count(pk_vorder_b) sumpk from cl_vorder_b where pk_vorder='"+pk_vorder+"'";//查询评价和星级
		Map<String,Integer> map = (Map<String, Integer>) dao.executeQuery(sql, new MapProcessor());
		if(map.get("sumlevel")!=map.get("sumpk")){//如果没有被评价，review设置为false
			isreview = false;
		}
		result.put("isreview",isreview);
		result.put("result", "true");
		return result;
	}
	/**
	 * 更新单据状态
	 * @param json
	 * @return
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject updateStaue(JSONObject json) throws DAOException, JSONException {
		JSONObject result = new JSONObject();
		String pk_vorder = json.getString("pk_vorder"); // 单据主键
		String billstate = json.getString("billstate"); // 单据状态
		String vstate = json.getString("vstate"); // 车辆状态
		String pk_vehicle = json.getString("pk_vehicle"); // 车辆主键
		//更新表头数据状态
		String sql="update cl_vorder set billstate='"+billstate
				       +"' where pk_vorder='"+pk_vorder+"'";
		//同步更新车辆的状态
		String sql_vehicle="update cl_vehicle set vstate='"+vstate
					 +"' where pk_vehicle='"+pk_vehicle+"'";
		//更新状态
		dao.executeUpdate(sql);
		dao.executeUpdate(sql_vehicle);
		if("1".equals(billstate)){//单据完成的时候更新返回里程和行驶里程
		String startMileage = json.getString("startMileage"); // 出发里程
		String backMileage = json.getString("backMileage"); // 返回里程
		String travelMileage = json.getString("travelMileage"); // 行驶里程
		String sql_mileage="update cl_vorder set startMileage='"+startMileage
					+"', backMileage='"+backMileage
					+"', travelMileage='"+travelMileage
			       +"' where pk_vorder='"+pk_vorder+"'";
		dao.executeUpdate(sql_mileage);
		}else if("5".equals(billstate)){//更新开始时间和出发里程
		String startMileage = json.getString("startMileage"); 
		UFDateTime begintime = AppContext.getInstance().getServerTime();
		String sql_begintime="update cl_vorder set begintime='"+begintime+"'"
							 +",startMileage="+startMileage
							 +" where pk_vorder='"+pk_vorder+"'";
		dao.executeUpdate(sql_begintime);
		}else if("7".equals(billstate)){
			UFDateTime endtime = AppContext.getInstance().getServerTime();
			String sql_endtime="update cl_vorder set endtime='"+endtime+"'"
								+" where pk_vorder='"+pk_vorder+"'";
			dao.executeUpdate(sql_endtime);
		}
			    
		result.put("result", "true");
		return result;
	}

	/**
	 * 司机端查新用车列表
	 * @param json
	 * @return
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject queryVorder(JSONObject json) throws DAOException, JSONException{
		//TEST_FLAG=true;
		JSONArray jsonArr = new JSONArray(); //用于存储单据信息集合
		String cuserid = json.getString("cuserid"); //当前登录人主键
		int maxcount = json.getInt("maxcount"); // 最大数量
		int mincount = json.getInt("mincount"); // 最小数量
		//司机主键
		String sql_driver ="select pk_driver from cl_driver where vdef1='"+cuserid+"' and dr=0";
		Object cudriver =  dao.executeQuery(sql_driver, new ColumnProcessor());
		String cupk_driver="";
		if(cudriver!=null){
			cupk_driver=cudriver.toString();
		}
		String sql="SELECT *FROM (SELECT ROWNUM AS rowno1, t.*FROM (select sumselectpnum,cl_vorder.billstate,cl_vorder.travelmileage,cl_vorder_b.* "
				+" from cl_vorder "
				+" inner join cl_vorder_b"
				+" on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0"
				+" and isfisrtapplier='Y'"
				+" left join ("
				+"	SELECT sum(selectpnum) sumselectpnum,cl_vorder_b.pk_vorder,cl_vorder_b.dr from cl_vorder_b "
				+"	group BY cl_vorder_b.pk_vorder,cl_vorder_b.dr"
				+" ) sum on sum.pk_vorder=cl_vorder.pk_vorder and sum.dr=0"
				+" where cl_vorder.dr=0 "
				//TODO:测试用
				+ ((TEST_FLAG) ? "" : "and pk_driver='"+cupk_driver+"'")
				+ "order by cl_vorder.ts desc) t where ROWNUM <= "+maxcount+") table_alias WHERE table_alias.rowno1 >"+mincount;
		List<Map<String,Object>> list= (List<Map<String, Object>>) dao.executeQuery(sql, new  MapListProcessor());
		
		//循环获取单据信息并存储成json对象
		for (Map<String, Object> map : list) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("pk_vorder", map.get("pk_vorder"));
			jsonObj.put("pk_vorder_b", map.get("pk_vorder_b"));
			jsonObj.put("billstate",state.get(map.get("billstate")));//状态
			jsonObj.put("departtime",gettime((String)map.get("departtime")));//用车时间
			jsonObj.put("returntime",gettime((String)map.get("returntime")));//返回时间
			jsonObj.put("origin", map.get("origin"));//始发地
			jsonObj.put("dest1", map.get("dest1")==null?"":(String)map.get("dest1"));//目的地1
			jsonObj.put("finaldest", map.get("finaldest"));//最终目的地
			jsonObj.put("selectedPNum", map.get("sumselectpnum"));//人数
			UFDouble travelmileage=(map.get("travelmileage")==null?UFDouble.ZERO_DBL:new UFDouble(map.get("travelmileage").toString()));
			jsonObj.put("travelMileage", travelmileage);//行驶里程
			jsonArr.put(jsonObj);
		}
		JSONObject result = new JSONObject();
		result.put("values", jsonArr);
		result.put("result", "true");
		return result;	
	}


	/**
	 * 获取时间（截取到时分，不需要秒）
	 * @param time
	 * @return
	 */
	private String gettime(String time) {
		if (StringUtils.isNotBlank(time)) {
			time = time.substring(0, 16);
		}
		return time;
	}
}
