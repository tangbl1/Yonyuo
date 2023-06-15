package nc.itf.insurance.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import nc.itf.vehicle.IInsuranceFilesMaintain;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.vehicle.insurance.AggInsuranceHVO;
import nc.vo.vehicle.insurance.InsuranceBVO;
import nc.vo.vehicle.insurance.InsuranceHVO;




@SuppressWarnings("restriction")
@WebServlet("/insurance")
public class InsuranceServlet extends HttpServlet {
	
	BaseDAO dao = new BaseDAO();
	HYPubBO hYPubBO = new HYPubBO();
	private HashMap<String, InsuranceBVO> containVos = new HashMap<String, InsuranceBVO>();
	private static final long serialVersionUID = 1L;
	
	/**
	 * 车辆基本信息保险单页面servlet
	 * 用于处理保险单页面请求
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
			if ("save".equals(method)) {
				result = saveInsurance(jsonObject);//修改后保存
			} else if ("add".equals(method)) {
				result = addInsurance(jsonObject);//新增保险单
			} else if("delete".equals(method)){
				result = deleInsurance(jsonObject);//删除保险单
			} else if("query".equals(method)){
				result = queryInsurance(jsonObject);//查询保险单列表
			} else if("query_vehicle".equals(method)){
				result = querVehicle(jsonObject);//查询车牌号基本信息
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
	 * 查询牌号和车牌号pk
	 * 参数：JSONObject jsonObject
	 * @return JSONObject
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject querVehicle(JSONObject jsonObject) throws DAOException, JSONException {
		JSONObject result = new JSONObject();
		//存放所有车牌号信息的JSONArray
		JSONArray jsonArray = new JSONArray();
		//查询车牌号和车牌号pk的sql，条件是所有dr不为0的
		String sql = "select pk_vehicle,vehicleno from cl_vehicle"
				+ " where cl_vehicle.dr=0";
		//存放车牌号额list
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql, new MapListProcessor());
		//判断是否查询到数据，如果list不为空，继续
		if (list != null) {
			//遍历
			for (Map<String, String> map : list) {
				//存放一条车牌号信息的json
				JSONObject jsonObj = new JSONObject();
				//将车牌号pk放入jsonObj
				jsonObj.put("pk_vehicle", map.get("pk_vehicle"));
				//将车牌号放入jsonObj
				jsonObj.put("vehicleno", map.get("vehicleno"));
				//将这条车牌号信息放入存放所有车牌号信息的jsonArray中
				jsonArray.put(jsonObj);
			}
			//将查询状态放入jsonResult
			result.put("result", "true");
			//将所有数据放入一个JSONObject
			result.put("data", jsonArray);
		}
		//返回数据给前台
		return result;
	}
	
	
	/**
	 * 保险单修改保存方法
	 * 参数：JSONObject json
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject saveInsurance(JSONObject json) throws Exception {
		//用于后面对比是否有被删除的子表
		containVos = new HashMap<String, InsuranceBVO>();
		JSONObject userjsonObject = json.getJSONObject("userjson"); // 登录人
		String pk_org = userjsonObject.getString("pk_org"); //当前登录人所属组织
		String pk_group = userjsonObject.getString("pk_group"); //当前登录人所属集团
		//用于返回前台的JSONObject
		JSONObject result = new JSONObject();
		//得到保险单主表pk
		String pk_insurance = json.getString("pk_insurance");
		//得到车牌号pk
		String pk_vehicle = json.getString("pk_vehicle");
		//修改主表的sql，根据保险单主表pk，修改对应单据的车牌号
		String sql = "update cl_insurance set vehicleno = '"+pk_vehicle+"' where pk_insurance = '"+pk_insurance+"'";
		//用于得到子表VO数组的sql语句
		String bvosql = "dr = 0 and pk_insurance = '"+ pk_insurance +"'";
		//调用sql修改主表
		try {
			dao.executeUpdate(sql);
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("主表修改失败！"+e.getMessage());
		}
		//存放子表VO数组
		InsuranceBVO[]	insuranceBVOs = null;
		try {
			//根据sql得到子表VO数组并赋值
			insuranceBVOs = (InsuranceBVO[])hYPubBO.queryByCondition(InsuranceBVO.class, bvosql);
		} catch (UifException e1) {
			ExceptionUtils.wrappBusinessException("查询保险单信息失败！"+e1.getMessage());
		}
		//获得存放子表所有数据的JSONArray
		JSONArray array = json.getJSONArray("body");
		//对这些数据进行遍历，对每一条数据进行操作
		for (int i = 0; i < array.length(); i++) {
			//得到其中一条JSONObject，
			JSONObject bjson = array.getJSONObject(i);
			//取值---得到子表pk
			String pk_insurance_b = bjson.getString("pk_insurance_b");
			//保险期限
			String ideadline = bjson.getString("ideadline");
			//保险公司
			String icompany = bjson.getString("icompany");
			//险种
			String itype = bjson.getString("itype");
			//保险日期
			String date = bjson.getString("idate");
			//到期日期
			String expiredate = bjson.getString("iexpiredate");
			//金额
			UFDouble money = new UFDouble(bjson.getString("money"));
			//将保险日期和到期日期转换成UFDate类型
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
			UFDate idate = new UFDate( timeFormat.parse(date));
			UFDate iexpiredate = new UFDate( timeFormat.parse(expiredate));
			//根据得到的子表pk_insurance_b，判断该条子表存不存在，如果存在，修改。如果不存在，新增
			if(StringUtils.isBlank(pk_insurance_b)){
				//pk_insurance_b为空，说明子表不存在，所以新建对象，新增该子表 
				InsuranceBVO bvo = new InsuranceBVO();
				//赋值--保险公司
				bvo.setIcompany(icompany);
				//保险日期
				bvo.setIdate(idate);
				//保险期限
				bvo.setIdeadline(ideadline);
				//险种
				bvo.setItype(itype);
				//到期日期
				bvo.setIexpiredate(iexpiredate);
				//金额
				bvo.setMoney(money);
				//集团==用户所属集团
				bvo.setPk_group(pk_group);
				//组织==用户所属组织
				bvo.setPk_org(pk_org);
				//设置dr=0，但是并没有用（我也不知道为啥）
				bvo.setAttributeValue("dr", 0);
				//设置子表关联的子表pk
				bvo.setPk_insurance(pk_insurance);
				//hYPubBO调用方法增加，并得到新增的子表的pk（实际上就是pk_insurance_b，但是不能重复）
				String pk = hYPubBO.insert(bvo);
				//这个sql用于给刚才新增的数据设置dr=0
				String drSql = "update cl_insurance_b set dr = 0 where pk_insurance_b = '"+pk+"'";
				//执行sql，为该子表设置dr
				dao.executeUpdate(drSql);
			}else{
				//将子表pk放入containVos，用于比较是否有子表删除
				containVos.put(pk_insurance_b, null);
				//修改子表单信息的sql
				String bsql = "update cl_insurance_b set icompany = '"+icompany+"',itype='"+itype+"',ideadline='"
						+ ""+ideadline+"',idate = '"+idate+"',iexpiredate='"+iexpiredate+"',money='"+money+
						"' where pk_insurance_b='"+pk_insurance_b+"'";
				//调用sql语句修改
				dao.executeUpdate(bsql);
			}	
		}
		//遍历对比找到前台删除掉的子表
		for(InsuranceBVO insuranceBVO : insuranceBVOs){
			//判断根据pk_insurance找到的所有子表的pk_insurance_b是否在containVos中，不在说明这条子表已经被删除
			if(!containVos.containsKey(insuranceBVO.getPrimaryKey())){
				//删除该子表
				dao.deleteVO(insuranceBVO);
			}
		}
		//返回是否修改成功
		result.put("result", "true");
		//将结果返回给前台
		return result;
	}
	
	/**
	 * 新增时得到子表数据集合
	 * 参数：JSONArray array,String pk_insurance
	 * @return List
	 * @throws Exception
	 */
	private List<InsuranceBVO> getBvos(JSONArray array,String pk_insurance) 
			throws Exception {
		//用于存放所有子表数据的集合
		List<InsuranceBVO> newChildren = new ArrayList<InsuranceBVO>();
		//遍历存放所有子表的JSONArray
		for (int i = 0; i < array.length(); i++) {
			//得到其中一个JSONObject
			JSONObject json = array.getJSONObject(i);
			//新建子表VO 
			InsuranceBVO bvo = new InsuranceBVO();
			//取值--保险公司
			String icompany = json.getString("icompany");
			//险种
			String itype = json.getString("itype");
			//保险期限
			String ideadline = json.getString("ideadline");
			String date = json.getString("idate");
			String expiredate = json.getString("iexpiredate");
			//金额
			UFDouble money = new UFDouble(json.getString("money"));
			//将保险日期和到期日期转换成UFDouble的格式
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
			//保险日期
			UFDate idate = new UFDate( timeFormat.parse(date));
			//到期日期
			UFDate iexpiredate = new UFDate( timeFormat.parse(expiredate));
			//子表赋值--保险公司
			bvo.setIcompany(icompany);	
			//保险日期
			bvo.setIdate(idate);
			//保险期限
			bvo.setIdeadline(ideadline);
			//到期日期
			bvo.setIexpiredate(iexpiredate);
			//险种
			bvo.setItype(itype);
			//金额
			bvo.setMoney(money);
			//子表所属主表PK
			bvo.setPk_insurance(pk_insurance);
			//设置状态为新增
			bvo.setStatus(VOStatus.NEW);
			//将这个子表VO放入集合
			newChildren.add(bvo);
		}
		//返回带有子表Vo的集合
		return newChildren;
	}
	
