package au.gov.nsw.records.digitalarchive.service;

import java.util.List;

import au.gov.nsw.records.digitalarchive.ORM.Publication;
import au.gov.nsw.records.digitalarchive.ORM.UploadedFile;


public interface FileService {
	
	public boolean addFile (UploadedFile uploadedFile) throws Exception;
	
	public boolean updateFile(UploadedFile uploadedFile) throws Exception;
	
	public List<UploadedFile> browseFiles (Publication publication) throws Exception;
	
	public boolean deleteFile(Integer id) throws Exception;
	
	public boolean deleteFileViaPublication(Publication publication) throws Exception;
	
	public boolean deletePhysicalFiles(UploadedFile inputFile) throws Exception;
	
	public boolean cleanUpInbox(UploadedFile inputFile) throws Exception;
	
	public boolean deletePhysicalFilesViaPub(Publication publication) throws Exception;
	
	public Publication findPubViaFile(Integer id) throws Exception;
	
	public UploadedFile loadFile(Integer id) throws Exception;
	
	public UploadedFile loadFileViaOrder(Integer fileOrder, Integer pubID) throws Exception;
	
	public UploadedFile loadFileViaOrder(Integer fileOrder, Integer pubID, Integer fileID) throws Exception;
	
	public int countFiles(String hql) throws Exception;
	
	public int countTotalPages(String pubID) throws Exception;
	
	public int maxFileOrder(String pubID) throws Exception;
	
	public int minFileOrder(String pubID) throws Exception;	


}