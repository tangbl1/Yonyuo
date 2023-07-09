//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package nc.vo.so.m30.entity;

import nc.vo.bd.feature.ffile.entity.AggFFileVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.*;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

public class SaleOrderBVO extends SuperVO {
    private AggFFileVO aggffilevo;
    public static final String CMFFILEID = "cmffileid";
    public static final String NMFFILEPRICE = "nmffileprice";
    public static final String BLRGCASHFLAG = "blrgcashflag";
    public static final String RMCONTRACTBFLAG = "rmcontractbflag";
    public static final String NACCPRICE = "naccprice";
    public static final String CCUSTMATERIALID = "ccustmaterialid";
    public static final String CBUYLARGESSACTID = "cbuylargessactid";
    public static final String CPRICEPROMTACTID = "cpricepromtactid";
    public static final String CBUYLARGESSID = "cbuylargessid";
    public static final String BARRANGEDFLAG = "barrangedflag";
    public static final String BBARSETTLEFLAG = "bbarsettleflag";
    public static final String BBCOSTSETTLEFLAG = "bbcostsettleflag";
    public static final String BBINDFLAG = "bbindflag";
    public static final String BBINVOICENDFLAG = "bbinvoicendflag";
    public static final String BBOUTENDFLAG = "bboutendflag";
    public static final String BBSENDENDFLAG = "bbsendendflag";
    public static final String BBSETTLEENDFLAG = "bbsettleendflag";
    public static final String BDISCOUNTFLAG = "bdiscountflag";
    public static final String BJCZXSFLAG = "bjczxsflag";
    public static final String BLABORFLAG = "blaborflag";
    public static final String BLARGESSFLAG = "blargessflag";
    public static final String BPREROWCLOSEFLAG = "bprerowcloseflag";
    public static final String BTRIATRADEFLAG = "btriatradeflag";
    public static final String CARORGID = "carorgid";
    public static final String CARORGVID = "carorgvid";
    public static final String CARRANGEPERSONID = "carrangepersonid";
    public static final String CASTUNITID = "castunitid";
    public static final String CBINDSRCID = "cbindsrcid";
    public static final String CBRANDID = "cbrandid";
    public static final String CCTMANAGEBID = "cctmanagebid";
    public static final String CCTMANAGEID = "cctmanageid";
    public static final String CCURRENCYID = "ccurrencyid";
    public static final String CEXCHANGESRCRETID = "cexchangesrcretid";
    public static final String CFACTORYID = "cfactoryid";
    public static final String CFIRSTBID = "cfirstbid";
    public static final String CFIRSTID = "cfirstid";
    public static final String CLARGESSSRCID = "clargesssrcid";
    public static final String CMATERIALID = "cmaterialid";
    public static final String CMATERIALVID = "cmaterialvid";
    public static final String CORIGAREAID = "corigareaid";
    public static final String CORIGCOUNTRYID = "corigcountryid";
    public static final String CPRICEFORMID = "cpriceformid";
    public static final String CPRICEITEMID = "cpriceitemid";
    public static final String CPRICEITEMTABLEID = "cpriceitemtableid";
    public static final String CPRICEPOLICYID = "cpricepolicyid";
    public static final String CPRODLINEID = "cprodlineid";
    public static final String CPRODUCTORID = "cproductorid";
    public static final String CPROFITCENTERID = "cprofitcenterid";
    public static final String CPROFITCENTERVID = "cprofitcentervid";
    public static final String CPROJECTID = "cprojectid";
    public static final String CQTUNITID = "cqtunitid";
    public static final String CQUALITYLEVELID = "cqualitylevelid";
    public static final String CRECECOUNTRYID = "crececountryid";
    public static final String CRECEIVEADDDOCID = "creceiveadddocid";
    public static final String CRECEIVEADDRID = "creceiveaddrid";
    public static final String CRECEIVEAREAID = "creceiveareaid";
    public static final String CRECEIVECUSTID = "creceivecustid";
    public static final String CRECEIVECUSTVID = "creceivecustvid";
    public static final String CRETPOLICYID = "cretpolicyid";
    public static final String CRETREASONID = "cretreasonid";
    public static final String CROWNO = "crowno";
    public static final String CSALEORDERBID = "csaleorderbid";
    public static final String CSALEORDERID = "csaleorderid";
    public static final String CSENDCOUNTRYID = "csendcountryid";
    public static final String CSENDSTOCKORGID = "csendstockorgid";
    public static final String CSENDSTOCKORGVID = "csendstockorgvid";
    public static final String CSENDSTORDOCID = "csendstordocid";
    public static final String CSETTLEORGID = "csettleorgid";
    public static final String CSETTLEORGVID = "csettleorgvid";
    public static final String CSRCBID = "csrcbid";
    public static final String CSRCID = "csrcid";
    public static final String CTAXCODEID = "ctaxcodeid";
    public static final String CTAXCOUNTRYID = "ctaxcountryid";
    public static final String CTRAFFICORGID = "ctrafficorgid";
    public static final String CTRAFFICORGVID = "ctrafficorgvid";
    public static final String CUNITID = "cunitid";
    public static final String CVENDORID = "cvendorid";
    public static final String CVENDORVID = "cvendorvid";
    public static final String DBILLDATE = "dbilldate";
    public static final String DR = "dr";
    public static final String DRECEIVEDATE = "dreceivedate";
    public static final String DSENDDATE = "dsenddate";
    public static final String FBUYSELLFLAG = "fbuysellflag";
    public static final String FLARGESSTYPEFLAG = "flargesstypeflag";
    public static final String FRETEXCHANGE = "fretexchange";
    public static final String FROWSTATUS = "frowstatus";
    public static final String FTAXTYPEFLAG = "ftaxtypeflag";
    public static final String METAPATH = "so_saleorder_b.";
    public static final String NARRANGEMONUM = "narrangemonum";
    public static final String NARRANGEPOAPPNUM = "narrangepoappnum";
    public static final String NARRANGEPONUM = "narrangeponum";
    public static final String NARRANGESCORNUM = "narrangescornum";
    public static final String NARRANGETOAPPNUM = "narrangetoappnum";
    public static final String NARRANGETOORNUM = "narrangetoornum";
    public static final String NARRANGEITCNUM = "narrangeitcnum";
    public static final String NASKQTORIGNETPRICE = "naskqtorignetprice";
    public static final String NASKQTORIGPRICE = "naskqtorigprice";
    public static final String NASKQTORIGTAXPRC = "naskqtorigtaxprc";
    public static final String NASKQTORIGTXNTPRC = "naskqtorigtxntprc";
    public static final String NASTNUM = "nastnum";
    public static final String NBFORIGSUBMNY = "nbforigsubmny";
    public static final String NCALTAXMNY = "ncaltaxmny";
    public static final String DEFERRALMNY = "deferralmny";
    public static final String REVCONFIRMMNY = "revconfirmmny";
    public static final String NDISCOUNT = "ndiscount";
    public static final String NDISCOUNTRATE = "ndiscountrate";
    public static final String NEXCHANGERATE = "nexchangerate";
    public static final String CRATETYPE = "cratetype";
    public static final String FRATECATEGORY = "fratecategory";
    public static final String DRATEDATE = "dratedate";
    public static final String NGLOBALEXCHGRATE = "nglobalexchgrate";
    public static final String NGLOBALMNY = "nglobalmny";
    public static final String NGLOBALTAXMNY = "nglobaltaxmny";
    public static final String NGROUPEXCHGRATE = "ngroupexchgrate";
    public static final String NGROUPMNY = "ngroupmny";
    public static final String NGROUPTAXMNY = "ngrouptaxmny";
    public static final String NINVOICEAUDITNUM = "ninvoiceauditnum";
    public static final String NINVUNFINISEDNUM = "ninvunfinisednum";
    public static final String NITEMDISCOUNTRATE = "nitemdiscountrate";
    public static final String NLARGESSMNY = "nlargessmny";
    public static final String NLARGESSTAXMNY = "nlargesstaxmny";
    public static final String NLOSSNOTAUDITNUM = "nlossnotauditnum";
    public static final String NMNY = "nmny";
    public static final String NNETPRICE = "nnetprice";
    public static final String NNOTARNUM = "nnotarnum";
    public static final String NNOTCOSTNUM = "nnotcostnum";
    public static final String NNUM = "nnum";
    public static final String NORIGDISCOUNT = "norigdiscount";
    public static final String NORIGMNY = "norigmny";
    public static final String NORIGNETPRICE = "norignetprice";
    public static final String NORIGPRICE = "norigprice";
    public static final String NORIGSUBMNY = "norigsubmny";
    public static final String NTOTALREDNUM = "ntotalrednum";
    public static final String NREDORIGSUBMNY = "nredorigsubmny";
    public static final String NORIGTAXMNY = "norigtaxmny";
    public static final String NORIGTAXNETPRICE = "norigtaxnetprice";
    public static final String NORIGTAXPRICE = "norigtaxprice";
    public static final String NOUTAUDITNUM = "noutauditnum";
    public static final String NOUTNOTAUDITNUM = "noutnotauditnum";
    public static final String NOUTUNFINISEDNUM = "noutunfinisednum";
    public static final String NPIECE = "npiece";
    public static final String NPRICE = "nprice";
    public static final String NQTNETPRICE = "nqtnetprice";
    public static final String NQTORIGNETPRICE = "nqtorignetprice";
    public static final String NQTORIGPRICE = "nqtorigprice";
    public static final String NQTORIGTAXNETPRC = "nqtorigtaxnetprc";
    public static final String NQTORIGTAXPRICE = "nqtorigtaxprice";
    public static final String NQTPRICE = "nqtprice";
    public static final String NQTTAXNETPRICE = "nqttaxnetprice";
    public static final String NQTTAXPRICE = "nqttaxprice";
    public static final String NQTUNITNUM = "nqtunitnum";
    public static final String NREQRSNUM = "nreqrsnum";
    public static final String NSENDAUDITNUM = "nsendauditnum";
    public static final String NSENDUNFINISEDNUM = "nsendunfinisednum";
    public static final String NTAX = "ntax";
    public static final String NTAXMNY = "ntaxmny";
    public static final String NTAXNETPRICE = "ntaxnetprice";
    public static final String NTAXPRICE = "ntaxprice";
    public static final String NTAXRATE = "ntaxrate";
    public static final String NTOTALARMNY = "ntotalarmny";
    public static final String NTOTALARNUM = "ntotalarnum";
    public static final String NTOTALCOSTNUM = "ntotalcostnum";
    public static final String NTOTALESTARMNY = "ntotalestarmny";
    public static final String NTOTALESTARNUM = "ntotalestarnum";
    public static final String NTOTALINVOICENUM = "ntotalinvoicenum";
    public static final String NTOTALNOTOUTNUM = "ntotalnotoutnum";
    public static final String NTOTALOUTNUM = "ntotaloutnum";
    public static final String NTOTALPAYMNY = "ntotalpaymny";
    public static final String NTOTALPLONUM = "ntotalplonum";
    public static final String NTOTALRETURNNUM = "ntotalreturnnum";
    public static final String NTOTALRUSHNUM = "ntotalrushnum";
    public static final String NTOTALSENDNUM = "ntotalsendnum";
    public static final String NTOTALSIGNNUM = "ntotalsignnum";
    public static final String NTOTALTRADENUM = "ntotaltradenum";
    public static final String NTRANSLOSSNUM = "ntranslossnum";
    public static final String NVOLUME = "nvolume";
    public static final String NWEIGHT = "nweight";
    public static final String PK_BATCHCODE = "pk_batchcode";
    public static final String PK_GROUP = "pk_group";
    public static final String PK_ORG = "pk_org";
    public static final String SRCBTS = "srcbts";
    public static final String SRCORGID = "srcorgid";
    public static final String SRCTS = "srcts";
    public static final String TLASTARRANGETIME = "tlastarrangetime";
    public static final String TS = "ts";
    public static final String VBATCHCODE = "vbatchcode";
    public static final String VBDEF1 = "vbdef1";
    public static final String VBDEF10 = "vbdef10";
    public static final String VBDEF11 = "vbdef11";
    public static final String VBDEF12 = "vbdef12";
    public static final String VBDEF13 = "vbdef13";
    public static final String VBDEF14 = "vbdef14";
    public static final String VBDEF15 = "vbdef15";
    public static final String VBDEF16 = "vbdef16";
    public static final String VBDEF17 = "vbdef17";
    public static final String VBDEF18 = "vbdef18";
    public static final String VBDEF19 = "vbdef19";
    public static final String VBDEF2 = "vbdef2";
    public static final String VBDEF20 = "vbdef20";
    public static final String VBDEF3 = "vbdef3";
    public static final String VBDEF4 = "vbdef4";
    public static final String VBDEF5 = "vbdef5";
    public static final String VBDEF6 = "vbdef6";
    public static final String VBDEF7 = "vbdef7";
    public static final String VBDEF8 = "vbdef8";
    public static final String VBDEF9 = "vbdef9";
    public static final String VBDEF21 = "vbdef21";
    public static final String VBDEF22 = "vbdef22";
    public static final String VBDEF23 = "vbdef23";
    public static final String VBDEF24 = "vbdef24";
    public static final String VBDEF25 = "vbdef25";
    public static final String VBDEF26 = "vbdef26";
    public static final String VBDEF27 = "vbdef27";
    public static final String VBDEF28 = "vbdef28";
    public static final String VBDEF29 = "vbdef29";
    public static final String VBDEF30 = "vbdef30";
    public static final String VBDEF31 = "vbdef31";
    public static final String VBDEF32 = "vbdef32";
    public static final String VBDEF33 = "vbdef33";
    public static final String VBDEF34 = "vbdef34";
    public static final String VBDEF35 = "vbdef35";
    public static final String VBDEF36 = "vbdef36";
    public static final String VBDEF37 = "vbdef37";
    public static final String VBDEF38 = "vbdef38";
    public static final String VBDEF39 = "vbdef39";
    public static final String VBDEF40 = "vbdef40";
    public static final String VBDEF41 = "vbdef41";
    public static final String VBDEF42 = "vbdef42";
    public static final String VBDEF43 = "vbdef43";
    public static final String VBDEF44 = "vbdef44";
    public static final String VBDEF45 = "vbdef45";
    public static final String VBDEF46 = "vbdef46";
    public static final String VBDEF47 = "vbdef47";
    public static final String VBDEF48 = "vbdef48";
    public static final String VBDEF49 = "vbdef49";
    public static final String VBDEF50 = "vbdef50";
    public static final String VBREVISEREASON = "vbrevisereason";
    public static final String VCHANGERATE = "vchangerate";
    public static final String VCLOSEREASON = "vclosereason";
    public static final String VCTCODE = "vctcode";
    public static final String VCTTYPE = "vcttype";
    public static final String VFIRSTCODE = "vfirstcode";
    public static final String VFIRSTROWNO = "vfirstrowno";
    public static final String VFIRSTTRANTYPE = "vfirsttrantype";
    public static final String VFIRSTTYPE = "vfirsttype";
    public static final String VFREE1 = "vfree1";
    public static final String VFREE10 = "vfree10";
    public static final String VFREE2 = "vfree2";
    public static final String VFREE3 = "vfree3";
    public static final String VFREE4 = "vfree4";
    public static final String VFREE5 = "vfree5";
    public static final String VFREE6 = "vfree6";
    public static final String VFREE7 = "vfree7";
    public static final String VFREE8 = "vfree8";
    public static final String VFREE9 = "vfree9";
    public static final String VQTUNITRATE = "vqtunitrate";
    public static final String VRETURNMODE = "vreturnmode";
    public static final String VROWNOTE = "vrownote";
    public static final String VSRCCODE = "vsrccode";
    public static final String VSRCROWNO = "vsrcrowno";
    public static final String VSRCTRANTYPE = "vsrctrantype";
    public static final String VSRCTYPE = "vsrctype";
    public static final String CBUYPROMOTTYPEID = "cbuypromottypeid";
    public static final String CPRCPROMOTTYPEID = "cprcpromottypeid";
    public static final String VCUSTOMBILLCODE = "vcustombillcode";
    public static final String CSPROFITCENTERID = "csprofitcenterid";
    public static final String CSPROFITCENTERVID = "csprofitcentervid";
    public static final String CPROMOTPRICEID = "cpromotpriceid";
    private static final long serialVersionUID = -8352354807354004228L;
    public static final String CEXTSRCBILLTYPE = "cextsrcbilltype";
    public static final String CEXTSRCID = "cextsrcid";
    public static final String CEXTSRCBID = "cextsrcbid";
    public static final String VEXTSRCBILLCODE = "vextsrcbillcode";

