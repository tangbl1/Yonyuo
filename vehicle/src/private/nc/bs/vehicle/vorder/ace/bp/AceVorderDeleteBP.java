package nc.bs.vehicle.vorder.ace.bp;

import nc.bs.vehicle.vorder.plugin.bpplugin.VorderPluginPoint;
import nc.vo.vehicle.vorder.AggVorderHVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceVorderDeleteBP {

	public void delete(AggVorderHVO[] bills) {

		DeleteBPTemplate<AggVorderHVO> bp = new DeleteBPTemplate<AggVorderHVO>(
				VorderPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggVorderHVO> processer) {
		// TODO ǰ����
		IRule<AggVorderHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggVorderHVO> processer) {
		// TODO �����

	}
}
