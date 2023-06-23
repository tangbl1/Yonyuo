package nc.bs.so.m30.weighbridge.plugin;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.mw.naming.TransactionFactory;
import nc.bs.pub.SystemException;
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
import uap.mw.trans.UAPUserTransanction;

//import javax.transaction.*;
import javax.persistence.RollbackException;
import javax.resource.NotSupportedException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToSaleOrderWorkPlugin2 implements IBackgroundWorkPlugin{

	String log_msg = "";
	LogMsgVO logVo = new LogMsgVO();
	private UAPUserTransanction trans = TransactionFactory.getUTransaction();
	public PreAlertObject executeTask(BgWorkingContext arg0)
			throws BusinessException {
		
		UFDate daytime = new UFDate();
		log_msg = daytime.toDate() + "��ʼ�������۶���������������";
		UFDouble price = UFDouble.ZERO_DBL;
		try {
			 String sql = "select cuserid from sm_user where user_code = 'XS001'";
			 String billmaker = (String) dao.executeQuery(sql, new ColumnProcessor());//Ĭ���Ƶ���XS001
			//���µ����� 
			ArrayList<Wb01VO>  updatesalelist = getUpdateWeighbridgeInfo();
			for(Wb01VO wbvo:updatesalelist){
				//������ˮ�ź���Ų�ѯ��Ӧ�����۶���
				 List oldsaleordervolist = (List)MDPersistenceService.lookupPersistenceQueryService() 
						.queryBillOfVOByCond(SaleOrderVO.class, " vdef1 = '"+wbvo.getXh()+"' "
						+" and vdef2 = '"+wbvo.getLsh()+"' "	+ "and dr = 0 ", false);
				SaleOrderVO  oldsaleordervo=(SaleOrderVO) oldsaleordervolist.get(0);
				SaleOrderHVO hvo = oldsaleordervo.getParentVO();
				// �ͻ�
				String ccustomerid = matchCustomer(wbvo);
				hvo.setCcustomerid(ccustomerid);
				SaleOrderBVO bvo=oldsaleordervo.getChildrenVO()[0];
				bvo.setVbdef2(wbvo.getCh());
				// �ذ�����
				bvo.setVbdef3(wbvo.getWbtype());
				// ������λ
				if ( wbvo.getSh_org() != null )
					bvo.setVbdef5(wbvo.getSh_org().toString());
				// ë��
				if ( wbvo.getMz() != null )
					bvo.setVbdef6(wbvo.getMz().div(new UFDouble("1000"),2).toString());
				// Ƥ��
				if ( wbvo.getPz() != null )
					bvo.setVbdef7(wbvo.getPz().div(new UFDouble("1000"),2).toString());
				// ����
				if ( wbvo.getJz() != null )
					bvo.setVbdef8(wbvo.getJz().div(new UFDouble("1000"),2).toString());
				// ����
				if ( wbvo.getKz() != null )
					bvo.setVbdef9(wbvo.getKz().div(new UFDouble("1000"),2).toString());
				// ʵ��
				if ( wbvo.getSz() != null )
					bvo.setVbdef10(wbvo.getSz().div(new UFDouble("1000"),2).toString());
				// ë��˾��Ա			
				bvo.setVbdef11(wbvo.getMz_psn());
				// ë��ʱ��
				if ( wbvo.getMz_datetime() != null )
					bvo.setVbdef12(wbvo.getMz_datetime().toString());
				// Ƥ��˾��Ա
				bvo.setVbdef13(wbvo.getPz_psn());
				// Ƥ��ʱ��
				if ( wbvo.getPz_datetime() != null )
					bvo.setVbdef14(wbvo.getPz_datetime().toString());
				// ����ʱ��
				if ( wbvo.getUpdatetime() != null ) 
					bvo.setVbdef17(wbvo.getPz_datetime().toString());
				// ����ʱ��
				if ( wbvo.getDelivery_time() != null ) 
					bvo.setVbdef18(wbvo.getDelivery_time().toString());
				dao.updateVO(hvo);
				dao.updateVO(bvo);
			}
			
			
			ArrayList<Wb01VO> wbvolist = getWeighbridgeInfo();
			log_msg +="������Ϣ�л�ȡ" + wbvolist.size() + "������,";
			SaleOrderVO[] sos = new SaleOrderVO[wbvolist.size()];
			int k=0;
			log_msg +="��ʼƴװ���۶���vo,";
			//���������
			for(Wb01VO wbvo:wbvolist){
				//SaleOrderBVO[] bvos = new SaleOrderBVO[bvoslist.size()];
				/**********************************��ͷ************************************************/
				SaleOrderVO svo = new SaleOrderVO();
				SaleOrderHVO hvo = new SaleOrderHVO();
				// ��ȡ���ݺ�
				//IBillcodeManage iBillcodeManage = (IBillcodeManage) NCLocator.getInstance().lookup(IBillcodeManage.class.getName());
				//billcode = iBillcodeManage.getBillCode_RequiresNew("30","0001A3100000000004NA", "0001A2100000000027QM",hvo);
				// ���
				hvo.setVdef1(wbvo.getXh()+"");
				// ��ˮ��
				hvo.setVdef2(wbvo.getLsh());
				// ����
				hvo.setPk_group("0001A3100000000004NA");	
				sql ="select pk_org, pk_vid from org_orgs_v where code ='2001'";//�������۵�Ԫ
				OrgVersionVO orgversionvo = (OrgVersionVO) dao.executeQuery(sql, new BeanProcessor(OrgVersionVO.class));
				// ������֯ 
				hvo.setPk_org(orgversionvo.getPk_org()); //�������۵�Ԫ
				// ������֯�汾
				hvo.setPk_org_v(orgversionvo.getPk_vid()); //�������۵�Ԫ
				
				sql = "select pk_billtypeid from bd_billtype where  pk_billtypecode  = '30-Cxx-01'";
				String ctrantypeid = (String) dao.executeQuery(sql, new ColumnProcessor());
				// ��������
				hvo.setCtrantypeid(ctrantypeid);
				// �������ͱ���
				hvo.setVtrantypecode("30-Cxx-01");//������ͨ����
				// ҵ������
				sql = "select pk_busitype from bd_busitype where busicode  = 'XS001'";//�µ�����
				String pk_busitype = (String) dao.executeQuery(sql, new ColumnProcessor());
				hvo.setCbiztypeid(pk_busitype);
				// ���ݱ���
				//hvo.setVbillcode(billcode);
				// ��������
				if ( wbvo.getPz_datetime() != null ){//����ΪƤ������
					hvo.setDbilldate(wbvo.getPz_datetime().getDate());
					hvo.setDmakedate(wbvo.getPz_datetime().getDate());
				}else{
					hvo.setDbilldate(daytime);
					hvo.setDmakedate(daytime);
				}
					
				// �ͻ�
				String ccustomerid = matchCustomer(wbvo);
				hvo.setCcustomerid(ccustomerid);
				
				// ���Ű汾
				 sql = "select pk_dept,pk_vid from org_dept_v where pk_org = (select pk_org from org_orgs where code = '2001' and isbusinessunit = 'Y')";
				 DeptVersionVO deptvo = (DeptVersionVO) dao.executeQuery(sql, new BeanProcessor(DeptVersionVO.class));
				hvo.setCdeptvid(deptvo.getPk_vid());
				// ����
				hvo.setCdeptid(deptvo.getPk_dept());
				// ��������
				hvo.setFstatusflag(1);
				// ��������
				hvo.setFpfstatusflag(-1);
				hvo.setBillmaker(billmaker);//Ĭ���Ƶ���XS001

				/**********************************����************************************************/			
				// ���� ��Ϊ��λ �� ��1000
				UFDouble jz = wbvo.getJz().div(1000);
				SaleOrderBVO bvo = new SaleOrderBVO();
				
				//���������֯���°汾
				bvo.setCsettleorgid(orgversionvo.getPk_org());
				//���������֯ 
				bvo.setCsettleorgvid(orgversionvo.getPk_vid());
				// ����
				bvo.setCmaterialid("1001A31000000005142V");
				// ���ϱ���
				bvo.setCmaterialvid("1001A31000000005142V");
				// ˰�� 
				bvo.setCtaxcodeid("1001A31000000007IRDU");
				// ��˰���
				bvo.setFtaxtypeflag(1);
				bvo.setNtaxrate(new UFDouble(13));
				//���Һ͹�������
				bvo.setCrececountryid("0001Z010000000079UJJ");
				bvo.setCtaxcountryid("0001Z010000000079UJJ");
				bvo.setCsendcountryid("0001Z010000000079UJJ");
				bvo.setFbuysellflag(1);
				/*****************************������������ͽ��********************************************/
				// ����
				bvo.setNastnum(new UFDouble(1));
				// ��˰���� Ĭ��ֵ1
				bvo.setNqtorigprice(new UFDouble(1));
				// ���۵�λ����
				bvo.setNqtunitnum(new UFDouble(1));		
				// ��˰���� Ĭ��ֵ1.13
				bvo.setNqtorigtaxprice(new UFDouble(1.13));
				// ��˰���� Ĭ��ֵ1
				bvo.setNqtorigprice(new UFDouble(1));
				// ��˰���� ����*1.13
				bvo.setNqtorigtaxnetprc(new UFDouble(1.13));
				// ��˰���� ����*1
				bvo.setNqtorignetprice(new UFDouble(1));
				// 
				bvo.setNorignetprice(new UFDouble(1));
				// ��������˰����
				bvo.setNnetprice(new UFDouble(1));
				// ������˰���
				bvo.setNmny(new UFDouble(1));
				// ���Ҽ�˰�ϼ�
				bvo.setNtaxmny(new UFDouble(1.13));	
				//��λ��
				bvo.setCcurrencyid("1002Z0100000000001K1");
				//�۱�����
				bvo.setNexchangerate(new UFDouble(1));
				
				//���������֯���°汾 
				bvo.setCsendstockorgid(orgversionvo.getPk_org()); 
				//���������֯
				bvo.setCsendstockorgvid(orgversionvo.getPk_vid()); 
				//Ӧ����֯
				bvo.setCarorgid(orgversionvo.getPk_org());
				bvo.setCarorgvid(orgversionvo.getPk_vid());
				
				/************************һЩ�Զ�����Ŀ �����ڵذ���Ϣ************************************/ 
				// ���κ�
				//bvo.setVbdef1();
				// ����
				bvo.setVbdef2(wbvo.getCh());
				// �ذ�����
				bvo.setVbdef3(wbvo.getWbtype());
				// ������λ
				if ( wbvo.getSh_org() != null )
					bvo.setVbdef5(wbvo.getSh_org().toString());
				// ë��
				if ( wbvo.getMz() != null )
					bvo.setVbdef6(wbvo.getMz().div(new UFDouble("1000"),2).toString());
				// Ƥ��
				if ( wbvo.getPz() != null )
					bvo.setVbdef7(wbvo.getPz().div(new UFDouble("1000"),2).toString());
				// ����
				if ( wbvo.getJz() != null )
					bvo.setVbdef8(wbvo.getJz().div(new UFDouble("1000"),2).toString());
				// ����
				if ( wbvo.getKz() != null )
					bvo.setVbdef9(wbvo.getKz().div(new UFDouble("1000"),2).toString());
				// ʵ��
				if ( wbvo.getSz() != null )
					bvo.setVbdef10(wbvo.getSz().div(new UFDouble("1000"),2).toString());
				// ë��˾��Ա			
				bvo.setVbdef11(wbvo.getMz_psn());
				// ë��ʱ��
				if ( wbvo.getMz_datetime() != null )
					bvo.setVbdef12(wbvo.getMz_datetime().toString());
				// Ƥ��˾��Ա
				bvo.setVbdef13(wbvo.getPz_psn());
				// Ƥ��ʱ��
				if ( wbvo.getPz_datetime() != null )
					bvo.setVbdef14(wbvo.getPz_datetime().toString());
				// ����ʱ��
				if ( wbvo.getUpdatetime() != null ) 
					bvo.setVbdef17(wbvo.getPz_datetime().toString());
				// ����ʱ��
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
					newhvo.setBillmaker(billmaker);//Ĭ���Ƶ���XS001
					newhvo.setDr(0);
					//���±�ͷ��������
					hvos.add(newhvo);
				}
				dao.updateVOList(hvos);
				//����֮����µ��ݣ����µ������ں��Ƶ��ˣ�
				updateWeightInfo();
			}
			log_msg +="ִ��sql������" + sos.length + "�����۶������.";
		} catch (Exception e) {
			log_msg +="������Ϣ��" + e.getMessage();
			logVo.setMsg(log_msg); 
			logVo.setOptype("saleorder");
			try {
				trans.begin();
				getBaseDao().insertVO(logVo);
			      trans.commit();
			} catch (IllegalStateException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			} catch (RollbackException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			} catch (HeuristicRollbackException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			} catch (HeuristicMixedException | javax.transaction.NotSupportedException |
					 javax.transaction.SystemException | javax.transaction.RollbackException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}
		} 
		if ( !log_msg.contains("������Ϣ") ) {
			logVo.setMsg(log_msg); 
			logVo.setOptype("saleorder");
			getBaseDao().insertVO(logVo);	
		}
		return null;
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
	 * �ջ���λƥ��NC�ͻ� ����Ĭ�� ��˳�¸����������ι�˾
	 * @param bvoslist
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
		if ( "��˳��¡̩��ó���޹�˾".equals(sh_org_name) ) { 
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
			log_msg += "ִ��sql��������������۶����ĵذ���Ϣ��sql��" + sql + ",";
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
			log_msg += "ִ��sql��������������۶����ĵذ���Ϣ��sql��" + sql + ",";
			list = (ArrayList<Wb01VO>)getBaseDao().executeQuery(sql, new BeanListProcessor(Wb01VO.class));		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	

}
