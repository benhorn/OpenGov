package au.gov.nsw.records.digitalarchive.service;

import java.util.List;

import au.gov.nsw.records.digitalarchive.ORM.*;

public interface BOSListService {
	
	public boolean addNewBosList(BosList bosList) throws Exception;
	
	public boolean updateBosList(BosList bosList) throws Exception;

	public List<BosList> browseBosList() throws Exception;
		
	public BosList loadBosList(Integer id) throws Exception;
	
}
