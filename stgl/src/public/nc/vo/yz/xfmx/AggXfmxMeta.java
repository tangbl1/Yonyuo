package nc.vo.yz.xfmx;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggXfmxMeta  extends AbstractBillMeta{
	
	public AggXfmxMeta(){
		this.init();
	}

    private void init() {
        this.setParent(nc.vo.yz.xfmx.Xfmx.class);
    }

}
