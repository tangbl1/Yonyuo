
package nc.impl.dd.refreshcost.refreshcosthvo;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import nc.bs.framework.common.NCLocator;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nccloud.bs.excel.IXChangeContext;
import nccloud.bs.excel.plugin.AbstractImportProceeWithContext;
import nc.vo.dd.refreshcost.RefreshCostHVO;
import nc.vo.dd.refreshcost.RefreshCostBVO;
import nc.vo.dd.refreshcost.AggRefreshCostHVO;
import nc.itf.dd.refreshcost.refreshcosthvo.IRefreshCostHVOService;

public class AggRefreshCostHVOImportProcess extends AbstractImportProceeWithContext {
	
	@Override
	protected void processBillWithContext(Object vo, IXChangeContext changcontext) throws BusinessException {
		AggRefreshCostHVO targetVO = (AggRefreshCostHVO)vo;
		targetVO.getParentVO().setStatus(VOStatus.NEW);

		RefreshCostBVO[] refreshCostBVOs = (RefreshCostBVO[])targetVO.getChildren(RefreshCostBVO.class);
		if(refreshCostBVOs!=null && refreshCostBVOs.length>0){
			Arrays.stream(refreshCostBVOs).forEach(subvo->{
				subvo.setStatus(VOStatus.NEW);
			});
		}
  		getService().initDefaultData(targetVO.getParentVO());
		getService().saveAggRefreshCostHVO(targetVO);
	}
	
	private IRefreshCostHVOService getService() {
		return NCLocator.getInstance().lookup(IRefreshCostHVOService.class);
	}

}
