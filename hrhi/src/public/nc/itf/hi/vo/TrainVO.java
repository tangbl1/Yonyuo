/*      */ package nc.itf.hi.vo;
/*      */ 
/*      */ import nc.vo.hi.psndoc.PsnSuperVO;
import nc.vo.pub.lang.UFBoolean;
/*      */ import nc.vo.pub.lang.UFDouble;
/*      */ import nc.vo.pub.lang.UFLiteralDate;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class TrainVO
/*      */   extends PsnSuperVO
/*      */ {
/**
	 * 
	 */
	private static final long serialVersionUID = -1543680498130976286L;
/*      */   private String pk_psndoc;
/*      */   private String act_code;
/*      */   private String act_name;
/*      */   private String act_name2;
/*      */   private String act_name3;
/*      */   private String act_name4;
/*      */   private String act_name5;
/*      */   private String act_name6;
/*      */   private UFLiteralDate begindate;
/*      */   private UFLiteralDate enddate;
/*      */   private String tra_type;
/*      */   private String tra_mode;
/*      */   private String traim;
/*      */   private String tra_content;
/*      */   private String trm_location;
/*      */   private UFDouble tra_time;
/*      */   private String tra_result;
/*      */   private UFDouble tra_cost;
/*      */   private String contcode;
/*      */   private String trm_certif_code;
/*      */   private String trm_certif_name;
/*      */   private UFLiteralDate certificate_date;
/*      */   private String certificate_unit;
/*      */   private Integer source_type;
/*      */   private UFLiteralDate signon_dt;
/*      */   private Integer taketrm_method;
/*      */   private String entrepreneur;
/*      */   private String assist_unit;
/*      */   private String purveyor;
/*      */   private UFBoolean isallduty;
/*      */   private Integer absence_count;
/*      */   private UFDouble ass_result;
/*      */   private String ass_option;
/*      */   private String memo;
/*      */   private String pk_psndoc_sub;
/*      */   private String pk_psnorg;
/*      */   private String pk_org;
/*      */   private String pk_group;
/*      */   private Integer recordnum;
/*      */   private UFBoolean lastflag;
/*      */   private String pk_psnjob;
/*      */   private String trainorg;
/*      */   private String pk_act;
private String glbdef1;
private String glbdef2;
private String glbdef3;
private UFBoolean glbdef4;
private String glbdef5;
private String glbdef6;
private String glbdef7;
private String glbdef8;
private String glbdef9;
private String glbdef10;
private String glbdef11;
private String glbdef12;
private String glbdef13;
//private String glbdef14;
private String glbdef14;
private String glbdef15;
/*      */   private Integer activetype;
			 public static final String GLBDEF1 = "glbdef1";
			 public static final String GLBDEF2 = "glbdef2";
			 public static final String GLBDEF3 = "glbdef3";
			 public static final String GLBDEF4 = "glbdef4";
			 public static final String GLBDEF5 = "glbdef5";
			 public static final String GLBDEF6 = "glbdef6";
			 public static final String GLBDEF7 = "glbdef7";
			 public static final String GLBDEF8 = "glbdef8";
			 public static final String GLBDEF9 = "glbdef9";
			 public static final String GLBDEF10 = "glbdef10";
			 public static final String GLBDEF11 = "glbdef11";
			 public static final String GLBDEF12 = "glbdef12";
			 public static final String GLBDEF13 = "glbdef13";
			 public static final String GLBDEF14 = "glbdef14";
			 public static final String GLBDEF15 = "glbdef15";
/*      */   public static final String PK_PSNDOC = "pk_psndoc";
/*      */   public static final String ACT_CODE = "act_code";
/*      */   public static final String ACT_NAME = "act_name";
/*      */   public static final String ACT_NAME2 = "act_name2";
/*      */   public static final String ACT_NAME3 = "act_name3";
/*      */   public static final String ACT_NAME4 = "act_name4";
/*      */   public static final String ACT_NAME5 = "act_name5";
/*      */   public static final String ACT_NAME6 = "act_name6";
/*      */   public static final String BEGINDATE = "begindate";
/*      */   public static final String ENDDATE = "enddate";
/*      */   public static final String TRA_TYPE = "tra_type";
/*      */   public static final String TRA_MODE = "tra_mode";
/*      */   public static final String TRAIM = "traim";
/*      */   public static final String TRA_CONTENT = "tra_content";
/*      */   public static final String TRM_LOCATION = "trm_location";
/*      */   public static final String TRA_TIME = "tra_time";
/*      */   public static final String TRA_RESULT = "tra_result";
/*      */   public static final String TRA_COST = "tra_cost";
/*      */   public static final String CONTCODE = "contcode";
/*      */   public static final String TRM_CERTIF_CODE = "trm_certif_code";
/*      */   public static final String TRM_CERTIF_NAME = "trm_certif_name";
/*      */   public static final String CERTIFICATE_DATE = "certificate_date";
/*      */   public static final String CERTIFICATE_UNIT = "certificate_unit";
/*      */   public static final String SOURCE_TYPE = "source_type";
/*      */   public static final String SIGNON_DT = "signon_dt";
/*      */   public static final String TAKETRM_METHOD = "taketrm_method";
/*      */   public static final String ENTREPRENEUR = "entrepreneur";
/*      */   public static final String ASSIST_UNIT = "assist_unit";
/*      */   public static final String PURVEYOR = "purveyor";
/*      */   public static final String ISALLDUTY = "isallduty";
/*      */   public static final String ABSENCE_COUNT = "absence_count";
/*      */   public static final String ASS_RESULT = "ass_result";
/*      */   public static final String ASS_OPTION = "ass_option";
/*      */   public static final String MEMO = "memo";
/*      */   public static final String PK_PSNDOC_SUB = "pk_psndoc_sub";
/*      */   public static final String PK_PSNORG = "pk_psnorg";
/*      */   public static final String PK_ORG = "pk_org";
/*      */   public static final String PK_GROUP = "pk_group";
/*      */   public static final String RECORDNUM = "recordnum";
/*      */   public static final String LASTFLAG = "lastflag";
/*      */   public static final String PK_ACT = "pk_act";
/*      */   public static final String PK_PSNJOB = "pk_psnjob";
/*      */   public static final String TRAINORG = "trainorg";
/*      */   public static final String ACTIVETYPE = "activetype";
/*      */   
/*      */   public String getPk_psndoc()
/*      */   {
/*  117 */     return pk_psndoc;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPk_psndoc(String newPk_psndoc)
/*      */   {
/*  127 */     pk_psndoc = newPk_psndoc;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getAct_code()
/*      */   {
/*  137 */     return act_code;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAct_code(String newAct_code)
/*      */   {
/*  147 */     act_code = newAct_code;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getAct_name()
/*      */   {
/*  157 */     return act_name;
/*      */   }
public String getGlbdef1() {
	return glbdef1;
}
public void setGlbdef1(String glbdef1) {
	this.glbdef1 = glbdef1;
}
public String getGlbdef2() {
	return glbdef2;
}
public void setGlbdef2(String glbdef2) {
	this.glbdef2 = glbdef2;
}

public String getGlbdef3() {
	return glbdef3;
}
public void setGlbdef3(String glbdef3) {
	this.glbdef3 = glbdef3;
}

public UFBoolean getGlbdef4() {
	return glbdef4;
}
public void setGlbdef4(UFBoolean glbdef4) {
	this.glbdef4 = glbdef4;
}
public String getGlbdef5() {
	return glbdef5;
}
public void setGlbdef5(String glbdef5) {
	this.glbdef5 = glbdef5;
}
public String getGlbdef6() {
	return glbdef6;
}
public void setGlbdef6(String glbdef6) {
	this.glbdef6 = glbdef6;
}
public String getGlbdef7() {
	return glbdef7;
}
public void setGlbdef7(String glbdef7) {
	this.glbdef7 = glbdef7;
}
public String getGlbdef8() {
	return glbdef8;
}
public void setGlbdef8(String glbdef8) {
	this.glbdef8 = glbdef8;
}
public String getGlbdef9() {
	return glbdef9;
}
public void setGlbdef9(String glbdef9) {
	this.glbdef9 = glbdef9;
}
public String getGlbdef10() {
	return glbdef10;
}
public void setGlbdef10(String glbdef10) {
	this.glbdef10 = glbdef10;
}
public String getGlbdef11() {
	return glbdef11;
}
public void setGlbdef11(String glbdef11) {
	this.glbdef11 = glbdef11;
}
public String getGlbdef12() {
	return glbdef12;
}
public void setGlbdef12(String glbdef12) {
	this.glbdef12 = glbdef12;
}
public String getGlbdef13() {
	return glbdef13;
}
public void setGlbdef13(String glbdef13) {
	this.glbdef13 = glbdef13;
}

public String getGlbdef14() {
	return glbdef14;
}
public void setGlbdef14(String glbdef14) {
	this.glbdef14 = glbdef14;
}

public String getGlbdef15() {
	return glbdef15;
}
public void setGlbdef15(String glbdef15) {
	this.glbdef15 = glbdef15;
}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAct_name(String newAct_name)
/*      */   {
/*  167 */     act_name = newAct_name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getAct_name2()
/*      */   {
/*  177 */     return act_name2;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAct_name2(String newAct_name2)
/*      */   {
/*  187 */     act_name2 = newAct_name2;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getAct_name3()
/*      */   {
/*  197 */     return act_name3;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAct_name3(String newAct_name3)
/*      */   {
/*  207 */     act_name3 = newAct_name3;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getAct_name4()
/*      */   {
/*  217 */     return act_name4;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAct_name4(String newAct_name4)
/*      */   {
/*  227 */     act_name4 = newAct_name4;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getAct_name5()
/*      */   {
/*  237 */     return act_name5;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAct_name5(String newAct_name5)
/*      */   {
/*  247 */     act_name5 = newAct_name5;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getAct_name6()
/*      */   {
/*  257 */     return act_name6;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAct_name6(String newAct_name6)
/*      */   {
/*  267 */     act_name6 = newAct_name6;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UFLiteralDate getBegindate()
/*      */   {
/*  277 */     return begindate;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBegindate(UFLiteralDate newBegindate)
/*      */   {
/*  287 */     begindate = newBegindate;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UFLiteralDate getEnddate()
/*      */   {
/*  297 */     return enddate;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEnddate(UFLiteralDate newEnddate)
/*      */   {
/*  307 */     enddate = newEnddate;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getTra_type()
/*      */   {
/*  317 */     return tra_type;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTra_type(String newTra_type)
/*      */   {
/*  327 */     tra_type = newTra_type;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getTra_mode()
/*      */   {
/*  337 */     return tra_mode;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTra_mode(String newTra_mode)
/*      */   {
/*  347 */     tra_mode = newTra_mode;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getTraim()
/*      */   {
/*  357 */     return traim;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTraim(String newTraim)
/*      */   {
/*  367 */     traim = newTraim;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getTra_content()
/*      */   {
/*  377 */     return tra_content;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTra_content(String newTra_content)
/*      */   {
/*  387 */     tra_content = newTra_content;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getTrm_location()
/*      */   {
/*  397 */     return trm_location;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTrm_location(String newTrm_location)
/*      */   {
/*  407 */     trm_location = newTrm_location;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UFDouble getTra_time()
/*      */   {
/*  417 */     return tra_time;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTra_time(UFDouble newTra_time)
/*      */   {
/*  427 */     tra_time = newTra_time;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getTra_result()
/*      */   {
/*  437 */     return tra_result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTra_result(String newTra_result)
/*      */   {
/*  447 */     tra_result = newTra_result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UFDouble getTra_cost()
/*      */   {
/*  457 */     return tra_cost;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTra_cost(UFDouble newTra_cost)
/*      */   {
/*  467 */     tra_cost = newTra_cost;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getContcode()
/*      */   {
/*  477 */     return contcode;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setContcode(String newContcode)
/*      */   {
/*  487 */     contcode = newContcode;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getTrm_certif_code()
/*      */   {
/*  497 */     return trm_certif_code;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTrm_certif_code(String newTrm_certif_code)
/*      */   {
/*  507 */     trm_certif_code = newTrm_certif_code;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getTrm_certif_name()
/*      */   {
/*  517 */     return trm_certif_name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTrm_certif_name(String newTrm_certif_name)
/*      */   {
/*  527 */     trm_certif_name = newTrm_certif_name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UFLiteralDate getCertificate_date()
/*      */   {
/*  537 */     return certificate_date;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCertificate_date(UFLiteralDate newCertificate_date)
/*      */   {
/*  547 */     certificate_date = newCertificate_date;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCertificate_unit()
/*      */   {
/*  557 */     return certificate_unit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCertificate_unit(String newCertificate_unit)
/*      */   {
/*  567 */     certificate_unit = newCertificate_unit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Integer getSource_type()
/*      */   {
/*  577 */     return source_type;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSource_type(Integer newSource_type)
/*      */   {
/*  587 */     source_type = newSource_type;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UFLiteralDate getSignon_dt()
/*      */   {
/*  597 */     return signon_dt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSignon_dt(UFLiteralDate newSignon_dt)
/*      */   {
/*  607 */     signon_dt = newSignon_dt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Integer getTaketrm_method()
/*      */   {
/*  617 */     return taketrm_method;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTaketrm_method(Integer newTaketrm_method)
/*      */   {
/*  627 */     taketrm_method = newTaketrm_method;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getEntrepreneur()
/*      */   {
/*  637 */     return entrepreneur;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEntrepreneur(String newEntrepreneur)
/*      */   {
/*  647 */     entrepreneur = newEntrepreneur;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getAssist_unit()
/*      */   {
/*  657 */     return assist_unit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAssist_unit(String newAssist_unit)
/*      */   {
/*  667 */     assist_unit = newAssist_unit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPurveyor()
/*      */   {
/*  677 */     return purveyor;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPurveyor(String newPurveyor)
/*      */   {
/*  687 */     purveyor = newPurveyor;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UFBoolean getIsallduty()
/*      */   {
/*  697 */     return isallduty;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIsallduty(UFBoolean newIsallduty)
/*      */   {
/*  707 */     isallduty = newIsallduty;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Integer getAbsence_count()
/*      */   {
/*  717 */     return absence_count;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAbsence_count(Integer newAbsence_count)
/*      */   {
/*  727 */     absence_count = newAbsence_count;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UFDouble getAss_result()
/*      */   {
/*  737 */     return ass_result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAss_result(UFDouble newAss_result)
/*      */   {
/*  747 */     ass_result = newAss_result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getAss_option()
/*      */   {
/*  757 */     return ass_option;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAss_option(String newAss_option)
/*      */   {
/*  767 */     ass_option = newAss_option;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getMemo()
/*      */   {
/*  777 */     return memo;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMemo(String newMemo)
/*      */   {
/*  787 */     memo = newMemo;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPk_psndoc_sub()
/*      */   {
/*  797 */     return pk_psndoc_sub;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPk_psndoc_sub(String newPk_psndoc_sub)
/*      */   {
/*  807 */     pk_psndoc_sub = newPk_psndoc_sub;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPk_psnorg()
/*      */   {
/*  817 */     return pk_psnorg;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPk_psnorg(String newPk_psnorg)
/*      */   {
/*  827 */     pk_psnorg = newPk_psnorg;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPk_org()
/*      */   {
/*  837 */     return pk_org;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPk_org(String newPk_org)
/*      */   {
/*  847 */     pk_org = newPk_org;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPk_group()
/*      */   {
/*  857 */     return pk_group;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPk_group(String newPk_group)
/*      */   {
/*  867 */     pk_group = newPk_group;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Integer getRecordnum()
/*      */   {
/*  877 */     return recordnum;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRecordnum(Integer newRecordnum)
/*      */   {
/*  887 */     recordnum = newRecordnum;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UFBoolean getLastflag()
/*      */   {
/*  897 */     return lastflag;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLastflag(UFBoolean newLastflag)
/*      */   {
/*  907 */     lastflag = newLastflag;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPk_act()
/*      */   {
/*  917 */     return pk_act;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPk_act(String newPk_act)
/*      */   {
/*  927 */     pk_act = newPk_act;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getParentPKFieldName()
/*      */   {
/*  941 */     return "pk_psndoc";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPKFieldName()
/*      */   {
/*  955 */     return "pk_psndoc_sub";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getTableName()
/*      */   {
/*  969 */     return "hi_psndoc_train";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getDefaultTableName()
/*      */   {
/*  982 */     return "hi_psndoc_train";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPk_psnjob()
/*      */   {
/*  995 */     return pk_psnjob;
/*      */   }
/*      */   
/*      */   public void setPk_psnjob(String pk_psnjob)
/*      */   {
/* 1000 */     this.pk_psnjob = pk_psnjob;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getTrainorg()
/*      */   {
/* 1008 */     return trainorg;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTrainorg(String newTrainorg)
/*      */   {
/* 1016 */     trainorg = newTrainorg;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Integer getActivetype()
/*      */   {
/* 1024 */     return activetype;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setActivetype(Integer newActivetype)
/*      */   {
/* 1032 */     activetype = newActivetype;
/*      */   }
/*      */ }

/* Location:           D:\work\home\hwnc651206\hwnc65\modules\hrhi\classes
 * Qualified Name:     nc.itf.hi.vo.TrainVO
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */