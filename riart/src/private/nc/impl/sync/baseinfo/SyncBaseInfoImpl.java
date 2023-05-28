package nc.impl.sync.baseinfo;

import com.alibaba.fastjson.JSONArray;
import nc.bs.dao.BaseDAO;
import nc.itf.sync.baseinfo.ISyncBaseInfo;
import nc.itf.sync.vo.SyncOrgVO;
import nc.jdbc.framework.processor.BeanListProcessor;

import nc.vo.pub.lang.UFDouble;
import org.apache.commons.codec.digest.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * @ProjectName: BIP
 * @Package: nc.impl.sync.baseinfo
 * @ClassName: SyncBaseInfoImpl
 * @Author: 15749
 * @Description:
 * @Date: 2023/4/10 8:54
 * @Version: 1.0
 */
public class SyncBaseInfoImpl implements ISyncBaseInfo {

    BaseDAO dao = null;
    public static final String DEF_APPKEY1 = "dbdx"; // �ṩ��������ѧ����Ȩ�û�
    @Override
    public String syncOrgs(String sign, String ts) throws Exception {

        // �������У��
        if (isSEmptyOrNull(sign)) {
            throw new Exception("��Ȩ���벻��Ϊ��!");
        }

        // ���÷���ȨУ��
        if (!sign.equals(loginSign(DEF_APPKEY1))) {
            throw new Exception("��Ȩʧ��!");
        }

        String sql = "select OrgCode,OrgName,OrgAbbr,OrgPIDS,ParentCode,IORDER,StatusFlag,TS from v_nc_org ";
        // ����������ʶ����
        if (!isSEmptyOrNull(ts)) {
            sql = sql + " where ts > = '" + ts + "'";
        }
        ArrayList<SyncOrgVO> resultlist = (ArrayList<SyncOrgVO>) getBaseDAO()
                .executeQuery(sql, new BeanListProcessor(SyncOrgVO.class));

        String resultjson = JSONArray.toJSONString(resultlist);
//                .fromObject(resultlist).toString();

        return resultjson;

    }

    @Override
    public String loginSign(String appkey) throws Exception {

        // TODO �Զ����ɵķ������
        if (isSEmptyOrNull(appkey)) {
            return "The appkey cannot be empty";
        }
        if (!appkey.equals("dbdx")) {
            return "Invalid appkey";
        }
        Date currDate = new Date();
        SimpleDateFormat dataFormate = new SimpleDateFormat("yyyy-MM-dd");
        String password = "qwe123";

        String codecPWD = DigestUtils.md5Hex(appkey
                + dataFormate.format(currDate) + password);

        return codecPWD;

    }
    /**
     * UAP���ݿ������
     *
     * @return`
     */
    public BaseDAO getBaseDAO() {
        if (dao == null) {
            dao = new BaseDAO();
        }
        return dao;
    }

    public static boolean isSEmptyOrNull(String s) {
        return s == null ? true : s.trim().length() <= 0;
    }

    public static UFDouble convertToUFDouble(String u) {
        return isSEmptyOrNull(u) ? new UFDouble(0) : new UFDouble(u);
    }
}