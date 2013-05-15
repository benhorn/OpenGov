package au.gov.nsw.records.digitalarchive.system;

import gov.loc.repository.pairtree.Pairtree;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import au.gov.nsw.records.digitalarchive.ORM.FullAgencyList;
import au.gov.nsw.records.digitalarchive.ORM.Publication;
import au.gov.nsw.records.digitalarchive.ORM.UploadedFile;
import au.gov.nsw.records.digitalarchive.base.Constants;
import au.gov.nsw.records.digitalarchive.base.NYTimesJSONFormat;
import au.gov.nsw.records.digitalarchive.search.TikaWrapper;
import au.gov.nsw.records.digitalarchive.service.FileService;
import au.gov.nsw.records.digitalarchive.service.FileServiceImpl;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListService;
import au.gov.nsw.records.digitalarchive.service.FullAgencyListServiceImpl;
import au.gov.nsw.records.digitalarchive.service.PublicationService;
import au.gov.nsw.records.digitalarchive.service.PublicationServiceImpl;

public class DBUpdater {
	
	public boolean updateDB(int pubNo)
	{
		System.out.println("\n\n\nID:" + pubNo);
		
		try
		{
				FileService fs = new FileServiceImpl();
				PublicationService ps = new PublicationServiceImpl();
				FullAgencyListService fal = new FullAgencyListServiceImpl();
				
				Publication publication = ps.loadPublication(pubNo);
				List<UploadedFile> uList = fs.browseFiles(publication);
				Iterator<UploadedFile> UploadedFileIterator = uList.iterator();
				while (UploadedFileIterator.hasNext())
				{
						UploadedFile uFile = (UploadedFile)UploadedFileIterator.next();
						List<FullAgencyList> faList = fal.loadAgencyViaPublication(Integer.toString(pubNo));
						List<String> agencyNameList = new ArrayList<String>();				
						Iterator<FullAgencyList> aIterator = faList.iterator(); 
		  	  	 		while(aIterator.hasNext())
		  	  	 		{
		  	  	 			FullAgencyList fl = (FullAgencyList)aIterator.next();
		  	  	 			agencyNameList.add(fl.getAgencyName());
		  	  	 		}
							
		  	  	 		Pairtree pt = new Pairtree();
		  	  	 		String pairTreeURL = Constants.PAIRTREE_ROOT + pt.mapToPPath(uFile.getUid()) + File.separator + "obj" + File.separator;
		  	  	 		
		  	  	 		TikaWrapper ta = new TikaWrapper();
		  	  	 		Integer pageNumber = Integer.parseInt(ta.getPageNumber(pairTreeURL + "document.pdf"));
		  	  	 		uFile.setPages(pageNumber);
		  	  	 		System.out.println("Original JSON " + uFile.getReaderJson());
				 		String hostURL = "https://www.opengov.nsw.gov.au/";
		  	  	 		NYTimesJSONFormat NYTimesJSON = new NYTimesJSONFormat(uFile.getFileName().substring(0, uFile.getFileName().length()-4),
												  uFile.getFileName().substring(0, uFile.getFileName().length()-4),
												  pageNumber.toString(),
												  "",
												  hostURL.concat("doc_opener?uid=").concat(uFile.getUid()),
												  hostURL.concat("publication/").concat(uFile.getFileId().toString()),
												  hostURL.concat("txt_opener?uid=").concat(uFile.getUid()),
												  hostURL.concat("thumb?uid=").concat(uFile.getUid()),
	  											  hostURL.concat("doc_search?q={query}&uid=").concat(uFile.getUid()),
												  hostURL.concat("txt_opener?uid=").concat(uFile.getUid())+"&counter={page}",
												  hostURL.concat("pdf_image_opener?uid=").concat(uFile.getUid())+"&counter={page}");
		  	  	 		uFile.setReaderJson(NYTimesJSON.toJSONString());
		  	  	 		fs.updateFile(uFile);
		  	  	 		System.out.println(pubNo + " New JSON " + uFile.getReaderJson());
				}
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
		return true;
	}
	
	
	public static void main(String[] args)
	{
		DBUpdater db_u = new DBUpdater();
		//for (int i = 9681; i < 11417; i++)
			//{db_u.updateDB(i);}
				db_u.updateDB(9681);
	}

}
