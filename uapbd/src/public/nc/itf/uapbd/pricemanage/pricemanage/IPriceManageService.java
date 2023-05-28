
package nc.itf.uapbd.pricemanage.pricemanage;

import java.util.Map;

import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

import nc.vo.uapbd.AggPriceManage;
import nc.vo.uapbd.PriceManage;
import nc.pub.billcode.vo.BillCodeContext;

public interface  IPriceManageService{

	/**
	 * �ͻ��۸�����AGGVO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk����
	 * @return �������
	 */
	public AggPriceManage[] listAggPriceManageByPk(String... pks) throws BusinessException;
	
	/**
	 * �ͻ��۸�����AGGVO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk����
	 * @return �������
	 */
	public AggPriceManage[] listAggPriceManageByPk(boolean blazyLoad,String... pks) throws BusinessException;
	
	/**
	 * �ͻ��۸�����AGGVO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk ����
	 * @return �������
	 */
	public  AggPriceManage findAggPriceManageByPk(String pk) throws BusinessException;
	
	/**
	 * �ͻ��۸�����AGGVO��ѯ����
	 * ���������ַ�����ѯAgg����
	 * @param condition ����
	 * @return �������
	 */
	public  AggPriceManage[] listAggPriceManageByCondition(String condition) throws BusinessException;
	
	/**
	 * �ͻ��۸�����AGGVO��ѯ����
	 * ���������ַ�����ѯAgg����
	 * @param condition ����
	 * @param orderPath ���򼯺�
	 * @return �������
	 */
	public  AggPriceManage[] listAggPriceManageByCondition(String condition,String[] orderPath) throws BusinessException;
	
	/**
	 * �ͻ��۸���������VO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk����
	 * @return �������
	 */
	public PriceManage[] listPriceManageByPk(String...pks) throws BusinessException;
	
	/**
	 * �ͻ��۸���������VO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk ����
	 * @return �������
	 */
	public  PriceManage findPriceManageByPk(String pk) throws BusinessException;
	
	/**
	 * �ͻ��۸���������VO��ѯ����
	 * ���������ַ�����ѯAgg����
	 * @param condition ����
	 * @return �������
	 */
	public  PriceManage[] listPriceManageByCondition(String condition) throws BusinessException;
	/**
	 * �ͻ��۸���������VO��ѯ����
	 * ���������ַ�����ѯAgg����
	 * @param condition ����
	 * @return �������
	 */
	public  PriceManage[] listPriceManageByCondition(String condition,String[] orderPath) throws BusinessException;

	/**
	 * �ͻ��۸���������pk��ѯ����
	 * ���������ַ�����ѯpk��������
	 * @param condition ��ѯ����+
	 * @return �������
	 */
	public String[] listPriceManagePkByCond(String condition) throws BusinessException;
	
	/**
	 * �ͻ��۸���������pk��ѯ����
	 * ���������ַ�����ѯpk��������
	 * @param condition ��ѯ����+
	 * @return �������
	 */
	public String[] listPriceManagePkByCond(String condition,String[] orderPath) throws BusinessException;
	/**
	 * ����ʵ��vo����Ĭ��ֵ
	 * @param vo
	 */
	public void initDefaultData(PriceManage vo);
	
	
	/**
	 * Ԥ���������ͻ��۸��������
	 * @param userJson  ����ʱ��Ҫ����չ��������
	 */
	public AggPriceManage preAddAggPriceManage(Map<String,Object> userJson) throws BusinessException;
	
	public AggPriceManage preAddAggPriceManage(AggPriceManage vo,Map<String,Object> userJson) throws BusinessException;
	 /**
	 * Ԥ�༭�����ͻ��۸��������
	 * @param userJson  ����ʱ��Ҫ����չ��������
	 */
	public AggPriceManage preEditAggPriceManage(String pk) throws BusinessException;
	
	 /**
	 * ���Ʋ����ͻ��۸��������
	 * 
	 */
	public AggPriceManage copyAggPriceManage(String pk) throws BusinessException;
	/**
	 * ��������ͻ��۸��������
	 * @param vos �������
	 * @return @
	 */
	public AggPriceManage[] saveAggPriceManage(AggPriceManage vo) throws BusinessException;

	public AggPriceManage[] saveAggPriceManage(AggPriceManage[] vos) throws BusinessException;
	
	
	/**
	 * ɾ�������ͻ��۸��������
	 * @param vos ɾ������
	 * @return @
	 */
	public AggPriceManage[] deleteAggPriceManages(Map<String,String> tsMap) throws BusinessException;
	
	/**
	 * �������������ݿͻ��۸����
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