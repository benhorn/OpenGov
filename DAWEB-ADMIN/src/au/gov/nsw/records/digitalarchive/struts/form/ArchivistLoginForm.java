package au.gov.nsw.records.digitalarchive.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ArchivistLoginForm extends ActionForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5359684918400111862L;
	private String userName;
	private String password;
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// TODO Auto-generated method stub
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	

}