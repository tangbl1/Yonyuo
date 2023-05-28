package nc.vo.vehicle.inspectionfiles;

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
 
public class InspectionFileBVO extends SuperVO {
	
/**
*上层单据主键
*/
public String pk_inspection;
/**
*时间戳
*/
public UFDateTime ts;
    
    
/**
* 属性 生成上层主键的Getter方法.属性名：上层主键
*  创建日期:2019-10-29
* @return String
*/
public String getPk_inspection(){
return this.pk_inspection;
}
/**
* 属性生成上层主键的Setter方法.属性名：上层主键
* 创建日期:2019-10-29
* @param newPk_inspection String
*/
public void setPk_inspection(String pk_inspection){
this.pk_inspection=pk_inspection;
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
    return VOMetaFactory.getInstance().getVOMeta("vehicle.InspectionFileBVO");
    }
   }
    