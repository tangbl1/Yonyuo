package nc.vo.so.m30.weighbridge;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class Wb01VO extends SuperVO{
	private int xh;
    private String lsh;
    private String ch;
    private String wbtype;
    private String fh_org;
    private String sh_org;
    private String hm;
    private String gg;
    private UFDouble mz;
    private UFDouble pz;
    private UFDouble jz;
    private UFDouble kz;
    private UFDouble sz;
    private UFDouble dj;
    private UFDouble je;
    private UFDouble zfxs;
    private UFDouble fl;
    private UFDouble gbf;
    private String mz_psn;
    private String pz_psn;
    private String mz_no;
    private String pz_no;
    private UFDateTime mz_datetime;
	private UFDateTime pz_datetime;
    private UFDateTime first_datetime;
    private UFDateTime second_datetime;
    private String updater;
    private UFDateTime updatetime;
    private String demo;
    private int prints;
    private String isupload;
    private String info_status;
    private String relog;
    private String def01;
    private String def02;
    private String def03;
    private String def04;
    private String def05;
    private String def06;
    private String def07;
    private String def08;
    private String def09;
    private String def10;
    private String def11;
    private String def12;
    private String def13;
    private String def14;
    private String def15;
    private String def16;
    private String def17;
    private String def18;
    private String def19;
    private String def20;
    private int dr;
    private UFDateTime ts;
    private String isupdate;
    private UFDateTime delivery_time;
   
	public String getIsupdate() {
		return isupdate;
	}

	public void setIsupdate(String isupdate) {
		this.isupdate = isupdate;
	}

	public UFDateTime getDelivery_time() {
		return delivery_time;
	}

	public void setDelivery_time(UFDateTime delivery_time) {
		this.delivery_time = delivery_time;
	}

	public String getPrimaryKey() {
		// TODO 自动生成的方法存根
		return "lsh";
	}

	public String getTableName() {
		// TODO 自动生成的方法存根
		return "weighbridge01";
	}
	public int getXh() {
		return xh;
	}
	public void setXh(int xh) {
		this.xh = xh;
	}
	public String getLsh() {
		return lsh;
	}
	public void setLsh(String lsh) {
		this.lsh = lsh;
	}
	public String getCh() {
		return ch;
	}
	public void setCh(String ch) {
		this.ch = ch;
	}
	public String getWbtype() {
		return wbtype;
	}
	public void setWbtype(String wbtype) {
		this.wbtype = wbtype;
	}
	public String getFh_org() {
		return fh_org;
	}
	public void setFh_org(String fh_org) {
		this.fh_org = fh_org;
	}
	public String getSh_org() {
		return sh_org;
	}
	public void setSh_org(String sh_org) {
		this.sh_org = sh_org;
	}
	public String getHm() {
		return hm;
	}
	public void setHm(String hm) {
		this.hm = hm;
	}
	public String getGg() {
		return gg;
	}
	public void setGg(String gg) {
		this.gg = gg;
	}
	public UFDouble getMz() {
		return mz;
	}
	public void setMz(UFDouble mz) {
		this.mz = mz;
	}
	public UFDouble getPz() {
		return pz;
	}
	public void setPz(UFDouble pz) {
		this.pz = pz;
	}
	public UFDouble getJz() {
		return jz;
	}
	public void setJz(UFDouble jz) {
		this.jz = jz;
	}
	public UFDouble getKz() {
		return kz;
	}
	public void setKz(UFDouble kz) {
		this.kz = kz;
	}
	public UFDouble getSz() {
		return sz;
	}
	public void setSz(UFDouble sz) {
		this.sz = sz;
	}
	public UFDouble getDj() {
		return dj;
	}
	public void setDj(UFDouble dj) {
		this.dj = dj;
	}
	public UFDouble getJe() {
		return je;
	}
	public void setJe(UFDouble je) {
		this.je = je;
	}
	public UFDouble getZfxs() {
		return zfxs;
	}
	public void setZfxs(UFDouble zfxs) {
		this.zfxs = zfxs;
	}
	public UFDouble getFl() {
		return fl;
	}
	public void setFl(UFDouble fl) {
		this.fl = fl;
	}
	public UFDouble getGbf() {
		return gbf;
	}
	public void setGbf(UFDouble gbf) {
		this.gbf = gbf;
	}
	public String getMz_psn() {
		return mz_psn;
	}
	public void setMz_psn(String mz_psn) {
		this.mz_psn = mz_psn;
	}
	public String getPz_psn() {
		return pz_psn;
	}
	public void setPz_psn(String pz_psn) {
		this.pz_psn = pz_psn;
	}
	public String getMz_no() {
		return mz_no;
	}
	public void setMz_no(String mz_no) {
		this.mz_no = mz_no;
	}
	public String getPz_no() {
		return pz_no;
	}
	public void setPz_no(String pz_no) {
		this.pz_no = pz_no;
	}
	public UFDateTime getMz_datetime() {
		return mz_datetime;
	}
	public void setMz_datetime(UFDateTime mz_datetime) {
		this.mz_datetime = mz_datetime;
	}
	public UFDateTime getPz_datetime() {
		return pz_datetime;
	}
	public void setPz_datetime(UFDateTime pz_datetime) {
		this.pz_datetime = pz_datetime;
	}
	public UFDateTime getFirst_datetime() {
		return first_datetime;
	}
	public void setFirst_datetime(UFDateTime first_datetime) {
		this.first_datetime = first_datetime;
	}
	public UFDateTime getSecond_datetime() {
		return second_datetime;
	}
	public void setSecond_datetime(UFDateTime second_datetime) {
		this.second_datetime = second_datetime;
	}
	public String getUpdater() {
		return updater;
	}
	public void setUpdater(String updater) {
		this.updater = updater;
	}
	public UFDateTime getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(UFDateTime updatetime) {
		this.updatetime = updatetime;
	}
	public String getDemo() {
		return demo;
	}
	public void setDemo(String demo) {
		this.demo = demo;
	}
	public int getPrints() {
		return prints;
	}
	public void setPrints(int prints) {
		this.prints = prints;
	}
	public String getIsupload() {
		return isupload;
	}
	public void setIsupload(String isupload) {
		this.isupload = isupload;
	}
	public String getRelog() {
		return relog;
	}
	public void setRelog(String relog) {
		this.relog = relog;
	}
	public String getDef01() {
		return def01;
	}
	public void setDef01(String def01) {
		this.def01 = def01;
	}
	public String getDef02() {
		return def02;
	}
	public void setDef02(String def02) {
		this.def02 = def02;
	}
	public String getDef03() {
		return def03;
	}
	public void setDef03(String def03) {
		this.def03 = def03;
	}
	public String getDef04() {
		return def04;
	}
	public void setDef04(String def04) {
		this.def04 = def04;
	}
	public String getDef05() {
		return def05;
	}
	public void setDef05(String def05) {
		this.def05 = def05;
	}
	public String getDef06() {
		return def06;
	}
	public void setDef06(String def06) {
		this.def06 = def06;
	}
	public String getDef07() {
		return def07;
	}
	public void setDef07(String def07) {
		this.def07 = def07;
	}
	public String getDef08() {
		return def08;
	}
	public void setDef08(String def08) {
		this.def08 = def08;
	}
	public String getDef09() {
		return def09;
	}
	public void setDef09(String def09) {
		this.def09 = def09;
	}
	public String getDef10() {
		return def10;
	}
	public void setDef10(String def10) {
		this.def10 = def10;
	}
	public String getDef11() {
		return def11;
	}
	public void setDef11(String def11) {
		this.def11 = def11;
	}
	public String getDef12() {
		return def12;
	}
	public void setDef12(String def12) {
		this.def12 = def12;
	}
	public String getDef13() {
		return def13;
	}
	public void setDef13(String def13) {
		this.def13 = def13;
	}
	public String getDef14() {
		return def14;
	}
	public void setDef14(String def14) {
		this.def14 = def14;
	}
	public String getDef15() {
		return def15;
	}
	public void setDef15(String def15) {
		this.def15 = def15;
	}
	public String getDef16() {
		return def16;
	}
	public void setDef16(String def16) {
		this.def16 = def16;
	}
	public String getDef17() {
		return def17;
	}
	public void setDef17(String def17) {
		this.def17 = def17;
	}
	public String getDef18() {
		return def18;
	}
	public void setDef18(String def18) {
		this.def18 = def18;
	}
	public String getDef19() {
		return def19;
	}
	public void setDef19(String def19) {
		this.def19 = def19;
	}
	public String getDef20() {
		return def20;
	}
	public void setDef20(String def20) {
		this.def20 = def20;
	}
	public int getDr() {
		return dr;
	}
	public void setDr(int dr) {
		this.dr = dr;
	}
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public String getInfo_status() {
		return info_status;
	}

	public void setInfo_status(String info_status) {
		this.info_status = info_status;
	}
}
