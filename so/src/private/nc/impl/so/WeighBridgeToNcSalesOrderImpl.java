package nc.impl.so;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.so.IWeighBridgeToNcSalesOrderMaintain;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.persist.framework.MDPersistenceService;
import nc.pubitf.so.m30.api.ISaleOrderMaintainAPI;
import nc.vo.jcom.xml.XMLUtil;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.m30.entity.SaleOrderBVO;
import nc.vo.so.m30.entity.SaleOrderHVO;
import nc.vo.so.m30.entity.SaleOrderVO;
import nc.vo.so.m30.weighbridge.LogMsgVO;
import nc.vo.so.m30.weighbridge.Wb01VO;
import nc.vo.vorg.DeptVersionVO;
import nc.vo.vorg.OrgVersionVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import uap.mw.trans.TransactionFactory;
import uap.mw.trans.UAPUserTransanction;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ProjectName: so
 * @Package: nc.bs.so.m30.weighbridge.plugin
 * @ClassName: WeighBridgeToNcSalesOrderImpl
 * @Author: tbl
 * @Description:
 * @Date: 2023/2/15 9:19
 * @Version: 1.0
 */
public class WeighBridgeToNcSalesOrderImpl implements IWeighBridgeToNcSalesOrderMaintain {

    BaseDAO dao = new BaseDAO();

    String title = "";//前端标题
    String whether = "success";//	成功:"success",警告:"warning",出错:"danger"
    String information = "";
    String message = "";
    //下载成功标志
    String sign = "";//fail 失败  success  成功
    String log_msg = "";
    String updatenum = "";
    String insertnum = "";
    LogMsgVO logVo = new LogMsgVO();
    private UAPUserTransanction trans = TransactionFactory.getUTransaction();

