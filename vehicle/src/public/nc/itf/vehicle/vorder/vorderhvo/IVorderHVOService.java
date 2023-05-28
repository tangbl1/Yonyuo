
package nc.itf.vehicle.vorder.vorderhvo;

import java.util.Map;

import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

import nc.vo.vehicle.vorder.AggVorderHVO;
import nc.vo.vehicle.vorder.VorderHVO;
import nc.pub.billcode.vo.BillCodeContext;

public interface  IVorderHVOService{

	/**
	 * �ó����뵥��AGGVO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk����
	 * @return �������
	 */
	public AggVorderHVO[] listAggVorderHVOByPk(String... pks) throws BusinessException;
	
	/**
	 * �ó����뵥��AGGVO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk����
	 * @return �������
	 */
	public AggVorderHVO[] listAggVorderHVOByPk(boolean blazyLoad,String... pks) throws BusinessException;
	
	/**
	 * �ó����뵥��AGGVO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk ����
	 * @return �������
	 */
	public  AggVorderHVO findAggVorderHVOByPk(String pk) throws BusinessException;
	
	/**
	 * �ó����뵥��AGGVO��ѯ����
	 * ���������ַ�����ѯAgg����
	 * @param condition ����
	 * @return �������
	 */
	public  AggVorderHVO[] listAggVorderHVOByCondition(String condition) throws BusinessException;
	
	/**
	 * �ó����뵥��AGGVO��ѯ����
	 * ���������ַ�����ѯAgg����
	 * @param condition ����
	 * @param orderPath ���򼯺�
	 * @return �������
	 */
	public  AggVorderHVO[] listAggVorderHVOByCondition(String condition,String[] orderPath) throws BusinessException;
	
	/**
	 * �ó����뵥������VO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk����
	 * @return �������
	 */
	public VorderHVO[] listVorderHVOByPk(String...pks) throws BusinessException;
	
	/**
	 * �ó����뵥������VO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk ����
	 * @return �������
	 */
	public  VorderHVO findVorderHVOByPk(String pk) throws BusinessException;
	
	/**
	 * �ó����뵥������VO��ѯ����
	 * ���������ַ�����ѯAgg����
	 * @param condition ����
	 * @return �������
	 */
	public  VorderHVO[] listVorderHVOByCondition(String condition) throws BusinessException;
	/**
	 * �ó����뵥������VO��ѯ����
	 * ���������ַ�����ѯAgg����
	 * @param condition ����
	 * @return �������
	 */
	public  VorderHVO[] listVorderHVOByCondition(String condition,String[] orderPath) throws BusinessException;

	/**
	 * �ó����뵥������pk��ѯ����
	 * ���������ַ�����ѯpk��������
	 * @param condition ��ѯ����+
	 * @return �������
	 */
	public String[] listVorderHVOPkByCond(String condition) throws BusinessException;
	
	/**
	 * �ó����뵥������pk��ѯ����
	 * ���������ַ�����ѯpk��������
	 * @param condition ��ѯ����+
	 * @return �������
	 */
	public String[] listVorderHVOPkByCond(String condition,String[] orderPath) throws BusinessException;
	/**
	 * ����ʵ��vo����Ĭ��ֵ
	 * @param vo
	 */
	public void initDefaultData(VorderHVO vo);
	
	
	/**
	 * Ԥ���������ó����뵥����
	 * @param userJson  ����ʱ��Ҫ����չ��������
	 */
	public AggVorderHVO preAddAggVorderHVO(Map<String,Object> userJson) throws BusinessException;
	
	public AggVorderHVO preAddAggVorderHVO(AggVorderHVO vo,Map<String,Object> userJson) throws BusinessException;
	 /**
	 * Ԥ�༭�����ó����뵥����
	 * @param userJson  ����ʱ��Ҫ����չ��������
	 */
	public AggVorderHVO preEditAggVorderHVO(String pk) throws BusinessException;
	
	 /**
	 * ���Ʋ����ó����뵥����
	 * 
	 */
	public AggVorderHVO copyAggVorderHVO(String pk) throws BusinessException;
	/**
	 * ��������ó����뵥����
	 * @param vos �������
	 * @return @
	 */
	public AggVorderHVO[] saveAggVorderHVO(AggVorderHVO vo) throws BusinessException;

