package au.gov.nsw.records.digitalarchive.service;

import au.gov.nsw.records.digitalarchive.ORM.*;

import java.util.*;

public interface ArchivistService {
	
	public boolean addArchivist(Archivist archivist) throws Exception;
	
	public Archivist archivistLogin(String login,String password) throws Exception;
		
	public boolean updateArchivist (Archivist archivist) throws Exception;
	
	public List<Archivist> browseArchivist() throws Exception;
	
	public boolean delArchivist(Integer id) throws Exception;
	
	public Archivist loadArchivist(Integer id) throws Exception;	
}