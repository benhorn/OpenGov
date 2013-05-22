package au.gov.nsw.records.digitalarchive.service;

import java.util.List;

import au.gov.nsw.records.digitalarchive.ORM.*;

public interface TempListService {
	
	public boolean addTempList(TempList tempList) throws Exception;
		
	public boolean updateTempList (TempList tempList) throws Exception;
	
	public List browseTempList() throws Exception;
	
	public boolean delTempList(Integer id) throws Exception;
	
	public TempList loadTempList(Integer id) throws Exception;	

}
