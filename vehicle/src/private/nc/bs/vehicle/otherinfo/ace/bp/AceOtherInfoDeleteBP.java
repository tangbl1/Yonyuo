package nc.bs.vehicle.otherinfo.ace.bp;

import nc.bs.vehicle.otherinfo.plugin.bpplugin.OtherInfoPluginPoint;
import nc.vo.vehicle.otherinfo.AggOtherInfoVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceOtherInfoDeleteBP {

	public void delete(AggOtherInfoVO[] bills) {

		DeleteBPTemplate<AggOtherInfoVO> bp = new DeleteBPTemplate<AggOtherInfoVO>(
				OtherInfoPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggOtherInfoVO> processer) {
		// TODO 前规则
		IRule<AggOtherInfoVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggOtherInfoVO> processer) {
		// TODO 后规则

	}
}
