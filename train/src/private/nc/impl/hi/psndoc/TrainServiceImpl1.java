package nc.impl.hi.psndoc;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.hi.psndoc.ITrainService1;
import nc.itf.hi.vo.TrainVO1;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFLiteralDate;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: BIP
 * @Package: nc.impl.hi.psndoc
 * @ClassName: TrainServiceImpl1
 * @Author: 15749
 * @Description:
 * @Date: 2023/4/20 10:44
 * @Version: 1.0
 */
public class TrainServiceImpl1 implements ITrainService1 {
    @Override
    public String train(String xml)  {
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        StringBuilder message = new StringBuilder();
        String resInt = "1";
        String resStr = "";
        try {
            doc = builder.build(new StringReader(xml));
            Element root = doc.getRootElement();// xml为根节点
            List<Element> datalist = root.getChildren("data");//从xml中获得多个data标签
            if (datalist.size() == 0) {
                resInt = "-1";
                resStr = "接收数据解析失败，请检查数据格式";
                Logger.error("人员教育培训接口异常" + xml, null);
                message = message.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?> ").append("<RES><errInt>").append(resInt)
                        .append("</errInt>").append("<errMsg>").append(resStr).append("</errMsg></RES>");
                return message.toString();
            }
            BaseDAO basedao = new BaseDAO();
            for (Element element : datalist) {
                TrainVO1 trainVO = this.saveTrain(element);
                String isupdateSql = "select glbdef4,pk_psndoc_sub from hi_psndoc_train where glbdef14 = '" + trainVO.getGlbdef14() + "'";
                TrainVO1 resoultvo = (TrainVO1) basedao.executeQuery(isupdateSql, new BeanProcessor(TrainVO1.class));
                if (resoultvo == null) {
                    String resourt = basedao.insertVO(trainVO);
                    if (resourt == null) {
                        resInt = "-1";
                        resStr = "id为" + trainVO.getGlbdef14() + "的数据插入失败！";
                        Logger.error("人员教育培训接口异常" + xml, null);
                        message = message.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?> ").append("<RES><errInt>").append(resInt)
                                .append("</errInt>").append("<errMsg>").append(resStr).append("</errMsg></RES>");
                        return message.toString();
                    }
                } else if ("N".equals(resoultvo.getGlbdef4().toString())) {
                    trainVO.setPk_psndoc_sub(resoultvo.getPk_psndoc_sub());
                    int i = basedao.updateVO(trainVO);
                    if (i != 1) {
                        resInt = "-1";
                        resStr = "id为" + trainVO.getGlbdef14() + "的数据更新失败！";
                        Logger.error("人员教育培训接口异常" + xml, null);
                        message = message.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?> ").append("<RES><errInt>").append(resInt)
                                .append("</errInt>").append("<errMsg>").append(resStr).append("</errMsg></RES>");
                        return message.toString();
                    }
                } else {
                    resInt = "-1";
                    resStr = "id为" + trainVO.getGlbdef14() + "的数据状态为Y,无需重复插入或更新！";
                    Logger.error("人员教育培训接口异常" + xml, null);
                    message = message.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?> ").append("<RES><errInt>").append(resInt)
                            .append("</errInt>").append("<errMsg>").append(resStr).append("</errMsg></RES>");
                    return message.toString();
                }


            }
            message = message.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?> ").append("<RES><errInt>").append(resInt)
                    .append("</errInt>").append("<errMsg>").append(resStr).append("</errMsg></RES>");
        } catch (Exception e) {
            e.printStackTrace();
            resInt = "-1";
            resStr = e.getMessage().toString();
            Logger.error("人员教育培训接口异常"+e.getMessage(), e);
            message = message.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?> ").append("<RES><errInt>").append(resInt)
                    .append("</errInt>").append("<errMsg>").append(resStr).append("</errMsg></RES>");
        }
        return message.toString();
    }


    private TrainVO1 saveTrain(Element element) throws BusinessException {
        IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);

