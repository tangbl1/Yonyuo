package nccloud.bs.excel.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import nc.itf.uif.pub.IUifService;
import nc.itf.so.m4331.IDeliveryMaintainApp;

import nc.pub.bi.meta.excel.IExcelSheet;
import nc.pub.bi.meta.excel.poi.PoiWorkbook;

import java.util.concurrent.CountDownLatch;


import nc.bs.framework.common.InvocationInfo;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.component.RemoteProcessComponetFactory;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.trade.business.HYPubBO;
import nc.itf.so.m4331.IDeliveryMaintain;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ic.m4c.entity.SaleOutBodyVO;
import nc.vo.pfxx.exception.PfxxException;
import nc.vo.platform.appsystemplate.PageTempletVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.m4331.entity.DeliveryBVO;
import nc.vo.so.m4331.entity.DeliveryHVO;
import nc.vo.so.m4331.entity.DeliveryVO;
import nc.vo.so.sosettlement.AggSettlementHVO;
import nc.vo.so.sosettlement.SettlementBVO;
import nc.vo.so.sosettlement.SettlementHVO;
import nc.vo.vorg.DeptVersionVO;
import nc.vo.vorg.OrgVersionVO;
import nccloud.base.exception.ExceptionUtils;
import nccloud.baseapp.core.log.NCCForUAPLogger;
import nccloud.bs.excel.ConfigInfoAnalyser;
import nccloud.bs.excel.IXChangeContext;
import nccloud.bs.excel.ResponseMessage;
import nccloud.bs.excel.XChangeContext;
import nccloud.bs.excel.plugin.IImportLogService;
import nccloud.bs.excel.util.ExcelProcessUtil;
import nccloud.commons.collections.CollectionUtils;
import nccloud.itf.excel.process.IExcelImporterProcess;
import nccloud.itf.excel.process.IExcelImporterProduce;
import nccloud.itf.excel.process.ThreadPoolManager;
import nccloud.itf.trade.excelimport.IImportProcess;
import nccloud.itf.trade.excelimport.IImportProcessAction;
import nccloud.itf.trade.excelimport.IImportProcessWithAffair;
import nccloud.nc.ui.trade.excelimport.vo.AbstractAggregatedValueForExcel;
import nccloud.nc.ui.trade.excelimport.vo.ChildNodeForExcelDescribe;
import nccloud.nc.ui.trade.excelimport.vo.ExcelCommonAggVO;
import nccloud.ui.trade.excelimport.AreaGroup;
import nccloud.ui.trade.excelimport.AreaGroup2TreeVOsListAdapter;
import nccloud.ui.trade.excelimport.ImportTypeDismatchException;
import nccloud.ui.trade.excelimport.InputItem;
import nccloud.ui.trade.excelimport.InputItemCreator;
import nccloud.ui.trade.excelimport.InputItemCreatorByTemp;
import nccloud.vo.excel.excelconfig.XChangeConfigInfo;
import nccloud.vo.excel.scheme.BillDefination;
import nccloud.vo.excel.util.PfxxPluginUtils;
import nccloud.vo.excel.util.PfxxUtils;
import nccloud.vo.excel.util.PropertyConvertorUtils;
import nccloud.vo.excel.util.TreeDataUtils;
import uap.pub.fs.domain.basic.FileHeader;

public class ExcelImporterProcess implements IExcelImporterProcess {
	public List<ResponseMessage> processExcelFile(Map<String, Object> requestparam, String filename,
												  InputStream inputstream)
			throws FileNotFoundException, ImportTypeDismatchException, IOException, BusinessException {
		String billtype = requestparam.get("billType").toString();

		String importSign = requestparam.get("importSign").toString();
		String isbatch = "N";
		if (null != requestparam.get("isbatch"))
			isbatch = requestparam.get("isbatch").toString();
		boolean issuccesstrans = true;
		boolean bbatch = UFBoolean.valueOf(isbatch).booleanValue();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String starttime = sdf.format(new Date());
		IXChangeContext context = initConfigInfo(requestparam, null);
		File localFile = ExcelProcessUtil.writeInputStreamToFile(requestparam, filename, inputstream);

		FileHeader fileheader = ExcelProcessUtil.writetoFileStore(filename, new FileInputStream(localFile),
				"exceltemp");
		IImportProcess importprocess = PfxxPluginUtils.getImportProcess(context);
		String proname = PropertyConvertorUtils.getProcessBillType(context.getBillDefination());
		List<AbstractAggregatedValueForExcel> vos = doImportAggVO(localFile, context);
		if (CollectionUtils.isEmpty(vos))
			throw new BusinessException(
					NCLangResOnserver.getInstance().getStrByID("excelimport", "ExcelImporter-000015"));
		List<ResponseMessage> responseMessages = new ArrayList<>();
		ResponseMessage responsemessage = new ResponseMessage();

		responseMessages.add(responsemessage);
		int currentSequence = 1;
		context.setTotallines(vos.size());
		List<AbstractAggregatedValueForExcel> aggvalueList = new ArrayList<>();
		for (AbstractAggregatedValueForExcel extendaggvalue : vos) {
			setProcessDecima(currentSequence, vos.size(), billtype + importSign);
			context.resetCurrentResponseMessage();
			ExcelProcessor billProcessor = new ExcelProcessor(context.getCurrentResponseMessage(), extendaggvalue);
			context.getCurrentResponseMessage().setContent(currentSequence + "");
			context.setCurrentlines(currentSequence);
			billProcessor.translate();
			if (!billProcessor.isSuccessTranslate()) {
				responseMessages.add(billProcessor.getResponseMessage());
				currentSequence++;
				issuccesstrans = false;
				continue;
			}
			if ("Y".equalsIgnoreCase(isbatch)) {
				aggvalueList.add(extendaggvalue);
			} else {
				((IImportProcessWithAffair) NCLocator.getInstance().lookup(IImportProcessWithAffair.class))
						.ProcessImport_RequiresNew(importprocess, extendaggvalue, context);
			}
			if (context.getCurrentResponseMessage().getResultDescription() != null
					&& !"".endsWith(context.getCurrentResponseMessage().getResultDescription()))
				responseMessages.add(context.getCurrentResponseMessage());
			currentSequence++;
		}
		boolean isbatchsuccess = true;
		if ("Y".equalsIgnoreCase(isbatch) && issuccesstrans) {
			((IImportProcessWithAffair) NCLocator.getInstance().lookup(IImportProcessWithAffair.class))
					.ProcessImport_RequiresNew(importprocess, aggvalueList, context);
			if (context.getCurrentResponseMessage().getResultDescription() != null
					&& !"".endsWith(context.getCurrentResponseMessage().getResultDescription())) {
				isbatchsuccess = false;
				if (aggvalueList.size() > 0)
					responseMessages.add(context.getCurrentResponseMessage());
			}
		}
		recordImportLogs(responseMessages, vos.size(), bbatch, isbatchsuccess, filename, billtype, starttime,
				fileheader, proname, issuccesstrans);
		return responseMessages;
	}

