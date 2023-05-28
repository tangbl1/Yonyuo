package nc.itf.ic.base;/**
 * @ProjectName: BIP
 * @Package: nc.itf.ic.base
 * @ClassName: ItransmitServer
 * @Author: 15749
 * @Description:
 * @Date: 2023/4/24 10:42
 * @Version: 1.0
 */

import nc.bs.dao.DAOException;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.ic.m4d.entity.MaterialOutVO;

/**
 * @ProjectName: BIP
 * @Package: nc.itf.ic.base
 * @ClassName: ItransmitServer
 * @Author: 15749
 * @Description:
 * @Date: 2023/4/24 10:42
 * @Version: 1.0
 */
public interface ItransmitServer {

    public MaterialOutVO Trans(MaterialOutVO aggVO) throws DAOException;
}