    /**
     * add
     * begin
     **/
    public  UFDouble factorygrade_sio2;
    public UFDouble  factorygrade_tio2;
    public UFDouble factorygrade_tfe;
    public UFDouble factorygrad_h2o;
    public UFDouble factorygrad_p;
    public UFDouble factorygrade_s;
    public UFDouble arrivalgrade_s;
    public UFDouble arrivalgrade_sio2;
    public UFDouble arrivalgrade_tio2;
    public UFDouble arrivalgrade_tfe;
    public UFDouble arrivalgrade_h2o;
    public UFDouble arrivalgrade_p;
    public UFDouble factorynnum;
    public UFDouble checkjtime;
    public UFDouble vehicleno;
    public UFDouble firstreceiptnum;
    public UFDouble admissiondate;
    public UFDouble sedreceiptnum;
    public UFDouble batchno;
    public UFDouble unitprice;

    public UFDouble getFactorygrade_sio2() {
        return factorygrade_sio2;
    }

    public void setFactorygrade_sio2(UFDouble factorygrade_sio2) {
        this.factorygrade_sio2 = factorygrade_sio2;
    }

    public UFDouble getFactorygrade_tio2() {
        return factorygrade_tio2;
    }

    public void setFactorygrade_tio2(UFDouble factorygrade_tio2) {
        this.factorygrade_tio2 = factorygrade_tio2;
    }

    public UFDouble getFactorygrade_tfe() {
        return factorygrade_tfe;
    }

    public void setFactorygrade_tfe(UFDouble factorygrade_tfe) {
        this.factorygrade_tfe = factorygrade_tfe;
    }

    public UFDouble getFactorygrad_h2o() {
        return factorygrad_h2o;
    }

    public void setFactorygrad_h2o(UFDouble factorygrad_h2o) {
        this.factorygrad_h2o = factorygrad_h2o;
    }

    public UFDouble getFactorygrad_p() {
        return factorygrad_p;
    }

    public void setFactorygrad_p(UFDouble factorygrad_p) {
        this.factorygrad_p = factorygrad_p;
    }

    public UFDouble getFactorygrade_s() {
        return factorygrade_s;
    }

    public void setFactorygrade_s(UFDouble factorygrade_s) {
        this.factorygrade_s = factorygrade_s;
    }

    public UFDouble getArrivalgrade_s() {
        return arrivalgrade_s;
    }

    public void setArrivalgrade_s(UFDouble arrivalgrade_s) {
        this.arrivalgrade_s = arrivalgrade_s;
    }

    public UFDouble getArrivalgrade_sio2() {
        return arrivalgrade_sio2;
    }

    public void setArrivalgrade_sio2(UFDouble arrivalgrade_sio2) {
        this.arrivalgrade_sio2 = arrivalgrade_sio2;
    }

    public UFDouble getArrivalgrade_tio2() {
        return arrivalgrade_tio2;
    }

    public void setArrivalgrade_tio2(UFDouble arrivalgrade_tio2) {
        this.arrivalgrade_tio2 = arrivalgrade_tio2;
    }

    public UFDouble getArrivalgrade_tfe() {
        return arrivalgrade_tfe;
    }

    public void setArrivalgrade_tfe(UFDouble arrivalgrade_tfe) {
        this.arrivalgrade_tfe = arrivalgrade_tfe;
    }

    public UFDouble getArrivalgrade_h2o() {
        return arrivalgrade_h2o;
    }

    public void setArrivalgrade_h2o(UFDouble arrivalgrade_h2o) {
        this.arrivalgrade_h2o = arrivalgrade_h2o;
    }

    public UFDouble getArrivalgrade_p() {
        return arrivalgrade_p;
    }

    public void setArrivalgrade_p(UFDouble arrivalgrade_p) {
        this.arrivalgrade_p = arrivalgrade_p;
    }

    public UFDouble getFactorynnum() {
        return factorynnum;
    }

    public void setFactorynnum(UFDouble factorynnum) {
        this.factorynnum = factorynnum;
    }

    public UFDouble getCheckjtime() {
        return checkjtime;
    }

    public void setCheckjtime(UFDouble checkjtime) {
        this.checkjtime = checkjtime;
    }

    public UFDouble getVehicleno() {
        return vehicleno;
    }

    public void setVehicleno(UFDouble vehicleno) {
        this.vehicleno = vehicleno;
    }

    public UFDouble getFirstreceiptnum() {
        return firstreceiptnum;
    }

    public void setFirstreceiptnum(UFDouble firstreceiptnum) {
        this.firstreceiptnum = firstreceiptnum;
    }

    public UFDouble getAdmissiondate() {
        return admissiondate;
    }

    public void setAdmissiondate(UFDouble admissiondate) {
        this.admissiondate = admissiondate;
    }

    public UFDouble getSedreceiptnum() {
        return sedreceiptnum;
    }

    public void setSedreceiptnum(UFDouble sedreceiptnum) {
        this.sedreceiptnum = sedreceiptnum;
    }

    public UFDouble getBatchno() {
        return batchno;
    }

