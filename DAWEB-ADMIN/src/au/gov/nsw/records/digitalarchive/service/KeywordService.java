package au.gov.nsw.records.digitalarchive.service;

import java.util.List;

import au.gov.nsw.records.digitalarchive.ORM.Keyword;

public interface KeywordService {
	
	public boolean addKeyword(Keyword keyword) throws Exception;
		
	public List<Keyword> browseKeywords() throws Exception;

	public Keyword loadKeyword(Integer id) throws Exception;

	public List<Keyword> loadKeywordViaPublication(String pubID) throws Exception;
	
	public List<Integer> browseKeywordID(String keyword) throws Exception; 

	public boolean chkKeyword(String keywordName) throws Exception;	

}
