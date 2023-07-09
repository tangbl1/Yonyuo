package nc.itf.so;

import nc.bs.dao.DAOException;
import net.sf.json.JSONObject;

public interface IWeighBridgeToNcSalesOrderMaintain {

    public JSONObject weighbridgeToTable();

    public JSONObject tableToNcSalesOrder() ;
}
