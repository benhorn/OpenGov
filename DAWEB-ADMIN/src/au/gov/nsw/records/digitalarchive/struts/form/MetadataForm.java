package au.gov.nsw.records.digitalarchive.struts.form;

import org.apache.struts.action.ActionForm;

public class MetadataForm extends ActionForm{
	
	String title;
	String description;
	String keywords;
	String presentationCategory;
	String agency;
	String governmentFunction;
	String datePublished;
	String type;
	String identifier;
	String coverage;
	String language;
	String publisher;
	String source;
	String contributor;
	String rights;
	String relation;
	String fileSize;
	String format;
	String pages;
	String registeredByAgency;
	String registeredByWhom;
	String dateRegistered;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getPresentationCategory() {
		return presentationCategory;
	}
	public void setPresentationCategory(String presentationCategory) {
		this.presentationCategory = presentationCategory;
	}
	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	public String getGovernmentFunction() {
		return governmentFunction;
	}
	public void setGovernmentFunction(String governmentFunction) {
		this.governmentFunction = governmentFunction;
	}
	public String getDatePublished() {
		return datePublished;
	}
	public void setDatePublished(String datePublished) {
		this.datePublished = datePublished;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getCoverage() {
		return coverage;
	}
	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getContributor() {
		return contributor;
	}
	public void setContributor(String contributor) {
		this.contributor = contributor;
	}
	public String getRights() {
		return rights;
	}
	public void setRights(String rights) {
		this.rights = rights;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getFormat() {
		return this.format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getPages() {
		return pages;
	}
	public void setPages(String pages) {
		this.pages = pages;
	}
	public String getRegisteredByAgency() {
		return registeredByAgency;
	}
	public void setRegisteredByAgency(String registeredByAgency) {
		this.registeredByAgency = registeredByAgency;
	}
	public String getRegisteredByWhom() {
		return registeredByWhom;
	}
	public void setRegisteredByWhom(String registeredByWhom) {
		this.registeredByWhom = registeredByWhom;
	}
	public String getDateRegistered() {
		return dateRegistered;
	}
	public void setDateRegistered(String dateRegistered) {
		this.dateRegistered = dateRegistered;
	}
	
}
