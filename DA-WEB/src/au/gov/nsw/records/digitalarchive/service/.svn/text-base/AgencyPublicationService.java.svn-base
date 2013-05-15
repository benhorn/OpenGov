package au.gov.nsw.records.digitalarchive.service;

import java.util.List;

import au.gov.nsw.records.digitalarchive.ORM.AgencyPublication;
import au.gov.nsw.records.digitalarchive.ORM.FullAgencyList;
import au.gov.nsw.records.digitalarchive.ORM.Publication;

public interface AgencyPublicationService {
	
	public boolean addAgencyPublication(AgencyPublication agencyPublication) throws Exception;
	
	public List<AgencyPublication> browseAgencyPublications() throws Exception;
	
	public List<Integer> listAgencyWithPublications() throws Exception;
	
	public List<Publication> loadPublicationViaFullAgency(FullAgencyList fList) throws Exception;
	
	public List<Publication> loadPublicationViaFullAgency(int pageSize, int pageNo, FullAgencyList fList) throws Exception;
	
	public boolean deleteAgencyPubViaPublication(Publication publication) throws Exception;
	
	public AgencyPublication loadAgencyPublication(Integer id) throws Exception;

	public int countRow(String fullAgencyListId) throws Exception;

}
