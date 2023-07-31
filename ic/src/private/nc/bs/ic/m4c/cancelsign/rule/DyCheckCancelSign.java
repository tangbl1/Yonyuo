package nc.bs.ic.m4c.cancelsign.rule;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ic.m4c.entity.SaleOutBodyVO;
import nc.vo.ic.m4c.entity.SaleOutVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class DyCheckCancelSign implements IRule<SaleOutVO> {
    @Override
    public void process(SaleOutVO[] saleOutVOS) {

        for (SaleOutVO aggvo : saleOutVOS
        ) {
            try {
                String org_code = (String) new BaseDAO().executeQuery("select code from org_orgs where pk_org = '" + aggvo.getHead().getPk_org() + "'", new ColumnProcessor());
                if (org_code != null && "2001".equals(org_code)) {
                    SaleOutBodyVO[] bvos = aggvo.getBodys();
                    for (SaleOutBodyVO bvo: bvos
                         ) {
                        if(bvo.getVbdef16() != null && "Y".equals(bvo.getVbdef16())){
                            ExceptionUtils.wrappBusinessException("单据已匹配生成客户结算单，无法取消签字，请取消匹配后重新操作！");
                        }
                    }

                }
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
