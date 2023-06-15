package nc.vo.vehicle;

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
 *  创建日期:2019-12-7
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class VehicleMessageVO extends SuperVO {
	
/**
*主键
*/
public String pk_vehicle;
/**
*单据号
*/
public String billno;
/**
*车牌号
*/
public String vehicleno;
/**
*车辆类型
*/
public String vtype;
/**
*车辆性质
*/
public String vcharacter;
/**
*所属单位
*/
public String unit;
/**
*所属部门
*/
public String dept;
/**
*车辆状态
*/
public String vstate;
/**
*司机姓名
*/
public String driver;
/**
*司机电话
*/
public String dphone;
/**
*载客数量
*/
public Integer passengernum;
/**
*车辆照片
*/
public Object vphoto;
/**
*司机照片
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
*集团
*/
public String pk_group;
/**
*单据ID
*/
public String billid;
/**
*所属组织
*/
public String pkorg;
/**
*业务类型
*/
public String busitype;
/**
*制单人
*/
public String billmaker;
/**
*审批人
*/
public String approver;
/**
*审批状态
*/
public Integer approvestatus;
/**
*审批批语
*/
public String approvenote;
/**
*审批时间
*/
public UFDateTime approvedate;
/**
*交易类型
*/
public String transtype;
/**
*单据类型
*/
public String billtype;
/**
*交易类型pk
*/
public String transtypepk;
/**
*来源单据类型
*/
public String srcbilltype;
/**
*来源单据id
*/
public String srcbillid;
/**
*修订枚举
*/
public Integer emendenum;
/**
*单据版本pk
*/
public String billversionpk;
/**
*创建人
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
*制单时间
*/
public UFDateTime maketime;
/**
*最后修改时间
*/
public UFDateTime lastmaketime;
/**
*行号
*/
public String rowno;
/**
*单据日期
*/
public UFDate billdate;
/**
*创建时间
*/
public String creationtime;
/**
*修改人
*/
public String modifier;
/**
*修改时间
*/
public UFDateTime modifiedtime;
/**
*id
*/
public String id;
/**
*组织
*/
public String pk_org;
/**
*组织版本
*/
public String pk_org_v;
/**
*司机主键
*/
public String pk_driver;
/**
*时间戳
*/
public UFDateTime ts;
    
    
/**
* 属性 pk_vehicle的Getter方法.属性名：主键
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getPk_vehicle() {
return this.pk_vehicle;
} 

/**
* 属性pk_vehicle的Setter方法.属性名：主键
* 创建日期:2019-12-7
* @param newPk_vehicle java.lang.String
*/
public void setPk_vehicle ( String pk_vehicle) {
this.pk_vehicle=pk_vehicle;
} 
 
/**
* 属性 billno的Getter方法.属性名：单据号
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getBillno() {
return this.billno;
} 

/**
* 属性billno的Setter方法.属性名：单据号
* 创建日期:2019-12-7
* @param newBillno java.lang.String
*/
public void setBillno ( String billno) {
this.billno=billno;
} 
 
/**
* 属性 vehicleno的Getter方法.属性名：车牌号
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getVehicleno() {
return this.vehicleno;
} 

/**
* 属性vehicleno的Setter方法.属性名：车牌号
* 创建日期:2019-12-7
* @param newVehicleno java.lang.String
*/
public void setVehicleno ( String vehicleno) {
this.vehicleno=vehicleno;
} 
 
/**
* 属性 vtype的Getter方法.属性名：车辆类型
*  创建日期:2019-12-7
* @return nc.vo.vehicle.vtype
*/
public String getVtype() {
return this.vtype;
} 

/**
* 属性vtype的Setter方法.属性名：车辆类型
* 创建日期:2019-12-7
* @param newVtype nc.vo.vehicle.vtype
*/
public void setVtype ( String vtype) {
this.vtype=vtype;
} 
 
/**
* 属性 vcharacter的Getter方法.属性名：车辆性质
*  创建日期:2019-12-7
* @return nc.vo.vehicle.vcharacter
*/
public String getVcharacter() {
return this.vcharacter;
} 

/**
* 属性vcharacter的Setter方法.属性名：车辆性质
* 创建日期:2019-12-7
* @param newVcharacter nc.vo.vehicle.vcharacter
*/
public void setVcharacter ( String vcharacter) {
this.vcharacter=vcharacter;
} 
 
/**
* 属性 unit的Getter方法.属性名：所属单位
*  创建日期:2019-12-7
* @return nc.vo.org.OrgVO
*/
public String getUnit() {
return this.unit;
} 

/**
* 属性unit的Setter方法.属性名：所属单位
* 创建日期:2019-12-7
* @param newUnit nc.vo.org.OrgVO
*/
public void setUnit ( String unit) {
this.unit=unit;
} 
 
/**
* 属性 dept的Getter方法.属性名：所属部门
*  创建日期:2019-12-7
* @return nc.vo.org.DeptVO
*/
public String getDept() {
return this.dept;
} 

/**
* 属性dept的Setter方法.属性名：所属部门
* 创建日期:2019-12-7
* @param newDept nc.vo.org.DeptVO
*/
public void setDept ( String dept) {
this.dept=dept;
} 
 
/**
* 属性 vstate的Getter方法.属性名：车辆状态
*  创建日期:2019-12-7
* @return nc.vo.vehicle.vstate
*/
public String getVstate() {
return this.vstate;
} 

/**
* 属性vstate的Setter方法.属性名：车辆状态
* 创建日期:2019-12-7
* @param newVstate nc.vo.vehicle.vstate
*/
public void setVstate ( String vstate) {
this.vstate=vstate;
} 
 
/**
* 属性 driver的Getter方法.属性名：司机姓名
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getDriver() {
return this.driver;
} 

/**
* 属性driver的Setter方法.属性名：司机姓名
* 创建日期:2019-12-7
* @param newDriver java.lang.String
*/
public void setDriver ( String driver) {
this.driver=driver;
} 
 
/**
* 属性 dphone的Getter方法.属性名：司机电话
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getDphone() {
return this.dphone;
} 

/**
* 属性dphone的Setter方法.属性名：司机电话
* 创建日期:2019-12-7
* @param newDphone java.lang.String
*/
public void setDphone ( String dphone) {
this.dphone=dphone;
} 
 
/**
* 属性 passengernum的Getter方法.属性名：载客数量
*  创建日期:2019-12-7
* @return java.lang.Integer
*/
public Integer getPassengernum() {
return this.passengernum;
} 

/**
* 属性passengernum的Setter方法.属性名：载客数量
* 创建日期:2019-12-7
* @param newPassengernum java.lang.Integer
*/
public void setPassengernum ( Integer passengernum) {
this.passengernum=passengernum;
} 
 
/**
* 属性 vphoto的Getter方法.属性名：车辆照片
*  创建日期:2019-12-7
* @return java.lang.Object
*/
public Object getVphoto() {
return this.vphoto;
} 

/**
* 属性vphoto的Setter方法.属性名：车辆照片
* 创建日期:2019-12-7
* @param newVphoto java.lang.Object
*/
public void setVphoto ( Object vphoto) {
this.vphoto=vphoto;
} 
 
/**
* 属性 dphoto的Getter方法.属性名：司机照片
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getDphoto() {
return this.dphoto;
} 

/**
* 属性dphoto的Setter方法.属性名：司机照片
* 创建日期:2019-12-7
* @param newDphoto java.lang.String
*/
public void setDphoto ( String dphoto) {
this.dphoto=dphoto;
} 
 
/**
* 属性 def1的Getter方法.属性名：def1
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getDef1() {
return this.def1;
} 

/**
* 属性def1的Setter方法.属性名：def1
* 创建日期:2019-12-7
* @param newDef1 java.lang.String
*/
public void setDef1 ( String def1) {
this.def1=def1;
} 
 
/**
* 属性 def2的Getter方法.属性名：def2
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getDef2() {
return this.def2;
} 

/**
* 属性def2的Setter方法.属性名：def2
* 创建日期:2019-12-7
* @param newDef2 java.lang.String
*/
public void setDef2 ( String def2) {
this.def2=def2;
} 
 
/**
* 属性 def3的Getter方法.属性名：def3
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getDef3() {
return this.def3;
} 

/**
* 属性def3的Setter方法.属性名：def3
* 创建日期:2019-12-7
* @param newDef3 java.lang.String
*/
public void setDef3 ( String def3) {
this.def3=def3;
} 
 
/**
* 属性 def4的Getter方法.属性名：def4
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getDef4() {
return this.def4;
} 

/**
* 属性def4的Setter方法.属性名：def4
* 创建日期:2019-12-7
* @param newDef4 java.lang.String
*/
public void setDef4 ( String def4) {
this.def4=def4;
} 
 
/**
* 属性 def5的Getter方法.属性名：def5
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getDef5() {
return this.def5;
} 

/**
* 属性def5的Setter方法.属性名：def5
* 创建日期:2019-12-7
* @param newDef5 java.lang.String
*/
public void setDef5 ( String def5) {
this.def5=def5;
} 
 
/**
* 属性 pk_group的Getter方法.属性名：集团
*  创建日期:2019-12-7
* @return nc.vo.org.GroupVO
*/
public String getPk_group() {
return this.pk_group;
} 

/**
* 属性pk_group的Setter方法.属性名：集团
* 创建日期:2019-12-7
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* 属性 billid的Getter方法.属性名：单据ID
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getBillid() {
return this.billid;
} 

/**
* 属性billid的Setter方法.属性名：单据ID
* 创建日期:2019-12-7
* @param newBillid java.lang.String
*/
public void setBillid ( String billid) {
this.billid=billid;
} 
 
/**
* 属性 pkorg的Getter方法.属性名：所属组织
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getPkorg() {
return this.pkorg;
} 

/**
* 属性pkorg的Setter方法.属性名：所属组织
* 创建日期:2019-12-7
* @param newPkorg java.lang.String
*/
public void setPkorg ( String pkorg) {
this.pkorg=pkorg;
} 
 
/**
* 属性 busitype的Getter方法.属性名：业务类型
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getBusitype() {
return this.busitype;
} 

/**
* 属性busitype的Setter方法.属性名：业务类型
* 创建日期:2019-12-7
* @param newBusitype java.lang.String
*/
public void setBusitype ( String busitype) {
this.busitype=busitype;
} 
 
/**
* 属性 billmaker的Getter方法.属性名：制单人
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getBillmaker() {
return this.billmaker;
} 

/**
* 属性billmaker的Setter方法.属性名：制单人
* 创建日期:2019-12-7
* @param newBillmaker java.lang.String
*/
public void setBillmaker ( String billmaker) {
this.billmaker=billmaker;
} 
 
/**
* 属性 approver的Getter方法.属性名：审批人
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getApprover() {
return this.approver;
} 

/**
* 属性approver的Setter方法.属性名：审批人
* 创建日期:2019-12-7
* @param newApprover java.lang.String
*/
public void setApprover ( String approver) {
this.approver=approver;
} 
 
/**
* 属性 approvestatus的Getter方法.属性名：审批状态
*  创建日期:2019-12-7
* @return nc.vo.pub.pf.BillStatusEnum
*/
public Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* 属性approvestatus的Setter方法.属性名：审批状态
* 创建日期:2019-12-7
* @param newApprovestatus nc.vo.pub.pf.BillStatusEnum
*/
public void setApprovestatus ( Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* 属性 approvenote的Getter方法.属性名：审批批语
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getApprovenote() {
return this.approvenote;
} 

/**
* 属性approvenote的Setter方法.属性名：审批批语
* 创建日期:2019-12-7
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* 属性 approvedate的Getter方法.属性名：审批时间
*  创建日期:2019-12-7
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* 属性approvedate的Setter方法.属性名：审批时间
* 创建日期:2019-12-7
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* 属性 transtype的Getter方法.属性名：交易类型
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getTranstype() {
return this.transtype;
} 

/**
* 属性transtype的Setter方法.属性名：交易类型
* 创建日期:2019-12-7
* @param newTranstype java.lang.String
*/
public void setTranstype ( String transtype) {
this.transtype=transtype;
} 
 
/**
* 属性 billtype的Getter方法.属性名：单据类型
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getBilltype() {
return this.billtype;
} 

/**
* 属性billtype的Setter方法.属性名：单据类型
* 创建日期:2019-12-7
* @param newBilltype java.lang.String
*/
public void setBilltype ( String billtype) {
this.billtype=billtype;
} 
 
/**
* 属性 transtypepk的Getter方法.属性名：交易类型pk
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getTranstypepk() {
return this.transtypepk;
} 

/**
* 属性transtypepk的Setter方法.属性名：交易类型pk
* 创建日期:2019-12-7
* @param newTranstypepk java.lang.String
*/
public void setTranstypepk ( String transtypepk) {
this.transtypepk=transtypepk;
} 
 
/**
* 属性 srcbilltype的Getter方法.属性名：来源单据类型
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getSrcbilltype() {
return this.srcbilltype;
} 

/**
* 属性srcbilltype的Setter方法.属性名：来源单据类型
* 创建日期:2019-12-7
* @param newSrcbilltype java.lang.String
*/
public void setSrcbilltype ( String srcbilltype) {
this.srcbilltype=srcbilltype;
} 
 
/**
* 属性 srcbillid的Getter方法.属性名：来源单据id
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getSrcbillid() {
return this.srcbillid;
} 

/**
* 属性srcbillid的Setter方法.属性名：来源单据id
* 创建日期:2019-12-7
* @param newSrcbillid java.lang.String
*/
public void setSrcbillid ( String srcbillid) {
this.srcbillid=srcbillid;
} 
 
/**
* 属性 emendenum的Getter方法.属性名：修订枚举
*  创建日期:2019-12-7
* @return java.lang.Integer
*/
public Integer getEmendenum() {
return this.emendenum;
} 

/**
* 属性emendenum的Setter方法.属性名：修订枚举
* 创建日期:2019-12-7
* @param newEmendenum java.lang.Integer
*/
public void setEmendenum ( Integer emendenum) {
this.emendenum=emendenum;
} 
 
/**
* 属性 billversionpk的Getter方法.属性名：单据版本pk
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getBillversionpk() {
return this.billversionpk;
} 

/**
* 属性billversionpk的Setter方法.属性名：单据版本pk
* 创建日期:2019-12-7
* @param newBillversionpk java.lang.String
*/
public void setBillversionpk ( String billversionpk) {
this.billversionpk=billversionpk;
} 
 
/**
* 属性 creator的Getter方法.属性名：创建人
*  创建日期:2019-12-7
* @return nc.vo.sm.UserVO
*/
public String getCreator() {
return this.creator;
} 

/**
* 属性creator的Setter方法.属性名：创建人
* 创建日期:2019-12-7
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( String creator) {
this.creator=creator;
} 
 
/**
* 属性 code的Getter方法.属性名：code
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getCode() {
return this.code;
} 

/**
* 属性code的Setter方法.属性名：code
* 创建日期:2019-12-7
* @param newCode java.lang.String
*/
public void setCode ( String code) {
this.code=code;
} 
 
/**
* 属性 name的Getter方法.属性名：name
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getName() {
return this.name;
} 

/**
* 属性name的Setter方法.属性名：name
* 创建日期:2019-12-7
* @param newName java.lang.String
*/
public void setName ( String name) {
this.name=name;
} 
 
/**
* 属性 maketime的Getter方法.属性名：制单时间
*  创建日期:2019-12-7
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getMaketime() {
return this.maketime;
} 

/**
* 属性maketime的Setter方法.属性名：制单时间
* 创建日期:2019-12-7
* @param newMaketime nc.vo.pub.lang.UFDateTime
*/
public void setMaketime ( UFDateTime maketime) {
this.maketime=maketime;
} 
 
/**
* 属性 lastmaketime的Getter方法.属性名：最后修改时间
*  创建日期:2019-12-7
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getLastmaketime() {
return this.lastmaketime;
} 

/**
* 属性lastmaketime的Setter方法.属性名：最后修改时间
* 创建日期:2019-12-7
* @param newLastmaketime nc.vo.pub.lang.UFDateTime
*/
public void setLastmaketime ( UFDateTime lastmaketime) {
this.lastmaketime=lastmaketime;
} 
 
/**
* 属性 rowno的Getter方法.属性名：行号
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getRowno() {
return this.rowno;
} 

/**
* 属性rowno的Setter方法.属性名：行号
* 创建日期:2019-12-7
* @param newRowno java.lang.String
*/
public void setRowno ( String rowno) {
this.rowno=rowno;
} 
 
/**
* 属性 billdate的Getter方法.属性名：单据日期
*  创建日期:2019-12-7
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getBilldate() {
return this.billdate;
} 

/**
* 属性billdate的Setter方法.属性名：单据日期
* 创建日期:2019-12-7
* @param newBilldate nc.vo.pub.lang.UFDate
*/
public void setBilldate ( UFDate billdate) {
this.billdate=billdate;
} 
 
/**
* 属性 creationtime的Getter方法.属性名：创建时间
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getCreationtime() {
return this.creationtime;
} 

/**
* 属性creationtime的Setter方法.属性名：创建时间
* 创建日期:2019-12-7
* @param newCreationtime java.lang.String
*/
public void setCreationtime ( String creationtime) {
this.creationtime=creationtime;
} 
 
/**
* 属性 modifier的Getter方法.属性名：修改人
*  创建日期:2019-12-7
* @return nc.vo.sm.UserVO
*/
public String getModifier() {
return this.modifier;
} 

/**
* 属性modifier的Setter方法.属性名：修改人
* 创建日期:2019-12-7
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( String modifier) {
this.modifier=modifier;
} 
 
/**
* 属性 modifiedtime的Getter方法.属性名：修改时间
*  创建日期:2019-12-7
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* 属性modifiedtime的Setter方法.属性名：修改时间
* 创建日期:2019-12-7
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* 属性 id的Getter方法.属性名：id
*  创建日期:2019-12-7
* @return java.lang.String
*/
public String getId() {
return this.id;
} 

/**
* 属性id的Setter方法.属性名：id
* 创建日期:2019-12-7
* @param newId java.lang.String
*/
public void setId ( String id) {
this.id=id;
} 
 
/**
* 属性 pk_org的Getter方法.属性名：组织
*  创建日期:2019-12-7
* @return nc.vo.org.OrgVO
*/
public String getPk_org() {
return this.pk_org;
} 

/**
* 属性pk_org的Setter方法.属性名：组织
* 创建日期:2019-12-7
* @param newPk_org nc.vo.org.OrgVO
*/
public void setPk_org ( String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* 属性 pk_org_v的Getter方法.属性名：组织版本
*  创建日期:2019-12-7
* @return nc.vo.vorg.OrgVersionVO
*/
public String getPk_org_v() {
return this.pk_org_v;
} 

/**
* 属性pk_org_v的Setter方法.属性名：组织版本
* 创建日期:2019-12-7
* @param newPk_org_v nc.vo.vorg.OrgVersionVO
*/
public void setPk_org_v ( String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* 属性 pk_driver的Getter方法.属性名：司机主键
*  创建日期:2019-12-7
* @return nc.vo.vehicle.driverfiles.DriverFiles
*/
public String getPk_driver() {
return this.pk_driver;
} 

/**
* 属性pk_driver的Setter方法.属性名：司机主键
* 创建日期:2019-12-7
* @param newPk_driver nc.vo.vehicle.driverfiles.DriverFiles
*/
public void setPk_driver ( String pk_driver) {
this.pk_driver=pk_driver;
} 
 
/**
* 属性 生成时间戳的Getter方法.属性名：时间戳
*  创建日期:2019-12-7
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* 属性生成时间戳的Setter方法.属性名：时间戳
* 创建日期:2019-12-7
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
    