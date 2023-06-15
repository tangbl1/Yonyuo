package nc.vo.vehicle.vorder;

import nc.vo.pub.*;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class VorderBVO extends SuperVO {

	//构造方法
	public VorderBVO() {
		super();
	}


    private nc.vo.pub.lang.UFDateTime departtime  ;
    private java.lang.String dest5  ;
    private java.lang.String finaldest  ;
    private java.lang.String destarea  ;
    private nc.vo.pub.lang.UFDateTime returntime  ;
    private java.lang.String remark  ;
    private java.lang.String turndownreason  ;
    private java.lang.String cancelreason  ;
    private java.lang.Integer selectpnum  ;
    private java.lang.String def0  ;
    private java.lang.String pk_vorder_b  ;
    private nc.vo.pub.lang.UFBoolean isfisrtapplier  ;
    private java.lang.String origin  ;
    private java.lang.String applier  ;
    private java.lang.String dept  ;
    private java.lang.String phone  ;
    private java.lang.String dest1  ;
    private java.lang.String dest2  ;
    private java.lang.String dest3  ;
    private java.lang.String dest4  ;
    private java.lang.String def4  ;
    private java.lang.String def1  ;
    private java.lang.String def2  ;
    private java.lang.String def3  ;
    private java.lang.String def5  ;
    private java.lang.String def6  ;
    private java.lang.String def7  ;
    private java.lang.String def8  ;
    private java.lang.String def10  ;
    private java.lang.String id  ;
    private java.lang.String code  ;
    private java.lang.String name  ;
    private java.lang.String rowno  ;
    private java.lang.String pk_group  ;
    private java.lang.String pk_org  ;
    private java.lang.String pk_org_v  ;
    private java.lang.Integer starlevel  ;
    private java.lang.String review  ;
    private java.lang.Integer dr  ;
    private nc.vo.pub.lang.UFDateTime ts  ;
    private java.lang.String pk_vorder  ;


	public static final String DEPARTTIME = "departtime";
	public static final String DEST5 = "dest5";
	public static final String FINALDEST = "finaldest";
	public static final String DESTAREA = "destarea";
	public static final String RETURNTIME = "returntime";
	public static final String REMARK = "remark";
	public static final String TURNDOWNREASON = "turndownreason";
	public static final String CANCELREASON = "cancelreason";
	public static final String SELECTPNUM = "selectpnum";
	public static final String DEF0 = "def0";
	public static final String PK_VORDER_B = "pk_vorder_b";
	public static final String ISFISRTAPPLIER = "isfisrtapplier";
	public static final String ORIGIN = "origin";
	public static final String APPLIER = "applier";
	public static final String DEPT = "dept";
	public static final String PHONE = "phone";
	public static final String DEST1 = "dest1";
	public static final String DEST2 = "dest2";
	public static final String DEST3 = "dest3";
	public static final String DEST4 = "dest4";
	public static final String DEF4 = "def4";
	public static final String DEF1 = "def1";
	public static final String DEF2 = "def2";
	public static final String DEF3 = "def3";
	public static final String DEF5 = "def5";
	public static final String DEF6 = "def6";
	public static final String DEF7 = "def7";
	public static final String DEF8 = "def8";
	public static final String DEF10 = "def10";
	public static final String ID = "id";
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String ROWNO = "rowno";
	public static final String PK_GROUP = "pk_group";
	public static final String PK_ORG = "pk_org";
	public static final String PK_ORG_V = "pk_org_v";
	public static final String STARLEVEL = "starlevel";
	public static final String REVIEW = "review";
	public static final String DR = "dr";
	public static final String TS = "ts";
	public static final String PK_VORDER = "pk_vorder";

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
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
	
	
	public void setPk_vorder(java.lang.String pk_vorder){
		this.pk_vorder = pk_vorder;
	}

	public java.lang.String getPk_vorder(){
		return this.pk_vorder;
	} 
	
	
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	@Override
	public java.lang.String getPKFieldName() {
	  return "pk_vorder_b";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	@Override
	public java.lang.String getTableName() {
		return "cl_vorder_b";
	}
	
	public static java.lang.String getDefaultTableName() {
		return "cl_vorder_b";
	}    
    
	@Override
	@nc.vo.annotation.MDEntityInfo(beanFullclassName =  "nc.vo.vehicle.vorder.VorderBVO" )
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("vehicle.VorderBVO");
  	}
  	
}
