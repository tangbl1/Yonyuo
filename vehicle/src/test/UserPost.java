import nc.itf.post.servlet.QueryPostServlet;
import nc.itf.stgl.pojo.token;
import nc.itf.stgl.pojo.tokenParam;
import nc.itf.stgl.util.getToken;
import nc.vo.pub.BusinessException;

import java.io.UnsupportedEncodingException;

public class UserPost {
    public static void main(String[] args) throws UnsupportedEncodingException, BusinessException {
        String code = "1746598822436929544";
        tokenParam tp = new tokenParam();
        //��ȡtoken����
        getToken getToken1 = new getToken();
        token token1 = getToken1.getAccessToken(tp.getAppid(),tp.getScret());
        //�õ�token
        String accessToken = token1.getAccessToken();
        //�ѿռ������¼
        String url = "https://openapi.yonyoucloud.com/certified/userInfo/"+code+"?access_token="+accessToken;
        String json0="";
        json0 = new QueryPostServlet().getUser(url);
        System.out.println(json0);

}
}
