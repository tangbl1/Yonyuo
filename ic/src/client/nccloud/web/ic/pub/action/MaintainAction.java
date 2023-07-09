package nccloud.web.ic.pub.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.ic.general.define.MetaNameConst;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.scale.TotalValueScale;
import nc.vo.scmpub.res.billtype.ICBillType;
import nc.vo.scmpub.util.AppInfoContext;
import nccloud.dto.ic.pub.entity.BillVOWithExtendInfo;
import nccloud.dto.scmpub.script.entity.SCMScriptResultDTO;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.convert.precision.GridPrecisionOperator;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.billcard.BillCard;
import nccloud.framework.web.ui.pattern.billcard.BillCardFormulaHandler;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.framework.web.ui.pattern.grid.GridOperator;
import nccloud.pubitf.ic.pub.service.IICBillVOQueryService;
import nccloud.pubitf.riart.pflow.CloudPFlowContext;
import nccloud.pubitf.scmpub.commit.service.IBatchRunScriptService;
import nccloud.web.ic.inbound.entity.ICBatchDelInfoDTO;
import nccloud.web.ic.inbound.entity.ICBatchDelInfosDTO;
import nccloud.web.ic.inbound.entity.ICBodyInfo;
import nccloud.web.ic.pub.precision.ICBaseBillcardPrecisionHandler;
import nccloud.web.ic.pub.resexp.ICPFResumeExceptionUtil;
import nccloud.web.ic.ui.pattern.billcard.ICBillCardOperator;
import nccloud.web.scmpub.pub.action.DataPermissionAction;
import nccloud.web.scmpub.pub.assign.CommitAssignUtils;
import nccloud.web.scmpub.pub.resexp.PfResumeExceptionNccUtils;
import nccloud.web.scmpub.pub.utils.compare.SCMCompareUtil;
import nccloud.web.scmpub.pub.utils.scale.GridTotalValueScaleProcessor;

/**
 * @author lihaos
 * @date 2018-4-27 下午4:10:36
 * @version ncc1.0
 */
