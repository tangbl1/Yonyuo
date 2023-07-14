package nccloud.web.ic.outbound.materialout.action;

import com.alibaba.fastjson.JSONObject;
import nc.itf.ic.base.ItransmitServer;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.bd.material.MaterialVO;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.general.define.ICBillVO;
import nc.vo.ic.m4d.entity.MaterialOutBodyVO;
import nc.vo.ic.m4d.entity.MaterialOutHeadVO;
import nc.vo.ic.m4d.entity.MaterialOutVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.core.util.GridCompareUtils;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.config.PageTemplet;
import nccloud.framework.web.ui.pattern.billcard.BillCard;
import nccloud.web.ic.pub.precision.ICBaseBillcardPrecisionHandler;
import nccloud.web.ic.pub.utils.PageUtils;
import nccloud.web.ic.ui.pattern.billcard.ICBillCardOperator;
import org.jdom.JDOMException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: BIP
 * @Package: nccloud.web.ic.outbound.materialout.action
 * @ClassName: MaterialOutTransmitAction
 * @Author: 15749
 * @Description:
 * @Date: 2023/4/23 13:52
 * @Version: 1.0
 */
public class MaterialOutTransmitAction implements ICommonAction {


    @Override
    public Object doAction(IRequest request) {
        BillCard retcard = null;
        IUAPQueryBS query = ServiceLocator.find(IUAPQueryBS.class);
        ItransmitServer itransmitServer = ServiceLocator.find(ItransmitServer.class);

        String str = request.read();
        IJson json = JsonFactory.create();
        BillCard card = json.fromJson(str, BillCard.class);
        JSONObject jsonObject = JSONObject.parseObject(str);
        String actionid = jsonObject.getString("actionid");
        ICBillCardOperator operator = this.getOperator(card);
        MaterialOutVO[] vos = new MaterialOutVO[]{ operator.toBill(request)};
        operator.setOriginBillcard(card);
        MaterialOutVO aggvo = vos[0];
        String type = "0";//0表示新增单据  1表示回滚单据
        if ("CancelTransmit".equals(actionid)) {
            type = "1";
        }

        vos = this.fillBill(vos);
        operator.setOriginBillcard(card);
        Map<String, Object> headMap = new HashMap<>();
        if (vos != null && vos.length > 0) {
            MaterialOutHeadVO headVO = aggvo.getHead();
            MaterialOutBodyVO[] Bodys = aggvo.getBodys();
            String sn = headVO.getVbillcode();
            String user_id = headVO.getCbizid();//领料员
            try {

                String sql = "select name from  bd_psndoc where pk_psndoc ='" + user_id + "'";
                String user_name = null;
                user_name = (String) query.executeQuery(sql, new ColumnProcessor());
                String date = headVO.getDbilldate().toString();
                headMap.put("sn", sn);
                headMap.put("type", type);
                headMap.put("user_id", user_id);
                headMap.put("user_name", user_name);
                headMap.put("date", date);
                Object[] products = new Object[Bodys.length];
                for (int i = 0; i < Bodys.length; i++) {
                    Map<String, Object> bodyMap = new HashMap<>();
                    ICBillBodyVO bodyVO = Bodys[i];
                    String cmaterialvid = bodyVO.getCmaterialvid();
                    sql = "select * from bd_material where  pk_material  = '" + cmaterialvid + "'";
                    MaterialVO materialVersionVO = (MaterialVO) query.executeQuery(sql, new BeanProcessor(MaterialVO.class));
                    String cbodywarehouseid = bodyVO.getCbodywarehouseid();
                    sql = "select code from bd_stordoc where pk_stordoc = '" + cbodywarehouseid + "'";
                    String warehouse = (String) query.executeQuery(sql, new ColumnProcessor());
                    String product_sn = materialVersionVO.getCode();
                    String product_name = materialVersionVO.getName();
                    String quantity = bodyVO.getNnum().toString();
                    String clocationid = bodyVO.getClocationid();
                    sql = "select code from bd_rack where pk_rack  = '" + clocationid + "'";
                    String location = (String) query.executeQuery(sql, new ColumnProcessor());
                    String description = materialVersionVO.getMaterialspec();//规格
                    bodyMap.put("product_sn", product_sn);
                    bodyMap.put("product_name", product_name);
                    bodyMap.put("quantity", quantity);
                    bodyMap.put("location", location);
                    bodyMap.put("warehouse", warehouse);//仓库编码
                    bodyMap.put("description", description);
                    products[i] = bodyMap;

                }
                headMap.put("products", products);

            } catch ( BusinessException e) {
                throw new RuntimeException(e+":材料出库单字段解析失败! ");
            }

                JSONObject js = new JSONObject(headMap);
                SendOrder sendOrder = new SendOrder();

            try {
                String token = sendOrder.getAccessToken(user_id);
//    正式            JSONObject returnjson = sendOrder.Orders(token, js);
//                int code = (int) returnjson.get("code");
//                String message = (String) returnjson.get("message");
//                String ncmessage;

                //begin 测试功能代码
                int code = 201;
                String ncmessage ;
                String message = "{state:0}";
                //end 测试功能代码
                switch (code) {
                    case 510:
                        ncmessage = "失败！传入参数不正确";
                        break;
                    case 511:
                        ncmessage = "失败！找不到传递的物料";
                        break;
                    case 519 | 512:
                        ncmessage = "失败！领料报错异常";
                        break;
                    case 201:
                        ncmessage = "成功！";
                        break;
                    default:
                        ncmessage = "失败！";
                        break;
                }
                if (code == 201 && "0".equals(JSONObject.parseObject(message).get("state").toString())) {
                    if ("0".equals(type)) {
                        aggvo.getHead().setVdef14("Y");
                    } else {
                        aggvo.getHead().setVdef14("N");
                    }
                    aggvo = itransmitServer.Trans(aggvo);
                    retcard = this.afterProcess(operator, aggvo);
                    retcard = GridCompareUtils.compareBillCardGrid(operator.getOriginBillcard(), retcard);
                    operator.translate(retcard);
                    this.dealCarrier(retcard);
                    return retcard;
                }else {
                    ExceptionUtils.wrappBusinessException(ncmessage);
                }
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            } catch (JDOMException e) {
                throw new RuntimeException(e+" 获取配置文件失败!");
            } catch (IOException e) {
                throw new RuntimeException(e+": 接口解析失败!");
            }

        }
       return null;
    }