	/**
	 * 组合AggVO数组
	 * 参数：InsuranceHVO insuranceHVO InsuranceBVO[] insuranceBVOs
	 * @return AggInsuranceHVO【】
	 */
	private AggInsuranceHVO[] getAggVos(InsuranceHVO insuranceHVO,InsuranceBVO[] insuranceBVOs){
		//新建aggVO
		AggInsuranceHVO aggInsuranceHVO = new AggInsuranceHVO();
		//为aggInsuranceHVO赋值--主表VO
		aggInsuranceHVO.setParentVO(insuranceHVO);
		//子表VO【】
		aggInsuranceHVO.setChildrenVO(insuranceBVOs);
		//新建AggVO数组并赋值
		AggInsuranceHVO[] aggVos = new AggInsuranceHVO[]{aggInsuranceHVO};
		//返回
		return aggVos;
	}


	/**
	 * 删除单据
	 * 参数：JSONObject json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject deleInsurance(JSONObject json) throws JSONException {
		//用于返回前台
		JSONObject result = new JSONObject();
		// 单据主键
		String pk_insurance = json.getString("pk_insurance");
		//用于得到主子表VO数组的sql
		String sql = "dr = 0 and pk_insurance = '"+ pk_insurance + "'";
		//新建主子表VO数组
		InsuranceHVO[] insuranceHVOs = null;
		InsuranceBVO[] insuranceBVOs = null;
		try {
			//得到主表VO【】
			insuranceHVOs = (InsuranceHVO[])hYPubBO.queryByCondition(InsuranceHVO.class, sql);
			//得到子表VO【】
			insuranceBVOs = (InsuranceBVO[])hYPubBO.queryByCondition(InsuranceBVO.class, sql);
		} catch (UifException e1) {
			ExceptionUtils.wrappBusinessException("获取保险单子表信息失败！"+e1.getMessage());
		}
			
		try {
			//删除主子表VO数组
			dao.deleteVOArray(insuranceHVOs);
			dao.deleteVOArray(insuranceBVOs);
			result.put("result", "true");
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("删除保险单信息失败！"+e.getMessage());
		}
		//返回
		return result;
	}
	/**
	 * 查询保险单
	 * 参数：JSONObject json
	 * @return 
	 * @return 
	 * @throws Exception
	 */
	private JSONObject queryInsurance(JSONObject json) throws Exception {
		JSONObject result = new JSONObject();
		//存放查询到的所有数据
		JSONArray jsonArray = new JSONArray();
		int maxnum = json.getInt("maxcount"); // 最大数量
		int minnum = json.getInt("mincount"); // 最小数量
		//查询主表pk、单据号、车牌号pk、车牌号、所属单位、部门、车辆类型、车辆性质的sql
		String sql="SELECT * FROM (SELECT ROWNUM AS rowno, t.* FROM (select "
				+ "cl_insurance.pk_insurance, cl_insurance.billno, cl_vehicle.pk_vehicle,"
				+ "cl_vehicle.vehicleno,org_hrorg.name as unit,org_dept.name as dept"
				+ ",cl_vehicle.vtype,cl_vehicle.vcharacter  from cl_insurance left join "
				+ "cl_vehicle on cl_vehicle.pk_vehicle = cl_insurance.vehicleno left join "
				+ "org_hrorg on org_hrorg.pk_hrorg = cl_vehicle.unit left join org_dept "
				+ "on org_dept.pk_dept = cl_vehicle.dept and cl_insurance.dr = 0 order by "
				+ "cl_insurance.ts desc) t where ROWNUM <= "+maxnum+") table_alias WHERE table_alias.rowno > "+minnum;
		//新建集合用于存放查询结果
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
		//遍历，将查询到的数据放入json
		for (Map<String,String> map : list) {
			//存放其中一条数据的json
			JSONObject jsonObj = new JSONObject();
			//取值 主表pk
			String pk_insurance = map.get("pk_insurance");
			//单据号
			String billno = map.get("billno");
			//车牌号
			String vehicleno = map.get("vehicleno");
			//单位--取车辆信息单位
			String unit = map.get("unit");
			//部门--取车辆信息部门
			String dept = map.get("dept");
			//车辆类型--取车辆信息的车辆类型
			String vtype = map.get("vtype");
			//车辆性质--取车辆信息的车辆性质
			String vcharacter = map.get("vcharacter");
			//车牌号pk
			String pk_vehicle = map.get("pk_vehicle");
			//赋值--保险单主表pk
			jsonObj.put("pk_insurance", pk_insurance);
			//单据号
			jsonObj.put("billno", billno);
			//车牌号pk
			jsonObj.put("pk_vehicle", pk_vehicle!=null?map.get("pk_vehicle"):"");
			//车牌号
			jsonObj.put("vehicleno", vehicleno!=null?map.get("vehicleno"):"");
			//单位
			jsonObj.put("unit", unit!=null?map.get("unit"):"");
			//部门
			jsonObj.put("dept", dept!=null?map.get("dept"):"");
			//车辆类型
			jsonObj.put("vtype", vtype!=null?map.get("vtype"):"");
			//车辆性质
			jsonObj.put("vcharacter", vcharacter!=null?map.get("vcharacter"):"");
			//查询子表pk、保险金额、保险年限、险种、保险日期、到期日期、保险公司
			String bvoSql = "select pk_insurance_b,money,ideadline,itype,"
					+ "idate,iexpiredate,icompany from cl_insurance_b where dr = 0 and  "
					+ " pk_insurance = '" + pk_insurance + "'";
			//存放子表数据集合
			List<Map<String,String>> bvoList= (List<Map<String, String>>) dao.executeQuery(bvoSql, new  MapListProcessor());
			//调用方法，得到存放所有保险单子表信息的Jsonarray
			JSONArray array = getData(bvoList);
			jsonObj.put("body", array);
			jsonArray.put(jsonObj);
		}
		result.put("values",jsonArray);
		result.put("result","true");
		return result;
	}
		
