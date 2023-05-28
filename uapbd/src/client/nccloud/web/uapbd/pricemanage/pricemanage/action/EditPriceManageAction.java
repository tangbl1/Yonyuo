package nccloud.web.uapbd.pricemanage.pricemanage.action;
import java.util.HashMap;
import java.util.Map;
import nc.pub.billcode.vo.BillCodeContext;
import nccloud.web.codeplatform.framework.action.base.BaseAction;
import nccloud.web.codeplatform.framework.action.base.RequstParamWapper;
import nccloud.web.codeplatform.framework.action.base.RequestDTO;
import nccloud.web.codeplatform.framework.action.base.VOTransform;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.container.IRequest;
import nc.vo.uapbd.AggPriceManage;
import nc.itf.uapbd.pricemanage.pricemanage.IPriceManageService;

/**
 * 编辑Action
 */
public class EditPriceManageAction extends BaseAction{
	@Override
	public Object doAction(IRequest request, RequstParamWapper paramWapper) throws Throwable{
		//json数据转换
		RequestDTO param = VOTransform.toVO(paramWapper.requestString,RequestDTO.class);
		//获取参数
		String pk = param.getPk();
		
		IPriceManageService service = ServiceLocator.find(IPriceManageService.class);
		
		AggPriceManage vo = service.preEditAggPriceManage(pk);
		
		if(vo == null){
			return null;
		}
		Map<String,Object> externalData = new HashMap<>();
		BillCodeContext context = service.getBillCodeContext("uapbdPriceManage");
		externalData.put("billCodeContext",context);
		return buildResult(param,true,externalData,vo);
	}
	
}