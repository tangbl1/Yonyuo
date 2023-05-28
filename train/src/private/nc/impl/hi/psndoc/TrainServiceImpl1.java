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
            Element root = doc.getRootElement();// xmlΪ���ڵ�
            List<Element> datalist = root.getChildren("data");//��xml�л�ö��data��ǩ
            if (datalist.size() == 0) {
                resInt = "-1";
                resStr = "�������ݽ���ʧ�ܣ��������ݸ�ʽ";
                Logger.error("��Ա������ѵ�ӿ��쳣" + xml, null);
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
                        resStr = "idΪ" + trainVO.getGlbdef14() + "�����ݲ���ʧ�ܣ�";
                        Logger.error("��Ա������ѵ�ӿ��쳣" + xml, null);
                        message = message.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?> ").append("<RES><errInt>").append(resInt)
                                .append("</errInt>").append("<errMsg>").append(resStr).append("</errMsg></RES>");
                        return message.toString();
                    }
                } else if ("N".equals(resoultvo.getGlbdef4().toString())) {
                    trainVO.setPk_psndoc_sub(resoultvo.getPk_psndoc_sub());
                    int i = basedao.updateVO(trainVO);
                    if (i != 1) {
                        resInt = "-1";
                        resStr = "idΪ" + trainVO.getGlbdef14() + "�����ݸ���ʧ�ܣ�";
                        Logger.error("��Ա������ѵ�ӿ��쳣" + xml, null);
                        message = message.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?> ").append("<RES><errInt>").append(resInt)
                                .append("</errInt>").append("<errMsg>").append(resStr).append("</errMsg></RES>");
                        return message.toString();
                    }
                } else {
                    resInt = "-1";
                    resStr = "idΪ" + trainVO.getGlbdef14() + "������״̬ΪY,�����ظ��������£�";
                    Logger.error("��Ա������ѵ�ӿ��쳣" + xml, null);
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
            Logger.error("��Ա������ѵ�ӿ��쳣"+e.getMessage(), e);
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
            throw new BusinessException("��Ա���벻��ȷ");
        }
