package nc.vo.vehicle.inspectionfiles;

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
 
public class InspectionFileHVO extends SuperVO {
	
/**
*ʱ���
*/
public UFDateTime ts;
    
    
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
    return VOMetaFactory.getInstance().getVOMeta("vehicle.InspectionFileHVO");
    }
   }
    