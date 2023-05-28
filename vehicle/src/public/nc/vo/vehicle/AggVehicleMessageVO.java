package nc.vo.vehicle;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.vehicle.VehicleMessageVO")

public class AggVehicleMessageVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggVehicleMessageVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public VehicleMessageVO getParentVO(){
	  	return (VehicleMessageVO)this.getParent();
	  }
	  
}