	/**
	 * 遍历赋值
	 * 参数：List<Map<String, Object>> list
	 * @return JSONArray
	 * @throws Exception
	 */
	private JSONArray getData (List<Map<String, String>> list) throws Exception {
		//存放所有数据的json
		JSONArray json = new JSONArray();
		//对子表数据进行迭代器遍历
		for(Map map : list){
			Iterator it =  map.keySet().iterator();
			JSONObject js = new JSONObject();
			while(it.hasNext()){
				String key = it.next().toString();
				//金额，开始日期和结束日期需要特殊处理
				if ("money".equals(key)) {
					js.put(key, map.get(key) == null ? "" : map.get(key));
					continue;
				}
				//保险日期取年月日
				else if ("idate".equals(key)) {
					String idate = (String) map.get(key);
					String value = idate.substring(0,10);
					js.put(key, value == null ? "" : value);
					continue;
				}
				//到期日期取年月日
				else if ("iexpiredate".equals(key)) {
					String iexpiredate = (String) map.get(key);
					String value = iexpiredate.substring(0,10);
					js.put(key, value == null ? "" : value);
					continue;
				}
				String value = (String) map.get(key);
				js.put(key, value == null ? "" : value);
			}
			//将一条数据放入json中
			json.put(js);
		}
		//返回
		return json;
	}



