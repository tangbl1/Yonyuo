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
 * 生成 销售订单
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
		// TODO 自动生成的方法存根
		Document doc = null;
		boolean analy = false;
		try {
			ArrayList<Wb01VO> list = getWeighbridgeInfo();
			for ( int i=0; i<list.size(); i++ ) {
				doc = createDocument(list.get(i));
				Document ren = sendXML(doc, "001");
				HashMap res = analyReturn(ren);				
				// 修改标志
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
	 * 根据数据来源，拼装xml，用于外部数据交换平台
	 * @return
	 */
	private Document createDocument(Wb01VO vo) {	
		
		// 获取当前系统时间
		UFDateTime daytime = new UFDateTime(new java.util.Date()).getDateTimeBefore(1);		
		String daytime1 = daytime.toString();
		String begin = daytime.toString().substring(0, 10) + " 00:00:00";
		String end = daytime.toString().substring(0, 10) + " 23:59:59";
		
		
		
		
		// 流水号对应 单据号？
		String lsh = vo.getLsh();
		// 客户？
		String customer = getCustomer(vo.getSh_org());
		// 净重对应 表头主数量？ 表体主数量？表体数量？
		UFDouble jz = vo.getJz().div(1000);
		// 单价 没有对应？单价默认1
		UFDouble price = new UFDouble("1");
		// 价税合计
		UFDouble jshj = jz.multiply(price).multiply(1.13);
		// 制单人？
		String zdr = "1001A2100000000005PV";
		// 创建人？
		String cjr = "1001A2100000000005PV";
		// 含税单价
		UFDouble hsPrice = price.multiply(1.13);
		// 无税合计
		UFDouble wshj = jz.multiply(price);
		// 税额
		UFDouble se = jz.multiply(price).multiply(0.13);
		// 销售组织
		String org = getOrg();
		// 销售组织版本
		String orgv = getOrgv();
		// 车号
		String ch = vo.getCh();
		Document doc = XMLUtil.getDocumentBuilder().newDocument();
		Element rootEle = doc.createElement("ufinterface");
		// 添加ufinterface信息
		rootEle.setAttribute("account", "design");
		rootEle.setAttribute("billtype", "30");
		rootEle.setAttribute("filename", "");
		rootEle.setAttribute("groupcode", "00000");
		rootEle.setAttribute("isexchange", "Y");
		rootEle.setAttribute("replace", "Y");
		rootEle.setAttribute("roottag", "");
		rootEle.setAttribute("sender", "sn01");
		// 新建<bill>节点 
		doc.appendChild(rootEle);
		Element bill = doc.createElement("bill");
		// 添加bill信息
		bill.setAttribute("id", "");
		rootEle.appendChild(bill);
		/********************************************表头信息**************************************************/
		Element billhead = doc
				.createElement("billhead");
		bill.appendChild(billhead);
		// <!--集团,最大长度为20,类型为:String-->
		Element pk_group = doc.createElement("pk_group");
		pk_group.appendChild(doc.createTextNode("0001A3100000000004NA"));
		billhead.appendChild(pk_group);
		// <!--销售组织,最大长度为20,类型为:String-->
		Element pk_org = doc.createElement("pk_org");
		pk_org.appendChild(doc.createTextNode(org));
		billhead.appendChild(pk_org);
		// <!--销售组织版本,最大长度为20,类型为:String-->
		Element pk_org_v = doc.createElement("pk_org_v");
		pk_org_v.appendChild(doc.createTextNode(orgv));
		billhead.appendChild(pk_org_v);
		// <!--订单类型,最大长度为20,类型为:String-->
		Element ctrantypeid = doc.createElement("ctrantypeid");
		ctrantypeid.appendChild(doc.createTextNode("0001A2100000000020XU"));
		billhead.appendChild(ctrantypeid);
		// <!--订单类型编码,最大长度为20,类型为:String-->
		Element vtrantypecode = doc.createElement("vtrantypecode");
		vtrantypecode.appendChild(doc.createTextNode("30-01"));
		billhead.appendChild(vtrantypecode);
		// <!--业务流程,最大长度为20,类型为:String-->
		Element cbiztypeid = doc.createElement("cbiztypeid");
		cbiztypeid.appendChild(doc.createTextNode("0001A210000000001E1W"));
		billhead.appendChild(cbiztypeid);
		// <!--单据号,最大长度为40,类型为:String-->
		Element vbillcode = doc.createElement("vbillcode");
//		vbillcode.appendChild(doc.createTextNode("SO302019050900000011"));
		vbillcode.appendChild(doc.createTextNode(lsh));
		billhead.appendChild(vbillcode);
		// <!--单据日期,最大长度为19,类型为:UFDate-->
		Element dbilldate = doc.createElement("dbilldate");
		dbilldate.appendChild(doc.createTextNode(daytime1));
		billhead.appendChild(dbilldate);
		// <!--客户,最大长度为20,类型为:String-->
		Element ccustomerid = doc.createElement("ccustomerid");
		ccustomerid.appendChild(doc.createTextNode(customer));
		billhead.appendChild(ccustomerid);
		// <!--部门,最大长度为20,类型为:String-->
		Element cdeptvid = doc.createElement("cdeptvid");
		cdeptvid.appendChild(doc.createTextNode("0001L910000000002937"));
		billhead.appendChild(cdeptvid);
		// <!--部门,最大长度为20,类型为:String-->
		Element cdeptid = doc.createElement("cdeptid");
		cdeptid.appendChild(doc.createTextNode("1001L910000000000A34"));
		billhead.appendChild(cdeptid);
		// <!--原币,最大长度为20,类型为:String-->
		Element corigcurrencyid = doc.createElement("corigcurrencyid");
		corigcurrencyid.appendChild(doc.createTextNode("1002Z0100000000001K1"));
		billhead.appendChild(corigcurrencyid);
		// <!--开票客户,最大长度为20,类型为:String-->
		Element cinvoicecustid = doc.createElement("cinvoicecustid");
		cinvoicecustid.appendChild(doc.createTextNode("1001A310000000002LKW"));
		billhead.appendChild(cinvoicecustid);
		// <!--总数量,最大长度为28,类型为:UFDouble-->
		Element ntotalnum = doc.createElement("ntotalnum");
		ntotalnum.appendChild(doc.createTextNode(jz.toString()));
		billhead.appendChild(ntotalnum);
		// <!--价税合计,最大长度为28,类型为:UFDouble-->
		Element ntotalorigmny = doc.createElement("ntotalorigmny");
		ntotalorigmny.appendChild(doc.createTextNode(jshj.toString()));
		billhead.appendChild(ntotalorigmny);
		// <!--单据状态,最大长度为0,类型为:Integer-->
		Element fstatusflag = doc.createElement("fstatusflag");
		fstatusflag.appendChild(doc.createTextNode("1"));
		billhead.appendChild(fstatusflag);
		// <!--审批流状态,最大长度为0,类型为:Integer-->
		Element fpfstatusflag = doc.createElement("fpfstatusflag");
		fpfstatusflag.appendChild(doc.createTextNode("-1"));
		billhead.appendChild(fpfstatusflag);
		// <!--制单人,最大长度为20,类型为:String-->
		Element billmaker = doc.createElement("billmaker");
		billmaker.appendChild(doc.createTextNode(zdr));
		billhead.appendChild(billmaker);
		// <!--制单日期,最大长度为19,类型为:UFDate-->
		Element dmakedate = doc.createElement("dmakedate");
		dmakedate.appendChild(doc.createTextNode(daytime1));
		billhead.appendChild(dmakedate);
		// <!--创建人,最大长度为20,类型为:String-->
		Element creator = doc.createElement("creator");
		creator.appendChild(doc.createTextNode(cjr));
		billhead.appendChild(creator);
		// <!--创建时间,最大长度为19,类型为:UFDateTime-->
		Element creationtime = doc.createElement("creationtime");
		creationtime.appendChild(doc.createTextNode(daytime1));
		billhead.appendChild(creationtime);
		// <!--车号-->
		Element vdef1 = doc.createElement("vdef1");
		vdef1.appendChild(doc.createTextNode(ch));
		billhead.appendChild(vdef1);
		/********************************************表体信息**************************************************/
		Element so_saleorder_b = doc.createElement("so_saleorder_b");
		billhead.appendChild(so_saleorder_b);
		Element item = doc.createElement("item");
		so_saleorder_b.appendChild(item);
		// <!--集团,最大长度为20,类型为:String-->
		Element pk_group1 = doc.createElement("pk_group");
		pk_group1.appendChild(doc.createTextNode("0001A3100000000004NA"));
		item.appendChild(pk_group1);
		// <!--销售组织,最大长度为20,类型为:String-->
		Element pk_org1 = doc.createElement("pk_org");
		pk_org1.appendChild(doc.createTextNode("0001A2100000000027QM"));
		item.appendChild(pk_org1);
		// <!--单据日期,最大长度为19,类型为:UFDate-->
		Element dbilldate1 = doc.createElement("dbilldate");
		dbilldate1.appendChild(doc.createTextNode(daytime1));
		item.appendChild(dbilldate1);
		// <!--行号,最大长度为20,类型为:String-->
		Element crowno = doc.createElement("crowno");
		crowno.appendChild(doc.createTextNode("5"));
		item.appendChild(crowno);
		// <!--物料编码,最大长度为20,类型为:String-->
		Element cmaterialvid = doc.createElement("cmaterialvid");
		cmaterialvid.appendChild(doc.createTextNode("1001A31000000005142V"));
		item.appendChild(cmaterialvid);
		// <!--物料,最大长度为20,类型为:String-->
		Element cmaterialid = doc.createElement("cmaterialid");
		cmaterialid.appendChild(doc.createTextNode("1001A31000000005142V"));
		item.appendChild(cmaterialid);
		// <!--主单位,最大长度为20,类型为:String-->
		Element cunitid = doc.createElement("cunitid");
		cunitid.appendChild(doc.createTextNode("1001A310000000003ZLT"));
		item.appendChild(cunitid);
		// <!--单位,最大长度为20,类型为:String-->
		Element castunitid = doc.createElement("castunitid");
		castunitid.appendChild(doc.createTextNode("1001A310000000003ZLT"));
		item.appendChild(castunitid);
		// <!--主数量,最大长度为28,类型为:UFDouble-->
		Element nnum = doc.createElement("nnum");
		nnum.appendChild(doc.createTextNode(jz.toString()));
		item.appendChild(nnum);
		// <!--数量,最大长度为28,类型为:UFDouble-->
		Element nastnum = doc.createElement("nastnum");
		nastnum.appendChild(doc.createTextNode(jz.toString()));
		item.appendChild(nastnum);
		// <!--报价单位,最大长度为20,类型为:String-->
		Element cqtunitid = doc.createElement("cqtunitid");
		cqtunitid.appendChild(doc.createTextNode("1001A310000000003ZLT"));
		item.appendChild(cqtunitid);
		// <!--报价单位数量,最大长度为28,类型为:UFDouble-->
		Element nqtunitnum = doc.createElement("nqtunitnum");
		nqtunitnum.appendChild(doc.createTextNode(jz.toString()));
		item.appendChild(nqtunitnum);
		// <!--整单折扣,最大长度为28,类型为:UFDouble-->
		Element ndiscountrate = doc.createElement("ndiscountrate");
		ndiscountrate.appendChild(doc.createTextNode("100"));
		item.appendChild(ndiscountrate);
		// <!--单品折扣,最大长度为28,类型为:UFDouble-->
		Element nitemdiscountrate = doc.createElement("nitemdiscountrate");
		nitemdiscountrate.appendChild(doc.createTextNode("100"));
		item.appendChild(nitemdiscountrate);
		// <!--税码,最大长度为20,类型为:String-->
		Element ctaxcodeid = doc.createElement("ctaxcodeid");
		ctaxcodeid.appendChild(doc.createTextNode("1001A31000000007IRDU"));
		item.appendChild(ctaxcodeid);
		// <!--税率,最大长度为28,类型为:UFDouble-->
		Element ntaxrate = doc.createElement("ntaxrate");
		ntaxrate.appendChild(doc.createTextNode("13"));
		item.appendChild(ntaxrate);
		// <!--扣税类别,最大长度为0,类型为:Integer-->
		Element ftaxtypeflag = doc.createElement("ftaxtypeflag");
		ftaxtypeflag.appendChild(doc.createTextNode("1"));
		item.appendChild(ftaxtypeflag);
		// <!--本位币,最大长度为20,类型为:String-->
		Element ccurrencyid = doc.createElement("ccurrencyid");
		ccurrencyid.appendChild(doc.createTextNode("1002Z0100000000001K1"));
		item.appendChild(ccurrencyid);
		// <!--折本汇率,最大长度为28,类型为:UFDouble-->
		Element nexchangerate = doc.createElement("nexchangerate");
		nexchangerate.appendChild(doc.createTextNode("1"));
		item.appendChild(nexchangerate);
		// <!--含税单价,最大长度为28,类型为:UFDouble-->
		Element nqtorigtaxprice = doc.createElement("nqtorigtaxprice");
		nqtorigtaxprice.appendChild(doc.createTextNode(hsPrice.toString()));
		item.appendChild(nqtorigtaxprice);
		// <!--无税单价,最大长度为28,类型为:UFDouble-->
		Element nqtorigprice = doc.createElement("nqtorigprice");
		nqtorigprice.appendChild(doc.createTextNode(price.toString()));
		item.appendChild(nqtorigprice);
		// <!--含税净价,最大长度为28,类型为:UFDouble-->
		Element nqtorigtaxnetprc = doc.createElement("nqtorigtaxnetprc");
		nqtorigtaxnetprc.appendChild(doc.createTextNode(jshj.toString()));
		item.appendChild(nqtorigtaxnetprc);
		// <!--无税净价,最大长度为28,类型为:UFDouble-->
		Element nqtorignetprice = doc.createElement("nqtorignetprice");
		nqtorignetprice.appendChild(doc.createTextNode(wshj.toString()));
		item.appendChild(nqtorignetprice);
		// <!--主含税单价,最大长度为28,类型为:UFDouble-->
		Element norigtaxprice = doc.createElement("norigtaxprice");
		norigtaxprice.appendChild(doc.createTextNode(hsPrice.toString()));
		item.appendChild(norigtaxprice);
		// <!--主无税单价,最大长度为28,类型为:UFDouble-->
		Element norigprice = doc.createElement("norigprice");
		norigprice.appendChild(doc.createTextNode(price.toString()));
		item.appendChild(norigprice);
		// <!--主含税净价,最大长度为28,类型为:UFDouble-->
		Element norigtaxnetprice = doc.createElement("norigtaxnetprice");
		norigtaxnetprice.appendChild(doc.createTextNode(jshj.toString()));
		item.appendChild(norigtaxnetprice);
		// <!--主无税净价,最大长度为28,类型为:UFDouble-->
		Element norignetprice = doc.createElement("norignetprice");
		norignetprice.appendChild(doc.createTextNode(wshj.toString()));
		item.appendChild(norignetprice);
		// <!--税额,最大长度为28,类型为:UFDouble-->
		Element ntax = doc.createElement("ntax");
		ntax.appendChild(doc.createTextNode(se.toString()));
		item.appendChild(ntax);
		// <!--计税金额,最大长度为28,类型为:UFDouble-->
		Element ncaltaxmny = doc.createElement("ncaltaxmny");
		ncaltaxmny.appendChild(doc.createTextNode(wshj.toString()));
		item.appendChild(ncaltaxmny);
		// <!--无税金额,最大长度为28,类型为:UFDouble-->
		Element norigmny = doc.createElement("norigmny");
		norigmny.appendChild(doc.createTextNode(wshj.toString()));
		item.appendChild(norigmny);
		// <!--价税合计,最大长度为28,类型为:UFDouble-->
		Element norigtaxmny = doc.createElement("norigtaxmny");
		norigtaxmny.appendChild(doc.createTextNode(jshj.toString()));
		item.appendChild(norigtaxmny);
		// <!--本币含税单价,最大长度为28,类型为:UFDouble-->
		Element nqttaxprice = doc.createElement("nqttaxprice");
		nqttaxprice.appendChild(doc.createTextNode(hsPrice.toString()));
		item.appendChild(nqttaxprice);
		// <!--本币无税单价,最大长度为28,类型为:UFDouble-->
		Element nqtprice = doc.createElement("nqtprice");
		nqtprice.appendChild(doc.createTextNode(price.toString()));
		item.appendChild(nqtprice);
		// <!--本币含税净价,最大长度为28,类型为:UFDouble-->
		Element nqttaxnetprice = doc.createElement("nqttaxnetprice");
		nqttaxnetprice.appendChild(doc.createTextNode(jshj.toString()));
		item.appendChild(nqttaxnetprice);
		// <!--本币无税净价,最大长度为28,类型为:UFDouble-->
		Element nqtnetprice = doc.createElement("nqtnetprice");
		nqtnetprice.appendChild(doc.createTextNode(wshj.toString()));
		item.appendChild(nqtnetprice);
		// <!--主本币含税单价,最大长度为28,类型为:UFDouble-->
		Element ntaxprice = doc.createElement("ntaxprice");
		ntaxprice.appendChild(doc.createTextNode(hsPrice.toString()));
		item.appendChild(ntaxprice);	
		// <!--主本币无税单价,最大长度为28,类型为:UFDouble-->
		Element nprice = doc.createElement("nprice");
		nprice.appendChild(doc.createTextNode(price.toString()));
		item.appendChild(nprice);
		// <!--主本币含税净价,最大长度为28,类型为:UFDouble-->
		Element ntaxnetprice = doc.createElement("ntaxnetprice");
		ntaxnetprice.appendChild(doc.createTextNode(jshj.toString()));
		item.appendChild(ntaxnetprice);		
		// <!--主本币无税净价,最大长度为28,类型为:UFDouble-->
		Element nnetprice = doc.createElement("nnetprice");
		nnetprice.appendChild(doc.createTextNode(wshj.toString()));
		item.appendChild(nnetprice);
		// <!--本币无税金额,最大长度为28,类型为:UFDouble-->
		Element nmny = doc.createElement("nmny");
		nmny.appendChild(doc.createTextNode(wshj.toString()));
		item.appendChild(nmny);		
		// <!--本币价税合计,最大长度为28,类型为:UFDouble-->
		Element ntaxmny = doc.createElement("ntaxmny");
		ntaxmny.appendChild(doc.createTextNode(jshj.toString()));
		item.appendChild(ntaxmny);
		// <!--要求发货日期,最大长度为19,类型为:UFDate-->
		Element dsenddate = doc.createElement("dsenddate");
		dsenddate.appendChild(doc.createTextNode(end));
		item.appendChild(dsenddate);
		// <!--计划到货日期,最大长度为19,类型为:UFDate-->
		Element dreceivedate = doc.createElement("dreceivedate");
		dreceivedate.appendChild(doc.createTextNode(end));
		item.appendChild(dreceivedate);
		// <!--收货客户,最大长度为20,类型为:String-->
		Element creceivecustid = doc.createElement("creceivecustid");
		creceivecustid.appendChild(doc.createTextNode("1001A310000000002LKW"));
		item.appendChild(creceivecustid);
		// <!--发货库存组织,最大长度为20,类型为:String-->
		Element csendstockorgvid = doc.createElement("csendstockorgvid");
		csendstockorgvid.appendChild(doc.createTextNode("0001A2100000000027QL"));
		item.appendChild(csendstockorgvid);
		// <!--发货库存组织最新版本,最大长度为20,类型为:String-->
		Element csendstockorgid = doc.createElement("csendstockorgid");
		csendstockorgid.appendChild(doc.createTextNode("0001A2100000000027QM"));
		item.appendChild(csendstockorgid);
		// <!--结算财务组织,最大长度为20,类型为:String-->
		Element csettleorgvid = doc.createElement("csettleorgvid");
		csettleorgvid.appendChild(doc.createTextNode("0001A2100000000027QL"));
		item.appendChild(csettleorgvid);
		// <!--结算财务组织,最大长度为20,类型为:String-->
		Element csettleorgid = doc.createElement("csettleorgid");
		csettleorgid.appendChild(doc.createTextNode("0001A2100000000027QM"));
		item.appendChild(csettleorgid);
		// <!--收货国家/地区,最大长度为20,类型为:String-->
		Element crececountryid = doc.createElement("crececountryid");
		crececountryid.appendChild(doc.createTextNode("0001Z010000000079UJJ"));
		item.appendChild(crececountryid);
		// <!--发货国家/地区,最大长度为20,类型为:String-->
		Element csendcountryid = doc.createElement("csendcountryid");
		csendcountryid.appendChild(doc.createTextNode("0001Z010000000079UJJ"));
		item.appendChild(csendcountryid);
		// <!--报税国家/地区,最大长度为20,类型为:String-->
		Element ctaxcountryid = doc.createElement("ctaxcountryid");
		ctaxcountryid.appendChild(doc.createTextNode("0001Z010000000079UJJ"));
		item.appendChild(ctaxcountryid);
		// <!--购销类型,最大长度为0,类型为:Integer-->
		Element fbuysellflag = doc.createElement("fbuysellflag");
		fbuysellflag.appendChild(doc.createTextNode("1"));
		item.appendChild(fbuysellflag);
		// <!--应收组织,最大长度为20,类型为:String-->
		Element carorgvid = doc.createElement("carorgvid");
		carorgvid.appendChild(doc.createTextNode("0001A2100000000027QL"));
		item.appendChild(carorgvid);
		// <!--应收组织最新版本,最大长度为20,类型为:String-->
		Element carorgid = doc.createElement("carorgid");
		carorgid.appendChild(doc.createTextNode("0001A2100000000027QM"));
		item.appendChild(carorgid);
		// <!--行状态,最大长度为0,类型为:Integer-->
		Element frowstatus = doc.createElement("frowstatus");
		frowstatus.appendChild(doc.createTextNode("1"));
		item.appendChild(frowstatus);
		// 返回xml
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
	 * 客户对照关系 待定 模拟数据：1001A310000000002LKW
	 * @param sh_org
	 * @return
	 */
	private String getCustomer(String sh_org) {
		return "1001A310000000002LKW";
	}
	
	/**
	 * 销售组织对照关系 待定 模拟数据：0001A2100000000027QM
	 * @return
	 */
	private String getOrg() {
		return "0001A2100000000027QM";
	}
	
	/**
	 * 销售组织版本对照关系 待定 模拟数据：0001A2100000000027QL
	 * @return
	 */
	private String getOrgv() {
		return "0001A2100000000027QL";
	}

	/**
	 * 发送会计平台
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
		XMLUtil.printDOMTree(writer, doc, 0, "GB2312"); // 按照XML文件格式输出
		writer.flush();
		writer.close();
		InputStream inputStream = connection.getInputStream();
		Document resDoc = XMLUtil.getDocumentBuilder().parse(inputStream); // 解析为Doc对象
		return resDoc;
	}

	/**
	 * 获取平台地址(可配置）
	 * 
	 * @param ce
	 * @return
	 */
	public String getConnectionUrl(String corpcode) throws BusinessException {
		String url = null;
		/**
		 * 防止万一出差，如果没有，则默认为
		 */
		if (url == null) {
			// 本地
			url ="http://127.0.0.1:80/service/XChangeServlet?account=develop&groupcode=00000";
		}
		return url;
	}

	/**
	 * 解析返回值
	 * @throws Exception 
	 * 
	 */
	public HashMap<String, String> analyReturn(Document doc) throws Exception {
		HashMap<String, String> res = new HashMap<String, String>();
		String ret = "N";
		String errorInfo = "成功生成销售订单";
		if (doc != null) {
			Element rootElt = doc.getDocumentElement(); // 获取根节点
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
	 * 更新数据来源表状态
	 * @param analy 状态
	 * @param lsh 流水号
	 */
	private void updateTableStatus(String lsh, HashMap<String, String> res) throws Exception {	
		String sql = "";
		String state = res.get("state");
		String errorInfo = res.get("errorInfo");
		if ( "Y".equals(state) ) {
			sql = "update weighbridge01 set info_status = 'Y', relog = '" + errorInfo + ",销售订单号："+lsh+"' where lsh = '" + lsh + "'";
		} else {
			sql = "update weighbridge01 set info_status = 'N', relog = '" + errorInfo + "' where lsh = '" + lsh + "'";
		}
		getBaseDao().executeUpdate(sql);	
	}
	
	/**
	 * 解析返回值
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
				// 如果这个节点属于Element ,再进行取值
				if (childNode instanceof Element) {
					if ("resultdescription".equals(childNode.getNodeName())) {
						message = childNode.getFirstChild().getNodeValue();
						if (message != null && message.length() > 900) {
							message = message.substring(0, 800);
							message = message + "错误描述超长，超出部分已截断";

						}

					}
				}
			}

		}
		return message;
	}
	
	/**
	 * 测试 xml 文件
	 * 
	 * @throws Exception
	 */
	public void textXML(Document document) throws Exception {

		// 开始把Document映射到文件
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transFormer = transFactory.newTransformer();
		transFormer.setOutputProperty("encoding","gb2312"); 
		// 设置输出结果
		DOMSource domSource = new DOMSource(document);
		// 生成xml文件
		File file = new File("D://yonyou201904//罕王项目//testReturn//re.xml");
		// 判断是否存在,如果不存在,则创建
		if (!file.exists()) {
			file.createNewFile();
		}
		// 文件输出流
		FileOutputStream out = new FileOutputStream(file);
		// 设置输入源
		StreamResult xmlResult = new StreamResult(out);
		// 输出xml文件
		transFormer.transform(domSource, xmlResult);
		// 测试文件输出的路径
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
