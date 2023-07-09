import com.alibaba.fastjson.JSONObject;
import nc.itf.post.servlet.QueryPostServlet;
import nc.itf.stgl.pojo.token;
import nc.itf.stgl.pojo.tokenParam;
import nc.itf.stgl.util.getToken;
import nc.itf.vehicle.util.HttpRequest;
import nc.vo.pub.BusinessException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class UserPost {

    // 第三方凭证id
    public static final String APPID = "bf10f69e652adee3";
    // 第三方凭证密钥
    public static final String SECRET = "085a04a021322cf460f8c76c3f79e68335ada3bb8e28f0b45c6c197544d5";
    // 企业自建应用获取access_token[get]
    // https://open.yonyoucloud.com/developer/doc?id=f97cf7f5cc1abe83d1dad1454ceb1757
    public static final String GETTOKEN_URL = "http://openapi.yonyoucloud.com/token?";
    // 根据用户邮箱或手机获得友空间用户memberid[post]
    // https://open.yonyoucloud.com/developer/doc?id=bd37dc6065d8375e14a05ecb711479a6
    public static final String GETMEMBERID_URL = "http://openapi.yonyoucloud.com/user/getMemberId?";
    // 发送应用通知[post]
    // https://open.yonyoucloud.com/developer/doc?id=87d473451c6ef88eeaf0f5f326a1c056
    public static final String SENDMESSAGE_URL = "http://openapi.yonyoucloud.com/rest/message/app/share?";

    public static void main(String[] args) throws UnsupportedEncodingException, BusinessException {
        String code = "1746598822436929544";

        String accessToken = getAccessToken();
        System.out.println(accessToken);
//        tokenParam tp = new tokenParam();
//        //获取token方法
//        getToken getToken1 = new getToken();
//        token token1 = getToken1.getAccessToken(tp.getAppid(),tp.getScret());
//        //得到token
//        String accessToken = token1.getAccessToken();


        //友空间虚拟登录
        String url = "https://openapi.yonyoucloud.com/certified/userInfo/"+code+"?access_token="+accessToken;
        String json0="";
        json0 = new QueryPostServlet().getUser(url);
        System.out.println(json0);

}

    /**
     * 企业自建应用获取access_token
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
}
