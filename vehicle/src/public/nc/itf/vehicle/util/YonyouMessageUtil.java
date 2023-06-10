package nc.itf.vehicle.util;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.yonyou.iuap.corp.demo.crypto.SignHelper;
import com.yonyou.iuap.corp.demo.model.AccessTokenResponse;
import com.yonyou.iuap.corp.demo.model.GenericResponse;
import com.yonyou.iuap.corp.demo.utils.RequestTool;

/**
 * �ѿռ䷢��Ӧ��֪ͨ
 * @author Zmy
 *
 */
public class YonyouMessageUtil {

//	// ������ƾ֤id
//	public static final String APPID = "bf10f69e652adee3";
//	// ������ƾ֤��Կ
//	public static final String SECRET = "085a04a021322cf460f8c76c3f79e68335ada3bb8e28f0b45c6c197544d5";
//	//

//	// https://open.yonyoucloud.com/developer/doc?id=f97cf7f5cc1abe83d1dad1454ceb1757
//	public static final String GETTOKEN_URL = "http://openapi.yonyoucloud.com/token?";
//	// �����û�������ֻ�����ѿռ��û�memberid[post]
//	// https://open.yonyoucloud.com/developer/doc?id=bd37dc6065d8375e14a05ecb711479a6
	public static final String GETMEMBERID_URL = "http://openapi.yonyoucloud.com/user/getMemberId?";
	// ����Ӧ��֪ͨ[post]
	// https://open.yonyoucloud.com/developer/doc?id=87d473451c6ef88eeaf0f5f326a1c056
	public static final String SENDMESSAGE_URL = "http://openapi.yonyoucloud.com/rest/message/app/share?";

	// 2023-06-08�޸� by tangbl
	//�ѿռ�API������Ȩ
	//appkey
	static String appKey = "4b979c1ce03944d694be80c1e4242b77";
	// ������ƾ֤��Կ
	static String appSecret = "343c68bcfd4f4029a6dfa7afc357545a";

	//��ҵ�Խ�Ӧ�û�ȡaccess_token[get]
	//https://yonbip.diwork.com/iuap-api-auth/open-auth/selfAppAuth/getAccessToken?appKey=4b979c1ce03944d694be80c1e4242b77&timestamp=1686029956013&signature=wgZHTV9jQNqUXJuiHtM3yYPkaqywIdC9VLh1FsZFeMs%3D
	static String accessTokenUrl = "https://yonbip.diwork.com/iuap-api-auth/open-auth/selfAppAuth/getAccessToken";



	/**
	 * ��ҵ�Խ�Ӧ�û�ȡaccess_token
	 * 
	 * @return
	 */
	public static String getAccessToken() {

		String accessToken = "";
		try {
			Map<String, String> params = new HashMap<>();
			// ��ǩ�������������
			params.put("appKey", appKey);
			//ʱ���
			String timestamp = String.valueOf(System.currentTimeMillis());
			params.put("timestamp",timestamp);
			// ����ǩ��
			String signature = null;
			signature = SignHelper.sign(params, appSecret);
			params.put("signature", signature);
			GenericResponse<AccessTokenResponse> response = RequestTool.doGet(accessTokenUrl, params, new TypeReference<GenericResponse<AccessTokenResponse>>() {});
			if (response.isSuccess()) {
				accessToken = response.getData().getAccessToken();
			}
		} catch (Exception e) {
			throw new RuntimeException("��ȡaccessTokenʧ�ܣ�");
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
			// �ƶ�����ת��ַ
			paramlist.add(paramE);
		}
		return paramlist;
	}

	public static void main(String[] args) {
		String accessToken = getAccessToken();
//		String memberId = getMemberId(accessToken, "zhangmym@yonyou.com", "2");
		String memberId = getMemberId(accessToken, "13032422361", "1");
		List<String> tos = new ArrayList<String>();
		tos.add(memberId);
		boolean result = sendMessage(accessToken, messagePojo(tos ,"ʳ����������", "[���]��12Ԫ����"));
		System.out.println(result);
		System.exit(0);
	}

}