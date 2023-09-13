//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package nccloud.pubimpl.ufoc.gbadjust.reportadjust;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.console.ActionHandler;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.func.excel.text.ImpExpFileNameUtil;
import com.ufsoft.iufo.inputplugin.biz.FontFactory;
import com.ufsoft.iufo.inputplugin.biz.data.ImportExcelDataBizUtil;
import com.ufsoft.iufo.inputplugin.biz.file.ChooseRepData;
import com.ufsoft.iuforeport.repdatainput.LoginEnvVO;
import com.ufsoft.iuforeport.repdatainput.TableInputHandlerHelper;
import com.ufsoft.iuforeport.tableinput.applet.IRepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.RepDataParam;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.ffw.asyndistribute.executor.AsynDistributeInvoker;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.uif2.LockFailedException;
import nc.impl.hbbb.adjustreport.AdjustReportSrvImpl;
import nc.impl.pubapp.pattern.database.IDQueryBuilder;
import nc.itf.hbbb.adjreport.IAdjReportService;
import nc.itf.hbbb.hbrepstru.HBRepStruUtil;
import nc.itf.hbbb.hbrepstru.IHBRepstruQrySrv;
import nc.itf.hbbb.hbscheme.IHBSchemeQrySrv;
import nc.itf.hbbb.vouch.IVouchQrySrv;
import nc.itf.iufo.data.IMeasurePubDataQuerySrv;
import nc.itf.iufo.report.IUfoeRepDataSrv;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.hbbb.exception.UFOCUnThrowableException;
import nc.pub.iufo.accperiod.AccPeriodSchemeUtil;
import nc.pub.iufo.cache.UFOCacheManager;
import nc.pub.iufo.exception.UFOSrvException;
import nc.pub.iufo.zip.ZipFileUtil;
import nc.pub.smart.util.SmartUtilities;
import nc.ui.iufo.dataexchange.RepDataWithCellsModelExport;
import nc.ui.iufo.dataexchange.TableDataToExcel;
import nc.ui.iufo.input.CSomeParam;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.util.hbbb.HBVersionUtil;
import nc.util.hbbb.MeasurePubDataUtil;
import nc.util.hbbb.gbadjust.HBRepAdjustUtil;
import nc.util.hbbb.hbscheme.HBSchemeSrvUtils;
import nc.util.hbbb.input.HBBBTableInputActionHandler;
import nc.util.hbbb.service.HBBaseDocItfService;
import nc.util.hbbb.workdraft.pub.IWorkDraft;
import nc.util.hbbb.workdraft.pub.ReportType;
import nc.util.hbbb.workdraft.pub.WorkDraftFactory;
import nc.vo.bd.pub.NODE_TYPE;
import nc.vo.bd.util.ExceptionUtils;
import nc.vo.corg.ReportCombineStruMemberWithCodeNameVO;
import nc.vo.ffw.businessparam.BusinessParamVO;
import nc.vo.ffw.para.DistributeResult;
import nc.vo.hbbb.adjustreport.AdjustRepExecCondVO;
import nc.vo.hbbb.adjustreport.AdjustRepExecVO;
import nc.vo.hbbb.adjustreport.DistributeExcVo;
import nc.vo.hbbb.adjustreport.IAdjustRepTypeContants;
import nc.vo.hbbb.adjustscheme.AdjustSchemeVO;
import nc.vo.hbbb.hbscheme.HBSchemeVO;
import nc.vo.iufo.data.MeasureDataUtil;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.keydef.DisplayVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.pub.date.UFODate;
import nc.vo.iufo.query.IUfoDetailQueryCondVO;
import nc.vo.iufo.query.IUfoQueryCondVO;
import nc.vo.iufo.repdataquery.RepDataQueryResultVO;
import nc.vo.iuforeport.rep.ReportVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.uif2.LoginContext;
import nc.vo.util.BDPKLockUtil;
import nc.vo.vorg.ReportCombineStruVersionVO;
import nccloud.dto.ufoc.datacenter.HBRepDataResultVO;
import nccloud.dto.ufoc.datacenter.HBRepQueryParamsVO;
import nccloud.dto.ufoc.datacenter.UfocRepDataParam;
import nccloud.dto.ufoe.exportinfo.ExportInfoVO;
import nccloud.dto.ufoe.support.base.KeyRefInfoVO;
import nccloud.pub.ufoc.common.consts.HBReportColumnsConsts;
import nccloud.pub.ufoe.common.consts.DataCenterUtils;
import nccloud.pub.ufoe.common.consts.ReportUtils;
import nccloud.pubitf.baseapp.apprbac.IAppAndOrgPermQueryPubService;
import nccloud.pubitf.ufoc.datacenter.reportdata.IHBDataCenterService4Cloud;
import nccloud.pubitf.ufoc.gbadjust.reportadjust.IReportAdjustService4Cloud;
import nccloud.pubitf.ufoc.gbadjust.reportadjust.utils.HBReportQueryUtil;
import nccloud.pubitf.ufoc.gbadjust.reportadjust.vo.ConditonVo;
import nccloud.pubitf.ufoc.gbadjust.reportadjust.vo.ExportListQueryVO;
import nccloud.pubitf.ufoc.gbadjust.reportadjust.vo.ExportQueryVO;
import nccloud.pubitf.ufoc.gbadjust.reportadjust.vo.UFOCQueryTreeFormatVO;
import nccloud.pubitf.ufoe.datacenter.mailrule.IMailRuleService4Cloud;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReportAdjustService4CloudImpl implements IReportAdjustService4Cloud {
    public ReportAdjustService4CloudImpl() {
    }

    public ArrayList<ArrayList<ReportCombineStruMemberWithCodeNameVO>> reportCombineStruMember(String date, String pkHbScheme, String appCode) throws BusinessException {
        if (date != null) {
            ArrayList<ArrayList<ReportCombineStruMemberWithCodeNameVO>> result = new ArrayList();
            ReportCombineStruVersionVO hbRepStruVO = getHBRepStruVO(date, pkHbScheme);
            String pkVid = hbRepStruVO.getPk_vid();
            HBSchemeVO hbScheme = HBSchemeSrvUtils.getHBSchemeByHBSchemeId(pkHbScheme);
            String pkGroup = hbScheme.getPk_group();
            if (pkVid != null) {
                ReportCombineStruMemberWithCodeNameVO struNode = new ReportCombineStruMemberWithCodeNameVO();
                struNode.setName(hbRepStruVO.getName() + "(" + hbRepStruVO.getVname() + ")");
                struNode.setPk_org(hbRepStruVO.getPk_reportcombinestru());
                struNode.setInnercode("root");
                struNode.setPk_rcs(pkVid);
                ArrayList<ReportCombineStruMemberWithCodeNameVO> root = new ArrayList();
                root.add(struNode);
                result.add(root);
                NODE_TYPE nodeTye = NODE_TYPE.ORG_NODE;
                LoginContext context = new LoginContext();
                context.setNodeType(nodeTye);
                context.setPk_group(pkGroup);

                try {
                    String pk_group = InvocationInfoProxy.getInstance().getGroupId();
                    String cuserId = InvocationInfoProxy.getInstance().getUserId();
                    OrgVO[] orgVOS = ((IAppAndOrgPermQueryPubService)NCLocator.getInstance().lookup(IAppAndOrgPermQueryPubService.class)).queryUserPermOrgByApp(cuserId, appCode, pk_group);
                    if (orgVOS != null) {
                        ReportCombineStruMemberWithCodeNameVO[] orgs = ((IHBRepstruQrySrv)NCLocator.getInstance().lookup(IHBRepstruQrySrv.class)).queryReportCombineStruMemberVOWithCodeNameByVersionId(pkVid, context);
                        Set<String> filter = (Set)Arrays.stream(orgVOS).map(OrgVO::getPk_org).collect(Collectors.toSet());
                        List<ReportCombineStruMemberWithCodeNameVO> lstMemberVO = (List)Arrays.stream(orgs).filter((org) -> {
                            return !filter.add(org.getPk_org());
                        }).collect(Collectors.toList());
                        result.add((ArrayList)lstMemberVO);
                    }

                    return result;
                } catch (BusinessException var17) {
                    throw new BusinessException(var17.getMessage());
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Map<String, String> delReport(List<RepDataQueryResultVO> repDatas) {
        StringBuilder delResul = new StringBuilder();
        int falseDel = 0;
        String code = "success";

        for(int i = 0; i < repDatas.size(); ++i) {
            try {
                ((IAdjReportService)NCLocator.getInstance().lookup(IAdjReportService.class)).delAdjReport_RequiresNew((RepDataQueryResultVO)repDatas.get(i));
            } catch (Exception var8) {
                String orgName = this.getOrgNameByPkOrg(((RepDataQueryResultVO)repDatas.get(i)).getPk_org());
                if (!code.equals("fail")) {
                    code = "fail";
                }

                ++falseDel;
                delResul.append(String.format(NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0109"), orgName, var8.getMessage()));
            }
        }

        Map<String, String> result = new HashMap();
        result.put("msgCode", code);
        String message = null;
        if (falseDel == 0 && repDatas.size() == 1) {
            message = NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0103");
        } else if (falseDel == 0 && repDatas.size() > 1) {
            message = String.format(NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0104"), repDatas.size());
        } else if (falseDel == repDatas.size() && repDatas.size() == 1) {
            message = delResul.toString();
        } else {
            message = String.format(NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0000") + "," + NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0002") + "\n%s", repDatas.size() - falseDel, falseDel, delResul);
        }

        result.put("message", message);
        return result;
    }

    public Map<String, String> HBdelReport(List<RepDataQueryResultVO> repDatas) throws BusinessException {
        StringBuilder delResul = new StringBuilder();
        int falseDel = 0;

        for(int i = 0; i < repDatas.size(); ++i) {
            try {
                ((IAdjReportService)NCLocator.getInstance().lookup(IAdjReportService.class)).delAdjReport_RequiresNew((RepDataQueryResultVO)repDatas.get(i));
            } catch (Exception var7) {
                String orgName = this.getOrgNameByPkOrg(((RepDataQueryResultVO)repDatas.get(i)).getPk_org());
                ++falseDel;
                delResul.append(String.format(NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0109"), orgName, var7.getMessage()));
            }
        }

        Map<String, String> result = new HashMap();
        String message = null;
        if (falseDel == 0 && repDatas.size() == 1) {
            message = NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0103");
        } else {
            if (falseDel != 0 || repDatas.size() <= 1) {
                if (falseDel == repDatas.size() && repDatas.size() == 1) {
                    message = delResul.toString();
                    throw new BusinessException(message);
                }

                message = String.format(NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0000") + "," + NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0002") + "\n%s", repDatas.size() - falseDel, falseDel, delResul);
                throw new BusinessException(message);
            }

            message = String.format(NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0104"), repDatas.size());
        }

        result.put("message", message);
        return result;
    }

    public ExportInfoVO assemFileWeb(ExportListQueryVO queryList) throws Exception {
        List<ExportInfoVO> results = new ArrayList();
        Iterator var3 = queryList.getParams().iterator();

        while(var3.hasNext()) {
            ExportQueryVO query = (ExportQueryVO)var3.next();
            String filepath = query.getFilePath();
            ExportInfoVO result = new ExportInfoVO();
            result.setByteArray(this.getCell(query, filepath));
            result.setFileName(filepath);
            results.add(result);
        }

        return results.size() != 1 ? ZipFileUtil.zipFile((ExportInfoVO[])results.toArray(new ExportInfoVO[0])) : (ExportInfoVO)results.get(0);
    }

    private byte[] getCell(ExportQueryVO query, String filepath) throws Exception {
        IHBDataCenterService4Cloud service = (IHBDataCenterService4Cloud)NCLocator.getInstance().lookup(IHBDataCenterService4Cloud.class);
        List<HBRepDataResultVO> voList = new ArrayList();

        for(int i = 0; i < query.getPkreport().size(); ++i) {
            HBRepQueryParamsVO hbRepQueryParamsVO = this.initCellModelQueryParam(query, i);
            voList.add(service.executeLoad(hbRepQueryParamsVO));
        }

        return this.Export2ExcelWithShowZero(voList, query.getRmspk(), filepath);
    }

    public Object executeImport(byte[] bytes, RepDataParam param, boolean isAutoCal, String endRow, boolean isXlsxFile) throws Exception {
        Workbook workBook = null;

        try {
            if (isXlsxFile) {
                workBook = new XSSFWorkbook(new ByteArrayInputStream(bytes));
            } else {
                workBook = new HSSFWorkbook(new ByteArrayInputStream(bytes));
            }
        } catch (Exception var17) {
            AppDebug.debug(var17);
            throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0057"));
        }

        int sheetNumber = ((Workbook)workBook).getNumberOfSheets();
        if (sheetNumber <= 0) {
            throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0058"));
        } else if (sheetNumber > 1) {
            throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0059"));
        } else {
            Hashtable<String, Object> matchMap = null;
            ChooseRepData[] chooseRepDatas = (ChooseRepData[])((ChooseRepData[])ActionHandler.execWithZip(HBBBTableInputActionHandler.class.getName(), "loadTableImportReps", param));
            String strCurRepPK = param.getReportPK();
            String[] sheetNames = new String[]{((Workbook)workBook).getSheetName(0)};
            matchMap = ImportExcelDataBizUtil.doGetAutoMatchMap(chooseRepDatas, sheetNames, strCurRepPK);
            ImportExcelDataBizUtil.checkMatchMap(matchMap);
            ChooseRepData chooseRepData = ImportExcelDataBizUtil.getCurRepData(chooseRepDatas, strCurRepPK);
            param.getTaskPK();
            if (chooseRepData == null) {
                throw new nc.bs.framework.json.core.exception.BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("1820pub_0", "01820006-0155"));
            } else {
                List<String[]> m_listResultArray = new ArrayList();
                String[] strInfos = new String[]{sheetNames[0], chooseRepData.getReportCode(), StringUtils.isEmpty(endRow) ? "-1" : endRow};
                if (strInfos[1] != null) {
                    m_listResultArray.add(strInfos);
                }

                List<Object[]> listImportInfos = ImportExcelDataBizUtil.getImportInfos(m_listResultArray, (Workbook)workBook, (IContext)null);
                if (listImportInfos == null) {
                    return null;
                } else {
                    LoginEnvVO loginEnv = DataCenterUtils.getLoginEnvInfo(param.getRepOrgPK(), ReportUtils.getRMSPK(param));
                    return ActionHandler.execWithZip(HBBBTableInputActionHandler.class.getName(), "importExcelData", new Object[]{param, loginEnv, listImportInfos, isAutoCal});
                }
            }
        }
    }

    public byte[] Export2ExcelWithShowZero(List<HBRepDataResultVO> resultVO, String rmspk, String filepath) throws Exception {
        boolean bShowZero = true;
        Workbook workBook = null;
        if (ImpExpFileNameUtil.isExcel2003(filepath)) {
            workBook = new HSSFWorkbook();
        } else if (ImpExpFileNameUtil.isExcel2007(filepath)) {
            workBook = new SXSSFWorkbook(500);
        }

        if (workBook == null) {
            throw new BusinessException("workBook error");
        } else {
            Iterator var6 = resultVO.iterator();

            while(var6.hasNext()) {
                HBRepDataResultVO result = (HBRepDataResultVO)var6.next();
                IRepDataParam param = result.getRepDataParam();
                LoginEnvVO loginEnv = DataCenterUtils.getLoginEnvInfo(param.getRepOrgPK(), rmspk);
                String strAloneID4ExportExcel = param.getAloneID();
                UfoContextVO context = TableInputHandlerHelper.getContextVO(param, loginEnv);
                RepDataWithCellsModelExport exportObj = new RepDataWithCellsModelExport(context, result.getCellsModel());
                String strReportPK4ExportExcel = param.getReportPK();
                CSomeParam cSomeParam = new CSomeParam();
                cSomeParam.setAloneId(strAloneID4ExportExcel);
                cSomeParam.setRepId(strReportPK4ExportExcel);
                cSomeParam.setUserID(param.getOperUserPK());
                MeasurePubDataVO pubData = param.getPubData();
                cSomeParam.setUnitId(pubData.getUnitPK());
                exportObj.setParam(cSomeParam);
                exportObj.setLoginDate(loginEnv.getCurLoginDate());
                exportObj.setShowZero(bShowZero);
                FontFactory fontFactory = new FontFactory((Workbook)workBook);
                exportObj.setSheetName(this.HBReportNameByPk(result.getRepDataParam().getReportPK()));
                TableDataToExcel.translate(exportObj, (Workbook)workBook, fontFactory);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ((Workbook)workBook).write(outputStream);
            outputStream.flush();
            byte[] bytes = outputStream.toByteArray();
            outputStream.close();
            return bytes;
        }
    }

    public HBRepQueryParamsVO initCellModelQueryParam(ExportQueryVO query, int index) throws Exception {
        HBRepQueryParamsVO queryParam = new HBRepQueryParamsVO();
        queryParam.setUfocRepDataParam(new UfocRepDataParam());
        String pk_hbscheme = query.getPk_hbscheme();
        HBSchemeVO hbSchemeVO = HBSchemeSrvUtils.getHBSchemeByHBSchemeId(pk_hbscheme);
        KeyGroupVO keyGroup = UFOCacheManager.getSingleton().getKeyGroupCache().getByPK(hbSchemeVO.getPk_keygroup());
        AdjustSchemeVO adjustScheme = this.getAdjustSchemeByHbSchemePk(pk_hbscheme);
        IWorkDraft workDraft = query.getWorkDraft() == null ? this.InitWorkDraftByReportType(query.getType(), (String)query.getPkreport().get(index), hbSchemeVO, adjustScheme) : query.getWorkDraft();
        this.setMeasurePubDataInfo(query, keyGroup, queryParam.getUfocRepDataParam(), hbSchemeVO.getPk_accperiodscheme());
        KeyRefInfoVO[] keyRefInfoVOS = ((IMailRuleService4Cloud)NCLocator.getInstance().lookup(IMailRuleService4Cloud.class)).queryKeyWordsRefInfoVOByKeygroupPk(keyGroup.getKeyGroupPK());
        KeyRefInfoVO[] var10 = keyRefInfoVOS;
        int var11 = keyRefInfoVOS.length;

        for(int var12 = 0; var12 < var11; ++var12) {
            KeyRefInfoVO key = var10[var12];
            DisplayVO displayVO = new DisplayVO();
            if (key.getPk_keyword().equals("00000000000000000000")) {
                displayVO.setValue(query.getOrgpk());
            } else {
                displayVO.setValue((String)query.getKeywords().get(key.getCode()));
            }

            key.setM_strKeyVal(displayVO);
        }

        queryParam.setM_gVec(keyRefInfoVOS);
        queryParam.getUfocRepDataParam().setReportPK((String)query.getPkreport().get(index));
        queryParam.getUfocRepDataParam().setTaskPK(pk_hbscheme);
        queryParam.getUfocRepDataParam().setWorkDraft(workDraft);
        queryParam.getUfocRepDataParam().setDataCenterType(query.getDataCenterType());
        if (query.getSurrender() != null) {
            queryParam.setSurrender(query.getSurrender());
        }

        this.setRepVerAndAloneIdByDraft(workDraft, queryParam.getUfocRepDataParam());
        queryParam.setRepDataParam(queryParam.getUfocRepDataParam());
        return queryParam;
    }

    private IWorkDraft InitWorkDraftByReportType(String type, String pk_report, HBSchemeVO hbSchemeVO, AdjustSchemeVO adjustScheme) {
        IWorkDraft workDraft = null;
        switch (type) {
            case "HB_ADJ":
                workDraft = WorkDraftFactory.getWorkDraft(pk_report, ReportType.HB_ADJ, "", hbSchemeVO, adjustScheme);
                break;
            case "HB_ADJ_DRAFT":
                workDraft = WorkDraftFactory.getWorkDraft(pk_report, ReportType.HB_ADJ_DRAFT, "", hbSchemeVO);
                break;
            case "HB":
                workDraft = WorkDraftFactory.getWorkDraft(pk_report, ReportType.HB, "", hbSchemeVO);
                break;
            case "SEP_ADJ":
                workDraft = WorkDraftFactory.getWorkDraft(pk_report, ReportType.SEP_ADJ, "", hbSchemeVO, adjustScheme);
                break;
            case "SEP_ADJ_DRAFT":
                workDraft = WorkDraftFactory.getWorkDraft(pk_report, ReportType.SEP_ADJ_DRAFT, "", hbSchemeVO);
                break;
            case "SEP":
                workDraft = WorkDraftFactory.getWorkDraft(pk_report, ReportType.SEP, "", hbSchemeVO);
                break;
            case "HB_CONTRAST":
                workDraft = WorkDraftFactory.getWorkDraft(pk_report, ReportType.HB_CONTRAST, "", hbSchemeVO);
                break;
            case "HB_DRAFT":
                workDraft = WorkDraftFactory.getWorkDraft(pk_report, ReportType.HB_DRAFT, "", hbSchemeVO);
        }

        return workDraft;
    }

    private void setMeasurePubDataInfo(ExportQueryVO query, KeyGroupVO keyGroup, RepDataParam repDataParam, String accSchemePk) throws UFOSrvException {
        MeasurePubDataVO measurePubDataVO = new MeasurePubDataVO();
        HashMap<String, String> keyValues = query.getKeywords();
        List<String> keywords = new ArrayList();
        keyValues.forEach((key, value) -> {
            keywords.add(value);
        });
        measurePubDataVO.setKType(keyGroup.getKeyGroupPK());
        measurePubDataVO.setKeyGroup(keyGroup);
        measurePubDataVO.setAccSchemePK(accSchemePk);
        measurePubDataVO.setKeywords((String[])keywords.toArray(new String[0]));
        ((IMeasurePubDataQuerySrv)NCLocator.getInstance().lookup(IMeasurePubDataQuerySrv.class)).findByKeywords(measurePubDataVO);
        repDataParam.setPubData(measurePubDataVO);
    }

    private void setRepVerAndAloneIdByDraft(IWorkDraft workDraft, RepDataParam repDataParam) {
        MeasurePubDataVO pubData = repDataParam.getPubData();
        if (pubData != null) {
            try {
                HBSchemeVO hbScheme = workDraft.getHbSchemevo();
                if (workDraft.getReporttype().equals(ReportType.HB)) {
                    pubData.setVer(hbScheme.getVersion());
                } else if (workDraft.getReporttype().equals(ReportType.SEP_ADJ)) {
                    pubData.setVer(workDraft.getAdjVersion());
                } else if (workDraft.getReporttype().equals(ReportType.HB_CONTRAST)) {
                    pubData.setVer(HBVersionUtil.getHBContrastByHBSchemeVO(hbScheme));
                } else if (workDraft.getReporttype().equals(ReportType.HBDIFF)) {
                    pubData.setVer(HBVersionUtil.getDiffByHBSchemeVO(hbScheme));
                } else {
                    pubData.setVer(workDraft.getVersion(hbScheme, 0));
                }

                pubData.setAloneID((String)null);
                boolean isAllKeyData = true;
                String[] keysvalues = pubData.getKeywords();
                if (keysvalues != null && keysvalues.length > 0) {
                    String[] var7 = keysvalues;
                    int var8 = keysvalues.length;

                    for(int var9 = 0; var9 < var8; ++var9) {
                        String s = var7[var9];
                        if (s == null) {
                            isAllKeyData = false;
                            break;
                        }
                    }
                }

                if (isAllKeyData) {
                    pubData.setAloneID(MeasureDataUtil.getAloneID(pubData));
                }

                repDataParam.setAloneID(pubData.getAloneID());
            } catch (Exception var11) {
                Logger.error(var11.getMessage(), var11);
            }
        } else {
            repDataParam.setAloneID((String)null);
        }

    }

    public static ReportCombineStruVersionVO getHBRepStruVO(String date, String pkHbScheme) throws BusinessException {
        HBSchemeVO hbSchemeVO = HBSchemeSrvUtils.getHBSchemeByHBSchemeId(pkHbScheme);
        if (hbSchemeVO.getPk_hbscheme().equals("null")) {
            throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0060"));
        } else {
            KeyGroupVO keyGroup = UFOCacheManager.getSingleton().getKeyGroupCache().getByPK(hbSchemeVO.getPk_keygroup());
            if (keyGroup == null) {
                throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0061"));
            } else {
                String pk_hbrepstru = hbSchemeVO.getPk_repmanastru();
                String strPeriodKeyPK = null;
                String strAccPeriodPK = hbSchemeVO.getPk_accperiodscheme();
                String busi_date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(InvocationInfoProxy.getInstance().getBizDateTime()));
                if (date.equals(busi_date.substring(0, 10))) {
                    date = busi_date;
                }

                Boolean isAcc = false;
                KeyVO keyVO = null;
                Iterator var10 = keyGroup.getVecKeys().iterator();

                while(var10.hasNext()) {
                    KeyVO key = (KeyVO)var10.next();
                    if (key.isAccPeriodKey()) {
                        isAcc = true;
                        strPeriodKeyPK = key.getPk_keyword();
                        keyVO = key;
                    }
                }

                if (isAcc) {
                    String strValue;
                    if (date.length() > 10 && keyVO != null) {
                        strValue = getCurLoginTTimeValue(date, (String)null, (String)null, keyVO, strAccPeriodPK, keyGroup);
                        if (nccloud.commons.lang.StringUtils.isNotEmpty(strValue)) {
                            date = strValue;
                        }
                    }

                    strValue = keyVO.getAccPeriodKeyIndex() == 0 ? date.substring(0, 4) : date.substring(0, 7);
                    String[] startEnd = AccPeriodSchemeUtil.getInstance().getNatDateByAccPeriod(strAccPeriodPK, strPeriodKeyPK, strValue);
                    if (startEnd != null) {
                        return HBRepAdjustUtil.getHBRepStruVOByDate(startEnd[1], pk_hbrepstru);
                    } else {
                        return new ReportCombineStruVersionVO();
                    }
                } else {
                    return HBRepAdjustUtil.getHBRepStruVOByDate(date, pk_hbrepstru);
                }
            }
        }
    }

    public String[] queryRepDataByCondAndTypePks(UFOCQueryTreeFormatVO queryInfo) throws BusinessException {
        IUfoQueryCondVO queryCond = new IUfoQueryCondVO();
        HBReportColumnsConsts queryConfig = new HBReportColumnsConsts();
        this.initQueryParam(queryInfo, queryCond);
        String reptype = (String)queryInfo.getUserdefObj().get("repType");
        return this.getRepDataPks(queryCond, queryConfig.getShowColumns(), reptype, (String[])null);
    }

    public List<RepDataQueryResultVO> queryRepDataByCondAndType(UFOCQueryTreeFormatVO queryInfo, String[] report_pks_aloneId) throws BusinessException {
        IUfoQueryCondVO queryCond = new IUfoQueryCondVO();
        HBReportColumnsConsts queryConfig = new HBReportColumnsConsts();
        this.initQueryParam(queryInfo, queryCond);
        String reptype = (String)queryInfo.getUserdefObj().get("repType");
        List<RepDataQueryResultVO> resultVOS = this.queryRepDataByCondAndType(queryCond, queryConfig.getShowColumns(), reptype, report_pks_aloneId);
        List<String> reports = (List)Arrays.stream(report_pks_aloneId).collect(Collectors.toList());
        RepDataQueryResultVO[] results = new RepDataQueryResultVO[reports.size()];
        Iterator var9 = resultVOS.iterator();

        while(var9.hasNext()) {
            RepDataQueryResultVO resultVO = (RepDataQueryResultVO)var9.next();
            int index = reports.indexOf(resultVO.getPk_report() + "," + resultVO.getAlone_id());
            if (index != -1) {
                results[index] = resultVO;
            }
        }

        return (List)Arrays.stream(results).collect(Collectors.toList());
    }

    private void initQueryParam(UFOCQueryTreeFormatVO queryInfo, IUfoQueryCondVO queryCond) throws BusinessException {
        List<ConditonVo> conditions = queryInfo.getConditions();
        String pk_hbScheme = null;
        String date = null;
        queryCond.setPk_mainOrg((String)queryInfo.getUserdefObj().get("selectOrg"));
        String[] orgpks = (String[])((String[])((ArrayList)queryInfo.getUserdefObj().get("orgs")).toArray(new String[0]));
        Iterator var7 = conditions.iterator();

        while(var7.hasNext()) {
            ConditonVo con = (ConditonVo)var7.next();
            String pk = con.getPk_keyword();
            String value = con.getValue().getFirstvalue();
            if (con.getField().equals("pk_hbscheme")) {
                pk_hbScheme = value;
                queryCond.setKeyVal(pk, queryCond.getPk_mainOrg());
            } else if (KeyVO.isTTimeKey(pk)) {
                queryCond.setDate(con.getDisplay());
            } else {
                queryCond.setKeyVal(pk, value);
            }
        }

        HBSchemeVO hbSchemeVO = HBSchemeSrvUtils.getHBSchemeByHBSchemeId(pk_hbScheme);
        if (hbSchemeVO.getPk_hbscheme() == null) {
            throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0062"));
        } else {
            queryCond.setPk_task(pk_hbScheme);
            queryCond.setKeyGroupPK(hbSchemeVO.getPk_keygroup());
            queryCond.setSelectedOrgPKs(orgpks);
            queryCond.setPk_rms((String)queryInfo.getUserdefObj().get("pkrms"));
            int repcommitstate;
            int repcheckstate;
            if (queryInfo.getUserdefObj().get("repType").equals("HB")) {
                repcommitstate = Integer.parseInt((String)queryInfo.getUserdefObj().get("repcommitstate"));
                if (repcommitstate != -1) {
                    ((IUfoDetailQueryCondVO)queryCond.getDetailcond()).setRepCommitState(repcommitstate);
                }

                repcheckstate = Integer.parseInt((String)queryInfo.getUserdefObj().get("repcheckstate"));
                if (repcheckstate != -1) {
                    ((IUfoDetailQueryCondVO)queryCond.getDetailcond()).setRepCheckState(repcheckstate);
                }

                int inputstate = Integer.parseInt((String)queryInfo.getUserdefObj().get("inputstate"));
                if (inputstate != -1) {
                    ((IUfoDetailQueryCondVO)queryCond.getDetailcond()).setInputState(inputstate);
                }
            } else {
                repcommitstate = Integer.parseInt((String)queryInfo.getUserdefObj().get("report"));
                if (repcommitstate != 20) {
                    ((IUfoDetailQueryCondVO)queryCond.getDetailcond()).setRepCommitState(repcommitstate);
                }

                repcheckstate = Integer.parseInt((String)queryInfo.getUserdefObj().get("approve"));
                if (repcheckstate != 0) {
                    ((IUfoDetailQueryCondVO)queryCond.getDetailcond()).setRepCheckState(repcheckstate);
                }
            }

        }
    }

    public List<RepDataQueryResultVO> queryRepDataByCondAndType(IUfoQueryCondVO queryCond, String[] showColumns, String repType, String[] report_pks_aloneId) throws UFOSrvException {
        try {
            List<RepDataQueryResultVO> lst = new ArrayList();
            List<String> repDataQuerySQL = this.getRepDataQuerySQL(queryCond, showColumns, repType, report_pks_aloneId);
            Iterator var7 = repDataQuerySQL.iterator();

            while(var7.hasNext()) {
                String strSQL = (String)var7.next();
                if (strSQL == null || strSQL.length() == 0) {
                    return null;
                }

                BaseDAO dao = new BaseDAO();
                List<RepDataQueryResultVO> hblst = (List)dao.executeQuery(strSQL, new BeanListProcessor(RepDataQueryResultVO.class));
                lst.addAll(hblst);
            }

            return lst;
        } catch (Exception var11) {
            AppDebug.debug(var11);
            throw new UFOSrvException(var11.getMessage(), var11);
        }
    }

    public String[] getRepDataPks(IUfoQueryCondVO queryCond, String[] showColumns, String repType, String[] reportPks) throws UFOSrvException {
        try {
            ArrayList<RepDataQueryResultVO> hblst = new ArrayList();
            List<String> repDataQuerySQL = this.getRepDataQuerySQL(queryCond, showColumns, repType, reportPks);
            if (repDataQuerySQL == null) {
                return null;
            } else {
                Iterator var7 = repDataQuerySQL.iterator();

                while(true) {
                    String sql;
                    do {
                        if (!var7.hasNext()) {
                            return (String[])hblst.stream().map((id) -> {
                                return id.getPk_report() + "," + id.getAlone_id();
                            }).toArray((x$0) -> {
                                return new String[x$0];
                            });
                        }

                        sql = (String)var7.next();
                    } while(sql.isEmpty());

                    String dbType = String.valueOf(SmartUtilities.getDbType(InvocationInfoProxy.getInstance().getUserDataSource()));
                    String strSQL;
                    switch (dbType.toLowerCase()) {
                        case "sqlserver":
                            strSQL = "SELECT pk_report , ALONE_ID,pk_org from(" + sql + ") " + dbType;
                            break;
                        default:
                            strSQL = "SELECT pk_report , ALONE_ID,pk_org from(" + sql + ")";
                    }

                    if (strSQL == null || strSQL.length() == 0) {
                        return null;
                    }

                    BaseDAO dao = new BaseDAO();
                    hblst.addAll((List)dao.executeQuery(strSQL, new BeanListProcessor(RepDataQueryResultVO.class)));
                }
            }
        } catch (Exception var13) {
            AppDebug.debug(var13);
            throw new UFOSrvException(var13.getMessage(), var13);
        }
    }

    public List<String> getRepDataQuerySQL(IUfoQueryCondVO queryCond, String[] showColumns, String repType, String[] report_pks_aloneId) throws Exception {
        List<String> sql = new ArrayList();
        if (queryCond.getPk_task() == null) {
            return null;
        } else {
            Set<String> vShowColumn = new HashSet(Arrays.asList(showColumns));
            HBSchemeVO hbSchemeVO = HBSchemeSrvUtils.getHBSchemeByHBSchemeId(queryCond.getPk_task());
            Integer ver = this.getVersionByRepType(hbSchemeVO, repType);
            if (ver == null) {
                return null;
            } else {
                boolean bContainIntrRep = true;
                if ("SEPADJ".equals(repType) || "HBADJ".equals(repType)) {
                    queryCond.setInputState(1);
                    bContainIntrRep = false;
                }

                Map<String, UFBoolean> isLeaf = null;
                Map<String, UFBoolean> isBalance = null;
                if ("HB".equals(repType)) {
                    KeyGroupVO keyGroup = UFOCacheManager.getSingleton().getKeyGroupCache().getByPK(hbSchemeVO.getPk_keygroup());
                    String pk_period_keyword = null;
                    if (keyGroup.getTTimeKey() != null) {
                        pk_period_keyword = keyGroup.getTTimeKey().getPk_keyword();
                    }

                    ReportCombineStruVersionVO versionVO = null;
                    if (KeyVO.isAccPeriodKey(pk_period_keyword)) {
                        versionVO = HBRepStruUtil.getHBStruVersionVO(hbSchemeVO.getPk_accperiodscheme(), pk_period_keyword, queryCond.getDate(), hbSchemeVO.getPk_repmanastru());
                    } else {
                        versionVO = HBRepStruUtil.getHBStruVersionVO(queryCond.getDate(), hbSchemeVO.getPk_repmanastru());
                    }

                    String[] pk_orgs = queryCond.getSelectedOrgPKs();
                    isLeaf = HBBaseDocItfService.getRemoteHBRepStru().isLeafMembers(pk_orgs, versionVO.getPk_vid());
                    isBalance = HBBaseDocItfService.getRemoteHBRepStru().isBalanceUnits(pk_orgs, versionVO.getPk_vid());
                    List<String> pk_orgList = new ArrayList();
                    int i;
                    if (isBalance != null && isBalance.size() > 0) {
                        ver = HBVersionUtil.getDiffByHBSchemeVO(hbSchemeVO);

                        for(i = 0; i < pk_orgs.length; ++i) {
                            if (isBalance.get(pk_orgs[i]) != null && ((UFBoolean)isBalance.get(pk_orgs[i])).booleanValue()) {
                                pk_orgList.add(pk_orgs[i]);
                            }
                        }

                        if (pk_orgList != null && pk_orgList.size() > 0) {
                            queryCond.setSelectedOrgPKs((String[])pk_orgList.toArray(new String[pk_orgList.size()]));
                        } else {
                            queryCond.setSelectedOrgPKs(new String[0]);
                        }

                        if (queryCond.getSelectedOrgPKs() == null && queryCond.getSelectedOrgPKs().length == 0) {
                            return null;
                        }

                        String HBBalanceSql = HBReportQueryUtil.getRepDataQuerySQL(queryCond, vShowColumn, hbSchemeVO, ver, bContainIntrRep, report_pks_aloneId);
                        sql.add(HBBalanceSql);
                    }

                    pk_orgList.clear();
                    ver = this.getVersionByRepType(hbSchemeVO, repType);

                    for(i = 0; i < pk_orgs.length; ++i) {
                        if (!((UFBoolean)isLeaf.get(pk_orgs[i])).booleanValue()) {
                            pk_orgList.add(pk_orgs[i]);
                        }
                    }

                    if (pk_orgList != null && pk_orgList.size() > 0) {
                        queryCond.setSelectedOrgPKs((String[])pk_orgList.toArray(new String[pk_orgList.size()]));
                    } else {
                        queryCond.setSelectedOrgPKs(new String[0]);
                    }
                }

                if (queryCond.getSelectedOrgPKs() == null && queryCond.getSelectedOrgPKs().length == 0) {
                    return null;
                } else {
                    String SelSql = HBReportQueryUtil.getRepDataQuerySQL(queryCond, vShowColumn, hbSchemeVO, ver, bContainIntrRep, report_pks_aloneId);
                    sql.add(SelSql);
                    return sql;
                }
            }
        }
    }

    private Integer getVersionByRepType(HBSchemeVO hbSchemeVO, String repType) {
        Integer ver = null;
        if ("SEPADJ".equals(repType) && hbSchemeVO.getPk_adjustscheme() != null) {
            ver = HBVersionUtil.getSepAdjustByHBSchemeVO(hbSchemeVO);
        } else if ("HBADJ".equals(repType) && hbSchemeVO.getPk_adjustscheme() != null) {
            ver = HBVersionUtil.getHBAdjustByHBSchemeVO(hbSchemeVO);
        } else if ("HB".equals(repType)) {
            ver = hbSchemeVO.getVersion();
        }

        return ver;
    }

    public Map<String, String> reportAdjust(UFOCQueryTreeFormatVO queryInfo, String userId) throws BusinessException {
        AdjustRepExecCondVO execCond = new AdjustRepExecCondVO();
        this.initQueryInfo(queryInfo, execCond, userId);
        String ffwTaskType = null;
        if ("1".equals(queryInfo.getUserdefObj().get("adjustType"))) {
            ffwTaskType = "1001ZE1000000000MIZ2";
        } else if ("4".equals(queryInfo.getUserdefObj().get("adjustType"))) {
            ffwTaskType = "1001ZE1000000000MIZ3";
        }

        return this.doAdjust(execCond, ffwTaskType);
    }

    public Map<String, String> doAdjust(AdjustRepExecCondVO execCondVo, String ffwTaskType) throws BusinessException {
        String pk_hbScheme = execCondVo.getHbSchemeVo().getPk_hbscheme();
        HBSchemeVO hbSchemeVO_new = HBSchemeSrvUtils.getHBSchemeByHBSchemeId(pk_hbScheme);
        execCondVo.setHbSchemeVo(hbSchemeVO_new);
        String[] orgPKs = execCondVo.getOrgPKs();
        ArrayList<String> excorgPKs = new ArrayList();
        Map<String, String> otherKeywords = execCondVo.getKeywordValues();
        DefaultConstEnum adjustType = execCondVo.getAdjustRepType();
        String userID = execCondVo.getUserID();
        String pk_adjustScheme = execCondVo.getHbSchemeVo().getPk_adjustscheme();
        String excFailMessage = "";
        int failIndex = 1;
        String excOrg = "";
        int sucExcOrg = 0;

        try {
            if (null == pk_adjustScheme) {
                throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pub_0", "01830003-0066"));
            }

            AdjustSchemeVO adjustSchemeVo = (AdjustSchemeVO)((IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class)).retrieveByPK(AdjustSchemeVO.class, pk_adjustScheme);
            int iDstVer = execCondVo.getAdjustRepType().equals(IAdjustRepTypeContants.TYPE_SEP_ADJUSTREP) ? adjustSchemeVo.getVersion() : HBVersionUtil.getHBAdjustByHBSchemeVO(execCondVo.getHbSchemeVo());
            ArrayList<String> lockPks = new ArrayList();
            String[] var18 = orgPKs;
            int var19 = orgPKs.length;

            String errMsg;
            for(int var20 = 0; var20 < var19; ++var20) {
                String pk_org = var18[var20];
                otherKeywords.put("00000000000000000000", pk_org);
                HBSchemeVO hbSchemeVO = execCondVo.getHbSchemeVo();
                String pk_hbscheme = hbSchemeVO.getPk_hbscheme();
                UFBoolean issreportverify;
                boolean qrySingleAdjVoucherIsAllVerify;
                if (IAdjustRepTypeContants.TYPE_SEP_ADJUSTREP.equals(adjustType)) {
                    errMsg = MeasurePubDataUtil.getMeasurePubdata(750, true, otherKeywords, execCondVo.getHbSchemeVo()).getAloneID();
                    issreportverify = hbSchemeVO.getIssreportverify();
                    if (issreportverify != null && issreportverify.booleanValue()) {
                        qrySingleAdjVoucherIsAllVerify = ((IVouchQrySrv)NCLocator.getInstance().lookup(IVouchQrySrv.class)).qrySingleAdjVoucherIsAllVerify(pk_hbscheme, new String[]{pk_org}, new String[]{errMsg});
                        if (!qrySingleAdjVoucherIsAllVerify) {
                            excFailMessage = excFailMessage + String.format("%s.%s,%s:%s\n", failIndex, this.getOrgNameByPkOrg(pk_org), NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0132"), NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0130"));
                            ++failIndex;
                            continue;
                        }
                    }
                } else {
                    errMsg = MeasurePubDataUtil.getMeasurePubdata(754, true, otherKeywords, execCondVo.getHbSchemeVo()).getAloneID();
                    issreportverify = hbSchemeVO.getIshberportverify();
                    if (issreportverify != null && issreportverify.booleanValue()) {
                        qrySingleAdjVoucherIsAllVerify = ((IVouchQrySrv)NCLocator.getInstance().lookup(IVouchQrySrv.class)).qryHbAdjVoucherIsAllVerify(pk_hbscheme, new String[]{pk_org}, new String[]{errMsg});
                        if (!qrySingleAdjVoucherIsAllVerify) {
                            excFailMessage = excFailMessage + String.format("%s.%s,%s:%s\n", failIndex, this.getOrgNameByPkOrg(pk_org), NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0132"), NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0130"));
                            ++failIndex;
                            continue;
                        }
                    }
                }

                excorgPKs.add(pk_org);
                MeasurePubDataVO measurePubdata = MeasurePubDataUtil.getMeasurePubdata(iDstVer, true, otherKeywords, execCondVo.getHbSchemeVo());
                lockPks.add(AdjustReportSrvImpl.LOCK_ADJREP_KEY + measurePubdata.getAloneID());
            }

            BDPKLockUtil.lockString((String[])lockPks.toArray(new String[0]));
            List<DistributeExcVo> execVos = new ArrayList();
            Iterator var30 = excorgPKs.iterator();

            while(var30.hasNext()) {
                String pk_org = (String)var30.next();
                Map<String, String> excKeyWords = new HashMap(otherKeywords);
                excKeyWords.put("00000000000000000000", pk_org);
                AdjustRepExecVO execVo = new AdjustRepExecVO(excKeyWords, execCondVo.getHbSchemeVo(), adjustType, userID);
                DistributeExcVo distributeExcVo = new DistributeExcVo();
                errMsg = this.checkAdjust(execVo, distributeExcVo);
                if (errMsg != null) {
                    excFailMessage = excFailMessage + String.format("%s.%s,%s:%s\n", failIndex, this.getOrgNameByPkOrg(pk_org), NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0132"), errMsg);
                    ++failIndex;
                } else {
                    distributeExcVo.setOrg(pk_org);
                    distributeExcVo.setExecVo(execVo);
                    distributeExcVo.setPk_user(userID);
                    execVos.add(distributeExcVo);
                }
            }

            if (execVos.size() > 0) {
                String busiExeClass = "nc.pubitf.ufoc.gbadjust.reportadjust.task.ExcuteRepAdjDistributeTask";
                this.distributeExc(busiExeClass, ffwTaskType, (DistributeExcVo[])execVos.toArray(new DistributeExcVo[0]));
                sucExcOrg = execVos.size();
            }
        } catch (Exception var27) {
            Logger.error(var27.getMessage(), var27);
            if (var27 instanceof LockFailedException) {
                if (execCondVo.getAdjustRepType().equals(IAdjustRepTypeContants.TYPE_HB_ADJUSTREP)) {
                    throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pub_0", "01830003-0044"), var27);
                }

                throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pub_0", "01830003-0042"), var27);
            }

            if (var27 instanceof UFOCUnThrowableException) {
                throw new BusinessException(String.format("%s:%s\n", this.getOrgNameByPkOrg(excOrg), var27.getMessage()));
            }

            throw new BusinessException(String.format("%s:%s\n", this.getOrgNameByPkOrg(excOrg), var27.getMessage()));
        }

        HashMap<String, String> results = new HashMap(2);
        if (orgPKs.length == sucExcOrg) {
            results.put("msgCode", "success");
            results.put("message", String.format(NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0133"), sucExcOrg));
            return results;
        } else {
            results.put("msgCode", "fail");
            results.put("message", String.format(NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0128") + "\n%s", orgPKs.length, sucExcOrg, orgPKs.length - sucExcOrg, excFailMessage));
            return results;
        }
    }

    private String checkAdjust(AdjustRepExecVO execVo, DistributeExcVo distributeExcVo) throws Exception {
        HBSchemeVO hbSchemeVo = execVo.getHbSchemeVo();
        String strVouchAloneID = null;
        if (execVo.getAdjustType().equals(IAdjustRepTypeContants.TYPE_HB_ADJUSTREP)) {
            strVouchAloneID = MeasurePubDataUtil.getMeasurePubdata(754, true, execVo.getKeywords(), execVo.getHbSchemeVo()).getAloneID();
        } else {
            strVouchAloneID = MeasurePubDataUtil.getMeasurePubdata(750, true, execVo.getKeywords(), execVo.getHbSchemeVo()).getAloneID();
        }

        int[] iVouchType = null;
//        int[] iVouchType;
        if (execVo.getAdjustType().equals(IAdjustRepTypeContants.TYPE_SEP_ADJUSTREP)) {
            iVouchType = new int[]{1, 5};
        } else {
            iVouchType = new int[]{2, 6};
        }

        IVouchQrySrv vouchQrySrv = (IVouchQrySrv)NCLocator.getInstance().lookup(IVouchQrySrv.class);
        if (!vouchQrySrv.isExsitVouch(strVouchAloneID, hbSchemeVo.getPk_keygroup(), iVouchType, hbSchemeVo)) {
            return NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0131");
        } else {
            String pk_adjustScheme = hbSchemeVo.getPk_adjustscheme();
            AdjustSchemeVO adjustSchemeVo = (AdjustSchemeVO)((IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class)).retrieveByPK(AdjustSchemeVO.class, pk_adjustScheme);
            int iSrcVer = execVo.getAdjustType().equals(IAdjustRepTypeContants.TYPE_SEP_ADJUSTREP) ? 0 : hbSchemeVo.getVersion();
            int iDstVer = execVo.getAdjustType().equals(IAdjustRepTypeContants.TYPE_SEP_ADJUSTREP) ? adjustSchemeVo.getVersion() : HBVersionUtil.getHBAdjustByHBSchemeVO(hbSchemeVo);
//            int iSrc_cur = false;
            int iSrc_cur;
            if (execVo.getAdjustType().equals(IAdjustRepTypeContants.TYPE_SEP_ADJUSTREP)) {
                iSrc_cur = HBVersionUtil.getSep_adj_src(iDstVer);
            } else {
                iSrc_cur = HBVersionUtil.getHB_adj_src(iDstVer);
            }

            MeasurePubDataVO srcPubdata = this.getMeasurePubdata(iSrcVer, false, execVo);
            if (srcPubdata == null) {
                return NCLangRes4VoTransl.getNCLangRes().getStrByID("pub_0", "01830001-0447") + "!";
            } else {
                String[] aryRepIDs = ((IHBSchemeQrySrv)NCLocator.getInstance().lookup(IHBSchemeQrySrv.class)).getReportIdByHBSchemeId(execVo.getHbSchemeVo().getPk_hbscheme());
                MeasurePubDataVO dstPubdata = this.getMeasurePubdata(iDstVer, true, execVo);
                String[] var15 = aryRepIDs;
                int iCreditVer = aryRepIDs.length;

                for(int var17 = 0; var17 < iCreditVer; ++var17) {
                    String pk_report = var15[var17];
                    if (HBBBTableInputActionHandler.isRepCommit(dstPubdata.getAloneID(), pk_report)) {
                        return NCLangRes4VoTransl.getNCLangRes().getStrByID("pub_0", "01830003-0041");
                    }

                    IUfoeRepDataSrv repDataSrv = (IUfoeRepDataSrv)NCLocator.getInstance().lookup(IUfoeRepDataSrv.class);
                    String dataOrigin = repDataSrv.checkRepCommitDataOrigin(pk_report, dstPubdata.getAloneID());
                    if (dataOrigin != null) {
                        return NCLangRes4VoTransl.getNCLangRes().getStrByID("pub_0", "01830003-0102") + NCLangRes4VoTransl.getNCLangRes().getStrByID("pub_0", "01830003-0103") + dataOrigin + NCLangRes4VoTransl.getNCLangRes().getStrByID("pub_0", "01830003-0104");
                    }
                }

                int iDebitVer = HBVersionUtil.getDebitVersion(iDstVer);
                iCreditVer = HBVersionUtil.getCreditVersion(iDstVer);
                MeasurePubDataVO debitPubdata = this.getMeasurePubdata(iDebitVer, true, execVo);
                MeasurePubDataVO creditPubdata = this.getMeasurePubdata(iCreditVer, true, execVo);
                MeasurePubDataVO src_currPubdata = this.getMeasurePubdata(iSrc_cur, true, execVo);
                distributeExcVo.setExecVo(execVo);
                distributeExcVo.setSrcPubdata(srcPubdata);
                distributeExcVo.setDebitPubdata(debitPubdata);
                distributeExcVo.setCreditPubdata(creditPubdata);
                distributeExcVo.setDstPubdata(dstPubdata);
                distributeExcVo.setAryRepIDs(aryRepIDs);
                distributeExcVo.setSrc_currPubdata(src_currPubdata);
                distributeExcVo.setiCreditVer(iCreditVer);
                distributeExcVo.setiDebitVer(iDebitVer);
                return null;
            }
        }
    }

    private MeasurePubDataVO getMeasurePubdata(int iVer, boolean bNew, AdjustRepExecVO execVo) throws Exception {
        return MeasurePubDataUtil.getMeasurePubdata(iVer, bNew, execVo.getKeywords(), execVo.getHbSchemeVo());
    }

    private String getOrgNameByPkOrg(String pk_org) {
        if (pk_org == null) {
            return "";
        } else {
            try {
                String cond = "pk_org = ?";
                SQLParameter param = new SQLParameter();
                param.addParam(pk_org);
                BaseDAO baseDao = new BaseDAO();
                Collection collection = null;
                collection = baseDao.retrieveByClause(OrgVO.class, cond, param);
                return ((OrgVO)((ArrayList)collection).get(0)).getName();
            } catch (DAOException var6) {
                var6.printStackTrace();
                return "";
            }
        }
    }

    private String distributeExc(String busiExeClass, String ffwTaskType, DistributeExcVo[] execVo) throws BusinessException {
        BusinessParamVO<DistributeExcVo, Serializable> businessParam = getBusinessParam(busiExeClass, ffwTaskType, execVo);
        businessParam.setNote(NCLangRes4VoTransl.getNCLangRes().getStrByID("1820pub_0", "01820006-0105"));
        DistributeResult[] results = new DistributeResult[0];

        try {
            results = (new AsynDistributeInvoker()).invokeInLocal(businessParam);
        } catch (Exception var7) {
            throw new BusinessException(var7.getMessage());
        }

        DistributeResult result = new DistributeResult();
        if (results != null) {
            result = results[0];
        }

        if (result.getEx() != null) {
            ExceptionUtils.asBusinessException(result.getEx());
        }

        return result.getResult().toString();
    }

    public static BusinessParamVO<DistributeExcVo, Serializable> getBusinessParam(String busiExeClass, String tasktype, DistributeExcVo[] distributeExcVo) {
        BusinessParamVO<DistributeExcVo, Serializable> businessParam = new BusinessParamVO();
        businessParam.setPk_tasktype(tasktype);
        businessParam.setOwnmodule("ufoc");
        businessParam.setBusiExeClass(busiExeClass);
        if (tasktype == "1001ZE1000000000MIZ2") {
            businessParam.setTaskname(NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0140"));
        } else {
            businessParam.setTaskname(NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0141"));
        }

        businessParam.setParameters(distributeExcVo);
        return businessParam;
    }

    public void initQueryInfo(UFOCQueryTreeFormatVO queryInfo, AdjustRepExecCondVO execCondVo, String userId) throws UFOSrvException {
        List<ConditonVo> conditions = queryInfo.getConditions();
        String pkSchem = null;
        HBSchemeVO hbSchemeVO = null;
        Map<String, String> keyWordMap = new HashMap();
        Iterator var8 = conditions.iterator();

        while(var8.hasNext()) {
            ConditonVo con = (ConditonVo)var8.next();
            if (con.getField().equals("pk_hbscheme")) {
                pkSchem = con.getValue().getFirstvalue();
                hbSchemeVO = HBSchemeSrvUtils.getHBSchemeByHBSchemeId(pkSchem);
            } else if (KeyVO.isTTimeKey(con.getPk_keyword())) {
                String value = con.getDisplay();
                keyWordMap.put(con.getPk_keyword(), value);
            } else if (con.getPk_keyword() != null) {
                keyWordMap.put(con.getPk_keyword(), con.getValue().getFirstvalue());
            }
        }

        execCondVo.setKeywordValues(keyWordMap);
        execCondVo.setHbSchemeVo(hbSchemeVO);
        if ("1".equals(queryInfo.getUserdefObj().get("adjustType"))) {
            execCondVo.setAdjustRepType(new DefaultConstEnum(1, NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0063")));
        } else if ("4".equals(queryInfo.getUserdefObj().get("adjustType"))) {
            execCondVo.setAdjustRepType(new DefaultConstEnum(4, NCLangRes4VoTransl.getNCLangRes().getStrByID("ufoc_ufocbasedoc_0", "01830ufocbasedoc001-0064")));
        }

        String[] orgs = (String[])((ArrayList)queryInfo.getUserdefObj().get("selData")).toArray(new String[0]);
        execCondVo.setOrgPKs(orgs);
        execCondVo.setUserID(userId);
    }

    public AdjustSchemeVO getAdjustSchemeByHbSchemePk(String pkHbScheme) throws UFOSrvException {
        HBSchemeVO hbSchemeVO = HBSchemeSrvUtils.getHBSchemeByHBSchemeId(pkHbScheme);
        return hbSchemeVO.getPk_adjustscheme() == null ? null : this.getAdjustSchemeByPk(hbSchemeVO.getPk_adjustscheme());
    }

    public AdjustSchemeVO getAdjustSchemeByPk(String pk_adjustScheme) {
        try {
            String cond = "pk_adjustscheme = ?";
            SQLParameter param = new SQLParameter();
            param.addParam(pk_adjustScheme);
            BaseDAO baseDao = new BaseDAO();
            Collection collection = baseDao.retrieveByClause(AdjustSchemeVO.class, cond, param);
            return (AdjustSchemeVO)((ArrayList)collection).get(0);
        } catch (DAOException var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public String HBReportNameByPk(String pk_report) {
        try {
            String cond = "pk_report = ?";
            SQLParameter param = new SQLParameter();
            param.addParam(pk_report);
            BaseDAO baseDao = new BaseDAO();
            Collection collection = baseDao.retrieveByClause(ReportVO.class, cond, param);
            return ((ReportVO)((ArrayList)collection).get(0)).getName() ;
//                    + "_" + ((ReportVO)((ArrayList)collection).get(0)).getCode();
        } catch (DAOException var6) {
            var6.printStackTrace();
            return null;
        }
    }

    private static String getCurLoginTTimeValue(String strQueryTTimeValue, String strStartTime, String strEndTime, KeyVO key, String strAccSchemePK, KeyGroupVO keyGroupVO) {
        if (strEndTime != null && strEndTime.trim().length() > 0 && strQueryTTimeValue.compareTo(strEndTime) > 0) {
            strQueryTTimeValue = strStartTime;
        } else if (strStartTime != null && strStartTime.trim().length() > 0 && strQueryTTimeValue.compareTo(strStartTime) < 0) {
            strQueryTTimeValue = strStartTime;
        }

        if (key.isAccPeriodKey()) {
            if (strAccSchemePK == null) {
                strAccSchemePK = AccPeriodSchemeUtil.getDefaultSchemePk(true, (String)null);
            }

            strQueryTTimeValue = AccPeriodSchemeUtil.getInstance().getAccPeriodByNatDate(strAccSchemePK, key.getPk_keyword(), strQueryTTimeValue);
        } else if (key.isTimeKeyVO() && nccloud.commons.lang.StringUtils.isNotEmpty(strQueryTTimeValue) && UFODate.isAllowDate(strQueryTTimeValue)) {
            strQueryTTimeValue = (new UFODate(strQueryTTimeValue)).getEndDay(keyGroupVO.getTimeProp()).toString();
        }

        return strQueryTTimeValue;
    }

    public ReportCombineStruVersionVO getHBRepStruVOToBatchAdjRule(String date, String pkHbScheme) throws BusinessException {
        return getHBRepStruVO(date, pkHbScheme);
    }

    public AdjustSchemeVO[] getAdjSchemeVOs(String[] adjSchemePKs) throws BusinessException {
        BaseDAO baseDao = new BaseDAO();
        Collection collection = baseDao.retrieveByClause(AdjustSchemeVO.class, (new IDQueryBuilder()).buildSQL("pk_adjustscheme", adjSchemePKs));
        return collection != null && collection.size() > 0 ? (AdjustSchemeVO[])((AdjustSchemeVO[])collection.toArray(new AdjustSchemeVO[0])) : null;
    }
}
