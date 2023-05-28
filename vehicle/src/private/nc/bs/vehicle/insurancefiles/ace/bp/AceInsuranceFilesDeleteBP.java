package nc.bs.vehicle.insurancefiles.ace.bp;

import nc.bs.vehicle.insurancefiles.plugin.bpplugin.InsuranceFilesPluginPoint;
import nc.vo.vehicle.insurance.AggInsuranceHVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceInsuranceFilesDeleteBP {

	public void delete(AggInsuranceHVO[] bills) {

		DeleteBPTemplate<AggInsuranceHVO> bp = new DeleteBPTemplate<AggInsuranceHVO>(
				InsuranceFilesPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggInsuranceHVO> processer) {
		// TODO ǰ����
		IRule<AggInsuranceHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggInsuranceHVO> processer) {
		// TODO �����

	}
}
