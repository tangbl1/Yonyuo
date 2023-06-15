package nc.bs.vehicle.vorder;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.pf.CheckStatusCallbackContext;
import nc.bs.pub.pf.ICheckStatusCallback;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.data.access.NCObject;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
* 对应单据类型中的审批流检查类。
* 检查过程判断如果不是终止流程则会return返回。
* 如果从流程实例管理节点终止流程实例，或者收回单据时会更新单据状态。
*/
public class PfVorderHVOCheck implements ICheckStatusCallback {

    private BaseDAO baseDAO = null;

    @Override
    public void callCheckStatus(CheckStatusCallbackContext cscc) throws BusinessException {
        if (!cscc.isTerminate()||cscc.getBillVo()==null) {
        	return ;
        }
        NCObject ncObj = NCObject.newInstance(cscc.getBillVo());
        IFlowBizItf itf = ncObj.getBizInterface(IFlowBizItf.class);
        String[] fields = new String[1];
        // 从上下文获取审批状态
        itf.setApproveStatus(cscc.getCheckStatus());
        // 审批状态的字段
        fields[0] = itf.getColumnName(IFlowBizItf.ATTRIBUTE_APPROVESTATUS);
        
        // 保存修改后数据
        SuperVO vo = (SuperVO) ((AggregatedValueObject) cscc.getBillVo()).getParentVO();
        if(vo==null){
        	return;
        }
        // 更新vo
        getBaseDAO().updateVO(vo, fields);
        // 更新parentVO
        vo = (SuperVO) getBaseDAO().retrieveByPK(vo.getClass(), vo.getPrimaryKey());
        ((AggregatedValueObject) cscc.getBillVo()).setParentVO(vo);
    }

    private BaseDAO getBaseDAO() {
        if (baseDAO == null) {
            baseDAO = new BaseDAO();
        }
        return baseDAO;
    }
}
