package nc.bs.ic.m4d.process;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pub.util.SuperSqlUtil;
import nc.vo.ic.m4a.entity.GeneralInVO;
import nc.vo.ic.m4d.entity.MaterialOutBodyVO;
import nc.vo.ic.m4d.entity.MaterialOutVO;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.res.billtype.ICBillType;

import java.util.ArrayList;
import java.util.List;

public class BillI4FinanceProcess {

    BaseDAO dao = new BaseDAO();

    public void saveI4Bill(MaterialOutVO[] vos) {

        MaterialOutVO vo = vos[0];
        String pk_org = vo.getParentVO().getPk_org();
        try {


            String sql = "SELECT p1.value FROM pub_sysinit p1 INNER JOIN pub_sysinittemp p2 ON p1.sysinit = p2.pk_sysinittemp " +
                    "WHERE p2.domainflag = '4008'  AND p2.showflag = 'Y' AND p1.pk_org = '" + pk_org + "' and p2.initcode = 'IC121'";
            String ison = (String) dao.executeQuery(sql, new ColumnProcessor());
            //业务单元级参数控制
            if ("Y".equals(ison)) {
                sql = "  select pk_material  from bd_material where code in "
                        + "( select code from bd_defdoc where pid ="
                        + "（SELECT pk_defdoc FROM bd_defdoc WHERE pk_defdoclist = "
                        + "( SELECT metaid  FROM pub_bcr_nbcr WHERE code = 'GYL003_01') and  enablestate = 2  and  pk_org = '"
                        + pk_org + "' and name = '物料明细'）) and enablestate = 2 ";

                List<String> pk_material_list = (List<String>) dao
                        .executeQuery(sql, new ColumnListProcessor());
                sql = "select b1.code from bd_defdoc b1 inner join  bd_defdoc b2 on b1.pid = b2.pk_defdoc "
                        + "inner join pub_bcr_nbcr  p1 on  b2.pk_defdoclist = p1.metaid "
                        + "where p1.code = 'GYL003_01' and b2.enablestate = 2 and b2.pk_org = '"
                        + pk_org + "' and b2.name = '物料分类' and b1.enablestate = 2";
                List<String> marbasclasscode_list = (List<String>) dao
                        .executeQuery(sql, new ColumnListProcessor());
                List<String> pk_marbasclass_list = new ArrayList<String>();// 本级和下级物料分类
                if (marbasclasscode_list.size() > 0) {
                    // 查询本级和下级的物料分类
                    StringBuffer classTypeBuffer = new StringBuffer();
                    classTypeBuffer
                            .append("select pk_marbasclass from bd_marbasclass  start with ");
                    classTypeBuffer.append(SuperSqlUtil.getInStr("code",
                            marbasclasscode_list, true));
                    classTypeBuffer
                            .append(" connect by prior pk_marbasclass  =  pk_parent  ");
                    pk_marbasclass_list = (List<String>) dao.executeQuery(
                            classTypeBuffer.toString(),
                            new ColumnListProcessor());
                }
                MaterialOutBodyVO[] bodys = (MaterialOutBodyVO[]) vo
                        .getChildrenVO();
                List<MaterialOutBodyVO> bodylist = new ArrayList<MaterialOutBodyVO>();
                int rowno = 1;
                if (pk_material_list.size() > 0) {

                    for (int j = 0; j < bodys.length; j++) {
                        MaterialOutBodyVO bodyvo = bodys[j];
                        String pk_material = bodyvo.getCmaterialvid();
                        // 先判断是否属于以旧换新的物料
                        for (int p = 0; p < pk_material_list.size(); p++) {
                            if (pk_material_list.get(p).equals(pk_material)) {
                                bodyvo.setCrowno(String.valueOf(rowno * 10));
                                bodylist.add(bodyvo);
                                rowno++;
                                break;
                            }
                            // 后判断物料是否属于以旧换新的物料分类
                            if (p == pk_material_list.size() - 1) {
                                if (pk_marbasclass_list.size() > 0) {
                                    sql = "select pk_marbasclass from bd_material where   pk_material  = '"
                                            + pk_material + "' ";
                                    String pk_marbasclass = (String) dao
                                            .executeQuery(sql,
                                                    new ColumnProcessor());
                                    for (int m = 0; m < pk_marbasclass_list.size(); m++) {
                                        if (pk_marbasclass_list.get(m).equals(pk_marbasclass)) {
                                            bodyvo.setCrowno(String
                                                    .valueOf(rowno * 10));
                                            bodylist.add(bodyvo);
                                            rowno++;
                                            break;
                                        }
                                    }
                                }

                            }
                        }

                    }

                } else if (pk_marbasclass_list.size() > 0) {
                    for (int j = 0; j < bodys.length; j++) {
                        MaterialOutBodyVO bodyvo = bodys[j];
                        String pk_material = bodyvo.getCmaterialvid();
                        sql = "select pk_marbasclass from bd_material where pk_material  = '"
                                + pk_material + "' ";
                        String pk_marbasclass = (String) dao.executeQuery(sql,
                                new ColumnProcessor());
                        for (int m = 0; m < pk_marbasclass_list.size(); m++) {
                            if (pk_marbasclass_list.get(m).equals(pk_marbasclass)) {
                                bodyvo.setCrowno(String.valueOf(rowno * 10));
                                bodylist.add(bodyvo);
                                rowno++;
                                break;
                            }
                        }

                    }
                }

                if (rowno == 1) {
                    return;
                } else {
                    MaterialOutBodyVO[] final_bvos = bodylist
                            .toArray(new MaterialOutBodyVO[bodylist.size()]);// 以旧换新定义过滤后的材料材料出库
                    vo.setChildrenVO(final_bvos);
                    GeneralInVO[] agg_general = PfServiceScmUtil
                            .executeVOChange(ICBillType.MaterialOut.getCode(),
                                    ICBillType.GeneralIn.getCode(), vos);

                    IPFBusiAction service = NCLocator.getInstance().lookup(
                            IPFBusiAction.class);
                    for (int i = 0; i < agg_general.length; i++) {
                        GeneralInVO aggvo = agg_general[0];
                        String vnoteString = aggvo.getParentVO().getVnote();
                        if (vnoteString != null && vnoteString.length() != 0) {
                            vnoteString = vnoteString + " 由材料出库自动生成单据";
                        } else {
                            vnoteString = "由材料出库自动生成单据";
                        }
                        aggvo.getParentVO().setVnote(vnoteString);
                        GeneralInVO[] icbills = (GeneralInVO[]) service
                                .processAction(IPFActionName.WRITE, "4A", null,
                                        aggvo, null, null);// 生成库存其他入库
                        if (icbills.length > 0) {
                            GeneralInVO[] signbills = (GeneralInVO[]) service
                                    .processAction("SIGN", "4A", null,
                                            icbills[0], null, null);// 库存其他入库签字生成其他入库单
                        }
                    }
                }
            }


        } catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }


    }

}
