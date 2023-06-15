package nc.bs.vehicle.insurancefiles.ace.bp;

import nc.bs.vehicle.insurancefiles.plugin.bpplugin.InsuranceFilesPluginPoint;
import nc.vo.vehicle.insurance.AggInsuranceHVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceInsuranceFilesDeleteBP {

	public void delete(AggInsuranceHVO[] bills) {

		DeleteBPTemplate<AggInsuranceHVO> bp = new DeleteBPTemplate<AggInsuranceHVO>(
				InsuranceFilesPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggInsuranceHVO> processer) {
		// TODO 前规则
		IRule<AggInsuranceHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggInsuranceHVO> processer) {
		// TODO 后规则

	}
}
