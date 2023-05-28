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
 * ����Action
 */
public class AddVorderHVOAction extends BaseAction{

	@Override
	public Object doAction(IRequest request, RequstParamWapper paramWapper) throws Throwable{
		//json����ת��
		RequestDTO param = VOTransform.toVO(paramWapper.requestString,RequestDTO.class);
		//��ȡ����
		Map<String,Object> userJson = param.getUserJson();

		AggVorderHVO[] vos = this.getVOs(param, AggVorderHVO.class);
		
		//���ʵ��VO
		IVorderHVOService service = ServiceLocator.find(IVorderHVOService.class);
		AggVorderHVO vo = service.preAddAggVorderHVO(vos[0],userJson);
		Map<String,Object> externalData = new HashMap<>();
		BillCodeContext context = service.getBillCodeContext("vehiclevorder");
		externalData.put("billCodeContext",context);
		return buildResult(param,true,externalData,vo);
		
	}

}