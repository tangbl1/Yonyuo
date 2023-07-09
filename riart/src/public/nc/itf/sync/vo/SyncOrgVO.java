package nc.itf.sync.vo;

public class SyncOrgVO {
	//组织编码
	private String OrgCode;
	//组织名称
	private String OrgName;
	//上级组织编号
	private String ParentCode;
	//组织简称
	private String OrgAbbr;
	//父组织ID列表
	private String OrgPIDS;	
	//排序
	private String IORDER;	
	//状态（1启用，0不启用）
	private String StatusFlag;	
	//最后修改时间
	private String TS;
	
	public String getOrgCode() {
		return OrgCode;
	}
	public void setOrgCode(String orgCode) {
		OrgCode = orgCode;
	}
	public String getOrgName() {
		return OrgName;
	}
	public void setOrgName(String orgName) {
		OrgName = orgName;
	}
	public String getParentCode() {
		return ParentCode;
	}
	public void setParentCode(String parentCode) {
		ParentCode = parentCode;
	}
	public String getOrgAbbr() {
		return OrgAbbr;
	}
	public void setOrgAbbr(String orgAbbr) {
		OrgAbbr = orgAbbr;
	}
	public String getOrgPIDS() {
		return OrgPIDS;
	}
	public void setOrgPIDS(String orgPIDS) {
		OrgPIDS = orgPIDS;
	}
	public String getIORDER() {
		return IORDER;
	}
	public void setIORDER(String iORDER) {
		IORDER = iORDER;
	}
	public String getStatusFlag() {
		return StatusFlag;
	}
	public void setStatusFlag(String statusFlag) {
		StatusFlag = statusFlag;
	}
	public String getTS() {
		return TS;
	}
	public void setTS(String tS) {
		TS = tS;
	}		
	

	
	
	
}
