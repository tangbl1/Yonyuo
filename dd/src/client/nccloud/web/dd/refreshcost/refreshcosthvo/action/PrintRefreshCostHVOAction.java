package nccloud.web.dd.refreshcost.refreshcosthvo.action;

import nccloud.web.platform.print.AbstractPrintAction;

public class PrintRefreshCostHVOAction extends AbstractPrintAction{

	@Override
	public String getPrintServiceModule() {
		return "dd";
	}
	
	@Override
	public String getPrintServiceName() {
		return "nc.impl.dd.refreshcost.refreshcosthvo.AggRefreshCostHVOPrintServiceImpl";
	}
}