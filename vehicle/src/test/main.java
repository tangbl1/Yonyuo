import com.alibaba.fastjson.JSONObject;
import nc.itf.vehicle.util.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class main {

    public static final String SENDMESSAGE_URL = "http://openapi.yonyoucloud.com/rest/message/app/share?";


    public static void main(String[] args) {

        String accessToken = "ZyQAAAAAAAABMAAAAAAAAAEwyHHZczSKAQAAAAAAAADEQinrZMSinOpkDWl1YXAtdXVhcy1jYXMAAJA3Ffz7R6VHRrUfArfvs2x5B2RjY29yZTAA";
        List<String> tos = new ArrayList<String>();
//        tos.add("3675182");//memberId
        tos.add("55c77f65-022d-42b1-b98b-ec991a706515");
        List<Map<String, Object>> mapList = messagePojo(tos,"车辆管理通知测试","您的用车申请单已审批完成！");
        sendMessage(accessToken,mapList);



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
        String yhtUserIds = SENDMESSAGE_URL + "access_token=" + accessToken;
        JSONObject memberIdRes = HttpRequest.sendPost(yhtUserIds, param);
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

}


