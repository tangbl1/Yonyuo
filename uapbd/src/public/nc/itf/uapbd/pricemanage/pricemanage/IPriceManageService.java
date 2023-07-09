
package nc.itf.uapbd.pricemanage.pricemanage;

import java.util.Map;

import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

import nc.vo.uapbd.AggPriceManage;
import nc.vo.uapbd.PriceManage;

public interface  IPriceManageService{

	/**
	 * 客户价格管理的AGGVO查询操作
	 * 根据主键条件查询Agg数组
	 * @param pk主键
	 * @return 结果数组
	 */
	public AggPriceManage[] listAggPriceManageByPk(String... pks) throws BusinessException;
	
	/**
	 * 客户价格管理的AGGVO查询操作
	 * 根据主键条件查询Agg数组
	 * @param pk主键
	 * @return 结果数组
	 */
	public AggPriceManage[] listAggPriceManageByPk(boolean blazyLoad,String... pks) throws BusinessException;
	
	/**
	 * 客户价格管理的AGGVO查询操作
	 * 根据主键条件查询Agg对象
	 * @param pk 主键
	 * @return 结果对象
	 */
	public  AggPriceManage findAggPriceManageByPk(String pk) throws BusinessException;
	
	/**
	 * 客户价格管理的AGGVO查询操作
	 * 根据条件字符串查询Agg数组
	 * @param condition 主键
	 * @return 结果数组
	 */
	public  AggPriceManage[] listAggPriceManageByCondition(String condition) throws BusinessException;
	
	/**
	 * 客户价格管理的AGGVO查询操作
	 * 根据条件字符串查询Agg数组
	 * @param condition 条件
	 * @param orderPath 排序集合
	 * @return 结果数组
	 */
	public  AggPriceManage[] listAggPriceManageByCondition(String condition,String[] orderPath) throws BusinessException;
	
	/**
	 * 客户价格管理的主表VO查询操作
	 * 根据主键条件查询Agg数组
	 * @param pk主键
	 * @return 结果数组
	 */
	public PriceManage[] listPriceManageByPk(String...pks) throws BusinessException;
	
	/**
	 * 客户价格管理的主表VO查询操作
	 * 根据主键条件查询Agg对象
	 * @param pk 主键
	 * @return 结果对象
	 */
	public  PriceManage findPriceManageByPk(String pk) throws BusinessException;
	
	/**
	 * 客户价格管理的主表VO查询操作
	 * 根据条件字符串查询Agg数组
	 * @param condition 主键
	 * @return 结果数组
	 */
	public  PriceManage[] listPriceManageByCondition(String condition) throws BusinessException;
	/**
	 * 客户价格管理的主表VO查询操作
	 * 根据条件字符串查询Agg数组
	 * @param condition 主键
	 * @return 结果数组
	 */
	public  PriceManage[] listPriceManageByCondition(String condition,String[] orderPath) throws BusinessException;

	/**
	 * 客户价格管理的主表pk查询操作
	 * 根据条件字符串查询pk数组数组
	 * @param condition 查询方案+
	 * @return 结果数组
	 */
	public String[] listPriceManagePkByCond(String condition) throws BusinessException;
	
	/**
	 * 客户价格管理的主表pk查询操作
	 * 根据条件字符串查询pk数组数组
	 * @param condition 查询方案+
	 * @return 结果数组
	 */
	public String[] listPriceManagePkByCond(String condition,String[] orderPath) throws BusinessException;
	/**
	 * 给主实体vo设置默认值
	 * @param vo
	 */
	public void initDefaultData(PriceManage vo);
	
	
	/**
	 * 预新增操作客户价格管理数据
	 * @param userJson  新增时需要的扩展参数对象
	 */
	public AggPriceManage preAddAggPriceManage(Map<String,Object> userJson) throws BusinessException;
	
	public AggPriceManage preAddAggPriceManage(AggPriceManage vo,Map<String,Object> userJson) throws BusinessException;
	 /**
	 * 预编辑操作客户价格管理数据
	 * @param userJson  新增时需要的扩展参数对象
	 */
	public AggPriceManage preEditAggPriceManage(String pk) throws BusinessException;
	
	 /**
	 * 复制操作客户价格管理数据
	 * 
	 */
	public AggPriceManage copyAggPriceManage(String pk) throws BusinessException;
	/**
	 * 保存操作客户价格管理数据
	 * @param vos 保存对象
	 * @return @
	 */
	public AggPriceManage[] saveAggPriceManage(AggPriceManage vo) throws BusinessException;

	public AggPriceManage[] saveAggPriceManage(AggPriceManage[] vos) throws BusinessException;
	
	
	/**
	 * 删除操作客户价格管理数据
	 * @param vos 删除对象
	 * @return @
	 */
	public AggPriceManage[] deleteAggPriceManages(Map<String,String> tsMap) throws BusinessException;
	
	/**
	 * 加载树类型数据客户价格管理
	 * @param vos 对象
	 * @return @
	 */
	public <T> T[] loadTreeData(Class<T> clazz,Map<String,Object> userJson) throws BusinessException;

	/**
	 * 根据主表主键查询子表pks
	 * @param childClazz 子表class
	 * @param parentId 主表主键
	 * @return 子表pks
	 * @throws BusinessException
	 */
	String[] queryChildPksByParentId(Class childClazz, String parentId) throws BusinessException;

	/**
	 * 根据子表主键查询子表数据
	 * @param childClazz 子表class
	 * @param pks 子表
	 * @return 子表vos
	 * @throws BusinessException
	 */
	SuperVO[] queryChildVOByPks(Class childClazz, String[] pks) throws BusinessException;

}