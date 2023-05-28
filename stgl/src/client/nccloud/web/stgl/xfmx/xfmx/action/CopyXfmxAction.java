package nccloud.web.stgl.xfmx.xfmx.action;
import java.util.HashMap;
import java.util.Map;
import nc.pub.billcode.vo.BillCodeContext;
import nccloud.web.codeplatform.framework.action.base.BaseAction;
import nccloud.web.codeplatform.framework.action.base.RequstParamWapper;
import nccloud.web.codeplatform.framework.action.base.RequestDTO;
import nccloud.web.codeplatform.framework.action.base.VOTransform;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.container.IRequest;
import nc.vo.yz.xfmx.AggXfmx;
import nc.itf.stgl.xfmx.xfmx.IXfmxService;

/**
 * �༭Action
 */
public class CopyXfmxAction extends BaseAction{
	@Override
	public Object doAction(IRequest request, RequstParamWapper paramWapper) throws Throwable{
		//json����ת��
		RequestDTO param = VOTransform.toVO(paramWapper.requestString,RequestDTO.class);
		//��ȡ����
		String pk = param.getPk();
		
		IXfmxService service = ServiceLocator.find(IXfmxService.class);
		
		AggXfmx vo = service.copyAggXfmx(pk);
		
		if(vo == null){
			return null;
		}
		Map<String,Object> externalData = new HashMap<>();
		BillCodeContext context = service.getBillCodeContext("hanwangxfmx");
		externalData.put("billCodeContext",context);
		return buildResult(param,true,externalData,vo);
	}
	
}