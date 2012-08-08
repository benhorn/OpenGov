package au.gov.nsw.records.digitalarchive.service;

import java.util.List;

import au.gov.nsw.records.digitalarchive.ORM.Agency;
import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.ORM.Publication;

public interface PublicationService {

	public boolean addPublication(Publication publication) throws Exception;
	
	public boolean updatePublication(Publication publication) throws Exception;

	public List<Publication> browsePublication() throws Exception;
	
	public List<Publication> browsePublication(Member member) throws Exception;

	public List<Publication> browsePublication(Member member, Agency agency) throws Exception;

	public boolean delPublication(Integer id) throws Exception;
	
	public Publication loadPublication(Integer id) throws Exception;
}