	private void multiProcessDATA(final List<ResponseMessage> responseMessages, final String billtype,
								  final String importSign, List<AbstractAggregatedValueForExcel> importaggvalues,
								  final List<AbstractAggregatedValueForExcel> aggvalueList, final boolean isbatch, IXChangeContext contexts)
			throws PfxxException {
		final int totalsize = importaggvalues.size();
		final IImportProcess importprocess = PfxxPluginUtils.getImportProcess(contexts);
		int poolsize = PfxxPluginUtils.getPoolSize(contexts);
		List<List<ExcelCommonAggVO>> aggvaluesList = ExcelProcessUtil.processMultiDatas(importaggvalues, poolsize);
		final InvocationInfo info = ThreadPoolManager.getInvocationInfo();
		final String logUtilPK = ThreadPoolManager.generateNewPk();
		final ThreadPoolManager poolmanager = ThreadPoolManager.getInstance(poolsize);
		final CountDownLatch latch = new CountDownLatch(aggvaluesList.size());
		for (List<ExcelCommonAggVO> aggvalues : aggvaluesList) {
			final IXChangeContext threadcontext = ThreadPoolManager.createNewXChangeContext(contexts);
			poolmanager.submit(new Runnable() {
				public void run() {
					RemoteProcessComponetFactory factory = (RemoteProcessComponetFactory) NCLocator.getInstance()
							.lookup("RemoteProcessComponetFactory");
					try {
						ThreadPoolManager.setInvocationInfoWithLogUtilPK(info, logUtilPK, threadcontext);
						if (factory != null)
							factory.preProcess();
						for (AbstractAggregatedValueForExcel extendaggvalue : aggvalues) {
							((IExcelImporterProduce) NCLocator.getInstance().lookup(IExcelImporterProduce.class))
									.upExcelImporterProduce(
											InvocationInfoProxy.getInstance().getUserId() + billtype + importSign,
											totalsize, poolmanager);
							threadcontext.resetCurrentResponseMessage();
							ExcelProcessorByTemp billProcessortemp = new ExcelProcessorByTemp(
									threadcontext.getCurrentResponseMessage(), extendaggvalue);
							threadcontext.getCurrentResponseMessage().setContent(extendaggvalue.getRowNum() + "");
							threadcontext.setCurrentlines(extendaggvalue.getRowNum());
							billProcessortemp.translate();
							if (!billProcessortemp.isSuccessTranslate()) {
								synchronized (poolmanager) {
									responseMessages.add(billProcessortemp.getResponseMessage());
								}
								continue;
							}
							if (isbatch) {
								synchronized (poolmanager) {
									aggvalueList.add(extendaggvalue);
								}
							} else {
								((IImportProcessWithAffair) NCLocator.getInstance()
										.lookup(IImportProcessWithAffair.class)).ProcessImport_RequiresNew(
										importprocess, extendaggvalue, threadcontext);
							}
							if (threadcontext.getCurrentResponseMessage().getResultDescription() != null
									&& !"".endsWith(threadcontext.getCurrentResponseMessage().getResultDescription()))
								synchronized (poolmanager) {
									responseMessages.add(threadcontext.getCurrentResponseMessage());
								}
						}
						if (factory != null)
							factory.postProcess();
					} catch (Exception ex) {
						if (factory != null)
							factory.postErrorProcess(ex);
						Logger.error(ex.getMessage(), ex);
						ResponseMessage exceptionmessage = new ResponseMessage();
						exceptionmessage.appendResultDescription(ex.getMessage());
						responseMessages.add(exceptionmessage);
						throw new BusinessRuntimeException(ex.getMessage());
					} finally {
						latch.countDown();
						PfxxUtils.releaseContext();
						PfxxUtils.releaseFormulaParser();
						if (factory != null)
							factory.clearThreadScopePostProcess();
					}
				}
			});
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			Logger.info("���̵߳���ʧ��---------------");
		} finally {
			((IExcelImporterProduce) NCLocator.getInstance().lookup(IExcelImporterProduce.class))
					.endExcelImporterProduce(billtype, importSign);
		}
	}

