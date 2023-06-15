package nc.itf.ispt.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import nc.itf.vehicle.IInspectionFilesMaintain;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pub.lang.UFDate;
import nc.vo.vehicle.inspectionfiles.AggInspectionFileHVO;
import nc.vo.vehicle.inspectionfiles.InspectionFileBVO;
import nc.vo.vehicle.inspectionfiles.InspectionFileHVO;


@SuppressWarnings("restriction")
@WebServlet("/hwip")
public class InspectionServlet extends HttpServlet {

	
	BaseDAO dao = new BaseDAO();
	private static final long serialVersionUID = 1L;
	
	/**
	 * 车辆基本信息检车单页面servlet
	 * 用于处理检车单页面请求
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
				result = addInspection(jsonObject);
			} else if ("query".equals(method)){
				result = queryInspection(jsonObject);
			} else if("delete".equals(method)){
				result = deleInspection(jsonObject);
			} else if("save".equals(method)){
				result = saveInspection(jsonObject);
			} else if("query_vehicle".equals(method)){
				result = querVehicle(jsonObject);
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
	 * 查询车辆的车牌号pk和车牌号
	 * 参数：JSONObject json
	 * @return JSONObject
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject querVehicle(JSONObject jsonObject) throws DAOException, JSONException {
		//用于返回前台的jsonResult
		JSONObject result = new JSONObject();
		//存放所有车牌号信息的JSONArray
		JSONArray jsonArray = new JSONArray();
		//查询所有dr=0的车牌号pk和车牌号
		String sql="select pk_vehicle,vehicleno from cl_vehicle"
				+ " where cl_vehicle.dr=0";
		//存放车牌号结果集的集合
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
		//如果查询的结果集不为空，遍历
		if (list != null) {
			//遍历结果集
			for (Map<String,String> map : list) {
				//存放一条车牌号信息的JSONObject
				JSONObject jsonObj = new JSONObject();
				//车牌号主键
				jsonObj.put("pk_vehicle", map.get("pk_vehicle"));
				//车牌号
				jsonObj.put("vehicleno", map.get("vehicleno"));
				//将这条数据放到JSONArray中
				jsonArray.put(jsonObj);
			}
			//提示信息放入jsonResult
			result.put("result", "true");
			//车牌号信息放入jsonResult
			result.put("data", jsonArray);
		}
		//返回
		return result;
	}
	/**
	 * 修改保存检车单
	 * 参数：JSONObject json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject saveInspection(JSONObject json) throws JSONException {
		//用于返回的jsonobject
		JSONObject result = new JSONObject();
		//检车单主键
		String pk_inspection = json.getString("pk_inspection"); 
		//检车单子表主键
		String pk_inspection_b = json.getString("pk_inspection_b");
		//车牌号
		String vehicleno = json.getString("vehicleno"); 
		//检车有效期
		String iexpiredate = json.getString("iexpiredate");
		//检车期限
		String dmv = json.getString("dmv");
		UFDate date=null;
		//如果检车有效期不为空，设置日期格式并截取赋值
		if(StringUtils.isNotBlank(iexpiredate)){
			//设置日期格式
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM");
			try {
				//转换
				 date = new UFDate( timeFormat.parse( iexpiredate ) ) ;
			} catch (java.text.ParseException e1) {
				ExceptionUtils.wrappBusinessException("检车有限期转换失败"+e1.getMessage());
			}
		}
		//更新表头数据
		String hvosql="update cl_inspection set vehicleno='"+vehicleno
				       +"' where pk_inspection='"+pk_inspection+"'";
		//更新表体数据
		String bvosql="update cl_inspection_b set iexpiredate='"+iexpiredate+"' , dmv='"+dmv
				       + "' where pk_inspection_b='"+pk_inspection_b+"'";
		try {
			//执行修改的sql
			dao.executeUpdate(hvosql);
			dao.executeUpdate(bvosql);
			result.put("result", "true");
		} catch (DAOException e1) {
			ExceptionUtils.wrappBusinessException("修改检车单信息失败！"+e1.getMessage());
		}
		return result;
	}
	
	/**
	 * 删除检车单
	 * 参数：JSONObject json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject deleInspection(JSONObject json) throws JSONException {
		//用于返回的jsonobject
		JSONObject result = new JSONObject();
		//检查单主表主键
		String pk_inspection = json.getString("pk_inspection");
		//检车单子表主键
		String pk_inspection_b = json.getString("pk_inspection_b");
		//删除表头数据
		String sql_h = "update cl_inspection set dr = 1 where pk_inspection ='"
				+ pk_inspection + "'";
		//删除表头数据
		String sql_b = "update cl_inspection_b set dr = 1 where pk_inspection_b ='"
				+ pk_inspection_b + "'";
		try {
			//执行sql语句删除数据
			dao.executeUpdate(sql_h);
			dao.executeUpdate(sql_b);
			result.put("result", "true");
		} catch (DAOException e) {
			ExceptionUtils.wrappBusinessException("删除司机档案信息失败！"+e.getMessage());
		}
		return result;
	}
	
	/**
	 * 查询检车单
	 * 参数：JSONObject json
	 * @return 
	 * @throws Exception
	 */
	private JSONObject queryInspection(JSONObject json) throws Exception {
		JSONObject result = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		int maxnum = json.getInt("maxcount"); // 最大数量
		int minnum = json.getInt("mincount"); // 最小数量
		//查询检车单的主表单据号和子表全部数据（分页查询）
		String sql="SELECT * from (SELECT ROWNUM AS rowno1, t.* from ( select cl_inspection.billno,"
				+ "cl_inspection_b.*,cl_vehicle.pk_vehicle,cl_vehicle.vehicleno vlno,org_hrorg.name as unit,"
				+ "cl_vehicle.vtype,cl_vehicle.vphoto,cl_vehicle.vcharacter"
				+ " from cl_inspection inner join cl_inspection_b"
				+" on cl_inspection.pk_inspection=cl_inspection_b.pk_inspection and cl_inspection_b.dr=0"
				+" left join cl_vehicle on cl_vehicle.pk_vehicle=cl_inspection.vehicleno left join org_hrorg  on org_hrorg.pk_hrorg = cl_vehicle.unit"
				+" where cl_inspection.dr=0 order by cl_inspection.ts desc) t where ROWNUM <= "+maxnum+") c WHERE c.rowno1 >"+minnum;
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
		//遍历结果集
		for (Map<String,String> map : list) {
			//存放一条数据的mapJSON
			JSONObject jsonObj = new JSONObject();
			//检车单主表pk
			jsonObj.put("pk_inspection", map.get("pk_inspection"));
			//检车单子表pk
			jsonObj.put("pk_inspection_b", map.get("pk_inspection_b"));
			//单据号
			jsonObj.put("billno", map.get("billno"));
			//部门
			jsonObj.put("dept", map.get("dept")!=null?map.get("dept"):"");
			//单位
			jsonObj.put("unit", map.get("unit")!=null?map.get("unit"):"");
			//车牌号存为json对象
			JSONObject jsonobj_vehicle = new JSONObject();
			//车牌号pk
			jsonobj_vehicle.put("pk_vehicle", map.get("pk_vehicle")!=null?map.get("pk_vehicle"):"");
			//车牌号
			jsonobj_vehicle.put("vehicleno", map.get("vlno")!=null?map.get("vlno"):"");
			//车牌号对象
			jsonObj.put("pk_vehicle", jsonobj_vehicle);
			//车牌号
			jsonObj.put("vlno", map.get("vlno")!=null?map.get("vlno"):"");
			//车辆类型
			jsonObj.put("vtype", map.get("vtype")!=null?map.get("vtype"):"");
			//车辆性质
			jsonObj.put("vcharacter",map.get("vcharacter")!=null?map.get("vcharacter"):"");
			//保险日期对象
			String iexpiredate=map.get("iexpiredate");
			//截取年月
			if(StringUtils.isNotBlank(iexpiredate)){
				jsonObj.put("iexpiredate", iexpiredate.toString().substring(0, 7));
			}
			//检测期限
			jsonObj.put("dmv", map.get("dmv"));
			//图片处理
			Object img = map.get("vphoto");
			byte[] byte_img = null;
			String imgDatas = "";
			if(img != null){
				byte_img = (byte[]) img;
				imgDatas = new String(byte_img,"UTF-8");
			}
			jsonObj.put("vphoto", imgDatas);// 图片
			//建这条数据放入mapArrJSON
			jsonArray.put(jsonObj);	
		}
		result.put("values", jsonArray);
		result.put("result", "true");
		return result;
	}
	
