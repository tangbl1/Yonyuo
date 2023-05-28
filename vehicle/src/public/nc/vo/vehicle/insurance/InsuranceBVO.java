package nc.vo.vehicle.insurance;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> �˴���Ҫ�������๦�� </b>
 * <p>
 *   �˴�����۵�������Ϣ
 * </p>
 *  ��������:2019-10-29
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class InsuranceBVO extends SuperVO {
	
/**
*����
*/
public String pk_insurance_b;
/**
*��������
*/
public String ideadline;
/**
*���չ�˾
*/
public String icompany;
/**
*��������
*/
public UFDate idate;
/**
*��������
*/
public UFDate iexpiredate;
/**
*���
*/
public UFDouble money;
/**
*����
*/
public String itype;
/**
*�к�
*/
public String rowno;
/**
*����
*/
public String pk_group;
/**
*��֯
*/
public String pk_org;
/**
*��֯�汾
*/
public String pk_org_v;
/**
*�Զ�����1
*/
public String def1;
/**
*�Զ�����2
*/
public String def2;
/**
*�Զ�����3
*/
public String def3;
/**
*�Զ�����4
*/
public String def4;
/**
*�Զ�����5
*/
public String def5;
/**
*�Զ�����6
*/
public String def6;
/**
*�Զ�����7
*/
public String def7;
/**
*�Զ�����8
*/
public String def8;
/**
*�Զ�����9
*/
public String def9;
/**
*�Զ�����10
*/
public String def10;
/**
*�Զ�����11
*/
public String def11;
/**
*�Զ�����12
*/
public String def12;
/**
*�Զ�����13
*/
public String def13;
/**
*�Զ�����14
*/
public String def14;
/**
*�Զ�����15
*/
public String def15;
/**
*�Զ�����16
*/
public String def16;
/**
*�Զ�����17
*/
public String def17;
/**
*�Զ�����18
*/
public String def18;
/**
*�Զ�����19
*/
public String def19;
/**
*�Զ�����20
*/
public String def20;
/**
*�ϲ㵥������
*/
public String pk_insurance;
/**
*ʱ���
*/
public UFDateTime ts;
    
    
/**
* ���� pk_insurance_b��Getter����.������������
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getPk_insurance_b() {
return this.pk_insurance_b;
} 

/**
* ����pk_insurance_b��Setter����.������������
* ��������:2019-10-29
* @param newPk_insurance_b java.lang.String
*/
public void setPk_insurance_b ( String pk_insurance_b) {
this.pk_insurance_b=pk_insurance_b;
} 
 
/**
* ���� ideadline��Getter����.����������������
*  ��������:2019-10-29
* @return nc.vo.vehicle.insurance.IdeadlineEnum
*/
public String getIdeadline() {
return this.ideadline;
} 

/**
* ����ideadline��Setter����.����������������
* ��������:2019-10-29
* @param newIdeadline nc.vo.vehicle.insurance.IdeadlineEnum
*/
public void setIdeadline ( String ideadline) {
this.ideadline=ideadline;
} 
 
/**
* ���� icompany��Getter����.�����������չ�˾
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getIcompany() {
return this.icompany;
} 

/**
* ����icompany��Setter����.�����������չ�˾
* ��������:2019-10-29
* @param newIcompany java.lang.String
*/
public void setIcompany ( String icompany) {
this.icompany=icompany;
} 
 
/**
* ���� idate��Getter����.����������������
*  ��������:2019-10-29
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getIdate() {
return this.idate;
} 

/**
* ����idate��Setter����.����������������
* ��������:2019-10-29
* @param newIdate nc.vo.pub.lang.UFDate
*/
public void setIdate ( UFDate idate) {
this.idate=idate;
} 
 
/**
* ���� iexpiredate��Getter����.����������������
*  ��������:2019-10-29
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getIexpiredate() {
return this.iexpiredate;
} 

/**
* ����iexpiredate��Setter����.����������������
* ��������:2019-10-29
* @param newIexpiredate nc.vo.pub.lang.UFDate
*/
public void setIexpiredate ( UFDate iexpiredate) {
this.iexpiredate=iexpiredate;
} 
 
/**
* ���� money��Getter����.�����������
*  ��������:2019-10-29
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getMoney() {
return this.money;
} 

/**
* ����money��Setter����.�����������
* ��������:2019-10-29
* @param newMoney nc.vo.pub.lang.UFDouble
*/
public void setMoney ( UFDouble money) {
this.money=money;
} 
 
/**
* ���� itype��Getter����.������������
*  ��������:2019-10-29
* @return nc.vo.vehicle.insurance.ItypeEnum
*/
public String getItype() {
return this.itype;
} 

/**
* ����itype��Setter����.������������
* ��������:2019-10-29
* @param newItype nc.vo.vehicle.insurance.ItypeEnum
*/
public void setItype ( String itype) {
this.itype=itype;
} 
 
/**
* ���� rowno��Getter����.���������к�
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getRowno() {
return this.rowno;
} 

/**
* ����rowno��Setter����.���������к�
* ��������:2019-10-29
* @param newRowno java.lang.String
*/
public void setRowno ( String rowno) {
this.rowno=rowno;
} 
 
/**
* ���� pk_group��Getter����.������������
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getPk_group() {
return this.pk_group;
} 

/**
* ����pk_group��Setter����.������������
* ��������:2019-10-29
* @param newPk_group java.lang.String
*/
public void setPk_group ( String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* ���� pk_org��Getter����.����������֯
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getPk_org() {
return this.pk_org;
} 

/**
* ����pk_org��Setter����.����������֯
* ��������:2019-10-29
* @param newPk_org java.lang.String
*/
public void setPk_org ( String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* ���� pk_org_v��Getter����.����������֯�汾
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getPk_org_v() {
return this.pk_org_v;
} 

/**
* ����pk_org_v��Setter����.����������֯�汾
* ��������:2019-10-29
* @param newPk_org_v java.lang.String
*/
public void setPk_org_v ( String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* ���� def1��Getter����.���������Զ�����1
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef1() {
return this.def1;
} 

/**
* ����def1��Setter����.���������Զ�����1
* ��������:2019-10-29
* @param newDef1 java.lang.String
*/
public void setDef1 ( String def1) {
this.def1=def1;
} 
 
/**
* ���� def2��Getter����.���������Զ�����2
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef2() {
return this.def2;
} 

/**
* ����def2��Setter����.���������Զ�����2
* ��������:2019-10-29
* @param newDef2 java.lang.String
*/
public void setDef2 ( String def2) {
this.def2=def2;
} 
 
/**
* ���� def3��Getter����.���������Զ�����3
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef3() {
return this.def3;
} 

/**
* ����def3��Setter����.���������Զ�����3
* ��������:2019-10-29
* @param newDef3 java.lang.String
*/
public void setDef3 ( String def3) {
this.def3=def3;
} 
 
/**
* ���� def4��Getter����.���������Զ�����4
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef4() {
return this.def4;
} 

/**
* ����def4��Setter����.���������Զ�����4
* ��������:2019-10-29
* @param newDef4 java.lang.String
*/
public void setDef4 ( String def4) {
this.def4=def4;
} 
 
/**
* ���� def5��Getter����.���������Զ�����5
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef5() {
return this.def5;
} 

/**
* ����def5��Setter����.���������Զ�����5
* ��������:2019-10-29
* @param newDef5 java.lang.String
*/
public void setDef5 ( String def5) {
this.def5=def5;
} 
 
/**
* ���� def6��Getter����.���������Զ�����6
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef6() {
return this.def6;
} 

/**
* ����def6��Setter����.���������Զ�����6
* ��������:2019-10-29
* @param newDef6 java.lang.String
*/
public void setDef6 ( String def6) {
this.def6=def6;
} 
 
/**
* ���� def7��Getter����.���������Զ�����7
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef7() {
return this.def7;
} 

/**
* ����def7��Setter����.���������Զ�����7
* ��������:2019-10-29
* @param newDef7 java.lang.String
*/
public void setDef7 ( String def7) {
this.def7=def7;
} 
 
/**
* ���� def8��Getter����.���������Զ�����8
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef8() {
return this.def8;
} 

/**
* ����def8��Setter����.���������Զ�����8
* ��������:2019-10-29
* @param newDef8 java.lang.String
*/
public void setDef8 ( String def8) {
this.def8=def8;
} 
 
/**
* ���� def9��Getter����.���������Զ�����9
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef9() {
return this.def9;
} 

/**
* ����def9��Setter����.���������Զ�����9
* ��������:2019-10-29
* @param newDef9 java.lang.String
*/
public void setDef9 ( String def9) {
this.def9=def9;
} 
 
/**
* ���� def10��Getter����.���������Զ�����10
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef10() {
return this.def10;
} 

/**
* ����def10��Setter����.���������Զ�����10
* ��������:2019-10-29
* @param newDef10 java.lang.String
*/
public void setDef10 ( String def10) {
this.def10=def10;
} 
 
/**
* ���� def11��Getter����.���������Զ�����11
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef11() {
return this.def11;
} 

/**
* ����def11��Setter����.���������Զ�����11
* ��������:2019-10-29
* @param newDef11 java.lang.String
*/
public void setDef11 ( String def11) {
this.def11=def11;
} 
 
/**
* ���� def12��Getter����.���������Զ�����12
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef12() {
return this.def12;
} 

/**
* ����def12��Setter����.���������Զ�����12
* ��������:2019-10-29
* @param newDef12 java.lang.String
*/
public void setDef12 ( String def12) {
this.def12=def12;
} 
 
/**
* ���� def13��Getter����.���������Զ�����13
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef13() {
return this.def13;
} 

/**
* ����def13��Setter����.���������Զ�����13
* ��������:2019-10-29
* @param newDef13 java.lang.String
*/
public void setDef13 ( String def13) {
this.def13=def13;
} 
 
/**
* ���� def14��Getter����.���������Զ�����14
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef14() {
return this.def14;
} 

/**
* ����def14��Setter����.���������Զ�����14
* ��������:2019-10-29
* @param newDef14 java.lang.String
*/
public void setDef14 ( String def14) {
this.def14=def14;
} 
 
/**
* ���� def15��Getter����.���������Զ�����15
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef15() {
return this.def15;
} 

/**
* ����def15��Setter����.���������Զ�����15
* ��������:2019-10-29
* @param newDef15 java.lang.String
*/
public void setDef15 ( String def15) {
this.def15=def15;
} 
 
/**
* ���� def16��Getter����.���������Զ�����16
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef16() {
return this.def16;
} 

/**
* ����def16��Setter����.���������Զ�����16
* ��������:2019-10-29
* @param newDef16 java.lang.String
*/
public void setDef16 ( String def16) {
this.def16=def16;
} 
 
/**
* ���� def17��Getter����.���������Զ�����17
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef17() {
return this.def17;
} 

/**
* ����def17��Setter����.���������Զ�����17
* ��������:2019-10-29
* @param newDef17 java.lang.String
*/
public void setDef17 ( String def17) {
this.def17=def17;
} 
 
/**
* ���� def18��Getter����.���������Զ�����18
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef18() {
return this.def18;
} 

/**
* ����def18��Setter����.���������Զ�����18
* ��������:2019-10-29
* @param newDef18 java.lang.String
*/
public void setDef18 ( String def18) {
this.def18=def18;
} 
 
/**
* ���� def19��Getter����.���������Զ�����19
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef19() {
return this.def19;
} 

/**
* ����def19��Setter����.���������Զ�����19
* ��������:2019-10-29
* @param newDef19 java.lang.String
*/
public void setDef19 ( String def19) {
this.def19=def19;
} 
 
/**
* ���� def20��Getter����.���������Զ�����20
*  ��������:2019-10-29
* @return java.lang.String
*/
public String getDef20() {
return this.def20;
} 

/**
* ����def20��Setter����.���������Զ�����20
* ��������:2019-10-29
* @param newDef20 java.lang.String
*/
public void setDef20 ( String def20) {
this.def20=def20;
} 
 
/**
* ���� �����ϲ�������Getter����.���������ϲ�����
*  ��������:2019-10-29
* @return String
*/
public String getPk_insurance(){
return this.pk_insurance;
}
/**
* ���������ϲ�������Setter����.���������ϲ�����
* ��������:2019-10-29
* @param newPk_insurance String
*/
public void setPk_insurance(String pk_insurance){
this.pk_insurance=pk_insurance;
} 
/**
* ���� ����ʱ�����Getter����.��������ʱ���
*  ��������:2019-10-29
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* ��������ʱ�����Setter����.��������ʱ���
* ��������:2019-10-29
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
    