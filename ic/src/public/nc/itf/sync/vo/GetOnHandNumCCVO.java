package nc.itf.sync.vo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @ProjectName:    NC65_DD 
 * @Package:        nc.itf.sync.vo
 * @ClassName:      GetOnHandNumCCVO
 * @Author:     tbl
 * @Description:    
 * @Date:    2022/10/9 8:44
 * @Version:    1.0
 */
public class GetOnHandNumCCVO {
    //库存组织
    private String OrgCode;
    //仓库
    private String StorCode;
    //物料编码
    private String MaterialCode;
    //物料名称
    private String MaterialName;
    //货位编码
    private String  ClocationCode;
    //规格
    private String MaterialModel;
    //型号
    private String Specification ;
    //单位编码
    private String UnitCode;
    //结存数量
//    @JSONField(name = "NOnhandNum")//问题记录：json首字母小写自动转化， @JSONField可避免这个问题，目前没修正这个问题
    private String NOnhandNum;

    public String getOrgCode() {
        return OrgCode;
    }

    public void setOrgCode(String orgCode) {
        OrgCode = orgCode;
    }

    public String getStorCode() {
        return StorCode;
    }

    public void setStorCode(String storCode) {
        StorCode = storCode;
    }

    public String getMaterialCode() {
        return MaterialCode;
    }

    public void setMaterialCode(String materialCode) {
        MaterialCode = materialCode;
    }

    public String getMaterialName() {
        return MaterialName;
    }

    public void setMaterialName(String materialName) {
        MaterialName = materialName;
    }

    public String getClocationCode() {
        return ClocationCode;
    }

    public void setClocationCode(String clocationCode) {
        ClocationCode = clocationCode;
    }

    public String getMaterialModel() {
        return MaterialModel;
    }

    public void setMaterialModel(String materialModel) {
        MaterialModel = materialModel;
    }

    public String getSpecification() {
        return Specification;
    }

    public void setSpecification(String specification) {
        Specification = specification;
    }

    public String getUnitCode() {
        return UnitCode;
    }

    public void setUnitCode(String unitCode) {
        UnitCode = unitCode;
    }

    public String getNOnhandNum() {
        return NOnhandNum;
    }

    public void setNOnhandNum(String NOnhandNum) {
        this.NOnhandNum = NOnhandNum;
    }
}