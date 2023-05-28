package nc.vo.yz.xfmx;

import nc.vo.pub.*;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class Xfmx extends SuperVO {

	//构造方法
	public Xfmx() {
		super();
	}


    private java.lang.String pk_xfmx  ;
    private java.lang.String code  ;
    private java.lang.String name  ;
    private java.lang.String pk_group  ;
    private java.lang.String pk_org  ;
    private java.lang.String pk_org_v  ;
    private java.lang.String creator  ;
    private nc.vo.pub.lang.UFDateTime creationtime  ;
    private java.lang.String modifier  ;
    private nc.vo.pub.lang.UFDateTime modifiedtime  ;
    private nc.vo.pub.lang.UFDate billdate  ;
    private java.lang.String approver  ;
    private nc.vo.pub.lang.UFDateTime approvedate  ;
    private java.lang.String billno  ;
    private java.lang.String billmaker  ;
    private nc.vo.pub.lang.UFDate documentdate  ;
    private java.lang.String approvenote  ;
    private java.lang.String billtype  ;
    private java.lang.String busitype  ;
    private java.lang.String transtype  ;
    private java.lang.String transtypepk  ;
    private java.lang.String srcbilltype  ;
    private java.lang.String srcbillid  ;
    private java.lang.String billversionpk  ;
    private java.lang.Integer emendenum  ;
    private java.lang.Integer approvestatus  ;
    private java.lang.String deptcode  ;
    private java.lang.String deptname  ;
    private java.lang.String psndoccode  ;
    private java.lang.String psndocname  ;
    private java.lang.String operation  ;
    private nc.vo.pub.lang.UFTime xfdate  ;
    private java.lang.String xftype  ;
    private nc.vo.pub.lang.UFDouble xfmoney  ;
    private nc.vo.pub.lang.UFDouble ymoney  ;
    private java.lang.String note  ;
    private nc.vo.pub.lang.UFDouble tnumber  ;
    private java.lang.String vdef1  ;
    private java.lang.String vdef2  ;
    private java.lang.String vdef3  ;
    private java.lang.String vdef4  ;
    private java.lang.String vdef5  ;
    private java.lang.String vdef6  ;
    private java.lang.String vdef7  ;
    private java.lang.String vdef8  ;
    private java.lang.String vdef9  ;
    private java.lang.String vdef10  ;
    private java.lang.String vdef11  ;
    private java.lang.String vdef12  ;
    private java.lang.String vdef13  ;
    private java.lang.String vdef14  ;
    private java.lang.String vdef15  ;
    private java.lang.String vdef16  ;
    private java.lang.String vdef17  ;
    private java.lang.String vdef18  ;
    private java.lang.String vdef19  ;
    private java.lang.String vdef20  ;
    private java.lang.Integer dr  ;
    private nc.vo.pub.lang.UFDateTime ts  ;


	public static final String PK_XFMX = "pk_xfmx";
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String PK_GROUP = "pk_group";
	public static final String PK_ORG = "pk_org";
	public static final String PK_ORG_V = "pk_org_v";
	public static final String CREATOR = "creator";
	public static final String CREATIONTIME = "creationtime";
	public static final String MODIFIER = "modifier";
	public static final String MODIFIEDTIME = "modifiedtime";
	public static final String BILLDATE = "billdate";
	public static final String APPROVER = "approver";
	public static final String APPROVEDATE = "approvedate";
	public static final String BILLNO = "billno";
	public static final String BILLMAKER = "billmaker";
	public static final String DOCUMENTDATE = "documentdate";
	public static final String APPROVENOTE = "approvenote";
	public static final String BILLTYPE = "billtype";
	public static final String BUSITYPE = "busitype";
	public static final String TRANSTYPE = "transtype";
	public static final String TRANSTYPEPK = "transtypepk";
	public static final String SRCBILLTYPE = "srcbilltype";
	public static final String SRCBILLID = "srcbillid";
	public static final String BILLVERSIONPK = "billversionpk";
	public static final String EMENDENUM = "emendenum";
	public static final String APPROVESTATUS = "approvestatus";
	public static final String DEPTCODE = "deptcode";
	public static final String DEPTNAME = "deptname";
	public static final String PSNDOCCODE = "psndoccode";
	public static final String PSNDOCNAME = "psndocname";
	public static final String OPERATION = "operation";
	public static final String XFDATE = "xfdate";
	public static final String XFTYPE = "xftype";
	public static final String XFMONEY = "xfmoney";
	public static final String YMONEY = "ymoney";
	public static final String NOTE = "note";
	public static final String TNUMBER = "tnumber";
	public static final String VDEF1 = "vdef1";
	public static final String VDEF2 = "vdef2";
	public static final String VDEF3 = "vdef3";
	public static final String VDEF4 = "vdef4";
	public static final String VDEF5 = "vdef5";
	public static final String VDEF6 = "vdef6";
	public static final String VDEF7 = "vdef7";
	public static final String VDEF8 = "vdef8";
	public static final String VDEF9 = "vdef9";
	public static final String VDEF10 = "vdef10";
	public static final String VDEF11 = "vdef11";
	public static final String VDEF12 = "vdef12";
	public static final String VDEF13 = "vdef13";
	public static final String VDEF14 = "vdef14";
	public static final String VDEF15 = "vdef15";
	public static final String VDEF16 = "vdef16";
	public static final String VDEF17 = "vdef17";
	public static final String VDEF18 = "vdef18";
	public static final String VDEF19 = "vdef19";
	public static final String VDEF20 = "vdef20";
	public static final String DR = "dr";
	public static final String TS = "ts";

	
	public void setCode(java.lang.String code){
		this.code = code;
	}

	public java.lang.String getCode(){
		return this.code;
	} 
	
	public void setName(java.lang.String name){
		this.name = name;
	}

	public java.lang.String getName(){
		return this.name;
	} 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void setEmendenum(java.lang.Integer emendenum){
		this.emendenum = emendenum;
	}

	public java.lang.Integer getEmendenum(){
		return this.emendenum;
	} 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void setDr(java.lang.Integer dr){
		this.dr = dr;
	}

	public java.lang.Integer getDr(){
		return this.dr;
	} 
	
	public void setTs(nc.vo.pub.lang.UFDateTime ts){
		this.ts = ts;
	}

	public nc.vo.pub.lang.UFDateTime getTs(){
		return this.ts;
	} 
	
	
	
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	@Override
	public java.lang.String getPKFieldName() {
	  return "pk_xfmx";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	@Override
	public java.lang.String getTableName() {
		return "comsumelog";
	}
	
	public static java.lang.String getDefaultTableName() {
		return "comsumelog";
	}    
    
	@Override
	@nc.vo.annotation.MDEntityInfo(beanFullclassName =  "nc.vo.yz.xfmx.Xfmx" )
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("hanwang.xfmx");
  	}
  	
}
