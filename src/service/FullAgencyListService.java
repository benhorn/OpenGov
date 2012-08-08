package au.gov.nsw.records.digitalarchive.service;

import java.util.List;

import au.gov.nsw.records.digitalarchive.ORM.AgencyPublication;
import au.gov.nsw.records.digitalarchive.ORM.FullAgencyList;

public interface FullAgencyListService {

	public boolean addFullAgencyList(FullAgencyList fullAgencyList) throws Exception;
	
	public boolean updateFullAgencyList(FullAgencyList fullAgencyList) throws Exception;
	
	public List<FullAgencyList> browseFullAgencyList () throws Exception;
		
	public boolean deleteFullAgencyList(Integer id) throws Exception;
	
	public List<AgencyPublication> loadAgencyWithPublication() throws Exception;
	
	public List<FullAgencyList> loadAgencyViaPublication(String pubID) throws Exception;
	
	public FullAgencyList loadFullAgencyList(Integer id) throws Exception;
	
	public List<Integer> browseAgencyID(String agencyName) throws Exception;
	
	public boolean chkAgencyName(String agencyName) throws Exception;	
	
	public List<FullAgencyList> list4AutoComplete(String agencyName) throws Exception;

}
