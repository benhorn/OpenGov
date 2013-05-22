package au.gov.nsw.records.digitalarchive.service;

import java.util.List;

import au.gov.nsw.records.digitalarchive.ORM.KeywordPublication;
import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.ORM.Publication;

public interface KeywordPublicationService {
	
	public boolean addKeywordPublication(KeywordPublication keyPub) throws Exception;
	
	public boolean updateKeywordPublicaion (KeywordPublication keyPub) throws Exception;
	
	public List<KeywordPublication> browseKeywordPublications() throws Exception;
	
	public boolean deleteKeyPubViaPublication(Publication publication) throws Exception;
	
	public KeywordPublication loadKeywordPublication(Integer id) throws Exception;	


}
