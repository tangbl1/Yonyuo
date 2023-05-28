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
	 * 判断保险单是否还有一个月到期
	 * @param 
	 * @return
	 * @throws DAOException 
	 */
	private void isInsuExpire() throws DAOException{
		//用于存放数据（将数据根据单据号进行整理）
		Map<String, List> vehMap = new HashMap<String, List>();
		//用于存放相同单据号的数据的集合
		List vehList = new ArrayList<Object>();
		//得到当前日期共有多少个月（年份*12+月份）
		Integer nowMonth = getIntMonth();
		//查询保险单单据号，车牌号，保险公司，到期日期
		String sql = "select h.billno,v.vehicleno, b.icompany, b.iexpiredate,"
				+ " b.money, b.itype, b.ideadline from cl_insurance h "
				+ "left join cl_vehicle v  on v.pk_vehicle = h.vehicleno "
				+ "left join cl_insurance_b b on h.pk_insurance = "
				+ "b.pk_insurance where h.dr = 0 and b.dr = 0";
		//执行sql
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
		//遍历
		for (Map<String,String> map : list) {
			//得到单据号
			String billno = map.get("billno");
			//判断vehMap中是否有billon这个key
			if(vehMap.containsKey(billno)){
				//如果有，根据key的到value
				vehList = vehMap.get(billno);
			}else{
				//没有新建集合
				vehList = new ArrayList<Object>();
			}
			//将map存入vehList集合
			vehList.add(map);
			//将集合放入vehMap中
			vehMap.put(billno,vehList);
		}
		//提示信息
		String message = "";
		//迭代器遍历
		Iterator it =  vehMap.keySet().iterator();
		while(it.hasNext()){
			String key = it.next().toString();
			//得到value
			List<Map<String,String>> onelist = vehMap.get(key);
			//车牌号
			String vehicleno = onelist.get(0).get("vehicleno");
			//单据号
			String billno = onelist.get(0).get("billno");
			//拼接信息
			message += "\n"+vehicleno + "的保险单即将到期。\n单据号："+billno;
			//遍历value集合
			for (Map<String,String> map : onelist) {
				//保险公司
				String icompany = map.get("icompany");
				//险种
				String itype = map.get("itype");
				//到期日期
				String iexpiredate = map.get("iexpiredate");
				//到期日期的月份
				Integer expireMonth = Integer.parseInt(iexpiredate.substring(5, 7));
				//到期日期的年份
				Integer expireYear = Integer.parseInt(iexpiredate.substring(0, 4));
				//到期日期共有多少月（年份*12+月份）
				Integer subMonth = (expireYear*12+expireMonth) - nowMonth;
				//险种名称
				String itypeName = "";
				if(subMonth == 1){
					if("1".equals(itype)){
						itypeName = "交强险";
					}else if("2".equals(itype)){
						itypeName = "车损险";
					}else if("3".equals(itype)){
						itypeName = "三者险";
					}else if("4".equals(itype)){
						itypeName = "座位险";
					}else if("5".equals(itype)){
						itypeName = "玻璃险";
					}else if("6".equals(itype)){
						itypeName = "无法找到第三方";
					}
					//拼接保险日期
					message += "\n保险公司："+icompany+ ""
							+ "\n险种："+itypeName+"\n到期日期："+iexpiredate.substring(0,10)+"。\n";
				}
			}
		}
		String name = "保险单";
		//发送消息
		sendYonyouMessage(message,name);
	}
	
	/**
	 * 判断检车单是否还有一个月到期
	 * @param 
	 * @return
	 * @throws DAOException 
	 */
	private void isInseExpire() throws DAOException{
		//当前日期共有多少个月
		Integer nowMonth = getIntMonth();
		//查询车牌号，检车单有效期的sql
		String sql = "select h.billno,v.vehicleno,b.iexpiredate from cl_inspection"
				+ " h left join cl_vehicle v on v.pk_vehicle = h.vehicleno left"
				+ " join cl_inspection_b b on h.pk_inspection = b.pk_inspection"
				+ " where h.dr = 0 and b.dr = 0";
		//执行sql
		List<Map<String,String>> list= (List<Map<String, String>>) dao.executeQuery(sql, new  MapListProcessor());
		//提示信息
		String message = "";
		//遍历
		for (Map<String,String> map : list) {
			//检车有效期
			String iexpiredate = map.get("iexpiredate");
			//检车有效期月份
			Integer expireMonth = Integer.parseInt(iexpiredate.substring(5, 7));
			//检车有效期年限
			Integer expireYear = Integer.parseInt(iexpiredate.substring(0, 4));
			//检车有效期共有多少个月
			Integer subMonth = (expireYear*12+expireMonth) - nowMonth;
			//如果检车有效期月数-当前日期月数 <= 3。发送提示信息
			if(subMonth <= 3){
				//车牌号
				String vehicleno = map.get("vehicleno");
				//单据号
				String billno = map.get("billno");
				//拼接提示信息
				message += vehicleno + "的检车单即将到期。\n单据号："+billno+"。\n检车有效期："+iexpiredate.substring(0,10)+"。\n\n";	
			}
		}
		String name = "检车单";
		//发送信息
		sendYonyouMessage(message,name);
	}
	
	
	/**
	 * 得到当前日期的月份（Integer类型）
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
	 * 发送友空间消息
	 * @param String message, String name
	 * @return
	 */
	private boolean sendYonyouMessage( String message ,String name){
		//连接审批的地址
		boolean messageResult = false;
		// 获取access_token
		String accessToken = YonyouMessageUtil.getAccessToken();
		// TODO 接收人需要更改为车管
		String field = "17609814307";
		String fieldtype = "1";
		// 获取MemberId（1：手机 2：邮箱）
		String memberId = YonyouMessageUtil.getMemberId(accessToken, field, fieldtype);
		List<String> tos = new ArrayList<String>();
		tos.add(memberId);
		String warnName = name + "到期提醒";
		messageResult = YonyouMessageUtil.sendMessage(accessToken, YonyouMessageUtil.messagePojo(tos , warnName , message));
		return messageResult;
	}	
}
