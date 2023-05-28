package nc.vo.vehicle.drivermileage;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggDriverMileageHVOMeta extends AbstractBillMeta{
	
	public AggDriverMileageHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.vehicle.drivermileage.DriverMileageHVO.class);
	}
}