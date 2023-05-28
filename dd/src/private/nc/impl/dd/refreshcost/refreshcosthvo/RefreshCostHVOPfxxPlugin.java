
package nc.impl.dd.refreshcost.refreshcosthvo;

import nc.bs.pfxx.plugin.AbstractPfxxPlugin;
import nc.vo.pub.BusinessException;
import nc.bs.pfxx.ISwapContext;
import nc.vo.pfxx.auxiliary.AggxsysregisterVO;
import nc.vo.pfxx.util.PfxxPluginUtils;
import nc.bs.framework.common.NCLocator;
import nc.vo.dd.refreshcost.AggRefreshCostHVO;
import nc.itf.dd.refreshcost.refreshcosthvo.IRefreshCostHVOService;

public class RefreshCostHVOPfxxPlugin extends AbstractPfxxPlugin {
	
	@Override
	protected Object processBill(Object vo, ISwapContext swapContext, AggxsysregisterVO aggxsysvo)
			throws BusinessException {
		AggRefreshCostHVO entityVO = (AggRefreshCostHVO)vo;
		String vopk = PfxxPluginUtils.queryBillPKBeforeSaveOrUpdate(swapContext.getBilltype(), swapContext.getDocID());
		entityVO.getParentVO().setPrimaryKey(vopk);
		AggRefreshCostHVO[] saveVO = getService().saveAggRefreshCostHVO(entityVO);
		return saveVO;
	}
	
	private IRefreshCostHVOService getService() {
		return NCLocator.getInstance().lookup(IRefreshCostHVOService.class);
	}
}