    private ICBillCardOperator getOperator(BillCard card) {
        String[] info = card.getUserjson().split(",");
        String pagecode = null;
        String appcode = null;
        String pagecodecard = null;
        if (info.length > 1) {
            pagecode = info[0];
            appcode = info[1];
            pagecodecard = info[2];
        } else {
            pagecode = info[0];
            pagecodecard = card.getPageid();
        }

        ICBillCardOperator operator;
        if (appcode == null) {
            operator = new ICBillCardOperator((String) null, pagecodecard, pagecode);
        } else {
            PageTemplet pageTemplet = PageUtils.getPageTemplet(appcode, pagecodecard);
            operator = new ICBillCardOperator(pageTemplet.getOid(), pagecodecard, pagecode);
        }

        return operator;
    }

    private BillCard afterProcess(ICBillCardOperator operator, ICBillVO vo) {
        BillCard retcard = operator.toCard(vo, false, false);
        operator.processICBillstatusForWeb(retcard);
        ICBaseBillcardPrecisionHandler handler = new ICBaseBillcardPrecisionHandler(retcard);
        handler.process();
        return retcard;
    }

    protected MaterialOutVO[] fillBill(MaterialOutVO[] vos) {
        return vos;
    }

    protected void dealCarrier(BillCard card) {
    }
}