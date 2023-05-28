package nc.vo.vehicle.drivermileage;

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
 *  ��������:2019-12-12
 * @author yonyouBQ
 * @version NCPrj ??
 */
 
public class DriverMileageHVO extends SuperVO {
	
/**
*����
*/
public String pk_drivermile;
/**
*���ݺ�
*/
public String billno;
/**
*���ƺ�
*/
public String vehicleno;
/**
*������ʼ������
*/
public UFDouble beginmile;
/**
*���ܽ�ֹ������
*/
public UFDouble endmile;
/**
*������ʻ���
*/
public UFDouble mile;
/**
*���ܼ���
*/
public UFDouble gas;
/**
*���ܼ��ͽ��
*/
public UFDouble gasmoney;
/**
*���ܰٹ����ͺ�
*/
public String gascost;
/**
*�����������
*/
public UFDouble othermile;
/**
*�����������
*/
public UFDouble othermoney;
/**
*��̱���
*/
public UFDouble odometermoney;
/**
*�ϼ�
*/
public UFDouble sum;
/**
*���ܿ���ʻ������
*/
public UFDouble nullmile;
/**
*������ʻ��Ч���
*/
public UFDouble effectivemile;
/**
*��Ч����
*/
public UFDouble meritpay;
/**
*��Ч����
*/
public UFDouble meritother;
/**
*��Ч�ϼ�
*/
public UFDouble meritsum;
/**
*������ĩ�Ӱ����
*/
public UFDouble overtimecount;
/**
*��ĩ��ѡ���
*/
public UFDouble weekendmoney;
/**
*���ܳ�ʱСʱ��
*/
public UFDouble overhourcount;
/**
*��ʱ�Ӱ���
*/
public UFDouble overhourmoney;
/**
*������ĩֵ�����
*/
public UFDouble ondutycount;
/**
*��ĩֵ����
*/
public UFDouble ondutymoney;
/**
*����ҹ�����
*/
public UFDouble nightcount;
/**
*ҹ����
*/
public UFDouble nightmoney;
/**
*��֯
*/
public String pk_org;
/**
*��֯�汾
*/
public String pk_org_v;
/**
*����
*/
public String pk_group;
/**
*����ID
*/
public String billid;
/**
*������֯
*/
public String pkorg;
/**
*ҵ������
*/
public String busitype;
/**
*�Ƶ���
*/
public String billmaker;
/**
*������
*/
public String approver;
/**
*����״̬
*/
public Integer approvestatus;
/**
*��������
*/
public String approvenote;
/**
*����ʱ��
*/
public UFDateTime approvedate;
/**
*��������
*/
public String transtype;
/**
*��������
*/
public String billtype;
/**
*��������pk
*/
public String transtypepk;
/**
*��Դ��������
*/
public String srcbilltype;
/**
*��Դ����id
*/
public String srcbillid;
/**
*�޶�ö��
*/
public Integer emendenum;
/**
*���ݰ汾pk
*/
public String billversionpk;
/**
*������
*/
public String creator;
/**
*�Ƶ�ʱ��
*/
public UFDateTime maketime;
/**
*����޸�ʱ��
*/
public UFDateTime lastmaketime;
/**
*��������
*/
public UFDate billdate;
/**
*����ʱ��
*/
public String creationtime;
/**
*�޸���
*/
public String modifier;
/**
*�޸�ʱ��
*/
public UFDateTime modifiedtime;
/**
*�к�
*/
public String rowno;
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
*ʱ���
*/
public UFDateTime ts;
    
    
/**
* ���� pk_drivermile��Getter����.������������
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getPk_drivermile() {
return this.pk_drivermile;
} 

/**
* ����pk_drivermile��Setter����.������������
* ��������:2019-12-12
* @param newPk_drivermile java.lang.String
*/
public void setPk_drivermile ( String pk_drivermile) {
this.pk_drivermile=pk_drivermile;
} 
 
/**
* ���� billno��Getter����.�����������ݺ�
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getBillno() {
return this.billno;
} 

/**
* ����billno��Setter����.�����������ݺ�
* ��������:2019-12-12
* @param newBillno java.lang.String
*/
public void setBillno ( String billno) {
this.billno=billno;
} 
 
/**
* ���� vehicleno��Getter����.�����������ƺ�
*  ��������:2019-12-12
* @return nc.vo.vehicle.VehicleMessageVO
*/
public String getVehicleno() {
return this.vehicleno;
} 

/**
* ����vehicleno��Setter����.�����������ƺ�
* ��������:2019-12-12
* @param newVehicleno nc.vo.vehicle.VehicleMessageVO
*/
public void setVehicleno ( String vehicleno) {
this.vehicleno=vehicleno;
} 
 
/**
* ���� beginmile��Getter����.��������������ʼ������
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getBeginmile() {
return this.beginmile;
} 

/**
* ����beginmile��Setter����.��������������ʼ������
* ��������:2019-12-12
* @param newBeginmile nc.vo.pub.lang.UFDouble
*/
public void setBeginmile ( UFDouble beginmile) {
this.beginmile=beginmile;
} 
 
/**
* ���� endmile��Getter����.�����������ܽ�ֹ������
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getEndmile() {
return this.endmile;
} 

/**
* ����endmile��Setter����.�����������ܽ�ֹ������
* ��������:2019-12-12
* @param newEndmile nc.vo.pub.lang.UFDouble
*/
public void setEndmile ( UFDouble endmile) {
this.endmile=endmile;
} 
 
/**
* ���� mile��Getter����.��������������ʻ���
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getMile() {
return this.mile;
} 

/**
* ����mile��Setter����.��������������ʻ���
* ��������:2019-12-12
* @param newMile nc.vo.pub.lang.UFDouble
*/
public void setMile ( UFDouble mile) {
this.mile=mile;
} 
 
/**
* ���� gas��Getter����.�����������ܼ���
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getGas() {
return this.gas;
} 

/**
* ����gas��Setter����.�����������ܼ���
* ��������:2019-12-12
* @param newGas nc.vo.pub.lang.UFDouble
*/
public void setGas ( UFDouble gas) {
this.gas=gas;
} 
 
/**
* ���� gasmoney��Getter����.�����������ܼ��ͽ��
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getGasmoney() {
return this.gasmoney;
} 

/**
* ����gasmoney��Setter����.�����������ܼ��ͽ��
* ��������:2019-12-12
* @param newGasmoney nc.vo.pub.lang.UFDouble
*/
public void setGasmoney ( UFDouble gasmoney) {
this.gasmoney=gasmoney;
} 
 
/**
* ���� gascost��Getter����.�����������ܰٹ����ͺ�
*  ��������:2019-12-12
* @return java.lang.UFDouble
*/
public String getGascost() {
return this.gascost;
} 

/**
* ����gascost��Setter����.�����������ܰٹ����ͺ�
* ��������:2019-12-12
* @param newGascost java.lang.UFDouble
*/
public void setGascost ( String gascost) {
this.gascost=gascost;
} 
 
/**
* ���� othermile��Getter����.�������������������
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getOthermile() {
return this.othermile;
} 

/**
* ����othermile��Setter����.�������������������
* ��������:2019-12-12
* @param newOthermile nc.vo.pub.lang.UFDouble
*/
public void setOthermile ( UFDouble othermile) {
this.othermile=othermile;
} 
 
/**
* ���� othermoney��Getter����.�������������������
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getOthermoney() {
return this.othermoney;
} 

/**
* ����othermoney��Setter����.�������������������
* ��������:2019-12-12
* @param newOthermoney nc.vo.pub.lang.UFDouble
*/
public void setOthermoney ( UFDouble othermoney) {
this.othermoney=othermoney;
} 
 
/**
* ���� odometermoney��Getter����.����������̱���
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getOdometermoney() {
return this.odometermoney;
} 

/**
* ����odometermoney��Setter����.����������̱���
* ��������:2019-12-12
* @param newOdometermoney nc.vo.pub.lang.UFDouble
*/
public void setOdometermoney ( UFDouble odometermoney) {
this.odometermoney=odometermoney;
} 
 
/**
* ���� sum��Getter����.���������ϼ�
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getSum() {
return this.sum;
} 

/**
* ����sum��Setter����.���������ϼ�
* ��������:2019-12-12
* @param newSum nc.vo.pub.lang.UFDouble
*/
public void setSum ( UFDouble sum) {
this.sum=sum;
} 
 
/**
* ���� nullmile��Getter����.�����������ܿ���ʻ������
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getNullmile() {
return this.nullmile;
} 

/**
* ����nullmile��Setter����.�����������ܿ���ʻ������
* ��������:2019-12-12
* @param newNullmile nc.vo.pub.lang.UFDouble
*/
public void setNullmile ( UFDouble nullmile) {
this.nullmile=nullmile;
} 
 
/**
* ���� effectivemile��Getter����.��������������ʻ��Ч���
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getEffectivemile() {
return this.effectivemile;
} 

/**
* ����effectivemile��Setter����.��������������ʻ��Ч���
* ��������:2019-12-12
* @param newEffectivemile nc.vo.pub.lang.UFDouble
*/
public void setEffectivemile ( UFDouble effectivemile) {
this.effectivemile=effectivemile;
} 
 
/**
* ���� meritpay��Getter����.����������Ч����
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getMeritpay() {
return this.meritpay;
} 

/**
* ����meritpay��Setter����.����������Ч����
* ��������:2019-12-12
* @param newMeritpay nc.vo.pub.lang.UFDouble
*/
public void setMeritpay ( UFDouble meritpay) {
this.meritpay=meritpay;
} 
 
/**
* ���� meritother��Getter����.����������Ч����
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getMeritother() {
return this.meritother;
} 

/**
* ����meritother��Setter����.����������Ч����
* ��������:2019-12-12
* @param newMeritother nc.vo.pub.lang.UFDouble
*/
public void setMeritother ( UFDouble meritother) {
this.meritother=meritother;
} 
 
/**
* ���� meritsum��Getter����.����������Ч�ϼ�
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getMeritsum() {
return this.meritsum;
} 

/**
* ����meritsum��Setter����.����������Ч�ϼ�
* ��������:2019-12-12
* @param newMeritsum nc.vo.pub.lang.UFDouble
*/
public void setMeritsum ( UFDouble meritsum) {
this.meritsum=meritsum;
} 
 
/**
* ���� overtimecount��Getter����.��������������ĩ�Ӱ����
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getOvertimecount() {
return this.overtimecount;
} 

/**
* ����overtimecount��Setter����.��������������ĩ�Ӱ����
* ��������:2019-12-12
* @param newOvertimecount nc.vo.pub.lang.UFDouble
*/
public void setOvertimecount ( UFDouble overtimecount) {
this.overtimecount=overtimecount;
} 
 
/**
* ���� weekendmoney��Getter����.����������ĩ��ѡ���
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getWeekendmoney() {
return this.weekendmoney;
} 

/**
* ����weekendmoney��Setter����.����������ĩ��ѡ���
* ��������:2019-12-12
* @param newWeekendmoney nc.vo.pub.lang.UFDouble
*/
public void setWeekendmoney ( UFDouble weekendmoney) {
this.weekendmoney=weekendmoney;
} 
 
/**
* ���� overhourcount��Getter����.�����������ܳ�ʱСʱ��
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getOverhourcount() {
return this.overhourcount;
} 

/**
* ����overhourcount��Setter����.�����������ܳ�ʱСʱ��
* ��������:2019-12-12
* @param newOverhourcount nc.vo.pub.lang.UFDouble
*/
public void setOverhourcount ( UFDouble overhourcount) {
this.overhourcount=overhourcount;
} 
 
/**
* ���� overhourmoney��Getter����.����������ʱ�Ӱ���
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getOverhourmoney() {
return this.overhourmoney;
} 

/**
* ����overhourmoney��Setter����.����������ʱ�Ӱ���
* ��������:2019-12-12
* @param newOverhourmoney nc.vo.pub.lang.UFDouble
*/
public void setOverhourmoney ( UFDouble overhourmoney) {
this.overhourmoney=overhourmoney;
} 
 
/**
* ���� ondutycount��Getter����.��������������ĩֵ�����
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getOndutycount() {
return this.ondutycount;
} 

/**
* ����ondutycount��Setter����.��������������ĩֵ�����
* ��������:2019-12-12
* @param newOndutycount nc.vo.pub.lang.UFDouble
*/
public void setOndutycount ( UFDouble ondutycount) {
this.ondutycount=ondutycount;
} 
 
/**
* ���� ondutymoney��Getter����.����������ĩֵ����
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getOndutymoney() {
return this.ondutymoney;
} 

/**
* ����ondutymoney��Setter����.����������ĩֵ����
* ��������:2019-12-12
* @param newOndutymoney nc.vo.pub.lang.UFDouble
*/
public void setOndutymoney ( UFDouble ondutymoney) {
this.ondutymoney=ondutymoney;
} 
 
/**
* ���� nightcount��Getter����.������������ҹ�����
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getNightcount() {
return this.nightcount;
} 

/**
* ����nightcount��Setter����.������������ҹ�����
* ��������:2019-12-12
* @param newNightcount nc.vo.pub.lang.UFDouble
*/
public void setNightcount ( UFDouble nightcount) {
this.nightcount=nightcount;
} 
 
/**
* ���� nightmoney��Getter����.��������ҹ����
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getNightmoney() {
return this.nightmoney;
} 

/**
* ����nightmoney��Setter����.��������ҹ����
* ��������:2019-12-12
* @param newNightmoney nc.vo.pub.lang.UFDouble
*/
public void setNightmoney ( UFDouble nightmoney) {
this.nightmoney=nightmoney;
} 
 
/**
* ���� pk_org��Getter����.����������֯
*  ��������:2019-12-12
* @return nc.vo.org.OrgVO
*/
public String getPk_org() {
return this.pk_org;
} 

/**
* ����pk_org��Setter����.����������֯
* ��������:2019-12-12
* @param newPk_org nc.vo.org.OrgVO
*/
public void setPk_org ( String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* ���� pk_org_v��Getter����.����������֯�汾
*  ��������:2019-12-12
* @return nc.vo.vorg.OrgVersionVO
*/
public String getPk_org_v() {
return this.pk_org_v;
} 

/**
* ����pk_org_v��Setter����.����������֯�汾
* ��������:2019-12-12
* @param newPk_org_v nc.vo.vorg.OrgVersionVO
*/
public void setPk_org_v ( String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* ���� pk_group��Getter����.������������
*  ��������:2019-12-12
* @return nc.vo.org.GroupVO
*/
public String getPk_group() {
return this.pk_group;
} 

/**
* ����pk_group��Setter����.������������
* ��������:2019-12-12
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* ���� billid��Getter����.������������ID
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getBillid() {
return this.billid;
} 

/**
* ����billid��Setter����.������������ID
* ��������:2019-12-12
* @param newBillid java.lang.String
*/
public void setBillid ( String billid) {
this.billid=billid;
} 
 
/**
* ���� pkorg��Getter����.��������������֯
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getPkorg() {
return this.pkorg;
} 

/**
* ����pkorg��Setter����.��������������֯
* ��������:2019-12-12
* @param newPkorg java.lang.String
*/
public void setPkorg ( String pkorg) {
this.pkorg=pkorg;
} 
 
/**
* ���� busitype��Getter����.��������ҵ������
*  ��������:2019-12-12
* @return nc.vo.pf.pub.BusitypeVO
*/
public String getBusitype() {
return this.busitype;
} 

/**
* ����busitype��Setter����.��������ҵ������
* ��������:2019-12-12
* @param newBusitype nc.vo.pf.pub.BusitypeVO
*/
public void setBusitype ( String busitype) {
this.busitype=busitype;
} 
 
/**
* ���� billmaker��Getter����.���������Ƶ���
*  ��������:2019-12-12
* @return nc.vo.sm.UserVO
*/
public String getBillmaker() {
return this.billmaker;
} 

/**
* ����billmaker��Setter����.���������Ƶ���
* ��������:2019-12-12
* @param newBillmaker nc.vo.sm.UserVO
*/
public void setBillmaker ( String billmaker) {
this.billmaker=billmaker;
} 
 
/**
* ���� approver��Getter����.��������������
*  ��������:2019-12-12
* @return nc.vo.sm.UserVO
*/
public String getApprover() {
return this.approver;
} 

/**
* ����approver��Setter����.��������������
* ��������:2019-12-12
* @param newApprover nc.vo.sm.UserVO
*/
public void setApprover ( String approver) {
this.approver=approver;
} 
 
/**
* ���� approvestatus��Getter����.������������״̬
*  ��������:2019-12-12
* @return nc.vo.pub.pf.BillStatusEnum
*/
public Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* ����approvestatus��Setter����.������������״̬
* ��������:2019-12-12
* @param newApprovestatus nc.vo.pub.pf.BillStatusEnum
*/
public void setApprovestatus ( Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* ���� approvenote��Getter����.����������������
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getApprovenote() {
return this.approvenote;
} 

/**
* ����approvenote��Setter����.����������������
* ��������:2019-12-12
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* ���� approvedate��Getter����.������������ʱ��
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* ����approvedate��Setter����.������������ʱ��
* ��������:2019-12-12
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* ���� transtype��Getter����.����������������
*  ��������:2019-12-12
* @return nc.vo.pub.billtype.BilltypeVO
*/
public String getTranstype() {
return this.transtype;
} 

/**
* ����transtype��Setter����.����������������
* ��������:2019-12-12
* @param newTranstype nc.vo.pub.billtype.BilltypeVO
*/
public void setTranstype ( String transtype) {
this.transtype=transtype;
} 
 
/**
* ���� billtype��Getter����.����������������
*  ��������:2019-12-12
* @return nc.vo.pub.billtype.BilltypeVO
*/
public String getBilltype() {
return this.billtype;
} 

/**
* ����billtype��Setter����.����������������
* ��������:2019-12-12
* @param newBilltype nc.vo.pub.billtype.BilltypeVO
*/
public void setBilltype ( String billtype) {
this.billtype=billtype;
} 
 
/**
* ���� transtypepk��Getter����.����������������pk
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getTranstypepk() {
return this.transtypepk;
} 

/**
* ����transtypepk��Setter����.����������������pk
* ��������:2019-12-12
* @param newTranstypepk java.lang.String
*/
public void setTranstypepk ( String transtypepk) {
this.transtypepk=transtypepk;
} 
 
/**
* ���� srcbilltype��Getter����.����������Դ��������
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getSrcbilltype() {
return this.srcbilltype;
} 

/**
* ����srcbilltype��Setter����.����������Դ��������
* ��������:2019-12-12
* @param newSrcbilltype java.lang.String
*/
public void setSrcbilltype ( String srcbilltype) {
this.srcbilltype=srcbilltype;
} 
 
/**
* ���� srcbillid��Getter����.����������Դ����id
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getSrcbillid() {
return this.srcbillid;
} 

/**
* ����srcbillid��Setter����.����������Դ����id
* ��������:2019-12-12
* @param newSrcbillid java.lang.String
*/
public void setSrcbillid ( String srcbillid) {
this.srcbillid=srcbillid;
} 
 
/**
* ���� emendenum��Getter����.���������޶�ö��
*  ��������:2019-12-12
* @return java.lang.Integer
*/
public Integer getEmendenum() {
return this.emendenum;
} 

/**
* ����emendenum��Setter����.���������޶�ö��
* ��������:2019-12-12
* @param newEmendenum java.lang.Integer
*/
public void setEmendenum ( Integer emendenum) {
this.emendenum=emendenum;
} 
 
/**
* ���� billversionpk��Getter����.�����������ݰ汾pk
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getBillversionpk() {
return this.billversionpk;
} 

/**
* ����billversionpk��Setter����.�����������ݰ汾pk
* ��������:2019-12-12
* @param newBillversionpk java.lang.String
*/
public void setBillversionpk ( String billversionpk) {
this.billversionpk=billversionpk;
} 
 
/**
* ���� creator��Getter����.��������������
*  ��������:2019-12-12
* @return nc.vo.sm.UserVO
*/
public String getCreator() {
return this.creator;
} 

/**
* ����creator��Setter����.��������������
* ��������:2019-12-12
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( String creator) {
this.creator=creator;
} 
 
/**
* ���� maketime��Getter����.���������Ƶ�ʱ��
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getMaketime() {
return this.maketime;
} 

/**
* ����maketime��Setter����.���������Ƶ�ʱ��
* ��������:2019-12-12
* @param newMaketime nc.vo.pub.lang.UFDateTime
*/
public void setMaketime ( UFDateTime maketime) {
this.maketime=maketime;
} 
 
/**
* ���� lastmaketime��Getter����.������������޸�ʱ��
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getLastmaketime() {
return this.lastmaketime;
} 

/**
* ����lastmaketime��Setter����.������������޸�ʱ��
* ��������:2019-12-12
* @param newLastmaketime nc.vo.pub.lang.UFDateTime
*/
public void setLastmaketime ( UFDateTime lastmaketime) {
this.lastmaketime=lastmaketime;
} 
 
/**
* ���� billdate��Getter����.����������������
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getBilldate() {
return this.billdate;
} 

/**
* ����billdate��Setter����.����������������
* ��������:2019-12-12
* @param newBilldate nc.vo.pub.lang.UFDate
*/
public void setBilldate ( UFDate billdate) {
this.billdate=billdate;
} 
 
/**
* ���� creationtime��Getter����.������������ʱ��
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getCreationtime() {
return this.creationtime;
} 

/**
* ����creationtime��Setter����.������������ʱ��
* ��������:2019-12-12
* @param newCreationtime java.lang.String
*/
public void setCreationtime ( String creationtime) {
this.creationtime=creationtime;
} 
 
/**
* ���� modifier��Getter����.���������޸���
*  ��������:2019-12-12
* @return nc.vo.sm.UserVO
*/
public String getModifier() {
return this.modifier;
} 

/**
* ����modifier��Setter����.���������޸���
* ��������:2019-12-12
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( String modifier) {
this.modifier=modifier;
} 
 
/**
* ���� modifiedtime��Getter����.���������޸�ʱ��
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* ����modifiedtime��Setter����.���������޸�ʱ��
* ��������:2019-12-12
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* ���� rowno��Getter����.���������к�
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getRowno() {
return this.rowno;
} 

/**
* ����rowno��Setter����.���������к�
* ��������:2019-12-12
* @param newRowno java.lang.String
*/
public void setRowno ( String rowno) {
this.rowno=rowno;
} 
 
/**
* ���� def1��Getter����.���������Զ�����1
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef1() {
return this.def1;
} 

/**
* ����def1��Setter����.���������Զ�����1
* ��������:2019-12-12
* @param newDef1 java.lang.String
*/
public void setDef1 ( String def1) {
this.def1=def1;
} 
 
/**
* ���� def2��Getter����.���������Զ�����2
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef2() {
return this.def2;
} 

/**
* ����def2��Setter����.���������Զ�����2
* ��������:2019-12-12
* @param newDef2 java.lang.String
*/
public void setDef2 ( String def2) {
this.def2=def2;
} 
 
/**
* ���� def3��Getter����.���������Զ�����3
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef3() {
return this.def3;
} 

/**
* ����def3��Setter����.���������Զ�����3
* ��������:2019-12-12
* @param newDef3 java.lang.String
*/
public void setDef3 ( String def3) {
this.def3=def3;
} 
 
/**
* ���� def4��Getter����.���������Զ�����4
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef4() {
return this.def4;
} 

/**
* ����def4��Setter����.���������Զ�����4
* ��������:2019-12-12
* @param newDef4 java.lang.String
*/
public void setDef4 ( String def4) {
this.def4=def4;
} 
 
/**
* ���� def5��Getter����.���������Զ�����5
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef5() {
return this.def5;
} 

/**
* ����def5��Setter����.���������Զ�����5
* ��������:2019-12-12
* @param newDef5 java.lang.String
*/
public void setDef5 ( String def5) {
this.def5=def5;
} 
 
/**
* ���� def6��Getter����.���������Զ�����6
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef6() {
return this.def6;
} 

/**
* ����def6��Setter����.���������Զ�����6
* ��������:2019-12-12
* @param newDef6 java.lang.String
*/
public void setDef6 ( String def6) {
this.def6=def6;
} 
 
/**
* ���� def7��Getter����.���������Զ�����7
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef7() {
return this.def7;
} 

/**
* ����def7��Setter����.���������Զ�����7
* ��������:2019-12-12
* @param newDef7 java.lang.String
*/
public void setDef7 ( String def7) {
this.def7=def7;
} 
 
/**
* ���� def8��Getter����.���������Զ�����8
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef8() {
return this.def8;
} 

/**
* ����def8��Setter����.���������Զ�����8
* ��������:2019-12-12
* @param newDef8 java.lang.String
*/
public void setDef8 ( String def8) {
this.def8=def8;
} 
 
/**
* ���� def9��Getter����.���������Զ�����9
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef9() {
return this.def9;
} 

/**
* ����def9��Setter����.���������Զ�����9
* ��������:2019-12-12
* @param newDef9 java.lang.String
*/
public void setDef9 ( String def9) {
this.def9=def9;
} 
 
/**
* ���� def10��Getter����.���������Զ�����10
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef10() {
return this.def10;
} 

/**
* ����def10��Setter����.���������Զ�����10
* ��������:2019-12-12
* @param newDef10 java.lang.String
*/
public void setDef10 ( String def10) {
this.def10=def10;
} 
 
/**
* ���� def11��Getter����.���������Զ�����11
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef11() {
return this.def11;
} 

/**
* ����def11��Setter����.���������Զ�����11
* ��������:2019-12-12
* @param newDef11 java.lang.String
*/
public void setDef11 ( String def11) {
this.def11=def11;
} 
 
/**
* ���� def12��Getter����.���������Զ�����12
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef12() {
return this.def12;
} 

/**
* ����def12��Setter����.���������Զ�����12
* ��������:2019-12-12
* @param newDef12 java.lang.String
*/
public void setDef12 ( String def12) {
this.def12=def12;
} 
 
/**
* ���� def13��Getter����.���������Զ�����13
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef13() {
return this.def13;
} 

/**
* ����def13��Setter����.���������Զ�����13
* ��������:2019-12-12
* @param newDef13 java.lang.String
*/
public void setDef13 ( String def13) {
this.def13=def13;
} 
 
/**
* ���� def14��Getter����.���������Զ�����14
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef14() {
return this.def14;
} 

/**
* ����def14��Setter����.���������Զ�����14
* ��������:2019-12-12
* @param newDef14 java.lang.String
*/
public void setDef14 ( String def14) {
this.def14=def14;
} 
 
/**
* ���� def15��Getter����.���������Զ�����15
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef15() {
return this.def15;
} 

/**
* ����def15��Setter����.���������Զ�����15
* ��������:2019-12-12
* @param newDef15 java.lang.String
*/
public void setDef15 ( String def15) {
this.def15=def15;
} 
 
/**
* ���� def16��Getter����.���������Զ�����16
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef16() {
return this.def16;
} 

/**
* ����def16��Setter����.���������Զ�����16
* ��������:2019-12-12
* @param newDef16 java.lang.String
*/
public void setDef16 ( String def16) {
this.def16=def16;
} 
 
/**
* ���� def17��Getter����.���������Զ�����17
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef17() {
return this.def17;
} 

/**
* ����def17��Setter����.���������Զ�����17
* ��������:2019-12-12
* @param newDef17 java.lang.String
*/
public void setDef17 ( String def17) {
this.def17=def17;
} 
 
/**
* ���� def18��Getter����.���������Զ�����18
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef18() {
return this.def18;
} 

/**
* ����def18��Setter����.���������Զ�����18
* ��������:2019-12-12
* @param newDef18 java.lang.String
*/
public void setDef18 ( String def18) {
this.def18=def18;
} 
 
/**
* ���� def19��Getter����.���������Զ�����19
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef19() {
return this.def19;
} 

/**
* ����def19��Setter����.���������Զ�����19
* ��������:2019-12-12
* @param newDef19 java.lang.String
*/
public void setDef19 ( String def19) {
this.def19=def19;
} 
 
/**
* ���� def20��Getter����.���������Զ�����20
*  ��������:2019-12-12
* @return java.lang.String
*/
public String getDef20() {
return this.def20;
} 

/**
* ����def20��Setter����.���������Զ�����20
* ��������:2019-12-12
* @param newDef20 java.lang.String
*/
public void setDef20 ( String def20) {
this.def20=def20;
} 
 
/**
* ���� ����ʱ�����Getter����.��������ʱ���
*  ��������:2019-12-12
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* ��������ʱ�����Setter����.��������ʱ���
* ��������:2019-12-12
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("vehicle.DriverMileageHVO");
    }
   }
    