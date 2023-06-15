package nccloud.web.vehicle.vorder.vorderhvo.action;

import nccloud.web.platform.print.AbstractPrintAction;

public class PrintVorderHVOAction extends AbstractPrintAction{

	@Override
	public String getPrintServiceModule() {
		return "vehicle";
	}
	
	@Override
	public String getPrintServiceName() {
		return "nc.impl.vehicle.vorder.vorderhvo.AggVorderHVOPrintServiceImpl";
	}
}