    public void setBatchno(UFDouble batchno) {
        this.batchno = batchno;
    }

    public UFDouble getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(UFDouble unitprice) {
        this.unitprice = unitprice;
    }

    /**
     * end
     **/


    public SaleOrderBVO() {
    }

    public UFDouble getNaccprice() {
        return (UFDouble) this.getAttributeValue("naccprice");
    }

    public void setNaccprice(UFDouble naccprice) {
        this.setAttributeValue("naccprice", naccprice);
    }

    public UFBoolean getBlrgcashflag() {
        return (UFBoolean) this.getAttributeValue("blrgcashflag");
    }

    public UFBoolean getRmcontractbflag() {
        return (UFBoolean) this.getAttributeValue("rmcontractbflag");
    }

    public void setRmcontractbflag(UFBoolean blrgcashflag) {
        this.setAttributeValue("rmcontractbflag", blrgcashflag);
    }

    public void setBlrgcashflag(UFBoolean blrgcashflag) {
        this.setAttributeValue("blrgcashflag", blrgcashflag);
    }

    public void setCcustmaterialid(String ccustmaterialid) {
        this.setAttributeValue("ccustmaterialid", ccustmaterialid);
    }

    public String getCcustmaterialid() {
        return (String) this.getAttributeValue("ccustmaterialid");
    }

    public String getCbuylargessactid() {
        return (String) this.getAttributeValue("cbuylargessactid");
    }

    public String getCpricepromtactid() {
        return (String) this.getAttributeValue("cpricepromtactid");
    }

    public String getCbuylargessid() {
        return (String) this.getAttributeValue("cbuylargessid");
    }

    public UFBoolean getBarrangedflag() {
        return (UFBoolean) this.getAttributeValue("barrangedflag");
    }

    public UFBoolean getBbarsettleflag() {
        return (UFBoolean) this.getAttributeValue("bbarsettleflag");
    }

    public UFBoolean getBbcostsettleflag() {
        return (UFBoolean) this.getAttributeValue("bbcostsettleflag");
    }

    public UFBoolean getBbindflag() {
        return (UFBoolean) this.getAttributeValue("bbindflag");
    }

    public UFBoolean getBbinvoicendflag() {
        return (UFBoolean) this.getAttributeValue("bbinvoicendflag");
    }

    public UFBoolean getBboutendflag() {
        return (UFBoolean) this.getAttributeValue("bboutendflag");
    }

    public UFBoolean getBbsendendflag() {
        return (UFBoolean) this.getAttributeValue("bbsendendflag");
    }

    public UFBoolean getBbsettleendflag() {
        return (UFBoolean) this.getAttributeValue("bbsettleendflag");
    }

    public UFBoolean getBdiscountflag() {
        return (UFBoolean) this.getAttributeValue("bdiscountflag");
    }

    public UFBoolean getBjczxsflag() {
        return (UFBoolean) this.getAttributeValue("bjczxsflag");
    }

    public UFBoolean getBlaborflag() {
        return (UFBoolean) this.getAttributeValue("blaborflag");
    }

    public UFBoolean getBlargessflag() {
        return (UFBoolean) this.getAttributeValue("blargessflag");
    }

    public UFBoolean getBprerowcloseflag() {
        return (UFBoolean) this.getAttributeValue("bprerowcloseflag");
    }

    public UFBoolean getBtriatradeflag() {
        return (UFBoolean) this.getAttributeValue("btriatradeflag");
    }

    public String getCarorgid() {
        return (String) this.getAttributeValue("carorgid");
    }

    public String getCarorgvid() {
        return (String) this.getAttributeValue("carorgvid");
    }

    public String getCarrangepersonid() {
        return (String) this.getAttributeValue("carrangepersonid");
    }

    public String getCastunitid() {
        return (String) this.getAttributeValue("castunitid");
    }

    public String getCbindsrcid() {
        return (String) this.getAttributeValue("cbindsrcid");
    }

    public String getCbrandid() {
        return (String) this.getAttributeValue("cbrandid");
    }

    public String getCctmanagebid() {
        return (String) this.getAttributeValue("cctmanagebid");
    }

    public String getCctmanageid() {
        return (String) this.getAttributeValue("cctmanageid");
    }

    public String getCcurrencyid() {
        return (String) this.getAttributeValue("ccurrencyid");
    }

    public String getCexchangesrcretid() {
        return (String) this.getAttributeValue("cexchangesrcretid");
    }

    public String getCfactoryid() {
        return (String) this.getAttributeValue("cfactoryid");
    }

    public String getCfirstbid() {
        return (String) this.getAttributeValue("cfirstbid");
    }

    public String getCfirstid() {
        return (String) this.getAttributeValue("cfirstid");
    }

    public String getClargesssrcid() {
        return (String) this.getAttributeValue("clargesssrcid");
    }

    public String getCmaterialid() {
        return (String) this.getAttributeValue("cmaterialid");
    }

    public String getCmaterialvid() {
        return (String) this.getAttributeValue("cmaterialvid");
    }

    public String getCorigareaid() {
        return (String) this.getAttributeValue("corigareaid");
    }

    public String getCorigcountryid() {
        return (String) this.getAttributeValue("corigcountryid");
    }

    public String getCpriceformid() {
        return (String) this.getAttributeValue("cpriceformid");
    }

    public String getCpriceitemid() {
        return (String) this.getAttributeValue("cpriceitemid");
    }

    public String getCpriceitemtableid() {
        return (String) this.getAttributeValue("cpriceitemtableid");
    }

    public String getCpricepolicyid() {
        return (String) this.getAttributeValue("cpricepolicyid");
    }

    public String getCprodlineid() {
        return (String) this.getAttributeValue("cprodlineid");
    }

    public String getCproductorid() {
        return (String) this.getAttributeValue("cproductorid");
    }

    public String getCprofitcenterid() {
        return (String) this.getAttributeValue("cprofitcenterid");
    }

    public String getCprofitcentervid() {
        return (String) this.getAttributeValue("cprofitcentervid");
    }

    public String getCprojectid() {
        return (String) this.getAttributeValue("cprojectid");
    }

    public String getCqtunitid() {
        return (String) this.getAttributeValue("cqtunitid");
    }

    public String getCqualitylevelid() {
        return (String) this.getAttributeValue("cqualitylevelid");
    }

    public String getCrececountryid() {
        return (String) this.getAttributeValue("crececountryid");
    }

    public String getCreceiveadddocid() {
        return (String) this.getAttributeValue("creceiveadddocid");
    }

    public String getCreceiveaddrid() {
        return (String) this.getAttributeValue("creceiveaddrid");
    }

    public String getCreceiveareaid() {
        return (String) this.getAttributeValue("creceiveareaid");
    }

    public String getCreceivecustid() {
        return (String) this.getAttributeValue("creceivecustid");
    }

    public String getCreceivecustvid() {
        return (String) this.getAttributeValue("creceivecustvid");
    }

    public String getCretpolicyid() {
        return (String) this.getAttributeValue("cretpolicyid");
    }

    public String getCretreasonid() {
        return (String) this.getAttributeValue("cretreasonid");
    }

    public String getCrowno() {
        return (String) this.getAttributeValue("crowno");
    }

    public String getCsaleorderbid() {
        return (String) this.getAttributeValue("csaleorderbid");
    }

    public String getCsaleorderid() {
        return (String) this.getAttributeValue("csaleorderid");
    }

    public String getCsendcountryid() {
        return (String) this.getAttributeValue("csendcountryid");
    }

    public String getCsendstockorgid() {
        return (String) this.getAttributeValue("csendstockorgid");
    }

    public String getCsendstockorgvid() {
        return (String) this.getAttributeValue("csendstockorgvid");
    }

    public String getCsendstordocid() {
        return (String) this.getAttributeValue("csendstordocid");
    }

    public String getCsettleorgid() {
        return (String) this.getAttributeValue("csettleorgid");
    }

    public String getCsettleorgvid() {
        return (String) this.getAttributeValue("csettleorgvid");
    }

    public String getCsrcbid() {
        return (String) this.getAttributeValue("csrcbid");
    }

    public String getCsrcid() {
        return (String) this.getAttributeValue("csrcid");
    }

    public String getCtaxcodeid() {
        return (String) this.getAttributeValue("ctaxcodeid");
    }

    public String getCtaxcountryid() {
        return (String) this.getAttributeValue("ctaxcountryid");
    }

    public String getCtrafficorgid() {
        return (String) this.getAttributeValue("ctrafficorgid");
    }

    public String getCtrafficorgvid() {
        return (String) this.getAttributeValue("ctrafficorgvid");
    }

    public String getCunitid() {
        return (String) this.getAttributeValue("cunitid");
    }

    public String getCvendorid() {
        return (String) this.getAttributeValue("cvendorid");
    }

    public String getCvendorvid() {
        return (String) this.getAttributeValue("cvendorvid");
    }

    public String getCratetype() {
        return (String) this.getAttributeValue("cratetype");
    }

    public Integer getFratecategory() {
        return (Integer) this.getAttributeValue("fratecategory");
    }

    public UFLiteralDate getDratedate() {
        return (UFLiteralDate) this.getAttributeValue("dratedate");
    }

    public UFDate getDbilldate() {
        return (UFDate) this.getAttributeValue("dbilldate");
    }

    public Integer getDr() {
        return (Integer) this.getAttributeValue("dr");
    }

    public UFDate getDreceivedate() {
        return (UFDate) this.getAttributeValue("dreceivedate");
    }

    public UFDate getDsenddate() {
        return (UFDate) this.getAttributeValue("dsenddate");
    }

    public Integer getFbuysellflag() {
        return (Integer) this.getAttributeValue("fbuysellflag");
    }

    public Integer getFlargesstypeflag() {
        return (Integer) this.getAttributeValue("flargesstypeflag");
    }

    public Integer getFretexchange() {
        return (Integer) this.getAttributeValue("fretexchange");
    }

    public Integer getFrowstatus() {
        return (Integer) this.getAttributeValue("frowstatus");
    }

    public Integer getFtaxtypeflag() {
        return (Integer) this.getAttributeValue("ftaxtypeflag");
    }

    public IVOMeta getMetaData() {
        IVOMeta meta = VOMetaFactory.getInstance().getVOMeta("so.so_saleorder_b");
        return meta;
    }

    public UFDouble getNarrangemonum() {
        return (UFDouble) this.getAttributeValue("narrangemonum");
    }

    public UFDouble getNarrangepoappnum() {
        return (UFDouble) this.getAttributeValue("narrangepoappnum");
    }

    public UFDouble getNarrangeponum() {
        return (UFDouble) this.getAttributeValue("narrangeponum");
    }

    public UFDouble getNarrangescornum() {
        return (UFDouble) this.getAttributeValue("narrangescornum");
    }

    public UFDouble getNarrangetoappnum() {
        return (UFDouble) this.getAttributeValue("narrangetoappnum");
    }