	/**
	 * 查询检车单
	 * 参数：JSONObject json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject addInspection(JSONObject json) throws JSONException {
		//用于返回的result
		JSONObject result = new JSONObject();
		//携带当前登录用户信息的userjson
		JSONObject userjsonObject = json.getJSONObject("userjson"); // 登录人
		String cuserid = userjsonObject.getString("cuserid"); //当前登录人主键
		String pk_org = userjsonObject.getString("pk_org"); //当前登录人所属组织
		String pk_group = userjsonObject.getString("pk_group"); //当前登录人所属集团
		String vehicleno = json.getString("vehicleno"); // 车牌号
		String iexpiredate = json.getString("iexpiredate");// 检车有效期
		String dmv =json.getString("dmv"); // 检车期限
		UFDate date=null;
		if(StringUtils.isNotBlank(iexpiredate)&&!"请选择".equals(iexpiredate)){
		//设置日期格式
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM");
			try {
				//转换
				 date = new UFDate( timeFormat.parse( iexpiredate ) ) ;
			} catch (java.text.ParseException e1) {
				ExceptionUtils.wrappBusinessException("检车有效期格式转换失败"+e1.getMessage());
			}
		}
		//新建AggVO
		AggInspectionFileHVO aggvo = new AggInspectionFileHVO();
		//新建检车单主表VO
		InspectionFileHVO hvo = new InspectionFileHVO();
		//赋值、车牌号
		hvo.setAttributeValue("vehicleno", vehicleno);
		//赋值、集团
		hvo.setAttributeValue("pk_group", pk_group);
		//赋值、组织
		hvo.setAttributeValue("pk_org", pk_org);
		//赋值、cuserId
		hvo.setAttributeValue("cuserId", cuserid);
		//aggvo主表赋值
		aggvo.setParent(hvo);
		//新建检测单子表VO
		InspectionFileBVO bvo=new InspectionFileBVO();
		//赋值、检车有有效期
		bvo.setAttributeValue("iexpiredate", date);
		//赋值、检车期限
		bvo.setAttributeValue("dmv", dmv);
		//将这个vo放入到子表vo数组中
		InspectionFileBVO[] bvos={bvo};
		//aggvo赋值
		aggvo.setChildrenVO(bvos);
		// 设置GroupId
		InvocationInfoProxy.getInstance().setGroupId(
				hvo.getAttributeValue("pk_group").toString());
		//创建人
		InvocationInfoProxy.getInstance().setUserId(
				hvo.getAttributeValue("cuserId").toString());
		//新建AggVO数组并赋值
		AggInspectionFileHVO[] aggvos = { aggvo };
		//检车单标准接口
		IInspectionFilesMaintain iInspectionMaintain = (IInspectionFilesMaintain) NCLocator
				.getInstance().lookup(IInspectionFilesMaintain.class);
		try {
			//调用接口的新增方法新增
			AggInspectionFileHVO[] aggdfVO = iInspectionMaintain.insert(aggvos,
					null);
			result.put("result", "true");
		} catch (nc.vo.pub.BusinessException e) {
			ExceptionUtils.wrappBusinessException("添加司机档案信息失败！"+e.getMessage());
		}
		return result;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

}
