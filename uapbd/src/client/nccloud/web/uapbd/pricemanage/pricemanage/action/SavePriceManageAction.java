package nccloud.web.uapbd.pricemanage.pricemanage.action;

import java.util.HashMap;
//import java.util.List;
import java.util.Map;
//import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.web.codeplatform.framework.action.base.BaseAction;
import nccloud.web.codeplatform.framework.action.base.RequstParamWapper;
import nccloud.web.codeplatform.framework.action.base.RequestDTO;
import nccloud.web.codeplatform.framework.action.base.VOTransform;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.core.util.ArrayUtils;
import nccloud.framework.web.container.IRequest;
import nc.itf.uapbd.pricemanage.pricemanage.IPriceManageService;
import nc.vo.uapbd.AggPriceManage;

/**
 * 保存action（主子、单表、单form）
 */
public class SavePriceManageAction extends BaseAction{

	@Override
	public Object doAction(IRequest request, RequstParamWapper paramWapper) throws Throwable{
		RequestDTO param = VOTransform.toVO(paramWapper.requestString,RequestDTO.class);
		
		IPriceManageService service = ServiceLocator.find(IPriceManageService.class);

		AggPriceManage[] vos = this.getVOs(param, AggPriceManage.class);
		
		vos = service.saveAggPriceManage(vos[0]);
		Object result = buildResult(param,param.getBillCard().getHead()!=null,null,vos);
		return result;
	
	}
	
}