public class MaintainAction extends DataPermissionAction implements
		ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String actioncode = null;

		String pagecode = null;

		ICBillCardOperator operator;
		CloudPFlowContext context = ICPFResumeExceptionUtil
				.processBefore(request);
		String str = request.read();
		IJson json = JsonFactory.create();
		ICBatchDelInfosDTO infoo = json.fromJson(str, ICBatchDelInfosDTO.class);
		ICBatchDelInfoDTO[] infos = infoo.getDelinfos();

		if (infos[0].getActionname().equals(IPFActionName.SAVE)) {
			// 出库申请-提交指派
			CommitAssignUtils.before(request);
		}

		pagecode = infos[0].getPagecode();
		boolean isCard = infos[0].isCard();
		operator = new ICBillCardOperator(null, pagecode,
				infos[0].getUserjson());

		// ICBillVO[] aggvo = this.getAggVOS(infos);
		List<String> ids = new ArrayList<String>();
		Map<String, UFDateTime> tss = new HashMap<>();
		Map<String, UFDateTime> btss = new HashMap<>();
		for (ICBatchDelInfoDTO info : infos) {
			ids.add(info.getId());
			tss.put(info.getId(), info.getTs());
			//处理表体ts
			ICBodyInfo[] bodyInfos = info.getBodys();
			if (bodyInfos != null && bodyInfos.length > 0) {
				for (ICBodyInfo bodyInfo : bodyInfos) {
					btss.put(bodyInfo.getId(), bodyInfo.getTs());
				}
			}
		}
		// 查询数据
		IICBillVOQueryService service = ServiceLocator
				.find(IICBillVOQueryService.class);
		ICBillVO[] aggvoqry = (ICBillVO[]) service.query(
				ICBillType.getICBillType(getBilltype()),
				ids.toArray(new String[ids.size()]));
		// 把前台传过来的ts更新到查询出来的数据中
		if (aggvoqry.length < 1) {
			ExceptionUtils
					.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("4008027_0",
									"04008027-0192")/*
													 * @res
													 * "你操作的数据已经被他人修改，请刷新界面"
													 */);
		}
		for (ICBillVO icBillVO : aggvoqry) {
			icBillVO.getParentVO().setTs(tss.get(icBillVO.getPrimaryKey()));
			if (btss.size() > 0) {
				ICBillBodyVO[] bodys = icBillVO.getBodys();
				for (ICBillBodyVO body : bodys) {
					body.setTs(btss.get(body.getPrimaryKey()));
				}
			}	
		}
		if (null != infos[0].getOprcode() && !infos[0].getOprcode().equals("commit")
				&& !infos[0].getOprcode().equals("uncommit")) {
			actioncode = infos[0].getOprcode();
			// 校验权限
			this.checkPermission(aggvoqry, actioncode);
		}
		//操作前校验，子类去重写
		this.checkBillVOs(aggvoqry);
		
		// vos签字前其它处理，子类去重写
		this.dealOtherOperation(aggvoqry);
		// 动作脚本执行
		context.setBillType(this.getBilltype());
		context.setActionName(infos[0].getActionname());
		context.setBillVos(aggvoqry);
		// 设置原始卡片，用于合并
		if (isCard) {
			BillCard origincard = operator.toCard(aggvoqry[0], false,false);
			operator.setOriginBillcard(origincard);
		}

		try {
			AppInfoContext.setBtnCode(getBtnCode(actioncode));
			SCMScriptResultDTO obj = ServiceLocator.find(
					IBatchRunScriptService.class).runBacth(context,
					this.getVOClass());

			// 提交指派(提交时处理)
			if (infos[0].getActionname().equals(IPFActionName.SAVE)) {
				Object data = obj.getData();
				if (data != null && data instanceof Map) {
					return data;
				}
			}
			processResult(obj, isCard, pagecode, operator);
			return obj;
		} catch (Exception e) {
			return PfResumeExceptionNccUtils.handleResumeException(e);
		}
	}
	public String getBtnCode(String actioncode) {
		return null;
	}

	/**
	 * 处理返回的dto，将其中的成功vo转化为前端可以识别的billcard或者grid，并处理精度
	 * 
	 * @param dto
	 * @param isCard
	 * @param pagecode
	 */
	private void processResult(SCMScriptResultDTO dto, boolean isCard,
			String pagecode, ICBillCardOperator operator) {
		AbstractBill[] sucessVOs = dto.getSucessVOs();
		if (sucessVOs == null || sucessVOs.length == 0 || sucessVOs[0] == null) {
			return;
		}
		if (isCard) {
			dto.setData(processCardBill(sucessVOs[0], pagecode, operator));
			dto.setSucessVOs(null);
		} else {
			dto.setData(processListBill(sucessVOs, pagecode));
			dto.setSucessVOs(null);
		}
	}

	/**
	 * 卡片成功数据的处理，根据聚合vo，返回billcard或者扩展info，并处理精度
	 * 
	 * @param bill
	 * @return
	 */
	private Object processCardBill(AbstractBill bill, String pagecode, ICBillCardOperator operator) {
		if (bill instanceof ICBillVO) {
			return processGeneralBill((ICBillVO) bill, pagecode, operator);
		}
		return null;
	}

	/**
	 * 列表操作成功数据的处理，根据聚合vo数组(单个或多个),返回列表要显示的grid，并处理精度
	 * 
	 * @param bills
	 * @return
	 */
	private Object processListBill(AbstractBill[] bills, String pagecode) {
		GridOperator operator = new GridOperator(pagecode);
		Grid grid = operator.toGrid(VOEntityUtil.getHeadVOs(bills));
		afterProcess(grid);
		return grid;
	}

	private Object processGeneralBill(ICBillVO bill, String pagecode, ICBillCardOperator operator) {
		BillVOWithExtendInfo extendinfo = ServiceLocator.find(
				IICBillVOQueryService.class).queryBillExtByVO(bill);
		// 查询后处理，解析查询回的数据
		BillCard retcard = this.afterProcess(pagecode,operator,
				extendinfo.getBillvo());

		// 处理icbillstatus
		operator.processICBillstatusForWeb(retcard);

		BillCardFormulaHandler handler = new BillCardFormulaHandler(retcard);
		handler.handleLoadFormula();
		handler.handleBodyLoadFormula();
		Map<String, Object> retmap = new HashMap<String, Object>();
		SCMCompareUtil.compareBillCard(operator.getOriginBillcard(),
				retcard, getBodyPKField());
		operator.translate(retcard);
		retmap.put("billcard", retcard);
		retmap.put("isLocationManaged", extendinfo.isLocationManaged());
		retmap.put("materialSerialMap", extendinfo.getMaterialSerialMap());
		retmap.put("materialSnunitMap", extendinfo.getMaterialSnunitMap());
		return retmap;
	}

	/**
	 * 查询后处理 查询会的数据只有主键信息，后处理可以将相关的信息全部查询出，然后将数据中每个字段组装成有display和value属性的格式
	 * 
	 * @param pageid
	 *            模板ID
	 * @param vo
	 *            查询的未组装的信息，这部分信息中个字段都还只是主键
	 * @return 返回组装后的数据
	 * 
	 */
	public BillCard afterProcess(String pageid, ICBillCardOperator operator, ICBillVO vo) {
		BillCard retcard = operator.toCard(vo, false,false);

		ICBaseBillcardPrecisionHandler handler = getPrecisionHandler(retcard);
		handler.process();

		return retcard;
	}

	protected String getBodyPKField() {
		return MetaNameConst.CGENERALBID;
	}

	/**
	 * 获取精度处理类，子类可复写
	 * 
	 * @param precision
	 * @return
	 */
	protected ICBaseBillcardPrecisionHandler getPrecisionHandler(BillCard card) {
		return new ICBaseBillcardPrecisionHandler(card);
	}

	/**
	 * 列表grid返回前处理(精度)
	 * 
	 * @param grid
	 */
	protected void afterProcess(Grid grid) {
		GridPrecisionOperator scale = new GridPrecisionOperator(grid);
		TotalValueScale totalvaluescale = new GridTotalValueScaleProcessor(grid);
		String[] headkeys = new String[] { ICPubMetaNameConst.NTOTALNUM,
				MetaNameConst.NTOTALPIECE, MetaNameConst.NTOTALVOLUME,
				MetaNameConst.NTOTALWEIGHT };
		totalvaluescale.setHeadTailKeys(headkeys);
		scale.processPrecision();
	}

	// 子类去重写
	protected ICBillVO[] getAggVOS(ICBatchDelInfoDTO[] infos) {
		return null;
	}
	
	// 操作前校验，子类去重写
	protected void checkBillVOs(ICBillVO[] billVOs) {
		return;
	}

	// vos签字前其它处理，子类去重写
	protected void dealOtherOperation(ICBillVO[] billVOs) {
		return;
	}

	// 子类去重写
	protected String getBilltype() {
		return null;
	}

	// 子类去重写
	protected Class<?> getVOClass() {
		return null;
	}

	@Override
	public String getPermissioncode() {

		return null;
	}

	@Override
	public String getActioncode() {
		return null;
	}

	@Override
	public String getBillCodeField() {
		return ICPubMetaNameConst.VBILLCODE;
	}

}