    public UFDouble getNarrangetoornum() {
        return (UFDouble) this.getAttributeValue("narrangetoornum");
    }

    public UFDouble getNaskqtorignetprice() {
        return (UFDouble) this.getAttributeValue("naskqtorignetprice");
    }

    public UFDouble getNaskqtorigprice() {
        return (UFDouble) this.getAttributeValue("naskqtorigprice");
    }

    public UFDouble getNaskqtorigtaxprc() {
        return (UFDouble) this.getAttributeValue("naskqtorigtaxprc");
    }

    public UFDouble getNaskqtorigtxntprc() {
        return (UFDouble) this.getAttributeValue("naskqtorigtxntprc");
    }

    public UFDouble getNastnum() {
        return (UFDouble) this.getAttributeValue("nastnum");
    }

    public UFDouble getNbforigsubmny() {
        return (UFDouble) this.getAttributeValue("nbforigsubmny");
    }

    public UFDouble getNcaltaxmny() {
        return (UFDouble) this.getAttributeValue("ncaltaxmny");
    }

    public UFDouble getDeferralmny() {
        return (UFDouble) this.getAttributeValue("deferralmny");
    }

    public UFDouble getRevconfirmmny() {
        return (UFDouble) this.getAttributeValue("revconfirmmny");
    }

    public UFDouble getNdiscount() {
        return (UFDouble) this.getAttributeValue("ndiscount");
    }

    public UFDouble getNdiscountrate() {
        return (UFDouble) this.getAttributeValue("ndiscountrate");
    }

    public UFDouble getNexchangerate() {
        return (UFDouble) this.getAttributeValue("nexchangerate");
    }

    public UFDouble getNglobalexchgrate() {
        return (UFDouble) this.getAttributeValue("nglobalexchgrate");
    }

    public UFDouble getNglobalmny() {
        return (UFDouble) this.getAttributeValue("nglobalmny");
    }

    public UFDouble getNglobaltaxmny() {
        return (UFDouble) this.getAttributeValue("nglobaltaxmny");
    }

    public UFDouble getNgroupexchgrate() {
        return (UFDouble) this.getAttributeValue("ngroupexchgrate");
    }

    public UFDouble getNgroupmny() {
        return (UFDouble) this.getAttributeValue("ngroupmny");
    }

    public UFDouble getNgrouptaxmny() {
        return (UFDouble) this.getAttributeValue("ngrouptaxmny");
    }

    public UFDouble getNinvoiceauditnum() {
        return (UFDouble) this.getAttributeValue("ninvoiceauditnum");
    }

    public UFDouble getNinvunfinisednum() {
        return (UFDouble) this.getAttributeValue("ninvunfinisednum");
    }

    public UFDouble getNitemdiscountrate() {
        return (UFDouble) this.getAttributeValue("nitemdiscountrate");
    }

    public UFDouble getNlargessmny() {
        return (UFDouble) this.getAttributeValue("nlargessmny");
    }

    public UFDouble getNlargesstaxmny() {
        return (UFDouble) this.getAttributeValue("nlargesstaxmny");
    }

    public UFDouble getNlossnotauditnum() {
        return (UFDouble) this.getAttributeValue("nlossnotauditnum");
    }

    public UFDouble getNmny() {
        return (UFDouble) this.getAttributeValue("nmny");
    }

    public UFDouble getNnetprice() {
        return (UFDouble) this.getAttributeValue("nnetprice");
    }

    public UFDouble getNnotarnum() {
        return (UFDouble) this.getAttributeValue("nnotarnum");
    }

    public UFDouble getNnotcostnum() {
        return (UFDouble) this.getAttributeValue("nnotcostnum");
    }

    public UFDouble getNnum() {
        return (UFDouble) this.getAttributeValue("nnum");
    }

    public UFDouble getNorigdiscount() {
        return (UFDouble) this.getAttributeValue("norigdiscount");
    }

    public UFDouble getNorigmny() {
        return (UFDouble) this.getAttributeValue("norigmny");
    }

    public UFDouble getNorignetprice() {
        return (UFDouble) this.getAttributeValue("norignetprice");
    }

    public UFDouble getNorigprice() {
        return (UFDouble) this.getAttributeValue("norigprice");
    }

    public UFDouble getNorigsubmny() {
        return (UFDouble) this.getAttributeValue("norigsubmny");
    }

    public UFDouble getNtotalrednum() {
        return (UFDouble) this.getAttributeValue("ntotalrednum");
    }

    public UFDouble getNredorigsubmny() {
        return (UFDouble) this.getAttributeValue("nredorigsubmny");
    }

    public UFDouble getNorigtaxmny() {
        return (UFDouble) this.getAttributeValue("norigtaxmny");
    }

    public UFDouble getNorigtaxnetprice() {
        return (UFDouble) this.getAttributeValue("norigtaxnetprice");
    }

    public UFDouble getNorigtaxprice() {
        return (UFDouble) this.getAttributeValue("norigtaxprice");
    }

    public UFDouble getNoutauditnum() {
        return (UFDouble) this.getAttributeValue("noutauditnum");
    }

    public UFDouble getNoutnotauditnum() {
        return (UFDouble) this.getAttributeValue("noutnotauditnum");
    }

    public UFDouble getNoutunfinisednum() {
        return (UFDouble) this.getAttributeValue("noutunfinisednum");
    }

    public UFDouble getNpiece() {
        return (UFDouble) this.getAttributeValue("npiece");
    }

    public UFDouble getNprice() {
        return (UFDouble) this.getAttributeValue("nprice");
    }

    public UFDouble getNqtnetprice() {
        return (UFDouble) this.getAttributeValue("nqtnetprice");
    }

    public UFDouble getNqtorignetprice() {
        return (UFDouble) this.getAttributeValue("nqtorignetprice");
    }

    public UFDouble getNqtorigprice() {
        return (UFDouble) this.getAttributeValue("nqtorigprice");
    }

    public UFDouble getNqtorigtaxnetprc() {
        return (UFDouble) this.getAttributeValue("nqtorigtaxnetprc");
    }

    public UFDouble getNqtorigtaxprice() {
        return (UFDouble) this.getAttributeValue("nqtorigtaxprice");
    }

    public UFDouble getNqtprice() {
        return (UFDouble) this.getAttributeValue("nqtprice");
    }

    public UFDouble getNqttaxnetprice() {
        return (UFDouble) this.getAttributeValue("nqttaxnetprice");
    }

    public UFDouble getNqttaxprice() {
        return (UFDouble) this.getAttributeValue("nqttaxprice");
    }

    public UFDouble getNqtunitnum() {
        return (UFDouble) this.getAttributeValue("nqtunitnum");
    }

    public UFDouble getNreqrsnum() {
        return (UFDouble) this.getAttributeValue("nreqrsnum");
    }

    public UFDouble getNsendauditnum() {
        return (UFDouble) this.getAttributeValue("nsendauditnum");
    }

    public UFDouble getNsendunfinisednum() {
        return (UFDouble) this.getAttributeValue("nsendunfinisednum");
    }

    public UFDouble getNtax() {
        return (UFDouble) this.getAttributeValue("ntax");
    }

    public UFDouble getNtaxmny() {
        return (UFDouble) this.getAttributeValue("ntaxmny");
    }

    public UFDouble getNtaxnetprice() {
        return (UFDouble) this.getAttributeValue("ntaxnetprice");
    }

    public UFDouble getNtaxprice() {
        return (UFDouble) this.getAttributeValue("ntaxprice");
    }

    public UFDouble getNtaxrate() {
        return (UFDouble) this.getAttributeValue("ntaxrate");
    }

    public UFDouble getNtotalarmny() {
        return (UFDouble) this.getAttributeValue("ntotalarmny");
    }

    public UFDouble getNtotalarnum() {
        return (UFDouble) this.getAttributeValue("ntotalarnum");
    }

    public UFDouble getNtotalcostnum() {
        return (UFDouble) this.getAttributeValue("ntotalcostnum");
    }

    public UFDouble getNtotalestarmny() {
        return (UFDouble) this.getAttributeValue("ntotalestarmny");
    }

    public UFDouble getNtotalestarnum() {
        return (UFDouble) this.getAttributeValue("ntotalestarnum");
    }

    public UFDouble getNtotalinvoicenum() {
        return (UFDouble) this.getAttributeValue("ntotalinvoicenum");
    }

    public UFDouble getNtotalnotoutnum() {
        return (UFDouble) this.getAttributeValue("ntotalnotoutnum");
    }

    public UFDouble getNtotaloutnum() {
        return (UFDouble) this.getAttributeValue("ntotaloutnum");
    }

    public UFDouble getNtotalpaymny() {
        return (UFDouble) this.getAttributeValue("ntotalpaymny");
    }

    public UFDouble getNtotalplonum() {
        return (UFDouble) this.getAttributeValue("ntotalplonum");
    }

    public UFDouble getNtotalreturnnum() {
        return (UFDouble) this.getAttributeValue("ntotalreturnnum");
    }

    public UFDouble getNtotalrushnum() {
        return (UFDouble) this.getAttributeValue("ntotalrushnum");
    }

    public UFDouble getNtotalsendnum() {
        return (UFDouble) this.getAttributeValue("ntotalsendnum");
    }

    public UFDouble getNtotalsignnum() {
        return (UFDouble) this.getAttributeValue("ntotalsignnum");
    }

    public UFDouble getNtotaltradenum() {
        return (UFDouble) this.getAttributeValue("ntotaltradenum");
    }

    public UFDouble getNtranslossnum() {
        return (UFDouble) this.getAttributeValue("ntranslossnum");
    }

    public UFDouble getNvolume() {
        return (UFDouble) this.getAttributeValue("nvolume");
    }

    public UFDouble getNweight() {
        return (UFDouble) this.getAttributeValue("nweight");
    }

    public String getPk_batchcode() {
        return (String) this.getAttributeValue("pk_batchcode");
    }

    public String getPk_group() {
        return (String) this.getAttributeValue("pk_group");
    }

    public String getPk_org() {
        return (String) this.getAttributeValue("pk_org");
    }

    public UFDateTime getSrcbts() {
        return (UFDateTime) this.getAttributeValue("srcbts");
    }

    public String getSrcorgid() {
        return (String) this.getAttributeValue("srcorgid");
    }

    public UFDateTime getSrcts() {
        return (UFDateTime) this.getAttributeValue("srcts");
    }

    public UFDateTime getTlastarrangetime() {
        return (UFDateTime) this.getAttributeValue("tlastarrangetime");
    }

    public UFDateTime getTs() {
        return (UFDateTime) this.getAttributeValue("ts");
    }

    public String getVbatchcode() {
        return (String) this.getAttributeValue("vbatchcode");
    }

    public String getVbdef1() {
        return (String) this.getAttributeValue("vbdef1");
    }

