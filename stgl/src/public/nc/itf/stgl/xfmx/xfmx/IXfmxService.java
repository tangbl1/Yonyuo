
package nc.itf.stgl.xfmx.xfmx;

import java.util.Map;

import nc.vo.pub.BusinessException;

import nc.vo.yz.xfmx.AggXfmx;
import nc.vo.yz.xfmx.Xfmx;
import nc.pub.billcode.vo.BillCodeContext;

public interface  IXfmxService{

	/**
	 * ������ϸ��AGGVO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk����
	 * @return �������
	 */
	public AggXfmx[] listAggXfmxByPk(String... pks) throws BusinessException;
	
	/**
	 * ������ϸ��AGGVO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk����
	 * @return �������
	 */
	public AggXfmx[] listAggXfmxByPk(boolean blazyLoad,String... pks) throws BusinessException;
	
	/**
	 * ������ϸ��AGGVO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk ����
	 * @return �������
	 */
	public  AggXfmx findAggXfmxByPk(String pk) throws BusinessException;
	
	/**
	 * ������ϸ��AGGVO��ѯ����
	 * ���������ַ�����ѯAgg����
	 * @param condition ����
	 * @return �������
	 */
	public  AggXfmx[] listAggXfmxByCondition(String condition) throws BusinessException;
	
	/**
	 * ������ϸ��AGGVO��ѯ����
	 * ���������ַ�����ѯAgg����
	 * @param condition ����
	 * @param orderPath ���򼯺�
	 * @return �������
	 */
	public  AggXfmx[] listAggXfmxByCondition(String condition,String[] orderPath) throws BusinessException;
	
	/**
	 * ������ϸ������VO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk����
	 * @return �������
	 */
	public Xfmx[] listXfmxByPk(String...pks) throws BusinessException;
	
	/**
	 * ������ϸ������VO��ѯ����
	 * ��������������ѯAgg����
	 * @param pk ����
	 * @return �������
	 */
	public  Xfmx findXfmxByPk(String pk) throws BusinessException;
	
	/**
	 * ������ϸ������VO��ѯ����
	 * ���������ַ�����ѯAgg����
	 * @param condition ����
	 * @return �������
	 */
	public  Xfmx[] listXfmxByCondition(String condition) throws BusinessException;
	/**
	 * ������ϸ������VO��ѯ����
	 * ���������ַ�����ѯAgg����
	 * @param condition ����
	 * @return �������
	 */
	public  Xfmx[] listXfmxByCondition(String condition,String[] orderPath) throws BusinessException;

	/**
	 * ������ϸ������pk��ѯ����
	 * ���������ַ�����ѯpk��������
	 * @param condition ��ѯ����+
	 * @return �������
	 */
	public String[] listXfmxPkByCond(String condition) throws BusinessException;
	
	/**
	 * ������ϸ������pk��ѯ����
	 * ���������ַ�����ѯpk��������
	 * @param condition ��ѯ����+
	 * @return �������
	 */
	public String[] listXfmxPkByCond(String condition,String[] orderPath) throws BusinessException;
	/**
	 * ����ʵ��vo����Ĭ��ֵ
	 * @param vo
	 */
	public void initDefaultData(Xfmx vo);
	
	
	/**
	 * Ԥ��������������ϸ����
	 * @param userJson  ����ʱ��Ҫ����չ��������
	 */
	public AggXfmx preAddAggXfmx(Map<String,Object> userJson) throws BusinessException;
	
	public AggXfmx preAddAggXfmx(AggXfmx vo,Map<String,Object> userJson) throws BusinessException;
	 /**
	 * Ԥ�༭����������ϸ����
	 * @param userJson  ����ʱ��Ҫ����չ��������
	 */
	public AggXfmx preEditAggXfmx(String pk) throws BusinessException;
	
	 /**
	 * ���Ʋ���������ϸ����
	 * 
	 */
	public AggXfmx copyAggXfmx(String pk) throws BusinessException;
	/**
	 * �������������ϸ����
	 * @param vos �������
	 * @return @
	 */
	public AggXfmx[] saveAggXfmx(AggXfmx vo) throws BusinessException;

	public AggXfmx[] saveAggXfmx(AggXfmx[] vos) throws BusinessException;
	
	
	/**
	 * ɾ������������ϸ����
	 * @param vos ɾ������
	 * @return @
	 */
	public AggXfmx[] deleteAggXfmxs(Map<String,String> tsMap) throws BusinessException;
	
	/**
	 * ��������������������ϸ
	 * @param vos ����
	 * @return @
	 */
	public <T> T[] loadTreeData(Class<T> clazz,Map<String,Object> userJson) throws BusinessException;

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