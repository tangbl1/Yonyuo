package nc.vo.vehicle.vorder;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.vehicle.vorder.VorderHVO;

/**
 * 
 * 单子表/单表头/单表体聚合VO
 *
 * 创建日期:
 * @author 
 * @version NCPrj ??
 */
@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.vehicle.vorder.VorderHVO")
public class  AggVorderHVO  extends AbstractBill {
	
	private static final long serialVersionUID = 1309613813878961316L;

      @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggVorderHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public VorderHVO getParentVO(){
	  	return (VorderHVO)this.getParent();
	  }

      @Override
        public String getPrimaryKey() {
            return super.getPrimaryKey();
      }

}
