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
     * 1ͬ�����ŵ���
     *
     * @param ts
     *           ʱ���������ͬ����ʶ���Ǳ��Ϊ��ȡ�������ݣ�
     * @return json��
     * @throws BusinessException
     *            ��ѯ�������׳��쳣
     */
    public String syncOrgs(String sign , String ts) throws Exception;
    /**
     * ��¼��Ȩ
     *
     * @param appKey_	String ע��Ӧ��ʱ���䵽��APPKEY
     * @return String ����ǩ�����Ա�֤����İ�ȫ��
     * @throws BusinessException
     *            ����������׳��쳣
     */
    public String loginSign(String appkey) throws Exception;

}
