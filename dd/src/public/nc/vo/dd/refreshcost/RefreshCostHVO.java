package nc.vo.dd.refreshcost;

import nc.vo.pub.*;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class RefreshCostHVO extends SuperVO {

	//构造方法
	public RefreshCostHVO() {
		super();
	}


    private java.lang.String pk_cost  ;
    private java.lang.String code  ;
    private java.lang.String name  ;
    private java.lang.String pk_group  ;
    private java.lang.String pk_org  ;
    private java.lang.String pk_org_v  ;
    private java.lang.String creator  ;
    private nc.vo.pub.lang.UFDateTime creationtime  ;
    private nc.vo.pub.lang.UFDate dbilldate  ;
    private java.lang.String pk_period  ;
    private java.lang.String billcode  ;
    private java.lang.String vmemo  ;
    private java.lang.String pkorg  ;
    private java.lang.String busitype  ;
    private java.lang.String billmaker  ;
    private nc.vo.pub.lang.UFDateTime maketime  ;
    private java.lang.String modifier  ;
    private nc.vo.pub.lang.UFDateTime modifiedtime  ;
    private java.lang.String approver  ;
    private java.lang.Integer approvestatus  ;
    private java.lang.String approvenote  ;
    private nc.vo.pub.lang.UFDateTime approvedate  ;
    private java.lang.String transtype  ;
    private java.lang.String billtype  ;
    private java.lang.String transtypepk  ;
    private java.lang.Integer emendenum  ;
    private java.lang.String billversionpk  ;
    private java.lang.String srcbilltype  ;
    private java.lang.String srcbillid  ;
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
    private java.lang.Integer dr  ;
    private nc.vo.pub.lang.UFDateTime ts  ;


	public static final String PK_COST = "pk_cost";
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String PK_GROUP = "pk_group";
	public static final String PK_ORG = "pk_org";
	public static final String PK_ORG_V = "pk_org_v";
	public static final String CREATOR = "creator";
	public static final String CREATIONTIME = "creationtime";
	public static final String DBILLDATE = "dbilldate";
	public static final String PK_PERIOD = "pk_period";
	public static final String BILLCODE = "billcode";
	public static final String VMEMO = "vmemo";
	public static final String PKORG = "pkorg";
	public static final String BUSITYPE = "busitype";
	public static final String BILLMAKER = "billmaker";
	public static final String MAKETIME = "maketime";
	public static final String MODIFIER = "modifier";
	public static final String MODIFIEDTIME = "modifiedtime";
	public static final String APPROVER = "approver";
	public static final String APPROVESTATUS = "approvestatus";
	public static final String APPROVENOTE = "approvenote";
	public static final String APPROVEDATE = "approvedate";
	public static final String TRANSTYPE = "transtype";
	public static final String BILLTYPE = "billtype";
	public static final String TRANSTYPEPK = "transtypepk";
	public static final String EMENDENUM = "emendenum";
	public static final String BILLVERSIONPK = "billversionpk";
	public static final String SRCBILLTYPE = "srcbilltype";
	public static final String SRCBILLID = "srcbillid";
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
	public static final String DR = "dr";
	public static final String TS = "ts";

	public void setPk_cost(java.lang.String pk_cost){
		this.pk_cost = pk_cost;
	}

	public java.lang.String getPk_cost(){
		return this.pk_cost;
	} 
	
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
	
	public void setPk_group(java.lang.String pk_group){
		this.pk_group = pk_group;
	}

	public java.lang.String getPk_group(){
		return this.pk_group;
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
	
	public void setCreator(java.lang.String creator){
		this.creator = creator;
	}

	public java.lang.String getCreator(){
		return this.creator;
	} 
	
	public void setCreationtime(nc.vo.pub.lang.UFDateTime creationtime){
		this.creationtime = creationtime;
	}

	public nc.vo.pub.lang.UFDateTime getCreationtime(){
		return this.creationtime;
	} 
	
	public void setDbilldate(nc.vo.pub.lang.UFDate dbilldate){
		this.dbilldate = dbilldate;
	}

	public nc.vo.pub.lang.UFDate getDbilldate(){
		return this.dbilldate;
	} 
	
	public void setPk_period(java.lang.String pk_period){
		this.pk_period = pk_period;
	}

	public java.lang.String getPk_period(){
		return this.pk_period;
	} 
	
	public void setBillcode(java.lang.String billcode){
		this.billcode = billcode;
	}

	public java.lang.String getBillcode(){
		return this.billcode;
	} 
	
	public void setVmemo(java.lang.String vmemo){
		this.vmemo = vmemo;
	}

	public java.lang.String getVmemo(){
		return this.vmemo;
	} 
	
	public void setPkorg(java.lang.String pkorg){
		this.pkorg = pkorg;
	}

	public java.lang.String getPkorg(){
		return this.pkorg;
	} 
	
	public void setBusitype(java.lang.String busitype){
		this.busitype = busitype;
	}

	public java.lang.String getBusitype(){
		return this.busitype;
	} 
	
	public void setBillmaker(java.lang.String billmaker){
		this.billmaker = billmaker;
	}

	public java.lang.String getBillmaker(){
		return this.billmaker;
	} 
	
	public void setMaketime(nc.vo.pub.lang.UFDateTime maketime){
		this.maketime = maketime;
	}

	public nc.vo.pub.lang.UFDateTime getMaketime(){
		return this.maketime;
	} 
	
	public void setModifier(java.lang.String modifier){
		this.modifier = modifier;
	}

	public java.lang.String getModifier(){
		return this.modifier;
	} 
	
	public void setModifiedtime(nc.vo.pub.lang.UFDateTime modifiedtime){
		this.modifiedtime = modifiedtime;
	}

	public nc.vo.pub.lang.UFDateTime getModifiedtime(){
		return this.modifiedtime;
	} 
	
	public void setApprover(java.lang.String approver){
		this.approver = approver;
	}

	public java.lang.String getApprover(){
		return this.approver;
	} 
	
	public void setApprovestatus(java.lang.Integer approvestatus){
		this.approvestatus = approvestatus;
	}

	public java.lang.Integer getApprovestatus(){
		return this.approvestatus;
	} 
	
	public void setApprovenote(java.lang.String approvenote){
		this.approvenote = approvenote;
	}

	public java.lang.String getApprovenote(){
		return this.approvenote;
	} 
	
	public void setApprovedate(nc.vo.pub.lang.UFDateTime approvedate){
		this.approvedate = approvedate;
	}

	public nc.vo.pub.lang.UFDateTime getApprovedate(){
		return this.approvedate;
	} 
	
	public void setTranstype(java.lang.String transtype){
		this.transtype = transtype;
	}

	public java.lang.String getTranstype(){
		return this.transtype;
	} 
	
	public void setBilltype(java.lang.String billtype){
		this.billtype = billtype;
	}

	public java.lang.String getBilltype(){
		return this.billtype;
	} 
	
	public void setTranstypepk(java.lang.String transtypepk){
		this.transtypepk = transtypepk;
	}

	public java.lang.String getTranstypepk(){
		return this.transtypepk;
	} 
	
	public void setEmendenum(java.lang.Integer emendenum){
		this.emendenum = emendenum;
	}

	public java.lang.Integer getEmendenum(){
		return this.emendenum;
	} 
	
	public void setBillversionpk(java.lang.String billversionpk){
		this.billversionpk = billversionpk;
	}

	public java.lang.String getBillversionpk(){
		return this.billversionpk;
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
	
	public void setVdef1(java.lang.String vdef1){
		this.vdef1 = vdef1;
	}

	public java.lang.String getVdef1(){
		return this.vdef1;
	} 
	
	public void setVdef2(java.lang.String vdef2){
		this.vdef2 = vdef2;
	}

	public java.lang.String getVdef2(){
		return this.vdef2;
	} 
	
	public void setVdef3(java.lang.String vdef3){
		this.vdef3 = vdef3;
	}

	public java.lang.String getVdef3(){
		return this.vdef3;
	} 
	
	public void setVdef4(java.lang.String vdef4){
		this.vdef4 = vdef4;
	}

	public java.lang.String getVdef4(){
		return this.vdef4;
	} 
	
	public void setVdef5(java.lang.String vdef5){
		this.vdef5 = vdef5;
	}

	public java.lang.String getVdef5(){
		return this.vdef5;
	} 
	
	public void setVdef6(java.lang.String vdef6){
		this.vdef6 = vdef6;
	}

	public java.lang.String getVdef6(){
		return this.vdef6;
	} 
	
	public void setVdef7(java.lang.String vdef7){
		this.vdef7 = vdef7;
	}

	public java.lang.String getVdef7(){
		return this.vdef7;
	} 
	
	public void setVdef8(java.lang.String vdef8){
		this.vdef8 = vdef8;
	}

	public java.lang.String getVdef8(){
		return this.vdef8;
	} 
	
	public void setVdef9(java.lang.String vdef9){
		this.vdef9 = vdef9;
	}

	public java.lang.String getVdef9(){
		return this.vdef9;
	} 
	
	public void setVdef10(java.lang.String vdef10){
		this.vdef10 = vdef10;
	}

	public java.lang.String getVdef10(){
		return this.vdef10;
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
	  return "pk_cost";
	}
    
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:
	 * @return java.lang.String
	 */
	@Override
	public java.lang.String getTableName() {
		return "dd_refreshcost";
	}
	
	public static java.lang.String getDefaultTableName() {
		return "dd_refreshcost";
	}    
    
	@Override
	@nc.vo.annotation.MDEntityInfo(beanFullclassName =  "nc.vo.dd.refreshcost.RefreshCostHVO" )
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("dd.RefreshCostHVO");
  	}
  	
}
