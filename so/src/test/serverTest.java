import nc.vo.pub.lang.UFDateTime;
import net.sf.json.JSONObject;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

public class serverTest {

    public static void main(String[] args) {

        String webService = "http://39.152.48.75:18090/hw_weight/services/WeightHandle?wsdl";
        String fromSQL = "select 序号, 流水号,车号,过磅类型,发货单位,收货单位,货名,规格,convert(varchar(20),毛重) 毛重, convert(varchar(20),皮重) 皮重, convert(varchar(20),净重) 净重,convert(varchar(20),扣重) 扣重, convert(varchar(20),实重) 实重, convert(varchar(20),单价) 单价,convert(varchar(20),金额) 金额, convert(varchar(20),折方系数) 折方系数, convert(varchar(20),方量) 方量,convert(varchar(20),过磅费) 过磅费, 毛重司磅员,皮重司磅员,毛重磅号,皮重磅号,毛重时间 毛重时间,皮重时间 皮重时间,一次过磅时间 一次过磅时间,二次过磅时间 二次过磅时间,更新人,更新时间 更新时间,备注,打印次数,上传否,备用1,备用2,备用13 是否更新,备用14 发货时间 from 称重信息";
        UFDateTime daytime = new UFDateTime(new java.util.Date());
        String beginBeforeDay = "2023-07-05 00:00:00";
        String endBeforeDay =  "2023-07-30 23:59:59";
//        String beginBeforeDay = "2023-07-01 00:00:00";
//        String endBeforeDay = "2023-07-31 12:00:00";
        String query = fromSQL
                + " where 更新时间 >= '" + beginBeforeDay + "' "
                + "and 更新时间 <= '" + endBeforeDay + "' "
                + " and "
                + "  货名 in  ('铁精粉','铁精粉(罕王)') ";

        JSONObject jsonobject = new JSONObject();
        jsonobject.put("doType", "query");
        jsonobject.put("sqlStr", query);
        Service service = new Service();
        Call call = null;
        try {
            call = (Call) service.createCall();
            call.setTargetEndpointAddress(new URL(webService));//"http://39.152.48.75:1809/hw_weight/services/WeightHandle?wsdl"
            call.setOperationName("opeHandle");
            String result = (String) call.invoke(new Object[]{jsonobject.toString()});
            System.out.println(result);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


    }

}