	public AggVorderHVO[] saveAggVorderHVO(AggVorderHVO[] vos) throws BusinessException;
	
	
	/**
	 * ɾ�������ó����뵥����
	 * @param vos ɾ������
	 * @return @
	 */
	public AggVorderHVO[] deleteAggVorderHVOs(Map<String,String> tsMap) throws BusinessException;
	
	/**
	 * ���������������ó����뵥
	 * @param vos ����
	 * @return @
	 */
	public <T> T[] loadTreeData(Class<T> clazz,Map<String,Object> userJson) throws BusinessException;

	/**
	 * ��������������ѯ�ӱ�pks
	 * @param childClazz �ӱ�class
	 * @param parentId ��������
	 * @return �ӱ�pks
	 * @throws BusinessException
	 */
	String[] queryChildPksByParentId(Class childClazz, String parentId) throws BusinessException;

	/**
	 * �����ӱ�������ѯ�ӱ�����
	 * @param childClazz �ӱ�class
	 * @param pks �ӱ�
	 * @return �ӱ�vos
	 * @throws BusinessException
	 */
	SuperVO[] queryChildVOByPks(Class childClazz, String[] pks) throws BusinessException;
	/**
	 * �����ύ
	 * @param actionName �����ű�����
	 * @param tsMap  keyΪ����  valueΪts
	 * @param assign ָ����Ϣ��ֻ�е����ύ����ָ��
	 * @return Object
	 * @throws BusinessException
	 */
	public Object commitAggVorderHVO(String actionName,Map<String,String> tsMap,Object assign) throws BusinessException;
	/**
	 * �����ύ
	 * @param actionName �����ű�����
	 * @param tsMap  keyΪ����  valueΪts
	 * @throws BusinessException
	 */
	public Object batchCommitAggVorderHVO(String actionName,Map<String,String> tsMap) throws BusinessException;
	/**
	 * �����ջ�
	 * @param actionName �����ű�����
	 * @param tsMap  keyΪ����  valueΪts
	 * @return
	 * @throws BusinessException
	 */
	public Object uncommitAggVorderHVO(String actionName,Map<String,String> tsMap) throws BusinessException;
	/**
	 * �����ջ�
	 * @param actionName �����ű�����
	 * @param tsMap  keyΪ����  valueΪts
	 * @throws BusinessException
	 */
	public Object batchUncommitAggVorderHVO(String actionName,Map<String,String> tsMap) throws BusinessException;
	/**
	 * SAVEBASE �ص�--�����ű�������
	 * @param vos
	 * @throws BusinessException
	 * @return
	 */
	public AggVorderHVO[] callbackSAVEBASE(AggVorderHVO...vos) throws BusinessException;
	
	/**
	 * SAVE �ص�--�����ű�������
	 * @param vos
	 * @throws BusinessException
	 * @return
	 */
	public AggVorderHVO[] callbackSAVE(AggVorderHVO...vos) throws BusinessException;
	
	/**
	 * UNSAVE �ص�--�����ű�������
	 * @param vos
	 * @throws BusinessException
	 * @return
	 */
	public AggVorderHVO[] callbackUNSAVE(AggVorderHVO...vos) throws BusinessException;
	
	/**
	 * APPROVE �ص�--�����ű�������
	 * @param vos
	 * @throws BusinessException
	 * @return
	 */
	public AggVorderHVO[] callbackAPPROVE(AggVorderHVO...vos) throws BusinessException;
	
	/**
	 * UNAPPROVE �ص�--�����ű�������
	 * @param vos
	 * @throws BusinessException
	 * @return
	 */
	public AggVorderHVO[] callbackUNAPPROVE(AggVorderHVO...vos) throws BusinessException;
	
	/**
	 * ��ȡĬ��(ȫ��)�������������
	 * 
	 * @param coderuleid �����������?
	 * @return �������������
	 * @throws BusinessException
	 */
	public BillCodeContext getBillCodeContext(String coderuleid) throws BusinessException;

	/**
	 * ��ȡ�������������
	 * 
	 * @param coderuleid
	 * @param pkgroup
	 * @param pkorg
	 * @return
	 * @throws BusinessException
	 */
	public BillCodeContext getBillCodeContext(String coderuleid, String pkgroup, String pkorg) throws BusinessException;

}