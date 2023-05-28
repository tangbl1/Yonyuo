package nc.vo.vehicle;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggVehicleMessageVOMeta extends AbstractBillMeta{
	
	public AggVehicleMessageVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.vehicle.VehicleMessageVO.class);
	}
}