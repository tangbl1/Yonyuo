package nc.vo.vehicle.insurance;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> 此处简要描述此类功能 </b>
 * <p>
 *   此处添加累的描述信息
 * </p>
 *  创建日期:2019-10-29
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class InsuranceBVO extends SuperVO {
	
/**
*主键
*/
public String pk_insurance_b;
/**
*保险期限
*/
public String ideadline;
/**
*保险公司
*/
public String icompany;
/**
*保险日期
*/
public UFDate idate;
/**
*到期日期
*/
public UFDate iexpiredate;
/**
*金额
*/
public UFDouble money;
/**
*险种
*/
public String itype;
/**
*行号
*/
public String rowno;
/**
*集团
*/
public String pk_group;
/**
*组织
*/
public String pk_org;
/**
*组织版本
*/
public String pk_org_v;
/**
*自定义项1
*/
public String def1;
/**
*自定义项2
*/
public String def2;
/**
*自定义项3
*/
public String def3;
/**
*自定义项4
*/
public String def4;
/**
*自定义项5
*/
public String def5;
/**
*自定义项6
*/
public String def6;
/**
*自定义项7
*/
public String def7;
/**
*自定义项8
*/
public String def8;
/**
*自定义项9
*/
public String def9;
/**
*自定义项10
*/
public String def10;
/**
*自定义项11
*/
public String def11;
/**
*自定义项12
*/
public String def12;
/**
*自定义项13
*/
public String def13;
/**
*自定义项14
*/
public String def14;
/**
*自定义项15
*/
public String def15;
/**
*自定义项16
*/
public String def16;
/**
*自定义项17
*/
public String def17;
/**
*自定义项18
*/
public String def18;
/**
*自定义项19
*/
public String def19;
/**
*自定义项20
*/
public String def20;
/**
*上层单据主键
*/
public String pk_insurance;
/**
*时间戳
*/
public UFDateTime ts;
    
    
/**
* 属性 pk_insurance_b的Getter方法.属性名：主键
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getPk_insurance_b() {
return this.pk_insurance_b;
} 

/**
* 属性pk_insurance_b的Setter方法.属性名：主键
* 创建日期:2019-10-29
* @param newPk_insurance_b java.lang.String
*/
public void setPk_insurance_b ( String pk_insurance_b) {
this.pk_insurance_b=pk_insurance_b;
} 
 
/**
* 属性 ideadline的Getter方法.属性名：保险期限
*  创建日期:2019-10-29
* @return nc.vo.vehicle.insurance.IdeadlineEnum
*/
public String getIdeadline() {
return this.ideadline;
} 

/**
* 属性ideadline的Setter方法.属性名：保险期限
* 创建日期:2019-10-29
* @param newIdeadline nc.vo.vehicle.insurance.IdeadlineEnum
*/
public void setIdeadline ( String ideadline) {
this.ideadline=ideadline;
} 
 
/**
* 属性 icompany的Getter方法.属性名：保险公司
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getIcompany() {
return this.icompany;
} 

/**
* 属性icompany的Setter方法.属性名：保险公司
* 创建日期:2019-10-29
* @param newIcompany java.lang.String
*/
public void setIcompany ( String icompany) {
this.icompany=icompany;
} 
 
/**
* 属性 idate的Getter方法.属性名：保险日期
*  创建日期:2019-10-29
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getIdate() {
return this.idate;
} 

/**
* 属性idate的Setter方法.属性名：保险日期
* 创建日期:2019-10-29
* @param newIdate nc.vo.pub.lang.UFDate
*/
public void setIdate ( UFDate idate) {
this.idate=idate;
} 
 
/**
* 属性 iexpiredate的Getter方法.属性名：到期日期
*  创建日期:2019-10-29
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getIexpiredate() {
return this.iexpiredate;
} 

/**
* 属性iexpiredate的Setter方法.属性名：到期日期
* 创建日期:2019-10-29
* @param newIexpiredate nc.vo.pub.lang.UFDate
*/
public void setIexpiredate ( UFDate iexpiredate) {
this.iexpiredate=iexpiredate;
} 
 
/**
* 属性 money的Getter方法.属性名：金额
*  创建日期:2019-10-29
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getMoney() {
return this.money;
} 

/**
* 属性money的Setter方法.属性名：金额
* 创建日期:2019-10-29
* @param newMoney nc.vo.pub.lang.UFDouble
*/
public void setMoney ( UFDouble money) {
this.money=money;
} 
 
/**
* 属性 itype的Getter方法.属性名：险种
*  创建日期:2019-10-29
* @return nc.vo.vehicle.insurance.ItypeEnum
*/
public String getItype() {
return this.itype;
} 

/**
* 属性itype的Setter方法.属性名：险种
* 创建日期:2019-10-29
* @param newItype nc.vo.vehicle.insurance.ItypeEnum
*/
public void setItype ( String itype) {
this.itype=itype;
} 
 
/**
* 属性 rowno的Getter方法.属性名：行号
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getRowno() {
return this.rowno;
} 

/**
* 属性rowno的Setter方法.属性名：行号
* 创建日期:2019-10-29
* @param newRowno java.lang.String
*/
public void setRowno ( String rowno) {
this.rowno=rowno;
} 
 
/**
* 属性 pk_group的Getter方法.属性名：集团
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getPk_group() {
return this.pk_group;
} 

/**
* 属性pk_group的Setter方法.属性名：集团
* 创建日期:2019-10-29
* @param newPk_group java.lang.String
*/
public void setPk_group ( String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* 属性 pk_org的Getter方法.属性名：组织
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getPk_org() {
return this.pk_org;
} 

/**
* 属性pk_org的Setter方法.属性名：组织
* 创建日期:2019-10-29
* @param newPk_org java.lang.String
*/
public void setPk_org ( String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* 属性 pk_org_v的Getter方法.属性名：组织版本
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getPk_org_v() {
return this.pk_org_v;
} 

/**
* 属性pk_org_v的Setter方法.属性名：组织版本
* 创建日期:2019-10-29
* @param newPk_org_v java.lang.String
*/
public void setPk_org_v ( String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* 属性 def1的Getter方法.属性名：自定义项1
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef1() {
return this.def1;
} 

/**
* 属性def1的Setter方法.属性名：自定义项1
* 创建日期:2019-10-29
* @param newDef1 java.lang.String
*/
public void setDef1 ( String def1) {
this.def1=def1;
} 
 
/**
* 属性 def2的Getter方法.属性名：自定义项2
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef2() {
return this.def2;
} 

/**
* 属性def2的Setter方法.属性名：自定义项2
* 创建日期:2019-10-29
* @param newDef2 java.lang.String
*/
public void setDef2 ( String def2) {
this.def2=def2;
} 
 
/**
* 属性 def3的Getter方法.属性名：自定义项3
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef3() {
return this.def3;
} 

/**
* 属性def3的Setter方法.属性名：自定义项3
* 创建日期:2019-10-29
* @param newDef3 java.lang.String
*/
public void setDef3 ( String def3) {
this.def3=def3;
} 
 
/**
* 属性 def4的Getter方法.属性名：自定义项4
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef4() {
return this.def4;
} 

/**
* 属性def4的Setter方法.属性名：自定义项4
* 创建日期:2019-10-29
* @param newDef4 java.lang.String
*/
public void setDef4 ( String def4) {
this.def4=def4;
} 
 
/**
* 属性 def5的Getter方法.属性名：自定义项5
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef5() {
return this.def5;
} 

/**
* 属性def5的Setter方法.属性名：自定义项5
* 创建日期:2019-10-29
* @param newDef5 java.lang.String
*/
public void setDef5 ( String def5) {
this.def5=def5;
} 
 
/**
* 属性 def6的Getter方法.属性名：自定义项6
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef6() {
return this.def6;
} 

/**
* 属性def6的Setter方法.属性名：自定义项6
* 创建日期:2019-10-29
* @param newDef6 java.lang.String
*/
public void setDef6 ( String def6) {
this.def6=def6;
} 
 
/**
* 属性 def7的Getter方法.属性名：自定义项7
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef7() {
return this.def7;
} 

/**
* 属性def7的Setter方法.属性名：自定义项7
* 创建日期:2019-10-29
* @param newDef7 java.lang.String
*/
public void setDef7 ( String def7) {
this.def7=def7;
} 
 
/**
* 属性 def8的Getter方法.属性名：自定义项8
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef8() {
return this.def8;
} 

/**
* 属性def8的Setter方法.属性名：自定义项8
* 创建日期:2019-10-29
* @param newDef8 java.lang.String
*/
public void setDef8 ( String def8) {
this.def8=def8;
} 
 
/**
* 属性 def9的Getter方法.属性名：自定义项9
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef9() {
return this.def9;
} 

/**
* 属性def9的Setter方法.属性名：自定义项9
* 创建日期:2019-10-29
* @param newDef9 java.lang.String
*/
public void setDef9 ( String def9) {
this.def9=def9;
} 
 
/**
* 属性 def10的Getter方法.属性名：自定义项10
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef10() {
return this.def10;
} 

/**
* 属性def10的Setter方法.属性名：自定义项10
* 创建日期:2019-10-29
* @param newDef10 java.lang.String
*/
public void setDef10 ( String def10) {
this.def10=def10;
} 
 
/**
* 属性 def11的Getter方法.属性名：自定义项11
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef11() {
return this.def11;
} 

/**
* 属性def11的Setter方法.属性名：自定义项11
* 创建日期:2019-10-29
* @param newDef11 java.lang.String
*/
public void setDef11 ( String def11) {
this.def11=def11;
} 
 
/**
* 属性 def12的Getter方法.属性名：自定义项12
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef12() {
return this.def12;
} 

/**
* 属性def12的Setter方法.属性名：自定义项12
* 创建日期:2019-10-29
* @param newDef12 java.lang.String
*/
public void setDef12 ( String def12) {
this.def12=def12;
} 
 
/**
* 属性 def13的Getter方法.属性名：自定义项13
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef13() {
return this.def13;
} 

/**
* 属性def13的Setter方法.属性名：自定义项13
* 创建日期:2019-10-29
* @param newDef13 java.lang.String
*/
public void setDef13 ( String def13) {
this.def13=def13;
} 
 
/**
* 属性 def14的Getter方法.属性名：自定义项14
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef14() {
return this.def14;
} 

/**
* 属性def14的Setter方法.属性名：自定义项14
* 创建日期:2019-10-29
* @param newDef14 java.lang.String
*/
public void setDef14 ( String def14) {
this.def14=def14;
} 
 
/**
* 属性 def15的Getter方法.属性名：自定义项15
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef15() {
return this.def15;
} 

/**
* 属性def15的Setter方法.属性名：自定义项15
* 创建日期:2019-10-29
* @param newDef15 java.lang.String
*/
public void setDef15 ( String def15) {
this.def15=def15;
} 
 
/**
* 属性 def16的Getter方法.属性名：自定义项16
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef16() {
return this.def16;
} 

/**
* 属性def16的Setter方法.属性名：自定义项16
* 创建日期:2019-10-29
* @param newDef16 java.lang.String
*/
public void setDef16 ( String def16) {
this.def16=def16;
} 
 
/**
* 属性 def17的Getter方法.属性名：自定义项17
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef17() {
return this.def17;
} 

/**
* 属性def17的Setter方法.属性名：自定义项17
* 创建日期:2019-10-29
* @param newDef17 java.lang.String
*/
public void setDef17 ( String def17) {
this.def17=def17;
} 
 
/**
* 属性 def18的Getter方法.属性名：自定义项18
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef18() {
return this.def18;
} 

/**
* 属性def18的Setter方法.属性名：自定义项18
* 创建日期:2019-10-29
* @param newDef18 java.lang.String
*/
public void setDef18 ( String def18) {
this.def18=def18;
} 
 
/**
* 属性 def19的Getter方法.属性名：自定义项19
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef19() {
return this.def19;
} 

/**
* 属性def19的Setter方法.属性名：自定义项19
* 创建日期:2019-10-29
* @param newDef19 java.lang.String
*/
public void setDef19 ( String def19) {
this.def19=def19;
} 
 
/**
* 属性 def20的Getter方法.属性名：自定义项20
*  创建日期:2019-10-29
* @return java.lang.String
*/
public String getDef20() {
return this.def20;
} 

/**
* 属性def20的Setter方法.属性名：自定义项20
* 创建日期:2019-10-29
* @param newDef20 java.lang.String
*/
public void setDef20 ( String def20) {
this.def20=def20;
} 
 
/**
* 属性 生成上层主键的Getter方法.属性名：上层主键
*  创建日期:2019-10-29
* @return String
*/
public String getPk_insurance(){
return this.pk_insurance;
}
/**
* 属性生成上层主键的Setter方法.属性名：上层主键
* 创建日期:2019-10-29
* @param newPk_insurance String
*/
public void setPk_insurance(String pk_insurance){
this.pk_insurance=pk_insurance;
} 
/**
* 属性 生成时间戳的Getter方法.属性名：时间戳
*  创建日期:2019-10-29
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* 属性生成时间戳的Setter方法.属性名：时间戳
* 创建日期:2019-10-29
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("vehicle.cl_insurance_b");
    }
   }
    