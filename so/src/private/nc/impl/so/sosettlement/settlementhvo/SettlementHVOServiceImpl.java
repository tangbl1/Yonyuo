package nc.impl.so.sosettlement.settlementhvo;

import java.util.Map;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.lang.String;
import java.util.stream.Stream;

import nc.itf.uap.pf.IPfExchangeService;
import nc.itf.uap.pf.IplatFormEntry;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.so.m32.entity.SaleInvoiceVO;
import nccloud.commons.lang.ArrayUtils;
import nc.vo.pubapp.AppContext;

import nc.jdbc.framework.processor.ColumnProcessor;
import nc.itf.so.sosettlement.settlementhvo.ISettlementHVOService;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.itf.uap.IUAPQueryBS;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.md.persist.framework.MDPersistenceService;

import nc.bs.framework.common.NCLocator;

import nc.itf.uap.pf.IPFBusiAction;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.sosettlement.AggSettlementHVO;
import nc.vo.so.sosettlement.SettlementBVO;
import nc.vo.so.sosettlement.SettlementHVO;
import org.apache.commons.lang3.StringUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.codeplatform.framework.service.ServiceSupport;
import nc.pub.billcode.vo.BillCodeContext;
import nc.vo.pub.pf.BillStatusEnum;
import nc.bs.framework.common.InvocationInfoProxy;
public class  SettlementHVOServiceImpl extends ServiceSupport implements ISettlementHVOService {


    @Override
    public AggSettlementHVO[] listAggSettlementHVOByPk(String...pks) throws BusinessException{
        return listAggSettlementHVOByPk(false,pks);
    }

    @Override
    public AggSettlementHVO[] listAggSettlementHVOByPk(boolean blazyLoad,String... pks) throws BusinessException{
        return dao.listByPksWithOrder(AggSettlementHVO.class,pks,blazyLoad);
    }

    @Override
    public AggSettlementHVO findAggSettlementHVOByPk(String pk) throws BusinessException{
        return dao.findByPk(AggSettlementHVO.class, pk, false);
    }

    @Override
    public  AggSettlementHVO[] listAggSettlementHVOByCondition(String condition) throws BusinessException{
        return listAggSettlementHVOByCondition(condition,new String[]{"pk_settlement"});
    }
    @Override
    public  AggSettlementHVO[] listAggSettlementHVOByCondition(String condition,String[] orderPath) throws BusinessException{
        return dao.listByCondition(AggSettlementHVO.class, condition, false,false,orderPath);
    }
    @Override
    public SettlementHVO[] listSettlementHVOByPk(String... pks) throws BusinessException{
        return dao.listByPk(SettlementHVO.class, pks, true);
    }

    @Override
    public  SettlementHVO findSettlementHVOByPk(String pk) throws BusinessException{
        return dao.findByPk(SettlementHVO.class, pk, true);
    }

    @Override
    public  SettlementHVO[] listSettlementHVOByCondition(String condition) throws BusinessException{
        return listSettlementHVOByCondition(condition,new String[]{"pk_settlement"});
    }
    @Override
    public  SettlementHVO[] listSettlementHVOByCondition(String condition,String[] orderPath) throws BusinessException{
        return dao.listByCondition(SettlementHVO.class, condition, false,false,orderPath);
    }

