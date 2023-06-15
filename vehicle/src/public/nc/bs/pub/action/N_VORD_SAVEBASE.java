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
import nc.vo.vehicle.vorder.AggVorderHVO;
import nc.itf.vehicle.vorder.vorderhvo.IVorderHVOService;

public class N_VORD_SAVEBASE extends AbstractPfScriptAction<AggVorderHVO>{
	
	public N_VORD_SAVEBASE(){
		super();
	}
	
	@Override
	protected Object process(PfParameterVO paraVO) throws BusinessException {
		try {
			IVorderHVOService service = NCLocator.getInstance().lookup(IVorderHVOService.class);
			AggVorderHVO[] vos = (AggVorderHVO[])this.getVos();
			setVoStatus(vos);
			
			
			vos = service.callbackSAVEBASE(vos);
			
			return vos;
		}catch(Exception ex) {
			if (ex instanceof BusinessException) {
		        throw (BusinessException) ex;
		      }
	        throw new PFBusinessException(ex.getMessage(), ex);
		}
	}
	
	/**
	* 设置vo的status为NEW，避免未设置vostatus时导致保存不上。
	*/
	private void setVoStatus(AggVorderHVO[] vos){
		if(vos==null||vos.length==0){
			return ;
		}
		Arrays.stream(vos).forEach(aggvo->{
			aggvo.getParentVO().setStatus(VOStatus.NEW);
			if(aggvo instanceof AbstractBill) {
				IBillMeta billMeta = aggvo.getMetaData();
				if(billMeta==null||billMeta.getChildren()==null) {
					return ;
				}
				Arrays.stream(billMeta.getChildren()).filter(meta->aggvo.getChildren(meta)!=null).forEach(meta->{
					Arrays.stream(aggvo.getChildren(meta)).filter(child->child!=null).forEach(child-> child.setStatus(VOStatus.NEW));
				});
			}else {
				CircularlyAccessibleValueObject[] childrenVO = aggvo.getChildrenVO();
				if(childrenVO==null) {
					return ;
				}
				Arrays.stream(childrenVO).filter(child->child!=null).forEach(child-> child.setStatus(VOStatus.NEW));
			}
		});
	}
}