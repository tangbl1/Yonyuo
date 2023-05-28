package nc.itf.vord.servlet;

import disposition.GetDatasourceName;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.comn.NetStreamContext;
import nc.bs.framework.core.service.IFwLogin;
import nc.bs.trade.business.HYPubBO;
import nc.bs.uap.lock.PKLock;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.md.model.impl.MDEnum;
import nc.ui.pub.beans.constenum.IConstEnum;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
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
@WebServlet("/voruserservlet")
public class VorderUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	BaseDAO dao = new BaseDAO();
	HYPubBO hypubBo = new HYPubBO();
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
	 * 用户申请单页面servlet
	 * 用于处理用户页面请求
	 * @author 周静
	 * @date 2019-11-20
	 * @param 
	 * 	req 前台页面请求
	 * 	resp 返回信息
	 */
	@SuppressWarnings("unchecked")
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
		try {
			if (method.equals("add")) {
				result = addVorder(jsonObject);//添加订单
			} else if (method.equals("query")) {
				result =queryVorder(jsonObject);//查询订单
			} else if (method.equals("delete")) {
				result = deleVorder(jsonObject);//删除订单
			} else if (method.equals("update")) {
				result = updateVorder(jsonObject);//修改时保存
			}else if (method.equals("query_vehicle")) {
				result = querVehicle(jsonObject);//查询车辆信息
			}else if (method.equals("remark")) {
				result = remarkVorder(jsonObject);//评论订单
			}else if (method.equals("iscarpool")) {
				result = isCarPool(jsonObject);//是否拼单
			}else if (method.equals("carpool")) {
				result = carPool(jsonObject);//拼单
			}else if (method.equals("query_remark")) {
				result = queryRemark(jsonObject);//判断订单是否已经被评价
			}else if (method.equals("filterdriver")) {
				result = filterDriver(jsonObject);//过滤司机
			}
		}  catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		resp.setContentType("text/html");
		resp.setCharacterEncoding("GBK");
		resp.getWriter().write(result.toString());
		resp.flushBuffer();

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
		Integer num = 0;
		String pk_vorder_b = jsonObject.getString("pk_vorder_b"); // 单据主键
		String sql = "select review,starlevel from cl_vorder_b where pk_vorder_b = '"+pk_vorder_b+"'";//查询评价和星级
		Map<String,Integer> map = (Map<String, Integer>) dao.executeQuery(sql, new MapProcessor());
		if(map.get("starlevel")!=null){//如果已经被评价，num设置为1
			num = 1;
		}
		
		result.put("num",num );//数量
		result.put("starlevel",map.get("starlevel"));//星级
		result.put("review",map.get("review")!=null?map.get("review"):"" );//评价
		result.put("result", "true");
		return result;
	}
	
	
	
	/**
	 * 判断是否拼车
	 * @param jsonObject
	 * @return
	 * @throws JSONException 
	 * @throws DAOException 
	 * @throws ParseException 
	 */
	private JSONObject isCarPool(JSONObject jsonObject) throws JSONException, DAOException, ParseException {
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
	 * 过滤司机
	 * @param json
	 * @return
	 * @throws JSONException 
	 * @throws DAOException 
	 * @throws ParseException 
	 */
	private JSONObject filterDriver(JSONObject json) throws JSONException, DAOException, ParseException {
		JSONObject result = new JSONObject();
		JSONArray jsonArr = new JSONArray(); //用于存储司机集合
		JSONArray jsonArr_pool = new JSONArray(); //用于存储拼车的司机集合
		JSONArray jsonArr_vord = new JSONArray(); //用于存储正常用车的司机集合
		String cuserid = json.getString("cuserid"); //当前登录人主键
		String origin = json.getString("origin"); // 出发地
		String destarea = json.getString("destarea");// 目标区域
		String selectDeparttime = json.getString("departtime"); //出发时间
		String selectReturntime = json.getString("returntime"); //返回时间
		boolean isupdate=false;//是否为更新状态
		String pk_vorder ="";
		if(StringUtils.isNotBlank(json.getString("pk_vorder"))){//主键不为空
			isupdate=true;//为编辑态的修改，查询时需要过滤掉当前一条
			pk_vorder= json.getString("pk_vorder"); //当前单据主键
		}
		Date now = new Date(); //系统当前时
		//查询所有的司机,不包括外出和休息的
		String sql_alldrivers="select cl_driver.pk_driver,cl_driver.dname,cl_driver.dphone,cl_vehicle.pk_vehicle,cl_vehicle.vehicleno "
				+ " from cl_driver "
				+ " left join cl_vehicle on cl_vehicle.pk_driver=cl_driver.pk_driver and cl_vehicle.dr=0"
				+ " where cl_driver.dr=0 and dstate not in (2,4) order by length(dname),dname";
		List<Map<String,String>> list_alldrivers= (List<Map<String, String>>) dao.executeQuery(sql_alldrivers, new  MapListProcessor());
		for(Map<String,String> driverMap:list_alldrivers){//遍历司机的主键
			Boolean isvord=true;//是否能正常用车
			Boolean iscarpool=false;//是否能拼车
			Boolean isContinueCuserid=false;//是否为当前用户
			String pk_driver=driverMap.get("pk_driver");//司机主键
			String dname=driverMap.get("dname");//司机姓名
			String dphone=driverMap.get("dphone");//司机电话
			String pk_vehicle=driverMap.get("pk_vehicle")==null?"":driverMap.get("pk_vehicle");//车辆主键
			String vehicleno=driverMap.get("vehicleno")==null?"":driverMap.get("vehicleno");//车牌号
			//查询符合该司机的订单（表体信息为第一申请人的）状态不为驳回和已完成
			String sql="select * "
					+" from cl_vorder "
					+" inner join cl_vorder_b"
					+" on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0"
					+" where pk_driver='"+pk_driver+"' "
					//+" and isfisrtapplier='Y'"
					+ " and cl_vorder.dr=0 "
					+" and billstate !=9 and billstate !=1"
					//查询前后四天的订单
					+" and trunc(to_date(substr(departtime,0,10),'yyyy-mm-dd')) BETWEEN  trunc(sysdate)-4 and trunc(sysdate)+4"
					+ ((isupdate) ? " and cl_vorder_b.pk_vorder!='"+pk_vorder+"'" : "")
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
					//申请人
					String cuserid_vord =(String) map.get("applier");
					
					//出发时间
					Date departtime = sf.parse((String)map.get("departtime"));
					//返回时间
					Date returntime = sf.parse((String)map.get("returntime"));
					//查询该时间段在申请中和部门审批完成的订单，人数小于4的可以选择,是否可拼车，值为‘否’的 也不能拼车,不能和自己拼车
					String sql_vehicle="select sum(selectpnum) selectpnum from cl_vorder"
							+ " inner join cl_vorder_b on cl_vorder_b.pk_vorder= cl_vorder.pk_vorder and cl_vorder_b.dr=0"
							+ " where cl_vorder.dr=0 and departtime='"+map.get("departtime")+"' and returntime='"+map.get("returntime")+"'"
							+ " and cl_vorder.billstate in ('2','3','4') and iscarpool='Y' "
							//+ "and applier!='"+cuserid+"'"
							+ " group by pk_driver, iscarpool,cl_vorder_b.origin, cl_vorder_b.finaldest, cl_vorder_b.departtime";
					
					
					Object selectpnum =  dao.executeQuery(sql_vehicle, new ColumnProcessor());
					//Boolean billiscarpool=true;//该时间段单据能不能拼车
					int selectpnums=0;
					if(selectpnum!=null){		
						selectpnums=Integer.parseInt(selectpnum.toString());//用车人数}
					}
				    //用户的出发时间>出发时间-30 并且 用户的返回时间<返回时间+30（可拼车）,出发地和目的地相同//可以拼车的
					//出发前半个小时不能拼车    用户的出发时间>当前系统时间+30
					if(selectDepartdate.after(new Date(departtime.getTime()- time))&&selectReturndate.before(new Date(returntime.getTime()+ time))
						&&origin.equals(map.get("origin"))&&destarea.equals(map.get("destarea"))&&selectpnums<4&&0<selectpnums&&departtime.after(new Date(now.getTime()+ time))){
						if(cuserid_vord.equals(cuserid)){
							isContinueCuserid=true;//是否为当前登录人
						}
						isvord=false;
						iscarpool=true;//可以拼车
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
						break;
					}	
				}
			}else{//可以正常用车
				iscarpool=false;//不能拼车
				isvord=true;//不能正常用车
			}
			if(!isvord&&iscarpool&&!isContinueCuserid){//可以拼车
				//可以拼车的话带出在司机字段后面加上可拼的标志
				JSONObject jsonVO = new JSONObject();
				jsonVO.put("pk_driver", pk_driver);
				jsonVO.put("driver", dname+"(可拼)");
				jsonVO.put("dphone", dphone);
				jsonVO.put("pk_vehicle", pk_vehicle);
				jsonVO.put("vehicleno", vehicleno);
				jsonArr_pool.put(jsonVO);
			}else if(isvord&&!iscarpool){//可以正常用车
				JSONObject jsonVO = new JSONObject();
				jsonVO.put("pk_driver", pk_driver);
				jsonVO.put("driver", dname);
				jsonVO.put("dphone", dphone);
				jsonVO.put("pk_vehicle", pk_vehicle);
				jsonVO.put("vehicleno", vehicleno);
				jsonArr_vord.put(jsonVO);
			}
		}
		
		jsonArr.put(jsonArr_pool);
		jsonArr.put(jsonArr_vord);
		result.put("values", jsonArr);
		result.put("jsonArr_pool", jsonArr_pool);
		result.put("jsonArr_vord", jsonArr_vord);
		result.put("result", "true");
		return result;	
	}
	/**
	 * 拼车
	 * @param json
	 * @return
	 * @throws BusinessException 
	 * @throws JSONException 
	 * @throws Exception 
	 */
	private JSONObject carPool(JSONObject json) throws Exception {

		String datasource = "";//数据源
		try {
			datasource = new GetDatasourceName().doreadxml();
		} catch (JDOMException e) {
			throw new RuntimeException("配置文件获取数据源名称失败！");
		}
		
		JSONObject result = new JSONObject();
		JSONObject userjsonObject = json.getJSONObject("userjson"); // 登录人
		String cuserid=userjsonObject.getString("cuserid");
		String pk_org=userjsonObject.getString("pk_org");
		String pk_group=userjsonObject.getString("pk_group");
		String pk_vorder = json.getString("pk_vorder"); // 主键
		String pk_vorder_b = json.getString("pk_vorder_b"); // 子表主键
		String dest1 =json.getString("dest1"); // 目的地1
		String dest2 =json.getString("dest2"); // 目的地2
		String dest3 =json.getString("dest3"); // 目的地3
		String reason =json.getString("reason"); // 事由
		int selectedPNum =Integer.parseInt(json.getString("selectedPNum")); // 人数
		String phone =json.getString("phone"); // 用户电话
		String pk_driver =json.getString("pk_driver"); // 司机主键
		//更新前校验司机  kkk
		String departtime =json.getString("departtime"); // 出发时间
		String returntime =json.getString("returntime"); // 返回时间
		JSONObject check_driver = new JSONObject();
		check_driver.put("pk_driver", pk_driver);
		check_driver.put("departtime", departtime);
		check_driver.put("returntime", returntime);
		check_driver.put("pk_vorder", "");
		JSONObject check=isCarPool(check_driver);
		JSONObject ret=check.getJSONObject("values");
		if(!ret.getBoolean("isvord")&&!ret.getBoolean("iscarpool")){
			result.put("result", "false");
			result.put("message", ret.get("message").toString());
			return result;
		}
		
		//旧的vo
		VorderHVO hvo = (VorderHVO) hypubBo.queryByPrimaryKey(VorderHVO.class, pk_vorder);
		VorderBVO bvo = (VorderBVO) hypubBo.queryByPrimaryKey(VorderBVO.class, pk_vorder_b);
		//设置pk，单据号，单据状态为空
		hvo.setAttributeValue("pk_vorder", null);
		hvo.setAttributeValue("ts", null);
		hvo.setAttributeValue("billno", null);
		UFDate vbilldate = new UFDate();
		hvo.setAttributeValue("vbilldate",vbilldate);
		hvo.setAttributeValue("approvestatus", -1);
		hvo.setAttributeValue("billstate", "2");//创建单据时单据状态为申请中
		hvo.setAttributeValue("pk_group", pk_group);
		hvo.setAttributeValue("pk_org", pk_org);
		bvo.setAttributeValue("pk_vorder_b", null);
		bvo.setAttributeValue("dest1", dest1);//目的地1
		bvo.setAttributeValue("dest2", dest2);//目的地2
		bvo.setAttributeValue("dest3", dest3);//目的地3
		bvo.setAttributeValue("remark", reason);//事由
		bvo.setAttributeValue("selectpnum",selectedPNum);//坐车人数
		bvo.setAttributeValue("phone", phone);//电话
		bvo.setAttributeValue("ts", null);
		bvo.setAttributeValue("applier", cuserid);//申请人
		// 设置GroupId
		InvocationInfoProxy.getInstance().setUserId(cuserid);
		
		AggVorderHVO aggvo = new AggVorderHVO();
		VorderBVO[] bvos={bvo};
		aggvo.setChildrenVO(bvos);
		aggvo.setParent(hvo);
		AggVorderHVO[] aggvos = { aggvo };
		//单据添加，提交
		IPFBusiAction ipfBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class);
		aggvos = (AggVorderHVO[]) ipfBusiAction.processAction("SAVEBASE","VORD" , null,aggvo , null, null);//保存
		ipfBusiAction.processAction("SAVE","VORD" , null,aggvos[0], null, null);//提交
		pk_vorder=(String) aggvos[0].getParentVO().getAttributeValue("pk_vorder");//得到刚生成的单据主表主键
		pk_vorder_b=(String) aggvos[0].getChildrenVO()[0].getAttributeValue("pk_vorder_b");//得到刚生成的单据子表主键
		PKLock.getInstance().releaseLock(pk_vorder, cuserid, datasource);//根据主键解锁主表
		PKLock.getInstance().releaseLock(pk_vorder_b, cuserid, datasource);//根据主键解锁子表
		result.put("result", "true");
		return result;	
	}


	/**
	 * 评价订单
	 * @param json
	 * @return 
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject remarkVorder(JSONObject json) throws DAOException, JSONException {
		JSONObject result = new JSONObject();
		String pk_vorder = json.getString("pk_vorder"); // 单据主键
		String pk_vorder_b = json.getString("pk_vorder_b"); // 单据子表主键
		String starlevel = json.getString("starlevel"); // 星级
		String review = json.getString("review"); // 评论
		//更新表头的评价和星级
		String sql="update cl_vorder_b set starlevel='"+starlevel
				+"',review='"+review+"'"
				+" where pk_vorder_b='"+pk_vorder_b+"'";
		dao.executeUpdate(sql);
		//wuchy1-2019-10-16 得到司机的星级
		UFDate date=new UFDate();//获取当前日期
		String starsql = "select count(starlevel) as \"count\", sum(starlevel) as \"sum\" from cl_vorder_b "
				+ " inner join cl_vorder on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder.dr=0"
				+ " where cl_vorder_b.dr = 0 and pk_driver ="
				+ " (select pk_driver from cl_vorder where pk_vorder = '"+pk_vorder+"')"
				+ " and substr(cl_vorder_b.departtime,0,7)='"+date.toString().substring(0, 7)+"'";//查询一个自然月的
		Map<String,Integer> map = (Map<String, Integer>) dao.executeQuery(starsql, new MapProcessor());
		UFDouble star = null;
		if(map.size()!=0){
			double count = map.get("count");
			double sum = map.get("sum");
			star = new UFDouble(sum/count);//星级为得到星星的总数，除以评价次数
		}
		
		//更新司机的星级
		String driversql = "update cl_driver set starlevel = "+star+" where dr = 0 "
				+ "and pk_driver = (select pk_driver from cl_vorder where pk_vorder = '"+pk_vorder+"')";
		dao.executeUpdate(driversql);
		result.put("result", "true");
		return result;
	}





	/**
	 * 查询车辆信息
	 * @param jsonObject
	 * @return 
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	@SuppressWarnings({ "unchecked" })
	private JSONObject querVehicle(JSONObject jsonObject) throws DAOException, JSONException {
		JSONArray jsonArr = new JSONArray(); //用于存储单据信息集合
		String cuserid = jsonObject.getString("cuserid"); //当前登录人主键
		//过滤掉休息和外出的司机
		String sql="select  a.pk_vehicle,a.pk_driver, a.vehicleno,a.driver,a.dphone"
				+" from cl_vehicle  a where"
				+"  (select dstate from cl_driver where  pk_driver=a.pk_driver and dr=0) not in (2,4)"
				+"  and a.dr=0";
		List<Map<String,Object>> list=(List<Map<String, Object>>) dao.executeQuery(sql, new  MapListProcessor());
		//循环获取单据信息,进行二次过滤并存储成json对象
		for (Map<String, Object> map : list) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("pk_driver", map.get("pk_driver"));	//	司机主键
			jsonObj.put("pk_vehicle", map.get("pk_vehicle"));//车辆主键
			jsonObj.put("vehicleno", map.get("vehicleno"));//车牌号
			jsonObj.put("driver", map.get("driver"));//司机姓名
			jsonObj.put("dphone", map.get("dphone"));//司机电话
			jsonArr.put(jsonObj);
		}
		JSONObject result = new JSONObject();
		result.put("values", jsonArr);
		result.put("result", "true");
		return result;	
	}





	/**
	 * 更新订单
	 * @param json
	 * @return
	 * @throws DAOException 
	 * @throws JSONException 
	 * @throws UifException 
	 */

	private JSONObject updateVorder(JSONObject json) throws DAOException, JSONException, UifException {
		JSONObject result = new JSONObject();
		String pk_vorder = json.getString("pk_vorder"); // 主键
		String pk_vorder_b = json.getString("pk_vorder_b");// 子表主键
		//旧的vo
		VorderHVO	hvo = (VorderHVO) hypubBo.queryByPrimaryKey(VorderHVO.class, pk_vorder);
		VorderBVO	bvo = (VorderBVO) hypubBo.queryByPrimaryKey(VorderBVO.class, pk_vorder_b);
		String origin = json.getString("origin"); // 出发地
//		IConstEnum[] list = MDEnum.valueOfConstEnum(Enumerate1.class);
//		for (IConstEnum iConstEnum:list){
//			if(iConstEnum.getName().equals(origin)){
//				origin = (String) iConstEnum.getValue();
//				break;
//			}
//		}
		String destarea = json.getString("destarea");// 目标区域
//		 list = MDEnum.valueOfConstEnum(Enumerate2.class);
//		for (IConstEnum iConstEnum:list){
//			if(iConstEnum.getName().equals(destarea)){
//				destarea = (String) iConstEnum.getValue();
//				break;
//			}
//		}

		String driver =json.getString("driver"); // 司机
		String pk_driver =json.getString("pk_driver"); // 司机主键
		String driverphone =json.getString("driverphone"); // 司机电话
		String vehicleno =json.getString("vehicleno"); // 车牌号
		String pk_vehicle =json.getString("pk_vehicle"); // 车牌号
		String dest1 =json.getString("dest1"); // 目的地1
		String dest2 =json.getString("dest2"); // 目的地2
		String dest3 =json.getString("dest3"); // 目的地3
		String finaldest =json.getString("finaldest"); // 最终目的地
		String reason =json.getString("reason"); // 事由
		int selectedPNum =Integer.parseInt(json.getString("selectedPNum")); // 人数
		UFBoolean iscarpool =new UFBoolean(json.getString("mySwitch")); // 是否拼车
		String phone =json.getString("phone"); // 用户电话
		String departtime =json.getString("departtime"); // 用车时间
		String returntime =json.getString("returntime"); // 返回时间
		if(departtime!=null&&!departtime.equals("")){
		departtime=departtime.replaceAll("-", "/");
		departtime=departtime+":00";
		}
		if(returntime!=null&&!returntime.equals("")){
			returntime=returntime.replaceAll("-", "/");
			returntime=returntime+":00";
			}
		AggVorderHVO aggvo = new AggVorderHVO();
		hvo.setAttributeValue("iscarpool", iscarpool);
		int allPNum=4;//车上一共4个座位
		hvo.setAttributeValue("remainpnum", allPNum-selectedPNum);//剩余座位
		hvo.setAttributeValue("billstate", "2");//创建单据时单据状态为申请中
		hvo.setAttributeValue("driver", driver);
		hvo.setAttributeValue("pk_driver", pk_driver);
		hvo.setAttributeValue("dphone", driverphone);
		hvo.setAttributeValue("vehicleno", vehicleno);
		hvo.setAttributeValue("pk_vehicle", pk_vehicle);
		aggvo.setParent(hvo);
		bvo.setAttributeValue("pk_vorder_b", pk_vorder_b);
		bvo.setAttributeValue("pk_vorder", pk_vorder);
		bvo.setAttributeValue("isfisrtapplier", 'Y');
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
		VorderBVO[] bvos={bvo};
		aggvo.setChildrenVO(bvos);
		VOUpdate vo=new VOUpdate();
		
		ISuperVO[] vos={bvo};
		vo.update(vos);//更新表体
		ISuperVO[] hvos={hvo};
		vo.update(hvos);//更新表头
		result.put("result", "true");
		return result;
	}








	private JSONObject deleVorder(JSONObject json) throws DAOException, JSONException {
		JSONObject result = new JSONObject();
		String pk_vorder = json.getString("pk_vorder");// 单据主键
		String pk_vorder_b = json.getString("pk_vorder_b");// 子表主键
		//删除表头数据，设置dr=0
		String sql_h = "update cl_vorder set dr = 1 where pk_vorder ='"
				+ pk_vorder + "'";
		//删除表体数据，设置dr=0
		String sql_b = "update cl_vorder_b set dr = 1 where pk_vorder_b ='"
				+ pk_vorder_b + "'";
		dao.executeUpdate(sql_h);
		dao.executeUpdate(sql_b);
		//删除审批流数据，设置dr=0
		String sql_workflownote = "delete  pub_workflownote where pk_billtype='VORD' and  billid ='"
				+ pk_vorder + "'";
		dao.executeUpdate(sql_workflownote);
		result.put("result", "true");
		return result;
	}








	/**
	 * 用户端查新用车列表
	 * @param json
	 * @return
	 * @throws Exception 
	 */
	private JSONObject queryVorder(JSONObject json) throws Exception{
		JSONArray jsonArr = new JSONArray(); //用于存储单据信息集合
		String cuserid = json.getString("cuserid"); //当前登录人主键
		int maxcount = json.getInt("maxcount"); // 最大数量
		int mincount = json.getInt("mincount"); // 最小数量
		String sql="SELECT * FROM (SELECT ROWNUM AS rowno1, t.* from (select cl_vorder.billstate,cl_vorder.driver,cl_vorder.pk_driver,cl_vorder.dphone,"
				+ "cl_vorder.billno,cl_vorder.pk_vehicle,cl_vorder.vehicleno,cl_vorder.begintime,cl_vorder.endtime,cl_vorder.iscarpool,"
				+ "cl_vorder_b.*,sm_user.user_name,d.vphoto dphoto"
				+" from cl_vorder "
				+" inner join cl_vorder_b"
				+" on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0"
				+" left join cl_vehicle d on d.pk_vehicle=cl_vorder.pk_vehicle and d.dr = 0"//关联车辆
				+" left join sm_user on sm_user.cuserid  =cl_vorder_b.applier and sm_user.dr=0"//关联人员
				+" where applier='"+cuserid+"' and cl_vorder.dr=0 order by cl_vorder.ts desc "
						+ ") t where ROWNUM <= "+maxcount+") table_alias WHERE table_alias.rowno1 >"+mincount;
		List<Map<String,Object>> list=(List<Map<String, Object>>) dao.executeQuery(sql, new  MapListProcessor());
		//遍历结果集
		for (Map<String, Object> map : list) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("pk_vorder", map.get("pk_vorder"));
			jsonObj.put("pk_vorder_b", map.get("pk_vorder_b"));
			jsonObj.put("billno", map.get("billno"));
			jsonObj.put("read", true);//只读
			jsonObj.put("applyer", map.get("user_name"));//申请人姓名
			String selectedOrigins[]={map.get("origin").toString()};
			jsonObj.put("selectedOrigins", selectedOrigins);//出发地
			String selectedDestAreas[]={map.get("destarea").toString()};
			jsonObj.put("selectedDestAreas", selectedDestAreas);//目标区域
			jsonObj.put("departtime",gettime((String)map.get("departtime")));//用车时间
			jsonObj.put("returntime",gettime((String)map.get("returntime")));//返回时间
			jsonObj.put("beginTime",map.get("begintime")==null?"":gettime((String)map.get("begintime")));//开始时间
			jsonObj.put("endTime",map.get("begintime")==null?"":gettime((String)map.get("endtime")));//结束时间
			jsonObj.put("status",state.get(map.get("billstate")));//状态
			jsonObj.put("dest1", map.get("dest1")==null?"":(String)map.get("dest1"));//目的地1
			//如果目的地有值就显示，并且开始加号不显示
			if(map.get("dest2")!=null&&""!=map.get("dest2")){
				jsonObj.put("dest2Div", "display: block;");//目的地2
			}else{
				jsonObj.put("dest2Div", "display: none;");//目的地2不显示
			}
			if(map.get("dest3")!=null&&""!=map.get("dest3")){
				jsonObj.put("dest3Div", "display: block;");//目的地3
			}else{
				jsonObj.put("dest3Div", "display: none;");//目的地3不显示
			}
			jsonObj.put("dest2", map.get("dest2")==null?"":map.get("dest2"));//目的地2
			jsonObj.put("dest3", map.get("dest3")==null?"":map.get("dest3"));//目的地3
			int PNum =Integer.parseInt(map.get("selectpnum").toString()); // 人数
			int  selectpnum[]={PNum};
			jsonObj.put("selectedPNum", selectpnum);//人数
			jsonObj.put("adddiv1", "display: none;align:right;");//加号不显示
			jsonObj.put("adddiv2", "display: none;align:right;");//加号不显示
			jsonObj.put("finaldest", map.get("finaldest"));//最终目的地
			jsonObj.put("reason", map.get("remark"));//事由
			jsonObj.put("iscarpool", "Y".equals(map.get("iscarpool"))?true:false);//是否拼车
			jsonObj.put("phone", map.get("phone"));//电话
			if(map.get("dphoto")!=null){
				// 照片 add by dingft start
				byte[] byte_img = (byte[]) map.get("dphoto");
				String imgDatas = new String(byte_img,"UTF-8");
				jsonObj.put("dphoto", imgDatas);// 图片
				// 照片 add by dingft end
			}else{
				jsonObj.put("dphoto", "");// 图片
				
			}
			//车量信息存为json对象
			JSONObject jsonobj_vehicle = new JSONObject();
			jsonobj_vehicle.put("vehicleno", map.get("vehicleno")==null?"":map.get("vehicleno"));
			jsonobj_vehicle.put("pk_vehicle", map.get("pk_vehicle")==null?"":map.get("pk_vehicle"));
			jsonobj_vehicle.put("driver", map.get("driver"));
			jsonobj_vehicle.put("dphone", map.get("dphone"));
			jsonobj_vehicle.put("pk_driver", map.get("pk_driver"));
			jsonObj.put("selectedDrivers", jsonobj_vehicle.toString());//车辆号的对象
			
			jsonObj.put("turndownreason", map.get("turndownreason"));//驳回原因
			jsonArr.put(jsonObj);
			
		}
		JSONObject result = new JSONObject();
		result.put("values", jsonArr);
		result.put("result", "true");
		return result;
		
	}








	private JSONObject addVorder(JSONObject json) throws BusinessException, JSONException {

		String datasource = "";//数据源
		try {
			datasource = new GetDatasourceName().doreadxml();
		} catch (JDOMException|IOException e ) {
			throw new RuntimeException("配置文件获取数据源名称失败！");
		}

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
		hvo.setAttributeValue("billstate", "2");//创建单据时单据状态为申请中
		hvo.setAttributeValue("pk_group", pk_group);
		hvo.setAttributeValue("pk_org", pk_org);
		hvo.setAttributeValue("cuserId", cuserid);
		UFDate vbilldate = new UFDate();
		hvo.setAttributeValue("vbilldate",vbilldate);
		hvo.setAttributeValue("billtype","VORD");
		
		aggvo.setParent(hvo);
		VorderBVO bvo=new VorderBVO();
		//创建订单时默认是第一申请人
		hvo.setAttributeValue("approvestatus", -1);
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
	//	InvocationInfoProxy.getInstance().setGroupId(hvo.getAttributeValue("pk_group").toString());
		InvocationInfoProxy.getInstance().setUserId(hvo.getAttributeValue("cuserId").toString());

		AggVorderHVO[] aggvos = { aggvo };
        //新增单据并提交
		IPFBusiAction ipfBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class);
		aggvos = (AggVorderHVO[]) ipfBusiAction.processAction("SAVEBASE","VORD" , null,aggvo , null, null);//新增一条申请单
		ipfBusiAction.processAction("SAVE","VORD" , null,aggvos[0], null, null);//提交
		String pk_vorder=(String) aggvos[0].getParentVO().getAttributeValue("pk_vorder");//得到刚生成的单据主表主键
		String pk_vorder_b=(String) aggvos[0].getChildrenVO()[0].getAttributeValue("pk_vorder_b");//得到刚生成的单据子表主键
		PKLock.getInstance().releaseLock(pk_vorder, cuserid, datasource);//根据主键解锁对应主表
		PKLock.getInstance().releaseLock(pk_vorder_b, cuserid, datasource);//根据主键解锁对应子表
		
		result.put("result", "true");
		return result;
	}

	/**
	 * 获取时间（截取到时分，不需要秒）
	 * @param time
	 * @return
	 */
	private String gettime(String time) {
		if (time != null && !time.equals("")) {
			time = time.substring(0, 16);
		}else{
			time ="";
		}
		return time;
	}
}

