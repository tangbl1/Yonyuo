package nc.itf.vord.servlet;

import disposition.GetDatasourceName;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.comn.NetStreamContext;
import nc.bs.framework.core.service.IFwLogin;
import nc.bs.trade.business.HYPubBO;
import nc.itf.vehicle.IVorderMaintain;
import nc.itf.vehicle.util.YonyouMessageUtil;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.vehicle.vorder.AggVorderHVO;
import nc.vo.vehicle.vorder.VorderBVO;
import nc.vo.vehicle.vorder.VorderHVO;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
@SuppressWarnings("restriction")
@WebServlet("/hworder")
public class VorderManagerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	BaseDAO dao = new BaseDAO();
	private static Map<String,String> state=new HashMap<String,String>();//状态映射，用于获取状态编码对应的状态名称
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
	 * 车管申请单页面servlet
	 * 用于处理车管页面请求
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
		String username = "hwapp";
		String password = "asdqwe123";
		IFwLogin loginService = (IFwLogin) NCLocator.getInstance().lookup(IFwLogin.class);
		byte[] token = loginService.login(username, password, null);
		NetStreamContext.setToken(token);
		//通过用户编码查询对应cuserid,pk_group
		String sqluserid = "select cuserid,pk_group from sm_user where user_code='"
				+ username + "'";
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
			if ("add".equals(method)) { // 添加
				result = addVorder(jsonObject);
			} else if ("query".equals(method)) { // 查询
				result = queryVorder(jsonObject);
			} else if ("querydetail".equals(method)) { // 详细
				result = queryDetail(jsonObject);
			} else if ("updateStaue".equals(method)) { // 更新状态
				result = updateStaue(jsonObject);
			}else if ("update_save".equals(method)) { // 修改订单
				result = updateVorder(jsonObject);//修改时保存
			}else if ("query_vehicle".equals(method)) {
				result = querVehicle(jsonObject);//查询车辆信息
			}else if (method.equals("iscarpool")) {
				result = filterDriver(jsonObject);//是否能用车
			}else if (method.equals("query_code")) {
				result = queryCode(jsonObject);//查询组织编码
			}
		} catch (DAOException e) {
			ExceptionUtils.wrappException(e);
		} catch (JSONException e) {
			ExceptionUtils.wrappException(e);
		}catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		} catch (ParseException e) {
			ExceptionUtils.wrappException(e);
		} 

		resp.setContentType("text/html");
		resp.setCharacterEncoding("GBK");
		resp.getWriter().write(result.toString());
		resp.flushBuffer();

	}

	/**
	 * 派车单添加
	 * 
	 * @param json
	 * @return
	 * @throws BusinessException 
	 */
	@SuppressWarnings("unused")
	private JSONObject addVorder(JSONObject json) throws JSONException, BusinessException{
		JSONObject result = new JSONObject();
		//得到前台穿过来的字段
		JSONObject userjsonObject = json.getJSONObject("userjson"); // 登录人
		String	cuserid=userjsonObject.getString("cuserid");//当前登录人主键
		String pk_org=userjsonObject.getString("pk_org");//组织
		String 	pk_group=userjsonObject.getString("pk_group");//集团
		String 	pk_dept=userjsonObject.getString("pk_dept");//部门
		String origin = json.getString("origin"); // 出发地
		String destarea = json.getString("destarea");// 目标区域
		String dest1 =json.getString("dest1"); // 目的地1
		String dest2 =json.getString("dest2"); // 目的地2
		String dest3 =json.getString("dest3"); // 目的地3
		String finaldest =json.getString("finaldest"); // 最终目的地
		String reason =json.getString("reason"); // 事由
		int selectedPNum =Integer.parseInt(json.getString("selectedPNum")); // 人数
		UFBoolean iscarpool =new UFBoolean(json.getString("mySwitch")); // 是否拼车
		String phone =json.getString("phone"); // 用户电话
		//根据用户电话查找人员
		String sql=" select cuserid  from bd_psndoc"
				+" inner join sm_user on bd_psndoc.pk_psndoc =sm_user.pk_psndoc and sm_user.dr=0"
				+"  where bd_psndoc.dr=0 and mobile ='"+phone+"'";
		Object o=dao.executeQuery(sql, new ColumnProcessor());
		if(o!=null){//人员重新赋值给实际用车人
			cuserid=o.toString();
		}
		String driver =json.getString("driver"); // 司机
		String pk_driver =json.getString("pk_driver"); // 司机主键
		String driverphone =json.getString("driverphone"); // 司机电话
		String vehicleno =json.getString("vehicleno"); // 车牌号
		String pk_vehicle =json.getString("pk_vehicle"); // 车牌号
		String departtime =json.getString("departtime"); // 用车时间
		String returntime =json.getString("returntime"); // 返回时间
		String turndownreason =json.getString("turndownreason"); // 驳回原因
		if(departtime!=null&&!departtime.equals("")){
		departtime=departtime.replaceAll("-", "/");
		departtime=departtime+":00";
		}
		if(returntime!=null&&!returntime.equals("")){
			returntime=returntime.replaceAll("-", "/");
			returntime=returntime+":00";
			}
		AggVorderHVO aggvo = new AggVorderHVO();
		VorderHVO hvo = new VorderHVO();
		hvo.setAttributeValue("iscarpool", iscarpool);
		hvo.setAttributeValue("driver", driver);
		hvo.setAttributeValue("pk_driver", pk_driver);
		hvo.setAttributeValue("dphone", driverphone);
		hvo.setAttributeValue("vehicleno", vehicleno);
		hvo.setAttributeValue("pk_vehicle", pk_vehicle);
		int allPNum=4;//车上一共4个座位
		hvo.setAttributeValue("remainpnum", allPNum-selectedPNum);//剩余座位
		hvo.setAttributeValue("billstate", "4");//创建单据时单据状态为准备出发
		hvo.setAttributeValue("pk_group", pk_group);
		hvo.setAttributeValue("pk_org", pk_org);
		hvo.setAttributeValue("cuserId", cuserid);
		UFDate vbilldate = new UFDate();
		hvo.setAttributeValue("vbilldate",vbilldate);
		
		aggvo.setParent(hvo);
		VorderBVO bvo=new VorderBVO();
		//创建订单时默认是第一申请人
		hvo.setAttributeValue("approvestatus", 3);
		bvo.setAttributeValue("isfisrtapplier", 'Y');
		bvo.setAttributeValue("applier", cuserid);
		bvo.setAttributeValue("dept", pk_dept);
		bvo.setAttributeValue("phone", phone);
		bvo.setAttributeValue("origin", origin);
		bvo.setAttributeValue("destarea", destarea);
		
		bvo.setAttributeValue("dest1", dest1);
		bvo.setAttributeValue("dest2", dest2);
		bvo.setAttributeValue("dest3", dest3);
		bvo.setAttributeValue("finaldest", finaldest);
		bvo.setAttributeValue("departtime", departtime);
		bvo.setAttributeValue("returntime", returntime);
	    bvo.setAttributeValue("selectpnum", selectedPNum);
		bvo.setAttributeValue("remark", reason);
		bvo.setAttributeValue("turndownreason", turndownreason);
		VorderBVO[] bvos={bvo};
		aggvo.setChildrenVO(bvos);

		// 设置GroupId
		InvocationInfoProxy.getInstance().setGroupId(
				hvo.getAttributeValue("pk_group").toString());
		InvocationInfoProxy.getInstance().setUserId(
				hvo.getAttributeValue("cuserId").toString());

		AggVorderHVO[] aggvos = { aggvo };
		IVorderMaintain iVorderMaintain = (IVorderMaintain) NCLocator
				.getInstance().lookup(IVorderMaintain.class);
		
		AggVorderHVO[] aggdfVO = iVorderMaintain.insert(aggvos, null);
		result.put("result", "true");
		
		return result;
	}
	
	/**
	 * 申请单列表-车管端
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private JSONObject queryVorder(JSONObject json) throws DAOException, JSONException{
		JSONArray jsonArr = new JSONArray(); //用于存储单据信息集合
		String departtime = json.getString("billdate"); // 用车日期
		String orgCode = json.getString("orgcode"); // 组织编码
		String sql="select cl_driver.dname user_name,cl_vorder.driver,cl_vorder.approvestatus,cl_vorder.vehicleno,cl_vorder.pk_vorder,cl_vorder.pk_driver,cl_vorder.billstate,substr(departtime,0,10) departdate,sumselectpnum,cl_vorder_b.* "
					+" from cl_vorder "
					+" inner join cl_vorder_b"
					+" on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0"
					+" and isfisrtapplier='Y' left join cl_driver on cl_driver.pk_driver = cl_vorder.pk_driver and cl_driver.dr=0 "
					+ "left join org_orgs on cl_driver.pk_org = org_orgs.pk_org " 
					+" left join ("
					+"	SELECT sum(selectpnum) sumselectpnum,cl_vorder_b.pk_vorder,cl_vorder_b.dr from cl_vorder_b "
					+"	group BY cl_vorder_b.pk_vorder,cl_vorder_b.dr"
					+" ) sum on sum.pk_vorder=cl_vorder.pk_vorder and sum.dr=0"
					+" where cl_vorder.dr=0 and substr(departtime,0,10)='"+departtime+"' and org_orgs.code='" +orgCode
							+ "' order by cl_vorder.ts desc ";
		
		List<Map<String,Object>> list=(List<Map<String, Object>>) dao.executeQuery(sql, new  MapListProcessor());
		//循环获取单据信息并存储成json对象
		for (Map<String, Object> map : list) {
				String pk_dept = (String) map.get("dept");
				String pk_vorder = (String) map.get("pk_vorder");
				String sql_dept = "select name from bd_psndoc where pk_psndoc "
						+ "= (select principal from org_dept where  "
						+ "pk_dept = '"+pk_dept+"' and dr = 0) and dr = 0";
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
				Map<String,Object> deptmap = (Map<String,Object>) dao.executeQuery(sql_dept, new MapProcessor());
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("name", deptmap==null?"":deptmap.get("name"));//部门名称
				jsonObj.put("appliername", name);
				jsonObj.put("pk_vorder", map.get("pk_vorder"));
				jsonObj.put("pk_vorder_b", map.get("pk_vorder_b"));
				jsonObj.put("billstate", state.get(map.get("billstate")));//单据状态
				jsonObj.put("vbilldate", map.get("departdate"));//时间(用车日期)
				jsonObj.put("origin", map.get("origin"));//始发地
				jsonObj.put("destarea", map.get("destarea"));//始发地
				jsonObj.put("selectedPNum", map.get("selectpnum"));//人数
				jsonObj.put("sumselectpnum", map.get("sumselectpnum"));//表体人数和
				jsonObj.put("finaldest", map.get("finaldest"));//最终目的地
				jsonObj.put("pk_driver", map.get("pk_driver"));
				jsonObj.put("driver", map.get("driver"));
				jsonObj.put("vehicleno", map.get("vehicleno")==null?"":map.get("vehicleno"));
				jsonObj.put("pk_vehicle", map.get("pk_vehicle")==null?"":map.get("pk_vehicle"));
				
				jsonObj.put("user_name", map.get("user_name")!=null?map.get("user_name"):"");
				String useTime = (String) map.get("departtime");
				String departdate = useTime.substring(11,16);
				String departApproval = "";//部门审批状态
				String vehicleApproval = "";//车管审批状态
				if(Integer.parseInt(map.get("billstate").toString())!=2&&Integer.parseInt(map.get("approvestatus").toString())==1&&Integer.parseInt(map.get("billstate").toString())!=9){
					 departApproval = "已审批";
				}else if(Integer.parseInt(map.get("billstate").toString())==9){
					departApproval = "驳回";
				}else{
					departApproval = "未审批";
				}
				if(Integer.parseInt(map.get("billstate").toString())!=2&&Integer.parseInt(map.get("billstate").toString())!=3&&Integer.parseInt(map.get("billstate").toString())!=9){
					vehicleApproval = "已审批";
				}else if(Integer.parseInt(map.get("billstate").toString())==9){
					vehicleApproval = "驳回";
				}else{
					vehicleApproval = "未审批";
				}
				jsonObj.put("departApproval", departApproval);//部门审批
				jsonObj.put("vehicleApproval", vehicleApproval);//车管审批
				jsonObj.put("departdate", departdate);//车管审批
				if("1001".equals(orgCode)) {
					jsonObj.put("approver", "丁喆");//车管审批
				}else if("1002".equals(orgCode)) {
					jsonObj.put("approver", "张九鹏");//车管审批
				}else {
					jsonObj.put("approver", "");//车管审批
				}
				jsonArr.put(jsonObj);
				
				
			}
			JSONObject result = new JSONObject();
			result.put("values", jsonArr);
			result.put("result", "true");
			return result;	
	}
	

	/**
	 * 申请单详情-车管端
	 * 
	 * @param json
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	private JSONObject queryDetail(JSONObject json) throws DAOException, JSONException, ParseException{
		JSONObject result = new JSONObject();
		String pk_vorder = json.getString("pk_vorder"); // 表头主键
		//表头数据
		String sql="select cl_vorder.billstate,cl_vorder.iscarpool,cl_vorder.driver,"
				+ "cl_vorder.pk_driver,cl_vorder.pk_vehicle,cl_vorder.vehicleno,cl_vorder.dphone,cl_vorder.startmileage,"
				+ "cl_vorder.backmileage,cl_vorder.travelmileage,cl_vorder_b.*"
				+" from cl_vorder "
				+" inner join cl_vorder_b"
				+" on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0"
				+" where cl_vorder.dr=0  and cl_vorder.pk_vorder='"+pk_vorder+"'";
		//表体数据
		String sql_detail="select cl_vorder_b.*,sm_user.user_name username"
				+" from cl_vorder_b"
				+" left join sm_user on sm_user.cuserid  =cl_vorder_b.applier and sm_user.dr=0"
				+" where cl_vorder_b.dr=0 and pk_vorder='"+pk_vorder+"'"
				+ "order by cl_vorder_b.ts desc ";
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> list_detail=new ArrayList<>();
		try {
			map = (Map<String,Object>) dao.executeQuery(sql, new  MapProcessor());
			list_detail = (List<Map<String,Object>>) dao.executeQuery(sql_detail, new  MapListProcessor());
		} catch (DAOException e) {
			ExceptionUtils.wrappBusinessException("查询用车申请失败！");
		}
		JSONObject jsonVO = new JSONObject();
		jsonVO.put("origin", map.get("origin"));//出发地
		jsonVO.put("destarea", map.get("destarea"));//目标区域
		jsonVO.put("dest1", map.get("dest1")==null?"":(String)map.get("dest1"));//目的地1
		//如果目的地有值就显示，并且开始加号不显示
		if(map.get("dest2")!=null&&""!=map.get("dest2")){
			jsonVO.put("dest2Div", "display: block;");//目的地2
		}else{
			jsonVO.put("dest2Div", "display: none;");//目的地2不显示
		}
		if(map.get("dest3")!=null&&""!=map.get("dest3")){
			jsonVO.put("dest3Div", "display: block;");//目的地3
		}else{
			jsonVO.put("dest3Div", "display: none;");//目的地3不显示
		}
		jsonVO.put("dest2", map.get("dest2"));//目的地2
		jsonVO.put("dest3", map.get("dest3"));//目的地3
		//司机信息存为json对象
		JSONObject jsonobj_driver = new JSONObject();
		jsonobj_driver.put("vehicleno", map.get("vehicleno")==null?"":map.get("vehicleno"));
		jsonobj_driver.put("pk_vehicle", map.get("pk_vehicle")==null?"":map.get("pk_vehicle"));
		jsonobj_driver.put("driver", map.get("driver"));
		jsonobj_driver.put("dphone", map.get("dphone"));
		jsonobj_driver.put("pk_driver", map.get("pk_driver"));
		jsonVO.put("selectedDrivers", jsonobj_driver);//车辆号的对象
		JSONObject jsonobj=querVehicle(json);
		JSONArray drivers = jsonobj.getJSONArray("values");//查询司机数组
		JSONArray jsonVehicleArray = jsonobj.getJSONArray("jsonVehicleArray");//查询车辆数组
		//jsonVO.put("jsonVehicleArray",  jsonVehicleArray);//车辆	
		JSONObject jsonobj_vehicle = new JSONObject();
		jsonobj_vehicle.put("vehicleno", map.get("vehicleno")==null?"":map.get("vehicleno"));
		jsonobj_vehicle.put("pk_vehicle", map.get("pk_vehicle")==null?"":map.get("pk_vehicle"));
		jsonVO.put("selectedVehicle", jsonobj_vehicle);//车辆号的对象
		
		jsonVO.put("jsonVehicleArray",  jsonVehicleArray);//车辆	
		//drivers.put(jsonobj_vehicle);
		String departtime = (String) map.get("departtime");
		if (departtime != null && !departtime.equals("")) {
			departtime = departtime.substring(0, 16);
		}
		jsonVO.put("drivers",  drivers);//司机	
		jsonVO.put("departtime", departtime);//出发时间
		String returntime = (String) map.get("returntime");
		if (returntime != null && !returntime.equals("")) {
			returntime = returntime.substring(0, 16);
		}		
		jsonVO.put("returntime", returntime);//返回时间
		jsonVO.put("iscarpool", map.get("iscarpool"));//能否拼车
		jsonVO.put("billstate", state.get(map.get("billstate")));//状态
		jsonVO.put("vehicleno", map.get("vehicleno")==null?"":map.get("vehicleno"));//车牌号
		jsonVO.put("pk_vehicle", map.get("pk_vehicle")==null?"":map.get("pk_vehicle"));//车辆主键
		jsonVO.put("remark", map.get("remark"));//事由
		jsonVO.put("turndownreason", map.get("turndownreason"));//驳回原因
		jsonVO.put("startMileage", map.get("startmileage")==null?"":map.get("startmileage"));//出发里程
		jsonVO.put("backMileage", map.get("backmileage")==null?"":map.get("backmileage"));//返回里程
		jsonVO.put("travelMileage", map.get("travelmileage")==null?"":map.get("travelmileage"));//行驶里程
		jsonVO.put("read", true);// 只读
		
		//循环表体，得到表体list
		JSONArray jsonDetails = new JSONArray(); //表体JSON数据合计
		for (Map<String, Object> map_detail : list_detail) {
			JSONObject jsonDetail = new JSONObject();
			int PNum =Integer.parseInt(map_detail.get("selectpnum").toString()); // 人数
			int  selectpnum[]={PNum};
			jsonDetail.put("selectedPNum", selectpnum);//人数
			jsonDetail.put("applier", map_detail.get("applier"));//人员主键
			jsonDetail.put("username", map_detail.get("username"));//姓名
			jsonDetail.put("userphone", map_detail.get("phone"));//联系电话
			jsonDetail.put("finaldest", map_detail.get("finaldest"));//目的地
			jsonDetail.put("starlevel", map_detail.get("starlevel")==null?"":map.get("starlevel"));//评分
			jsonDetail.put("review", map_detail.get("review")==null?"":map.get("review"));//评价
			jsonDetail.put("read", true);// 只读
			jsonDetails.put(jsonDetail);
		}
		jsonVO.put("user", jsonDetails);
		
		result.put("values", jsonVO);
		result.put("result", "true");
		return result;

	}
	/**
	 * 更新单据状态
	 * @param jsonObject
	 * @return
	 */
	private JSONObject updateStaue(JSONObject json) throws DAOException, JSONException{
		HYPubBO hYPubBO = new HYPubBO();
		JSONObject result = new JSONObject();
		String pk_vorder = json.getString("pk_vorder"); // 单据主键
		String billstate = json.getString("billstate"); // 单据状态
		String origin = json.getString("origin"); // 出发地
		String fdestination = json.getString("destarea"); // 最终目的地
		String pk_driver = json.getString("pk_driver"); // 司机
		String vstate = json.getString("vstate"); // 车辆状态
		String pk_vehicle = json.getString("pk_vehicle"); // 车辆主键
		//拼接审批通过提示信息
		String message = "";
		//更新表头数据状态
		String sql="update cl_vorder set billstate='"+billstate
				       +"' where pk_vorder='"+pk_vorder+"'";
		//同步更新车辆的状态
		String sql_vehicle="update cl_vehicle set vstate='"+vstate
					 +"' where pk_vehicle='"+pk_vehicle+"'";
		//执行sql更改单据状态
		try {
			dao.executeUpdate(sql);
			dao.executeUpdate(sql_vehicle);
		} catch (DAOException e1) {
			ExceptionUtils.wrappBusinessException("更改单据状态失败！"+e1.getMessage());
		}
	
		//查询用车时间的sql
		String timesql = "select departtime from cl_vorder_b where pk_vorder = '"+pk_vorder+"'";
		//用车时间
		String departtime = (String) new BaseDAO().executeQuery(timesql, new ColumnProcessor());
		message = "。\n用车时间："+departtime+"。\n行程："+origin+"--"+fdestination+"。";
		//找到出发地、目的地，出发时间，司机相同的主表pk和子表pk
		String sameSql = "select u.user_name,h.pk_vorder,b.pk_vorder_b,b.phone，b.ts from cl_vorder h, cl_vorder_b b "
				+ ",sm_user u where h.pk_vorder = b.pk_vorder and b.origin = '"+origin+"' "
						+ "and b.finaldest = '"+fdestination+"' and b.departtime "
								+ "= '"+departtime+"' and pk_driver = '"+pk_driver+"' "
										+ "and h.dr = 0 and b.dr = 0 and billstate!=9 and u.cuserid = b.applier";
		//查询出满足以上条件的子表的pk
		List<Map<String, Object>> pklist=(List<Map<String, Object>>)dao.executeQuery(sameSql, new  MapListProcessor());
		
		VorderBVO bvo = null;
		VorderBVO[] bvos = new VorderBVO[pklist.size()];
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//遍历子表pk集合
		//得到第一申请人的ts
		if (pklist != null&&pklist.size()>0) {
			
			//比较，如果日期
			Date maxdate = null;
			try {
				maxdate = sf.parse((String)pklist.get(0).get("ts"));
			} catch (ParseException e3) {
				ExceptionUtils.wrappBusinessException("日期转换失败！"+e3.getMessage());
			}
			//得到第一申请人的ts（即日期最小的那个ts）
			for (int j = 1; j < pklist.size(); j++) {	
				//取出ts依次比较
				String olddate = (String) pklist.get(j).get("ts");
				Date nextts = null;
				try {
					nextts = sf.parse(olddate);
				} catch (ParseException e) {
					ExceptionUtils.wrappBusinessException("日期转换失败！"+e.getMessage());
				}
				if(nextts.compareTo(maxdate)<0){
					maxdate = nextts;
				}
			}
			//循环得到所有拼车人（只能在发消息之前得到）
			String text = "用车人："; 
			for (int i = 0; i < pklist.size(); i++) {
				Map<String, Object> map = pklist.get(i);
				//拼车人姓名
				String user_name = (String) map.get("user_name");
				if(i==pklist.size()-1){
					text += user_name+"。";
				}else{
					text += user_name+"，";
				}
			}
			message = message + "\n" + text;
			for (int i = 0; i < pklist.size(); i++) {
				String isfirstApplier = "N";
				//取出每个子表pk
				Map<String, Object> map = pklist.get(i);
				String hvopk = (String) map.get("pk_vorder");
				String bvopk = (String) map.get("pk_vorder_b");
				//取出每个拼车人的电话
				String phone = (String) map.get("phone");
				//得到ts判断是否是第一申请人
				String ts = (String) map.get("ts");
				Date date = null;
				try {
					date =  sf.parse(ts);
				} catch (ParseException e2) {
					ExceptionUtils.wrappBusinessException("日期转换失败！"+e2.getMessage());
				}
				//根据子表pk得到子表对象
				try {
					bvo = (VorderBVO) hYPubBO.queryByPrimaryKey(VorderBVO.class, bvopk);
				} catch (UifException e) {
					ExceptionUtils.wrappBusinessException("得到子表对象失败！"+e.getMessage());
				}
				//如果当前人的ts==第一申请人的ts，设置为第一申请人
				if(date.compareTo(maxdate)==0){
					isfirstApplier = "Y";
				}
				
				//将得到的bvo对象的关联的pk_vorder改成和传入的pk_vorder一样
				String updatehpk = "update cl_vorder_b set isfisrtapplier = '"+isfirstApplier+"', pk_vorder = '"+pk_vorder+"' where pk_vorder_b ='"+bvopk+"' ";
				try {
					dao.executeUpdate(updatehpk);
				} catch (DAOException e1) {
					ExceptionUtils.wrappBusinessException("关联子表失败！"+e1.getMessage());
				}
				//子表合并
				bvos[i] = bvo;
				if(!hvopk.equals(pk_vorder)){
					String updateDrsql = "update cl_vorder set dr = 1 where pk_vorder = '"+hvopk+"'";
					try {
						dao.executeUpdate(updateDrsql);
					} catch (DAOException e) {
						ExceptionUtils.wrappBusinessException("删除多余子表单据失败！"+e.getMessage());
					}
				}
				//友空间给拼车人发送消息，4通过
				if("4".equals(billstate)){
					//车管通过申请单，发送通过的提示信息给拼车人（最后一个参数0：给用户发通过消息;1：给司机发通过的消息;3：给用户发不通过的消息）
					sendYonyouMessage(message,phone,0);
				}else{
					//车管驳回申请单，设置不通过的提示信息，并发送个拼车人
					message = "您提交的用车申请单已被驳回，请修改单据后重新提交";
					sendYonyouMessage(message,phone,3);
				}
			}
		}
		
		
		//“4”代表审批通过，如果审批通过还需要给司机发送通知
		if("4".equals(billstate)){	
			//查询司机电话,用于给司机发送通知
			String driversql = "select dphone from cl_vorder where pk_vorder = '"+pk_vorder+"'";
			//得到司机的电话
			String dphone = "";
			try {
				dphone = (String) new BaseDAO().executeQuery(driversql, new ColumnProcessor());
				//友空间给司机发送消息
				sendYonyouMessage(message,dphone,1);
			} catch (DAOException e) {
				ExceptionUtils.wrappBusinessException("查询司机电话失败！"+e.getMessage());
			}
		}else{
			//更新驳回原因
			String turndownreason = "";
			if(StringUtils.isNotBlank(json.getString("turndownreason"))){
				turndownreason=	json.getString("turndownreason"); //当前登录人主键
				String sql_turndownreason="update cl_vorder_b set turndownreason='"+turndownreason+"'"
						+ " where pk_vorder='"+pk_vorder+"'";
				dao.executeUpdate(sql_turndownreason);
			}
			//如果单据被驳回，调用弃审方法修改单据的审批状态
			VorderHVO oldhvo = null;;
			try {
				oldhvo = (VorderHVO) hYPubBO.queryByPrimaryKey(VorderHVO.class, pk_vorder);
			} catch (UifException e) {
				ExceptionUtils.wrappBusinessException("查询主表信息失败！"+e.getMessage());
			}
			//要修改审批状态为弃审（回到提交状态）的主表VO
			VorderHVO newhvo = (VorderHVO) oldhvo.clone();
			//设置审批状态为提交
			newhvo.setAttributeValue("approvestatus", 3);
			//原有的Aggvo数组（已审批状态）
			AggVorderHVO[] oldAggvos = getAggVOs(oldhvo, bvos);	
			//新的Aggvo数组（提交状态状态）
			AggVorderHVO[] newAggvos = getAggVOs(newhvo, bvos);
			//调用弃审方法
			//new AceVorderUnApproveBP().unApprove(newAggvos,oldAggvos);
		}
		return result;
	}
	/**
	 * 更新单据信息
	 * @param jsonObject
	 * @return
	 * @throws ParseException 
	 * @throws Exception 
	 */
	private JSONObject updateVorder(JSONObject json) throws DAOException, JSONException, ParseException{
	
		JSONObject result = new JSONObject();
		String pk_vorder = json.getString("pk_vorder"); // 主键
		String driver =json.getString("driver"); // 司机
		String pk_driver =json.getString("pk_driver"); // 司机主键
		String dphone =json.getString("dphone"); // 司机电话
		String billstate =json.getString("billstate"); // 单据状态
		String pk_vehicle ="";
		if(json.get("pk_vehicle")!=null){
			pk_vehicle=json.getString("pk_vehicle"); // 车牌号
		}
		String vehicleno ="";
		if(json.get("vehicleno")!=null){
			vehicleno=json.getString("vehicleno"); // 车牌号
		}
		//更新前校验司机  kkk
		String departtime =json.getString("departtime"); // 出发时间
		String returntime =json.getString("returntime"); // 返回时间
		JSONObject check_driver = new JSONObject();
		check_driver.put("pk_driver", pk_driver);
		check_driver.put("departtime", departtime);
		check_driver.put("returntime", returntime);
		check_driver.put("pk_vorder", pk_vorder);
		JSONObject check=updateCheck(check_driver);
		JSONObject ret=check.getJSONObject("values");
		if(!ret.getBoolean("isvord")&&!ret.getBoolean("iscarpool")){
			result.put("result", "false");
			result.put("message", ret.get("message").toString());
			return result;
		}
		//更新表头数据
		String hvosql="update cl_vorder set driver='"+driver
					   +"' , pk_driver='"+pk_driver
					   +"' , dphone='"+dphone
					   +"' , pk_vehicle='"+pk_vehicle
					   +"' , vehicleno='"+vehicleno
				       +"' where pk_vorder='"+pk_vorder+"'";
		dao.executeUpdate(hvosql);
		if("已完成".equals(billstate)){//如果 单据状态为已完成，更新里程
			String startMileage =json.getString("startMileage"); 
			String backMileage =json.getString("backMileage"); 
			String travelMileage =json.getString("travelMileage"); 
			//更新表头数据
			String sql="update cl_vorder set startMileage='"+startMileage
						   +"' , backMileage='"+backMileage
						   +"' , travelMileage='"+travelMileage
					       +"' where pk_vorder='"+pk_vorder+"'";
			dao.executeUpdate(sql);
		}
		result.put("result", "true");
		return result;
	}
	/**
	 * 查询车辆信息
	 * @param jsonObject
	 * @return
	 */
	private JSONObject querVehicle(JSONObject jsonObject) throws DAOException, JSONException{
		JSONArray jsonArr = new JSONArray(); //用于存储单据信息集合
		//过滤掉休息和外出的司机
		String sql_alldrivers="select cl_driver.pk_driver,cl_driver.dname,cl_driver.dphone,cl_vehicle.pk_vehicle,cl_vehicle.vehicleno "
				+ " from cl_driver "
				+ " left join cl_vehicle on cl_vehicle.pk_driver=cl_driver.pk_driver and cl_vehicle.dr=0"
				+ " where cl_driver.dr=0 and dstate not in (2,4)";
		
		List<Map<String,Object>> list=(List<Map<String, Object>>) dao.executeQuery(sql_alldrivers, new  MapListProcessor());
		for (Map<String, Object> map : list) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("pk_driver", map.get("pk_driver"));//司机主键
			jsonObj.put("pk_vehicle", map.get("pk_vehicle")==null?"":map.get("pk_vehicle"));//车辆主键
			jsonObj.put("vehicleno", map.get("vehicleno")==null?"":map.get("vehicleno"));//车牌号
			jsonObj.put("driver", map.get("dname"));//司机姓名
			jsonObj.put("dphone", map.get("dphone"));//司机电话
			jsonArr.put(jsonObj);
		}
		//存放所有车牌号信息的JSONArray
		JSONArray jsonVehicleArray = new JSONArray();
		//查询车牌号和车牌号pk的sql，条件是所有dr不为0的
		String sql_vehicle = "select pk_vehicle,vehicleno from cl_vehicle"
				+ " where cl_vehicle.dr=0";
		//存放车牌号额list
		List<Map<String,String>> list_vehicle= (List<Map<String, String>>) dao.executeQuery(sql_vehicle, new MapListProcessor());
		//遍历
		for (Map<String, String> map : list_vehicle) {
			//存放一条车牌号信息的json
			JSONObject jsonObj = new JSONObject();
			//将车牌号pk放入jsonObj
			jsonObj.put("pk_vehicle", map.get("pk_vehicle"));
			//将车牌号放入jsonObj
			jsonObj.put("vehicleno", map.get("vehicleno"));
			//将这条车牌号信息放入存放所有车牌号信息的jsonArray中
			jsonVehicleArray.put(jsonObj);
		}
		JSONObject result = new JSONObject();
		result.put("values", jsonArr);//司机
		result.put("jsonVehicleArray", jsonVehicleArray);//车辆
		result.put("result", "true");
		return result;	
	}
	
	/**
	 * 过滤司机
	 * @param jsonObject
	 * @return
	 * @throws JSONException 
	 * @throws DAOException 
	 * @throws ParseException 
	 */
	private JSONObject filterDriver(JSONObject json) throws JSONException, DAOException, ParseException {
		JSONObject result = new JSONObject();
		JSONArray jsonArr_vord = new JSONArray(); //用于存储正常用车的司机集合
		String selectDeparttime = json.getString("departtime"); //出发时间
		String selectReturntime = json.getString("returntime"); //返回时间
		//查询所有的司机,不包括外出和休息的
		String sql_alldrivers="select cl_driver.pk_driver,cl_driver.dname,cl_driver.dphone,cl_vehicle.pk_vehicle,cl_vehicle.vehicleno "
				+ " from cl_driver "
				+ " left join cl_vehicle on cl_vehicle.pk_driver=cl_driver.pk_driver and cl_vehicle.dr=0"
				+ " where cl_driver.dr=0 and dstate not in (2,4)";
		List<Map<String,String>> list_alldrivers= (List<Map<String, String>>) dao.executeQuery(sql_alldrivers, new  MapListProcessor());
		for(Map<String,String> driverMap:list_alldrivers){//遍历司机的主键
			Boolean isvord=false;//是否能正常用车
			String pk_driver=driverMap.get("pk_driver");//司机主键
			String dname=driverMap.get("dname");//司机姓名
			String dphone=driverMap.get("dphone");//司机电话
			String pk_vehicle=driverMap.get("pk_vehicle")==null?"":driverMap.get("pk_vehicle");//车辆主键
			String vehicleno=driverMap.get("vehicleno")==null?"":driverMap.get("vehicleno");//车牌号
			//查询符合该司机的订单（表体信息为第一申请人的）
			String sql="select * "
					+" from cl_vorder "
					+" inner join cl_vorder_b"
					+" on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0"
					+" where pk_driver='"+pk_driver+"' "
					+ " and cl_vorder.dr=0 "
					+" and billstate !=9 and billstate !=1"
					//查询前后四天的订单
					+" and trunc(to_date(substr(departtime,0,10),'yyyy-mm-dd')) BETWEEN  trunc(sysdate)-4 and trunc(sysdate)+4"
					+ " order by cl_vorder_b.departtime ";
			List<Map<String,String>> list_vords=(List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
			//出发时间和返回时间都不为空
			//比较用户选择的时间和用车时间,在30分钟内可以拼车，超过时间提示该时间段被占用
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long time = 10*60*1000;//10分钟
			//用户选择的出发时间
			Date selectDepartdate = sf.parse(selectDeparttime+":00");
			//用户选择的返回时间
			Date selectReturndate = sf.parse(selectReturntime+":00");
			if(list_vords.size()>0){
				for(int i = 0; i < list_vords.size(); i++){
					Map<String, String> map = list_vords.get(i);
					//出发时间
					Date departtime = sf.parse((String)map.get("departtime"));
					//返回时间
					Date returntime = sf.parse((String)map.get("returntime"));
					 if(selectDepartdate.after(new Date(returntime.getTime() + time))){//用户出发时间>返回时间+30，可以正常用车
						isvord=true;//正常用车
					}else if(selectReturndate.before(new Date(departtime.getTime() - time))){//用户返回时间<用车时间-30，可以正常用车
						isvord=true;//正常用车
					}else{//否则提示该时间段被占用
						isvord=false;//不能正常用车
						break;
					}	
				}
			}else{
				isvord=true;//可以正常用车
			}
			 if(isvord){//可以正常用车
				JSONObject jsonVO = new JSONObject();
				jsonVO.put("pk_driver", pk_driver);
				jsonVO.put("driver", dname);
				jsonVO.put("dphone", dphone);
				jsonVO.put("pk_vehicle", pk_vehicle);
				jsonVO.put("vehicleno", vehicleno);
				jsonArr_vord.put(jsonVO);
			}
		}
		
		result.put("values", jsonArr_vord);
		result.put("result", "true");
		return result;
		
	}
	
	/**
	 * 更新校验
	 * @param jsonObject
	 * @return
	 * @throws JSONException 
	 * @throws DAOException 
	 * @throws ParseException 
	 */
	private JSONObject updateCheck(JSONObject jsonObject) throws JSONException, DAOException, ParseException {
		JSONObject result = new JSONObject();
		String pk_driver = jsonObject.getString("pk_driver"); // 车辆主键
		Date now = new Date(); //系统当前时间
		boolean isupdate=false;//是否为更新状态
		String pk_vorder ="";
		if(StringUtils.isNotBlank(jsonObject.getString("pk_vorder"))){//主键不为空
			isupdate=true;//为编辑态的修改，查询时需要过滤掉当前一条
			pk_vorder= jsonObject.getString("pk_vorder"); //当前单据主键
		}
		//查询该车辆的订单（表体信息为第一申请人的）
		String sql="select cl_vorder.*,cl_vorder_b.*,sm_user.user_name username "
				+" from cl_vorder "
				+" inner join cl_vorder_b"
				+" on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0"
				+" left join sm_user on sm_user.cuserid  =cl_vorder_b.applier and sm_user.dr=0"
				+" where pk_driver='"+pk_driver+"' "
				+" and billstate !=9 and billstate !=1"
			//	+" and isfisrtapplier='Y'"
				+ " and cl_vorder.dr=0 "
				+" and trunc(to_date(substr(departtime,0,10),'yyyy-mm-dd')) BETWEEN  trunc(sysdate) and trunc(sysdate)+1"
				+ ((isupdate) ? " and cl_vorder_b.pk_vorder!='"+pk_vorder+"'" : "")
				+ " order by cl_vorder_b.departtime ";
		List<Map<String,String>> list=(List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
		String selectDeparttime = jsonObject.getString("departtime"); //出发时间
		String selectReturntime = jsonObject.getString("returntime"); //返回时间
		JSONObject jsonVO = new JSONObject();
		String message="";//返回前台的提示信息
		Boolean isvord=true;//是否能正常用车
		Boolean iscarpool=false;//是否能拼车
		//出发时间和返回时间都不为空
		if(StringUtils.isNotBlank(selectDeparttime)&&StringUtils.isNotBlank(selectReturntime)){
			//比较用户选择的时间和用车时间,在30分钟内可以拼车，超过时间提示该时间段被占用
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long time = 10*60*1000;//10分钟
			//用户选择的出发时间
			Date selectDepartdate = sf.parse(selectDeparttime+":00");
			//用户选择的返回时间
			Date selectReturndate = sf.parse(selectReturntime+":00");
			if(list.size()>0){
				for(int i = 0; i < list.size(); i++){
					Map<String, String> map = list.get(i);
					//出发时间
					Date departtime = sf.parse((String)map.get("departtime"));
					//返回时间
					Date returntime = sf.parse((String)map.get("returntime"));
					//查询该时间段在申请中和部门审批完成的订单，人数小于4的可以选择,是否可拼车，值为‘否’的 也不能拼车
					String sql_vehicle="select sum(selectpnum) selectpnum from cl_vorder"
							+ " inner join cl_vorder_b on cl_vorder_b.pk_vorder= cl_vorder.pk_vorder and cl_vorder_b.dr=0"
							+ " where cl_vorder.dr=0 and departtime='"+map.get("departtime")+"' and returntime='"+map.get("returntime")+"'"
							+ " and cl_vorder.billstate in ('2','3','4') and iscarpool='Y' and pk_driver='"+pk_driver+"'"
							+ " group by pk_vehicle, iscarpool,cl_vorder_b.origin, cl_vorder_b.finaldest, cl_vorder_b.departtime";
					Object selectpnum =  dao.executeQuery(sql_vehicle, new ColumnProcessor());
					//Boolean billiscarpool=true;//该时间段单据能不能拼车
					int selectpnums=0;
					if(selectpnum!=null){		
						selectpnums=Integer.parseInt(selectpnum.toString());//用车人数}
					}
				    //用户的出发时间>出发时间-30 并且 用户的返回时间<返回时间+30（可拼车）
					if(selectDepartdate.after(new Date(departtime.getTime()- time))&&selectReturndate.before(new Date(returntime.getTime()+ time))
							&&selectpnums<4&&0<selectpnums&&departtime.after(new Date(now.getTime()+ time))){//如果在时间范围之内可以拼车
						isvord=false;
						iscarpool=true;//可以拼车
						//可以拼车的话带出拼车信息
						jsonVO.put("pk_vorder", map.get("pk_vorder"));
						jsonVO.put("pk_vorder_b", map.get("pk_vorder_b"));
						jsonVO.put("read", true);//只读
						int allPNum=4;//车上一共4个座位
						jsonVO.put("remainPNum", allPNum-selectpnums);//剩余人数
						String selectedOrigins[]={map.get("origin").toString()};
						
						jsonVO.put("username", map.get("username"));//姓名//人员
						jsonVO.put("selectedOrigins", selectedOrigins);//出发地
						String selectedDestAreas[]={map.get("destarea").toString()};
						jsonVO.put("selectedDestAreas", selectedDestAreas);//目标区域
						jsonVO.put("finaldest", map.get("finaldest"));//最终目的地
						jsonVO.put("pk_driver", pk_driver);//司机主键
						jsonVO.put("driver", map.get("driver"));//司机
						jsonVO.put("dPhone", map.get("dphone"));//司机电话
						jsonVO.put("vehicleno", map.get("vehicleno")==null?"":map.get("vehicleno"));//车牌号
						jsonVO.put("pk_vehicle", map.get("pk_vehicle")==null?"":map.get("pk_vehicle"));//车量主键
						jsonVO.put("turndownreason", map.get("turndownreason")==null?"":map.get("turndownreason"));//驳回原因
						String billdeparttime = (String) map.get("departtime");
						if (billdeparttime != null && !billdeparttime.equals("")) {
							billdeparttime = billdeparttime.substring(0, 16);
						}		
						jsonVO.put("departtime", billdeparttime);//出发时间
						
						String billreturntime = (String) map.get("returntime");
						if (billreturntime != null && !billreturntime.equals("")) {
							billreturntime = billreturntime.substring(0, 16);
						}		
						jsonVO.put("returntime", billreturntime);//返回时间
						jsonVO.put("iscarpool", "Y".equals(map.get("iscarpool"))?true:false);//是否拼车
						jsonVO.put("dest2Div", "display: none;");//目的地2不显示
						jsonVO.put("dest3Div", "display: none;");//目的地3不显示
						break;
					}else if(selectDepartdate.after(new Date(returntime.getTime() + time))){//用户出发时间>返回时间+30，可以正常用车
						isvord=true;//正常用车
						iscarpool=false;//不能拼车
					}else if(selectReturndate.before(new Date(departtime.getTime() - time))){//用户返回时间<用车时间-30，可以正常用车
						isvord=true;//正常用车
						iscarpool=false;//不能拼车
					}else{//否则提示该时间段被占用
						iscarpool=false;//不能拼车
						isvord=false;//不能正常用车
						String departday = (String) map.get("departtime").substring(0, 16);
						String returnday = (String) map.get("returntime").substring(0, 16);
						message=message+"该司机在"+ "\n"+departday+"~"+returnday+ "\n"+"被占用，请重新选择用车时间";
						break;
					}
				}
				jsonVO.put("message", message);//提示信息
				jsonVO.put("isvord", isvord);
				jsonVO.put("iscarpool", iscarpool);
			}else{//该司机没有订单
				jsonVO.put("isvord", true);
				jsonVO.put("iscarpool", false);
			}
		}else{
			//查询时间给申请人提示可以选择的时间
			//如果该司机有未完成的申请单，给出如下提示信息
			String departday = "";
			if(list.size()>0){
				message="该司机在"+ "\n";
				for(int i = 0; i < list.size(); i++){
					Map<String, String> map = list.get(i);
					//时间截取年月日时分
					if(departday.equals((String)map.get("departtime").substring(0, 16))) continue;
					departday = (String) map.get("departtime").substring(0, 16);
					String returnday = (String) map.get("returntime").substring(0, 16);
					if(i==list.size()-1){
						message=message+departday+"~"+returnday+ "\n";
					}else{
						message=message+departday+"~"+returnday+","+ "\n";
					}
				}
				message=message+"被占用，请合理选择用车时间";
			}
			jsonVO.put("message", message);//提示信息
			jsonVO.put("iscarpool", false);//是否拼车为否
			jsonVO.put("isvord", true);//正常用车
		}
		result.put("values", jsonVO);
		result.put("result", "true");
		return result;	
	}
	/**
	 * 发送友空间消息
	 * @return
	 */
	private boolean sendYonyouMessage(String message,String field,Integer num){
		boolean messageResult = false;
		// 获取access_token
		String accessToken = YonyouMessageUtil.getAccessToken();
		// TODO 接收人需要更改为动态人员
		/*field = "15541523099";*/
		String fieldtype = "1";
		// 获取MemberId（1：手机 2：邮箱）
		String memberId = YonyouMessageUtil.getMemberId(accessToken, field, fieldtype);
		List<String> tos = new ArrayList<String>();
		tos.add(memberId);
		String text = "";
		if(num == 0){
			text = "您提交的用车申请单已审批通过，请注意用车时间";
		}else if(num == 1){
			text = "有新的用车申请单已审批通过，请及时查看处理";
		}
		//拼接完整的提示信息
		message = text + message;
		//发送消息
		messageResult = YonyouMessageUtil.sendMessage(accessToken, YonyouMessageUtil.messagePojo(tos ,"车辆申请单审批提醒", message));
		return messageResult;
	}
	
	
	/**
	 * 获得aggvo【】的方法
	 * @return 
	 * @return
	 */
	private AggVorderHVO[] getAggVOs(VorderHVO hvo,VorderBVO[] bvos){
		//新建aggVorderHVO对象
		AggVorderHVO aggVorderHVO = new AggVorderHVO();
		//赋值--主表
		aggVorderHVO.setParentVO(hvo);
		//子表
		aggVorderHVO.setChildrenVO(bvos);
		//新建AggVorderHVO【】并赋值
		AggVorderHVO[] aggVorderHVOs = new AggVorderHVO[]{aggVorderHVO};
		//返回AggVorderHVO【】数组
		return aggVorderHVOs;
	}
	
	/**
	 * 查询组织编码
	 * @param jsonObject
	 * @return
	 */
	private JSONObject queryCode(JSONObject jsonObject) throws DAOException, JSONException{
		//根据主键查询组织编码
		String sql="select code from org_orgs where pk_org = '" + jsonObject.getString("pk_org")+ "'";
		
		String code=(String) dao.executeQuery(sql, new  ColumnProcessor());
	
		JSONObject result = new JSONObject();
		result.put("values", code);//司机
		result.put("result", "true");
		return result;	
	}
}
