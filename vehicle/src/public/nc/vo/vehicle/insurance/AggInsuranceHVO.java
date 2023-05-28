package nc.vo.vehicle.insurance;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.vehicle.insurance.InsuranceHVO")

public class AggInsuranceHVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggInsuranceHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public InsuranceHVO getParentVO(){
	  	return (InsuranceHVO)this.getParent();
	  }
	  
}