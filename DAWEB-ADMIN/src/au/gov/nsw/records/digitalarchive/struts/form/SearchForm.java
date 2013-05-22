package au.gov.nsw.records.digitalarchive.struts.form;

import org.apache.struts.action.ActionForm;

public class SearchForm extends ActionForm {
	
	
	private static final long serialVersionUID = 1L;

	private String searchText;
	
	public SearchForm(){};

	public String getSearchText() {
		return this.searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	
	

}
