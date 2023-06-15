package nc.bs.vehicle.drivermileage.ace.bp;

import nc.bs.vehicle.drivermileage.plugin.bpplugin.DriverMileagePluginPoint;
import nc.vo.vehicle.drivermileage.AggDriverMileageHVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceDriverMileageDeleteBP {

	public void delete(AggDriverMileageHVO[] bills) {

		DeleteBPTemplate<AggDriverMileageHVO> bp = new DeleteBPTemplate<AggDriverMileageHVO>(
				DriverMileagePluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggDriverMileageHVO> processer) {
		// TODO 前规则
		IRule<AggDriverMileageHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggDriverMileageHVO> processer) {
		// TODO 后规则

	}
}
