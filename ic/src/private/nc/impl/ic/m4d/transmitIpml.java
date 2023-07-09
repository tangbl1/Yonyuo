package nc.impl.ic.m4d;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.itf.ic.base.ItransmitServer;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.ic.m4d.entity.MaterialOutBodyVO;
import nc.vo.ic.m4d.entity.MaterialOutHeadVO;
import nc.vo.ic.m4d.entity.MaterialOutVO;

import java.util.ArrayList;

/**
 * @ProjectName: BIP
 * @Package: nc.impl.ic.m4d
 * @ClassName: transmitIpml
 * @Author: 15749
 * @Description:
 * @Date: 2023/4/24 10:47
 * @Version: 1.0
 */
public class transmitIpml implements ItransmitServer {
    @Override
    public MaterialOutVO Trans(MaterialOutVO aggVO) throws DAOException {
        BaseDAO baseDAO = new BaseDAO();
        String pk = aggVO.getHead().getCgeneralhid();
        String sql = "update ic_material_h set vdef14 = '"+aggVO.getHead().getVdef14()+"' where cgeneralhid  ='"+aggVO.getParentVO().getCgeneralhid()+"' and dr = 0";

        new BaseDAO().executeUpdate(sql);
        // 从数据库重新查询，避免前台数据错误
        aggVO = this.Sort(aggVO, pk);
        return aggVO;


    }
    //表体按行号排序，表头更新字段
    private MaterialOutVO Sort(MaterialOutVO aggVO,String pk) throws DAOException{
        String sql = "select * from ic_material_b where nvl(dr,0)=0 and cgeneralhid ='"+pk+"' ";
        ArrayList<MaterialOutBodyVO[]> bodylist = (ArrayList<MaterialOutBodyVO[]>) new BaseDAO().executeQuery(sql, new BeanListProcessor(MaterialOutBodyVO.class));
        aggVO.setChildrenVO(bodylist.toArray(new MaterialOutBodyVO[0]));
        MaterialOutHeadVO headVO = (MaterialOutHeadVO) new BaseDAO().retrieveByPK(
                MaterialOutHeadVO.class, pk);
        aggVO.setParentVO(headVO);
        return aggVO;

    }


}