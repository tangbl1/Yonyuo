package nccloud.web.uapbd.pricemanage.pricemanage.action;
import java.util.HashMap;
import java.util.Map;
import nccloud.web.codeplatform.framework.action.base.BaseAction;
import nccloud.web.codeplatform.framework.action.base.RequstParamWapper;
import nccloud.web.codeplatform.framework.action.base.RequestDTO;
import nccloud.web.codeplatform.framework.action.base.VOTransform;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.container.IRequest;
import nc.vo.uapbd.AggPriceManage;
import nc.itf.uapbd.pricemanage.pricemanage.IPriceManageService;


/**
 * ����Action
 */
public class AddPriceManageAction extends BaseAction{

	@Override
	public Object doAction(IRequest request, RequstParamWapper paramWapper) throws Throwable{
		//json����ת��
		RequestDTO param = VOTransform.toVO(paramWapper.requestString,RequestDTO.class);
		//��ȡ����
		Map<String,Object> userJson = param.getUserJson();

		AggPriceManage[] vos = this.getVOs(param, AggPriceManage.class);
		
		//���ʵ��VO
		IPriceManageService service = ServiceLocator.find(IPriceManageService.class);
		AggPriceManage vo = service.preAddAggPriceManage(vos[0],userJson);
		return buildResult(param,true,null,vo);
		
	}

}