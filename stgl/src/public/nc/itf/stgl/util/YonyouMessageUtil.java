package nc.itf.stgl.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import nc.itf.vehicle.util.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * �ѿռ䷢��Ӧ��֪ͨ
 * @author Zmy
 *
 */
public class YonyouMessageUtil {

	// ������ƾ֤id
	public static final String APPID = "bf10f69e652adee3";
	// ������ƾ֤��Կ
	public static final String SECRET = "085a04a021322cf460f8c76c3f79e68335ada3bb8e28f0b45c6c197544d5";
	// ��ҵ�Խ�Ӧ�û�ȡaccess_token[get]
	// https://open.yonyoucloud.com/developer/doc?id=f97cf7f5cc1abe83d1dad1454ceb1757
	public static final String GETTOKEN_URL = "http://openapi.yonyoucloud.com/token?";
	// �����û�������ֻ�����ѿռ��û�memberid[post]
	// https://open.yonyoucloud.com/developer/doc?id=bd37dc6065d8375e14a05ecb711479a6
	public static final String GETMEMBERID_URL = "http://openapi.yonyoucloud.com/user/getMemberId?";
	// ����Ӧ��֪ͨ[post]
	// https://open.yonyoucloud.com/developer/doc?id=87d473451c6ef88eeaf0f5f326a1c056
	public static final String SENDMESSAGE_URL = "http://openapi.yonyoucloud.com/rest/message/app/share?";

	/**
	 * ��ҵ�Խ�Ӧ�û�ȡaccess_token
	 * 
	 * @return
	 */
	public static String getAccessToken() {
		String accessToken = "";
		String tokenUrl = GETTOKEN_URL + "appid=" + APPID + "&secret=" + SECRET;
//		JSONObject tokenRes = HttpRequest2.sendGet(tokenUrl, null);
		JSONObject tokenRes = HttpRequest.sendGet(tokenUrl, new ArrayList<String>());
		if (Integer.parseInt(String.valueOf(tokenRes.get("code"))) == 0) {
			accessToken = tokenRes.getJSONObject("data").getString("access_token");
		}
		return accessToken;
	}

	/**
	 * �����û�������ֻ�����ѿռ��û�memberid
	 * 
	 * @param accessToken
	 *            ��¼ƾ֤
	 * @param field
	 *            �����ֶΣ�Ŀǰ֧��������ֻ�)
	 * @param type
	 *            1���ֻ� 2������
	 * @return
	 */
	public static String getMemberId(String accessToken, String field,
			String type) {
		/**
		 * begin 2023-05-26 �����������������ʧ�ܵ�����
		 */
		field = field.replace(" ","");
		/**
		 * end
		 */
		String memberId = "";
		JSONObject json = new JSONObject();
		String memberIdUrl = GETMEMBERID_URL + "access_token=" + accessToken
				+ "&field=" + field + "&type=" + type;
		JSONObject memberIdRes = JSON.parseObject(HttpRequest.sendPost(memberIdUrl, json.toJSONString()));
		if (Integer.parseInt(String.valueOf(memberIdRes.get("code"))) == 0) {
			memberId = memberIdRes.getJSONObject("data").getString("id");
		}
		return memberId;
	}

	/**
	 * ����Ӧ��֪ͨ
	 * 
	 * @param accessToken
	 *            ��¼ƾ֤
	 * @param param
	 *            ����
	 * @return
	 */
	public static boolean sendMessage(String accessToken, List<Map<String, Object>> param) {
		String memberIdUrl = SENDMESSAGE_URL + "access_token=" + accessToken;
		JSONObject memberIdRes = HttpRequest.sendPost(memberIdUrl, param);
		if(memberIdRes != null && !memberIdRes.isEmpty()){
			if (Integer.parseInt(String.valueOf(memberIdRes.get("flag"))) == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ��װ����֪ͨ�������
	 * @param tos ���ն�������
	 * @param title ����
	 * @param desc ����
	 * @return
	 */
	public static List<Map<String, Object>> messagePojo(List<String> tos, String title, String desc) {
		List<Map<String, Object>> paramlist = new ArrayList<Map<String, Object>>();
		if(tos != null && tos.size() > 0){
			Map<String, Object> paramE = new HashMap<String, Object>();
			// �ռ�Id
			paramE.put("spaceId", "121905");
			// Ӧ��Id
			paramE.put("appId", "bf10f69e652adee3");
			// ����;����appNotify:Ӧ��֪ͨ
			paramE.put("sendThrough", "appNotify");
			// ���ͷ�Χ��all����ǰ�ռ��ȫ����Ա��list��to�б��г�Ա
			paramE.put("sendScope", "list");
			// ���Ͷ��󣬿ռ��û�memberId����
			paramE.put("to", tos.toArray());
			// ����
			paramE.put("title", title);
			// ��������
			paramE.put("desc", desc);
			if("������������".equals(title)){
				//zhoujing 
				paramE.put("webUrl", "http://49.4.14.125:8611/Mine/Mine/main.html?temp=Works_myworkList&name=%E5%BE%85%E5%8A%9E%E5%B7%A5%E4%BD%9C/code=${esncode}");
			}
			/*paramE.put("detailUrl", "http://49.4.14.125:8611/Mine/Mine/main.html?temp=Works_myworkList&name=%E5%BE%85%E5%8A%9E%E5%B7%A5%E4%BD%9C/code=${esncode}");
			//paramE.put("detailUrl", "http://www.baidu.com");
			Map<String, Object> paramA = new HashMap<String, Object>();
			paramA.put("systemDefaultBrowser", "true");
			paramE.put("attributes",paramA);*/
			// �ƶ�����ת��ַ
			paramlist.add(paramE);
		}
		return paramlist;
	}

	public static void main(String[] args) {
		String accessToken = getAccessToken();
//		String memberId = getMemberId(accessToken, "zhangmym@yonyou.com", "2");
		String memberId = getMemberId(accessToken, "17609814307", "1");
		List<String> tos = new ArrayList<String>();
		tos.add(memberId);
		//boolean result = sendMessage(accessToken, messagePojo(tos ,"ʳ����������", "[���]��12Ԫ����"));
		boolean result1 = sendMessage(accessToken, messagePojo(tos ,"������������", "������������"));
		System.out.println(result1);
		System.exit(0);
	}

}
