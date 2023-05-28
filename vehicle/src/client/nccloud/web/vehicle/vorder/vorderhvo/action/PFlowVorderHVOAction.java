package nccloud.web.vehicle.vorder.vorderhvo.action;

import java.util.Map;
import nccloud.web.codeplatform.framework.action.base.BaseAction;
import nccloud.web.codeplatform.framework.action.base.RequstParamWapper;
import nccloud.web.codeplatform.framework.action.base.VOTransform;
import nccloud.web.codeplatform.framework.action.base.RequestDTO;
import nc.vo.pub.BusinessException;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.core.exception.ExceptionUtils;
import nc.vo.vehicle.vorder.AggVorderHVO;
import nc.itf.vehicle.vorder.vorderhvo.IVorderHVOService;

public class PFlowVorderHVOAction extends BaseAction{

	@Override
	public Object doAction(IRequest request, RequstParamWapper paramWapper) throws Throwable{
		RequestDTO param = VOTransform.toVO(paramWapper.requestString,RequestDTO.class);
		Map<String,String> tsMap = param.getTsMap();
		String actionName = param.getActionName();
		if(tsMap==null || tsMap.size()==0){
			ExceptionUtils.wrapBusinessException("��ѡ����Ҫ���������ݣ�");
		}
		Map<String,Object> userJson = param.getUserJson();
		Object assign=null;//ָ����Ϣ
		if(userJson!=null&&userJson.get("assign")!=null) {//��ȡָ����Ϣ
			assign = userJson.get("assign");
		}
		 
		return this.exeFlow(actionName,tsMap,assign);
	}
	private Object exeFlow(String actionName,Map<String,String> tsMap,Object assign) throws BusinessException{
		final String ACTION_SAVE="SAVE",ACTION_UNSAVE="UNSAVE";//�ύ���ջ�
		IVorderHVOService service = ServiceLocator.find(IVorderHVOService.class);
		Object res = null;
		
		if(tsMap.size()==1){//��������
			if(ACTION_SAVE.equals(actionName)){//�����ύ
				res = service.commitAggVorderHVO(actionName,tsMap,assign);
			}else if(ACTION_UNSAVE.equals(actionName)){//�����ջ�
				res = service.uncommitAggVorderHVO(actionName,tsMap);
			}
		}else{//��������
			if(ACTION_SAVE.equals(actionName)){//�����ύ
				res = service.batchCommitAggVorderHVO(actionName, tsMap);
			}else if(ACTION_UNSAVE.equals(actionName)){//�����ջ�
				res = service.batchUncommitAggVorderHVO(actionName, tsMap);
			}
		}
		
		return res;
	}
}