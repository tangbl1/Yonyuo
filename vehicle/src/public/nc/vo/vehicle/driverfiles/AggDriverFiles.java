package nc.vo.vehicle.driverfiles;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.vehicle.driverfiles.DriverFiles")

public class AggDriverFiles extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggDriverFilesMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public DriverFiles getParentVO(){
	  	return (DriverFiles)this.getParent();
	  }
	  
}