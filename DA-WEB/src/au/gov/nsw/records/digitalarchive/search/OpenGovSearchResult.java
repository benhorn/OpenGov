package au.gov.nsw.records.digitalarchive.search;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class OpenGovSearchResult {

	private Map<String, String> results;
	private int resultCount;
	private List<FacetResultItem> facets;
	
	private String fileID;
	private String fileName;
	private String agencyName;
	private String title;
	private String keyword;
	private String description;
	private String content;
	private String datePublished;
		
	private SortedMap<String, String> yearToFileID = new TreeMap<String, String>();
	private SortedMap<String, String> extractedParagraph = new TreeMap<String, String>();

	
	public OpenGovSearchResult(Map<String, String> results, List<FacetResultItem> facets, int resultCount) {
		super();
		this.results = results;
		this.facets = facets;
		this.resultCount = resultCount;
	}
	
	public OpenGovSearchResult(String datePublished, String fileID, 
							   String fileName, String agencyName, String title, String keyword,
							   String description, String content, List<FacetResultItem> facets,
							   int resultCount, Map<String, String> results, SortedMap<String, String> yearToFileID,
							   SortedMap<String, String> extractedParagraph) 
	{
		super();
		this.datePublished = datePublished;
		this.fileID = fileID;
		this.fileName = fileName;
		this.agencyName = agencyName;
		this.title = title;
		this.keyword = keyword;
		this.description = description;
		this.content = content;
		this.facets = facets;
		this.resultCount = resultCount;
		this.results = results;
		this.yearToFileID = yearToFileID;
		this.extractedParagraph = extractedParagraph;
	}

	public int getResultCount() {
		return resultCount;
	}
	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}
	
	public Map<String, String> getResults() {
		return results;
	}
	public void setResults(Map<String, String> results) {
		this.results = results;
	}
	public List<FacetResultItem> getFacets() {
		return facets;
	}
	public void setFacets(List<FacetResultItem> facets) {
		this.facets = facets;
	}

	public String getDatePublished() {
		return datePublished;
	}

	public void setDatePublished(String datePublished) {
		this.datePublished = datePublished;
	}

	public String getFileID() {
		return fileID;
	}

	public void setFileID(String fileID) {
		this.fileID = fileID;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public SortedMap<String, String> getYearToFileID() {
		return yearToFileID;
	}

	public void setYearToFileID(SortedMap<String, String> yearToFileID) {
		this.yearToFileID = yearToFileID;
	}

	public SortedMap<String, String> getExtractedParagraph() {
		return extractedParagraph;
	}

	public void setExtractedParagraph(SortedMap<String, String> extractedParagraph) {
		this.extractedParagraph = extractedParagraph;
	}

	
}
