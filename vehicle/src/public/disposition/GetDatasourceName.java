package disposition;

import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class GetDatasourceName {

    public String doreadxml() throws JDOMException, IOException {
        String path = this.getClass().getResource("GetDatasource.xml").getPath();
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(fis);
        Element root = doc.getRootElement();
        // ��Item�ڵ��л��appId�ڵ�
        List<Element> childNodes = root.getChildren("datasourcename");
        String datasourcesname = childNodes.get(0).getText();
        if (datasourcesname == null) {
            ExceptionUtils.wrappBusinessException("orderurlΪ��,���������ļ�!!");
        }
        return datasourcesname;
    }

}
