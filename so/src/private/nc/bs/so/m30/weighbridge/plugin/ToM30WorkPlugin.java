package nc.bs.so.m30.weighbridge.plugin;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.jcom.xml.XMLUtil;
import nc.vo.pfxx.pub.PostFile;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.m30.weighbridge.Wb01VO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ���� ���۶���
 * @author admin
 *
 */

public class ToM30WorkPlugin implements IBackgroundWorkPlugin {

	java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");

	static BaseDAO dao = null;

	public BaseDAO getBaseDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	
	public PreAlertObject executeTask(BgWorkingContext arg0)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		Document doc = null;
		boolean analy = false;
		try {
			ArrayList<Wb01VO> list = getWeighbridgeInfo();
			for ( int i=0; i<list.size(); i++ ) {
				doc = createDocument(list.get(i));
				Document ren = sendXML(doc, "001");
				HashMap res = analyReturn(ren);				
				// �޸ı�־
				updateTableStatus(list.get(i).getLsh(), res);			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private ArrayList<Wb01VO> getWeighbridgeInfo() {
		ArrayList<Wb01VO> list = null;
		try {
			UFDateTime daytime = new UFDateTime(new java.util.Date());		
			String beginBeforeDay = daytime.getDateTimeBefore(1).toString().substring(0, 10) + " 00:00:00";
			String endBeforeDay = daytime.getDateTimeBefore(1).toString().substring(0, 10) + " 23:59:59";
			String sql = " select * from weighbridge01 where updatetime >= '" + beginBeforeDay + "' and updatetime < '" + endBeforeDay + "'";
			list = (ArrayList<Wb01VO>)getBaseDao().executeQuery(sql, new BeanListProcessor(Wb01VO.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ����������Դ��ƴװxml�������ⲿ���ݽ���ƽ̨
	 * @return
	 */
	private Document createDocument(Wb01VO vo) {	
		
		// ��ȡ��ǰϵͳʱ��
		UFDateTime daytime = new UFDateTime(new java.util.Date()).getDateTimeBefore(1);		
		String daytime1 = daytime.toString();
		String begin = daytime.toString().substring(0, 10) + " 00:00:00";
		String end = daytime.toString().substring(0, 10) + " 23:59:59";
		
		
		
		
		// ��ˮ�Ŷ�Ӧ ���ݺţ�
		String lsh = vo.getLsh();
		// �ͻ���
		String customer = getCustomer(vo.getSh_org());
		// ���ض�Ӧ ��ͷ�������� ����������������������
		UFDouble jz = vo.getJz().div(1000);
		// ���� û�ж�Ӧ������Ĭ��1
		UFDouble price = new UFDouble("1");
		// ��˰�ϼ�
		UFDouble jshj = jz.multiply(price).multiply(1.13);
		// �Ƶ��ˣ�
		String zdr = "1001A2100000000005PV";
		// �����ˣ�
		String cjr = "1001A2100000000005PV";
		// ��˰����
		UFDouble hsPrice = price.multiply(1.13);
		// ��˰�ϼ�
		UFDouble wshj = jz.multiply(price);
		// ˰��
		UFDouble se = jz.multiply(price).multiply(0.13);
		// ������֯
		String org = getOrg();
		// ������֯�汾
		String orgv = getOrgv();
		// ����
		String ch = vo.getCh();
		Document doc = XMLUtil.getDocumentBuilder().newDocument();
		Element rootEle = doc.createElement("ufinterface");
		// ���ufinterface��Ϣ
		rootEle.setAttribute("account", "design");
		rootEle.setAttribute("billtype", "30");
		rootEle.setAttribute("filename", "");
		rootEle.setAttribute("groupcode", "00000");
		rootEle.setAttribute("isexchange", "Y");
		rootEle.setAttribute("replace", "Y");
		rootEle.setAttribute("roottag", "");
		rootEle.setAttribute("sender", "sn01");
		// �½�<bill>�ڵ� 
		doc.appendChild(rootEle);
		Element bill = doc.createElement("bill");
		// ���bill��Ϣ
		bill.setAttribute("id", "");
		rootEle.appendChild(bill);
		/********************************************��ͷ��Ϣ**************************************************/
		Element billhead = doc
				.createElement("billhead");
		bill.appendChild(billhead);
		// <!--����,��󳤶�Ϊ20,����Ϊ:String-->
		Element pk_group = doc.createElement("pk_group");
		pk_group.appendChild(doc.createTextNode("0001A3100000000004NA"));
		billhead.appendChild(pk_group);
		// <!--������֯,��󳤶�Ϊ20,����Ϊ:String-->
		Element pk_org = doc.createElement("pk_org");
		pk_org.appendChild(doc.createTextNode(org));
		billhead.appendChild(pk_org);
		// <!--������֯�汾,��󳤶�Ϊ20,����Ϊ:String-->
		Element pk_org_v = doc.createElement("pk_org_v");
		pk_org_v.appendChild(doc.createTextNode(orgv));
		billhead.appendChild(pk_org_v);
		// <!--��������,��󳤶�Ϊ20,����Ϊ:String-->
		Element ctrantypeid = doc.createElement("ctrantypeid");
		ctrantypeid.appendChild(doc.createTextNode("0001A2100000000020XU"));
		billhead.appendChild(ctrantypeid);
		// <!--�������ͱ���,��󳤶�Ϊ20,����Ϊ:String-->
		Element vtrantypecode = doc.createElement("vtrantypecode");
		vtrantypecode.appendChild(doc.createTextNode("30-01"));
		billhead.appendChild(vtrantypecode);
		// <!--ҵ������,��󳤶�Ϊ20,����Ϊ:String-->
		Element cbiztypeid = doc.createElement("cbiztypeid");
		cbiztypeid.appendChild(doc.createTextNode("0001A210000000001E1W"));
		billhead.appendChild(cbiztypeid);
		// <!--���ݺ�,��󳤶�Ϊ40,����Ϊ:String-->
		Element vbillcode = doc.createElement("vbillcode");
//		vbillcode.appendChild(doc.createTextNode("SO302019050900000011"));
		vbillcode.appendChild(doc.createTextNode(lsh));
		billhead.appendChild(vbillcode);
		// <!--��������,��󳤶�Ϊ19,����Ϊ:UFDate-->
		Element dbilldate = doc.createElement("dbilldate");
		dbilldate.appendChild(doc.createTextNode(daytime1));
		billhead.appendChild(dbilldate);
		// <!--�ͻ�,��󳤶�Ϊ20,����Ϊ:String-->
		Element ccustomerid = doc.createElement("ccustomerid");
		ccustomerid.appendChild(doc.createTextNode(customer));
		billhead.appendChild(ccustomerid);
		// <!--����,��󳤶�Ϊ20,����Ϊ:String-->
		Element cdeptvid = doc.createElement("cdeptvid");
		cdeptvid.appendChild(doc.createTextNode("0001L910000000002937"));
		billhead.appendChild(cdeptvid);
		// <!--����,��󳤶�Ϊ20,����Ϊ:String-->
		Element cdeptid = doc.createElement("cdeptid");
		cdeptid.appendChild(doc.createTextNode("1001L910000000000A34"));
		billhead.appendChild(cdeptid);
		// <!--ԭ��,��󳤶�Ϊ20,����Ϊ:String-->
		Element corigcurrencyid = doc.createElement("corigcurrencyid");
		corigcurrencyid.appendChild(doc.createTextNode("1002Z0100000000001K1"));
		billhead.appendChild(corigcurrencyid);
		// <!--��Ʊ�ͻ�,��󳤶�Ϊ20,����Ϊ:String-->
		Element cinvoicecustid = doc.createElement("cinvoicecustid");
		cinvoicecustid.appendChild(doc.createTextNode("1001A310000000002LKW"));
		billhead.appendChild(cinvoicecustid);
		// <!--������,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element ntotalnum = doc.createElement("ntotalnum");
		ntotalnum.appendChild(doc.createTextNode(jz.toString()));
		billhead.appendChild(ntotalnum);
		// <!--��˰�ϼ�,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element ntotalorigmny = doc.createElement("ntotalorigmny");
		ntotalorigmny.appendChild(doc.createTextNode(jshj.toString()));
		billhead.appendChild(ntotalorigmny);
		// <!--����״̬,��󳤶�Ϊ0,����Ϊ:Integer-->
		Element fstatusflag = doc.createElement("fstatusflag");
		fstatusflag.appendChild(doc.createTextNode("1"));
		billhead.appendChild(fstatusflag);
		// <!--������״̬,��󳤶�Ϊ0,����Ϊ:Integer-->
		Element fpfstatusflag = doc.createElement("fpfstatusflag");
		fpfstatusflag.appendChild(doc.createTextNode("-1"));
		billhead.appendChild(fpfstatusflag);
		// <!--�Ƶ���,��󳤶�Ϊ20,����Ϊ:String-->
		Element billmaker = doc.createElement("billmaker");
		billmaker.appendChild(doc.createTextNode(zdr));
		billhead.appendChild(billmaker);
		// <!--�Ƶ�����,��󳤶�Ϊ19,����Ϊ:UFDate-->
		Element dmakedate = doc.createElement("dmakedate");
		dmakedate.appendChild(doc.createTextNode(daytime1));
		billhead.appendChild(dmakedate);
		// <!--������,��󳤶�Ϊ20,����Ϊ:String-->
		Element creator = doc.createElement("creator");
		creator.appendChild(doc.createTextNode(cjr));
		billhead.appendChild(creator);
		// <!--����ʱ��,��󳤶�Ϊ19,����Ϊ:UFDateTime-->
		Element creationtime = doc.createElement("creationtime");
		creationtime.appendChild(doc.createTextNode(daytime1));
		billhead.appendChild(creationtime);
		// <!--����-->
		Element vdef1 = doc.createElement("vdef1");
		vdef1.appendChild(doc.createTextNode(ch));
		billhead.appendChild(vdef1);
		/********************************************������Ϣ**************************************************/
		Element so_saleorder_b = doc.createElement("so_saleorder_b");
		billhead.appendChild(so_saleorder_b);
		Element item = doc.createElement("item");
		so_saleorder_b.appendChild(item);
		// <!--����,��󳤶�Ϊ20,����Ϊ:String-->
		Element pk_group1 = doc.createElement("pk_group");
		pk_group1.appendChild(doc.createTextNode("0001A3100000000004NA"));
		item.appendChild(pk_group1);
		// <!--������֯,��󳤶�Ϊ20,����Ϊ:String-->
		Element pk_org1 = doc.createElement("pk_org");
		pk_org1.appendChild(doc.createTextNode("0001A2100000000027QM"));
		item.appendChild(pk_org1);
		// <!--��������,��󳤶�Ϊ19,����Ϊ:UFDate-->
		Element dbilldate1 = doc.createElement("dbilldate");
		dbilldate1.appendChild(doc.createTextNode(daytime1));
		item.appendChild(dbilldate1);
		// <!--�к�,��󳤶�Ϊ20,����Ϊ:String-->
		Element crowno = doc.createElement("crowno");
		crowno.appendChild(doc.createTextNode("5"));
		item.appendChild(crowno);
		// <!--���ϱ���,��󳤶�Ϊ20,����Ϊ:String-->
		Element cmaterialvid = doc.createElement("cmaterialvid");
		cmaterialvid.appendChild(doc.createTextNode("1001A31000000005142V"));
		item.appendChild(cmaterialvid);
		// <!--����,��󳤶�Ϊ20,����Ϊ:String-->
		Element cmaterialid = doc.createElement("cmaterialid");
		cmaterialid.appendChild(doc.createTextNode("1001A31000000005142V"));
		item.appendChild(cmaterialid);
		// <!--����λ,��󳤶�Ϊ20,����Ϊ:String-->
		Element cunitid = doc.createElement("cunitid");
		cunitid.appendChild(doc.createTextNode("1001A310000000003ZLT"));
		item.appendChild(cunitid);
		// <!--��λ,��󳤶�Ϊ20,����Ϊ:String-->
		Element castunitid = doc.createElement("castunitid");
		castunitid.appendChild(doc.createTextNode("1001A310000000003ZLT"));
		item.appendChild(castunitid);
		// <!--������,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element nnum = doc.createElement("nnum");
		nnum.appendChild(doc.createTextNode(jz.toString()));
		item.appendChild(nnum);
		// <!--����,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element nastnum = doc.createElement("nastnum");
		nastnum.appendChild(doc.createTextNode(jz.toString()));
		item.appendChild(nastnum);
		// <!--���۵�λ,��󳤶�Ϊ20,����Ϊ:String-->
		Element cqtunitid = doc.createElement("cqtunitid");
		cqtunitid.appendChild(doc.createTextNode("1001A310000000003ZLT"));
		item.appendChild(cqtunitid);
		// <!--���۵�λ����,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element nqtunitnum = doc.createElement("nqtunitnum");
		nqtunitnum.appendChild(doc.createTextNode(jz.toString()));
		item.appendChild(nqtunitnum);
		// <!--�����ۿ�,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element ndiscountrate = doc.createElement("ndiscountrate");
		ndiscountrate.appendChild(doc.createTextNode("100"));
		item.appendChild(ndiscountrate);
		// <!--��Ʒ�ۿ�,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element nitemdiscountrate = doc.createElement("nitemdiscountrate");
		nitemdiscountrate.appendChild(doc.createTextNode("100"));
		item.appendChild(nitemdiscountrate);
		// <!--˰��,��󳤶�Ϊ20,����Ϊ:String-->
		Element ctaxcodeid = doc.createElement("ctaxcodeid");
		ctaxcodeid.appendChild(doc.createTextNode("1001A31000000007IRDU"));
		item.appendChild(ctaxcodeid);
		// <!--˰��,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element ntaxrate = doc.createElement("ntaxrate");
		ntaxrate.appendChild(doc.createTextNode("13"));
		item.appendChild(ntaxrate);
		// <!--��˰���,��󳤶�Ϊ0,����Ϊ:Integer-->
		Element ftaxtypeflag = doc.createElement("ftaxtypeflag");
		ftaxtypeflag.appendChild(doc.createTextNode("1"));
		item.appendChild(ftaxtypeflag);
		// <!--��λ��,��󳤶�Ϊ20,����Ϊ:String-->
		Element ccurrencyid = doc.createElement("ccurrencyid");
		ccurrencyid.appendChild(doc.createTextNode("1002Z0100000000001K1"));
		item.appendChild(ccurrencyid);
		// <!--�۱�����,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element nexchangerate = doc.createElement("nexchangerate");
		nexchangerate.appendChild(doc.createTextNode("1"));
		item.appendChild(nexchangerate);
		// <!--��˰����,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element nqtorigtaxprice = doc.createElement("nqtorigtaxprice");
		nqtorigtaxprice.appendChild(doc.createTextNode(hsPrice.toString()));
		item.appendChild(nqtorigtaxprice);
		// <!--��˰����,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element nqtorigprice = doc.createElement("nqtorigprice");
		nqtorigprice.appendChild(doc.createTextNode(price.toString()));
		item.appendChild(nqtorigprice);
		// <!--��˰����,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element nqtorigtaxnetprc = doc.createElement("nqtorigtaxnetprc");
		nqtorigtaxnetprc.appendChild(doc.createTextNode(jshj.toString()));
		item.appendChild(nqtorigtaxnetprc);
		// <!--��˰����,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element nqtorignetprice = doc.createElement("nqtorignetprice");
		nqtorignetprice.appendChild(doc.createTextNode(wshj.toString()));
		item.appendChild(nqtorignetprice);
		// <!--����˰����,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element norigtaxprice = doc.createElement("norigtaxprice");
		norigtaxprice.appendChild(doc.createTextNode(hsPrice.toString()));
		item.appendChild(norigtaxprice);
		// <!--����˰����,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element norigprice = doc.createElement("norigprice");
		norigprice.appendChild(doc.createTextNode(price.toString()));
		item.appendChild(norigprice);
		// <!--����˰����,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element norigtaxnetprice = doc.createElement("norigtaxnetprice");
		norigtaxnetprice.appendChild(doc.createTextNode(jshj.toString()));
		item.appendChild(norigtaxnetprice);
		// <!--����˰����,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element norignetprice = doc.createElement("norignetprice");
		norignetprice.appendChild(doc.createTextNode(wshj.toString()));
		item.appendChild(norignetprice);
		// <!--˰��,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element ntax = doc.createElement("ntax");
		ntax.appendChild(doc.createTextNode(se.toString()));
		item.appendChild(ntax);
		// <!--��˰���,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element ncaltaxmny = doc.createElement("ncaltaxmny");
		ncaltaxmny.appendChild(doc.createTextNode(wshj.toString()));
		item.appendChild(ncaltaxmny);
		// <!--��˰���,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element norigmny = doc.createElement("norigmny");
		norigmny.appendChild(doc.createTextNode(wshj.toString()));
		item.appendChild(norigmny);
		// <!--��˰�ϼ�,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element norigtaxmny = doc.createElement("norigtaxmny");
		norigtaxmny.appendChild(doc.createTextNode(jshj.toString()));
		item.appendChild(norigtaxmny);
		// <!--���Һ�˰����,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element nqttaxprice = doc.createElement("nqttaxprice");
		nqttaxprice.appendChild(doc.createTextNode(hsPrice.toString()));
		item.appendChild(nqttaxprice);
		// <!--������˰����,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element nqtprice = doc.createElement("nqtprice");
		nqtprice.appendChild(doc.createTextNode(price.toString()));
		item.appendChild(nqtprice);
		// <!--���Һ�˰����,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element nqttaxnetprice = doc.createElement("nqttaxnetprice");
		nqttaxnetprice.appendChild(doc.createTextNode(jshj.toString()));
		item.appendChild(nqttaxnetprice);
		// <!--������˰����,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element nqtnetprice = doc.createElement("nqtnetprice");
		nqtnetprice.appendChild(doc.createTextNode(wshj.toString()));
		item.appendChild(nqtnetprice);
		// <!--�����Һ�˰����,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element ntaxprice = doc.createElement("ntaxprice");
		ntaxprice.appendChild(doc.createTextNode(hsPrice.toString()));
		item.appendChild(ntaxprice);	
		// <!--��������˰����,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element nprice = doc.createElement("nprice");
		nprice.appendChild(doc.createTextNode(price.toString()));
		item.appendChild(nprice);
		// <!--�����Һ�˰����,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element ntaxnetprice = doc.createElement("ntaxnetprice");
		ntaxnetprice.appendChild(doc.createTextNode(jshj.toString()));
		item.appendChild(ntaxnetprice);		
		// <!--��������˰����,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element nnetprice = doc.createElement("nnetprice");
		nnetprice.appendChild(doc.createTextNode(wshj.toString()));
		item.appendChild(nnetprice);
		// <!--������˰���,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element nmny = doc.createElement("nmny");
		nmny.appendChild(doc.createTextNode(wshj.toString()));
		item.appendChild(nmny);		
		// <!--���Ҽ�˰�ϼ�,��󳤶�Ϊ28,����Ϊ:UFDouble-->
		Element ntaxmny = doc.createElement("ntaxmny");
		ntaxmny.appendChild(doc.createTextNode(jshj.toString()));
		item.appendChild(ntaxmny);
		// <!--Ҫ�󷢻�����,��󳤶�Ϊ19,����Ϊ:UFDate-->
		Element dsenddate = doc.createElement("dsenddate");
		dsenddate.appendChild(doc.createTextNode(end));
		item.appendChild(dsenddate);
		// <!--�ƻ���������,��󳤶�Ϊ19,����Ϊ:UFDate-->
		Element dreceivedate = doc.createElement("dreceivedate");
		dreceivedate.appendChild(doc.createTextNode(end));
		item.appendChild(dreceivedate);
		// <!--�ջ��ͻ�,��󳤶�Ϊ20,����Ϊ:String-->
		Element creceivecustid = doc.createElement("creceivecustid");
		creceivecustid.appendChild(doc.createTextNode("1001A310000000002LKW"));
		item.appendChild(creceivecustid);
		// <!--���������֯,��󳤶�Ϊ20,����Ϊ:String-->
		Element csendstockorgvid = doc.createElement("csendstockorgvid");
		csendstockorgvid.appendChild(doc.createTextNode("0001A2100000000027QL"));
		item.appendChild(csendstockorgvid);
		// <!--���������֯���°汾,��󳤶�Ϊ20,����Ϊ:String-->
		Element csendstockorgid = doc.createElement("csendstockorgid");
		csendstockorgid.appendChild(doc.createTextNode("0001A2100000000027QM"));
		item.appendChild(csendstockorgid);
		// <!--���������֯,��󳤶�Ϊ20,����Ϊ:String-->
		Element csettleorgvid = doc.createElement("csettleorgvid");
		csettleorgvid.appendChild(doc.createTextNode("0001A2100000000027QL"));
		item.appendChild(csettleorgvid);
		// <!--���������֯,��󳤶�Ϊ20,����Ϊ:String-->
		Element csettleorgid = doc.createElement("csettleorgid");
		csettleorgid.appendChild(doc.createTextNode("0001A2100000000027QM"));
		item.appendChild(csettleorgid);
		// <!--�ջ�����/����,��󳤶�Ϊ20,����Ϊ:String-->
		Element crececountryid = doc.createElement("crececountryid");
		crececountryid.appendChild(doc.createTextNode("0001Z010000000079UJJ"));
		item.appendChild(crececountryid);
		// <!--��������/����,��󳤶�Ϊ20,����Ϊ:String-->
		Element csendcountryid = doc.createElement("csendcountryid");
		csendcountryid.appendChild(doc.createTextNode("0001Z010000000079UJJ"));
		item.appendChild(csendcountryid);
		// <!--��˰����/����,��󳤶�Ϊ20,����Ϊ:String-->
		Element ctaxcountryid = doc.createElement("ctaxcountryid");
		ctaxcountryid.appendChild(doc.createTextNode("0001Z010000000079UJJ"));
		item.appendChild(ctaxcountryid);
		// <!--��������,��󳤶�Ϊ0,����Ϊ:Integer-->
		Element fbuysellflag = doc.createElement("fbuysellflag");
		fbuysellflag.appendChild(doc.createTextNode("1"));
		item.appendChild(fbuysellflag);
		// <!--Ӧ����֯,��󳤶�Ϊ20,����Ϊ:String-->
		Element carorgvid = doc.createElement("carorgvid");
		carorgvid.appendChild(doc.createTextNode("0001A2100000000027QL"));
		item.appendChild(carorgvid);
		// <!--Ӧ����֯���°汾,��󳤶�Ϊ20,����Ϊ:String-->
		Element carorgid = doc.createElement("carorgid");
		carorgid.appendChild(doc.createTextNode("0001A2100000000027QM"));
		item.appendChild(carorgid);
		// <!--��״̬,��󳤶�Ϊ0,����Ϊ:Integer-->
		Element frowstatus = doc.createElement("frowstatus");
		frowstatus.appendChild(doc.createTextNode("1"));
		item.appendChild(frowstatus);
		// ����xml
		try {
			TransformerFactory tf = TransformerFactory.newInstance();

		       Transformer transformer = tf.newTransformer();

		       DOMSource source = new DOMSource(doc);

		       transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		       transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		       StringWriter writer = new StringWriter();

		       StreamResult result = new StreamResult(writer);

		       transformer.transform(source, result);

		       String output = writer.getBuffer().toString();
		} catch (Exception e) {
		}
		
	       
	       
		return doc;
	}
	
	/**
	 * �ͻ����չ�ϵ ���� ģ�����ݣ�1001A310000000002LKW
	 * @param sh_org
	 * @return
	 */
	private String getCustomer(String sh_org) {
		return "1001A310000000002LKW";
	}
	
	/**
	 * ������֯���չ�ϵ ���� ģ�����ݣ�0001A2100000000027QM
	 * @return
	 */
	private String getOrg() {
		return "0001A2100000000027QM";
	}
	
	/**
	 * ������֯�汾���չ�ϵ ���� ģ�����ݣ�0001A2100000000027QL
	 * @return
	 */
	private String getOrgv() {
		return "0001A2100000000027QL";
	}

	/**
	 * ���ͻ��ƽ̨
	 * 
	 * @param doc
	 * @param corpcode
	 * @return
	 * @throws Exception
	 */
	private Document sendXML(Document doc, String corpcode) throws Exception {
		String url = getConnectionUrl(corpcode);
		HttpURLConnection connection = PostFile.getConnection(url, null, false);
		Writer writer = new OutputStreamWriter(connection.getOutputStream(),
				"GB2312");
		XMLUtil.printDOMTree(writer, doc, 0, "GB2312"); // ����XML�ļ���ʽ���
		writer.flush();
		writer.close();
		InputStream inputStream = connection.getInputStream();
		Document resDoc = XMLUtil.getDocumentBuilder().parse(inputStream); // ����ΪDoc����
		return resDoc;
	}

	/**
	 * ��ȡƽ̨��ַ(�����ã�
	 * 
	 * @param ce
	 * @return
	 */
	public String getConnectionUrl(String corpcode) throws BusinessException {
		String url = null;
		/**
		 * ��ֹ��һ������û�У���Ĭ��Ϊ
		 */
		if (url == null) {
			// ����
			url ="http://127.0.0.1:80/service/XChangeServlet?account=develop&groupcode=00000";
		}
		return url;
	}

	/**
	 * ��������ֵ
	 * @throws Exception 
	 * 
	 */
	public HashMap<String, String> analyReturn(Document doc) throws Exception {
		HashMap<String, String> res = new HashMap<String, String>();
		String ret = "N";
		String errorInfo = "�ɹ��������۶���";
		if (doc != null) {
			Element rootElt = doc.getDocumentElement(); // ��ȡ���ڵ�
			String success = rootElt.getAttribute("successful");
			if (success != null && success.endsWith("Y")) {
				ret = "Y";
			} else {
				errorInfo = getError(doc);
			}
		}
		res.put("state", ret);
		res.put("errorInfo", errorInfo);
		return res;
	}
	
	/**
	 * ����������Դ��״̬
	 * @param analy ״̬
	 * @param lsh ��ˮ��
	 */
	private void updateTableStatus(String lsh, HashMap<String, String> res) throws Exception {	
		String sql = "";
		String state = res.get("state");
		String errorInfo = res.get("errorInfo");
		if ( "Y".equals(state) ) {
			sql = "update weighbridge01 set info_status = 'Y', relog = '" + errorInfo + ",���۶����ţ�"+lsh+"' where lsh = '" + lsh + "'";
		} else {
			sql = "update weighbridge01 set info_status = 'N', relog = '" + errorInfo + "' where lsh = '" + lsh + "'";
		}
		getBaseDao().executeUpdate(sql);	
	}
	
	/**
	 * ��������ֵ
	 * 
	 */
	public String getError(Document doc) {
		String message = null;
		if (doc != null) {
			NodeList nodeList = doc.getElementsByTagName("sendresult"); // sendresult
																		// ufinterface
			Node fatherNode = nodeList.item(0);
			NodeList childNodes = fatherNode.getChildNodes();
			System.out.println(childNodes.getLength());
			for (int j = 0; j < childNodes.getLength(); j++) {
				Node childNode = childNodes.item(j);
				// �������ڵ�����Element ,�ٽ���ȡֵ
				if (childNode instanceof Element) {
					if ("resultdescription".equals(childNode.getNodeName())) {
						message = childNode.getFirstChild().getNodeValue();
						if (message != null && message.length() > 900) {
							message = message.substring(0, 800);
							message = message + "�����������������������ѽض�";

						}

					}
				}
			}

		}
		return message;
	}
	
	/**
	 * ���� xml �ļ�
	 * 
	 * @throws Exception
	 */
	public void textXML(Document document) throws Exception {

		// ��ʼ��Documentӳ�䵽�ļ�
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transFormer = transFactory.newTransformer();
		transFormer.setOutputProperty("encoding","gb2312"); 
		// ����������
		DOMSource domSource = new DOMSource(document);
		// ����xml�ļ�
		File file = new File("D://yonyou201904//������Ŀ//testReturn//re.xml");
		// �ж��Ƿ����,���������,�򴴽�
		if (!file.exists()) {
			file.createNewFile();
		}
		// �ļ������
		FileOutputStream out = new FileOutputStream(file);
		// ��������Դ
		StreamResult xmlResult = new StreamResult(out);
		// ���xml�ļ�
		transFormer.transform(domSource, xmlResult);
		// �����ļ������·��
		System.out.println(file.getAbsolutePath());
	}
	
	public static void main(String[] args) {
		ToM30WorkPlugin test = new ToM30WorkPlugin();
		Document doc = test.createDocument(null);
		try {
			Document ren = test.sendXML(doc, "sn01");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
