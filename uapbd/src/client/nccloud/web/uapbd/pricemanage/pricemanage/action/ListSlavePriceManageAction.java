package nccloud.web.uapbd.pricemanage.pricemanage.action;

import nccloud.web.codeplatform.framework.action.base.BaseAction;
import nccloud.web.codeplatform.framework.action.base.RequestDTO;
import nccloud.web.codeplatform.framework.action.base.RequstParamWapper;
import nccloud.web.codeplatform.framework.action.base.VOTransform;
import nc.vo.pub.SuperVO;
import nccloud.framework.core.env.Locator;
import nccloud.framework.core.util.ArrayUtils;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.ui.config.Area;
import nccloud.framework.web.ui.config.ITempletResource;
import nccloud.framework.web.ui.config.PageTemplet;
import nccloud.framework.web.ui.config.TempletQueryPara;
import nccloud.framework.web.ui.model.PageInfo;
import nc.itf.uapbd.pricemanage.pricemanage.IPriceManageService;

/**
 * 类说明：子表查询
 *
 **/
public class ListSlavePriceManageAction extends BaseAction {

    @Override
    public Object doAction(IRequest request, RequstParamWapper paramWapper) throws Throwable {

        // 参数接收
        RequestDTO param = VOTransform.toVO(paramWapper.requestString, RequestDTO.class);
        String pk = param.getPk();
        String pageCode = param.getPageCode();
        String formId = param.getFormId();
        // 分页信息
        PageInfo pageInfo = param.getPageInfo();

        VOTransform tf = new VOTransform(null, pageCode);

        // 查询模板
        ITempletResource resource = Locator.find(ITempletResource.class);
        TempletQueryPara templetQueryPara = new TempletQueryPara();
        templetQueryPara.setPagecode(pageCode);
        PageTemplet pageTemplet = resource.query(templetQueryPara);
        // 获取当前要查询的页签区域信息
        Area area = pageTemplet.getArea(formId);
        // 获取页签对应的Class对象
        Class childClazz = Class.forName(area.getClazz());

        // 服务调用
        IPriceManageService service = ServiceLocator.find(IPriceManageService.class);
        String[] allpks = service.queryChildPksByParentId(childClazz, pk);
		
        // 分页处理
        SuperVO[] returnVOs;
        if(!ArrayUtils.isEmpty(allpks) && pageInfo != null) {
            String[] currentPagePks = paramWapper.pageResult(pageInfo, allpks);
            returnVOs = service.queryChildVOByPks(childClazz, currentPagePks);
        } else {
            returnVOs = service.queryChildVOByPks(childClazz, allpks);
        }
        param.setAllpks(allpks);
		
        // 数据返回
        return buildResult(param, false, null, returnVOs);
    }
}
