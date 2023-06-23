package nc.vo.so.m30.weighbridge;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

public class LogMsgVO extends SuperVO{
	
	private UFDateTime ts;
	private String pk;
	private String msg;
	private String optype;
	private int dr;
	
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getOptype() {
		return optype;
	}
	public void setOptype(String optype) {
		this.optype = optype;
	}
	public int getDr() {
		return dr;
	}
	public void setDr(int dr) {
		this.dr = dr;
	}
	
	public String getPrimaryKey() {
		// TODO 自动生成的方法存根
		return "pk";
	}

	public String getTableName() {
		// TODO 自动生成的方法存根
		return "weight_log";
	}
	
}
