package nc.vo.vehicle.vorder;

import nc.vo.pub.*;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class VorderHVO extends SuperVO {

	//构造方法
	public VorderHVO() {
		super();
	}


    private java.lang.String def3  ;
    private java.lang.String def4  ;
    private java.lang.String def5  ;
    private java.lang.String def6  ;
    private java.lang.String def7  ;
    private java.lang.String def8  ;
    private java.lang.String def10  ;
    private java.lang.String pk_group  ;
    private java.lang.String pk_org  ;
    private java.lang.String pk_org_v  ;
    private java.lang.String code  ;
    private java.lang.String name  ;
    private nc.vo.pub.lang.UFDateTime maketime  ;
    private nc.vo.pub.lang.UFDateTime lastmaketime  ;
    private nc.vo.pub.lang.UFDate vbilldate  ;
    private java.lang.String creator  ;
    private nc.vo.pub.lang.UFDateTime creationtime  ;
    private java.lang.String modifier  ;
    private nc.vo.pub.lang.UFDateTime modifiedtime  ;
    private java.lang.String busitype  ;
    private java.lang.String billmaker  ;
    private java.lang.String approver  ;
    private java.lang.Integer approvestatus  ;
    private java.lang.String approvenote  ;
    private nc.vo.pub.lang.UFDateTime approvedate  ;
    private java.lang.String transtype  ;
    private java.lang.String billtype  ;
    private java.lang.String transtypepk  ;
    private java.lang.String srcbilltype  ;
    private java.lang.String srcbillid  ;
    private java.lang.Integer emendenum  ;
    private java.lang.String billversionpk  ;
    private nc.vo.pub.lang.UFDouble startmileage  ;
    private nc.vo.pub.lang.UFDouble backmileage  ;
    private nc.vo.pub.lang.UFDouble travelmileage  ;
    private java.lang.String pk_driver  ;
    private java.lang.String pk_vorder  ;
    private java.lang.String billno  ;
    private nc.vo.pub.lang.UFBoolean iscarpool  ;
    private java.lang.String driver  ;
    private java.lang.String dphone  ;
    private java.lang.String vehicleno  ;
    private java.lang.String pk_vehicle  ;
    private java.lang.String billstate  ;
    private java.lang.String origin  ;
    private java.lang.String fdestination  ;
    private java.lang.String destarea  ;
    private java.lang.Integer remainpnum  ;
    private nc.vo.pub.lang.UFDateTime begintime  ;
    private nc.vo.pub.lang.UFDateTime endtime  ;
    private java.lang.String def0  ;
    private java.lang.String def1  ;
    private java.lang.String def2  ;
    private java.lang.Integer dr  ;
    private nc.vo.pub.lang.UFDateTime ts  ;


	public static final String DEF3 = "def3";
	public static final String DEF4 = "def4";
	public static final String DEF5 = "def5";
	public static final String DEF6 = "def6";
	public static final String DEF7 = "def7";
	public static final String DEF8 = "def8";
	public static final String DEF10 = "def10";
	public static final String PK_GROUP = "pk_group";
	public static final String PK_ORG = "pk_org";
	public static final String PK_ORG_V = "pk_org_v";
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String MAKETIME = "maketime";
	public static final String LASTMAKETIME = "lastmaketime";
	public static final String VBILLDATE = "vbilldate";
	public static final String CREATOR = "creator";
	public static final String CREATIONTIME = "creationtime";
	public static final String MODIFIER = "modifier";
	public static final String MODIFIEDTIME = "modifiedtime";
	public static final String BUSITYPE = "busitype";
	public static final String BILLMAKER = "billmaker";
	public static final String APPROVER = "approver";
	public static final String APPROVESTATUS = "approvestatus";
	public static final String APPROVENOTE = "approvenote";
	public static final String APPROVEDATE = "approvedate";
	public static final String TRANSTYPE = "transtype";
	public static final String BILLTYPE = "billtype";
	public static final String TRANSTYPEPK = "transtypepk";
	public static final String SRCBILLTYPE = "srcbilltype";
	public static final String SRCBILLID = "srcbillid";
	public static final String EMENDENUM = "emendenum";
	public static final String BILLVERSIONPK = "billversionpk";
	public static final String STARTMILEAGE = "startmileage";
	public static final String BACKMILEAGE = "backmileage";
	public static final String TRAVELMILEAGE = "travelmileage";
	public static final String PK_DRIVER = "pk_driver";
	public static final String PK_VORDER = "pk_vorder";
	public static final String BILLNO = "billno";
	public static final String ISCARPOOL = "iscarpool";
	public static final String DRIVER = "driver";
	public static final String DPHONE = "dphone";
	public static final String VEHICLENO = "vehicleno";
	public static final String PK_VEHICLE = "pk_vehicle";
	public static final String BILLSTATE = "billstate";
	public static final String ORIGIN = "origin";
	public static final String FDESTINATION = "fdestination";
	public static final String DESTAREA = "destarea";
	public static final String REMAINPNUM = "remainpnum";
	public static final String BEGINTIME = "begintime";
	public static final String ENDTIME = "endtime";
	public static final String DEF0 = "def0";
	public static final String DEF1 = "def1";
	public static final String DEF2 = "def2";
	public static final String DR = "dr";
	public static final String TS = "ts";

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
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
	  return "pk_vorder";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	@Override
	public java.lang.String getTableName() {
		return "cl_vorder";
	}
	
	public static java.lang.String getDefaultTableName() {
		return "cl_vorder";
	}    
    
	@Override
	@nc.vo.annotation.MDEntityInfo(beanFullclassName =  "nc.vo.vehicle.vorder.VorderHVO" )
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("vehicle.VorderHVO");
  	}
  	
}
