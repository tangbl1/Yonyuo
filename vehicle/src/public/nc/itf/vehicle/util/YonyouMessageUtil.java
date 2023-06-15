package nc.itf.vehicle.util;
import java.io.IOException;
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
import com.yonyou.iuap.corp.demo.model.UserIdResponse;
import com.yonyou.iuap.corp.demo.utils.RequestTool;

/**
 * 友空间发送应用通知
 * @author Zmy
 *
 */
public class YonyouMessageUtil {

/*	// 第三方凭证id
	public static final String APPID = "bf10f69e652adee3";
	// 第三方凭证密钥
	public static final String SECRET = "085a04a021322cf460f8c76c3f79e68335ada3bb8e28f0b45c6c197544d5";
	//

	// https://open.yonyoucloud.com/developer/doc?id=f97cf7f5cc1abe83d1dad1454ceb1757
	public static final String GETTOKEN_URL = "http://openapi.yonyoucloud.com/token?";
	// 根据用户邮箱或手机获得友空间用户memberid[post]
	// https://open.yonyoucloud.com/developer/doc?id=bd37dc6065d8375e14a05ecb711479a6
	public static final String GETMEMBERID_URL = "http://openapi.yonyoucloud.com/user/getMemberId?";
	// 发送应用通知[post]
	// https://open.yonyoucloud.com/developer/doc?id=87d473451c6ef88eeaf0f5f326a1c056
	public static final String SENDMESSAGE_URL = "http://openapi.yonyoucloud.com/rest/message/app/share?";*/

	/**
	 * 2023-06-14 修改 by tangbl
	 */



	/**
	 * 获取租户所在数据中心域名
	 *
	 * @param URL ：
	 *            https://api.diwork.com/open-auth/dataCenter/getGatewayAddress?tenantId=itvqa0x6
	 * @param request ：
	 *            get
	 * @return
	 *              {
	 * 	  	"code": "00000",
	 * 	 	"message": "成功！",
	 * 	  	"data": {
	 * 	  		"gatewayUrl": "https://yonbip.diwork.com/iuap-api-gateway",
	 * 	  		"tokenUrl": "https://yonbip.diwork.com/iuap-api-auth"
	 *             }
	 */


	//友空间API调用授权
	static String appKey = "4b979c1ce03944d694be80c1e4242b77";
	// 第三方凭证密钥
	static String appSecret = "343c68bcfd4f4029a6dfa7afc357545a";

	//企业自建应用获取access_token[get]
	//https://yonbip.diwork.com/iuap-api-auth/open-auth/selfAppAuth/getAccessToken?appKey=4b979c1ce03944d694be80c1e4242b77&timestamp=1686029956013&signature=wgZHTV9jQNqUXJuiHtM3yYPkaqywIdC9VLh1FsZFeMs%3D
	static String accessTokenUrl = "https://yonbip.diwork.com/iuap-api-auth/open-auth/selfAppAuth/getAccessToken";

	//根据用户邮箱或手机获得友空间用户友户通user_id [get]
	//https://yonbip.diwork.com/iuap-api-gateway/yonbip/uspace/staff/info_by_mobile_email?access_token=0ec5b801bbba4cceb3116f6d5a98104b&field=18340319070&type=1
	static String GETMEUSERID_URL = "https://yonbip.diwork.com/iuap-api-gateway/yonbip/uspace/staff/info_by_mobile_email";

