package nc.vo.so.m30.entity;

import nc.vo.pub.lang.UFDouble;

public class SoCalcPriceVO {
	String pk_org;
	String pk_customer;
	String org_name;
	String customer_name;
	String zbmc;
	UFDouble value;
	String sfgdje;
	String date;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public String getPk_customer() {
		return pk_customer;
	}

	public void setPk_customer(String pk_customer) {
		this.pk_customer = pk_customer;
	}

	public String getZbmc() {
		return zbmc;
	}

	public void setZbmc(String zbmc) {
		this.zbmc = zbmc;
	}

	public UFDouble getValue() {
		return value;
	}

	public void setValue(UFDouble value) {
		this.value = value;
	}

	public String getSfgdje() {
		return sfgdje;
	}

	public void setSfgdje(String sfgdje) {
		this.sfgdje = sfgdje;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

}
