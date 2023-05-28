
package nc.impl.dd.refreshcost.refreshcosthvo;

import nc.ui.pub.print.IDataSource;
import nccloud.pubitf.platform.print.AbstractPrintService;
import nccloud.pubitf.platform.print.IPrintInfo;
import nccloud.pubitf.platform.print.vo.PrintInfo;

public class AggRefreshCostHVOPrintServiceImpl extends AbstractPrintService {
	
	@Override
    public IDataSource[] getDataSources(IPrintInfo info) {
        PrintInfo printinfo = (PrintInfo) info;
        String[] ids = printinfo.getIds();
        AggRefreshCostHVOPrintDataSource ds = new AggRefreshCostHVOPrintDataSource(ids);
       	return new IDataSource[] { ds };
       
    }
}