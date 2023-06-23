package nccloud.web.uapbd.pricemanage.pricemanage.action;
import nccloud.commons.lang.StringUtils;
import nccloud.web.codeplatform.framework.action.base.BaseAction;
import nccloud.web.codeplatform.framework.action.base.RequstParamWapper;
import nccloud.web.codeplatform.framework.action.base.VOTransform;
import nccloud.web.codeplatform.framework.action.base.RequestDTO;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.core.exception.BusinessException;
import nc.vo.uapbd.AggPriceManage;
import nc.itf.uapbd.pricemanage.pricemanage.IPriceManageService;
/**
 * 卡片查询
 */
public class LoadPriceManageAction extends BaseAction{

	@Override
	public Object doAction(IRequest request, RequstParamWapper paramWapper) throws Throwable{
		RequestDTO param = VOTransform.toVO(paramWapper.requestString,RequestDTO.class);
		
		String pk = param.getPk();
		if(StringUtils.isEmpty(pk)){
			throw new BusinessException("参数中缺少pk参数，请检查代码！");
		}
		IPriceManageService service = ServiceLocator.find(IPriceManageService.class);
		
		AggPriceManage vo = service.findAggPriceManageByPk(pk);
		if(vo == null){
			return null;
		}
		
		return buildResult(param,true,null,vo);
	}
	
}