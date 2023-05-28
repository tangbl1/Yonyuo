
package nc.itf.vehicle.vorder.vorderhvo;

import java.util.Map;

import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

import nc.vo.vehicle.vorder.AggVorderHVO;
import nc.vo.vehicle.vorder.VorderHVO;
import nc.pub.billcode.vo.BillCodeContext;

public interface  IVorderHVOService{

	/**
	 * 用车申请单的AGGVO查询操作
	 * 根据主键条件查询Agg数组
	 * @param pk主键
	 * @return 结果数组
	 */
	public AggVorderHVO[] listAggVorderHVOByPk(String... pks) throws BusinessException;
	
	/**
	 * 用车申请单的AGGVO查询操作
	 * 根据主键条件查询Agg数组
	 * @param pk主键
	 * @return 结果数组
	 */
	public AggVorderHVO[] listAggVorderHVOByPk(boolean blazyLoad,String... pks) throws BusinessException;
	
	/**
	 * 用车申请单的AGGVO查询操作
	 * 根据主键条件查询Agg对象
	 * @param pk 主键
	 * @return 结果对象
	 */
	public  AggVorderHVO findAggVorderHVOByPk(String pk) throws BusinessException;
	
	/**
	 * 用车申请单的AGGVO查询操作
	 * 根据条件字符串查询Agg数组
	 * @param condition 主键
	 * @return 结果数组
	 */
	public  AggVorderHVO[] listAggVorderHVOByCondition(String condition) throws BusinessException;
	
	/**
	 * 用车申请单的AGGVO查询操作
	 * 根据条件字符串查询Agg数组
	 * @param condition 条件
	 * @param orderPath 排序集合
	 * @return 结果数组
	 */
	public  AggVorderHVO[] listAggVorderHVOByCondition(String condition,String[] orderPath) throws BusinessException;
	
	/**
	 * 用车申请单的主表VO查询操作
	 * 根据主键条件查询Agg数组
	 * @param pk主键
	 * @return 结果数组
	 */
	public VorderHVO[] listVorderHVOByPk(String...pks) throws BusinessException;
	
	/**
	 * 用车申请单的主表VO查询操作
	 * 根据主键条件查询Agg对象
	 * @param pk 主键
	 * @return 结果对象
	 */
	public  VorderHVO findVorderHVOByPk(String pk) throws BusinessException;
	
	/**
	 * 用车申请单的主表VO查询操作
	 * 根据条件字符串查询Agg数组
	 * @param condition 主键
	 * @return 结果数组
	 */
	public  VorderHVO[] listVorderHVOByCondition(String condition) throws BusinessException;
	/**
	 * 用车申请单的主表VO查询操作
	 * 根据条件字符串查询Agg数组
	 * @param condition 主键
	 * @return 结果数组
	 */
	public  VorderHVO[] listVorderHVOByCondition(String condition,String[] orderPath) throws BusinessException;

	/**
	 * 用车申请单的主表pk查询操作
	 * 根据条件字符串查询pk数组数组
	 * @param condition 查询方案+
	 * @return 结果数组
	 */
	public String[] listVorderHVOPkByCond(String condition) throws BusinessException;
	
	/**
	 * 用车申请单的主表pk查询操作
	 * 根据条件字符串查询pk数组数组
	 * @param condition 查询方案+
	 * @return 结果数组
	 */
	public String[] listVorderHVOPkByCond(String condition,String[] orderPath) throws BusinessException;
	/**
	 * 给主实体vo设置默认值
	 * @param vo
	 */
	public void initDefaultData(VorderHVO vo);
	
	
	/**
	 * 预新增操作用车申请单数据
	 * @param userJson  新增时需要的扩展参数对象
	 */
	public AggVorderHVO preAddAggVorderHVO(Map<String,Object> userJson) throws BusinessException;
	
	public AggVorderHVO preAddAggVorderHVO(AggVorderHVO vo,Map<String,Object> userJson) throws BusinessException;
	 /**
	 * 预编辑操作用车申请单数据
	 * @param userJson  新增时需要的扩展参数对象
	 */
	public AggVorderHVO preEditAggVorderHVO(String pk) throws BusinessException;
	
	 /**
	 * 复制操作用车申请单数据
	 * 
	 */
	public AggVorderHVO copyAggVorderHVO(String pk) throws BusinessException;
	/**
	 * 保存操作用车申请单数据
	 * @param vos 保存对象
	 * @return @
	 */
	public AggVorderHVO[] saveAggVorderHVO(AggVorderHVO vo) throws BusinessException;

	public AggVorderHVO[] saveAggVorderHVO(AggVorderHVO[] vos) throws BusinessException;
	
	
	/**
	 * 删除操作用车申请单数据
	 * @param vos 删除对象
	 * @return @
	 */
	public AggVorderHVO[] deleteAggVorderHVOs(Map<String,String> tsMap) throws BusinessException;
	
	/**
	 * 加载树类型数据用车申请单
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
	/**
	 * 单个提交
	 * @param actionName 动作脚本编码
	 * @param tsMap  key为主键  value为ts
	 * @param assign 指派信息，只有单个提交允许指派
	 * @return Object
	 * @throws BusinessException
	 */
	public Object commitAggVorderHVO(String actionName,Map<String,String> tsMap,Object assign) throws BusinessException;
	/**
	 * 批量提交
	 * @param actionName 动作脚本编码
	 * @param tsMap  key为主键  value为ts
	 * @throws BusinessException
	 */
	public Object batchCommitAggVorderHVO(String actionName,Map<String,String> tsMap) throws BusinessException;
	/**
	 * 单个收回
	 * @param actionName 动作脚本编码
	 * @param tsMap  key为主键  value为ts
	 * @return
	 * @throws BusinessException
	 */
	public Object uncommitAggVorderHVO(String actionName,Map<String,String> tsMap) throws BusinessException;
	/**
	 * 批量收回
	 * @param actionName 动作脚本编码
	 * @param tsMap  key为主键  value为ts
	 * @throws BusinessException
	 */
	public Object batchUncommitAggVorderHVO(String actionName,Map<String,String> tsMap) throws BusinessException;
	/**
	 * SAVEBASE 回调--动作脚本处调用
	 * @param vos
	 * @throws BusinessException
	 * @return
	 */
	public AggVorderHVO[] callbackSAVEBASE(AggVorderHVO...vos) throws BusinessException;
	
	/**
	 * SAVE 回调--动作脚本处调用
	 * @param vos
	 * @throws BusinessException
	 * @return
	 */
	public AggVorderHVO[] callbackSAVE(AggVorderHVO...vos) throws BusinessException;
	
	/**
	 * UNSAVE 回调--动作脚本处调用
	 * @param vos
	 * @throws BusinessException
	 * @return
	 */
	public AggVorderHVO[] callbackUNSAVE(AggVorderHVO...vos) throws BusinessException;
	
	/**
	 * APPROVE 回调--动作脚本处调用
	 * @param vos
	 * @throws BusinessException
	 * @return
	 */
	public AggVorderHVO[] callbackAPPROVE(AggVorderHVO...vos) throws BusinessException;
	
	/**
	 * UNAPPROVE 回调--动作脚本处调用
	 * @param vos
	 * @throws BusinessException
	 * @return
	 */
	public AggVorderHVO[] callbackUNAPPROVE(AggVorderHVO...vos) throws BusinessException;
	
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