package nc.vo.vehicle.drivermileage;

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
 *  创建日期:2019-12-12
 * @author yonyouBQ
 * @version NCPrj ??
 */
 
public class DriverMileageHVO extends SuperVO {
	
/**
*主键
*/
public String pk_drivermile;
/**
*单据号
*/
public String billno;
/**
*车牌号
*/
public String vehicleno;
/**
*本周起始公里数
*/
public UFDouble beginmile;
/**
*本周截止公里数
*/
public UFDouble endmile;
/**
*本周行驶里程
*/
public UFDouble mile;
/**
*本周加油
*/
public UFDouble gas;
/**
*本周加油金额
*/
public UFDouble gasmoney;
/**
*本周百公里油耗
*/
public String gascost;
/**
*其他车辆里程
*/
public UFDouble othermile;
/**
*其他车辆金额
*/
public UFDouble othermoney;
/**
*里程表金额
*/
public UFDouble odometermoney;
/**
*合计
*/
public UFDouble sum;
/**
*本周空行驶公里数
*/
public UFDouble nullmile;
/**
*本周行驶有效里程
*/
public UFDouble effectivemile;
/**
*绩效工资
*/
public UFDouble meritpay;
/**
*绩效奖罚
*/
public UFDouble meritother;
/**
*绩效合计
*/
public UFDouble meritsum;
/**
*本周周末加班次数
*/
public UFDouble overtimecount;
/**
*周末被选金额
*/
public UFDouble weekendmoney;
/**
*本周超时小时数
*/
public UFDouble overhourcount;
/**
*超时加班金额
*/
public UFDouble overhourmoney;
/**
*本周周末值班个数
*/
public UFDouble ondutycount;
/**
*周末值班金额
*/
public UFDouble ondutymoney;
/**
*本周夜班个数
*/
public UFDouble nightcount;
/**
*夜班金额
*/
public UFDouble nightmoney;
/**
*组织
*/
public String pk_org;
/**
*组织版本
*/
public String pk_org_v;
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
*制单时间
*/
public UFDateTime maketime;
/**
*最后修改时间
*/
public UFDateTime lastmaketime;
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
*行号
*/
public String rowno;
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
*时间戳
*/
public UFDateTime ts;
    
    
/**
* 属性 pk_drivermile的Getter方法.属性名：主键
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getPk_drivermile() {
return this.pk_drivermile;
} 

/**
* 属性pk_drivermile的Setter方法.属性名：主键
* 创建日期:2019-12-12
* @param newPk_drivermile java.lang.String
*/
public void setPk_drivermile ( String pk_drivermile) {
this.pk_drivermile=pk_drivermile;
} 
 
/**
* 属性 billno的Getter方法.属性名：单据号
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getBillno() {
return this.billno;
} 

/**
* 属性billno的Setter方法.属性名：单据号
* 创建日期:2019-12-12
* @param newBillno java.lang.String
*/
public void setBillno ( String billno) {
this.billno=billno;
} 
 
/**
* 属性 vehicleno的Getter方法.属性名：车牌号
*  创建日期:2019-12-12
* @return nc.vo.vehicle.VehicleMessageVO
*/
public String getVehicleno() {
return this.vehicleno;
} 

/**
* 属性vehicleno的Setter方法.属性名：车牌号
* 创建日期:2019-12-12
* @param newVehicleno nc.vo.vehicle.VehicleMessageVO
*/
public void setVehicleno ( String vehicleno) {
this.vehicleno=vehicleno;
} 
 
/**
* 属性 beginmile的Getter方法.属性名：本周起始公里数
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getBeginmile() {
return this.beginmile;
} 

/**
* 属性beginmile的Setter方法.属性名：本周起始公里数
* 创建日期:2019-12-12
* @param newBeginmile nc.vo.pub.lang.UFDouble
*/
public void setBeginmile ( UFDouble beginmile) {
this.beginmile=beginmile;
} 
 
/**
* 属性 endmile的Getter方法.属性名：本周截止公里数
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getEndmile() {
return this.endmile;
} 

/**
* 属性endmile的Setter方法.属性名：本周截止公里数
* 创建日期:2019-12-12
* @param newEndmile nc.vo.pub.lang.UFDouble
*/
public void setEndmile ( UFDouble endmile) {
this.endmile=endmile;
} 
 
/**
* 属性 mile的Getter方法.属性名：本周行驶里程
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getMile() {
return this.mile;
} 

/**
* 属性mile的Setter方法.属性名：本周行驶里程
* 创建日期:2019-12-12
* @param newMile nc.vo.pub.lang.UFDouble
*/
public void setMile ( UFDouble mile) {
this.mile=mile;
} 
 
/**
* 属性 gas的Getter方法.属性名：本周加油
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getGas() {
return this.gas;
} 

/**
* 属性gas的Setter方法.属性名：本周加油
* 创建日期:2019-12-12
* @param newGas nc.vo.pub.lang.UFDouble
*/
public void setGas ( UFDouble gas) {
this.gas=gas;
} 
 
/**
* 属性 gasmoney的Getter方法.属性名：本周加油金额
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getGasmoney() {
return this.gasmoney;
} 

/**
* 属性gasmoney的Setter方法.属性名：本周加油金额
* 创建日期:2019-12-12
* @param newGasmoney nc.vo.pub.lang.UFDouble
*/
public void setGasmoney ( UFDouble gasmoney) {
this.gasmoney=gasmoney;
} 
 
/**
* 属性 gascost的Getter方法.属性名：本周百公里油耗
*  创建日期:2019-12-12
* @return java.lang.UFDouble
*/
public String getGascost() {
return this.gascost;
} 

/**
* 属性gascost的Setter方法.属性名：本周百公里油耗
* 创建日期:2019-12-12
* @param newGascost java.lang.UFDouble
*/
public void setGascost ( String gascost) {
this.gascost=gascost;
} 
 
/**
* 属性 othermile的Getter方法.属性名：其他车辆里程
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getOthermile() {
return this.othermile;
} 

/**
* 属性othermile的Setter方法.属性名：其他车辆里程
* 创建日期:2019-12-12
* @param newOthermile nc.vo.pub.lang.UFDouble
*/
public void setOthermile ( UFDouble othermile) {
this.othermile=othermile;
} 
 
/**
* 属性 othermoney的Getter方法.属性名：其他车辆金额
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getOthermoney() {
return this.othermoney;
} 

/**
* 属性othermoney的Setter方法.属性名：其他车辆金额
* 创建日期:2019-12-12
* @param newOthermoney nc.vo.pub.lang.UFDouble
*/
public void setOthermoney ( UFDouble othermoney) {
this.othermoney=othermoney;
} 
 
/**
* 属性 odometermoney的Getter方法.属性名：里程表金额
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getOdometermoney() {
return this.odometermoney;
} 

/**
* 属性odometermoney的Setter方法.属性名：里程表金额
* 创建日期:2019-12-12
* @param newOdometermoney nc.vo.pub.lang.UFDouble
*/
public void setOdometermoney ( UFDouble odometermoney) {
this.odometermoney=odometermoney;
} 
 
/**
* 属性 sum的Getter方法.属性名：合计
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getSum() {
return this.sum;
} 

/**
* 属性sum的Setter方法.属性名：合计
* 创建日期:2019-12-12
* @param newSum nc.vo.pub.lang.UFDouble
*/
public void setSum ( UFDouble sum) {
this.sum=sum;
} 
 
/**
* 属性 nullmile的Getter方法.属性名：本周空行驶公里数
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getNullmile() {
return this.nullmile;
} 

/**
* 属性nullmile的Setter方法.属性名：本周空行驶公里数
* 创建日期:2019-12-12
* @param newNullmile nc.vo.pub.lang.UFDouble
*/
public void setNullmile ( UFDouble nullmile) {
this.nullmile=nullmile;
} 
 
/**
* 属性 effectivemile的Getter方法.属性名：本周行驶有效里程
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getEffectivemile() {
return this.effectivemile;
} 

/**
* 属性effectivemile的Setter方法.属性名：本周行驶有效里程
* 创建日期:2019-12-12
* @param newEffectivemile nc.vo.pub.lang.UFDouble
*/
public void setEffectivemile ( UFDouble effectivemile) {
this.effectivemile=effectivemile;
} 
 
/**
* 属性 meritpay的Getter方法.属性名：绩效工资
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getMeritpay() {
return this.meritpay;
} 

/**
* 属性meritpay的Setter方法.属性名：绩效工资
* 创建日期:2019-12-12
* @param newMeritpay nc.vo.pub.lang.UFDouble
*/
public void setMeritpay ( UFDouble meritpay) {
this.meritpay=meritpay;
} 
 
/**
* 属性 meritother的Getter方法.属性名：绩效奖罚
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getMeritother() {
return this.meritother;
} 

/**
* 属性meritother的Setter方法.属性名：绩效奖罚
* 创建日期:2019-12-12
* @param newMeritother nc.vo.pub.lang.UFDouble
*/
public void setMeritother ( UFDouble meritother) {
this.meritother=meritother;
} 
 
/**
* 属性 meritsum的Getter方法.属性名：绩效合计
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getMeritsum() {
return this.meritsum;
} 

/**
* 属性meritsum的Setter方法.属性名：绩效合计
* 创建日期:2019-12-12
* @param newMeritsum nc.vo.pub.lang.UFDouble
*/
public void setMeritsum ( UFDouble meritsum) {
this.meritsum=meritsum;
} 
 
/**
* 属性 overtimecount的Getter方法.属性名：本周周末加班次数
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getOvertimecount() {
return this.overtimecount;
} 

/**
* 属性overtimecount的Setter方法.属性名：本周周末加班次数
* 创建日期:2019-12-12
* @param newOvertimecount nc.vo.pub.lang.UFDouble
*/
public void setOvertimecount ( UFDouble overtimecount) {
this.overtimecount=overtimecount;
} 
 
/**
* 属性 weekendmoney的Getter方法.属性名：周末被选金额
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getWeekendmoney() {
return this.weekendmoney;
} 

/**
* 属性weekendmoney的Setter方法.属性名：周末被选金额
* 创建日期:2019-12-12
* @param newWeekendmoney nc.vo.pub.lang.UFDouble
*/
public void setWeekendmoney ( UFDouble weekendmoney) {
this.weekendmoney=weekendmoney;
} 
 
/**
* 属性 overhourcount的Getter方法.属性名：本周超时小时数
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getOverhourcount() {
return this.overhourcount;
} 

/**
* 属性overhourcount的Setter方法.属性名：本周超时小时数
* 创建日期:2019-12-12
* @param newOverhourcount nc.vo.pub.lang.UFDouble
*/
public void setOverhourcount ( UFDouble overhourcount) {
this.overhourcount=overhourcount;
} 
 
/**
* 属性 overhourmoney的Getter方法.属性名：超时加班金额
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getOverhourmoney() {
return this.overhourmoney;
} 

/**
* 属性overhourmoney的Setter方法.属性名：超时加班金额
* 创建日期:2019-12-12
* @param newOverhourmoney nc.vo.pub.lang.UFDouble
*/
public void setOverhourmoney ( UFDouble overhourmoney) {
this.overhourmoney=overhourmoney;
} 
 
/**
* 属性 ondutycount的Getter方法.属性名：本周周末值班个数
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getOndutycount() {
return this.ondutycount;
} 

/**
* 属性ondutycount的Setter方法.属性名：本周周末值班个数
* 创建日期:2019-12-12
* @param newOndutycount nc.vo.pub.lang.UFDouble
*/
public void setOndutycount ( UFDouble ondutycount) {
this.ondutycount=ondutycount;
} 
 
/**
* 属性 ondutymoney的Getter方法.属性名：周末值班金额
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getOndutymoney() {
return this.ondutymoney;
} 

/**
* 属性ondutymoney的Setter方法.属性名：周末值班金额
* 创建日期:2019-12-12
* @param newOndutymoney nc.vo.pub.lang.UFDouble
*/
public void setOndutymoney ( UFDouble ondutymoney) {
this.ondutymoney=ondutymoney;
} 
 
/**
* 属性 nightcount的Getter方法.属性名：本周夜班个数
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getNightcount() {
return this.nightcount;
} 

/**
* 属性nightcount的Setter方法.属性名：本周夜班个数
* 创建日期:2019-12-12
* @param newNightcount nc.vo.pub.lang.UFDouble
*/
public void setNightcount ( UFDouble nightcount) {
this.nightcount=nightcount;
} 
 
/**
* 属性 nightmoney的Getter方法.属性名：夜班金额
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getNightmoney() {
return this.nightmoney;
} 

/**
* 属性nightmoney的Setter方法.属性名：夜班金额
* 创建日期:2019-12-12
* @param newNightmoney nc.vo.pub.lang.UFDouble
*/
public void setNightmoney ( UFDouble nightmoney) {
this.nightmoney=nightmoney;
} 
 
/**
* 属性 pk_org的Getter方法.属性名：组织
*  创建日期:2019-12-12
* @return nc.vo.org.OrgVO
*/
public String getPk_org() {
return this.pk_org;
} 

/**
* 属性pk_org的Setter方法.属性名：组织
* 创建日期:2019-12-12
* @param newPk_org nc.vo.org.OrgVO
*/
public void setPk_org ( String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* 属性 pk_org_v的Getter方法.属性名：组织版本
*  创建日期:2019-12-12
* @return nc.vo.vorg.OrgVersionVO
*/
public String getPk_org_v() {
return this.pk_org_v;
} 

/**
* 属性pk_org_v的Setter方法.属性名：组织版本
* 创建日期:2019-12-12
* @param newPk_org_v nc.vo.vorg.OrgVersionVO
*/
public void setPk_org_v ( String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* 属性 pk_group的Getter方法.属性名：集团
*  创建日期:2019-12-12
* @return nc.vo.org.GroupVO
*/
public String getPk_group() {
return this.pk_group;
} 

/**
* 属性pk_group的Setter方法.属性名：集团
* 创建日期:2019-12-12
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* 属性 billid的Getter方法.属性名：单据ID
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getBillid() {
return this.billid;
} 

/**
* 属性billid的Setter方法.属性名：单据ID
* 创建日期:2019-12-12
* @param newBillid java.lang.String
*/
public void setBillid ( String billid) {
this.billid=billid;
} 
 
/**
* 属性 pkorg的Getter方法.属性名：所属组织
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getPkorg() {
return this.pkorg;
} 

/**
* 属性pkorg的Setter方法.属性名：所属组织
* 创建日期:2019-12-12
* @param newPkorg java.lang.String
*/
public void setPkorg ( String pkorg) {
this.pkorg=pkorg;
} 
 
/**
* 属性 busitype的Getter方法.属性名：业务类型
*  创建日期:2019-12-12
* @return nc.vo.pf.pub.BusitypeVO
*/
public String getBusitype() {
return this.busitype;
} 

/**
* 属性busitype的Setter方法.属性名：业务类型
* 创建日期:2019-12-12
* @param newBusitype nc.vo.pf.pub.BusitypeVO
*/
public void setBusitype ( String busitype) {
this.busitype=busitype;
} 
 
/**
* 属性 billmaker的Getter方法.属性名：制单人
*  创建日期:2019-12-12
* @return nc.vo.sm.UserVO
*/
public String getBillmaker() {
return this.billmaker;
} 

/**
* 属性billmaker的Setter方法.属性名：制单人
* 创建日期:2019-12-12
* @param newBillmaker nc.vo.sm.UserVO
*/
public void setBillmaker ( String billmaker) {
this.billmaker=billmaker;
} 
 
/**
* 属性 approver的Getter方法.属性名：审批人
*  创建日期:2019-12-12
* @return nc.vo.sm.UserVO
*/
public String getApprover() {
return this.approver;
} 

/**
* 属性approver的Setter方法.属性名：审批人
* 创建日期:2019-12-12
* @param newApprover nc.vo.sm.UserVO
*/
public void setApprover ( String approver) {
this.approver=approver;
} 
 
/**
* 属性 approvestatus的Getter方法.属性名：审批状态
*  创建日期:2019-12-12
* @return nc.vo.pub.pf.BillStatusEnum
*/
public Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* 属性approvestatus的Setter方法.属性名：审批状态
* 创建日期:2019-12-12
* @param newApprovestatus nc.vo.pub.pf.BillStatusEnum
*/
public void setApprovestatus ( Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* 属性 approvenote的Getter方法.属性名：审批批语
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getApprovenote() {
return this.approvenote;
} 

/**
* 属性approvenote的Setter方法.属性名：审批批语
* 创建日期:2019-12-12
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* 属性 approvedate的Getter方法.属性名：审批时间
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* 属性approvedate的Setter方法.属性名：审批时间
* 创建日期:2019-12-12
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* 属性 transtype的Getter方法.属性名：交易类型
*  创建日期:2019-12-12
* @return nc.vo.pub.billtype.BilltypeVO
*/
public String getTranstype() {
return this.transtype;
} 

/**
* 属性transtype的Setter方法.属性名：交易类型
* 创建日期:2019-12-12
* @param newTranstype nc.vo.pub.billtype.BilltypeVO
*/
public void setTranstype ( String transtype) {
this.transtype=transtype;
} 
 
/**
* 属性 billtype的Getter方法.属性名：单据类型
*  创建日期:2019-12-12
* @return nc.vo.pub.billtype.BilltypeVO
*/
public String getBilltype() {
return this.billtype;
} 

/**
* 属性billtype的Setter方法.属性名：单据类型
* 创建日期:2019-12-12
* @param newBilltype nc.vo.pub.billtype.BilltypeVO
*/
public void setBilltype ( String billtype) {
this.billtype=billtype;
} 
 
/**
* 属性 transtypepk的Getter方法.属性名：交易类型pk
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getTranstypepk() {
return this.transtypepk;
} 

/**
* 属性transtypepk的Setter方法.属性名：交易类型pk
* 创建日期:2019-12-12
* @param newTranstypepk java.lang.String
*/
public void setTranstypepk ( String transtypepk) {
this.transtypepk=transtypepk;
} 
 
/**
* 属性 srcbilltype的Getter方法.属性名：来源单据类型
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getSrcbilltype() {
return this.srcbilltype;
} 

/**
* 属性srcbilltype的Setter方法.属性名：来源单据类型
* 创建日期:2019-12-12
* @param newSrcbilltype java.lang.String
*/
public void setSrcbilltype ( String srcbilltype) {
this.srcbilltype=srcbilltype;
} 
 
/**
* 属性 srcbillid的Getter方法.属性名：来源单据id
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getSrcbillid() {
return this.srcbillid;
} 

/**
* 属性srcbillid的Setter方法.属性名：来源单据id
* 创建日期:2019-12-12
* @param newSrcbillid java.lang.String
*/
public void setSrcbillid ( String srcbillid) {
this.srcbillid=srcbillid;
} 
 
/**
* 属性 emendenum的Getter方法.属性名：修订枚举
*  创建日期:2019-12-12
* @return java.lang.Integer
*/
public Integer getEmendenum() {
return this.emendenum;
} 

/**
* 属性emendenum的Setter方法.属性名：修订枚举
* 创建日期:2019-12-12
* @param newEmendenum java.lang.Integer
*/
public void setEmendenum ( Integer emendenum) {
this.emendenum=emendenum;
} 
 
/**
* 属性 billversionpk的Getter方法.属性名：单据版本pk
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getBillversionpk() {
return this.billversionpk;
} 

/**
* 属性billversionpk的Setter方法.属性名：单据版本pk
* 创建日期:2019-12-12
* @param newBillversionpk java.lang.String
*/
public void setBillversionpk ( String billversionpk) {
this.billversionpk=billversionpk;
} 
 
/**
* 属性 creator的Getter方法.属性名：创建人
*  创建日期:2019-12-12
* @return nc.vo.sm.UserVO
*/
public String getCreator() {
return this.creator;
} 

/**
* 属性creator的Setter方法.属性名：创建人
* 创建日期:2019-12-12
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( String creator) {
this.creator=creator;
} 
 
/**
* 属性 maketime的Getter方法.属性名：制单时间
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getMaketime() {
return this.maketime;
} 

/**
* 属性maketime的Setter方法.属性名：制单时间
* 创建日期:2019-12-12
* @param newMaketime nc.vo.pub.lang.UFDateTime
*/
public void setMaketime ( UFDateTime maketime) {
this.maketime=maketime;
} 
 
/**
* 属性 lastmaketime的Getter方法.属性名：最后修改时间
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getLastmaketime() {
return this.lastmaketime;
} 

/**
* 属性lastmaketime的Setter方法.属性名：最后修改时间
* 创建日期:2019-12-12
* @param newLastmaketime nc.vo.pub.lang.UFDateTime
*/
public void setLastmaketime ( UFDateTime lastmaketime) {
this.lastmaketime=lastmaketime;
} 
 
/**
* 属性 billdate的Getter方法.属性名：单据日期
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getBilldate() {
return this.billdate;
} 

/**
* 属性billdate的Setter方法.属性名：单据日期
* 创建日期:2019-12-12
* @param newBilldate nc.vo.pub.lang.UFDate
*/
public void setBilldate ( UFDate billdate) {
this.billdate=billdate;
} 
 
/**
* 属性 creationtime的Getter方法.属性名：创建时间
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getCreationtime() {
return this.creationtime;
} 

/**
* 属性creationtime的Setter方法.属性名：创建时间
* 创建日期:2019-12-12
* @param newCreationtime java.lang.String
*/
public void setCreationtime ( String creationtime) {
this.creationtime=creationtime;
} 
 
/**
* 属性 modifier的Getter方法.属性名：修改人
*  创建日期:2019-12-12
* @return nc.vo.sm.UserVO
*/
public String getModifier() {
return this.modifier;
} 

/**
* 属性modifier的Setter方法.属性名：修改人
* 创建日期:2019-12-12
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( String modifier) {
this.modifier=modifier;
} 
 
/**
* 属性 modifiedtime的Getter方法.属性名：修改时间
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* 属性modifiedtime的Setter方法.属性名：修改时间
* 创建日期:2019-12-12
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* 属性 rowno的Getter方法.属性名：行号
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getRowno() {
return this.rowno;
} 

/**
* 属性rowno的Setter方法.属性名：行号
* 创建日期:2019-12-12
* @param newRowno java.lang.String
*/
public void setRowno ( String rowno) {
this.rowno=rowno;
} 
 
/**
* 属性 def1的Getter方法.属性名：自定义项1
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef1() {
return this.def1;
} 

/**
* 属性def1的Setter方法.属性名：自定义项1
* 创建日期:2019-12-12
* @param newDef1 java.lang.String
*/
public void setDef1 ( String def1) {
this.def1=def1;
} 
 
/**
* 属性 def2的Getter方法.属性名：自定义项2
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef2() {
return this.def2;
} 

/**
* 属性def2的Setter方法.属性名：自定义项2
* 创建日期:2019-12-12
* @param newDef2 java.lang.String
*/
public void setDef2 ( String def2) {
this.def2=def2;
} 
 
/**
* 属性 def3的Getter方法.属性名：自定义项3
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef3() {
return this.def3;
} 

/**
* 属性def3的Setter方法.属性名：自定义项3
* 创建日期:2019-12-12
* @param newDef3 java.lang.String
*/
public void setDef3 ( String def3) {
this.def3=def3;
} 
 
/**
* 属性 def4的Getter方法.属性名：自定义项4
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef4() {
return this.def4;
} 

/**
* 属性def4的Setter方法.属性名：自定义项4
* 创建日期:2019-12-12
* @param newDef4 java.lang.String
*/
public void setDef4 ( String def4) {
this.def4=def4;
} 
 
/**
* 属性 def5的Getter方法.属性名：自定义项5
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef5() {
return this.def5;
} 

/**
* 属性def5的Setter方法.属性名：自定义项5
* 创建日期:2019-12-12
* @param newDef5 java.lang.String
*/
public void setDef5 ( String def5) {
this.def5=def5;
} 
 
/**
* 属性 def6的Getter方法.属性名：自定义项6
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef6() {
return this.def6;
} 

/**
* 属性def6的Setter方法.属性名：自定义项6
* 创建日期:2019-12-12
* @param newDef6 java.lang.String
*/
public void setDef6 ( String def6) {
this.def6=def6;
} 
 
/**
* 属性 def7的Getter方法.属性名：自定义项7
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef7() {
return this.def7;
} 

/**
* 属性def7的Setter方法.属性名：自定义项7
* 创建日期:2019-12-12
* @param newDef7 java.lang.String
*/
public void setDef7 ( String def7) {
this.def7=def7;
} 
 
/**
* 属性 def8的Getter方法.属性名：自定义项8
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef8() {
return this.def8;
} 

/**
* 属性def8的Setter方法.属性名：自定义项8
* 创建日期:2019-12-12
* @param newDef8 java.lang.String
*/
public void setDef8 ( String def8) {
this.def8=def8;
} 
 
/**
* 属性 def9的Getter方法.属性名：自定义项9
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef9() {
return this.def9;
} 

/**
* 属性def9的Setter方法.属性名：自定义项9
* 创建日期:2019-12-12
* @param newDef9 java.lang.String
*/
public void setDef9 ( String def9) {
this.def9=def9;
} 
 
/**
* 属性 def10的Getter方法.属性名：自定义项10
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef10() {
return this.def10;
} 

/**
* 属性def10的Setter方法.属性名：自定义项10
* 创建日期:2019-12-12
* @param newDef10 java.lang.String
*/
public void setDef10 ( String def10) {
this.def10=def10;
} 
 
/**
* 属性 def11的Getter方法.属性名：自定义项11
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef11() {
return this.def11;
} 

/**
* 属性def11的Setter方法.属性名：自定义项11
* 创建日期:2019-12-12
* @param newDef11 java.lang.String
*/
public void setDef11 ( String def11) {
this.def11=def11;
} 
 
/**
* 属性 def12的Getter方法.属性名：自定义项12
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef12() {
return this.def12;
} 

/**
* 属性def12的Setter方法.属性名：自定义项12
* 创建日期:2019-12-12
* @param newDef12 java.lang.String
*/
public void setDef12 ( String def12) {
this.def12=def12;
} 
 
/**
* 属性 def13的Getter方法.属性名：自定义项13
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef13() {
return this.def13;
} 

/**
* 属性def13的Setter方法.属性名：自定义项13
* 创建日期:2019-12-12
* @param newDef13 java.lang.String
*/
public void setDef13 ( String def13) {
this.def13=def13;
} 
 
/**
* 属性 def14的Getter方法.属性名：自定义项14
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef14() {
return this.def14;
} 

/**
* 属性def14的Setter方法.属性名：自定义项14
* 创建日期:2019-12-12
* @param newDef14 java.lang.String
*/
public void setDef14 ( String def14) {
this.def14=def14;
} 
 
/**
* 属性 def15的Getter方法.属性名：自定义项15
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef15() {
return this.def15;
} 

/**
* 属性def15的Setter方法.属性名：自定义项15
* 创建日期:2019-12-12
* @param newDef15 java.lang.String
*/
public void setDef15 ( String def15) {
this.def15=def15;
} 
 
/**
* 属性 def16的Getter方法.属性名：自定义项16
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef16() {
return this.def16;
} 

/**
* 属性def16的Setter方法.属性名：自定义项16
* 创建日期:2019-12-12
* @param newDef16 java.lang.String
*/
public void setDef16 ( String def16) {
this.def16=def16;
} 
 
/**
* 属性 def17的Getter方法.属性名：自定义项17
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef17() {
return this.def17;
} 

/**
* 属性def17的Setter方法.属性名：自定义项17
* 创建日期:2019-12-12
* @param newDef17 java.lang.String
*/
public void setDef17 ( String def17) {
this.def17=def17;
} 
 
/**
* 属性 def18的Getter方法.属性名：自定义项18
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef18() {
return this.def18;
} 

/**
* 属性def18的Setter方法.属性名：自定义项18
* 创建日期:2019-12-12
* @param newDef18 java.lang.String
*/
public void setDef18 ( String def18) {
this.def18=def18;
} 
 
/**
* 属性 def19的Getter方法.属性名：自定义项19
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef19() {
return this.def19;
} 

/**
* 属性def19的Setter方法.属性名：自定义项19
* 创建日期:2019-12-12
* @param newDef19 java.lang.String
*/
public void setDef19 ( String def19) {
this.def19=def19;
} 
 
/**
* 属性 def20的Getter方法.属性名：自定义项20
*  创建日期:2019-12-12
* @return java.lang.String
*/
public String getDef20() {
return this.def20;
} 

/**
* 属性def20的Setter方法.属性名：自定义项20
* 创建日期:2019-12-12
* @param newDef20 java.lang.String
*/
public void setDef20 ( String def20) {
this.def20=def20;
} 
 
/**
* 属性 生成时间戳的Getter方法.属性名：时间戳
*  创建日期:2019-12-12
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* 属性生成时间戳的Setter方法.属性名：时间戳
* 创建日期:2019-12-12
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
    