package au.gov.nsw.records.digitalarchive.struts.form;

import org.apache.struts.validator.ValidatorForm;

public class MemberForm extends ValidatorForm {


	private static final long serialVersionUID = 1L;
	
	private String firstName;
	private String lastName;
	private String email;
	private String agencyNumber;
	private String unregisteredAgencyName;
	private String phone;
	private String loginName;
	private String loginPwd;
	private String activated;
	
	/**flagging variables**/
	private String agency_register_method;
	private String agency_name_text;
	
	public MemberForm(){};
	
	public String getFirstName() {
		return this.firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return this.lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAgencyNumber() {
		return this.agencyNumber;
	}
	public void setAgencyNumber(String agencyNumber) {
		this.agencyNumber = agencyNumber;
	}
	public String getUnregisteredAgencyName() {
		return this.unregisteredAgencyName;
	}

	public void setUnregisteredAgencyName(String unregisteredAgencyName) {
		this.unregisteredAgencyName = unregisteredAgencyName;
	}

	public String getPhone() {
		return this.phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLoginName() {
		return this.loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPwd() {
		return this.loginPwd;
	}
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}
	public String getActivated() {
		return this.activated;
	}
	public void setActivated(String activated) {
		this.activated = activated;
	}
	
	public String getAgency_register_method() {
		return this.agency_register_method;
	}

	public void setAgency_register_method(String agency_register_method) {
		this.agency_register_method = agency_register_method;
	}

	public String getAgency_name_text() {
		return this.agency_name_text;
	}

	public void setAgency_name_text(String agency_name_text) {
		this.agency_name_text = agency_name_text;
	}

}