package nc.bs.ic.m4d.sign.rule;


import nc.bs.ic.m4d.process.BillI4FinanceProcess;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.ic.m4d.entity.MaterialOutVO;

public class PushSaveI4Bill implements IRule<MaterialOutVO>{

	@Override
	public void process(MaterialOutVO[] vos) {
		// TODO 自动生成的方法存根
			
			BillI4FinanceProcess billi4save = new BillI4FinanceProcess();
			billi4save.saveI4Bill(vos);

		
		
		
	}
	

}