    public String getVbdef10() {
        return (String) this.getAttributeValue("vbdef10");
    }

    public String getVbdef11() {
        return (String) this.getAttributeValue("vbdef11");
    }

    public String getVbdef12() {
        return (String) this.getAttributeValue("vbdef12");
    }

    public String getVbdef13() {
        return (String) this.getAttributeValue("vbdef13");
    }

    public String getVbdef14() {
        return (String) this.getAttributeValue("vbdef14");
    }

    public String getVbdef15() {
        return (String) this.getAttributeValue("vbdef15");
    }

    public String getVbdef16() {
        return (String) this.getAttributeValue("vbdef16");
    }

    public String getVbdef17() {
        return (String) this.getAttributeValue("vbdef17");
    }

    public String getVbdef18() {
        return (String) this.getAttributeValue("vbdef18");
    }

    public String getVbdef19() {
        return (String) this.getAttributeValue("vbdef19");
    }

    public String getVbdef2() {
        return (String) this.getAttributeValue("vbdef2");
    }

    public String getVbdef20() {
        return (String) this.getAttributeValue("vbdef20");
    }

    public String getVbdef3() {
        return (String) this.getAttributeValue("vbdef3");
    }

    public String getVbdef4() {
        return (String) this.getAttributeValue("vbdef4");
    }

    public String getVbdef5() {
        return (String) this.getAttributeValue("vbdef5");
    }

    public String getVbdef6() {
        return (String) this.getAttributeValue("vbdef6");
    }

    public String getVbdef7() {
        return (String) this.getAttributeValue("vbdef7");
    }

    public String getVbdef8() {
        return (String) this.getAttributeValue("vbdef8");
    }

    public String getVbdef9() {
        return (String) this.getAttributeValue("vbdef9");
    }

    public String getVbdef21() {
        return (String) this.getAttributeValue("vbdef21");
    }

    public String getVbdef22() {
        return (String) this.getAttributeValue("vbdef22");
    }

    public String getVbdef23() {
        return (String) this.getAttributeValue("vbdef23");
    }

    public String getVbdef24() {
        return (String) this.getAttributeValue("vbdef24");
    }

    public String getVbdef25() {
        return (String) this.getAttributeValue("vbdef25");
    }

    public String getVbdef26() {
        return (String) this.getAttributeValue("vbdef26");
    }

    public String getVbdef27() {
        return (String) this.getAttributeValue("vbdef27");
    }

    public String getVbdef28() {
        return (String) this.getAttributeValue("vbdef28");
    }

    public String getVbdef29() {
        return (String) this.getAttributeValue("vbdef29");
    }

    public String getVbdef30() {
        return (String) this.getAttributeValue("vbdef30");
    }

    public String getVbdef31() {
        return (String) this.getAttributeValue("vbdef31");
    }

    public String getVbdef32() {
        return (String) this.getAttributeValue("vbdef32");
    }

    public String getVbdef33() {
        return (String) this.getAttributeValue("vbdef33");
    }

    public String getVbdef34() {
        return (String) this.getAttributeValue("vbdef34");
    }

    public String getVbdef35() {
        return (String) this.getAttributeValue("vbdef35");
    }

    public String getVbdef36() {
        return (String) this.getAttributeValue("vbdef36");
    }

    public String getVbdef37() {
        return (String) this.getAttributeValue("vbdef37");
    }

    public String getVbdef38() {
        return (String) this.getAttributeValue("vbdef38");
    }

    public String getVbdef39() {
        return (String) this.getAttributeValue("vbdef39");
    }

    public String getVbdef40() {
        return (String) this.getAttributeValue("vbdef40");
    }

    public String getVbdef41() {
        return (String) this.getAttributeValue("vbdef41");
    }

    public String getVbdef42() {
        return (String) this.getAttributeValue("vbdef42");
    }

    public String getVbdef43() {
        return (String) this.getAttributeValue("vbdef43");
    }

    public String getVbdef44() {
        return (String) this.getAttributeValue("vbdef44");
    }

    public String getVbdef45() {
        return (String) this.getAttributeValue("vbdef45");
    }

    public String getVbdef46() {
        return (String) this.getAttributeValue("vbdef46");
    }

    public String getVbdef47() {
        return (String) this.getAttributeValue("vbdef47");
    }

    public String getVbdef48() {
        return (String) this.getAttributeValue("vbdef48");
    }

    public String getVbdef49() {
        return (String) this.getAttributeValue("vbdef49");
    }

    public String getVbdef50() {
        return (String) this.getAttributeValue("vbdef50");
    }

    public String getVbrevisereason() {
        return (String) this.getAttributeValue("vbrevisereason");
    }

    public String getVchangerate() {
        return (String) this.getAttributeValue("vchangerate");
    }

    public String getVclosereason() {
        return (String) this.getAttributeValue("vclosereason");
    }

    public String getVctcode() {
        return (String) this.getAttributeValue("vctcode");
    }

    public String getVcttype() {
        return (String) this.getAttributeValue("vcttype");
    }

    public String getVfirstcode() {
        return (String) this.getAttributeValue("vfirstcode");
    }

    public String getVfirstrowno() {
        return (String) this.getAttributeValue("vfirstrowno");
    }

    public String getVfirsttrantype() {
        return (String) this.getAttributeValue("vfirsttrantype");
    }

    public String getCpromotpriceid() {
        return (String) this.getAttributeValue("cpromotpriceid");
    }

    public String getVfirsttype() {
        return (String) this.getAttributeValue("vfirsttype");
    }

    public String getVfree1() {
        return (String) this.getAttributeValue("vfree1");
    }

    public String getVfree10() {
        return (String) this.getAttributeValue("vfree10");
    }

    public String getVfree2() {
        return (String) this.getAttributeValue("vfree2");
    }

    public String getVfree3() {
        return (String) this.getAttributeValue("vfree3");
    }

    public String getVfree4() {
        return (String) this.getAttributeValue("vfree4");
    }

    public String getVfree5() {
        return (String) this.getAttributeValue("vfree5");
    }

    public String getVfree6() {
        return (String) this.getAttributeValue("vfree6");
    }

    public String getVfree7() {
        return (String) this.getAttributeValue("vfree7");
    }

    public String getVfree8() {
        return (String) this.getAttributeValue("vfree8");
    }

    public String getVfree9() {
        return (String) this.getAttributeValue("vfree9");
    }

    public String getVqtunitrate() {
        return (String) this.getAttributeValue("vqtunitrate");
    }

    public String getVreturnmode() {
        return (String) this.getAttributeValue("vreturnmode");
    }

    public String getVrownote() {
        return (String) this.getAttributeValue("vrownote");
    }

    public String getVsrccode() {
        return (String) this.getAttributeValue("vsrccode");
    }

    public String getVsrcrowno() {
        return (String) this.getAttributeValue("vsrcrowno");
    }

    public String getVsrctrantype() {
        return (String) this.getAttributeValue("vsrctrantype");
    }

    public String getVsrctype() {
        return (String) this.getAttributeValue("vsrctype");
    }

    public String getCbuypromottypeid() {
        return (String) this.getAttributeValue("cbuypromottypeid");
    }

    public String getCprcpromottypeid() {
        return (String) this.getAttributeValue("cprcpromottypeid");
    }

    public String getVcustombillcode() {
        return (String) this.getAttributeValue("vcustombillcode");
    }

    public String getCsprofitcenterid() {
        return (String) this.getAttributeValue("csprofitcenterid");
    }

    public String getCsprofitcentervid() {
        return (String) this.getAttributeValue("csprofitcentervid");
    }

    public void setBarrangedflag(UFBoolean barrangedflag) {
        this.setAttributeValue("barrangedflag", barrangedflag);
    }

    public void setBbarsettleflag(UFBoolean bbarsettleflag) {
        this.setAttributeValue("bbarsettleflag", bbarsettleflag);
    }

    public void setBbcostsettleflag(UFBoolean bbcostsettleflag) {
        this.setAttributeValue("bbcostsettleflag", bbcostsettleflag);
    }

    public void setBbindflag(UFBoolean bbindflag) {
        this.setAttributeValue("bbindflag", bbindflag);
    }

    public void setBbinvoicendflag(UFBoolean bbinvoicendflag) {
        this.setAttributeValue("bbinvoicendflag", bbinvoicendflag);
    }

    public void setBboutendflag(UFBoolean bboutendflag) {
        this.setAttributeValue("bboutendflag", bboutendflag);
    }

    public void setBbsendendflag(UFBoolean bbsendendflag) {
        this.setAttributeValue("bbsendendflag", bbsendendflag);
    }

    public void setBbsettleendflag(UFBoolean bbsettleendflag) {
        this.setAttributeValue("bbsettleendflag", bbsettleendflag);
    }

    public void setBdiscountflag(UFBoolean bdiscountflag) {
        this.setAttributeValue("bdiscountflag", bdiscountflag);
    }

    public void setBjczxsflag(UFBoolean bjczxsflag) {
        this.setAttributeValue("bjczxsflag", bjczxsflag);
    }

    public void setBlaborflag(UFBoolean blaborflag) {
        this.setAttributeValue("blaborflag", blaborflag);
    }

    public void setBlargessflag(UFBoolean blargessflag) {
        this.setAttributeValue("blargessflag", blargessflag);
    }

    public void setBprerowcloseflag(UFBoolean bprerowcloseflag) {
        this.setAttributeValue("bprerowcloseflag", bprerowcloseflag);
    }

    public void setBtriatradeflag(UFBoolean btriatradeflag) {
        this.setAttributeValue("btriatradeflag", btriatradeflag);
    }

    public void setCarorgid(String carorgid) {
        this.setAttributeValue("carorgid", carorgid);
    }

    public void setCarorgvid(String carorgvid) {
        this.setAttributeValue("carorgvid", carorgvid);
    }

    public void setCarrangepersonid(String carrangepersonid) {
        this.setAttributeValue("carrangepersonid", carrangepersonid);
    }

    public void setCastunitid(String castunitid) {
        this.setAttributeValue("castunitid", castunitid);
    }

    public void setCbindsrcid(String cbindsrcid) {
        this.setAttributeValue("cbindsrcid", cbindsrcid);
    }

    public void setCbrandid(String cbrandid) {
        this.setAttributeValue("cbrandid", cbrandid);
    }

    public void setCctmanagebid(String cctmanagebid) {
        this.setAttributeValue("cctmanagebid", cctmanagebid);
    }

    public void setCctmanageid(String cctmanageid) {
        this.setAttributeValue("cctmanageid", cctmanageid);
    }

    public void setCcurrencyid(String ccurrencyid) {
        this.setAttributeValue("ccurrencyid", ccurrencyid);
    }

    public void setCexchangesrcretid(String cexchangesrcretid) {
        this.setAttributeValue("cexchangesrcretid", cexchangesrcretid);
    }