	/**
	 * 新增保险单
	 * 参数：JSONObject json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject addInsurance(JSONObject json) throws JSONException {
		//用于返回的JSONObject
		JSONObject result = new JSONObject();
		//子表数据
		JSONArray bjson = json.getJSONArray("body");
		//车牌号
		String vehicleno = json.getString("pk_vehicle"); // 车牌号
		JSONObject userjsonObject = json.getJSONObject("userjson"); // 登录人
		String cuserid = userjsonObject.getString("cuserid"); //当前登录人主键
		String pk_org = userjsonObject.getString("pk_org"); //当前登录人所属组织
		String pk_group = userjsonObject.getString("pk_group"); //当前登录人所属集团
		//新建主表VO	
		InsuranceHVO insuranceHVO = new InsuranceHVO();
		//赋值--设置车牌号
		insuranceHVO.setVehicleno(vehicleno);
		//设置集团--取用户所属集团
		insuranceHVO.setPk_group(pk_group);
		//设置组织--取用户所属组织
		insuranceHVO.setPk_org(pk_org);
		//设置创建人
		insuranceHVO.setAttributeValue("creator", cuserid);
		//得到子表集合
		List<InsuranceBVO> bvos = null;
		try {
			//调用赋值方法得到子表vo集合
			bvos = getBvos(bjson, "");
		} catch (Exception e2) {
			ExceptionUtils.wrappBusinessException("获得子表数组失败！"+e2.getMessage());
		}
		//子表VO数组
		InsuranceBVO[] vos = new InsuranceBVO[bjson.length()];
		//取出集合中的VO，放入子表VO数组中
		for (int i = 0; i < bvos.size(); i++) {
			//赋值
			vos[i] = bvos.get(i);
		}
		//调用方法得到AggVO数组
		AggInsuranceHVO[]  aggInsuranceHVOs = getAggVos(insuranceHVO, vos);
		// 设置GroupId，和创建人
		try {
			InvocationInfoProxy.getInstance().setGroupId(pk_group);
			InvocationInfoProxy.getInstance().setUserId(cuserid);
		} catch (Exception e1) {
			ExceptionUtils.wrappBusinessException("通用错误error"+e1.getMessage());
		}
		//保险单标准接口
		IInsuranceFilesMaintain service = (IInsuranceFilesMaintain) NCLocator
				.getInstance().lookup(IInsuranceFilesMaintain.class);
		try {
			//调用新增方法
			AggInsuranceHVO[] aggVos = service.insert(aggInsuranceHVOs,null);
			result.put("result", "true");
		} catch (nc.vo.pub.BusinessException e) {
			ExceptionUtils.wrappBusinessException("添加保险单信息失败！"+e.getMessage());
		}
		return result;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

}
