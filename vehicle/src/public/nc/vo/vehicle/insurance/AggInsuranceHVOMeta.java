package nc.vo.vehicle.insurance;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggInsuranceHVOMeta extends AbstractBillMeta{
	
	public AggInsuranceHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.vehicle.insurance.InsuranceHVO.class);
		this.addChildren(nc.vo.vehicle.insurance.InsuranceBVO.class);
	}
}