        String[] names = new String[] {"id","psncode", "act_name","begindate","enddate","tra_type",
                "tra_mode","traim","tra_content","trm_location","tra_time","tra_cost"
                ,"isallduty","absence_count","ass_result","ass_option" ,"memo","lecturer","credit","courseName"
                ,"status","course_class","course_mode","course_type","course_status","creationtime","modifiedtime","creator","createorg","course_org","lecturer_code"};
        Map<String, String> xmlvalheadlist = this.getxmldocumnet(element, names);
        String psnSql = " select p.pk_org,p.pk_group,p.pk_psndoc,h.pk_psnjob,O.PK_PSNORG  from bd_psndoc p "
                + "left join hi_psnjob h on p.pk_psndoc = h.pk_psndoc and h.ismainjob ='Y' and h.lastflag='Y'"
                + "LEFT JOIN hi_psnorg O ON O.PK_PSNDOC = P.PK_PSNDOC AND O.DR = 0 "
                + "where p.dr='0' and p.dr='0' and p.code='"+xmlvalheadlist.get("psncode")+"'";
        Map<String,String> psnInfo = (Map<String, String>) queryBS.executeQuery(psnSql,new MapProcessor());
        if(psnInfo==null){
            throw new BusinessException("人员编码不正确");
        }
//		String courseSql = "SELECT t.enablestate,t.creationtime,t.modifiedtime,t.creator,s.pk_org as creator_org,t.pk_org as course_org" +
//				" FROM trm_course t left join sm_user s on t.creator = s.cuserid where t.coursename = '"+xmlvalheadlist.get("courseName")+"'";
//		 Map<String,String> courseInfo = (Map<String, String>) queryBS.executeQuery(courseSql,new MapProcessor());

        TrainVO1 trainVO = new TrainVO1();
        trainVO.setGlbdef14(xmlvalheadlist.get("id"));//主键
        trainVO.setAttributeValue("glbdef14", xmlvalheadlist.get("id"));
        trainVO.setPk_org(psnInfo.get("pk_org"));
        trainVO.setPk_group(psnInfo.get("pk_group"));
        trainVO.setPk_psndoc(psnInfo.get("pk_psndoc"));
        trainVO.setPk_psnjob(psnInfo.get("pk_psnjob"));
        trainVO.setPk_psnorg(psnInfo.get("pk_psnorg"));
        trainVO.setAct_name(xmlvalheadlist.get("act_name"));//培训活动名称
        trainVO.setBegindate(new UFLiteralDate(xmlvalheadlist.get("begindate")));//培训开始日期
        trainVO.setEnddate(new UFLiteralDate(xmlvalheadlist.get("enddate")));//培训结束日期
        trainVO.setTra_type(getDefdoc(queryBS,xmlvalheadlist.get("tra_type"),"HR021_0xx"));//培训类型
        trainVO.setTra_mode(getDefdoc(queryBS,xmlvalheadlist.get("tra_mode"),"HR020_0xx"));//培训方式
        trainVO.setTraim(xmlvalheadlist.get("traim"));//培训目标
        trainVO.setTra_content(xmlvalheadlist.get("tra_content"));//培训内容
        trainVO.setTrm_location(xmlvalheadlist.get("trm_location"));//培训地点
        trainVO.setTra_time(new UFDouble(xmlvalheadlist.get("tra_time")));//培训学时
        trainVO.setTra_cost(new UFDouble(xmlvalheadlist.get("tra_cost")));//培训费用
        trainVO.setIsallduty(new UFBoolean(xmlvalheadlist.get("isallduty")));//全勤
        trainVO.setAbsence_count(Integer.parseInt(xmlvalheadlist.get("absence_count")));//缺勤次数
        trainVO.setAss_result(new UFDouble(xmlvalheadlist.get("ass_result")));//考核成绩
        trainVO.setAss_option(xmlvalheadlist.get("ass_option"));//考核评价
        trainVO.setMemo(xmlvalheadlist.get("memo"));//备注
        trainVO.setGlbdef1(xmlvalheadlist.get("lecturer"));//讲师
        trainVO.setAttributeValue("glbdef1", xmlvalheadlist.get("lecturer"));
        trainVO.setGlbdef2(xmlvalheadlist.get("credit"));//学分
        trainVO.setAttributeValue("glbdef2", xmlvalheadlist.get("credit"));
        trainVO.setGlbdef3(xmlvalheadlist.get("courseName"));//课程名称
        trainVO.setAttributeValue("glbdef3", xmlvalheadlist.get("courseName"));//课程名称

