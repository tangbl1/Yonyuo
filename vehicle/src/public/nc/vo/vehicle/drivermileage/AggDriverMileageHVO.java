package nc.vo.vehicle.drivermileage;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.vehicle.drivermileage.DriverMileageHVO")

public class AggDriverMileageHVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggDriverMileageHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public DriverMileageHVO getParentVO(){
	  	return (DriverMileageHVO)this.getParent();
	  }
	  
}