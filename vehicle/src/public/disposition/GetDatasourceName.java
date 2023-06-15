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
        // 从Item节点中获得appId节点
        List<Element> childNodes = root.getChildren("datasourcename");
        String datasourcesname = childNodes.get(0).getText();
        if (datasourcesname == null) {
            ExceptionUtils.wrappBusinessException("orderurl为空,请检查配置文件!!");
        }
        return datasourcesname;
    }

}