    public void setCfactoryid(String cfactoryid) {
        this.setAttributeValue("cfactoryid", cfactoryid);
    }

    public void setCfirstbid(String cfirstbid) {
        this.setAttributeValue("cfirstbid", cfirstbid);
    }

    public void setCfirstid(String cfirstid) {
        this.setAttributeValue("cfirstid", cfirstid);
    }

    public void setClargesssrcid(String clargesssrcid) {
        this.setAttributeValue("clargesssrcid", clargesssrcid);
    }

    public void setCmaterialid(String cmaterialid) {
        this.setAttributeValue("cmaterialid", cmaterialid);
    }

    public void setCmaterialvid(String cmaterialvid) {
        this.setAttributeValue("cmaterialvid", cmaterialvid);
    }

    public void setCorigareaid(String corigareaid) {
        this.setAttributeValue("corigareaid", corigareaid);
    }

    public void setCorigcountryid(String corigcountryid) {
        this.setAttributeValue("corigcountryid", corigcountryid);
    }

    public void setCpriceformid(String cpriceformid) {
        this.setAttributeValue("cpriceformid", cpriceformid);
    }

    public void setCpriceitemid(String cpriceitemid) {
        this.setAttributeValue("cpriceitemid", cpriceitemid);
    }

    public void setCpriceitemtableid(String cpriceitemtableid) {
        this.setAttributeValue("cpriceitemtableid", cpriceitemtableid);
    }

    public void setCpricepolicyid(String cpricepolicyid) {
        this.setAttributeValue("cpricepolicyid", cpricepolicyid);
    }

    public void setCprodlineid(String cprodlineid) {
        this.setAttributeValue("cprodlineid", cprodlineid);
    }

    public void setCproductorid(String cproductorid) {
        this.setAttributeValue("cproductorid", cproductorid);
    }

    public void setCprofitcenterid(String cprofitcenterid) {
        this.setAttributeValue("cprofitcenterid", cprofitcenterid);
    }

    public void setCprofitcentervid(String cprofitcentervid) {
        this.setAttributeValue("cprofitcentervid", cprofitcentervid);
    }

    public void setCprojectid(String cprojectid) {
        this.setAttributeValue("cprojectid", cprojectid);
    }

    public void setCqtunitid(String cqtunitid) {
        this.setAttributeValue("cqtunitid", cqtunitid);
    }

    public void setCqualitylevelid(String cqualitylevelid) {
        this.setAttributeValue("cqualitylevelid", cqualitylevelid);
    }

    public void setCrececountryid(String crececountryid) {
        this.setAttributeValue("crececountryid", crececountryid);
    }

    public void setCreceiveadddocid(String creceiveadddocid) {
        this.setAttributeValue("creceiveadddocid", creceiveadddocid);
    }

    public void setCreceiveaddrid(String creceiveaddrid) {
        this.setAttributeValue("creceiveaddrid", creceiveaddrid);
    }

    public void setCreceiveareaid(String creceiveareaid) {
        this.setAttributeValue("creceiveareaid", creceiveareaid);
    }

    public void setCreceivecustid(String creceivecustid) {
        this.setAttributeValue("creceivecustid", creceivecustid);
    }

    public void setCreceivecustvid(String creceivecustvid) {
        this.setAttributeValue("creceivecustvid", creceivecustvid);
    }

    public void setCretpolicyid(String cretpolicyid) {
        this.setAttributeValue("cretpolicyid", cretpolicyid);
    }

    public void setCretreasonid(String cretreasonid) {
        this.setAttributeValue("cretreasonid", cretreasonid);
    }

    public void setCrowno(String crowno) {
        this.setAttributeValue("crowno", crowno);
    }

    public void setCsaleorderbid(String csaleorderbid) {
        this.setAttributeValue("csaleorderbid", csaleorderbid);
    }

    public void setCsaleorderid(String csaleorderid) {
        this.setAttributeValue("csaleorderid", csaleorderid);
    }

    public void setCsendcountryid(String csendcountryid) {
        this.setAttributeValue("csendcountryid", csendcountryid);
    }

    public void setCsendstockorgid(String csendstockorgid) {
        this.setAttributeValue("csendstockorgid", csendstockorgid);
    }

    public void setCsendstockorgvid(String csendstockorgvid) {
        this.setAttributeValue("csendstockorgvid", csendstockorgvid);
    }

    public void setCsendstordocid(String csendstordocid) {
        this.setAttributeValue("csendstordocid", csendstordocid);
    }

    public void setCsettleorgid(String csettleorgid) {
        this.setAttributeValue("csettleorgid", csettleorgid);
    }

    public void setCsettleorgvid(String csettleorgvid) {
        this.setAttributeValue("csettleorgvid", csettleorgvid);
    }

    public void setCsrcbid(String csrcbid) {
        this.setAttributeValue("csrcbid", csrcbid);
    }

    public void setCsrcid(String csrcid) {
        this.setAttributeValue("csrcid", csrcid);
    }

    public void setCtaxcodeid(String ctaxcodeid) {
        this.setAttributeValue("ctaxcodeid", ctaxcodeid);
    }

    public void setCtaxcountryid(String ctaxcountryid) {
        this.setAttributeValue("ctaxcountryid", ctaxcountryid);
    }

    public void setCtrafficorgid(String ctrafficorgid) {
        this.setAttributeValue("ctrafficorgid", ctrafficorgid);
    }

    public void setCtrafficorgvid(String ctrafficorgvid) {
        this.setAttributeValue("ctrafficorgvid", ctrafficorgvid);
    }

    public void setCunitid(String cunitid) {
        this.setAttributeValue("cunitid", cunitid);
    }

    public void setCvendorid(String cvendorid) {
        this.setAttributeValue("cvendorid", cvendorid);
    }

    public void setCvendorvid(String cvendorvid) {
        this.setAttributeValue("cvendorvid", cvendorvid);
    }

    public void setCratetype(String cratetype) {
        this.setAttributeValue("cratetype", cratetype);
    }

    public void setFratecategory(Integer fratecategory) {
        this.setAttributeValue("fratecategory", fratecategory);
    }

    public void setDratedate(UFLiteralDate dratedate) {
        this.setAttributeValue("dratedate", dratedate);
    }

    public void setDbilldate(UFDate dbilldate) {
        this.setAttributeValue("dbilldate", dbilldate);
    }

    public void setDr(Integer dr) {
        this.setAttributeValue("dr", dr);
    }

    public void setDreceivedate(UFDate dreceivedate) {
        this.setAttributeValue("dreceivedate", dreceivedate);
    }

    public void setDsenddate(UFDate dsenddate) {
        this.setAttributeValue("dsenddate", dsenddate);
    }

    public void setFbuysellflag(Integer fbuysellflag) {
        this.setAttributeValue("fbuysellflag", fbuysellflag);
    }

    public void setFlargesstypeflag(Integer flargesstypeflag) {
        this.setAttributeValue("flargesstypeflag", flargesstypeflag);
    }

    public void setFretexchange(Integer fretexchange) {
        this.setAttributeValue("fretexchange", fretexchange);
    }

    public void setFrowstatus(Integer frowstatus) {
        this.setAttributeValue("frowstatus", frowstatus);
    }

    public void setFtaxtypeflag(Integer ftaxtypeflag) {
        this.setAttributeValue("ftaxtypeflag", ftaxtypeflag);
    }

    public void setNarrangemonum(UFDouble narrangemonum) {
        this.setAttributeValue("narrangemonum", narrangemonum);
    }

    public void setNarrangepoappnum(UFDouble narrangepoappnum) {
        this.setAttributeValue("narrangepoappnum", narrangepoappnum);
    }

    public void setNarrangeponum(UFDouble narrangeponum) {
        this.setAttributeValue("narrangeponum", narrangeponum);
    }

    public void setNarrangescornum(UFDouble narrangescornum) {
        this.setAttributeValue("narrangescornum", narrangescornum);
    }

    public void setNarrangetoappnum(UFDouble narrangetoappnum) {
        this.setAttributeValue("narrangetoappnum", narrangetoappnum);
    }

    public void setNarrangetoornum(UFDouble narrangetoornum) {
        this.setAttributeValue("narrangetoornum", narrangetoornum);
    }

    public void setNaskqtorignetprice(UFDouble naskqtorignetprice) {
        this.setAttributeValue("naskqtorignetprice", naskqtorignetprice);
    }

    public void setNaskqtorigprice(UFDouble naskqtorigprice) {
        this.setAttributeValue("naskqtorigprice", naskqtorigprice);
    }

    public void setNaskqtorigtaxprc(UFDouble naskqtorigtaxprc) {
        this.setAttributeValue("naskqtorigtaxprc", naskqtorigtaxprc);
    }

    public void setNaskqtorigtxntprc(UFDouble naskqtorigtxntprc) {
        this.setAttributeValue("naskqtorigtxntprc", naskqtorigtxntprc);
    }

    public void setNastnum(UFDouble nastnum) {
        this.setAttributeValue("nastnum", nastnum);
    }

    public void setNbforigsubmny(UFDouble nbforigsubmny) {
        this.setAttributeValue("nbforigsubmny", nbforigsubmny);
    }

    public void setNcaltaxmny(UFDouble ncaltaxmny) {
        this.setAttributeValue("ncaltaxmny", ncaltaxmny);
    }

    public void setDeferralmny(UFDouble deferralmny) {
        this.setAttributeValue("deferralmny", deferralmny);
    }

    public void setRevconfirmmny(UFDouble revconfirmmny) {
        this.setAttributeValue("revconfirmmny", revconfirmmny);
    }

    public void setNdiscount(UFDouble ndiscount) {
        this.setAttributeValue("ndiscount", ndiscount);
    }

    public void setNdiscountrate(UFDouble ndiscountrate) {
        this.setAttributeValue("ndiscountrate", ndiscountrate);
    }

    public void setNexchangerate(UFDouble nexchangerate) {
        this.setAttributeValue("nexchangerate", nexchangerate);
    }

    public void setNglobalexchgrate(UFDouble nglobalexchgrate) {
        this.setAttributeValue("nglobalexchgrate", nglobalexchgrate);
    }

    public void setNglobalmny(UFDouble nglobalmny) {
        this.setAttributeValue("nglobalmny", nglobalmny);
    }

    public void setNglobaltaxmny(UFDouble nglobaltaxmny) {
        this.setAttributeValue("nglobaltaxmny", nglobaltaxmny);
    }

    public void setNgroupexchgrate(UFDouble ngroupexchgrate) {
        this.setAttributeValue("ngroupexchgrate", ngroupexchgrate);
    }

    public void setNgroupmny(UFDouble ngroupmny) {
        this.setAttributeValue("ngroupmny", ngroupmny);
    }

    public void setNgrouptaxmny(UFDouble ngrouptaxmny) {
        this.setAttributeValue("ngrouptaxmny", ngrouptaxmny);
    }

