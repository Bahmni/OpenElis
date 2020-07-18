package us.mn.state.health.lims.typeofteststatus.valueholder;

import java.sql.Timestamp;

import us.mn.state.health.lims.common.valueholder.BaseObject;

public class TypeOfTestStatus extends BaseObject {

	/**
	 * Buvaneswari Arun
	 */
	private static final long serialVersionUID = -7050083545355949587L;

	private String id;
	
	private String statusName;

	private String description;

	private String statusType;
	
	private String isActive;

	private String isResultRequired;
	
	private String isApprovalRequired;
	
	private Timestamp dateCreated;
	
	public TypeOfTestStatus() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatusType() {
		return statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}

	public Timestamp getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIsResultRequired() {
		return isResultRequired;
	}

	public void setIsResultRequired(String isResultRequired) {
		this.isResultRequired = isResultRequired;
	}

	public String getIsApprovalRequired() {
		return isApprovalRequired;
	}

	public void setIsApprovalRequired(String isApprovalRequired) {
		this.isApprovalRequired = isApprovalRequired;
	}
}
