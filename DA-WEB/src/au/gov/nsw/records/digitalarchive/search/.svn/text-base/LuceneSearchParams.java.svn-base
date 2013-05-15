package au.gov.nsw.records.digitalarchive.search;

import org.apache.lucene.facet.search.params.FacetSearchParams;

public class LuceneSearchParams {

	private FacetSearchParams facetParams;
	private String query;
	private String agency;
	private String type;
	private String publicationYearStart;
	private String publicationYearEnd;
	private int pageSize;
	private int pageNo;
	
	public LuceneSearchParams(){}
	
	public LuceneSearchParams(String query, 
							  FacetSearchParams facetParams,
							  String agency, 
							  String type,
							  String publicationYearStart,
							  String publicationYearEnd,
							  int pageNo, 
							  int pageSize) 
	{
		super();
		this.query = query;
		this.facetParams = facetParams;
		this.agency = agency;
		this.type = type;
		this.publicationYearStart = publicationYearStart;
		this.publicationYearEnd = publicationYearEnd;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public FacetSearchParams getFacetParams() {
		return facetParams;
	}
	public void setFacetParams(FacetSearchParams facetParams) {
		this.facetParams = facetParams;
	}
	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPublicationYearStart() {
		return this.publicationYearStart;
	}

	public void setPublicationYearStart(String publicationYearStart) {
		this.publicationYearStart = publicationYearStart;
	}

	public String getPublicationYearEnd() {
		return publicationYearEnd;
	}

	public void setPublicationYearEnd(String publicationYearEnd) {
		this.publicationYearEnd = publicationYearEnd;
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	
	
}