    public void setNinvoiceauditnum(UFDouble ninvoiceauditnum) {
        this.setAttributeValue("ninvoiceauditnum", ninvoiceauditnum);
    }

    public void setNinvunfinisednum(UFDouble ninvunfinisednum) {
        this.setAttributeValue("ninvunfinisednum", ninvunfinisednum);
    }

    public void setNitemdiscountrate(UFDouble nitemdiscountrate) {
        this.setAttributeValue("nitemdiscountrate", nitemdiscountrate);
    }

    public void setNlargessmny(UFDouble nlargessmny) {
        this.setAttributeValue("nlargessmny", nlargessmny);
    }

    public void setNlargesstaxmny(UFDouble nlargesstaxmny) {
        this.setAttributeValue("nlargesstaxmny", nlargesstaxmny);
    }

    public void setNlossnotauditnum(UFDouble nlossnotauditnum) {
        this.setAttributeValue("nlossnotauditnum", nlossnotauditnum);
    }

    public void setNmny(UFDouble nmny) {
        this.setAttributeValue("nmny", nmny);
    }

    public void setNnetprice(UFDouble nnetprice) {
        this.setAttributeValue("nnetprice", nnetprice);
    }

    public void setNnotarnum(UFDouble nnotarnum) {
        this.setAttributeValue("nnotarnum", nnotarnum);
    }

    public void setNnotcostnum(UFDouble nnotcostnum) {
        this.setAttributeValue("nnotcostnum", nnotcostnum);
    }

    public void setNnum(UFDouble nnum) {
        this.setAttributeValue("nnum", nnum);
    }

    public void setNorigdiscount(UFDouble norigdiscount) {
        this.setAttributeValue("norigdiscount", norigdiscount);
    }

    public void setNorigmny(UFDouble norigmny) {
        this.setAttributeValue("norigmny", norigmny);
    }

    public void setNorignetprice(UFDouble norignetprice) {
        this.setAttributeValue("norignetprice", norignetprice);
    }

    public void setNorigprice(UFDouble norigprice) {
        this.setAttributeValue("norigprice", norigprice);
    }

    public void setNorigsubmny(UFDouble norigsubmny) {
        this.setAttributeValue("norigsubmny", norigsubmny);
    }

    public void setNtotalrednum(UFDouble ntotalrednum) {
        this.setAttributeValue("ntotalrednum", ntotalrednum);
    }

    public void setNredorigsubmny(UFDouble nredorigsubmny) {
        this.setAttributeValue("nredorigsubmny", nredorigsubmny);
    }

    public void setNorigtaxmny(UFDouble norigtaxmny) {
        this.setAttributeValue("norigtaxmny", norigtaxmny);
    }

    public void setNorigtaxnetprice(UFDouble norigtaxnetprice) {
        this.setAttributeValue("norigtaxnetprice", norigtaxnetprice);
    }

    public void setNorigtaxprice(UFDouble norigtaxprice) {
        this.setAttributeValue("norigtaxprice", norigtaxprice);
    }

    public void setNoutauditnum(UFDouble noutauditnum) {
        this.setAttributeValue("noutauditnum", noutauditnum);
    }

    public void setNoutnotauditnum(UFDouble noutnotauditnum) {
        this.setAttributeValue("noutnotauditnum", noutnotauditnum);
    }

    public void setNoutunfinisednum(UFDouble noutunfinisednum) {
        this.setAttributeValue("noutunfinisednum", noutunfinisednum);
    }

    public void setNpiece(UFDouble npiece) {
        this.setAttributeValue("npiece", npiece);
    }

    public void setNprice(UFDouble nprice) {
        this.setAttributeValue("nprice", nprice);
    }

    public void setNqtnetprice(UFDouble nqtnetprice) {
        this.setAttributeValue("nqtnetprice", nqtnetprice);
    }

    public void setNqtorignetprice(UFDouble nqtorignetprice) {
        this.setAttributeValue("nqtorignetprice", nqtorignetprice);
    }

    public void setNqtorigprice(UFDouble nqtorigprice) {
        this.setAttributeValue("nqtorigprice", nqtorigprice);
    }

    public void setNqtorigtaxnetprc(UFDouble nqtorigtaxnetprc) {
        this.setAttributeValue("nqtorigtaxnetprc", nqtorigtaxnetprc);
    }

    public void setNqtorigtaxprice(UFDouble nqtorigtaxprice) {
        this.setAttributeValue("nqtorigtaxprice", nqtorigtaxprice);
    }

    public void setNqtprice(UFDouble nqtprice) {
        this.setAttributeValue("nqtprice", nqtprice);
    }

    public void setNqttaxnetprice(UFDouble nqttaxnetprice) {
        this.setAttributeValue("nqttaxnetprice", nqttaxnetprice);
    }

    public void setNqttaxprice(UFDouble nqttaxprice) {
        this.setAttributeValue("nqttaxprice", nqttaxprice);
    }

    public void setNqtunitnum(UFDouble nqtunitnum) {
        this.setAttributeValue("nqtunitnum", nqtunitnum);
    }

    public void setNreqrsnum(UFDouble nreqrsnum) {
        this.setAttributeValue("nreqrsnum", nreqrsnum);
    }

    public void setNsendauditnum(UFDouble nsendauditnum) {
        this.setAttributeValue("nsendauditnum", nsendauditnum);
    }

    public void setNsendunfinisednum(UFDouble nsendunfinisednum) {
        this.setAttributeValue("nsendunfinisednum", nsendunfinisednum);
    }

    public void setNtax(UFDouble ntax) {
        this.setAttributeValue("ntax", ntax);
    }

    public void setNtaxmny(UFDouble ntaxmny) {
        this.setAttributeValue("ntaxmny", ntaxmny);
    }

    public void setNtaxnetprice(UFDouble ntaxnetprice) {
        this.setAttributeValue("ntaxnetprice", ntaxnetprice);
    }

    public void setNtaxprice(UFDouble ntaxprice) {
        this.setAttributeValue("ntaxprice", ntaxprice);
    }

    public void setNtaxrate(UFDouble ntaxrate) {
        this.setAttributeValue("ntaxrate", ntaxrate);
    }

    public void setNtotalarmny(UFDouble ntotalarmny) {
        this.setAttributeValue("ntotalarmny", ntotalarmny);
    }

    public void setNtotalarnum(UFDouble ntotalarnum) {
        this.setAttributeValue("ntotalarnum", ntotalarnum);
    }

    public void setNtotalcostnum(UFDouble ntotalcostnum) {
        this.setAttributeValue("ntotalcostnum", ntotalcostnum);
    }

    public void setNtotalestarmny(UFDouble ntotalestarmny) {
        this.setAttributeValue("ntotalestarmny", ntotalestarmny);
    }

    public void setNtotalestarnum(UFDouble ntotalestarnum) {
        this.setAttributeValue("ntotalestarnum", ntotalestarnum);
    }

    public void setNtotalinvoicenum(UFDouble ntotalinvoicenum) {
        this.setAttributeValue("ntotalinvoicenum", ntotalinvoicenum);
    }

    public void setNtotalnotoutnum(UFDouble ntotalnotoutnum) {
        this.setAttributeValue("ntotalnotoutnum", ntotalnotoutnum);
    }

    public void setNtotaloutnum(UFDouble ntotaloutnum) {
        this.setAttributeValue("ntotaloutnum", ntotaloutnum);
    }

    public void setNtotalpaymny(UFDouble ntotalpaymny) {
        this.setAttributeValue("ntotalpaymny", ntotalpaymny);
    }

    public void setNtotalplonum(UFDouble ntotalplonum) {
        this.setAttributeValue("ntotalplonum", ntotalplonum);
    }

    public void setNtotalreturnnum(UFDouble ntotalreturnnum) {
        this.setAttributeValue("ntotalreturnnum", ntotalreturnnum);
    }

    public void setNtotalrushnum(UFDouble ntotalrushnum) {
        this.setAttributeValue("ntotalrushnum", ntotalrushnum);
    }

    public void setNtotalsendnum(UFDouble ntotalsendnum) {
        this.setAttributeValue("ntotalsendnum", ntotalsendnum);
    }

    public void setNtotalsignnum(UFDouble ntotalsignnum) {
        this.setAttributeValue("ntotalsignnum", ntotalsignnum);
    }

    public void setNtotaltradenum(UFDouble ntotaltradenum) {
        this.setAttributeValue("ntotaltradenum", ntotaltradenum);
    }

    public void setNtranslossnum(UFDouble ntranslossnum) {
        this.setAttributeValue("ntranslossnum", ntranslossnum);
    }

    public void setNvolume(UFDouble nvolume) {
        this.setAttributeValue("nvolume", nvolume);
    }

    public void setNweight(UFDouble nweight) {
        this.setAttributeValue("nweight", nweight);
    }

    public void setPk_batchcode(String pk_batchcode) {
        this.setAttributeValue("pk_batchcode", pk_batchcode);
    }

    public void setPk_group(String pk_group) {
        this.setAttributeValue("pk_group", pk_group);
    }

    public void setPk_org(String pk_org) {
        this.setAttributeValue("pk_org", pk_org);
    }

    public void setSrcbts(UFDateTime srcbts) {
        this.setAttributeValue("srcbts", srcbts);
    }

    public void setSrcorgid(String srcorgid) {
        this.setAttributeValue("srcorgid", srcorgid);
    }

    public void setSrcts(UFDateTime srcts) {
        this.setAttributeValue("srcts", srcts);
    }

    public void setTlastarrangetime(UFDateTime tlastarrangetime) {
        this.setAttributeValue("tlastarrangetime", tlastarrangetime);
    }

    public void setTs(UFDateTime ts) {
        this.setAttributeValue("ts", ts);
    }

    public void setVbatchcode(String vbatchcode) {
        this.setAttributeValue("vbatchcode", vbatchcode);
    }

    public void setVbdef1(String vbdef1) {
        this.setAttributeValue("vbdef1", vbdef1);
    }

    public void setVbdef10(String vbdef10) {
        this.setAttributeValue("vbdef10", vbdef10);
    }

    public void setVbdef11(String vbdef11) {
        this.setAttributeValue("vbdef11", vbdef11);
    }

    public void setVbdef12(String vbdef12) {
        this.setAttributeValue("vbdef12", vbdef12);
    }

    public void setVbdef13(String vbdef13) {
        this.setAttributeValue("vbdef13", vbdef13);
    }

    public void setVbdef14(String vbdef14) {
        this.setAttributeValue("vbdef14", vbdef14);
    }

    public void setVbdef15(String vbdef15) {
        this.setAttributeValue("vbdef15", vbdef15);
    }

    public void setVbdef16(String vbdef16) {
        this.setAttributeValue("vbdef16", vbdef16);
    }

    public void setVbdef17(String vbdef17) {
        this.setAttributeValue("vbdef17", vbdef17);
    }

