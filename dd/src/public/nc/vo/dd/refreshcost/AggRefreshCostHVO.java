package nc.vo.dd.refreshcost;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.dd.refreshcost.RefreshCostHVO;

/**
 * 
 * 单子表/单表头/单表体聚合VO
 *
 * 创建日期:
 * @author 
 * @version NCPrj ??
 */
@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.dd.refreshcost.RefreshCostHVO")
public class  AggRefreshCostHVO  extends AbstractBill {
	
	private static final long serialVersionUID = 1309613813878961316L;

      @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggRefreshCostHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public RefreshCostHVO getParentVO(){
	  	return (RefreshCostHVO)this.getParent();
	  }

      @Override
        public String getPrimaryKey() {
            return super.getPrimaryKey();
      }

}
