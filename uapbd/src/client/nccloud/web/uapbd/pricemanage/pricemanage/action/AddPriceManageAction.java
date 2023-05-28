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
 * 新增Action
 */
public class AddPriceManageAction extends BaseAction{

	@Override
	public Object doAction(IRequest request, RequstParamWapper paramWapper) throws Throwable{
		//json数据转换
		RequestDTO param = VOTransform.toVO(paramWapper.requestString,RequestDTO.class);
		//获取参数
		Map<String,Object> userJson = param.getUserJson();

		AggPriceManage[] vos = this.getVOs(param, AggPriceManage.class);
		
		//获得实体VO
		IPriceManageService service = ServiceLocator.find(IPriceManageService.class);
		AggPriceManage vo = service.preAddAggPriceManage(vos[0],userJson);
		Map<String,Object> externalData = new HashMap<>();
		BillCodeContext context = service.getBillCodeContext("uapbdPriceManage");
		externalData.put("billCodeContext",context);
		return buildResult(param,true,externalData,vo);
		
	}

}