        trainVO.setCreationtime(new UFDateTime(new Date()));

        trainVO.setCreator("");//插入谁呢  TODU
        trainVO.setLastflag(new UFBoolean("Y"));//如果有上一次记录，将Lastflag 更新为N TODU

        trainVO.setGlbdef4(new UFBoolean(xmlvalheadlist.get("status")));//完成状态
        trainVO.setAttributeValue("glbdef4", xmlvalheadlist.get("status"));
        trainVO.setGlbdef5(xmlvalheadlist.get("course_class"));//课程分类
        trainVO.setAttributeValue("glbdef5", xmlvalheadlist.get("course_class"));
        trainVO.setGlbdef6(xmlvalheadlist.get("course_mode"));//选课方式
        trainVO.setAttributeValue("glbdef6", xmlvalheadlist.get("course_mode"));
        String course_type = xmlvalheadlist.get("course_type");
        if("在线学习".equals(course_type)){
            course_type = "电子学习";
        }
        if("课堂培训".equals(course_type)){
            course_type = "面授";
        }
        trainVO.setGlbdef7(course_type);//课程类型
        trainVO.setAttributeValue("glbdef7", course_type);


        trainVO.setGlbdef8(xmlvalheadlist.get("course_status"));//课程状态
        trainVO.setAttributeValue("glbdef8", xmlvalheadlist.get("course_status"));
        trainVO.setGlbdef9(xmlvalheadlist.get("creationtime"));//创建日期
        trainVO.setAttributeValue("glbdef9", xmlvalheadlist.get("creationtime"));


        trainVO.setGlbdef10(xmlvalheadlist.get("modifiedtime"));//修改日期
        trainVO.setAttributeValue("glbdef10", xmlvalheadlist.get("modifiedtime"));

        trainVO.setGlbdef11(xmlvalheadlist.get("creator"));//创建者
        trainVO.setAttributeValue("glbdef11", xmlvalheadlist.get("creator"));
        trainVO.setGlbdef12(xmlvalheadlist.get("createorg"));//创建者所在组织
        trainVO.setAttributeValue("glbdef12", xmlvalheadlist.get("createorg"));
        trainVO.setGlbdef13(xmlvalheadlist.get("course_org"));//课程组织
        trainVO.setAttributeValue("glbdef13", xmlvalheadlist.get("course_org"));
        trainVO.setGlbdef15(xmlvalheadlist.get("lecturer_code"));//讲师编码
        trainVO.setAttributeValue("glbdef15", xmlvalheadlist.get("lecturer_code"));



        return trainVO;

    }

    public String getDefdoc(IUAPQueryBS queryBS,String code,String listCode) throws BusinessException{
        String sql = "select d.pk_defdoc from bd_defdoc d where d.pk_defdoclist = "
                + "(select l.pk_defdoclist from bd_defdoclist l where l.code='"+listCode+"') "
                + "and d.dr=0 and d.code='"+code+"'";

        return (String) queryBS.executeQuery(sql, new ColumnProcessor());
    }

    public Map<String, String> getxmldocumnet(Element doc, String[] names) {
        Map<String, String> xmlvalue = new HashMap<String, String>();
        for (String name : names) {
            String xmldocval = ((Element) doc.getChildren(name).get(0)).getText();
            xmlvalue.put(name, xmldocval);
        }
        return xmlvalue;
    }
}