    public void setVbdef18(String vbdef18) {
        this.setAttributeValue("vbdef18", vbdef18);
    }

    public void setVbdef19(String vbdef19) {
        this.setAttributeValue("vbdef19", vbdef19);
    }

    public void setVbdef2(String vbdef2) {
        this.setAttributeValue("vbdef2", vbdef2);
    }

    public void setVbdef20(String vbdef20) {
        this.setAttributeValue("vbdef20", vbdef20);
    }

    public void setVbdef3(String vbdef3) {
        this.setAttributeValue("vbdef3", vbdef3);
    }

    public void setVbdef4(String vbdef4) {
        this.setAttributeValue("vbdef4", vbdef4);
    }

    public void setVbdef5(String vbdef5) {
        this.setAttributeValue("vbdef5", vbdef5);
    }

    public void setVbdef6(String vbdef6) {
        this.setAttributeValue("vbdef6", vbdef6);
    }

    public void setVbdef7(String vbdef7) {
        this.setAttributeValue("vbdef7", vbdef7);
    }

    public void setVbdef8(String vbdef8) {
        this.setAttributeValue("vbdef8", vbdef8);
    }

    public void setVbdef9(String vbdef9) {
        this.setAttributeValue("vbdef9", vbdef9);
    }

    public void setVbdef21(String vbdef21) {
        this.setAttributeValue("vbdef21", vbdef21);
    }

    public void setVbdef22(String vbdef22) {
        this.setAttributeValue("vbdef22", vbdef22);
    }

    public void setVbdef23(String vbdef23) {
        this.setAttributeValue("vbdef23", vbdef23);
    }

    public void setVbdef24(String vbdef24) {
        this.setAttributeValue("vbdef24", vbdef24);
    }

    public void setVbdef25(String vbdef25) {
        this.setAttributeValue("vbdef25", vbdef25);
    }

    public void setVbdef26(String vbdef26) {
        this.setAttributeValue("vbdef26", vbdef26);
    }

    public void setVbdef27(String vbdef27) {
        this.setAttributeValue("vbdef27", vbdef27);
    }

    public void setVbdef28(String vbdef28) {
        this.setAttributeValue("vbdef28", vbdef28);
    }

    public void setVbdef29(String vbdef29) {
        this.setAttributeValue("vbdef29", vbdef29);
    }

    public void setVbdef30(String vbdef30) {
        this.setAttributeValue("vbdef30", vbdef30);
    }

    public void setVbdef31(String vbdef31) {
        this.setAttributeValue("vbdef31", vbdef31);
    }

    public void setVbdef32(String vbdef32) {
        this.setAttributeValue("vbdef32", vbdef32);
    }

    public void setVbdef33(String vbdef33) {
        this.setAttributeValue("vbdef33", vbdef33);
    }

    public void setVbdef34(String vbdef34) {
        this.setAttributeValue("vbdef34", vbdef34);
    }

    public void setVbdef35(String vbdef35) {
        this.setAttributeValue("vbdef35", vbdef35);
    }

    public void setVbdef36(String vbdef36) {
        this.setAttributeValue("vbdef36", vbdef36);
    }

    public void setVbdef37(String vbdef37) {
        this.setAttributeValue("vbdef37", vbdef37);
    }

    public void setVbdef38(String vbdef38) {
        this.setAttributeValue("vbdef38", vbdef38);
    }

    public void setVbdef39(String vbdef39) {
        this.setAttributeValue("vbdef39", vbdef39);
    }

    public void setVbdef40(String vbdef40) {
        this.setAttributeValue("vbdef40", vbdef40);
    }

    public void setVbdef41(String vbdef41) {
        this.setAttributeValue("vbdef41", vbdef41);
    }

    public void setVbdef42(String vbdef42) {
        this.setAttributeValue("vbdef42", vbdef42);
    }

    public void setVbdef43(String vbdef43) {
        this.setAttributeValue("vbdef43", vbdef43);
    }

    public void setVbdef44(String vbdef44) {
        this.setAttributeValue("vbdef44", vbdef44);
    }

    public void setVbdef45(String vbdef45) {
        this.setAttributeValue("vbdef45", vbdef45);
    }

    public void setVbdef46(String vbdef46) {
        this.setAttributeValue("vbdef46", vbdef46);
    }

    public void setVbdef47(String vbdef47) {
        this.setAttributeValue("vbdef47", vbdef47);
    }

    public void setVbdef48(String vbdef48) {
        this.setAttributeValue("vbdef48", vbdef48);
    }

    public void setVbdef49(String vbdef49) {
        this.setAttributeValue("vbdef49", vbdef49);
    }

    public void setVbdef50(String vbdef50) {
        this.setAttributeValue("vbdef50", vbdef50);
    }

    public void setVbrevisereason(String vbrevisereason) {
        this.setAttributeValue("vbrevisereason", vbrevisereason);
    }

    public void setVchangerate(String vchangerate) {
        this.setAttributeValue("vchangerate", vchangerate);
    }

    public void setVclosereason(String vclosereason) {
        this.setAttributeValue("vclosereason", vclosereason);
    }

    public void setVctcode(String vctcode) {
        this.setAttributeValue("vctcode", vctcode);
    }

    public void setVcttype(String vcttype) {
        this.setAttributeValue("vcttype", vcttype);
    }

    public void setVfirstcode(String vfirstcode) {
        this.setAttributeValue("vfirstcode", vfirstcode);
    }

    public void setVfirstrowno(String vfirstrowno) {
        this.setAttributeValue("vfirstrowno", vfirstrowno);
    }

    public void setVfirsttrantype(String vfirsttrantype) {
        this.setAttributeValue("vfirsttrantype", vfirsttrantype);
    }

    public void setVfirsttype(String vfirsttype) {
        this.setAttributeValue("vfirsttype", vfirsttype);
    }

    public void setVfree1(String vfree1) {
        this.setAttributeValue("vfree1", vfree1);
    }

    public void setVfree10(String vfree10) {
        this.setAttributeValue("vfree10", vfree10);
    }

    public void setVfree2(String vfree2) {
        this.setAttributeValue("vfree2", vfree2);
    }

    public void setVfree3(String vfree3) {
        this.setAttributeValue("vfree3", vfree3);
    }

    public void setVfree4(String vfree4) {
        this.setAttributeValue("vfree4", vfree4);
    }

    public void setVfree5(String vfree5) {
        this.setAttributeValue("vfree5", vfree5);
    }

    public void setVfree6(String vfree6) {
        this.setAttributeValue("vfree6", vfree6);
    }

    public void setVfree7(String vfree7) {
        this.setAttributeValue("vfree7", vfree7);
    }

    public void setVfree8(String vfree8) {
        this.setAttributeValue("vfree8", vfree8);
    }

    public void setVfree9(String vfree9) {
        this.setAttributeValue("vfree9", vfree9);
    }

    public void setVqtunitrate(String vqtunitrate) {
        this.setAttributeValue("vqtunitrate", vqtunitrate);
    }

    public void setVreturnmode(String vreturnmode) {
        this.setAttributeValue("vreturnmode", vreturnmode);
    }

    public void setVrownote(String vrownote) {
        this.setAttributeValue("vrownote", vrownote);
    }

    public void setVsrccode(String vsrccode) {
        this.setAttributeValue("vsrccode", vsrccode);
    }

    public void setVsrcrowno(String vsrcrowno) {
        this.setAttributeValue("vsrcrowno", vsrcrowno);
    }

    public void setVsrctrantype(String vsrctrantype) {
        this.setAttributeValue("vsrctrantype", vsrctrantype);
    }

    public void setVsrctype(String vsrctype) {
        this.setAttributeValue("vsrctype", vsrctype);
    }

    public void setCbuypromottypeid(String cbuypromottypeid) {
        this.setAttributeValue("cbuypromottypeid", cbuypromottypeid);
    }

    public void setCprcpromottypeid(String cprcpromottypeid) {
        this.setAttributeValue("cprcpromottypeid", cprcpromottypeid);
    }

    public void setVcustombillcode(String vcustombillcode) {
        this.setAttributeValue("vcustombillcode", vcustombillcode);
    }

    public void setCbuylargessactid(String cbuylargessactid) {
        this.setAttributeValue("cbuylargessactid", cbuylargessactid);
    }

    public void setCpricepromtactid(String cpricepromtactid) {
        this.setAttributeValue("cpricepromtactid", cpricepromtactid);
    }

    public void setCbuylargessid(String cbuylargessid) {
        this.setAttributeValue("cbuylargessid", cbuylargessid);
    }

    public UFDouble getNarrangeitcnum() {
        return (UFDouble) this.getAttributeValue("narrangeitcnum");
    }

    public void setNarrangeitcnum(UFDouble narrangeitcnum) {
        this.setAttributeValue("narrangeitcnum", narrangeitcnum);
    }

    public void setCsprofitcenterid(String csprofitcenterid) {
        this.setAttributeValue("csprofitcenterid", csprofitcenterid);
    }

    public void setCsprofitcentervid(String csprofitcentervid) {
        this.setAttributeValue("csprofitcentervid", csprofitcentervid);
    }

    public void setCPromotpriceid(String cpromotpriceid) {
        this.setAttributeValue("cpromotpriceid", cpromotpriceid);
    }

    public AggFFileVO getAggffilevo() {
        return this.aggffilevo;
    }

    public void setAggffilevo(AggFFileVO aggffilevo) {
        this.aggffilevo = aggffilevo;
    }

    public String getCmffileid() {
        return (String) this.getAttributeValue("cmffileid");
    }

    public void setCmffileid(String cmffileid) {
        this.setAttributeValue("cmffileid", cmffileid);
    }

    public UFDouble getNmffileprice() {
        return (UFDouble) this.getAttributeValue("nmffileprice");
    }

    public void setNmffileprice(UFDouble nmffileprice) {
        this.setAttributeValue("nmffileprice", nmffileprice);
    }

    public String getCextsrcbilltype() {
        return (String) this.getAttributeValue("cextsrcbilltype");
    }

    public void setCextsrcbilltype(String cextsrcbilltype) {
        this.setAttributeValue("cextsrcbilltype", cextsrcbilltype);
    }

    public String getCextsrcid() {
        return (String) this.getAttributeValue("cextsrcid");
    }

    public void setCextsrcid(String cextrsrcid) {
        this.setAttributeValue("cextsrcid", cextrsrcid);
    }

    public String getCextsrcbid() {
        return (String) this.getAttributeValue("cextsrcbid");
    }

    public void setCextsrcbid(String cextsrcbid) {
        this.setAttributeValue("cextsrcbid", cextsrcbid);
    }

    public String getVextsrcbillcode() {
        return (String) this.getAttributeValue("vextsrcbillcode");
    }

    public void setVextsrcbillcode(String vextsrcbillcode) {
        this.setAttributeValue("vextsrcbillcode", vextsrcbillcode);
    }
}
