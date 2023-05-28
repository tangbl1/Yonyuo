package nccloud.web.vehicle.vorder.vorderhvo.action;
import java.util.HashMap;
import java.util.Map;
import nc.pub.billcode.vo.BillCodeContext;
import nccloud.web.codeplatform.framework.action.base.BaseAction;
import nccloud.web.codeplatform.framework.action.base.RequstParamWapper;
import nccloud.web.codeplatform.framework.action.base.RequestDTO;
import nccloud.web.codeplatform.framework.action.base.VOTransform;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.container.IRequest;
import nc.vo.vehicle.vorder.AggVorderHVO;
import nc.itf.vehicle.vorder.vorderhvo.IVorderHVOService;

/**
 * 编辑Action
 */
public class CopyVorderHVOAction extends BaseAction{
	@Override
	public Object doAction(IRequest request, RequstParamWapper paramWapper) throws Throwable{
		//json数据转换
		RequestDTO param = VOTransform.toVO(paramWapper.requestString,RequestDTO.class);
		//获取参数
		String pk = param.getPk();
		
		IVorderHVOService service = ServiceLocator.find(IVorderHVOService.class);
		
		AggVorderHVO vo = service.copyAggVorderHVO(pk);
		
		if(vo == null){
			return null;
		}
		Map<String,Object> externalData = new HashMap<>();
		BillCodeContext context = service.getBillCodeContext("vehiclevorder");
		externalData.put("billCodeContext",context);
		return buildResult(param,true,externalData,vo);
	}
	
}