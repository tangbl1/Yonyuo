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
 * ��˵�����ӱ��ѯ
 *
 **/
public class ListSlavePriceManageAction extends BaseAction {

    @Override
    public Object doAction(IRequest request, RequstParamWapper paramWapper) throws Throwable {

        // ��������
        RequestDTO param = VOTransform.toVO(paramWapper.requestString, RequestDTO.class);
        String pk = param.getPk();
        String pageCode = param.getPageCode();
        String formId = param.getFormId();
        // ��ҳ��Ϣ
        PageInfo pageInfo = param.getPageInfo();

        VOTransform tf = new VOTransform(null, pageCode);

        // ��ѯģ��
        ITempletResource resource = Locator.find(ITempletResource.class);
        TempletQueryPara templetQueryPara = new TempletQueryPara();
        templetQueryPara.setPagecode(pageCode);
        PageTemplet pageTemplet = resource.query(templetQueryPara);
        // ��ȡ��ǰҪ��ѯ��ҳǩ������Ϣ
        Area area = pageTemplet.getArea(formId);
        // ��ȡҳǩ��Ӧ��Class����
        Class childClazz = Class.forName(area.getClazz());

        // �������
        IPriceManageService service = ServiceLocator.find(IPriceManageService.class);
        String[] allpks = service.queryChildPksByParentId(childClazz, pk);
		
        // ��ҳ����
        SuperVO[] returnVOs;
        if(!ArrayUtils.isEmpty(allpks) && pageInfo != null) {
            String[] currentPagePks = paramWapper.pageResult(pageInfo, allpks);
            returnVOs = service.queryChildVOByPks(childClazz, currentPagePks);
        } else {
            returnVOs = service.queryChildVOByPks(childClazz, allpks);
        }
        param.setAllpks(allpks);
		
        // ���ݷ���
        return buildResult(param, false, null, returnVOs);
    }
}