    public JSONObject weighbridgeToTable() {

        Map<String, String> sqlServerConfigMap = null;

        try {
            //读取配置信息
            sqlServerConfigMap = loadSqlServerConfigFromXML();
        } catch (IOException | SAXException e) {
            title = "失败！";
            whether = "danger";
            information = "读取xml配置文件失败，请检查！";
        }
        if ("success".equals(whether)) {
            try {
                //读取配置文件中的开始结束日期，如果没有设置或设置长度不正确，则使用默认的导入周期
                String webService = sqlServerConfigMap.get("WebService");
                String fromSQL = sqlServerConfigMap.get("FROMSQL");
                String[] array = this.monthBeginAndEnd();
                String beginBeforeDay = array[0];
                String endBeforeDay = array[1];
                String nc_sql = " select lsh, ch from weighbridge01 "
                        + "where  updatetime >= '" + beginBeforeDay + "' and updatetime <= '" + endBeforeDay + "'";


                ArrayList<HashMap> al = (ArrayList<HashMap>) dao.executeQuery(nc_sql, new MapListProcessor());
                HashMap history = new HashMap();
                for (int i = 0; i < al.size(); i++) {
                    Map hm = al.get(i);
                    history.put(hm.get("lsh"), hm.get("ch"));
                }
                String query = fromSQL
                        + " where 更新时间 >= '" + beginBeforeDay + "' "
                        + "and 更新时间 <= '" + endBeforeDay + "' "
                        + " and "
                        + "  货名 in  ('铁精粉','铁精粉(罕王)') ";

                JSONObject jsonobject = new JSONObject();
                jsonobject.put("doType", "query");
                jsonobject.put("sqlStr", query);
                Service service = new Service();
                Call call = (Call) service.createCall();
                call.setTargetEndpointAddress(new URL(webService));//"http://39.152.48.75:1809/hw_weight/services/WeightHandle?wsdl"
                call.setOperationName("opeHandle");
                String result = (String) call.invoke(new Object[]{jsonobject.toString()});
                ArrayList<Wb01VO> res_insert = new ArrayList<Wb01VO>();//要插入的数据
                ArrayList<Wb01VO> res_update = new ArrayList<Wb01VO>();//要更新的数据
                Wb01VO vo = null;
                JSONObject jsonStr = JSONObject.fromObject(result);
                String status = jsonStr.getString("status");
                JSONObject jb = null;
                if ("true".equals(status)) {
                    JSONArray ja = jsonStr.getJSONArray("result");
                    for (int i = 0; i < ja.size(); i++) {
                        jb = ja.getJSONObject(i);
                        int xh = (int) jb.get("序号");
                        String lsh = jb.get("流水号").toString();
                        //更新标志判断是否更新
                        if (jb.get("是否更新") != null && "是".equals(jb.get("是否更新").toString()) && history.containsKey(lsh)) {
                            //更新时-先根据序号和流水号找到对应的数据
                            String sql = "select * from weighbridge01 where xh='" + xh + "' "
                                    + " and xh='" + lsh + "' ";
                            vo = (Wb01VO) dao.executeQuery(sql, new BeanProcessor(Wb01VO.class));
                            vo.setIsupdate("Y");
                        } else {//不是更新--插入的
                            if (history.containsKey(lsh))
                                continue;
                            vo = new Wb01VO();
                            vo.setIsupdate("N");
                        }
                        vo.setAttributeValue("xh", xh);
                        vo.setAttributeValue("lsh", lsh);
                        vo.setAttributeValue("ch", jb.get("车号"));
                        vo.setAttributeValue("wbtype", jb.get("过磅类型"));
                        vo.setAttributeValue("fh_org", jb.get("发货单位"));
                        vo.setAttributeValue("sh_org", jb.get("收货单位"));
                        vo.setAttributeValue("hm", jb.get("货名"));
                        vo.setAttributeValue("gg", jb.get("规格"));
                        vo.setMz(new UFDouble(jb.get("毛重") == null ? "0" : jb.get("毛重").toString()));
                        vo.setPz(new UFDouble(jb.get("皮重") == null ? "0" : jb.get("皮重").toString()));
                        vo.setJz(new UFDouble(jb.get("净重") == null ? "0" : jb.get("净重").toString()));
                        vo.setKz(new UFDouble(jb.get("扣重") == null ? "0" : jb.get("扣重").toString()));
                        vo.setSz(new UFDouble(jb.get("实重") == null ? "0" : jb.get("实重").toString()));
                        vo.setDj(new UFDouble(jb.get("单价") == null ? "0" : jb.get("单价").toString()));
                        vo.setJe(new UFDouble(jb.get("金额") == null ? "0" : jb.get("金额").toString()));
                        vo.setZfxs(new UFDouble(jb.get("折方系数") == null ? "0" : jb.get("折方系数").toString()));
                        vo.setFl(new UFDouble(jb.get("方量") == null ? "0" : jb.get("方量").toString()));
                        vo.setGbf(new UFDouble(jb.get("过磅费") == null ? "0" : jb.get("过磅费").toString()));
                        vo.setAttributeValue("mz_psn", jb.get("毛重司磅员"));
                        vo.setAttributeValue("pz_psn", jb.get("皮重司磅员"));
                        vo.setAttributeValue("mz_no", jb.get("毛重磅号"));
                        vo.setAttributeValue("pz_no", jb.get("皮重磅号"));
                        if (jb.get("毛重时间") != null)
                            vo.setAttributeValue("mz_datetime", new UFDateTime(jb.get("毛重时间").toString().substring(0, 19)));
                        if (jb.get("皮重时间") != null)
                            vo.setAttributeValue("pz_datetime", new UFDateTime(jb.get("皮重时间").toString().substring(0, 19)));
                        if (jb.get("一次过磅时间") != null)
                            vo.setAttributeValue("first_datetime", new UFDateTime(jb.get("一次过磅时间").toString().substring(0, 19)));
                        if (jb.get("二次过磅时间") != null)
                            vo.setAttributeValue("second_datetime", new UFDateTime(jb.get("二次过磅时间").toString().substring(0, 19)));
                        vo.setAttributeValue("updater", jb.get("更新人"));
                        if (jb.get("更新时间") != null)
                            vo.setAttributeValue("updatetime", new UFDateTime(jb.get("更新时间").toString().substring(0, 19)));
                        if (jb.get("发货时间") != null)
                            vo.setAttributeValue("delivery_time", new UFDateTime(jb.get("发货时间").toString().substring(0, 19)));
                        vo.setAttributeValue("demo", jb.get("备注"));
                        vo.setAttributeValue("prints", jb.get("打印次数"));
                        if (jb.get("上传否") != null) {
                            vo.setAttributeValue("isupload", "true".equals(jb.get("上传否").toString()) ? "Y" : "N");
                        }
                        vo.setInfo_status("N");
                        vo.setDef01("N");
                        if (jb.get("是否更新") != null && "是".equals(jb.get("是否更新").toString()) && history.containsKey(lsh)) {
                            res_update.add(vo);
                        } else {//不是更新--插入的
                            res_insert.add(vo);
                        }

                    }

                    title = "成功！";
                    whether = "success";
                    information = "接口获取地磅数据成功！";

                } else {
                    title = "失败！";
                    whether = "danger";
                    information = "接口状态不正确，无法获取地磅数据！";

                }
                if (res_insert != null && res_insert.size() != 0) {
                    dao.insertVOList(res_insert);
                }
                ;
                if (res_update != null && res_update.size() != 0) {
                    dao.updateVOList(res_update);
                }
                ;
            } catch (Exception e) {
                title = "失败！";
                whether = "danger";
                information = "接口状态不正确，无法获取地磅数据！";
            }

        }
        Map<String, String> map = new HashMap<>();
        map.put("title", title);
        map.put("whether", whether);
        map.put("information", information);

        return JSONObject.fromObject(map);
    }

