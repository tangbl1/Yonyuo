
package nccloud.web.dd.refreshcost.refreshcosthvo.action;

import nc.itf.org.IOrgResourceCodeConst;
import nccloud.framework.web.processor.refgrid.RefQueryInfo;
import nccloud.framework.web.ui.meta.RefMeta;
import nccloud.web.refer.DefaultGridRefAction;

/**
 * 
 * 档案特性生成默认参照
 * 
 * @author zhaoxwf
 * 
 */
public class RefreshCostHVOGridRefAction extends DefaultGridRefAction {
	
	public RefreshCostHVOGridRefAction() {
		super();
		setResourceCode(IOrgResourceCodeConst.ORG);
	}

	@Override
	public RefMeta getRefMeta(RefQueryInfo refQueryInfo) {
		RefMeta refMeta = new RefMeta();		
		setResourceCode(IOrgResourceCodeConst.ORG);
		refMeta.setCodeField("code");
		refMeta.setNameField("name");
		refMeta.setPkField("pk_cost");
		refMeta.setTableName("dd_refreshcost");
		refMeta.setMutilLangNameRef(true);
		return refMeta;
	}

}
