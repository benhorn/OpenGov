package au.gov.nsw.records.digitalarchive.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

public class MemberLoginForm extends ValidatorForm {
	

	/** loginPwd property */
	private String loginPwd;

	/** loginName property */
	private String loginName;

	/*
	 * Generated Methods
	 */

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		
		ActionErrors errors = new ActionErrors();
        if (("test").equals(loginName)) {
            errors.add("loginName", new ActionMessage("errors.required"));
        }
        return errors;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// TODO Auto-generated method stub
	}

	/** 
	 * Returns the loginPwd.
	 * @return String
	 */
	public String getLoginPwd() {
		return loginPwd;
	}

	/** 
	 * Set the loginPwd.
	 * @param loginPwd The loginPwd to set
	 */
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	/** 
	 * Returns the loginName.
	 * @return String
	 */
	public String getLoginName() {
		return loginName;
	}

	/** 
	 * Set the loginName.
	 * @param loginName The loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}