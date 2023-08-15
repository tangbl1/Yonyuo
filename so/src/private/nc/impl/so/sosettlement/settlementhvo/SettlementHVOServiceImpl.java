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

        //下面这段要判断是否是树表界面插件
        Map<String,String> data = userJson!=null && userJson.get("data") != null?(Map<String,String>)userJson.get("data"):null;
        if(data!=null && data.size()>0){
            String parentKey = data.get("parentKey");
            String parentPk = data.get("parentPk");
            getMainVO(vo).setAttributeValue(parentKey,parentPk);
        }

        //编码规则生成vo的编码
        BillCodeContext billCodeContext = getBillCodeContext("sosoSettlement");
        if(billCodeContext == null){
            throw new BusinessException("当前编码规则不存在，请到【编码规则定义-全局】节点检查是否存在"+"sosoSettlement");
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
        //设置默认值
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
        //编码规则生成vo的编码
        BillCodeContext billCodeContext = getBillCodeContext("sosoSettlement");
        if(billCodeContext == null){
            throw new BusinessException("当前编码规则不存在，请到【编码规则定义-全局】节点检查是否存在"+"sosoSettlement");
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
        //设置审计信息为空
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
            return dao.insert(vo); //插入
        }else{
            return dao.update(vo); //更新
        }
    }
    /**
     * 保存前处理编码规则
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
     * 保存前设置审计信息
     * @param vos
     */
    private void setAuditInfo(AggSettlementHVO... vos) throws BusinessException {
        if(ArrayUtils.isNotEmpty(vos)) {
            UFDateTime now = new UFDateTime();
            for(AggSettlementHVO vo : vos) {
                String pk = getVOPrimaryKey(vo);
                if(StringUtils.isEmpty(pk)){
                    //设置创建人创建时间
                    getMainVO(vo).setAttributeValue("creator",InvocationInfoProxy.getInstance().getUserId());
                    getMainVO(vo).setAttributeValue("creationtime",now);
                    getMainVO(vo).setAttributeValue("maketime",now);
                    getMainVO(vo).setAttributeValue("billmaker", InvocationInfoProxy.getInstance().getUserId()); // 制单人
                    getMainVO(vo).setAttributeValue("modifier",null);
                    getMainVO(vo).setAttributeValue("modifiedtime",null);
                }else{
                    //设置修改人修改时间
                    getMainVO(vo).setAttributeValue("modifier",InvocationInfoProxy.getInstance().getUserId());
                    getMainVO(vo).setAttributeValue("modifiedtime",now);
                    getMainVO(vo).setAttributeValue("lastmaketime",now);
                }
            }
        }
    }
    /**
     * 保存前处理一些默认值
     * @param vos
     */
    private void setDefaultVal(AggSettlementHVO... vos) throws BusinessException {
        setBillCode(vos);
        setAuditInfo(vos);
        //其他默认值处理
    }

    // 给单表（行编辑表）单独适配
    @Override
    public AggSettlementHVO[] saveAggSettlementHVO(AggSettlementHVO[] vos) throws BusinessException {
        if (ArrayUtils.isEmpty(vos)) {
            return new AggSettlementHVO[0];
        }
        setDefaultVal(vos); // 设置默认值
        List<String> pks = Arrays.stream(vos).filter(v -> getMainVO(v).getStatus() == VOStatus.DELETED)
                .map(v -> getMainVO(v).getPrimaryKey()).collect(Collectors.toList()); // 删除单据主键
        if (pks == null || pks.size() == 0) {
            return dao.save(vos, true);
        }
        AggSettlementHVO[] deleteVOs = dao.listByPk(AggSettlementHVO.class, pks.toArray(new String[0]));
        for (int i = 0; i < deleteVOs.length; i++) {
            SuperVO mainVO = getMainVO(deleteVOs[i]);
            // 删除单据时校验单据状态
            Integer approveStatus = (Integer) mainVO.getAttributeValue("approvestatus");
            if (approveStatus != null && !approveStatus.equals(-1)) {
                throw new BusinessException("第" + (i + 1) + "张单据处理失败：单据状态不正确，不能删除！");
            }
            // 删除单据时回退单据号
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

    //校验  包括ts校验  已提交校验
    private void validate(AggSettlementHVO[] vos,Map<String,String> tsMap) throws BusinessException{

        Boolean isPass = true;
        String error = "";    //错误信息
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
                error += "第"+(i+1)+"张单据处理失败：审批状态不正确，不能删除！\n";
            }
        }
        if(!isPass) {
            throw new BusinessException("您操作的数据已被他人修改或删除，请刷新后重试！");
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
     * 提交前校验:
     * 检查单据状态
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
            errors+="单据号：["+vo.getParentVO().getAttributeValue("billno")+"]提交失败，失败原因：单据状态不正确，请检查。\n";
        }
        throw new BusinessException(errors);
    }
    /**
     * 收回前校验:
     * 检查单据状态
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
            errors+="单据号：["+vo.getParentVO().getAttributeValue("billno")+"]收回失败，失败原因：单据状态不正确，请检查。\n";
        }
        throw new BusinessException(errors);
    }
    @Override
    public Object commitAggSettlementHVO(String actionName,Map<String,String> tsMap,Object assign) throws BusinessException{
        AggSettlementHVO[] vos = dao.listByPk(AggSettlementHVO.class,getAllPks(tsMap),false);
        validateTs(tsMap,vos);
        //提交前校验及业务逻辑
        validateCommitAggSettlementHVO(vos);
        Map<String,Object> res = this.execFlows(actionName,"KHJS",assign,vos);
        //提交后业务逻辑
        return res;
    }

    @Override
    public Object batchCommitAggSettlementHVO(String actionName,Map<String,String> tsMap) throws BusinessException{
        AggSettlementHVO[] vos = dao.listByPk(AggSettlementHVO.class,getAllPks(tsMap),false);
        validateTs(tsMap,vos);
        //批量提交前校验及业务逻辑
        validateCommitAggSettlementHVO(vos);
        Map<String,Object> res = this.execFlows(actionName,"KHJS",vos);
        //批量提交后业务逻辑
        return res;
    }

    @Override
    public Object uncommitAggSettlementHVO(String actionName,Map<String,String> tsMap) throws BusinessException{
        AggSettlementHVO[] vos = dao.listByPk(AggSettlementHVO.class,getAllPks(tsMap),false);
        validateTs(tsMap,vos);
        //收回前校验及业务逻辑
        validateUnCommitAggSettlementHVO(vos);
        Map<String,Object> res = this.execFlows(actionName,"KHJS",vos);
        //收回后业务逻辑
        return res;
    }

    @Override
    public Object batchUncommitAggSettlementHVO(String actionName,Map<String,String> tsMap) throws BusinessException{
        AggSettlementHVO[] vos = dao.listByPk(AggSettlementHVO.class,getAllPks(tsMap),false);
        validateTs(tsMap,vos);
        //批量收回前校验及业务逻辑
        validateUnCommitAggSettlementHVO(vos);
        Map<String,Object> res = this.execFlows(actionName,"KHJS",vos);
        //批量收回后业务逻辑
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
        //同步单据状态和审批状态(只有提交时候需要手动设置审批状态。其余审批完后审批状态都已更新)
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
                //交易类型为汇总的单据时生成销售发票
                String transtype=(String)aggvo.getParentVO().getAttributeValue("transtype");
                //KHJS-Cxx-02
                if("KHJS-Cxx-01".equals(transtype)){
                    //生成销售发票
                    AggSettlementHVO[] aggvos = {aggvo};
                    //单据转换:客户结算单--销售发票
                    AggregatedValueObject[] arrReturn = NCLocator.getInstance().lookup(IPfExchangeService.class)
                            .runChangeDataAry("KHJS", "32", aggvos, null);
                    SaleInvoiceVO saleinvoicevo =(SaleInvoiceVO ) arrReturn[0];
                    //流程平台 保存
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
        //得到月份
        String month=newhvo.getAttributeValue("creationtime").toString().substring(0, 7);

        //表体
        SettlementBVO bvo=(SettlementBVO) aggVo.getChildrenVO()[0];
        String cordercustid=bvo.getAttributeValue("cordercustid").toString();//客户
        //查询该客户本月有没有生成过汇总单
        String sql_sum="select count(pk_settlement) from so_settlement"
                +" where dr=0 and substr(so_settlement.creationtime,0,7) = '"+month+"'"
                + " and pk_settlement in (select pk_settlement from so_settlement_b where dr=0 and  cordercustid='"+cordercustid+"')"
                + " and transtype='KHJS-Cxx-01'";

        Integer count_in=(Integer) iUAPQueryBS.executeQuery(sql_sum, new ColumnProcessor());
        if(count_in!=0){
            ExceptionUtils.wrappBusinessException("该客户本月已经生成过汇总单，请到客户汇总结算单查看!");

        }
        newhvo.setAttributeValue("transtype", "KHJS-Cxx-01");//交易类型设置为汇总
        String sql_transtype="select pk_billtypeid  from bd_billtype where pk_billtypecode ='KHJS-Cxx-01'";
        newhvo.setAttributeValue("transtypepk",iUAPQueryBS.executeQuery(sql_transtype, new ColumnProcessor()));//交易类型设置为汇总
        newhvo.setAttributeValue("transtype1","KHJS-Cxx-01");//交易类型设置为汇总
        String[] clearHeadKeys = new String[] {
                "pk_settlement", "billno",
                "dmakedate", "creator", "creationtime", "modifier",
                "modifiedtime", "approver", "approvedate", "billmaker", "ts",
                "srcbilltype", "srcbillid" };
        VOEntityUtil.clearVOValue(newhvo, clearHeadKeys);
        newhvo.setAttributeValue("approvestatus", -1);// 单据状态
        newhvo.setAttributeValue("dmakedate", AppContext.getInstance().getBusiDate());//设置单据日期
        //查询相同客户+月份+物料的单据
        List aggvolist = (List) MDPersistenceService.lookupPersistenceQueryService() .queryBillOfVOByCond(AggSettlementHVO.class,
                " substr(so_settlement.creationtime,0,7) = '"+month+"' and dr = 0 "
                        + " and pk_settlement in (select pk_settlement from so_settlement_b where dr=0 and  cordercustid='"+cordercustid+"')", false);
        SettlementBVO newbvo=(SettlementBVO) ((AggSettlementHVO)aggvolist.get(0)).getChildrenVO()[0];
        String[] clearbodykeys = { "pk_settlement","pk_settlement_b","vsrcrowno","batch","ts" };
        VOEntityUtil.clearVOValue(newbvo, clearbodykeys);
        UFDouble sumWetWeight=UFDouble.ZERO_DBL;//表体湿重和
        UFDouble sumDryWeight=UFDouble.ZERO_DBL;//表体干重和
        UFDouble sumMoney=UFDouble.ZERO_DBL;//表体金额和
        UFDouble sumNnum=UFDouble.ZERO_DBL;//表体主数量和
        UFDouble sumNastnum=UFDouble.ZERO_DBL;//表体数量和

        SettlementHVO[] updatevo = new SettlementHVO[aggvolist.size()];
        for(int i=0;i<aggvolist.size();i++){
            AggSettlementHVO aggvo=(AggSettlementHVO) aggvolist.get(i);
            //校验是否全部为审批通过状态，否则报错
            if((Integer)aggvo.getParentVO().getAttributeValue("approvestatus")!=1){
                ExceptionUtils.wrappBusinessException("单据号"+aggvo.getParentVO().getAttributeValue("billno")+"为的结算单没有审批通过，请审批!");
            };
            SettlementHVO hvo = aggvo.getParentVO();
            hvo.setAttributeValue("vdef2", "Y");

            updatevo[i] = hvo;
            SettlementBVO oldbvo=(SettlementBVO) aggvo.getChildrenVO()[0];
            String s_wet_weight =oldbvo.getAttributeValue("wet_weight")==null?"0":oldbvo.getAttributeValue("wet_weight").toString();
            UFDouble wet_weight =new UFDouble(s_wet_weight);//湿重
            sumWetWeight=sumWetWeight.add(wet_weight);
            String s_dry_weight =oldbvo.getAttributeValue("dry_weight")==null?"0":oldbvo.getAttributeValue("dry_weight").toString();
            UFDouble dry_weight =new UFDouble(s_dry_weight);//干重
            sumDryWeight=sumDryWeight.add(dry_weight);
            String s_money =oldbvo.getAttributeValue("money")==null?"0":oldbvo.getAttributeValue("money").toString();
            UFDouble money =new UFDouble(s_money);//金额
            sumMoney=sumMoney.add(money);
            String s_nnum =oldbvo.getAttributeValue("nnum")==null?"0":oldbvo.getAttributeValue("nnum").toString();

            UFDouble nnum =new UFDouble(s_nnum);//主数量
            sumNnum=sumNnum.add(nnum);
            String s_nastnum =oldbvo.getAttributeValue("nastnum")==null?"0":oldbvo.getAttributeValue("nastnum").toString();
            UFDouble nastnum =new UFDouble(s_nastnum);//数量
            sumNastnum=sumNastnum.add(nastnum);
        }
        newbvo.setAttributeValue("wet_weight", sumWetWeight);//湿重
        newbvo.setAttributeValue("dry_weight", sumDryWeight);//干重
        newbvo.setAttributeValue("money", sumMoney.setScale(2, UFDouble.ROUND_HALF_UP));//金额
        newbvo.setAttributeValue("unitprice", sumMoney.div(sumDryWeight).setScale(2, UFDouble.ROUND_HALF_UP));//单价
        newbvo.setAttributeValue("nnum", sumNnum);//主数量
        newbvo.setAttributeValue("nastnum", sumNastnum);//数量
        newAggVo.setParentVO(newhvo);
        newAggVo.setChildrenVO(new SettlementBVO[]{newbvo});
        //新增一条主子单据
        // 保存结算单
        IPFBusiAction ipfBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class);
        AggSettlementHVO[] settleVOs= (AggSettlementHVO[]) ipfBusiAction.processAction("SAVEBASE","KHJS" , null,newAggVo , null, null);
        if(settleVOs!=null&&settleVOs.length>0){
            return settleVOs;
        }
        return null;
    }
}
