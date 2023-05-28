package nc.vo.vehicle.driverfiles;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggDriverFilesMeta extends AbstractBillMeta{
	
	public AggDriverFilesMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.vehicle.driverfiles.DriverFiles.class);
	}
}