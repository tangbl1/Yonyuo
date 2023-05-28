package nc.bs.vehicle.vehicle.ace.bp;

import nc.bs.vehicle.vehicle.plugin.bpplugin.VehiclePluginPoint;
import nc.vo.vehicle.AggVehicleMessageVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceVehicleDeleteBP {

	public void delete(AggVehicleMessageVO[] bills) {

		DeleteBPTemplate<AggVehicleMessageVO> bp = new DeleteBPTemplate<AggVehicleMessageVO>(
				VehiclePluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggVehicleMessageVO> processer) {
		// TODO ǰ����
		IRule<AggVehicleMessageVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggVehicleMessageVO> processer) {
		// TODO �����

	}
}
