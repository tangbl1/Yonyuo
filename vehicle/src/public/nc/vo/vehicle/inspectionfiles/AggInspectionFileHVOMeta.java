package nc.vo.vehicle.inspectionfiles;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggInspectionFileHVOMeta extends AbstractBillMeta{
	
	public AggInspectionFileHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.vehicle.inspectionfiles.InspectionFileHVO.class);
		this.addChildren(nc.vo.vehicle.inspectionfiles.InspectionFileBVO.class);
	}
}