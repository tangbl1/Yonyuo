package nc.vo.vehicle;

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
 *  ��������:2019-12-7
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class VehicleMessageVO extends SuperVO {
	
/**
*����
*/
public String pk_vehicle;
/**
*���ݺ�
*/
public String billno;
/**
*���ƺ�
*/
public String vehicleno;
/**
*��������
*/
public String vtype;
/**
*��������
*/
public String vcharacter;
/**
*������λ
*/
public String unit;
/**
*��������
*/
public String dept;
/**
*����״̬
*/
public String vstate;
/**
*˾������
*/
public String driver;
/**
*˾���绰
*/
public String dphone;
/**
*�ؿ�����
*/
public Integer passengernum;
/**
*������Ƭ
*/
public Object vphoto;
/**
*˾����Ƭ
*/
public String dphoto;
/**
*def1
*/
public String def1;
/**
*def2
*/
public String def2;
/**
*def3
*/
public String def3;
/**
*def4
*/
public String def4;
/**
*def5
*/
public String def5;
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
*code
*/
public String code;
/**
*name
*/
public String name;
/**
*�Ƶ�ʱ��
*/
public UFDateTime maketime;
/**
*����޸�ʱ��
*/
public UFDateTime lastmaketime;
/**
*�к�
*/
public String rowno;
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
*id
*/
public String id;
/**
*��֯
*/
public String pk_org;
/**
*��֯�汾
*/
public String pk_org_v;
/**
*˾������
*/
public String pk_driver;
/**
*ʱ���
*/
public UFDateTime ts;
    
    
/**
* ���� pk_vehicle��Getter����.������������
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getPk_vehicle() {
return this.pk_vehicle;
} 

/**
* ����pk_vehicle��Setter����.������������
* ��������:2019-12-7
* @param newPk_vehicle java.lang.String
*/
public void setPk_vehicle ( String pk_vehicle) {
this.pk_vehicle=pk_vehicle;
} 
 
/**
* ���� billno��Getter����.�����������ݺ�
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getBillno() {
return this.billno;
} 

/**
* ����billno��Setter����.�����������ݺ�
* ��������:2019-12-7
* @param newBillno java.lang.String
*/
public void setBillno ( String billno) {
this.billno=billno;
} 
 
/**
* ���� vehicleno��Getter����.�����������ƺ�
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getVehicleno() {
return this.vehicleno;
} 

/**
* ����vehicleno��Setter����.�����������ƺ�
* ��������:2019-12-7
* @param newVehicleno java.lang.String
*/
public void setVehicleno ( String vehicleno) {
this.vehicleno=vehicleno;
} 
 
/**
* ���� vtype��Getter����.����������������
*  ��������:2019-12-7
* @return nc.vo.vehicle.vtype
*/
public String getVtype() {
return this.vtype;
} 

/**
* ����vtype��Setter����.����������������
* ��������:2019-12-7
* @param newVtype nc.vo.vehicle.vtype
*/
public void setVtype ( String vtype) {
this.vtype=vtype;
} 
 
/**
* ���� vcharacter��Getter����.����������������
*  ��������:2019-12-7
* @return nc.vo.vehicle.vcharacter
*/
public String getVcharacter() {
return this.vcharacter;
} 

/**
* ����vcharacter��Setter����.����������������
* ��������:2019-12-7
* @param newVcharacter nc.vo.vehicle.vcharacter
*/
public void setVcharacter ( String vcharacter) {
this.vcharacter=vcharacter;
} 
 
/**
* ���� unit��Getter����.��������������λ
*  ��������:2019-12-7
* @return nc.vo.org.OrgVO
*/
public String getUnit() {
return this.unit;
} 

/**
* ����unit��Setter����.��������������λ
* ��������:2019-12-7
* @param newUnit nc.vo.org.OrgVO
*/
public void setUnit ( String unit) {
this.unit=unit;
} 
 
/**
* ���� dept��Getter����.����������������
*  ��������:2019-12-7
* @return nc.vo.org.DeptVO
*/
public String getDept() {
return this.dept;
} 

/**
* ����dept��Setter����.����������������
* ��������:2019-12-7
* @param newDept nc.vo.org.DeptVO
*/
public void setDept ( String dept) {
this.dept=dept;
} 
 
/**
* ���� vstate��Getter����.������������״̬
*  ��������:2019-12-7
* @return nc.vo.vehicle.vstate
*/
public String getVstate() {
return this.vstate;
} 

/**
* ����vstate��Setter����.������������״̬
* ��������:2019-12-7
* @param newVstate nc.vo.vehicle.vstate
*/
public void setVstate ( String vstate) {
this.vstate=vstate;
} 
 
/**
* ���� driver��Getter����.��������˾������
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getDriver() {
return this.driver;
} 

/**
* ����driver��Setter����.��������˾������
* ��������:2019-12-7
* @param newDriver java.lang.String
*/
public void setDriver ( String driver) {
this.driver=driver;
} 
 
/**
* ���� dphone��Getter����.��������˾���绰
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getDphone() {
return this.dphone;
} 

/**
* ����dphone��Setter����.��������˾���绰
* ��������:2019-12-7
* @param newDphone java.lang.String
*/
public void setDphone ( String dphone) {
this.dphone=dphone;
} 
 
/**
* ���� passengernum��Getter����.���������ؿ�����
*  ��������:2019-12-7
* @return java.lang.Integer
*/
public Integer getPassengernum() {
return this.passengernum;
} 

/**
* ����passengernum��Setter����.���������ؿ�����
* ��������:2019-12-7
* @param newPassengernum java.lang.Integer
*/
public void setPassengernum ( Integer passengernum) {
this.passengernum=passengernum;
} 
 
/**
* ���� vphoto��Getter����.��������������Ƭ
*  ��������:2019-12-7
* @return java.lang.Object
*/
public Object getVphoto() {
return this.vphoto;
} 

/**
* ����vphoto��Setter����.��������������Ƭ
* ��������:2019-12-7
* @param newVphoto java.lang.Object
*/
public void setVphoto ( Object vphoto) {
this.vphoto=vphoto;
} 
 
/**
* ���� dphoto��Getter����.��������˾����Ƭ
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getDphoto() {
return this.dphoto;
} 

/**
* ����dphoto��Setter����.��������˾����Ƭ
* ��������:2019-12-7
* @param newDphoto java.lang.String
*/
public void setDphoto ( String dphoto) {
this.dphoto=dphoto;
} 
 
/**
* ���� def1��Getter����.��������def1
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getDef1() {
return this.def1;
} 

/**
* ����def1��Setter����.��������def1
* ��������:2019-12-7
* @param newDef1 java.lang.String
*/
public void setDef1 ( String def1) {
this.def1=def1;
} 
 
/**
* ���� def2��Getter����.��������def2
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getDef2() {
return this.def2;
} 

/**
* ����def2��Setter����.��������def2
* ��������:2019-12-7
* @param newDef2 java.lang.String
*/
public void setDef2 ( String def2) {
this.def2=def2;
} 
 
/**
* ���� def3��Getter����.��������def3
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getDef3() {
return this.def3;
} 

/**
* ����def3��Setter����.��������def3
* ��������:2019-12-7
* @param newDef3 java.lang.String
*/
public void setDef3 ( String def3) {
this.def3=def3;
} 
 
/**
* ���� def4��Getter����.��������def4
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getDef4() {
return this.def4;
} 

/**
* ����def4��Setter����.��������def4
* ��������:2019-12-7
* @param newDef4 java.lang.String
*/
public void setDef4 ( String def4) {
this.def4=def4;
} 
 
/**
* ���� def5��Getter����.��������def5
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getDef5() {
return this.def5;
} 

/**
* ����def5��Setter����.��������def5
* ��������:2019-12-7
* @param newDef5 java.lang.String
*/
public void setDef5 ( String def5) {
this.def5=def5;
} 
 
/**
* ���� pk_group��Getter����.������������
*  ��������:2019-12-7
* @return nc.vo.org.GroupVO
*/
public String getPk_group() {
return this.pk_group;
} 

/**
* ����pk_group��Setter����.������������
* ��������:2019-12-7
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* ���� billid��Getter����.������������ID
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getBillid() {
return this.billid;
} 

/**
* ����billid��Setter����.������������ID
* ��������:2019-12-7
* @param newBillid java.lang.String
*/
public void setBillid ( String billid) {
this.billid=billid;
} 
 
/**
* ���� pkorg��Getter����.��������������֯
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getPkorg() {
return this.pkorg;
} 

/**
* ����pkorg��Setter����.��������������֯
* ��������:2019-12-7
* @param newPkorg java.lang.String
*/
public void setPkorg ( String pkorg) {
this.pkorg=pkorg;
} 
 
/**
* ���� busitype��Getter����.��������ҵ������
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getBusitype() {
return this.busitype;
} 

/**
* ����busitype��Setter����.��������ҵ������
* ��������:2019-12-7
* @param newBusitype java.lang.String
*/
public void setBusitype ( String busitype) {
this.busitype=busitype;
} 
 
/**
* ���� billmaker��Getter����.���������Ƶ���
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getBillmaker() {
return this.billmaker;
} 

/**
* ����billmaker��Setter����.���������Ƶ���
* ��������:2019-12-7
* @param newBillmaker java.lang.String
*/
public void setBillmaker ( String billmaker) {
this.billmaker=billmaker;
} 
 
/**
* ���� approver��Getter����.��������������
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getApprover() {
return this.approver;
} 

/**
* ����approver��Setter����.��������������
* ��������:2019-12-7
* @param newApprover java.lang.String
*/
public void setApprover ( String approver) {
this.approver=approver;
} 
 
/**
* ���� approvestatus��Getter����.������������״̬
*  ��������:2019-12-7
* @return nc.vo.pub.pf.BillStatusEnum
*/
public Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* ����approvestatus��Setter����.������������״̬
* ��������:2019-12-7
* @param newApprovestatus nc.vo.pub.pf.BillStatusEnum
*/
public void setApprovestatus ( Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* ���� approvenote��Getter����.����������������
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getApprovenote() {
return this.approvenote;
} 

/**
* ����approvenote��Setter����.����������������
* ��������:2019-12-7
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* ���� approvedate��Getter����.������������ʱ��
*  ��������:2019-12-7
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* ����approvedate��Setter����.������������ʱ��
* ��������:2019-12-7
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* ���� transtype��Getter����.����������������
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getTranstype() {
return this.transtype;
} 

/**
* ����transtype��Setter����.����������������
* ��������:2019-12-7
* @param newTranstype java.lang.String
*/
public void setTranstype ( String transtype) {
this.transtype=transtype;
} 
 
/**
* ���� billtype��Getter����.����������������
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getBilltype() {
return this.billtype;
} 

/**
* ����billtype��Setter����.����������������
* ��������:2019-12-7
* @param newBilltype java.lang.String
*/
public void setBilltype ( String billtype) {
this.billtype=billtype;
} 
 
/**
* ���� transtypepk��Getter����.����������������pk
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getTranstypepk() {
return this.transtypepk;
} 

/**
* ����transtypepk��Setter����.����������������pk
* ��������:2019-12-7
* @param newTranstypepk java.lang.String
*/
public void setTranstypepk ( String transtypepk) {
this.transtypepk=transtypepk;
} 
 
/**
* ���� srcbilltype��Getter����.����������Դ��������
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getSrcbilltype() {
return this.srcbilltype;
} 

/**
* ����srcbilltype��Setter����.����������Դ��������
* ��������:2019-12-7
* @param newSrcbilltype java.lang.String
*/
public void setSrcbilltype ( String srcbilltype) {
this.srcbilltype=srcbilltype;
} 
 
/**
* ���� srcbillid��Getter����.����������Դ����id
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getSrcbillid() {
return this.srcbillid;
} 

/**
* ����srcbillid��Setter����.����������Դ����id
* ��������:2019-12-7
* @param newSrcbillid java.lang.String
*/
public void setSrcbillid ( String srcbillid) {
this.srcbillid=srcbillid;
} 
 
/**
* ���� emendenum��Getter����.���������޶�ö��
*  ��������:2019-12-7
* @return java.lang.Integer
*/
public Integer getEmendenum() {
return this.emendenum;
} 

/**
* ����emendenum��Setter����.���������޶�ö��
* ��������:2019-12-7
* @param newEmendenum java.lang.Integer
*/
public void setEmendenum ( Integer emendenum) {
this.emendenum=emendenum;
} 
 
/**
* ���� billversionpk��Getter����.�����������ݰ汾pk
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getBillversionpk() {
return this.billversionpk;
} 

/**
* ����billversionpk��Setter����.�����������ݰ汾pk
* ��������:2019-12-7
* @param newBillversionpk java.lang.String
*/
public void setBillversionpk ( String billversionpk) {
this.billversionpk=billversionpk;
} 
 
/**
* ���� creator��Getter����.��������������
*  ��������:2019-12-7
* @return nc.vo.sm.UserVO
*/
public String getCreator() {
return this.creator;
} 

/**
* ����creator��Setter����.��������������
* ��������:2019-12-7
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( String creator) {
this.creator=creator;
} 
 
/**
* ���� code��Getter����.��������code
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getCode() {
return this.code;
} 

/**
* ����code��Setter����.��������code
* ��������:2019-12-7
* @param newCode java.lang.String
*/
public void setCode ( String code) {
this.code=code;
} 
 
/**
* ���� name��Getter����.��������name
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getName() {
return this.name;
} 

/**
* ����name��Setter����.��������name
* ��������:2019-12-7
* @param newName java.lang.String
*/
public void setName ( String name) {
this.name=name;
} 
 
/**
* ���� maketime��Getter����.���������Ƶ�ʱ��
*  ��������:2019-12-7
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getMaketime() {
return this.maketime;
} 

/**
* ����maketime��Setter����.���������Ƶ�ʱ��
* ��������:2019-12-7
* @param newMaketime nc.vo.pub.lang.UFDateTime
*/
public void setMaketime ( UFDateTime maketime) {
this.maketime=maketime;
} 
 
/**
* ���� lastmaketime��Getter����.������������޸�ʱ��
*  ��������:2019-12-7
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getLastmaketime() {
return this.lastmaketime;
} 

/**
* ����lastmaketime��Setter����.������������޸�ʱ��
* ��������:2019-12-7
* @param newLastmaketime nc.vo.pub.lang.UFDateTime
*/
public void setLastmaketime ( UFDateTime lastmaketime) {
this.lastmaketime=lastmaketime;
} 
 
/**
* ���� rowno��Getter����.���������к�
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getRowno() {
return this.rowno;
} 

/**
* ����rowno��Setter����.���������к�
* ��������:2019-12-7
* @param newRowno java.lang.String
*/
public void setRowno ( String rowno) {
this.rowno=rowno;
} 
 
/**
* ���� billdate��Getter����.����������������
*  ��������:2019-12-7
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getBilldate() {
return this.billdate;
} 

/**
* ����billdate��Setter����.����������������
* ��������:2019-12-7
* @param newBilldate nc.vo.pub.lang.UFDate
*/
public void setBilldate ( UFDate billdate) {
this.billdate=billdate;
} 
 
/**
* ���� creationtime��Getter����.������������ʱ��
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getCreationtime() {
return this.creationtime;
} 

/**
* ����creationtime��Setter����.������������ʱ��
* ��������:2019-12-7
* @param newCreationtime java.lang.String
*/
public void setCreationtime ( String creationtime) {
this.creationtime=creationtime;
} 
 
/**
* ���� modifier��Getter����.���������޸���
*  ��������:2019-12-7
* @return nc.vo.sm.UserVO
*/
public String getModifier() {
return this.modifier;
} 

/**
* ����modifier��Setter����.���������޸���
* ��������:2019-12-7
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( String modifier) {
this.modifier=modifier;
} 
 
/**
* ���� modifiedtime��Getter����.���������޸�ʱ��
*  ��������:2019-12-7
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* ����modifiedtime��Setter����.���������޸�ʱ��
* ��������:2019-12-7
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* ���� id��Getter����.��������id
*  ��������:2019-12-7
* @return java.lang.String
*/
public String getId() {
return this.id;
} 

/**
* ����id��Setter����.��������id
* ��������:2019-12-7
* @param newId java.lang.String
*/
public void setId ( String id) {
this.id=id;
} 
 
/**
* ���� pk_org��Getter����.����������֯
*  ��������:2019-12-7
* @return nc.vo.org.OrgVO
*/
public String getPk_org() {
return this.pk_org;
} 

/**
* ����pk_org��Setter����.����������֯
* ��������:2019-12-7
* @param newPk_org nc.vo.org.OrgVO
*/
public void setPk_org ( String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* ���� pk_org_v��Getter����.����������֯�汾
*  ��������:2019-12-7
* @return nc.vo.vorg.OrgVersionVO
*/
public String getPk_org_v() {
return this.pk_org_v;
} 

/**
* ����pk_org_v��Setter����.����������֯�汾
* ��������:2019-12-7
* @param newPk_org_v nc.vo.vorg.OrgVersionVO
*/
public void setPk_org_v ( String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* ���� pk_driver��Getter����.��������˾������
*  ��������:2019-12-7
* @return nc.vo.vehicle.driverfiles.DriverFiles
*/
public String getPk_driver() {
return this.pk_driver;
} 

/**
* ����pk_driver��Setter����.��������˾������
* ��������:2019-12-7
* @param newPk_driver nc.vo.vehicle.driverfiles.DriverFiles
*/
public void setPk_driver ( String pk_driver) {
this.pk_driver=pk_driver;
} 
 
/**
* ���� ����ʱ�����Getter����.��������ʱ���
*  ��������:2019-12-7
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* ��������ʱ�����Setter����.��������ʱ���
* ��������:2019-12-7
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("hanwang.cl_vehicle");
    }
   }
    