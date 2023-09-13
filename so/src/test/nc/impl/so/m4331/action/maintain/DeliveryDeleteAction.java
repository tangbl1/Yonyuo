//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package nc.impl.so.m4331.action.maintain;

import nc.bs.ml.NCLangResOnserver;
import nc.bs.so.m4331.maintain.DeleteDeliveryBP;
import nc.bs.so.m4331.plugin.Action4331PlugInPoint;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.pubapp.pattern.log.TimeLog;
import nc.vo.so.m4331.entity.DeliveryVO;

public class DeliveryDeleteAction {
    public DeliveryDeleteAction() {
    }

    public void delete(DeliveryVO[] bills) {
        AroundProcesser<DeliveryVO> processer = new AroundProcesser(Action4331PlugInPoint.DeleteAction);
        TimeLog.logStart();
        processer.before(bills);
        TimeLog.info(NCLangResOnserver.getInstance().getStrByID("4006002_0", "04006002-0140"));
        TimeLog.logStart();
        DeleteDeliveryBP action = new DeleteDeliveryBP();
        action.delete(bills);
        TimeLog.info(NCLangResOnserver.getInstance().getStrByID("4006002_0", "04006002-0141"));
        TimeLog.logStart();
        processer.after(bills);
        TimeLog.info(NCLangResOnserver.getInstance().getStrByID("4006002_0", "04006002-0142"));
    }
}
