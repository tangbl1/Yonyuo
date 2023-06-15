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
			ExceptionUtils.wrapBusinessException("请选择需要操作的数据！");
		}
		Map<String,Object> userJson = param.getUserJson();
		Object assign=null;//指派信息
		if(userJson!=null&&userJson.get("assign")!=null) {//获取指派信息
			assign = userJson.get("assign");
		}
		 
		return this.exeFlow(actionName,tsMap,assign);
	}
	private Object exeFlow(String actionName,Map<String,String> tsMap,Object assign) throws BusinessException{
		final String ACTION_SAVE="SAVE",ACTION_UNSAVE="UNSAVE";//提交、收回
		IVorderHVOService service = ServiceLocator.find(IVorderHVOService.class);
		Object res = null;
		
		if(tsMap.size()==1){//单个操作
			if(ACTION_SAVE.equals(actionName)){//单个提交
				res = service.commitAggVorderHVO(actionName,tsMap,assign);
			}else if(ACTION_UNSAVE.equals(actionName)){//单个收回
				res = service.uncommitAggVorderHVO(actionName,tsMap);
			}
		}else{//批量操作
			if(ACTION_SAVE.equals(actionName)){//批量提交
				res = service.batchCommitAggVorderHVO(actionName, tsMap);
			}else if(ACTION_UNSAVE.equals(actionName)){//批量收回
				res = service.batchUncommitAggVorderHVO(actionName, tsMap);
			}
		}
		
		return res;
	}
}