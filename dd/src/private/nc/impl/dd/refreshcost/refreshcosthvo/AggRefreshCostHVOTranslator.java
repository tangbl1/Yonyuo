package nc.impl.dd.refreshcost.refreshcosthvo;

import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.pfxx.exception.PfxxException;
import nccloud.bs.excel.ITranslateContext;
import nccloud.bs.excel.bdtranslate.AbstractRefTranslator;
 
/**
 * 
 * ���뵼���Զ��巭����
 */
public class AggRefreshCostHVOTranslator extends AbstractRefTranslator {

	@Override
	public String translateExToNC(String srcValue, String metaDataID, ITranslateContext translateContext)
			throws PfxxException {
		return null;
	}

	@Override
	protected void processRefModel(AbstractRefModel refmodel, ITranslateContext translateContext) {
		return;
	}

}
