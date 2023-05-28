package nc.vo.vehicle.inspection.approve;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.itf.vehicle.util.YonyouMessageUtil;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;


public class ToApproveBorrowTimeTask implements IBackgroundWorkPlugin {
	
	BaseDAO dao = new BaseDAO();
	
	@Override
	public PreAlertObject executeTask(BgWorkingContext arg0)
			throws BusinessException {
		isInsuExpire();
		isInseExpire();
		return null;
	}
	
	/**
	 * �жϱ��յ��Ƿ���һ���µ���
	 * @param 
	 * @return
	 * @throws DAOException 
	 */
	private void isInsuExpire() throws DAOException{
		//���ڴ�����ݣ������ݸ��ݵ��ݺŽ�������
		Map<String, List> vehMap = new HashMap<String, List>();
		//���ڴ����ͬ���ݺŵ����ݵļ���
		List vehList = new ArrayList<Object>();
		//�õ���ǰ���ڹ��ж��ٸ��£����*12+�·ݣ�
		Integer nowMonth = getIntMonth();
		//��ѯ���յ����ݺţ����ƺţ����չ�˾����������
		String sql = "select h.billno,v.vehicleno, b.icompany, b.iexpiredate,"
				+ " b.money, b.itype, b.ideadline from cl_insurance h "
				+ "left join cl_vehicle v  on v.pk_vehicle = h.vehicleno "
				+ "left join cl_insurance_b b on h.pk_insurance = "
				+ "b.pk_insurance where h.dr = 0 and b.dr = 0";
		//ִ��sql
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
		//����
		for (Map<String,String> map : list) {
			//�õ����ݺ�
			String billno = map.get("billno");
			//�ж�vehMap���Ƿ���billon���key
			if(vehMap.containsKey(billno)){
				//����У�����key�ĵ�value
				vehList = vehMap.get(billno);
			}else{
				//û���½�����
				vehList = new ArrayList<Object>();
			}
			//��map����vehList����
			vehList.add(map);
			//�����Ϸ���vehMap��
			vehMap.put(billno,vehList);
		}
		//��ʾ��Ϣ
		String message = "";
		//����������
		Iterator it =  vehMap.keySet().iterator();
		while(it.hasNext()){
			String key = it.next().toString();
			//�õ�value
			List<Map<String,String>> onelist = vehMap.get(key);
			//���ƺ�
			String vehicleno = onelist.get(0).get("vehicleno");
			//���ݺ�
			String billno = onelist.get(0).get("billno");
			//ƴ����Ϣ
			message += "\n"+vehicleno + "�ı��յ��������ڡ�\n���ݺţ�"+billno;
			//����value����
			for (Map<String,String> map : onelist) {
				//���չ�˾
				String icompany = map.get("icompany");
				//����
				String itype = map.get("itype");
				//��������
				String iexpiredate = map.get("iexpiredate");
				//�������ڵ��·�
				Integer expireMonth = Integer.parseInt(iexpiredate.substring(5, 7));
				//�������ڵ����
				Integer expireYear = Integer.parseInt(iexpiredate.substring(0, 4));
				//�������ڹ��ж����£����*12+�·ݣ�
				Integer subMonth = (expireYear*12+expireMonth) - nowMonth;
				//��������
				String itypeName = "";
				if(subMonth == 1){
					if("1".equals(itype)){
						itypeName = "��ǿ��";
					}else if("2".equals(itype)){
						itypeName = "������";
					}else if("3".equals(itype)){
						itypeName = "������";
					}else if("4".equals(itype)){
						itypeName = "��λ��";
					}else if("5".equals(itype)){
						itypeName = "������";
					}else if("6".equals(itype)){
						itypeName = "�޷��ҵ�������";
					}
					//ƴ�ӱ�������
					message += "\n���չ�˾��"+icompany+ ""
							+ "\n���֣�"+itypeName+"\n�������ڣ�"+iexpiredate.substring(0,10)+"��\n";
				}
			}
		}
		String name = "���յ�";
		//������Ϣ
		sendYonyouMessage(message,name);
	}
	
	/**
	 * �жϼ쳵���Ƿ���һ���µ���
	 * @param 
	 * @return
	 * @throws DAOException 
	 */
	private void isInseExpire() throws DAOException{
		//��ǰ���ڹ��ж��ٸ���
		Integer nowMonth = getIntMonth();
		//��ѯ���ƺţ��쳵����Ч�ڵ�sql
		String sql = "select h.billno,v.vehicleno,b.iexpiredate from cl_inspection"
				+ " h left join cl_vehicle v on v.pk_vehicle = h.vehicleno left"
				+ " join cl_inspection_b b on h.pk_inspection = b.pk_inspection"
				+ " where h.dr = 0 and b.dr = 0";
		//ִ��sql
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
		//��ʾ��Ϣ
		String message = "";
		//����
		for (Map<String,String> map : list) {
			//�쳵��Ч��
			String iexpiredate = map.get("iexpiredate");
			//�쳵��Ч���·�
			Integer expireMonth = Integer.parseInt(iexpiredate.substring(5, 7));
			//�쳵��Ч������
			Integer expireYear = Integer.parseInt(iexpiredate.substring(0, 4));
			//�쳵��Ч�ڹ��ж��ٸ���
			Integer subMonth = (expireYear*12+expireMonth) - nowMonth;
			//����쳵��Ч������-��ǰ�������� <= 3��������ʾ��Ϣ
			if(subMonth <= 3){
				//���ƺ�
				String vehicleno = map.get("vehicleno");
				//���ݺ�
				String billno = map.get("billno");
				//ƴ����ʾ��Ϣ
				message += vehicleno + "�ļ쳵���������ڡ�\n���ݺţ�"+billno+"��\n�쳵��Ч�ڣ�"+iexpiredate.substring(0,10)+"��\n\n";	
			}
		}
		String name = "�쳵��";
		//������Ϣ
		sendYonyouMessage(message,name);
	}
	
	
	/**
	 * �õ���ǰ���ڵ��·ݣ�Integer���ͣ�
	 * @param 
	 * @return Integer 
	 */
	private Integer getIntMonth() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String month = (dateFormat.format(date)).substring(5, 7);
		String year = (dateFormat.format(date)).substring(0, 4);
		Integer newMonth = Integer.parseInt(month);
		Integer newYear = Integer.parseInt(year);
		return newYear*12+newMonth;
	}
	
	/**
	 * �����ѿռ���Ϣ
	 * @param String message, String name
	 * @return
	 */
	private boolean sendYonyouMessage( String message ,String name){
		//���������ĵ�ַ
		boolean messageResult = false;
		// ��ȡaccess_token
		String accessToken = YonyouMessageUtil.getAccessToken();
		// TODO ��������Ҫ����Ϊ����
		String field = "17609814307";
		String fieldtype = "1";
		// ��ȡMemberId��1���ֻ� 2�����䣩
		String memberId = YonyouMessageUtil.getMemberId(accessToken, field, fieldtype);
		List<String> tos = new ArrayList<String>();
		tos.add(memberId);
		String warnName = name + "��������";
		messageResult = YonyouMessageUtil.sendMessage(accessToken, YonyouMessageUtil.messagePojo(tos , warnName , message));
		return messageResult;
	}	
}