	public List<ResponseMessage> processExcelFileByTemp(Map<String, Object> requestparam, String filename,
														InputStream inputstream, PageTempletVO pagetempletvo)
			throws FileNotFoundException, ImportTypeDismatchException, IOException, BusinessException {
		String billtype = requestparam.get("billType").toString();
		String importSign = requestparam.get("importSign").toString();
		String isbatch = "N";
		if (null != requestparam.get("isbatch"))
			isbatch = requestparam.get("isbatch").toString();
		boolean bbatch = UFBoolean.valueOf(isbatch).booleanValue();
		boolean issuccesstrans = true;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String starttime = sdf.format(new Date());
		IXChangeContext context = initConfigInfo(requestparam, pagetempletvo);
		boolean ismulti = PfxxPluginUtils.isbatchImport(context);
		File localFile = ExcelProcessUtil.writeInputStreamToFile(requestparam, filename, inputstream);
		// chensyk add HW ����������start
		if("4331".equals(billtype)) {

			String msg=dealFile(localFile);
			List<ResponseMessage> responseMessages = new ArrayList<>();
			ResponseMessage responsemessage = new ResponseMessage();
			responsemessage.setContent(msg);

			responseMessages.add(responsemessage);
			//���±�
			((IDeliveryMaintainApp) NCLocator.getInstance().lookup(IDeliveryMaintainApp.class)).insertErrorlog(msg);

			return responseMessages;
		}
		//chensyk add HW ����������end
		FileHeader fileheader = ExcelProcessUtil.writetoFileStore(filename, new FileInputStream(localFile),
				"exceltemp");
		IImportProcess importprocess = PfxxPluginUtils.getImportProcess(context);
		String proname = PropertyConvertorUtils.getProcessBillType(context.getBillDefination());
		List<AbstractAggregatedValueForExcel> vos = doImportAggVO(localFile, context);
		if (CollectionUtils.isEmpty(vos))
			throw new BusinessException(
					NCLangResOnserver.getInstance().getStrByID("excelimport", "ExcelImporter-000015"));
		List<ResponseMessage> responseMessages = new ArrayList<>();
		ResponseMessage responsemessage = new ResponseMessage();
		responseMessages.add(responsemessage);
		int currentSequence = 1;
		context.setTotallines(vos.size());
		XChangeConfigInfo xcConf = context.getXChangeConfigInfo();
		String noShowConflict = (String) xcConf.getAttribute("not_show_conflict");
		Boolean isbatchsuccess = Boolean.valueOf(true);
		try {
			if (noShowConflict != null) {
				processRegion(context, vos, billtype, importSign);
			} else {
				List<AbstractAggregatedValueForExcel> aggvalueList = new ArrayList<>();
				if (ismulti) {
					multiProcessDATA(responseMessages, billtype, importSign, vos, aggvalueList, bbatch, context);
				} else {
					for (AbstractAggregatedValueForExcel extendaggvalue : vos) {
						context.resetCurrentResponseMessage();
						ExcelProcessorByTemp billProcessortemp = new ExcelProcessorByTemp(
								context.getCurrentResponseMessage(), extendaggvalue);
						context.getCurrentResponseMessage().setContent(currentSequence + "");
						context.setCurrentlines(currentSequence);
						billProcessortemp.translate();
						if (!billProcessortemp.isSuccessTranslate()) {
							responseMessages.add(billProcessortemp.getResponseMessage());
							currentSequence++;
							issuccesstrans = false;
							setProcessDecima(currentSequence, vos.size(), billtype + importSign);
							continue;
						}
						if (bbatch) {
							aggvalueList.add(extendaggvalue);
						} else {
							((IImportProcessWithAffair) NCLocator.getInstance().lookup(IImportProcessWithAffair.class))
									.ProcessImport_RequiresNew(importprocess, extendaggvalue, context);
						}
						if (context.getCurrentResponseMessage().getResultDescription() != null
								&& !"".endsWith(context.getCurrentResponseMessage().getResultDescription()))
							responseMessages.add(context.getCurrentResponseMessage());
						setProcessDecima(currentSequence, vos.size(), billtype + importSign);
						currentSequence++;
					}
				}
				if (bbatch && issuccesstrans) {
					context.resetCurrentResponseMessage();
					context.getCurrentResponseMessage().setContent(String.valueOf(currentSequence - 1));
					((IImportProcessWithAffair) NCLocator.getInstance().lookup(IImportProcessWithAffair.class))
							.ProcessImport_RequiresNew(importprocess, aggvalueList, context);
					if (context.getCurrentResponseMessage().getResultDescription() != null
							&& !"".endsWith(context.getCurrentResponseMessage().getResultDescription())) {
						isbatchsuccess = Boolean.valueOf(false);
						if (aggvalueList.size() > 0)
							responseMessages.add(context.getCurrentResponseMessage());
					}
				}
			}
			recordImportLogs(responseMessages, vos.size(), bbatch, isbatchsuccess.booleanValue(), filename, billtype,
					starttime, fileheader, proname, issuccesstrans);
		} finally {
			((IExcelImporterProduce) NCLocator.getInstance().lookup(IExcelImporterProduce.class))
					.endExcelImporterProduce(billtype, importSign);
		}
		return responseMessages;
	}

