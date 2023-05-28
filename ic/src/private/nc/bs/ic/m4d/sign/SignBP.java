//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package nc.bs.ic.m4d.sign;

import com.yonyou.cloud.ncc.plugin.entity.OperationInfo;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import nc.bs.ic.general.insert.rule.before.CheckBiliValue;
import nc.bs.ic.general.sign.ISignBP;
import nc.bs.ic.general.sign.ISignRuleProvider;
import nc.bs.ic.general.sign.SignBPTemplate;
import nc.bs.ic.m4d.base.BPPlugInPoint;
import nc.bs.ic.m4d.base.UpdateSCOnhandRule;
import nc.bs.ic.m4d.sign.rule.AfterSignRuleForLiabilityProcess;
import nc.bs.ic.m4d.sign.rule.PushSaveI4Bill;
import nc.bs.ic.m4d.sign.rule.PushSaveIAandTOBill;
import nc.bs.ic.pub.util.SagasUtils;
import nc.bs.scmpub.rule.VOSagaFrozenValidateRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.itf.ic.m4d.compensate.IMaterialOutSagasCompensate;
import nc.vo.ic.m4d.entity.MaterialOutVO;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.scmpub.res.billtype.ICBillType;

public class SignBP implements ISignBP<MaterialOutVO>, ISignRuleProvider<MaterialOutVO> {
    public SignBP() {
    }

    public void addAfterRule(MaterialOutVO[] vos, AroundProcesser<MaterialOutVO> processor) {
        processor.addAfterRule(new UpdateSCOnhandRule(false));
        processor.addAfterRule(new PushSaveIAandTOBill());
        processor.addAfterRule(new AfterSignRuleForLiabilityProcess());
        processor.addAfterRule(new PushSaveI4Bill());
    }

    public void addBeforeRule(MaterialOutVO[] vos, AroundProcesser<MaterialOutVO> processor) {
        processor.addBeforeRule(new VOSagaFrozenValidateRule(true));
        processor.addBeforeRule(new CheckBiliValue());
    }

    public MaterialOutVO[] sign(MaterialOutVO[] vos) {
        SignBPTemplate<MaterialOutVO> signBP = new SignBPTemplate(BPPlugInPoint.SignBP, this);
        SagasUtils.frozenAndAddSaga(vos, ICBillType.MaterialOut.getCode(), "1", (OperationInfo)null);
        Map<String, Serializable> paramMap = new HashMap();
        paramMap.put("actionname", "sign_4D");
        paramMap.put("hid", VOEntityUtil.getPksFromAggVO(vos));
        SagasUtils.compensate(paramMap, IMaterialOutSagasCompensate.class);
        return (MaterialOutVO[])signBP.sign(vos);
    }
}
