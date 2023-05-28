package nc.vo.vehicle.inspectionfiles;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.vehicle.inspectionfiles.InspectionFileHVO")

public class AggInspectionFileHVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggInspectionFileHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public InspectionFileHVO getParentVO(){
	  	return (InspectionFileHVO)this.getParent();
	  }
	  
}