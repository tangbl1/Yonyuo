package nc.itf.sync.baseinfo;

import nc.vo.pub.BusinessException;

/**
 * @ProjectName: NC65_DD
 * @Package: nc.itf.sync.baseinfo
 * @ClassName: ISyncBaseInfocc
 * @Author: tbl
 * @Description:
 * @Date: 2022/10/9 8:31
 * @Version: 1.0
 */

public interface ISyncBaseInfocc {
    /**
     * 现存量查询
     *
     * @param clocationcode,materialcode
     *            货位编码,物料编码
     * @return json串
     * @throws BusinessException
     *            查询出错则抛出异常
     */
    public String getOnHandNum(String clocationcode ,String materialcode,String storcode) throws Exception;
}
