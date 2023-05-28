package nc.vo.dd.refreshcost;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggRefreshCostHVOMeta  extends AbstractBillMeta{
	
	public AggRefreshCostHVOMeta(){
		this.init();
	}

    private void init() {
        this.setParent(nc.vo.dd.refreshcost.RefreshCostHVO.class);
            this.addChildren(nc.vo.dd.refreshcost.RefreshCostBVO.class);
    }

}
