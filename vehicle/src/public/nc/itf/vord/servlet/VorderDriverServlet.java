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
	private static boolean TEST_FLAG = false;	//���Ա�ʶ��true->���Է�����	false->��ʽ������
	private static Map<String,String> state=new HashMap<String,String>();	//״̬ӳ�䣬���ڻ�ȡ״̬�����Ӧ��״̬����
	static {
		//����״̬��ӳ�䣬����ɡ������С�����������ɡ�׼��������������������̡���Ϣ��ֵ�ࡣ
		state.put("1", "�����");
		state.put("2", "������");
		state.put("3", "�����������");
		state.put("4", "׼������");
		state.put("5", "����");
		state.put("6", "����");
		state.put("7", "����");
		state.put("8", "��Ϣ");
		state.put("9", "����");
	}
	/**
	 * ˾�����뵥ҳ��servlet
	 * ���ڴ���˾��ҳ������
	 * @author �ܾ�
	 * @date 2019-11-20
	 * @param 
	 * 	req ǰ̨ҳ������
	 * 	resp ������Ϣ
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		JSONObject result = new JSONObject();	//����ǰ̨���
		String errMsg = "";		//������Ϣ
		String datasource = "";//����Դ
		try {
			datasource = new GetDatasourceName().doreadxml();
		} catch (JDOMException e) {
			throw new RuntimeException("�����ļ���ȡ����Դ����ʧ�ܣ�");
		}
		// �����������
		InvocationInfoProxy.getInstance().setUserDataSource(datasource);	//ָ�����β������ݵ�����Դ
		req.setCharacterEncoding("utf-8");		//��������ı���ΪUTF-8
		String method = req.getParameter("method");	//��ȡǰ̨����method��ʶ���������ִ�����
		String json = req.getParameter("json");	//��ȡǰ̨���ݹ����Ĳ���JSON����
		
		if(StringUtils.isBlank(method))
			errMsg = "methodΪ�գ����������쳣!";
		if(StringUtils.isBlank(json))
			errMsg = "jsonΪ�գ����������쳣!";
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(json); //String->JSONObject
		} catch (JSONException e1) {
			ExceptionUtils.wrappException(e1);
		}	
		
		// ����NC�����¼
		String username = "hwapp";		//�û���
		String password = "asdqwe123";	//����
		IFwLogin loginService = (IFwLogin) NCLocator.getInstance().lookup(IFwLogin.class);
		byte[] token = loginService.login(username, password, null);
		NetStreamContext.setToken(token);
		
		//ͨ���û������ѯ��Ӧcuserid,pk_group
		String sqluserid = "select cuserid,pk_group from sm_user where user_code='" + username + "'";
		Map<String,String> map = null;
		try {
			map = (Map<String,String>) dao.executeQuery(sqluserid, new MapProcessor());
			if(map==null || map.size() <= 0)
				errMsg = "��ѯhwapp�û�ʧ�ܣ�����ϵ����Ա��";
		} catch (Exception e) {
			errMsg = "cuserid,pk_group��ѯʧ�ܣ�";
			ExceptionUtils.wrappBusinessException(errMsg);
		}
		
		String userid = map.get("cuserid");
		String pk_group = map.get("pk_group");
		InvocationInfoProxy.getInstance().setUserCode(username);
		InvocationInfoProxy.getInstance().setGroupId(pk_group);// ��Ա������Ϣ��
		InvocationInfoProxy.getInstance().setUserId(userid);
		try {
			//��ѯ����
			if (method.equals("query")) {
				result = queryVorder(jsonObject);//��ѯ�����б�
			} else if (method.equals("updateStaue")) {
				result = updateStaue(jsonObject);//����״̬
			} else if(method.equals("querydetail")){
				result = queryDetail(jsonObject);//��ѯ��������
			}else if (method.equals("query_remark")) {
				result = queryRemark(jsonObject);//�ж϶����Ƿ��Ѿ�������
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
	 * ˾���˲����ó�����
	 * @param jsonObject
	 * @return
	 * @throws JSONException 
	 */
	private JSONObject queryDetail(JSONObject jsonObject) throws JSONException {
		JSONObject result = new JSONObject();
		String pk_vorder = jsonObject.getString("pk_vorder"); // ��ͷ����
		//��ѯ��ͷ��Ϣ
		String sql="select cl_vorder.billstate,cl_vorder.iscarpool,cl_vorder.pk_vehicle,"
				+ "cl_vorder.startMileage,cl_vorder.backMileage,cl_vorder.travelMileage,cl_vorder_b.*"
				+" from cl_vorder "
				+" inner join cl_vorder_b"
				+" on cl_vorder.pk_vorder=cl_vorder_b.pk_vorder and cl_vorder_b.dr=0"
				+" and isfisrtapplier='Y'"
				+" where cl_vorder.dr=0  and cl_vorder.pk_vorder='"+pk_vorder+"'";
		//��ѯ������Ϣ
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
			ExceptionUtils.wrappBusinessException("��ѯ�ó�����ʧ�ܣ�");
		}
		JSONObject jsonVO = new JSONObject();
		jsonVO.put("origin", map.get("origin"));//������
		jsonVO.put("destarea", map.get("destarea"));//Ŀ������
		jsonVO.put("dest1", map.get("dest1")==null?"":(String)map.get("dest1"));//Ŀ�ĵ�1
		//���Ŀ�ĵ���ֵ����ʾ�����ҿ�ʼ�ӺŲ���ʾ
		if(StringUtils.isNotBlank(map.get("dest2"))){
			jsonVO.put("dest2Div", "display: block;");//Ŀ�ĵ�2
		}else{
			jsonVO.put("dest2Div", "display: none;");//Ŀ�ĵ�2����ʾ
		}
		if(StringUtils.isNotBlank(map.get("dest3"))){
			jsonVO.put("dest3Div", "display: block;");//Ŀ�ĵ�3
		}else{
			jsonVO.put("dest3Div", "display: none;");//Ŀ�ĵ�3����ʾ
		}
		jsonVO.put("dest2", map.get("dest2"));//Ŀ�ĵ�2
		jsonVO.put("dest3", map.get("dest3"));//Ŀ�ĵ�3
		jsonVO.put("departtime",gettime(map.get("departtime")));//�ó�ʱ��
		jsonVO.put("returntime",gettime(map.get("returntime")));//����ʱ��
		jsonVO.put("iscarpool", "Y".equals(map.get("iscarpool"))?"��":"��");//�ܷ�ƴ��
		jsonVO.put("billstate", state.get(map.get("billstate")));//״̬
		jsonVO.put("finaldest", map.get("finaldest"));//����Ŀ�ĵ�
		jsonVO.put("startMileage", map.get("startmileage"));//�������
		jsonVO.put("backMileage", map.get("backmileage"));//�������
		jsonVO.put("travelMileage", map.get("travelmileage"));//��ʻ���
		jsonVO.put("pk_vehicle", map.get("pk_vehicle"));//��������
		
		Boolean isreview = true;//�ж϶����Ƿ�����(Ĭ��Ϊ��)
		//ѭ�����壬�õ�����list
		JSONArray jsonDetails = new JSONArray(); //����JSON���ݺϼ�
		for (Map<String, String> map_detail : list_detail) {
			JSONObject jsonDetail = new JSONObject();
			jsonDetail.put("selectedPNum", map_detail.get("selectpnum"));//����
			jsonDetail.put("username", map_detail.get("username"));//����
			jsonDetail.put("userphone", map_detail.get("phone"));//��ϵ�绰
			jsonDetail.put("dest1", map_detail.get("dest1"));//Ŀ�ĵ�1
			jsonDetail.put("finaldest", map_detail.get("finaldest"));//Ŀ�ĵ�
			jsonDetail.put("remark", map_detail.get("remark"));//����
			if(map.get("starlevel")==null){//���û�б����ۣ�review����Ϊfalse
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
	 * �жϸö����Ƿ��Ѿ�����
	 * @param jsonObject
	 * @return
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject queryRemark(JSONObject jsonObject) throws DAOException, JSONException {
		JSONObject result = new JSONObject();
		Boolean isreview = true;//�ж϶����Ƿ�����(Ĭ��Ϊ��)
		String pk_vorder = jsonObject.getString("pk_vorder"); // ��������
		String sql = "select count(starlevel) sumlevel,count(pk_vorder_b) sumpk from cl_vorder_b where pk_vorder='"+pk_vorder+"'";//��ѯ���ۺ��Ǽ�
		Map<String,Integer> map = (Map<String, Integer>) dao.executeQuery(sql, new MapProcessor());
		if(map.get("sumlevel")!=map.get("sumpk")){//���û�б����ۣ�review����Ϊfalse
			isreview = false;
		}
		result.put("isreview",isreview);
		result.put("result", "true");
		return result;
	}
	/**
	 * ���µ���״̬
	 * @param json
	 * @return
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject updateStaue(JSONObject json) throws DAOException, JSONException {
		JSONObject result = new JSONObject();
		String pk_vorder = json.getString("pk_vorder"); // ��������
		String billstate = json.getString("billstate"); // ����״̬
		String vstate = json.getString("vstate"); // ����״̬
		String pk_vehicle = json.getString("pk_vehicle"); // ��������
		//���±�ͷ����״̬
		String sql="update cl_vorder set billstate='"+billstate
				       +"' where pk_vorder='"+pk_vorder+"'";
		//ͬ�����³�����״̬
		String sql_vehicle="update cl_vehicle set vstate='"+vstate
					 +"' where pk_vehicle='"+pk_vehicle+"'";
		//����״̬
		dao.executeUpdate(sql);
		dao.executeUpdate(sql_vehicle);
		if("1".equals(billstate)){//������ɵ�ʱ����·�����̺���ʻ���
		String startMileage = json.getString("startMileage"); // �������
		String backMileage = json.getString("backMileage"); // �������
		String travelMileage = json.getString("travelMileage"); // ��ʻ���
		String sql_mileage="update cl_vorder set startMileage='"+startMileage
					+"', backMileage='"+backMileage
					+"', travelMileage='"+travelMileage
			       +"' where pk_vorder='"+pk_vorder+"'";
		dao.executeUpdate(sql_mileage);
		}else if("5".equals(billstate)){//���¿�ʼʱ��ͳ������
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
	 * ˾���˲����ó��б�
	 * @param json
	 * @return
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject queryVorder(JSONObject json) throws DAOException, JSONException{
		//TEST_FLAG=true;
		JSONArray jsonArr = new JSONArray(); //���ڴ洢������Ϣ����
		String cuserid = json.getString("cuserid"); //��ǰ��¼������
		int maxcount = json.getInt("maxcount"); // �������
		int mincount = json.getInt("mincount"); // ��С����
		//˾������
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
				//TODO:������
				+ ((TEST_FLAG) ? "" : "and pk_driver='"+cupk_driver+"'")
				+ "order by cl_vorder.ts desc) t where ROWNUM <= "+maxcount+") table_alias WHERE table_alias.rowno1 >"+mincount;
		List<Map<String,Object>> list= (List<Map<String, Object>>) dao.executeQuery(sql, new  MapListProcessor());
		
		//ѭ����ȡ������Ϣ���洢��json����
		for (Map<String, Object> map : list) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("pk_vorder", map.get("pk_vorder"));
			jsonObj.put("pk_vorder_b", map.get("pk_vorder_b"));
			jsonObj.put("billstate",state.get(map.get("billstate")));//״̬
			jsonObj.put("departtime",gettime((String)map.get("departtime")));//�ó�ʱ��
			jsonObj.put("returntime",gettime((String)map.get("returntime")));//����ʱ��
			jsonObj.put("origin", map.get("origin"));//ʼ����
			jsonObj.put("dest1", map.get("dest1")==null?"":(String)map.get("dest1"));//Ŀ�ĵ�1
			jsonObj.put("finaldest", map.get("finaldest"));//����Ŀ�ĵ�
			jsonObj.put("selectedPNum", map.get("sumselectpnum"));//����
			UFDouble travelmileage=(map.get("travelmileage")==null?UFDouble.ZERO_DBL:new UFDouble(map.get("travelmileage").toString()));
			jsonObj.put("travelMileage", travelmileage);//��ʻ���
			jsonArr.put(jsonObj);
		}
		JSONObject result = new JSONObject();
		result.put("values", jsonArr);
		result.put("result", "true");
		return result;	
	}


	/**
	 * ��ȡʱ�䣨��ȡ��ʱ�֣�����Ҫ�룩
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
