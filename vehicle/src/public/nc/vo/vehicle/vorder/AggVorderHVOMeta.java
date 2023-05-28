package nc.vo.vehicle.vorder;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggVorderHVOMeta  extends AbstractBillMeta{
	
	public AggVorderHVOMeta(){
		this.init();
	}

    private void init() {
        this.setParent(nc.vo.vehicle.vorder.VorderHVO.class);
            this.addChildren(nc.vo.vehicle.vorder.VorderBVO.class);
    }

}