//		String courseSql = "SELECT t.enablestate,t.creationtime,t.modifiedtime,t.creator,s.pk_org as creator_org,t.pk_org as course_org" +
//				" FROM trm_course t left join sm_user s on t.creator = s.cuserid where t.coursename = '"+xmlvalheadlist.get("courseName")+"'";
//		 Map<String,String> courseInfo = (Map<String, String>) queryBS.executeQuery(courseSql,new MapProcessor());

        TrainVO1 trainVO = new TrainVO1();
        trainVO.setGlbdef14(xmlvalheadlist.get("id"));//����
        trainVO.setAttributeValue("glbdef14", xmlvalheadlist.get("id"));
        trainVO.setPk_org(psnInfo.get("pk_org"));
        trainVO.setPk_group(psnInfo.get("pk_group"));
        trainVO.setPk_psndoc(psnInfo.get("pk_psndoc"));
        trainVO.setPk_psnjob(psnInfo.get("pk_psnjob"));
        trainVO.setPk_psnorg(psnInfo.get("pk_psnorg"));
        trainVO.setAct_name(xmlvalheadlist.get("act_name"));//��ѵ�����
        trainVO.setBegindate(new UFLiteralDate(xmlvalheadlist.get("begindate")));//��ѵ��ʼ����
        trainVO.setEnddate(new UFLiteralDate(xmlvalheadlist.get("enddate")));//��ѵ��������
        trainVO.setTra_type(getDefdoc(queryBS,xmlvalheadlist.get("tra_type"),"HR021_0xx"));//��ѵ����
        trainVO.setTra_mode(getDefdoc(queryBS,xmlvalheadlist.get("tra_mode"),"HR020_0xx"));//��ѵ��ʽ
        trainVO.setTraim(xmlvalheadlist.get("traim"));//��ѵĿ��
        trainVO.setTra_content(xmlvalheadlist.get("tra_content"));//��ѵ����
        trainVO.setTrm_location(xmlvalheadlist.get("trm_location"));//��ѵ�ص�
        trainVO.setTra_time(new UFDouble(xmlvalheadlist.get("tra_time")));//��ѵѧʱ
        trainVO.setTra_cost(new UFDouble(xmlvalheadlist.get("tra_cost")));//��ѵ����
        trainVO.setIsallduty(new UFBoolean(xmlvalheadlist.get("isallduty")));//ȫ��
        trainVO.setAbsence_count(Integer.parseInt(xmlvalheadlist.get("absence_count")));//ȱ�ڴ���
        trainVO.setAss_result(new UFDouble(xmlvalheadlist.get("ass_result")));//���˳ɼ�
        trainVO.setAss_option(xmlvalheadlist.get("ass_option"));//��������
        trainVO.setMemo(xmlvalheadlist.get("memo"));//��ע
        trainVO.setGlbdef1(xmlvalheadlist.get("lecturer"));//��ʦ
        trainVO.setAttributeValue("glbdef1", xmlvalheadlist.get("lecturer"));
        trainVO.setGlbdef2(xmlvalheadlist.get("credit"));//ѧ��
        trainVO.setAttributeValue("glbdef2", xmlvalheadlist.get("credit"));
        trainVO.setGlbdef3(xmlvalheadlist.get("courseName"));//�γ�����
        trainVO.setAttributeValue("glbdef3", xmlvalheadlist.get("courseName"));//�γ�����

        trainVO.setCreationtime(new UFDateTime(new Date()));

        trainVO.setCreator("");//����˭��  TODU
        trainVO.setLastflag(new UFBoolean("Y"));//�������һ�μ�¼����Lastflag ����ΪN TODU

        trainVO.setGlbdef4(new UFBoolean(xmlvalheadlist.get("status")));//���״̬
        trainVO.setAttributeValue("glbdef4", xmlvalheadlist.get("status"));
        trainVO.setGlbdef5(xmlvalheadlist.get("course_class"));//�γ̷���
        trainVO.setAttributeValue("glbdef5", xmlvalheadlist.get("course_class"));
        trainVO.setGlbdef6(xmlvalheadlist.get("course_mode"));//ѡ�η�ʽ
        trainVO.setAttributeValue("glbdef6", xmlvalheadlist.get("course_mode"));
        String course_type = xmlvalheadlist.get("course_type");
        if("����ѧϰ".equals(course_type)){
            course_type = "����ѧϰ";
        }
        if("������ѵ".equals(course_type)){
            course_type = "����";
        }
        trainVO.setGlbdef7(course_type);//�γ�����
        trainVO.setAttributeValue("glbdef7", course_type);


        trainVO.setGlbdef8(xmlvalheadlist.get("course_status"));//�γ�״̬
        trainVO.setAttributeValue("glbdef8", xmlvalheadlist.get("course_status"));
        trainVO.setGlbdef9(xmlvalheadlist.get("creationtime"));//��������
        trainVO.setAttributeValue("glbdef9", xmlvalheadlist.get("creationtime"));


        trainVO.setGlbdef10(xmlvalheadlist.get("modifiedtime"));//�޸�����
        trainVO.setAttributeValue("glbdef10", xmlvalheadlist.get("modifiedtime"));

        trainVO.setGlbdef11(xmlvalheadlist.get("creator"));//������
        trainVO.setAttributeValue("glbdef11", xmlvalheadlist.get("creator"));
        trainVO.setGlbdef12(xmlvalheadlist.get("createorg"));//������������֯
        trainVO.setAttributeValue("glbdef12", xmlvalheadlist.get("createorg"));
        trainVO.setGlbdef13(xmlvalheadlist.get("course_org"));//�γ���֯
        trainVO.setAttributeValue("glbdef13", xmlvalheadlist.get("course_org"));
        trainVO.setGlbdef15(xmlvalheadlist.get("lecturer_code"));//��ʦ����
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