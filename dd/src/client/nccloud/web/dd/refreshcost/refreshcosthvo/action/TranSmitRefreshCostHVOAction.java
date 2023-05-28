package nccloud.web.dd.refreshcost.refreshcosthvo.action;

import nc.itf.dd.refreshcost.refreshcosthvo.IRefreshCostHVOService;
import nc.vo.dd.refreshcost.AggRefreshCostHVO;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.container.IRequest;
import nccloud.web.codeplatform.framework.action.base.BaseAction;
import nccloud.web.codeplatform.framework.action.base.RequestDTO;
import nccloud.web.codeplatform.framework.action.base.RequstParamWapper;
import nccloud.web.codeplatform.framework.action.base.VOTransform;

/**
 * @ProjectName: BIP
 * @Package: nccloud.web.dd.refreshcost.refreshcosthvo.action
 * @ClassName: TranSmitRefreshCostHVOAction
 * @Author: 15749
 * @Description:
 * @Date: 2023/4/12 16:38   
 * @Version: 1.0
 */
public class TranSmitRefreshCostHVOAction  extends BaseAction {
    @Override
    public Object doAction(IRequest iRequest, RequstParamWapper paramWapper) throws Throwable {

        RequestDTO param = VOTransform.toVO(paramWapper.requestString,RequestDTO.class);

        IRefreshCostHVOService service = ServiceLocator.find(IRefreshCostHVOService.class);

        AggRefreshCostHVO[] vos = this.getVOs(param, AggRefreshCostHVO.class);
        vos[0].getParentVO().setName("≤‚ ‘≤È—Ø");
        vos[0].getParentVO().setVdef1("Y");

        vos = service.saveAggRefreshCostHVO(vos[0]);

        Object result = buildResult(param,param.getBillCard().getHead()!=null,null,vos);
        return result;
    }
}