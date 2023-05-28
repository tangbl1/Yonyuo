package nc.vo.yz.xfmx;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.yz.xfmx.Xfmx;

/**
 * 
 * 单子表/单表头/单表体聚合VO
 *
 * 创建日期:
 * @author 
 * @version NCPrj ??
 */
@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.yz.xfmx.Xfmx")
public class  AggXfmx  extends AbstractBill {
	
	private static final long serialVersionUID = 1309613813878961316L;

      @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggXfmxMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public Xfmx getParentVO(){
	  	return (Xfmx)this.getParent();
	  }

      @Override
        public String getPrimaryKey() {
            return super.getPrimaryKey();
      }

}
