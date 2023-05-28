
package nc.itf.stgl.xfmx.xfmx;

import java.util.Map;

import nc.vo.pub.BusinessException;

import nc.vo.yz.xfmx.AggXfmx;
import nc.vo.yz.xfmx.Xfmx;
import nc.pub.billcode.vo.BillCodeContext;

public interface  IXfmxService{

	/**
	 * 消费明细的AGGVO查询操作
	 * 根据主键条件查询Agg数组
	 * @param pk主键
	 * @return 结果数组
	 */
	public AggXfmx[] listAggXfmxByPk(String... pks) throws BusinessException;
	
	/**
	 * 消费明细的AGGVO查询操作
	 * 根据主键条件查询Agg数组
	 * @param pk主键
	 * @return 结果数组
	 */
	public AggXfmx[] listAggXfmxByPk(boolean blazyLoad,String... pks) throws BusinessException;
	
	/**
	 * 消费明细的AGGVO查询操作
	 * 根据主键条件查询Agg对象
	 * @param pk 主键
	 * @return 结果对象
	 */
	public  AggXfmx findAggXfmxByPk(String pk) throws BusinessException;
	
	/**
	 * 消费明细的AGGVO查询操作
	 * 根据条件字符串查询Agg数组
	 * @param condition 主键
	 * @return 结果数组
	 */
	public  AggXfmx[] listAggXfmxByCondition(String condition) throws BusinessException;
	
	/**
	 * 消费明细的AGGVO查询操作
	 * 根据条件字符串查询Agg数组
	 * @param condition 条件
	 * @param orderPath 排序集合
	 * @return 结果数组
	 */
	public  AggXfmx[] listAggXfmxByCondition(String condition,String[] orderPath) throws BusinessException;
	
	/**
	 * 消费明细的主表VO查询操作
	 * 根据主键条件查询Agg数组
	 * @param pk主键
	 * @return 结果数组
	 */
	public Xfmx[] listXfmxByPk(String...pks) throws BusinessException;
	
	/**
	 * 消费明细的主表VO查询操作
	 * 根据主键条件查询Agg对象
	 * @param pk 主键
	 * @return 结果对象
	 */
	public  Xfmx findXfmxByPk(String pk) throws BusinessException;
	
	/**
	 * 消费明细的主表VO查询操作
	 * 根据条件字符串查询Agg数组
	 * @param condition 主键
	 * @return 结果数组
	 */
	public  Xfmx[] listXfmxByCondition(String condition) throws BusinessException;
	/**
	 * 消费明细的主表VO查询操作
	 * 根据条件字符串查询Agg数组
	 * @param condition 主键
	 * @return 结果数组
	 */
	public  Xfmx[] listXfmxByCondition(String condition,String[] orderPath) throws BusinessException;

	/**
	 * 消费明细的主表pk查询操作
	 * 根据条件字符串查询pk数组数组
	 * @param condition 查询方案+
	 * @return 结果数组
	 */
	public String[] listXfmxPkByCond(String condition) throws BusinessException;
	
	/**
	 * 消费明细的主表pk查询操作
	 * 根据条件字符串查询pk数组数组
	 * @param condition 查询方案+
	 * @return 结果数组
	 */
	public String[] listXfmxPkByCond(String condition,String[] orderPath) throws BusinessException;
	/**
	 * 给主实体vo设置默认值
	 * @param vo
	 */
	public void initDefaultData(Xfmx vo);
	
	
	/**
	 * 预新增操作消费明细数据
	 * @param userJson  新增时需要的扩展参数对象
	 */
	public AggXfmx preAddAggXfmx(Map<String,Object> userJson) throws BusinessException;
	
	public AggXfmx preAddAggXfmx(AggXfmx vo,Map<String,Object> userJson) throws BusinessException;
	 /**
	 * 预编辑操作消费明细数据
	 * @param userJson  新增时需要的扩展参数对象
	 */
	public AggXfmx preEditAggXfmx(String pk) throws BusinessException;
	
	 /**
	 * 复制操作消费明细数据
	 * 
	 */
	public AggXfmx copyAggXfmx(String pk) throws BusinessException;
	/**
	 * 保存操作消费明细数据
	 * @param vos 保存对象
	 * @return @
	 */
	public AggXfmx[] saveAggXfmx(AggXfmx vo) throws BusinessException;

	public AggXfmx[] saveAggXfmx(AggXfmx[] vos) throws BusinessException;
	
	
	/**
	 * 删除操作消费明细数据
	 * @param vos 删除对象
	 * @return @
	 */
	public AggXfmx[] deleteAggXfmxs(Map<String,String> tsMap) throws BusinessException;
	
	/**
	 * 加载树类型数据消费明细
	 * @param vos 对象
	 * @return @
	 */
	public <T> T[] loadTreeData(Class<T> clazz,Map<String,Object> userJson) throws BusinessException;

	/**
	 * 获取默认(全局)编码规则上下文
	 * 
	 * @param coderuleid 编码规则主键?
	 * @return 编码规则上下文
	 * @throws BusinessException
	 */
	public BillCodeContext getBillCodeContext(String coderuleid) throws BusinessException;

	/**
	 * 获取编码规则上下文
	 * 
	 * @param coderuleid
	 * @param pkgroup
	 * @param pkorg
	 * @return
	 * @throws BusinessException
	 */
	public BillCodeContext getBillCodeContext(String coderuleid, String pkgroup, String pkorg) throws BusinessException;

}