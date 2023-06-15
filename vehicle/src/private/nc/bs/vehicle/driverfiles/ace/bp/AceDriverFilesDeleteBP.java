package nc.bs.vehicle.driverfiles.ace.bp;

import nc.bs.vehicle.driverfiles.plugin.bpplugin.DriverFilesPluginPoint;
import nc.vo.vehicle.driverfiles.AggDriverFiles;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceDriverFilesDeleteBP {

	public void delete(AggDriverFiles[] bills) {

		DeleteBPTemplate<AggDriverFiles> bp = new DeleteBPTemplate<AggDriverFiles>(
				DriverFilesPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggDriverFiles> processer) {
		// TODO 前规则
		IRule<AggDriverFiles> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggDriverFiles> processer) {
		// TODO 后规则

	}
}