	private void recordImportLogs(List<ResponseMessage> responseMessages, int totalsize, boolean isbatch,
								  boolean batchsucc, String filename, String billtype, String starttime, FileHeader fileheader,
								  String proname, boolean issuccesstrans) {
		SimpleDateFormat endsdf = new SimpleDateFormat("yyyy-MM-d HH:mm:ss");
		String endtime = endsdf.format(new Date());
		int errorsize = responseMessages.size() - 1;
		int successsize = totalsize - errorsize;
		((ResponseMessage) responseMessages.get(0))
				.setResultDescription(new StringBuffer(NCLangResOnserver.getInstance().getStrByID("excelimport",
						"excel_import_message01", null, new String[]{Integer.valueOf(totalsize).toString(),
								Integer.valueOf(successsize).toString(), Integer.valueOf(errorsize).toString()})));
		((ResponseMessage) responseMessages.get(0)).setSuccesssize(successsize);
		((ResponseMessage) responseMessages.get(0)).setErrorsize(errorsize);
		if (isbatch) {
			int successisze = !issuccesstrans
					? 0
					: ((responseMessages.get(responseMessages.size() - 1) != null)
					? ((ResponseMessage) responseMessages.get(responseMessages.size() - 1)).getSuccesssize()
					: successsize);
			((ResponseMessage) responseMessages.get(0)).setSuccesssize(successisze);
			((ResponseMessage) responseMessages.get(0)).setErrorsize(totalsize - successisze);
			String resid = issuccesstrans ? "excel_import_message03" : "excel_import_message02";
			((ResponseMessage) responseMessages.get(0)).setResultDescription(
					new StringBuffer(NCLangResOnserver.getInstance().getStrByID("excelimport", resid, null,
							new String[]{Integer.valueOf(totalsize).toString(), Integer.valueOf(successisze).toString(),
									Integer.valueOf(totalsize - successisze).toString()})));
		}
		((ResponseMessage) responseMessages.get(0)).setErrortype(1);
		String cuserid = InvocationInfoProxy.getInstance().getUserId();
		sendMessage(cuserid, InvocationInfoProxy.getInstance().getGroupId(), billtype, filename, endtime);
		((IImportLogService) NCLocator.getInstance().lookup(IImportLogService.class)).insertImportLog(responseMessages,
				fileheader, proname, cuserid, filename, starttime, endtime, billtype);
	}

	private void sendMessage(String cuserid, String pk_group, String billType, String fielName, String endtime) {
		Logger.error("��ʼ������Ϣ֪ͨ");
		ExcelProcessUtil.sendMessage(cuserid, pk_group, billType, fielName, endtime);
		Logger.error("����������Ϣ֪ͨ");
	}

	private void setProcessDecima(int current, int lenght, String userKEY) {
		NumberFormat formatter = new DecimalFormat("0");
		Double x = Double.valueOf((new Double(current * 1.0D / lenght * 1.0D)).doubleValue() * 100.0D);
		String xx = formatter.format(x);
		((IExcelImporterProduce) NCLocator.getInstance().lookup(IExcelImporterProduce.class))
				.setExcelImporterProduce(InvocationInfoProxy.getInstance().getUserId() + userKEY, xx);
	}

	private IXChangeContext initConfigInfo(Map<String, Object> requestparam, PageTempletVO pagetemplet)
			throws PfxxException {
		XChangeConfigInfo xchangeconfiginfo = ConfigInfoAnalyser.initConfigInfoWithEN(requestparam, pagetemplet);
		XChangeContext xChangeContext = new XChangeContext();
		xChangeContext.init(xchangeconfiginfo);
		PfxxUtils.registerContext((IXChangeContext) xChangeContext);
		return (IXChangeContext) xChangeContext;
	}

	private List<AbstractAggregatedValueForExcel> doImportAggVO(File file, IXChangeContext context)
			throws FileNotFoundException, IOException, ImportTypeDismatchException, BusinessException {
		BillDefination billdefination = context.getBillDefination();
		List<InputItem> inputitems = InputItemCreator.getInputItems(billdefination);
		if (context.getXChangeConfigInfo().getPagetemplet() != null)
			inputitems = InputItemCreatorByTemp.getInputItems(billdefination,
					context.getXChangeConfigInfo().getPagetemplet());
		AreaGroup areagroup = ExcelProcessUtil.convertAreaGroup(file, inputitems);
		if (areagroup == null)
			return null;
		ChildNodeForExcelDescribe childNode = new ChildNodeForExcelDescribe();
		TreeDataUtils.DefineChildNodeDescribe(billdefination, childNode);
		return convert2AggVO(areagroup, childNode);
	}

	private List<AbstractAggregatedValueForExcel> convert2AggVO(AreaGroup group, ChildNodeForExcelDescribe childNode) {
		AreaGroup2TreeVOsListAdapter extendagglist = new AreaGroup2TreeVOsListAdapter(group, childNode);
		return (List<AbstractAggregatedValueForExcel>) extendagglist;
	}

