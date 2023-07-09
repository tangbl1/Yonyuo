package nccloud.web.so.saleorder.action;

import nc.bs.dao.DAOException;
import nc.itf.so.IWeighBridgeToNcSalesOrderMaintain;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import net.sf.json.JSONObject;

public class DownloadWeighdataAction implements ICommonAction {
    @Override
    public Object doAction(IRequest iRequest) {

        //手动下载当月数据
        IWeighBridgeToNcSalesOrderMaintain server = ServiceLocator.find(IWeighBridgeToNcSalesOrderMaintain.class);
        JSONObject jsonObject = server.weighbridgeToTable();
        String whether = (String) jsonObject.get("whether");
        if ("success".equals(whether)) {
                jsonObject = server.tableToNcSalesOrder();
               return jsonObject;

        }



        return jsonObject;
    }
}
