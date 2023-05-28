
package nc.impl.vehicle.vorder.vorderhvo;

import nc.ui.pub.print.IDataSource;
import nccloud.pubitf.platform.print.AbstractPrintService;
import nccloud.pubitf.platform.print.IPrintInfo;
import nccloud.pubitf.platform.print.vo.PrintInfo;

public class AggVorderHVOPrintServiceImpl extends AbstractPrintService {
	
	@Override
    public IDataSource[] getDataSources(IPrintInfo info) {
        PrintInfo printinfo = (PrintInfo) info;
        String[] ids = printinfo.getIds();
        AggVorderHVOPrintDataSource ds = new AggVorderHVOPrintDataSource(ids);
       	return new IDataSource[] { ds };
       
    }
}