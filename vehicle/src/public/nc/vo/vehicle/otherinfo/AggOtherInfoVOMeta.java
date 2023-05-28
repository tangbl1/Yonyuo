package nc.vo.vehicle.otherinfo;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggOtherInfoVOMeta extends AbstractBillMeta{
	
	public AggOtherInfoVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.vehicle.otherinfo.OtherInfoVO.class);
	}
}