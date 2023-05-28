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
	 * 车辆基本信息其他信息填报页面servlet
	 * 用于处理其他信息填报页面请求
	 * @author 
	 * @date 2021-04-25
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
			if ("save".equals(method)) {
				result = saveOtherInfo(jsonObject);//修改后保存
			} else if ("add".equals(method)) {
				result = addOtherInfo(jsonObject);//新增其他信息填报
			} else if("delete".equals(method)){
				result = deleOtherInfo(jsonObject);//删除其他信息填报
			} else if("query".equals(method)){
				result = queryOtherInfo(jsonObject);//查询其他信息填报列表
			} else if("updateStatus".equals(method)){
				result = updateStatus(jsonObject);//更新单据状态
				
			}
		} catch (Exception e) {
			errMsg = e.getMessage();
			if(StringUtils.isBlank(errMsg)) {
				errMsg = "未知错误";
			}
//			ExceptionUtils.wrappException(e);
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
	 * 其他信息填报修改保存方法
	 * 参数：JSONObject json
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject saveOtherInfo(JSONObject json) throws Exception {
		//用于后面对比是否有被删除的子表
		containVos = new HashMap<String, InsuranceBVO>();
		JSONObject userjsonObject = json.getJSONObject("userjson"); // 登录人
		String pk_org = userjsonObject.getString("pk_org"); //当前登录人所属组织
		String pk_group = userjsonObject.getString("pk_group"); //当前登录人所属集团
		//用于返回前台的JSONObject
		JSONObject result = new JSONObject();
		//得到其他信息填报主表pk
		String pk_otherinfo = json.getString("pk_otherinfo");
				
		//修改主表的sql，根据其他信息填报主表pk，修改对应单据的车牌号
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
		//返回是否修改成功
		result.put("success", true);
		result.put("result", "true");
		//将结果返回给前台
		return result;
	}
	

	/**
	 * 删除单据
	 * 参数：JSONObject json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject deleOtherInfo(JSONObject json) throws JSONException {
		//用于返回前台
		JSONObject result = new JSONObject();
		// 单据主键
		String pk_otherinfo = json.getString("pk_otherinfo");
//		//用于得到主子表VO数组的sql
//		String sql = "update cl_otherinfo set dr = 1 where pk_otherinfo = '"+ pk_otherinfo + "'";
		
		try {
			//删除主子表VO数组
			dao.deleteByPK(OtherInfoVO.class, pk_otherinfo);
			result.put("success", true);
			result.put("result", "true");
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("删除其他信息填报信息失败！"+e.getMessage());
		}
		//返回
		return result;
	}
	/**
	 * 查询其他信息填报
	 * 参数：JSONObject json
	 * @return 
	 * @return 
	 * @throws Exception
	 */
	private JSONObject queryOtherInfo(JSONObject json) throws Exception {
		JSONObject result = new JSONObject();
		//存放查询到的所有数据
		JSONArray jsonArray = new JSONArray();
		JSONObject userjsonObject = json.getJSONObject("userjson"); // 登录人
		String roleName = userjsonObject.getString("role_name"); //当前登录人角色
//		postName = new String(postName.getBytes("iso-8859-1"),"UTF-8");
		String cuserId = userjsonObject.getString("cuserid"); //当前登录人主键
		
		int maxnum = json.getInt("maxcount"); // 最大数量
		int minnum = json.getInt("mincount"); // 最小数量
		//查询主表pk、日期、日加油升、金额、日其他车辆公里数、早空驶、晚空驶、日奖罚金额的sql
		
		String sql="SELECT * FROM (SELECT ROWNUM AS rowno, t.* "
				+ "FROM (select pk_otherinfo, infodate, dayfuel, amount, otherkm, emptytimesam, emptytimespm, bonuspenalty, wkdovertime, approvestatus,def1,def2,user_name "
				+ "from cl_otherinfo,sm_user where  cl_otherinfo.creator = sm_user.cuserid and cl_otherinfo.dr=0 ";
		if(roleName.equals("车辆管理员")) {
			sql += "and cl_otherinfo.approvestatus in ( 2, 3 )";
		}else {
			sql += "and cl_otherinfo.creator = '" + cuserId + "' ";
		}
		sql += "order by cl_otherinfo.infodate desc) t "
				+ "where ROWNUM <= " + maxnum + ") table_alias WHERE table_alias.rowno > " + minnum	+ " ";
		//新建集合用于存放查询结果
		List<Map<String,Object>> list= (List<Map<String, Object>>) dao.executeQuery(sql, new  MapListProcessor());
		//遍历，将查询到的数据放入json
		for (Map<String,Object> map : list) {
			//存放其中一条数据的json
			JSONObject jsonObj = new JSONObject();
			
			//赋值--其他信息填报主表pk
			jsonObj.put("pk_otherinfo", map.get("pk_otherinfo") == null ? "" : map.get("pk_otherinfo"));
			//日期
			jsonObj.put("infodate", map.get("infodate") == null ? "" : map.get("infodate"));
			//日加油升
			jsonObj.put("dayfuel", map.get("dayfuel") == null ? "" : map.get("dayfuel"));
			//金额
			jsonObj.put("amount", map.get("amount") == null ? "" : map.get("amount"));
			//日其他车辆公里数
			jsonObj.put("otherkm", map.get("otherkm") == null ? "" : map.get("otherkm"));
			//早空驶
			jsonObj.put("emptytimesam", map.get("emptytimesam") == null ? "" : map.get("emptytimesam"));
			//晚空驶
			jsonObj.put("emptytimespm", map.get("emptytimespm") == null ? "" : map.get("emptytimespm"));
			//日奖罚金额
			jsonObj.put("bonuspenalty", map.get("bonuspenalty") == null ? "" : map.get("bonuspenalty"));
			//周末加班次数
			jsonObj.put("wkdovertime", map.get("wkdovertime") == null ? "" : map.get("wkdovertime"));
			//班车每天起始里程
			jsonObj.put("def1", map.get("def1") == null ? "" : map.get("def1"));
			//班车每天截止里程
			jsonObj.put("def2", map.get("def2") == null ? "" : map.get("def2"));
			//制单人
			jsonObj.put("user_name", map.get("user_name") == null ? "" : map.get("user_name"));
			//状态
			Integer approvestatus = Integer.valueOf(map.get("approvestatus").toString());
			switch(approvestatus) {
			case -1://自由
				jsonObj.put("approvestatus", "自由");
				break;
			case 3://提交
				jsonObj.put("approvestatus", "已提交");
				break;
			case 1://已审批
				jsonObj.put("approvestatus", "已审批");
				break;
			default:
				jsonObj.put("approvestatus", "错误");
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
	 * 新增其他信息填报
	 * 参数：JSONObject json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject addOtherInfo(JSONObject json) throws JSONException {
		//用于返回的JSONObject
		JSONObject result = new JSONObject();
		String infodate = json.getString("infodate"); // 日期
		JSONObject userjsonObject = json.getJSONObject("userjson"); // 登录人
		String cuserid = userjsonObject.getString("cuserid"); //当前登录人主键
		String pk_org = userjsonObject.getString("pk_org"); //当前登录人所属组织
		String pk_group = userjsonObject.getString("pk_group"); //当前登录人所属集团
		//新建主表VO	
		OtherInfoVO hvo = new OtherInfoVO();
		hvo.setStatus(VOStatus.NEW);
		//设置集团--取用户所属集团
		hvo.setAttributeValue("pk_group", pk_group);
		//设置组织--取用户所属组织
		hvo.setAttributeValue("pk_org", pk_org);
		//设置创建人
		hvo.setAttributeValue("creator", cuserid);
		//设置单据日期
		hvo.setAttributeValue("billdate", new UFDate());
		//设置单据状态
		hvo.setAttributeValue("approvestatus", BillStatusEnum.FREE.value());
		
		//设置日期
		hvo.setAttributeValue("infodate", new UFDate(infodate));
		//设置日加油升
		hvo.setAttributeValue("dayfuel", new UFDouble(json.getString("dayfuel")));
		//设置金额
		hvo.setAttributeValue("amount", new UFDouble(json.getString("amount")));
		//设置日其他车辆公里数
		hvo.setAttributeValue("otherkm", new UFDouble(json.getString("otherkm")));
		//设置早空驶
		hvo.setAttributeValue("emptytimesam", new UFBoolean(json.getBoolean("emptytimesam")));
		//设置晚空驶
		hvo.setAttributeValue("emptytimespm", new UFBoolean(json.getBoolean("emptytimespm")));
		//设置日奖罚金额
		hvo.setAttributeValue("bonuspenalty", new UFDouble(json.getString("bonuspenalty")));
		//设置周末加班次数
		hvo.setAttributeValue("wkdovertime", new UFDouble(json.getString("wkdovertime")));
		//班车每天起始里程
		hvo.setAttributeValue("def1", json.getString("def1"));
		//班车每天截止里程
		hvo.setAttributeValue("def2", json.getString("def2"));
		AggOtherInfoVO otherInfoAggVO = new AggOtherInfoVO();
		otherInfoAggVO.setParent(hvo);
		
		try {
			InvocationInfoProxy.getInstance().setGroupId(pk_group);
			InvocationInfoProxy.getInstance().setUserId(cuserid);
		} catch (Exception e1) {
			ExceptionUtils.wrappBusinessException("通用错误error"+e1.getMessage());
		}
		//司机+日期唯一校验
		String checksql = "select 1 from cl_otherinfo where creator = '" + cuserid
				+ "' and substr(infodate,0,10) = '" + infodate + "'";
		Integer checkresult = 0;
		try {
			checkresult = (Integer) dao.executeQuery(checksql, new ColumnProcessor());
		} catch (DAOException e1) {
			ExceptionUtils.wrappBusinessException("添加其他信息填报信息失败！"+e1.getMessage());
		}
		if(checkresult != null && checkresult == 1) {
			ExceptionUtils.wrappBusinessException("当前日期已有数据,请查询后修改");
		}
		//其他信息填报标准接口
		IOtherInfoMaintain service = (IOtherInfoMaintain) NCLocator.getInstance().lookup(IOtherInfoMaintain.class);
		try {
			//调用新增方法
			AggOtherInfoVO[] aggVos = service.insert(new AggOtherInfoVO[] {otherInfoAggVO}, null);
			//TODO: 提交,校验
			result.put("success", true);
			result.put("result", "true");
		} catch (nc.vo.pub.BusinessException e) {
			ExceptionUtils.wrappBusinessException("添加其他信息填报信息失败！"+e.getMessage());
		}
		return result;
	}

	/**
	 * 更新单据状态
	 * 参数：JSONObject json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject updateStatus(JSONObject json) throws JSONException {
		//用于返回前台
		JSONObject result = new JSONObject();
		// 单据主键
		String pk_otherinfo = json.getString("pk_otherinfo");
		// 状态
		Integer status = json.getInt("status");
		String sql = "update cl_otherinfo set approvestatus = " + status 
				+ " where pk_otherinfo = '"+ pk_otherinfo + "'";
		
		try {
			//删除主子表VO数组
			dao.executeUpdate(sql);
			result.put("success", true);
			result.put("result", "true");
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("更新单据状态失败！"+e.getMessage());
		}
		//返回
		return result;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

}