    public JSONObject tableToNcSalesOrder() {

        UFDate daytime = new UFDate();
        log_msg = daytime.toDate() + "开始生成销售订单。。。。。。";
        UFDouble price = UFDouble.ZERO_DBL;
        try {
            String sql = "select cuserid from sm_user where user_code = 'XS001'";
            String billmaker = (String) dao.executeQuery(sql, new ColumnProcessor());//默认制单人XS001
            //更新的数据
            ArrayList<Wb01VO> updatesalelist = getUpdateWeighbridgeInfo();
            for (Wb01VO wbvo : updatesalelist) {
                //根据流水号和序号查询对应的销售订单
                List oldsaleordervolist = (List) MDPersistenceService.lookupPersistenceQueryService()
                        .queryBillOfVOByCond(SaleOrderVO.class, " vdef1 = '" + wbvo.getXh() + "' "
                                + " and vdef2 = '" + wbvo.getLsh() + "' " + "and dr = 0 ", false);
                SaleOrderVO oldsaleordervo = (SaleOrderVO) oldsaleordervolist.get(0);
                SaleOrderHVO hvo = oldsaleordervo.getParentVO();
                // 客户
                String ccustomerid = matchCustomer(wbvo);
                hvo.setCcustomerid(ccustomerid);
                hvo.setCcustomervid(ccustomerid);
                hvo.setChreceivecustid(ccustomerid);
                hvo.setChreceivecustvid(ccustomerid);

                SaleOrderBVO bvo = oldsaleordervo.getChildrenVO()[0];
                bvo.setVbdef2(wbvo.getCh());
                // 地磅类型
                bvo.setVbdef3(wbvo.getWbtype());
                // 发货单位
                if (wbvo.getSh_org() != null)
                    bvo.setVbdef5(wbvo.getSh_org().toString());
                // 毛重
                if (wbvo.getMz() != null)
                    bvo.setVbdef6(wbvo.getMz().div(new UFDouble("1000"), 2).toString());
                // 皮重
                if (wbvo.getPz() != null)
                    bvo.setVbdef7(wbvo.getPz().div(new UFDouble("1000"), 2).toString());
                // 净重
                if (wbvo.getJz() != null)
                    bvo.setVbdef8(wbvo.getJz().div(new UFDouble("1000"), 2).toString());
                // 扣重
                if (wbvo.getKz() != null)
                    bvo.setVbdef9(wbvo.getKz().div(new UFDouble("1000"), 2).toString());
                // 实重
                if (wbvo.getSz() != null)
                    bvo.setVbdef10(wbvo.getSz().div(new UFDouble("1000"), 2).toString());
                // 毛重司磅员
                bvo.setVbdef11(wbvo.getMz_psn());
                // 毛重时间
                if (wbvo.getMz_datetime() != null)
                    bvo.setVbdef12(wbvo.getMz_datetime().toString());
                // 皮重司磅员
                bvo.setVbdef13(wbvo.getPz_psn());
                // 皮重时间
                if (wbvo.getPz_datetime() != null)
                    bvo.setVbdef14(wbvo.getPz_datetime().toString());
                // 更新时间
                if (wbvo.getUpdatetime() != null)
                    bvo.setVbdef17(wbvo.getPz_datetime().toString());
                // 发货时间
                if (wbvo.getDelivery_time() != null)
                    bvo.setVbdef18(wbvo.getDelivery_time().toString());
                dao.updateVO(hvo);
                dao.updateVO(bvo);
            }


            ArrayList<Wb01VO> wbvolist = getWeighbridgeInfo();
            log_msg += "称重信息中获取" + wbvolist.size() + "条数据,";
            SaleOrderVO[] sos = new SaleOrderVO[wbvolist.size()];
            int k = 0;
            log_msg += "开始拼装销售订单vo,";
            //插入的数据
            for (Wb01VO wbvo : wbvolist) {
                //SaleOrderBVO[] bvos = new SaleOrderBVO[bvoslist.size()];
                /**********************************表头************************************************/
                SaleOrderVO svo = new SaleOrderVO();
                SaleOrderHVO hvo = new SaleOrderHVO();
                // 获取单据号
                //IBillcodeManage iBillcodeManage = (IBillcodeManage) NCLocator.getInstance().lookup(IBillcodeManage.class.getName());
                //billcode = iBillcodeManage.getBillCode_RequiresNew("30","0001A3100000000004NA", "0001A2100000000027QM",hvo);
                // 序号
                hvo.setVdef1(wbvo.getXh() + "");
                // 流水号
                hvo.setVdef2(wbvo.getLsh());
                // 集团
                hvo.setPk_group("0001A3100000000004NA");
                sql = "select pk_org, pk_vid from org_orgs_v where code ='2001'";//东洋销售单元
                OrgVersionVO orgversionvo = (OrgVersionVO) dao.executeQuery(sql, new BeanProcessor(OrgVersionVO.class));
                // 销售组织
                hvo.setPk_org(orgversionvo.getPk_org()); //东洋销售单元
                // 销售组织版本
                hvo.setPk_org_v(orgversionvo.getPk_vid()); //东洋销售单元

                sql = "select pk_billtypeid from bd_billtype where  pk_billtypecode  = '30-Cxx-01'";
                String ctrantypeid = (String) dao.executeQuery(sql, new ColumnProcessor());
                // 订单类型
                hvo.setCtrantypeid(ctrantypeid);
                // 订单类型编码
                hvo.setVtrantypecode("30-Cxx-01");//东洋普通销售
                // 业务流程
                sql = "select pk_busitype from bd_busitype where busicode  = 'XS001'";//新的流程
                String pk_busitype = (String) dao.executeQuery(sql, new ColumnProcessor());
                hvo.setCbiztypeid(pk_busitype);
                // 单据编码
                //hvo.setVbillcode(billcode);
                // 单据日期
                if (wbvo.getPz_datetime() != null) {//设置为皮重日期
                    hvo.setDbilldate(wbvo.getPz_datetime().getDate());
                    hvo.setDmakedate(wbvo.getPz_datetime().getDate());
                } else {
                    hvo.setDbilldate(daytime);
                    hvo.setDmakedate(daytime);
                }

                // 客户
                String ccustomerid = matchCustomer(wbvo);
                hvo.setCcustomerid(ccustomerid);
                hvo.setCcustomervid(ccustomerid);
                hvo.setChreceivecustid(ccustomerid);
                hvo.setChreceivecustvid(ccustomerid);

                // 部门版本
                sql = "select pk_dept,pk_vid from org_dept_v where pk_org = (select pk_org from org_orgs where code = '2001' and isbusinessunit = 'Y')";
                DeptVersionVO deptvo = (DeptVersionVO) dao.executeQuery(sql, new BeanProcessor(DeptVersionVO.class));
                hvo.setCdeptvid(deptvo.getPk_vid());
                // 部门
                hvo.setCdeptid(deptvo.getPk_dept());
                // 单据类型
                hvo.setFstatusflag(1);
                // 流程类型
                hvo.setFpfstatusflag(-1);
                hvo.setBillmaker(billmaker);//默认制单人XS001
                hvo.setCreator(billmaker);

                /**********************************表体************************************************/
                // 净重 改为单位 吨 除1000
                UFDouble jz = wbvo.getJz().div(1000);
                SaleOrderBVO bvo = new SaleOrderBVO();
                //结算财务组织最新版本
                bvo.setCsettleorgid(orgversionvo.getPk_org());
                //结算财务组织
                bvo.setCsettleorgvid(orgversionvo.getPk_vid());
                // 物料
                bvo.setCmaterialid("1001A31000000005142V");
                // 物料编码
                bvo.setCmaterialvid("1001A31000000005142V");
                // 税码
                bvo.setCtaxcodeid("1001A31000000007IRDU");
                // 扣税类别
                bvo.setFtaxtypeflag(1);
                bvo.setNtaxrate(new UFDouble(13));
                //国家和购销类型
                bvo.setCrececountryid("0001Z010000000079UJJ");
                bvo.setCtaxcountryid("0001Z010000000079UJJ");
                bvo.setCsendcountryid("0001Z010000000079UJJ");
                bvo.setFbuysellflag(1);
                /*****************************表体各种数量和金额********************************************/
                // 数量
                bvo.setNastnum(new UFDouble(1));
                // 无税单价 默认值1
                bvo.setNqtorigprice(new UFDouble(1));
                // 报价单位数量
                bvo.setNqtunitnum(new UFDouble(1));
                // 含税单价 默认值1.13
                bvo.setNqtorigtaxprice(new UFDouble(1.13));
                // 无税单价 默认值1
                bvo.setNqtorigprice(new UFDouble(1));
                // 含税净价 数量*1.13
                bvo.setNqtorigtaxnetprc(new UFDouble(1.13));
                // 无税净价 数量*1
                bvo.setNqtorignetprice(new UFDouble(1));
                //
                bvo.setNorignetprice(new UFDouble(1));
                // 主本币无税净价
                bvo.setNnetprice(new UFDouble(1));
                // 本币无税金额
                bvo.setNmny(new UFDouble(1));
                // 本币价税合计
                bvo.setNtaxmny(new UFDouble(1.13));
                //本位币
                bvo.setCcurrencyid("1002Z0100000000001K1");
                //折本汇率
                bvo.setNexchangerate(new UFDouble(1));
                //发货库存组织最新版本
                bvo.setCsendstockorgid(orgversionvo.getPk_org());
                //发货库存组织
                bvo.setCsendstockorgvid(orgversionvo.getPk_vid());
                //应收组织
                bvo.setCarorgid(orgversionvo.getPk_org());
                bvo.setCarorgvid(orgversionvo.getPk_vid());

                /************************一些自定义项目 来自于地磅信息************************************/
                // 批次号
                //bvo.setVbdef1();
                // 车号
                bvo.setVbdef2(wbvo.getCh());
                // 地磅类型
                bvo.setVbdef3(wbvo.getWbtype());
                // 发货单位
                if (wbvo.getSh_org() != null)
                    bvo.setVbdef5(wbvo.getSh_org().toString());
                // 毛重
                if (wbvo.getMz() != null)
                    bvo.setVbdef6(wbvo.getMz().div(new UFDouble("1000"), 2).toString());
                // 皮重
                if (wbvo.getPz() != null)
                    bvo.setVbdef7(wbvo.getPz().div(new UFDouble("1000"), 2).toString());
                // 净重
                if (wbvo.getJz() != null)
                    bvo.setVbdef8(wbvo.getJz().div(new UFDouble("1000"), 2).toString());
                // 扣重
                if (wbvo.getKz() != null)
                    bvo.setVbdef9(wbvo.getKz().div(new UFDouble("1000"), 2).toString());
                // 实重
                if (wbvo.getSz() != null)
                    bvo.setVbdef10(wbvo.getSz().div(new UFDouble("1000"), 2).toString());
                // 毛重司磅员
                bvo.setVbdef11(wbvo.getMz_psn());
                // 毛重时间
                if (wbvo.getMz_datetime() != null)
                    bvo.setVbdef12(wbvo.getMz_datetime().toString());
                // 皮重司磅员
                bvo.setVbdef13(wbvo.getPz_psn());
                // 皮重时间
                if (wbvo.getPz_datetime() != null)
                    bvo.setVbdef14(wbvo.getPz_datetime().toString());
                // 更新时间
                if (wbvo.getUpdatetime() != null)
                    bvo.setVbdef17(wbvo.getPz_datetime().toString());
                // 发货时间
                if (wbvo.getDelivery_time() != null)
                    bvo.setVbdef18(wbvo.getDelivery_time().toString());
                svo.setParent(hvo);
                svo.setChildrenVO(new SaleOrderBVO[]{bvo});
                sos[k] = svo;
                k++;
            }
            if (sos.length != 0) {
                SaleOrderVO[] vos = NCLocator.getInstance().lookup(ISaleOrderMaintainAPI.class).insertBills(sos);
                List<SaleOrderHVO> hvos = new ArrayList<>();
                for (SaleOrderVO vo : vos) {
                    SaleOrderHVO newhvo = vo.getParentVO();
                    newhvo.setDbilldate(newhvo.getDmakedate());
                    newhvo.setBillmaker(billmaker);//默认制单人XS001
                    newhvo.setDr(0);
                    //更新表头单据日期
                    hvos.add(newhvo);
                }
                dao.updateVOList(hvos);
                //插入之后更新单据（更新单据日期和制单人）
                updateWeightInfo();
            }
            log_msg += "执行sql，生成" + sos.length + "条销售订单完成.";
            updatenum = String.valueOf(updatesalelist.size());
            insertnum = String.valueOf(sos.length);
            title = "成功！";
            whether = "success";
            information += "成功更新" + updatesalelist.size() + "笔订单，生成 " + sos.length + " 笔订单。";
        } catch (Exception e) {
            log_msg += "报错信息：" + e.getMessage();
            logVo.setMsg(log_msg);
            logVo.setOptype("saleorder");
            title = "失败！";
            whether = "danger";
            information += "报错信息：" + e.getMessage() + "";
            try {
                trans.begin();
                dao.insertVO(logVo);
                trans.commit();
            } catch (Exception e1) {
                log_msg += "报错信息：" + e1.getMessage();
                logVo.setMsg(log_msg);
                logVo.setOptype("saleorder");
                title = "失败！";
                whether = "danger";
                information += "报错信息：" + e1.getMessage() + "";
            }
        }
        if (!log_msg.contains("报错信息")) {
            logVo.setMsg(log_msg);
            logVo.setOptype("saleorder");
            try {
                dao.insertVO(logVo);
            } catch (DAOException e2) {
                log_msg += "报错信息：" + e2.getMessage();
                logVo.setMsg(log_msg);
                logVo.setOptype("saleorder");
                title = "失败！";
                whether = "danger";
                information += "报错信息：" + e2.getMessage() + "";
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("title", title);
        map.put("whether", whether);
        map.put("information", information);
        return JSONObject.fromObject(map);


    }

    private ArrayList<Wb01VO> getUpdateWeighbridgeInfo() {
        ArrayList<Wb01VO> list = new ArrayList<Wb01VO>();
        try {
            String[] array = this.monthBeginAndEnd();
            String beginBeforeDay = array[0];
            String endBeforeDay = array[1];
            String sql = " select * from weighbridge01 where def01 = 'Y' and isupdate='Y' "
                    + "and updatetime >= '" + beginBeforeDay + "' and updatetime < '" + endBeforeDay + "'";
            log_msg += "执行sql查出符合生成销售订单的地磅信息，sql：" + sql + ",";
            list = (ArrayList<Wb01VO>) dao.executeQuery(sql, new BeanListProcessor(Wb01VO.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private String matchCustomer(Wb01VO vo) {
        String sh_org_name = vo.getSh_org();
        try {
            String sql = "select pk_customer from bd_customer where name = '" + sh_org_name + "'";
            String str = (String) dao.executeQuery(sql, new ColumnProcessor());
            if (str != null)
                return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("抚顺市隆泰工贸有限公司".equals(sh_org_name)) {
            return "1001A310000000002LE6";
        }
        return "1001A310000000002LBW";
    }

    private ArrayList<Wb01VO> getWeighbridgeInfo() {
        ArrayList<Wb01VO> list = new ArrayList<Wb01VO>();
        try {
            String[] array = this.monthBeginAndEnd();
            String beginBeforeDay = array[0];
            String endBeforeDay = array[1];
            String sql = " select * from weighbridge01 where  def01 <> 'Y'"
                    + " and updatetime >= '" + beginBeforeDay + "' and updatetime < '" + endBeforeDay + "'";
            log_msg += "执行sql查出符合生成销售订单的地磅信息，sql：" + sql + ",";
            list = (ArrayList<Wb01VO>) dao.executeQuery(sql, new BeanListProcessor(Wb01VO.class));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private String[] monthBeginAndEnd() {
        //当月第一天和最后一天
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String beginBeforeDay = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()) + " 00:00:00";
        cal.roll(Calendar.DAY_OF_MONTH, -1);
        String endBeforeDay = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()) + " 23:59:59";
        String[] array = new String[]{beginBeforeDay, endBeforeDay};
        return array;
    }

    private void updateWeightInfo() {
        HashMap<String, ArrayList<Wb01VO>> res = new HashMap<String, ArrayList<Wb01VO>>();
        try {
            String[] array = this.monthBeginAndEnd();
            String beginBeforeDay = array[0];
            String endBeforeDay = array[1];
            String sql = " update weighbridge01 set def01 = 'Y' "
                    + "where updatetime >= '" + beginBeforeDay + "' and updatetime < '" + endBeforeDay + "' and def01 <> 'Y'";
            dao.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> loadSqlServerConfigFromXML() throws IOException, SAXException {
        //清空配置Map
        Map<String, String> sqlServerConfigMap = new HashMap<String, String>();
        //读取配置文件
//        String nchome = RuntimeEnv.getInstance().getProperty("nc.server.location");
//        String path = (new StringBuilder(String.valueOf(nchome))).append("/WeighBridgeToNCTaskConfig.xml").toString();
        String path = new StringBuilder(this.getClass().getResource("").getPath()).append("WeighBridgeToNCTaskConfig.xml").toString();
        File file = new File(path);
        String WebService = "";
        String FROMSQL = "";
        String startDate = "";
        String endDate = "";

        Document doc = XMLUtil.getDocumentBuilder().parse(file);
        Element e = doc.getDocumentElement();

        int webServiceFlag = 0;
        for (Iterator ite = XMLUtil.getElementsByTagName(e, "WebService"); ite.hasNext(); ) {
            Element sys = (Element) ite.next();
            WebService = sys.getTextContent();
            sqlServerConfigMap.put("WebService", WebService);
            webServiceFlag = 1;
            if (WebService.isEmpty()) {
                Logger.error(new String("配置文件中WebService值错误！"));
            }
        }
        if (webServiceFlag == 0) {
            Logger.error(new String("配置文件中WebService标签错误！"));
        }


        int fROMSQLFlag = 0;
        for (Iterator ite = XMLUtil.getElementsByTagName(e, "FROMSQL"); ite.hasNext(); ) {
            Element sys = (Element) ite.next();
            FROMSQL = sys.getTextContent();
            sqlServerConfigMap.put("FROMSQL", FROMSQL);
            fROMSQLFlag = 1;
            if (FROMSQL.isEmpty()) {
                Logger.error(new String("配置文件中FROMSQL值错误！"));
            }
        }
        if (fROMSQLFlag == 0) {
            Logger.error(new String("配置文件中FROMSQL标签错误！"));
        }

        for (Iterator ite = XMLUtil.getElementsByTagName(e, "StartDate"); ite.hasNext(); ) {
            Element sys = (Element) ite.next();
            startDate = sys.getTextContent();
            sqlServerConfigMap.put("startDate", startDate);
        }

        for (Iterator ite = XMLUtil.getElementsByTagName(e, "EndDate"); ite.hasNext(); ) {
            Element sys = (Element) ite.next();
            endDate = sys.getTextContent();
            sqlServerConfigMap.put("endDate", endDate);
        }


        return sqlServerConfigMap;
    }

}