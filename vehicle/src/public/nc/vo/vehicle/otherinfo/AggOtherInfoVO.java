package nc.vo.vehicle.otherinfo;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.vehicle.otherinfo.OtherInfoVO")

public class AggOtherInfoVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggOtherInfoVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public OtherInfoVO getParentVO(){
	  	return (OtherInfoVO)this.getParent();
	  }
	  
}