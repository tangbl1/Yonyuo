package nc.vo.uapbd.pricemanage;


import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class Baseprice extends SuperVO {

	//构造方法
	public Baseprice() {
		super();
	}


    private java.lang.String pk_baseprice  ;
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
    private nc.vo.pub.lang.UFDouble baseprice  ;
    private nc.vo.pub.lang.UFDateTime begindate  ;
    private nc.vo.pub.lang.UFDateTime enddate  ;
    private nc.vo.pub.lang.UFBoolean islastest  ;
    private java.lang.String modifier  ;
    private nc.vo.pub.lang.UFDateTime modifiedtime  ;
    private java.lang.String creator  ;
    private nc.vo.pub.lang.UFDateTime createtime  ;
    private java.lang.String def1  ;
    private java.lang.String def2  ;
    private java.lang.String def3  ;
    private java.lang.Integer dr  ;
    private nc.vo.pub.lang.UFDateTime ts  ;
    private java.lang.String pk_pricemanage  ;


	public static final String PK_BASEPRICE = "pk_baseprice";
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
	public static final String BASEPRICE = "baseprice";
	public static final String BEGINDATE = "begindate";
	public static final String ENDDATE = "enddate";
	public static final String ISLASTEST = "islastest";
	public static final String MODIFIER = "modifier";
	public static final String MODIFIEDTIME = "modifiedtime";
	public static final String CREATOR = "creator";
	public static final String CREATETIME = "createtime";
	public static final String DEF1 = "def1";
	public static final String DEF2 = "def2";
	public static final String DEF3 = "def3";
	public static final String DR = "dr";
	public static final String TS = "ts";
	public static final String PK_PRICEMANAGE = "pk_pricemanage";

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
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
	
	
	public void setPk_pricemanage(java.lang.String pk_pricemanage){
		this.pk_pricemanage = pk_pricemanage;
	}

	public java.lang.String getPk_pricemanage(){
		return this.pk_pricemanage;
	} 
	
	
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	@Override
	public java.lang.String getPKFieldName() {
	  return "pk_baseprice";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	@Override
	public java.lang.String getTableName() {
		return "uapbd_Baseprice";
	}
	
	public static java.lang.String getDefaultTableName() {
		return "uapbd_Baseprice";
	}    
    
	@Override
	@nc.vo.annotation.MDEntityInfo(beanFullclassName =  "nc.vo.uapbd.pricemanage.Baseprice" )
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("uapbd.Baseprice");
  	}
  	
}
