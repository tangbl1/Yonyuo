import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yonyou.diwork.exception.BusinessException;
import nc.itf.vehicle.util.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class oldclgl {


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


    public static void main(String[] args) {

        String ass = getAccessToken();
        System.out.println("token:"+ass);
        try {
            String m = getMemberId(ass,"18340319070","1");
            System.out.println("id :"+m);
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }


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
     * @throws BusinessException
     */
    public static String getMemberId(String accessToken, String field,
                                     String type) throws BusinessException {
        String memberId = "";
        JSONObject json = new JSONObject();
//		String memberIdUrl = GETMEMBERID_URL + "access_token=" + accessToken
//				+ "&field=" + field + "&type=" + type;
//		JSONObject memberIdRes = JSON.parseObject(HttpRequest.sendPost(memberIdUrl, json.toJSONString()));
        String memberIdUrl = GETMEMBERID_URL+"access_token=" + accessToken+ "&field=" + field + "&type=" + type ;
        String  para = "access_token=" + accessToken+ "&field=" + field + "&type=" + type;
        JSONObject memberIdRes = JSON.parseObject(sendMsgByPost(memberIdUrl, para)); //20230522-修改调用方法


        if (Integer.parseInt(String.valueOf(memberIdRes.get("code"))) == 0) {
            memberId = memberIdRes.getJSONObject("data").getString("id");
        }
        return memberId;
    }

    /**
     * post方式发送
     * @param param
     */
    public static String sendMsgByPost(String url,String para)throws BusinessException{
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);//连接超时时间
            //获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
            out.write(para);

            // 发送请求参数
            //   out.print(json);
            //flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("post方式调用接口错误:"+e.getMessage());
        }finally {
            try {
                if (out != null) { //关闭输出流、输入流
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }




}