	// 发送应用幂通知[post]
	// https://yonbip.diwork.com/iuap-api-gateway/yonbip/uspace/rest/openapi/idempotent/work/notify/push?access_token=5065f07441e24d7a9b0f3294dcb349da
//	public static final String SENDMESSAGE_URL = "https://yonbip.diwork.com/iuap-api-gateway/yonbip/uspace/rest/openapi/idempotent/work/notify/push?";
	//发送工作通知
	//请求地址
	//https://yonbip.diwork.com/iuap-api-gateway/yonbip/uspace/notify/share?access_token=ccb76bcc22bb458dba8f9fe20f2f4646
	public static final String SENDMESSAGE_URL = "https://yonbip.diwork.com/iuap-api-gateway/yonbip/uspace/notify/share?";


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
			String signature = SignHelper.sign(params, appSecret);
			params.put("signature", signature);
			GenericResponse<AccessTokenResponse> response = RequestTool.doGet(accessTokenUrl, params, new TypeReference<GenericResponse<AccessTokenResponse>>() {});
			if (response.isSuccess()) {//code :00000
				accessToken = response.getData().getAccessToken();
			}
		} catch (Exception e) {
			throw new RuntimeException("获取accessToken失败！");
		}
		return accessToken;
	}

	/**
	 * 根据用户邮箱或手机获得友空间用户user_id
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
		String user_id = "";
		Map<String, String> params = new HashMap<>();
		// 手机号
		params.put("field", field);
		// 查询类型：1-手机，2-邮箱
		params.put("type",type);

		try {
			GenericResponse<UserIdResponse> response = RequestTool.doGet(GETMEUSERID_URL, params, new TypeReference<GenericResponse<UserIdResponse>>() {});
			if (response != null && Integer.valueOf(response.getCode()) == 200) {
				user_id = response.getData().getUser_id();
			}
		} catch (IOException e) {
			throw new RuntimeException("获得友空间用户user_id失败！");
		}
		return  user_id;

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
		// begin modifly by 2023-06-15
		String URL = SENDMESSAGE_URL + "access_token=" + accessToken;
		JSONObject memberIdRes = HttpRequest.sendPost(URL, param);
		if(memberIdRes != null && !memberIdRes.isEmpty()){

			if (Integer.parseInt(memberIdRes.getString("code")) == 200) {
				return true;
			}
		}
		return false;
	}
	// end  modifly by 2023-06-15


//		String yhtUserIds = SENDMESSAGE_URL + "access_token=" + accessToken;
//		JSONObject memberIdRes = HttpRequest.sendPost(yhtUserIds, param);
//		if(memberIdRes != null && !memberIdRes.isEmpty()){
//			if (Integer.parseInt(String.valueOf(memberIdRes.get("flag"))) == 0) {
//				return true;
//			}
//		}
//		return false;

	/**
	 * 组装发送通知所需参数
	 * @param yhtUserIds 接收对象数组
	 * @param title 标题
	 * @param content 内容
	 * @return
	 */
	// begin modifly by 2023-06-15
	public static List<Map<String, Object>> messagePojo(List<String> yhtUserIds, String title, String content) {
		List<Map<String, Object>> paramlist = new ArrayList<Map<String, Object>>();
		if(yhtUserIds != null && yhtUserIds.size() > 0){
			Map<String, Object> paramE = new HashMap<String, Object>();
			// 应用Id
			paramE.put("appId", "158200");//http://49.4.14.203:8181/clgl/index.html?code=1748875378452594694&qzId=121905&groupname=抚顺罕王傲牛矿业股份有限公司&appId=158200&serviceCode=f604dd8f-fcf8-be95-7d3b-f27250b546d7&locale=zh_CN&refimestamp=1686795890743
			//标题
			paramE.put("title",title);
			//内容
			paramE.put("content",content);
			//接收范围
			paramE.put("sendScope","list");
			//yhtUserIds列表
			paramE.put("yhtUserIds",yhtUserIds.toArray());

			paramlist.add(paramE);
		}
		return paramlist;
	}

	// end  modifly by 2023-06-15



//	public static List<Map<String, Object>> messagePojo(List<String> tos, String title, String desc) {
//		List<Map<String, Object>> paramlist = new ArrayList<Map<String, Object>>();
//		if(tos != null && tos.size() > 0){
//			Map<String, Object> paramE = new HashMap<String, Object>();
//			// 空间Id
//			paramE.put("spaceId", "121905");
//			// 应用Id
//			paramE.put("appId", "bf10f69e652adee3");
//			// 发送途径，appNotify:应用通知
//			paramE.put("sendThrough", "appNotify");
//			// 发送范围，all：当前空间的全部成员，list：to列表中成员
//			paramE.put("sendScope", "list");
//			// 发送对象，空间用户memberId集合
//			paramE.put("to", tos.toArray());
//			// 标题
//			paramE.put("title", title);
//			// 内容描述
//			paramE.put("desc", desc);
//			// 移动端跳转地址
//			paramlist.add(paramE);
//		}
//		return paramlist;
//	}

//	public static void main(String[] args) {
//		String accessToken = getAccessToken();
////		String memberId = getMemberId(accessToken, "zhangmym@yonyou.com", "2");
//		String memberId = getMemberId(accessToken, "13032422361", "1");
//		List<String> tos = new ArrayList<String>();
//		tos.add(memberId);
//		boolean result = sendMessage(accessToken, messagePojo(tos ,"食堂消费提醒", "[早餐]：12元整。"));
//		System.out.println(result);
//		System.exit(0);
//	}

}