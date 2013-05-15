package au.gov.nsw.records.digitalarchive.struts.action;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DownloadAction;

import au.gov.nsw.records.digitalarchive.ORM.UploadedFile;
import au.gov.nsw.records.digitalarchive.service.FileService;
import au.gov.nsw.records.digitalarchive.service.FileServiceImpl;


public class FileDownloadAction extends DownloadAction{

	@Override
	protected StreamInfo getStreamInfo(ActionMapping mapping, 
									   ActionForm form,
									   HttpServletRequest request, 
									   HttpServletResponse response)
	{
		String id = request.getParameter("id");
		UploadedFile uploadedFile = new UploadedFile();
		FileService fs = new FileServiceImpl();
		try {
			uploadedFile = fs.loadFile(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			System.out.println("\nNumber format\n");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("\nCatch all\n");
			e.printStackTrace();
		}
				
		String filename=  uploadedFile.getInboxUrl().trim();
		if (!fileExists(filename)) {
			filename = uploadedFile.getPairtreeUrl().trim();
		} 
		
		response.setHeader("Content-disposition","attachment; filename=" + uploadedFile.getFileName().trim().replace(" ", "_"));
		String contentType = uploadedFile.getContentType();
		
		return new FileStreamInfo(contentType, new File(filename));
	}

	
	private boolean fileExists(String filePath)
	{
		File aFile = new File(filePath);
		if (aFile.exists())
			return true;
		else
			return false;
	}
	
}
