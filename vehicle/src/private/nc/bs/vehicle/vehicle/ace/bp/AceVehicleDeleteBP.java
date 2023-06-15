package nc.bs.vehicle.vehicle.ace.bp;

import nc.bs.vehicle.vehicle.plugin.bpplugin.VehiclePluginPoint;
import nc.vo.vehicle.AggVehicleMessageVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceVehicleDeleteBP {

	public void delete(AggVehicleMessageVO[] bills) {

		DeleteBPTemplate<AggVehicleMessageVO> bp = new DeleteBPTemplate<AggVehicleMessageVO>(
				VehiclePluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggVehicleMessageVO> processer) {
		// TODO 前规则
		IRule<AggVehicleMessageVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggVehicleMessageVO> processer) {
		// TODO 后规则

	}
}
