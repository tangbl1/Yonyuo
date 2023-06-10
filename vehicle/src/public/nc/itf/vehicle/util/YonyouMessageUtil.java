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
 * 友空间发送应用通知
 * @author Zmy
 *
 */
public class YonyouMessageUtil {

//	// 第三方凭证id
//	public static final String APPID = "bf10f69e652adee3";
//	// 第三方凭证密钥
//	public static final String SECRET = "085a04a021322cf460f8c76c3f79e68335ada3bb8e28f0b45c6c197544d5";
//	//

//	// https://open.yonyoucloud.com/developer/doc?id=f97cf7f5cc1abe83d1dad1454ceb1757
//	public static final String GETTOKEN_URL = "http://openapi.yonyoucloud.com/token?";
//	// 根据用户邮箱或手机获得友空间用户memberid[post]
//	// https://open.yonyoucloud.com/developer/doc?id=bd37dc6065d8375e14a05ecb711479a6
	public static final String GETMEMBERID_URL = "http://openapi.yonyoucloud.com/user/getMemberId?";
	// 发送应用通知[post]
	// https://open.yonyoucloud.com/developer/doc?id=87d473451c6ef88eeaf0f5f326a1c056
	public static final String SENDMESSAGE_URL = "http://openapi.yonyoucloud.com/rest/message/app/share?";

	// 2023-06-08修改 by tangbl
	//友空间API调用授权
	//appkey
	static String appKey = "4b979c1ce03944d694be80c1e4242b77";
	// 第三方凭证密钥
	static String appSecret = "343c68bcfd4f4029a6dfa7afc357545a";

	//企业自建应用获取access_token[get]
	//https://yonbip.diwork.com/iuap-api-auth/open-auth/selfAppAuth/getAccessToken?appKey=4b979c1ce03944d694be80c1e4242b77&timestamp=1686029956013&signature=wgZHTV9jQNqUXJuiHtM3yYPkaqywIdC9VLh1FsZFeMs%3D
	static String accessTokenUrl = "https://yonbip.diwork.com/iuap-api-auth/open-auth/selfAppAuth/getAccessToken";



	/**
	 * 企业自建应用获取access_token
	 * 
	 * @return
	 */
	public static String getAccessToken() {

		String accessToken = "";
		try {
			Map<String, String> params = new HashMap<>();
			// 除签名外的其他参数
			params.put("appKey", appKey);
			//时间戳
			String timestamp = String.valueOf(System.currentTimeMillis());
			params.put("timestamp",timestamp);
			// 计算签名
			String signature = null;
			signature = SignHelper.sign(params, appSecret);
			params.put("signature", signature);
			GenericResponse<AccessTokenResponse> response = RequestTool.doGet(accessTokenUrl, params, new TypeReference<GenericResponse<AccessTokenResponse>>() {});
			if (response.isSuccess()) {
				accessToken = response.getData().getAccessToken();
			}
		} catch (Exception e) {
			throw new RuntimeException("获取accessToken失败！");
		}
		return accessToken;
	}

	/**
	 * 根据用户邮箱或手机获得友空间用户memberid
	 * 
	 * @param accessToken
	 *            登录凭证
	 * @param field
	 *            搜索字段（目前支持邮箱或手机)
	 * @param type
	 *            1：手机 2：邮箱
	 * @return
	 */
	public static String getMemberId(String accessToken, String field,
			String type) {
		/**
		 * begin 2023-05-26 解决车管审批报操作失败的问题
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
	 * 发送应用通知
	 * 
	 * @param accessToken
	 *            登录凭证
	 * @param param
	 *            参数
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
	 * 组装发送通知所需参数
	 * @param tos 接收对象数组
	 * @param title 标题
	 * @param desc 内容
	 * @return
	 */
	public static List<Map<String, Object>> messagePojo(List<String> tos, String title, String desc) {
		List<Map<String, Object>> paramlist = new ArrayList<Map<String, Object>>();
		if(tos != null && tos.size() > 0){
			Map<String, Object> paramE = new HashMap<String, Object>();
			// 空间Id
			paramE.put("spaceId", "121905");
			// 应用Id
			paramE.put("appId", "bf10f69e652adee3");
			// 发送途径，appNotify:应用通知
			paramE.put("sendThrough", "appNotify");
			// 发送范围，all：当前空间的全部成员，list：to列表中成员
			paramE.put("sendScope", "list");
			// 发送对象，空间用户memberId集合
			paramE.put("to", tos.toArray());
			// 标题
			paramE.put("title", title);
			// 内容描述
			paramE.put("desc", desc);
			// 移动端跳转地址
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
		boolean result = sendMessage(accessToken, messagePojo(tos ,"食堂消费提醒", "[早餐]：12元整。"));
		System.out.println(result);
		System.exit(0);
	}

}