	public Map<String, Object> processExcelFileWithReturn(Map<String, Object> requestparam, String filename,
														  InputStream inputstream, PageTempletVO pagetempletvo)
			throws FileNotFoundException, ImportTypeDismatchException, IOException, BusinessException {
		Map<String, Object> returnMap = new HashMap<>();
		String billtype = requestparam.get("billType").toString();
		String importSign = requestparam.get("importSign").toString();
		IXChangeContext context = initConfigInfo(requestparam, pagetempletvo);
		File localFile = ExcelProcessUtil.writeInputStreamToFile(requestparam, filename, inputstream);
		IImportProcess importprocess = PfxxPluginUtils.getImportProcess(context);
		List<AbstractAggregatedValueForExcel> vos = doImportAggVO(localFile, context);
		if (CollectionUtils.isEmpty(vos))
			throw new BusinessException(
					NCLangResOnserver.getInstance().getStrByID("excelimport", "ExcelImporter-000015"));
		List<ResponseMessage> responseMessages = new ArrayList<>();
		int currentSequence = 1;
		int totalsize = vos.size();
		context.setTotallines(totalsize);
		List<Object> returnobj = new ArrayList();
		for (AbstractAggregatedValueForExcel extendaggvalue : vos) {
			setProcessDecima(currentSequence, vos.size(), billtype + importSign);
			context.resetCurrentResponseMessage();
			ExcelProcessorByTemp billProcessortemp = new ExcelProcessorByTemp(context.getCurrentResponseMessage(),
					extendaggvalue);
			context.getCurrentResponseMessage().setContent(currentSequence + "");
			context.setCurrentlines(currentSequence);
			billProcessortemp.translate();
			if (!billProcessortemp.isSuccessTranslate()) {
				responseMessages.add(billProcessortemp.getResponseMessage());
				currentSequence++;
				continue;
			}
			Object obj = ((IImportProcessWithAffair) NCLocator.getInstance().lookup(IImportProcessWithAffair.class))
					.ProcessImport_RequiresNew_WithReturn(importprocess, extendaggvalue, context);
			if (context.getCurrentResponseMessage().getResultDescription() != null
					&& !"".endsWith(context.getCurrentResponseMessage().getResultDescription()))
				responseMessages.add(context.getCurrentResponseMessage());
			currentSequence++;
			returnobj.add(obj);
		}
		returnMap.put("fail", responseMessages);
		returnMap.put("success", returnobj);
		return returnMap;
	}

	public void processRegion(IXChangeContext context, final List<AbstractAggregatedValueForExcel> vos,
							  final String billtype, final String importSign) throws BusinessException {
		try {
			IImportProcessAction importprocessAction = PfxxPluginUtils.getImportProcessComplete(context);
			if (importprocessAction != null)
				importprocessAction.importProcessWithAllVOListAction(vos, context,
						new IImportProcessAction.IImportProgress() {
							public void startTranslate(AbstractAggregatedValueForExcel extendaggvalue, int index)
									throws BusinessException {
								ExcelImporterProcess.this.setProcessDecima(index, vos.size(), billtype + importSign);
							}

							public void runningImport(int progress) {
								NCCForUAPLogger.debug("runningImport.........." + progress);
							}

							public void endImport() {
							}
						});
		} catch (Exception se) {
			Throwable cause = ExceptionUtils.unmarsh(se);
			throw new BusinessException(cause.getMessage());
		}
	}

