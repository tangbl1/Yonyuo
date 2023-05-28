
package nc.itf.dd.refreshcost.refreshcosthvo;

import java.util.Map;

import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

import nc.vo.dd.refreshcost.AggRefreshCostHVO;
import nc.vo.dd.refreshcost.RefreshCostHVO;
import nc.pub.billcode.vo.BillCodeContext;

public interface  IRefreshCostHVOService{

	/**
	 * �ɱ����������AGGVO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk����
	 * @return �������
	 */
	public AggRefreshCostHVO[] listAggRefreshCostHVOByPk(String... pks) throws BusinessException;
	
	/**
	 * �ɱ����������AGGVO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk����
	 * @return �������
	 */
	public AggRefreshCostHVO[] listAggRefreshCostHVOByPk(boolean blazyLoad,String... pks) throws BusinessException;
	
	/**
	 * �ɱ����������AGGVO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk ����
	 * @return �������
	 */
	public  AggRefreshCostHVO findAggRefreshCostHVOByPk(String pk) throws BusinessException;
	
	/**
	 * �ɱ����������AGGVO��ѯ����
	 * ���������ַ�����ѯAgg����
	 * @param condition ����
	 * @return �������
	 */
	public  AggRefreshCostHVO[] listAggRefreshCostHVOByCondition(String condition) throws BusinessException;
	
	/**
	 * �ɱ����������AGGVO��ѯ����
	 * ���������ַ�����ѯAgg����
	 * @param condition ����
	 * @param orderPath ���򼯺�
	 * @return �������
	 */
	public  AggRefreshCostHVO[] listAggRefreshCostHVOByCondition(String condition,String[] orderPath) throws BusinessException;
	
	/**
	 * �ɱ��������������VO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk����
	 * @return �������
	 */
	public RefreshCostHVO[] listRefreshCostHVOByPk(String...pks) throws BusinessException;
	
	/**
	 * �ɱ��������������VO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk ����
	 * @return �������
	 */
	public  RefreshCostHVO findRefreshCostHVOByPk(String pk) throws BusinessException;
	
	/**
	 * �ɱ��������������VO��ѯ����
	 * ���������ַ�����ѯAgg����
	 * @param condition ����
	 * @return �������
	 */
	public  RefreshCostHVO[] listRefreshCostHVOByCondition(String condition) throws BusinessException;
	/**
	 * �ɱ��������������VO��ѯ����
	 * ���������ַ�����ѯAgg����
	 * @param condition ����
	 * @return �������
	 */
	public  RefreshCostHVO[] listRefreshCostHVOByCondition(String condition,String[] orderPath) throws BusinessException;

	/**
	 * �ɱ��������������pk��ѯ����
	 * ���������ַ�����ѯpk��������
	 * @param condition ��ѯ����+
	 * @return �������
	 */
	public String[] listRefreshCostHVOPkByCond(String condition) throws BusinessException;
	
	/**
	 * �ɱ��������������pk��ѯ����
	 * ���������ַ�����ѯpk��������
	 * @param condition ��ѯ����+
	 * @return �������
	 */
	public String[] listRefreshCostHVOPkByCond(String condition,String[] orderPath) throws BusinessException;
	/**
	 * ����ʵ��vo����Ĭ��ֵ
	 * @param vo
	 */
	public void initDefaultData(RefreshCostHVO vo);
	
	
	/**
	 * Ԥ���������ɱ�������������
	 * @param userJson  ����ʱ��Ҫ����չ��������
	 */
	public AggRefreshCostHVO preAddAggRefreshCostHVO(Map<String,Object> userJson) throws BusinessException;
	
	public AggRefreshCostHVO preAddAggRefreshCostHVO(AggRefreshCostHVO vo,Map<String,Object> userJson) throws BusinessException;
	 /**
	 * Ԥ�༭�����ɱ�������������
	 * @param userJson  ����ʱ��Ҫ����չ��������
	 */
	public AggRefreshCostHVO preEditAggRefreshCostHVO(String pk) throws BusinessException;
	
	 /**
	 * ���Ʋ����ɱ�������������
	 * 
	 */
	public AggRefreshCostHVO copyAggRefreshCostHVO(String pk) throws BusinessException;
	/**
	 * ��������ɱ�������������
	 * @param vos �������
	 * @return @
	 */
	public AggRefreshCostHVO[] saveAggRefreshCostHVO(AggRefreshCostHVO vo) throws BusinessException;

	public AggRefreshCostHVO[] saveAggRefreshCostHVO(AggRefreshCostHVO[] vos) throws BusinessException;
	
	
	/**
	 * ɾ�������ɱ�������������
	 * @param vos ɾ������
	 * @return @
	 */
	public AggRefreshCostHVO[] deleteAggRefreshCostHVOs(Map<String,String> tsMap) throws BusinessException;
	
	/**
	 * �������������ݳɱ���������
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
	public Object commitAggRefreshCostHVO(String actionName,Map<String,String> tsMap,Object assign) throws BusinessException;
	/**
	 * �����ύ
	 * @param actionName �����ű�����
	 * @param tsMap  keyΪ����  valueΪts
	 * @throws BusinessException
	 */
	public Object batchCommitAggRefreshCostHVO(String actionName,Map<String,String> tsMap) throws BusinessException;
	/**
	 * �����ջ�
	 * @param actionName �����ű�����
	 * @param tsMap  keyΪ����  valueΪts
	 * @return
	 * @throws BusinessException
	 */
	public Object uncommitAggRefreshCostHVO(String actionName,Map<String,String> tsMap) throws BusinessException;
	/**
	 * �����ջ�
	 * @param actionName �����ű�����
	 * @param tsMap  keyΪ����  valueΪts
	 * @throws BusinessException
	 */
	public Object batchUncommitAggRefreshCostHVO(String actionName,Map<String,String> tsMap) throws BusinessException;
	/**
	 * SAVEBASE �ص�--�����ű�������
	 * @param vos
	 * @throws BusinessException
	 * @return
	 */
	public AggRefreshCostHVO[] callbackSAVEBASE(AggRefreshCostHVO...vos) throws BusinessException;
	
	/**
	 * SAVE �ص�--�����ű�������
	 * @param vos
	 * @throws BusinessException
	 * @return
	 */
	public AggRefreshCostHVO[] callbackSAVE(AggRefreshCostHVO...vos) throws BusinessException;
	
	/**
	 * UNSAVE �ص�--�����ű�������
	 * @param vos
	 * @throws BusinessException
	 * @return
	 */
	public AggRefreshCostHVO[] callbackUNSAVE(AggRefreshCostHVO...vos) throws BusinessException;
	
	/**
	 * APPROVE �ص�--�����ű�������
	 * @param vos
	 * @throws BusinessException
	 * @return
	 */
	public AggRefreshCostHVO[] callbackAPPROVE(AggRefreshCostHVO...vos) throws BusinessException;
	
	/**
	 * UNAPPROVE �ص�--�����ű�������
	 * @param vos
	 * @throws BusinessException
	 * @return
	 */
	public AggRefreshCostHVO[] callbackUNAPPROVE(AggRefreshCostHVO...vos) throws BusinessException;
	
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