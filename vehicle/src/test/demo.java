import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.yonyou.iuap.corp.demo.crypto.SignHelper;
import com.yonyou.iuap.corp.demo.model.AccessTokenResponse;
import com.yonyou.iuap.corp.demo.model.GenericResponse;
import com.yonyou.iuap.corp.demo.model.UserIdResponse;
import com.yonyou.iuap.corp.demo.utils.RequestTool;
import nc.itf.vehicle.util.HttpRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class demo {


    public static final String SENDMESSAGE_URL = "https://yonbip.diwork.com/iuap-api-gateway/yonbip/uspace/notify/share?";

    static String GETMEUSERID_URL = "https://yonbip.diwork.com/iuap-api-gateway/yonbip/uspace/staff/info_by_mobile_email";

    //友空间API调用授权
    static String appKey = "4b979c1ce03944d694be80c1e4242b77";
    // 第三方凭证密钥
    static String appSecret = "343c68bcfd4f4029a6dfa7afc357545a";

    static String accessTokenUrl = "https://yonbip.diwork.com/iuap-api-auth/open-auth/selfAppAuth/getAccessToken";

    public static void main(String[] args) throws IOException {

        String accessToken = getAccessToken();
        System.out.println(accessToken);
        String message ="有新的用车申请单部门审批已完成，请注意查看处理。";
        String user_id = getMemberId(accessToken,"18340319070","1");
        System.out.println(user_id);
        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("55c77f65-022d-42b1-b98b-ec991a706515");
        arrayList.add(user_id);
        boolean b  = sendMessage(accessToken,messagePojo(arrayList,"车辆管理测试通知",message));
        System.out.println(b);


    }


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
        params.put("access_token",accessToken);
        // 手机号
        params.put("field", field);
        // 查询类型：1-手机，2-邮箱
        params.put("type",type);

        try {
            GenericResponse<List<UserIdResponse>> response = RequestTool.doGet(GETMEUSERID_URL, params, new TypeReference<GenericResponse<List<UserIdResponse>>>() {});
            if (response != null && Integer.valueOf(response.getCode()) == 200) {
//                user_id = response.getData().getUser_id();
//                ArrayList<HashMap<String,String>>  arrayList = (ArrayList<HashMap<String, String>>) response.getData();
//                user_id = arrayList.get(0).get("user_id");
            }
        } catch (IOException e) {
            throw new RuntimeException("获得友空间用户user_id失败！");
        }
        return  user_id;

    }

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

    public static boolean sendMessage(String accessToken, List<Map<String, Object>> param) {
        String URL = SENDMESSAGE_URL + "access_token=" + accessToken;
        JSONObject memberIdRes = HttpRequest.sendPost(URL, param);
        if(memberIdRes != null && !memberIdRes.isEmpty()){

            if (Integer.parseInt(memberIdRes.getString("code")) == 200) {
                return true;
            }
        }
        return false;
    }

    }