	//chesyk add HW ����������
	public  String  dealFile(File file) throws BusinessException {
		String log_msg = "";
		PoiWorkbook workbook = null;
		workbook = new PoiWorkbook(file.getPath(), "UTF-8");
		IExcelSheet sheet = workbook.getSheet(0);
		int rows = sheet.getRows();
		String custName = null;
		String pk_customer = null;
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		custName = (String) (sheet.getCell(1, 1).getValue() == null ? null
				: sheet.getCell(1, 1).getValue());
		if (custName == null || "".equals(custName)) {
			throw new BusinessException("��ά�������ļ���һ�пͻ�����");
		}else{
			String queryCust = " select pk_customer from bd_customer where name = '"+custName+"'";
			pk_customer = (String) queryBS.executeQuery(queryCust,
					new ColumnProcessor());
			if(pk_customer == null || "".equals(pk_customer)){
				throw new BusinessException("û�в�ѯ����Ӧ�ͻ�");
			}
		}
		DeliveryVO  deliveryVO  = new DeliveryVO ();
		DeliveryHVO  headvo = new DeliveryHVO ();

		UFDate daytime = new UFDate();
		log_msg = daytime.toDate() + "��ʼ���ɷ�����������������";

		// ��ͷ��ֵ
		headvo.setBillmaker(InvocationInfoProxy.getInstance().getUserId());


		// ���Ű汾
		String sql = "select pk_dept,pk_vid from org_dept_v where pk_org = (select pk_org from org_orgs where code = '2001' and isbusinessunit = 'Y')";
		DeptVersionVO deptvo = (DeptVersionVO) queryBS.executeQuery(sql, new BeanProcessor(DeptVersionVO.class));
		headvo.setCsenddeptvid (deptvo.getPk_vid()); //�������Ű汾�ݶ��� ��Ҫȷ��
		headvo.setCsenddeptid (deptvo.getPk_dept()) ;
		//	headvo.setCorpoid(WorkbenchEnvironment.getInstance().getLoginUser().getPk_org()); //��Ҫȷ��
		sql = "  select pk_billtypeid from bd_billtype where  pk_billtypecode  = '4331-Cxx-03'";//��������
		String ctrantypeid = (String) queryBS.executeQuery(sql, new ColumnProcessor());
		headvo.setCtrantypeid(ctrantypeid);//��������

		headvo.setDbilldate(daytime);
		headvo.setFstatusflag (1); //����̬
//			headvo.setPk_org(WorkbenchEnvironment.getInstance().getLoginUser().getPk_org());
		sql ="select pk_org, pk_vid from org_orgs_v where code ='2001'";//�������۵�Ԫ
		OrgVersionVO orgversionvo = (OrgVersionVO) queryBS.executeQuery(sql, new BeanProcessor(OrgVersionVO.class));
		headvo.setPk_org(orgversionvo.getPk_org());
		headvo.setPk_org_v(orgversionvo.getPk_vid());

		headvo.setPk_group("0001A3100000000004NA");
		headvo.setVtrantypecode("4331-Cxx-03");//�������ͱ���--��ͨ
		headvo.setCbiztypeid ("0001A210000000001E1W");//ҵ������

		UFDouble price = UFDouble.ZERO_DBL;
		Map<String, Integer> zbMap = new HashMap<String, Integer>(); // ���ָ���map
		zbMap.put("�������", 3);
		zbMap.put("TFe", 6);
		zbMap.put("SiO2", 7);
		zbMap.put("TiO2", 8);
		zbMap.put("S", 9);
		//	zbMap.put("����", i);
		zbMap.put("����", 1);
		zbMap.put("�������", 0);
		//	zbMap.put("ԭ������", i);
		zbMap.put("ˮ��", 10);
		zbMap.put("��ˮ����", 11);
		zbMap.put("��������", 12);
		zbMap.put("���㵥��", 15);
		zbMap.put("������", 16);


		ArrayList<DeliveryBVO > bodyvos = new ArrayList<DeliveryBVO >();
		DeliveryBVO  bodyVO = null;

		for (int row = 3; row < rows; row++) {
			bodyVO = new DeliveryBVO ();
			bodyVO.setAttributeValue("fbuysellflag", 1);
			bodyVO.setFbuysellflag(0);
			//���κźͳ���Ϊ��ʱ����
			if ( sheet.getCell(zbMap.get("����"), row).getValue() == null && sheet.getCell(zbMap.get("����"), row).getValue() == null )
				continue;
			if ( "".equals(sheet.getCell(zbMap.get("����"), row).getValue()) && "".equals(sheet.getCell(zbMap.get("����"), row).getValue()) )
				continue;
			// ���帳ֵ
			bodyVO.setCordercustid (pk_customer);//�ͻ�
			bodyVO.setCsendstordocid  ("1001A31000000007J3QG");// �����ֿ⣬��ȷ��
			bodyVO.setFbuysellflag (1);
			bodyVO.setCdeptvid("1001A31000000002BU98");//���۲���


			bodyVO.setCastunitid("1001A310000000003ZLT");//������λ
			bodyVO.setCinstordocid ("1001A31000000007J3QG");//�ջ��ֿ�  ��Ҫȷ��
			bodyVO.setCmaterialid ("1001A31000000005142V"); //1.02.01.04.001	������	�������  ���˵ֻ������һ������
			bodyVO.setCmaterialvid("1001A31000000005142V");//1.02.01.04.001	������	�������  ���˵ֻ������һ������
//				bodyVO.setCorpoid(WorkbenchEnvironment.getInstance().getLoginUser().getPk_org());
			bodyVO.setCrowno(10 * (row-3 + 1) + "");
			bodyVO.setCunitid("1001A310000000003ZLT");
//				bodyVO.setPk_org(WorkbenchEnvironment.getInstance().getLoginUser().getPk_org());
			bodyVO.setPk_org(orgversionvo.getPk_org());
			bodyVO.setCrececountryid("0001Z010000000079UJJ");//�ջ�����/����
			bodyVO.setCsendcountryid ("0001Z010000000079UJJ");//��������/����
			bodyVO.setCtaxcountryid ("0001Z010000000079UJJ");//��˰��/����
			bodyVO.setBtriatradeflag (UFBoolean.FALSE);//����ó��
			bodyVO.setFtaxtypeflag (1);//��˰���

			bodyVO.setBcheckflag (UFBoolean.FALSE);//�Ƿ��ѱ���
			bodyVO.setBusecheckflag (UFBoolean.FALSE);
			bodyVO.setCinstockorgvid (orgversionvo.getPk_vid());//�ջ������֯
			bodyVO.setCinstockorgid (orgversionvo.getPk_org());//�ջ������֯���°汾
			bodyVO.setCsettleorgid(orgversionvo.getPk_org());//���������֯
			bodyVO.setCsettleorgvid(orgversionvo.getPk_vid());//���������֯
			bodyVO.setCsendstockorgvid  (orgversionvo.getPk_vid());//���������֯
			bodyVO.setCsendstockorgid  (orgversionvo.getPk_org());//���������֯���°汾
			//�����Զ����ֵ
			bodyVO.setVbdef1(sheet.getCell(zbMap.get("����"), row).getValue().toString());
			//		bodyVO.setVbdef2(sheet.getCell(zbMap.get("����"), row).getValue().toString());
			bodyVO.setVbdef3(sheet.getCell(zbMap.get("�������"), row).getValue().toString());
			bodyVO.setVbdef4(sheet.getCell(zbMap.get("ˮ��"), row).getValue().toString());
			bodyVO.setVbdef5(sheet.getCell(zbMap.get("�������"), row).getValue().toString());//ʪ��
			bodyVO.setVbdef6(sheet.getCell(zbMap.get("��������"), row).getValue().toString());//����
			bodyVO.setVbdef7(sheet.getCell(zbMap.get("TFe"), row).getValue().toString());
			bodyVO.setVbdef8(sheet.getCell(zbMap.get("SiO2"), row).getValue().toString());
			bodyVO.setVbdef9(sheet.getCell(zbMap.get("TiO2"), row).getValue().toString());
			bodyVO.setVbdef10(sheet.getCell(zbMap.get("S"), row).getValue().toString());
			bodyVO.setVbdef14(custName);
			bodyVO.setVbdef11(sheet.getCell(zbMap.get("���㵥��"), row).getValue().toString());
			bodyVO.setVbdef12(sheet.getCell(zbMap.get("������"), row).getValue().toString());

			//��������
			UFDouble jssl= new UFDouble(sheet.getCell(zbMap.get("��������"), row).getValue().toString());
			// ������
			bodyVO.setNnum(jssl);
			// ����
			bodyVO.setNastnum (jssl);
			// ������
			bodyVO.setVchangerate("1.00/1.00");
			// ��λ��
			bodyVO.setCcurrencyid("1002Z0100000000001K1");
			// ����
			bodyVO.setCorigcurrencyid("1002Z0100000000001K1");
			// ˰��
			bodyVO.setCtaxcodeid("1001A31000000007IRDU");
			// ����˰����
			bodyVO.setNorigtaxnetprice(UFDouble.ZERO_DBL);
			// ����˰����
			bodyVO.setNorigprice(UFDouble.ZERO_DBL);
			// ����˰����
			bodyVO.setNorigtaxprice(UFDouble.ZERO_DBL);
			// ����˰����
			bodyVO.setNorignetprice(UFDouble.ZERO_DBL);
			//��˰���
			bodyVO.setNcaltaxmny (UFDouble.ZERO_DBL);
			//�۱�����
			bodyVO.setNexchangerate (UFDouble.ONE_DBL);
			bodyVO.setNorigmny (UFDouble.ONE_DBL);
			bodyVO.setNorigtaxmny (UFDouble.ONE_DBL);
			bodyVO.setNmny (UFDouble.ONE_DBL);
			bodyVO.setNtaxmny (UFDouble.ONE_DBL);
			bodyVO.setNtax (UFDouble.ONE_DBL);
			//bodyVO.setc
			//cqtunitid ���۵�λ
			bodyVO.setCqtunitid("1001A310000000003ZLT");
			bodyVO.setNqtunitnum(jssl);
			//nqtunitnum  ��������
			bodyVO.setVqtunitrate("1.00/1.00");
			//vqtunitrate ���ۻ�����
			// ��Ʊ�ͻ�
			bodyVO.setCinvoicecustid(pk_customer);
			bodyvos.add(bodyVO);
		}
		//�ȸ�ֵ���������ֵ���汨���ڱ���֮���ٸ���Ϊ��
		bodyVO.setCsrcbid ("cs");
		bodyVO.setCsrcid("cs");
		bodyVO.setVsrctype ("30");// ��������
		bodyVO.setDreceivedate(new UFDate());
		DeliveryBVO[] deliveryBVO = new DeliveryBVO[bodyvos.size()];
		bodyvos.toArray(deliveryBVO);
		deliveryVO.setParentVO(headvo);
		deliveryVO.setChildrenVO(deliveryBVO);
		// �������۳���
		DeliveryVO[] deliveryVOs=	NCLocator.getInstance().lookup(IDeliveryMaintain.class).insertDelivery(new DeliveryVO []{deliveryVO});
		//����֮�����ƥ��
		int notMatchRows=match(deliveryVOs[0]);

		DeliveryBVO[] newvos =	deliveryVOs[0].getChildrenVO();
		//����Ϊ��ȷ����Դ��������
		for(DeliveryBVO bvo:newvos){
			bvo.setDr(0);
			bvo.setVsrctype("");
			bvo.setCsrcbid ("");
			bvo.setCsrcid("");
		}
		((IUifService) NCLocator.getInstance().lookup(IUifService.class.getName())).updateAry(newvos);


		String message = "����ɹ�";
		if (  notMatchRows > 0 ) {
			message += ",�����ɵķ�����"+notMatchRows+"��δƥ��ɹ�����ת��-����ص�ƥ��-�ڵ�ά����Ӧ��ϵ��";
		}
		return  message;


	}
	/**
	 *
	 * @param deli
	 * @param custName ��Ӧ��
	 * @param ph ���κ�
	 * @param rq ����
	 * @param gjzl ʪ��
	 * @return
	 * @throws BusinessException
	 */
	private int match(DeliveryVO deliveryvo) throws BusinessException {
		IUAPQueryBS bs=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String cust="";//��Ӧ��
		String ph="";//���κ�
		String rq="";//����
		String sjzl=null;//ʪ��
		int notMatchRows=0;//��¼ƥ��ʧ�ܵ�����
		//��¼������δƥ��ɹ����к�
		//List rows=new ArrayList<>();
		DeliveryBVO[] bvos=deliveryvo.getChildrenVO();
		for(int a=0;a<bvos.length;a++){
			DeliveryBVO bvo=bvos[a];
			cust=bvo.getCordercustid();
			ph=bvo.getVbdef1();
			rq=bvo.getVbdef3();
			sjzl=bvo.getVbdef5();
			//������ʪ�غ�
			String sql =
					" select sum(vbdef19) sumsz from ic_saleout_b where "
							+ " ( vbdef16 <> 'Y' or vbdef16 is null)"
							+ " and casscustid = '" + cust + "' "
							+ " and vbdef1 = '"+ ph+"' "
							+ " and substr(vbdef18,0,10) = '"+ rq +"'"
							+ " and dr = 0 ";

			System.out.println(cust);
			System.out.println(ph);
			System.out.println(rq);

			Object n=bs.executeQuery(sql, new ColumnProcessor());

			if(n!=null&&new UFDouble(n.toString()) .compareTo(new UFDouble(sjzl.toString()))==0){//���ʪ�����õ��ڳ����ʪ�غͣ���ƥ��ɹ�
				//��ѯ�������������۳��ⵥ//20230220������۳��⽻������
				String sql_saleout=" ( vbdef16 <> 'Y' or vbdef16 is null) "
						+ " and casscustid = '" + cust + "' "+ " and vbdef1 = '"+ ph+"' "
						+ " and substr(vbdef18,0,10) = '"+ rq +"'"
						+ " and dr = 0 and cbodytranstypecode='4C-Cxx-04'";
				HYPubBO hypub= new HYPubBO();
				SaleOutBodyVO[] itemList=(SaleOutBodyVO[]) hypub.queryByCondition(SaleOutBodyVO.class, sql_saleout);

				AggSettlementHVO  agg=new AggSettlementHVO();
				SettlementHVO settlehvo=new SettlementHVO();
				settlehvo.setAttributeValue("pk_group",bvo.getPk_group());
				settlehvo.setAttributeValue("pk_org",bvo.getPk_org());
				settlehvo.setAttributeValue("transtype","KHJS-Cxx-01");//������������Ϊ��ϸ
				/** begin add by tbl **/
				settlehvo.setAttributeValue("transtype1","KHJS-Cxx-01");
				settlehvo.setAttributeValue("billtype","KHJS");
				settlehvo.setAttributeValue("pk_org_v",bvo.getPk_org());
				/** end **/
				String sql_transtype="select pk_billtypeid  from bd_billtype where pk_billtypecode ='KHJS-Cxx-01'";
				IUAPQueryBS iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
				settlehvo.setAttributeValue("transtypepk",iUAPQueryBS.executeQuery(sql_transtype, new ColumnProcessor()));//������������Ϊ��ϸ
				settlehvo.setAttributeValue("dbilldate", new UFDate());
				settlehvo.setAttributeValue("approvestatus", -1);//	����״̬
				settlehvo.setAttributeValue("creator",InvocationInfoProxy.getInstance().getUserId());//	����״̬
				settlehvo.setAttributeValue("vdef1", bvo.getPrimaryKey());//	����Ϊ��������������
				settlehvo.setAttributeValue("srcbilltype", "4331");//	��ͷ��Դ��������
				settlehvo.setAttributeValue("srcbillid", bvo.getCdeliveryid());//	��ͷ��Դ������������Ϊ��������ͷ����

				SettlementBVO settlebvo=new SettlementBVO();//���㵥����
				/** begin add by tbl **/
				settlebvo.setAttributeValue("pk_org",bvo.getPk_org());
				settlebvo.setAttributeValue("pk_org_v",bvo.getPk_org());
				/** end */
				settlebvo.setAttributeValue("cordercustid", bvo.getCordercustid());//�ͻ�
				settlebvo.setAttributeValue("cmaterialid", bvo.getCmaterialid());//���ϵ���
				settlebvo.setAttributeValue("cmaterialvid", bvo.getCmaterialvid());//���ϵ���
				settlebvo.setAttributeValue("castunitid", bvo.getCastunitid());//��λ
				settlebvo.setAttributeValue("nastnum", bvo.getNastnum());//����
				settlebvo.setAttributeValue("cunitid", bvo.getCunitid());//����λ
				settlebvo.setAttributeValue("nnum", bvo.getNnum());//����λ
				settlebvo.setAttributeValue("cunitid", bvo.getCunitid());//������
				settlebvo.setAttributeValue("wet_weight", bvo.getVbdef5());//ʪ��
				settlebvo.setAttributeValue("dry_weight", bvo.getVbdef6());//����
				settlebvo.setAttributeValue("money", bvo.getVbdef12());//���
				settlebvo.setAttributeValue("unitprice", bvo.getVbdef11());//����
				settlebvo.setAttributeValue("batch", bvo.getVbdef1());//����
				settlebvo.setAttributeValue("rowno", 10);
				String keys="";//���۳�����������ϼ�
				for(int i=0 ;i<itemList.length;i++){
					SaleOutBodyVO saleoutbodyvo=itemList[i];
					bvo.setAttributeValue("csrcbid", saleoutbodyvo.getPrimaryKey());//����
					if(keys.equals("")){
						keys=saleoutbodyvo.getPrimaryKey();
					}else{
						keys=keys+"+"+saleoutbodyvo.getPrimaryKey();
					}
					saleoutbodyvo.setVbdef16("Y");
					saleoutbodyvo.setAttributeValue("dr", 0);
				}
				settlebvo.setAttributeValue("saleoutkeys", keys);
				agg.setParentVO(settlehvo);
				agg.setChildrenVO(new SettlementBVO[]{settlebvo});
				IPFBusiAction ipfBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class);
				AggSettlementHVO[] settleVOs= (AggSettlementHVO[]) ipfBusiAction.processAction("SAVEBASE","KHJS" , null,agg , null, null);

				//����֮���д�����������۳���
				IVOPersistence ivop = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName());
				ivop.updateVOArray(itemList);
				bvo.setVbdef16(settleVOs[0].getParentVO().getPrimaryKey());//�������Զ�����16Ϊ���ݺ�
				bvo.setAttributeValue("dr", 0);
				ivop.updateVO(bvo);

			}else{
				//rows.add(a);
				notMatchRows=notMatchRows+1;
				//throw new BusinessException(sql);

			}
		}

		return notMatchRows;
	}


}