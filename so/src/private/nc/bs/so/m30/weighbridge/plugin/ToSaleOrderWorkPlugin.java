package nc.bs.so.m30.weighbridge.plugin;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.persist.framework.MDPersistenceService;
import nc.pubitf.so.m30.api.ISaleOrderMaintainAPI;
import nc.vo.pub.BusinessException;
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
import uap.mw.trans.TransactionFactory;
import uap.mw.trans.UAPUserTransanction;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToSaleOrderWorkPlugin implements IBackgroundWorkPlugin{

	String log_msg = "";
	LogMsgVO logVo = new LogMsgVO();
	private UAPUserTransanction trans = TransactionFactory.getUTransaction();
	public PreAlertObject executeTask(BgWorkingContext arg0)
			throws BusinessException {
		runTask();
		return null;
	}

	public void runTask() {
		UFDate daytime = new UFDate();
		log_msg = daytime.toDate() + "开始生成销售订单。。。。。。";
		UFDouble price = UFDouble.ZERO_DBL;
		try {
			 String sql = "select cuserid from sm_user where user_code = 'XS001'";
			 String billmaker = (String) dao.executeQuery(sql, new ColumnProcessor());//默认制单人XS001
			//更新的数据 
			ArrayList<Wb01VO>  updatesalelist = getUpdateWeighbridgeInfo();
			for(Wb01VO wbvo:updatesalelist){
				//根据流水号和序号查询对应的销售订单
				 List oldsaleordervolist = (List)MDPersistenceService.lookupPersistenceQueryService() 
						.queryBillOfVOByCond(SaleOrderVO.class, " vdef1 = '"+wbvo.getXh()+"' "
						+" and vdef2 = '"+wbvo.getLsh()+"' "	+ "and dr = 0 ", false);
				SaleOrderVO  oldsaleordervo=(SaleOrderVO) oldsaleordervolist.get(0);
				SaleOrderHVO hvo = oldsaleordervo.getParentVO();
				// 客户
				String ccustomerid = matchCustomer(wbvo);
				hvo.setCcustomerid(ccustomerid);
				hvo.setCcustomervid(ccustomerid);
				hvo.setChreceivecustid(ccustomerid);
				hvo.setChreceivecustvid(ccustomerid);

				SaleOrderBVO bvo=oldsaleordervo.getChildrenVO()[0];
				bvo.setVbdef2(wbvo.getCh());
				// 地磅类型
				bvo.setVbdef3(wbvo.getWbtype());
				// 发货单位
				if ( wbvo.getSh_org() != null )
					bvo.setVbdef5(wbvo.getSh_org().toString());
				// 毛重
				if ( wbvo.getMz() != null )
					bvo.setVbdef6(wbvo.getMz().div(new UFDouble("1000"),2).toString());
				// 皮重
				if ( wbvo.getPz() != null )
					bvo.setVbdef7(wbvo.getPz().div(new UFDouble("1000"),2).toString());
				// 净重
				if ( wbvo.getJz() != null )
					bvo.setVbdef8(wbvo.getJz().div(new UFDouble("1000"),2).toString());
				// 扣重
				if ( wbvo.getKz() != null )
					bvo.setVbdef9(wbvo.getKz().div(new UFDouble("1000"),2).toString());
				// 实重
				if ( wbvo.getSz() != null )
					bvo.setVbdef10(wbvo.getSz().div(new UFDouble("1000"),2).toString());
				// 毛重司磅员			
				bvo.setVbdef11(wbvo.getMz_psn());
				// 毛重时间
				if ( wbvo.getMz_datetime() != null )
					bvo.setVbdef12(wbvo.getMz_datetime().toString());
				// 皮重司磅员
				bvo.setVbdef13(wbvo.getPz_psn());
				// 皮重时间
				if ( wbvo.getPz_datetime() != null )
					bvo.setVbdef14(wbvo.getPz_datetime().toString());
				// 更新时间
				if ( wbvo.getUpdatetime() != null ) 
					bvo.setVbdef17(wbvo.getPz_datetime().toString());
				// 发货时间
				if ( wbvo.getDelivery_time() != null ) 
					bvo.setVbdef18(wbvo.getDelivery_time().toString());
				dao.updateVO(hvo);
				dao.updateVO(bvo);
			}
			
			
			ArrayList<Wb01VO> wbvolist = getWeighbridgeInfo();
			log_msg +="称重信息中获取" + wbvolist.size() + "条数据,";
			SaleOrderVO[] sos = new SaleOrderVO[wbvolist.size()];
			int k=0;
			log_msg +="开始拼装销售订单vo,";
			//插入的数据
			for(Wb01VO wbvo:wbvolist){
				//SaleOrderBVO[] bvos = new SaleOrderBVO[bvoslist.size()];
				/**********************************表头************************************************/
				SaleOrderVO svo = new SaleOrderVO();
				SaleOrderHVO hvo = new SaleOrderHVO();
				// 获取单据号
				//IBillcodeManage iBillcodeManage = (IBillcodeManage) NCLocator.getInstance().lookup(IBillcodeManage.class.getName());
				//billcode = iBillcodeManage.getBillCode_RequiresNew("30","0001A3100000000004NA", "0001A2100000000027QM",hvo);
				// 序号
				hvo.setVdef1(wbvo.getXh()+"");
				// 流水号
				hvo.setVdef2(wbvo.getLsh());
				// 集团
				hvo.setPk_group("0001A3100000000004NA");	
				sql ="select pk_org, pk_vid from org_orgs_v where code ='2001'";//东洋销售单元
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
				if ( wbvo.getPz_datetime() != null ){//设置为皮重日期
					hvo.setDbilldate(wbvo.getPz_datetime().getDate());
					hvo.setDmakedate(wbvo.getPz_datetime().getDate());
				}else{
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
				if ( wbvo.getSh_org() != null )
					bvo.setVbdef5(wbvo.getSh_org().toString());
				// 毛重
				if ( wbvo.getMz() != null )
					bvo.setVbdef6(wbvo.getMz().div(new UFDouble("1000"),2).toString());
				// 皮重
				if ( wbvo.getPz() != null )
					bvo.setVbdef7(wbvo.getPz().div(new UFDouble("1000"),2).toString());
				// 净重
				if ( wbvo.getJz() != null )
					bvo.setVbdef8(wbvo.getJz().div(new UFDouble("1000"),2).toString());
				// 扣重
				if ( wbvo.getKz() != null )
					bvo.setVbdef9(wbvo.getKz().div(new UFDouble("1000"),2).toString());
				// 实重
				if ( wbvo.getSz() != null )
					bvo.setVbdef10(wbvo.getSz().div(new UFDouble("1000"),2).toString());
				// 毛重司磅员			
				bvo.setVbdef11(wbvo.getMz_psn());
				// 毛重时间
				if ( wbvo.getMz_datetime() != null )
					bvo.setVbdef12(wbvo.getMz_datetime().toString());
				// 皮重司磅员
				bvo.setVbdef13(wbvo.getPz_psn());
				// 皮重时间
				if ( wbvo.getPz_datetime() != null )
					bvo.setVbdef14(wbvo.getPz_datetime().toString());
				// 更新时间
				if ( wbvo.getUpdatetime() != null ) 
					bvo.setVbdef17(wbvo.getPz_datetime().toString());
				// 发货时间
				if ( wbvo.getDelivery_time() != null ) 
					bvo.setVbdef18(wbvo.getDelivery_time().toString());
				svo.setParent(hvo);
				svo.setChildrenVO(new SaleOrderBVO[]{bvo});
				sos[k] = svo;
				k++;
			}		
			if ( sos.length != 0 ) {
				SaleOrderVO[] vos=NCLocator.getInstance().lookup(ISaleOrderMaintainAPI.class).insertBills(sos);
				List<SaleOrderHVO> hvos=new ArrayList<>();
				for(SaleOrderVO vo:vos){
					SaleOrderHVO newhvo=vo.getParentVO();
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
			log_msg +="执行sql，生成" + sos.length + "条销售订单完成.";
		} catch (Exception e) {
				log_msg +="报错信息：" + e.getMessage();
			logVo.setMsg(log_msg); 
			logVo.setOptype("saleorder");
			try {
				trans.begin();
				getBaseDao().insertVO(logVo);
			      trans.commit();
			} catch (IllegalStateException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}  catch (HeuristicRollbackException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			} catch (HeuristicMixedException | NotSupportedException | SystemException |
					 javax.transaction.RollbackException | DAOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
		logVo.setTs(new UFDateTime());
		if ( !log_msg.contains("报错信息") ) {
			logVo.setMsg(log_msg); 
			logVo.setOptype("saleorder");
			try {
				getBaseDao().insertVO(logVo);
			} catch (DAOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	

	private void updateWeightInfo() {
		HashMap<String, ArrayList<Wb01VO>> res = new HashMap<String, ArrayList<Wb01VO>>();
		try {
			UFDateTime daytime = new UFDateTime(new java.util.Date());		
			String beginBeforeDay = daytime.getDateTimeBefore(2).toString().substring(0, 10) + " 00:00:00";
			String endBeforeDay = daytime.getDateTimeBefore(1).toString().substring(0, 10) + " 23:59:59";
//			beginBeforeDay ="2021-02-07 00:00:00";
//            endBeforeDay="2021-02-08 23:59:59";
			String sql = " update weighbridge01 set def01 = 'Y' "
					+ "where updatetime >= '" + beginBeforeDay + "' and updatetime < '" + endBeforeDay + "'";
			getBaseDao().executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	static BaseDAO dao = null;

	public BaseDAO getBaseDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	
	/**
	 * 收货单位匹配NC客户 测试默认 抚顺新钢铁有限责任公司
	 * @param vo
	 * @return
	 */
	private String matchCustomer(Wb01VO vo) {
		String sh_org_name = vo.getSh_org();
		try {	
			String sql = "select pk_customer from bd_customer where name = '" +sh_org_name+ "'";
			String str = (String)getBaseDao().executeQuery(sql, new ColumnProcessor());
			if ( str != null)
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ( "抚顺市隆泰工贸有限公司".equals(sh_org_name) ) { 
			return "1001A310000000002LE6";
		}
		return "1001A310000000002LBW";
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<Wb01VO> getWeighbridgeInfo() {
		ArrayList<Wb01VO> list = new ArrayList<Wb01VO>();
		try {
			UFDateTime daytime = new UFDateTime(new java.util.Date());		
			String beginBeforeDay = daytime.getDateTimeBefore(2).toString().substring(0, 10) + " 00:00:00";
			String endBeforeDay = daytime.getDateTimeBefore(1).toString().substring(0, 10) + " 23:59:59";
//			beginBeforeDay ="2021-07-27 00:00:00";
//            endBeforeDay="2021-07-27 23:59:59";
			String sql = " select * from weighbridge01 where  def01 <> 'Y'"
					+ " and updatetime >= '" + beginBeforeDay + "' and updatetime < '" + endBeforeDay + "'";
			log_msg += "执行sql查出符合生成销售订单的地磅信息，sql：" + sql + ",";
			list = (ArrayList<Wb01VO>)getBaseDao().executeQuery(sql, new BeanListProcessor(Wb01VO.class));		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	@SuppressWarnings("unchecked")
	private ArrayList<Wb01VO> getUpdateWeighbridgeInfo() {
		ArrayList<Wb01VO> list = new ArrayList<Wb01VO>();
		try {
			UFDateTime daytime = new UFDateTime(new java.util.Date());		
			String beginBeforeDay = daytime.getDateTimeBefore(2).toString().substring(0, 10) + " 00:00:00";
			String endBeforeDay = daytime.getDateTimeBefore(1).toString().substring(0, 10) + " 23:59:59";
//			beginBeforeDay ="2021-02-07 00:00:00";
//            endBeforeDay="2021-02-08 23:59:59";
			String sql = " select * from weighbridge01 where def01 = 'Y' and isupdate='Y' "
					+ "and updatetime >= '" + beginBeforeDay + "' and updatetime < '" + endBeforeDay + "'";
			log_msg += "执行sql查出符合生成销售订单的地磅信息，sql：" + sql + ",";
			list = (ArrayList<Wb01VO>)getBaseDao().executeQuery(sql, new BeanListProcessor(Wb01VO.class));		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	

}
