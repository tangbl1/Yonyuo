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
* ��Ӧ���������е�����������ࡣ
* �������ж����������ֹ�������return���ء�
* ���������ʵ������ڵ���ֹ����ʵ���������ջص���ʱ����µ���״̬��
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
        // �������Ļ�ȡ����״̬
        itf.setApproveStatus(cscc.getCheckStatus());
        // ����״̬���ֶ�
        fields[0] = itf.getColumnName(IFlowBizItf.ATTRIBUTE_APPROVESTATUS);
        
        // �����޸ĺ�����
        SuperVO vo = (SuperVO) ((AggregatedValueObject) cscc.getBillVo()).getParentVO();
        if(vo==null){
        	return;
        }
        // ����vo
        getBaseDAO().updateVO(vo, fields);
        // ����parentVO
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