    @Override
    public String[] listSettlementHVOPkByCond(String condition) throws BusinessException{
        return listSettlementHVOPkByCond(condition,new String[]{"pk_settlement"});
    }
    @Override
    public String[] listSettlementHVOPkByCond(String condition,String[] orderPath) throws BusinessException{
        if(StringUtils.isEmpty(condition)) {
            condition = " 1 = 1 ";
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select pk_settlement from ").append(SettlementHVO.getDefaultTableName());
        sql.append(" where ").append(condition);
        if (ArrayUtils.isNotEmpty(orderPath)) {
            sql.append(" order by ").append(StringUtils.join(orderPath, ", "));
        }
        return (String[]) dao.getBaseDAO().executeQuery(sql.toString(), (rs) -> {
            List<String> pks = new ArrayList<>();
            while (rs.next()) {
                pks.add(rs.getString(1));
            }
            return pks.toArray(new String[0]);
        });
    }
    @Override
    public void initDefaultData(SettlementHVO vo){
        if(vo.getAttributeValue("pk_group") == null){
            vo.setAttributeValue("pk_group",InvocationInfoProxy.getInstance().getGroupId());
        }
        if(vo.getAttributeValue("billmaker") == null){
            vo.setAttributeValue("billmaker",InvocationInfoProxy.getInstance().getUserId());
        }
        if(vo.getAttributeValue("maketime") == null){
            vo.setAttributeValue("maketime",new UFDateTime(InvocationInfoProxy.getInstance().getBizDateTime()));
        }
        if(vo.getAttributeValue("billtype") == null){
            vo.setAttributeValue("billtype","KHJS");
        }
        if(vo.getAttributeValue("approvestatus") == null){
            vo.setAttributeValue("approvestatus",BillStatusEnum.FREE.toIntValue());
        }
    }
    @Override
    public AggSettlementHVO preAddAggSettlementHVO(AggSettlementHVO vo,Map<String,Object> userJson) throws BusinessException{

        getMainVO(vo).setStatus(VOStatus.NEW);
        initDefaultData((SettlementHVO)getMainVO(vo));

        //�������Ҫ�ж��Ƿ������������
        Map<String,String> data = userJson!=null && userJson.get("data") != null?(Map<String,String>)userJson.get("data"):null;
        if(data!=null && data.size()>0){
            String parentKey = data.get("parentKey");
            String parentPk = data.get("parentPk");
            getMainVO(vo).setAttributeValue(parentKey,parentPk);
        }

        //�����������vo�ı���
        BillCodeContext billCodeContext = getBillCodeContext("sosoSettlement");
        if(billCodeContext == null){
            throw new BusinessException("��ǰ������򲻴��ڣ��뵽�����������-ȫ�֡��ڵ����Ƿ����"+"sosoSettlement");
        }
        if(billCodeContext.isPrecode()){
            String pk_group = InvocationInfoProxy.getInstance().getGroupId();
            String code = getBillcodeManage().getPreBillCode_RequiresNew("sosoSettlement", pk_group, pk_group);
            getMainVO(vo).setAttributeValue("billno",code);
        }

        return vo;
    }
    @Override
    public AggSettlementHVO preAddAggSettlementHVO(Map<String,Object> userJson) throws BusinessException{
        AggSettlementHVO result = null;

        SettlementHVO mainvo = new SettlementHVO();
        //����Ĭ��ֵ
        initDefaultData(mainvo);
        AggSettlementHVO aggvo = new AggSettlementHVO();
        aggvo.setParent(mainvo);
        result = aggvo;
        return preAddAggSettlementHVO(result,userJson);
    }

    @Override
    public AggSettlementHVO preEditAggSettlementHVO(String pk) throws BusinessException{
        return dao.findByPk(AggSettlementHVO.class, pk, false);
    }

    @Override
    public AggSettlementHVO copyAggSettlementHVO(String pk) throws BusinessException{

        AggSettlementHVO vo = dao.findByPk(AggSettlementHVO.class, pk, false);

        getMainVO(vo).setPrimaryKey(null);
        getMainVO(vo).setStatus(VOStatus.NEW);

        getMainVO(vo).setAttributeValue("srcbilltype",null);
        getMainVO(vo).setAttributeValue("srcbillid",null);

        getMainVO(vo).setAttributeValue("billno",null);
        getMainVO(vo).setAttributeValue("name",null);
        //�����������vo�ı���
        BillCodeContext billCodeContext = getBillCodeContext("sosoSettlement");
        if(billCodeContext == null){
            throw new BusinessException("��ǰ������򲻴��ڣ��뵽�����������-ȫ�֡��ڵ����Ƿ����"+"sosoSettlement");
        }
        if(billCodeContext.isPrecode()){
            String pk_group = InvocationInfoProxy.getInstance().getGroupId();
            String code = getBillcodeManage().getPreBillCode_RequiresNew("sosoSettlement", pk_group, pk_group);
            getMainVO(vo).setAttributeValue("billno",code);
        }
        getMainVO(vo).setAttributeValue("approvestatus", BillStatusEnum.FREE.toIntValue());
        getMainVO(vo).setAttributeValue("billmaker", InvocationInfoProxy.getInstance().getUserId());
        getMainVO(vo).setAttributeValue("approver", null);
        getMainVO(vo).setAttributeValue("approvenote", null);
        getMainVO(vo).setAttributeValue("approvedate", null);
        //���������ϢΪ��
        getMainVO(vo).setAttributeValue("creator",null);
        getMainVO(vo).setAttributeValue("creationtime",null);
        getMainVO(vo).setAttributeValue("modifier",null);
        getMainVO(vo).setAttributeValue("modifiedtime",null);
        getMainVO(vo).setAttributeValue("maketime", new UFDateTime(InvocationInfoProxy.getInstance().getBizDateTime()));

        SettlementBVO[] settlementBVOs = (SettlementBVO[])vo.getChildren(SettlementBVO.class);
        if(settlementBVOs!=null && settlementBVOs.length>0){
            Arrays.stream(settlementBVOs).forEach(subvo->{
                subvo.setPrimaryKey(null);
                subvo.setStatus(VOStatus.NEW);
                subvo.setAttributeValue("srcbilltype",null);
                subvo.setAttributeValue("srcbillid",null);
                subvo.setAttributeValue("rowno", null);
                subvo.setAttributeValue("vsrcrowno", null);
            });
        }
        return vo;
    }
    @Override
    public AggSettlementHVO[] saveAggSettlementHVO(AggSettlementHVO vo) throws BusinessException{
        String pk = getVOPrimaryKey(vo);
        setDefaultVal(vo);
        if(StringUtils.isEmpty(pk)){
            return dao.insert(vo); //����
        }else{
            return dao.update(vo); //����
        }
    }
    /**
     * ����ǰ����������
     * @param vos
     */
    private void setBillCode(AggSettlementHVO... vos) throws BusinessException {
        if(ArrayUtils.isNotEmpty(vos)) {
            for(AggSettlementHVO vo : vos) {
                String pk = getVOPrimaryKey(vo);
                if(StringUtils.isEmpty(pk)){
                    BillCodeContext billCodeContext = getBillCodeContext("sosoSettlement");
                    String pk_group = InvocationInfoProxy.getInstance().getGroupId();
                    if(billCodeContext!=null && !billCodeContext.isPrecode()){
                        if(getMainVO(vo).getAttributeValue("billno") == null){
                            String code = getBillcodeManage().getBillCode_RequiresNew("sosoSettlement", pk_group, pk_group, getMainVO(vo));
                            getMainVO(vo).setAttributeValue("billno",code);
                        }
                    } else {
                        String code = (String) getMainVO(vo).getAttributeValue("billno");
                        getBillcodeManage().commitPreBillCode("sosoSettlement", pk_group, pk_group, code);
                    }
                }
            }
        }
    }
    /**
     * ����ǰ���������Ϣ
     * @param vos
     */
    private void setAuditInfo(AggSettlementHVO... vos) throws BusinessException {
        if(ArrayUtils.isNotEmpty(vos)) {
            UFDateTime now = new UFDateTime();
            for(AggSettlementHVO vo : vos) {
                String pk = getVOPrimaryKey(vo);
                if(StringUtils.isEmpty(pk)){
                    //���ô����˴���ʱ��
                    getMainVO(vo).setAttributeValue("creator",InvocationInfoProxy.getInstance().getUserId());
                    getMainVO(vo).setAttributeValue("creationtime",now);
                    getMainVO(vo).setAttributeValue("maketime",now);
                    getMainVO(vo).setAttributeValue("billmaker", InvocationInfoProxy.getInstance().getUserId()); // �Ƶ���
                    getMainVO(vo).setAttributeValue("modifier",null);
                    getMainVO(vo).setAttributeValue("modifiedtime",null);
                }else{
                    //�����޸����޸�ʱ��
                    getMainVO(vo).setAttributeValue("modifier",InvocationInfoProxy.getInstance().getUserId());
                    getMainVO(vo).setAttributeValue("modifiedtime",now);
                    getMainVO(vo).setAttributeValue("lastmaketime",now);
                }
            }
        }
    }
    /**
     * ����ǰ����һЩĬ��ֵ
     * @param vos
     */
    private void setDefaultVal(AggSettlementHVO... vos) throws BusinessException {
        setBillCode(vos);
        setAuditInfo(vos);
        //����Ĭ��ֵ����
    }

    // �������б༭����������
    @Override
    public AggSettlementHVO[] saveAggSettlementHVO(AggSettlementHVO[] vos) throws BusinessException {
        if (ArrayUtils.isEmpty(vos)) {
            return new AggSettlementHVO[0];
        }
        setDefaultVal(vos); // ����Ĭ��ֵ
        List<String> pks = Arrays.stream(vos).filter(v -> getMainVO(v).getStatus() == VOStatus.DELETED)
                .map(v -> getMainVO(v).getPrimaryKey()).collect(Collectors.toList()); // ɾ����������
        if (pks == null || pks.size() == 0) {
            return dao.save(vos, true);
        }
        AggSettlementHVO[] deleteVOs = dao.listByPk(AggSettlementHVO.class, pks.toArray(new String[0]));
        for (int i = 0; i < deleteVOs.length; i++) {
            SuperVO mainVO = getMainVO(deleteVOs[i]);
            // ɾ������ʱУ�鵥��״̬
            Integer approveStatus = (Integer) mainVO.getAttributeValue("approvestatus");
            if (approveStatus != null && !approveStatus.equals(-1)) {
                throw new BusinessException("��" + (i + 1) + "�ŵ��ݴ���ʧ�ܣ�����״̬����ȷ������ɾ����");
            }
            // ɾ������ʱ���˵��ݺ�
            String code = (String)mainVO.getAttributeValue("billno");
            if (StringUtils.isNotEmpty(code)) {
                String pk_group = InvocationInfoProxy.getInstance().getGroupId();
                getBillcodeManage().returnBillCodeOnDelete("sosoSettlement", pk_group, pk_group, code, deleteVOs[i]);
            }
        }
        return dao.save(vos,true);
    }

    @Override
    public AggSettlementHVO[] deleteAggSettlementHVOs(Map<String,String> tsMap) throws BusinessException{
        AggSettlementHVO[] vos = dao.listByPk(AggSettlementHVO.class,tsMap.keySet().toArray(new String[0]));
        validate(vos,tsMap);
        String pk_group = InvocationInfoProxy.getInstance().getGroupId();
        Arrays.stream(vos).forEach(vo->{
            String code = (String)getMainVO(vo).getAttributeValue("billno");
            try {
                getBillcodeManage().returnBillCodeOnDelete("sosoSettlement",pk_group,pk_group,code,vo);
            } catch (BusinessException e) {
                ExceptionUtils.wrappBusinessException(e.getMessage(),e);
            }
        });
        dao.delete(vos,true);
        return vos;
    }

    //У��  ����tsУ��  ���ύУ��
    private void validate(AggSettlementHVO[] vos,Map<String,String> tsMap) throws BusinessException{

        Boolean isPass = true;
        String error = "";    //������Ϣ
        if(ArrayUtils.isEmpty(vos)){
            isPass = false;
        }

        for(int i = 0 ; i < vos.length ; i++){
            SuperVO mainvo = vos[i].getParentVO();
            UFDateTime ts = (UFDateTime)mainvo.getAttributeValue("ts");
            if(!StringUtils.equals(tsMap.get(mainvo.getPrimaryKey()),ts.toString())){
                isPass = false;
                break;
            }
            Integer approvestatus = (Integer) mainvo.getAttributeValue("approvestatus");
            if(approvestatus == null || approvestatus != BillStatusEnum.FREE.toIntValue()){
                error += "��"+(i+1)+"�ŵ��ݴ���ʧ�ܣ�����״̬����ȷ������ɾ����\n";
            }
        }
        if(!isPass) {
            throw new BusinessException("�������������ѱ������޸Ļ�ɾ������ˢ�º����ԣ�");
        }
        if(!"".equals(error)){
            throw new BusinessException(error);
        }
    }

    @Override
    public <T> T[] loadTreeData(Class<T> clazz,Map<String,Object> userJson) throws BusinessException{
        String condition = "dr = 0 ";
        return dao.listByCondition(clazz, condition, false);
    }

    @Override
    public String[] queryChildPksByParentId(Class childClazz, String parentId) throws BusinessException{
        String cond = " pk_settlement = '" + parentId + "' ";
        SuperVO[] vos  = (SuperVO[]) dao.listByCondition(childClazz, cond, false);
        if (vos == null || vos.length == 0) {
            return new String[0];
        }
        return Stream.of(vos).map(vo -> vo.getPrimaryKey()).toArray(String[]::new);
    }


    public SuperVO[] queryChildVOByPks(Class childClazz, String[] pks) throws BusinessException{
        return (SuperVO[]) dao.listByPk(childClazz, pks, false);
    }

    /**
     * �ύǰУ��:
     * ��鵥��״̬
     * @throws BusinessException
     * */
    private void validateCommitAggSettlementHVO(AggSettlementHVO... vos) throws BusinessException {
        if(ArrayUtils.isEmpty(vos)) {
            return ;
        }
        List<AggSettlementHVO> list = Arrays.stream(vos)
                .filter(item ->item.getParentVO()!=null)
                .filter(item->{
                    Integer status =  (Integer) item.getParentVO().getAttributeValue("approvestatus");
                    return status==null||status!=BillStatusEnum.FREE.toIntValue()&&status!=BillStatusEnum.NOPASS.toIntValue();
                }).map(item->item)
                .collect(Collectors.toList());
        if(list == null||list.size() == 0) {
            return;
        }
        String errors = "";
        for(AggSettlementHVO vo  : list) {
            errors+="���ݺţ�["+vo.getParentVO().getAttributeValue("billno")+"]�ύʧ�ܣ�ʧ��ԭ�򣺵���״̬����ȷ�����顣\n";
        }
        throw new BusinessException(errors);
    }
    /**
     * �ջ�ǰУ��:
     * ��鵥��״̬
     * @throws BusinessException
     * */
    private void validateUnCommitAggSettlementHVO(AggSettlementHVO... vos) throws BusinessException {
        if(ArrayUtils.isEmpty(vos)) {
            return ;
        }
        List<AggSettlementHVO> list = Arrays.stream(vos)
                .filter(item ->item.getParentVO()!=null)
                .filter(item->{
                    Integer status =  (Integer)item.getParentVO().getAttributeValue("approvestatus");
                    return status==null||status==BillStatusEnum.FREE.toIntValue();
                }).map(item->item)
                .collect(Collectors.toList());
        if(list == null||list.size() == 0) {
            return;
        }
        String errors = "";
        for(AggSettlementHVO vo  : list) {
            errors+="���ݺţ�["+vo.getParentVO().getAttributeValue("billno")+"]�ջ�ʧ�ܣ�ʧ��ԭ�򣺵���״̬����ȷ�����顣\n";
        }
        throw new BusinessException(errors);
    }
    @Override
    public Object commitAggSettlementHVO(String actionName,Map<String,String> tsMap,Object assign) throws BusinessException{
        AggSettlementHVO[] vos = dao.listByPk(AggSettlementHVO.class,getAllPks(tsMap),false);
        validateTs(tsMap,vos);
        //�ύǰУ�鼰ҵ���߼�
        validateCommitAggSettlementHVO(vos);
        Map<String,Object> res = this.execFlows(actionName,"KHJS",assign,vos);
        //�ύ��ҵ���߼�
        return res;
    }

    @Override
    public Object batchCommitAggSettlementHVO(String actionName,Map<String,String> tsMap) throws BusinessException{
        AggSettlementHVO[] vos = dao.listByPk(AggSettlementHVO.class,getAllPks(tsMap),false);
        validateTs(tsMap,vos);
        //�����ύǰУ�鼰ҵ���߼�
        validateCommitAggSettlementHVO(vos);
        Map<String,Object> res = this.execFlows(actionName,"KHJS",vos);
        //�����ύ��ҵ���߼�
        return res;
    }

    @Override
    public Object uncommitAggSettlementHVO(String actionName,Map<String,String> tsMap) throws BusinessException{
        AggSettlementHVO[] vos = dao.listByPk(AggSettlementHVO.class,getAllPks(tsMap),false);
        validateTs(tsMap,vos);
        //�ջ�ǰУ�鼰ҵ���߼�
        validateUnCommitAggSettlementHVO(vos);
        Map<String,Object> res = this.execFlows(actionName,"KHJS",vos);
        //�ջغ�ҵ���߼�
        return res;
    }

    @Override
    public Object batchUncommitAggSettlementHVO(String actionName,Map<String,String> tsMap) throws BusinessException{
        AggSettlementHVO[] vos = dao.listByPk(AggSettlementHVO.class,getAllPks(tsMap),false);
        validateTs(tsMap,vos);
        //�����ջ�ǰУ�鼰ҵ���߼�
        validateUnCommitAggSettlementHVO(vos);
        Map<String,Object> res = this.execFlows(actionName,"KHJS",vos);
        //�����ջغ�ҵ���߼�
        return res;
    }

    @Override
    public AggSettlementHVO[] callbackSAVEBASE(AggSettlementHVO...vos) throws BusinessException{
        if(ArrayUtils.isEmpty(vos)) {
            return null;
        }
        return this.saveAggSettlementHVO(vos);

    }


    @Override
    public AggSettlementHVO[] callbackSAVE(AggSettlementHVO...vos) throws BusinessException{
        if(ArrayUtils.isEmpty(vos)) {
            return null;
        }
        //ͬ������״̬������״̬(ֻ���ύʱ����Ҫ�ֶ���������״̬�����������������״̬���Ѹ���)
        Arrays.stream(vos).forEach(v->{
            v.getParent().setAttributeValue("approvestatus",BillStatusEnum.COMMIT.toIntValue());
        });
        return this.saveAggSettlementHVO(vos);

    }


    @Override
    public AggSettlementHVO[] callbackUNSAVE(AggSettlementHVO...vos) throws BusinessException{
        if(ArrayUtils.isEmpty(vos)) {
            return null;
        }
        return this.saveAggSettlementHVO(vos);

    }


    @Override
    public AggSettlementHVO[] callbackAPPROVE(AggSettlementHVO...vos) throws BusinessException{
        if(ArrayUtils.isEmpty(vos)) {
            return null;
        }else{
            for (AggSettlementHVO aggvo:vos) {
                //��������Ϊ���ܵĵ���ʱ�������۷�Ʊ
                String transtype=(String)aggvo.getParentVO().getAttributeValue("transtype");
                //KHJS-Cxx-02
                if("KHJS-Cxx-01".equals(transtype)){
                    //�������۷�Ʊ
                    AggSettlementHVO[] aggvos = {aggvo};
                    //����ת��:�ͻ����㵥--���۷�Ʊ
                    AggregatedValueObject[] arrReturn = NCLocator.getInstance().lookup(IPfExchangeService.class)
                            .runChangeDataAry("KHJS", "32", aggvos, null);
                    SaleInvoiceVO saleinvoicevo =(SaleInvoiceVO ) arrReturn[0];
                    //����ƽ̨ ����
                    NCLocator.getInstance().lookup(IplatFormEntry.class).processAction("WRITE","32", null, saleinvoicevo, null, null);
                }

            }
        }
        return this.saveAggSettlementHVO(vos);

    }


    @Override
    public AggSettlementHVO[] callbackUNAPPROVE(AggSettlementHVO...vos) throws BusinessException{
        if(ArrayUtils.isEmpty(vos)) {
            return null;
        }
        return this.saveAggSettlementHVO(vos);

    }


    @Override
    public BillCodeContext getBillCodeContext(String coderuleid) throws BusinessException{
        return super.getBillCodeContext(coderuleid);
    }

    @Override
    public BillCodeContext getBillCodeContext(String coderuleid, String pkgroup, String pkorg) throws BusinessException{
        return super.getBillCodeContext(coderuleid,pkgroup,pkorg);
    }

    @Override
    public AggSettlementHVO[] summaryAggSettlementHVO(Map<String,String> tsMap) throws BusinessException {
        // chensyk
        AggSettlementHVO[] vos = dao.listByPk(AggSettlementHVO.class,tsMap.keySet().toArray(new String[0]));
        AggSettlementHVO aggVo=vos[0];
        AggSettlementHVO newAggVo=new AggSettlementHVO();
        SettlementHVO newhvo=(SettlementHVO) aggVo.getParentVO().clone();
        IUAPQueryBS iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        //�õ��·�
        String month=newhvo.getAttributeValue("creationtime").toString().substring(0, 7);

        //����
        SettlementBVO bvo=(SettlementBVO) aggVo.getChildrenVO()[0];
        String cordercustid=bvo.getAttributeValue("cordercustid").toString();//�ͻ�
        //��ѯ�ÿͻ�������û�����ɹ����ܵ�
        String sql_sum="select count(pk_settlement) from so_settlement"
                +" where dr=0 and substr(so_settlement.creationtime,0,7) = '"+month+"'"
                + " and pk_settlement in (select pk_settlement from so_settlement_b where dr=0 and  cordercustid='"+cordercustid+"')"
                + " and transtype='KHJS-Cxx-01'";

        Integer count_in=(Integer) iUAPQueryBS.executeQuery(sql_sum, new ColumnProcessor());
        if(count_in!=0){
            ExceptionUtils.wrappBusinessException("�ÿͻ������Ѿ����ɹ����ܵ����뵽�ͻ����ܽ��㵥�鿴!");

        }
        newhvo.setAttributeValue("transtype", "KHJS-Cxx-01");//������������Ϊ����
        String sql_transtype="select pk_billtypeid  from bd_billtype where pk_billtypecode ='KHJS-Cxx-01'";
        newhvo.setAttributeValue("transtypepk",iUAPQueryBS.executeQuery(sql_transtype, new ColumnProcessor()));//������������Ϊ����
        newhvo.setAttributeValue("transtype1","KHJS-Cxx-01");//������������Ϊ����
        String[] clearHeadKeys = new String[] {
                "pk_settlement", "billno",
                "dmakedate", "creator", "creationtime", "modifier",
                "modifiedtime", "approver", "approvedate", "billmaker", "ts",
                "srcbilltype", "srcbillid" };
        VOEntityUtil.clearVOValue(newhvo, clearHeadKeys);
        newhvo.setAttributeValue("approvestatus", -1);// ����״̬
        newhvo.setAttributeValue("dmakedate", AppContext.getInstance().getBusiDate());//���õ�������
        //��ѯ��ͬ�ͻ�+�·�+���ϵĵ���
        List aggvolist = (List) MDPersistenceService.lookupPersistenceQueryService() .queryBillOfVOByCond(AggSettlementHVO.class,
                " substr(so_settlement.creationtime,0,7) = '"+month+"' and dr = 0 "
                        + " and pk_settlement in (select pk_settlement from so_settlement_b where dr=0 and  cordercustid='"+cordercustid+"')", false);
        SettlementBVO newbvo=(SettlementBVO) ((AggSettlementHVO)aggvolist.get(0)).getChildrenVO()[0];
        String[] clearbodykeys = { "pk_settlement","pk_settlement_b","vsrcrowno","batch","ts" };
        VOEntityUtil.clearVOValue(newbvo, clearbodykeys);
        UFDouble sumWetWeight=UFDouble.ZERO_DBL;//����ʪ�غ�
        UFDouble sumDryWeight=UFDouble.ZERO_DBL;//������غ�
        UFDouble sumMoney=UFDouble.ZERO_DBL;//�������
        UFDouble sumNnum=UFDouble.ZERO_DBL;//������������
        UFDouble sumNastnum=UFDouble.ZERO_DBL;//����������

        SettlementHVO[] updatevo = new SettlementHVO[aggvolist.size()];
        for(int i=0;i<aggvolist.size();i++){
            AggSettlementHVO aggvo=(AggSettlementHVO) aggvolist.get(i);
            //У���Ƿ�ȫ��Ϊ����ͨ��״̬�����򱨴�
            if((Integer)aggvo.getParentVO().getAttributeValue("approvestatus")!=1){
                ExceptionUtils.wrappBusinessException("���ݺ�"+aggvo.getParentVO().getAttributeValue("billno")+"Ϊ�Ľ��㵥û������ͨ����������!");
            };
            SettlementHVO hvo = aggvo.getParentVO();
            hvo.setAttributeValue("vdef2", "Y");

            updatevo[i] = hvo;
            SettlementBVO oldbvo=(SettlementBVO) aggvo.getChildrenVO()[0];
            String s_wet_weight =oldbvo.getAttributeValue("wet_weight")==null?"0":oldbvo.getAttributeValue("wet_weight").toString();
            UFDouble wet_weight =new UFDouble(s_wet_weight);//ʪ��
            sumWetWeight=sumWetWeight.add(wet_weight);
            String s_dry_weight =oldbvo.getAttributeValue("dry_weight")==null?"0":oldbvo.getAttributeValue("dry_weight").toString();
            UFDouble dry_weight =new UFDouble(s_dry_weight);//����
            sumDryWeight=sumDryWeight.add(dry_weight);
            String s_money =oldbvo.getAttributeValue("money")==null?"0":oldbvo.getAttributeValue("money").toString();
            UFDouble money =new UFDouble(s_money);//���
            sumMoney=sumMoney.add(money);
            String s_nnum =oldbvo.getAttributeValue("nnum")==null?"0":oldbvo.getAttributeValue("nnum").toString();

            UFDouble nnum =new UFDouble(s_nnum);//������
            sumNnum=sumNnum.add(nnum);
            String s_nastnum =oldbvo.getAttributeValue("nastnum")==null?"0":oldbvo.getAttributeValue("nastnum").toString();
            UFDouble nastnum =new UFDouble(s_nastnum);//����
            sumNastnum=sumNastnum.add(nastnum);
        }
        newbvo.setAttributeValue("wet_weight", sumWetWeight);//ʪ��
        newbvo.setAttributeValue("dry_weight", sumDryWeight);//����
        newbvo.setAttributeValue("money", sumMoney.setScale(2, UFDouble.ROUND_HALF_UP));//���
        newbvo.setAttributeValue("unitprice", sumMoney.div(sumDryWeight).setScale(2, UFDouble.ROUND_HALF_UP));//����
        newbvo.setAttributeValue("nnum", sumNnum);//������
        newbvo.setAttributeValue("nastnum", sumNastnum);//����
        newAggVo.setParentVO(newhvo);
        newAggVo.setChildrenVO(new SettlementBVO[]{newbvo});
        //����һ�����ӵ���
        // ������㵥
        IPFBusiAction ipfBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class);
        AggSettlementHVO[] settleVOs= (AggSettlementHVO[]) ipfBusiAction.processAction("SAVEBASE","KHJS" , null,newAggVo , null, null);
        if(settleVOs!=null&&settleVOs.length>0){
            return settleVOs;
        }
        return null;
    }
}
