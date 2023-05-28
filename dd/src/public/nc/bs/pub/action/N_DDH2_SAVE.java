package nc.bs.pub.action;

import java.util.Arrays;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.compiler.IWorkFlowRet;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.VOStatus;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nccloud.commons.lang.StringUtils;
import nc.codeplatform.framework.service.AbstractPfScriptAction;
import nc.vo.dd.refreshcost.AggRefreshCostHVO;
import nc.itf.dd.refreshcost.refreshcosthvo.IRefreshCostHVOService;

public class N_DDH2_SAVE extends AbstractPfScriptAction<AggRefreshCostHVO>{
	
	public N_DDH2_SAVE(){
		super();
	}
	
	@Override
	protected Object process(PfParameterVO paraVO) throws BusinessException {
		try {
			IRefreshCostHVOService service = NCLocator.getInstance().lookup(IRefreshCostHVOService.class);
			AggRefreshCostHVO[] vos = (AggRefreshCostHVO[])this.getVos();
			Arrays.stream(vos).forEach(v-> v.getParent().setStatus(VOStatus.UPDATED));
			
			
			vos = service.callbackSAVE(vos);
			
			return vos;
		}catch(Exception ex) {
			if (ex instanceof BusinessException) {
		        throw (BusinessException) ex;
		      }
	        throw new PFBusinessException(ex.getMessage(), ex);
		}
	}
	
	protected void before(PfParameterVO paraVO)  throws BusinessException{
		AggRefreshCostHVO vo = (AggRefreshCostHVO)this.getVo();
		if(StringUtils.isEmpty(vo.getPrimaryKey())){
			throw new BusinessException("当前单据未执行保存动作脚本，如果配置了业务流程定义，请检查动作驱动配置！");
		}
	}
}