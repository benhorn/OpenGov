package au.gov.nsw.records.digitalarchive.struts.form;

import org.apache.struts.action.ActionForm;

public class SearchForm extends ActionForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6686461358801577808L;
	
	private String query;

	public SearchForm(){};
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
