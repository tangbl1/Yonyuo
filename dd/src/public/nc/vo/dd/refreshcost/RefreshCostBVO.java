package nc.vo.dd.refreshcost;

import nc.vo.pub.*;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class RefreshCostBVO extends SuperVO {

	//构造方法
	public RefreshCostBVO() {
		super();
	}


    private java.lang.String pk_cost_b  ;
    private java.lang.String rowno  ;
    private java.lang.String equip_code  ;
    private java.lang.String equip_name  ;
    private java.lang.String equip_code2  ;
    private java.lang.String equip_name2  ;
    private java.lang.String equip_code3  ;
    private java.lang.String equip_name3  ;
    private java.lang.String pk_org  ;
    private java.lang.String pk_org_v  ;
    private java.lang.String pk_group  ;
    private java.lang.String spec  ;
    private java.lang.String equiptype  ;
    private java.lang.String pk_category  ;
    private java.lang.String pk_used_status  ;
    private nc.vo.pub.lang.UFDate status_date  ;
    private java.lang.String pk_priority  ;
    private java.lang.String pk_capital_source  ;
    private java.lang.String pk_location  ;
    private java.lang.String pk_mandept  ;
    private java.lang.String pk_manager  ;
    private java.lang.String pk_usedept  ;
    private java.lang.String pk_user  ;
    private nc.vo.pub.lang.UFBoolean is_spare  ;
    private nc.vo.pub.lang.UFBoolean is_energy  ;
    private nc.vo.pub.lang.UFBoolean is_other  ;
    private java.lang.String srcrowno  ;
    private java.lang.String vbmemo  ;
    private java.lang.String srcbilltype  ;
    private java.lang.String srcbillid  ;
    private java.lang.String vbdef1  ;
    private java.lang.String vbdef2  ;
    private java.lang.String vbdef3  ;
    private java.lang.String vbdef4  ;
    private java.lang.String vbdef5  ;
    private java.lang.String vbdef6  ;
    private java.lang.String vbdef7  ;
    private java.lang.String vbdef8  ;
    private java.lang.String vbdef9  ;
    private java.lang.String vbdef10  ;
    private java.lang.Integer dr  ;
    private nc.vo.pub.lang.UFDateTime ts  ;
    private java.lang.String pk_cost  ;


	public static final String PK_COST_B = "pk_cost_b";
	public static final String ROWNO = "rowno";
	public static final String EQUIP_CODE = "equip_code";
	public static final String EQUIP_NAME = "equip_name";
	public static final String EQUIP_CODE2 = "equip_code2";
	public static final String EQUIP_NAME2 = "equip_name2";
	public static final String EQUIP_CODE3 = "equip_code3";
	public static final String EQUIP_NAME3 = "equip_name3";
	public static final String PK_ORG = "pk_org";
	public static final String PK_ORG_V = "pk_org_v";
	public static final String PK_GROUP = "pk_group";
	public static final String SPEC = "spec";
	public static final String EQUIPTYPE = "equiptype";
	public static final String PK_CATEGORY = "pk_category";
	public static final String PK_USED_STATUS = "pk_used_status";
	public static final String STATUS_DATE = "status_date";
	public static final String PK_PRIORITY = "pk_priority";
	public static final String PK_CAPITAL_SOURCE = "pk_capital_source";
	public static final String PK_LOCATION = "pk_location";
	public static final String PK_MANDEPT = "pk_mandept";
	public static final String PK_MANAGER = "pk_manager";
	public static final String PK_USEDEPT = "pk_usedept";
	public static final String PK_USER = "pk_user";
	public static final String IS_SPARE = "is_spare";
	public static final String IS_ENERGY = "is_energy";
	public static final String IS_OTHER = "is_other";
	public static final String SRCROWNO = "srcrowno";
	public static final String VBMEMO = "vbmemo";
	public static final String SRCBILLTYPE = "srcbilltype";
	public static final String SRCBILLID = "srcbillid";
	public static final String VBDEF1 = "vbdef1";
	public static final String VBDEF2 = "vbdef2";
	public static final String VBDEF3 = "vbdef3";
	public static final String VBDEF4 = "vbdef4";
	public static final String VBDEF5 = "vbdef5";
	public static final String VBDEF6 = "vbdef6";
	public static final String VBDEF7 = "vbdef7";
	public static final String VBDEF8 = "vbdef8";
	public static final String VBDEF9 = "vbdef9";
	public static final String VBDEF10 = "vbdef10";
	public static final String DR = "dr";
	public static final String TS = "ts";
	public static final String PK_COST = "pk_cost";

	public void setPk_cost_b(java.lang.String pk_cost_b){
		this.pk_cost_b = pk_cost_b;
	}

	public java.lang.String getPk_cost_b(){
		return this.pk_cost_b;
	} 
	
	public void setRowno(java.lang.String rowno){
		this.rowno = rowno;
	}

	public java.lang.String getRowno(){
		return this.rowno;
	} 
	
	public void setEquip_code(java.lang.String equip_code){
		this.equip_code = equip_code;
	}

	public java.lang.String getEquip_code(){
		return this.equip_code;
	} 
	
	public void setEquip_name(java.lang.String equip_name){
		this.equip_name = equip_name;
	}

	public java.lang.String getEquip_name(){
		return this.equip_name;
	} 
	
	public void setEquip_code2(java.lang.String equip_code2){
		this.equip_code2 = equip_code2;
	}

	public java.lang.String getEquip_code2(){
		return this.equip_code2;
	} 
	
	public void setEquip_name2(java.lang.String equip_name2){
		this.equip_name2 = equip_name2;
	}

	public java.lang.String getEquip_name2(){
		return this.equip_name2;
	} 
	
	public void setEquip_code3(java.lang.String equip_code3){
		this.equip_code3 = equip_code3;
	}

	public java.lang.String getEquip_code3(){
		return this.equip_code3;
	} 
	
	public void setEquip_name3(java.lang.String equip_name3){
		this.equip_name3 = equip_name3;
	}

	public java.lang.String getEquip_name3(){
		return this.equip_name3;
	} 
	
	public void setPk_org(java.lang.String pk_org){
		this.pk_org = pk_org;
	}

	public java.lang.String getPk_org(){
		return this.pk_org;
	} 
	
	public void setPk_org_v(java.lang.String pk_org_v){
		this.pk_org_v = pk_org_v;
	}

	public java.lang.String getPk_org_v(){
		return this.pk_org_v;
	} 
	
	public void setPk_group(java.lang.String pk_group){
		this.pk_group = pk_group;
	}

	public java.lang.String getPk_group(){
		return this.pk_group;
	} 
	
	public void setSpec(java.lang.String spec){
		this.spec = spec;
	}

	public java.lang.String getSpec(){
		return this.spec;
	} 
	
	public void setEquiptype(java.lang.String equiptype){
		this.equiptype = equiptype;
	}

	public java.lang.String getEquiptype(){
		return this.equiptype;
	} 
	
	public void setPk_category(java.lang.String pk_category){
		this.pk_category = pk_category;
	}

	public java.lang.String getPk_category(){
		return this.pk_category;
	} 
	
	public void setPk_used_status(java.lang.String pk_used_status){
		this.pk_used_status = pk_used_status;
	}

	public java.lang.String getPk_used_status(){
		return this.pk_used_status;
	} 
	
	public void setStatus_date(nc.vo.pub.lang.UFDate status_date){
		this.status_date = status_date;
	}

	public nc.vo.pub.lang.UFDate getStatus_date(){
		return this.status_date;
	} 
	
	public void setPk_priority(java.lang.String pk_priority){
		this.pk_priority = pk_priority;
	}

	public java.lang.String getPk_priority(){
		return this.pk_priority;
	} 
	
	public void setPk_capital_source(java.lang.String pk_capital_source){
		this.pk_capital_source = pk_capital_source;
	}

	public java.lang.String getPk_capital_source(){
		return this.pk_capital_source;
	} 
	
	public void setPk_location(java.lang.String pk_location){
		this.pk_location = pk_location;
	}

	public java.lang.String getPk_location(){
		return this.pk_location;
	} 
	
	public void setPk_mandept(java.lang.String pk_mandept){
		this.pk_mandept = pk_mandept;
	}

	public java.lang.String getPk_mandept(){
		return this.pk_mandept;
	} 
	
	public void setPk_manager(java.lang.String pk_manager){
		this.pk_manager = pk_manager;
	}

	public java.lang.String getPk_manager(){
		return this.pk_manager;
	} 
	
	public void setPk_usedept(java.lang.String pk_usedept){
		this.pk_usedept = pk_usedept;
	}

	public java.lang.String getPk_usedept(){
		return this.pk_usedept;
	} 
	
	public void setPk_user(java.lang.String pk_user){
		this.pk_user = pk_user;
	}

	public java.lang.String getPk_user(){
		return this.pk_user;
	} 
	
	public void setIs_spare(nc.vo.pub.lang.UFBoolean is_spare){
		this.is_spare = is_spare;
	}

	public nc.vo.pub.lang.UFBoolean getIs_spare(){
		return this.is_spare;
	} 
	
	public void setIs_energy(nc.vo.pub.lang.UFBoolean is_energy){
		this.is_energy = is_energy;
	}

	public nc.vo.pub.lang.UFBoolean getIs_energy(){
		return this.is_energy;
	} 
	
	public void setIs_other(nc.vo.pub.lang.UFBoolean is_other){
		this.is_other = is_other;
	}

	public nc.vo.pub.lang.UFBoolean getIs_other(){
		return this.is_other;
	} 
	
	public void setSrcrowno(java.lang.String srcrowno){
		this.srcrowno = srcrowno;
	}

	public java.lang.String getSrcrowno(){
		return this.srcrowno;
	} 
	
	public void setVbmemo(java.lang.String vbmemo){
		this.vbmemo = vbmemo;
	}

	public java.lang.String getVbmemo(){
		return this.vbmemo;
	} 
	
	public void setSrcbilltype(java.lang.String srcbilltype){
		this.srcbilltype = srcbilltype;
	}

	public java.lang.String getSrcbilltype(){
		return this.srcbilltype;
	} 
	
	public void setSrcbillid(java.lang.String srcbillid){
		this.srcbillid = srcbillid;
	}

	public java.lang.String getSrcbillid(){
		return this.srcbillid;
	} 
	
	public void setVbdef1(java.lang.String vbdef1){
		this.vbdef1 = vbdef1;
	}

	public java.lang.String getVbdef1(){
		return this.vbdef1;
	} 
	
	public void setVbdef2(java.lang.String vbdef2){
		this.vbdef2 = vbdef2;
	}

	public java.lang.String getVbdef2(){
		return this.vbdef2;
	} 
	
	public void setVbdef3(java.lang.String vbdef3){
		this.vbdef3 = vbdef3;
	}

	public java.lang.String getVbdef3(){
		return this.vbdef3;
	} 
	
	public void setVbdef4(java.lang.String vbdef4){
		this.vbdef4 = vbdef4;
	}

	public java.lang.String getVbdef4(){
		return this.vbdef4;
	} 
	
	public void setVbdef5(java.lang.String vbdef5){
		this.vbdef5 = vbdef5;
	}

	public java.lang.String getVbdef5(){
		return this.vbdef5;
	} 
	
	public void setVbdef6(java.lang.String vbdef6){
		this.vbdef6 = vbdef6;
	}

	public java.lang.String getVbdef6(){
		return this.vbdef6;
	} 
	
	public void setVbdef7(java.lang.String vbdef7){
		this.vbdef7 = vbdef7;
	}

	public java.lang.String getVbdef7(){
		return this.vbdef7;
	} 
	
	public void setVbdef8(java.lang.String vbdef8){
		this.vbdef8 = vbdef8;
	}

	public java.lang.String getVbdef8(){
		return this.vbdef8;
	} 
	
	public void setVbdef9(java.lang.String vbdef9){
		this.vbdef9 = vbdef9;
	}

	public java.lang.String getVbdef9(){
		return this.vbdef9;
	} 
	
	public void setVbdef10(java.lang.String vbdef10){
		this.vbdef10 = vbdef10;
	}

	public java.lang.String getVbdef10(){
		return this.vbdef10;
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
	
	
	public void setPk_cost(java.lang.String pk_cost){
		this.pk_cost = pk_cost;
	}

	public java.lang.String getPk_cost(){
		return this.pk_cost;
	} 
	
	
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	@Override
	public java.lang.String getPKFieldName() {
	  return "pk_cost_b";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	@Override
	public java.lang.String getTableName() {
		return "dd_refreshcost_b";
	}
	
	public static java.lang.String getDefaultTableName() {
		return "dd_refreshcost_b";
	}    
    
	@Override
	@nc.vo.annotation.MDEntityInfo(beanFullclassName =  "nc.vo.dd.refreshcost.RefreshCostBVO" )
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("dd.RefreshCostBVO");
  	}
  	
}
