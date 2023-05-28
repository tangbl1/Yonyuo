package nc.itf.sync.baseinfo;/**
 * @ProjectName: BIP
 * @Package: nc.itf.sync.baseinfo
 * @ClassName: ISyncBaseInfo
 * @Author: 15749
 * @Description:
 * @Date: 2023/4/10 8:52
 * @Version: 1.0
 */

import nc.vo.pub.BusinessException;

/**
 * @ProjectName: BIP
 * @Package: nc.itf.sync.baseinfo
 * @ClassName: ISyncBaseInfo
 * @Author: 15749
 * @Description:
 * @Date: 2023/4/10 8:52
 * @Version: 1.0
 */
public interface ISyncBaseInfo {
    /**
     * 1同步部门档案
     *
     * @param ts
     *           时间戳（增量同步标识，非必填，为空取所有数据）
     * @return json串
     * @throws BusinessException
     *            查询出错则抛出异常
     */
    public String syncOrgs(String sign , String ts) throws Exception;
    /**
     * 登录授权
     *
     * @param appKey_	String 注册应用时分配到的APPKEY
     * @return String 数字签名，以保证请求的安全性
     * @throws BusinessException
     *            插入出错则抛出异常
     */
    public String loginSign(String appkey) throws Exception;

}
