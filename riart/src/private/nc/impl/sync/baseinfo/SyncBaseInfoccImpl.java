package nc.impl.sync.baseinfo;

import com.alibaba.fastjson.JSONArray;
import nc.bs.dao.BaseDAO;
import nc.itf.sync.baseinfo.ISyncBaseInfocc;
import nc.itf.sync.vo.GetOnHandNumCCVO;
import nc.jdbc.framework.processor.BeanListProcessor;

import java.util.ArrayList;

/**
 * @ProjectName:    NC65_DD 
 * @Package:        nc.impl.sync.baseinfo
 * @ClassName:      SyncBaseInfoccImpl
 * @Author:     tbl
 * @Description:    
 * @Date:    2022/10/9 8:36
 * @Version:    1.0
 */
public class SyncBaseInfoccImpl implements ISyncBaseInfocc {

    BaseDAO dao = null;
    /**
     * UAP数据库访问类
     *
     * @return`
     */
    public BaseDAO getBaseDAO() {
        if (dao == null) {
            dao = new BaseDAO();
        }
        return dao;
    }
    @Override
    public String getOnHandNum(String clocationcode, String materialcode,String storcode) throws Exception {
        // 输入参数校验

        if (isSEmptyOrNull(storcode) || isSEmptyOrNull(clocationcode)) {
            throw new Exception("货位和仓库不能为空");
        }
       
        String sql = null;
       if(isSEmptyOrNull(materialcode)){
            sql = "select OrgCode,StorCode,MaterialCode,MaterialName,ClocationCode, MaterialModel,Specification,UnitCode,NOnhandNum " +
                    "from V_IC_ONHANDNUM_NOSTOR_CC where ClocationCode = '"+clocationcode+"' and StorCode = '"+storcode+"'";
        }else{
            sql = "select OrgCode,StorCode,MaterialCode,MaterialName,ClocationCode, MaterialModel,Specification,UnitCode,NOnhandNum " +
                    "from V_IC_ONHANDNUM_NOSTOR_CC where ClocationCode = '"+clocationcode+"' and MaterialCode = '"+materialcode+"' and StorCode = '"+storcode+"'";
        }

        		ArrayList<GetOnHandNumCCVO> resultlist = (ArrayList<GetOnHandNumCCVO>) getBaseDAO()
				.executeQuery(sql, new BeanListProcessor(GetOnHandNumCCVO.class));

		String resultjson = JSONArray.toJSONString(resultlist);
//                JSONArray.fromObject(resultlist).toString();

		return resultjson;
    }
    public static boolean isSEmptyOrNull(String s) {
        return s == null ? true : s.trim().length() <= 0;
    }

}