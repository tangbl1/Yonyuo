package nc.vo.uapbd;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggPriceManageMeta  extends AbstractBillMeta{
	
	public AggPriceManageMeta(){
		this.init();
	}

    private void init() {
        this.setParent(nc.vo.uapbd.PriceManage.class);
            this.addChildren(nc.vo.uapbd.pricemanage.Baseprice.class);
            this.addChildren(nc.vo.uapbd.pricemanage.Addprice.class);
    }

}
