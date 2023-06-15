package nc.bs.vehicle.vorder.ace.bp;

import nc.bs.vehicle.vorder.plugin.bpplugin.VorderPluginPoint;
import nc.vo.vehicle.vorder.AggVorderHVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceVorderDeleteBP {

	public void delete(AggVorderHVO[] bills) {

		DeleteBPTemplate<AggVorderHVO> bp = new DeleteBPTemplate<AggVorderHVO>(
				VorderPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggVorderHVO> processer) {
		// TODO 前规则
		IRule<AggVorderHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggVorderHVO> processer) {
		// TODO 后规则

	}
}
