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
	 * ����������Ϣ���յ�ҳ��servlet
	 * ���ڴ����յ�ҳ������
	 * @author 
	 * @date 2019-11-21
	 * @param 
	 * 	req ǰ̨ҳ������
	 * 	resp ������Ϣ
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
		} catch (Exception e1) {
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
			if ("save".equals(method)) {
				result = saveInsurance(jsonObject);//�޸ĺ󱣴�
			} else if ("add".equals(method)) {
				result = addInsurance(jsonObject);//�������յ�
			} else if("delete".equals(method)){
				result = deleInsurance(jsonObject);//ɾ�����յ�
			} else if("query".equals(method)){
				result = queryInsurance(jsonObject);//��ѯ���յ��б�
			} else if("query_vehicle".equals(method)){
				result = querVehicle(jsonObject);//��ѯ���ƺŻ�����Ϣ
			}
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		
		if(StringUtils.isNotBlank(errMsg)){
			try {
				result.put("success", false);
				result.put("errMsg", errMsg);
			} catch (JSONException e) {
				ExceptionUtils.wrappBusinessException("ʧ�ܵ�result��ֵʧ��!"+e.getMessage());
			}	
		}
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().write(result.toString());
		resp.flushBuffer();
	}
	
	
	
	/**
	 * ��ѯ�ƺźͳ��ƺ�pk
	 * ������JSONObject jsonObject
	 * @return JSONObject
	 * @throws DAOException 
	 * @throws JSONException 
	 */
	private JSONObject querVehicle(JSONObject jsonObject) throws DAOException, JSONException {
		JSONObject result = new JSONObject();
		//������г��ƺ���Ϣ��JSONArray
		JSONArray jsonArray = new JSONArray();
		//��ѯ���ƺźͳ��ƺ�pk��sql������������dr��Ϊ0��
		String sql = "select pk_vehicle,vehicleno from cl_vehicle"
				+ " where cl_vehicle.dr=0";
		//��ų��ƺŶ�list
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql, new MapListProcessor());
		//�ж��Ƿ��ѯ�����ݣ����list��Ϊ�գ�����
		if (list != null) {
			//����
			for (Map<String, String> map : list) {
				//���һ�����ƺ���Ϣ��json
				JSONObject jsonObj = new JSONObject();
				//�����ƺ�pk����jsonObj
				jsonObj.put("pk_vehicle", map.get("pk_vehicle"));
				//�����ƺŷ���jsonObj
				jsonObj.put("vehicleno", map.get("vehicleno"));
				//���������ƺ���Ϣ���������г��ƺ���Ϣ��jsonArray��
				jsonArray.put(jsonObj);
			}
			//����ѯ״̬����jsonResult
			result.put("result", "true");
			//���������ݷ���һ��JSONObject
			result.put("data", jsonArray);
		}
		//�������ݸ�ǰ̨
		return result;
	}
	
	
	/**
	 * ���յ��޸ı��淽��
	 * ������JSONObject json
	 * @return JSONObject
	 * @throws Exception
	 */
	private JSONObject saveInsurance(JSONObject json) throws Exception {
		//���ں���Ա��Ƿ��б�ɾ�����ӱ�
		containVos = new HashMap<String, InsuranceBVO>();
		JSONObject userjsonObject = json.getJSONObject("userjson"); // ��¼��
		String pk_org = userjsonObject.getString("pk_org"); //��ǰ��¼��������֯
		String pk_group = userjsonObject.getString("pk_group"); //��ǰ��¼����������
		//���ڷ���ǰ̨��JSONObject
		JSONObject result = new JSONObject();
		//�õ����յ�����pk
		String pk_insurance = json.getString("pk_insurance");
		//�õ����ƺ�pk
		String pk_vehicle = json.getString("pk_vehicle");
		//�޸������sql�����ݱ��յ�����pk���޸Ķ�Ӧ���ݵĳ��ƺ�
		String sql = "update cl_insurance set vehicleno = '"+pk_vehicle+"' where pk_insurance = '"+pk_insurance+"'";
		//���ڵõ��ӱ�VO�����sql���
		String bvosql = "dr = 0 and pk_insurance = '"+ pk_insurance +"'";
		//����sql�޸�����
		try {
			dao.executeUpdate(sql);
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("�����޸�ʧ�ܣ�"+e.getMessage());
		}
		//����ӱ�VO����
		InsuranceBVO[]	insuranceBVOs = null;
		try {
			//����sql�õ��ӱ�VO���鲢��ֵ
			insuranceBVOs = (InsuranceBVO[])hYPubBO.queryByCondition(InsuranceBVO.class, bvosql);
		} catch (UifException e1) {
			ExceptionUtils.wrappBusinessException("��ѯ���յ���Ϣʧ�ܣ�"+e1.getMessage());
		}
		//��ô���ӱ��������ݵ�JSONArray
		JSONArray array = json.getJSONArray("body");
		//����Щ���ݽ��б�������ÿһ�����ݽ��в���
		for (int i = 0; i < array.length(); i++) {
			//�õ�����һ��JSONObject��
			JSONObject bjson = array.getJSONObject(i);
			//ȡֵ---�õ��ӱ�pk
			String pk_insurance_b = bjson.getString("pk_insurance_b");
			//��������
			String ideadline = bjson.getString("ideadline");
			//���չ�˾
			String icompany = bjson.getString("icompany");
			//����
			String itype = bjson.getString("itype");
			//��������
			String date = bjson.getString("idate");
			//��������
			String expiredate = bjson.getString("iexpiredate");
			//���
			UFDouble money = new UFDouble(bjson.getString("money"));
			//���������ں͵�������ת����UFDate����
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
			UFDate idate = new UFDate( timeFormat.parse(date));
			UFDate iexpiredate = new UFDate( timeFormat.parse(expiredate));
			//���ݵõ����ӱ�pk_insurance_b���жϸ����ӱ�治���ڣ�������ڣ��޸ġ���������ڣ�����
			if(StringUtils.isBlank(pk_insurance_b)){
				//pk_insurance_bΪ�գ�˵���ӱ����ڣ������½������������ӱ� 
				InsuranceBVO bvo = new InsuranceBVO();
				//��ֵ--���չ�˾
				bvo.setIcompany(icompany);
				//��������
				bvo.setIdate(idate);
				//��������
				bvo.setIdeadline(ideadline);
				//����
				bvo.setItype(itype);
				//��������
				bvo.setIexpiredate(iexpiredate);
				//���
				bvo.setMoney(money);
				//����==�û���������
				bvo.setPk_group(pk_group);
				//��֯==�û�������֯
				bvo.setPk_org(pk_org);
				//����dr=0�����ǲ�û���ã���Ҳ��֪��Ϊɶ��
				bvo.setAttributeValue("dr", 0);
				//�����ӱ�������ӱ�pk
				bvo.setPk_insurance(pk_insurance);
				//hYPubBO���÷������ӣ����õ��������ӱ��pk��ʵ���Ͼ���pk_insurance_b�����ǲ����ظ���
				String pk = hYPubBO.insert(bvo);
				//���sql���ڸ��ղ���������������dr=0
				String drSql = "update cl_insurance_b set dr = 0 where pk_insurance_b = '"+pk+"'";
				//ִ��sql��Ϊ���ӱ�����dr
				dao.executeUpdate(drSql);
			}else{
				//���ӱ�pk����containVos�����ڱȽ��Ƿ����ӱ�ɾ��
				containVos.put(pk_insurance_b, null);
				//�޸��ӱ���Ϣ��sql
				String bsql = "update cl_insurance_b set icompany = '"+icompany+"',itype='"+itype+"',ideadline='"
						+ ""+ideadline+"',idate = '"+idate+"',iexpiredate='"+iexpiredate+"',money='"+money+
						"' where pk_insurance_b='"+pk_insurance_b+"'";
				//����sql����޸�
				dao.executeUpdate(bsql);
			}	
		}
		//�����Ա��ҵ�ǰ̨ɾ�������ӱ�
		for(InsuranceBVO insuranceBVO : insuranceBVOs){
			//�жϸ���pk_insurance�ҵ��������ӱ��pk_insurance_b�Ƿ���containVos�У�����˵�������ӱ��Ѿ���ɾ��
			if(!containVos.containsKey(insuranceBVO.getPrimaryKey())){
				//ɾ�����ӱ�
				dao.deleteVO(insuranceBVO);
			}
		}
		//�����Ƿ��޸ĳɹ�
		result.put("result", "true");
		//��������ظ�ǰ̨
		return result;
	}
	
	/**
	 * ����ʱ�õ��ӱ����ݼ���
	 * ������JSONArray array,String pk_insurance
	 * @return List
	 * @throws Exception
	 */
	private List<InsuranceBVO> getBvos(JSONArray array,String pk_insurance) 
			throws Exception {
		//���ڴ�������ӱ����ݵļ���
		List<InsuranceBVO> newChildren = new ArrayList<InsuranceBVO>();
		//������������ӱ��JSONArray
		for (int i = 0; i < array.length(); i++) {
			//�õ�����һ��JSONObject
			JSONObject json = array.getJSONObject(i);
			//�½��ӱ�VO 
			InsuranceBVO bvo = new InsuranceBVO();
			//ȡֵ--���չ�˾
			String icompany = json.getString("icompany");
			//����
			String itype = json.getString("itype");
			//��������
			String ideadline = json.getString("ideadline");
			String date = json.getString("idate");
			String expiredate = json.getString("iexpiredate");
			//���
			UFDouble money = new UFDouble(json.getString("money"));
			//���������ں͵�������ת����UFDouble�ĸ�ʽ
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
			//��������
			UFDate idate = new UFDate( timeFormat.parse(date));
			//��������
			UFDate iexpiredate = new UFDate( timeFormat.parse(expiredate));
			//�ӱ�ֵ--���չ�˾
			bvo.setIcompany(icompany);	
			//��������
			bvo.setIdate(idate);
			//��������
			bvo.setIdeadline(ideadline);
			//��������
			bvo.setIexpiredate(iexpiredate);
			//����
			bvo.setItype(itype);
			//���
			bvo.setMoney(money);
			//�ӱ���������PK
			bvo.setPk_insurance(pk_insurance);
			//����״̬Ϊ����
			bvo.setStatus(VOStatus.NEW);
			//������ӱ�VO���뼯��
			newChildren.add(bvo);
		}
		//���ش����ӱ�Vo�ļ���
		return newChildren;
	}
	
	/**
	 * ���AggVO����
	 * ������InsuranceHVO insuranceHVO InsuranceBVO[] insuranceBVOs
	 * @return AggInsuranceHVO����
	 */
	private AggInsuranceHVO[] getAggVos(InsuranceHVO insuranceHVO,InsuranceBVO[] insuranceBVOs){
		//�½�aggVO
		AggInsuranceHVO aggInsuranceHVO = new AggInsuranceHVO();
		//ΪaggInsuranceHVO��ֵ--����VO
		aggInsuranceHVO.setParentVO(insuranceHVO);
		//�ӱ�VO����
		aggInsuranceHVO.setChildrenVO(insuranceBVOs);
		//�½�AggVO���鲢��ֵ
		AggInsuranceHVO[] aggVos = new AggInsuranceHVO[]{aggInsuranceHVO};
		//����
		return aggVos;
	}


	/**
	 * ɾ������
	 * ������JSONObject json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject deleInsurance(JSONObject json) throws JSONException {
		//���ڷ���ǰ̨
		JSONObject result = new JSONObject();
		// ��������
		String pk_insurance = json.getString("pk_insurance");
		//���ڵõ����ӱ�VO�����sql
		String sql = "dr = 0 and pk_insurance = '"+ pk_insurance + "'";
		//�½����ӱ�VO����
		InsuranceHVO[] insuranceHVOs = null;
		InsuranceBVO[] insuranceBVOs = null;
		try {
			//�õ�����VO����
			insuranceHVOs = (InsuranceHVO[])hYPubBO.queryByCondition(InsuranceHVO.class, sql);
			//�õ��ӱ�VO����
			insuranceBVOs = (InsuranceBVO[])hYPubBO.queryByCondition(InsuranceBVO.class, sql);
		} catch (UifException e1) {
			ExceptionUtils.wrappBusinessException("��ȡ���յ��ӱ���Ϣʧ�ܣ�"+e1.getMessage());
		}
			
		try {
			//ɾ�����ӱ�VO����
			dao.deleteVOArray(insuranceHVOs);
			dao.deleteVOArray(insuranceBVOs);
			result.put("result", "true");
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException("ɾ�����յ���Ϣʧ�ܣ�"+e.getMessage());
		}
		//����
		return result;
	}
	/**
	 * ��ѯ���յ�
	 * ������JSONObject json
	 * @return 
	 * @return 
	 * @throws Exception
	 */
	private JSONObject queryInsurance(JSONObject json) throws Exception {
		JSONObject result = new JSONObject();
		//��Ų�ѯ������������
		JSONArray jsonArray = new JSONArray();
		int maxnum = json.getInt("maxcount"); // �������
		int minnum = json.getInt("mincount"); // ��С����
		//��ѯ����pk�����ݺš����ƺ�pk�����ƺš�������λ�����š��������͡��������ʵ�sql
		String sql="SELECT * FROM (SELECT ROWNUM AS rowno, t.* FROM (select "
				+ "cl_insurance.pk_insurance, cl_insurance.billno, cl_vehicle.pk_vehicle,"
				+ "cl_vehicle.vehicleno,org_hrorg.name as unit,org_dept.name as dept"
				+ ",cl_vehicle.vtype,cl_vehicle.vcharacter  from cl_insurance left join "
				+ "cl_vehicle on cl_vehicle.pk_vehicle = cl_insurance.vehicleno left join "
				+ "org_hrorg on org_hrorg.pk_hrorg = cl_vehicle.unit left join org_dept "
				+ "on org_dept.pk_dept = cl_vehicle.dept and cl_insurance.dr = 0 order by "
				+ "cl_insurance.ts desc) t where ROWNUM <= "+maxnum+") table_alias WHERE table_alias.rowno > "+minnum;
		//�½��������ڴ�Ų�ѯ���
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
		//����������ѯ�������ݷ���json
		for (Map<String,String> map : list) {
			//�������һ�����ݵ�json
			JSONObject jsonObj = new JSONObject();
			//ȡֵ ����pk
			String pk_insurance = map.get("pk_insurance");
			//���ݺ�
			String billno = map.get("billno");
			//���ƺ�
			String vehicleno = map.get("vehicleno");
			//��λ--ȡ������Ϣ��λ
			String unit = map.get("unit");
			//����--ȡ������Ϣ����
			String dept = map.get("dept");
			//��������--ȡ������Ϣ�ĳ�������
			String vtype = map.get("vtype");
			//��������--ȡ������Ϣ�ĳ�������
			String vcharacter = map.get("vcharacter");
			//���ƺ�pk
			String pk_vehicle = map.get("pk_vehicle");
			//��ֵ--���յ�����pk
			jsonObj.put("pk_insurance", pk_insurance);
			//���ݺ�
			jsonObj.put("billno", billno);
			//���ƺ�pk
			jsonObj.put("pk_vehicle", pk_vehicle!=null?map.get("pk_vehicle"):"");
			//���ƺ�
			jsonObj.put("vehicleno", vehicleno!=null?map.get("vehicleno"):"");
			//��λ
			jsonObj.put("unit", unit!=null?map.get("unit"):"");
			//����
			jsonObj.put("dept", dept!=null?map.get("dept"):"");
			//��������
			jsonObj.put("vtype", vtype!=null?map.get("vtype"):"");
			//��������
			jsonObj.put("vcharacter", vcharacter!=null?map.get("vcharacter"):"");
			//��ѯ�ӱ�pk�����ս��������ޡ����֡��������ڡ��������ڡ����չ�˾
			String bvoSql = "select pk_insurance_b,money,ideadline,itype,"
					+ "idate,iexpiredate,icompany from cl_insurance_b where dr = 0 and  "
					+ " pk_insurance = '" + pk_insurance + "'";
			//����ӱ����ݼ���
			List<Map<String,String>> bvoList= (List<Map<String, String>>) dao.executeQuery(bvoSql, new  MapListProcessor());
			//���÷������õ�������б��յ��ӱ���Ϣ��Jsonarray
			JSONArray array = getData(bvoList);
			jsonObj.put("body", array);
			jsonArray.put(jsonObj);
		}
		result.put("values",jsonArray);
		result.put("result","true");
		return result;
	}
		
	/**
	 * ������ֵ
	 * ������List<Map<String, Object>> list
	 * @return JSONArray
	 * @throws Exception
	 */
	private JSONArray getData (List<Map<String, String>> list) throws Exception {
		//����������ݵ�json
		JSONArray json = new JSONArray();
		//���ӱ����ݽ��е���������
		for(Map map : list){
			Iterator it =  map.keySet().iterator();
			JSONObject js = new JSONObject();
			while(it.hasNext()){
				String key = it.next().toString();
				//����ʼ���ںͽ���������Ҫ���⴦��
				if ("money".equals(key)) {
					js.put(key, map.get(key) == null ? "" : map.get(key));
					continue;
				}
				//��������ȡ������
				else if ("idate".equals(key)) {
					String idate = (String) map.get(key);
					String value = idate.substring(0,10);
					js.put(key, value == null ? "" : value);
					continue;
				}
				//��������ȡ������
				else if ("iexpiredate".equals(key)) {
					String iexpiredate = (String) map.get(key);
					String value = iexpiredate.substring(0,10);
					js.put(key, value == null ? "" : value);
					continue;
				}
				String value = (String) map.get(key);
				js.put(key, value == null ? "" : value);
			}
			//��һ�����ݷ���json��
			json.put(js);
		}
		//����
		return json;
	}



	/**
	 * �������յ�
	 * ������JSONObject json
	 * @return JSONObject
	 * @throws JSONException 
	 */
	private JSONObject addInsurance(JSONObject json) throws JSONException {
		//���ڷ��ص�JSONObject
		JSONObject result = new JSONObject();
		//�ӱ�����
		JSONArray bjson = json.getJSONArray("body");
		//���ƺ�
		String vehicleno = json.getString("pk_vehicle"); // ���ƺ�
		JSONObject userjsonObject = json.getJSONObject("userjson"); // ��¼��
		String cuserid = userjsonObject.getString("cuserid"); //��ǰ��¼������
		String pk_org = userjsonObject.getString("pk_org"); //��ǰ��¼��������֯
		String pk_group = userjsonObject.getString("pk_group"); //��ǰ��¼����������
		//�½�����VO	
		InsuranceHVO insuranceHVO = new InsuranceHVO();
		//��ֵ--���ó��ƺ�
		insuranceHVO.setVehicleno(vehicleno);
		//���ü���--ȡ�û���������
		insuranceHVO.setPk_group(pk_group);
		//������֯--ȡ�û�������֯
		insuranceHVO.setPk_org(pk_org);
		//���ô�����
		insuranceHVO.setAttributeValue("creator", cuserid);
		//�õ��ӱ���
		List<InsuranceBVO> bvos = null;
		try {
			//���ø�ֵ�����õ��ӱ�vo����
			bvos = getBvos(bjson, "");
		} catch (Exception e2) {
			ExceptionUtils.wrappBusinessException("����ӱ�����ʧ�ܣ�"+e2.getMessage());
		}
		//�ӱ�VO����
		InsuranceBVO[] vos = new InsuranceBVO[bjson.length()];
		//ȡ�������е�VO�������ӱ�VO������
		for (int i = 0; i < bvos.size(); i++) {
			//��ֵ
			vos[i] = bvos.get(i);
		}
		//���÷����õ�AggVO����
		AggInsuranceHVO[]  aggInsuranceHVOs = getAggVos(insuranceHVO, vos);
		// ����GroupId���ʹ�����
		try {
			InvocationInfoProxy.getInstance().setGroupId(pk_group);
			InvocationInfoProxy.getInstance().setUserId(cuserid);
		} catch (Exception e1) {
			ExceptionUtils.wrappBusinessException("ͨ�ô���error"+e1.getMessage());
		}
		//���յ���׼�ӿ�
		IInsuranceFilesMaintain service = (IInsuranceFilesMaintain) NCLocator
				.getInstance().lookup(IInsuranceFilesMaintain.class);
		try {
			//������������
			AggInsuranceHVO[] aggVos = service.insert(aggInsuranceHVOs,null);
			result.put("result", "true");
		} catch (nc.vo.pub.BusinessException e) {
			ExceptionUtils.wrappBusinessException("��ӱ��յ���Ϣʧ�ܣ�"+e.getMessage());
		}
		return result;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

}
