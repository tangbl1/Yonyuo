import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class demotest {
    public static void main(String[] args) {
        Map<String, Object> headMap = new HashMap<>();
//        headMap.put("status","成功");
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("code",1);
        Object[] products = new Object[]{bodyMap};
        headMap.put("data", products);
        JSONObject js = new JSONObject(headMap);
        System.out.println(js.toJSONString());


    }
}
