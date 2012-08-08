package au.gov.nsw.records.digitalarchive.service;

import java.util.List;

import au.gov.nsw.records.digitalarchive.ORM.FullAgencyList;
import au.gov.nsw.records.digitalarchive.ORM.PublisherPublication;

public interface PublisherPublicationService {

	public boolean addPublisherPublication(PublisherPublication publisherPublication) throws Exception;
	
	public List<PublisherPublication> browsePublisherPublication() throws Exception;
	
	public boolean deletePublisherViaPublication(PublisherPublication publisherPublication) throws Exception;

	public List<FullAgencyList> loadPublisherViaPublication(String pubID) throws Exception;
	
	public PublisherPublication loadPublisherPublication(Integer id) throws Exception;	

}
