package nccloud.web.stgl.xfmx.xfmx.action;

import nccloud.web.platform.print.AbstractPrintAction;

public class PrintXfmxAction extends AbstractPrintAction{

	@Override
	public String getPrintServiceModule() {
		return "stgl";
	}
	
	@Override
	public String getPrintServiceName() {
		return "nc.impl.stgl.xfmx.xfmx.AggXfmxPrintServiceImpl";
	}
}