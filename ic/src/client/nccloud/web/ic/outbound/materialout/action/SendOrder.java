package nccloud.web.ic.outbound.materialout.action;

import com.alibaba.fastjson.JSONObject;
import nc.bs.framework.common.InvocationInfoProxy;

import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.framework.service.ServiceLocator;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: NC65_DD
 * @Package: nc.ui.ic.m4d.action
 * @ClassName: GetToken
 * @Author: 15749
 * @Description:
 * @Date: 2022/10/20 11:54
 * @Version: 1.0
 */
public class SendOrder {

    public String getAccessToken(String user_id) throws BusinessException, IOException, JDOMException {

        IUAPQueryBS query = ServiceLocator.find(IUAPQueryBS.class);
        // 获得xml文件中的item节点
        Element root = doreadxml();
        // 从Item节点中获得appId节点
        List<Element> childNodes = root.getChildren("tokenurl");
        String tokenurl = childNodes.get(0).getText();
        if (isSEmptyOrNull(tokenurl)) {
            ExceptionUtils.wrappBusinessException("tokenurl为空,请检查配置文件!!");
        }
        String sql = "select mobile from  bd_psndoc  where pk_psndoc = '"+user_id+"'";
        String inputMobile;
        try {
            inputMobile = (String) query.executeQuery(sql, new ColumnProcessor());

        } catch (BusinessException e) {
            throw new RuntimeException("inputMobile获取失败！");
        }
        if (inputMobile == null || inputMobile.length() < 11) {
            childNodes = root.getChildren("inputMobile");
            inputMobile = childNodes.get(0).getText();
            if (isSEmptyOrNull(inputMobile)) {
                ExceptionUtils.wrappBusinessException("inputMobile为空,请检查配置文件!!");
            }
        }

        childNodes = root.getChildren("inputPassword");
        String inputPassword = childNodes.get(0).getText();
        if (isSEmptyOrNull(inputPassword)) {
            ExceptionUtils.wrappBusinessException("inputPassword为空,请检查配置文件!!");
        }

//        Request request = Request.Post("http://49.4.14.119/api/v1/at/auth/");
//
        String body = "inputMobile=" + inputMobile + "&inputPassword=" + inputPassword;

        Request request = Request.Post(tokenurl);
        //String body = "inputEmail=admin@hanking.com&inputPassword=Lee_0520";
        request.bodyString(body, ContentType.APPLICATION_FORM_URLENCODED);

        request.addHeader("User-Agent", "Apipost client Runtime/+https://www.apipost.cn/");
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");
        HttpResponse httpResponse = request.execute().returnResponse();
        String access_token = null;
        if (httpResponse.getEntity() != null) {
            String html = EntityUtils.toString(httpResponse.getEntity());
            JSONObject json = JSONObject.parseObject(html);
            access_token = (String) json.get("access_token");
        }
        return access_token;
    }

    public JSONObject Orders(String token, JSONObject body) throws IOException, JDOMException {
        // 获得xml文件中的item节点
        Element root = doreadxml();
        // 从Item节点中获得appId节点
        List<Element> childNodes = root.getChildren("orderurl");
        String orderurl = childNodes.get(0).getText();
        if (isSEmptyOrNull(orderurl)) {
            ExceptionUtils.wrappBusinessException("orderurl为空,请检查配置文件!!");
        }
        Request request = Request.Post(orderurl);
        request.bodyString(body.toString(), ContentType.APPLICATION_JSON);
        String Authorization = "Bearer " + token;
        request.addHeader("User-Agent", "Apipost client Runtime/+https://www.apipost.cn/");
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", Authorization);
        HttpResponse httpResponse = request.execute().returnResponse();
        Map<String, Object> returnMap = new HashMap<>();
        if (httpResponse.getEntity() != null) {
            int code = httpResponse.getStatusLine().getStatusCode();
            String html = EntityUtils.toString(httpResponse.getEntity());
            returnMap.put("code", code);
            returnMap.put("message", html);
        }
        return new JSONObject(returnMap);
    }

    public Element doreadxml() throws JDOMException, IOException {
        String path = this.getClass().getResource("GetApiInfo.xml").getPath();
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(fis);
        Element root = doc.getRootElement();
        return root;
    }

    public static boolean isSEmptyOrNull(String s) {
        return s == null ? true : s.trim().length() <= 0;
    }

}