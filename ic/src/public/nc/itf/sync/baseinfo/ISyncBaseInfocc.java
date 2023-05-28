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
     * �ִ�����ѯ
     *
     * @param clocationcode,materialcode
     *            ��λ����,���ϱ���
     * @return json��
     * @throws BusinessException
     *            ��ѯ�������׳��쳣
     */
    public String getOnHandNum(String clocationcode ,String materialcode,String storcode) throws Exception;
}
