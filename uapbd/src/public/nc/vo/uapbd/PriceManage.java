package nc.vo.uapbd;

import nc.vo.pub.*;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class PriceManage extends SuperVO {

	//构造方法
	public PriceManage() {
		super();
	}


    private java.lang.String pk_pricemanage  ;
    private java.lang.String rowno  ;
    private java.lang.String name  ;
    private java.lang.String pk_customer  ;
    private java.lang.String pk_group  ;
    private java.lang.String pk_org  ;
    private java.lang.String pk_org_v  ;
    private java.lang.String code  ;
    private nc.vo.pub.lang.UFDate maketime  ;
    private nc.vo.pub.lang.UFDateTime creationtime  ;
    private java.lang.String creator  ;
    private nc.vo.pub.lang.UFDateTime modifiedtime  ;
    private java.lang.String modifier  ;
    private java.lang.String srcrowno  ;
    private java.lang.String def1  ;
    private java.lang.String def2  ;
    private java.lang.String def3  ;
    private java.lang.String def4  ;
    private java.lang.String def5  ;
    private java.lang.String def6  ;
    private java.lang.String def7  ;
    private java.lang.String def8  ;
    private java.lang.String def9  ;
    private java.lang.String def10  ;
    private java.lang.String def11  ;
    private java.lang.String def12  ;
    private java.lang.String def13  ;
    private java.lang.String def14  ;
    private java.lang.String def15  ;
    private java.lang.String def16  ;
    private java.lang.String def17  ;
    private java.lang.String def18  ;
    private java.lang.String def19  ;
    private java.lang.String def20  ;
    private java.lang.Integer dr  ;
    private nc.vo.pub.lang.UFDateTime ts  ;


	public static final String PK_PRICEMANAGE = "pk_pricemanage";
	public static final String ROWNO = "rowno";
	public static final String NAME = "name";
	public static final String PK_CUSTOMER = "pk_customer";
	public static final String PK_GROUP = "pk_group";
	public static final String PK_ORG = "pk_org";
	public static final String PK_ORG_V = "pk_org_v";
	public static final String CODE = "code";
	public static final String MAKETIME = "maketime";
	public static final String CREATIONTIME = "creationtime";
	public static final String CREATOR = "creator";
	public static final String MODIFIEDTIME = "modifiedtime";
	public static final String MODIFIER = "modifier";
	public static final String SRCROWNO = "srcrowno";
	public static final String DEF1 = "def1";
	public static final String DEF2 = "def2";
	public static final String DEF3 = "def3";
	public static final String DEF4 = "def4";
	public static final String DEF5 = "def5";
	public static final String DEF6 = "def6";
	public static final String DEF7 = "def7";
	public static final String DEF8 = "def8";
	public static final String DEF9 = "def9";
	public static final String DEF10 = "def10";
	public static final String DEF11 = "def11";
	public static final String DEF12 = "def12";
	public static final String DEF13 = "def13";
	public static final String DEF14 = "def14";
	public static final String DEF15 = "def15";
	public static final String DEF16 = "def16";
	public static final String DEF17 = "def17";
	public static final String DEF18 = "def18";
	public static final String DEF19 = "def19";
	public static final String DEF20 = "def20";
	public static final String DR = "dr";
	public static final String TS = "ts";

	
	public void setRowno(java.lang.String rowno){
		this.rowno = rowno;
	}

	public java.lang.String getRowno(){
		return this.rowno;
	} 
	
	public void setName(java.lang.String name){
		this.name = name;
	}

	public java.lang.String getName(){
		return this.name;
	} 
	
	
	
	
	
	
	
	
	
	
	
	public void setSrcrowno(java.lang.String srcrowno){
		this.srcrowno = srcrowno;
	}

	public java.lang.String getSrcrowno(){
		return this.srcrowno;
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
	  return "pk_pricemanage";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	@Override
	public java.lang.String getTableName() {
		return "uapbd_pricemanage";
	}
	
	public static java.lang.String getDefaultTableName() {
		return "uapbd_pricemanage";
	}    
    
	@Override
	@nc.vo.annotation.MDEntityInfo(beanFullclassName =  "nc.vo.uapbd.PriceManage" )
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("uapbd.PriceManage");
  	}
  	
}
