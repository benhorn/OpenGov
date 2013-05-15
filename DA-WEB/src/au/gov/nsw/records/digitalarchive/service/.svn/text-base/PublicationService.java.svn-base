package au.gov.nsw.records.digitalarchive.service;

import java.util.List;

import au.gov.nsw.records.digitalarchive.ORM.Agency;
import au.gov.nsw.records.digitalarchive.ORM.Member;
import au.gov.nsw.records.digitalarchive.ORM.Publication;
import au.gov.nsw.records.digitalarchive.ORM.UploadedFile;

public interface PublicationService {

	public Publication loadPublication(Integer id) throws Exception;
	
	public boolean addPublication(Publication publication) throws Exception;
	
	public boolean updatePublication(Publication publication) throws Exception;

	public List<Publication> browsePublication() throws Exception;
	
	public List<Publication> lastestPublication() throws Exception;
	
	public List<Publication> mostPopularPublication() throws Exception;
	
	public List<Publication> browsePublication(Member member) throws Exception;

	public List<Publication> browsePublication(Member member, Agency agency) throws Exception;

	public int countTotalPages(Publication publication) throws Exception;
	
	public boolean delPublication(Integer id) throws Exception;
	
	public List<UploadedFile> listFilesViaPublication(Publication pub) throws Exception;
	
}
