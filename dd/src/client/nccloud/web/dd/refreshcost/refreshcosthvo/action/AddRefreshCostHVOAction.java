package nccloud.web.dd.refreshcost.refreshcosthvo.action;
import java.util.HashMap;
import java.util.Map;
import nc.pub.billcode.vo.BillCodeContext;
import nccloud.web.codeplatform.framework.action.base.BaseAction;
import nccloud.web.codeplatform.framework.action.base.RequstParamWapper;
import nccloud.web.codeplatform.framework.action.base.RequestDTO;
import nccloud.web.codeplatform.framework.action.base.VOTransform;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.container.IRequest;
import nc.vo.dd.refreshcost.AggRefreshCostHVO;
import nc.itf.dd.refreshcost.refreshcosthvo.IRefreshCostHVOService;


/**
 * ����Action
 */
public class AddRefreshCostHVOAction extends BaseAction{

	@Override
	public Object doAction(IRequest request, RequstParamWapper paramWapper) throws Throwable{
		//json����ת��
		RequestDTO param = VOTransform.toVO(paramWapper.requestString,RequestDTO.class);
		//��ȡ����
		Map<String,Object> userJson = param.getUserJson();

		AggRefreshCostHVO[] vos = this.getVOs(param, AggRefreshCostHVO.class);
		
		//���ʵ��VO
		IRefreshCostHVOService service = ServiceLocator.find(IRefreshCostHVOService.class);
		AggRefreshCostHVO vo = service.preAddAggRefreshCostHVO(vos[0],userJson);
		Map<String,Object> externalData = new HashMap<>();
		BillCodeContext context = service.getBillCodeContext("ddrefreshcost");
		externalData.put("billCodeContext",context);
		return buildResult(param,true,externalData,vo);
		
	}

}